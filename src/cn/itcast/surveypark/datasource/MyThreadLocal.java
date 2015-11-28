package cn.itcast.surveypark.datasource;


/**
 * 自定义
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
	 * 从当前线程提取对象
	 */
	public static MyThreadLocal getCurrentObject(){
		return mtl.get();
	}
	
	/**
	 * 绑定对象到当前线程
	 */
	public static void setCurrentObject(MyThreadLocal mtl0){
		mtl.set(mtl0);
	}
}
