package cn.itcast.surveypark.domain.statistics;

import java.io.Serializable;

/**
 * 选项统计模型
 */
public class OptionStatisticsModel implements Serializable{
	private static final long serialVersionUID = -2563039646716391673L;
	private String optionLabel ;
	private int optionIndex ;
	
	private String matrixRowTitleLabel ;
	private int matrixRowTitleIndex ;
	
	private String matrixColTitleLabel ;
	private int matrixColTitleIndex ;
	
	private String matrixSelectOptionLabel ;
	private int matrixSelectOptionIndex;
	//选项的选择人数
	private int count ;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getOptionLabel() {
		return optionLabel;
	}
	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}
	public int getOptionIndex() {
		return optionIndex;
	}
	public void setOptionIndex(int optionIndex) {
		this.optionIndex = optionIndex;
	}
	public String getMatrixRowTitleLabel() {
		return matrixRowTitleLabel;
	}
	public void setMatrixRowTitleLabel(String matrixRowTitleLabel) {
		this.matrixRowTitleLabel = matrixRowTitleLabel;
	}
	public int getMatrixRowTitleIndex() {
		return matrixRowTitleIndex;
	}
	public void setMatrixRowTitleIndex(int matrixRowTitleIndex) {
		this.matrixRowTitleIndex = matrixRowTitleIndex;
	}
	public String getMatrixColTitleLabel() {
		return matrixColTitleLabel;
	}
	public void setMatrixColTitleLabel(String matrixColTitleLabel) {
		this.matrixColTitleLabel = matrixColTitleLabel;
	}
	public int getMatrixColTitleIndex() {
		return matrixColTitleIndex;
	}
	public void setMatrixColTitleIndex(int matrixColTitleIndex) {
		this.matrixColTitleIndex = matrixColTitleIndex;
	}
	public String getMatrixSelectOptionLabel() {
		return matrixSelectOptionLabel;
	}
	public void setMatrixSelectOptionLabel(String matrixSelectOptionLabel) {
		this.matrixSelectOptionLabel = matrixSelectOptionLabel;
	}
	public int getMatrixSelectOptionIndex() {
		return matrixSelectOptionIndex;
	}
	public void setMatrixSelectOptionIndex(int matrixSelectOptionIndex) {
		this.matrixSelectOptionIndex = matrixSelectOptionIndex;
	}
}
