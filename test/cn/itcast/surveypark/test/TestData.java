package cn.itcast.surveypark.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.Survey;


/**
 * TestUserService
 */
public class TestData {
	public static void main(String[] args) throws Exception {
		Page p1 = new Page();
		p1.setTitle("p1");
		
		Survey s1 = new Survey();
		s1.setTitle("s1");
		
		Question q1 = new Question();
		q1.setTitle("q1");
		
		Question q2 = new Question();
		q2.setTitle("q2");
		
		p1.setSurvey(s1);
		p1.getQuestions().add(q1);
		p1.getQuestions().add(q2);
		
		//Ç³¶È
//		Page newPage = new Page();
//		newPage.setSurvey(p1.getSurvey());
//		newPage.setQuestions(p1.getQuestions());
//		System.out.println("kk");
		
		//
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(p1);
		oos.close();
		baos.close();
		
		byte[] data = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		ois.readObject();
		ois.close();
		bais.close();
		System.out.println("over");
	}
}
