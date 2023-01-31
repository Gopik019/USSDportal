package com.hdsoft.hdpay.Repositories;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Response_001 
{
	private String SUBORGCODE;
	private String CHCODE;
	private String PAYTYPE;
	private String MSGTYPE;
	private String FLOW;
	private String RESDATE;
	private String RESTIME;
	private Timestamp Timestamp;
	private String UNIREFNO;
	private String MSGURL;
	private String IP;
	private String PORT;
	private String HEAD_MSG;
	private String BODY_MSG;
	private String RESBY;
	private String HASHVAL;
	
	public Response_001()
	{
		
	}
	
	public Response_001(String sUBORGCODE, String cHCODE, String pAYTYPE, String mSGTYPE, String fLOW, String rESDATE, Timestamp timestamp, String uNIREFNO, String mSGURL, String iP, String pORT, String hEAD_MSG, String bODY_MSG, String rESBY,String hASHVAL) 
	{
		SUBORGCODE = sUBORGCODE;
		CHCODE = cHCODE;
		PAYTYPE = pAYTYPE;
		MSGTYPE = mSGTYPE;
		FLOW = fLOW;
		RESDATE = rESDATE;
		Timestamp = timestamp;
		UNIREFNO = uNIREFNO;
		MSGURL = mSGURL;
		IP = iP;
		PORT = pORT;
		HEAD_MSG = hEAD_MSG;
		BODY_MSG = bODY_MSG;
		RESBY = rESBY;
		HASHVAL = hASHVAL;
	}
	
	/*** Auto Generate REQDATE & RESTIME ***/
	
	public Response_001(String sUBORGCODE, String cHCODE, String pAYTYPE, String mSGTYPE, String fLOW, String uNIREFNO, String mSGURL, String iP, String pORT, String hEAD_MSG, String bODY_MSG, String rESBY,String hASHVAL) 
	{
		SUBORGCODE = sUBORGCODE;
		CHCODE = cHCODE;
		PAYTYPE = pAYTYPE;
		MSGTYPE = mSGTYPE;
		FLOW = fLOW;
		RESDATE = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(System.currentTimeMillis());
		Timestamp = new java.sql.Timestamp(new java.util.Date().getTime());
		UNIREFNO = uNIREFNO;
		MSGURL = mSGURL;
		IP = iP;
		PORT = pORT;
		HEAD_MSG = hEAD_MSG;
		BODY_MSG = bODY_MSG;
		RESBY = rESBY;
		HASHVAL = hASHVAL;
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
	public String getPAYTYPE() {
		return PAYTYPE;
	}

	public void setPAYTYPE(String pAYTYPE) {
		PAYTYPE = pAYTYPE;
	}

	public void setCHCODE(String cHCODE) {
		CHCODE = cHCODE;
	}
	public String getMSGTYPE() {
		return MSGTYPE;
	}
	public void setMSGTYPE(String mSGTYPE) {
		MSGTYPE = mSGTYPE;
	}
	public String getFLOW() {
		return FLOW;
	}
	public void setFLOW(String fLOW) {
		FLOW = fLOW;
	}
	public String getRESDATE() {
		return RESDATE;
	}
	public void setRESDATE(String rESDATE) {
		RESDATE = rESDATE;
	}
	public String getRESTIME() {
		return RESTIME;
	}
	public void setRESTIME(String rESTIME) {
		RESTIME = rESTIME;
	}
	public String getUNIREFNO() {
		return UNIREFNO;
	}
	public void setUNIREFNO(String uNIREFNO) {
		UNIREFNO = uNIREFNO;
	}
	public String getMSGURL() {
		return MSGURL;
	}
	public void setMSGURL(String mSGURL) {
		MSGURL = mSGURL;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getPORT() {
		return PORT;
	}
	public void setPORT(String pORT) {
		PORT = pORT;
	}
	public String getHEAD_MSG() {
		return HEAD_MSG;
	}
	public void setHEAD_MSG(String hEAD_MSG) {
		HEAD_MSG = hEAD_MSG;
	}
	public String getBODY_MSG() {
		return BODY_MSG;
	}
	public void setBODY_MSG(String bODY_MSG) {
		BODY_MSG = bODY_MSG;
	}
	public String getRESBY() {
		return RESBY;
	}
	public void setRESBY(String rESBY) {
		RESBY = rESBY;
	}
	public String getHASHVAL() {
		return HASHVAL;
	}
	public void setHASHVAL(String hASHVAL) {
		HASHVAL = hASHVAL;
	}

	public Timestamp getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		Timestamp = timestamp;
	}
}
