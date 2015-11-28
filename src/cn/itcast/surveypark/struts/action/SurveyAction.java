package cn.itcast.surveypark.struts.action;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.SurveyService;
import cn.itcast.surveypark.struts.UserAware;
import cn.itcast.surveypark.util.ValidateUtil;

/**
 * 调查action
 */
@Controller
@Scope("prototype")
public class SurveyAction extends BaseAction<Survey> implements UserAware,ServletContextAware {

	private static final long serialVersionUID = 4640764824027522277L;
	
	@Resource
	private SurveyService surveyService ;
	
	//mySurveys
	private List<Survey> mySurveys ;
	
	//接收sid
	private Integer sid ;
	

	//接收session中user对象
	private User user;
	
	//实现上传
	private File logoPhoto ;
	
	//input错误页
	private String inputPage ; 
	
	public String getInputPage() {
		return inputPage;
	}

	public void setInputPage(String inputPage) {
		this.inputPage = inputPage;
	}

	public File getLogoPhoto() {
		return logoPhoto;
	}

	public void setLogoPhoto(File logoPhoto) {
		this.logoPhoto = logoPhoto;
	}

	public String getLogoPhotoFileName() {
		return logoPhotoFileName;
	}

	public void setLogoPhotoFileName(String logoPhotoFileName) {
		this.logoPhotoFileName = logoPhotoFileName;
	}

	private String logoPhotoFileName ;

	//接收ServletContext对象
	private ServletContext sc;
	
	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public List<Survey> getMySurveys() {
		return mySurveys;
	}

	public void setMySurveys(List<Survey> mySurveys) {
		this.mySurveys = mySurveys;
	}

	/**
	 * 新建调查
	 */
	public String newSurvey(){
		return "designSurveyPage" ;
	}

	/**
	 * 
	 */
	public void prepareNewSurvey(){
		this.model = surveyService.newSurvey(user);
	}
	
	/*
	 * 我的调查
	 */
	public String mySurveys(){
		this.mySurveys = surveyService.findMySurveys(user.getId());
		return "mySurveyListPage" ;
	}
	
	/**
	 * 设计调查
	 */
	public String designSurvey(){
		return "designSurveyPage" ;
	}
	
	/**
	 * 该方法在designSurvey之前而且还在modelDriven的getModel之前先执行
	 */
	public void prepareDesignSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
	}
	
	/**
	 * 编辑调查
	 */
	public String editSurvey(){
		return "editSurveyPage" ;
	}

	public void prepareEditSurvey(){
		this.model = surveyService.getSurvey(sid);
	}
	
	/**
	 * 更新调查
	 */
	public String updateSurvey(){
		//给sid赋值
		this.sid = model.getId();
		model.setUser(user);
		surveyService.updateSurvey(model);
		return "designSurveyAction" ;
	}
	
	/**
	 * 删除调查
	 */
	public String deleteSurvey(){
		surveyService.deleteSurvey(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 * 清除调查
	 */
	public String clearAnswers(){
		surveyService.clearAnswers(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 * 切换状态
	 */
	public String toggleStatus(){
		surveyService.toggleStatus(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 *到达增加Logo页面
	 */
	public String toAddLogoPage(){
		return "addLogoPage" ; 
	}
	
	public String doAddLogo(){
		//1.实现文件上传
		if(ValidateUtil.isValid(logoPhotoFileName)){
			///upload目录
			String dir = sc.getRealPath("/upload");
			//文件名
			long l = System.nanoTime();
			//扩展名
			String ext = logoPhotoFileName.substring(logoPhotoFileName.lastIndexOf("."));
			File newFile = new File(dir,l + ext);
			//实现上传
			logoPhoto.renameTo(newFile);
			
			//2.更新数据库logoPhotoPath路径
			surveyService.updateLogoPhotoPath(sid,"/upload/" + l + ext);
		}
		return "designSurveyAction" ;
	}
	
	/**
	 * 分析调查 
	 */
	public String analyzeSurvey(){
		return "analyzeSurveyListPage" ;
	}

	public void prepareAnalyzeSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
	}
	
	public void setUser(User user) {
		this.user = user ;
	}

	/**
	 * 注入ServletContext
	 */
	public void setServletContext(ServletContext context) {
		this.sc = context ;
	}
	
	/**
	 * logo图片存在吗?
	 */
	public boolean logoExist(){
		ServletActionContext.getServletContext();
		if(ValidateUtil.isValid(model.getLogoPhotoPath())){
			File file = new File(sc.getRealPath(model.getLogoPhotoPath()));
			return file.exists();
		}
		return false ;
	}
}
