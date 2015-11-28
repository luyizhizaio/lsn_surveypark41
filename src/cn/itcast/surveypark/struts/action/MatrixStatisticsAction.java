package cn.itcast.surveypark.struts.action;

import java.text.DecimalFormat;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.statistics.OptionStatisticsModel;
import cn.itcast.surveypark.domain.statistics.QuestionStatisticsModel;
import cn.itcast.surveypark.service.StatisticsService;

/**
 * MatrixStatisticsAction
 */
@Controller
@Scope("prototype")
public class MatrixStatisticsAction extends BaseAction<Question> {

	private static final long serialVersionUID = -8124973622074975599L;

	private Integer qid;

	private QuestionStatisticsModel qsm;

	@Resource
	private StatisticsService ss;

	public QuestionStatisticsModel getQsm() {
		return qsm;
	}

	public void setQsm(QuestionStatisticsModel qsm) {
		this.qsm = qsm;

	}

	public Integer getQid() {
		return qid;
	}

	public void setQid(Integer qid) {
		this.qid = qid;
	}

	public String execute() throws Exception {
		this.qsm = ss.statistics(qid);
		return qsm.getQuestion().getQuestionType() + "";
	}
	
	public String getScale(int rowIndex ,int colIndex){
		//问题总人数
		int qcount = qsm.getCount();
		int ocount = 0 ;
		for(OptionStatisticsModel osm : qsm.getOsms()){
			if(osm.getMatrixRowTitleIndex() == rowIndex &&
					osm.getMatrixColTitleIndex() == colIndex){
				ocount = osm.getCount();
				break ;
			}
		}
		float scale = 0 ;
		if(qcount != 0){
			scale = new Float(ocount) /new Float(qcount);
		}
		DecimalFormat df = new DecimalFormat();
		df.applyPattern("#,###.00");
		return "" + ocount + "(" + df.format(scale * 100) + "%)";
	}
	
	public String getScale(int rowIndex ,int colIndex,int optIndex){
		//问题总人数
		int qcount = qsm.getCount();
		int ocount = 0 ;
		for(OptionStatisticsModel osm : qsm.getOsms()){
			if(osm.getMatrixRowTitleIndex() == rowIndex &&
					osm.getMatrixColTitleIndex() == colIndex
					&& osm.getMatrixSelectOptionIndex() == optIndex){
				ocount = osm.getCount();
				break ;
			}
		}
		float scale = 0 ;
		if(qcount != 0){
			scale = new Float(ocount) /new Float(qcount);
		}
		DecimalFormat df = new DecimalFormat();
		df.applyPattern("#,###.00");
		return "" + ocount + "(" + df.format(scale * 100) + "%)";
	}
	
	/**
	 * 
	 */
	public int getPercent(int rowIndex ,int colIndex,int optIndex){
		//问题总人数
		int qcount = qsm.getCount();
		int ocount = 0 ;
		for(OptionStatisticsModel osm : qsm.getOsms()){
			if(osm.getMatrixRowTitleIndex() == rowIndex &&
					osm.getMatrixColTitleIndex() == colIndex
					&& osm.getMatrixSelectOptionIndex() == optIndex){
				ocount = osm.getCount();
				break ;
			}
		}
		float scale = 0 ;
		if(qcount != 0){
			scale = new Float(ocount) /new Float(qcount) *100;
		}
		return (int) scale;
	}
}
