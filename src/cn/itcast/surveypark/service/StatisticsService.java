package cn.itcast.surveypark.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import cn.itcast.surveypark.domain.statistics.QuestionStatisticsModel;

/**
 * ͳ�Ʒ���ӿ�
 * SEI:service endpoint interface,�����ն˽ӿ�
 */
@WebService
public interface StatisticsService {
	
	@WebMethod
	public QuestionStatisticsModel statistics(Integer qid);
}
