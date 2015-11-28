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
 	 * 新建调查
	 */
	public Survey newSurvey(User user){
		Survey s = new Survey();
		//设置Survey和user之间的关联
		s.setUser(user);
		Page p = new Page();
		//设置Survey和Page之间的关联
		p.setSurvey(s);
		s.getPages().add(p);
		surveyDao.saveEntity(s);
		pageDao.saveEntity(p);
		return s ;
	}
	
	/**
	 * 查询我的调查
	 */
	public List<Survey> findMySurveys(Integer userid){
		String hql = "from Survey s where s.user.id = ?";
		return surveyDao.findEntityByHQL(hql,userid);
	}
	
	/**
	 * 按照id查询Survey
	 */
	public Survey getSurvey(Integer sid){
		return surveyDao.getEntity(sid);
	}
	
	/**
	 * 查询调查对象,携带所有孩子
	 */
	public Survey getSurveyWithChildren(Integer sid){
		Survey s = surveyDao.getEntity(sid);
		for(Page p : s.getPages()){
			p.getQuestions().size();
		}
		return s;
	}
	
	/**
	 * 更新调查
	 */
	public void updateSurvey(Survey s){
		surveyDao.updateEntity(s);
	}
	
	/**
	 * 保存更新Page
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
	 * 保存/更新问题
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
	 * 删除问题
	 */
	public void deleteQuestion(Integer qid){
		String hql = "delete from Answer a where a.questionId = ?" ;
		answerDao.batchEntityByHQL(hql,qid);
		hql = "delete from Question q where q.id = ?" ;
		questionDao.batchEntityByHQL(hql,qid);
	}
	
	/**
	 * 删除页面
	 */
	public void deletePage(Integer pid){
		Page p = this.getPage(pid);
		Survey s = p.getSurvey();
		
		//查询页面数量
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
		
		//如果是最后Page,需要手动保存一个Page对象
		if(count == 1){
			p = new Page();
			p.setSurvey(s);
			//s.getPages().add(p);
			pageDao.saveEntity(p);
		}
	}
	
	/**
	 * 删除调查
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
	 * 清除调查 
	 */
	public void clearAnswers(Integer sid){
		String hql = "delete from Answer a where a.surveyId = ?" ;
		answerDao.batchEntityByHQL(hql,sid);
	}
	
	/**
	 * 切换状态
	 */
	public void toggleStatus(Integer sid){
		Survey s = this.getSurvey(sid);
		String hql = "update Survey s set s.closed = ? where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql,!s.isClosed(),sid);
	}
	
	/**
	 * 更新logo路径
	 */
	public void updateLogoPhotoPath(Integer sid, String path){
		String hql = "update Survey s set s.logoPhotoPath = ? where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql, path,sid);
	}
	
	/**
	 * 查询所有调查(携带page集合)
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
	 * 完成移动/复制
	 */
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos){
		Page srcPage = this.getPage(srcPid);
		Survey srcSurvey = srcPage.getSurvey();
		
		Page targPage = this.getPage(targPid);
		Survey targSurvey = targPage.getSurvey();
		
		//移动
		if(srcSurvey.getId().equals(targSurvey.getId())){
			setOrderno(srcPage,targPage,pos);
		}
		//复制
		else{
			//强行初始化问题集合
			srcPage.getQuestions().size();
			Page newPage = (Page) DataUtil.deeplyCopy(srcPage) ;//深度复制srcPage对象
			//重新设置调查对象为目标调查
			newPage.setSurvey(targSurvey);
			//保存page
			pageDao.saveEntity(newPage);
			//手动保存问题对象
			for(Question q : newPage.getQuestions()){
				questionDao.saveEntity(q);
			}
			setOrderno(newPage,targPage,pos);
		}
		
	}

	/**
	 * 设置页序
	 */
	private void setOrderno(Page srcPage, Page targPage, int pos) {
		//之前
		if(pos == 0){
			//是首页
			if(isFirstPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() - 0.01f);
			}
			//不是首页
			else{
				Page prePage = getPrePage(targPage);
				srcPage.setOrderno((prePage.getOrderno() + targPage.getOrderno()) / 2);
			}
		}
		//之后
		else{
			//是尾页
			if(isLastPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() + 0.01f);
			}
			//不是尾页
			else{
				Page nextPage = getNextPage(targPage);
				srcPage.setOrderno((nextPage.getOrderno() + targPage.getOrderno()) / 2);
			}
		}
	}

	/**
	 * 得到指定页面的前一页
	 */
	private Page getPrePage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno < ? order by p.orderno desc" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return list.get(0);
	}
	/**
	 * 得到指定页面的下一页
	 */
	private Page getNextPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno > ? order by p.orderno asc" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return list.get(0);
	}

	/**
	 * 是否是首页
	 */
	private boolean isFirstPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno < ?" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return !ValidateUtil.isValid(list);
	}
	
	/**
	 * 是否是尾页
	 */
	private boolean isLastPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno > ?" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return !ValidateUtil.isValid(list);
	}
	

	/**
	 * 查询所有可用的调查
	 */
	public List<Survey> findAllAvailableSurveys(){
		String hql = "from Survey s where s.closed = ?" ;
		return surveyDao.findEntityByHQL(hql, false);
	}
	
	/**
	 * 查询调查首页
	 */
	public Page getFirstPage(Integer sid){
		String hql = "from Page p where p.survey.id = ? order by p.orderno asc" ;
		List<Page>  list = pageDao.findEntityByHQL(hql,sid);
		if(ValidateUtil.isValid(list)){
			Page p = list.get(0);
			//初始化调查对象
			p.getSurvey().getTitle();
			//初始化问题集合
			p.getQuestions().size();
			return p;
		}
		return null ;
	}
	
	/**
	 * 获得前一页
	 */
	public Page getPrePage(Integer currPid){
		Page p = this.getPage(currPid);
		p = this.getPrePage(p);
		p.getQuestions().size();
		return p ;
	}
	
	/**
	 * 获得前一页
	 */
	public Page getNextPage(Integer currPid){
		Page p = this.getPage(currPid);
		p = this.getNextPage(p);
		p.getQuestions().size();
		return p ;
	}
	
	/**
	 *批量保存答案集合
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
	 * 查询调查的所有问题
	 */
	public List<Question> getAllQuestions(Integer sid){
		String hql = "from Question q where q.page.survey.id = ?" ;
		return questionDao.findEntityByHQL(hql,sid);
	}
	

	/**
	 * 查询所有答案
	 */
	public List<Answer> findAllAnswers(Integer sid){
		String hql = "from Answer a where a.surveyId = ? order by a.uuid" ;
		return answerDao.findEntityByHQL(hql, sid);
	}
}
