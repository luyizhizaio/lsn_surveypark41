package cn.itcast.surveypark.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * ·������Դ
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {

	/**
	 * ��ǰ����ʱ��
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
