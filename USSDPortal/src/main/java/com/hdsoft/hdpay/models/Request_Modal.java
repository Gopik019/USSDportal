package com.hdsoft.hdpay.models;

import java.sql.Timestamp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.hdsoft.hdpay.Repositories.Callback001;
import com.hdsoft.hdpay.Repositories.Callback002;
import com.hdsoft.hdpay.Repositories.Job_005;
import com.hdsoft.hdpay.Repositories.PAY_001;
import com.hdsoft.hdpay.Repositories.PAY_002;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Request_002;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Request_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	@Autowired
	public Webservice_Modal Ws;
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Insert_Request_001(Request_001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into request001(SUBORGCODE,CHCODE,PAYTYPE,MSGTYPE,FLOW,REQDATE,REQTIME,UNIREFNO,MSGURL,IP,PORT,HEAD_MSG,BODY_MSG,REQBY,HASHVAL) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getMSGTYPE(), Info.getFLOW(), Info.getREQDATE(), Info.getTimestamp(), Info.getUNIREFNO(), 
					 Info.getMSGURL(),  Info.getIP(), Info.getPORT(), Info.getHEAD_MSG(), Info.getBODY_MSG(), Info.getREQBY(), Info.getHASHVAL()});
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Request added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Request_001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Insert_Request_001(String SUBORGCODE, String CHCODE, String PAYTYPE, String MSGTYPE, String FLOW, String REQDATE, Timestamp REQTIME, String UNIREFNO, String MSGURL, String IP, String PORT, String HEAD_MSG, String BODY_MSG, String INITATEDBY, String Checksum) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into request001(SUBORGCODE,CHCODE,PAYTYPE,MSGTYPE,FLOW,REQDATE,REQTIME,UNIREFNO,MSGURL,IP,PORT,HEAD_MSG,BODY_MSG,REQBY,HASHVAL) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { SUBORGCODE,CHCODE,PAYTYPE,MSGTYPE,FLOW,REQDATE,REQTIME,UNIREFNO,MSGURL,IP,PORT,HEAD_MSG,BODY_MSG,INITATEDBY,Checksum });
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Request added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Request_001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Insert_Job_005(Job_005 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into job005(SUBORGCODE,SYSCODE,CHCODE,PAYTYPE,TRANTYPE,REASON,REQDATE,REFNO,REQSL,STATUS,REV_REF,SERVCODE) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getSYSCODE(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getTRANTYPE(), 
					 Info.getREASON(), Info.getREQDATE(), Info.getREFNO(), Info.getREQSL(),  Info.getSTATUS(), Info.getREV_REF(), Info.getSERVCODE() });
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Job added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Job_005 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Insert_Request_002(Request_002 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into request002(SUBORGCODE,SYSCODE,CHCODE,PAYTYPE,FLOW,REQDATE,REQREFNO,TRANREFNO,REQSL,REQTIME,PAYDATE,STATUS,TRANAMT,"+
				 	"TRANCURR,REQTYPE,SERPORCD,DEBITAMT,DEBITCURR,CREDITAMT,CREDITCURR,S_ACCOUNT,D_ACCOUNT,S_BANKBIC,D_BANKBIC,S_ACNAME,D_ACNAME,INITIATOR_CIF,S_CLIENTNO,"+
					"D_CLIENTNO,FEEAMT1,FEECURR1,FEEAMT2,FEECURR2,FEEAMT3,FEECURR3,FEEAMT4,FEECURR4,INVOICENO,S_IBAN,D_IBAN,S_BRNCODE,D_BRNCODE,S_ACTYPE,D_ACTYPE,S_ADDRESS,"+
				 	"D_ADDRESS,S_EMAILID,D_EMAILID,S_MOBILE,D_MOBILE,S_TELEPHONE,D_TELEPHONE,SENDER_INFO,RECEIVER_INFO,PAYEEREF,LONGITUDE,LATITUDE,IPADDRESS,DEVICEID,"+
					"LOCATION,PURPOSECD,PURSPOSEDESC,OTPPASSED,USERAGENT,SESSIONID,PROCTIME,INITATEDBY,FRAUDCHK,PSPCODE,SPCODE,SUBSPCODE,RECEIPTNO,AMOUNTTYPE,CALLBACKURL,PAYERID,TRANTYPE,CUSTMSISDN,VERSION,"+
					"S_IDENTIFIERTYPE,D_IDENTIFIERTYPE,S_ACCATEGORY,D_ACCATEGORY,S_FSPID,D_FSPID,S_IDENTIFYTYPE,D_IDENTIFYTYPE,S_IDENTIFYVALUE,D_IDENTIFYVALUE,D_RECEIVERID,REMARKS, SWITCHREF, S_SENDERID, RESCODE, RESPDESC, ERRCD, ERRDESC) "+
				 	"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				 	
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(),Info.getSYSCODE(),Info.getCHCODE(),Info.getPAYTYPE(),Info.getFLOW(), Info.getREQDATE(),Info.getREQREFNO(), Info.getTRANREFNO(), Info.getREQSL(),
					 Info.getREQTIME(),Info.getPAYDATE(),Info.getSTATUS(),Info.getTRANAMT(),Info.getTRANCURR(),Info.getREQTYPE(),Info.getSERPORCD(),Info.getDEBITAMT(),
					 Info.getDEBITCURR(),Info.getCREDITAMT(),Info.getCREDITCURR(),Info.getS_ACCOUNT(),Info.getD_ACCOUNT(),Info.getS_BANKBIC(),Info.getD_BANKBIC(),Info.getS_ACNAME(),
					 Info.getD_ACNAME(),Info.getINITIATOR_CIF(),Info.getS_CLIENTNO(),Info.getD_CLIENTNO(),Info.getFEEAMT1(),Info.getFEECURR1(),Info.getFEEAMT2(),Info.getFEECURR2(),
					 Info.getFEEAMT3(),Info.getFEECURR3(),Info.getFEEAMT4(),Info.getFEECURR4(),Info.getINVOICENO(),Info.getS_IBAN(),Info.getD_IBAN(),Info.getS_BRNCODE(),
					 Info.getD_BRNCODE(),Info.getS_ACTYPE(),Info.getD_ACTYPE(),Info.getS_ADDRESS(),Info.getD_ADDRESS(),Info.getS_EMAILID(),Info.getD_EMAILID(), Info.getS_MOBILE(), Info.getD_MOBILE(),Info.getS_TELEPHONE(),
					 Info.getD_TELEPHONE(),Info.getSENDER_INFO(),Info.getRECEIVER_INFO(),Info.getPAYEEREF(),Info.getLONGITUDE(),Info.getLATITUDE(), Info.getIPADDRESS(),Info.getDEVICEID(), Info.getLOCATION(),Info.getPURPOSECD(),
					 Info.getPURSPOSEDESC(),Info.getOTPPASSED(),Info.getUSERAGENT(),Info.getSESSIONID(),Info.getPROCTIME(),Info.getINITATEDBY(),Info.getFRAUDCHK(), Info.getPSPCODE(), Info.getSPCODE(), Info.getSUBSPCODE(), 
					 Info.getRECEIPTNO(), Info.getAMOUNTTYPE(), Info.getCALLBACKURL(), Info.getPAYERID(), Info.getTRANTYPE(), Info.getCUSTMSISDN(), Info.getVERSION(), Info.getS_IDENTIFIERTYPE(), Info.getD_IDENTIFIERTYPE(),
					 Info.getS_ACCATEGORY(), Info.getD_ACCATEGORY(), Info.getS_FSPID(), Info.getD_FSPID(), Info.getS_IDENTIFYTYPE(), Info.getD_IDENTIFYTYPE(), Info.getS_IDENTIFYVALUE(), Info.getD_IDENTIFYVALUE(), Info.getD_RECEIVERID(), Info.getREMARKS(), Info.getSWITCHREF(), Info.getS_SENDERID(),
					 Info.getRESCODE(), Info.getRESPDESC(), Info.getERRCD(), Info.getERRDESC() });
					 		
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Request added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Request_002 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Insert_Callback_001(Callback001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into callback001(SUBORGCODE,SYSCODE,CHCODE,PAYTYPE,FLOW,REQDATE,REQREFNO,TRANREFNO,PAYDATE,TRANTYPE,PAYSL,REQSL,REQTIME,STATUS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getSYSCODE(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getFLOW(), Info.getREQDATE(), Info.getREQREFNO(), 
					 Info.getTRANREFNO(), Info.getPAYDATE(), Info.getTRANTYPE(), Info.getPAYSL(), Info.getREQSL(),  Info.getTimestamp(), Info.getSTATUS() });
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Job added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Callback_001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Insert_Callback_002(Callback002 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into callback002(SUBORGCODE,SYSCODE,CHCODE,PAYTYPE,FLOW,REQDATE,REQREFNO,TRANREFNO,PAYDATE,TRANTYPE,PAYSL,REQSL,REQTIME,RESTIME,ERRCD,ERRDESC,RESCODE,RESPDESC,REMARKS,STATUS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getSYSCODE(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getFLOW(), Info.getREQDATE(), Info.getREQREFNO(), Info.getTRANREFNO(), Info.getPAYDATE(),
					  Info.getTRANTYPE(), Info.getPAYSL(), Info.getREQSL(), Info.getTimestamp2(), Info.getTimestamp(), Info.getERRCD(), Info.getERRDESC(), Info.getRESCODE(), Info.getRESPDESC(), Info.getREMARKS(), Info.getSTATUS() });
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Job added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Callback_002 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Update_Request_002(Request_002 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "update request002 w set w.STATUS=?, w.ERRCD=?, w.ERRDESC=?, w.RESCODE=?, w.RESPDESC=? where w.CHCODE=? and w.PAYTYPE=? and w.REQREFNO=? and w.REQSL=?";
			   
			 int status = Jdbctemplate.update(sql, new Object[] { Info.getSTATUS(), Info.getERRCD(), Info.getERRDESC(), Info.getRESCODE(), Info.getRESPDESC(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getREQREFNO(), Info.getREQSL() });
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Stscode", status);
			 details.addProperty("Message", "Request002 updated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Update_Request_002 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Update_PAY_001(PAY_001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "update pay001 w set w.STATUS=?, w.ERRCD=?, w.RESCODE=? where w.CHCODE=? and w.PAYTYPE=? and w.REQREFNO=? and w.REQSL=?";
			   
			 int status = Jdbctemplate.update(sql, new Object[] { Info.getSTATUS(), Info.getERRCD(), Info.getRESCODE(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getREQREFNO(), Info.getREQSL() });
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Stscode", status);
			 details.addProperty("Message", "PAY001 updated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Update_PAY_001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Update_PAY_002_Reversal_Status(PAY_002 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "update pay002 set REV_STATUS=?, ERRCD=?, ERRDES=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
			   
			 int status = Jdbctemplate.update(sql, new Object[] { Info.getREV_STATE(), Info.getERRCD(), Info.getERRDES(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getREQREFNO(), Info.getREQSL() } );
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Stscode", status);
			 details.addProperty("Message", "Response updated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Update_PAY_002_Status :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Generate_Serial() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select REQ001.nextval from dual";
			   
			 int SL = Jdbctemplate.queryForObject(sql, Integer.class);
			 
			 details.addProperty("Serial", SL);
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Serial generated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Generate_Serial from REQ001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Generate_CallabckSerial() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select CALLBACK_SEQ.nextval from dual";
			   
			 int SL = Jdbctemplate.queryForObject(sql, Integer.class);
			 
			 details.addProperty("Serial", SL);
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Serial generated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Generate_Serial from CALLBACK_SEQ :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Generate_Report_Serial() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select seq_report.nextval from dual";
			   
			 int SL = Jdbctemplate.queryForObject(sql, Integer.class);
			 
			 details.addProperty("Serial", SL);
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Serial generated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Generate_Serial from REQ001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Generate_GEPG_Serial() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select GEPG_TISS_SL.nextval from dual";
			   
			 String SL = Jdbctemplate.queryForObject(sql, String.class);
			 
			 details.addProperty("Serial", SL);
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Serial generated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Generate_Serial from REQ001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Generate_Register_Reference_Id() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select 'IDNTREG-' || lpad ( REG_SEQ.nextval, 7, '0' ) from dual";
			   
			 String SL = Jdbctemplate.queryForObject(sql, String.class);
			 
			 details.addProperty("Reference_Id", SL);
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Reference_Id generated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Generate_Register_Reference_Id from REG_SEQ :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
}
