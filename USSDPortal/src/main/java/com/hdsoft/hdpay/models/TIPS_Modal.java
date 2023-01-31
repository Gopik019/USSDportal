package com.hdsoft.hdpay.models;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
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
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.common.Database;
import com.hdsoft.common.Token_System;
import com.hdsoft.common.base64simulator;
import com.hdsoft.hdpay.Repositories.Account_Information;
import com.hdsoft.hdpay.Repositories.Callback001;
import com.hdsoft.hdpay.Repositories.Callback002;
import com.hdsoft.hdpay.Repositories.Identifiers001;
import com.hdsoft.hdpay.Repositories.Job_005;
import com.hdsoft.hdpay.Repositories.PAY_001;
import com.hdsoft.hdpay.Repositories.PAY_002;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Request_002;
import com.hdsoft.hdpay.Repositories.Response_001;
import com.hdsoft.hdpay.Repositories.TIPS001;
import com.hdsoft.hdpay.Repositories.TIPS002;
import com.hdsoft.hdpay.Repositories.Transactions;
import com.hdsoft.hdpay.threads.Identifier_Thread;
import com.hdsoft.utils.FormatUtils;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class TIPS_Modal implements Database
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
			 String s_senderid = "";
			 
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
					  s_senderid = s_identifier;
					  
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
			 String d_receiverid =  "";	
				 			
			 if(d_identifiertype.equals("ALIAS"))  /*** If it is not bank type means consider the identifier as Account number ***/
			 {
				 d_receiverid = d_identifier;
			 }
			 else if(d_identifiertype.equals("BANK"))  /*** If it is not bank type means consider the identifier as Account number ***/
			 {
				 d_accoutno = d_identifier;
			 }
			 else
			 {
				 d_mobile = d_identifier;
			 }
			 
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
			 //String DATEANDTIME = Headers.get("ChannelID").getAsString();  
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
			 Info2.setS_SENDERID(s_senderid);
			
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
			
			 Validation_details = Payment_Posting_Validation2(Req, Headers);
			
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
			 String S_ACCOUTNO = "";
			 String S_identifierType = payer.get("identifierType").getAsString();
			 String S_identifier = payer.get("identifier").getAsString();
			 String S_AccountCategory = payer.get("accountCategory").getAsString();
			 String S_ACTYPE = payer.get("accountType").getAsString();
			 String S_CLIENTNO =  "";
			 String S_InitiatorCIF =  "";
			 String S_EMAIL = "";
			 String S_Mobile =  "";
			 String S_telephone = "";
			 String Sender_info = initiator;
			 String S_Address =  "";
			 String S_fspId = payer.get("fspId").getAsString();
			 String S_BRACNCH = "";
			 String S_SENDERID = "";
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
				 S_FEECURR = endUserFee.get("currency").getAsString();  
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
			 String MSGTYPE 	=  "1111";  				
			 String FLOW    	=  "I"; 					
			 String REQSL		=  Req_Modal.Generate_Serial().get("Serial").getAsString();							
			 String MSGURL 	 	=  "";   				
			 String IP      	=  ""; 
			 String PORT    	=  "";  				
			 String HEAD_MSG    =  Headers.toString();  				
			 String BODY_MSG    =  Request.toString();   
			 
			 String ReqRefID = System.currentTimeMillis() + REQSL;
			  
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
			 
			 sql = "Select count(*) from request002 where SUBORGCODE=? and PAYTYPE=? and FLOW=? and REQDATE=? and TRANREFNO=?"; 
			 
			 count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, PAYTYPE, FLOW, DATE, S_PAYERID }, Integer.class);
			 
			 if(count !=0)
			 {
				 JsonObject errorInformation = new JsonObject();
				 
				 errorInformation.addProperty("errorCode", "4006");
				 errorInformation.addProperty("errorDescription", "Payer duplicate reference");
				 
				 details.add("errorInformation", errorInformation);
				 	 
				 return details; 
			 }
			
			 if(S_identifierType.equals("ALIAS"))	 // Identifier type ( Possible values : BANK, ALIAS, MSISDN)
			 {
				 S_SENDERID = S_identifier;
			 }
			 else if(S_identifierType.equals("BANK"))	 // Identifier type ( Possible values : BANK, ALIAS, MSISDN)
			 {
				 S_ACCOUTNO = S_identifier;
			 }
			 else
			 {
				 S_Mobile = S_identifier;
			 }
			 
			 JsonObject Response = new JsonObject();
			 
			 if(D_IdentifierType.equals("ALIAS"))	 // Identifier type ( Possible values : BANK, ALIAS, MSISDN)
			 {
				  D_RECEIVERID = D_IDENTIFIER;
				 
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

			 if(D_IdentifierType.equals("BANK"))	 // Identifier type ( Possible values : BANK, ALIAS, MSISDN)  
			 {
				  D_ACCOUTNO = D_IDENTIFIER;
				 
				  logger.debug(">>>>>> validating source account with bank system <<<<<<<<"); 
				 
				  Response = Retrieve_Account_Information(D_IDENTIFIER);
				 
				  logger.debug(">>>>>> validation result <<<<<<<< "+Response); 
				  
				/*  if(!Response.has("Informations"))
				  {
					   JsonObject errorInformation = new JsonObject();
						 
					   errorInformation.addProperty("errorCode", "3204");
					   errorInformation.addProperty("errorDescription", "Party not found");
					 
					   details.add("errorInformation", errorInformation);
					 	 
					   return details; 
				  }
				  */
			 }
			
			 if(D_IdentifierType.equals("MSISDN"))	 // Identifier type ( Possible values : BANK, ALIAS, MSISDN) 
			 {
				  D_Mobile = D_IDENTIFIER;
				  
				  logger.debug(">>>>>> validating source account with bank system <<<<<<<<"); 
				
				  Response = Retrieve_Account_Information_for_Tips(D_IdentifierType, D_IDENTIFIER);
				 
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
			 Info2.setS_BANKBIC(S_fspId);
			 Info2.setS_BANKBIC(D_fspId);	
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
			 Info2.setS_SENDERID(S_SENDERID);
			 
			 if(!Response.has("Informations"))
			 {
				 Info2.setD_ACCOUNT(D_IDENTIFIER); 
				 Info2.setSTATUS("Failed");
				 Info2.setRESCODE("HP14");
				 Info2.setRESPDESC("Invalid Transaction Account");
			 }
				
			 Req_Modal.Insert_Request_002(Info2);
			
			 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, PAYTYPE, DATE, ReqRefID, REQSL, "Q", TRANTYPE, "", "", "");   /** Insert only if async ***/
				 
			 Req_Modal.Insert_Job_005(Info3);
	
			 details.addProperty("payerRef", payerRef);
			 details.addProperty("status", "RECEIVED");
			 details.addProperty("datetime", util.getCurrentDateTime());
		 }
		 catch(Exception e)
		 {
			 JsonObject errorInformation = new JsonObject();
			 
			 errorInformation.addProperty("errorCode", "2000");
			 errorInformation.addProperty("errorDescription", "Internal server error");

			 logger.debug("Exception in Insert_Payment_Posting_Request :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}

	public JsonObject TIPS_Payment_Executer(final Job_005 Job) 
	{
		JsonObject details = new JsonObject();
		
		final String SUBORGCODE = "EXIM";
		final String PAYTYPE = "TIPS"; 
		final String Service_Code = "1110";  
		final String SYSCODE = "HP"; 
		
		try
		{
			logger.debug("******** Payment Process Started for REF ID "+Job.getREFNO()+" ********");
			
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
 			
 			Common_Utils util = new Common_Utils();
 			
 			final String O_TRANAMOUNT = util.ReplaceNull(resultMap.get("o_tranamount"));
 			final String O_DBCURR = util.ReplaceNull(resultMap.get("o_dbcurr"));
 			final String O_CHRGAMOUNT = util.ReplaceNull(resultMap.get("o_chrgamount"));
 			final String O_CHRGCURR = util.ReplaceNull(resultMap.get("o_chrgcurr"));
 			final String O_DBACCOUNT = util.ReplaceNull(resultMap.get("o_dbaccount"));
 			final String O_CRACCOUNT = util.ReplaceNull(resultMap.get("o_craccount"));
 			final String O_FLOW = util.ReplaceNull(resultMap.get("o_flow"));
 			final String HEADERID = util.ReplaceNull(resultMap.get("o_headerid"));		
 			final String O_RESDESC = util.ReplaceNull(resultMap.get("o_resdesc"));
 		
 			if(O_RESDESC.equalsIgnoreCase("F"))
 			{
 				String sql = "Select * from pay001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQDATE=? and REQSL=?";
				 
				List<PAY_001> INFO = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, Job.getCHCODE(), Job.getPAYTYPE(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD"), Job.getREQSL() }, new Pay001_Mapper());
				 		
				if(INFO.size() !=0)
				{
					String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
				     
				    Timestamp REQTIME  = new java.sql.Timestamp(new java.util.Date().getTime());
				     
				    Callback001 Info = new Callback001(INFO.get(0).getSUBORGCODE(), SYSCODE, INFO.get(0).getCHCODE(), INFO.get(0).getPAYTYPE(), INFO.get(0).getFLOW(), util.getCurrentDate("dd-MMM-yyyy"), INFO.get(0).getREQREFNO(), INFO.get(0).getTRANREFNO(), FormatUtils.dynaSQLDate(INFO.get(0).getPAYDATE(),"YYYY-MM-DD"), INFO.get(0).getTRANTYPE(), INFO.get(0).getREQSL(), Serial, REQTIME, "Q");
				     
				    Req_Modal.Insert_Callback_001(Info);				    
				}
				
			    details.addProperty("result", "success");
				details.addProperty("stscode", "HP00");
				details.addProperty("message", "Payment Request Processed Successfuly !!");
				 
				return details;
 			}
            		
		    logger.debug("******** START CBS POSTING ********");
			  
		    logger.debug("O_TRANAMOUNT :::: "+O_TRANAMOUNT);
 			logger.debug("O_DBCURR     :::: "+O_DBCURR);
 			logger.debug("O_CHRGAMOUNT :::: "+O_CHRGAMOUNT);
 			logger.debug("O_CHRGCURR   :::: "+O_CHRGCURR);
 			logger.debug("O_DBACCOUNT  :::: "+O_DBACCOUNT);
 			logger.debug("O_CRACCOUNT  :::: "+O_CRACCOUNT);
 			
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
			
			  String DBCurr = util.ReplaceNull(cbsresultMap.get("o_dbcurr"));
			  String Reqcode = util.ReplaceNull(cbsresultMap.get("o_reqcode"));
	 		  String BRNCODE = util.ReplaceNull(cbsresultMap.get("o_brncode"));
		  
	 		  String sql = "Select * from transactions where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? order by transeq,legsl";
			 	
	 		  List<Transactions> Transactions_Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), "HP", "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD") }, new Transaction_Mapper());
	 	
              List<Transactions> Credit_Info = new ArrayList<Transactions>();
			 
			  List<Transactions> Debit_Info = new ArrayList<Transactions>();
			    
			  for(int x=0;x<Transactions_Info.size();x++)
		 	  {
			 	  String DBCR = Transactions_Info.get(x).getDBCR();

			 	  if(DBCR.equals("D")) {  Debit_Info.add(Transactions_Info.get(x));  }
			 
			 	  if(DBCR.equals("C")) {  Credit_Info.add(Transactions_Info.get(x)); }
		 	  }
			  
			  int err = 0; int crt = 0;
			 
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
				
		 		 errormsg =  base64simulator.formrequestnew(Billamount, Charge, DBCurr, DBAccount, CRAccount, Narration, Reqcode, BRNCODE);
		 		
		 		 logger.debug("CBS RESPONSE for Transeq "+Transeq+" is :::: "+errormsg);  
		 		 
		 		 if(errormsg.equalsIgnoreCase("S"))
				 {
   					 String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? and TRANSEQ=?";
					
   					 Jdbctemplate.update(Update_Sql, new Object[] { "POSTED", Job.getSUBORGCODE(), Job.getCHCODE(), "HP", "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD"), Transeq });
   					 
   					 crt++;
				 }
		 		 else
		 		 {
		 			 err++;
		 			 
		 			 logger.debug("Error in CBS POSTING  :::: "+errormsg);
					 
		 			 break;
		 		 }
			}
			   
			logger.debug("******** END CBS POSTING ********");
			  
			String STATUS = "";
			String ERRCD = "";
			String ERRDESC = "";
			
			boolean flag = true; 
			
			if(err != 0)
			{
				  logger.debug("CBS POSTING for ref id "+Job.getREFNO()+" is failed");
			     
				  JsonObject js = Get_CBS_ERRINFO(errormsg);
				  
				  ERRCD = js.get("ERRCD").getAsString();
				  ERRDESC = js.get("ERRDESC").getAsString();
				  
				  flag = false;
				  
				  /*** Initiate Internal Reversal CBS Posting ***/
				  
				  if(crt != 0)
				  {
					  JsonObject Reversal_details = Internal_Reversal_Executer(Job);
				  
					  logger.debug("<<<<<<< Reversal_details >>>>>> "+Reversal_details);
				  }
			  }
			
			  boolean Callback = false;
			  
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
					     ERRCD = "HP111";
						
						 if(err == 0)
						 {
							 ERRDESC = "CBS Posting is Success, But Payment gateway token generation failed";	 
						 }
						 else
						 {
							 ERRDESC = "CBS Posting is Failed and Payment gateway token generation failed";
						 }
						 
						 logger.debug(">>>>>> "+ERRDESC+" <<<<<<");
						 
						 flag = false;  Callback = true;
				   }
			  }
			   
			  if(O_FLOW.equals("I"))  /** check whether this Request is from TIPs **/
			  {
				  String SERVICECD = "1111";
				  
				  logger.debug(">>>>>> Initite Transfer Status Update Service <<<<<<");
				  
				  sql = "Select * from pay001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQDATE=? and REQSL=?";
					 
				  List<PAY_001> INFO = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, Job.getCHCODE(), Job.getPAYTYPE(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD"), Job.getREQSL() }, new Pay001_Mapper());
				 		
				  if(INFO.size() == 0)
				  {
					  logger.debug(">>>>>> Transaction Info not found <<<<<<");
					 
					  ERRCD = "HP200";
					  ERRDESC = "Transaction details not found !!";
						  
					  flag = false;
				  }
				  
				  if(flag && INFO.size() != 0)
				  {
					  STATUS = "Success";
					  
					  Request_002 info = new Request_002();   /**** Create a object for request 002 ****/
					  
					  info.setSTATUS(STATUS);
					  info.setCHCODE(Job.getCHCODE());
					  info.setPAYTYPE(Job.getPAYTYPE());
					  info.setTRANTYPE(Job.getTRANTYPE());
					  info.setREQREFNO(Job.getREFNO());
					  info.setRESCODE("200");
					  info.setREQSL(Job.getREQSL());
					  info.setRESPDESC(STATUS);	
					  
					  PAY_001 pay = new PAY_001();
					  
					  pay.setSTATUS(STATUS);
					  pay.setCHCODE(Job.getCHCODE());
					  pay.setPAYTYPE(Job.getPAYTYPE());
					  pay.setTRANTYPE(Job.getTRANTYPE());
					  pay.setREQREFNO(Job.getREFNO());
					  pay.setREQSL(Job.getREQSL());
					  pay.setRESCODE("200");
					  
					  Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
					  
					  Req_Modal.Update_PAY_001(pay);
				  }
				  else
				  {
					  STATUS = "Failed";
					  
					  Request_002 info = new Request_002();   /**** Create a object for request 002 ****/
					  
					  info.setSTATUS(STATUS);
					  info.setCHCODE(Job.getCHCODE());
					  info.setPAYTYPE(Job.getPAYTYPE());
					  info.setTRANTYPE(Job.getTRANTYPE());
					  info.setREQREFNO(Job.getREFNO());
					  info.setREQSL(Job.getREQSL());
					  info.setRESCODE(ERRCD);
					  info.setRESPDESC(ERRDESC);	
					  
					  PAY_001 pay = new PAY_001();
					  
					  pay.setSTATUS(STATUS);
					  pay.setCHCODE(Job.getCHCODE());
					  pay.setPAYTYPE(Job.getPAYTYPE());
					  pay.setTRANTYPE(Job.getTRANTYPE());
					  pay.setREQREFNO(Job.getREFNO());
					  pay.setREQSL(Job.getREQSL());
					  pay.setRESCODE(ERRCD);
					  
					  Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
					  
					  Req_Modal.Update_PAY_001(pay);
					  
					  logger.debug(">>>>>> Transaction aborted and updated successfully <<<<<<");
				  }
				  
				  if(Callback) /*** Initiate auto callback in case of tips token generation  failed ****/
				  {
					   String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
					     
					   Timestamp REQTIME  = new java.sql.Timestamp(new java.util.Date().getTime());
					     
					   Callback001 Info = new Callback001(INFO.get(0).getSUBORGCODE(), SYSCODE, INFO.get(0).getCHCODE(), INFO.get(0).getPAYTYPE(), INFO.get(0).getFLOW(), util.getCurrentDate("dd-MMM-yyyy"), INFO.get(0).getREQREFNO(), INFO.get(0).getTRANREFNO(), FormatUtils.dynaSQLDate(INFO.get(0).getPAYDATE(),"YYYY-MM-DD"), INFO.get(0).getTRANTYPE(), INFO.get(0).getREQSL(), Serial, REQTIME, "Q");
					     
					   Req_Modal.Insert_Callback_001(Info);
				  }
				  else
				  {
					  JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, PAYTYPE, SERVICECD);
						 
					  String urlparam = webservice_details.get("PAYLOAD").getAsString();
					  
			          String requesturl = webservice_details.get("URI").getAsString(); 
			          
			          requesturl = requesturl.replace("~payerRef~", INFO.get(0).getPAYERID());
			          
			          if(flag)
			          {
			        	  urlparam = urlparam.replace("~payeeRef~", INFO.get(0).getPAYEEREF());
						  urlparam = urlparam.replace("~transferState~", "COMMITTED");
						  urlparam = urlparam.replace("~reasonCode~", "60");
			          }
			          else
			          {
			        	  String reasonCode = Get_TIPS_ERRORCODE(ERRCD);
			        	  
			        	  urlparam = urlparam.replace("~payeeRef~", INFO.get(0).getPAYEEREF());
						  urlparam = urlparam.replace("~transferState~", "ABORTED");
						  urlparam = urlparam.replace("~reasonCode~", reasonCode);
			          }
					  
					  String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
					  
					  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
					  
					  String Headers_str = O_Headers.toString();
					  
					  Headers_str = Headers_str.replace("~Token~", token);
					  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
					  Headers_str = Headers_str.replace("~fsp-source~", INFO.get(0).getD_FSPID());
					  Headers_str = Headers_str.replace("~fsp-destination~", INFO.get(0).getS_FSPID());
					  Headers_str = Headers_str.replace("~fsp-encryption~", "");
					  Headers_str = Headers_str.replace("~fsp-signature~", get_signature(PAYLOAD));
					  Headers_str = Headers_str.replace("~fsp-uri~", "");
					  Headers_str = Headers_str.replace("~fsp-http-method~", "");
					  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());  
					  
					  O_Headers = util.StringToJsonArray(Headers_str);
						 
					  webservice_details.addProperty("URI", requesturl);
					  webservice_details.addProperty("PAYLOAD", urlparam);
					  webservice_details.add("Headers", O_Headers);
					  
					  String o_FLOW  =  "O"; 
				      String O_ReqRefID  = Job.getREFNO();
				      String O_MSGURL = requesturl; 
				      String O_IP = "";
				      String O_PORT = "";
				      String O_HEAD_MSG = O_Headers.toString();
				      String O_BODY_MSG = urlparam;
				      String O_INITATEDBY = "HPAY";
				      String O_Checksum = "";
				      String date = util.getCurrentDate("dd-MMM-yyyy");
				      
				      Request_001 Request_001 = new Request_001(SUBORGCODE, Job.getCHCODE(), PAYTYPE, SERVICECD, o_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
					 
					  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
					  
					  JsonObject API_Response = new JsonObject();
			       
					  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
					  
					  logger.debug(">>>>>> Confirm Transfer API_Response :::: "+API_Response+" <<<<<<");
					  
					  Response_001 Response = new Response_001(SUBORGCODE, Job.getCHCODE(), PAYTYPE, SERVICECD, "O", O_ReqRefID , requesturl , O_IP, O_PORT, O_HEAD_MSG, API_Response.toString(), "TIPS", "");
						
					  Res_Modal.Insert_Response_001(Response);
					  
					  int Response_Code = API_Response.get("Response_Code").getAsInt();
					  
					  if(Response_Code != 202) /*** Initiate auto callback in case of tips transfer update service is failed ****/
					  {
						  String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
						     
						  Timestamp REQTIME  = new java.sql.Timestamp(new java.util.Date().getTime());
						     
						  Callback001 Info = new Callback001(INFO.get(0).getSUBORGCODE(), SYSCODE, INFO.get(0).getCHCODE(), INFO.get(0).getPAYTYPE(), INFO.get(0).getFLOW(), date, INFO.get(0).getREQREFNO(), INFO.get(0).getTRANREFNO(), FormatUtils.dynaSQLDate(INFO.get(0).getPAYDATE(),"YYYY-MM-DD"), INFO.get(0).getTRANTYPE(), INFO.get(0).getREQSL(), Serial, REQTIME, "Q");
						     
						  Req_Modal.Insert_Callback_001(Info);
					  }
				  }

				  details.addProperty("result", "success");
				  details.addProperty("stscode", "HP00");
				  details.addProperty("message", "Payment Request Processed Successfuly !!");
				 
				  return details;
			  }
			  else   /** check whether this Request is from Channels **/
			  {
				  String Channel_Code = "TIPS";
				  
				  sql = "Select * from pay001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQDATE=? and REQSL=?";
					 
				  List<PAY_001> INFO = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, Job.getCHCODE(), Job.getPAYTYPE(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD"), Job.getREQSL() }, new Pay001_Mapper());
				 
				  if(INFO.size() == 0)
				  {
					  logger.debug(">>>>>> Transaction Info not found <<<<<<");
					 
					  ERRCD = "HP200";
					  ERRDESC = "Transaction details not found !!";
						  
					  flag = false;
				  }
				 
				  if(flag)   /*** Initiate Transfer Request to TIPS ***/
				  {
				  	  logger.debug(">>>>>> Initite Transfer Request to TIPS <<<<<<");
				  	  
			 		  String PAYLOAD = util.ReplaceNull(resultMap.get("o_payload"));
			 		  String SIGN_PAYLOAD = util.ReplaceNull(resultMap.get("o_signpayload"));
		
					  JsonObject Info = util.StringToJsonObject(PAYLOAD);
					  
					  JsonObject payer  = Info.get("payer").getAsJsonObject();
					  JsonObject payee  = Info.get("payee").getAsJsonObject();
					 
					  String fsp_source = payer.get("fspId").getAsString();
					  String fsp_destination = payee.get("fspId").getAsString();
					  
					  JsonObject webservice_details = Ws.Get_Webserice002_Info(Service_Code, Channel_Code, HEADERID);
					  
					  webservice_details.addProperty("PAYLOAD", PAYLOAD);
		 			  webservice_details.addProperty("SIGNPAYLOAD", SIGN_PAYLOAD);
		 			  webservice_details.addProperty("HEADERID", HEADERID);
		 			  webservice_details.addProperty("METHOD", util.ReplaceNull(resultMap.get("o_method")));
		 			  webservice_details.addProperty("URI", util.ReplaceNull(resultMap.get("o_uri")));
		 			  webservice_details.addProperty("FORMAT", util.ReplaceNull(resultMap.get("o_format")));
		 			  webservice_details.addProperty("PROTOCOL", util.ReplaceNull(resultMap.get("o_protocol")));
						
					  String Requesturl = webservice_details.get("URI").getAsString();
					    
					  JsonArray Headers = webservice_details.get("Headers").getAsJsonArray();

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
					  
					  String O_DATE  = util.getCurrentDate("dd-MMM-yyyy");
				      Timestamp O_REQTIME  = new java.sql.Timestamp(new java.util.Date().getTime());
				      String O_ReqRefID  = Job.getREFNO();
				      String O_MSGURL = webservice_details.get("URI").getAsString(); 
				      String O_IP = "";
				      String O_PORT = "";
				      String O_HEAD_MSG = webservice_details.get("Headers").getAsJsonArray().toString();
				      String O_BODY_MSG = webservice_details.get("PAYLOAD").getAsString();
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
					  info.setREQSL(Job.getREQSL());
					  
					  PAY_001 pay = new PAY_001();
					  
					  pay.setSTATUS(API_Response.get("Result").getAsString());
					  pay.setCHCODE(Job.getCHCODE());
					  pay.setPAYTYPE(Job.getPAYTYPE());
					  pay.setTRANTYPE(Job.getTRANTYPE());
					  pay.setREQREFNO(Job.getREFNO());
					  pay.setREQSL(Job.getREQSL());
					  
					  String response = "";  
					  
					  if(API_Response.get("Result").getAsString().equals("Success") && API_Response.get("Response_Code").getAsInt() == 202)
					  {
						  logger.debug(">>>>>> Transfer Request has been sent to TIPS <<<<<<");
						  
						  response = API_Response.get("Response").getAsString();  
						  
						  info.setRESCODE(API_Response.get("Response_Code").getAsString());
						  info.setRESPDESC(API_Response.get("Message").getAsString());	
						  pay.setRESCODE(API_Response.get("Response_Code").getAsString());
					  }  
					  else
					  {
						  logger.debug(">>>>>> Transfer Request not sent to TIPS <<<<<<");
						  
						  logger.debug(">>>>>> ERRCD   "+API_Response.get("Response_Code").getAsString()+" <<<<<<");
						  logger.debug(">>>>>> ERRDESC "+API_Response.get("Message").getAsString()+" <<<<<<");
						  
						  response = API_Response.toString();
						  
						  info.setERRCD(API_Response.get("Response_Code").getAsString());
						  info.setERRDESC(API_Response.get("Message").getAsString());
						  pay.setRESCODE(API_Response.get("Response_Code").getAsString());

						  /*** Initiate the Internal Reversal ***/
						  
						  JsonObject Reversal_details = Internal_Reversal_Executer(Job);
						  
						  logger.debug("<<<<<<< Reversal_details >>>>>> "+Reversal_details);
					  }
					  
					  Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
					  
					  Req_Modal.Update_PAY_001(pay);
		 
					  Response_001 Response = new Response_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Service_Code, "O", Job.getREFNO() , Requesturl , "IP", "PORT", Headers.toString(), response, "TIPS", "");
						
					  Res_Modal.Insert_Response_001(Response);  /**** Insert Response to Response_001 ****/	 
					  
					  details.addProperty("result", API_Response.get("Response_Code").getAsInt() == 202 ? "success" : "failed");
					  details.addProperty("stscode", API_Response.get("Response_Code").getAsInt() == 202 ? "HP00" : "HP06");
					  details.addProperty("message", API_Response.get("Response_Code").getAsInt() == 202 ? "payment transfer success" : "payment transfer failed");
				  }
				  else  /*** update Transfer aborted update status ***/
				  {
					  logger.debug(">>>>>> update Transfer aborted status <<<<<<"); 
					  
					  STATUS = "Failed";
					  
					  Request_002 info = new Request_002();   /**** Create a object for request 002 ****/
					  
					  info.setSTATUS(STATUS);
					  info.setCHCODE(Job.getCHCODE());
					  info.setPAYTYPE(Job.getPAYTYPE());
					  info.setTRANTYPE(Job.getTRANTYPE());
					  info.setREQREFNO(Job.getREFNO());
					  info.setREQSL(Job.getREQSL());
					  info.setRESCODE(ERRCD);
					  info.setRESPDESC(ERRDESC);	
					  
					  PAY_001 pay = new PAY_001();
					  
					  pay.setSTATUS(STATUS);
					  pay.setCHCODE(Job.getCHCODE());
					  pay.setPAYTYPE(Job.getPAYTYPE());
					  pay.setTRANTYPE(Job.getTRANTYPE());
					  pay.setREQREFNO(Job.getREFNO());
					  pay.setREQSL(Job.getREQSL());
					  pay.setRESCODE(ERRCD);
					  
					  Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
					  
					  Req_Modal.Update_PAY_001(pay);
					 
					  details.addProperty("result", "success");
					  details.addProperty("stscode", "HP00");
					  details.addProperty("message", "Payment Request Processed Successfuly !!");
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
	
	public JsonObject TIPS_Payment_Reversal_Executer(final Job_005 Job) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			logger.debug("***** Calling PACK_O_PAY_REV.PROC_O_REV_REQUEST *****");
			
			logger.debug("SUBORGCODE :::: "+Job.getSUBORGCODE());
 			logger.debug("CHCODE     :::: "+Job.getCHCODE());
 			logger.debug("PAYTYPE    :::: "+Job.getPAYTYPE());
 			logger.debug("REQDATE    :::: "+FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"));
 			logger.debug("REFNO      :::: "+Job.getREFNO());
 			logger.debug("REQSL      :::: "+Job.getREQSL());
 			logger.debug("SERVCODE   :::: "+Job.getSERVCODE());
 			
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
 			
 			JsonObject webservice_details = new JsonObject();
 	
 			webservice_details.addProperty("PAYLOAD", PAYLOAD);
 			webservice_details.addProperty("SIGNPAYLOAD", util.ReplaceNull(resultMap.get("o_signpayload")));
 			webservice_details.addProperty("HEADERID", HEADERID);
 			webservice_details.addProperty("METHOD", util.ReplaceNull(resultMap.get("o_method")));
 			webservice_details.addProperty("URI", util.ReplaceNull(resultMap.get("o_uri")));
 			webservice_details.addProperty("FORMAT", util.ReplaceNull(resultMap.get("o_format")));
 			webservice_details.addProperty("PROTOCOL", util.ReplaceNull(resultMap.get("o_protocol")));
			
		    logger.debug("Final Payload :::: "+PAYLOAD);
	     
		    int err = 0;  String errormsg = "";
			 
		    if(Job.getCHCODE().equals("TIPS") || Job.getSERVCODE().equals("2110"))  
		    {
		    	logger.debug("O_TRANAMOUNT :::: "+O_TRANAMOUNT);
	 			logger.debug("O_DBCURR     :::: "+O_DBCURR);
	 			logger.debug("O_CHRGAMOUNT :::: "+O_CHRGAMOUNT);
	 			logger.debug("O_CHRGCURR   :::: "+O_CHRGCURR);
	 			logger.debug("O_DBACCOUNT  :::: "+O_DBACCOUNT);
	 			logger.debug("O_CRACCOUNT  :::: "+O_CRACCOUNT);
	 			
	 			logger.debug("******** START CBS POSTING ********");
	 					
	 			String sql = "Select * from transactions where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? order by transeq,legsl";
			 	
	 			List<Transactions> Transactions_Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), O_CHANNEL, "HP", "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD") }, new Transaction_Mapper());
	 	
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
	   					 String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? and TRANSEQ=?";
						
	   					 Jdbctemplate.update(Update_Sql, new Object[] { "REVERSED", Job.getSUBORGCODE(), O_CHANNEL, "HP", "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD"), Transeq });
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
		      
		      if(Job.getCHCODE().equals("TIPS") && Job.getSERVCODE().equals("2110"))  /**** Update Reversal Status  ****/
			  {
		    	  String REV_DATE = util.getCurrentDate("dd-MMM-yyyy");
	        	  
				  String sql = "update pay002 set ISREVERSED=?, REV_DATE=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
				   
				  Jdbctemplate.update(sql, new Object[] { "1", REV_DATE, "F", "1", "REVERSED", O_CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });			  
				  
				  details.addProperty("result",  "success");
				  details.addProperty("stscode", "HP00");
				  details.addProperty("message", "Reversal Status Updated Successfully !!");
			  }
		      else  if(Job.getCHCODE().equals("TIPS") && Job.getSERVCODE().equals("2120")) /**** Confirm Hold to TIPS ****/
			  {
				  details = Confirm_Hold_Response_to_TIPS(webservice_details, Job, O_CHANNEL, err, errormsg); 
			  }
			  else /*** for banking channels ****/
			  {
				  if(Job.getSERVCODE().equals("2120"))  /*** Initiate hold Request ***/
				  {
					  details = Channel_Hold_Request_To_TIPS(Job, O_CHANNEL, err, errormsg);  
				  }
				  
				  if(Job.getSERVCODE().equals("2110"))   /*** Transfer update Request ***/
				  {
					  String REV_DATE = util.getCurrentDate("dd-MMM-yyyy");
					  
					  if(err == 0)
					  {
						  String sql = "update pay002 set ISREVERSED=?, REV_DATE=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
						   
						  Jdbctemplate.update(sql, new Object[] { "1", REV_DATE, "F", "1", "REVERSED", O_CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });			  				  
					  }
					  else
					  {
						  JsonObject js = Get_CBS_ERRINFO(errormsg);
						  
						  String ERRCD = js.get("ERRCD").getAsString();
						  String ERRDESC = js.get("ERRDESC").getAsString();
						  
						  String sql = "update pay002 set ISREVERSED=?, REV_DATE=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=?, ERRCD=?, ERRDES=? where CHCODE=? and PAYTYPE=? and TRANREFNO=? and REQSL=?";
						   
						  Jdbctemplate.update(sql, new Object[] { "0", REV_DATE, "F", "1", "FAILED", ERRCD, ERRDESC, O_CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });			  					  
					  }
					  	 
					  details.addProperty("result",  "success");
					  details.addProperty("stscode", "HP00");
					  details.addProperty("message", "Reversal Status Updated Successfully !!");
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
	
	public JsonObject Internal_Reversal_Executer(final Job_005 Job) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			logger.debug("***** Initiate Reversal Process for Ref Id "+Job.getREFNO()+" ***********");
			
			final String INITIATEBY = "EXIMPAY";
			
			Common_Utils util = new Common_Utils();
			
			logger.debug("SUBORGCODE :::: "+Job.getSUBORGCODE());
 			logger.debug("CHCODE     :::: "+Job.getCHCODE());
 			logger.debug("PAYTYPE    :::: "+Job.getPAYTYPE());
 			logger.debug("REQDATE    :::: "+FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"));
 			logger.debug("REFNO      :::: "+Job.getREFNO());
 			logger.debug("REQSL      :::: "+Job.getREQSL());
 			logger.debug("SERVCODE   :::: "+Job.getSERVCODE());
		
			final String procedureCall = "{CALL PACK_O_PAY_REV.PROC_O_REV_REQUEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
 			
			Map<String, Object> cbsresultMap = Jdbctemplate.call(new CallableStatementCreator() {
 	 
					public CallableStatement createCallableStatement(Connection connection) throws SQLException {
 
						CallableStatement CS = connection.prepareCall(procedureCall);
						
						CS.setString(1, Job.getSUBORGCODE()); 
						CS.setString(2, INITIATEBY);
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
			
			String DBCurr = util.ReplaceNull(cbsresultMap.get("o_dbcurr"));
			String Reqcode = util.ReplaceNull(cbsresultMap.get("o_reqcode"));
			String BRNCODE = util.ReplaceNull(cbsresultMap.get("o_brncode"));
	 		
 			int err = 0; int crt = 0;
	 		 
			logger.debug("***** Start INTERNAL Reversal CBS Posting ***********");
			
			String sql = "Select * from transactions where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? order by transeq,legsl";
		
			List<Transactions> Transactions_Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getSYSCODE(), "TRANSFER", Job.getPAYTYPE(), "POSTED", Job.getREFNO(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD") }, new Transaction_Mapper());
		
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
				 String CRBRN = Credit_Info.get(y).getBRANCHCD();    
				 String DBBRN = Debit_Info.get(y).getBRANCHCD();
				 
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
				 
				 BRNCODE = CRBRN + "|" + CRBRN + "|" +DBBRN + "|";
		 
		 		 String errormsg =  base64simulator.formrequestnew(Billamount, Charge, DBCurr, CRAccount, DBAccount, Narration, Reqcode, BRNCODE);
				 
		 		 logger.debug("CBS RESPONSE for Reversal Transeq "+Transeq+" is :::: "+errormsg);  
		 		  		
		 		 if(errormsg.equalsIgnoreCase("S"))
				 {
					  String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? and TRANSEQ=?";
					
					  Jdbctemplate.update(Update_Sql, new Object[] { "REVERSED", Job.getSUBORGCODE(), Job.getCHCODE(), Job.getSYSCODE(), "TRANSFER", Job.getPAYTYPE(), "POSTED", Job.getREFNO(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD"), Transeq });
					  
					  crt++;
				 }
		 		 else
		 		 {
		 			  String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? and TRANSEQ=?";
					
					  Jdbctemplate.update(Update_Sql, new Object[] { "RV_PENDING", Job.getSUBORGCODE(), Job.getCHCODE(), Job.getSYSCODE(), "TRANSFER", Job.getPAYTYPE(), "POSTED", Job.getREFNO(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD"), Transeq });
					  
					  err++;
					  
		 			  logger.debug("Error in Reversal CBS POSTING  :::: "+errormsg);
		 		 }
		 		 
		 		 if(err == 0)
		 		 {
		 			  String REV_DATE = util.getCurrentDate("dd-MMM-yyyy");
		        	  
		        	  sql = "update pay002 set ISREVERSED=?, REV_DATE=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQDATE=? and REQSL=?";
					   
		        	  Jdbctemplate.update(sql, new Object[] { "1", REV_DATE, "F", "1", "REVERSED", Job.getCHCODE(), Job.getPAYTYPE(), Job.getREFNO(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD"), Job.getREQSL() });
		 		 }
		 		 else if(crt == 0 && err !=0)
		 		 {
		 			 sql = "update pay002 set ISREVERSED=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQDATE=? and REQSL=?";
					   
		        	 Jdbctemplate.update(sql, new Object[] { "0", "F", "1", "NOT REVERSED", Job.getCHCODE(), Job.getPAYTYPE(), Job.getREFNO(), FormatUtils.dynaSQLDate(Job.getREQDATE(),"YYYY-MM-DD"), Job.getREQSL() });
		 		 }
			}
			   
			logger.debug("******** END INTERNAL REVERSAL CBS POSTING ********");
			
			details.addProperty("result",  "success");
			details.addProperty("stscode", "HP00");
			details.addProperty("message", err == 0 ? "Internal Payment Reversal Successfull !!" : "Internal Payment Reversal Failed !!");
        } 
		catch(Exception e)
		{
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in Internal_Reversal_Executer :::: "+details);
		}
		
		return details;
	}
	
	public JsonObject TIPS_Callabck_Executer(final Callback001 Job) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			Common_Utils util = new Common_Utils();
			
			JsonObject Token_details = new JsonObject();
			  
			String token = ""; boolean Callback = false;
			   
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
					    
					   Token_Info info = new Token_Info(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), token);
					     
					   tk.Check_and_Update_tokens(info);
				  }
				  else
				  {
					  	Callback = true;	    
				  }
			 }
			
			 if(Callback)
			 {
				 String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
			     
			     Timestamp REQTIME  = util.get_oracle_Timestamp();
			     
			     Callback001 Info = new Callback001(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getFLOW(), FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"), Job.getREQREFNO(), Job.getTRANREFNO(), FormatUtils.dynaSQLDate(Job.getPAYDATE() ,"YYYY-MM-DD"), Job.getTRANTYPE(), Job.getPAYSL(), Serial, REQTIME, "Q");
			    
			     Req_Modal.Insert_Callback_001(Info);
			     
			     details.addProperty("result", "success");
				 details.addProperty("stscode", "HP00");
				 details.addProperty("message", "Callback failed !!");	
			 }
			 else
			 {
				 if(Job.getTRANTYPE().equalsIgnoreCase("TRANSFER"))
				 {
					 details = Transfer_Callback(Job, token);
				 }
				 
				 if(Job.getTRANTYPE().equalsIgnoreCase("HOLD"))
				 {
					 details = Hold_Callback(Job, token);
				 }
				 
				 if(Job.getTRANTYPE().equalsIgnoreCase("REVERSAL"))
				 {
					 details = Reversal_Callback(Job, token);
				 }
			 }	 
		}
		catch(Exception e)
		{
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in TIPS_Callabck_Executer :::: "+details);
		}
		
		return details;
	}
	
	public JsonObject Transfer_Callback(final Callback001 Job, String token)
	{
		String SERVICECD = "1111";
	
		JsonObject details = new JsonObject();  
		
		try
		{
			 Common_Utils util = new Common_Utils();
		
			 String sql = "Select * from request002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQREFNO=? and TRANREFNO=? and REQSL=?";
			 
			 List<Request_002> INFO = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getREQREFNO(), Job.getTRANREFNO(), Job.getPAYSL() }, new Request_002_Mapper());
			 
			 if(INFO.size() !=0)
			 {				 
				 String RESCODE = INFO.get(0).getRESCODE();  
				 String RESDESC = INFO.get(0).getRESPDESC();
				 
				 JsonObject webservice_details = Ws.Get_Webserice_Info(Job.getSYSCODE(), Job.getPAYTYPE(), SERVICECD);
				 
				 String urlparam = webservice_details.get("PAYLOAD").getAsString();
				  
		         String requesturl = webservice_details.get("URI").getAsString(); 
		          
		         requesturl = requesturl.replace("~payerRef~", INFO.get(0).getPAYERID());
				 
				 if((RESCODE.equalsIgnoreCase("HP111") && RESDESC.contains("CBS Posting is Success")) || RESDESC.equalsIgnoreCase("COMMITTED"))
				 {
					  urlparam = urlparam.replace("~payeeRef~", INFO.get(0).getPAYEEREF());
					  urlparam = urlparam.replace("~transferState~", "COMMITTED");
					  urlparam = urlparam.replace("~reasonCode~", "60");
				 }
				 else
				 {
					  String reasonCode = Get_TIPS_ERRORCODE(RESCODE);
		        	  
		        	  urlparam = urlparam.replace("~payeeRef~", INFO.get(0).getPAYEEREF());
					  urlparam = urlparam.replace("~transferState~", "ABORTED");
					  urlparam = urlparam.replace("~reasonCode~", reasonCode);
				 }
				 
				 String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
				  
				 JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
				  
				 String Headers_str = O_Headers.toString();
				  
				 String signature = get_signature(PAYLOAD);
				 
				 Headers_str = Headers_str.replace("~Token~", token);
				 Headers_str = Headers_str.replace("~x-forwarded-for~", "");
				 Headers_str = Headers_str.replace("~fsp-source~", INFO.get(0).getD_FSPID());
				 Headers_str = Headers_str.replace("~fsp-destination~", INFO.get(0).getS_FSPID());
				 Headers_str = Headers_str.replace("~fsp-encryption~", "");
				 Headers_str = Headers_str.replace("~fsp-signature~", signature);
				 Headers_str = Headers_str.replace("~fsp-uri~", "");
				 Headers_str = Headers_str.replace("~fsp-http-method~", "");
				 Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());  
				 
				 O_Headers = util.StringToJsonArray(Headers_str);
				 
				 webservice_details.addProperty("URI", requesturl);
				 webservice_details.addProperty("PAYLOAD", urlparam);
				 webservice_details.add("Headers", O_Headers);
				 
				 String o_FLOW  =  "O"; 
			     String O_ReqRefID  = INFO.get(0).getREQREFNO();
			     String O_MSGURL = requesturl; 
			     String O_IP = "";
			     String O_PORT = "";
			     String O_HEAD_MSG = O_Headers.toString();
			     String O_BODY_MSG = urlparam;
			     String O_INITATEDBY = "HPAY";
			     String O_Checksum = "";
			     String date = util.getCurrentDate("dd-MMM-yyyy");
			     
			     Request_001 Request_001 = new Request_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), SERVICECD, o_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
				 
				 Req_Modal.Insert_Request_001(Request_001); 
				  
				 JsonObject API_Response = new JsonObject();
		       
				 API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
				  
				 logger.debug(">>>>>> Callback API_Response :::: "+API_Response+" <<<<<<");
				 
				 Response_001 Response = new Response_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), SERVICECD, "O", O_ReqRefID , requesturl , O_IP, O_PORT, O_HEAD_MSG, API_Response.toString(), O_INITATEDBY, signature);
					
				 Res_Modal.Insert_Response_001(Response);
				 
				 int Response_Code = API_Response.get("Response_Code").getAsInt();
				 
				 Timestamp RESTIME = util.get_oracle_Timestamp();
				 
				 Timestamp REQTIME = util.String_To_Timestamp(Job.getREQTIME());
				 
				 if(Response_Code == 202)
				 {
					 Callback002 info = new Callback002(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getFLOW(), FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"), Job.getREQREFNO(), 
					 Job.getTRANREFNO(),  FormatUtils.dynaSQLDate(Job.getPAYDATE() ,"YYYY-MM-DD"), Job.getTRANTYPE(), Job.getPAYSL(), Job.getREQSL(), REQTIME, RESTIME, "SUCCESS", "", "", "HP200", "Callback Sent", "");
					 
					 Req_Modal.Insert_Callback_002(info);
					 
					 logger.debug(">>>>>> Callback successfully sent with Tran Ref Id :::: "+Job.getTRANREFNO()+" <<<<<<");
					 
					 details.addProperty("result", "success");
					 details.addProperty("stscode", "HP00");
					 details.addProperty("message", "Callback Request Processed Successfuly !!");
				 }
				 else
				 {
					 String ERRCD = Response_Code+"";
					 
					 String Message = API_Response.get("Message").getAsString();
					 
					 Callback002 info = new Callback002(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getFLOW(), FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"), Job.getREQREFNO(), 
					 Job.getTRANREFNO(),  FormatUtils.dynaSQLDate(Job.getPAYDATE() ,"YYYY-MM-DD"), Job.getTRANTYPE(), Job.getPAYSL(), Job.getREQSL(), REQTIME, RESTIME, "FAILED", ERRCD, Message, "", "", "");
					 
					 Req_Modal.Insert_Callback_002(info);
							 
					 String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
				     
					 Callback001 Info = new Callback001(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getFLOW(), FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"), Job.getREQREFNO(), Job.getTRANREFNO(), FormatUtils.dynaSQLDate(Job.getPAYDATE() ,"YYYY-MM-DD"), Job.getTRANTYPE(), Job.getPAYSL(), Serial, REQTIME, "Q");
					     
					 Req_Modal.Insert_Callback_001(Info);
					 
					 details.addProperty("result", "failed");
					 details.addProperty("stscode", "HP108");
					 details.addProperty("message", "Callback Request failed !!");
				 }	 
			 }
			 else
			 {
				 logger.debug(">>>>>> Payment details not found for ref id :::: "+Job.getTRANREFNO()+" <<<<<<");
				 
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP200");
				 details.addProperty("message", "Payment details not found !!");
			 }
		}
		catch(Exception e)
		{
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in TIPS_Callabck_Executer :::: "+details);
		}
	
		return details;
	}
	
	public JsonObject Hold_Callback(final Callback001 Job, String token)
	{
		String SERVICECD = "2121";
		
		JsonObject details = new JsonObject();  
		
		try
		{
			   Common_Utils util = new Common_Utils();
			
			   String sql = "Select * from pay001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQREFNO=? and TRANREFNO=? and REQSL=?";
			 
			   List<PAY_001> INFO = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getREQREFNO(), Job.getTRANREFNO(), Job.getPAYSL() }, new Pay001_Mapper());
				
			   if(INFO.size() !=0)   
			   {
				   JsonObject webservice_details = Ws.Get_Webserice_Info(Job.getSYSCODE(), Job.getPAYTYPE(), SERVICECD);
					  
				   JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
				  
				   String Headers_str = O_Headers.toString();
				  
				   String fsp_source = INFO.get(0).getD_FSPID();
				   String fsp_destination = INFO.get(0).getS_FSPID();
				   
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
		           
		           requesturl = requesturl.replace("~payerRef~", INFO.get(0).getPAYERID());
		           
		           sql = "Select * from PAY002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and TRANREFNO=? and REQSL=?";
					  
				   List<PAY_002> info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getTRANREFNO(), Job.getPAYSL() }, new Pay002_Mapper());
		    	  
		           String reversalReason = "";
		          
		           if(info.get(0).getHOLD_STATE().equalsIgnoreCase("CONFIRMED_HOLD"))
		           {
		        	  reversalReason = "successfull hold funds";
		        	  
		        	  PAYLOAD = PAYLOAD.replace("~reversalState~", "CONFIRMED_HOLD");
		        	  PAYLOAD = PAYLOAD.replace("~reversalReason~", reversalReason);
		          }
		          else
		          {
		        	  reversalReason = "failed to hold funds";
		        	  
		        	  PAYLOAD = PAYLOAD.replace("~reversalState~", "CONFIRMED_WITHDRAWN");
		        	  PAYLOAD = PAYLOAD.replace("~reversalReason~", reversalReason);
		          }
		          
				  webservice_details.addProperty("URI", requesturl);
				  webservice_details.addProperty("PAYLOAD", PAYLOAD);
				  webservice_details.add("Headers", O_Headers);
				  
				  String o_FLOW  =  "O"; 
			      String O_ReqRefID  = Job.getREQREFNO();
			      String O_MSGURL = requesturl; 
			      String O_IP = "";
			      String O_PORT = "";
			      String O_HEAD_MSG = O_Headers.toString();
			      String O_BODY_MSG = PAYLOAD;
			      String O_INITATEDBY = "HPAY";
			      String O_Checksum = "";
			      String date = util.getCurrentDate("dd-MMM-yyyy");
			      
			      Request_001 Request_001 = new Request_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), SERVICECD, o_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
				 
				  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
				  
				  JsonObject API_Response = new JsonObject();
		       
				  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
				  
				  logger.debug("PUT /messageTransfersReversal API_Response :::: "+API_Response);
				  
				  Response_001 Response = new Response_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), SERVICECD, "O", O_ReqRefID , requesturl , O_IP, O_PORT, O_HEAD_MSG, API_Response.toString(), "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);
				  
				  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
				  Timestamp RESTIME = util.get_oracle_Timestamp();
					 
				  Timestamp REQTIME = util.String_To_Timestamp(Job.getREQTIME());
					 
				  if(Response_Code == 202)
				  {
					  Callback002 call_Info = new Callback002(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getFLOW(), FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"), Job.getREQREFNO(), 
							  Job.getTRANREFNO(), FormatUtils.dynaSQLDate(Job.getPAYDATE() ,"YYYY-MM-DD"), Job.getTRANTYPE(), Job.getPAYSL(), Job.getREQSL(), REQTIME, RESTIME, "SUCCESS", "", "", "HP200", "Callback Sent", "");
					 
					  Req_Modal.Insert_Callback_002(call_Info);
					 
					  logger.debug(">>>>>> Hold Callback successfully sent with Tran Ref Id :::: "+Job.getTRANREFNO()+" <<<<<<");

					  /*** Initiate POST /transfersReversal to tips ****/
					  
					  String SERCODE = "2110";
					  
					  Job_005 Info3 = new Job_005(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getREQDATE(), Job.getREQREFNO(), Job.getREQSL(), "Q", "REVERSAL", reversalReason, "", SERCODE); 
						 
					  TIPS_Reversal_Transfer_Request(Info3, Job.getCHCODE(), fsp_source, fsp_destination);  
				  }
				  else  /*** Initiate Autocallback for hold service ****/
				  {
					  String ERRCD = Response_Code+"";
						 
					  String Message = API_Response.get("Message").getAsString();
						 
					  Callback002 callback_info = new Callback002(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getFLOW(), FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"), Job.getREQREFNO(), 
								 Job.getTRANREFNO(),  FormatUtils.dynaSQLDate(Job.getPAYDATE() ,"YYYY-MM-DD"), Job.getTRANTYPE(), Job.getPAYSL(), Job.getREQSL(), REQTIME, RESTIME, "FAILED", ERRCD, Message, "", "", "");
								 
					  Req_Modal.Insert_Callback_002(callback_info);
					  
					  String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
					 
					  Callback001 Info = new Callback001(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), "O", FormatUtils.dynaSQLDate(INFO.get(0).getREQDATE() ,"YYYY-MM-DD"), INFO.get(0).getREQREFNO(), INFO.get(0).getTRANREFNO(), FormatUtils.dynaSQLDate(INFO.get(0).getPAYDATE() ,"YYYY-MM-DD"), "HOLD", INFO.get(0).getREQSL(), Serial, REQTIME, "Q");
					    
					  Req_Modal.Insert_Callback_001(Info);
					  
					  logger.debug("Hold Callback to Tips is failed and request added to Callback Queue for Transref No:::: "+INFO.get(0).getTRANREFNO());
				  }
				  
				  details.addProperty("result", "success");
				  details.addProperty("stscode", Response_Code == 202 ? "HP00" : "HP301");
				  details.addProperty("message", Response_Code == 202 ? "Payment Hold Callback Successfull !!" : "Payment Hold Callback Failed !!");			  
			   }
			   else
			   {
					 logger.debug(">>>>>> Payment details not found for ref id :::: "+Job.getTRANREFNO()+" <<<<<<");
					 
					 details.addProperty("result", "failed");
					 details.addProperty("stscode", "HP200");
					 details.addProperty("message", "Payment details not found !!");
			   }			 			  
		}
		catch(Exception e)
		{
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in TIPS_Callabck_Executer :::: "+details);
		}
	
		return details;
	}
	
	public JsonObject Reversal_Callback(final Callback001 Job, String token)
	{
		String SERVICECD = "2110";
		
		JsonObject details = new JsonObject();  
		
		try
		{
			  Common_Utils util = new Common_Utils();
			
			  String sql = "Select * from PAY001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and TRANREFNO=? and REQSL=?";
			  
			  List<PAY_001> Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getTRANREFNO(), Job.getPAYSL() }, new Pay001_Mapper());
				
			  if(Info.size() != 0)
			  {
				  JsonObject webservice_details = Ws.Get_Webserice_Info(Job.getSYSCODE(), Job.getPAYTYPE(), SERVICECD);
				  
				  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
				  
				  String Headers_str = O_Headers.toString();
				  
				  Headers_str = Headers_str.replace("~Token~", token);
				  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
				  Headers_str = Headers_str.replace("~fsp-source~", Info.get(0).getD_FSPID());
				  Headers_str = Headers_str.replace("~fsp-destination~", Info.get(0).getS_FSPID());
				  Headers_str = Headers_str.replace("~fsp-encryption~", "");
				  Headers_str = Headers_str.replace("~fsp-signature~", "");
				  Headers_str = Headers_str.replace("~fsp-uri~", "");
				  Headers_str = Headers_str.replace("~fsp-http-method~", "");
				  Headers_str = Headers_str.replace("~date~", util.getCurrentDateTime());  
				  
				  O_Headers = util.StringToJsonArray(Headers_str);
				  
				  sql = "Select * from PAY002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and TRANREFNO=? and REQSL=?";
				  
				  List<PAY_002> info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getTRANREFNO(), Job.getPAYSL() }, new Pay002_Mapper());
	    	  
				  String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
				  
		          String requesturl = webservice_details.get("URI").getAsString(); 
		          
		          PAYLOAD = PAYLOAD.replace("~payerRef~", info.get(0).getPAYERID());
		          PAYLOAD = PAYLOAD.replace("~payeeReversalRef~", info.get(0).getPAYEE_REVREF());
		          PAYLOAD = PAYLOAD.replace("~reversalState~", "REVERSED");
		          PAYLOAD = PAYLOAD.replace("~reversalReason~", info.get(0).getREV_REASON());
		          
		          webservice_details.addProperty("URI", requesturl);
				  webservice_details.addProperty("PAYLOAD", PAYLOAD);
				  webservice_details.add("Headers", O_Headers);
				  
				  String o_FLOW  =  "O"; 
			      String O_ReqRefID  = info.get(0).getREQREFNO(); 
			      String O_MSGURL = requesturl; 
			      String O_IP = "";
			      String O_PORT = "";
			      String O_HEAD_MSG = O_Headers.toString();
			      String O_BODY_MSG = PAYLOAD;
			      String O_INITATEDBY = "HPAY";
			      String O_Checksum = "";
			      String date = util.getCurrentDate("dd-MMM-yyyy");
			      
			      Request_001 Request_001 = new Request_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), SERVICECD, o_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
				 
				  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
				  
				  JsonObject API_Response = new JsonObject();
		       
				  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
				  
				  logger.debug("POST /transfersReversal API_Response :::: "+API_Response);
				  
				  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
				  Response_001 Response = new Response_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), SERVICECD, "O", O_ReqRefID , requesturl , O_IP, O_PORT, O_HEAD_MSG, API_Response.toString(), "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);
				  
				  Timestamp RESTIME = util.get_oracle_Timestamp();
					 
				  Timestamp REQTIME = util.String_To_Timestamp(Job.getREQTIME());
					 
				  if(Response_Code == 202)
				  {
					  Callback002 Callback_info = new Callback002(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getFLOW(), FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"), Job.getREQREFNO(), 
								 Job.getTRANREFNO(),  FormatUtils.dynaSQLDate(Job.getPAYDATE() ,"YYYY-MM-DD"), Job.getTRANTYPE(), Job.getPAYSL(), Job.getREQSL(), REQTIME, RESTIME, "SUCCESS", "", "", "HP200", "Callback Sent", "");
								 
					  Req_Modal.Insert_Callback_002(Callback_info);
					 
					  logger.debug(">>>>>> Reversal Callback successfully sent with Tran Ref Id :::: "+Job.getTRANREFNO()+" <<<<<<");
					 
					  /*** Update Reversal Status ***/
					  
					  String REV_DATE = util.getCurrentDate("dd-MMM-yyyy");
		        	  
		        	  sql = "update pay002 set ISREVERSED=?, REV_DATE=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
					   
					  Jdbctemplate.update(sql, new Object[] { "1", REV_DATE, "F", "1", "REVERSED", Info.get(0).getCHCODE(), Job.getPAYTYPE(), Info.get(0).getREQREFNO(), Info.get(0).getREQSL() });
					  
					  details.addProperty("result", "success");
					  details.addProperty("stscode", "HP00");
					  details.addProperty("message", "Callback Request Processed Successfuly !!");	
				  }
				  else
				  {
					 String ERRCD = Response_Code+"";
						 
					 String Message = API_Response.get("Message").getAsString();
					 
					 Callback002 Callback_info = new Callback002(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getFLOW(), FormatUtils.dynaSQLDate(Job.getREQDATE() ,"YYYY-MM-DD"), Job.getREQREFNO(), 
							  Job.getTRANREFNO(),  FormatUtils.dynaSQLDate(Job.getPAYDATE() ,"YYYY-MM-DD"), Job.getTRANTYPE(), Job.getPAYSL(), Job.getREQSL(), REQTIME, RESTIME, "FAILED", ERRCD, Message, "", "", "");
					 
					 Req_Modal.Insert_Callback_002(Callback_info);
					 
					  /*** Initiate auto callback to TIPS ***/
						 
					 String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
				     
					 Callback001 callback = new Callback001(Info.get(0).getSUBORGCODE(), Job.getSYSCODE(), Info.get(0).getCHCODE(), Info.get(0).getPAYTYPE(), "O", FormatUtils.dynaSQLDate(Info.get(0).getREQDATE() ,"YYYY-MM-DD"), Info.get(0).getREQREFNO(), Info.get(0).getTRANREFNO(), FormatUtils.dynaSQLDate(Info.get(0).getPAYDATE() ,"YYYY-MM-DD"), "REVERSAL", Info.get(0).getREQSL(), Serial, REQTIME, "Q");
					
					 Req_Modal.Insert_Callback_001(callback);
					 
					 details.addProperty("result", "failed");
					 details.addProperty("stscode", "HP108");
					 details.addProperty("message", "Callback Request failed !!");
				  }
			  }
			  else
			  {
				  logger.debug(">>>>>> Payment Reversal details not found for ref id :::: "+Job.getTRANREFNO()+" <<<<<<");
					 
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP200");
				  details.addProperty("message", "Payment details not found !!");
			  } 
		}
		catch(Exception e)
		{
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in TIPS_Callabck_Executer :::: "+details);
		}
	
		return details;
	}
	
	public JsonObject Confirm_Hold_Response_to_TIPS(JsonObject webservice_details, Job_005 Job, String CHANNEL, int err, String errormsg) /***  PUT /messageTransfersReversal/{payerRef} (for update & notification ***/
	{
		String SERVICECD = "2121";
		
		JsonObject details = new JsonObject(); 
		
		try
		{
			   Common_Utils util = new Common_Utils();
			   
			   JsonObject Token_details = new JsonObject();
				  
			   String token = ""; boolean Callback = false;
			   
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
						 
						 Callback = true;
				   }
			   }
			  
			   String old_payload = webservice_details.get("PAYLOAD").getAsString();
				  
			   JsonObject info = util.StringToJsonObject(old_payload);
			  
			   String payerRef = info.get("payerRef").getAsString();
			   String payeeRef = info.get("payeeRef").getAsString();
			  
			   String[] payer_temp = payerRef.split("-");
			   String[] payee_temp = payeeRef.split("-");
			   
			   String fsp_source = payee_temp[0];   /*** Exim Bank ***/
			   String fsp_destination = payer_temp[0]; /*** other bank ***/
			   
			   if(err == 0)
	           {
				  String HOLD_DATE = util.getCurrentDate("dd-MMM-yyyy");
				   
				  String payeeReversalRef = Generate_Transfer_Reference("013", "REVERSAL").get("Reference_Id").getAsString();
				  
				  String sql = "update pay002 set HOLD_DATE=?, HOLD_STATE=?, PAYEE_REVREF=? where CHCODE=? and PAYTYPE=? and tranrefno=? and REQSL=?";
				   
				  Jdbctemplate.update(sql, new Object[] { HOLD_DATE, "CONFIRMED_HOLD", payeeReversalRef, Job.getCHCODE(), Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });
	           }
			   else
			   {   
				  JsonObject js = Get_CBS_ERRINFO(errormsg);
				  
				  String ERRCD = js.get("ERRCD").getAsString();
				  String ERRDESC = js.get("ERRDESC").getAsString();
				  
				  String sql = "update pay002 set HOLD_STATE=?, ERRCD=?, ERRDES=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
				   
				  Jdbctemplate.update(sql, new Object[] { "CONFIRMED_WITHDRAWN", ERRCD, ERRDESC, CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });
			   }	
			   
			   if(Callback)
			   { 
				  String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
				     
				  Timestamp REQTIME  = util.get_oracle_Timestamp();
				  
				  String sql = "Select * from pay001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQSL=?";
					 
				  List<PAY_001> INFO = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), Job.getREQSL() }, new Pay001_Mapper());
					
				  if(INFO.size() !=0)   
				  {
					  Callback001 Info = new Callback001(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), "O", FormatUtils.dynaSQLDate(INFO.get(0).getREQDATE() ,"YYYY-MM-DD"), INFO.get(0).getREQREFNO(), INFO.get(0).getTRANREFNO(), FormatUtils.dynaSQLDate(INFO.get(0).getPAYDATE() ,"YYYY-MM-DD"), "HOLD", INFO.get(0).getREQSL(), Serial, REQTIME, "Q");
					    
					  Req_Modal.Insert_Callback_001(Info);
					  
					  logger.debug("Hold Callback to Tips is failed and request added to Callback Queue for Transref No:::: "+INFO.get(0).getTRANREFNO());					  
				  }
				  else
				  {  
					  details.addProperty("result", "success");
					  details.addProperty("stscode", "HP200");
					  details.addProperty("message", "Payment details not found !!");	
					  
					  logger.debug("payement details not found for the hold service for Ref No:::: "+Job.getREFNO());
				  }
			   }
			   else
			   {
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
		           
		           String reversalReason = "";
		          
		           if(err == 0)
		           {
		        	  reversalReason = "successfull hold funds";
		        	  
		        	  PAYLOAD = PAYLOAD.replace("~reversalState~", "CONFIRMED_HOLD");
		        	  PAYLOAD = PAYLOAD.replace("~reversalReason~", reversalReason);
		          }
		          else
		          {
		        	  reversalReason = "failed to hold funds";
		        	  
		        	  PAYLOAD = PAYLOAD.replace("~reversalState~", "CONFIRMED_WITHDRAWN");
		        	  PAYLOAD = PAYLOAD.replace("~reversalReason~", reversalReason);
		          }
		          
				  webservice_details.addProperty("URI", requesturl);
				  webservice_details.addProperty("PAYLOAD", PAYLOAD);
				  webservice_details.add("Headers", O_Headers);
				  
				  String o_FLOW  =  "O"; 
			      String O_ReqRefID  = Job.getREFNO();
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
				  
				  logger.debug("PUT /messageTransfersReversal API_Response :::: "+API_Response);
				  
				  Response_001 Response = new Response_001(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), SERVICECD, "O", O_ReqRefID , requesturl , O_IP, O_PORT, O_HEAD_MSG, API_Response.toString(), "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);
				  
				  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
				  if(Response_Code == 202)
				  {
					  /*** Initiate POST /transfersReversal to tips ****/
					  
					  String SERCODE = "2110";
					  
					  Job_005 Info3 = new Job_005(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getREQDATE(), Job.getREFNO(), Job.getREQSL(), "Q", "REVERSAL", reversalReason, "", SERCODE); 
						 
					  TIPS_Reversal_Transfer_Request(Info3, Job.getCHCODE(),  fsp_source , fsp_destination);  
				  }
				  else  /*** Initiate Autocallback for hold service ****/
				  {
					  String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
					     
					  Timestamp REQTIME  = util.get_oracle_Timestamp();
					  
					  String sql = "Select * from pay001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQSL=?";
						 
					  List<PAY_001> INFO = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), Job.getREQSL() }, new Pay001_Mapper());
						
					  if(INFO.size() !=0)   
					  {
						  Callback001 Info = new Callback001(Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), "O", FormatUtils.dynaSQLDate(INFO.get(0).getREQDATE() ,"YYYY-MM-DD"), INFO.get(0).getREQREFNO(), INFO.get(0).getTRANREFNO(), FormatUtils.dynaSQLDate(INFO.get(0).getPAYDATE() ,"YYYY-MM-DD"), "HOLD", INFO.get(0).getREQSL(), Serial, REQTIME, "Q");
						    
						  Req_Modal.Insert_Callback_001(Info);
						  
						  logger.debug("Hold Callback to Tips is failed and request added to Callback Queue for Transref No:::: "+INFO.get(0).getTRANREFNO());
						  	  
						  details.addProperty("result", "success");
						  details.addProperty("stscode", "HP06");
						  details.addProperty("message", "Callback failed !!");	
					  }
					  else
					  {
						  logger.debug("payement details not found for the hold service for Ref No:::: "+Job.getREFNO());
					  }
				  }
				  
				  details.addProperty("result", "success");
				  details.addProperty("stscode", Response_Code == 202 ? "HP00" : "HP301");
				  details.addProperty("message", Response_Code == 202 ? "Payment Hold Callback Successfull !!" : "Payment Hold Callback Failed !!");			  
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
	
	public JsonObject TIPS_Reversal_Transfer_Request(Job_005 Job, String CHANNEL, String fsp_source, String fsp_destination) /***  POST /transfersReversal/{payerRef} ***/
	{
		String SERVICECD = "2110";
		
		JsonObject details = new JsonObject();  
		
		try
		{
			   Common_Utils util = new Common_Utils();
			
			   JsonObject Token_details = new JsonObject();
			  
			   String token = ""; boolean Callback = false;
			   
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
						 
						 Callback = true; 
				   }
			  }
			
			  String sql = "Select * from PAY002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and TRANREFNO=? and REQSL=?";
				  
			  List<PAY_002> Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() }, new Pay002_Mapper());
    
			  if(Callback)
			  {
				 /*** Initiate auto callback to TIPS ***/
					 
				 String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
			     
				 Timestamp REQTIME  = new java.sql.Timestamp(new java.util.Date().getTime());  //Info.get(0)
				     
				 Callback001 info = new Callback001(Info.get(0).getSUBORGCODE(), Job.getSYSCODE(), Info.get(0).getCHCODE(), Info.get(0).getPAYTYPE(), "O", FormatUtils.dynaSQLDate(Info.get(0).getREQDATE() ,"YYYY-MM-DD"), Info.get(0).getREQREFNO(), Info.get(0).getTRANREFNO(), FormatUtils.dynaSQLDate(Info.get(0).getPAYDATE() ,"YYYY-MM-DD"), "REVERSAL", Info.get(0).getREQSL(), Serial, REQTIME, "Q");
				
				 Req_Modal.Insert_Callback_001(info);
			  }
			  else
			  {
				  JsonObject webservice_details = Ws.Get_Webserice_Info(Job.getSYSCODE(), Job.getPAYTYPE(), SERVICECD);
				  
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
		          
		          PAYLOAD = PAYLOAD.replace("~payerRef~", Info.get(0).getPAYERID());
		          PAYLOAD = PAYLOAD.replace("~payeeReversalRef~", Info.get(0).getPAYEE_REVREF());
		          PAYLOAD = PAYLOAD.replace("~reversalState~", "REVERSED");
		          PAYLOAD = PAYLOAD.replace("~reversalReason~", Info.get(0).getREV_REASON());
		          
	        	  webservice_details.addProperty("URI", requesturl);
				  webservice_details.addProperty("PAYLOAD", PAYLOAD);
				  webservice_details.add("Headers", O_Headers);
				  
				  String o_FLOW  =  "O"; 
			      String O_ReqRefID  = Job.getREFNO();
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
				  
				  logger.debug("POST /transfersReversal API_Response :::: "+API_Response);
				  
				  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
				  Response_001 Response = new Response_001(Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), SERVICECD, "O", O_ReqRefID , requesturl , O_IP, O_PORT, O_HEAD_MSG, API_Response.toString(), "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);
				  
				  if(Response_Code == 202)
				  {
					  String REV_DATE = util.getCurrentDate("dd-MMM-yyyy");
		        	  
		        	  sql = "update pay002 set ISREVERSED=?, REV_DATE=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
					   
					  Jdbctemplate.update(sql, new Object[] { "1", REV_DATE, "F", "1", "REVERSED", CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() });
				  }
				  else
				  {
					  /*** Initiate auto callback to TIPS ***/
						 
					 String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
				     
					 Timestamp REQTIME  = new java.sql.Timestamp(new java.util.Date().getTime());  //Info.get(0)
					     
					 Callback001 info = new Callback001(Info.get(0).getSUBORGCODE(), Job.getSYSCODE(), Info.get(0).getCHCODE(), Info.get(0).getPAYTYPE(), "O", FormatUtils.dynaSQLDate(Info.get(0).getREQDATE() ,"YYYY-MM-DD"), Info.get(0).getREQREFNO(), Info.get(0).getTRANREFNO(), FormatUtils.dynaSQLDate(Info.get(0).getPAYDATE() ,"YYYY-MM-DD"), "REVERSAL", Info.get(0).getREQSL(), Serial, REQTIME, "Q");
					
					 Req_Modal.Insert_Callback_001(info);
				  }
				  
				  logger.debug(Response_Code == 202 ? "Payment Reversed request sent Successfull !!" : "Payment Reversed request sent Failed !!");
				  
				  details.addProperty("result", "success");
				  details.addProperty("stscode", Response_Code == 202 ? "HP00" : "HP301");
				  details.addProperty("message", Response_Code == 202 ? "Payment Reversed Successfull !!" : "Payment Reversed Failed !!");
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
	
	public JsonObject Channel_Hold_Request_To_TIPS(Job_005 Job, String CHANNEL, int err, String errormsg) /***  POST /messageTransfersReversal  ***/
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
			 
			  String sql = "Select * from pay001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQSL=?";
				 
			  List<PAY_001> info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), Job.getREQSL() }, new Pay001_Mapper());
				  
			  String fsp_source = "013";
			  String fsp_destination = "tips";
			  
	          if(info.size() !=0) 
	          {
	        	  fsp_destination = info.get(0).getD_FSPID();
	          }
			   
			  JsonObject webservice_details = Ws.Get_Webserice_Info(Job.getSYSCODE(), Job.getPAYTYPE(), SERVICECD);

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
	          
	          sql = "Select * from PAY002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and TRANREFNO=? and REQSL=?";
			  
			  List<PAY_002> Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), CHANNEL, Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() }, new Pay002_Mapper());
			
	          if(Info.size() !=0) 
	          {
	        	  PAYLOAD = PAYLOAD.replace("~payerReversalRef~", Info.get(0).getPAYER_REVREF());
	        	  PAYLOAD = PAYLOAD.replace("~payerRef~", Info.get(0).getPAYERID());
	        	  PAYLOAD = PAYLOAD.replace("~payeeRef~", Info.get(0).getPAYEEID());
	        	  PAYLOAD = PAYLOAD.replace("~switchRef~", Info.get(0).getTRAN_SWITCHREF());
	        	  PAYLOAD = PAYLOAD.replace("~amount~", Info.get(0).getREV_AMOUNT());
	        	  PAYLOAD = PAYLOAD.replace("~currency~", Info.get(0).getREV_CURR());
	        	  PAYLOAD = PAYLOAD.replace("~reversalReason~", Info.get(0).getREV_REASON());
	        	  PAYLOAD = PAYLOAD.replace("~reversalState~", Info.get(0).getREV_STATE());

				  webservice_details.addProperty("URI", requesturl);
				  webservice_details.addProperty("PAYLOAD", PAYLOAD);
				  webservice_details.add("Headers", O_Headers);
				  
				  String o_FLOW  =  "O"; 
			      String O_ReqRefID  = Job.getREFNO();
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
			 String Email = primaryarg.has("email") ? primaryarg.get("email").getAsString() : "";
			 
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
					 Email = FullName.length() == 0 ? Ac_Info.getEMAIL_ID() : Email;
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
		      TIPS_001.setEMAIL(Email);
		      
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
					  	 		 
					  TIPS001 tips_001 = new TIPS001();
					  
					  tips_001.setSUBORGCODE(SUBORGCODE);
					  tips_001.setREQUESTID(reqrefid); 
					  tips_001.setREQUESTDATE(Request_Date); 
					  tips_001.setIDENTIFIERNO(identifier); 
					  tips_001.setSTATUS(PRIELEMENTS.get("status").getAsString());
					  
					  Update_TIPS_001(tips_001);
					  
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
					  TIPS_002.setEMAIL(Email);
					  
					  Insert_TIPS_002(TIPS_002); 
					  
					  details.addProperty("result", "success");  
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
					
					  Update_Fail_TIPS_001(tips_001);
					  
					  details.addProperty("result", "failed");  
					  details.addProperty("stscode", "HP06");  
					  details.addProperty("message", errorDescription);  
				  }
				  
				  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, "O", reqrefid , requesturl , IP, PORT, HEAD_MSG, response, "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	
			 }
	         else
	         {
	        	 TIPS001 tips_001 = new TIPS001();
				  
				 tips_001.setSUBORGCODE(SUBORGCODE);
				 tips_001.setERRORCD("failed");
				 tips_001.setERRDESC(API_Response.get("Message").getAsString());
				 tips_001.setREQUESTID(reqrefid); 
				 tips_001.setREQUESTDATE(Request_Date); 
				 tips_001.setIDENTIFIERNO(identifier); 
				  
				 Update_Fail_TIPS_001(tips_001);
				 
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
	
	public JsonObject Register_Identifier(Identifiers001 Info)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE  = "EXIM";
		String CHCODE  = "EXIMPAY";  
		String SERVICECD = "2000"; 
		String SYSCODE = "HP"; 
		String BANKCODE = "013";
		String PAYTYPE = "TIPS";
		
		try
		{
			 Common_Utils util = new Common_Utils();
			
			 String date = util.getCurrentDate("dd-MMM-yyyy");
			 String reqrefid = Req_Modal.Generate_Register_Reference_Id().get("Reference_Id").getAsString();
			
			 String identifier = Info.getIDENTIFIERNO();
			 String acno = Info.getACCOUNTNO();
			 String identifiertype = "ALIAS";
			 String fspId = BANKCODE;
			 String accountType = "BANK";  
			 String accountCategory = Info.getACCAT();
			 String custno = Info.getCUSTNO();
			 String msisdn = Info.getMSISDN();
			 String identity_type = Info.getIDTYPE1();
			 String identity_value = Info.getIDTYPEVALUE1();
			 String FullName = Info.getIDENTIFIERNAME();
			 String Email = Info.getEMAIL();
			 
			 String MSGTYPE 	     =  SERVICECD;  								
			 String FLOW    	     =  "O"; 									  									
			 String IP      	     =  ""; 
			 String PORT    	     =  "";  									
			 String HEAD_MSG    	 =  "";
			 String fsp_source  	 =  BANKCODE;
			 String fsp_destination  =  "tips";
			 
			 TIPS001 TIPS_001 = new TIPS001();
		      
		     TIPS_001.setSUBORGCODE(SUBORGCODE);
		     TIPS_001.setREQUESTID(reqrefid); 
		     TIPS_001.setREQUESTDATE(date); 
		     TIPS_001.setFSP_SRCID(fsp_source); 
		     TIPS_001.setFSP_DESID(fsp_destination); 
		     TIPS_001.setIDENTIFIERNO(identifier); 
		     TIPS_001.setIDENTIFIERTYPE(identifiertype);
		     TIPS_001.setFSPID(fspId); 
		     TIPS_001.setIDENTIFIERNAME(FullName);    
		     TIPS_001.setACCAT(accountCategory); 
		     TIPS_001.setACTYPE(accountType); 
		     TIPS_001.setACCAT(accountCategory);
		     TIPS_001.setACCOUNTNO(acno);
		     TIPS_001.setCUSTNO(custno);
		     TIPS_001.setMSISDN(msisdn);
		     TIPS_001.setEMAIL(Email);
		     
		     JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, PAYTYPE, SERVICECD);
			 
			 if(webservice_details.get("Result").getAsString().equals("Failed"))
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP103");
				 details.addProperty("message", webservice_details.get("Message").getAsString()); 
				 	  
				 TIPS_001.setSTATUS(details.get("result").getAsString()); 
			     TIPS_001.setERRORCD(details.get("stscode").getAsString()); 
			     TIPS_001.setERRDESC(details.get("message").getAsString()); 
			      
			     Insert_TIPS_001(TIPS_001); 
			      
				 return details; 
			 }
		  
			 String sql = "Select count(*) from tips002 where SUBORGCODE=? and IDENTIFIERNO=? and IDENTIFIERTYPE=? and ACCOUNTNO=? and MSISDN=?"; 
			 
			 int count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, identifier, identifiertype, acno , msisdn }, Integer.class);
			 
			 if(count !=0)
			 {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP94");
				  details.addProperty("message", "duplicate request , Identifier already created with this Account No and MSISDN");
				  
				  TIPS_001.setSTATUS(details.get("result").getAsString()); 
			      TIPS_001.setERRORCD(details.get("stscode").getAsString()); 
			      TIPS_001.setERRDESC(details.get("message").getAsString()); 
			      
			      Insert_TIPS_001(TIPS_001); 
				 
				  return details;
			 }
			
			 JsonObject Account_Info = Retrieve_Account_Information(acno, msisdn);
			 
			// Account_Information Ac_Info = new Account_Information();
			 
			 if(Account_Info.get("result").getAsString().equals("failed"))   
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06"); 
				 details.addProperty("message", "Account details not found from the database !!");
				 
				 TIPS_001.setSTATUS(details.get("result").getAsString()); 
			     TIPS_001.setERRORCD(details.get("stscode").getAsString()); 
			     TIPS_001.setERRDESC(details.get("message").getAsString()); 
			      
			     Insert_TIPS_001(TIPS_001); 
				 
				 return details;
			 }
			 /*else
			 {
				 JsonObject js = Account_Info.get("Informations").getAsJsonObject();
				 
				 Ac_Info = new Gson().fromJson(js, Account_Information.class);
				 
				 String Cus_No = Ac_Info.getCUSTOMERNO();
				 
				 if(Cus_No != null && !Cus_No.equals(custno))
				 {
					 details.addProperty("result", "failed");
					 details.addProperty("stscode", "HP06"); 
					 details.addProperty("message", "Provided Customer No is different from existing customer No !!");
					 
					 TIPS_001.setSTATUS(details.get("result").getAsString()); 
				     TIPS_001.setERRORCD(details.get("stscode").getAsString()); 
				     TIPS_001.setERRDESC(details.get("message").getAsString()); 
				      
				     Insert_TIPS_001(TIPS_001); 
				     
					 return details;
				 }	
			  } */
			
			  JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
			 
			  JsonObject Token_details = new JsonObject();
					  
			  String token = "";
			 
			  Token_details = tk.get_stored_token(SUBORGCODE, CHCODE, PAYTYPE);
			  
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
					   
					    Token_Info info = new Token_Info(SUBORGCODE, CHCODE, PAYTYPE, token);
					   
					    tk.Check_and_Update_tokens(info);
				   }
				   else
				   {
					    logger.debug("Error in Generating token from TIPs :::: "+Token_details);
						 
					    details.addProperty("result",  "failed");
						details.addProperty("stscode", "HP111");
						details.addProperty("message", "payment gateway token generation failed");  
						 
						TIPS_001.setSTATUS(details.get("result").getAsString()); 
					    TIPS_001.setERRORCD(details.get("stscode").getAsString()); 
					    TIPS_001.setERRDESC(details.get("message").getAsString()); 
					      
					    Insert_TIPS_001(TIPS_001); 
					    
						return details; 
				   }
			  }
				  
			  String urlparam = webservice_details.get("PAYLOAD").getAsString();
			  
			  urlparam = urlparam.replace("~identifier~", identifier);
			 
			  webservice_details.addProperty("PAYLOAD", urlparam);
			  
			  String Headers_str = O_Headers.toString();
			  
			  Headers_str = Headers_str.replace("~Token~", token);
			  Headers_str = Headers_str.replace("~x-forwarded-for~", "");
			  Headers_str = Headers_str.replace("~fsp-source~", BANKCODE);
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
		      String O_IP = "";
		      String O_PORT = "";
		      String O_HEAD_MSG = O_Headers.toString();
		      String O_BODY_MSG = urlparam;
		      String O_INITATEDBY = "HPAY";
		      String O_Checksum = "";
		      
		      Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, PAYTYPE, SERVICECD, O_FLOW, date, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
			 
			  Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
			  
			  JsonObject API_Response = new JsonObject();
		
			  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
			    
	          logger.debug("Register_Identifier API_Response :::: "+API_Response);
	         
	          if(API_Response.get("Result").getAsString().equals("Success"))
			  {
	        	  int Response_Code = API_Response.get("Response_Code").getAsInt();
				  
	        	  String response = API_Response.get("Response").getAsString();
	        	  
				  if(Response_Code == 200)
				  {
					  String Status = response.contains("Active") ? "Active" : "Inactive";	  
					  	 		 
					  TIPS_001.setSTATUS("Success"); 
					 
					  Insert_TIPS_001(TIPS_001); 
					  
					  TIPS002 TIPS_002 = new TIPS002();

					  TIPS_002.setSUBORGCODE(SUBORGCODE); 
					  TIPS_002.setFSP_SRCID(fsp_source); 
					  TIPS_002.setFSP_DESID(fsp_destination); 
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
					  TIPS_002.setREGDATE(date); 
					  TIPS_002.setREGDATETIME(util.get_oracle_Timestamp()); 
					  TIPS_002.setSTATUS(Status); 
					  TIPS_002.setMSISDN(msisdn);
					  TIPS_002.setEMAIL(Email);
					  
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
					  
					  details.addProperty("result", "failed");  
					  details.addProperty("stscode", "HP06");  
					  details.addProperty("message", errorDescription);  
					  
					  TIPS_001.setSTATUS(details.get("result").getAsString()); 
					  TIPS_001.setERRORCD(details.get("stscode").getAsString()); 
					  TIPS_001.setERRDESC(details.get("message").getAsString()); 
						      
					  Insert_TIPS_001(TIPS_001);  
				  }
				  
				  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, PAYTYPE, MSGTYPE, FLOW, reqrefid , requesturl , IP, PORT, HEAD_MSG, response, "TIPS", "");
					
				  Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	
			 }
	         else
	         {
	        	 Response_001 Response = new Response_001(SUBORGCODE, CHCODE, PAYTYPE, MSGTYPE, FLOW , reqrefid , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), "TIPS", "");
					
			     Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	  
  
			     details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06");
				 details.addProperty("message", API_Response.get("Message").getAsString()); 
				 
				 TIPS_001.setSTATUS(details.get("result").getAsString()); 
				 TIPS_001.setERRORCD(details.get("stscode").getAsString()); 
				 TIPS_001.setERRDESC(details.get("message").getAsString()); 
				      
				 Insert_TIPS_001(TIPS_001); 
  
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
						  
						  Update_Fail_TIPS_001(tips_001);		    
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
	
	public JsonObject Identifier_Excel_Upload(String PAYTYPE, CommonsMultipartFile Attachment)
	{	
		 JsonObject details = new JsonObject();
		
		 JsonArray Validation_errors = new JsonArray();  
		 
		 try
		 {
			 String File_Name = Attachment.getOriginalFilename();
			 
			 String Extension = FilenameUtils.getExtension(File_Name);
			 
			 InputStream Uploaded_File =  new BufferedInputStream(Attachment.getInputStream());
			
			 if(File_Name != null && !File_Name.contains("TIPS_Identifier_Bulk_Upload") && !Extension.contains("xlsx"))  
			 {
			     Validation_errors.add("Wrong file Uploaded !!");
			 }
			 
			 XSSFWorkbook workbook = new XSSFWorkbook(Uploaded_File);
			 
			 XSSFSheet Identifier_sheet = workbook.getSheetAt(0); 
			 
			 Row Header_row = (Row)Identifier_sheet.getRow(0);  
			 
			 if(!Header_row.getCell(0).toString().equals("SERIAL") || !Header_row.getCell(1).toString().equals("ACCOUNT NO") || !Header_row.getCell(2).toString().equals("MOBILE NO") || 
			 !Header_row.getCell(3).toString().equals("ACCOUNT CURRENCY CODE") || !Header_row.getCell(4).toString().equals("ACCOUNT TYPE") || !Header_row.getCell(5).toString().equals("CUSTOMER NAME") ||
			 !Header_row.getCell(6).toString().equals("CUSTOMER NO") || !Header_row.getCell(7).toString().equals("EMAIL ID") || !Header_row.getCell(8).toString().equals("NIDA ID") || !Header_row.getCell(9).toString().equals("TIPS IDENTIFIER"))
			 {
				 Validation_errors.add("Excel file not in the Proper Format !!");
			 }		 
			 
			 if(Identifier_sheet.getLastRowNum() > 10001)
			 {
				 Validation_errors.add("Maximun No of Row limit is 10,000 only !!");
			 }
			 
			 Common_Utils utils = new Common_Utils();
				
		     DataFormatter formatter = new DataFormatter();
		   
			 List<Identifiers001> Infos = new ArrayList<Identifiers001>();
			 
			 int Valid_Cnt = 0;
			 
			 if(Validation_errors.size() == 0)
			 {
				 for(int i=1; i<=Identifier_sheet.getLastRowNum(); i++)			
				 { 												 
					 Row row = (Row)Identifier_sheet.getRow(i);  

					 String SERIAL = utils.ReplaceNull(formatter.formatCellValue(row.getCell(0)));
					 String ACCOUNTNO = utils.ReplaceNull(formatter.formatCellValue(row.getCell(1)));
					 String MSISDN = utils.ReplaceNull(formatter.formatCellValue(row.getCell(2)));
					 String CURRENCY_CODE = utils.ReplaceNull(formatter.formatCellValue(row.getCell(3)));
					 String ACCAT = utils.ReplaceNull(formatter.formatCellValue(row.getCell(4)));
					 String IDENTIFIERNAME = utils.ReplaceNull(formatter.formatCellValue(row.getCell(5)));
					 String CUSTNO = utils.ReplaceNull(formatter.formatCellValue(row.getCell(6)));
					 String EMAIL = utils.ReplaceNull(formatter.formatCellValue(row.getCell(7)));
					 String NIDA_ID = utils.ReplaceNull(formatter.formatCellValue(row.getCell(8)));
					 String IDENTIFIERNO = utils.ReplaceNull(formatter.formatCellValue(row.getCell(9)));
	    		    		
					 Identifiers001 Info = new Identifiers001();
		    		
					 Info.setSERIAL(SERIAL);
					 Info.setREQSL(i);
					 Info.setACCOUNTNO(ACCOUNTNO);
					 Info.setMSISDN(MSISDN); 
					 Info.setCURRENCY(CURRENCY_CODE); 
					 Info.setACCAT(ACCAT); 
					 Info.setIDENTIFIERNAME(IDENTIFIERNAME); 
					 Info.setCUSTNO(CUSTNO); 
					 Info.setEMAIL(EMAIL); 
					 Info.setIDTYPE1("NIN");
					 Info.setIDTYPEVALUE1(NIDA_ID);
					 Info.setIDENTIFIERNO(IDENTIFIERNO); 
					
					 ACCAT = ACCAT.equalsIgnoreCase("P") ? "PERSON" : ACCAT.equalsIgnoreCase("B") ? "BUSINESS" : ACCAT.equalsIgnoreCase("M") ? "MERCHANT" : ACCAT;
					 
					 Info.setACCAT(ACCAT); 
					
					 if(utils.isNullOrEmpty(ACCOUNTNO) || utils.isNullOrEmpty(MSISDN) || utils.isNullOrEmpty(ACCAT) || utils.isNullOrEmpty(IDENTIFIERNAME)  || utils.isNullOrEmpty(CUSTNO) || utils.isNullOrEmpty(IDENTIFIERNO))
					 {
						 Info.setVALID("0");
						 
						 Info.setREASON("Any one of the following values are Missing ACCOUNTNO / MOBILE NO / CURRENCY / ACCOUNT TYPE / CUSTOMER NO / IDENTIFIER NO");
					 }
					 else
					 {
						 if(!CURRENCY_CODE.equalsIgnoreCase("TZS"))
						 {
							 Info.setREASON("CURRENCY value must be TZS, Not in "+CURRENCY_CODE);
							 
							 Info.setVALID("0");
						 }
						 else if(!ACCAT.equalsIgnoreCase("PERSON") && !ACCAT.equalsIgnoreCase("BUSINESS") && !ACCAT.equalsIgnoreCase("MERCHANT"))
						 {
							 Info.setREASON("Possible value of the ACCOUNT TYPE are P / B / M");
							 
							 Info.setVALID("0");
						 }
						 else
						 {
							 Valid_Cnt++;
							 
							 Info.setREASON("-");
							 
							 Info.setVALID("1");
						 }					 
					 }
					 
					 if(Info.getVALID().equals("1"))
		   			 {				
		   				 Info.setAPPROVED("1");
		   			 }
					 else
					 {
						 Info.setAPPROVED("");
					 }
					
					 Infos.add(Info);
				 }	 
				 
				 details.addProperty("Total_Valid", Valid_Cnt);
				 details.addProperty("Total_InValid", Identifier_sheet.getLastRowNum() - Valid_Cnt);
				 
				 details.add("Identifiers", new Gson().toJsonTree(Infos)); 
			 }
			 else
			 {
				 details.add("Validation_errors", Validation_errors);
			 }	

			 details.addProperty("result",  Validation_errors.size() == 0 ? "success" : "failed");
			 details.addProperty("stscode", Validation_errors.size() == 0 ? "HP00" : "HP06");
			 details.addProperty("message", Validation_errors.size() == 0 ? "Excel Validated Successfully !!!" : "Excel Validation failed !!");	
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");  
			 details.addProperty("stscode", "HP06");  
			 details.addProperty("message", e.getLocalizedMessage());  
		 }
		 
		 return details;
	}
	
	public JsonObject Identifier_Excel_Data_Upload(String request)
	{	
		 String SUBORGCODE = "EXIM";
		 String SYSCODE    = "HP";
		 
		 JsonObject details = new JsonObject();
		
		 try
		 {
			 Common_Utils utils = new Common_Utils();
			 
			 JsonObject Request = utils.StringToJsonObject(request);
			 
			 String PAYTYPE = Request.get("PAYTYPE").getAsString();
			 
			 String REQUESTDATE  = utils.getCurrentDate("dd-MMM-yyyy");
			 
			 String REQUESTID  = System.currentTimeMillis() + "";
			
			 JsonArray info = Request.get("Info").getAsJsonArray();
			 
			 List<Object[]> batch = new ArrayList<Object[]>();
			 
			 for(int i=0; i<info.size(); i++)
			 {
				 JsonObject Information = info.get(i).getAsJsonObject();
				 
				 Identifiers001 Info = new Gson().fromJson(Information, Identifiers001.class);
				
				 Info.setSTATUS("Q");
				 
				 Object[] values = new Object[] { SUBORGCODE, SYSCODE, PAYTYPE, Info.getCHCODE(), REQUESTID, REQUESTDATE,  i+1,
						 Info.getFSP_SRCID(), Info.getFSP_DESID(), Info.getIDENTIFIERNO(), Info.getSUBIDENT(), Info.getIDENTIFIERTYPE(), Info.getFSPID(), Info.getIDENTIFIERNAME(), Info.getACCAT(), Info.getACTYPE(), Info.getCURRENCY(),
						 Info.getACCOUNTNO(), Info.getCUSTNO(), Info.getMSISDN(), Info.getEMAIL(), Info.getIDTYPE1(), Info.getIDTYPEVALUE1(), Info.getSTATUS(), Info.getAPPROVED() };
		            
		         batch.add(values);
			 }
			
			 String sql = "Insert into identifiers001(SUBORGCODE,SYSCODE,PAYTYPE,CHCODE,REQUESTID,REQUESTDATE,REQSL,FSP_SRCID,FSP_DESID,IDENTIFIERNO,SUBIDENT,IDENTIFIERTYPE,FSPID,IDENTIFIERNAME,ACCAT,ACTYPE,CURRENCY,ACCOUNTNO,CUSTNO,MSISDN,EMAIL,IDTYPE1, IDTYPEVALUE1, STATUS, APPROVED) "+
					      "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.batchUpdate(sql, batch);
			 
			 details.addProperty("result",  "success");
			 details.addProperty("stscode", "HP00");
			 details.addProperty("message", "Data Inserted Successfully !!");	
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");  
			 details.addProperty("stscode", "HP06");  
			 details.addProperty("message", e.getLocalizedMessage());  
		 }
		 
		 return details;
	}
	
	public JsonObject Queued_Identifiers()
	{
		JsonObject details = new JsonObject();
		
		try 
		{
			 String Sql = "Select * from identifiers001 where SUBORGCODE=? and SYSCODE=? and STATUS=? and APPROVED=?";
			
			 List<Identifiers001> Identifiers = Jdbctemplate.query(Sql, new Object[] { "EXIM", "HP", "A", "1" }, new Identifier_Thread().new Identifiers001_Mapper());
		
			 details.add("Identifiers", new Gson().toJsonTree(Identifiers).getAsJsonArray());
			 
			 details.addProperty("Total_Valid", Identifiers.size());
			 details.addProperty("Total_InValid", "0");
			 
			 details.addProperty("result",  Identifiers.size() != 0 ? "success" : "failed");
			 details.addProperty("stscode", Identifiers.size() != 0 ? "HP00" : "HP200");
			 details.addProperty("message", Identifiers.size() != 0 ? "Data found !!" : "Data Not Found !!");
		}
		catch(Exception e) 
		{
			 details.addProperty("result", "failed");  
			 details.addProperty("stscode", "HP06");  
			 details.addProperty("message", e.getLocalizedMessage());  
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
			 
			 JsonObject API_Response = new JsonObject();
			 
			 int Retry = 0;  boolean flag = true;
			 
			 do
			 {
				 API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 	
				 
				 if(API_Response.get("Response_Code").getAsInt() == 500)
				 {
					 flag = false; 
					 
					 Retry++;
				 }
				 else
				 {
					 flag = true;
				 }
				 
			 }while(!flag && Retry < 3);
			 
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
			 details.addProperty("Response_Code", "500");
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
	
	public boolean verify_signature(String signed, String PAYLOAD)
	{
		boolean verify = false;
		
		//String Signeddata = "";
		
		String password = "passphrase";
		String alias = "eximtzt0";

		try
		{
			char[] passord = null;
			
			//PrivateKey privateKey = null;
			PublicKey publicKey = null;
				
		    File file = new File("C:\\HDPAY\\TIPS\\EXIMTZT0Keystore.pfx");
			
		    InputStream stream = new FileInputStream(file);
		    java.security.KeyStore keyStore1 = KeyStore.getInstance("PKCS12");  
		    keyStore1.load(stream, passord);

		    PrivateKey privateKey = (PrivateKey) keyStore1.getKey(alias, password.toCharArray());
		 
		    System.out.println("Private Key " + privateKey); 
		    
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
		    
		     byte[] data = PAYLOAD.getBytes("UTF8");
		    
		     Signature signature1 = Signature.getInstance("SHA256WithRSA");
		    
		    //byte[] signatureBytes = signature1.sign();
	        
		    //signed = Base64.getDecoder().decode(signed); //.getEncoder().encodeToString(signatureBytes);
	               
		    signature1.initVerify(publicKey);
		    signature1.update(data);
		    boolean result = signature1.verify(Base64.getDecoder().decode(signed));
		    
		    //byte[] data = PAYLOAD.getBytes("UTF8");
		    
		    //byte[] data2 = signed.getBytes("UTF8");
	        
		    //Signature sig = Signature.getInstance("SHA256withRSA");
		    
		   // byte[] signatureBytes = sig.sign();
		    
		    //signed = Base64.getDecoder().decode(signatureBytes).toString();
		      
	        //sig.initVerify(publicKey);
	       // sig.update(data);

	        System.out.println("hiiii resr :::: " +result);  
		}
		catch(Exception e)
		{
			logger.debug("Exception in signature Verification ::: "+e.getLocalizedMessage());
		}
		
		return verify;
	}
	
	public boolean VerifyDigitalSignature(String Signed, String PAYLOAD)
	{
		 boolean verify = false;
		 
	     try 
	     {
	    	 /*
	    	  *  File file = new File("C:\\HDPAY\\TIPS\\EXIMTZT0Keystore.pfx");
			
		    InputStream stream = new FileInputStream(file);
		    java.security.KeyStore keyStore1 = KeyStore.getInstance("PKCS12");  
		    keyStore1.load(stream, passord);
	    	  */
	    	 
	    	//Creating KeyPair generator object
	         KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
	   	      
	         //Initializing the key pair generator
	         keyPairGen.initialize(2048);
	   	      
	         //Generate the pair of keys
	         KeyPair pair = keyPairGen.generateKeyPair();
	         
	         //Getting the privatekey from the key pair
	         PrivateKey privKey = pair.getPrivate();

	         //Creating a Signature object
	         Signature sign = Signature.getInstance("SHA256withDSA");

	         //Initializing the signature
	         sign.initSign(privKey);
	         
	         byte[] bytes = PAYLOAD.getBytes();
	         
	         //Adding data to the signature
	         sign.update(bytes);
	         
	         //Calculating the signature
	         byte[] signature = sign.sign();      
	         
	         //Initializing the signature
	         sign.initVerify(pair.getPublic());
	         sign.update(bytes);
	         
	         //Verifying the signature
	         boolean bool = sign.verify(signature);
	         
	         if(bool) 
	         {
	            System.out.println("Signature verified");   
	         } 
	         else 
	         {
	            System.out.println("Signature failed");
	         }
	     } 
	     catch (Exception e) 
	     {
	         e.printStackTrace();
	     }
	     
	     return verify;
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
	
	public JsonObject Payment_Posting_Validation2(String Req, JsonObject Headers)
	{
		 JsonObject details = new JsonObject();
		
		 boolean flag = true;
		 
		 try
		 {
			 JsonObject Request = new Common_Utils().StringToJsonObject(Req);
			 
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
			 
		/*	 if(Headers.get("signature").isJsonNull())
			 {
				 flag = false;
			 }
			 else 
			 {
				 String Actual_Signature = Headers.get("signature").getAsString();
				
				 logger.debug("Actual_Signature :::: "+Actual_Signature);
				 
				 logger.debug("Expected_Signature :::: "+get_signature(Req));
				 
				 boolean res = verify_signature(Actual_Signature, Req); 
				 
				 if(res)
				 {
					 flag = false;
				 }
				 
				 logger.debug("Signature verification :::: "+res);
			 }
			*/
			 
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
			 String sql = "Insert into tips001(SUBORGCODE, REQUESTID, REQUESTDATE, FSP_SRCID, FSP_DESID, IDENTIFIERNO, SUBIDENT, STATUS, ERRORCD, ERRDESC, IDENTIFIERTYPE, FSPID, IDENTIFIERNAME, ACCAT, ACTYPE, ACCOUNTNO, CUSTNO, MSISDN, EMAIL) " + 
			 			  "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getREQUESTID(), Info.getREQUESTDATE(), Info.getFSP_SRCID(), Info.getFSP_DESID(), Info.getIDENTIFIERNO(), Info.getSUBIDENT(), Info.getSTATUS(),
					 Info.getERRORCD(), Info.getERRDESC(), Info.getIDENTIFIERTYPE(), Info.getFSPID(), Info.getIDENTIFIERNAME(), Info.getACCAT(), Info.getACTYPE(), Info.getACCOUNTNO(), Info.getCUSTNO(), Info.getMSISDN(), Info.getEMAIL() });
			
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
			 String sql = "Insert into tips002(SUBORGCODE, FSP_SRCID, FSP_DESID, IDENTIFIERNO, IDENTIFIERTYPE, FSPID, IDENTIFIERNAME, ACCAT, ACTYPE, ACCOUNTNO, CUSTNO, MSISDN, IDTYPE1, IDTYPEVALUE1, IDTYPE2, IDTYPEVALUE2, IDTYPE3, IDTYPEVALUE3, IDTYPE4, IDTYPEVALUE4, REGDATE, REGDATETIME, STATUS, EMAIL) " + 
			 			  "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getFSP_SRCID(), Info.getFSP_DESID(), Info.getIDENTIFIERNO(), Info.getIDENTIFIERTYPE(), Info.getFSPID(), Info.getIDENTIFIERNAME(), Info.getACCAT(), Info.getACTYPE(), Info.getACCOUNTNO(), Info.getCUSTNO(), Info.getMSISDN(), 
					 	Info.getIDTYPE1(), Info.getIDTYPEVALUE1(), Info.getIDTYPE2(), Info.getIDTYPEVALUE2(), Info.getIDTYPE3(), Info.getIDTYPEVALUE3(), Info.getIDTYPE4(), Info.getIDTYPEVALUE4(), Info.getREGDATE(), Info.getREGDATETIME(), Info.getSTATUS(), Info.getEMAIL() }); 
					 
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
			 String sql = "Update tips001 set STATUS=? where SUBORGCODE=? and REQUESTID=? and REQUESTDATE=? and IDENTIFIERNO=?";
			 			 
			 Jdbctemplate.update(sql, new Object[] { Info.getSTATUS(), Info.getSUBORGCODE(), Info.getREQUESTID(), Info.getREQUESTDATE(), Info.getIDENTIFIERNO() }); 
					 
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
	
	public JsonObject Update_Fail_TIPS_001(TIPS001 Info) 
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
		 String SUBORGCODE  = "EXIM";
		 String PAYTYPE     = "TIPS";
		
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 Common_Utils util = new Common_Utils();
				
			 JsonObject Request = util.StringToJsonObject(Body);
			 
			 String payeeRef = Request.get("payeeRef").getAsString();
			 String transferState = Request.get("transferState").getAsString();
			 String switchRef = Request.get("switchRef").getAsString();
			 
			 String sql = "Update pay001 set STATUS=?, RESCODE=?, PAYEEREF=?, SWITCHREF=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=?";
			 
			 Jdbctemplate.update(sql, new Object[] { "SUCCESS" , "200", payeeRef, switchRef, SUBORGCODE, PAYTYPE, payerRef });
			 
			 sql = "Update request002 set STATUS=?, RESCODE=?, RESPDESC=?, PAYEEREF=?, SWITCHREF=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=?";
			 
			 Jdbctemplate.update(sql, new Object[] { "SUCCESS", "200", transferState , payeeRef, switchRef, SUBORGCODE, PAYTYPE, payerRef });
			 
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
		 String SUBORGCODE  = "EXIM";
		 String SYSCODE     = "HP";
		 String PAYTYPE     = "TIPS";
		
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 Common_Utils util = new Common_Utils();
				
			 JsonObject Request = util.StringToJsonObject(Body);
			 
			 JsonObject errorInformation = Request.get("errorInformation").getAsJsonObject();
			 
			 String errorCode = errorInformation.get("errorCode").getAsString();
			 
			 String errorDescription = errorInformation.has("errorDescription") ? errorInformation.get("errorDescription").getAsString() : "";
			 
			 String sql = "Select * from pay001 where SUBORGCODE=? and PAYTYPE=? and PAYERID=? order by REQDATE desc";
			 
			 List<PAY_001> info = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, PAYTYPE, payerRef }, new Pay001_Mapper());
			 
			 if(info.size() !=0)
			 {
				 sql = "Update pay001 set STATUS=?, ERRCD=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=? and PAYEEREF=? and REQSL=?";
				 
				 Jdbctemplate.update(sql, new Object[] { "FAILED" , errorCode, SUBORGCODE, PAYTYPE, payerRef, info.get(0).getPAYEEREF(), info.get(0).getREQSL() });
				 
				 sql = "Update request002 set STATUS=?, ERRCD=?, ERRDESC=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=? and PAYEEREF=? and REQSL=?";
				 
				 Jdbctemplate.update(sql, new Object[] { "FAILED", errorCode, errorDescription , SUBORGCODE, PAYTYPE, payerRef, info.get(0).getPAYEEREF(), info.get(0).getREQSL() });
				 
				 Job_005 Job = new Job_005(SUBORGCODE, SYSCODE, info.get(0).getCHCODE(), info.get(0).getPAYTYPE(), info.get(0).getREQDATE(), info.get(0).getREQREFNO(), info.get(0).getREQSL(), "Q", "REVERSAL", "", "", "");   
				 
				 if(info.get(0).getSTATUS().equalsIgnoreCase("SUCCESS"))
				 {
					 JsonObject Reversal_details = Internal_Reversal_Executer(Job);
					  
					 logger.debug("<<<<<<< Reversal_details >>>>>> "+Reversal_details);
				 } 
			 }

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
			 String reversalReason = Request.get("reversalReason").getAsString();
			 
			 //String DATE = util.getCurrentDate("dd-MMM-yyyy");
			 
			 String sql = "Select * from pay001 where SUBORGCODE=? and PAYTYPE=? and PAYERID=? and payeeref=? and switchref=? and TRANAMT=? and TRANCURR=?";
			 
			 List<PAY_001> info = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, PAYTYPE, payerRef, payeeRef, switchRef, amount, currency }, new Pay001_Mapper());
			 
			 if(info.size() !=0)
			 {
				 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, PAYTYPE, FormatUtils.dynaSQLDate(info.get(0).getREQDATE(),"YYYY-MM-DD"), info.get(0).getTRANREFNO(), info.get(0).getREQSL(), "Q", "REVERSAL", reversalReason, payerReversalRef, MSGTYPE); 
				 
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
		// String SYSCODE     = "HP";
		 String PAYTYPE     = "TIPS" ;
		// String MSGTYPE 	= "2110";  	 
		 
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 Common_Utils util = new Common_Utils();
				
			 JsonObject Request = util.StringToJsonObject(Body);
			 
			 String sql = "Select * from pay001 where SUBORGCODE=? and PAYTYPE=? and PAYERID=? order by PAYDATE desc";
			 
			 List<PAY_001> info = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, PAYTYPE, payerRef }, new Pay001_Mapper());
			 
			 if(info.size() !=0)
			 {
				 if(Request.has("reversalState"))
				 {
					 String reversalState = Request.get("reversalState").getAsString();
					 
					 String reversalReason = Request.get("reversalReason").getAsString();
					 
					 String HOLD_DATE = util.getCurrentDate("dd-MMM-yyyy");
					 
					 sql = "Update pay002 set HOLD_REASON=?, HOLD_STATE=?, HOLD_DATE=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=? and TRANREFNO=? and REQSL=?";
					 
					 Jdbctemplate.update(sql, new Object[] { reversalReason , reversalState, HOLD_DATE, SUBORGCODE, PAYTYPE, payerRef, info.get(0).getTRANREFNO(), info.get(0).getREQSL() });
					 
					 // /**** Intiate Reversal Process ****/
					 
					 //Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, info.get(0).getCHCODE(), PAYTYPE, HOLD_DATE, info.get(0).getTRANREFNO(), info.get(0).getREQSL(), "Q", "REVERSAL", reversalReason, "", MSGTYPE); 
					 
					 //Req_Modal.Insert_Job_005(Info3); 
				 }
				 else
				 {
					 JsonObject errorInformation = Request.get("errorInformation").getAsJsonObject();
					 
					 String errorDescription = "Payment hold failed due to "+errorInformation.get("errorDescription").getAsString();
					 
					 sql = "Update pay002 set HOLD_STATE=?, ERRCD=?, ERRDES=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=? and TRANREFNO=? and REQSL=?"; 
					 
					 Jdbctemplate.update(sql, new Object[] { "FAILED", "HP301", errorDescription, SUBORGCODE, PAYTYPE, payerRef, info.get(0).getTRANREFNO(), info.get(0).getREQSL() });
				 }
				 
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

	public JsonObject Transfer_Reversal_Update(String Body, String payerRef, JsonObject Headers, HttpServletRequest req)
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
			
			 String switchRef = Request.get("switchRef").getAsString();
			 String payeeRef = Request.get("payeeRef").getAsString();
			 String switchReversalRef = Request.get("switchReversalRef").getAsString();
			 String payerReversalRef = Request.get("payerReversalRef").getAsString();
			 String payeeReversalRef = Request.get("payeeReversalRef").getAsString();
			 String reversalState = Request.get("reversalState").getAsString();
			
			 String sql = "Select * from pay001 where SUBORGCODE=? and PAYTYPE=? and PAYERID=? and PAYEEREF=? and SWITCHREF=?";
			 
			 List<PAY_001> info = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, PAYTYPE, payerRef, payeeRef, switchRef }, new Pay001_Mapper());
			 
			 if(info.size() !=0)
			 {
				 sql = "Update pay002 set PAYEE_REVREF=?, REV_SWITCHREF=?, REV_STATE=? where SUBORGCODE=? and PAYTYPE=? and PAYERID=? and PAYEEID=? and PAYER_REVREF=? and TRANREFNO=? and REQSL=?";
				 
				 Jdbctemplate.update(sql, new Object[] { payeeReversalRef, switchReversalRef, reversalState, SUBORGCODE, PAYTYPE, payerRef, payeeRef, payerReversalRef, info.get(0).getTRANREFNO(), info.get(0).getREQSL() });
				 
				 details.addProperty("payeeReversalRef", payeeReversalRef);
				 details.addProperty("status", "RECEIVED");
				 details.addProperty("datetime", util.getCurrentDateTime());
				 
				 /**** Intiate Reversal Process ****/
				 
				 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, info.get(0).getCHCODE(), PAYTYPE, util.getCurrentDate("dd-MMM-yyyy"), info.get(0).getTRANREFNO(), info.get(0).getREQSL(), "Q", "REVERSAL", "", "", MSGTYPE); 
				 
				 Req_Modal.Insert_Job_005(Info3);
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
	
	public JsonObject Retrieve_Transaction_by_PayerRef(String payerRef, JsonObject Headers)
	{
		 JsonObject details = new JsonObject();
		 
		 JsonObject Information = new JsonObject();
		
		 try
		 {   
			 String sql = "Select * from request002 where SUBORGCODE=? and SYSCODE=? and PAYTYPE=? and TRANREFNO=?";
			 
			 List<Request_002> Info = Jdbctemplate.query(sql, new Object[] { "EXIM" , "HP", "TIPS", payerRef }, new Request_002_Mapper());
			 
			 if(Info.size() !=0)
			 {
				 JsonObject payer = new JsonObject();
				 JsonObject payer_identity = new JsonObject();
				 JsonObject payee = new JsonObject();
				 JsonObject payee_identity = new JsonObject();
				 JsonObject amount = new JsonObject();
				 JsonObject endUserFee = new JsonObject();
				 JsonObject transactionType = new JsonObject();
				 
				 Information.addProperty("payerRef", Info.get(0).getPAYERID());
				 
				 payer.addProperty("identifierType", Info.get(0).getS_IDENTIFIERTYPE());
				 payer.addProperty("identifier", Info.get(0).getS_IDENTIFYVALUE());
				 payer.addProperty("fspId", Info.get(0).getS_FSPID());
				 payer.addProperty("fullName", Info.get(0).getS_ACNAME());
				 payer.addProperty("accountCategory", Info.get(0).getS_ACCATEGORY());
				 payer.addProperty("accountType", Info.get(0).getS_ACTYPE());		 
				 payer_identity.addProperty("type", Info.get(0).getS_IDENTIFYTYPE());
				 payer_identity.addProperty("value", Info.get(0).getS_IDENTIFYVALUE());
				 
				 payer.add("identity", payer_identity);
				 
				 payee.addProperty("identifierType", Info.get(0).getD_IDENTIFIERTYPE());
				 payee.addProperty("identifier", Info.get(0).getD_IDENTIFYVALUE());
				 payee.addProperty("fspId", Info.get(0).getD_FSPID());
				 payee.addProperty("fullName", Info.get(0).getD_ACNAME());
				 payee.addProperty("accountCategory", Info.get(0).getD_ACCATEGORY());
				 payee.addProperty("accountType", Info.get(0).getD_ACTYPE());		 
				 payee_identity.addProperty("type", Info.get(0).getD_IDENTIFYTYPE());
				 payee_identity.addProperty("value", Info.get(0).getD_IDENTIFYVALUE());
				 
				 payee.add("identity", payee_identity);
				 
				 amount.addProperty("amount", Info.get(0).getTRANAMT());
				 amount.addProperty("currency", Info.get(0).getTRANCURR());
				 
				 endUserFee.addProperty("amount", Info.get(0).getFEEAMT1());
				 endUserFee.addProperty("currency", Info.get(0).getFEECURR1());
				 
				 transactionType.addProperty("scenario", Info.get(0).getTRANTYPE());
				 transactionType.addProperty("initiator", Info.get(0).getSENDER_INFO());
				 transactionType.addProperty("initiatorType", Info.get(0).getINITATEDBY());
				 
				 Information.addProperty("description", Info.get(0).getREMARKS());
				 
				 String RESCODE = Info.get(0).getRESCODE();			 
				 String RESPDESC = Info.get(0).getRESPDESC();				 
				 String STATUS = Info.get(0).getSTATUS();				 
				 String ERRDESC = Info.get(0).getERRDESC();
				 
				 if(RESCODE.equals("202"))
				 {
					 Information.addProperty("transferState", "RECEIVED");
				 }
				 else if(RESCODE.equals("200") && RESPDESC.equalsIgnoreCase("COMMITTED"))
				 {
					 Information.addProperty("transferState", "COMMITTED");
				 }
				 else if(STATUS.equalsIgnoreCase("Failed"))
				 {
					 if(ERRDESC.contains("CBS Posting is Success"))
					 {
						 Information.addProperty("transferState", "CONFIRMED");
					 }
					 else
					 {
						 Information.addProperty("transferState", "ABORTED");
					 }
				 }
				
				 Information.addProperty("payeeRef", Info.get(0).getPAYEEREF());
				 Information.addProperty("transferDate", Info.get(0).getPAYDATE());
				 Information.addProperty("completedDate", Info.get(0).getPAYDATE());
				 
				 Information.add("payer", payer);
				 Information.add("payee", payee);
				 Information.add("amount", amount);
				 Information.add("endUserFee", endUserFee);
				 Information.add("transactionType", transactionType);
				 
				 details.add("Information", Information);
				 
				 /*** Initiate auto callback to TIPS ***/
				 
				 String Serial = Req_Modal.Generate_CallabckSerial().get("Serial").getAsString();
			     
				 Timestamp REQTIME  = new java.sql.Timestamp(new java.util.Date().getTime());
				     
				 Callback001 info = new Callback001(Info.get(0).getSUBORGCODE(), Info.get(0).getSYSCODE(), Info.get(0).getCHCODE(), Info.get(0).getPAYTYPE(), Info.get(0).getFLOW(), FormatUtils.dynaSQLDate(Info.get(0).getREQDATE() ,"YYYY-MM-DD"), Info.get(0).getREQREFNO(), Info.get(0).getTRANREFNO(), FormatUtils.dynaSQLDate(Info.get(0).getPAYDATE() ,"YYYY-MM-DD"), Info.get(0).getTRANTYPE(), Info.get(0).getREQSL(), Serial, REQTIME, "Q");
				
				 Req_Modal.Insert_Callback_001(info);
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
			 
			 errorInformation.addProperty("errorCode", 3200);
			 errorInformation.addProperty("errorDescription", "Timeout error");	
			 
			 details.add("errorInformation", errorInformation);	
			 
			 logger.debug("Exception in Retrieve_Transaction_by_PayerRef :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	/*public JsonObject Inward_Request_Storage(JsonObject Headers, String Body_MSG, HttpServletRequest request)
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String SESSIONID   = "";  
			 String LONGITUDE   = "";  
			 String LATITUDE    = "";  
			 String IPADDRESS   = "";  
			 String DEVICEID    = "";  
			 String LOCATION    = "";  
			 String VERSION    = "";
			 
			 Timestamp REQTIME = new java.sql.Timestamp(new java.util.Date().getTime());
			 
			 Request_001 Info = new Request_001(SUBORGCODE, CHCODE, PAYTYPE, MSGTYPE, FLOW, DATE, REQTIME, ReqRefID, MSGURL, IP, PORT, HEAD_MSG, BODY_MSG, INITATEDBY, Checksum);
			 
			 Req_Modal.Insert_Request_001(Info); 
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Reference Id generated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Retrieve_Transaction_by_PayerRef :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	*/
	
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
	
	public JsonObject Retrieve_All_TIPS_Identfifer()
	{
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 String sql = "Select * from tips002";
			 
			 List<TIPS002> Info = Jdbctemplate.query(sql, new Tips002_Mapper());
		
			 details.add("Informations", new Gson().toJsonTree(Info));

			 details.addProperty("result", Info.size() !=0 ? "success" : "failed");
			 details.addProperty("stscode", Info.size() !=0 ? "200" : "400");
			 details.addProperty("message", Info.size() !=0 ? "ac Information found" : "ac Information not found");
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
	
	public JsonObject Get_CBS_ERRINFO(String errormsg)
	{
		  JsonObject details = new JsonObject();
		
		  String ERRCD = ""; String ERRDESC = "";
		
		  if(errormsg.equalsIgnoreCase("0014"))  /** Invalid Account **/
		  {
			  ERRCD = "HP14";				  
			  ERRDESC = "Invalid Account";
		  }
		  else if(errormsg.equalsIgnoreCase("0096")) /** Transaction Declined **/
		  {
			  ERRCD = "HP407";
			  ERRDESC = "Transaction Declined";
		  }
		  else if(errormsg.equalsIgnoreCase("0051")) /** Insufficient Fund **/
		  {
			  ERRCD = "HP51";
			  ERRDESC = "Insufficient Fund";
		  }
		  else if(errormsg.equalsIgnoreCase("0093")) /** Transaction not allowed, Rule Violated **/
		  {
			  ERRCD = "HP406";
			  ERRDESC = "Transaction not allowed, Rule Violated";
		  }
		  else if(errormsg.equalsIgnoreCase("0061")) /** Withdrawal Limit Exceeds **/
		  {
			  ERRCD = "HP61";
			  ERRDESC = "Transaction Limit Exceeds";
		  }
		  else if(errormsg.equalsIgnoreCase("0094")) /** Duplicate Transaction **/
		  {
			  ERRCD = "HP94";
			  ERRDESC = "Duplicate Request within Time Limit";
		  }
		  else if(errormsg.equalsIgnoreCase("0351")) /** Account Closed **/
		  {
			  ERRCD = "HP401";
			  ERRDESC = "Closed Account";
		  }
		  else if(errormsg.equalsIgnoreCase("0451")) /** Inoperative Closed **/
		  {
			  ERRCD = "HP402";
			  ERRDESC = "Inoperative Closed Account";
		  }
		  else if(errormsg.equalsIgnoreCase("0551")) /** Dormant Closed **/
		  {
			  ERRCD = "HP403";
			  ERRDESC = "Dormant Closed Account";
		  }
		  else if(errormsg.equalsIgnoreCase("0151")) /** Debit Freezed **/
		  {
			  ERRCD = "HP404";
			  ERRDESC = "Debit Freezed Account";
		  }
		  else if(errormsg.equalsIgnoreCase("0251")) /** Credit Freezed **/
		  {
			  ERRCD = "HP405";
			  ERRDESC = "Credit Freezed Account";
		  }
		  else
		  {
			  ERRCD = "HP110";
			  ERRDESC = "Issue In CBS";
		  }
		  
		  details.addProperty("ERRCD", ERRCD);
		  details.addProperty("ERRDESC", ERRDESC);
		  
		  return details;
	}
	
	public String Get_TIPS_ERRORCODE(String ERRCD)
	{
		  String reasonCode = "86";
		 
		  if(ERRCD.equalsIgnoreCase("HP14"))  /** Invalid Account **/
		  {
			  reasonCode = "78";
		  }
		  else if(ERRCD.equalsIgnoreCase("HP407")) /** Transaction Declined **/
		  {
			  reasonCode = "86";
		  }
		  else if(ERRCD.equalsIgnoreCase("HP51")) /** Insufficient Fund **/
		  {
			  reasonCode = "85";
		  }
		  else if(ERRCD.equalsIgnoreCase("HP406")) /** Transaction not allowed, Rule Violated **/
		  {
			  reasonCode = "83";
		  }
		  else if(ERRCD.equalsIgnoreCase("HP61")) /** Withdrawal Limit Exceeds **/
		  {
			  reasonCode = "77";
		  }
		  else if(ERRCD.equalsIgnoreCase("HP94")) /** Duplicate Transaction **/
		  {
			  reasonCode = "84";
		  }
		  else if(ERRCD.equalsIgnoreCase("HP401")) /** Account Closed **/
		  {
			  reasonCode = "79";
		  }
		  else if(ERRCD.equalsIgnoreCase("HP402")) /** Inoperative Closed **/
		  {
			  reasonCode = "80";
		  }
		  else if(ERRCD.equalsIgnoreCase("HP403")) /** Dormant Closed **/
		  {
			  reasonCode = "80";
		  }
		  else if(ERRCD.equalsIgnoreCase("HP404")) /** Debit Freezed **/
		  {
			  reasonCode = "81";
		  }
		  else if(ERRCD.equalsIgnoreCase("HP405")) /** Credit Freezed **/
		  {
			  reasonCode = "81";
		  }
		 
		 return reasonCode;
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
			
			Info.setSERIAL(rowNum+1);
			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));         
			Info.setFSP_SRCID(util.ReplaceNull(rs.getString("FSP_SRCID")));             
			Info.setFSP_DESID(util.ReplaceNull(rs.getString("FSP_DESID")));            
			Info.setIDENTIFIERNO(util.ReplaceNull(rs.getString("IDENTIFIERNO")));               
			Info.setIDENTIFIERTYPE(util.ReplaceNull(rs.getString("IDENTIFIERTYPE")));        
			Info.setFSPID(util.ReplaceNull(rs.getString("FSPID")));         
			Info.setIDENTIFIERNAME(util.ReplaceNull(rs.getString("IDENTIFIERNAME")));        
			Info.setACCAT(util.ReplaceNull(rs.getString("ACCAT")));       
			Info.setACTYPE(util.ReplaceNull(rs.getString("ACTYPE")));       
			Info.setACCOUNTNO(util.ReplaceNull(rs.getString("ACCOUNTNO")));        
			Info.setCUSTNO(util.ReplaceNull(rs.getString("CUSTNO"))); 
			Info.setMSISDN(util.ReplaceNull(rs.getString("MSISDN"))); 
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
    
    public class Request_002_Mapper implements RowMapper<Request_002> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Request_002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Request_002 Info = new Request_002(); 
			
			Info.setROWNUM(rowNum+1);
			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));         
			Info.setSYSCODE(util.ReplaceNull(rs.getString("SYSCODE")));         
			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));             
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));            
			Info.setFLOW(util.ReplaceNull(rs.getString("FLOW")));       
			Info.setREQDATE(util.ReplaceNull(rs.getString("REQDATE")));  	
			Info.setREQREFNO(util.ReplaceNull(rs.getString("REQREFNO"))); 
			Info.setTRANREFNO(util.ReplaceNull(rs.getString("TRANREFNO"))); 
			Info.setREQSL(util.ReplaceNull(rs.getString("REQSL"))); 
			Info.setREQTIME2(util.ReplaceNull(rs.getString("REQTIME")));         
			Info.setPAYDATE(util.ReplaceNull(rs.getString("PAYDATE")));  
			Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS"))); 
			Info.setERRCD(util.ReplaceNull(rs.getString("ERRCD")));   
			Info.setERRDESC(util.ReplaceNull(rs.getString("ERRDESC")));   
			Info.setRESCODE(util.ReplaceNull(rs.getString("RESCODE"))); 
			Info.setRESPDESC(util.ReplaceNull(rs.getString("RESPDESC")));
			Info.setTRANAMT(util.ReplaceNull(rs.getString("TRANAMT"))); 
			Info.setTRANCURR(util.ReplaceNull(rs.getString("TRANCURR"))); 	
			Info.setREQTYPE(util.ReplaceNull(rs.getString("REQTYPE")));
			Info.setSERPORCD(util.ReplaceNull(rs.getString("SERPORCD"))); 	  
			Info.setDEBITAMT(util.ReplaceNull(rs.getString("DEBITAMT")));        
			Info.setDEBITCURR(util.ReplaceNull(rs.getString("DEBITCURR"))); 
			Info.setCREDITAMT(util.ReplaceNull(rs.getString("CREDITAMT")));       
			Info.setCREDITCURR(util.ReplaceNull(rs.getString("CREDITCURR")));     
			Info.setS_ACCOUNT(util.ReplaceNull(rs.getString("S_ACCOUNT")));       
			Info.setD_ACCOUNT(util.ReplaceNull(rs.getString("D_ACCOUNT")));       
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
			Info.setPAYEEREF(util.ReplaceNull(rs.getString("PAYEEREF")));
			Info.setLONGITUDE(util.ReplaceNull(rs.getString("LONGITUDE")));
			Info.setLATITUDE(util.ReplaceNull(rs.getString("LATITUDE")));
			Info.setIPADDRESS(util.ReplaceNull(rs.getString("IPADDRESS")));
			Info.setDEVICEID(util.ReplaceNull(rs.getString("DEVICEID")));
			Info.setLOCATION(util.ReplaceNull(rs.getString("LOCATION")));
			Info.setPURPOSECD(util.ReplaceNull(rs.getString("PURPOSECD")));
			Info.setPURSPOSEDESC(util.ReplaceNull(rs.getString("PURSPOSEDESC")));
			Info.setOTPPASSED(util.ReplaceNull(rs.getString("OTPPASSED")));
			Info.setUSERAGENT(util.ReplaceNull(rs.getString("USERAGENT")));
			Info.setSESSIONID(util.ReplaceNull(rs.getString("SESSIONID")));
			Info.setPROCTIME(util.ReplaceNull(rs.getString("PROCTIME")));
			Info.setINITATEDBY(util.ReplaceNull(rs.getString("INITATEDBY")));
			Info.setFRAUDCHK(util.ReplaceNull(rs.getString("FRAUDCHK")));
			Info.setPSPCODE(util.ReplaceNull(rs.getString("PSPCODE")));
			Info.setSPCODE(util.ReplaceNull(rs.getString("SPCODE")));
			Info.setSUBSPCODE(util.ReplaceNull(rs.getString("SUBSPCODE")));
			Info.setRECEIPTNO(util.ReplaceNull(rs.getString("RECEIPTNO")));
			Info.setAMOUNTTYPE(util.ReplaceNull(rs.getString("AMOUNTTYPE")));
			Info.setCALLBACKURL(util.ReplaceNull(rs.getString("CALLBACKURL")));
			Info.setPAYERID(util.ReplaceNull(rs.getString("PAYERID")));
			Info.setTRANTYPE(util.ReplaceNull(rs.getString("TRANTYPE")));
			Info.setCUSTMSISDN(util.ReplaceNull(rs.getString("CUSTMSISDN")));
			Info.setVERSION(util.ReplaceNull(rs.getString("VERSION")));
			Info.setREMARKS(util.ReplaceNull(rs.getString("REMARKS")));
			Info.setSWITCHREF(util.ReplaceNull(rs.getString("SWITCHREF")));
			Info.setS_SENDERID(util.ReplaceNull(rs.getString("S_SENDERID")));
					
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
