package com.hdsoft.hdpay.Repositories;

import java.sql.Blob;
import java.sql.Date;

public class Users014 {//   , , , , 
	private String SUBORGCODE;
	private String USERID;
	private byte[] PHOTO;
	private Date UPLDTIME;
	
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
	public byte[] getPHOTO() {
		return PHOTO;
	}
	public void setPHOTO(byte[] pHOTO) {
		PHOTO = pHOTO;
	}
	public Date getUPLDTIME() {
		return UPLDTIME;
	}
	public void setUPLDTIME(Date uPLDTIME) {
		UPLDTIME = uPLDTIME;
	}
}
