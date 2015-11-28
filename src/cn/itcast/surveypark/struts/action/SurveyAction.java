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
 * ����action
 */
@Controller
@Scope("prototype")
public class SurveyAction extends BaseAction<Survey> implements UserAware,ServletContextAware {

	private static final long serialVersionUID = 4640764824027522277L;
	
	@Resource
	private SurveyService surveyService ;
	
	//mySurveys
	private List<Survey> mySurveys ;
	
	//����sid
	private Integer sid ;
	

	//����session��user����
	private User user;
	
	//ʵ���ϴ�
	private File logoPhoto ;
	
	//input����ҳ
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

	//����ServletContext����
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
	 * �½�����
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
	 * �ҵĵ���
	 */
	public String mySurveys(){
		this.mySurveys = surveyService.findMySurveys(user.getId());
		return "mySurveyListPage" ;
	}
	
	/**
	 * ��Ƶ���
	 */
	public String designSurvey(){
		return "designSurveyPage" ;
	}
	
	/**
	 * �÷�����designSurvey֮ǰ���һ���modelDriven��getModel֮ǰ��ִ��
	 */
	public void prepareDesignSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
	}
	
	/**
	 * �༭����
	 */
	public String editSurvey(){
		return "editSurveyPage" ;
	}

	public void prepareEditSurvey(){
		this.model = surveyService.getSurvey(sid);
	}
	
	/**
	 * ���µ���
	 */
	public String updateSurvey(){
		//��sid��ֵ
		this.sid = model.getId();
		model.setUser(user);
		surveyService.updateSurvey(model);
		return "designSurveyAction" ;
	}
	
	/**
	 * ɾ������
	 */
	public String deleteSurvey(){
		surveyService.deleteSurvey(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 * �������
	 */
	public String clearAnswers(){
		surveyService.clearAnswers(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 * �л�״̬
	 */
	public String toggleStatus(){
		surveyService.toggleStatus(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 *��������Logoҳ��
	 */
	public String toAddLogoPage(){
		return "addLogoPage" ; 
	}
	
	public String doAddLogo(){
		//1.ʵ���ļ��ϴ�
		if(ValidateUtil.isValid(logoPhotoFileName)){
			///uploadĿ¼
			String dir = sc.getRealPath("/upload");
			//�ļ���
			long l = System.nanoTime();
			//��չ��
			String ext = logoPhotoFileName.substring(logoPhotoFileName.lastIndexOf("."));
			File newFile = new File(dir,l + ext);
			//ʵ���ϴ�
			logoPhoto.renameTo(newFile);
			
			//2.�������ݿ�logoPhotoPath·��
			surveyService.updateLogoPhotoPath(sid,"/upload/" + l + ext);
		}
		return "designSurveyAction" ;
	}
	
	/**
	 * �������� 
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
	 * ע��ServletContext
	 */
	public void setServletContext(ServletContext context) {
		this.sc = context ;
	}
	
	/**
	 * logoͼƬ������?
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
