package cn.itcast.surveypark.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 路由数据源
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {

	/**
	 * 当前纳秒时间
	 */
	protected Object determineCurrentLookupKey() {
		MyThreadLocal mtl = MyThreadLocal.getCurrentObject();
		if(mtl != null){
			long l = mtl.getNanoTime();
			System.out.println(l);
			return "" + (l % 2) ;
		}
		return null ;
	}
}
