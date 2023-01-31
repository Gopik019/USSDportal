package com.hdsoft.hdpay.Repositories;

public class Channels_Store 
{
	private String SUBORGCODE;
	private String SYSCODE;
	private String CHCODE;
	private String CHNAME;
	private String STATUS;
	private String BUS_STARTIME;
	private String BUS_ENDTIME;
	private String AUTO_RECON;
	private String AMTLIMITREQ;
	private String USERID;
	private String HASHPWD;
	private String OAUTHVALREQ;
	private String PAYGATEWAYCD;
	private String PAYGATEWAYALLOWED;
	private String SECRETKEY;
	private String[] PAYGATECD;
	private String[] ALLOWED;
	
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
	public String getSECRETKEY() {
		return SECRETKEY;
	}
	public void setSECRETKEY(String sECRETKEY) {
		SECRETKEY = sECRETKEY;
	}
	public void setCHCODE(String cHCODE) {
		CHCODE = cHCODE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getBUS_STARTIME() {
		return BUS_STARTIME;
	}
	public String getCHNAME() {
		return CHNAME;
	}
	public void setCHNAME(String cHNAME) {
		CHNAME = cHNAME;
	}
	public String getPAYGATEWAYCD() {
		return PAYGATEWAYCD;
	}
	public void setPAYGATEWAYCD(String pAYGATEWAYCD) {
		PAYGATEWAYCD = pAYGATEWAYCD;
	}
	public String getPAYGATEWAYALLOWED() {
		return PAYGATEWAYALLOWED;
	}
	public void setPAYGATEWAYALLOWED(String pAYGATEWAYALLOWED) {
		PAYGATEWAYALLOWED = pAYGATEWAYALLOWED;
	}
	public void setBUS_STARTIME(String bUS_STARTIME) {
		BUS_STARTIME = bUS_STARTIME;
	}
	public String getBUS_ENDTIME() {
		return BUS_ENDTIME;
	}
	public void setBUS_ENDTIME(String bUS_ENDTIME) {
		BUS_ENDTIME = bUS_ENDTIME;
	}
	public String getAUTO_RECON() {
		return AUTO_RECON;
	}
	public void setAUTO_RECON(String aUTO_RECON) {
		AUTO_RECON = aUTO_RECON;
	}
	public String getAMTLIMITREQ() {
		return AMTLIMITREQ;
	}
	public void setAMTLIMITREQ(String aMTLIMITREQ) {
		AMTLIMITREQ = aMTLIMITREQ;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getHASHPWD() {
		return HASHPWD;
	}
	public void setHASHPWD(String hASHPWD) {
		HASHPWD = hASHPWD;
	}
	public String getOAUTHVALREQ() {
		return OAUTHVALREQ;
	}
	public void setOAUTHVALREQ(String oAUTHVALREQ) {
		OAUTHVALREQ = oAUTHVALREQ;
	}
	public String[] getPAYGATECD() {
		return PAYGATECD;
	}
	public void setPAYGATECD(String[] pAYGATECD) {
		PAYGATECD = pAYGATECD;
	}
	public String[] getALLOWED() {
		return ALLOWED;
	}
	public void setALLOWED(String[] aLLOWED) {
		ALLOWED = aLLOWED;
	}
}
