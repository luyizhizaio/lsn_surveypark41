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
	
	//��ǰ����
	private static final String CURRENT_SURVEY = "current_survey" ;
	
	//���в����ļ���,���ڻ���
	private static final String ALL_PARAMS_MAP = "all_params_map" ;

	//
	private List<Survey> allSurveys ;
	
	//ע���service�������ģ��
	@Resource(name="surveyServiceCacheProxy")
	private SurveyService surveyService ;
	
	private Integer sid ;
	
	//��ǰҳ��
	private Page currPage ;
	
	//��ǰҳ��id
	private Integer currPid ;

	public Integer getCurrPid() {
		return currPid;
	}
	public void setCurrPid(Integer currPid) {
		this.currPid = currPid;
	}

	//����session map
	private Map<String, Object> sessionMap;

	//���ղ���map
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
	 * ��ѯ���п��õĵ���
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
	 * �״β������
	 */
	public String entry(){
		this.currPage = surveyService.getFirstPage(sid);
		Survey s = currPage.getSurvey();
		//��ǰ����
		sessionMap.put(CURRENT_SURVEY, s);
		//��ʼ�����в�������
		sessionMap.put(ALL_PARAMS_MAP, new HashMap<Integer,Map<String,String[]>>());
		return "engageSurveyPage" ;
	}
	
	/**
	 * �������
	 */
	public String doEngageSurvey(){
		String submitName = getSubmitName();
		//��һ��
		if(submitName.endsWith("pre")){
			mergeParamsToSession();
			this.currPage = surveyService.getPrePage(currPid);
			return "engageSurveyPage" ;
		}
		//��һ��
		else if(submitName.endsWith("next")){
			mergeParamsToSession();
			this.currPage = surveyService.getNextPage(currPid);
			return "engageSurveyPage" ;
		}
		//ȡ��
		else if(submitName.endsWith("exit")){
			//���session����
			clearSessionData();
			return "findAllAvailableSurveyAction" ;
		}
		//���
		else if(submitName.endsWith("done")){
			mergeParamsToSession();
			//����Threaddlocal����,�󶨵���ǰ�߳�
			MyThreadLocal mtl = new MyThreadLocal();
			mtl.setNanoTime(System.nanoTime());
			MyThreadLocal.setCurrentObject(mtl);
			//�����
			surveyService.saveAnswers(processAnswers());
			clearSessionData();
			return "findAllAvailableSurveyAction" ;
		}
		return null;
	}
	
	/**
	 * �������д�
	 */
	private List<Answer> processAnswers() {
		//���д𰸼���
		List<Answer> answers = new ArrayList<Answer>();
		//����ʽ��ѡ��ť
		Map<Integer,String> matrixRadioMap = new HashMap<Integer,String>();
		Answer a = null ;
		String key = null ;
		String[] values = null ;
		for(Map<String,String[]> map : getAllParamsMapInSession().values()){
			for(Entry<String, String[]> entry : map.entrySet()){
				key = entry.getKey();
				values = entry.getValue();
				//q��ͷ�Ĳ���
				if(key.startsWith("q")){
					//����other,����_
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
					//����ʽ��ѡ��ť
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
	 * �������ʽradio
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
	 * ��ȡ����ʽradio����:q7_0-->7
	 */
	private Integer getMatrixRadioQid(String key) {
		return Integer.parseInt(key.substring(1, key.indexOf("_")));
	}
	/**
	 * ȡ�õ�ǰ��session�еĵ���id
	 */
	private Integer getCurrentSuveyId() {
		return ((Survey)sessionMap.get(CURRENT_SURVEY)).getId();
	}
	/**
	 * ��ȡ�����id,q12-->12
	 */
	private Integer getQid(String key) {
		return Integer.parseInt(key.substring(1));
	}
	/**
	 * �ϲ�������session��
	 */
	private void mergeParamsToSession() {
		Map<Integer, Map<String,String[]>> allParamsMap = getAllParamsMapInSession();
		allParamsMap.put(currPid, paramsMap);
	}
	
	/**
	 * ȡ��session�����в�����map
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, Map<String, String[]>> getAllParamsMapInSession() {
		return (Map<Integer, Map<String, String[]>>) sessionMap.get(ALL_PARAMS_MAP);
	}
	
	/**
	 * ���session����
	 */
	private void clearSessionData() {
		//1.current_survey
		sessionMap.remove(CURRENT_SURVEY);
		//2.paramsMap
		sessionMap.remove(ALL_PARAMS_MAP);
	}
	
	/**
	 * �õ��ύ��ť����
	 */
	private String getSubmitName() {
		//�������в�����
		for(String key : paramsMap.keySet()){
			if(key.startsWith("submit_")){
				return key ;
			}
		}
		return null;
	}
	/**
	 * ע��session map 
	 */
	public void setSession(Map<String, Object> session) {
		this.sessionMap = session ;
	}

	/**
	 * ע�����еĲ���
	 */
	public void setParameters(Map<String, String[]> parameters) {
		this.paramsMap = parameters ;
	}
	
	/**
	 * ����ѡ�б�ǩ
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
	 * �����ı���ѡ�б�ǩ
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
