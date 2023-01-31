package com.hdsoft.hdpay.Repositories;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Request_001 
{
	private int ROWNUM;
	private String SUBORGCODE;
	private String CHCODE;
	private String PAYTYPE;
	private String MSGTYPE;
	private String FLOW;
	private String REQDATE;
	private String REQTIME;
	private Timestamp Timestamp;
	private String UNIREFNO;
	private String MSGURL;
	private String IP;
	private String PORT;
	private String HEAD_MSG;
	private String BODY_MSG;
	private String REQBY;
	private String HASHVAL;
	
	public Request_001() { }
	
	public Request_001(String sUBORGCODE, String cHCODE, String pAYTYPE, String mSGTYPE, String fLOW, String rEQDATE, Timestamp timestamp,String uNIREFNO, String mSGURL, String iP, String pORT, String hEAD_MSG, String bODY_MSG, String rEQBY,String hASHVAL) 
	{
		SUBORGCODE = sUBORGCODE;
		CHCODE = cHCODE;
		PAYTYPE = pAYTYPE;
		MSGTYPE = mSGTYPE;
		FLOW = fLOW;
		REQDATE = rEQDATE;
		Timestamp = timestamp;
		UNIREFNO = uNIREFNO;
		MSGURL = mSGURL;
		IP = iP;
		PORT = pORT;
		HEAD_MSG = hEAD_MSG;
		BODY_MSG = bODY_MSG;
		REQBY = rEQBY;
		HASHVAL = hASHVAL;
	}
	
	public Request_001(String sUBORGCODE, String cHCODE, String pAYTYPE, String mSGTYPE, String fLOW, String uNIREFNO, String mSGURL, String iP, String pORT, String hEAD_MSG, String bODY_MSG, String rEQBY,String hASHVAL) 
	{
		SUBORGCODE = sUBORGCODE;
		CHCODE = cHCODE;
		PAYTYPE = pAYTYPE;
		MSGTYPE = mSGTYPE;
		FLOW = fLOW;
		REQDATE = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(System.currentTimeMillis());
		Timestamp = new java.sql.Timestamp(new java.util.Date().getTime());
		UNIREFNO = uNIREFNO;
		MSGURL = mSGURL;
		IP = iP;
		PORT = pORT;
		HEAD_MSG = hEAD_MSG;
		BODY_MSG = bODY_MSG;
		REQBY = rEQBY;
		HASHVAL = hASHVAL;
	}
	
	public Request_001(String sUBORGCODE, String cHCODE, String pAYTYPE, String mSGTYPE, String fLOW, String Req_date, String uNIREFNO, String mSGURL, String iP, String pORT, String hEAD_MSG, String bODY_MSG, String rEQBY,String hASHVAL) 
	{
		SUBORGCODE = sUBORGCODE;
		CHCODE = cHCODE;
		PAYTYPE = pAYTYPE;
		MSGTYPE = mSGTYPE;
		FLOW = fLOW;
		REQDATE = Req_date;
		Timestamp = new java.sql.Timestamp(new java.util.Date().getTime());
		UNIREFNO = uNIREFNO;
		MSGURL = mSGURL;
		IP = iP;
		PORT = pORT;
		HEAD_MSG = hEAD_MSG;
		BODY_MSG = bODY_MSG;
		REQBY = rEQBY;
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
	public void setCHCODE(String cHCODE) {
		CHCODE = cHCODE;
	}
	public String getPAYTYPE() {
		return PAYTYPE;
	}

	public void setPAYTYPE(String pAYTYPE) {
		PAYTYPE = pAYTYPE;
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
	public String getREQDATE() {
		return REQDATE;
	}
	public void setREQDATE(String rEQDATE) {
		REQDATE = rEQDATE;
	}
	public String getREQTIME() {
		return REQTIME;
	}
	public void setREQTIME(String rEQTIME) {
		REQTIME = rEQTIME;
	}
	public String getUNIREFNO() {
		return UNIREFNO;
	}
	public int getROWNUM() {
		return ROWNUM;
	}
	public void setROWNUM(int rOWNUM) {
		ROWNUM = rOWNUM;
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
	public String getREQBY() {
		return REQBY;
	}
	public void setREQBY(String rEQBY) {
		REQBY = rEQBY;
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
