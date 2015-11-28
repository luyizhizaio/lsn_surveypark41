package cn.itcast.surveypark.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.Answer;
import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.SurveyService;
import cn.itcast.surveypark.util.DataUtil;
import cn.itcast.surveypark.util.ValidateUtil;

/**
 * SurveyServiceImpl
 */
@Service("surveyService")
public class SurveyServiceImpl implements SurveyService {
	@Resource(name="surveyDao")
	private BaseDao<Survey> surveyDao ;
	
	@Resource(name="pageDao")
	private BaseDao<Page> pageDao ;
	
	@Resource(name="questionDao")
	private BaseDao<Question> questionDao ;
	
	@Resource(name="answerDao")
	private BaseDao<Answer> answerDao ;
	
	/**
 	 * �½�����
	 */
	public Survey newSurvey(User user){
		Survey s = new Survey();
		//����Survey��user֮��Ĺ���
		s.setUser(user);
		Page p = new Page();
		//����Survey��Page֮��Ĺ���
		p.setSurvey(s);
		s.getPages().add(p);
		surveyDao.saveEntity(s);
		pageDao.saveEntity(p);
		return s ;
	}
	
	/**
	 * ��ѯ�ҵĵ���
	 */
	public List<Survey> findMySurveys(Integer userid){
		String hql = "from Survey s where s.user.id = ?";
		return surveyDao.findEntityByHQL(hql,userid);
	}
	
	/**
	 * ����id��ѯSurvey
	 */
	public Survey getSurvey(Integer sid){
		return surveyDao.getEntity(sid);
	}
	
	/**
	 * ��ѯ�������,Я�����к���
	 */
	public Survey getSurveyWithChildren(Integer sid){
		Survey s = surveyDao.getEntity(sid);
		for(Page p : s.getPages()){
			p.getQuestions().size();
		}
		return s;
	}
	
	/**
	 * ���µ���
	 */
	public void updateSurvey(Survey s){
		surveyDao.updateEntity(s);
	}
	
	/**
	 * �������Page
	 */
	public void saveOrUpdatePage(Page model){
		pageDao.saveOrUpdateEntity(model);
	}
	
	/**
	 * getPage
	 */
	public Page getPage(Integer pid){
		return pageDao.getEntity(pid);
	}
	

	/**
	 * ����/��������
	 */
	public void saveOrUpdateQuestion(Question model){
		questionDao.saveOrUpdateEntity(model);
	}
	
	/**
	 * getQuestion
	 */
	public Question getQuestion(Integer id){
		return questionDao.getEntity(id);
	}
	
	/**
	 * ɾ������
	 */
	public void deleteQuestion(Integer qid){
		String hql = "delete from Answer a where a.questionId = ?" ;
		answerDao.batchEntityByHQL(hql,qid);
		hql = "delete from Question q where q.id = ?" ;
		questionDao.batchEntityByHQL(hql,qid);
	}
	
	/**
	 * ɾ��ҳ��
	 */
	public void deletePage(Integer pid){
		Page p = this.getPage(pid);
		Survey s = p.getSurvey();
		
		//��ѯҳ������
		String hql = "select count(*) from Page p where p.survey.id = ?" ;
		Long count = (Long) pageDao.uniqueResult(hql, s.getId());
		
		//answer
		hql = "delete from Answer a where a.questionId in (select q.id from Question q where q.page.id = ?)" ;
		answerDao.batchEntityByHQL(hql,pid);
		//question
		hql = "delete from Question q where q.page.id = ?" ;
		questionDao.batchEntityByHQL(hql,pid);
		//page
		hql = "delete from Page p where p.id = ?" ;
		pageDao.batchEntityByHQL(hql,pid);
		
		//��������Page,��Ҫ�ֶ�����һ��Page����
		if(count == 1){
			p = new Page();
			p.setSurvey(s);
			//s.getPages().add(p);
			pageDao.saveEntity(p);
		}
	}
	
	/**
	 * ɾ������
	 */
	public void deleteSurvey(Integer sid){
		//1.answer
		String hql = "delete from Answer a where a.surveyId = ?" ;
		answerDao.batchEntityByHQL(hql,sid);
		//2.question
		hql = "delete from Question q where q.page.id in (select p.id from Page p where p.survey.id = ?)" ;
		questionDao.batchEntityByHQL(hql,sid);
		//3.page
		hql = "delete from Page p where p.survey.id = ?" ;
		pageDao.batchEntityByHQL(hql,sid);
		//4.survey
		hql = "delete from Survey s where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql,sid);
	}
	
	/**
	 * ������� 
	 */
	public void clearAnswers(Integer sid){
		String hql = "delete from Answer a where a.surveyId = ?" ;
		answerDao.batchEntityByHQL(hql,sid);
	}
	
	/**
	 * �л�״̬
	 */
	public void toggleStatus(Integer sid){
		Survey s = this.getSurvey(sid);
		String hql = "update Survey s set s.closed = ? where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql,!s.isClosed(),sid);
	}
	
