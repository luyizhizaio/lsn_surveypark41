package cn.itcast.surveypark.struts.action;

import java.awt.Font;

import javax.annotation.Resource;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.statistics.OptionStatisticsModel;
import cn.itcast.surveypark.domain.statistics.QuestionStatisticsModel;
import cn.itcast.surveypark.service.StatisticsService;

/**
 * ��ԷǾ���ʽ���͵����,���б�ͼ ��״ͼ ����ͼ��
 */
@Controller
@Scope("prototype")
public class ChartOutputAction extends BaseAction<Question> {
	private static final long serialVersionUID = -9021947290801678287L;
	/* ƽ���ͼ */
	private static final int CHARTTYPE_PIE_2D = 0;
	/* �����ͼ */
	private static final int CHARTTYPE_PIE_3D = 1;
	/* ˮƽƽ����״ͼ */
	private static final int CHARTTYPE_BAR_2D_H = 2;
	/* ��ֱƽ����״ͼ */
	private static final int CHARTTYPE_BAR_2D_V = 3;
	/* ˮƽ������״ͼ */
	private static final int CHARTTYPE_BAR_3D_H = 4;
	/* ��ֱ������״ͼ */
	private static final int CHARTTYPE_BAR_3D_V = 5;
	/* ƽ������ͼ */
	private static final int CHARTTYPE_LINE_2D = 6;
	/* ��������ͼ */
	private static final int CHARTTYPE_LINE_3D = 7;

	// ͼ������
	private int chartType;
	// ���ĸ��������ͳ��
	private Integer qid;
	/* ͼ�� */
	private JFreeChart chart;

	// ע��ͳ�Ʒ���
	@Resource
	private StatisticsService ss;

	/**
	 * ����ͼ������������
	 */
	public String execute() {
		buildChart();
		return SUCCESS;
	}

	@SuppressWarnings("deprecation")
	private void buildChart() {
		try {
			Font font = new Font("����", 0, 20);// ����
			QuestionStatisticsModel qsm = ss.statistics(qid);
			Question q = qsm.getQuestion();
			
			DefaultPieDataset pieds = null;// ��ͼ�����ݼ�
			DefaultCategoryDataset cateds = null;// �������ݼ�
			if(chartType < 2){
				pieds = new DefaultPieDataset();
				for (OptionStatisticsModel om : qsm.getOsms()) {
					pieds.setValue(om.getOptionLabel(), om.getCount());
				}
			}
			else{
				cateds = new DefaultCategoryDataset();
				for (OptionStatisticsModel osm : qsm.getOsms()) {
					cateds.addValue(osm.getCount(), osm.getOptionLabel(), "");
				}
			}
			
			// �ж�Ҫ���ͼ��
			switch (chartType) {
			case CHARTTYPE_PIE_2D:// ƽ���ͼ
				chart = ChartFactory.createPieChart(q.getTitle(), pieds, true, false, false);
				break ;
			case CHARTTYPE_PIE_3D:// �����ͼ
				chart = ChartFactory.createPieChart3D(qsm.getQuestion().getTitle(), pieds, true, true, true);
				break ;
			case CHARTTYPE_BAR_2D_H:// ƽ������ͼ
				chart = ChartFactory.createBarChart(qsm.getQuestion().getTitle(), "", "", cateds,
						PlotOrientation.HORIZONTAL, true, true, true);
				break ;
			case CHARTTYPE_BAR_2D_V:// ƽ������ͼ
				chart = ChartFactory.createBarChart(qsm.getQuestion().getTitle(), "", "", cateds,
						PlotOrientation.VERTICAL, true, true, true);
			case CHARTTYPE_BAR_3D_H:// ƽ������ͼ
				chart = ChartFactory.createBarChart3D(qsm.getQuestion().getTitle(), "", "", cateds,
						PlotOrientation.HORIZONTAL, true, true, true);
			case CHARTTYPE_BAR_3D_V:// ƽ������ͼ
				chart = ChartFactory.createBarChart3D(qsm.getQuestion().getTitle(), "", "", cateds,
						PlotOrientation.VERTICAL, true, true, true);
				break ;
			//
			case CHARTTYPE_LINE_2D:// ƽ������ͼ
				chart = ChartFactory.createLineChart(qsm.getQuestion().getTitle(), "", "", cateds,
						PlotOrientation.VERTICAL, true, true, true);
				break ;
			case CHARTTYPE_LINE_3D:// ƽ������ͼ
				chart = ChartFactory.createLineChart3D(qsm.getQuestion().getTitle(), "", "", cateds,
						PlotOrientation.HORIZONTAL, true, true, true);
				break ;
			}
			//���ñ������ʾ������
			chart.getTitle().setFont(font);
			chart.getLegend().setItemFont(font);
			//chart.setBackgroundImageAlpha(0.2f);
		
			//���ñ�ͼ��Ч
			if(chart.getPlot() instanceof PiePlot){
				PiePlot pieplot = (PiePlot) chart.getPlot();
				pieplot.setLabelFont(font);
				pieplot.setExplodePercent(0, 0.09);
				pieplot.setStartAngle(-15);
				pieplot.setDirection(Rotation.CLOCKWISE);
				pieplot.setNoDataMessage("No data to display");
				//pieplot.setForegroundAlpha(0.5f);
				//pieplot.setBackgroundImageAlpha(0.3f);
			}
			//���÷Ǳ�ͼЧ��
			else{
				chart.getCategoryPlot().getRangeAxis().setLabelFont(font);
				chart.getCategoryPlot().getRangeAxis().setTickLabelFont(font);
				chart.getCategoryPlot().getDomainAxis().setLabelFont(font);
				chart.getCategoryPlot().getDomainAxis().setTickLabelFont(font);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getChartType() {
		return chartType;
	}

	public void setChartType(int chartType) {
		this.chartType = chartType;
	}

	public Integer getQid() {
		return qid;
	}

	public void setQid(Integer qid) {
		this.qid = qid;
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}
}