package cn.itcast.surveypark.struts.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.service.SurveyService;

/*
 * PageAction
 */
@Controller
@Scope("prototype")
public class PageAction extends BaseAction<Page> {
	private static final long serialVersionUID = -8203517049246348361L;
	
	private Integer sid ;
	
	private Integer pid ;
	
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Resource
	private SurveyService surveyService ;
	
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	/**
	 * 添加页
	 */
	public String toAddPage(){
		return "addPagePage" ;
	}
	
	/**
	 * 保存/更新page
	 */
	public String saveOrUpdatePage(){
		Survey s = new Survey();
		s.setId(sid);
		model.setSurvey(s);
		surveyService.saveOrUpdatePage(model);
		return "designSurveyAction" ;
	}
	
	/**
	 * 编辑页面
	 */
	public String editPage(){
		return "editPagePage" ;
	}

	public void prepareEditPage(){
		this.model = surveyService.getPage(pid);
	}
	
	/**
	 * 删除页面
	 */
	public  String deletePage(){
		surveyService.deletePage(pid);
		return "designSurveyAction" ;
	}
}