	/**
	 * ����logo·��
	 */
	public void updateLogoPhotoPath(Integer sid, String path){
		String hql = "update Survey s set s.logoPhotoPath = ? where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql, path,sid);
	}
	
	/**
	 * ��ѯ���е���(Я��page����)
	 */
	public List<Survey> findSurveysWithPage(Integer userid){
		String hql = "from Survey s where s.user.id = ?" ;
		List<Survey> list =  surveyDao.findEntityByHQL(hql, userid) ;
		for(Survey s : list){
			s.getPages().size();
		}
		return list ;
	}
	
	/**
	 * ����ƶ�/����
	 */
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos){
		Page srcPage = this.getPage(srcPid);
		Survey srcSurvey = srcPage.getSurvey();
		
		Page targPage = this.getPage(targPid);
		Survey targSurvey = targPage.getSurvey();
		
		//�ƶ�
		if(srcSurvey.getId().equals(targSurvey.getId())){
			setOrderno(srcPage,targPage,pos);
		}
		//����
		else{
			//ǿ�г�ʼ�����⼯��
			srcPage.getQuestions().size();
			Page newPage = (Page) DataUtil.deeplyCopy(srcPage) ;//��ȸ���srcPage����
			//�������õ������ΪĿ�����
			newPage.setSurvey(targSurvey);
			//����page
			pageDao.saveEntity(newPage);
			//�ֶ������������
			for(Question q : newPage.getQuestions()){
				questionDao.saveEntity(q);
			}
			setOrderno(newPage,targPage,pos);
		}
		
	}

	/**
	 * ����ҳ��
	 */
	private void setOrderno(Page srcPage, Page targPage, int pos) {
		//֮ǰ
		if(pos == 0){
			//����ҳ
			if(isFirstPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() - 0.01f);
			}
			//������ҳ
			else{
				Page prePage = getPrePage(targPage);
				srcPage.setOrderno((prePage.getOrderno() + targPage.getOrderno()) / 2);
			}
		}
		//֮��
		else{
			//��βҳ
			if(isLastPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() + 0.01f);
			}
			//����βҳ
			else{
				Page nextPage = getNextPage(targPage);
				srcPage.setOrderno((nextPage.getOrderno() + targPage.getOrderno()) / 2);
			}
		}
	}

	/**
	 * �õ�ָ��ҳ���ǰһҳ
	 */
	private Page getPrePage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno < ? order by p.orderno desc" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return list.get(0);
	}
	/**
	 * �õ�ָ��ҳ�����һҳ
	 */
	private Page getNextPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno > ? order by p.orderno asc" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return list.get(0);
	}

	/**
	 * �Ƿ�����ҳ
	 */
	private boolean isFirstPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno < ?" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return !ValidateUtil.isValid(list);
	}
	
	/**
	 * �Ƿ���βҳ
	 */
	private boolean isLastPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno > ?" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return !ValidateUtil.isValid(list);
	}
	

	/**
	 * ��ѯ���п��õĵ���
	 */
	public List<Survey> findAllAvailableSurveys(){
		String hql = "from Survey s where s.closed = ?" ;
		return surveyDao.findEntityByHQL(hql, false);
	}
	
	/**
	 * ��ѯ������ҳ
	 */
	public Page getFirstPage(Integer sid){
		String hql = "from Page p where p.survey.id = ? order by p.orderno asc" ;
		List<Page>  list = pageDao.findEntityByHQL(hql,sid);
		if(ValidateUtil.isValid(list)){
			Page p = list.get(0);
			//��ʼ���������
			p.getSurvey().getTitle();
			//��ʼ�����⼯��
			p.getQuestions().size();
			return p;
		}
		return null ;
	}
	
	/**
	 * ���ǰһҳ
	 */
	public Page getPrePage(Integer currPid){
		Page p = this.getPage(currPid);
		p = this.getPrePage(p);
		p.getQuestions().size();
		return p ;
	}
	
	/**
	 * ���ǰһҳ
	 */
	public Page getNextPage(Integer currPid){
		Page p = this.getPage(currPid);
		p = this.getNextPage(p);
		p.getQuestions().size();
		return p ;
	}
	
	/**
	 *��������𰸼���
	 */
	public void saveAnswers(List<Answer> processAnswers){
		Date date = new Date();
		String uuid = UUID.randomUUID().toString();
		for(Answer a : processAnswers){
			a.setAnswerTime(date);
			a.setUuid(uuid);
			answerDao.saveEntity(a);
		}
	}
	
	/**
	 * ��ѯ�������������
	 */
	public List<Question> getAllQuestions(Integer sid){
		String hql = "from Question q where q.page.survey.id = ?" ;
		return questionDao.findEntityByHQL(hql,sid);
	}
	

	/**
	 * ��ѯ���д�
	 */
	public List<Answer> findAllAnswers(Integer sid){
		String hql = "from Answer a where a.surveyId = ? order by a.uuid" ;
		return answerDao.findEntityByHQL(hql, sid);
	}
}
