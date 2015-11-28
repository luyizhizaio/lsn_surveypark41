package cn.itcast.surveypark.service;

import java.util.List;

import cn.itcast.surveypark.domain.Answer;
import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.domain.User;

/**
 * ����Service
 */
public interface SurveyService {

	/**
	 * �½�����
	 */
	public Survey newSurvey(User user);

	/**
	 * ��ѯ�ҵĵ���
	 */
	public List<Survey> findMySurveys(Integer id);

	/**
	 * ����id��ѯSurvey
	 */
	public Survey getSurvey(Integer sid);

	/**
	 * ��ѯ�������,Я�����к���
	 */
	public Survey getSurveyWithChildren(Integer sid);

	/**
	 * ���µ���
	 */
	public void updateSurvey(Survey model);

	/**
	 * �������Page
	 */
	public void saveOrUpdatePage(Page model);

	/**
	 * getPage
	 */
	public Page getPage(Integer pid);

	/**
	 * ����/��������
	 */
	public void saveOrUpdateQuestion(Question model);

	
	/**
	 * getQuestion
	 */
	public Question getQuestion(Integer id);

	/**
	 * ɾ������
	 */
	public void deleteQuestion(Integer qid);

	/**
	 * ɾ��ҳ��
	 */
	public void deletePage(Integer pid);

	/**
	 * ɾ������
	 */
	public void deleteSurvey(Integer sid);

	/**
	 * ������� 
	 */
	public void clearAnswers(Integer sid);

	/**
	 * �л�״̬
	 */
	public void toggleStatus(Integer sid);

	/**
	 * ����logo·��
	 */
	public void updateLogoPhotoPath(Integer sid, String string);

	/**
	 * ��ѯ���е���(Я��page����)
	 */
	public List<Survey> findSurveysWithPage(Integer userid);

	/**
	 * ����ƶ�/����
	 */
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos);

	/**
	 * ��ѯ���п��õĵ���
	 */
	public List<Survey> findAllAvailableSurveys();

	/**
	 * ��ѯ������ҳ
	 */
	public Page getFirstPage(Integer sid);

	/**
	 * ���ǰһҳ
	 */
	public Page getPrePage(Integer currPid);
	/**
	 * �����һҳ
	 */
	public Page getNextPage(Integer currPid);

	/**
	 *��������𰸼���
	 */
	public void saveAnswers(List<Answer> processAnswers);

	/**
	 * ��ѯ�������������
	 */
	public List<Question> getAllQuestions(Integer sid);

	/**
	 * ��ѯ���д�
	 */
	public List<Answer> findAllAnswers(Integer sid);

}
