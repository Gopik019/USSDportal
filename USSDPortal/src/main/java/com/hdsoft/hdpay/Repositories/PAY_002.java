package com.hdsoft.hdpay.Repositories;

public class PAY_002 
{
	private int ROWNUM;
	private String SUBORGCODE;     
	private String CHCODE;       
	private String PAYTYPE;      
	private String REQDATE;      
	private String REQREFNO;     
	private String TRANREFNO;    
	private String REQSL;        
	private String REQTIME;      
	private String PAYDATE;      
	private String TRANTYPE;     
	private String INITIATEBY;   
	private String ISREVERSED;   
	private String REV_DATE;     
	private String ORG_AMNT;     
	private String ORG_CURR;     
	private String ORG_CHRGAMNT; 
	private String ORG_CHRGCURR; 
	private String ORG_REFNO;    
	private String REV_AMOUNT;   
	private String REV_CURR;     
	private String REV_CHRGAMOUNT; 
	private String REV_CHRGCURR;   
	private String REV_REFNO;      
	private String REV_TYPE;       
	private String ISFINAL_REV;    
	private String REV_CODE;       
	private String REV_REASON;     
	private String ISHOLD_MARKED;  
	private String HOLD_AMOUNT;   
	private String HOLD_CURR ;     
	private String HOLD_REFNO;     
	private String HOLD_DATE;
	private String HOLD_REASON;
	private String REV_STATE;
	private String HOLD_STATE;
	private String PAYERID;
	private String PAYEEID;
	private String TRAN_SWITCHREF;
	private String REV_SWITCHREF;
	private String REV_STATUS;
	private String PAYER_REVREF;
	private String PAYEE_REVREF;
	private String ERRCD;
	private String ERRDES;
	private String ISHOLDAPPROVED;
	private String ISREVAPPROVED;
	private String HOLDREJREASON;
	private String REVREJREASON;
	
