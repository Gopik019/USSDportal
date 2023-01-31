package com.hdsoft.hdpay.Repositories;

public class Reversal_Info 
{
	private String SUBORGCODE;
	private String CHCODE;
	private String PAYTYPE;
	private String REQSL;
	private String FLOW;
	private String HOLD_REASON;
	private String HOLD_APPROVAL;
	private String HOLD_REJECT_REASON;
	private String REVERSAL_APPROVAL;
	private String REVERSAL_REJECT_REASON;
	private String PAYMENTS;
	private String TXNID;
	
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
	public String getREQSL() {
		return REQSL;
	}
	public void setREQSL(String rEQSL) {
		REQSL = rEQSL;
	}
	public String getFLOW() {
		return FLOW;
	}
	public void setFLOW(String fLOW) {
		FLOW = fLOW;
	}
	public String getHOLD_REASON() {
		return HOLD_REASON;
	}
	public void setHOLD_REASON(String hOLD_REASON) {
		HOLD_REASON = hOLD_REASON;
	}
	public String getHOLD_APPROVAL() {
		return HOLD_APPROVAL;
	}
	public void setHOLD_APPROVAL(String hOLD_APPROVAL) {
		HOLD_APPROVAL = hOLD_APPROVAL;
	}
	public String getHOLD_REJECT_REASON() {
		return HOLD_REJECT_REASON;
	}
	public void setHOLD_REJECT_REASON(String hOLD_REJECT_REASON) {
		HOLD_REJECT_REASON = hOLD_REJECT_REASON;
	}
	public String getREVERSAL_APPROVAL() {
		return REVERSAL_APPROVAL;
	}
	public void setREVERSAL_APPROVAL(String rEVERSAL_APPROVAL) {
		REVERSAL_APPROVAL = rEVERSAL_APPROVAL;
	}
	public String getREVERSAL_REJECT_REASON() {
		return REVERSAL_REJECT_REASON;
	}
	public void setREVERSAL_REJECT_REASON(String rEVERSAL_REJECT_REASON) {
		REVERSAL_REJECT_REASON = rEVERSAL_REJECT_REASON;
	}
	public String getPAYMENTS() {
		return PAYMENTS;
	}
	public void setPAYMENTS(String pAYMENTS) {
		PAYMENTS = pAYMENTS;
	}
	public String getTXNID() {
		return TXNID;
	}
	public void setTXNID(String tXNID) {
		TXNID = tXNID;
	}
}
