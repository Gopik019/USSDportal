package com.hdsoft.hdpay.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.common.Database;
import com.hdsoft.common.Token_System;
import com.hdsoft.common.base64simulator;
import com.hdsoft.hdpay.Repositories.Account_Information;
import com.hdsoft.hdpay.Repositories.Job_005;
import com.hdsoft.hdpay.Repositories.PAY_001;
import com.hdsoft.hdpay.Repositories.PAY_002;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Request_002;
import com.hdsoft.hdpay.Repositories.Response_001;
import com.hdsoft.hdpay.Repositories.TIPS001;
import com.hdsoft.hdpay.Repositories.TIPS002;
import com.hdsoft.hdpay.Repositories.Transactions;
import com.hdsoft.utils.FormatUtils;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class TIPS_Modal_bk implements Database
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	@Autowired
	public Webservice_Modal Ws;
	
	@Autowired
	public Webservice_call_Modal Wsc;
	
	@Autowired
	public Request_Modal Req_Modal; 
	
	@Autowired
	public Response_Modal Res_Modal;
	
	@Autowired
	public Token_System tk;
	
	@Autowired
	public Simulator_Modal Simulator;
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Handle_TIPS_Transfer_Request_From_Channels(JsonObject Request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();
		
		String SUBORGCODE  = "EXIM";
		String SYSCODE     = "HP";
		String CHCODE 	   =  Headers.get("ChannelID").getAsString();  					
		String MSGTYPE 	   =  "1110";  					
		String FLOW    	   =  "O"; 		

		try
		{
			 JsonObject Validation_details = new JsonObject();
			
			 Validation_details = Payment_Posting_Validation(Request, Headers);
			
			 if(Validation_details.get("result").getAsString().equals("failed"))
			 {
				 return Validation_details;
			 }
			  
			 JsonObject transfers = Request.get("transfers").getAsJsonObject();			 
			 JsonObject trandetail = transfers.get("trandetail").getAsJsonObject();			 
			 JsonObject sourcedtl = transfers.get("sourcedtl").getAsJsonObject();	
			 JsonObject destinationdtl = transfers.get("destinationdtl").getAsJsonObject();

			 String paytype = transfers.get("paytype").getAsString();
			 String date = transfers.get("date").getAsString();   
			 String reqrefid = transfers.get("reqrefid").getAsString();
			 String paydate = transfers.get("paydate").getAsString();
			 
			 String sql = "Select count(*) from request001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and MSGTYPE=? and FLOW=? and REQDATE=? and UNIREFNO=?"; 
			 
			 int count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, paytype, MSGTYPE, FLOW , date, reqrefid }, Integer.class);
			 
			 if(count !=0)
			 {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP94");
				  details.addProperty("message", "duplicate request within to time limit !!");
				 
				  return details;
			 }
			 
			 String tranamt = trandetail.get("tranamt").getAsString();
			 String trancurr = trandetail.get("trancurr").getAsString();
			 String tranrefid = trandetail.get("tranrefid").getAsString();

			 String purposecd = transfers.has("purposecd") ? transfers.get("purposecd").getAsString() : "";        /*** optional ***/
			 String purposedesc = transfers.has("purposedesc") ? transfers.get("purposedesc").getAsString() : "";  /*** optional ***/
			 String checksum = transfers.has("checksum") ? transfers.get("checksum").getAsString() : "";		   /*** optional ***/
			 String initiatedby = transfers.get("initiatedby").getAsString();
			 String trantype = transfers.get("trantype").getAsString();	 
			 String description = transfers.get("description").getAsString();
			 
			 trantype = trantype.toUpperCase();
			 
			 String s_accoutno = sourcedtl.has("s_accoutno") ? sourcedtl.get("s_accoutno").getAsString() : "";
			 String s_identifier = sourcedtl.has("s_identifier") ? sourcedtl.get("s_identifier").getAsString() : "";
			 String s_identifiertype = sourcedtl.get("s_identifiertype").getAsString();  /*** Identifier type ( Possible values : MSISDN, Bank, ALIAS) ***/ 
			 String s_bic = sourcedtl.get("s_bic").getAsString();	 
			 
			 s_identifiertype = s_identifiertype.toUpperCase(); 
			 
			 String s_identify_type = ""; String s_identify_value = ""; 
			 
			 JsonObject s_identify = new JsonObject();
			 
			 if(sourcedtl.has("s_identify"))
			 {
				 s_identify = sourcedtl.get("s_identify").getAsJsonObject();
				 
				 s_identify_type = s_identify.has("type") ? s_identify.get("type").getAsString() : "";
				 s_identify_value = s_identify.has("value") ? s_identify.get("value").getAsString() : "";
			 }
			 
			 String s_feeamt = "0";  String s_feecurr = ""; 
			 
			 if(sourcedtl.has("fee"))
			 {
				 JsonObject fee = sourcedtl.get("fee").getAsJsonObject();
				 
				 if(fee.has("feeamt") && fee.has("feecurr"))
				 {
					 s_feeamt = fee.get("feeamt").getAsString();
					 s_feecurr = fee.get("feecurr").getAsString();
				 }
			 }
			 
			 String s_mobile = sourcedtl.has("s_mobile") ? sourcedtl.get("s_mobile").getAsString() : "";					/*** optional ***/
			 String s_clientno = sourcedtl.has("s_clientno") ? sourcedtl.get("s_clientno").getAsString() : "";				/*** optional ***/
			 String s_initiatorcif = sourcedtl.has("s_initiatorcif") ? sourcedtl.get("s_initiatorcif").getAsString() : "";  /*** optional ***/
			 String s_email = sourcedtl.has("s_email") ? sourcedtl.get("s_email").getAsString() : "";						/*** optional ***/
			 String s_telephone = sourcedtl.has("s_telephone") ? sourcedtl.get("s_telephone").getAsString() : "";			/*** optional ***/
			 String sender_info = sourcedtl.has("sender_info") ? sourcedtl.get("sender_info").getAsString() : "";			/*** optional ***/
			 //String s_payeeref = sourcedtl.has("s_payeeref") ? sourcedtl.get("s_payeeref").getAsString() : "";				/*** optional ***/
			 String s_address = sourcedtl.has("s_address") ? sourcedtl.get("s_address").getAsString() : "";					/*** optional ***/
			 String s_branch = sourcedtl.has("s_branch") ? sourcedtl.get("s_branch").getAsString() : "";					/*** optional ***/
			 String s_iban = sourcedtl.has("s_iban") ? sourcedtl.get("s_iban").getAsString() : "";							/*** optional ***/
			 String s_accoutname = sourcedtl.has("s_accoutname") ? sourcedtl.get("s_accoutname").getAsString() : "";		/*** optional ***/

			 String s_fspId = "013";				
			 String s_accountcategory = "PERSON";   /** Account Category ( Possible values : PERSON, BUSINESS) **/ 
			 String s_actype = sourcedtl.has("s_actype") ? sourcedtl.get("s_actype").getAsString().toUpperCase() : "BANK";   /*** Account type ( Possible values : Bank/ Wallet ) ***/   
			 
			 String Payer_Id = Generate_EXIM_Transfer_Reference(s_fspId, "Transfer", CHCODE, tranrefid).get("Reference_Id").getAsString();
			 
			 sql = "Select count(*) from request002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQDATE=? and TRANREFNO=?"; 
			 
			 count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, paytype, date, Payer_Id }, Integer.class);
			 
			 if(count !=0)
			 {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP104");
				  details.addProperty("message", "Invalid Transaction Reference Id !!");
				 
				  return details;
			 }
			 
			 String VERSION  = Headers.get("VERSION").getAsString(); 
			 
			 JsonObject Response = new JsonObject();
			 
			 if(!VERSION.equals("demo"))  /*** simulator Response Code ***/
			 {
				 logger.debug(">>>>>> source identifier type <<<<<<<<"+s_identifiertype); 
				 
				 if(s_identifiertype.equals("ALIAS"))	 /*** Identifier type ( Possible values : BANK, ALIAS, MSISDN) ***/ 
				 {
					  logger.debug(">>>>>> validating source account with Tips <<<<<<<<"); 

					  Response =  Retrieve_TIPS_Identfifer(s_identifiertype, s_identifier);
					  
					  logger.debug(">>>>>> validation result <<<<<<<< "+Response); 
					  
					  if(!Response.has("Informations"))
					  {
						   details.addProperty("result", "failed");
						   details.addProperty("stscode", "HP14");
						   details.addProperty("message", "source account is not registered with TIPS !!");
						   
						   return details;
					  }
				 }

				 if(s_identifiertype.equals("BANK"))	 /*** Identifier type ( Possible values : BANK, ALIAS, MSISDN) ***/ 
				 {
					  logger.debug(">>>>>> validating source account with bank system <<<<<<<<"); 
					 
					  Response = Retrieve_Account_Information(s_accoutno);
					 
					  logger.debug(">>>>>> validation result <<<<<<<< "+Response); 
					  
					  if(!Response.has("Informations"))
					  {
						  details.addProperty("result", "failed");
						  details.addProperty("stscode", "HP14");
						  details.addProperty("message", "invalid source account !!");
						   
						  return details;
					  }
				 }
				 
				 if(s_identifiertype.equals("MSISDN"))	 /*** Identifier type ( Possible values : BANK, ALIAS, MSISDN) ***/ 
				 {
					  logger.debug(">>>>>> validating source account with bank system <<<<<<<<"); 
					 
					  Response = Retrieve_Account_Information(s_accoutno, s_mobile);
					 
					  logger.debug(">>>>>> validation result <<<<<<<< "+Response); 
					  
					  if(!Response.has("Informations"))
					  {
						  details.addProperty("result", "failed");
						  details.addProperty("stscode", "HP14");
						  details.addProperty("message", "invalid source account !!");
						   
						  return details;
					  }
				 }
			 }
			 
			 if(Response.has("Informations"))
			 {
				 JsonObject AC_detail = Response.get("Informations").getAsJsonObject();
				 
				 s_accoutname = AC_detail.get("CUSTOMERNAME").getAsString(); 		 
				 s_accoutno = AC_detail.get("AC_NO").getAsString();		 
				 s_branch = AC_detail.get("BRN_CODE").getAsString(); 
			 }
			 
			 String d_accoutno = destinationdtl.get("d_accoutno").getAsString();
			 String d_identifier = destinationdtl.get("d_identifier").getAsString();
			 String d_identifiertype = destinationdtl.get("d_identifiertype").getAsString();   /*** Identifier type ( Possible values : MSISDN, Bank, ALIAS) ***/ 
			 
			 d_identifiertype = d_identifiertype.toUpperCase();
			 
			 if(!d_identifiertype.equals("BANK"))  /*** If it is not bank type means consider the identifier as Account number ***/
			 {
				 d_accoutno = d_identifier;
			 }
			 
			 String d_identify_type = ""; String d_identify_value = "";
			 
			 JsonObject d_identify = new JsonObject();
			 
			 if(destinationdtl.has("d_identify"))
			 {
				 d_identify = destinationdtl.get("d_identify").getAsJsonObject();
				 
				 d_identify_type = d_identify.has("type") ? d_identify.get("type").getAsString() : "";
				 d_identify_value = d_identify.has("value") ? d_identify.get("value").getAsString() : "";	
			 }
			 
			 String d_fspId = destinationdtl.get("d_bic").getAsString();																
			 String d_accountcategory = "PERSON";  		/** Account Category ( Possible values : Person, BUSINESS) **/  
			 String d_actype = destinationdtl.has("d_actype") ? destinationdtl.get("d_actype").getAsString().toUpperCase() : "BANK";   /*** Account type ( Possible values : WALLET, BANK) ***/   
			 
			 String d_accoutname = destinationdtl.has("d_accoutname") ? destinationdtl.get("d_accoutname").getAsString() : "";	/*** optional ***/	  
			 String d_mobile = destinationdtl.has("d_mobile") ? destinationdtl.get("d_mobile").getAsString() : "";				/*** optional ***/
			 String d_clientno = destinationdtl.has("d_clientno") ? destinationdtl.get("d_clientno").getAsString() : "";		/*** optional ***/
			 String d_email = destinationdtl.has("d_email") ? destinationdtl.get("d_email").getAsString() : "";					/*** optional ***/
			 String d_telephone = destinationdtl.has("d_telephone") ? destinationdtl.get("d_telephone").getAsString() : "";		/*** optional ***/
			 String d_address = destinationdtl.has("d_address") ? destinationdtl.get("d_address").getAsString() : "";			/*** optional ***/
			 String d_branch = destinationdtl.has("d_branch") ? destinationdtl.get("d_branch").getAsString() : "";				/*** optional ***/
			 String d_iban = destinationdtl.has("d_iban") ? destinationdtl.get("d_iban").getAsString() : ""; 					/*** optional ***/
			 String d_receiverid = destinationdtl.has("d_receiverid") ? destinationdtl.get("d_receiverid").getAsString() : "";	/*** optional ***/
				 			
			 //String Payer_Id = Generate_EXIM_Transfer_Reference(s_fspId, "Transfer", CHCODE, tranrefid).get("Reference_Id").getAsString();					 
			 
			 String Payee_Id = ""; //Generate_Transfer_Reference(d_fspId, "Transfer").get("Reference_Id").getAsString();	
			 
			 String REQSL		=  Req_Modal.Generate_Serial().get("Serial").getAsString();							
			 String MSGURL 	 	=  "";   					/*** (blank) **/
			 String IP      	=  Headers.get("IPAddress").getAsString(); 
			 String PORT    	=  "";  					/*** Need to Discuss **/
			 String HEAD_MSG    =  Headers.toString();  				
			 String BODY_MSG    =  Request.toString();   
			 String Initiator	=  "CUSTOMER";
			 String scenario    =  trantype;
			 String initiatorType = "SENDER"; 
			 
			 initiatedby = initiatorType;    /*** consider as initiatorType  **/	
			 
			 sender_info = Initiator;   	/*** consider as Initiator  **/	
			 
			 String SESSIONID   = Headers.get("Session_ID").getAsString();  
			 String DATEANDTIME = Headers.get("ChannelID").getAsString();  
			 String LONGITUDE   = Headers.get("Longitude").getAsString();  
			 String LATITUDE    = Headers.get("Latitude").getAsString();  
			 String IPADDRESS   = Headers.get("IPAddress").getAsString();  
			 String DEVICEID    = Headers.get("DEVICEID").getAsString();  
			 String LOCATION    = Headers.get("Locationcode").getAsString();  
			
			 Timestamp REQTIME = new java.sql.Timestamp(new java.util.Date().getTime());
 
			 Request_001 Info = new Request_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, FLOW, date, REQTIME, reqrefid, MSGURL, IP, PORT, HEAD_MSG, BODY_MSG, initiatedby, checksum);
			 
			 Req_Modal.Insert_Request_001(Info);  /**** Insert request into request001 ***/
			 
			 logger.debug(">>>>>> Inserted into request 001 <<<<<<<<"); 
			 
			 Request_002 Info2 = new Request_002();
			 
			 Info2.setSUBORGCODE(SUBORGCODE);
			 Info2.setSYSCODE(SYSCODE);
			 Info2.setCHCODE(CHCODE);			
			 Info2.setPAYTYPE(paytype);
			 Info2.setFLOW(FLOW);
			 Info2.setREQDATE(date);
			 Info2.setREQTIME(REQTIME);
			 Info2.setREQREFNO(reqrefid);		
			 Info2.setTRANREFNO(Payer_Id);
			 Info2.setREQSL(REQSL);					 
			 Info2.setPAYDATE(paydate);
			 Info2.setTRANAMT(tranamt);
			 Info2.setTRANCURR(trancurr);	
			 Info2.setREQTYPE("");	  	 	
			 Info2.setSERPORCD(""); 		
			 Info2.setREMARKS(description);
			 Info2.setDEBITAMT(""); 		
			 Info2.setDEBITCURR(""); 		
			 Info2.setCREDITAMT("");	
			 Info2.setCREDITCURR("");
			 Info2.setS_ACCOUNT(s_accoutno); 			
			 Info2.setD_ACCOUNT(d_accoutno);
			 Info2.setS_BANKBIC(s_bic); 			
			 Info2.setD_BANKBIC(d_fspId); 		
			 Info2.setS_ACNAME(s_accoutname); 	
			 Info2.setD_ACNAME(d_accoutname);			
			 Info2.setINITIATOR_CIF(s_initiatorcif);
			 Info2.setS_CLIENTNO(s_clientno);				
			 Info2.setD_CLIENTNO(d_clientno);	 
			 Info2.setFEEAMT1(s_feeamt);					
			 Info2.setFEECURR1(s_feecurr);				
			 Info2.setFEEAMT2("");					
			 Info2.setFEECURR2("");				
			 Info2.setFEEAMT3("");				
			 Info2.setFEECURR3("");					
			 Info2.setFEEAMT4("");					
			 Info2.setFEECURR4("");				
			 Info2.setINVOICENO("");	 				 
			 Info2.setS_IBAN(s_iban);	 		
			 Info2.setD_IBAN(d_iban);	 		
			 Info2.setS_BRNCODE(s_branch);		
			 Info2.setD_BRNCODE(d_branch);	 		
			 Info2.setS_ACTYPE(s_actype);	 		
			 Info2.setD_ACTYPE(d_actype);	 		
			 Info2.setS_ADDRESS(s_address);	 		
			 Info2.setD_ADDRESS(d_address);	 		
			 Info2.setS_EMAILID(s_email);		
			 Info2.setD_EMAILID(d_email);			
			 Info2.setS_MOBILE(s_mobile);		
			 Info2.setD_MOBILE(d_mobile);				
			 Info2.setS_TELEPHONE(s_telephone);
			 Info2.setD_TELEPHONE(d_telephone);
			 Info2.setSENDER_INFO(sender_info);		/*** consider as Initiator  **/	
			 Info2.setRECEIVER_INFO("");
			 Info2.setPAYEEREF(Payee_Id);					 
			 Info2.setLONGITUDE(LONGITUDE);  
			 Info2.setLATITUDE(LATITUDE);
			 Info2.setIPADDRESS(IPADDRESS);
			 Info2.setDEVICEID(DEVICEID);
			 Info2.setLOCATION(LOCATION);				 
			 Info2.setPURPOSECD(purposecd);
			 Info2.setPURSPOSEDESC(purposedesc);
			 Info2.setOTPPASSED("");
			 Info2.setUSERAGENT("");
			 Info2.setSESSIONID(SESSIONID);
			 Info2.setPROCTIME("");				 
			 Info2.setINITATEDBY(initiatedby);	 /*** consider as initiatorType as always CONSUMER ***/
			 Info2.setFRAUDCHK("");				 
			 Info2.setPSPCODE("");
			 Info2.setSPCODE("");
			 Info2.setSUBSPCODE("");				 
			 Info2.setRECEIPTNO("");		
			 Info2.setAMOUNTTYPE("");
			 Info2.setCALLBACKURL("");
			 Info2.setPAYERID(Payer_Id);
			 Info2.setTRANTYPE(scenario);      /*** consider as scenario **/
			 Info2.setCUSTMSISDN(s_mobile);
			 Info2.setVERSION(VERSION);			 
			 Info2.setDATEANDTIME(DATEANDTIME);	 
			 Info2.setS_IDENTIFIERTYPE(s_identifiertype);
			 Info2.setD_IDENTIFIERTYPE(d_identifiertype);
			 Info2.setS_ACCATEGORY(s_accountcategory);
			 Info2.setD_ACCATEGORY(d_accountcategory);
			 Info2.setS_FSPID(s_fspId);
			 Info2.setD_FSPID(d_fspId);
			 Info2.setS_IDENTIFYTYPE(s_identify_type);
			 Info2.setD_IDENTIFYTYPE(d_identify_type);
			 Info2.setS_IDENTIFYVALUE(s_identify_value);
			 Info2.setD_IDENTIFYVALUE(d_identify_value);
			 Info2.setD_RECEIVERID(d_receiverid);
			
			 Req_Modal.Insert_Request_002(Info2);
			 
			 logger.debug(">>>>>> Inserted into request 002 <<<<<<<<"); 
			
			 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, paytype, date, reqrefid, REQSL, "Q", trantype, "", "", "");   /** Insert only if async ***/
				 
			 Req_Modal.Insert_Job_005(Info3);
			 
			 logger.debug(">>>>>> Inserted into job 005 <<<<<<<<"); 
	
			 details.addProperty("result", "success");
			 details.addProperty("stscode", "HP00");
			 details.addProperty("message", "transfer request received successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Payment_Posting_Request :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Handle_TIPS_Transfer_Request_From_TIPS(String Req, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 Common_Utils util = new Common_Utils();
			
			 JsonObject Request = util.StringToJsonObject(Req);
			 
			 JsonObject Validation_details = new JsonObject();
			
			 Validation_details = Payment_Posting_Validation2(Request, Headers);
			
			 if(Validation_details.get("result").getAsString().equals("failed"))
			 {
				 JsonObject errorInformation = new JsonObject();
				 
				 errorInformation.addProperty("errorCode", "3100");
				 errorInformation.addProperty("errorDescription", "Generic validation error of the request.");
				 
				 details.add("errorInformation", errorInformation);
				 	 
				 return details; 
			 }
			 
			 JsonObject amount = Request.get("amount").getAsJsonObject();	
			 		 
			 JsonObject payee = Request.get("payee").getAsJsonObject();	
			 JsonObject payer = Request.get("payer").getAsJsonObject();
			 JsonObject transactionType = Request.get("transactionType").getAsJsonObject();
			 
			 String payerRef = Request.get("payerRef").getAsString();	
			 String description = Request.get("description").getAsString();
			 
			 JsonObject payee_identity = payee.get("identity").getAsJsonObject();	
			 JsonObject payer_identity = payer.get("identity").getAsJsonObject();	
			 
			 String payee_identity_type = payee_identity.get("type").getAsString();	
			 String payee_identity_value = payee_identity.get("value").getAsString();
			 
			 String payer_identity_type = payer_identity.get("type").getAsString();	
			 String payer_identity_value = payer_identity.get("value").getAsString();
			 
			 String initiator = transactionType.get("initiator").getAsString();
			 String initiatorType = transactionType.get("initiatorType").getAsString();
			 String scenario = transactionType.get("scenario").getAsString();
			 
			 String PAYTYPE = "TIPS";
			 String DATE = util.getCurrentDate("dd-MMM-yyyy");  
			 String PAYDATE = util.getCurrentDate("dd-MMM-yyyy");
			 String PURPOSECD = "";
			 String PURPOSEDESC = "";
			 String Checksum = "";
			 String INITATEDBY = initiatorType;
			 String TRANTYPE = scenario;
			
			 String TRANAMT = amount.get("amount").getAsString();
			 String TRANCURR = amount.get("currency").getAsString();
			 String TRANREFID = payerRef;
			 
			 String DEBITAMT = "";
			 String DEBITCURR =  "";
			 String S_ACCOUTNO = payer.get("identifier").getAsString();
			 String S_AccountCategory = payer.get("accountCategory").getAsString();
			 String S_ACTYPE = payer.get("accountType").getAsString();
			 String S_identifierType = payer.get("identifierType").getAsString();
			 String S_CLIENTNO =  "";
			 String S_InitiatorCIF =  "";
			 String S_EMAIL = "";
			 String S_Mobile =  "";
			 String S_telephone = "";
			 String Sender_info = initiator;
			 String S_Address =  "";
			 String S_BIC = "";
			 String S_fspId = payer.get("fspId").getAsString();
			 String S_BRACNCH = "";
			 String S_ACCOUTNAME = payer.get("fullName").getAsString();
			 String S_PAYERID = Request.get("payerRef").getAsString();
			 
			 String S_IDENTIFY_TYPE = payer_identity_type;
			 String S_IDENTIFY_VALUE = payer_identity_value;
			 
			 String S_FEEAMT = "";  
			 String S_FEECURR = "" ; 
			 
			 if(Request.has("endUserFee"))
			 {
				 JsonObject endUserFee = Request.get("endUserFee").getAsJsonObject();	
				 
				 S_FEEAMT = endUserFee.get("amount").getAsString();  
				 S_FEECURR = endUserFee.get("currency").getAsString();  ;
			 }

			 String CREDITAMT = "";
			 String CREDITCURR = "";
			 String D_IDENTIFIER = payee.get("identifier").getAsString();
			 String D_ACCOUTNO = "";
			 String D_AccountCategory = payee.get("accountCategory").getAsString();
			 String D_ACTYPE = payee.get("accountType").getAsString();
			 String D_IdentifierType = payee.get("identifierType").getAsString();
			 String D_CLIENTNO = "";
			 String D_Mobile = "";
			 String D_EMAIL = "";
			 String D_telephone = "";
			 String D_Address = "";
			 String D_BIC = "";
			 String D_fspId = payee.get("fspId").getAsString();
			 String D_BRACNCH =  "";
			 String D_ACCOUTNAME = payee.get("fullName").getAsString();
			 String D_RECEIVERID = "";
			 String D_IDENTIFY_TYPE = payee_identity_type;
			 String D_IDENTIFY_VALUE = payee_identity_value;
			
			 String Payee_Id = Generate_Transfer_Reference(D_fspId, "Transfer").get("Reference_Id").getAsString();
			 
			 String SUBORGCODE  = "EXIM";
			 String SYSCODE     = "HP";
			 String CHCODE 		=  "TIPS";  					
			 String MSGTYPE 	=  "1111";  					/*** service code hardcoded value **/
			 String FLOW    	=  "I"; 					
			 String REQSL		=  Req_Modal.Generate_Serial().get("Serial").getAsString();							
			 String MSGURL 	 	=  "";   					/*** (blank) **/
			 String IP      	=  ""; 
			 String PORT    	=  "";  					/*** Need to Discuss **/
			 String HEAD_MSG    =  Headers.toString();  				
			 String BODY_MSG    =  Request.toString();   
			 
			 String ReqRefID = D_fspId + "-"+System.currentTimeMillis()+"";
			  
			 String sql = "Select count(*) from request001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and MSGTYPE=? and FLOW=? and REQDATE=? and UNIREFNO=?"; 
			 
			 int count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, PAYTYPE, MSGTYPE, FLOW , DATE, ReqRefID }, Integer.class);
			 
			 if(count !=0)
			 {
				 JsonObject errorInformation = new JsonObject();
				 
				 errorInformation.addProperty("errorCode", "3100");
				 errorInformation.addProperty("errorDescription", "Generic validation error of the request.");
				 
				 details.add("errorInformation", errorInformation);
				 	 
				 return details; 
			 }
			 
			 sql = "Select count(*) from request002 where SUBORGCODE=? and PAYTYPE=? and FLOW=? and TRANREFNO=?"; 
			 
			 count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, PAYTYPE, FLOW , S_PAYERID }, Integer.class);
			 
			 if(count !=0)
			 {
				 JsonObject errorInformation = new JsonObject();
				 
				 errorInformation.addProperty("errorCode", "4006");
				 errorInformation.addProperty("errorDescription", "Payer duplicate reference");
				 
				 details.add("errorInformation", errorInformation);
				 	 
				 return details; 
			 }
			 
			 JsonObject Response = new JsonObject();
			 
			 if(D_IdentifierType.equals("ALIAS"))	 /*** Identifier type ( Possible values : BANK, ALIAS, MSISDN) ***/ 
			 {
				  logger.debug(">>>>>> validating source account with Tips <<<<<<<<"); 

				  Response =  Retrieve_TIPS_Identfifer(D_IdentifierType, D_IDENTIFIER);
				  
				  logger.debug(">>>>>> validation result <<<<<<<< "+Response); 
				  
				  if(!Response.has("Informations"))
				  {
					   JsonObject errorInformation = new JsonObject();
						 
					   errorInformation.addProperty("errorCode", "3204");
					   errorInformation.addProperty("errorDescription", "Party not found");
					 
					   details.add("errorInformation", errorInformation);
					 	 
					   return details; 
				  }
			 }

			 if(D_IdentifierType.equals("BANK"))	 /*** Identifier type ( Possible values : BANK, ALIAS, MSISDN) ***/ 
			 {
				  logger.debug(">>>>>> validating source account with bank system <<<<<<<<"); 
				 
				  Response = Retrieve_Account_Information(D_IDENTIFIER);
				 
				  logger.debug(">>>>>> validation result <<<<<<<< "+Response); 
				  
				  if(!Response.has("Informations"))
				  {
					   JsonObject errorInformation = new JsonObject();
						 
					   errorInformation.addProperty("errorCode", "3204");
					   errorInformation.addProperty("errorDescription", "Party not found");
					 
					   details.add("errorInformation", errorInformation);
					 	 
					   return details; 
				  }
			 }
		
		
			 if(D_IdentifierType.equals("MSISDN"))	 /*** Identifier type ( Possible values : BANK, ALIAS, MSISDN) ***/ 
			 {
				  logger.debug(">>>>>> validating source account with bank system <<<<<<<<"); 
				 
				  Response = Retrieve_TIPS_Identfifer(D_IdentifierType, D_IDENTIFIER);
				 
				  logger.debug(">>>>>> validation result <<<<<<<< "+Response); 
				  
				  if(!Response.has("Informations"))
				  {
					   JsonObject errorInformation = new JsonObject();
						 
					   errorInformation.addProperty("errorCode", "3204");
					   errorInformation.addProperty("errorDescription", "Party not found");
					 
					   details.add("errorInformation", errorInformation);
					 	 
					   return details; 
				  }
			 }
		 
			 if(Response.has("Informations"))
			 {
				 JsonObject AC_detail = Response.get("Informations").getAsJsonObject();
				 
				 D_ACCOUTNAME = AC_detail.get("CUSTOMERNAME").getAsString(); 		 
				 D_ACCOUTNO = AC_detail.get("AC_NO").getAsString();		 
				 D_BRACNCH = AC_detail.get("BRN_CODE").getAsString(); 
			 }
			 
			 String SESSIONID   = "";  
			 String LONGITUDE   = "";  
			 String LATITUDE    = "";  
			 String IPADDRESS   = "";  
			 String DEVICEID    = "";  
			 String LOCATION    = "";  
			 String VERSION    = "";
			 
			 Timestamp REQTIME = new java.sql.Timestamp(new java.util.Date().getTime());
			 
			 Request_001 Info = new Request_001(SUBORGCODE, CHCODE, PAYTYPE, MSGTYPE, FLOW, DATE, REQTIME, ReqRefID, MSGURL, IP, PORT, HEAD_MSG, BODY_MSG, INITATEDBY, Checksum);
			 
			 Req_Modal.Insert_Request_001(Info);  /**** Insert request into request001 ***/
			 
			 Request_002 Info2 = new Request_002();
			 
			 Info2.setSUBORGCODE(SUBORGCODE);
			 Info2.setSYSCODE(SYSCODE);
			 Info2.setCHCODE(CHCODE);			
			 Info2.setPAYTYPE(PAYTYPE);
			 Info2.setFLOW(FLOW);
			 Info2.setREQDATE(DATE);
			 Info2.setREQTIME(REQTIME);
			 Info2.setREQREFNO(ReqRefID);		
			 Info2.setTRANREFNO(TRANREFID);
			 Info2.setREQSL(REQSL);					 
			 Info2.setPAYDATE(PAYDATE);
			 Info2.setTRANAMT(TRANAMT);
			 Info2.setTRANCURR(TRANCURR);	
			 Info2.setREQTYPE("");			
			 Info2.setSERPORCD(""); 		
			 Info2.setREMARKS(description);
			 Info2.setDEBITAMT(DEBITAMT); 		
			 Info2.setDEBITCURR(DEBITCURR); 		
			 Info2.setCREDITAMT(CREDITAMT);	
			 Info2.setCREDITCURR(CREDITCURR);
			 Info2.setS_ACCOUNT(S_ACCOUTNO); 			
			 Info2.setD_ACCOUNT(D_ACCOUTNO);
			 Info2.setS_BANKBIC(S_BIC); 			
			 Info2.setD_BANKBIC(D_BIC); 		
			 Info2.setS_ACNAME(S_ACCOUTNAME); 	
			 Info2.setD_ACNAME(D_ACCOUTNAME);			
			 Info2.setINITIATOR_CIF(S_InitiatorCIF);
			 Info2.setS_CLIENTNO(S_CLIENTNO);				
			 Info2.setD_CLIENTNO(D_CLIENTNO);	  	
			 Info2.setFEEAMT1(S_FEEAMT);					
			 Info2.setFEECURR1(S_FEECURR);					
			 Info2.setS_IBAN("");	 		
			 Info2.setD_IBAN("");	 		
			 Info2.setS_BRNCODE(S_BRACNCH);		
			 Info2.setD_BRNCODE(D_BRACNCH);	 		
			 Info2.setS_ACTYPE(S_ACTYPE);	 		
			 Info2.setD_ACTYPE(D_ACTYPE);	 		
			 Info2.setS_ADDRESS(S_Address);	 		
			 Info2.setD_ADDRESS(D_Address);	 		
			 Info2.setS_EMAILID(S_EMAIL);		
			 Info2.setD_EMAILID(D_EMAIL);			
			 Info2.setS_MOBILE(S_Mobile);		
			 Info2.setD_MOBILE(D_Mobile);				
			 Info2.setS_TELEPHONE(S_telephone);
			 Info2.setD_TELEPHONE(D_telephone);
			 Info2.setSENDER_INFO(Sender_info);
			 Info2.setRECEIVER_INFO("");
			 Info2.setPAYEEREF(Payee_Id);					 
			 Info2.setLONGITUDE(LONGITUDE);  
			 Info2.setLATITUDE(LATITUDE);
			 Info2.setIPADDRESS(IPADDRESS);
			 Info2.setDEVICEID(DEVICEID);
			 Info2.setLOCATION(LOCATION);				 
			 Info2.setPURPOSECD(PURPOSECD);
			 Info2.setPURSPOSEDESC(PURPOSEDESC);
			 Info2.setOTPPASSED("");
			 Info2.setUSERAGENT("");
			 Info2.setSESSIONID(SESSIONID);
			 Info2.setPROCTIME("");				 
			 Info2.setINITATEDBY(initiatorType);				 
			 Info2.setFRAUDCHK("");				 
			 Info2.setPSPCODE("");
			 Info2.setSPCODE("");
			 Info2.setSUBSPCODE("");				 
			 Info2.setRECEIPTNO("");		
			 Info2.setAMOUNTTYPE("");
			 Info2.setCALLBACKURL("");
			 Info2.setPAYERID(S_PAYERID);
			 Info2.setTRANTYPE(TRANTYPE);
			 Info2.setCUSTMSISDN("");
			 Info2.setVERSION(VERSION);			 
			 Info2.setS_IDENTIFIERTYPE(S_identifierType);
			 Info2.setD_IDENTIFIERTYPE(D_IdentifierType);
			 Info2.setS_ACCATEGORY(S_AccountCategory);
			 Info2.setD_ACCATEGORY(D_AccountCategory);
			 Info2.setS_FSPID(S_fspId);
			 Info2.setD_FSPID(D_fspId);
			 Info2.setS_IDENTIFYTYPE(S_IDENTIFY_TYPE);
			 Info2.setD_IDENTIFYTYPE(D_IDENTIFY_TYPE);
			 Info2.setS_IDENTIFYVALUE(S_IDENTIFY_VALUE);
			 Info2.setD_IDENTIFYVALUE(D_IDENTIFY_VALUE);
			 Info2.setD_RECEIVERID(D_RECEIVERID);
				
			 Req_Modal.Insert_Request_002(Info2);
			
			 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, PAYTYPE, DATE, ReqRefID, REQSL, "Q", TRANTYPE, "", "", "");   /** Insert only if async ***/
				 
			 Req_Modal.Insert_Job_005(Info3);
	
			 details.addProperty("result", "success");
			 details.addProperty("stscode", "HP00");
			 details.addProperty("message", "transfer request received successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Payment_Posting_Request :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject TIPS_Payment_Executer(final Job_005 Job) 
	{
		JsonObject details = new JsonObject();
		
		final String SUBORGCODE = "EXIM";
		final String PAYTYPE = "TIPS";
		final String Channel_Code = "TIPS";  
		final String Service_Code = "1110";  
		//final String Service_Code2 = "1111";  
		final String SYSCODE = "HP"; 
		
		try
		{
			final String procedureCall = "{CALL PACK_O_PAY.PROC_O_REQUEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
 			
 			Map<String, Object> resultMap = Jdbctemplate.call(new CallableStatementCreator() {
 	 
					public CallableStatement createCallableStatement(Connection connection) throws SQLException {
 
						CallableStatement CS = connection.prepareCall(procedureCall);
						CS.setString(1, Job.getSUBORGCODE()); 
						CS.setString(2, Job.getCHCODE());
						CS.setString(3, Job.getPAYTYPE());
						CS.setString(4, FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"));
						CS.setString(5, Job.getREFNO());
						CS.setString(6, Job.getREQSL());
						CS.setString(7, Service_Code);
						CS.registerOutParameter(8, Types.VARCHAR);
						CS.registerOutParameter(9, Types.VARCHAR);
						CS.registerOutParameter(10, Types.VARCHAR);
						CS.registerOutParameter(11, Types.VARCHAR);
						CS.registerOutParameter(12, Types.VARCHAR);
						CS.registerOutParameter(13, Types.VARCHAR);
						CS.registerOutParameter(14, Types.VARCHAR);
						CS.registerOutParameter(15, Types.VARCHAR);
						CS.registerOutParameter(16, Types.VARCHAR);
						CS.registerOutParameter(17, Types.VARCHAR);
						CS.registerOutParameter(18, Types.VARCHAR);
						CS.registerOutParameter(19, Types.VARCHAR);
						CS.registerOutParameter(20, Types.VARCHAR);
						CS.registerOutParameter(21, Types.VARCHAR);
						CS.registerOutParameter(22, Types.VARCHAR);
						CS.registerOutParameter(23, Types.VARCHAR);
						CS.registerOutParameter(24, Types.VARCHAR);
						
						return CS;
					}
 				}, get_ProcedureParams());
 			
 			Common_Utils utils = new Common_Utils();
 			
 			String HEADERID = utils.ReplaceNull(resultMap.get("o_headerid"));
 			
 			final String O_TRANAMOUNT = utils.ReplaceNull(resultMap.get("o_tranamount"));
 			final String O_DBCURR = utils.ReplaceNull(resultMap.get("o_dbcurr"));
 			final String O_CHRGAMOUNT = utils.ReplaceNull(resultMap.get("o_chrgamount"));
 			final String O_CHRGCURR = utils.ReplaceNull(resultMap.get("o_chrgcurr"));
 			final String O_DBACCOUNT = utils.ReplaceNull(resultMap.get("o_dbaccount"));
 			final String O_CRACCOUNT = utils.ReplaceNull(resultMap.get("o_craccount"));
 			final String O_FLOW = utils.ReplaceNull(resultMap.get("o_flow")); //.equals("I"); // ? Service_Code2 : Service_Code;
            
 			JsonObject webservice_details = Ws.Get_Webserice002_Info(Service_Code, Channel_Code, HEADERID);
 	
 			if(webservice_details.get("Result").getAsString().equals("Failed"))
			{
 				 Request_002 info = new Request_002();

 				 info.setSTATUS(webservice_details.get("Result").getAsString());
				 info.setERRCD("HP103");
				 info.setERRDESC(webservice_details.get("Message").getAsString());
				 info.setCHCODE(Job.getCHCODE());
				 info.setPAYTYPE(Job.getPAYTYPE());
				 info.setTRANTYPE(Job.getTRANTYPE());
				 info.setREQREFNO(Job.getREFNO());
			  
				 Req_Modal.Update_Request_002(info);  
				 
				 PAY_001 pay = new PAY_001();
				  
				 pay.setSTATUS("Failed");
				 pay.setCHCODE(Job.getCHCODE());
				 pay.setPAYTYPE(Job.getPAYTYPE());
				 pay.setTRANTYPE(Job.getTRANTYPE());
				 pay.setREQREFNO(Job.getREFNO());
				 pay.setRESCODE("400");
			  
				 Req_Modal.Update_PAY_001(pay);
				
				 logger.debug("Exception in Get_Webserice_Info :::: "+webservice_details);
				 
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP103");
				 details.addProperty("message", webservice_details.get("Message").getAsString()); 
				 
				 return details;
			}
 			
 			String PAYLOAD = utils.ReplaceNull(resultMap.get("o_payload"));
 			String SIGN_PAYLOAD = utils.ReplaceNull(resultMap.get("o_signpayload"));
 			
 			webservice_details.addProperty("PAYLOAD", PAYLOAD);
 			webservice_details.addProperty("SIGNPAYLOAD", SIGN_PAYLOAD);
 			webservice_details.addProperty("HEADERID", HEADERID);
 			webservice_details.addProperty("METHOD", utils.ReplaceNull(resultMap.get("o_method")));
 			webservice_details.addProperty("URI", utils.ReplaceNull(resultMap.get("o_uri")));
 			webservice_details.addProperty("FORMAT", utils.ReplaceNull(resultMap.get("o_format")));
 			webservice_details.addProperty("PROTOCOL", utils.ReplaceNull(resultMap.get("o_protocol")));
			
		    logger.debug("Final Payload :::: "+PAYLOAD);
	     		  
		    String Requesturl = webservice_details.get("URI").getAsString();
		    
		    JsonArray Headers = webservice_details.get("Headers").getAsJsonArray();
		
		    logger.debug("******** START CBS POSTING ********");
			  
		    logger.debug("O_TRANAMOUNT :::: "+O_TRANAMOUNT);
 			logger.debug("O_DBCURR     :::: "+O_DBCURR);
 			logger.debug("O_CHRGAMOUNT :::: "+O_CHRGAMOUNT);
 			logger.debug("O_CHRGCURR   :::: "+O_CHRGCURR);
 			logger.debug("O_DBACCOUNT  :::: "+O_DBACCOUNT);
 			logger.debug("O_CRACCOUNT  :::: "+O_CRACCOUNT);
 			
		    Common_Utils util = new Common_Utils();
		    
			final String cbsprocedureCall = "{CALL CBS_POST_PACK.PROC_CBSPOST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	 			
	 		  Map<String, Object> cbsresultMap = Jdbctemplate.call(new CallableStatementCreator() {
	 	 
						public CallableStatement createCallableStatement(Connection connection) throws SQLException {
	 
							CallableStatement CS = connection.prepareCall(cbsprocedureCall);
							CS.setString(1, Job.getSUBORGCODE());
							CS.setString(2, Job.getCHCODE());
							CS.setString(3, Job.getPAYTYPE());
							CS.setString(4, O_FLOW);
							CS.setString(5, Job.getREFNO());
							CS.setString(6, O_TRANAMOUNT);
							CS.setString(7, O_DBCURR);
							CS.setString(8, O_DBACCOUNT);
							CS.setString(9, O_CRACCOUNT);
							CS.setString(10, O_CHRGAMOUNT);
							CS.setString(11, O_CHRGCURR);
							CS.registerOutParameter(12, Types.VARCHAR);
							CS.registerOutParameter(13, Types.VARCHAR);
							CS.registerOutParameter(14, Types.VARCHAR);
							CS.registerOutParameter(15, Types.VARCHAR);
							CS.registerOutParameter(16, Types.VARCHAR);
							CS.registerOutParameter(17, Types.VARCHAR);
							CS.registerOutParameter(18, Types.VARCHAR);
							CS.registerOutParameter(19, Types.VARCHAR);
							CS.registerOutParameter(20, Types.VARCHAR);
							CS.registerOutParameter(21, Types.VARCHAR);
							CS.registerOutParameter(22, Types.VARCHAR);
							
							return CS;
						}
	 				}, get_CBS_ProcedureParams());
			  
	 		 //String O_NARRATION = cbsresultMap.get("o_narration").toString();  //O_NARRATION
	 		 
	 		/*  
	 		  String o_result = cbsresultMap.get("o_result").toString();
	 		  String o_message = cbsresultMap.get("o_message").toString(); */
	 		  
	 		  String sql = "Select * from transactions where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? order by transeq,legsl";
			 	
	 		  List<Transactions> Transactions_Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), "HP", "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO() }, new Transaction_Mapper());
	 	
              List<Transactions> Credit_Info = new ArrayList<Transactions>();
			 
			  List<Transactions> Debit_Info = new ArrayList<Transactions>();
			    
			  for(int x=0;x<Transactions_Info.size();x++)
		 	  {
			 	  String DBCR = Transactions_Info.get(x).getDBCR();

			 	  if(DBCR.equals("D")) {  Debit_Info.add(Transactions_Info.get(x));  }
			 
			 	  if(DBCR.equals("C")) {  Credit_Info.add(Transactions_Info.get(x)); }
		 	  }
			  
			  int err = 0;
			 
			  String errormsg = "";
			  
			  for(int y=0;y<Credit_Info.size();y++)  
			  {
				     String Billamount = Debit_Info.get(y).getAMOUNT();
 					 String Charge = Debit_Info.get(y).getCHARGES();
 					 String Narration = Debit_Info.get(y).getREMARKS();
 					 String DBAccount = Debit_Info.get(y).getLEDGERNO();
 					 String CRAccount = Credit_Info.get(y).getLEDGERNO();
 					 String Transeq = Debit_Info.get(y).getTRANSEQ();

 					 if(util.isNullOrEmpty(Charge))
   					 {
 						  Charge = "0";
   					 }
 					 
 					 if(util.isNullOrEmpty(DBAccount))
   					 {
 						  DBAccount = Debit_Info.get(y).getACCOUNTNO();
   					 }

  					 if(util.isNullOrEmpty(CRAccount))
  					 {
  						  CRAccount = Credit_Info.get(y).getACCOUNTNO();
  					 }
  					 
  					 String DBCurr = cbsresultMap.get("o_dbcurr").toString();
  					 String Reqcode = cbsresultMap.get("o_reqcode").toString();
  			 		 String BRNCODE = cbsresultMap.get("o_brncode").toString();
  					 
  			 		 errormsg =  base64simulator.formrequestnew(Billamount, Charge, DBCurr, DBAccount, CRAccount, Narration, Reqcode, BRNCODE);
  			 		
  			 		 logger.debug("CBS RESPONSE for Transeq "+Transeq+" is :::: "+errormsg);  
  			 		 
  			 		 if(errormsg.equalsIgnoreCase("S"))
   					 {
	   					 String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and TRANSEQ=?";
						
	   					 Jdbctemplate.update(Update_Sql, new Object[] { "POSTED", Job.getSUBORGCODE(), Job.getCHCODE(), "HP", "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO(), Transeq });
   					 }
  			 		 else
  			 		 {
  			 			 err++;
  			 			 
  			 			 logger.debug("Error in CBS POSTING  :::: "+errormsg);
  						 
  			 			 break;
  			 		 }
			  }
			   
			  logger.debug("******** END CBS POSTING ********");
			  
			  if(err != 0)
			  {
				  logger.debug("CBS POSTING for ref id "+Job.getREFNO()+" is :::: failed ");
			      
				  Request_002 info = new Request_002();   /**** Create a object for request 002 ****/
				  
				  info.setSTATUS("Failed");
				  info.setCHCODE(Job.getCHCODE());
				  info.setPAYTYPE(Job.getPAYTYPE());
				  info.setTRANTYPE(Job.getTRANTYPE());
				  info.setREQREFNO(Job.getREFNO());
				  info.setERRCD("400");
				  info.setERRDESC("CBS POSTING Failed for a reson ::: "+errormsg);
				  
				  Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
				  
				  PAY_001 pay = new PAY_001();
				  
				  pay.setSTATUS("Failed");
				  pay.setCHCODE(Job.getCHCODE());
				  pay.setPAYTYPE(Job.getPAYTYPE());
				  pay.setTRANTYPE(Job.getTRANTYPE());
				  pay.setREQREFNO(Job.getREFNO());
				  pay.setRESCODE("400");
				  
				  Req_Modal.Update_PAY_001(pay);
				  
				  String response = errormsg; String stscode = "";

				  Response_001 Response = new Response_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Service_Code, "O", Job.getREFNO() , Requesturl , "IP", "PORT", Headers.toString(), response, "CBS", "");
					
				  Res_Modal.Insert_Response_001(Response);  /**** Insert Response to Response_001 ****/	 
				  
				  if(errormsg.equalsIgnoreCase("0014"))  /** Invalid Account **/
				  {
					  stscode = "HP14";
				  }
				  else if(errormsg.equalsIgnoreCase("0096")) /** Transaction Declined **/
				  {
					  stscode = "HP112";
				  }
				  else if(errormsg.equalsIgnoreCase("0051")) /** Insufficient Fund **/
				  {
					  stscode = "HP51";
				  }
				  else
				  {
					  stscode = "HP110";
				  }
				  
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", stscode);
				  details.addProperty("message", "Payment transfer failed"); 
				  
				  return details;
			  }
			  
			  /*** Tips Token Process ***/
			  
			   JsonObject Token_details = new JsonObject();
			  
			   String token = "";
			   
			   Token_details = tk.get_stored_token(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE());
			  
			   if(Token_details.get("Result").getAsString().equals("Success"))
			   {
			 	   token = Token_details.get("token").getAsString();
			   }
			   else
			   {
				   Token_details = Generate_Token();
				   
				   int Response_Code = Token_details.get("Response_Code").getAsInt();
					  
				   if(Response_Code == 200)
				   {
					    String res = Token_details.get("Response").getAsString();
						   
					    JsonObject Response = util.StringToJsonObject(res);
					   
					    token = Response.get("access_token").getAsString();
					   
					    Token_Info info = new Token_Info(SUBORGCODE, Job.getCHCODE(), Job.getPAYTYPE(), token);
					   
					    tk.Check_and_Update_tokens(info);
				   }
				   else
				   {
					     logger.debug("Error in Generating token from TIPs :::: "+Token_details);
					    
					     Request_002 info = new Request_002();

		 				 info.setSTATUS(webservice_details.get("Result").getAsString());
						 info.setERRCD("HP103");
						 info.setERRDESC(webservice_details.get("Message").getAsString());
						 info.setCHCODE(Job.getCHCODE());
						 info.setPAYTYPE(Job.getPAYTYPE());
						 info.setTRANTYPE(Job.getTRANTYPE());
						 info.setREQREFNO(Job.getREFNO());
					  
						 Req_Modal.Update_Request_002(info);  
						 
						 PAY_001 pay = new PAY_001();
						  
						 pay.setSTATUS("Failed");
						 pay.setCHCODE(Job.getCHCODE());
						 pay.setPAYTYPE(Job.getPAYTYPE());
						 pay.setTRANTYPE(Job.getTRANTYPE());
						 pay.setREQREFNO(Job.getREFNO());
						 pay.setRESCODE("HP103");
					  
						 Req_Modal.Update_PAY_001(pay);
					    
					     details.addProperty("result",  "failed");
						 details.addProperty("stscode", "HP111");
						 details.addProperty("message", "payment gateway token generation failed");  
						 
						 return details; 
				   }
			  }
			   
			  if(O_FLOW.equals("I"))  /** check whether this Request is from TIPs **/
			  {
				  String SERVICECD = "1111";
				  
				  logger.debug(">>>>>> Initite Transfer Status Update Service <<<<<<");

				  sql = "Select * from pay001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQSL=?";
					 
				  List<PAY_001> INFO = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, Job.getCHCODE(), Job.getPAYTYPE(), Job.getREQSL() }, new Pay001_Mapper());
				 
				  webservice_details = Ws.Get_Webserice_Info(SYSCODE, PAYTYPE, SERVICECD);
					 
				  if(webservice_details.get("Result").getAsString().equals("Failed") || INFO.size() == 0)
				  {
					  logger.debug("Error in Get_Webserice_Info :::: "+webservice_details);
					 
					  details.addProperty("result", "failed");
					  details.addProperty("stscode", INFO.size() == 0 ? "HP200" : "HP103");
					  details.addProperty("message", INFO.size() == 0 ? "details not found !!" : webservice_details.get("Message").getAsString()); 
					 	  
					  return details; 
				  }
				  
				  Request_002 info = new Request_002();   /**** Create a object for request 002 ****/
				  
				  info.setSTATUS("Success");
				  info.setCHCODE(Job.getCHCODE());
				  info.setPAYTYPE(Job.getPAYTYPE());
				  info.setTRANTYPE(Job.getTRANTYPE());
				  info.setREQREFNO(Job.getREFNO());
				  info.setRESCODE("200");
				  info.setRESPDESC("Success");	
				  
				  PAY_001 pay = new PAY_001();
				  
				  pay.setSTATUS("Success");
				  pay.setCHCODE(Job.getCHCODE());
				  pay.setPAYTYPE(Job.getPAYTYPE());
				  pay.setTRANTYPE(Job.getTRANTYPE());
				  pay.setREQREFNO(Job.getREFNO());
				  pay.setRESCODE("200");
				  
				  Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
				  
				  Req_Modal.Update_PAY_001(pay);
				  
				  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
				  
				  String Headers_str = O_Headers.toString();
				  
				  Headers_str = Headers_str.replace("~Token~", token);
				  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
				  Headers_str = Headers_str.replace("~fsp-source~", "013");
				  Headers_str = Headers_str.replace("~fsp-destination~", "tips");
				  Headers_str = Headers_str.replace("~fsp-encryption~", "");
				  Headers_str = Headers_str.replace("~fsp-signature~", get_signature(PAYLOAD));
				  Headers_str = Headers_str.replace("~fsp-uri~", "");
				  Headers_str = Headers_str.replace("~fsp-http-method~", "");
				  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());  
				  
				  O_Headers = util.StringToJsonArray(Headers_str);
					 
				  String urlparam = webservice_details.get("PAYLOAD").getAsString();
				  
		          String requesturl = webservice_details.get("URI").getAsString(); 
		          
		          requesturl = requesturl.replace("~payerRef~", INFO.get(0).getPAYERID());
		          
		          if(err == 0)
		          {
		        	  urlparam = urlparam.replace("~payeeRef~", INFO.get(0).getPAYEEREF());
					  urlparam = urlparam.replace("~transferState~", "COMMITTED");
					  urlparam = urlparam.replace("~reasonCode~", "60");
		          }
		          else
		          {
		        	  urlparam = urlparam.replace("~payeeRef~", INFO.get(0).getPAYEEREF());
					  urlparam = urlparam.replace("~transferState~", "ABORTED");
					  urlparam = urlparam.replace("~reasonCode~", "78");
		          }
		          
				  webservice_details.addProperty("URI", requesturl);
				  webservice_details.addProperty("PAYLOAD", urlparam);
				  webservice_details.add("Headers", O_Headers);
				  
				  String o_FLOW  =  "O"; 
			      String O_ReqRefID  = "";
			      String O_MSGURL = requesturl; 
			      String O_IP = "";
			      String O_PORT = "";
			      String O_HEAD_MSG = O_Headers.toString();
			      String O_BODY_MSG = urlparam;
			      String O_INITATEDBY = "HPAY";
			      String O_Checksum = "";
			      String date = util.getCurrentDateTime();
			      
			      Request_001 Request_001 = new Request_001(SUBORGCODE, Job.getCHCODE(), PAYTYPE, SERVICECD, o_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
				 
				  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
				  
				  JsonObject API_Response = new JsonObject();
		       
				  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
				  
				  logger.debug(">>>>>> Confirm Transfer API_Response :::: "+API_Response+" <<<<<<");
				  
				  Response_001 Response = new Response_001(SUBORGCODE, Job.getCHCODE(), PAYTYPE, SERVICECD, "O", O_ReqRefID , requesturl , O_IP, O_PORT, O_HEAD_MSG, API_Response.toString(), "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);
				  
				  details.addProperty("result", "success");
				  details.addProperty("stscode", "HP00");
				  details.addProperty("message", "Payment Request Processed Successfuly !!");
				 
				  return details;
			  }
			  else   /** check whether this Request is from Channels **/
			  {
				  JsonObject Info = util.StringToJsonObject(PAYLOAD);
				  
				  JsonObject payer  = Info.get("payer").getAsJsonObject();
				  JsonObject payee  = Info.get("payee").getAsJsonObject();
				 
				  String fsp_source = payer.get("fspId").getAsString();
				  String fsp_destination = payee.get("fspId").getAsString();
				  
				  String Headers_str = Headers.toString();
				  
				  Headers_str = Headers_str.replace("~Token~", token);
				  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
				  Headers_str = Headers_str.replace("~fsp-source~", fsp_source);
				  Headers_str = Headers_str.replace("~fsp-destination~", fsp_destination);
				  Headers_str = Headers_str.replace("~fsp-encryption~", "");
				  Headers_str = Headers_str.replace("~fsp-signature~", get_signature(PAYLOAD));
				  Headers_str = Headers_str.replace("~fsp-uri~", "");
				  Headers_str = Headers_str.replace("~fsp-http-method~", "");
				  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime_add(1));
				 
				  Headers = util.StringToJsonArray(Headers_str); 
				  
				  webservice_details.add("Headers", Headers);
				  	  
				  logger.debug("Final Headers :::: "+Headers);
				  		  
			      String O_DATE  = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(System.currentTimeMillis());
			      Timestamp O_REQTIME  = new java.sql.Timestamp(new java.util.Date().getTime());
			      String O_ReqRefID  = Job.getREFNO();
			      String O_MSGURL = Requesturl; 
			      String O_IP = "";
			      String O_PORT = "";
			      String O_HEAD_MSG = Headers.toString();
			      String O_BODY_MSG = PAYLOAD;
			      String O_INITATEDBY = "HPAY";
			      String O_Checksum = "";
			      String O_MSGTYPE = Service_Code;
				     
				  Req_Modal.Insert_Request_001(SUBORGCODE, Channel_Code, PAYTYPE, O_MSGTYPE, O_FLOW, O_DATE, O_REQTIME, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum); 		

				  JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
				  
				  logger.debug("Payment Post API_Response for ref id "+O_ReqRefID+" is :::: "+API_Response);
				  
				  Request_002 info = new Request_002();   /**** Create a object for request 002 ****/
				  
				  info.setSTATUS(API_Response.get("Result").getAsString());
				  info.setCHCODE(Job.getCHCODE());
				  info.setPAYTYPE(Job.getPAYTYPE());
				  info.setTRANTYPE(Job.getTRANTYPE());
				  info.setREQREFNO(Job.getREFNO());
				  
				  PAY_001 pay = new PAY_001();
				  
				  pay.setSTATUS(API_Response.get("Result").getAsString());
				  pay.setCHCODE(Job.getCHCODE());
				  pay.setPAYTYPE(Job.getPAYTYPE());
				  pay.setTRANTYPE(Job.getTRANTYPE());
				  pay.setREQREFNO(Job.getREFNO());
				  
				  String response = "";  
				  
				  if(API_Response.get("Result").getAsString().equals("Success") && API_Response.get("Response_Code").getAsInt() == 202)
				  {
					  response = API_Response.get("Response").getAsString();  
					  
					  info.setRESCODE(API_Response.get("Response_Code").getAsString());
					  info.setRESPDESC(API_Response.get("Message").getAsString());	
					  pay.setRESCODE(API_Response.get("Response_Code").getAsString());
				  }  
				  else
				  {
					  response = API_Response.toString();
					  
					  info.setERRCD(API_Response.get("Response_Code").getAsString());
					  info.setERRDESC(API_Response.get("Message").getAsString());
					  pay.setRESCODE(API_Response.get("Response_Code").getAsString());
					  
					  /*** Initiate the Internal Reversal ***/
					  
					  if(err == 0)
					  {
						  String Reversal_Id =  Generate_EXIM_Transfer_Reference("013", "Reversal", Job.getCHCODE(), Job.getREFNO()).get("Reference_Id").getAsString();
						  
						  Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, "EXIMPAY", Job.getPAYTYPE(), O_DATE, Job.getREFNO(), Job.getREQSL(), "Q", "REVERSAL", "TIPS POSTING FAILED", Reversal_Id, ""); 
						 
						  Req_Modal.Insert_Job_005(Info3);
					  }
				  }
				  
				  Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
				  
				  Req_Modal.Update_PAY_001(pay);
				 
				  Response_001 Response = new Response_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Service_Code, "O", Job.getREFNO() , Requesturl , "IP", "PORT", Headers.toString(), response, "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);  /**** Insert Response to Response_001 ****/	  
				  
				  details.addProperty("result", API_Response.get("Response_Code").getAsInt() == 202 ? "success" : "failed");
				  details.addProperty("stscode", API_Response.get("Response_Code").getAsInt() == 202 ? "HP00" : "HP06");
				  details.addProperty("message", API_Response.get("Response_Code").getAsInt() == 202 ? "payment transfer success" : "payment transfer failed" );
			  }  
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in TIPS Payment Executor :::: "+details);
		 }
		
		 return details;
	}
	
	public JsonObject TIPS_Payment_Reversal_Executer(final Job_005 Job) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			final String procedureCall = "{CALL PACK_O_PAY_REV.PROC_O_REV_REQUEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
 			
 			Map<String, Object> resultMap = Jdbctemplate.call(new CallableStatementCreator() {
 	 
					public CallableStatement createCallableStatement(Connection connection) throws SQLException {
 
						CallableStatement CS = connection.prepareCall(procedureCall);
						CS.setString(1, Job.getSUBORGCODE()); 
						CS.setString(2, Job.getCHCODE());
						CS.setString(3, Job.getPAYTYPE());
						CS.setString(4, FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"));
						CS.setString(5, Job.getREFNO());
						CS.setString(6, Job.getREQSL());
						CS.setString(7, Job.getSERVCODE());
						CS.registerOutParameter(8, Types.VARCHAR);
						CS.registerOutParameter(9, Types.VARCHAR);
						CS.registerOutParameter(10, Types.VARCHAR);
						CS.registerOutParameter(11, Types.VARCHAR);
						CS.registerOutParameter(12, Types.VARCHAR);
						CS.registerOutParameter(13, Types.VARCHAR);
						CS.registerOutParameter(14, Types.VARCHAR);
						CS.registerOutParameter(15, Types.VARCHAR);
						CS.registerOutParameter(16, Types.VARCHAR);
						CS.registerOutParameter(17, Types.VARCHAR);
						CS.registerOutParameter(18, Types.VARCHAR);
						CS.registerOutParameter(19, Types.VARCHAR);
						CS.registerOutParameter(20, Types.VARCHAR);
						CS.registerOutParameter(21, Types.VARCHAR);
						CS.registerOutParameter(22, Types.VARCHAR);
						CS.registerOutParameter(23, Types.VARCHAR);
						CS.registerOutParameter(24, Types.VARCHAR);
						CS.registerOutParameter(25, Types.VARCHAR);
						CS.registerOutParameter(26, Types.VARCHAR);
						CS.registerOutParameter(27, Types.VARCHAR);
						CS.registerOutParameter(28, Types.VARCHAR);
						CS.registerOutParameter(29, Types.VARCHAR);
						CS.registerOutParameter(30, Types.VARCHAR);
						CS.registerOutParameter(31, Types.VARCHAR);
						
						return CS;
					}
 				}, get_ProcedureParams_rev());
 			
 			Common_Utils util = new Common_Utils();
 			
 			String HEADERID = util.ReplaceNull(resultMap.get("o_headerid"));
 			
 			final String O_CHANNEL = util.ReplaceNull(resultMap.get("o_channel"));
 			
 			final String O_TRANAMOUNT = util.ReplaceNull(resultMap.get("o_tranamount"));
 			final String O_DBCURR = util.ReplaceNull(resultMap.get("o_dbcurr"));
 			final String O_CHRGAMOUNT = util.ReplaceNull(resultMap.get("o_chrgamount"));
 			final String O_CHRGCURR = util.ReplaceNull(resultMap.get("o_chrgcurr"));
 			final String O_DBACCOUNT = util.ReplaceNull(resultMap.get("o_dbaccount"));
 			final String O_CRACCOUNT = util.ReplaceNull(resultMap.get("o_craccount"));
 			
 			String PAYLOAD = util.ReplaceNull(resultMap.get("o_payload"));
 			
 			JsonObject webservice_details = new JsonObject(); //Ws.Get_Webserice002_Info(Job.getSERVCODE(), Job.getPAYTYPE(), HEADERID);
 	
 			webservice_details.addProperty("PAYLOAD", PAYLOAD);
 			webservice_details.addProperty("SIGNPAYLOAD", util.ReplaceNull(resultMap.get("o_signpayload")));
 			webservice_details.addProperty("HEADERID", HEADERID);
 			webservice_details.addProperty("METHOD", util.ReplaceNull(resultMap.get("o_method")));
 			webservice_details.addProperty("URI", util.ReplaceNull(resultMap.get("o_uri")));
 			webservice_details.addProperty("FORMAT", util.ReplaceNull(resultMap.get("o_format")));
 			webservice_details.addProperty("PROTOCOL", util.ReplaceNull(resultMap.get("o_protocol")));
			
		    logger.debug("Final Payload :::: "+PAYLOAD);
	     	
		    //JsonArray Headers = webservice_details.get("Headers").getAsJsonArray();
            
		    int err = 0;  String errormsg = "";
			 
		    if(Job.getCHCODE().equals("TIPS") || Job.getCHCODE().equals("EXIMPAY") || Job.getSERVCODE().equals("2110"))  
		    {
		    	logger.debug("O_TRANAMOUNT :::: "+O_TRANAMOUNT);
	 			logger.debug("O_DBCURR     :::: "+O_DBCURR);
	 			logger.debug("O_CHRGAMOUNT :::: "+O_CHRGAMOUNT);
	 			logger.debug("O_CHRGCURR   :::: "+O_CHRGCURR);
	 			logger.debug("O_DBACCOUNT  :::: "+O_DBACCOUNT);
	 			logger.debug("O_CRACCOUNT  :::: "+O_CRACCOUNT);
	 			
	 			logger.debug("******** START CBS POSTING ********");
	 					
	 			String sql = "Select * from transactions where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? order by transeq,legsl";
			 	
	 			List<Transactions> Transactions_Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), O_CHANNEL, "HP", "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO() }, new Transaction_Mapper());
	 	
	 			List<Transactions> Credit_Info = new ArrayList<Transactions>();
			 
	 			List<Transactions> Debit_Info = new ArrayList<Transactions>();
			    
			    for(int x=0;x<Transactions_Info.size();x++)
		 	    {
			    	String DBCR = Transactions_Info.get(x).getDBCR();

			    	if(DBCR.equals("D")) {  Debit_Info.add(Transactions_Info.get(x));  }
			 
			    	if(DBCR.equals("C")) {  Credit_Info.add(Transactions_Info.get(x)); }
		 	    }
				  
				for(int y=0;y<Credit_Info.size();y++)  
				{
				     String Billamount = Debit_Info.get(y).getAMOUNT();
					 String Charge = Debit_Info.get(y).getCHARGES();
					 String Narration = Debit_Info.get(y).getREMARKS();
					 String DBAccount = Debit_Info.get(y).getLEDGERNO();
					 String CRAccount = Credit_Info.get(y).getLEDGERNO();
					 String Transeq = Debit_Info.get(y).getTRANSEQ();

					 if(util.isNullOrEmpty(Charge))
					 {
						  Charge = "0";
					 }
					 
					 if(util.isNullOrEmpty(DBAccount))
					 {
						  DBAccount = Debit_Info.get(y).getACCOUNTNO();
					 }

					 if(util.isNullOrEmpty(CRAccount))
					 {
						  CRAccount = Credit_Info.get(y).getACCOUNTNO();
					 }
	  					 
					 String DBCurr = resultMap.get("o_dbcurr").toString();
					 String Reqcode = resultMap.get("o_reqcode").toString();
			 		 String BRNCODE = resultMap.get("o_brncode").toString();
					 
			 		 errormsg =  base64simulator.formrequestnew(Billamount, Charge, DBCurr, DBAccount, CRAccount, Narration, Reqcode, BRNCODE);
	  			 		
			 		 logger.debug("CBS RESPONSE for Transeq "+Transeq+" is :::: "+errormsg);  
			 		 
			 		 if(errormsg.equalsIgnoreCase("S"))
					 {
	   					 String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and TRANSEQ=?";
						
	   					 Jdbctemplate.update(Update_Sql, new Object[] { "REVERSED", Job.getSUBORGCODE(), O_CHANNEL, "HP", "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO(), Transeq });
					 }
			 		 else
			 		 {
			 			 err++;
			 			 
			 			 logger.debug("Error in CBS POSTING  :::: "+errormsg);
						 
			 			 break;
			 		 }
				  }
				   
				  logger.debug("******** END CBS POSTING ********");
		      }
		    
			  if(Job.getCHCODE().equals("TIPS") && Job.getSERVCODE().equals("2120")) /**** Confirm Hold to TIPS ****/
			  {
				  details = TIPS_Hold_Request_Response(webservice_details, Job, O_CHANNEL, err, errormsg); 
			  }
			  else if(Job.getCHCODE().equals("TIPS") && Job.getSERVCODE().equals("2110"))  /**** Initiate Reversal to TIPS ****/
			  {
				  details = TIPS_Reversal_Transfer_Response(Job, O_CHANNEL, err, errormsg);
			  }
			  else if(Job.getCHCODE().equals("EXIMPAY"))  
			  {
				  String sql = "Select * from PAY002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and TRANREFNO=? and REQSL=?";
				  
				  List<PAY_002> Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), O_CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() }, new Pay002_Mapper());
				
		          if(err == 0 && Info.size() !=0) 
		          {
		        	  String REV_DATE = util.getCurrentDate("dd-MMM-yyyy");
		        	  
		        	  sql = "update pay002 set ISREVERSED=?, REV_DATE=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
					   
					  Jdbctemplate.update(sql, new Object[] { "1", REV_DATE, "F", "1", "REVERSED", O_CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });
					  
					  details.addProperty("result", "success");
					  details.addProperty("stscode", "HP00");
					  details.addProperty("message", "Payment Reversal Successfull !!");
					 
					  return details;
		          }
		          else
		          {
		        	  details.addProperty("result", "failed");
					  details.addProperty("stscode", "HP300");
					  details.addProperty("message", "Payment Reversal Failed !!");
					 
					  return details;
		          }
			  }
			  else
			  {
				  if(Job.getSERVCODE().equals("2120"))
				  {
					  details = Channel_Hold_Request_To_TIPS(Job, O_CHANNEL, err, errormsg);  
				  }
				  else
				  {
					  String REV_DATE = util.getCurrentDate("dd-MMM-yyyy");
		        	  
					  String sql = "update pay002 set ISREVERSED=?, REV_DATE=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
					   
					  Jdbctemplate.update(sql, new Object[] { "1", REV_DATE, "F", "1", "REVERSED", O_CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });
					  
					  details.addProperty("result", "success");
					  details.addProperty("stscode", "HP00");
					  details.addProperty("message", "Payment Reversal Successfull !!");
					 
					  return details;
				  }
			  }  
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in TIPS Payment Executor :::: "+details);
		 }
		
		 return details;
	}
	
	public JsonObject TIPS_Hold_Request_Response(JsonObject webservice_details, Job_005 Job, String CHANNEL, int err, String errormsg) /***  PUT /messageTransfersReversal/{payerRef} (for update & notification ***/
	{
		String SERVICECD = "2121";
		
		JsonObject details = new JsonObject();  
		
		try
		{
			   Common_Utils util = new Common_Utils();
			
			   JsonObject Token_details = new JsonObject();
			  
			   String token = "";
			   
			   Token_details = tk.get_stored_token(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE());
			  
			   if(Token_details.get("Result").getAsString().equals("Success"))
			   {
			 	   token = Token_details.get("token").getAsString();
			   }
			   else
			   {
				   Token_details = Generate_Token();
				   
				   int Response_Code = Token_details.get("Response_Code").getAsInt();
					  
				   if(Response_Code == 200)
				   {
					    String res = Token_details.get("Response").getAsString();
						   
					    JsonObject Response = util.StringToJsonObject(res);
					   
					    token = Response.get("access_token").getAsString();
					   
					    Token_Info info = new Token_Info(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), token);
					   
					    tk.Check_and_Update_tokens(info);
				   }
				   else
				   {
					     logger.debug("Error in Generating token from TIPs :::: "+Token_details);
					    
						 PAY_002 pay = new PAY_002();
						  
						 pay.setREV_STATE("failed");
						 pay.setERRCD("HP111");
						 pay.setERRDES("payment gateway token generation failed");
						 pay.setCHCODE(CHANNEL);
						 pay.setPAYTYPE(Job.getPAYTYPE());
						 pay.setTRANTYPE(Job.getTRANTYPE());
						 pay.setREQREFNO(Job.getREFNO());
						 pay.setREQSL(Job.getREQSL());
					  
						 Req_Modal.Update_PAY_002_Reversal_Status(pay);
					    
					     details.addProperty("result",  "failed");
						 details.addProperty("stscode", "HP111");
						 details.addProperty("message", "payment gateway token generation failed");  
						 
						 return details; 
				   }
			  }
			   
			  String old_payload = webservice_details.get("PAYLOAD").getAsString();
			  
			  JsonObject info = util.StringToJsonObject(old_payload);
			  
			  String payerRef = info.get("payerRef").getAsString();
			  String payeeRef = info.get("payeeRef").getAsString();
			  
			  String[] payer_temp = payerRef.split("-");
			  String[] payee_temp = payeeRef.split("-");
			  
			  String fsp_source = payer_temp[0];
			  String fsp_destination = payee_temp[0];
			  
			  webservice_details = Ws.Get_Webserice_Info(Job.getSYSCODE(), Job.getPAYTYPE(), SERVICECD);
			  
			  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
			  
			  String Headers_str = O_Headers.toString();
			  
			  Headers_str = Headers_str.replace("~Token~", token);
			  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
			  Headers_str = Headers_str.replace("~fsp-source~", fsp_source);
			  Headers_str = Headers_str.replace("~fsp-destination~", fsp_destination);
			  Headers_str = Headers_str.replace("~fsp-encryption~", "");
			  Headers_str = Headers_str.replace("~fsp-signature~", "");
			  Headers_str = Headers_str.replace("~fsp-uri~", "");
			  Headers_str = Headers_str.replace("~fsp-http-method~", "");
			  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());  
			  
			  O_Headers = util.StringToJsonArray(Headers_str);
			  
			  String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
			  
	          String requesturl = webservice_details.get("URI").getAsString(); 
	          
	          requesturl = requesturl.replace("~payerRef~", payerRef);
	          
	          if(err == 0)
	          {
	        	  PAYLOAD = PAYLOAD.replace("~reversalState~", "CONFIRMED_HOLD");
	        	  PAYLOAD = PAYLOAD.replace("~reversalReason~", "successfull hold funds");
	          }
	          else
	          {
	        	  PAYLOAD = PAYLOAD.replace("~reversalState~", "CONFIRMED_WITHDRAWN");
	        	  PAYLOAD = PAYLOAD.replace("~reversalReason~", "failed to hold funds");
	          }
	          
			  webservice_details.addProperty("URI", requesturl);
			  webservice_details.addProperty("PAYLOAD", PAYLOAD);
			  webservice_details.add("Headers", O_Headers);
			  
			  String o_FLOW  =  "O"; 
		      String O_ReqRefID  = "";
		      String O_MSGURL = requesturl; 
		      String O_IP = "";
		      String O_PORT = "";
		      String O_HEAD_MSG = O_Headers.toString();
		      String O_BODY_MSG = PAYLOAD;
		      String O_INITATEDBY = "HPAY";
		      String O_Checksum = "";
		      String date = util.getCurrentDateTime();
		      
		      Request_001 Request_001 = new Request_001(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), SERVICECD, o_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
			 
			  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
			  
			  JsonObject API_Response = new JsonObject();
	       
			  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
			  
			  logger.debug("PUT /messageTransfersReversal API_Response :::: "+API_Response);
			  
			  int Response_Code = API_Response.get("Response_Code").getAsInt();
			  
			  if(Response_Code == 200)
			  {
				  String HOLD_DATE = util.getCurrentDate("dd-MMM-yyyy");
				  
				  if(err == 0)
		          {
					  String sql = "update pay002 set HOLD_DATE=?, HOLD_STATE=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
					   
					  Jdbctemplate.update(sql, new Object[] { HOLD_DATE, "CONFIRMED_HOLD", CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });
		          }
				  else
				  {
					  String sql = "update pay002 set HOLD_STATE=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
					   
					  Jdbctemplate.update(sql, new Object[] { "CONFIRMED_WITHDRAWN", CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });
				  }	  
			  }
			  
			  Response_001 Response = new Response_001(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), SERVICECD, "O", O_ReqRefID , requesturl , O_IP, O_PORT, O_HEAD_MSG, API_Response.toString(), "TIPS", "");
				
			  Res_Modal.Insert_Response_001(Response);
			  
			  details.addProperty("result", "success");
			  details.addProperty("stscode", Response_Code == 200 ? "HP00" : "HP301");
			  details.addProperty("message", Response_Code == 200 ? "Payment Hold Successfull !!" : "Payment Hold Failed !!");
			 
			  return details;
		}
		catch(Exception e)
		{
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in Call_TIPS_Hold_Request :::: "+details);
		}
		
		return details;
	}
	
	public JsonObject TIPS_Reversal_Transfer_Response(Job_005 Job, String CHANNEL, int err, String errormsg) /***  PUT /transfersReversal/{payerRef} ***/
	{
		String SERVICECD = "2111";
		
		JsonObject details = new JsonObject();  
		
		try
		{
			   Common_Utils util = new Common_Utils();
			
			   JsonObject Token_details = new JsonObject();
			  
			   String token = "";
			   
			   Token_details = tk.get_stored_token(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE());
			  
			   if(Token_details.get("Result").getAsString().equals("Success"))
			   {
			 	   token = Token_details.get("token").getAsString();
			   }
			   else
			   {
				   Token_details = Generate_Token();
				   
				   int Response_Code = Token_details.get("Response_Code").getAsInt();
					  
				   if(Response_Code == 200)
				   {
					    String res = Token_details.get("Response").getAsString();
						   
					    JsonObject Response = util.StringToJsonObject(res);
					   
					    token = Response.get("access_token").getAsString();
					   
					    Token_Info info = new Token_Info(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), token);
					   
					    tk.Check_and_Update_tokens(info);
				   }
				   else
				   {
					     logger.debug("Error in Generating token from TIPs :::: "+Token_details);
					    
						 PAY_002 pay = new PAY_002();
						  
						 pay.setREV_STATE("failed");
						 pay.setERRCD("HP111");
						 pay.setERRDES("payment gateway token generation failed");
						 pay.setCHCODE(CHANNEL);
						 pay.setPAYTYPE(Job.getPAYTYPE());
						 pay.setTRANTYPE(Job.getTRANTYPE());
						 pay.setREQREFNO(Job.getREFNO());
						 pay.setREQSL(Job.getREQSL());
					  
						 Req_Modal.Update_PAY_002_Reversal_Status(pay);
					    
					     details.addProperty("result",  "failed");
						 details.addProperty("stscode", "HP111");
						 details.addProperty("message", "payment gateway token generation failed");  
						 
						 return details; 
				   }
			  }
			   
			  String sql = "Select * from PAY002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and TRANREFNO=? and REQSL=?";
				  
			  List<PAY_002> Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() }, new Pay002_Mapper());
			
	          if(err == 0 && Info.size() !=0) 
	          {
	        	  String REV_DATE = util.getCurrentDate("dd-MMM-yyyy");
	        	  
	        	  sql = "update pay002 set ISREVERSED=?, REV_DATE=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
				   
				  Jdbctemplate.update(sql, new Object[] { "1", REV_DATE, "F", "1", "REVERSED", CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });
				  
	        	  JsonObject webservice_details = Ws.Get_Webserice_Info(Job.getSYSCODE(), Job.getPAYTYPE(), SERVICECD);
				  
				  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
				  
				  String Headers_str = O_Headers.toString();
				  
				  Headers_str = Headers_str.replace("~Token~", token);
				  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
				  Headers_str = Headers_str.replace("~fsp-source~", "013");
				  Headers_str = Headers_str.replace("~fsp-destination~", "tips");
				  Headers_str = Headers_str.replace("~fsp-encryption~", "");
				  Headers_str = Headers_str.replace("~fsp-signature~", "");
				  Headers_str = Headers_str.replace("~fsp-uri~", "");
				  Headers_str = Headers_str.replace("~fsp-http-method~", "");
				  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());  
				  
				  O_Headers = util.StringToJsonArray(Headers_str);
				  
				  String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
				  
		          String requesturl = webservice_details.get("URI").getAsString(); 
		          
		          requesturl = requesturl.replace("~payerRef~", Info.get(0).getPAYERID());
		          
	        	  PAYLOAD = PAYLOAD.replace("~switchRef~", Info.get(0).getTRAN_SWITCHREF());
	        	  PAYLOAD = PAYLOAD.replace("~payeeRef~", Info.get(0).getPAYEEID());
	        	  PAYLOAD = PAYLOAD.replace("~switchReversalRef~", Info.get(0).getREV_SWITCHREF());
	        	  PAYLOAD = PAYLOAD.replace("~payerReversalRef~", Info.get(0).getPAYER_REVREF());
	        	  PAYLOAD = PAYLOAD.replace("~payeeReversalRef~", Info.get(0).getPAYEE_REVREF());
	        	  PAYLOAD = PAYLOAD.replace("~reversalState~", Info.get(0).getREV_STATE());
	        	  PAYLOAD = PAYLOAD.replace("~completedTimestamp~", Info.get(0).getREV_DATE());
	        	  
	        	  webservice_details.addProperty("URI", requesturl);
				  webservice_details.addProperty("PAYLOAD", PAYLOAD);
				  webservice_details.add("Headers", O_Headers);
				  
				  String o_FLOW  =  "O"; 
			      String O_ReqRefID  = "";
			      String O_MSGURL = requesturl; 
			      String O_IP = "";
			      String O_PORT = "";
			      String O_HEAD_MSG = O_Headers.toString();
			      String O_BODY_MSG = PAYLOAD;
			      String O_INITATEDBY = "HPAY";
			      String O_Checksum = "";
			      String date = util.getCurrentDateTime();
			      
			      Request_001 Request_001 = new Request_001(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), SERVICECD, o_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
				 
				  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
				  
				  JsonObject API_Response = new JsonObject();
		       
				  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
				  
				  logger.debug("PUT /transfersReversal API_Response :::: "+API_Response);
				  
				  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
				  Response_001 Response = new Response_001(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), SERVICECD, "O", O_ReqRefID , requesturl , O_IP, O_PORT, O_HEAD_MSG, API_Response.toString(), "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);
				  
				  details.addProperty("result", "success");
				  details.addProperty("stscode", Response_Code == 200 ? "HP00" : "HP301");
				  details.addProperty("message", Response_Code == 200 ? "Payment Hold Successfull !!" : "Payment Hold Failed !!");
				 
				  return details;
	          }
	          else
			  {
				  PAY_002 pay = new PAY_002();
				  
				  pay.setREV_STATE("failed");
				  pay.setERRCD("HP200");
				  pay.setERRDES("transaction details not found !!");
				  pay.setCHCODE(CHANNEL);
				  pay.setPAYTYPE(Job.getPAYTYPE());
				  pay.setTRANTYPE(Job.getTRANTYPE());
				  pay.setREQREFNO(Job.getREFNO());
				  pay.setREQSL(Job.getREQSL());
			  
				  Req_Modal.Update_PAY_002_Reversal_Status(pay);
			    
			      details.addProperty("result",  "failed");
				  details.addProperty("stscode", "HP111");
				  details.addProperty("message", "payment gateway token generation failed");  
				 
				  return details; 
			  } 
		}
		catch(Exception e)
		{
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in Call_TIPS_Hold_Request :::: "+details);
		}
		
		return details;
	}
	
	public JsonObject Channel_Hold_Request_To_TIPS(Job_005 Job, String CHANNEL, int err, String errormsg) /***  PUT /messageTransfersReversal/{payerRef} (for update & notification ***/
	{
		String SERVICECD = "2120";
		
		JsonObject details = new JsonObject();  
		
		try
		{
			   Common_Utils util = new Common_Utils();
			
			   JsonObject Token_details = new JsonObject();
			  
			   String token = "";
			   
			   Token_details = tk.get_stored_token(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE());
			  
			   if(Token_details.get("Result").getAsString().equals("Success"))
			   {
			 	   token = Token_details.get("token").getAsString();
			   }
			   else
			   {
				   Token_details = Generate_Token();
				   
				   int Response_Code = Token_details.get("Response_Code").getAsInt();
					  
				   if(Response_Code == 200)
				   {
					    String res = Token_details.get("Response").getAsString();
						   
					    JsonObject Response = util.StringToJsonObject(res);
					   
					    token = Response.get("access_token").getAsString();
					   
					    Token_Info info = new Token_Info(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), token);
					   
					    tk.Check_and_Update_tokens(info);
				   }
				   else
				   {
					     logger.debug("Error in Generating token from TIPs :::: "+Token_details);
					    
						 PAY_002 pay = new PAY_002();
						  
						 pay.setREV_STATE("failed");
						 pay.setERRCD("HP111");
						 pay.setERRDES("payment gateway token generation failed");
						 pay.setCHCODE(CHANNEL);
						 pay.setPAYTYPE(Job.getPAYTYPE());
						 pay.setTRANTYPE(Job.getTRANTYPE());
						 pay.setREQREFNO(Job.getREFNO());
						 pay.setREQSL(Job.getREQSL());
					  
						 Req_Modal.Update_PAY_002_Reversal_Status(pay);
					    
					     details.addProperty("result",  "failed");
						 details.addProperty("stscode", "HP111");
						 details.addProperty("message", "payment gateway token generation failed");  
						 
						 return details; 
				   }
			  }
			   
			  JsonObject webservice_details = Ws.Get_Webserice_Info(Job.getSYSCODE(), Job.getPAYTYPE(), SERVICECD);
			
			  String fsp_source = "013";
			  String fsp_destination = "tips";
			  
			  webservice_details = Ws.Get_Webserice_Info(Job.getSYSCODE(), Job.getPAYTYPE(), SERVICECD);
			  
			  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
			  
			  String Headers_str = O_Headers.toString();
			  
			  Headers_str = Headers_str.replace("~Token~", token);
			  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
			  Headers_str = Headers_str.replace("~fsp-source~", fsp_source);
			  Headers_str = Headers_str.replace("~fsp-destination~", fsp_destination);
			  Headers_str = Headers_str.replace("~fsp-encryption~", "");
			  Headers_str = Headers_str.replace("~fsp-signature~", "");
			  Headers_str = Headers_str.replace("~fsp-uri~", "");
			  Headers_str = Headers_str.replace("~fsp-http-method~", "");
			  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());  
			  
			  O_Headers = util.StringToJsonArray(Headers_str);
			  
			  String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
			  
	          String requesturl = webservice_details.get("URI").getAsString(); 
	          
	          String sql = "Select * from PAY002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and TRANREFNO=? and REQSL=?";
			  
			  List<PAY_002> Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() }, new Pay002_Mapper());
			
	          if(Info.size() !=0) 
	          {
	        	  PAYLOAD = PAYLOAD.replace("~payerReversalRef~", Info.get(0).getPAYER_REVREF());
	        	  PAYLOAD = PAYLOAD.replace("~payerRef~", Info.get(0).getPAYERID());
	        	  PAYLOAD = PAYLOAD.replace("~payeeRef~", Info.get(0).getPAYEEID());
	        	  PAYLOAD = PAYLOAD.replace("~switchRef~", Info.get(0).getTRAN_SWITCHREF());
	        	  PAYLOAD = PAYLOAD.replace("~amount~", Info.get(0).getHOLD_AMOUNT());
	        	  PAYLOAD = PAYLOAD.replace("~currency~", Info.get(0).getHOLD_CURR());
	        	  PAYLOAD = PAYLOAD.replace("~reversalReason~", Info.get(0).getREV_REASON());
	        	  PAYLOAD = PAYLOAD.replace("~reversalState~", Info.get(0).getREV_STATE());

				  webservice_details.addProperty("URI", requesturl);
				  webservice_details.addProperty("PAYLOAD", PAYLOAD);
				  webservice_details.add("Headers", O_Headers);
				  
				  String o_FLOW  =  "O"; 
			      String O_ReqRefID  = System.currentTimeMillis()+"";
			      String O_MSGURL = requesturl; 
			      String O_IP = "";
			      String O_PORT = "";
			      String O_HEAD_MSG = O_Headers.toString();
			      String O_BODY_MSG = PAYLOAD;
			      String O_INITATEDBY = "HPAY";
			      String O_Checksum = "";
			      String date = util.getCurrentDate("dd-MMM-yyyy");
			      
			      Request_001 Request_001 = new Request_001(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), SERVICECD, o_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
				 
				  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
				  
				  JsonObject API_Response = new JsonObject();
		       
				  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
				  
				  logger.debug("POST /messageTransfersReversal API_Response :::: "+API_Response);
				  
				  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
				  if(Response_Code == 202)
				  {
					  sql = "update pay002 set HOLD_STATE=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
						   
					  Jdbctemplate.update(sql, new Object[] { "REQUESTED", CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL()});  
				  }
				  
				  Response_001 Response = new Response_001(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), SERVICECD, "O", O_ReqRefID , requesturl , O_IP, O_PORT, O_HEAD_MSG, API_Response.toString(), "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);
				  
				  details.addProperty("result", "success");
				  details.addProperty("stscode", Response_Code == 202 ? "HP00" : "HP301");
				  details.addProperty("message", Response_Code == 202 ? "Payment Hold Requested !!" : "Payment hold failed !!");
				 
				  return details;
	          }
	          else
			  {
				  sql = "update pay002 set HOLD_STATE=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
				   
				  Jdbctemplate.update(sql, new Object[] { "failed", CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL()});
			    
			      details.addProperty("result",  "failed");
				  details.addProperty("stscode", "HP111");
				  details.addProperty("message", "payment gateway token generation failed");  
				 
				  return details; 
			  } 
		}
		catch(Exception e)
		{
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in Call_TIPS_Hold_Request :::: "+details);
		}
		
		return details;
	}
	
	public JsonObject Get_Account_Lookup_Details(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE  = "EXIM";
		String CHCODE  = Headers.get("ChannelID").getAsString();  
		String SERVICECD = ""; 
		String SYSCODE = "HP"; 
		
		try
		{
			 JsonObject Request = new Common_Utils().StringToJsonObject(request);
			
			 JsonObject aclookup = Request.get("aclookup").getAsJsonObject();			
			 
			 String paytype = aclookup.get("paytype").getAsString();   
			 
			 JsonObject PRIMARYARG = aclookup.get("primaryarg").getAsJsonObject();	
 			 
			 String date = aclookup.get("date").getAsString();
			 String reqrefid = aclookup.get("reqrefid").getAsString();
			 String initiatedby = aclookup.get("initiatedby").getAsString();
			 
			 String acno = PRIMARYARG.get("acno").getAsString();
			 String custno = PRIMARYARG.has("custno") ? PRIMARYARG.get("custno").getAsString() : "";
			 String identifier = PRIMARYARG.get("identifier").getAsString();
			 String identifiertype = PRIMARYARG.get("identifiertype").getAsString();
			 String fspid = PRIMARYARG.has("bankcode") ? PRIMARYARG.get("bankcode").getAsString() : "";
			 String nin = PRIMARYARG.has("nin") ? PRIMARYARG.get("nin").getAsString() : ""; 
			 String msisdn = PRIMARYARG.has("msisdn") ? PRIMARYARG.get("msisdn").getAsString() : "";
			 String firstname = PRIMARYARG.has("firstname") ? PRIMARYARG.get("firstname").getAsString() : "";
			 String middlename = PRIMARYARG.has("middlename") ? PRIMARYARG.get("middlename").getAsString() : "";
			 String lastname = PRIMARYARG.has("lastname") ? PRIMARYARG.get("lastname").getAsString() : "";
			 String fullname = PRIMARYARG.has("fullname") ? PRIMARYARG.get("fullname").getAsString() : "";
			
			 if(fspid.equals("013"))
			 {
				 SERVICECD = "2003";  /*** Participant Service (Exim Ac loolup) ***/
			 }
			 else
			 {
				 SERVICECD = "2004";  /*** Parties Service (Outside bank Ac lookup) ***/
			 }
			 
			 String MSGTYPE 	=  SERVICECD;  									
			 String FLOW    	=  "O"; 									  									
			 String IP      	=  Headers.get("IPAddress").getAsString(); 
			 String PORT    	=  req.getRemotePort()+"";  									
			 String HEAD_MSG    =  Headers.toString();  				
			
			 String sql = "Select count(*) from request001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and MSGTYPE=? and FLOW=? and REQDATE=? and UNIREFNO=?"; 
			 
			 int count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, paytype, MSGTYPE, FLOW , date, reqrefid }, Integer.class);
			 
			 if(count !=0)
			 {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP94");
				  details.addProperty("message", "duplicate request within to time limit !!");
				 
				  return details;
			 }
			 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, paytype, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {
				  logger.debug("Error in Get_Webserice_Info :::: "+webservice_details);
				 
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP103");
				  details.addProperty("message", webservice_details.get("Message").getAsString()); 
				 	  
				  return details; 			 	  
			 }
			 		
			  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
			  
			  Common_Utils util = new Common_Utils();
			    
			  JsonObject Token_details = new JsonObject();
					  
			  String token = "";
			  
			  String VERSION  = Headers.get("VERSION").getAsString();  
				  
			  if(VERSION.equals("demo"))  /*** simulator Response Code ***/
			  {
				  Token_details = Simulator.Get_Simulator_Response("", "", SUBORGCODE, SYSCODE, "TIPS", "900", FLOW);  /*** simulator Response ***/
				  
				  Token_details = util.StringToJsonObject(Token_details.get("Response").getAsString());
			  }
			  else
			  {
				  Token_details = tk.get_stored_token(SUBORGCODE, CHCODE, paytype);
			  }
			 
			  if(Token_details.get("Result").getAsString().equals("Success"))
			  {
				   token = Token_details.get("token").getAsString();
			  }
			  else
			  {
				   Token_details = Generate_Token();
				   
				   int Response_Code = Token_details.get("Response_Code").getAsInt();
					  
				   if(Response_Code == 200)
				   {
					    String res = Token_details.get("Response").getAsString();
						   
					    JsonObject Response = util.StringToJsonObject(res);
					   
					    token = Response.get("access_token").getAsString();
					   
					    Token_Info info = new Token_Info(SUBORGCODE, CHCODE, paytype, token);
					   
					    tk.Check_and_Update_tokens(info);
				   }
				   else
				   {
					    logger.debug("Error in Generating token from TIPs :::: "+Token_details);
					    
					    details.addProperty("result",  "failed");
						details.addProperty("stscode", "HP111");
						details.addProperty("message", "payment gateway token generation failed");  
						 
						return details; 
				   }
			  }
			  
			  String Headers_str = O_Headers.toString();
			  
			  Headers_str = Headers_str.replace("~Token~", token);
			  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
			  Headers_str = Headers_str.replace("~fsp-source~", "013");
			  Headers_str = Headers_str.replace("~fsp-destination~", fspid);
			  Headers_str = Headers_str.replace("~fsp-encryption~", "");
			  Headers_str = Headers_str.replace("~fsp-signature~", "");
			  Headers_str = Headers_str.replace("~fsp-uri~", "");
			  Headers_str = Headers_str.replace("~fsp-http-method~", "");
			  Headers_str = Headers_str.replace("~requestid~", reqrefid);
			  Headers_str = Headers_str.replace("~LookupId~", System.currentTimeMillis()+"");
			  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());  
			
			  O_Headers = util.StringToJsonArray(Headers_str);
			 
			  String urlparam = webservice_details.get("PAYLOAD").getAsString();
			 
	          String requesturl = webservice_details.get("URI").getAsString(); 
	          
	          requesturl = requesturl.replace("~identifierType~", identifiertype);
	          requesturl = requesturl.replace("~identifier~", identifier);
				
			  webservice_details.addProperty("URI", requesturl);
			  webservice_details.add("Headers", O_Headers);
	       
		      String O_FLOW  =  "O"; 
		      String O_ReqRefID  = reqrefid;
		      String O_MSGURL = requesturl; 
		      String O_IP = "";
		      String O_PORT = "";
		      String O_HEAD_MSG = O_Headers.toString();
		      String O_BODY_MSG = urlparam;
		      String O_Method = webservice_details.get("METHOD").getAsString(); 
		      String O_INITATEDBY = "HPAY";
		      String O_Checksum = "";
		     
		      Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, paytype, SERVICECD, O_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
			 
			  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
			  
			  JsonObject API_Response = new JsonObject();
			  
			  if(VERSION.equals("demo"))  /*** simulator Response Code ***/
			  {
				  API_Response = Simulator.Get_Simulator_Response(requesturl, O_Method, SUBORGCODE, SYSCODE, "TIPS", MSGTYPE, FLOW);  /*** simulator Response ***/
			  }
			  else
			  {
				  if(fspid.equals("013"))
				  {
					  if(identifiertype.equals("MSISDN"))	 /*** Identifier type ( Possible values : BANK, ALIAS, MSISDN) ***/ 
					  {
						  API_Response = Retrieve_Account_Information(identifiertype, identifier);
					  }
					  else 
					  {
						  API_Response = Retrieve_Account_Information(identifiertype, acno);
					  }
					 
					  if(API_Response.has("Informations"))
					  {
						  JsonObject AC_detail = API_Response.get("Informations").getAsJsonObject();
						 
						  String s_accoutname = AC_detail.get("CUSTOMERNAME").getAsString(); 		 
						
						  JsonObject PRIELEMENTS = new JsonObject();
						 
						  PRIELEMENTS.addProperty("identifier", identifiertype.equals("MSISDN") ? identifier : acno);
						  PRIELEMENTS.addProperty("status", "Active");	
					
						  JsonObject acinfo = new JsonObject(); 
						  JsonObject acdetails = new JsonObject();					   
						  JsonObject secelements = new JsonObject();

						  acinfo.addProperty("paytype", paytype);
						  acinfo.addProperty("date", date);
						  acinfo.addProperty("reqrefid", reqrefid);
						  acinfo.addProperty("initiatedby", initiatedby);
						  
						  acdetails.addProperty("acno", acno);
						  
						  secelements.addProperty("custno", custno); 
						  secelements.addProperty("msisdn", msisdn); 
						  secelements.addProperty("nin", nin);
						  secelements.addProperty("firstname", firstname); 
						  secelements.addProperty("middlename", middlename); 
						  secelements.addProperty("lastname", lastname); 
						  secelements.addProperty("fullname", s_accoutname); 
						  secelements.addProperty("fspid", fspid); 
						
						  acdetails.add("prielements", PRIELEMENTS);
						  acdetails.add("secelements", secelements);
						  
						  acinfo.add("acdetails", acdetails);
						  
						  details.add("acinfo", acinfo);  
						  
						  details.addProperty("result", "success");  
						  details.addProperty("stscode", "HP00");
						  details.addProperty("message", "account found !!");  
						  
						  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), "TIPS", "");
							
						  Res_Modal.Insert_Response_001(Response);
						  
						  return details;
					  }
					  else
					  {
						  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), "TIPS", "");
							
						  Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	
						     
						  details.addProperty("result", "failed");
						  details.addProperty("stscode", "HP14");
						  details.addProperty("message", "invalid account !!");
						   
						  return details;
					  }
				  }
				  else
				  {
					  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
				  }  	
			  }
			 
	          logger.debug("Ac lookup API_Response :::: "+API_Response);
	         
	          if(API_Response.get("Result").getAsString().equals("Success"))
			  {
				  String response = API_Response.get("Response").getAsString();
				  
				  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
				  if(Response_Code == 200 && !response.contains("errorInformation"))
				  {
					  JsonObject PRIELEMENTS = new JsonObject();
					  
					  PRIELEMENTS = new Common_Utils().StringToJsonObject(response);
					  
					  PRIELEMENTS.addProperty("identifier", PRIELEMENTS.has("identifier") ? PRIELEMENTS.get("identifier").getAsString() : "");
					  PRIELEMENTS.addProperty("status", PRIELEMENTS.has("status") ? PRIELEMENTS.get("status").getAsString() : "");	
				
					  JsonObject acinfo = new JsonObject(); 
					  JsonObject acdetails = new JsonObject();					   
					  JsonObject secelements = new JsonObject();

					  acinfo.addProperty("paytype", paytype);
					  acinfo.addProperty("date", date);
					  acinfo.addProperty("reqrefid", reqrefid);
					  acinfo.addProperty("initiatedby", initiatedby);
					  
					  acdetails.addProperty("acno", acno);
					  
					  if(PRIMARYARG.has("custno"))  secelements.addProperty("custno", custno); 
					  if(PRIMARYARG.has("msisdn"))  secelements.addProperty("msisdn", msisdn); 
					  if(PRIMARYARG.has("nin"))  secelements.addProperty("nin", nin);
					  if(PRIMARYARG.has("firstname"))  secelements.addProperty("firstname", firstname); 
					  if(PRIMARYARG.has("middlename"))  secelements.addProperty("middlename", middlename); 
					  if(PRIMARYARG.has("lastname"))  secelements.addProperty("lastname", lastname); 
					  if(PRIMARYARG.has("fullname"))  secelements.addProperty("fullname", fullname); 
					  if(PRIMARYARG.has("bankcode"))  secelements.addProperty("fspid", fspid); 
					
					  acdetails.add("prielements", PRIELEMENTS);
					  acdetails.add("secelements", secelements);
					  
					  acinfo.add("acdetails", acdetails);
					  
					  details.add("acinfo", acinfo);  
					  
					  details.addProperty("result", "success");  
					  details.addProperty("stscode", "HP00");
					  details.addProperty("message", "account found !!");  
				  }
				  else
				  {
					  String errorCode = "HP14";
					  String errorDescription = "Invalid Transaction Account";
					  
					  /*if(response.contains("errorInformation"))
					  {
						  JsonObject errorInformation = new Common_Utils().StringToJsonObject(response);
						  
						  errorCode = errorInformation.has("errorCode") ? errorInformation.get("errorCode").getAsString() : errorCode;
						  errorDescription = errorInformation.has("errorDescription") ? errorInformation.get("errorDescription").getAsString() : errorDescription;	  
					  }*/
					  
					  details.addProperty("result", "failed");  
					  details.addProperty("stscode", errorCode);  
					  details.addProperty("message", errorDescription);  	 
				  }
				  
				  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, response, "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	
			 }
	         else
	         { 
	        	 Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), "TIPS", "");
					
			     Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	
			     
			     details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06");
				 details.addProperty("message", API_Response.get("Message").getAsString()); 
  
	        	 return details;
	         }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());  

			 logger.debug("Exception in Get_Account_Lookup_Details :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Get_All_Account_Lookup_Details(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE  = "EXIM";
		String CHCODE  = Headers.get("ChannelID").getAsString();  
		String SERVICECD = "2005"; 
		String SYSCODE = "HP"; 
		
		try
		{
			 JsonObject Request = new Common_Utils().StringToJsonObject(request);
			
			 JsonObject aclookup = Request.get("aclookup").getAsJsonObject();			
			 
			 String paytype = aclookup.get("paytype").getAsString();   
			 
			 JsonObject PRIMARYARG = aclookup.get("primaryarg").getAsJsonObject();	
 			 
			 String date = aclookup.get("date").getAsString();
			 String reqrefid = aclookup.get("reqrefid").getAsString();
			 String initiatedby = aclookup.get("initiatedby").getAsString();
			 
			 String identifiertype = PRIMARYARG.get("identifiertype").getAsString();
			 String fspid = PRIMARYARG.has("bankcode") ? PRIMARYARG.get("bankcode").getAsString() : "";
			
			 String MSGTYPE 	=  SERVICECD;  									
			 String FLOW    	=  "O"; 									  									
			 String IP      	=  Headers.get("IPAddress").getAsString(); 
			 String PORT    	=  req.getRemotePort()+"";  									
			 String HEAD_MSG    =  Headers.toString();  				
			
			 String sql = "Select count(*) from request001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and MSGTYPE=? and FLOW=? and REQDATE=? and UNIREFNO=?"; 
			 
			 int count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, paytype, MSGTYPE, FLOW , date, reqrefid }, Integer.class);
			 
			 if(count !=0)
			 {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP94");
				  details.addProperty("message", "duplicate request within to time limit !!");
				 
				  return details;
			 }
			 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, paytype, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {
				 logger.debug("Error in Get_Webserice_Info :::: "+webservice_details);
				 	  
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP103");
				 details.addProperty("message", webservice_details.get("Message").getAsString()); 
				 	  
				 return details; 
			 }
			 		
			  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
			  
			  Common_Utils util = new Common_Utils();
			    
			  JsonObject Token_details = new JsonObject();
					  
			  String token = "";
			  
			  String VERSION  = Headers.get("VERSION").getAsString();  
			  
			  if(VERSION.equals("demo"))
			  {
				  Token_details = Simulator.Get_Simulator_Response("", "", SUBORGCODE, SYSCODE, "TIPS", "900", FLOW);  /*** simulator Response ***/
				  
				  Token_details = util.StringToJsonObject(Token_details.get("Response").getAsString());
			  }
			  else
			  {
				  Token_details = tk.get_stored_token(SUBORGCODE, CHCODE, paytype);
			  }
				  
			  if(Token_details.get("Result").getAsString().equals("Success"))
			  {
				   token = Token_details.get("token").getAsString();
			  }
			  else
			  {
				   Token_details = Generate_Token();
				   
				   int Response_Code = Token_details.get("Response_Code").getAsInt();
					  
				   if(Response_Code == 200)
				   {
					    String res = Token_details.get("Response").getAsString();
						   
					    JsonObject Response = util.StringToJsonObject(res);
					   
					    token = Response.get("access_token").getAsString();
					   
					    Token_Info info = new Token_Info(SUBORGCODE, CHCODE, paytype, token);
					   
					    tk.Check_and_Update_tokens(info);
				   }
				   else
				   {
					    logger.debug("Error in Generating token from TIPs :::: "+Token_details);
						 
					    details.addProperty("result",  "failed");
						details.addProperty("stscode", "HP111");
						details.addProperty("message", "payment gateway token generation failed");  
						 
						return details; 
				   }
			  }
			  
			  String Headers_str = O_Headers.toString();
			  
			  Headers_str = Headers_str.replace("~Token~", token);
			  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
			  Headers_str = Headers_str.replace("~fsp-source~", fspid);
			  Headers_str = Headers_str.replace("~fsp-destination~", "tips");
			  Headers_str = Headers_str.replace("~fsp-encryption~", "");
			  Headers_str = Headers_str.replace("~fsp-signature~", "");
			  Headers_str = Headers_str.replace("~fsp-uri~", "");
			  Headers_str = Headers_str.replace("~fsp-http-method~", "");
			  Headers_str = Headers_str.replace("~requestid~", reqrefid);
			  Headers_str = Headers_str.replace("~LookupId~", System.currentTimeMillis()+"");
			  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());  
			
			  O_Headers = util.StringToJsonArray(Headers_str);
			  
			  String urlparam = webservice_details.get("PAYLOAD").getAsString();
			 
	          String requesturl = webservice_details.get("URI").getAsString(); 
	          
	          requesturl = requesturl.replace("~identifierType~", identifiertype);
	         
			  webservice_details.addProperty("URI", requesturl);
			  webservice_details.add("Headers", O_Headers);
	       
		      String O_FLOW  =  "O"; 
		      String O_ReqRefID  = reqrefid;
		      String O_MSGURL = requesturl; 
		      String O_Method = webservice_details.get("METHOD").getAsString(); 
		      String O_IP = "";
		      String O_PORT = "";
		      String O_HEAD_MSG = O_Headers.toString();
		      String O_BODY_MSG = urlparam;
		      String O_INITATEDBY = "HPAY";
		      String O_Checksum = "";
		     
		      Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, paytype, SERVICECD, O_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
			 
			  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
			  
			  JsonObject API_Response = new JsonObject();
			  
			  if(VERSION.equals("demo"))
			  {
				   API_Response = Simulator.Get_Simulator_Response(requesturl, O_Method, SUBORGCODE, SYSCODE, "TIPS", MSGTYPE, FLOW);  /*** simulator Response ***/
			  }
			  else
			  {
				   API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
			  }
			  
	          logger.debug("All Ac lookup API_Response :::: "+API_Response);
	         
	          if(API_Response.get("Result").getAsString().equals("Success"))
			  {
				  String response = API_Response.get("Response").getAsString();
				  
				  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
				  if(Response_Code == 200)
				  {
					  JsonObject PRIELEMENTS = new JsonObject();
					  
					  JsonArray RESPONSE_ARR = new Common_Utils().StringToJsonArray(response); 
					  
					  JsonObject acinfo = new JsonObject(); 
					  JsonObject acdetails = new JsonObject();					   
					 
					  acinfo.addProperty("paytype", paytype);
					  acinfo.addProperty("date", date);
					  acinfo.addProperty("reqrefid", reqrefid);
					  acinfo.addProperty("initiatedby", initiatedby);
					  
					  PRIELEMENTS.addProperty("identifier", fspid);
					  
					  PRIELEMENTS.add("all_identifiers", RESPONSE_ARR);
					  
					  acdetails.add("prielements", PRIELEMENTS);
					  
					  acinfo.add("acdetails", acdetails);
					  
					  details.add("acinfo", acinfo);  
					  
					  details.addProperty("result", "success");  
					  details.addProperty("stscode", "HP00");
					  details.addProperty("message", "accounts found !!");  
				  }
				  else
				  {
					  String errorCode = "400";
					  String errorDescription = "Unspecified Error";
					  
					  if(response.contains("errorInformation"))
					  {
						  JsonObject errorInformation = new Common_Utils().StringToJsonObject(response);
						  
						  errorCode = errorInformation.has("errorCode") ? errorInformation.get("errorCode").getAsString() : errorCode;
						  errorDescription = errorInformation.has("errorDescription") ? errorInformation.get("errorDescription").getAsString() : errorDescription;	  
					  }
					  
					  details.addProperty("result", "Failed");  
					  details.addProperty("stscode", "HP06");  
					  details.addProperty("message", errorDescription);  	 
				  }
				  
				  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, response, "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	
			 }
	         else
	         { 
	        	 Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), "TIPS", "");
					
			     Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	  
  
			     details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06");
				 details.addProperty("message", API_Response.get("Message").getAsString()); 
  
	        	 return details;
	         }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());  

			 logger.debug("Exception in Get_All_Account_Lookup_Details :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Register_Identifier_details(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE  = "EXIM";
		String CHCODE  = Headers.get("ChannelID").getAsString();  
		String SERVICECD = "2000"; 
		String SYSCODE = "HP"; 
		
		try
		{
			 JsonObject Request = new Common_Utils().StringToJsonObject(request);
			
			 JsonObject Identifierreg = Request.get("identifierreg").getAsJsonObject();			
			 
			 String paytype = Identifierreg.get("paytype").getAsString();   /**** Taken as Requested Date ****/
			 
			 JsonObject primaryarg = Identifierreg.get("primaryarg").getAsJsonObject();	
 			 
			 String date = Identifierreg.get("date").getAsString();
			 String reqrefid = Identifierreg.get("reqrefid").getAsString();
			 String initiatedby = Identifierreg.get("initiatedby").getAsString();
			 
			 String identifier = primaryarg.get("identifier").getAsString();
			 String acno = primaryarg.get("acno").getAsString();
			 String identifiertype = primaryarg.get("identifiertype").getAsString();
			 String fspId = primaryarg.get("bankcode").getAsString();
			 String accountType = "BANK";  
			 String accountCategory = "PERSON";
			 String custno = primaryarg.has("custno") ? primaryarg.get("custno").getAsString() : "";
			 String msisdn = primaryarg.has("msisdn") ? primaryarg.get("msisdn").getAsString() : "";
			 String identity_type = primaryarg.has("nin") ? "nin" : "";
			 String identity_value = primaryarg.has("nin") ? primaryarg.get("nin").getAsString() : "";
			 String FullName = primaryarg.has("fullname") ? primaryarg.get("fullname").getAsString() : "";
			 
			 String MSGTYPE 	=  SERVICECD;  									/*** service code hardcoded value **/
			 String FLOW    	=  "O"; 									  									
			 String IP      	=  Headers.get("IPAddress").getAsString(); 
			 String PORT    	=  req.getRemotePort()+"";  									
			 String HEAD_MSG    =  Headers.toString();  				
			
			 String sql = "Select count(*) from request001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and MSGTYPE=? and FLOW=? and REQDATE=? and UNIREFNO=?"; 
			 
			 int count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, paytype, MSGTYPE, FLOW , date, reqrefid }, Integer.class);
			 
			 if(count !=0)
			 {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP94");
				  details.addProperty("message", "duplicate request within to time limit !!");
				 
				  return details;
			 }
			 
			 String VERSION  = Headers.get("VERSION").getAsString();  
			  
			 if(!VERSION.equals("demo"))  /*** simulator Response Code ***/
			 {
				 JsonObject Account_Info = Retrieve_Account_Information(acno);
				 
				 Account_Information Ac_Info = new Account_Information();
				 
				 if(Account_Info.get("result").getAsString().equals("success"))   
				 {
					 Ac_Info = new Gson().fromJson(Account_Info.get("Informations").getAsJsonObject(), Account_Information.class);
					 
					 custno = custno.length() == 0 ? Ac_Info.getCUSTOMERNO() : custno;
					 msisdn = msisdn.length() == 0 ? Ac_Info.getMOBILENO() : msisdn;
					 FullName = FullName.length() == 0 ? Ac_Info.getCUSTOMERNAME() : FullName;
				 }
				 else
				 {
					 details.addProperty("result", "failed");
					 details.addProperty("stscode", "HP06"); 
					 details.addProperty("message", "Not an Exim Bank Account !!");
					 
					 return details;
				 }
			 }
			 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, paytype, SERVICECD);
			 
			  if(!webservice_details.get("Result").getAsString().equals("Success"))
			  {
				     logger.debug("Error in Get_Webserice_Info :::: "+webservice_details);
				 	  
					 details.addProperty("result", "failed");
					 details.addProperty("stscode", "HP103");
					 details.addProperty("message", webservice_details.get("Message").getAsString()); 
					 	  
					 return details; 
			  }
			 	
			  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
			 
			  Common_Utils util = new Common_Utils();
			    
			  JsonObject Token_details = new JsonObject();
					  
			  String token = "";
			 
			  if(VERSION.equals("demo"))  /*** simulator Response Code ***/
			  {
				  Token_details = Simulator.Get_Simulator_Response("", "", SUBORGCODE, SYSCODE, "TIPS", "900", FLOW);  /*** simulator Response ***/
				  
				  Token_details = util.StringToJsonObject(Token_details.get("Response").getAsString());
			  }
			  else
			  {
				  Token_details = tk.get_stored_token(SUBORGCODE, CHCODE, paytype);
			  }
  
			  if(Token_details.get("Result").getAsString().equals("Success"))
			  {
				   token = Token_details.get("token").getAsString();
			  }
			  else
			  {
				   Token_details = Generate_Token();
				   
				   int Response_Code = Token_details.get("Response_Code").getAsInt();
					  
				   if(Response_Code == 200)
				   {
					    String res = Token_details.get("Response").getAsString();
					   
					    JsonObject Response = util.StringToJsonObject(res);
					   
					    token = Response.get("access_token").getAsString();
					   
					    Token_Info info = new Token_Info(SUBORGCODE, CHCODE, paytype, token);
					   
					    tk.Check_and_Update_tokens(info);
				   }
				   else
				   {
					    logger.debug("Error in Generating token from TIPs :::: "+Token_details);
						 
					    details.addProperty("result",  "failed");
						details.addProperty("stscode", "HP111");
						details.addProperty("message", "payment gateway token generation failed");  
						 
						return details; 
				   }
			  }
				  
			  String urlparam = webservice_details.get("PAYLOAD").getAsString();
			  
			  urlparam = urlparam.replace("~identifier~", identifier);
			 
			  webservice_details.addProperty("PAYLOAD", urlparam);
			  
			  String Headers_str = O_Headers.toString();
			  
			  Headers_str = Headers_str.replace("~Token~", token);
			  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
			  Headers_str = Headers_str.replace("~fsp-source~", "013");
			  Headers_str = Headers_str.replace("~fsp-destination~", "tips");
			  Headers_str = Headers_str.replace("~fsp-encryption~", "");
			  Headers_str = Headers_str.replace("~fsp-signature~", get_signature(urlparam));
			  Headers_str = Headers_str.replace("~fsp-uri~", "");
			  Headers_str = Headers_str.replace("~fsp-http-method~", "");
			  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());
			  Headers_str = Headers_str.replace("~request-id~", reqrefid);
			  
			  O_Headers = util.StringToJsonArray(Headers_str);
			
			  JsonObject final_headers = new JsonObject();
			  
			  for(int i=0;i<O_Headers.size();i++)
			  {
				  JsonObject head = O_Headers.get(i).getAsJsonObject();
				  
				  final_headers.addProperty(head.get("Key").getAsString(), head.get("Value").getAsString());
			  }
			  
	          String requesturl = webservice_details.get("URI").getAsString(); 
	          
	          requesturl = requesturl.replace("~identifierType~", identifiertype);
	          
			  webservice_details.addProperty("URI", requesturl);
			  webservice_details.add("Headers", O_Headers);
	       
		      String O_FLOW  =  "O"; 
		      String O_ReqRefID  = reqrefid;
		      String O_MSGURL = requesturl; 
		      String O_Method = webservice_details.get("METHOD").getAsString(); 
		      String O_IP = "";
		      String O_PORT = "";
		      String O_HEAD_MSG = O_Headers.toString();
		      String O_BODY_MSG = urlparam;
		      String O_INITATEDBY = "HPAY";
		      String O_Checksum = "";
		      
		      String Request_Date = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(System.currentTimeMillis());
		      
		      TIPS001 TIPS_001 = new TIPS001();
		      
		      TIPS_001.setSUBORGCODE(SUBORGCODE);
		      TIPS_001.setREQUESTID(reqrefid); 
		      TIPS_001.setREQUESTDATE(Request_Date); 
		      TIPS_001.setFSP_SRCID(final_headers.get("fsp-source").getAsString()); 
		      TIPS_001.setFSP_DESID(final_headers.get("fsp-destination").getAsString()); 
		      TIPS_001.setIDENTIFIERNO(identifier); 
		      TIPS_001.setSUBIDENT("");
		      TIPS_001.setSTATUS(""); 
		      TIPS_001.setERRORCD(""); 
		      TIPS_001.setERRDESC(""); 
		      TIPS_001.setIDENTIFIERTYPE(identifiertype);
		      TIPS_001.setFSPID(fspId); 
		      TIPS_001.setIDENTIFIERNAME(FullName);    
		      TIPS_001.setACCAT(accountCategory); 
		      TIPS_001.setACTYPE(accountType); 
		      TIPS_001.setACCOUNTNO(acno);
		      TIPS_001.setCUSTNO(custno);
		      TIPS_001.setMSISDN(msisdn);
		      
		      Insert_TIPS_001(TIPS_001);   /*** Insert into TIPS_001 ****/
		      
		      Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, paytype, SERVICECD, O_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
			 
			  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
			  
			  JsonObject API_Response = new JsonObject();
			  
			  if(VERSION.equals("demo"))  /*** simulator Response Code ***/
			  {
				  API_Response = Simulator.Get_Simulator_Response(requesturl, O_Method, SUBORGCODE, SYSCODE, "TIPS", MSGTYPE, FLOW);  /*** simulator Response ***/
			  }
			  else
			  {
				  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
			  }
			      
	          logger.debug("Register_Identifier API_Response :::: "+API_Response);
	         
	          if(API_Response.get("Result").getAsString().equals("Success"))
			  {
	        	  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
	        	  String response = API_Response.get("Response").getAsString();
	        	  
				  if(Response_Code == 200)
				  {
					  JsonObject PRIELEMENTS = new JsonObject();
					  
					  PRIELEMENTS = new Common_Utils().StringToJsonObject(response);
					  
					  PRIELEMENTS.addProperty("identifier", PRIELEMENTS.has("identifier") ? PRIELEMENTS.get("identifier").getAsString() : "");
					  PRIELEMENTS.addProperty("status", PRIELEMENTS.has("status") ? PRIELEMENTS.get("status").getAsString() : "");
				 
					  JsonObject IdentifierInfo = new JsonObject(); 
					  JsonObject IdentifierDetails = new JsonObject();					  
				  
					  IdentifierInfo.addProperty("paytype", paytype);
					  IdentifierInfo.addProperty("date", date);
					  IdentifierInfo.addProperty("reqrefid", reqrefid);
					  IdentifierInfo.addProperty("initiatedby", initiatedby);
					  
					  PRIELEMENTS.addProperty("identifiertype", identifiertype);
					  PRIELEMENTS.addProperty("acno", acno);
					  
					  if(primaryarg.has("custno")) 
					  {
						  PRIELEMENTS.addProperty("custno", custno); 
					  }
					  
					  IdentifierDetails.add("prielements", PRIELEMENTS);
					 
					  IdentifierInfo.add("identifierdetails", IdentifierDetails);
					  
					  details.add("identifierinfo", IdentifierInfo);  
					  	 		 
					  TIPS002 TIPS_002 = new TIPS002();

					  TIPS_002.setSUBORGCODE(SUBORGCODE); 
					  TIPS_002.setFSP_SRCID(final_headers.get("fsp-source").getAsString()); 
					  TIPS_002.setFSP_DESID(final_headers.get("fsp-destination").getAsString()); 
				      TIPS_002.setIDENTIFIERNO(identifier); 
					  TIPS_002.setIDENTIFIERTYPE(identifiertype); 
					  TIPS_002.setFSPID(fspId); 
					  TIPS_002.setIDENTIFIERNAME(FullName); 
					  TIPS_002.setACCAT(accountCategory); 
					  TIPS_002.setACTYPE(accountType); 
					  TIPS_002.setACCOUNTNO(acno);
					  TIPS_002.setCUSTNO(custno);
					  TIPS_002.setIDTYPE1(identity_type); 
					  TIPS_002.setIDTYPEVALUE1(identity_value); 
					  TIPS_002.setREGDATE(Request_Date); 
					  TIPS_002.setREGDATETIME(new java.sql.Timestamp(new java.util.Date().getTime())); 
					  TIPS_002.setSTATUS(PRIELEMENTS.get("status").getAsString()); 
					  TIPS_002.setMSISDN(msisdn);
					  
					  Insert_TIPS_002(TIPS_002); 
					  
					  details.addProperty("result", "Success");  
					  details.addProperty("stscode", "HP00");  
					  details.addProperty("message", "Account Registered Successfully !!");  	    
				  }			  
				  else
				  {
					  String errorCode = "400";
					  String errorDescription = "Unspecified Error";
					  
					  if(response.contains("errorInformation"))
					  {
						  JsonObject errorInformation = new Common_Utils().StringToJsonObject(response);
						  
						  errorCode = errorInformation.has("errorCode") ? errorInformation.get("errorCode").getAsString() : errorCode;
						  errorDescription = errorInformation.has("errorDescription") ? errorInformation.get("errorDescription").getAsString() : errorDescription;	  
					  }
					  
					  TIPS001 tips_001 = new TIPS001();
					  
					  tips_001.setSUBORGCODE(SUBORGCODE);
					  tips_001.setERRORCD(errorCode);
					  tips_001.setERRDESC(errorDescription);
					  tips_001.setREQUESTID(reqrefid); 
					  tips_001.setREQUESTDATE(Request_Date); 
					  tips_001.setIDENTIFIERNO(identifier); 
					  
					  Update_TIPS_001(tips_001);
					  
					  details.addProperty("result", "failed");  
					  details.addProperty("stscode", "HP06");  
					  details.addProperty("message", errorDescription);  
				  }
				  
				  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, response, "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	
			 }
	         else
	         {
	        	 Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), "TIPS", "");
					
			     Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	  
  
			     details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06");
				 details.addProperty("message", API_Response.get("Message").getAsString()); 
  
	        	 return details;
	         }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");  
			 details.addProperty("message", e.getLocalizedMessage());  

			 logger.debug("Exception in Register_Identifier_details :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Update_Identifier_details(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE  = "EXIM";
		String CHCODE  = Headers.get("ChannelID").getAsString();  
		String SERVICECD = "2001"; 
		String SYSCODE = "HP"; 
		
		try
		{
			 JsonObject Request = new Common_Utils().StringToJsonObject(request);
			
			 JsonObject identifierupdate = Request.get("identifierupdate").getAsJsonObject();			
			 
			 String paytype = identifierupdate.get("paytype").getAsString();   /**** Taken as Requested Date ****/
			 
			 JsonObject primaryarg = identifierupdate.get("primaryarg").getAsJsonObject();	
 			 
			 String date = identifierupdate.get("date").getAsString();
			 String reqrefid = identifierupdate.get("reqrefid").getAsString();
			 String initiatedby = identifierupdate.get("initiatedby").getAsString();
			 
			 String identifier = primaryarg.get("identifier").getAsString();
			 String acno = primaryarg.get("acno").getAsString();
			 String custno = primaryarg.has("custno") ? primaryarg.get("custno").getAsString() : "";
			 String identifierType = "ALIAS";
			
			 String status = primaryarg.get("status").getAsString();
			 
			 if(status.toLowerCase().equals("active"))
			 {
				 status = "Active";
			 }
			 else
			 {
				 status = "Inactive";
			 }
			
			 String MSGTYPE 	=  SERVICECD;  									/*** service code hardcoded value **/
			 String FLOW    	=  "O"; 									  									
			 String IP      	=  Headers.get("IPAddress").getAsString(); 
			 String PORT    	=  req.getRemotePort()+"";  									
			 String HEAD_MSG    =  Headers.toString();  				
			
			 String sql = "Select count(*) from request001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and MSGTYPE=? and FLOW=? and REQDATE=? and UNIREFNO=?"; 
			 
			 int count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, paytype, MSGTYPE, FLOW , date, reqrefid }, Integer.class);
			 
			 if(count !=0)
			 {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP94");
				  details.addProperty("message", "duplicate request within to time limit !!");
				 
				  return details;
			 }
			 
			  JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, paytype, SERVICECD);
			 
			  if(!webservice_details.get("Result").getAsString().equals("Success"))
			  {
				    logger.debug("Error in Get_Webserice_Info :::: "+webservice_details);
				 	  
					details.addProperty("result", "failed");
					details.addProperty("stscode", "HP103");
					details.addProperty("message", webservice_details.get("Message").getAsString()); 
					 	  
					return details;  
			  }
			 	
			  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
			 
			  Common_Utils util = new Common_Utils();
			    
			  JsonObject Token_details = new JsonObject();
					  
			  String token = "";
			  
			  String VERSION  = Headers.get("VERSION").getAsString();  
			  
			  if(VERSION.equals("demo"))  /*** simulator Response Code ***/
			  {
				  Token_details = Simulator.Get_Simulator_Response("", "", SUBORGCODE, SYSCODE, "TIPS", "900", FLOW);  /*** simulator Response ***/
				  
				  Token_details = util.StringToJsonObject(Token_details.get("Response").getAsString());
			  }
			  else
			  {
				  Token_details = tk.get_stored_token(SUBORGCODE, CHCODE, paytype);
			  }
			
			  if(Token_details.get("Result").getAsString().equals("Success"))
			  {
				   token = Token_details.get("token").getAsString();
			  }
			  else
			  {
				   Token_details = Generate_Token();
				   
				   int Response_Code = Token_details.get("Response_Code").getAsInt();
					  
				   if(Response_Code == 200)
				   {
					    String res = Token_details.get("Response").getAsString();
						   
					    JsonObject Response = util.StringToJsonObject(res);
					   
					    token = Response.get("access_token").getAsString();
					   
					    Token_Info info = new Token_Info(SUBORGCODE, CHCODE, paytype, token);
					   
					    tk.Check_and_Update_tokens(info);
				   }
				   else
				   {
					    logger.debug("Error in Generating token from TIPs :::: "+Token_details);
						 
					    details.addProperty("result",  "failed");
						details.addProperty("stscode", "HP111");
						details.addProperty("message", "payment gateway token generation failed");  
						 
						return details; 
				   }
			  }
				  
			  String urlparam = webservice_details.get("PAYLOAD").getAsString();
			  
			  urlparam = urlparam.replace("~status~", status);
			 
			  webservice_details.addProperty("PAYLOAD", urlparam);
			  
			  String Headers_str = O_Headers.toString();
			  
			  Headers_str = Headers_str.replace("~Token~", token);
			  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
			  Headers_str = Headers_str.replace("~fsp-source~", "013");
			  Headers_str = Headers_str.replace("~fsp-destination~", "tips");
			  Headers_str = Headers_str.replace("~fsp-encryption~", "");
			  Headers_str = Headers_str.replace("~fsp-signature~", get_signature(urlparam));
			  Headers_str = Headers_str.replace("~fsp-uri~", "");
			  Headers_str = Headers_str.replace("~fsp-http-method~", "");
			  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());
			  Headers_str = Headers_str.replace("~request-id~", reqrefid);
			  Headers_str = Headers_str.replace("~requestid~", reqrefid);
			  
			  O_Headers = util.StringToJsonArray(Headers_str);
			
			  JsonObject final_headers = new JsonObject();
			  
			  for(int i=0;i<O_Headers.size();i++)
			  {
				  JsonObject head = O_Headers.get(i).getAsJsonObject();
				  
				  final_headers.addProperty(head.get("Key").getAsString(), head.get("Value").getAsString());
			  }

	          String requesturl = webservice_details.get("URI").getAsString(); 
	         
	          requesturl = requesturl.replace("~identifierType~", identifierType);
	          requesturl = requesturl.replace("~identifier~", identifier);
	          
			  webservice_details.addProperty("URI", requesturl);
			  webservice_details.add("Headers", O_Headers);
	       
		      String O_FLOW  =  "O"; 
		      String O_ReqRefID  = reqrefid;
		      String O_MSGURL = requesturl; 
		      String O_Method = webservice_details.get("METHOD").getAsString(); 
		      String O_IP = "";
		      String O_PORT = "";
		      String O_HEAD_MSG = O_Headers.toString();
		      String O_BODY_MSG = urlparam;
		      String O_INITATEDBY = "HPAY";
		      String O_Checksum = "";
		      
		      String Request_Date = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(System.currentTimeMillis());
		      
		      TIPS001 TIPS_001 = new TIPS001();
		      
		      TIPS_001.setSUBORGCODE(SUBORGCODE);
		      TIPS_001.setREQUESTID(reqrefid); 
		      TIPS_001.setREQUESTDATE(Request_Date); 
		      TIPS_001.setFSP_SRCID(final_headers.get("fsp-source").getAsString()); 
		      TIPS_001.setFSP_DESID(final_headers.get("fsp-destination").getAsString()); 
		      TIPS_001.setIDENTIFIERNO(identifier); 
		      TIPS_001.setSTATUS(status); 
		      TIPS_001.setERRORCD(""); 
		      TIPS_001.setERRDESC(""); 
		      TIPS_001.setIDENTIFIERTYPE(identifierType);
		      TIPS_001.setACCOUNTNO(acno);
		      TIPS_001.setCUSTNO(custno);
		      
		      Insert_TIPS_001(TIPS_001);   /*** Insert into TIPS_001 ****/
		      
		      Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, paytype, SERVICECD, O_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
			 
			  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
			  
			  JsonObject API_Response = new JsonObject();
			  
			  if(VERSION.equals("demo"))  /*** simulator Response Code ***/
			  {
				  API_Response = Simulator.Get_Simulator_Response(requesturl, O_Method, SUBORGCODE, SYSCODE, "TIPS", MSGTYPE, FLOW);  /*** simulator Response ***/
			  }
			  else
			  {
				  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
			  }
			  
	          logger.debug("Update_Identifier API_Response :::: "+API_Response);
	         
	          if(API_Response.get("Result").getAsString().equals("Success"))
			  {
	        	  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
	        	  String response = API_Response.get("Response").getAsString();
	        	  
				  if(Response_Code == 200)
				  {
					  JsonObject PRIELEMENTS = new JsonObject();
					  
					  PRIELEMENTS = new Common_Utils().StringToJsonObject(response);
					  
					  PRIELEMENTS.addProperty("identifier", PRIELEMENTS.has("identifier") ? PRIELEMENTS.get("identifier").getAsString() : "");
					  PRIELEMENTS.addProperty("status", PRIELEMENTS.has("status") ? PRIELEMENTS.get("status").getAsString() : "");
					
					  JsonObject IdentifierInfo = new JsonObject(); 
					  JsonObject IdentifierDetails = new JsonObject();					  
				  
					  IdentifierInfo.addProperty("paytype", paytype);
					  IdentifierInfo.addProperty("date", date);
					  IdentifierInfo.addProperty("reqrefid", reqrefid);
					  IdentifierInfo.addProperty("initiatedby", initiatedby);
					  
					  PRIELEMENTS.addProperty("identifiertype", identifierType);
					  PRIELEMENTS.addProperty("acno", acno);
			
					  if(primaryarg.has("custno")) PRIELEMENTS.addProperty("custno", custno); 
					 
					  IdentifierDetails.add("prielements", PRIELEMENTS);
					 
					  IdentifierInfo.add("identifierdetails", IdentifierDetails);
					  
					  details.add("identifierinfo", IdentifierInfo);  
					  					 
					  TIPS002 TIPS_002 = new TIPS002();

					  TIPS_002.setSUBORGCODE(SUBORGCODE); 				
				      TIPS_002.setIDENTIFIERNO(identifier); 
					  TIPS_002.setIDENTIFIERTYPE(identifierType); 
					  TIPS_002.setSTATUS(PRIELEMENTS.get("status").getAsString()); 
					  
					  Update_TIPS_002(TIPS_002);
					  
					  details.addProperty("result", "success");  
					  details.addProperty("stscode", "HP00");  
					  details.addProperty("message", "account updated successfully !!");  
					  
					  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, response, "TIPS", "");
						
					  Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/		  
				  }
				  else
				  {
					  String errorCode = "400";
					  String errorDescription = "Unspecified Error";
					  
					  if(response.contains("errorInformation"))
					  {
						  JsonObject errorInformation = new Common_Utils().StringToJsonObject(response);
						  
						  errorCode = errorInformation.has("errorCode") ? errorInformation.get("errorCode").getAsString() : "";
						  errorDescription = errorInformation.has("errorDescription") ? errorInformation.get("errorDescription").getAsString() : "";
						  
						  TIPS001 tips_001 = new TIPS001();
						  
						  tips_001.setSUBORGCODE(SUBORGCODE);
						  tips_001.setERRORCD(errorCode);
						  tips_001.setERRDESC(errorDescription);
						  tips_001.setREQUESTID(reqrefid); 
						  tips_001.setREQUESTDATE(Request_Date); 
						  tips_001.setIDENTIFIERNO(identifier); 
						  tips_001.setACCOUNTNO(acno);
						  
						  Update_TIPS_001(tips_001);		    
					  }
					  
					  details.addProperty("result", "failed");  
					  details.addProperty("stscode", "HP06");  
					  details.addProperty("message", errorDescription);
					  
					  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, response, "TIPS", "");
						
					  Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	  
				  }
			 }
	         else
	         {
	        	  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), "TIPS", "");
					
			      Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	  
  
			      details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP06");
				  details.addProperty("message", API_Response.get("Message").getAsString()); 
  
	        	  return details;
	         }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");  
			 details.addProperty("message", e.getLocalizedMessage());  

			 logger.debug("Exception in Update_Identifier_details :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Generate_Token()
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "999"; 
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			 
			 String ReqRefID = System.currentTimeMillis()+"";
		
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, SYSCODE, CHCODE, SERVICECD, "I", ReqRefID, "", "", "" , "", "", SYSCODE,  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
			 	 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, SYSCODE, CHCODE, SERVICECD, "I", ReqRefID , "", "", "", "", webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
		    
		     String form_data = webservice_details.get("PAYLOAD").getAsString();
		   
		     JsonObject form_object = util.StringToJsonObject(form_data);
		    
		     HashMap<String,String> params = new HashMap<String,String>();
		     
	         if(form_object.has("grant_type"))  { 	params.put("grant_type", form_object.get("grant_type").getAsString()); }
	         if(form_object.has("username")) 	{	params.put("username", form_object.get("username").getAsString());	   }
	         if(form_object.has("password")) 	{	params.put("password", form_object.get("password").getAsString());	   }
		        
	         JsonArray Params = new JsonArray();
	         
	         for(Entry<String, String> entry: params.entrySet()) 
			 {
	        	 JsonObject form_datas = new JsonObject();
	        	 
	        	 form_datas.addProperty("Key", entry.getKey());  
	        	 form_datas.addProperty("Value", entry.getValue());  
	        	 
	        	 Params.add(form_datas);
			 }
	         
	         webservice_details.add("Form_Data", Params);
	         
	         //logger.debug("Request :::: "+webservice_details);
	         //logger.debug("params :::: "+params);
	         
	         String URI = webservice_details.get("URI").getAsString();  
	         String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();	         
		           
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, Params.toString(), "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/

	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("Token API_Response :::: "+API_Response);
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);  /*** Insert outward Response to Response_001 ****/
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, SYSCODE, CHCODE, SERVICECD, "I", ReqRefID , "", "", "", "", details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		     
		     details =  API_Response;
		}
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Generate_Token :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public String get_signature(String PAYLOAD)
	{
		String Signeddata = "";
		
		String password = "passphrase";
		String alias = "eximtzt0";

		try
		{
			char[] passord = null;
			
			PrivateKey privateKey = null;
			PublicKey publicKey = null;
				
		    File file = new File("C:\\HDPAY\\TIPS\\EXIMTZT0Keystore.pfx");
			
		    InputStream stream = new FileInputStream(file);
		    java.security.KeyStore keyStore1 = KeyStore.getInstance("PKCS12");  
		    keyStore1.load(stream, passord);

		    privateKey = (PrivateKey) keyStore1.getKey(alias, password.toCharArray());
		 
		    //System.out.println("Private Key " + privateKey); 
		    
		    File file2 = new File("C:\\HDPAY\\TIPS\\EXIMTZT0Keystore.jks");
			
		    InputStream stream2= new FileInputStream(file2);
		    
		    KeyStore ks = KeyStore.getInstance("JKS");
		    ks.load(stream2, password.toCharArray());
		    
		    Key key = ks.getKey(alias, password.toCharArray());
		    
		    if(key instanceof PrivateKey) 
		    {
		        Certificate cert = (Certificate) ks.getCertificate(alias);

		        publicKey = cert.getPublicKey();

		        new KeyPair(publicKey, (PrivateKey) key);
		    }
		  
		    //System.out.println("Public Key" + publicKey);
			    
		    String dgdata = PAYLOAD;
		    
	        byte[] data = dgdata.getBytes("UTF8");
	
	        Signature sig = Signature.getInstance("SHA256WithRSA");
	        
	        sig.initSign(privateKey);
	        sig.update(data);
	        
	        byte[] signatureBytes = sig.sign();
	        
	        Signeddata = Base64.getEncoder().encodeToString(signatureBytes);
	        
	        Signeddata = Signeddata.replaceAll("\n", "");
	        Signeddata = Signeddata.replaceAll("\r", "");
	        
	        sig.initVerify(publicKey);
	        sig.update(data);     
	        
	        logger.debug("signature Verification ::: "+sig.verify(signatureBytes));        
		}
		catch(Exception e)
		{
			logger.debug("Exception in signature Verification ::: "+e.getLocalizedMessage());
		}
		
		return Signeddata;
	}
		
	public JsonObject Payment_Posting_Validation(JsonObject Request, JsonObject Headers)
	{
		 JsonObject details = new JsonObject();
		
		 boolean flag = true;
		 
		 try
		 {
			 Common_Utils util = new Common_Utils();
			 
			 if(!Request.has("transfers"))
			 {
				 flag = false;
			 }
			 else
			 {
				 JsonObject transfers = Request.get("transfers").getAsJsonObject();	
				 
				 if(!transfers.has("trandetail") || !transfers.has("sourcedtl") || !transfers.has("destinationdtl") || !transfers.has("paytype") || !transfers.has("date") || !transfers.has("reqrefid") || !transfers.has("paydate") || !transfers.has("initiatedby") || !transfers.has("trantype"))
				 {
					 flag = false;  
				 }
				 else
				 {
					 JsonObject trandetail = transfers.get("trandetail").getAsJsonObject();			 
					 JsonObject sourcedtl = transfers.get("sourcedtl").getAsJsonObject();	
					 JsonObject destinationdtl = transfers.get("destinationdtl").getAsJsonObject();	
					
					 if(!trandetail.has("tranrefid") || !trandetail.has("tranamt") || !trandetail.has("trancurr"))
					 {
						 flag = false; 
					 }					 
					 else if(!sourcedtl.has("s_accoutno") || !sourcedtl.has("s_identifiertype") || !sourcedtl.has("s_bic"))
					 {
						 flag = false; 
					 }
					 else if(!destinationdtl.has("d_accoutno") || !destinationdtl.has("d_identifier") || !destinationdtl.has("d_identifiertype") || !destinationdtl.has("d_bic") || !destinationdtl.has("d_accoutname"))
					 {
						 flag = false; 
					 }
				 } 
			 }
			 
			 if(!flag)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP12");
				 details.addProperty("message", "request body validation failed !!");
				 
				 return details;
			 }
			 
			 String Error_Reason = "";
			 
			 JsonObject transfers = Request.get("transfers").getAsJsonObject();	
			 JsonObject trandetail = transfers.get("trandetail").getAsJsonObject();			 
			 JsonObject sourcedtl = transfers.get("sourcedtl").getAsJsonObject();	
			 JsonObject destinationdtl = transfers.get("destinationdtl").getAsJsonObject();	
			 
			 String reqrefid = transfers.get("reqrefid").getAsString();
			 String date = transfers.get("date").getAsString();
			 String paydate = transfers.get("paydate").getAsString();
			 String trantype = transfers.get("trantype").getAsString();
			 String initiatedby = transfers.get("initiatedby").getAsString();
			 String description = transfers.get("description").getAsString();
			 
			 String tranrefid = trandetail.get("tranrefid").getAsString();
			 String tranamt = trandetail.get("tranamt").getAsString();
			 String trancurr = trandetail.get("trancurr").getAsString();
			 
			 String s_accoutno = sourcedtl.get("s_accoutno").getAsString();
			 String s_identifiertype = sourcedtl.get("s_identifiertype").getAsString();
			 String s_bic = sourcedtl.get("s_bic").getAsString();
			 
			 String d_accoutno = destinationdtl.get("d_accoutno").getAsString();
			 String d_identifier = destinationdtl.get("d_identifier").getAsString();
			 String d_identifiertype = destinationdtl.get("d_identifiertype").getAsString();
			 String d_bic = destinationdtl.get("d_bic").getAsString();
			 
			 if(!util.isvalidDate(date, "dd-MMM-yyyy"))
			 {
				 Error_Reason = "date is not in valid format, use dd-MM-yyyy format";
			 }
			 else if(!util.isvalidDate(paydate, "dd-MMM-yyyy"))
			 {
				 Error_Reason = "paydate is not in valid format, use dd-MM-yyyy format";
			 }
			 else if(!util.getCurrentDate("dd-MMM-yyyy").equals(date))
			 {
				 Error_Reason = "invalid date value found";
			 }
			 else if(!util.getCurrentDate("dd-MMM-yyyy").equals(paydate)) 
			 {
				 Error_Reason = "invalid pay date value found";
			 }
			 else if(!trantype.equals("TRANSFER"))
			 {
				 Error_Reason = "trantype value should be TRANSFER";
			 }
			 else if(reqrefid.length() == 0)
			 {
				 Error_Reason = "reqrefid should be not be Empty";
			 }
			 else if(initiatedby.length() == 0)
			 {
				 Error_Reason = "initiatedby should be not be Empty";
			 }
			 else if(description.length() == 0)
			 {
				 Error_Reason = "description should be not be Empty";
			 }
			 else if(tranrefid.length() == 0)
			 {
				 Error_Reason = "tranrefid should be not be Empty";
			 }
			 else if(tranamt.length() == 0)
			 {
				 Error_Reason = "tranamt should be not be Empty";
			 }
			 else if(trancurr.length() == 0)
			 {
				 Error_Reason = "trancurr should be not be Empty";
			 }
			 else if(s_accoutno.length() == 0)
			 {
				 Error_Reason = "s_accoutno should be not be Empty";
			 }
			 else if(s_identifiertype.length() == 0)
			 {
				 Error_Reason = "s_identifiertype should be not be Empty";
			 }
			 else if(!s_identifiertype.equals("ALIAS") && !s_identifiertype.equals("BANK") && !s_identifiertype.equals("MSISDN"))
			 {
				 Error_Reason = "s_identifiertype possible values are ALIAS / BANK / MSISDN";
			 }
			 else if(!s_bic.equals("013"))
			 {
				 Error_Reason = "s_bic value should be 013 for exim bank";
			 }
			 else if(d_accoutno.length() == 0)
			 {
				 Error_Reason = "d_accoutno should be not be Empty";
			 }
			 else if(d_identifiertype.length() == 0)
			 {
				 Error_Reason = "d_identifiertype should be not be Empty";
			 }
			 else if(!d_identifiertype.equals("ALIAS") && !d_identifiertype.equals("BANK") && !d_identifiertype.equals("BUSINESS") && !d_identifiertype.equals("MSISDN"))
			 {
				 Error_Reason = "d_identifiertype possible values are ALIAS / BANK / BUSINESS / MSISDN";
			 }
			 else if(d_identifiertype.equals("ALIAS") && d_identifier.length() == 0)
			 {
				 Error_Reason = "d_identifiertype ALIAS must have the d_identifier value";
			 }
			 else if(d_bic.length() != 3)
			 {
				 Error_Reason = "d_bic value is invalid";
			 }
			 
			 if(Error_Reason.length() == 0)
			 {
				 details.addProperty("result", "success");
				 details.addProperty("stscode", "HP00");
				 details.addProperty("message", "validation success !!");
			 }
			 else
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP12");
				 details.addProperty("message", Error_Reason);
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Payment_Posting_Validation :::: "+e.getLocalizedMessage());
		 }
		 
		 return details;
	}
	
	public JsonObject Payment_Posting_Validation2(JsonObject Request, JsonObject Headers)
	{
		 JsonObject details = new JsonObject();
		
		 boolean flag = true;
		 
		 try
		 {
			 if(!Request.has("amount") || !Request.has("description") || !Request.has("payee") || !Request.has("payer") || !Request.has("payerRef") || !Request.has("transactionType"))
			 {
				 flag = false;
			 }
			 else
			 {
				 JsonObject amount = Request.get("amount").getAsJsonObject();	
				 JsonObject payee = Request.get("payee").getAsJsonObject();	
				 JsonObject payer = Request.get("payer").getAsJsonObject();
				 JsonObject transactionType = Request.get("transactionType").getAsJsonObject();
				 
				 if(!amount.has("amount") || !amount.has("currency"))
				 {
					 flag = false;
				 }
				 else if(!payee.has("identity") || !payer.has("identity"))
				 {
					 flag = false;
				 }
				 else
				 {
					 if(!payee.has("accountCategory") || !payee.has("accountType") || !payee.has("fspId") || !payee.has("fullName") || !payee.has("identifier") || !payee.has("identifierType"))
					 {
						 flag = false;
					 }
					 else if(!payer.has("accountCategory") || !payer.has("accountType") || !payer.has("fspId") || !payer.has("fullName") || !payer.has("identifier") || !payer.has("identifierType"))
					 {
						 flag = false;
					 }
					 else if(!transactionType.has("initiator") || !transactionType.has("initiatorType") || !transactionType.has("scenario"))
					 {
						 flag = false;
					 }
				 } 
			 }
			 
			 if(flag)
			 {
				 details.addProperty("result", "success");
				 details.addProperty("stscode", "HP00");
				 details.addProperty("message", "validation success !!");
			 }
			 else
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP12");
				 details.addProperty("message", "validation failed !!");
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Payment_Posting_Validation :::: "+e.getLocalizedMessage());
		 }
		 
		 return details;
	}
	
	public JsonObject Insert_TIPS_001(TIPS001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into tips001(SUBORGCODE, REQUESTID, REQUESTDATE, FSP_SRCID, FSP_DESID, IDENTIFIERNO, SUBIDENT, STATUS, ERRORCD, ERRDESC, IDENTIFIERTYPE, FSPID, IDENTIFIERNAME, ACCAT, ACTYPE, ACCOUNTNO, CUSTNO, MSISDN) " + 
			 			  "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getREQUESTID(), Info.getREQUESTDATE(), Info.getFSP_SRCID(), Info.getFSP_DESID(), Info.getIDENTIFIERNO(), Info.getSUBIDENT(), Info.getSTATUS(),
					 Info.getERRORCD(), Info.getERRDESC(), Info.getIDENTIFIERTYPE(), Info.getFSPID(), Info.getIDENTIFIERNAME(), Info.getACCAT(), Info.getACTYPE(), Info.getACCOUNTNO(), Info.getCUSTNO(), Info.getMSISDN() });
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Information added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Request_001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject TIPS_Transfer_Enquiry(String SUBORGCODE, String SYSCODE, String CHCODE, JsonObject Request) 
	{
		JsonObject details = new JsonObject();
		
		JsonObject transfers = Request.get("transfers").getAsJsonObject();	
		
		JsonObject trandetail = transfers.get("trandetail").getAsJsonObject();	
		 
		 String TRANREFNO = trandetail.get("tranrefid").getAsString();
		 
		 String PAYDATE = transfers.get("paydate").getAsString();
		 
		 String PAYTYPE = transfers.get("paytype").getAsString().toUpperCase(); 
		
		 try
		 {
			 String sql = "select * from request002 where SUBORGCODE = ? and SYSCODE = ? and CHCODE=? and PAYTYPE=? and PAYDATE =? and TRANREFNO = ?";
			   
			 List<Request_002> Info = Jdbctemplate.query(sql, new Object[] { "EXIM", "HP", CHCODE, PAYTYPE, PAYDATE, TRANREFNO }, new Payment_Enquiry_Mapper()); 
			 
			 if(Info.size() !=0)
			 {
				  String Status = Info.get(0).getSTATUS().toLowerCase();
				  String RESCODE = Info.get(0).getRESCODE();
				  String ERRDESC = Info.get(0).getERRDESC();
				 
				 if(Status.equals("success"))  
				 {
					 if(RESCODE.equals("202"))  
					 {
						 trandetail.addProperty("status", "pending");
						 trandetail.addProperty("reason", "in progress");
					 }
					 else if(RESCODE.equals("200")) 
					 {
						 trandetail.addProperty("status", "success");
						 trandetail.addProperty("reason", "payment success");
					 }
				 }
				 else 
				 {
					 trandetail.addProperty("status", "failed");
					 trandetail.addProperty("reason", ERRDESC);
				 } 
			 }
			 
			 transfers.add("trandetail", trandetail); 
			 
			 details.add("transfers", transfers);
			 
			 //details.add("Enquiry_Information", new Gson().toJsonTree(Info.get(0)).getAsJsonObject());
		 
			 details.addProperty("result", Info.size() != 0 ? "success" : "failed");
			 details.addProperty("stscode", Info.size() != 0 ? "HP00" : "HP200");
			 details.addProperty("message", Info.size() != 0 ? "details found !!" : "details not found !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Request_001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Insert_TIPS_002(TIPS002 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into tips002(SUBORGCODE, FSP_SRCID, FSP_DESID, IDENTIFIERNO, IDENTIFIERTYPE, FSPID, IDENTIFIERNAME, ACCAT, ACTYPE, ACCOUNTNO, CUSTNO, MSISDN, IDTYPE1, IDTYPEVALUE1, IDTYPE2, IDTYPEVALUE2, IDTYPE3, IDTYPEVALUE3, IDTYPE4, IDTYPEVALUE4, REGDATE, REGDATETIME, STATUS) " + 
			 			  "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getFSP_SRCID(), Info.getFSP_DESID(), Info.getIDENTIFIERNO(), Info.getIDENTIFIERTYPE(), Info.getFSPID(), Info.getIDENTIFIERNAME(), Info.getACCAT(), Info.getACTYPE(), Info.getACCOUNTNO(), Info.getCUSTNO(), Info.getMSISDN(), 
					 	Info.getIDTYPE1(), Info.getIDTYPEVALUE1(), Info.getIDTYPE2(), Info.getIDTYPEVALUE2(), Info.getIDTYPE3(), Info.getIDTYPEVALUE3(), Info.getIDTYPE4(), Info.getIDTYPEVALUE4(), Info.getREGDATE(), Info.getREGDATETIME(), Info.getSTATUS() }); 
					 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Information added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Request_001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Update_TIPS_002(TIPS002 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Update tips002 set STATUS=? where SUBORGCODE=? and IDENTIFIERNO=? and IDENTIFIERTYPE=?";
			 			 
			 Jdbctemplate.update(sql, new Object[] { Info.getSTATUS(), Info.getSUBORGCODE(), Info.getIDENTIFIERNO(), Info.getIDENTIFIERTYPE() }); 
					 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Information Updated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Update_TIPS_002 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Update_TIPS_001(TIPS001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Update tips001 set ERRORCD=?, ERRDESC=? where SUBORGCODE=? and REQUESTID=? and REQUESTDATE=? and IDENTIFIERNO=?";
			 			 
			 Jdbctemplate.update(sql, new Object[] { Info.getERRORCD(), Info.getERRDESC(), Info.getSUBORGCODE(), Info.getREQUESTID(), Info.getREQUESTDATE(), Info.getIDENTIFIERNO() }); 
					 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Information Updated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Update_TIPS_002 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Generate_HDPAY_Token(String USERID, String PASSWORD, String SUBORGCODE, String CHCODE, String Authorization) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 Authorization = Authorization.replace("Basic", "");
			 
			 String Secret_key = Authorization.trim();
			
			 String sql = "select count(*) from channel001 where SUBORGCODE = ? and CHCODE = ? and USERID=? and HASHPWD=? and SECRETKEY = ?";
					 	    
			 int Count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, USERID, PASSWORD, Secret_key }, Integer.class); 
			 
			 if(Count != 0)
			 {
				 String Token = tk.getJWTToken(SUBORGCODE, CHCODE);
				 
				 details.addProperty("token", Token);
				 details.addProperty("expires", "10800");
			 }
	
			 details.addProperty("result", Count != 0 ? "success" : "failed");
			 details.addProperty("stscode", Count != 0 ? "200" : "400");
			 details.addProperty("message", Count != 0 ? "token generated successfully !!" : "authorization failed !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Token_TEST :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Retrieve_Account_Information(String Ac_No)
	{
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 List<Account_Information> Info = new ArrayList<Account_Information>();
			 
			 String sql =   "SELECT IACLINK_BRN_CODE BRN_CODE, ACNTS_INOP_ACNT, ACNTS_DORMANT_ACNT,ACNTS_DB_FREEZED, ACNTS_CR_FREEZED, IACLINK_ACTUAL_ACNUM AC_No, ACNTS_AC_NAME1 CUSTOMERNAME,\r\n" + 
					 		"ACNTS_CURR_cODE CURRENCY, ACNTBAL_AC_BAL BALANCE, ACNTS_CLIENT_NUM CUSTOMERNO, A.ACNTS_AC_TYPE, VD.MobileNo, VM.Email_Id\r\n" + 
					 		"FROM ACNTS@CBSUAT A , IACLINK@CBSUAT I , ACNTBAL@CBSUAT , V_ACCOUNT_DETAILS@CBSUAT VD , V_ACCOUNT_MAILID@CBSUAT VM \r\n" + 
					 		"WHERE  I.IACLINK_ENTITY_NUM = ACNTS_ENTITY_NUM AND ACNTS_ENTITY_NUM = ACNTBAL_ENTITY_NUM AND ACNTS_BRN_CODE = I.IACLINK_BRN_CODE \r\n" + 
					 		"AND I.IACLINK_BRN_CODE = VD.BRANCH and ACNTS_PROD_CODE = I.IACLINK_PROD_CODE and VD.PRODUCT = ACNTS_PROD_CODE \r\n" + 
					 		"AND VD.ACCTYPE = A.ACNTS_AC_TYPE and ACNTBAL_CURR_CODE = VD.CURRENCY and A.ACNTS_AC_SUB_TYPE = VD.ACCSUBTYPE\r\n" + 
					 		"AND ACNTS_INTERNAL_ACNUM = ACNTBAL_INTERNAL_ACNUM AND A.ACNTS_INTERNAL_ACNUM = I.IACLINK_INTERNAL_ACNUM \r\n" + 
					 		"AND CBS_ALERT.facno(1,a.acnts_internal_acnum)=vd.AccountNo(+) AND CBS_ALERT.facno(1,a.acnts_internal_acnum)= vm.Account_Number(+)\r\n" + 
					 		"AND IACLINK_ACTUAL_ACNUM = ?";
			 
			 if(Active_Mode.equalsIgnoreCase("live"))
			 {
				  sql = "SELECT I.IACLINK_BRN_CODE BRN_CODE, A.ACNTS_INOP_ACNT, A.ACNTS_DORMANT_ACNT,A.ACNTS_DB_FREEZED,A. ACNTS_CR_FREEZED, I.IACLINK_ACTUAL_ACNUM AC_No, A.ACNTS_AC_NAME1  CUSTOMERNAME,\r\n" + 
				  		"A.ACNTS_CURR_CODE CURRENCY, B.ACNTBAL_AC_BAL BALANCE, A.ACNTS_CLIENT_NUM CUSTOMERNO, A.ACNTS_AC_TYPE,AD.ADDRDTLS_MOBILE_NUM MOBILENO, TRIM(AM.ACNTMAIL_EMAIL_ADDR) Email_Id\r\n" + 
				  		"FROM ACNTS@eximtzpcbs A ,  IACLINK@eximtzpcbs I ,ACNTBAL@eximtzpcbs B,CLIENTS@eximtzpcbs C, ADDRDTLS@eximtzpcbs AD,ACNTMAIL@eximtzpcbs AM\r\n" + 
				  		"WHERE A.ACNTS_INTERNAL_ACNUM=I.IACLINK_INTERNAL_ACNUM AND B.ACNTBAL_INTERNAL_ACNUM=I.IACLINK_INTERNAL_ACNUM AND  AM.ACNTMAIL_INTERNAL_ACNUM =A.ACNTS_INTERNAL_ACNUM \r\n" + 
				  		"AND C.CLIENTS_CODE(+)=A.ACNTS_CLIENT_NUM AND  C.CLIENTS_ADDR_INV_NUM=AD.ADDRDTLS_INV_NUM(+)  AND AD.ADDRDTLS_COMM_ADDR(+)=1 AND A.ACNTS_ENTITY_NUM=1\r\n" + 
				  		"AND I.IACLINK_ENTITY_NUM=1 AND B.ACNTBAL_ENTITY_NUM=1 AND AM.ACNTMAIL_ENTITY_NUM=1 AND IACLINK_ACTUAL_ACNUM = ?";
			 }
				
		     Info = Jdbctemplate.query(sql, new Object[] { Ac_No }, new Account_Info_Mapper());
			 
			 if(Info.size() !=0)
			 {
				 JsonElement jsonElement = new Gson().toJsonTree(Info.get(0));
				 
				 details.add("Informations", jsonElement.getAsJsonObject());
			 }
			 
			 details.addProperty("result", Info.size() != 0 ? "success" : "failed");
			 details.addProperty("stscode", Info.size() != 0 ? "200" : "400");
			 details.addProperty("message", Info.size() != 0 ? "ac Information found" : "ac Information not found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Retrieve_Account_Information :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}

	public JsonObject Retrieve_Account_Information(String Ac_No, String Mobile_No)
	{
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 List<Account_Information> Info = new ArrayList<Account_Information>();
			 
			 String sql =   "SELECT IACLINK_BRN_CODE BRN_CODE, ACNTS_INOP_ACNT, ACNTS_DORMANT_ACNT,ACNTS_DB_FREEZED, ACNTS_CR_FREEZED, IACLINK_ACTUAL_ACNUM AC_No, ACNTS_AC_NAME1 CUSTOMERNAME,\r\n" + 
					 		"ACNTS_CURR_cODE CURRENCY, ACNTBAL_AC_BAL BALANCE, ACNTS_CLIENT_NUM CUSTOMERNO, A.ACNTS_AC_TYPE, VD.MobileNo, VM.Email_Id\r\n" + 
					 		"FROM ACNTS@CBSUAT A , IACLINK@CBSUAT I , ACNTBAL@CBSUAT , V_ACCOUNT_DETAILS@CBSUAT VD , V_ACCOUNT_MAILID@CBSUAT VM \r\n" + 
					 		"WHERE  I.IACLINK_ENTITY_NUM = ACNTS_ENTITY_NUM AND ACNTS_ENTITY_NUM = ACNTBAL_ENTITY_NUM AND ACNTS_BRN_CODE = I.IACLINK_BRN_CODE \r\n" + 
					 		"AND I.IACLINK_BRN_CODE = VD.BRANCH and ACNTS_PROD_CODE = I.IACLINK_PROD_CODE and VD.PRODUCT = ACNTS_PROD_CODE \r\n" + 
					 		"AND VD.ACCTYPE = A.ACNTS_AC_TYPE and ACNTBAL_CURR_CODE = VD.CURRENCY and A.ACNTS_AC_SUB_TYPE = VD.ACCSUBTYPE\r\n" + 
					 		"AND ACNTS_INTERNAL_ACNUM = ACNTBAL_INTERNAL_ACNUM AND A.ACNTS_INTERNAL_ACNUM = I.IACLINK_INTERNAL_ACNUM \r\n" + 
					 		"AND CBS_ALERT.facno(1,a.acnts_internal_acnum)=vd.AccountNo(+) AND CBS_ALERT.facno(1,a.acnts_internal_acnum)= vm.Account_Number(+)\r\n" + 
					 		"AND IACLINK_ACTUAL_ACNUM = ? AND VD.MobileNo = ?";
				 
			 if(Active_Mode.equalsIgnoreCase("live"))
			 {
				 sql = "SELECT I.IACLINK_BRN_CODE BRN_CODE, A.ACNTS_INOP_ACNT, A.ACNTS_DORMANT_ACNT,A.ACNTS_DB_FREEZED,A. ACNTS_CR_FREEZED, I.IACLINK_ACTUAL_ACNUM AC_No, A.ACNTS_AC_NAME1  CUSTOMERNAME,\r\n" + 
				  		"A.ACNTS_CURR_CODE CURRENCY, B.ACNTBAL_AC_BAL BALANCE, A.ACNTS_CLIENT_NUM CUSTOMERNO, A.ACNTS_AC_TYPE,AD.ADDRDTLS_MOBILE_NUM MOBILENO, TRIM(AM.ACNTMAIL_EMAIL_ADDR) Email_Id\r\n" + 
				  		"FROM ACNTS@eximtzpcbs A ,  IACLINK@eximtzpcbs I ,ACNTBAL@eximtzpcbs B,CLIENTS@eximtzpcbs C, ADDRDTLS@eximtzpcbs AD,ACNTMAIL@eximtzpcbs AM\r\n" + 
				  		"WHERE A.ACNTS_INTERNAL_ACNUM=I.IACLINK_INTERNAL_ACNUM AND B.ACNTBAL_INTERNAL_ACNUM=I.IACLINK_INTERNAL_ACNUM AND  AM.ACNTMAIL_INTERNAL_ACNUM =A.ACNTS_INTERNAL_ACNUM \r\n" + 
				  		"AND C.CLIENTS_CODE(+)=A.ACNTS_CLIENT_NUM AND  C.CLIENTS_ADDR_INV_NUM=AD.ADDRDTLS_INV_NUM(+)  AND AD.ADDRDTLS_COMM_ADDR(+)=1 AND A.ACNTS_ENTITY_NUM=1\r\n" + 
				  		"AND I.IACLINK_ENTITY_NUM=1 AND B.ACNTBAL_ENTITY_NUM=1 AND AM.ACNTMAIL_ENTITY_NUM=1 \r\n" +
				  		"AND IACLINK_ACTUAL_ACNUM = ? AND AD.ADDRDTLS_MOBILE_NUM = ?";
			 }
			 
			 Info = Jdbctemplate.query(sql, new Object[] { Ac_No, Mobile_No }, new Account_Info_Mapper());
			
			 if(Info.size() !=0)
			 {
				 JsonElement jsonElement = new Gson().toJsonTree(Info.get(0));
				 
				 details.add("Informations", jsonElement.getAsJsonObject());
			 }
			 
			 details.addProperty("result", Info.size() != 0 ? "success" : "failed");
			 details.addProperty("stscode", Info.size() != 0 ? "200" : "400");
			 details.addProperty("message", Info.size() != 0 ? "ac Information found" : "ac Information not found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Retrieve_Account_Information :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Retrieve_TIPS_Identfifer(String identifierType, String identifier)
	{
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 List<TIPS002> Info = new ArrayList<TIPS002>();
			 
			 if(identifierType.equals("ALIAS"))
			 {
				 String sql = "Select * from tips002 where IDENTIFIERNO=? and IDENTIFIERTYPE=?";
				 
				 Info = Jdbctemplate.query(sql, new Object[] { identifier , identifierType }, new Tips002_Mapper());
			 }
			 
			 boolean flag = false;

			 if(Info.size() !=0)
			 {
				 String Account_No = Info.get(0).getACCOUNTNO();
				 
				 JsonObject Account_Info = Retrieve_Account_Information(Account_No);
				 
				 Account_Info = Retrieve_Account_Information(Account_No);
				
				 if(Account_Info.get("result").getAsString().equals("success"))   
				 {
					 details.add("Informations", Account_Info.get("Informations").getAsJsonObject());
					 
					 flag = true;
				 }
			 }
			 
			 details.addProperty("result", flag ? "success" : "failed");
			 details.addProperty("stscode", flag ? "200" : "400");
			 details.addProperty("message", flag ? "ac Information found" : "ac Information not found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Retrieve_TIPS_Identfifer :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	/****** Consumed by Tips *******/
	
	public JsonObject Retrieve_Account_Information_for_Tips(String identifierType, String Value)
	{
		 JsonObject details = new JsonObject();
		
		 JsonObject Informations = new JsonObject();
		 
		 try
		 {   
			 List<Account_Information> Info = new ArrayList<Account_Information>();
			 
			 List<TIPS002> info = new ArrayList<TIPS002>();
			 
			 boolean flag = false;
			 
			 if(identifierType.equals("ALIAS"))
			 {
				 String sql = "Select * from tips002 where IDENTIFIERNO=? and IDENTIFIERTYPE=?";
				 
				 info = Jdbctemplate.query(sql, new Object[] { Value, identifierType }, new Tips002_Mapper());
				 
				 if(info.size() !=0)
				 {
					 String Account_No = info.get(0).getACCOUNTNO();
					 
					 JsonObject Account_Info = Retrieve_Account_Information(Account_No);
						 
					 Account_Information Ac_Info = new Account_Information();
					 
					 if(Account_Info.get("result").getAsString().equals("success"))   
					 {
						 Ac_Info = new Gson().fromJson(Account_Info.get("Informations").getAsJsonObject(), Account_Information.class);
						 
						 JsonObject identity = new JsonObject();
						 
						 Informations.addProperty("identifierType", identifierType);				 
						 Informations.addProperty("identifier", Value);				 
						 Informations.addProperty("fspId", "013");
						 Informations.addProperty("fullName", Ac_Info.getCUSTOMERNAME());
						 Informations.addProperty("accountCategory", "PERSON");
						 Informations.addProperty("accountType", info.get(0).getACTYPE());
						 
						 identity.addProperty("type", ""); 
						 identity.addProperty("value", ""); 
						 
						 Informations.add("identity", identity);
						 
						 flag = true;
					 }
				 }
			 } 
			 else if(identifierType.equals("MSISDN"))
			 {
				 String sql =   "SELECT IACLINK_BRN_CODE BRN_CODE, ACNTS_INOP_ACNT, ACNTS_DORMANT_ACNT,ACNTS_DB_FREEZED, ACNTS_CR_FREEZED, IACLINK_ACTUAL_ACNUM AC_No, ACNTS_AC_NAME1 CUSTOMERNAME,\r\n" + 
					 		"ACNTS_CURR_cODE CURRENCY, ACNTBAL_AC_BAL BALANCE, ACNTS_CLIENT_NUM CUSTOMERNO, A.ACNTS_AC_TYPE, VD.MobileNo, VM.Email_Id\r\n" + 
					 		"FROM ACNTS@CBSUAT A , IACLINK@CBSUAT I , ACNTBAL@CBSUAT , V_ACCOUNT_DETAILS@CBSUAT VD , V_ACCOUNT_MAILID@CBSUAT VM \r\n" + 
					 		"WHERE  I.IACLINK_ENTITY_NUM = ACNTS_ENTITY_NUM AND ACNTS_ENTITY_NUM = ACNTBAL_ENTITY_NUM AND ACNTS_BRN_CODE = I.IACLINK_BRN_CODE \r\n" + 
					 		"AND I.IACLINK_BRN_CODE = VD.BRANCH and ACNTS_PROD_CODE = I.IACLINK_PROD_CODE and VD.PRODUCT = ACNTS_PROD_CODE \r\n" + 
					 		"AND VD.ACCTYPE = A.ACNTS_AC_TYPE and ACNTBAL_CURR_CODE = VD.CURRENCY and A.ACNTS_AC_SUB_TYPE = VD.ACCSUBTYPE\r\n" + 
					 		"AND ACNTS_INTERNAL_ACNUM = ACNTBAL_INTERNAL_ACNUM AND A.ACNTS_INTERNAL_ACNUM = I.IACLINK_INTERNAL_ACNUM \r\n" + 
					 		"AND CBS_ALERT.facno(1,a.acnts_internal_acnum)=vd.AccountNo(+) AND CBS_ALERT.facno(1,a.acnts_internal_acnum)= vm.Account_Number(+)\r\n" + 
					 		"AND VD.MobileNo = ?";
				
				 if(Active_Mode.equalsIgnoreCase("live"))
				 {
					 sql = "SELECT I.IACLINK_BRN_CODE BRN_CODE, A.ACNTS_INOP_ACNT, A.ACNTS_DORMANT_ACNT,A.ACNTS_DB_FREEZED,A. ACNTS_CR_FREEZED, I.IACLINK_ACTUAL_ACNUM AC_No, A.ACNTS_AC_NAME1  CUSTOMERNAME,\r\n" + 
					  		"A.ACNTS_CURR_CODE CURRENCY, B.ACNTBAL_AC_BAL BALANCE, A.ACNTS_CLIENT_NUM CUSTOMERNO, A.ACNTS_AC_TYPE,AD.ADDRDTLS_MOBILE_NUM MOBILENO, TRIM(AM.ACNTMAIL_EMAIL_ADDR) Email_Id\r\n" + 
					  		"FROM ACNTS@eximtzpcbs A ,  IACLINK@eximtzpcbs I ,ACNTBAL@eximtzpcbs B,CLIENTS@eximtzpcbs C, ADDRDTLS@eximtzpcbs AD,ACNTMAIL@eximtzpcbs AM\r\n" + 
					  		"WHERE A.ACNTS_INTERNAL_ACNUM=I.IACLINK_INTERNAL_ACNUM AND B.ACNTBAL_INTERNAL_ACNUM=I.IACLINK_INTERNAL_ACNUM AND  AM.ACNTMAIL_INTERNAL_ACNUM =A.ACNTS_INTERNAL_ACNUM \r\n" + 
					  		"AND C.CLIENTS_CODE(+)=A.ACNTS_CLIENT_NUM AND  C.CLIENTS_ADDR_INV_NUM=AD.ADDRDTLS_INV_NUM(+)  AND AD.ADDRDTLS_COMM_ADDR(+)=1 AND A.ACNTS_ENTITY_NUM=1\r\n" + 
					  		"AND I.IACLINK_ENTITY_NUM=1 AND B.ACNTBAL_ENTITY_NUM=1 AND AM.ACNTMAIL_ENTITY_NUM=1 \r\n" +
					  		"AND AD.ADDRDTLS_MOBILE_NUM = ?";
				 }
				 
				 Info = Jdbctemplate.query(sql, new Object[] { Value }, new Account_Info_Mapper());
				 
				 if(Info.size() !=0)
				 {
					 JsonObject identity = new JsonObject();
					 
					 Informations.addProperty("identifierType", identifierType);				 
					 Informations.addProperty("identifier", Value);				 
					 Informations.addProperty("fspId", "013");
					 Informations.addProperty("fullName", Info.get(0).getCUSTOMERNAME());
					 Informations.addProperty("accountCategory", "PERSON");
					 Informations.addProperty("accountType", "WALLET");
					 
					 identity.addProperty("type", ""); 
					 identity.addProperty("value", ""); 
					 
					 Informations.add("identity", identity);
					 
					 flag = true;
				 }
			 }
			 else
			 {
				 String sql =   "SELECT IACLINK_BRN_CODE BRN_CODE, ACNTS_INOP_ACNT, ACNTS_DORMANT_ACNT,ACNTS_DB_FREEZED, ACNTS_CR_FREEZED, IACLINK_ACTUAL_ACNUM AC_No, ACNTS_AC_NAME1 CUSTOMERNAME,\r\n" + 
						 		"ACNTS_CURR_cODE CURRENCY, ACNTBAL_AC_BAL BALANCE, ACNTS_CLIENT_NUM CUSTOMERNO, A.ACNTS_AC_TYPE, VD.MobileNo, VM.Email_Id\r\n" + 
						 		"FROM ACNTS@CBSUAT A , IACLINK@CBSUAT I , ACNTBAL@CBSUAT , V_ACCOUNT_DETAILS@CBSUAT VD , V_ACCOUNT_MAILID@CBSUAT VM \r\n" + 
						 		"WHERE  I.IACLINK_ENTITY_NUM = ACNTS_ENTITY_NUM AND ACNTS_ENTITY_NUM = ACNTBAL_ENTITY_NUM AND ACNTS_BRN_CODE = I.IACLINK_BRN_CODE \r\n" + 
						 		"AND I.IACLINK_BRN_CODE = VD.BRANCH and ACNTS_PROD_CODE = I.IACLINK_PROD_CODE and VD.PRODUCT = ACNTS_PROD_CODE \r\n" + 
						 		"AND VD.ACCTYPE = A.ACNTS_AC_TYPE and ACNTBAL_CURR_CODE = VD.CURRENCY and A.ACNTS_AC_SUB_TYPE = VD.ACCSUBTYPE\r\n" + 
						 		"AND ACNTS_INTERNAL_ACNUM = ACNTBAL_INTERNAL_ACNUM AND A.ACNTS_INTERNAL_ACNUM = I.IACLINK_INTERNAL_ACNUM \r\n" + 
						 		"AND CBS_ALERT.facno(1,a.acnts_internal_acnum)=vd.AccountNo(+) AND CBS_ALERT.facno(1,a.acnts_internal_acnum)= vm.Account_Number(+)\r\n" + 
						 		"AND IACLINK_ACTUAL_ACNUM = ?";
				
				 if(Active_Mode.equalsIgnoreCase("live"))
				 {
					 sql = "SELECT I.IACLINK_BRN_CODE BRN_CODE, A.ACNTS_INOP_ACNT, A.ACNTS_DORMANT_ACNT,A.ACNTS_DB_FREEZED,A. ACNTS_CR_FREEZED, I.IACLINK_ACTUAL_ACNUM AC_No, A.ACNTS_AC_NAME1  CUSTOMERNAME,\r\n" + 
					  		"A.ACNTS_CURR_CODE CURRENCY, B.ACNTBAL_AC_BAL BALANCE, A.ACNTS_CLIENT_NUM CUSTOMERNO, A.ACNTS_AC_TYPE,AD.ADDRDTLS_MOBILE_NUM MOBILENO, TRIM(AM.ACNTMAIL_EMAIL_ADDR) Email_Id\r\n" + 
					  		"FROM ACNTS@eximtzpcbs A ,  IACLINK@eximtzpcbs I ,ACNTBAL@eximtzpcbs B,CLIENTS@eximtzpcbs C, ADDRDTLS@eximtzpcbs AD,ACNTMAIL@eximtzpcbs AM\r\n" + 
					  		"WHERE A.ACNTS_INTERNAL_ACNUM=I.IACLINK_INTERNAL_ACNUM AND B.ACNTBAL_INTERNAL_ACNUM=I.IACLINK_INTERNAL_ACNUM AND  AM.ACNTMAIL_INTERNAL_ACNUM =A.ACNTS_INTERNAL_ACNUM \r\n" + 
					  		"AND C.CLIENTS_CODE(+)=A.ACNTS_CLIENT_NUM AND  C.CLIENTS_ADDR_INV_NUM=AD.ADDRDTLS_INV_NUM(+)  AND AD.ADDRDTLS_COMM_ADDR(+)=1 AND A.ACNTS_ENTITY_NUM=1\r\n" + 
					  		"AND I.IACLINK_ENTITY_NUM=1 AND B.ACNTBAL_ENTITY_NUM=1 AND AM.ACNTMAIL_ENTITY_NUM=1 \r\n" +
					  		"AND IACLINK_ACTUAL_ACNUM = ?";
				 }
				 
				 Info = Jdbctemplate.query(sql, new Object[] { Value }, new Account_Info_Mapper());
				 
				 if(Info.size() !=0)
				 {
					 JsonObject identity = new JsonObject();
					 
					 Informations.addProperty("identifierType", identifierType);				 
					 Informations.addProperty("identifier", Value);				 
					 Informations.addProperty("fspId", "013");
					 Informations.addProperty("fullName", Info.get(0).getCUSTOMERNAME());
					 Informations.addProperty("accountCategory", "PERSON");
					 Informations.addProperty("accountType", "BANK");
					 
					 identity.addProperty("type", ""); 
					 identity.addProperty("value", ""); 
					 
					 Informations.add("identity", identity);
					 
					 flag = true;
				 }
			 }
			
			 if(flag)
			 {
				 details.add("Informations", Informations);
				 
				 details.addProperty("result", "success");
				 details.addProperty("message", "party found");
				 
			 }
			 else
			 {
				 JsonObject errorInformation = new JsonObject();
				 
				 errorInformation.addProperty("errorCode", "3204");
				 errorInformation.addProperty("errorDescription", "party not found");
				 
				 details.add("errorInformation", errorInformation);
			 }
		 }
		 catch(Exception e)
		 {
			 JsonObject errorInformation = new JsonObject();
			 
			 errorInformation.addProperty("errorCode", "3204");
			 errorInformation.addProperty("errorDescription", "Party not found");
			 
			 details.add("errorInformation", errorInformation);
			 
			 logger.debug("Exception in Retrieve_Account_Information_for_Tips :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Transfer_Confirmation_Notification(String Body, JsonObject Headers, HttpServletRequest req, String payerRef)
	{
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 Common_Utils util = new Common_Utils();
				
			 JsonObject Request = util.StringToJsonObject(Body);
			 
			 String payeeRef = Request.get("payeeRef").getAsString();
			 String transferState = Request.get("transferState").getAsString();
			 String switchRef = Request.get("switchRef").getAsString();
			 
			 String sql = "Update pay001 set STATUS=?, RESCODE=?, PAYEEREF=?, SWITCHREF=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=?";
			 
			 Jdbctemplate.update(sql, new Object[] { "SUCCESS" , "200", payeeRef, switchRef, "EXIM", "TIPS", payerRef });
			 
			 sql = "Update request002 set STATUS=?, RESCODE=?, RESPDESC=?, PAYEEREF=?, SWITCHREF=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=?";
			 
			 Jdbctemplate.update(sql, new Object[] { "SUCCESS", "200", transferState , payeeRef, switchRef, "EXIM", "TIPS", payerRef });
			 
			 details.addProperty("payerRef", payerRef);
			 details.addProperty("status", "RECEIVED");
			 details.addProperty("datetime", util.getCurrentDateTime());
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Transfer_Confirmation_Notification :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Transfer_Failed_Notification(String Body, String payerRef, JsonObject Headers, HttpServletRequest req)
	{
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 Common_Utils util = new Common_Utils();
				
			 JsonObject Request = util.StringToJsonObject(Body);
			 
			 JsonObject errorInformation = Request.get("errorInformation").getAsJsonObject();
			 
			 String errorCode = errorInformation.get("errorCode").getAsString();
			 
			 String errorDescription = errorInformation.has("errorDescription") ? errorInformation.get("errorDescription").getAsString() : "";
	
			 String sql = "Update pay001 set STATUS=?, ERRCD=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=?";
			 
			 Jdbctemplate.update(sql, new Object[] { "FAILED" , errorCode, "EXIM", "TIPS", payerRef });
			 
			 sql = "Update request002 set STATUS=?, ERRCD=?, ERRDESC=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=?";
			 
			 Jdbctemplate.update(sql, new Object[] { "FAILED", errorCode, errorDescription , "EXIM", "TIPS", payerRef });
			 
			 details.addProperty("payerRef", payerRef);
			 details.addProperty("status", "RECEIVED");
			 details.addProperty("datetime", util.getCurrentDateTime());
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Transfer_Failed_Notification :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Transfer_Reversal_Message_From_Channel(String SUBORGCODE, String CHCODE, String PAYTYPE, String REQSL, String HOLD_REASON)
	{
		 String SYSCODE     = "HP";
		 String MSGTYPE 	= "2120";   
		
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 Common_Utils util = new Common_Utils();
				
			 String reversalReason = HOLD_REASON;
			 
			 String DATE = util.getCurrentDate("dd-MMM-yyyy");
			 
			 String sql = "Select * from pay001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQSL=?";
			 
			 List<PAY_001> info = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, CHCODE, PAYTYPE, REQSL }, new Pay001_Mapper());
			 
			 if(info.size() !=0)
			 {
				 String payerReversalRef = Generate_EXIM_Transfer_Reference("013", "Reversal", CHCODE, info.get(0).getTRANREFNO()).get("Reference_Id").getAsString();	
				 
				 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, PAYTYPE, DATE, info.get(0).getTRANREFNO(), info.get(0).getREQSL(), "Q", "REVERSAL", reversalReason, payerReversalRef, MSGTYPE); 
				 
				 Req_Modal.Insert_Job_005(Info3);
				 
				 details.addProperty("payerReversalRef", payerReversalRef);
				 details.addProperty("result", "success");
				 details.addProperty("stscode", "HP00");
				 details.addProperty("message", "Payment Hold Request Received Successfully");
			 }
			 else
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP12");
				 details.addProperty("message", "Invalid Transaction Request");
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in Transfer_Reversal_Message_From_Channel :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Transfer_Reversal_Message(String Body, JsonObject Headers, HttpServletRequest req)
	{
		 String SUBORGCODE  = "EXIM";
		 String SYSCODE     = "HP";
		 String CHCODE 	    = "TIPS" ;
		 String PAYTYPE     = "TIPS" ;
		 String MSGTYPE 	= "2120";   
		
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 Common_Utils util = new Common_Utils();
				
			 JsonObject Request = util.StringToJsonObject(Body);
			
			 String payerReversalRef = Request.get("payerReversalRef").getAsString();
			 String payerRef = Request.get("payerRef").getAsString();
			 String payeeRef = Request.get("payeeRef").getAsString();
			 String switchRef = Request.get("switchRef").getAsString();
			 
			 JsonObject amount_details = Request.get("amount").getAsJsonObject(); 
			
			 String amount = amount_details.get("amount").getAsString();
			 String currency = amount_details.get("currency").getAsString();
			 String reversalReason = amount_details.get("reversalReason").getAsString();
			 
			 String DATE = util.getCurrentDate("dd-MMM-yyyy");
			 
			 String sql = "Select * from pay001 where SUBORGCODE=? and PAYTYPE=? and PAYERID=? and payeeref =? and switchref=? and TRANAMT=? and TRANCURR=?";
			 
			 List<PAY_001> info = Jdbctemplate.query(sql, new Object[] { "EXIM", "TIPS", payerRef, payeeRef, switchRef, amount, currency }, new Pay001_Mapper());
			 
			 if(info.size() !=0)
			 {
				 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, PAYTYPE, DATE, info.get(0).getTRANREFNO(), info.get(0).getREQSL(), "Q", "REVERSAL", reversalReason, payerReversalRef, MSGTYPE); 
				 
				 Req_Modal.Insert_Job_005(Info3);
				 
				 details.addProperty("payerReversalRef", payerReversalRef);
				 details.addProperty("status", "RECEIVED");
				 details.addProperty("datetime", util.getCurrentDateTime());
			 }
			 else
			 {
				 JsonObject errorInformation = new JsonObject();
				 
				 errorInformation.addProperty("errorCode", 3208);
				 errorInformation.addProperty("errorDescription", "Transfer Ref not found");	
				 
				 details.add("errorInformation", errorInformation);
			 }
		 }
		 catch(Exception e)
		 {
			 JsonObject errorInformation = new JsonObject();
			 
			 errorInformation.addProperty("errorCode", 5000);
			 errorInformation.addProperty("errorDescription", "Generic Payee error");	
			 
			 details.add("errorInformation", errorInformation);
			 
			 logger.debug("Exception in Transfer_Reversal_Message :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Transfer_Reversal_Message_Notification(String Body, String payerRef, JsonObject Headers, HttpServletRequest req)
	{
		 String SUBORGCODE  = "EXIM";
		 String SYSCODE     = "HP";
		 String PAYTYPE     = "TIPS" ;
		 String MSGTYPE 	= "2110";  	 
		 
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 Common_Utils util = new Common_Utils();
				
			 JsonObject Request = util.StringToJsonObject(Body);
			
			 String reversalState = Request.get("reversalState").getAsString();
			 
			 String reversalReason = Request.get("reversalReason").getAsString();
			 
			 String sql = "Select * from pay001 where SUBORGCODE=? and PAYTYPE=? and PAYERID=?";
			 
			 List<PAY_001> info = Jdbctemplate.query(sql, new Object[] { "EXIM", "TIPS", payerRef }, new Pay001_Mapper());
			 
			 if(info.size() !=0)
			 {
				 String HOLD_DATE = util.getCurrentDate("dd-MMM-yyyy");
				 
				 sql = "Update pay002 set HOLD_REASON=?, HOLD_STATE=?, HOLD_DATE=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=?";
				 
				 Jdbctemplate.update(sql, new Object[] { reversalReason , reversalState, HOLD_DATE, "EXIM", "TIPS", payerRef });
				 
				 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, info.get(0).getCHCODE(), PAYTYPE, HOLD_DATE, info.get(0).getTRANREFNO(), info.get(0).getREQSL(), "Q", "REVERSAL", reversalReason, "", MSGTYPE); 
				 
				 Req_Modal.Insert_Job_005(Info3); 
				 
				 details.addProperty("payerRef", payerRef);
				 details.addProperty("status", "RECEIVED");
				 details.addProperty("datetime", util.getCurrentDate());
			 }
			 else
			 {
				 JsonObject errorInformation = new JsonObject();
				 
				 errorInformation.addProperty("errorCode", 3208);
				 errorInformation.addProperty("errorDescription", "Transfer Ref not found");	
				 
				 details.add("errorInformation", errorInformation);
			 } 
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Transfer_Reversal_Message_Notification :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Transfer_Reversal_Request(String Body, JsonObject Headers, HttpServletRequest req)
	{
		 String SUBORGCODE  = "EXIM";
		 String SYSCODE     = "HP";
		 String CHCODE 	    = "TIPS" ;
		 String PAYTYPE     = "TIPS" ;
		 String MSGTYPE 	= "2110";   
		
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 Common_Utils util = new Common_Utils();
				
			 JsonObject Request = util.StringToJsonObject(Body);
			
			 String payerRef = Request.get("payerRef").getAsString();
			 String payeeReversalRef = Request.get("payeeReversalRef").getAsString();

			 //String reversalState = Request.get("reversalState").getAsString();
			 String reversalReason = Request.get("reversalReason").getAsString();
			 
			 String sql = "Select * from pay001 where SUBORGCODE=? and PAYTYPE=? and PAYERID=?";
			 
			 List<PAY_001> info = Jdbctemplate.query(sql, new Object[] { "EXIM", "TIPS", payerRef }, new Pay001_Mapper());
			 
			 if(info.size() !=0)
			 {
				 String DATE = util.getCurrentDate("dd-MMM-yyyy");
				 
				 sql = "Update pay002 set payeeReversalRef=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=? and TRANREFNO=? and REQSL=?";
				 
				 Jdbctemplate.update(sql, new Object[] { payeeReversalRef, "EXIM", "TIPS", payerRef, info.get(0).getTRANREFNO(), info.get(0).getREQSL() });
				 
				 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, PAYTYPE, DATE, info.get(0).getTRANREFNO(), info.get(0).getREQSL(), "Q", "REVERSAL", reversalReason, "", MSGTYPE); 
				 
				 Req_Modal.Insert_Job_005(Info3);
				 
				 details.addProperty("payeeReversalRef", payeeReversalRef);
				 details.addProperty("status", "RECEIVED");
				 details.addProperty("datetime", util.getCurrentDateTime());
			 }
			 else
			 {
				 JsonObject errorInformation = new JsonObject();
				 
				 errorInformation.addProperty("errorCode", 3208);
				 errorInformation.addProperty("errorDescription", "Transfer Ref not found");	
				 
				 details.add("errorInformation", errorInformation);
			 }
		 }
		 catch(Exception e)
		 {
			 JsonObject errorInformation = new JsonObject();
			 
			 errorInformation.addProperty("errorCode", 5000);
			 errorInformation.addProperty("errorDescription", "Generic Payee error");	
			 
			 details.add("errorInformation", errorInformation);
			 
			 logger.debug("Exception in Transfer_Reversal_Message :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Transfer_Reversal_Notification(String Body, String payerRef, JsonObject Headers, HttpServletRequest req)
	{
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 Common_Utils util = new Common_Utils();
				
			 JsonObject Request = util.StringToJsonObject(Body);
			
			 String switchRef = Request.get("switchRef").getAsString();
			 String payeeRef = Request.get("payeeRef").getAsString();
			 String switchReversalRef = Request.get("switchReversalRef").getAsString();
			 String payerReversalRef = Request.get("payerReversalRef").getAsString();
			 String payeeReversalRef = Request.get("payeeReversalRef").getAsString();
			 String reversalState = Request.get("reversalState").getAsString();
			 
			 String REV_DATE = util.getCurrentDate("dd-MMM-yyyy");
	
			 String sql = "Update pay002 set REV_STATUS=?, ISREVERSED=?, REV_DATE=?, REV_SWITCHREF=?, PAYER_REVREF=?, PAYEE_REVREF=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=? and PAYEEID=? and TRAN_SWITCHREF=?";
			 
			 Jdbctemplate.update(sql, new Object[] { reversalState , "1", REV_DATE, switchReversalRef, payerReversalRef, payeeReversalRef, "EXIM", "TIPS", payerRef, payeeRef, switchRef });
			 
			 details.addProperty("payerRef", payerRef);
			 details.addProperty("status", "RECEIVED");
			 details.addProperty("datetime", util.getCurrentDate());
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Transfer_Reversal_Message_Notification :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	/*public JsonObject Retrieve_Transaction_by_PayerRef(String payerRef, JsonObject Headers, HttpServletRequest req)
	{
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 String sql = "Select * from pay001 where SUBORGCODE=? and PAYTYPE=? and TRANREFNO=?";
			 
			 List<PAY_001> Info = Jdbctemplate.query(sql, new Object[] { "EXIM" , "TIPS", payerRef }, new Pay001_Mapper());
			 
			 details.add("details", new Gson().toJsonTree(Info));
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Recon :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}*/
	
	public JsonObject Generate_EXIM_Transfer_Reference(String fsp_Id, String Type, String Channel_Id, String Ref_No)  /** for Exim **/
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String Id = Type.equals("Transfer") ? (fsp_Id+"-"+"T") : (fsp_Id+"-"+"R");
			 
			 if(Type.equals("Transfer"))
			 {
				 String sql = "select '"+Id+"' || REPLACE(TO_CHAR(sysdate, 'yyyy-mm-dd') || '"+Channel_Id+"' || '"+Ref_No+"', '-' , '') as Reference_Id from dual";
				   
				 String Reference_Id = Jdbctemplate.queryForObject(sql, String.class);
				 
				 details.addProperty("Reference_Id", Reference_Id);
			 }
			 else
			 {
				 String sql = "select '"+Id+"' || REPLACE(TO_CHAR(sysdate, 'yyyy-mm-dd'), '-' , '') || trunc(dbms_random.value(100000,999999)) || REF_ID.nextval as Reference_Id from dual";
				 
				 String Reference_Id = Jdbctemplate.queryForObject(sql, String.class);
				 
				 details.addProperty("Reference_Id", Reference_Id);
			 }
					 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Reference Id generated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Reference_Id from REF_ID :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Generate_Transfer_Reference(String fsp_Id, String Type)  /** for other fsp **/
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String Id = Type.equals("Transfer") ? (fsp_Id+"-"+"T") : (fsp_Id+"-"+"R");
					 
			 String sql = "select '"+Id+"' || REPLACE(TO_CHAR(sysdate, 'yyyy-mm-dd') || (select REMARKS from fsp001 where FSPCODE = ? ) || REF_ID.nextval, '-' , '') as Reference_Id from dual";
			   
			 String Reference_Id = Jdbctemplate.queryForObject(sql, new Object[] { fsp_Id }, String.class);
			 
			 details.addProperty("Reference_Id", Reference_Id);
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Reference Id generated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Reference_Id from REF_ID :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public List<SqlParameter> get_ProcedureParams()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("i_suborg"  		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_chncd"   		, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("i_paytype" 		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_reqdate" 		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_refno"   		, Types.VARCHAR));  
		inParamMap.add(new SqlParameter("i_reqrefno"   		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_servcd"   		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_status" 		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_rescd" 		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_resdesc" 		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_payload" 		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_signpayload"  , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_headerid"     , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_method"       , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_uri"          , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_format"       , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_protocol"     , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_tranamount" 	, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_dbcurr" 	    , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_chrgamount"   , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_chrgcurr"     , Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("o_dbaccount" 	, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_craccount"    , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_flow"         , Types.VARCHAR));
		
		return inParamMap;
	}
	
	public List<SqlParameter> get_ProcedureParams_rev()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("i_suborg"  		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_initiateby"   	, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("i_paytype" 		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_reqdate" 		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_reqrefno"   		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_reqsl"   		, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("i_servcd"   		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_status" 		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_rescd" 		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_resdesc" 		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_payload" 		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_signpayload"  , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_headerid"     , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_method"       , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_uri"          , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_format"       , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_protocol"     , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_flow"         , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_tranamount" 	, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_dbcurr" 	    , Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("o_chrgamount"   , Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("o_chrgcurr"     , Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("o_dbaccount" 	, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_craccount"    , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_chrgaccount"  , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_narration"    , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_reqcode"      , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_brncode"      , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_channel"      , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_result"       , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_message"      , Types.VARCHAR));
		
		return inParamMap;
	}
	
	public List<SqlParameter> get_CBS_ProcedureParams()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("i_suborgcode"  	  , Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_chcode"   		  , Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("i_paytype" 		  , Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_flow" 		      , Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_refno"   		  , Types.VARCHAR));  
		inParamMap.add(new SqlParameter("i_tranamnt"   		  , Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_trancurr"   		  , Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_dbaccount"   	  , Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_craccount"   	  , Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_chrgamnt"   		  , Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_chrgcurr"   		  , Types.VARCHAR));		
		inParamMap.add(new SqlOutParameter("o_billamount" 	  , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_chrgamount" 	  , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_dbcurr" 		  , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_dbaccount" 	  , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_craccount"      , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_crchrgaccount"  , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_narration"      , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_reqcode"        , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_brncode"        , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_result"         , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("o_message"        , Types.VARCHAR));
		
		return inParamMap;
	}
	
	public class Transaction_Mapper implements RowMapper<Transactions> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Transactions mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Transactions Info = new Transactions();  

			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));
			Info.setTRANTYPE(util.ReplaceNull(rs.getString("TRANTYPE")));
			Info.setTRANCODE(util.ReplaceNull(rs.getString("TRANCODE")));
			Info.setSYSTEMDATE(util.ReplaceNull(rs.getString("SYSTEMDATE")));
			Info.setTRANSEQ(util.ReplaceNull(rs.getString("TRANSEQ")));
			Info.setLEGSL(util.ReplaceNull(rs.getString("LEGSL")));
			Info.setBRANCHCD(util.ReplaceNull(rs.getString("BRANCHCD")));
			Info.setTRANDATE(util.ReplaceNull(rs.getString("TRANDATE")));
			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));
			Info.setCHREFNO(util.ReplaceNull(rs.getString("CHREFNO")));
			Info.setDBCR(util.ReplaceNull(rs.getString("DBCR")));
			Info.setAMOUNT(util.ReplaceNull(rs.getString("AMOUNT")));
			Info.setTRANCURR(util.ReplaceNull(rs.getString("TRANCURR")));
			Info.setSYSAMOUNT(util.ReplaceNull(rs.getString("SYSAMOUNT")));
			Info.setSYSCURR(util.ReplaceNull(rs.getString("SYSCURR")));
			Info.setACCOUNTNO(util.ReplaceNull(rs.getString("ACCOUNTNO")));
			Info.setIBAN(util.ReplaceNull(rs.getString("IBAN")));
			Info.setSWIFTCD(util.ReplaceNull(rs.getString("SWIFTCD")));
			Info.setLEDGERNO(util.ReplaceNull(rs.getString("LEDGERNO")));
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));
			Info.setCHARGES(util.ReplaceNull(rs.getString("CHARGES")));
			Info.setCHGCODE(util.ReplaceNull(rs.getString("CHGCODE")));
			Info.setREMARKS(util.ReplaceNull(rs.getString("REMARKS")));
			Info.setEUSER(util.ReplaceNull(rs.getString("EUSER")));
			Info.setEDATE(util.ReplaceNull(rs.getString("EDATE")));
			Info.setAUSER(util.ReplaceNull(rs.getString("AUSER")));
			Info.setADATE(util.ReplaceNull(rs.getString("ADATE")));
			Info.setCUSER(util.ReplaceNull(rs.getString("CUSER")));
			Info.setCDATE(util.ReplaceNull(rs.getString("CDATE")));
			Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));
			Info.setINVOICENO(util.ReplaceNull(rs.getString("INVOICENO")));
			Info.setSYSCODE(util.ReplaceNull(rs.getString("SYSCODE")));
			
			return Info;
		}
     }
	
	public class Tips002_Mapper implements RowMapper<TIPS002> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public TIPS002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			TIPS002 Info = new TIPS002();  
			
			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));         
			Info.setFSP_SRCID(util.ReplaceNull(rs.getString("FSP_SRCID")));             
			Info.setFSP_DESID(util.ReplaceNull(rs.getString("FSP_DESID")));            
			Info.setIDENTIFIERNO(util.ReplaceNull(rs.getString("IDENTIFIERNO")));               
			Info.setIDENTIFIERTYPE(util.ReplaceNull(rs.getString("IDENTIFIERTYPE")));        
			Info.setFSPID(util.ReplaceNull(rs.getString("FSPID")));         
			Info.setIDENTIFIERNAME(util.ReplaceNull(rs.getString("IDENTIFIERNAME")));        
			Info.setACCAT(util.ReplaceNull(rs.getString("ACCAT")));       
			Info.setACTYPE(util.ReplaceNull(rs.getString("ACTYPE")));           
			Info.setIDTYPE1(util.ReplaceNull(rs.getString("IDTYPE1")));         
			Info.setIDTYPEVALUE1(util.ReplaceNull(rs.getString("IDTYPEVALUE1")));         
			Info.setIDTYPE2(util.ReplaceNull(rs.getString("IDTYPE2")));       
			Info.setIDTYPEVALUE2(util.ReplaceNull(rs.getString("IDTYPEVALUE2")));      
			Info.setIDTYPE3(util.ReplaceNull(rs.getString("IDTYPE3")));         
			Info.setIDTYPEVALUE3(util.ReplaceNull(rs.getString("IDTYPEVALUE3")));          
			Info.setIDTYPE4(util.ReplaceNull(rs.getString("IDTYPE4")));           
			Info.setIDTYPEVALUE4(util.ReplaceNull(rs.getString("IDTYPEVALUE4")));         
			Info.setREGDATE(util.ReplaceNull(rs.getString("REGDATE")));    
			Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));   
			
			return Info;
		}
     }
    
	public class Pay001_Mapper implements RowMapper<PAY_001> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public PAY_001 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			PAY_001 Info = new PAY_001();  

			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));         
			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));             
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));            
			Info.setFLOW(util.ReplaceNull(rs.getString("FLOW")));               
			Info.setTRANBRNCODE(util.ReplaceNull(rs.getString("TRANBRNCODE")));        
			Info.setREQDATE(util.ReplaceNull(rs.getString("REQDATE")));         
			Info.setREQREFNO(util.ReplaceNull(rs.getString("REQREFNO")));        
			Info.setTRANREFNO(util.ReplaceNull(rs.getString("TRANREFNO")));       
			Info.setREQSL(util.ReplaceNull(rs.getString("REQSL")));           
			Info.setREQTIME(util.ReplaceNull(rs.getString("REQTIME")));         
			Info.setPAYDATE(util.ReplaceNull(rs.getString("PAYDATE")));         
			Info.setTRANTYPE(util.ReplaceNull(rs.getString("TRANTYPE")));       
			Info.setAMOUNTTYPE(util.ReplaceNull(rs.getString("AMOUNTTYPE")));      
			Info.setREQTYPE(util.ReplaceNull(rs.getString("REQTYPE")));         
			Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));          
			Info.setERRCD(util.ReplaceNull(rs.getString("ERRCD")));           
			Info.setRESCODE(util.ReplaceNull(rs.getString("RESCODE")));         
			Info.setTRANAMT(util.ReplaceNull(rs.getString("TRANAMT")));         
			Info.setTRANCURR(util.ReplaceNull(rs.getString("TRANCURR")));        
			Info.setSERPORCD(util.ReplaceNull(rs.getString("SERPORCD")));        
			Info.setPAYERID(util.ReplaceNull(rs.getString("PAYERID")));         
			Info.setDEBITAMT(util.ReplaceNull(rs.getString("DEBITAMT")));        
			Info.setDEBITCURR(util.ReplaceNull(rs.getString("DEBITCURR")));       
			Info.setCREDITAMT(util.ReplaceNull(rs.getString("CREDITAMT")));       
			Info.setCREDITCURR(util.ReplaceNull(rs.getString("CREDITCURR")));      
			Info.setS_ACCOUNT(util.ReplaceNull(rs.getString("S_ACCOUNT")));       
			Info.setD_ACCOUNT(util.ReplaceNull(rs.getString("D_ACCOUNT")));       
			Info.setS_GLNO(util.ReplaceNull(rs.getString("S_GLNO")));          
			Info.setD_GLNO(util.ReplaceNull(rs.getString("D_GLNO")));          
			Info.setCHQEUENO(util.ReplaceNull(rs.getString("CHQEUENO")));        
			Info.setS_BANKBIC(util.ReplaceNull(rs.getString("S_BANKBIC")));       
			Info.setD_BANKBIC(util.ReplaceNull(rs.getString("D_BANKBIC")));       
			Info.setS_ACNAME(util.ReplaceNull(rs.getString("S_ACNAME")));        
			Info.setD_ACNAME(util.ReplaceNull(rs.getString("D_ACNAME")));        
			Info.setINITIATOR_CIF(util.ReplaceNull(rs.getString("INITIATOR_CIF")));   
			Info.setS_CLIENTNO(util.ReplaceNull(rs.getString("S_CLIENTNO")));      
			Info.setD_CLIENTNO(util.ReplaceNull(rs.getString("D_CLIENTNO")));      
			Info.setFEEAMT1(util.ReplaceNull(rs.getString("FEEAMT1")));         
			Info.setFEECURR1(util.ReplaceNull(rs.getString("FEECURR1")));        
			Info.setFEEAMT2(util.ReplaceNull(rs.getString("FEEAMT2")));         
			Info.setFEECURR2(util.ReplaceNull(rs.getString("FEECURR2")));        
			Info.setFEEAMT3(util.ReplaceNull(rs.getString("FEEAMT3")));         
			Info.setFEECURR3(util.ReplaceNull(rs.getString("FEECURR3")));        
			Info.setFEEAMT4(util.ReplaceNull(rs.getString("FEEAMT4")));         
			Info.setFEECURR4(util.ReplaceNull(rs.getString("FEECURR4")));        
			Info.setINVOICENO(util.ReplaceNull(rs.getString("INVOICENO")));       
			Info.setS_IBAN(util.ReplaceNull(rs.getString("S_IBAN")));          
			Info.setD_IBAN(util.ReplaceNull(rs.getString("D_IBAN")));          
			Info.setS_BRNCODE(util.ReplaceNull(rs.getString("S_BRNCODE")));       
			Info.setD_BRNCODE(util.ReplaceNull(rs.getString("D_BRNCODE")));       
			Info.setS_ACTYPE(util.ReplaceNull(rs.getString("S_ACTYPE")));        
			Info.setD_ACTYPE(util.ReplaceNull(rs.getString("D_ACTYPE")));        
			Info.setS_ADDRESS(util.ReplaceNull(rs.getString("S_ADDRESS")));       
			Info.setD_ADDRESS(util.ReplaceNull(rs.getString("D_ADDRESS")));       
			Info.setS_EMAILID(util.ReplaceNull(rs.getString("S_EMAILID")));       
			Info.setD_EMAILID(util.ReplaceNull(rs.getString("D_EMAILID")));       
			Info.setS_MOBILE(util.ReplaceNull(rs.getString("S_MOBILE")));        
			Info.setD_MOBILE(util.ReplaceNull(rs.getString("D_MOBILE")));        
			Info.setS_TELEPHONE(util.ReplaceNull(rs.getString("S_TELEPHONE")));     
			Info.setD_TELEPHONE(util.ReplaceNull(rs.getString("D_TELEPHONE")));    
			Info.setS_IDENTIFIERTYPE(util.ReplaceNull(rs.getString("S_IDENTIFIERTYPE")));
			Info.setD_IDENTIFIERTYPE(util.ReplaceNull(rs.getString("D_IDENTIFIERTYPE")));
			Info.setS_ACCATEGORY(util.ReplaceNull(rs.getString("S_ACCATEGORY")));    
			Info.setD_ACCATEGORY(util.ReplaceNull(rs.getString("D_ACCATEGORY"))); 
			Info.setS_FSPID(util.ReplaceNull(rs.getString("S_FSPID")));         
			Info.setD_FSPID(util.ReplaceNull(rs.getString("D_FSPID")));   
			Info.setS_IDENTIFYTYPE(util.ReplaceNull(rs.getString("S_IDENTIFYTYPE")));  
			Info.setD_IDENTIFYTYPE(util.ReplaceNull(rs.getString("D_IDENTIFYTYPE")));  
			Info.setS_IDENTIFYVALUE(util.ReplaceNull(rs.getString("S_IDENTIFYVALUE"))); 
			Info.setD_IDENTIFYVALUE(util.ReplaceNull(rs.getString("D_IDENTIFYVALUE"))); 
			Info.setD_RECEIVERID(util.ReplaceNull(rs.getString("D_RECEIVERID")));  
			Info.setSENDER_INFO(util.ReplaceNull(rs.getString("SENDER_INFO")));     
			Info.setRECEIVER_INFO(util.ReplaceNull(rs.getString("RECEIVER_INFO")));   
			Info.setRECEIPTNO(util.ReplaceNull(rs.getString("RECEIPTNO"))); 
			Info.setPAYEEREF(util.ReplaceNull(rs.getString("PAYEEREF")));        
			Info.setPURPOSECD(util.ReplaceNull(rs.getString("PURPOSECD")));   
			Info.setRECONCILED(util.ReplaceNull(rs.getString("RECONCILED")));   
			//Info.setDEBIT_CREDIT(util.ReplaceNull(rs.getString("DEBIT_CREDIT")));   
			Info.setDEBIT_CREDIT("");   
			Info.setREMARKS(util.ReplaceNull(rs.getString("REMARKS")));         
			Info.setSWITCHREF(util.ReplaceNull(rs.getString("SWITCHREF")));              

			return Info;
		}
     }
    
    public class Pay002_Mapper implements RowMapper<PAY_002> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public PAY_002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			PAY_002 Info = new PAY_002();  

			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));         
			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));             
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));  
			Info.setREQDATE(util.ReplaceNull(rs.getString("REQDATE")));   
			Info.setREQREFNO(util.ReplaceNull(rs.getString("REQREFNO")));  
			Info.setTRANREFNO(util.ReplaceNull(rs.getString("TRANREFNO")));    
			Info.setREQSL(util.ReplaceNull(rs.getString("REQSL")));   
			Info.setREQTIME(util.ReplaceNull(rs.getString("REQTIME")));         
			Info.setPAYDATE(util.ReplaceNull(rs.getString("PAYDATE")));         
			Info.setTRANTYPE(util.ReplaceNull(rs.getString("TRANTYPE")));      			
			Info.setINITIATEBY(util.ReplaceNull(rs.getString("INITIATEBY")));               
			Info.setISREVERSED(util.ReplaceNull(rs.getString("ISREVERSED")));        
			Info.setREV_DATE(util.ReplaceNull(rs.getString("REV_DATE")));      
			Info.setORG_AMNT(util.ReplaceNull(rs.getString("ORG_AMNT")));         
			Info.setORG_CURR(util.ReplaceNull(rs.getString("ORG_CURR")));          
			Info.setORG_CHRGAMNT(util.ReplaceNull(rs.getString("ORG_CHRGAMNT")));           
			Info.setORG_CHRGCURR(util.ReplaceNull(rs.getString("ORG_CHRGCURR")));         
			Info.setORG_REFNO(util.ReplaceNull(rs.getString("ORG_REFNO")));         
			Info.setREV_AMOUNT(util.ReplaceNull(rs.getString("REV_AMOUNT")));        
			Info.setREV_CURR(util.ReplaceNull(rs.getString("REV_CURR")));        
			Info.setREV_CHRGAMOUNT(util.ReplaceNull(rs.getString("REV_CHRGAMOUNT")));         
			Info.setREV_CHRGCURR(util.ReplaceNull(rs.getString("REV_CHRGCURR")));        
			Info.setREV_REFNO(util.ReplaceNull(rs.getString("REV_REFNO")));       
			Info.setREV_TYPE(util.ReplaceNull(rs.getString("REV_TYPE")));       
			Info.setISFINAL_REV(util.ReplaceNull(rs.getString("ISFINAL_REV")));      
			Info.setREV_CODE(util.ReplaceNull(rs.getString("REV_CODE")));       
			Info.setREV_REASON(util.ReplaceNull(rs.getString("REV_REASON")));       
			Info.setISHOLD_MARKED(util.ReplaceNull(rs.getString("ISHOLD_MARKED")));          
			Info.setHOLD_AMOUNT(util.ReplaceNull(rs.getString("HOLD_AMOUNT")));          
			Info.setHOLD_CURR(util.ReplaceNull(rs.getString("HOLD_CURR")));        
			Info.setHOLD_REFNO(util.ReplaceNull(rs.getString("HOLD_REFNO")));       
			Info.setHOLD_DATE(util.ReplaceNull(rs.getString("HOLD_DATE")));       
			Info.setHOLD_REASON(util.ReplaceNull(rs.getString("HOLD_REASON")));        
			Info.setREV_STATE(util.ReplaceNull(rs.getString("REV_STATE")));        
			Info.setHOLD_STATE(util.ReplaceNull(rs.getString("HOLD_STATE")));   
			Info.setPAYERID(util.ReplaceNull(rs.getString("PAYERID")));      
			Info.setPAYEEID(util.ReplaceNull(rs.getString("PAYEEID")));      
			Info.setTRAN_SWITCHREF(util.ReplaceNull(rs.getString("TRAN_SWITCHREF")));         
			Info.setREV_SWITCHREF(util.ReplaceNull(rs.getString("REV_SWITCHREF")));        
			Info.setREV_STATUS(util.ReplaceNull(rs.getString("REV_STATUS")));         
			Info.setPAYER_REVREF(util.ReplaceNull(rs.getString("PAYER_REVREF")));        
			Info.setPAYEE_REVREF(util.ReplaceNull(rs.getString("PAYEE_REVREF")));         
			Info.setERRCD(util.ReplaceNull(rs.getString("ERRCD")));        
			Info.setERRDES(util.ReplaceNull(rs.getString("ERRDES")));         		           

			return Info;
		}
     }
    
    public class Account_Info_Mapper implements RowMapper<Account_Information> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Account_Information mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Account_Information Info = new Account_Information();   
			
			Info.setACNTS_INOP_ACNT(util.ReplaceNull(rs.getString("ACNTS_INOP_ACNT")));
			Info.setACNTS_DORMANT_ACNT(util.ReplaceNull(rs.getString("ACNTS_DORMANT_ACNT")));
			Info.setACNTS_DB_FREEZED(util.ReplaceNull(rs.getString("ACNTS_DB_FREEZED")));
			Info.setACNTS_CR_FREEZED(util.ReplaceNull(rs.getString("ACNTS_CR_FREEZED")));
			Info.setACNTS_INOP_ACNT(util.ReplaceNull(rs.getString("ACNTS_INOP_ACNT")));
			Info.setBRN_CODE(util.ReplaceNull(rs.getString("BRN_CODE")));
			Info.setAC_NO(util.ReplaceNull(rs.getString("AC_NO")));
			Info.setCUSTOMERNAME(util.ReplaceNull(rs.getString("CUSTOMERNAME")));
			Info.setCURRENCY(util.ReplaceNull(rs.getString("CURRENCY")));
			Info.setBALANCE(util.ReplaceNull(rs.getString("BALANCE")));
			Info.setCUSTOMERNO(util.ReplaceNull(rs.getString("CUSTOMERNO")));
			Info.setACNTS_AC_TYPE(util.ReplaceNull(rs.getString("ACNTS_AC_TYPE")));
			Info.setMOBILENO(util.ReplaceNull(rs.getString("MOBILENO")));
			Info.setEMAIL_ID(util.ReplaceNull(rs.getString("EMAIL_ID")));
			
			return Info;
		}
     }
    
    public class Payment_Enquiry_Mapper implements RowMapper<Request_002> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Request_002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Request_002 Info = new Request_002();  

			Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));         
			Info.setERRCD(util.ReplaceNull(rs.getString("ERRCD")));             
			Info.setERRDESC(util.ReplaceNull(rs.getString("ERRDESC")));            
			Info.setRESCODE(util.ReplaceNull(rs.getString("RESCODE")));               
			Info.setRESPDESC(util.ReplaceNull(rs.getString("RESPDESC")));        
			
			return Info;
		}
     }
}
