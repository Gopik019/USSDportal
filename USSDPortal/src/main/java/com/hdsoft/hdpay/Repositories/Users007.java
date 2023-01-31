package com.hdsoft.hdpay.Repositories;

import java.sql.Date;
import java.sql.Timestamp;

public class Users007 {
	private String SUBORGCODE;
	private String USERID;
	private Date FORGOTINTIDATE;
	private Timestamp CHGTIME;
	private int OTPFAILCOUNT;
	private String VSTATUS;
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
	public Date getFORGOTINTIDATE() {
		return FORGOTINTIDATE;
	}
	public void setFORGOTINTIDATE(Date fORGOTINTIDATE) {
		FORGOTINTIDATE = fORGOTINTIDATE;
	}
	public Timestamp getCHGTIME() {
		return CHGTIME;
	}
	public void setCHGTIME(Timestamp cHGTIME) {
		CHGTIME = cHGTIME;
	}
	public int getOTPFAILCOUNT() {
		return OTPFAILCOUNT;
	}
	public void setOTPFAILCOUNT(int oTPFAILCOUNT) {
		OTPFAILCOUNT = oTPFAILCOUNT;
	}
	public String getVSTATUS() {
		return VSTATUS;
	}
	public void setVSTATUS(String vSTATUS) {
		VSTATUS = vSTATUS;
	}
	
	
}
