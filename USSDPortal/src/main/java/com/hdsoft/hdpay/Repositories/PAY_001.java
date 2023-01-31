package com.hdsoft.hdpay.Repositories;

public class PAY_001 
{
	private int ROWNUM;
	private String SUBORGCODE;    
	private String CHCODE;      
	private String PAYTYPE;   
	private String FLOW;        
	private String TRANBRNCODE;
	private String REQDATE;
	private String REQREFNO;
	private String TRANREFNO;
	private String REQSL;
	private String REQTIME;
	private String PAYDATE;
	private String TRANTYPE;
	private String AMOUNTTYPE;
	private String REQTYPE;
	private String STATUS;
	private String ERRCD;
	private String RESCODE;
	private String TRANAMT;
	private String TRANCURR;
	private String SERPORCD;
	private String PAYERID;
	private String DEBITAMT;
	private String DEBITCURR;
	private String CREDITAMT;
	private String CREDITCURR;
	private String S_ACCOUNT;
	private String D_ACCOUNT;
	private String S_GLNO;
	private String D_GLNO;
	private String CHQEUENO;
	private String S_BANKBIC;
	private String D_BANKBIC;
	private String S_ACNAME;
	private String D_ACNAME;
	private String INITIATOR_CIF;
	private String S_CLIENTNO;
	private String D_CLIENTNO;
	private String FEEAMT1;
	private String FEECURR1;
	private String FEEAMT2;
	private String FEECURR2;
	private String FEEAMT3;
	private String FEECURR3;
	private String FEEAMT4;
	private String FEECURR4;
	private String INVOICENO;
	private String S_IBAN;
	private String D_IBAN;
	private String S_BRNCODE;
	private String D_BRNCODE;
	private String S_ACTYPE;
	private String D_ACTYPE;
	private String S_ADDRESS;
	private String D_ADDRESS;
	private String S_EMAILID;
	private String D_EMAILID;
	private String S_MOBILE;
	private String D_MOBILE;
	private String S_TELEPHONE;
	private String D_TELEPHONE;
	private String S_IDENTIFIERTYPE;
	private String D_IDENTIFIERTYPE;
	private String S_ACCATEGORY;
	private String D_ACCATEGORY;
	private String S_FSPID;
	private String D_FSPID;
	private String S_IDENTIFYTYPE;
	private String D_IDENTIFYTYPE;
	private String S_IDENTIFYVALUE;
	private String D_IDENTIFYVALUE;
	private String D_RECEIVERID;
	private String SENDER_INFO;
	private String RECEIVER_INFO;
	private String RECEIPTNO;
	private String PAYEEREF;
	private String PURPOSECD;
	private String RECONCILED;
	private String DEBIT_CREDIT;
	private String REMARKS;
	private String SWITCHREF;
	private String HOLD_STATUS;
	private String REVERSAL_STATUS;
	
