package com.hdsoft.other.Repositories;

public class Report_Params 
{
	private String Procedure_Name;
	private String[] Inputs_labels;
	private String[] Inputs_values;
	private String[] Inputs_types;
	private String REPORTSL;
	private String Total_Columns;
	private String MESSAGE;
	
	public String getProcedure_Name() {
		return Procedure_Name;
	}
	public void setProcedure_Name(String procedure_Name) {
		Procedure_Name = procedure_Name;
	}
	public String[] getInputs_labels() {
		return Inputs_labels;
	}
	public String[] getInputs_types() {
		return Inputs_types;
	}
	public void setInputs_types(String[] inputs_types) {
		Inputs_types = inputs_types;
	}
	public void setInputs_labels(String[] inputs_labels) {
		Inputs_labels = inputs_labels;
	}
	public String getTotal_Columns() {
		return Total_Columns;
	}
	public void setTotal_Columns(String total_Columns) {
		Total_Columns = total_Columns;
	}
	public String[] getInputs_values() {
		return Inputs_values;
	}
	public void setInputs_values(String[] inputs_values) {
		Inputs_values = inputs_values;
	}
	public String getREPORTSL() {
		return REPORTSL;
	}
	public void setREPORTSL(String rEPORTSL) {
		REPORTSL = rEPORTSL;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
}
