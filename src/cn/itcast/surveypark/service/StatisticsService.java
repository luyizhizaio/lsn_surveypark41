package cn.itcast.surveypark.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import cn.itcast.surveypark.domain.statistics.QuestionStatisticsModel;

/**
 * 统计服务接口
 * SEI:service endpoint interface,服务终端接口
 */
@WebService
public interface StatisticsService {
	
	@WebMethod
	public QuestionStatisticsModel statistics(Integer qid);
}
