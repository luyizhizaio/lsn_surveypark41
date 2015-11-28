package cn.itcast.surveypark.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Set;

import cn.itcast.surveypark.domain.BaseEntity;

/**
 * 数据工具类
 */
public class DataUtil {

	/**
	 * 对数据进行加密
	 */
	public static String md5(String src) {
		StringBuffer buffer = new StringBuffer();
		char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(src.getBytes());
			for (byte b : bytes) {
				buffer.append(chars[b >> 4 & 0xf]);
				buffer.append(chars[b & 0xf]);
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 深度复制
	 */
	public static Serializable deeplyCopy(Serializable src) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(src);
			oos.close();
			baos.close();

			byte[] data = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Serializable copy = (Serializable) ois.readObject();
			ois.close();
			bais.close();
			return copy ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 抽取所有实体的id
	 */
	public static String extractIds(Set<? extends BaseEntity> entities){
		String str = "" ;
		Iterator<? extends BaseEntity> it = entities.iterator();
		while(it.hasNext()){
			str = str + it.next().getId() + ",";
		}
		return str.substring(0,str.length() -1) ;
	}
}
