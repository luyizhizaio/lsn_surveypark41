package cn.itcast.surveypark.struts.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

import cn.itcast.surveypark.datasource.MyThreadLocal;
import cn.itcast.surveypark.domain.Answer;
import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.service.SurveyService;
import cn.itcast.surveypark.util.StringUtil;

/**
 * EngageSureyAction
 */
@Controller
@Scope("prototype")
public class EngageSurveyAction extends BaseAction<Survey> implements SessionAware,ParameterAware {

	private static final long serialVersionUID = 5653820953117495609L;
	
	//当前调查
	private static final String CURRENT_SURVEY = "current_survey" ;
	
	//所有参数的集合,用于回显
	private static final String ALL_PARAMS_MAP = "all_params_map" ;

	//
	private List<Survey> allSurveys ;
	
	//注入的service缓存代理模块
	@Resource(name="surveyServiceCacheProxy")
	private SurveyService surveyService ;
	
	private Integer sid ;
	
	//当前页面
	private Page currPage ;
	
	//当前页面id
	private Integer currPid ;

	public Integer getCurrPid() {
		return currPid;
	}
	public void setCurrPid(Integer currPid) {
		this.currPid = currPid;
	}

	//接收session map
	private Map<String, Object> sessionMap;

	//接收参数map
	private Map<String, String[]> paramsMap;
	