	public int getROWNUM() {
		return ROWNUM;
	}
	public void setROWNUM(int rOWNUM) {
		ROWNUM = rOWNUM;
	}
	public String getSUBORGCODE() {
		return SUBORGCODE;
	}
	public void setSUBORGCODE(String sUBORGCODE) {
		SUBORGCODE = sUBORGCODE;
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
	public String getTRAN_SWITCHREF() {
		return TRAN_SWITCHREF;
	}
	public void setTRAN_SWITCHREF(String tRAN_SWITCHREF) {
		TRAN_SWITCHREF = tRAN_SWITCHREF;
	}
	public String getREV_SWITCHREF() {
		return REV_SWITCHREF;
	}
	public void setREV_SWITCHREF(String rEV_SWITCHREF) {
		REV_SWITCHREF = rEV_SWITCHREF;
	}
	public String getREV_STATUS() {
		return REV_STATUS;
	}
	public void setREV_STATUS(String rEV_STATUS) {
		REV_STATUS = rEV_STATUS;
	}
	public String getPAYER_REVREF() {
		return PAYER_REVREF;
	}
	public void setPAYER_REVREF(String pAYER_REVREF) {
		PAYER_REVREF = pAYER_REVREF;
	}
	public String getPAYEE_REVREF() {
		return PAYEE_REVREF;
	}
	public void setPAYEE_REVREF(String pAYEE_REVREF) {
		PAYEE_REVREF = pAYEE_REVREF;
	}
	public String getPAYERID() {
		return PAYERID;
	}
	public void setPAYERID(String pAYERID) {
		PAYERID = pAYERID;
	}
	public String getPAYEEID() {
		return PAYEEID;
	}
	public void setPAYEEID(String pAYEEID) {
		PAYEEID = pAYEEID;
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
	public String getPAYDATE() {
		return PAYDATE;
	}
	public void setPAYDATE(String pAYDATE) {
		PAYDATE = pAYDATE;
	}
	public String getTRANTYPE() {
		return TRANTYPE;
	}
	public void setTRANTYPE(String tRANTYPE) {
		TRANTYPE = tRANTYPE;
	}
	public String getINITIATEBY() {
		return INITIATEBY;
	}
	public void setINITIATEBY(String iNITIATEBY) {
		INITIATEBY = iNITIATEBY;
	}
	public String getISREVERSED() {
		return ISREVERSED;
	}
	public void setISREVERSED(String iSREVERSED) {
		ISREVERSED = iSREVERSED;
	}
	public String getREV_DATE() {
		return REV_DATE;
	}
	public void setREV_DATE(String rEV_DATE) {
		REV_DATE = rEV_DATE;
	}
	public String getORG_AMNT() {
		return ORG_AMNT;
	}
	public void setORG_AMNT(String oRG_AMNT) {
		ORG_AMNT = oRG_AMNT;
	}
	public String getORG_CURR() {
		return ORG_CURR;
	}
	public void setORG_CURR(String oRG_CURR) {
		ORG_CURR = oRG_CURR;
	}
	public String getORG_CHRGAMNT() {
		return ORG_CHRGAMNT;
	}
	public void setORG_CHRGAMNT(String oRG_CHRGAMNT) {
		ORG_CHRGAMNT = oRG_CHRGAMNT;
	}
	public String getORG_CHRGCURR() {
		return ORG_CHRGCURR;
	}
	public void setORG_CHRGCURR(String oRG_CHRGCURR) {
		ORG_CHRGCURR = oRG_CHRGCURR;
	}
	public String getORG_REFNO() {
		return ORG_REFNO;
	}
	public void setORG_REFNO(String oRG_REFNO) {
		ORG_REFNO = oRG_REFNO;
	}
	public String getREV_AMOUNT() {
		return REV_AMOUNT;
	}
	public void setREV_AMOUNT(String rEV_AMOUNT) {
		REV_AMOUNT = rEV_AMOUNT;
	}
	public String getREV_CURR() {
		return REV_CURR;
	}
	public void setREV_CURR(String rEV_CURR) {
		REV_CURR = rEV_CURR;
	}
	public String getREV_CHRGAMOUNT() {
		return REV_CHRGAMOUNT;
	}
	public void setREV_CHRGAMOUNT(String rEV_CHRGAMOUNT) {
		REV_CHRGAMOUNT = rEV_CHRGAMOUNT;
	}
	public String getREV_CHRGCURR() {
		return REV_CHRGCURR;
	}
	public void setREV_CHRGCURR(String rEV_CHRGCURR) {
		REV_CHRGCURR = rEV_CHRGCURR;
	}
	public String getREV_REFNO() {
		return REV_REFNO;
	}
	public void setREV_REFNO(String rEV_REFNO) {
		REV_REFNO = rEV_REFNO;
	}
	public String getREV_TYPE() {
		return REV_TYPE;
	}
	public void setREV_TYPE(String rEV_TYPE) {
		REV_TYPE = rEV_TYPE;
	}
	public String getISFINAL_REV() {
		return ISFINAL_REV;
	}
	public void setISFINAL_REV(String iSFINAL_REV) {
		ISFINAL_REV = iSFINAL_REV;
	}
	public String getREV_CODE() {
		return REV_CODE;
	}
	public void setREV_CODE(String rEV_CODE) {
		REV_CODE = rEV_CODE;
	}
	public String getREV_REASON() {
		return REV_REASON;
	}
	public void setREV_REASON(String rEV_REASON) {
		REV_REASON = rEV_REASON;
	}
	public String getISHOLD_MARKED() {
		return ISHOLD_MARKED;
	}
	public void setISHOLD_MARKED(String iSHOLD_MARKED) {
		ISHOLD_MARKED = iSHOLD_MARKED;
	}
	public String getHOLD_AMOUNT() {
		return HOLD_AMOUNT;
	}
	public void setHOLD_AMOUNT(String hOLD_AMOUNT) {
		HOLD_AMOUNT = hOLD_AMOUNT;
	}
	public String getHOLD_CURR() {
		return HOLD_CURR;
	}
	public void setHOLD_CURR(String hOLD_CURR) {
		HOLD_CURR = hOLD_CURR;
	}
	public String getHOLD_REFNO() {
		return HOLD_REFNO;
	}
	public void setHOLD_REFNO(String hOLD_REFNO) {
		HOLD_REFNO = hOLD_REFNO;
	}
	public String getHOLD_DATE() {
		return HOLD_DATE;
	}
	public void setHOLD_DATE(String hOLD_DATE) {
		HOLD_DATE = hOLD_DATE;
	}
	public String getHOLD_REASON() {
		return HOLD_REASON;
	}
	public void setHOLD_REASON(String hOLD_REASON) {
		HOLD_REASON = hOLD_REASON;
	}
	public String getREV_STATE() {
		return REV_STATE;
	}
	public void setREV_STATE(String rEV_STATE) {
		REV_STATE = rEV_STATE;
	}
	public String getISHOLDAPPROVED() {
		return ISHOLDAPPROVED;
	}
	public void setISHOLDAPPROVED(String iSHOLDAPPROVED) {
		ISHOLDAPPROVED = iSHOLDAPPROVED;
	}
	public String getISREVAPPROVED() {
		return ISREVAPPROVED;
	}
	public void setISREVAPPROVED(String iSREVAPPROVED) {
		ISREVAPPROVED = iSREVAPPROVED;
	}
	public String getHOLDREJREASON() {
		return HOLDREJREASON;
	}
	public void setHOLDREJREASON(String hOLDREJREASON) {
		HOLDREJREASON = hOLDREJREASON;
	}
	public String getREVREJREASON() {
		return REVREJREASON;
	}
	public void setREVREJREASON(String rEVREJREASON) {
		REVREJREASON = rEVREJREASON;
	}
	public String getHOLD_STATE() {
		return HOLD_STATE;
	}
	public void setHOLD_STATE(String hOLD_STATE) {
		HOLD_STATE = hOLD_STATE;
	}
	public String getERRCD() {
		return ERRCD;
	}
	public void setERRCD(String eRRCD) {
		ERRCD = eRRCD;
	}
	public String getERRDES() {
		return ERRDES;
	}
	public void setERRDES(String eRRDES) {
		ERRDES = eRRDES;
	}
}