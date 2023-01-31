package com.hdsoft.hdpay.Repositories;

import java.sql.Date;
import java.sql.Timestamp;

public class Users006 {
	private String SUBORGCODE;
	private String USERID;
	private String PREPASSWORD;
	private String CHANGED_BY;
	private Date CHGDATE;
	private Timestamp CHGTIME;
	public String getSUBORGCODE() {
		return SUBORGCODE;
	}
	public void setSUBORGCODE(String sUBORGCODE) {
		SUBORGCODE = sUBORGCODE;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getPREPASSWORD() {
		return PREPASSWORD;
	}
	public void setPREPASSWORD(String pREPASSWORD) {
		PREPASSWORD = pREPASSWORD;
	}
	public String getCHANGED_BY() {
		return CHANGED_BY;
	}
	public void setCHANGED_BY(String cHANGED_BY) {
		CHANGED_BY = cHANGED_BY;
	}
	public Date getCHGDATE() {
		return CHGDATE;
	}
	public void setCHGDATE(Date cHGDATE) {
		CHGDATE = cHGDATE;
	}
	public Timestamp getCHGTIME() {
		return CHGTIME;
	}
	public void setCHGTIME(Timestamp cHGTIME) {
		CHGTIME = cHGTIME;
	}
	
	
}
