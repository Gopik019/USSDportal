package com.hdsoft.hdpay.models;

public class Token_Info 
{
	private String SUBORGCODE;
	private String CHCODE;
	private String MODULEID;
	private String CREATED_AT;
	private String TOKEN;
	
	public Token_Info(String sUBORGCODE, String cHCODE, String mODULEID, String tOKEN) 
	{
		SUBORGCODE = sUBORGCODE;
		CHCODE = cHCODE;
		MODULEID = mODULEID;
		TOKEN = tOKEN;
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
	public String getMODULEID() {
		return MODULEID;
	}
	public void setMODULEID(String mODULEID) {
		MODULEID = mODULEID;
	}
	public String getTOKEN() {
		return TOKEN;
	}
	public void setTOKEN(String tOKEN) {
		TOKEN = tOKEN;
	}

	public String getCREATED_AT() {
		return CREATED_AT;
	}

	public void setCREATED_AT(String cREATED_AT) {
		CREATED_AT = cREATED_AT;
	}
}
