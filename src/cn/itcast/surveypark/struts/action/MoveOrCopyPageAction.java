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

	// Դҳ��id
	private Integer srcPid;
	private Integer targPid;
	private Integer sid;

	// 0-֮ǰ 1-֮��
	private int pos;

	// ���е���
	private List<Survey> allSurveys;

	@Resource
	private SurveyService surveyService;

	// ����user
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
	 * ����ѡ��Ŀ��ҳ
	 */
	public String toSelectTargetPage() {
		this.allSurveys = surveyService.findSurveysWithPage(user.getId());
		return "moveOrCopyPageListPage";
	}

	/**
	 * �����ƶ�/����
	 */
	public String doMoveOrCopyPage() {
		surveyService.moveOrCopyPage(srcPid,targPid,pos);
		return "designSurveyAction";
	}

	public void setUser(User user) {
		this.user = user;
	}
}
