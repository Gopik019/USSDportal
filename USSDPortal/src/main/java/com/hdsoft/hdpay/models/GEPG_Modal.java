package com.hdsoft.hdpay.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import com.hdsoft.common.base64simulator;
import com.hdsoft.hdpay.Repositories.Account_Information;
import com.hdsoft.hdpay.Repositories.Job_005;
import com.hdsoft.hdpay.Repositories.PAY_001;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Request_002;
import com.hdsoft.hdpay.Repositories.Response_001;
import com.hdsoft.hdpay.Repositories.Transactions;
import com.hdsoft.utils.FormatUtils;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class GEPG_Modal implements Database
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	@Autowired
	public Request_Modal Req_Modal;
	
	@Autowired
	public Response_Modal Res_Modal;
	
	@Autowired
	public Webservice_Modal Ws;
	
	@Autowired
	public Webservice_call_Modal Wsc;
	
	@Autowired
	public Simulator_Modal Simulator;
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Get_GEPG_Bill_Details(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE  = "EXIM";
		String CHCODE  = Headers.get("ChannelID").getAsString();  
		String SERVICECD = "001"; 
		String SYSCODE = "HP"; 
		
		try
		{
			 JsonObject Request = new Common_Utils().StringToJsonObject(request);
			 
			 JsonObject Validation_details = new JsonObject();
				
			 Validation_details = Bill_Validation(Request, Headers);
			
			 if(Validation_details.get("result").getAsString().equals("failed"))
			 {
				 return Validation_details;
			 }
			
			 JsonObject GETBILL = Request.get("getbill").getAsJsonObject();			
			 
			 String PAYTYPE = GETBILL.get("paytype").getAsString();   /**** Taken as Requested Date ****/
			 
			 JsonObject PRIMARYARG = GETBILL.get("primaryarg").getAsJsonObject();	
 			 
			 String DATE = GETBILL.get("date").getAsString();
			 String ReqRefID = GETBILL.get("reqrefid").getAsString();
			 String INITATEDBY = GETBILL.get("initiatedby").getAsString();
			 String BILLNO = PRIMARYARG.get("billno").getAsString();
			 
			 String MSGTYPE 	=  SERVICECD;  									/*** service code hardcoded value **/
			 String FLOW    	=  "O"; 									  									
			 String IP      	=  Headers.get("IPAddress").getAsString(); 
			 String PORT    	=  req.getRemotePort()+"";  									
			 String HEAD_MSG    =  Headers.toString();  				
			
			 String sql = "Select count(*) from request001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and MSGTYPE=? and FLOW=? and REQDATE=? and UNIREFNO=?"; 
			 
			 int count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, PAYTYPE, MSGTYPE, FLOW , DATE, ReqRefID }, Integer.class);
			 
			 if(count !=0)
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Duplicate ReqRefID !!");
				 
				 return details;
			 }
			 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, PAYTYPE, SERVICECD);
			 
			 if(webservice_details.get("Result").getAsString().equals("Failed"))
			 {
				 logger.debug("Error in Get_Webserice_Info :::: "+webservice_details);
				 	  
				 details.addProperty("result", "success");
				 details.addProperty("stscode", "HP200");
				 details.addProperty("message", "Webservive details not found !!");
				 
				 return details;
			 }
			 
			 String SIGNPAYLOAD = webservice_details.get("SIGNPAYLOAD").getAsString();
		     
			 SIGNPAYLOAD = SIGNPAYLOAD.replace("~BillReqId~", ReqRefID);	
			 SIGNPAYLOAD = SIGNPAYLOAD.replace("~BillCtrNum~", BILLNO);	
		     
			 String Signature = getsignature(SIGNPAYLOAD);
			 	
	         String requesturl = webservice_details.get("URI").getAsString(); 
			
	         String urlparam = webservice_details.get("PAYLOAD").getAsString();
	         
	         urlparam = urlparam.replace("<gepgBillQryReq>~gepgBillQryReq~</gepgBillQryReq>", SIGNPAYLOAD);	
	         urlparam = urlparam.replace("~gepgSignature~", Signature);
	         
	         webservice_details.addProperty("PAYLOAD", urlparam);
	       
		     String O_FLOW  =  "O"; 
		     String O_ReqRefID  = ReqRefID;
		     String O_MSGURL = requesturl; 
		     String O_IP = "";
		     String O_PORT = "";
		     String O_HEAD_MSG = Headers.toString();
		     String O_BODY_MSG = urlparam;
		     String O_INITATEDBY = INITATEDBY;
		     String O_Checksum = "";
		     
		     Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, PAYTYPE, SERVICECD, O_FLOW, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
	
			 JsonObject API_Response = new JsonObject();
			 
			 String VERSION = Headers.get("VERSION").getAsString(); 
			 
			 if(VERSION.equalsIgnoreCase("demo"))  /*** simulator Response Code ***/
			 {
				 String Range = BILLNO.substring(BILLNO.length() - 4);
				 
				 API_Response = Simulator.Get_Simulator_Response_with_range("", "", SUBORGCODE, SYSCODE, PAYTYPE, SERVICECD, FLOW, Range);
			 }
			 else
			 {
				 API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
			 }
			 
			 logger.debug("Bill API_Response :::: "+API_Response);
			 
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, PAYTYPE, MSGTYPE, "O", ReqRefID , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), "HP", Signature);
				
		     Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	  
		  
	         String api_Response = API_Response.toString();
	         
	         if(api_Response.contains("7101") && api_Response.contains("TxfrAcc")) /** Successful response ***/
	         {
	        	 String response = API_Response.get("Response").getAsString();
	        	 
	        	 JsonObject final_Response = Parse_XML_Bill_Response(response, GETBILL, BILLNO, Signature, ReqRefID);
	        	
			     details = final_Response;
	         }
	         else
	         {
			     if(api_Response.contains("7101")) /** Successful response ***/
		         {
			    	 String response = API_Response.get("Response").getAsString();
			    	 
			    	 JsonObject final_Response = Parse_XML_Bill_Response_V2(response, GETBILL, BILLNO, Signature, ReqRefID);

				     details = final_Response;
		         }
			     else
			     {
			    	 details.addProperty("result",  "failed"); 
					 details.addProperty("stscode", "HP109"); 
					 details.addProperty("message", "Biller Gateway Currently Not Available !!"); 
			     }
	         }
	         
	       /*  else
	         {
	        	 SERVICECD = "101"; 
	        	 
	        	 JsonObject webservice_details2 = Ws.Get_Webserice_Info(SYSCODE, PAYTYPE, SERVICECD);
	        	 
	        	 if(webservice_details2.get("Result").getAsString().equals("Failed"))
				 {
					 logger.debug("Error in Get_Webserice_Info :::: "+webservice_details2);
					 	  
					 details.addProperty("result", "success");
					 details.addProperty("stscode", "HP200");
					 details.addProperty("message", "Webservive details not found !!");
					 
					 return details;
				 }
	        	 
	        	 requesturl = webservice_details2.get("URI").getAsString(); 
	 			
		         webservice_details.addProperty("URI", requesturl);
		         
	        	 Request_001 = new Request_001(SUBORGCODE, CHCODE, PAYTYPE, SERVICECD, O_FLOW, O_ReqRefID, requesturl, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
				 
				 Req_Modal.Insert_Request_001(Request_001);     
		
		         API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
		        
		         Response = new Response_001(SUBORGCODE, CHCODE, PAYTYPE, MSGTYPE, "O", ReqRefID , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), "GEPG", Signature);
					
			     Res_Modal.Insert_Response_001(Response);	 	  
			    
		         logger.debug("Bill API_Response ENH-3 :::: "+API_Response);
		         
		         int Response_Code = API_Response.get("Response_Code").getAsInt();
		         
		         if(Response_Code == 500)
		         {
		        	 details.addProperty("result",  "failed"); 
					 details.addProperty("stscode", "HP109"); 
					 details.addProperty("message", "Biller Gateway Currently Not Available !!"); 
		         }
		         else
		         {
		        	 String response = API_Response.get("Response").getAsString();
		        	 
		        	 JsonObject final_Response = Parse_XML_Bill_Response_V2(response, GETBILL, BILLNO);
		        	
				     details = final_Response;
		         } 
	         }*/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed"); 
			 details.addProperty("stscode", "HP06"); 
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in GEPG_Bill_Details :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Handle_GEPG_Payment_Request(JsonObject Request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();
		
		String SUBORGCODE  = "EXIM";
		String SYSCODE     = "HP";
		String CHCODE 	   =  Headers.get("ChannelID").getAsString();  					
		String MSGTYPE 	   =  "002";  					
		String FLOW    	   =  "O"; 		
		
		String CallBack_CHCODE = "GEPG";
		String CallBack_SERVICECD = "003";
		String CallBack_Flow = "I";
		
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
			 JsonObject billpay = transfers.get("billpay").getAsJsonObject();
			 
			 String paytype = transfers.get("paytype").getAsString();
			 String date = transfers.get("date").getAsString();  
			 String reqrefid = transfers.get("reqrefid").getAsString();
			 String paydate = transfers.get("paydate").getAsString();
			 String initatedby = transfers.get("initiatedby").getAsString();
			 String trantype = transfers.get("trantype").getAsString();
			 String checksum = transfers.has("checksum") ? transfers.get("checksum").getAsString() : "";  
			
			 String tranamt = trandetail.get("tranamt").getAsString();
			 String trancurr = trandetail.get("trancurr").getAsString();
			 String tranrefid = trandetail.get("tranrefid").getAsString();
			 
			 String debitamt = sourcedtl.get("debitamt").getAsString();
			 String debitcurr = sourcedtl.get("debitcurr").getAsString();
			 
			 String s_accoutno = sourcedtl.get("s_accoutno").getAsString();
			 String s_bracnch = sourcedtl.get("s_bracnch").getAsString();
			 String s_accoutname = sourcedtl.has("s_accoutname") ? sourcedtl.get("s_accoutname").getAsString() : "";
			 String s_initiatorcif = sourcedtl.has("s_initiatorcif") ? sourcedtl.get("s_initiatorcif").getAsString() : "";
			 String s_email = sourcedtl.has("s_email") ? sourcedtl.get("s_email").getAsString() : "";
			 String s_mobile = sourcedtl.has("s_mobile") ? sourcedtl.get("s_mobile").getAsString() : "";			 
			 String s_actype = sourcedtl.has("s_actype") ? sourcedtl.get("s_actype").getAsString() : "";
			
			 JsonObject fee = new JsonObject(); 
			 
			 String feeamt = ""; String feecurr = "";
			 
			 if(sourcedtl.has("fee"))
			 {
				  fee = sourcedtl.get("fee").getAsJsonObject();
				  
				  feeamt = fee.get("feeamt").getAsString();
				  feecurr = fee.get("feecurr").getAsString();
			 }
			 
			 String d_accoutno = destinationdtl.get("d_accoutno").getAsString();
			 String d_bracnch = destinationdtl.get("d_bracnch").getAsString();
			 
			 JsonObject tissdtl = new JsonObject(); 
			 
			 String receiver_info = ""; 
			 
			 if(transfers.has("tissdtl"))
			 {
				 tissdtl = transfers.get("tissdtl").getAsJsonObject();
				 
				 receiver_info = tissdtl.toString();
			 }
			 
			 String invoiceno = billpay.get("invoiceno").getAsString();
			 String billrefid = billpay.get("billrefid").getAsString();
			 String pspcode = billpay.get("pspcode").getAsString();
			 String spcode = billpay.get("spcode").getAsString();
			 String custmsisdn = billpay.get("custmsisdn").getAsString();
			 String amttype = billpay.get("amttype").getAsString();
			 String payplan = billpay.get("payplan").getAsString();
			 
			 String REQSL		=  Req_Modal.Generate_Serial().get("Serial").getAsString();							
			 String MSGURL 	 	=  "";   					
			 String IP      	=  Headers.get("IPAddress").getAsString(); 
			 String PORT    	=  "";  				
			 String HEAD_MSG    =  Headers.toString();  				
			 String BODY_MSG    =  Request.toString();   
			
			 String SESSIONID   = Headers.get("Session_ID").getAsString();    
			 String LONGITUDE   = Headers.get("Longitude").getAsString();  
			 String LATITUDE    = Headers.get("Latitude").getAsString();  
			 String IPADDRESS   = Headers.get("IPAddress").getAsString();  
			 String DEVICEID    = Headers.get("DEVICEID").getAsString();  
			 String LOCATION    = Headers.get("Locationcode").getAsString();  
			 String VERSION  = Headers.get("VERSION").getAsString(); 
			 
			 String sql = "Select count(*) from request002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and FLOW=? and REQDATE=? and (REQREFNO=? or TRANREFNO=?)"; 
			 
			 int count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, paytype, FLOW, date, reqrefid, tranrefid }, Integer.class);
			 
			 if(count !=0)
			 {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP104");
				  details.addProperty("message", "Invalid Transaction Reference Id !!");
				 
				  return details;
			 }
			 
			 String CALLBACKURL =  "";	
			 
			 JsonObject CallBack_Res = Ws.Get_CALLBACK_URL(SUBORGCODE, SYSCODE, CallBack_CHCODE, CallBack_SERVICECD, CallBack_Flow); 	/**** Getting Callback url from webservice002 ***/
			 
			 if(CallBack_Res.get("Result").getAsString().equals("Success"))
			 {
				  CALLBACKURL = CallBack_Res.get("CallBackURL").getAsString();
			 }
			 else
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP200");
				 details.addProperty("message", "calllback url details not found !!");
				 
				 return details;
			 }
			 
			 Common_Utils util = new Common_Utils();
			 
			 JsonObject Response = new JsonObject();
			 
			 logger.debug(">>>>>> validating source account with bank system <<<<<<<<"); 
			 
			 if(VERSION.equalsIgnoreCase("demo"))  /*** simulator Response Code ***/
			 {
				 Response = Simulator.Get_Simulator_Response("", "", SUBORGCODE, SYSCODE, "COMMON", "000", "I");  /*** simulator Response ***/
				  
				 String res = Response.get("Response").getAsString();
				 
				 res = res.replace("~AC_NO~", s_accoutno);
				 
				 Response = util.StringToJsonObject(res);
			 }
			 else
			 {
				 Response = Retrieve_Account_Information(s_accoutno);
			 }
			 
			 logger.debug(">>>>>> validation result <<<<<<<< "+Response); 
			  
			 if(!Response.has("Informations"))
			 {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP14");
				  details.addProperty("message", "invalid source account !!");
				   
				  return details;
			 } 
			 
			 Timestamp REQTIME = new java.sql.Timestamp(new java.util.Date().getTime());
		
			 Request_001 Info = new Request_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, FLOW, date, REQTIME, reqrefid, MSGURL, IP, PORT, HEAD_MSG, BODY_MSG, initatedby, checksum);
			 
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
			 Info2.setTRANREFNO(tranrefid);
			 Info2.setREQSL(REQSL);					 
			 Info2.setPAYDATE(paydate);
			 Info2.setTRANAMT(tranamt);
			 Info2.setTRANCURR(trancurr);	
			 Info2.setREQTYPE(payplan);		/*** payplan (prepaid or postpaid) ***/ 	
			 Info2.setSERPORCD(""); 			
			 Info2.setDEBITAMT(debitamt); 		
			 Info2.setDEBITCURR(debitcurr); 		
			 Info2.setCREDITAMT(tranamt);	
			 Info2.setCREDITCURR(trancurr);
			 Info2.setS_ACCOUNT(s_accoutno); 			
			 Info2.setD_ACCOUNT(d_accoutno);
			 Info2.setS_BANKBIC(""); 			
			 Info2.setD_BANKBIC(""); 		
			 Info2.setS_ACNAME(s_accoutname); 	
			 Info2.setD_ACNAME("");			
			 Info2.setINITIATOR_CIF(s_initiatorcif);
			 Info2.setS_CLIENTNO("");				
			 Info2.setD_CLIENTNO("");				
			 Info2.setFEEAMT1(feeamt);					
			 Info2.setFEECURR1(feecurr);				
			 Info2.setFEEAMT2("");					
			 Info2.setFEECURR2("");				
			 Info2.setFEEAMT3("");				
			 Info2.setFEECURR3("");					
			 Info2.setFEEAMT4("");					
			 Info2.setFEECURR4("");				
			 Info2.setINVOICENO(invoiceno);	 				 
			 Info2.setS_IBAN("");	 		
			 Info2.setD_IBAN("");	 		
			 Info2.setS_BRNCODE(s_bracnch);		
			 Info2.setD_BRNCODE(d_bracnch);	 		
			 Info2.setS_ACTYPE(s_actype);	 		
			 Info2.setD_ACTYPE("");	 		
			 Info2.setS_ADDRESS("");	 		
			 Info2.setD_ADDRESS("");	 		
			 Info2.setS_EMAILID(s_email);		
			 Info2.setD_EMAILID("");			
			 Info2.setS_MOBILE(s_mobile);		
			 Info2.setD_MOBILE("");				
			 Info2.setS_TELEPHONE("");
			 Info2.setD_TELEPHONE("");
			 Info2.setSENDER_INFO("");
			 Info2.setRECEIVER_INFO(receiver_info); /** tiss file details ***/
			 Info2.setPAYEEREF("");					 
			 Info2.setLONGITUDE(LONGITUDE);  
			 Info2.setLATITUDE(LATITUDE);
			 Info2.setIPADDRESS(IPADDRESS);
			 Info2.setDEVICEID(DEVICEID);
			 Info2.setLOCATION(LOCATION);				 
			 Info2.setPURPOSECD("");
			 Info2.setPURSPOSEDESC("");
			 Info2.setOTPPASSED("");
			 Info2.setUSERAGENT("");
			 Info2.setSESSIONID(SESSIONID);
			 Info2.setPROCTIME("");				 
			 Info2.setINITATEDBY(initatedby);				 
			 Info2.setFRAUDCHK("");				 
			 Info2.setPSPCODE(pspcode);
			 Info2.setSPCODE(spcode);
			 Info2.setSUBSPCODE("");				 
			 Info2.setRECEIPTNO(billrefid);		
			 Info2.setAMOUNTTYPE(amttype);
			 Info2.setCALLBACKURL(CALLBACKURL);
			 Info2.setPAYERID(tranrefid);
			 Info2.setTRANTYPE(trantype); /*** Mode of Pay ****/
			 Info2.setCUSTMSISDN(custmsisdn);
			 Info2.setVERSION(VERSION);			 
				
			 Req_Modal.Insert_Request_002(Info2);
			
			 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, paytype, date, reqrefid, REQSL, "Q", trantype, "", "", MSGTYPE);  
				 
			 Req_Modal.Insert_Job_005(Info3);
	
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Request added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Payment_Posting_Request :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject GEPG_Executer(final Job_005 Job) 
	{
		JsonObject details = new JsonObject();
	
		try
		{
			logger.debug("******** Payment Process Started for REF ID "+Job.getREFNO()+" ********");
			
 			final String procedureCall = "{CALL PACK_O_PAY_SIMULATOR.PROC_O_REQUEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
 			
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
 			final String Payplan = util.ReplaceNull(resultMap.get("o_rescd")); 
 			
 			if(O_RESDESC.equals("TISS"))
 			{
 				details = GEPG_TISS_Payment(Job, resultMap);
 			}
 			else
 			{
	 			logger.debug("******** START CBS POSTING ********");
				  
			    logger.debug("O_TRANAMOUNT :::: "+O_TRANAMOUNT);
	 			logger.debug("O_DBCURR     :::: "+O_DBCURR);
	 			logger.debug("O_CHRGAMOUNT :::: "+O_CHRGAMOUNT);
	 			logger.debug("O_CHRGCURR   :::: "+O_CHRGCURR);
	 			logger.debug("O_DBACCOUNT  :::: "+O_DBACCOUNT);
	 			logger.debug("O_CRACCOUNT  :::: "+O_CRACCOUNT);
	 			
	 			final String cbsprocedureCall = "{CALL CBS_POST_PACK_SIMULATOR.PROC_CBSPOST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	 			
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
				
				/*  
		 		  String o_result = cbsresultMap.get("o_result").toString();
		 		  String o_message = cbsresultMap.get("o_message").toString(); 
		 		*/
				
				 String sql = "select VERSION from request002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
				 
				 List<String> VR = Jdbctemplate.queryForList(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() },  String.class );
				
				 sql = "Select * from transactions where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? order by transeq,legsl";
			 	
		 		 List<Transactions> Transactions_Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getSYSCODE(), "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO() }, new Transaction_Mapper());
		 	
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
					 
					 if(VR.get(0).contains("demo"))
					 {
						 JsonObject API_Response = Simulator.Get_Simulator_Response("", "", Job.getSUBORGCODE(), Job.getSYSCODE(), "CBS", "000", "I");  /*** simulator Response ***/
						 
						 String response = API_Response.get("Response").getAsString();
						 
						 errormsg = response;
					 }
					 else
					 {
						 errormsg = base64simulator.formrequestnew(Billamount, Charge, DBCurr, DBAccount, CRAccount, Narration, Reqcode, BRNCODE);
					 }
			 		 		 		
			 		 logger.debug("CBS RESPONSE for Transeq "+Transeq+" is :::: "+errormsg);  
			 		 
			 		 if(errormsg.equalsIgnoreCase("S"))
					 {
	 					 String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and TRANSEQ=?";
						
	 					 Jdbctemplate.update(Update_Sql, new Object[] { "POSTED", Job.getSUBORGCODE(), Job.getCHCODE(), Job.getSYSCODE(), "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO(), Transeq });
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
				       
					  ERRDESC = errormsg; 
	
					  if(errormsg.equalsIgnoreCase("0014"))  /** Invalid Account **/
					  {
						  ERRCD = "HP14";				  
						  ERRDESC = "Invalid Account";
					  }
					  else if(errormsg.equalsIgnoreCase("0096")) /** Transaction Declined **/
					  {
						  ERRCD = "HP112";
						  ERRDESC = "Transaction Declined";
					  }
					  else if(errormsg.equalsIgnoreCase("0051")) /** Insufficient Fund **/
					  {
						  ERRCD = "HP51";
						  ERRDESC = "Insufficient Fund";
					  }
					  else
					  {
						  ERRCD = "HP110";
						  ERRDESC = "Issue In CBS";
					  }
					  
					  flag = false;
				}
			
				if(flag)
				{
					if(Payplan.equals("1") || Job.getSERVCODE().equals("004")) /*** check Postpaid or callback Post**/
	 				{
						JsonObject webservice_details = Ws.Get_Webserice002_Info(Job.getSERVCODE(), Job.getPAYTYPE(), HEADERID);
					 	
			 			webservice_details.addProperty("PAYLOAD", util.ReplaceNull(resultMap.get("o_payload")));
			 			webservice_details.addProperty("SIGNPAYLOAD", util.ReplaceNull(resultMap.get("o_signpayload")));
			 			webservice_details.addProperty("HEADERID", HEADERID);
			 			webservice_details.addProperty("METHOD", util.ReplaceNull(resultMap.get("o_method")));
			 			webservice_details.addProperty("URI", util.ReplaceNull(resultMap.get("o_uri")));
			 			webservice_details.addProperty("FORMAT", util.ReplaceNull(resultMap.get("o_format")));
			 			webservice_details.addProperty("PROTOCOL", util.ReplaceNull(resultMap.get("o_protocol")));
						 		
					    String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
					  
					    String SIGNPAYLOAD = webservice_details.get("SIGNPAYLOAD").getAsString();
					  
					    String Requesturl = webservice_details.get("URI").getAsString();
					  
					    JsonArray Headers = webservice_details.get("Headers").getAsJsonArray();
					 
					    String Signature = getsignature(SIGNPAYLOAD);
	
			            PAYLOAD = PAYLOAD.replace("~gepgSignature~", Signature);
			          
			            logger.debug("Final Payload :::: "+PAYLOAD);
			          
					    webservice_details.addProperty("PAYLOAD", PAYLOAD);
							 
				        String O_DATE  = util.getCurrentDate("dd-MMM-yyyy");
				        Timestamp O_REQTIME  = util.get_oracle_Timestamp();
				        String O_ReqRefID  = Job.getREFNO();
				        String O_MSGURL = webservice_details.get("URI").getAsString(); 
				        String O_IP = "";
				        String O_PORT = "";
				        String O_HEAD_MSG = Headers.toString();
				        String O_BODY_MSG = PAYLOAD;
				        String O_INITATEDBY = "HPAY";
				        String O_Checksum = "";
				        String O_MSGTYPE = Job.getSERVCODE();
				        
				        String Channel_Code = Job.getPAYTYPE();
				        
				        Req_Modal.Insert_Request_001(Job.getSUBORGCODE(), Channel_Code, Job.getPAYTYPE(), O_MSGTYPE, O_FLOW, O_DATE, O_REQTIME, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum); 		
				        
				        JsonObject API_Response = new JsonObject();
				        
				        if(VR.get(0).contains("demo"))
						{
				        	API_Response.addProperty("Result", "Success");
				        	API_Response.addProperty("Message", "Payment Success");
				        	API_Response.addProperty("Response_Code", 200);
						}
				        else
				        {
				        	API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
				        }
						  	     
						logger.debug("Payment Post API_Response for ref id "+O_ReqRefID+" is :::: "+API_Response);
						
						Response_001 Response = new Response_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getSERVCODE(), "O", Job.getREFNO() , Requesturl , "IP", "PORT", Headers.toString(), API_Response.toString(), "TIPS", "");
							
						Res_Modal.Insert_Response_001(Response);  /**** Insert Response to Response_001 ****/	
						
						if(API_Response.get("Result").getAsString().equals("Success") && API_Response.get("Response_Code").getAsInt() == 200)
						{
							  //String response = API_Response.get("Response").getAsString();
							
							  Request_002 info = new Request_002();
	
							  info.setSTATUS(API_Response.get("Result").getAsString());
							  info.setCHCODE(Job.getCHCODE());
							  info.setPAYTYPE(Job.getPAYTYPE());
							  info.setTRANTYPE(Job.getTRANTYPE());
							  info.setREQREFNO(Job.getREFNO());
							  info.setREQSL(Job.getREQSL());
							  info.setRESCODE("200");
							  info.setRESPDESC("REQUEST SENT TO GEPG SUCCESSFULLY");
							  
							  PAY_001 pay = new PAY_001();
							  
							  pay.setSTATUS(API_Response.get("Result").getAsString());
							  pay.setCHCODE(Job.getCHCODE());
							  pay.setPAYTYPE(Job.getPAYTYPE());
							  pay.setTRANTYPE(Job.getTRANTYPE());
							  pay.setREQREFNO(Job.getREFNO());
							  pay.setREQSL(Job.getREQSL());
							  pay.setRESCODE("200");
							  
							  Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
							  
							  Req_Modal.Update_PAY_001(pay);
							  
							  details.addProperty("result", "success");
							  details.addProperty("stscode", "HP00");
							  details.addProperty("message", "Payment Request Processed Successfuly !!");
						 }  
						 else
						 {
							 Request_002 info = new Request_002();
	
							 info.setSTATUS(API_Response.get("Result").getAsString());
							 info.setCHCODE(Job.getCHCODE());
							 info.setPAYTYPE(Job.getPAYTYPE());
							 info.setTRANTYPE(Job.getTRANTYPE());
							 info.setREQREFNO(Job.getREFNO());
							 info.setREQSL(Job.getREQSL());
							 info.setERRCD(API_Response.get("Response_Code").getAsString());
							 info.setERRDESC(API_Response.get("Message").getAsString());
							  
							 PAY_001 pay = new PAY_001();
							  
							 pay.setSTATUS(API_Response.get("Result").getAsString());
							 pay.setCHCODE(Job.getCHCODE());
							 pay.setPAYTYPE(Job.getPAYTYPE());
							 pay.setTRANTYPE(Job.getTRANTYPE());
							 pay.setREQREFNO(Job.getREFNO());
							 pay.setREQSL(Job.getREQSL());
							 pay.setRESCODE(API_Response.get("Response_Code").getAsString());
							  
							 Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
							  
							 Req_Modal.Update_PAY_001(pay);
							 
							 details.addProperty("result", "failed");
							 details.addProperty("stscode", "HP108");
							 details.addProperty("message", "Payment Gateway Currently Not Available !!"); 
						 }
	 				}
	 				else /** initiate Quote post service ***/
	 				{
	 					String Quote_SERVCODE = "004";
	 					
	 					JsonObject webservice_details = Ws.Get_Webserice002_Info(Quote_SERVCODE, Job.getPAYTYPE(), HEADERID);
					 	
			 			webservice_details.addProperty("PAYLOAD", util.ReplaceNull(resultMap.get("o_payload")));
			 			webservice_details.addProperty("SIGNPAYLOAD", util.ReplaceNull(resultMap.get("o_signpayload")));
			 			webservice_details.addProperty("HEADERID", HEADERID);
			 			webservice_details.addProperty("METHOD", util.ReplaceNull(resultMap.get("o_method")));
			 			webservice_details.addProperty("URI", util.ReplaceNull(resultMap.get("o_uri")));
			 			webservice_details.addProperty("FORMAT", util.ReplaceNull(resultMap.get("o_format")));
			 			webservice_details.addProperty("PROTOCOL", util.ReplaceNull(resultMap.get("o_protocol")));
						 		
					    String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
					  
					    String SIGNPAYLOAD = webservice_details.get("SIGNPAYLOAD").getAsString();
					  
					    String Requesturl = webservice_details.get("URI").getAsString();
					  
					    JsonArray Headers = webservice_details.get("Headers").getAsJsonArray();
					 
					    String Signature = getsignature(SIGNPAYLOAD);
	
			            PAYLOAD = PAYLOAD.replace("~gepgSignature~", Signature);
			          
			            logger.debug("Final Payload :::: "+PAYLOAD);
			          
					    webservice_details.addProperty("PAYLOAD", PAYLOAD);
							 
				        String O_DATE  = util.getCurrentDate("dd-MMM-yyyy");
				        Timestamp O_REQTIME  = util.get_oracle_Timestamp();
				        String O_ReqRefID  = Job.getREFNO();
				        String O_MSGURL = webservice_details.get("URI").getAsString(); 
				        String O_IP = "";
				        String O_PORT = "";
				        String O_HEAD_MSG = Headers.toString();
				        String O_BODY_MSG = PAYLOAD;
				        String O_INITATEDBY = "HPAY";
				        String O_Checksum = "";
				        String O_MSGTYPE = Quote_SERVCODE;
				        
				        String Channel_Code = Job.getPAYTYPE();
				        
				        Req_Modal.Insert_Request_001(Job.getSUBORGCODE(), Channel_Code, Job.getPAYTYPE(), O_MSGTYPE, O_FLOW, O_DATE, O_REQTIME, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum); 		
	
				        JsonObject API_Response = new JsonObject();
				        
				        if(VR.get(0).contains("demo"))
						{
				        	API_Response.addProperty("Result", "Success");
				        	API_Response.addProperty("Message", "Payment Success");
				        	API_Response.addProperty("Response_Code", 200);
						}
				        else
				        {
				        	API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
				        }
						  	     
						logger.debug("Payment Post API_Response for ref id "+O_ReqRefID+" is :::: "+API_Response);
						
						Response_001 Response = new Response_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getSERVCODE(), "O", Job.getREFNO() , Requesturl , "IP", "PORT", Headers.toString(), API_Response.toString(), "TIPS", "");
							
						Res_Modal.Insert_Response_001(Response);  /**** Insert Response to Response_001 ****/	
						
						if(API_Response.get("Result").getAsString().equals("Success") && API_Response.get("Response_Code").getAsInt() == 200)
						{
							  //String response = API_Response.get("Response").getAsString();
							
							  Request_002 info = new Request_002();
	
							  info.setSTATUS(API_Response.get("Result").getAsString());
							  info.setCHCODE(Job.getCHCODE());
							  info.setPAYTYPE(Job.getPAYTYPE());
							  info.setTRANTYPE(Job.getTRANTYPE());
							  info.setREQREFNO(Job.getREFNO());
							  info.setREQSL(Job.getREQSL());
							  info.setRESCODE("200");
							  info.setRESPDESC("QUOTE REQUEST SENT TO GEPG SUCCESSFULLY");
							  
							  PAY_001 pay = new PAY_001();
							  
							  pay.setSTATUS(API_Response.get("Result").getAsString());
							  pay.setCHCODE(Job.getCHCODE());
							  pay.setPAYTYPE(Job.getPAYTYPE());
							  pay.setTRANTYPE(Job.getTRANTYPE());
							  pay.setREQREFNO(Job.getREFNO());
							  pay.setREQSL(Job.getREQSL());
							  
							  Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
							  
							  Req_Modal.Update_PAY_001(pay);
							  
							  details.addProperty("result", "success");
							  details.addProperty("stscode", "HP00");
							  details.addProperty("message", "Payment Request Processed Successfuly !!");
						 }  
						 else
						 {
							 Request_002 info = new Request_002();
	
							 info.setSTATUS(API_Response.get("Result").getAsString());
							 info.setCHCODE(Job.getCHCODE());
							 info.setPAYTYPE(Job.getPAYTYPE());
							 info.setTRANTYPE(Job.getTRANTYPE());
							 info.setREQREFNO(Job.getREFNO());
							 info.setREQSL(Job.getREQSL());
							 info.setERRCD(API_Response.get("Response_Code").getAsString());
							 info.setERRDESC(API_Response.get("Message").getAsString());
							  
							 PAY_001 pay = new PAY_001();
							  
							 pay.setSTATUS(API_Response.get("Result").getAsString());
							 pay.setCHCODE(Job.getCHCODE());
							 pay.setPAYTYPE(Job.getPAYTYPE());
							 pay.setTRANTYPE(Job.getTRANTYPE());
							 pay.setREQREFNO(Job.getREFNO());
							 pay.setREQSL(Job.getREQSL());
							 pay.setRESCODE(API_Response.get("Response_Code").getAsString());
							  
							 Req_Modal.Update_Request_002(info);  /**** Update request 002 ****/
							  
							 Req_Modal.Update_PAY_001(pay);
							 
							 details.addProperty("result", "failed");
							 details.addProperty("stscode", "HP108");
							 details.addProperty("message", "Payment Gateway Currently Not Available !!"); 
						 }
	 				}
				}
				else
				{
					 STATUS = "Failed";
					 
					 if(Payplan.equals("1") || Job.getSERVCODE().equals("004")) /*** check Postpaid or callback Post**/
		 			 {
						 ERRDESC += " when calling GEPG Payment Service";
		 			 }
					 else
					 {
						 ERRDESC += " when calling Quote Service";
					 }
				
					 Request_002 info = new Request_002();
	
					 info.setSTATUS(STATUS);
					 info.setCHCODE(Job.getCHCODE());
					 info.setPAYTYPE(Job.getPAYTYPE());
					 info.setTRANTYPE(Job.getTRANTYPE());
					 info.setREQREFNO(Job.getREFNO());
					 info.setREQSL(Job.getREQSL());
					 info.setERRCD(ERRCD);
					 info.setERRDESC(ERRDESC);
					  
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
					 
					 details.addProperty("result", "failed");
					 details.addProperty("stscode", ERRCD);
					 details.addProperty("message", ERRDESC);
				}
 			}
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  
			 
			 logger.debug("Exception in PACK_O_PAY.PROC_O_REQUEST :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject GEPG_TISS_Payment(final Job_005 Job, Map<String, Object> resultMap)
	{
		JsonObject details = new JsonObject();
		
		try
		{
			logger.debug("******** TISS Payment Process Started for REF ID "+Job.getREFNO()+" ********");
			
 			Common_Utils util = new Common_Utils();
 			
 			final String O_TRANAMOUNT = util.ReplaceNull(resultMap.get("o_tranamount"));
 			final String O_DBCURR = util.ReplaceNull(resultMap.get("o_dbcurr"));
 			final String O_CHRGAMOUNT = util.ReplaceNull(resultMap.get("o_chrgamount"));
 			final String O_CHRGCURR = util.ReplaceNull(resultMap.get("o_chrgcurr"));
 			final String O_DBACCOUNT = util.ReplaceNull(resultMap.get("o_dbaccount"));
 			final String O_CRACCOUNT = util.ReplaceNull(resultMap.get("o_craccount"));
 			final String O_FLOW = util.ReplaceNull(resultMap.get("o_flow"));
 			//final String HEADERID = util.ReplaceNull(resultMap.get("o_headerid"));
 			
 			logger.debug("******** START CBS POSTING ********");
			  
		    logger.debug("O_TRANAMOUNT :::: "+O_TRANAMOUNT);
 			logger.debug("O_DBCURR     :::: "+O_DBCURR);
 			logger.debug("O_CHRGAMOUNT :::: "+O_CHRGAMOUNT);
 			logger.debug("O_CHRGCURR   :::: "+O_CHRGCURR);
 			logger.debug("O_DBACCOUNT  :::: "+O_DBACCOUNT);
 			logger.debug("O_CRACCOUNT  :::: "+O_CRACCOUNT);
 			
 			final String cbsprocedureCall = "{CALL CBS_POST_PACK_SIMULATOR.PROC_CBSPOST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
 			
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
			
			/*  
	 		  String o_result = cbsresultMap.get("o_result").toString();
	 		  String o_message = cbsresultMap.get("o_message").toString(); 
	 		*/
			
			 String sql = "select VERSION from request002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
			 
			 List<String> VR = Jdbctemplate.queryForList(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL() }, String.class );
			
			 sql = "Select * from transactions where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? order by transeq,legsl";
		 	
	 		 List<Transactions> Transactions_Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getSYSCODE(), "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO() }, new Transaction_Mapper());
	 	
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
			  
			  String CRAccount = "";  String Billamount = "";  String  DBCurr = "";  String  BRNCODE = "";  String Bill_No = "";
			
			  for(int y=0;y<Credit_Info.size();y++)  
			  {
			     Billamount = Debit_Info.get(y).getAMOUNT();
				 String Charge = Debit_Info.get(y).getCHARGES();
				 String Narration = Debit_Info.get(y).getREMARKS();
				 String DBAccount = Debit_Info.get(y).getLEDGERNO();
				 CRAccount = Credit_Info.get(y).getLEDGERNO();
				 String Transeq = Debit_Info.get(y).getTRANSEQ();
				 Bill_No = Debit_Info.get(y).getINVOICENO();

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
				 
				 DBCurr = cbsresultMap.get("o_dbcurr").toString();
				 String Reqcode = cbsresultMap.get("o_reqcode").toString();
		 		 BRNCODE = cbsresultMap.get("o_brncode").toString();
				 
		 		 if(VR.get(0).contains("demo"))
				 {
					 JsonObject API_Response = Simulator.Get_Simulator_Response("", "", Job.getSUBORGCODE(), Job.getSYSCODE(), "CBS", "000", "I");  /*** simulator Response ***/
					 
					 String response = API_Response.get("Response").getAsString();
					 
					 errormsg = response;
				 }
				 else
				 {
					 errormsg = base64simulator.formrequestnew(Billamount, Charge, DBCurr, DBAccount, CRAccount, Narration, Reqcode, BRNCODE);
				 }
		 		 
		 		 logger.debug("CBS RESPONSE for Transeq "+Transeq+" is :::: "+errormsg);  
		 		 
		 		 if(errormsg.equalsIgnoreCase("S"))
				 {
 					 String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and TRANSEQ=?";
					
 					 Jdbctemplate.update(Update_Sql, new Object[] { "POSTED", Job.getSUBORGCODE(), Job.getCHCODE(), Job.getSYSCODE(), "TRANSFER", Job.getPAYTYPE(), "PENDING", Job.getREFNO(), Transeq });
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
			       
				  ERRDESC = errormsg; 

				  if(errormsg.equalsIgnoreCase("0014"))  /** Invalid Account **/
				  {
					  ERRCD = "HP14";				  
					  ERRDESC = "Invalid Account";
				  }
				  else if(errormsg.equalsIgnoreCase("0096")) /** Transaction Declined **/
				  {
					  ERRCD = "HP112";
					  ERRDESC = "Transaction Declined";
				  }
				  else if(errormsg.equalsIgnoreCase("0051")) /** Insufficient Fund **/
				  {
					  ERRCD = "HP51";
					  ERRDESC = "Insufficient Fund";
				  }
				  else
				  {
					  ERRCD = "HP110";
					  ERRDESC = "Issue In CBS";
				  }
				  
				  flag = false;
			}
			
			if(flag)
			{
				  //Generate_Tiss_File(String reportsl1, String dmyear, String DBCurrs, String TISSAMT1, String ourrecpno, String ibranchno1, String Refid, String CRAccounts, String billno);
				 
				  Generate_Tiss_File(Job.getREQSL(), util.getCurrentYear(), DBCurr, Billamount, Job.getREFNO()+Job.getREQSL(), BRNCODE, Job.getREFNO(), CRAccount, Bill_No);
				
				  STATUS = "SUCCESS";
				 
				  Request_002 info = new Request_002();
				
				  info.setSTATUS(STATUS);
				  info.setCHCODE(Job.getCHCODE());
				  info.setPAYTYPE(Job.getPAYTYPE());
				  info.setTRANTYPE(Job.getTRANTYPE());
				  info.setREQREFNO(Job.getREFNO());
				  info.setREQSL(Job.getREQSL());
				  info.setRESCODE("200");
				  info.setRESPDESC("TISS FILE GENERATED SUCCESSFULLY");
				  
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
  
				  details.addProperty("result", "success");
				  details.addProperty("stscode", "HP00");
				  details.addProperty("message", "Payment Request Processed Successfuly !!");
			}
			else
			{
				 STATUS = "Failed";
			
				 Request_002 info = new Request_002();

				 info.setSTATUS(STATUS);
				 info.setCHCODE(Job.getCHCODE());
				 info.setPAYTYPE(Job.getPAYTYPE());
				 info.setTRANTYPE(Job.getTRANTYPE());
				 info.setREQREFNO(Job.getREFNO());
				 info.setREQSL(Job.getREQSL());
				 info.setERRCD(ERRCD);
				 info.setERRDESC(ERRDESC);
				  
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
				 
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", ERRCD);
				 details.addProperty("message", ERRDESC);
			}
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  
			 
			 logger.debug("Exception in PACK_O_PAY.PROC_O_REQUEST :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Get_GEPG_Callback_Details(String request, String PAYTYPE, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();   
		
		JsonObject gepgPmtPstReqAck = new JsonObject();  
	
		String SUBORGCODE  = "EXIM";
		String SYSCODE     = "HP";
		String CHCODE      = "GEPG";  
		String SERVICECD   = "004"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			 
			 logger.debug("GEPG Callback Request :::: "+request);
			 
			 JsonObject Request = new Common_Utils().XMLToJsonObject(request); 
				
			 JsonObject Gepg = Request.get("Gepg").getAsJsonObject();
			 JsonObject gepgPymtResp = Gepg.get("gepgPymtResp").getAsJsonObject();		 
			 JsonObject PymtTrxInf = gepgPymtResp.get("PymtTrxInf").getAsJsonObject();
			 
			 String TrxId = PymtTrxInf.get("TrxId").getAsString();  
			 String RcptNum = PymtTrxInf.get("RcptNum").getAsString();
			 String TrxStsCode = PymtTrxInf.get("TrxStsCode").getAsString();
			 
			 String REQSL		=  Req_Modal.Generate_Serial().get("Serial").getAsString();		
			 
			 String MSGTYPE 	=  SERVICECD;  									/*** service code hardcoded value **/
			 String FLOW    	=  "I"; 									
			 String MSGURL 	 	=  "";   									/*** (blank) **/
			 String IP      	=  ""; 
			 String PORT    	=  "";  									/*** Need to Discuss **/
			 String HEAD_MSG    =  "";  	
			 String REQDATE 	=  util.getCurrentDate("dd-MMM-yyyy");  
			 String ReqRefID    =  System.currentTimeMillis() + REQSL;
			 String INITATEDBY  =  "GEPG";
			 String Checksum    =  "";
			 Timestamp REQTIME  =  util.get_oracle_Timestamp();
			 String BODY_MSG    =  request;   
			
			 Req_Modal.Insert_Request_001(SUBORGCODE, CHCODE, PAYTYPE, MSGTYPE, FLOW, REQDATE, REQTIME, ReqRefID, MSGURL, IP, PORT, HEAD_MSG, BODY_MSG, INITATEDBY,  Checksum); 		 /**** Insert request into request001 ***/
			  
			 if(TrxStsCode.equals("7101"))
			 {
				 String sql = "update PAY001 set STATUS=?,RESCODE=? where PAYTYPE=? and INVOICENO=? and REFNO=?";
				   
				 Jdbctemplate.update(sql, new Object[] { TrxStsCode, TrxStsCode, PAYTYPE, RcptNum, TrxId } );
				 
				 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, PAYTYPE, REQDATE, ReqRefID, REQSL, "Q", "QUOTE", "", "", MSGTYPE);  
				 
				 Req_Modal.Insert_Job_005(Info3);
			 }
			 else
			 {
				 String sql = "update PAY001 set STATUS=?,RESCODE=?,ERRCD=? where PAYTYPE=? and INVOICENO=? and REFNO=?";
				   
				 Jdbctemplate.update(sql, new Object[] { TrxStsCode, TrxStsCode, TrxStsCode, PAYTYPE, RcptNum, TrxId } );
			 }
			 
			 gepgPmtPstReqAck.addProperty("TrxStsCode", "7101");
			
			 details.add("gepgPmtPstReqAck", gepgPmtPstReqAck);
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
		 }
		
		 return details;
	}
	
	public JsonObject Parse_XML_Bill_Response(String response, JsonObject request, String BILLNO, String Signature, String BillreqId)
	{
		JsonObject details = new JsonObject();
		
		JsonObject PRIELEMENTS = new JsonObject();
		
		JsonObject SECELEMENTS = new JsonObject();
		
		try
		{
			 response = response.replace("~BillCtrNum~", BILLNO);
			 response = response.replace("~gepgSignature~", Signature);
			 response = response.replace("~BillReqId~", BillreqId);
			
			 JsonObject XML_Response = new Common_Utils().XMLToJsonObject(response);
			
			 JsonObject Gepg = XML_Response.get("Gepg").getAsJsonObject();				
			 JsonObject gepgBillQryResp = Gepg.get("gepgBillQryResp").getAsJsonObject();			 
			 JsonObject BillHdr = gepgBillQryResp.get("BillHdr").getAsJsonObject();			 
			 String BillStsCode = BillHdr.get("BillStsCode").getAsString();	 
			
			 if(BillStsCode.equals("7101"))
			 {				 			 
				 String BillStsDesc = BillHdr.get("BillStsDesc").getAsString();		
				 
				 JsonObject TxfrAcc = gepgBillQryResp.get("TxfrAcc").getAsJsonObject();			 
				 JsonArray AccDtl = TxfrAcc.get("AccDtl").getAsJsonArray();
				 JsonObject BillDtl = gepgBillQryResp.get("BillDtl").getAsJsonObject();	
				 
				 String SpCode = BillHdr.get("SpCode").getAsString();				 
				 String SpName = BillHdr.get("SpName").getAsString();
				 String BillReqId = BillHdr.get("BillReqId").getAsString();	 
				 String PayRefId = BillHdr.has("PayRefId") ? BillHdr.get("PayRefId").getAsString() : "";	
				 String BillCtrNum = BillDtl.get("BillCtrNum").getAsString();
				 String BillDesc = BillDtl.get("BillDesc").getAsString();
				 String MinPayAmt = BillDtl.get("MinPayAmt").getAsString();
				 String BillExprDt = BillDtl.get("BillExprDt").getAsString();
				 String BillRsv1 = BillDtl.get("BillRsv1").getAsString();
				 String BillRsv2 = BillDtl.get("BillRsv2").getAsString();
				 String BillRsv3 = BillDtl.get("BillRsv3").getAsString();
				 String PyrCellNum = BillDtl.get("PyrCellNum").getAsString();
				 String PyrName = BillDtl.get("PyrName").getAsString();
				 String BillAmt = BillDtl.get("BillAmt").getAsString();
				 String PayPlan = BillDtl.get("PayPlan").getAsString();
				 String Ccy = BillDtl.get("Ccy").getAsString();
				 String PyrEmailAddr = BillDtl.get("PyrEmailAddr").getAsString();
				 String PayOpt = BillDtl.get("PayOpt").getAsString();
				 
				 PRIELEMENTS.addProperty("spcode", SpCode);
				 PRIELEMENTS.addProperty("payrefid", PayRefId);
				 PRIELEMENTS.addProperty("billctrnum", BillCtrNum);
				 PRIELEMENTS.addProperty("billamt", BillAmt);
				 PRIELEMENTS.addProperty("ccy", Ccy);
				 PRIELEMENTS.addProperty("pyrname", PyrName);
				 PRIELEMENTS.addProperty("pyrcellnum", PyrCellNum);
				 PRIELEMENTS.addProperty("pyremailaddr", PyrEmailAddr);
				 PRIELEMENTS.addProperty("payopt", PayOpt);
				 PRIELEMENTS.addProperty("minpayamt", MinPayAmt);
				
				 SECELEMENTS.add("accdtl", AccDtl);
				 SECELEMENTS.addProperty("billstsdesc", BillStsDesc);
				 SECELEMENTS.addProperty("billstscode", BillStsCode);
				 SECELEMENTS.addProperty("spname", SpName);
				 SECELEMENTS.addProperty("billreqid", BillReqId);
				 SECELEMENTS.addProperty("billdesc", BillDesc);
				 SECELEMENTS.addProperty("billexprdt", BillExprDt);
				 SECELEMENTS.addProperty("billrsv1", BillRsv1);
				 SECELEMENTS.addProperty("billrsv2", BillRsv2);
				 SECELEMENTS.addProperty("billrsv3", BillRsv3);
				 SECELEMENTS.addProperty("payplan", PayPlan);
				 SECELEMENTS.addProperty("version", "v2");
				
				 JsonObject billdetails = new JsonObject();
				 JsonObject Bill_Info = new JsonObject();
				 
				 String paytype = request.get("paytype").getAsString();
				 String date = request.get("date").getAsString();
				 String reqrefid = request.get("reqrefid").getAsString();
				 String initiatedby = request.get("initiatedby").getAsString();
				 
				 Bill_Info.addProperty("paytype", paytype);
				 Bill_Info.addProperty("date", date);
				 Bill_Info.addProperty("reqrefid", reqrefid);
				 Bill_Info.addProperty("initiatedby", initiatedby);
				 
				 billdetails.addProperty("billno", BILLNO);				 
				 billdetails.add("prielements", PRIELEMENTS);
				 billdetails.add("secelements", SECELEMENTS);	

				 Bill_Info.add("billdetails", billdetails);
				
			     details.add("billinfo", Bill_Info); 
			     
			     details.addProperty("result", "success"); 
				 details.addProperty("stscode", "HP00"); 
				 details.addProperty("message", "Bill details found !!"); 
			 }
			 else
			 {
				 details.addProperty("result", "failed"); 
				 details.addProperty("stscode", "HP200"); 
				 details.addProperty("message", "Bill details not found !!"); 
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed"); 
			 details.addProperty("stscode", "HP06"); 
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Parsing XML Response ENH-2 :::: "+e.getLocalizedMessage());
		 }
		
		return details;
	}
	
	public JsonObject Parse_XML_Bill_Response_V2(String response, JsonObject request, String BILLNO, String Signature, String BillreqId)
	{
		JsonObject details = new JsonObject();
		
		JsonObject PRIELEMENTS = new JsonObject();
		
		JsonObject SECELEMENTS = new JsonObject();
		
		try
		{
			 response = response.replace("~BillCtrNum~", BILLNO);
			 response = response.replace("~gepgSignature~", Signature);
			 response = response.replace("~BillReqId~", BillreqId);
			 
			 JsonObject XML_Response = new Common_Utils().XMLToJsonObject(response);  
			
			 JsonObject Gepg = XML_Response.get("Gepg").getAsJsonObject();	
			
			 JsonObject gepgBillQryResp = Gepg.get("gepgBillQryResp").getAsJsonObject();	
			// JsonObject TxfrAcc = gepgBillQryResp.get("TxfrAcc").getAsJsonObject();	
			 
			 JsonObject BillHdr = gepgBillQryResp.get("BillHdr").getAsJsonObject();
			 
			 String BillStsCode = BillHdr.get("BillStsCode").getAsString();
			
			 if(BillStsCode.equals("7101"))
			 {
				 JsonObject BillDtl = gepgBillQryResp.get("BillDtl").getAsJsonObject();
					
				 String BillStsDesc = BillHdr.get("BillStsDesc").getAsString();
				 String SpCode = BillHdr.get("SpCode").getAsString();				 
				 String SpName = BillHdr.get("SpName").getAsString();
				 String BillReqId = BillHdr.get("BillReqId").getAsString();	
				 String PayRefId = BillHdr.has("PayRefId") ? BillHdr.get("PayRefId").getAsString() : "";		
				 
				 String BillCtrNum = BillDtl.get("BillCtrNum").getAsString();
				 String BillDesc = BillDtl.get("BillDesc").getAsString();
				 String MinPayAmt = BillDtl.get("MinPayAmt").getAsString();
				 String BillExprDt = BillDtl.get("BillExprDt").getAsString();
				 String BillRsv1 = BillDtl.get("BillRsv1").getAsString();
				 String BillRsv2 = BillDtl.get("BillRsv2").getAsString();
				 String BillRsv3 = BillDtl.get("BillRsv3").getAsString();
				 String PyrCellNum = BillDtl.get("PyrCellNum").getAsString();
				 String PyrName = BillDtl.get("PyrName").getAsString();
				 String BillAmt = BillDtl.get("BillAmt").getAsString();
				 String PayPlan = BillDtl.get("PayPlan").getAsString();
				 String Ccy = BillDtl.get("Ccy").getAsString();
				 String PyrEmailAddr = BillDtl.get("PyrEmailAddr").getAsString();
				 String PayOpt = BillDtl.get("PayOpt").getAsString();

				 PRIELEMENTS.addProperty("spcode", SpCode);
				 PRIELEMENTS.addProperty("payrefid", PayRefId);
				 PRIELEMENTS.addProperty("billctrnum", BillCtrNum);
				 PRIELEMENTS.addProperty("billamt", BillAmt);
				 PRIELEMENTS.addProperty("ccy", Ccy);
				 PRIELEMENTS.addProperty("pyrname", PyrName);
				 PRIELEMENTS.addProperty("pyrcellnum", PyrCellNum);
				 PRIELEMENTS.addProperty("pyremailaddr", PyrEmailAddr);
				 PRIELEMENTS.addProperty("payopt", PayOpt);
				 PRIELEMENTS.addProperty("minpayamt", MinPayAmt);
				
				// SECELEMENTS.add("AccDtl", AccDtl);
				 SECELEMENTS.addProperty("billstsdesc", BillStsDesc);
				 SECELEMENTS.addProperty("billstscode", BillStsCode);
				 SECELEMENTS.addProperty("spname", SpName);
				 SECELEMENTS.addProperty("billreqid", BillReqId);
				 SECELEMENTS.addProperty("billdesc", BillDesc);
				 SECELEMENTS.addProperty("billexprdt", BillExprDt);
				 SECELEMENTS.addProperty("billrsv1", BillRsv1);
				 SECELEMENTS.addProperty("billrsv2", BillRsv2);
				 SECELEMENTS.addProperty("billrsv3", BillRsv3);
				 SECELEMENTS.addProperty("payplan", PayPlan);
				 SECELEMENTS.addProperty("version", "v3");
				 
				 JsonObject billdetails = new JsonObject();
				 JsonObject Bill_Info = new JsonObject();
				 
				 String paytype = request.get("paytype").getAsString();
				 String date = request.get("date").getAsString();
				 String reqrefid = request.get("reqrefid").getAsString();
				 String initiatedby = request.get("initiatedby").getAsString();
				 
				 Bill_Info.addProperty("paytype", paytype);
				 Bill_Info.addProperty("date", date);
				 Bill_Info.addProperty("reqrefid", reqrefid);
				 Bill_Info.addProperty("initiatedby", initiatedby); 
				 
				 billdetails.addProperty("billno", BILLNO);				 
				 billdetails.add("prielements", PRIELEMENTS);
				 billdetails.add("secelements", SECELEMENTS);	

				 Bill_Info.add("billdetails", billdetails);
				
			     details.add("billinfo", Bill_Info); 
			     
			     details.addProperty("result", "success"); 
				 details.addProperty("stscode", "HP00"); 
				 details.addProperty("message", "Bill details found !!"); 
			 }
			 else
			 {
				 details.addProperty("result", "failed"); 
				 details.addProperty("stscode", "HP200"); 
				 details.addProperty("message", "Bill details not found !!"); 
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed"); 
			 details.addProperty("stscode", "HP06"); 
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Parsing XML Response ENH-3 :::: "+e.getLocalizedMessage());
		 }
		
		return details;
	}
	
	public JsonObject Bill_Validation(JsonObject Request, JsonObject Headers)
	{
		 JsonObject details = new JsonObject();
			
		 boolean flag = true;
		 
		 try
		 {
			 Common_Utils util = new Common_Utils();
			 
			 if(!Request.has("getbill"))
			 {
				 flag = false;
			 }
			 else
			 {
				 JsonObject getbill = Request.get("getbill").getAsJsonObject();	
				 
				 String[] getbill_Members = { "paytype", "date", "reqrefid", "initiatedby", "primaryarg" };
				 
				 if(!util.JsonMemberNullChecker(getbill, getbill_Members))
				 {
					 flag = false;
				 }
				 else
				 {
					 JsonObject primaryarg = getbill.get("primaryarg").getAsJsonObject();
					 
					 String[] primaryarg_Members = { "billno" };
					 
					 if(!util.JsonMemberNullChecker(primaryarg, primaryarg_Members))
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
				 
			 JsonObject getbill = Request.get("getbill").getAsJsonObject();	
			 JsonObject primaryarg = getbill.get("primaryarg").getAsJsonObject();			 
			 
			 String date = getbill.get("date").getAsString();
			 String reqrefid = getbill.get("reqrefid").getAsString();
			 String initiatedby = getbill.get("initiatedby").getAsString();
			 String billno = primaryarg.get("billno").getAsString();
			 
			 String Error_Reason = "";
			 
			 if(!util.isvalidDate(date, "dd-MMM-yyyy"))
			 {
				 Error_Reason = "date is not in valid format, use dd-MM-yyyy format";
			 }
			 else if(!util.getCurrentDate("dd-MMM-yyyy").equals(date))
			 {
				 Error_Reason = "invalid date value found";
			 }
			 else if(util.isNullOrEmpty(reqrefid))
			 {
				 Error_Reason = "reqrefid should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(initiatedby))
			 {
				 Error_Reason = "initiatedby should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(billno))
			 {
				 Error_Reason = "billno should be not be Empty";
			 }
			 
			 if(util.isNullOrEmpty(Error_Reason))
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
			 
			 logger.debug("Exception in GEPG Payment_Bill_Validation :::: "+e.getLocalizedMessage());
		 }
		 
		 return details;
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
				 
				 String[] transfers_Members = { "trandetail" , "sourcedtl", "billpay", "paytype", "date", "reqrefid", "initiatedby", "trantype" };
				 
				 if(!util.JsonMemberNullChecker(transfers, transfers_Members))
				 {
					 flag = false;
				 }
				 else
				 {
					 JsonObject trandetail = transfers.get("trandetail").getAsJsonObject();			 
					 JsonObject sourcedtl = transfers.get("sourcedtl").getAsJsonObject();	
					 JsonObject billpay = transfers.get("billpay").getAsJsonObject();
					 JsonObject destinationdtl = transfers.get("destinationdtl").getAsJsonObject();
					 
					 String[] trandetails_Members = { "tranrefid" , "tranamt", "trancurr" };
					 String[] sourcedtl_Members = { "debitamt", "debitcurr", "s_accoutname" , "s_accoutno", "s_mobile", "s_bracnch" };
					 String[] billpay_Members = { "invoiceno", "billrefid", "pspcode", "spcode", "custmsisdn", "amttype", "payplan" };
					 String[] destinationdtl_Members = { "d_accoutno", "d_bracnch" };
					
					 if(!util.JsonMemberNullChecker(trandetail, trandetails_Members))
					 {
						 flag = false;
					 }
					 
					 if(!util.JsonMemberNullChecker(sourcedtl, sourcedtl_Members))
					 {
						 flag = false;
					 }
					 
					 if(!util.JsonMemberNullChecker(billpay, billpay_Members))
					 {
						 flag = false;
					 }
					 
					 if(!util.JsonMemberNullChecker(destinationdtl, destinationdtl_Members))
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
			 
			 JsonObject transfers = Request.get("transfers").getAsJsonObject();	
			 JsonObject trandetail = transfers.get("trandetail").getAsJsonObject();			 
			 JsonObject sourcedtl = transfers.get("sourcedtl").getAsJsonObject();
			 JsonObject billpay = transfers.get("billpay").getAsJsonObject();
			 JsonObject destinationdtl = transfers.get("destinationdtl").getAsJsonObject();

			 String reqrefid = transfers.get("reqrefid").getAsString();
			 String date = transfers.get("date").getAsString();
			 String paydate = transfers.get("paydate").getAsString();
			 String trantype = transfers.get("trantype").getAsString();
			 String initiatedby = transfers.get("initiatedby").getAsString();
			 
			 String tranrefid = trandetail.get("tranrefid").getAsString();
			 String tranamt = trandetail.get("tranamt").getAsString();
			 String trancurr = trandetail.get("trancurr").getAsString();
			 
			 String debitamt = sourcedtl.get("debitamt").getAsString();
			 String debitcurr = sourcedtl.get("debitcurr").getAsString();
			 String s_accoutno = sourcedtl.get("s_accoutno").getAsString();
			 String s_bracnch = sourcedtl.get("s_bracnch").getAsString();
			 String s_mobile = sourcedtl.get("s_mobile").getAsString();
			 
			 String d_accoutno = destinationdtl.get("d_accoutno").getAsString();
			 String d_bracnch = destinationdtl.get("d_bracnch").getAsString();
			 
			 String invoiceno = billpay.get("invoiceno").getAsString();
			 String billrefid = billpay.get("billrefid").getAsString();
			 String pspcode = billpay.get("pspcode").getAsString();
			 String spcode = billpay.get("spcode").getAsString();
			 String custmsisdn = billpay.get("custmsisdn").getAsString();
			 String amttype = billpay.get("amttype").getAsString();
			 String payplan = billpay.get("payplan").getAsString();
			
			 String Error_Reason = "";
			 
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
			 else if(!trantype.equals("K") && !trantype.equals("D") && !trantype.equals("C") && !trantype.equals("T"))
			 {
				 Error_Reason = "trantype possible values are K / D / C / T";
			 }
			 else if(util.isNullOrEmpty(reqrefid))
			 {
				 Error_Reason = "reqrefid should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(initiatedby))
			 {
				 Error_Reason = "initiatedby should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(tranrefid))
			 {
				 Error_Reason = "tranrefid should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(tranamt))
			 {
				 Error_Reason = "tranamt should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(trancurr))
			 {
				 Error_Reason = "trancurr should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(debitamt))
			 {
				 Error_Reason = "debitamt should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(debitcurr))
			 {
				 Error_Reason = "debitcurr should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(s_accoutno))
			 {
				 Error_Reason = "s_accoutno should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(s_bracnch))
			 {
				 Error_Reason = "s_bracnch should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(s_mobile))
			 {
				 Error_Reason = "s_mobile should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(d_accoutno))
			 {
				 Error_Reason = "d_accoutno should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(d_bracnch))
			 {
				 Error_Reason = "d_bracnch should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(invoiceno))
			 {
				 Error_Reason = "invoiceno should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(billrefid))
			 {
				 Error_Reason = "billrefid should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(pspcode))
			 {
				 Error_Reason = "pspcode should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(spcode))
			 {
				 Error_Reason = "spcode should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(custmsisdn))
			 {
				 Error_Reason = "custmsisdn should be not be Empty";
			 }
			 else if(util.isNullOrEmpty(amttype))
			 {
				 Error_Reason = "amttype should be not be Empty";
			 }
			 else if(!amttype.equals("F") && !amttype.equals("P") && !amttype.equals("E") && !amttype.equals("I") && !amttype.equals("L"))
			 {
				 Error_Reason = "amttype possible values are F / P / E / I / L";
			 }
			 else if(util.isNullOrEmpty(payplan))
			 {
				 Error_Reason = "payplan should be not be Empty";
			 }
			 else if(!payplan.equals("1") && !payplan.equals("2"))
			 {
				 Error_Reason = "payplan possible values are 1 for Postpaid and 2 for Prepaid";
			 }
			 
			 if(transfers.has("tissdtl"))
			 {
				 JsonObject tissdtl = transfers.get("tissdtl").getAsJsonObject();
				 
				 String[] tissdtl_Members = { "accoutno", "acname", "bankname" , "bankcode" };
				 
				 if(!util.JsonMemberNullChecker(tissdtl, tissdtl_Members))
				 {
					 Error_Reason = "Mandatory inputs missed in tissdtl";
				 }
				 
				 String accoutno = tissdtl.get("accoutno").getAsString();
				 String acname = tissdtl.get("acname").getAsString();
				 String bankname = tissdtl.get("bankname").getAsString();
				 String bankcode = tissdtl.get("bankcode").getAsString();
				 
				 if(util.isNullOrEmpty(accoutno))
				 {
					 Error_Reason = "tissdtl accoutno should be not be Empty";
				 }
				 else if(util.isNullOrEmpty(acname))
				 {
					 Error_Reason = "tissdtl acname should be not be Empty";
				 }
				 else if(util.isNullOrEmpty(bankname))
				 {
					 Error_Reason = "tissdtl bankname should be not be Empty";
				 }
				 else if(util.isNullOrEmpty(bankcode))
				 {
					 Error_Reason = "tissdtl bankcode should be not be Empty";
				 }
			 }
			 
			 if(util.isNullOrEmpty(Error_Reason))
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
			 
			 logger.debug("Exception in GEPG Payment_Posting_Validation :::: "+e.getLocalizedMessage());
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
			 
		/*	 if(Active_System.equalsIgnoreCase("live"))
			 {
				  sql = "SELECT I.IACLINK_BRN_CODE BRN_CODE, A.ACNTS_INOP_ACNT, A.ACNTS_DORMANT_ACNT,A.ACNTS_DB_FREEZED,A. ACNTS_CR_FREEZED, I.IACLINK_ACTUAL_ACNUM AC_No, A.ACNTS_AC_NAME1  CUSTOMERNAME,\r\n" + 
				  		"A.ACNTS_CURR_CODE CURRENCY, B.ACNTBAL_AC_BAL BALANCE, A.ACNTS_CLIENT_NUM CUSTOMERNO, A.ACNTS_AC_TYPE,AD.ADDRDTLS_MOBILE_NUM MOBILENO, TRIM(AM.ACNTMAIL_EMAIL_ADDR) Email_Id\r\n" + 
				  		"FROM ACNTS@eximtzpcbs A ,  IACLINK@eximtzpcbs I ,ACNTBAL@eximtzpcbs B,CLIENTS@eximtzpcbs C, ADDRDTLS@eximtzpcbs AD,ACNTMAIL@eximtzpcbs AM\r\n" + 
				  		"WHERE A.ACNTS_INTERNAL_ACNUM=I.IACLINK_INTERNAL_ACNUM AND B.ACNTBAL_INTERNAL_ACNUM=I.IACLINK_INTERNAL_ACNUM AND  AM.ACNTMAIL_INTERNAL_ACNUM =A.ACNTS_INTERNAL_ACNUM \r\n" + 
				  		"AND C.CLIENTS_CODE(+)=A.ACNTS_CLIENT_NUM AND  C.CLIENTS_ADDR_INV_NUM=AD.ADDRDTLS_INV_NUM(+)  AND AD.ADDRDTLS_COMM_ADDR(+)=1 AND A.ACNTS_ENTITY_NUM=1\r\n" + 
				  		"AND I.IACLINK_ENTITY_NUM=1 AND B.ACNTBAL_ENTITY_NUM=1 AND AM.ACNTMAIL_ENTITY_NUM=1 AND IACLINK_ACTUAL_ACNUM = ?";
			 }
			*/	
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
	
	public String getsignature(String dgdata)
	{
		String Signeddata = "";
		
		try
		{
			String password = "eximbank123";
			String alias = "eximbank";

			char[] passord = null;
			PrivateKey privateKey = null;

			File file = new File("C:\\gepg\\gepg.p12");
			InputStream stream = new FileInputStream(file);
			java.security.KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(stream, passord);
			privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());

			FileInputStream fin = new FileInputStream("C:\\gepg\\gepgclient.cer");
			CertificateFactory f = CertificateFactory.getInstance("X.509");
			X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
			PublicKey pk = certificate.getPublicKey();

			byte[] data2 = dgdata.getBytes("UTF8");

			Signature sig2 = Signature.getInstance("SHA1WithRSA");
			sig2.initSign(privateKey);
			sig2.update(data2);
			byte[] signatureBytes2 = sig2.sign();

			Signeddata = Base64.getEncoder().encodeToString(signatureBytes2);

			Signeddata = Signeddata.replaceAll("\n", "");
			Signeddata = Signeddata.replaceAll("\r", "");

			sig2.initVerify(pk);
			sig2.update(data2);
			
			logger.debug("Final Signature :::: "+Signeddata);
		}
		catch(Exception e)
		{
			logger.debug("Exception in getsignature :::: "+e.getLocalizedMessage());
		}
		
		return Signeddata;
	}
	
	public boolean Generate_Tiss_File(String reportsl1, String dmyear, String DBCurrs, String TISSAMT1, String ourrecpno, String ibranchno1, String Refid, String CRAccounts, String billno)
	{
		//String date ="TISS";	
		
		String payerNameo = ""; 
		String w_cracna = "";   
		String datetime1 = new Common_Utils().getCurrentDateTime();  	
		String tisspath = "C://GEPG//TISS";   
	    
		try
		{
			String datet ="{1:F01EXTNTZTZAXXX0000000000}{2:I103TARATZTZXXXXN}{3:{103:TIS}}{4:\r\n" + 
					":20:0088Tr"+reportsl1+"\r\n" + 
					":23B:CRED\r\n" + 
					":32A:"+dmyear+DBCurrs+TISSAMT1+"\r\n" + 
					":50K:/"+ourrecpno+
					"\r\n@payerNameo@\r\n" +
					"TANZANIA|"+ibranchno1+"/"+Refid+"\r\n" + 
					":56A:TANZTZTXXXX\r\n" + 
					":57A:TARATZTZXXX\r\n" + 
					":59:/"+CRAccounts+"\r\n@w_cracna@\r\n" + "TANZANIA"+"\r\n"+
					":70:/ROC/"+billno+"\r\n" + 
					":71A:SHA\r\n" + 
					":72:/TRA/GePG PAYMENT\r\n" + 
					"-}\r\n" + 
					"";
			
			String bigname = payerNameo;
			
			String bigname1 = w_cracna;
			
			//System.out.println(datet);
			
			bigname = splitString(bigname);
			
			bigname1 = splitString(bigname1);
			
			datet = datet.replaceAll("@payerNameo@", bigname);
			
			datet = datet.replaceAll("@w_cracna@", bigname1);
			
			//System.out.println(datet);
			
			String filename =  ""+ billno+"" + datetime1 + ".txt";
			
			FileWriter writer = new FileWriter(tisspath + filename , false);
			
			//System.out.println(datet);
			
			//System.out.println(filename);
			
			writer.write(datet);
			
			writer.close();
			
			return true;
		}
		catch(Exception e)
		{
			logger.debug("Exception in Generate_Tiss_File :::: "+e.getLocalizedMessage());
			
			return false;
		}
	}
	
	public String splitString(String payerNameo) 
	{
	    if(payerNameo.length() >35)	
	    {
	        List<String> res = new ArrayList<String>();

	        int lineSize=35;
	        Pattern p = Pattern.compile("\\b.{1," + (lineSize-1) + "}\\b\\W?");
	        Matcher m = p.matcher(payerNameo);
	        String test1 = null ;
			String test = null ; 
			String test2 = null ;
				
			while(m.find()) 
			{     
                res.add(m.group());
                
                if (test == null)
                {
                	 test =m.group().trim();
                }
                else if(test2 == null)
                {
                	 test2 =m.group().trim();                	
                }
                else 
                {
                	 test1 =m.group().trim();
                }

                if(test2 != null)
                {
                	payerNameo =test +"\n"+ test2;               	
                }
   
                if(test1 != null)
                {
                	payerNameo =test +"\n"+ test2+"\n"+ test1 ;               	 
                }    
	        }
	    }
	    
	    return payerNameo;
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
	
	public class GEPG_Mapper implements RowMapper<Request_002> 
    {
		public Request_002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Request_002 API = new Request_002();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setSYSCODE(rs.getString("SYSCODE"));
			API.setCHCODE(rs.getString("CHCODE"));
			API.setPSPCODE(rs.getString("PSPCODE"));
			API.setSPCODE(rs.getString("SPCODE"));
			API.setPAYEEREF(rs.getString("PAYEEREF"));
			API.setREQREFNO(rs.getString("REQREFNO"));
			API.setINVOICENO(rs.getString("INVOICENO"));
			API.setCREDITAMT(rs.getString("CREDITAMT"));
			API.setCREDITCURR(rs.getString("CREDITCURR"));  
			API.setD_ACCOUNT(rs.getString("D_ACCOUNT"));
			API.setS_ACCOUNT(rs.getString("S_ACCOUNT"));
			API.setS_MOBILE(rs.getString("S_MOBILE"));
			API.setS_ACNAME(rs.getString("S_ACNAME"));
			API.setS_EMAILID(rs.getString("S_EMAILID"));
			API.setCALLBACKURL(rs.getString("CALLBACKURL"));
			API.setPAYTYPE(rs.getString("PAYTYPE"));
			
			return API;
		}
    }
}
