package com.hdsoft.hdpay.Repositories;

import java.sql.Timestamp;

public class Callback001 
{
	private String SUBORGCODE; 
	private String SYSCODE;    
	private String CHCODE;     
	private String PAYTYPE;    
	private String FLOW;       
	private String REQDATE;    
	private String REQREFNO;   
	private String TRANREFNO;  
	private String PAYDATE;   
	private String TRANTYPE;
	private String PAYSL;      
	private String REQSL;     
	private String REQTIME;    
	private Timestamp Timestamp;
	private String STATUS;     
	
	public Callback001() {};
	
	public Callback001(String sUBORGCODE, String sYSCODE, String cHCODE, String pAYTYPE, String fLOW, String rEQDATE,String rEQREFNO, String tRANREFNO, String pAYDATE, String tRANTYPE, String pAYSL, String rEQSL,Timestamp timestamp, String sTATUS) 
	{
		SUBORGCODE = sUBORGCODE;
		SYSCODE = sYSCODE;
		CHCODE = cHCODE;
		PAYTYPE = pAYTYPE;
		FLOW = fLOW;
		REQDATE = rEQDATE;
		REQREFNO = rEQREFNO;
		TRANREFNO = tRANREFNO;
		PAYDATE = pAYDATE;
		TRANTYPE = tRANTYPE;
		PAYSL = pAYSL;
		REQSL = rEQSL;
		Timestamp = timestamp;
		STATUS = sTATUS;
	}
	
	public String getSUBORGCODE() {
		return SUBORGCODE;
	}
	public void setSUBORGCODE(String sUBORGCODE) {
		SUBORGCODE = sUBORGCODE;
	}
	public String getSYSCODE() {
		return SYSCODE;
	}
	public void setSYSCODE(String sYSCODE) {
		SYSCODE = sYSCODE;
	}
	public String getCHCODE() {
		return CHCODE;
	}
	public String getTRANTYPE() {
		return TRANTYPE;
	}
	public void setTRANTYPE(String tRANTYPE) {
		TRANTYPE = tRANTYPE;
	}
	public void setCHCODE(String cHCODE) {
		CHCODE = cHCODE;
	}
	public Timestamp getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		Timestamp = timestamp;
	}

	public String getPAYTYPE() {
		return PAYTYPE;
	}
	public void setPAYTYPE(String pAYTYPE) {
		PAYTYPE = pAYTYPE;
	}
	public String getFLOW() {
		return FLOW;
	}
	public void setFLOW(String fLOW) {
		FLOW = fLOW;
	}
	public String getREQDATE() {
		return REQDATE;
	}
	public void setREQDATE(String rEQDATE) {
		REQDATE = rEQDATE;
	}
	public String getREQREFNO() {
		return REQREFNO;
	}
	public void setREQREFNO(String rEQREFNO) {
		REQREFNO = rEQREFNO;
	}
	public String getTRANREFNO() {
		return TRANREFNO;
	}
	public void setTRANREFNO(String tRANREFNO) {
		TRANREFNO = tRANREFNO;
	}
	public String getPAYDATE() {
		return PAYDATE;
	}
	public void setPAYDATE(String pAYDATE) {
		PAYDATE = pAYDATE;
	}
	public String getPAYSL() {
		return PAYSL;
	}
	public void setPAYSL(String pAYSL) {
		PAYSL = pAYSL;
	}
	public String getREQSL() {
		return REQSL;
	}
	public void setREQSL(String rEQSL) {
		REQSL = rEQSL;
	}
	public String getREQTIME() {
		return REQTIME;
	}
	public void setREQTIME(String rEQTIME) {
		REQTIME = rEQTIME;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	} 
}
