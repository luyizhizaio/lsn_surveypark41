package cn.itcast.surveypark.struts.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.SurveyService;
import cn.itcast.surveypark.struts.UserAware;

/**
 * MoveOrCopyPageAction
 */
@Controller
@Scope("prototype")
public class MoveOrCopyPageAction extends BaseAction<Page> implements UserAware {
	private static final long serialVersionUID = -4100391501966727342L;

	// 源页面id
	private Integer srcPid;
	private Integer targPid;
	private Integer sid;

	// 0-之前 1-之后
	private int pos;

	// 所有调查
	private List<Survey> allSurveys;

	@Resource
	private SurveyService surveyService;

	// 接收user
	private User user;
	
	public Integer getTargPid() {
		return targPid;
	}

	public void setTargPid(Integer targPid) {
		this.targPid = targPid;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}


	public List<Survey> getAllSurveys() {
		return allSurveys;
	}

	public void setAllSurveys(List<Survey> allSurveys) {
		this.allSurveys = allSurveys;
	}

	public Integer getSrcPid() {
		return srcPid;
	}

	public void setSrcPid(Integer srcPid) {
		this.srcPid = srcPid;
	}

	/**
	 * 到达选择目标页
	 */
	public String toSelectTargetPage() {
		this.allSurveys = surveyService.findSurveysWithPage(user.getId());
		return "moveOrCopyPageListPage";
	}

	/**
	 * 进行移动/复制
	 */
	public String doMoveOrCopyPage() {
		surveyService.moveOrCopyPage(srcPid,targPid,pos);
		return "designSurveyAction";
	}

	public void setUser(User user) {
		this.user = user;
	}
}
