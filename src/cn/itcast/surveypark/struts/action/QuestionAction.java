package cn.itcast.surveypark.struts.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.service.SurveyService;

/**
 * QuestionAction
 */
@Controller
@Scope("prototype")
public class QuestionAction extends BaseAction<Question> {

	private static final long serialVersionUID = 2935999925989541649L;

	private Integer sid;
	
	private Integer pid;
	
	private Integer qid;
	
	public Integer getQid() {
		return qid;
	}

	public void setQid(Integer qid) {
		this.qid = qid;
	}

	@Resource
	private SurveyService surveyService ;
	
	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}


	/**
	 * 到达选题型页面
	 */
	public String toSelectQuestionType(){
		return "selectQuestionTypePage" ;
	}
	
	/**
	 * 到达设计问题页面
	 */
	public String toDesignQuestionPage(){
		return model.getQuestionType() + "" ;
	}
	
	/**
	 * 保存/更新问题
	 */
	public String saveOrUpdateQuestion(){
		Page p = new Page();
		p.setId(pid);
		model.setPage(p);
		surveyService.saveOrUpdateQuestion(model);
		return "designSurveyAction" ;
	}
	
	/**
	 * 编辑问题
	 */
	public String editQuestion(){
		return "" + model.getQuestionType() ;
	}

	public void prepareEditQuestion(){
		this.model = surveyService.getQuestion(qid);
	}
	
	/**
	 * 删除问题
	 */
	public String deleteQuestion(){
		surveyService.deleteQuestion(qid);
		return "designSurveyAction" ;
	}
}