	public String getSUBORGCODE() {
		return SUBORGCODE;
	}
	public void setSUBORGCODE(String sUBORGCODE) {
		SUBORGCODE = sUBORGCODE;
	}
	public String getCHCODE() {
		return CHCODE;
	}
	public String getRECEIPTNO() {
		return RECEIPTNO;
	}
	public String getHOLD_STATUS() {
		return HOLD_STATUS;
	}
	public void setHOLD_STATUS(String hOLD_STATUS) {
		HOLD_STATUS = hOLD_STATUS;
	}
	public String getREVERSAL_STATUS() {
		return REVERSAL_STATUS;
	}
	public void setREVERSAL_STATUS(String rEVERSAL_STATUS) {
		REVERSAL_STATUS = rEVERSAL_STATUS;
	}
	public void setRECEIPTNO(String rECEIPTNO) {
		RECEIPTNO = rECEIPTNO;
	}
	public int getROWNUM() {
		return ROWNUM;
	}
	public String getSWITCHREF() {
		return SWITCHREF;
	}
	public void setSWITCHREF(String sWITCHREF) {
		SWITCHREF = sWITCHREF;
	}
	public void setROWNUM(int rOWNUM) {
		ROWNUM = rOWNUM;
	}
	public String getREMARKS() {
		return REMARKS;
	}
	public void setREMARKS(String rEMARKS) {
		REMARKS = rEMARKS;
	}
	public void setCHCODE(String cHCODE) {
		CHCODE = cHCODE;
	}
	public String getS_IDENTIFIERTYPE() {
		return S_IDENTIFIERTYPE;
	}
	public void setS_IDENTIFIERTYPE(String s_IDENTIFIERTYPE) {
		S_IDENTIFIERTYPE = s_IDENTIFIERTYPE;
	}
	public String getD_IDENTIFIERTYPE() {
		return D_IDENTIFIERTYPE;
	}
	public void setD_IDENTIFIERTYPE(String d_IDENTIFIERTYPE) {
		D_IDENTIFIERTYPE = d_IDENTIFIERTYPE;
	}
	public String getS_ACCATEGORY() {
		return S_ACCATEGORY;
	}
	public void setS_ACCATEGORY(String s_ACCATEGORY) {
		S_ACCATEGORY = s_ACCATEGORY;
	}
	public String getD_ACCATEGORY() {
		return D_ACCATEGORY;
	}
	public void setD_ACCATEGORY(String d_ACCATEGORY) {
		D_ACCATEGORY = d_ACCATEGORY;
	}
	public String getS_FSPID() {
		return S_FSPID;
	}
	public void setS_FSPID(String s_FSPID) {
		S_FSPID = s_FSPID;
	}
	public String getD_FSPID() {
		return D_FSPID;
	}
	public void setD_FSPID(String d_FSPID) {
		D_FSPID = d_FSPID;
	}
	public String getS_IDENTIFYTYPE() {
		return S_IDENTIFYTYPE;
	}
	public void setS_IDENTIFYTYPE(String s_IDENTIFYTYPE) {
		S_IDENTIFYTYPE = s_IDENTIFYTYPE;
	}
	public String getD_IDENTIFYTYPE() {
		return D_IDENTIFYTYPE;
	}
	public void setD_IDENTIFYTYPE(String d_IDENTIFYTYPE) {
		D_IDENTIFYTYPE = d_IDENTIFYTYPE;
	}
	public String getS_IDENTIFYVALUE() {
		return S_IDENTIFYVALUE;
	}
	public void setS_IDENTIFYVALUE(String s_IDENTIFYVALUE) {
		S_IDENTIFYVALUE = s_IDENTIFYVALUE;
	}
	public String getD_IDENTIFYVALUE() {
		return D_IDENTIFYVALUE;
	}
	public void setD_IDENTIFYVALUE(String d_IDENTIFYVALUE) {
		D_IDENTIFYVALUE = d_IDENTIFYVALUE;
	}
	public String getD_RECEIVERID() {
		return D_RECEIVERID;
	}
	public void setD_RECEIVERID(String d_RECEIVERID) {
		D_RECEIVERID = d_RECEIVERID;
	}
	public String getPAYTYPE() {
		return PAYTYPE;
	}
	public void setPAYTYPE(String pAYTYPE) {
		PAYTYPE = pAYTYPE;
	}
	public String getFLOW() {
		return FLOW;
	}
	public void setFLOW(String fLOW) {
		FLOW = fLOW;
	}
	public String getTRANBRNCODE() {
		return TRANBRNCODE;
	}
	public void setTRANBRNCODE(String tRANBRNCODE) {
		TRANBRNCODE = tRANBRNCODE;
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
	public String getAMOUNTTYPE() {
		return AMOUNTTYPE;
	}
	public void setAMOUNTTYPE(String aMOUNTTYPE) {
		AMOUNTTYPE = aMOUNTTYPE;
	}
	public String getREQTYPE() {
		return REQTYPE;
	}
	public void setREQTYPE(String rEQTYPE) {
		REQTYPE = rEQTYPE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getERRCD() {
		return ERRCD;
	}
	public void setERRCD(String eRRCD) {
		ERRCD = eRRCD;
	}
	public String getRESCODE() {
		return RESCODE;
	}
	public void setRESCODE(String rESCODE) {
		RESCODE = rESCODE;
	}
	public String getTRANAMT() {
		return TRANAMT;
	}
	public void setTRANAMT(String tRANAMT) {
		TRANAMT = tRANAMT;
	}
	public String getTRANCURR() {
		return TRANCURR;
	}
	public void setTRANCURR(String tRANCURR) {
		TRANCURR = tRANCURR;
	}
	public String getSERPORCD() {
		return SERPORCD;
	}
	public void setSERPORCD(String sERPORCD) {
		SERPORCD = sERPORCD;
	}
	public String getPAYERID() {
		return PAYERID;
	}
	public void setPAYERID(String pAYERID) {
		PAYERID = pAYERID;
	}
	public String getDEBITAMT() {
		return DEBITAMT;
	}
	public void setDEBITAMT(String dEBITAMT) {
		DEBITAMT = dEBITAMT;
	}
	public String getDEBITCURR() {
		return DEBITCURR;
	}
	public void setDEBITCURR(String dEBITCURR) {
		DEBITCURR = dEBITCURR;
	}
	public String getCREDITAMT() {
		return CREDITAMT;
	}
	public void setCREDITAMT(String cREDITAMT) {
		CREDITAMT = cREDITAMT;
	}
	public String getCREDITCURR() {
		return CREDITCURR;
	}
	public void setCREDITCURR(String cREDITCURR) {
		CREDITCURR = cREDITCURR;
	}
	public String getS_ACCOUNT() {
		return S_ACCOUNT;
	}
	public void setS_ACCOUNT(String s_ACCOUNT) {
		S_ACCOUNT = s_ACCOUNT;
	}
	public String getD_ACCOUNT() {
		return D_ACCOUNT;
	}
	public void setD_ACCOUNT(String d_ACCOUNT) {
		D_ACCOUNT = d_ACCOUNT;
	}
	public String getS_GLNO() {
		return S_GLNO;
	}
	public void setS_GLNO(String s_GLNO) {
		S_GLNO = s_GLNO;
	}
	public String getD_GLNO() {
		return D_GLNO;
	}
	public void setD_GLNO(String d_GLNO) {
		D_GLNO = d_GLNO;
	}
	public String getCHQEUENO() {
		return CHQEUENO;
	}
	public void setCHQEUENO(String cHQEUENO) {
		CHQEUENO = cHQEUENO;
	}
	public String getS_BANKBIC() {
		return S_BANKBIC;
	}
	public void setS_BANKBIC(String s_BANKBIC) {
		S_BANKBIC = s_BANKBIC;
	}
	public String getD_BANKBIC() {
		return D_BANKBIC;
	}
	public void setD_BANKBIC(String d_BANKBIC) {
		D_BANKBIC = d_BANKBIC;
	}
	public String getS_ACNAME() {
		return S_ACNAME;
	}
	public void setS_ACNAME(String s_ACNAME) {
		S_ACNAME = s_ACNAME;
	}
	public String getD_ACNAME() {
		return D_ACNAME;
	}
	public void setD_ACNAME(String d_ACNAME) {
		D_ACNAME = d_ACNAME;
	}
	public String getINITIATOR_CIF() {
		return INITIATOR_CIF;
	}
	public void setINITIATOR_CIF(String iNITIATOR_CIF) {
		INITIATOR_CIF = iNITIATOR_CIF;
	}
	public String getS_CLIENTNO() {
		return S_CLIENTNO;
	}
	public void setS_CLIENTNO(String s_CLIENTNO) {
		S_CLIENTNO = s_CLIENTNO;
	}
	public String getD_CLIENTNO() {
		return D_CLIENTNO;
	}
	public void setD_CLIENTNO(String d_CLIENTNO) {
		D_CLIENTNO = d_CLIENTNO;
	}
	public String getFEEAMT1() {
		return FEEAMT1;
	}
	public void setFEEAMT1(String fEEAMT1) {
		FEEAMT1 = fEEAMT1;
	}
	public String getFEECURR1() {
		return FEECURR1;
	}
	public void setFEECURR1(String fEECURR1) {
		FEECURR1 = fEECURR1;
	}
	public String getFEEAMT2() {
		return FEEAMT2;
	}
	public void setFEEAMT2(String fEEAMT2) {
		FEEAMT2 = fEEAMT2;
	}
	public String getFEECURR2() {
		return FEECURR2;
	}
	public void setFEECURR2(String fEECURR2) {
		FEECURR2 = fEECURR2;
	}
	public String getFEEAMT3() {
		return FEEAMT3;
	}
	public void setFEEAMT3(String fEEAMT3) {
		FEEAMT3 = fEEAMT3;
	}
	public String getFEECURR3() {
		return FEECURR3;
	}
	public void setFEECURR3(String fEECURR3) {
		FEECURR3 = fEECURR3;
	}
	public String getFEEAMT4() {
		return FEEAMT4;
	}
	public void setFEEAMT4(String fEEAMT4) {
		FEEAMT4 = fEEAMT4;
	}
	public String getFEECURR4() {
		return FEECURR4;
	}
	public void setFEECURR4(String fEECURR4) {
		FEECURR4 = fEECURR4;
	}
	public String getINVOICENO() {
		return INVOICENO;
	}
	public void setINVOICENO(String iNVOICENO) {
		INVOICENO = iNVOICENO;
	}
	public String getS_IBAN() {
		return S_IBAN;
	}
	public void setS_IBAN(String s_IBAN) {
		S_IBAN = s_IBAN;
	}
	public String getD_IBAN() {
		return D_IBAN;
	}
	public void setD_IBAN(String d_IBAN) {
		D_IBAN = d_IBAN;
	}
	public String getS_BRNCODE() {
		return S_BRNCODE;
	}
	public void setS_BRNCODE(String s_BRNCODE) {
		S_BRNCODE = s_BRNCODE;
	}
	public String getD_BRNCODE() {
		return D_BRNCODE;
	}
	public void setD_BRNCODE(String d_BRNCODE) {
		D_BRNCODE = d_BRNCODE;
	}
	public String getS_ACTYPE() {
		return S_ACTYPE;
	}
	public void setS_ACTYPE(String s_ACTYPE) {
		S_ACTYPE = s_ACTYPE;
	}
	public String getD_ACTYPE() {
		return D_ACTYPE;
	}
	public void setD_ACTYPE(String d_ACTYPE) {
		D_ACTYPE = d_ACTYPE;
	}
	public String getS_ADDRESS() {
		return S_ADDRESS;
	}
	public void setS_ADDRESS(String s_ADDRESS) {
		S_ADDRESS = s_ADDRESS;
	}
	public String getD_ADDRESS() {
		return D_ADDRESS;
	}
	public void setD_ADDRESS(String d_ADDRESS) {
		D_ADDRESS = d_ADDRESS;
	}
	public String getS_EMAILID() {
		return S_EMAILID;
	}
	public void setS_EMAILID(String s_EMAILID) {
		S_EMAILID = s_EMAILID;
	}
	public String getD_EMAILID() {
		return D_EMAILID;
	}
	public void setD_EMAILID(String d_EMAILID) {
		D_EMAILID = d_EMAILID;
	}
	public String getS_MOBILE() {
		return S_MOBILE;
	}
	public void setS_MOBILE(String s_MOBILE) {
		S_MOBILE = s_MOBILE;
	}
	public String getD_MOBILE() {
		return D_MOBILE;
	}
	public void setD_MOBILE(String d_MOBILE) {
		D_MOBILE = d_MOBILE;
	}
	public String getS_TELEPHONE() {
		return S_TELEPHONE;
	}
	public void setS_TELEPHONE(String s_TELEPHONE) {
		S_TELEPHONE = s_TELEPHONE;
	}
	public String getD_TELEPHONE() {
		return D_TELEPHONE;
	}
	public void setD_TELEPHONE(String d_TELEPHONE) {
		D_TELEPHONE = d_TELEPHONE;
	}
	public String getSENDER_INFO() {
		return SENDER_INFO;
	}
	public void setSENDER_INFO(String sENDER_INFO) {
		SENDER_INFO = sENDER_INFO;
	}
	public String getRECEIVER_INFO() {
		return RECEIVER_INFO;
	}
	public void setRECEIVER_INFO(String rECEIVER_INFO) {
		RECEIVER_INFO = rECEIVER_INFO;
	}
	public String getPAYEEREF() {
		return PAYEEREF;
	}
	public void setPAYEEREF(String pAYEEREF) {
		PAYEEREF = pAYEEREF;
	}
	public String getPURPOSECD() {
		return PURPOSECD;
	}
	public void setPURPOSECD(String pURPOSECD) {
		PURPOSECD = pURPOSECD;
	}
	public String getRECONCILED() {
		return RECONCILED;
	}
	public void setRECONCILED(String rECONCILED) {
		RECONCILED = rECONCILED;
	}
	public String getDEBIT_CREDIT() {
		return DEBIT_CREDIT;
	}
	public void setDEBIT_CREDIT(String dEBIT_CREDIT) {
		DEBIT_CREDIT = dEBIT_CREDIT;
	}
}