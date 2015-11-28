package cn.itcast.surveypark.datasource;


/**
 * �Զ���
 */
public class MyThreadLocal {
	
	private long nanoTime = 0 ;
	
	public long getNanoTime() {
		return nanoTime;
	}

	public void setNanoTime(long nanoTime) {
		this.nanoTime = nanoTime;
	}

	private static ThreadLocal<MyThreadLocal> mtl = new ThreadLocal<MyThreadLocal>();
	
	/**
	 * �ӵ�ǰ�߳���ȡ����
	 */
	public static MyThreadLocal getCurrentObject(){
		return mtl.get();
	}
	
	/**
	 * �󶨶��󵽵�ǰ�߳�
	 */
	public static void setCurrentObject(MyThreadLocal mtl0){
		mtl.set(mtl0);
	}
}