	public Page getCurrPage() {
		return currPage;
	}
	public void setCurrPage(Page currPage) {
		this.currPage = currPage;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public List<Survey> getAllSurveys() {
		return allSurveys;
	}
	public void setAllSurveys(List<Survey> allSurveys) {
		this.allSurveys = allSurveys;
	}
	
	/**
	 * 查询所有可用的调查
	 */
	public String findAllAvailableSurveys(){
		ActionContext ac = ActionContext.getContext();
		Map<String, Object> map = ac.getContextMap();
		for(Entry<String,Object> entry:map.entrySet()){
			System.out.println(entry.getKey() + "=====" + entry.getValue());
		}
		this.allSurveys = surveyService.findAllAvailableSurveys();
		return "engageSurveyListPage" ;
	}
	
	/**
	 * 首次参与调查
	 */
	public String entry(){
		this.currPage = surveyService.getFirstPage(sid);
		Survey s = currPage.getSurvey();
		//当前调查
		sessionMap.put(CURRENT_SURVEY, s);
		//初始化所有参数集合
		sessionMap.put(ALL_PARAMS_MAP, new HashMap<Integer,Map<String,String[]>>());
		return "engageSurveyPage" ;
	}
	
	/**
	 * 参与调查
	 */
	public String doEngageSurvey(){
		String submitName = getSubmitName();
		//上一步
		if(submitName.endsWith("pre")){
			mergeParamsToSession();
			this.currPage = surveyService.getPrePage(currPid);
			return "engageSurveyPage" ;
		}
		//下一步
		else if(submitName.endsWith("next")){
			mergeParamsToSession();
			this.currPage = surveyService.getNextPage(currPid);
			return "engageSurveyPage" ;
		}
		//取消
		else if(submitName.endsWith("exit")){
			//清除session数据
			clearSessionData();
			return "findAllAvailableSurveyAction" ;
		}
		//完成
		else if(submitName.endsWith("done")){
			mergeParamsToSession();
			//创建Threaddlocal对象,绑定到当前线程
			MyThreadLocal mtl = new MyThreadLocal();
			mtl.setNanoTime(System.nanoTime());
			MyThreadLocal.setCurrentObject(mtl);
			//答案入库
			surveyService.saveAnswers(processAnswers());
			clearSessionData();
			return "findAllAvailableSurveyAction" ;
		}
		return null;
	}
	
	/**
	 * 处理所有答案
	 */
	private List<Answer> processAnswers() {
		//所有答案集合
		List<Answer> answers = new ArrayList<Answer>();
		//矩阵式单选按钮
		Map<Integer,String> matrixRadioMap = new HashMap<Integer,String>();
		Answer a = null ;
		String key = null ;
		String[] values = null ;
		for(Map<String,String[]> map : getAllParamsMapInSession().values()){
			for(Entry<String, String[]> entry : map.entrySet()){
				key = entry.getKey();
				values = entry.getValue();
				//q开头的参数
				if(key.startsWith("q")){
					//不含other,不含_
					if(!key.contains("other") && !key.contains("_")){
						a  = new Answer();
						//setQid
						a.setQuestionId(getQid(key));
						//surveyid
						a.setSurveyId(getCurrentSuveyId());
						//answerIds
						a.setAnswerIds(StringUtil.arr2Str(values));
						//otherAnswer
						a.setOtherAnswer(StringUtil.arr2Str(map.get(key + "other")));
						//
						answers.add(a);
					}
					//矩阵式单选按钮
					else if(key.contains("_")){
						Integer qid = getMatrixRadioQid(key);
						String value = matrixRadioMap.get(qid);
						//
						if(value == null){
							matrixRadioMap.put(qid, values[0]);
						}
						else{
							matrixRadioMap.put(qid, value + "," + values[0]);
						}
					}
				}
			}
		}
		processMatrixRadioAnswers(matrixRadioMap,answers);
		return answers ;
	}
	
	/**
	 * 处理矩阵式radio
	 */
	private void processMatrixRadioAnswers(Map<Integer,String> matrixRadioMap,List<Answer> answers) {
		Answer a = null ;
		Integer key = null ;
		String value = null ;
		for(Entry<Integer, String> entry:matrixRadioMap.entrySet()){
			key = entry.getKey();
			value = entry.getValue();
			a = new Answer();
			a.setQuestionId(key);
			a.setSurveyId(getCurrentSuveyId());
			a.setAnswerIds(value);
			answers.add(a);
		}
	}
	/**
	 * 提取矩阵式radio问题:q7_0-->7
	 */
	private Integer getMatrixRadioQid(String key) {
		return Integer.parseInt(key.substring(1, key.indexOf("_")));
	}
	/**
	 * 取得当前的session中的调查id
	 */
	private Integer getCurrentSuveyId() {
		return ((Survey)sessionMap.get(CURRENT_SURVEY)).getId();
	}
	/**
	 * 提取问题的id,q12-->12
	 */
	private Integer getQid(String key) {
		return Integer.parseInt(key.substring(1));
	}
	/**
	 * 合并参数到session中
	 */
	private void mergeParamsToSession() {
		Map<Integer, Map<String,String[]>> allParamsMap = getAllParamsMapInSession();
		allParamsMap.put(currPid, paramsMap);
	}
	
	/**
	 * 取得session中所有参数的map
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, Map<String, String[]>> getAllParamsMapInSession() {
		return (Map<Integer, Map<String, String[]>>) sessionMap.get(ALL_PARAMS_MAP);
	}
	
	/**
	 * 清除session数据
	 */
	private void clearSessionData() {
		//1.current_survey
		sessionMap.remove(CURRENT_SURVEY);
		//2.paramsMap
		sessionMap.remove(ALL_PARAMS_MAP);
	}
	
	/**
	 * 得到提交按钮名称
	 */
	private String getSubmitName() {
		//迭代所有参数名
		for(String key : paramsMap.keySet()){
			if(key.startsWith("submit_")){
				return key ;
			}
		}
		return null;
	}
	/**
	 * 注入session map 
	 */
	public void setSession(Map<String, Object> session) {
		this.sessionMap = session ;
	}

	/**
	 * 注入所有的参数
	 */
	public void setParameters(Map<String, String[]> parameters) {
		this.paramsMap = parameters ;
	}
	
	/**
	 * 设置选中标签
	 */
	public String setTag(String qid,String index,String tag){
		String key = null ;
		String[] values = null ;
		for(Entry<String, String[]> entry : getAllParamsMapInSession().get(currPage.getId()).entrySet()){
			key = entry.getKey();
			values = entry.getValue();
			//
			if(key.equals(qid)){
				if(StringUtil.contains(values, index)){
					return tag ;
				}
			}
		}
		return "" ;
	}
	/**
	 * 设置文本框选中标签
	 */
	public String setText(String qid){
		String key = null ;
		String[] values = null ;
		for(Map<String,String[]> map : getAllParamsMapInSession().values()){
			for(Entry<String, String[]> entry : map.entrySet()){
				key = entry.getKey();
				values = entry.getValue();
				//
				if(key.equals(qid)){
					return "value='" + values[0]+ "'" ;
				}
			}
		}
		return "" ;
	}
}
