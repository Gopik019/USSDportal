package com.hdsoft.hdpay.Repositories;

import org.springframework.jdbc.core.JdbcTemplate;

public class Thread_Store
{
	private String Thread_Id;
	private String Thread_Name;
	private String FREQINSEC;
	private String SYSCODE;
	private String EXECODE;
	private String JOBCODE;
	private String EXECMETHOD;
	private String STARTTIME;
	private String ENDTIME;
	private String METHODNAME;
	private String INPUTPARAM;
	private String OUTPUTPARAM;
	private String PARAMETER1;
	private String PARAMETER2;
	private JdbcTemplate Jdbctemplate;
	 
	public String getThread_Id() {
		return Thread_Id;
	}
	public String getSYSCODE() {
		return SYSCODE;
	}
	public String getEXECODE() {
		return EXECODE;
	}
	public void setEXECODE(String eXECODE) {
		EXECODE = eXECODE;
	}
	public void setSYSCODE(String sYSCODE) {
		SYSCODE = sYSCODE;
	}
	public void setThread_Id(String thread_Id) {
		Thread_Id = thread_Id;
	}
	public String getThread_Name() {
		return Thread_Name;
	}
	public void setThread_Name(String thread_Name) {
		Thread_Name = thread_Name;
	}
	public String getFREQINSEC() {
		return FREQINSEC;
	}
	public void setFREQINSEC(String fREQINSEC) {
		FREQINSEC = fREQINSEC;
	}
	public String getJOBCODE() {
		return JOBCODE;
	}
	public void setJOBCODE(String jOBCODE) {
		JOBCODE = jOBCODE;
	}
	public String getEXECMETHOD() {
		return EXECMETHOD;
	}
	public void setEXECMETHOD(String eXECMETHOD) {
		EXECMETHOD = eXECMETHOD;
	}
	public String getSTARTTIME() {
		return STARTTIME;
	}
	public void setSTARTTIME(String sTARTTIME) {
		STARTTIME = sTARTTIME;
	}
	public String getENDTIME() {
		return ENDTIME;
	}
	public void setENDTIME(String eNDTIME) {
		ENDTIME = eNDTIME;
	}
	public String getMETHODNAME() {
		return METHODNAME;
	}
	public void setMETHODNAME(String mETHODNAME) {
		METHODNAME = mETHODNAME;
	}
	public String getINPUTPARAM() {
		return INPUTPARAM;
	}
	public void setINPUTPARAM(String iNPUTPARAM) {
		INPUTPARAM = iNPUTPARAM;
	}
	public String getOUTPUTPARAM() {
		return OUTPUTPARAM;
	}
	public void setOUTPUTPARAM(String oUTPUTPARAM) {
		OUTPUTPARAM = oUTPUTPARAM;
	}
	public String getPARAMETER1() {
		return PARAMETER1;
	}
	public void setPARAMETER1(String pARAMETER1) {
		PARAMETER1 = pARAMETER1;
	}
	public String getPARAMETER2() {
		return PARAMETER2;
	}
	public void setPARAMETER2(String pARAMETER2) {
		PARAMETER2 = pARAMETER2;
	}
	public JdbcTemplate getJdbctemplate() {
		return Jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		Jdbctemplate = jdbctemplate;
	}
}
