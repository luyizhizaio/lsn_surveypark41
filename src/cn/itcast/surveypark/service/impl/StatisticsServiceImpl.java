package cn.itcast.surveypark.service.impl;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.springframework.stereotype.Service;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.Answer;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.statistics.OptionStatisticsModel;
import cn.itcast.surveypark.domain.statistics.QuestionStatisticsModel;
import cn.itcast.surveypark.service.StatisticsService;


/**
 * ͳ�Ʒ���
 */
@Service("statisticsService")
@WebService
public class StatisticsServiceImpl implements StatisticsService {
	
	@Resource(name="questionDao")
	private BaseDao<Question> questionDao ; 
	
	@Resource(name="answerDao")
	private BaseDao<Answer> answerDao ; 
	//ͳ��
	public QuestionStatisticsModel statistics(Integer qid){
		Question q = questionDao.getEntity(qid);
		QuestionStatisticsModel qsm = new QuestionStatisticsModel();
		qsm.setQuestion(q);
		
		//�ش������������
		String qhql = "select count(*) from Answer a where a.questionId = ?" ;
		String ohql = "select count(*) from Answer a where a.questionId = ? and concat(',',a.answerIds,',') like ?" ;
		Long qcount = (Long) answerDao.uniqueResult(qhql, qid);
		qsm.setCount(qcount.intValue());
		
		OptionStatisticsModel osm = null ;
		//ѡ��ͳ��
		switch(q.getQuestionType()){
			//�Ǿ���ʽ����ͳ��
			case 0:
			case 1: 
			case 2: 
			case 3: 
			case 4: 
				String[] optionArr = q.getOptionArr();
				for(int i = 0 ; i < optionArr.length ; i ++){
					osm = new OptionStatisticsModel();
					osm.setOptionLabel(optionArr[i]);
					osm.setOptionIndex(i);
					Long ocount = (Long) answerDao.uniqueResult(ohql, qid,"%,"+i+",%");
					osm.setCount(ocount.intValue());
					qsm.getOsms().add(osm);
				}
				if(q.isOther()){
					osm = new OptionStatisticsModel();
					osm.setOptionLabel("����");
					Long ocount = (Long) answerDao.uniqueResult(ohql, qid,"%other%");
					osm.setCount(ocount.intValue());
					qsm.getOsms().add(osm);
				}
				break ;
			//����ʽ����ͳ��
			case 6: 
			case 7: 
			case 8:
				String[] rowArr = q.getMatrixRowTitleArr();
				String[] colArr = q.getMatrixColTitleArr();
				for(int i = 0 ; i < rowArr.length ; i ++){
					for(int j = 0 ; j < colArr.length ; j ++){
						//����ʽradio|checkbox
						if(q.getQuestionType() != 8){
							osm = new OptionStatisticsModel();
							osm.setMatrixRowTitleLabel(rowArr[i]);
							osm.setMatrixRowTitleIndex(i);
							osm.setMatrixColTitleLabel(colArr[j]);
							osm.setMatrixColTitleIndex(j);
							
							Long ocount = (Long) answerDao.uniqueResult(ohql, qid,"%,"+i + "_" + j +",%");
							osm.setCount(ocount.intValue());
							qsm.getOsms().add(osm);
						}
						//select
						else{
							String[] selectArr = q.getMatrixSelectOptionArr(); 
							for(int k = 0 ; k < selectArr.length ; k ++){
								osm = new OptionStatisticsModel();
								osm.setMatrixRowTitleLabel(rowArr[i]);
								osm.setMatrixRowTitleIndex(i);
								osm.setMatrixColTitleLabel(colArr[j]);
								osm.setMatrixColTitleIndex(j);
								osm.setMatrixSelectOptionLabel(selectArr[k]);
								osm.setMatrixSelectOptionIndex(k);
								
								Long ocount = (Long) answerDao.uniqueResult(ohql, qid,"%,"+i + "_" + j + "_" + k +",%");
								osm.setCount(ocount.intValue());
								qsm.getOsms().add(osm);
							}
						}
					}
				}
				break ;
		}
		return qsm ;
	}
}
