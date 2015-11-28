package cn.itcast.surveypark.struts.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Answer;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.service.SurveyService;

/**
 * CollSurveyAction
 */
@Controller
@Scope("prototype")
public class CollSurveyAction extends BaseAction<Survey> {

	private static final long serialVersionUID = -5986313966566149820L;

	private Integer sid ;
	
	@Resource
	private SurveyService surveySerivce ;
	
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	public InputStream getIs(){
		try {
			Map<Integer, Integer> qidIndexMap = new HashMap<Integer, Integer>();
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("survey sheet");
			HSSFRow row = sheet.createRow(0);
			List<Question> questions = surveySerivce.getAllQuestions(sid);
			Question q = null ;
			HSSFCell cell = null ;
			//输出表头
			for(int i = 0 ; i < questions.size() ; i ++){
				q = questions.get(i);
				cell = row.createCell(i);
				cell.setCellValue(q.getTitle());
				qidIndexMap.put(q.getId(), i);
			}
			
			//输出answers
			String oldUuid = "" ;
			String newUuid = "" ;
			int rowIndex = 0 ;
			List<Answer> answers = surveySerivce.findAllAnswers(sid);
			for(Answer a : answers){
				newUuid = a.getUuid();
				if(!oldUuid.equals(newUuid)){
					oldUuid = newUuid ;
					rowIndex ++ ;
					row = sheet.createRow(rowIndex);
				}
				row.createCell(qidIndexMap.get(a.getQuestionId())).setCellValue(a.getAnswerIds());
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			wb.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
}
