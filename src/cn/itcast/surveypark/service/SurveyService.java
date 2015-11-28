package cn.itcast.surveypark.service;

import java.util.List;

import cn.itcast.surveypark.domain.Answer;
import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.domain.User;

/**
 * 调查Service
 */
public interface SurveyService {

	/**
	 * 新建调查
	 */
	public Survey newSurvey(User user);

	/**
	 * 查询我的调查
	 */
	public List<Survey> findMySurveys(Integer id);

	/**
	 * 按照id查询Survey
	 */
	public Survey getSurvey(Integer sid);

	/**
	 * 查询调查对象,携带所有孩子
	 */
	public Survey getSurveyWithChildren(Integer sid);

	/**
	 * 更新调查
	 */
	public void updateSurvey(Survey model);

	/**
	 * 保存更新Page
	 */
	public void saveOrUpdatePage(Page model);

	/**
	 * getPage
	 */
	public Page getPage(Integer pid);

	/**
	 * 保存/更新问题
	 */
	public void saveOrUpdateQuestion(Question model);

	
	/**
	 * getQuestion
	 */
	public Question getQuestion(Integer id);

	/**
	 * 删除问题
	 */
	public void deleteQuestion(Integer qid);

	/**
	 * 删除页面
	 */
	public void deletePage(Integer pid);

	/**
	 * 删除调查
	 */
	public void deleteSurvey(Integer sid);

	/**
	 * 清除调查 
	 */
	public void clearAnswers(Integer sid);

	/**
	 * 切换状态
	 */
	public void toggleStatus(Integer sid);

	/**
	 * 更新logo路径
	 */
	public void updateLogoPhotoPath(Integer sid, String string);

	/**
	 * 查询所有调查(携带page集合)
	 */
	public List<Survey> findSurveysWithPage(Integer userid);

	/**
	 * 完成移动/复制
	 */
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos);

	/**
	 * 查询所有可用的调查
	 */
	public List<Survey> findAllAvailableSurveys();

	/**
	 * 查询调查首页
	 */
	public Page getFirstPage(Integer sid);

	/**
	 * 获得前一页
	 */
	public Page getPrePage(Integer currPid);
	/**
	 * 获得下一页
	 */
	public Page getNextPage(Integer currPid);

	/**
	 *批量保存答案集合
	 */
	public void saveAnswers(List<Answer> processAnswers);

	/**
	 * 查询调查的所有问题
	 */
	public List<Question> getAllQuestions(Integer sid);

	/**
	 * 查询所有答案
	 */
	public List<Answer> findAllAnswers(Integer sid);

}
