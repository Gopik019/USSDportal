package com.hdsoft.hdpay.Repositories;

public class Job_005 
{
	private String SUBORGCODE;
	private String SYSCODE;
	private String CHCODE;
	private String PAYTYPE;
	private String REQDATE;
	private String REFNO;
	private String REQSL;
	private String STATUS;
	private String Thread_Name;
	private String FREQINSEC;
	private String TRANTYPE;
	private String REASON;
	private String REV_REF;
	private String SERVCODE;
	
	public Job_005() { }
	
	public Job_005(String sUBORGCODE, String sYSCODE, String cHCODE, String pAYTYPE, String rEQDATE, String rEFNO,String rEQSL, String sTATUS, String tRANTYPE, String rEASON, String rEV_REF, String sERVCODE)
	{
		SUBORGCODE = sUBORGCODE;
		SYSCODE = sYSCODE;
		CHCODE = cHCODE;
		PAYTYPE = pAYTYPE;
		REQDATE = rEQDATE;
		REFNO = rEFNO;
		REQSL = rEQSL;
		STATUS = sTATUS;
		TRANTYPE = tRANTYPE;
		REASON = rEASON;
		REV_REF = rEV_REF;
		SERVCODE = sERVCODE;
	}
	
	public String getSUBORGCODE() {
		return SUBORGCODE;
	}
	public String getREV_REF() {
		return REV_REF;
	}

	public void setREV_REF(String rEV_REF) {
		REV_REF = rEV_REF;
	}

	public void setSUBORGCODE(String sUBORGCODE) {
		SUBORGCODE = sUBORGCODE;
	}
	public String getSYSCODE() {
		return SYSCODE;
	}
	public String getREASON() {
		return REASON;
	}

	public void setREASON(String rEASON) {
		REASON = rEASON;
	}

	public String getFREQINSEC() {
		return FREQINSEC;
	}

	public void setFREQINSEC(String fREQINSEC) {
		FREQINSEC = fREQINSEC;
	}

	public void setSYSCODE(String sYSCODE) {
		SYSCODE = sYSCODE;
	}
	public String getThread_Name() {
		return Thread_Name;
	}
	public void setThread_Name(String thread_Name) {
		Thread_Name = thread_Name;
	}

	public String getCHCODE() {
		return CHCODE;
	}
	public void setCHCODE(String cHCODE) {
		CHCODE = cHCODE;
	}
	public String getPAYTYPE() {
		return PAYTYPE;
	}
	public void setPAYTYPE(String pAYTYPE) {
		PAYTYPE = pAYTYPE;
	}
	public String getREQDATE() {
		return REQDATE;
	}
	public void setREQDATE(String rEQDATE) {
		REQDATE = rEQDATE;
	}
	public String getREFNO() {
		return REFNO;
	}
	public void setREFNO(String rEFNO) {
		REFNO = rEFNO;
	}
	public String getREQSL() {
		return REQSL;
	}
	public void setREQSL(String rEQSL) {
		REQSL = rEQSL;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getSERVCODE() {
		return SERVCODE;
	}

	public void setSERVCODE(String sERVCODE) {
		SERVCODE = sERVCODE;
	}

	public String getTRANTYPE() {
		return TRANTYPE;
	}

	public void setTRANTYPE(String tRANTYPE) {
		TRANTYPE = tRANTYPE;
	}
}
