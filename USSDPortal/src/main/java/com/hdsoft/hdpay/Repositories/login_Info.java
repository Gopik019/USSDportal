package com.hdsoft.hdpay.Repositories;

public class login_Info 
{
	private String ULOGIN_SESSION_ID;
	private String ULOGIN_OUT_DATE;
	private String AUTO_LOGOUT_SECS;
	private String MULTIPLE_ALLOWED;
	private String SUBORGCODE;
	private String USER_ID;
	private String SESSION_ID;
	private String INVALID_COUNT;
	
	public String getULOGIN_SESSION_ID() {
		return ULOGIN_SESSION_ID;
	}
	public void setULOGIN_SESSION_ID(String uLOGIN_SESSION_ID) {
		ULOGIN_SESSION_ID = uLOGIN_SESSION_ID;
	}
	public String getULOGIN_OUT_DATE() {
		return ULOGIN_OUT_DATE;
	}
	public void setULOGIN_OUT_DATE(String uLOGIN_OUT_DATE) {
		ULOGIN_OUT_DATE = uLOGIN_OUT_DATE;
	}
	public String getAUTO_LOGOUT_SECS() {
		return AUTO_LOGOUT_SECS;
	}
	public void setAUTO_LOGOUT_SECS(String aUTO_LOGOUT_SECS) {
		AUTO_LOGOUT_SECS = aUTO_LOGOUT_SECS;
	}
	public String getINVALID_COUNT() {
		return INVALID_COUNT;
	}
	public void setINVALID_COUNT(String uPWDINV_INVALID_COUNT) {
		INVALID_COUNT = uPWDINV_INVALID_COUNT;
	}
	public String getMULTIPLE_ALLOWED() {
		return MULTIPLE_ALLOWED;
	}
	public void setMULTIPLE_ALLOWED(String mULTIPLE_ALLOWED) {
		MULTIPLE_ALLOWED = mULTIPLE_ALLOWED;
	}
	public String getSUBORGCODE() {
		return SUBORGCODE;
	}
	public void setSUBORGCODE(String sUBORGCODE) {
		SUBORGCODE = sUBORGCODE;
	}
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getSESSION_ID() {
		return SESSION_ID;
	}
	public void setSESSION_ID(String sESSION_ID) {
		SESSION_ID = sESSION_ID;
	}
}
