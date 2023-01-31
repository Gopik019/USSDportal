package com.hdsoft.hdpay.Repositories;

import java.sql.Timestamp;

public class Callback002 
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
	private String RESTIME;    
	private Timestamp Timestamp;
	private Timestamp Timestamp2;
	private String STATUS;     
	private String ERRCD;      
	private String ERRDESC;    
	private String RESCODE;    
	private String RESPDESC;   
	private String REMARKS;
	
	public Callback002() {};
	
	public Callback002(String sUBORGCODE, String sYSCODE, String cHCODE, String pAYTYPE, String fLOW, String rEQDATE,String rEQREFNO, String tRANREFNO, String pAYDATE, String tRANTYPE, String pAYSL, String rEQSL,
	Timestamp rEQTIME, Timestamp rESTIME, String sTATUS, String eRRCD, String eRRDESC, String rESCODE, String rESPDESC, String rEMARKS) 
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
		Timestamp2 = rEQTIME;
		Timestamp = rESTIME;
		STATUS = sTATUS;
		ERRCD = eRRCD;
		ERRDESC = eRRDESC;
		RESCODE = rESCODE;
		RESPDESC = rESPDESC;
		REMARKS = rEMARKS;
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
	public String getTRANTYPE() {
		return TRANTYPE;
	}
	public void setTRANTYPE(String tRANTYPE) {
		TRANTYPE = tRANTYPE;
	}
	public void setSYSCODE(String sYSCODE) {
		SYSCODE = sYSCODE;
	}
	public String getCHCODE() {
		return CHCODE;
	}
	public Timestamp getTimestamp2() {
		return Timestamp2;
	}

	public void setTimestamp2(Timestamp timestamp2) {
		Timestamp2 = timestamp2;
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
	public String getRESTIME() {
		return RESTIME;
	}
	public void setRESTIME(String rESTIME) {
		RESTIME = rESTIME;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getERRCD() {
		return ERRCD;
	}
	public void setERRCD(String eRRCD) {
		ERRCD = eRRCD;
	}
	public String getERRDESC() {
		return ERRDESC;
	}
	public void setERRDESC(String eRRDESC) {
		ERRDESC = eRRDESC;
	}
	public String getRESCODE() {
		return RESCODE;
	}
	public void setRESCODE(String rESCODE) {
		RESCODE = rESCODE;
	}
	public String getRESPDESC() {
		return RESPDESC;
	}
	public void setRESPDESC(String rESPDESC) {
		RESPDESC = rESPDESC;
	}
	public String getREMARKS() {
		return REMARKS;
	}
	public void setREMARKS(String rEMARKS) {
		REMARKS = rEMARKS;
	}
	public Timestamp getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		Timestamp = timestamp;
	}    
}
