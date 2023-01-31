package com.hdsoft.hdpay.models;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
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
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Request_002;
import com.hdsoft.hdpay.Repositories.Response_001;
import com.hdsoft.hdpay.Repositories.Transactions;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.nimbusds.jose.util.IOUtils;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class MSC_Modal implements Database
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
	
	public JsonObject Get_MSC_Bill_Details(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE  = "EXIM";
		String CHCODE  = Headers.get("ChannelID").getAsString();  
		String SERVICECD = "001"; 
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			
			 JsonObject Request = util.StringToJsonObject(request);
			 
			 JsonObject Validation_details = new JsonObject();
				
			 Validation_details = Bill_Validation(Request, Headers);
			
			 if(Validation_details.get("result").getAsString().equals("failed"))
			 {
				 return Validation_details;
			 }
			
			 JsonObject getbill = Request.get("getbill").getAsJsonObject();			
			 
			 String paytype = getbill.get("paytype").getAsString();   /**** Taken as Requested Date ****/
			 
			 JsonObject primaryarg = getbill.get("primaryarg").getAsJsonObject();	
 			 
			 String date = getbill.get("date").getAsString();
			 String reqrefid = getbill.get("reqrefid").getAsString();
			 String initiatedby = getbill.get("initiatedby").getAsString();
			 String billno = primaryarg.get("billno").getAsString();
			 
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
			 
			 if(webservice_details.get("Result").getAsString().equals("Failed"))
			 {
				 logger.debug("Error in Get_Webserice_Info :::: "+webservice_details);
				 	  
				 return webservice_details; 
			 }
			
	         String requesturl = webservice_details.get("URI").getAsString(); 
			
	         requesturl = requesturl.replace("~invoices~", billno);	

	         String VERSION  = Headers.get("VERSION").getAsString(); 
	         
	         JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
			  
			 String Headers_str = O_Headers.toString();  String token = "";
			 
			 JsonObject Token_details = new JsonObject();
			 
			 int Response_code = 0;
			 
			 if(VERSION.equalsIgnoreCase("demo"))  /*** simulator Response Code ***/
			 {
				 Token_details = Simulator.Get_Simulator_Response("", "", SUBORGCODE, SYSCODE, paytype, "999", "O");  /*** simulator Response ***/
				  
				 Response_code = Token_details.get("Response_Code").getAsInt();
				 
				 String res = Token_details.get("Response").getAsString();
				
				 Token_details = util.StringToJsonObject(res);
			 }
			 else
			 {
				 Token_details = Generate_Token();
				 
				 Response_code = Token_details.get("Response_Code").getAsInt();
			 }
			
		     if(Response_code == 200)
		     {
			     token = Token_details.get("access_token").getAsString();
		     }
		     else
		     {
		    	 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP111");
				 details.addProperty("message", "token generation failed !!");
				 
				 return details;
		     }
		     
			 Headers_str = Headers_str.replace("~Token~", token);
			 
			 O_Headers = util.StringToJsonArray(Headers_str);
			 
			 webservice_details.addProperty("URI", requesturl);
			 webservice_details.add("Headers", O_Headers);
			
		     String O_FLOW  =  "O"; 
		     String O_ReqRefID  = reqrefid;
		     String O_MSGURL = requesturl; 
		     String O_IP = "";
		     String O_PORT = "";
		     String O_HEAD_MSG = Headers.toString();
		     String O_BODY_MSG = request;
		     String O_INITATEDBY = "HPAY";
		     String O_Checksum = "";
		     
		     Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, paytype, SERVICECD, O_FLOW, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
	
			 JsonObject API_Response = new JsonObject();
			
			 if(VERSION.equalsIgnoreCase("demo"))  /*** simulator Response Code ***/
			 {
				 API_Response = Simulator.Get_Simulator_Response("", "", SUBORGCODE, SYSCODE, paytype, SERVICECD, O_FLOW);  /*** simulator Response ***/	 
				 
				 String Res = API_Response.toString();
				 
				 Res = Res.replaceAll("~InvoiceBlNumber~", billno);
				 Res = Res.replaceAll("~InvoiceAmount~", util.Generate_Random_Amount());
				 
				 API_Response = util.StringToJsonObject(Res);
			 }
			 else
			 {
				 API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
			 }
	         
	         logger.debug("MSC Bill API_Response :::: "+API_Response);
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, FLOW, reqrefid , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), paytype, "");
				
		     Res_Modal.Insert_Response_001(Response);	 /**** Insert Response to Response_001 ****/	  
		    
		     int Response_Code = API_Response.get("Response_Code").getAsInt();
		     
		     if(Response_Code != 200)
	         {
	        	 details.addProperty("result",  "failed"); 
				 details.addProperty("stscode", "HP109"); 
				 details.addProperty("message", "Biller Gateway Currently Not Available !!"); 
	         }
		     else
			 {					
	        	 String response = API_Response.get("Response").getAsString();
	        	
	        	 JsonArray Total_Response = util.StringToJsonArray(response);
	        	 
	        	 JsonObject Bill_Response = Total_Response.get(0).getAsJsonObject();
	        	 
	        	 if(Bill_Response.get("InvoiceAmount").isJsonNull())
	        	 {
	        		 details.addProperty("result", "failed"); 
					 details.addProperty("stscode", "HP200"); 
					 details.addProperty("message", "Bill details not found !!"); 
	        	 }
	        	 else
	        	 {
	        		 String InvoiceCurrency = util.JsonNullRemover(Bill_Response, "InvoiceCurrency");
	        		 String InvoiceCustomer = util.JsonNullRemover(Bill_Response, "InvoiceCustomer");
	        		 String InvoiceStatus = util.JsonNullRemover(Bill_Response, "InvoiceStatus");
	        		 String InvoiceCustomerTaxCode = util.JsonNullRemover(Bill_Response, "InvoiceCustomerTaxCode");
	        		 String InvoiceCustomerPhone = util.JsonNullRemover(Bill_Response, "InvoiceCustomerPhone");
	        		 String InvoiceBlNumber = util.JsonNullRemover(Bill_Response, "InvoiceBlNumber");
	        		 String InvoiceVessel = util.JsonNullRemover(Bill_Response, "InvoiceVessel");
	        		 String InvoiceVoyage = util.JsonNullRemover(Bill_Response, "InvoiceVoyage");
	        		 String InvoiceNumber = util.JsonNullRemover(Bill_Response, "InvoiceNumber");
	        		 String InvoiceAmount = util.JsonNullRemover(Bill_Response, "InvoiceAmount");
	        		 
	        		 JsonObject Primary = new JsonObject();	        			
	        		 JsonObject Secondary = new JsonObject();
	        		 
	        		 Primary.addProperty("invoicecurrency", InvoiceCurrency);
	        		 Primary.addProperty("invoiceamount", InvoiceAmount);
	        		 Primary.addProperty("invoicestatus", InvoiceStatus);
	        		 Primary.addProperty("invoicecustomer", InvoiceCustomer);
	        		 
	        		 Secondary.addProperty("invoicecustomerTaxCode", InvoiceCustomerTaxCode);
	        		 Secondary.addProperty("invoicecustomerPhone", InvoiceCustomerPhone);
	        		 Secondary.addProperty("invoiceblNumber", InvoiceBlNumber);
	        		 Secondary.addProperty("invoicevessel", InvoiceVessel);
	        		 Secondary.addProperty("invoicevoyage", InvoiceVoyage);
	        		 
	        		 JsonObject billdetails = new JsonObject();
					 JsonObject Bill_Info = new JsonObject();
					 
					 Bill_Info.addProperty("paytype", paytype);
					 Bill_Info.addProperty("date", date);
					 Bill_Info.addProperty("reqrefid", reqrefid);
					 Bill_Info.addProperty("initiatedby", initiatedby);
					 
					 billdetails.addProperty("billno", InvoiceNumber);				 
					 billdetails.add("prielements", Primary);
					 billdetails.add("secelements", Secondary);	

					 Bill_Info.add("billdetails", billdetails);
					
				     details.add("billinfo", Bill_Info); 
				     			     
				     details.addProperty("result", "success"); 
					 details.addProperty("stscode", "HP00"); 
					 details.addProperty("message", "Bill details found !!"); 
	        	 }
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed"); 
			 details.addProperty("stscode", "HP06"); 
			 details.addProperty("message", e.getLocalizedMessage());   

			 logger.debug("Exception in MSC_Bill_Details :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Handle_MSC_Payment_Request(JsonObject Request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();
		
		final String SUBORGCODE  = "EXIM";
		final String SYSCODE     = "HP";
		final String CHCODE 	 =  Headers.get("ChannelID").getAsString();  					
		final String MSGTYPE 	 =  "002";  					
		final String FLOW        =  "O"; 		
		
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
			 JsonObject billpay = transfers.get("billpay").getAsJsonObject();
			 
			 final String paytype = transfers.get("paytype").getAsString();
			 final String date = transfers.get("date").getAsString();  
			 final String reqrefid = transfers.get("reqrefid").getAsString();
			 String paydate = transfers.get("paydate").getAsString();
			 String initiatedby = transfers.get("initiatedby").getAsString();
			 String trantype = transfers.get("trantype").getAsString();
			 String checksum = transfers.has("checksum") ? transfers.get("checksum").getAsString() : "";  
			
			 String tranamt = trandetail.get("tranamt").getAsString();
			 String trancurr = trandetail.get("trancurr").getAsString();
			 String tranrefid = trandetail.get("tranrefid").getAsString();
			 
			 String s_accoutno = sourcedtl.get("s_accoutno").getAsString();
			 String s_bracnch = sourcedtl.get("s_bracnch").getAsString();
			 String s_accoutname = sourcedtl.has("s_accoutname") ? sourcedtl.get("s_accoutname").getAsString() : "";
			 String s_initiatorcif = sourcedtl.has("s_initiatorcif") ? sourcedtl.get("s_initiatorcif").getAsString() : "";
			 String s_email = sourcedtl.has("s_email") ? sourcedtl.get("s_email").getAsString() : "";
			 String s_mobile = sourcedtl.has("s_mobile") ? sourcedtl.get("s_mobile").getAsString() : "";			 
			
			 JsonObject fee = new JsonObject(); 
			 
			 String feeamt = ""; String feecurr = "";
			 
			 if(sourcedtl.has("fee"))
			 {
				  fee = sourcedtl.get("fee").getAsJsonObject();
				  
				  feeamt = fee.get("feeamt").getAsString();
				  feecurr = fee.get("feecurr").getAsString();
			 }
			
			 String invoiceno = billpay.get("invoiceno").getAsString();
			
			 final String REQSL		=  Req_Modal.Generate_Serial().get("Serial").getAsString();							
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
			 String VERSION     = Headers.get("VERSION").getAsString(); 
			 
			 String sql = "Select count(*) from request002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and FLOW=? and REQDATE=? and (REQREFNO=? or TRANREFNO=?)"; 
			 
			 int count = Jdbctemplate.queryForObject(sql, new Object[] { SUBORGCODE, CHCODE, paytype, FLOW, date, reqrefid, tranrefid }, Integer.class);
			 
			 if(count !=0)
			 {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP104");
				  details.addProperty("message", "Invalid Transaction Reference Id !!");
				 
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
			 Info2.setTRANREFNO(tranrefid);
			 Info2.setREQSL(REQSL);					 
			 Info2.setPAYDATE(paydate);
			 Info2.setTRANAMT(tranamt);
			 Info2.setTRANCURR(trancurr);	
			 Info2.setREQTYPE("");			
			 Info2.setSERPORCD(""); 			
			 Info2.setDEBITAMT(tranamt); 		
			 Info2.setDEBITCURR(trancurr); 		
			 Info2.setCREDITAMT(tranamt);	
			 Info2.setCREDITCURR(trancurr);
			 Info2.setS_ACCOUNT(s_accoutno); 			
			 Info2.setD_ACCOUNT("");
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
			 Info2.setD_BRNCODE("");	 		
			 Info2.setS_ACTYPE("");	 		
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
			 Info2.setRECEIVER_INFO("");
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
			 Info2.setINITATEDBY(initiatedby);				 
			 Info2.setFRAUDCHK("");				 
			 Info2.setPSPCODE("");
			 Info2.setSPCODE("");
			 Info2.setSUBSPCODE("");				 
			 Info2.setRECEIPTNO("");		
			 Info2.setAMOUNTTYPE("");
			 Info2.setCALLBACKURL("");
			 Info2.setPAYERID(tranrefid);
			 Info2.setTRANTYPE(trantype);  
			 Info2.setCUSTMSISDN("");
			 Info2.setVERSION(VERSION);			 
				
			 Req_Modal.Insert_Request_002(Info2);
			 
			 logger.debug(">>>>>> Inserted into request 002 <<<<<<<<"); 
			
			 logger.debug("******** Payment Process Started for REF ID "+reqrefid+" ********");
				
			 final String procedureCall = "{CALL PACK_O_PAY_SIMULATOR.PROC_O_REQUEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	 			
	 		 Map<String, Object> resultMap = Jdbctemplate.call(new CallableStatementCreator() {
	 	 
						public CallableStatement createCallableStatement(Connection connection) throws SQLException {
	 
							CallableStatement CS = connection.prepareCall(procedureCall);
							CS.setString(1, SUBORGCODE); 
							CS.setString(2, CHCODE);
							CS.setString(3, paytype);
							CS.setString(4, date);
							CS.setString(5, reqrefid);
							CS.setString(6, REQSL);
							CS.setString(7, MSGTYPE);
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
	 			
	 			final String O_TRANAMOUNT = util.ReplaceNull(resultMap.get("o_tranamount"));
	 			final String O_DBCURR = util.ReplaceNull(resultMap.get("o_dbcurr"));
	 			final String O_CHRGAMOUNT = util.ReplaceNull(resultMap.get("o_chrgamount"));
	 			final String O_CHRGCURR = util.ReplaceNull(resultMap.get("o_chrgcurr"));
	 			final String O_DBACCOUNT = util.ReplaceNull(resultMap.get("o_dbaccount"));
	 			final String O_CRACCOUNT = util.ReplaceNull(resultMap.get("o_craccount"));
	 			final String O_FLOW = util.ReplaceNull(resultMap.get("o_flow"));
	 			final String O_PAYLOAD = util.ReplaceNull(resultMap.get("o_payload")); 
	 			final String O_RESCD = util.ReplaceNull(resultMap.get("o_rescd"));
	 			final String O_RESDESC = util.ReplaceNull(resultMap.get("o_resdesc"));
	 			final String O_STATUS = util.ReplaceNull(resultMap.get("o_status"));
	 			
	 			if(O_STATUS.equalsIgnoreCase("F"))
	 			{
	 				String STATUS = "Failed";
	 				
					Status_Update(CHCODE, paytype, trantype, reqrefid, REQSL, STATUS, O_RESCD, O_RESDESC);
					  
	 				details.addProperty("result", "failed");
					details.addProperty("stscode", O_RESCD);
					details.addProperty("message", O_RESDESC);
					   
				    return details;
	 			}
	 			
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
							CS.setString(1, SUBORGCODE);
							CS.setString(2, CHCODE);
							CS.setString(3, paytype);
							CS.setString(4, O_FLOW);
							CS.setString(5, reqrefid);
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
				
				  sql = "select VERSION from request002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and REQREFNO=? and REQSL=?";
				 
				  List<String> VR = Jdbctemplate.queryForList(sql, new Object[] { SUBORGCODE, CHCODE, paytype, reqrefid, REQSL }, String.class);
								 
				  String DBCurr = util.ReplaceNull(cbsresultMap.get("o_dbcurr"));
				  String Reqcode = util.ReplaceNull(cbsresultMap.get("o_reqcode"));
		 		  String BRNCODE = util.ReplaceNull(cbsresultMap.get("o_brncode"));
			  
		 		  sql = "Select * from transactions where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? order by transeq,legsl";
				 	
		 		  List<Transactions> Transactions_Info = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, CHCODE, SYSCODE, "TRANSFER", paytype, "PENDING", reqrefid, date }, new Transaction_Mapper());
		 	
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
					
					 if(VR.get(0).contains("demo"))
					 {
						 JsonObject API_Response = Simulator.Get_Simulator_Response("", "", SUBORGCODE, SYSCODE, "CBS", "000", "I");  /*** simulator Response ***/
						 
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
	   					 String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? and TRANSEQ=?";
						
	   					 Jdbctemplate.update(Update_Sql, new Object[] { "POSTED", SUBORGCODE, CHCODE, SYSCODE, "TRANSFER", paytype, "PENDING", reqrefid, date, Transeq });
	   					 
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
				
				String token = ""; 
				
				if(err != 0)
				{
					  logger.debug("CBS POSTING for ref id "+reqrefid+" is failed");
				     
					  JsonObject js = Get_CBS_ERRINFO(errormsg);
					  
					  ERRCD = js.get("ERRCD").getAsString();
					  ERRDESC = js.get("ERRDESC").getAsString();
					  
					  STATUS = "Failed";
					 
					  /*** Initiate Internal Reversal CBS Posting ***/
					  
					  if(crt != 0)
					  {
						  Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, paytype, date, reqrefid, REQSL, "", trantype, "", "", "");  
							 
						  JsonObject Reversal_details = Internal_Reversal_Executer(Info3);
					  
					      logger.debug("<<<<<<< Reversal_details >>>>>> "+Reversal_details);
				      }
					  
					  /** Update transfer Status ***/
					  
					  Status_Update(CHCODE, paytype, trantype, reqrefid, REQSL, STATUS, ERRCD, ERRDESC);
					  
					  details.addProperty("result", "failed");
					  details.addProperty("stscode", ERRCD);
					  details.addProperty("message", ERRDESC);
						
					  return details;
			    }
				else
				{
					/*** MSC Token Process ***/
					  
					 JsonObject Token_details = new JsonObject();
					 
					 int Response_code = 0;
					 
					 if(VERSION.equalsIgnoreCase("demo"))  /*** simulator Response Code ***/
					 {
						 Token_details = Simulator.Get_Simulator_Response("", "", SUBORGCODE, SYSCODE, paytype, "999", "O");  /*** simulator Response ***/
						  
						 Response_code = Token_details.get("Response_Code").getAsInt();
						 
						 String res = Token_details.get("Response").getAsString();
						
						 Token_details = util.StringToJsonObject(res);
					 }
					 else
					 {
						 Token_details = Generate_Token();
						 
						 Response_code = Token_details.get("Response_Code").getAsInt();
					 }
 
				     if(Response_code == 200)
				     {
					     token = Token_details.get("access_token").getAsString();
				     }
				     else
				     {
				    	 STATUS = "Failed";
				    	 
				    	 ERRCD = "HP111";			     				
						 ERRDESC = err == 0 ? "CBS Posting is Success, But Payment gateway token generation failed" : "CBS Posting is Failed and Payment gateway token generation failed";	 
						
				    	 logger.debug(">>>>>> "+ERRDESC+" <<<<<<");
				    	 
				    	 /*** Initiate Internal Reversal ***/
				    	 
				    	 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, paytype, date, reqrefid, REQSL, "", trantype, "", "", "");  
						 
						 JsonObject Reversal_details = Internal_Reversal_Executer(Info3);
					  
					     logger.debug("<<<<<<< Reversal_details >>>>>> "+Reversal_details);
					     
					     Status_Update(CHCODE, paytype, trantype, reqrefid, REQSL, STATUS, ERRCD, ERRDESC);
					     
					     logger.debug(">>>>>> Transaction aborted and updated successfully <<<<<<");
				    	 
				    	 details.addProperty("result", "failed");
						 details.addProperty("stscode", ERRCD);
						 details.addProperty("message", ERRDESC);
						 
						 return details;
				   }
			  }
	
		     JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, paytype, MSGTYPE);
		     
		     if(webservice_details.get("Result").getAsString().equals("Failed"))
			 {
		    	 STATUS = "Failed";
		    	 ERRCD = "HP111";			     				
				 ERRDESC = webservice_details.get("Message").getAsString();
				 
				 logger.debug("Error in Get_Webserice_Info :::: "+webservice_details);
				 
				 /*** Initiate Internal Reversal ***/
		    	 
		    	 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, paytype, date, reqrefid, REQSL, "", trantype, "", "", "");  
				 
				 JsonObject Reversal_details = Internal_Reversal_Executer(Info3);
			  
			     logger.debug("<<<<<<< Reversal_details >>>>>> "+Reversal_details);
			     
			     Status_Update(CHCODE, paytype, trantype, reqrefid, REQSL, STATUS, ERRCD, ERRDESC);
			     
			     logger.debug(">>>>>> Transaction aborted and updated successfully <<<<<<");
			     
			     details.addProperty("result", "failed");
				 details.addProperty("stscode", ERRCD);
				 details.addProperty("message", ERRDESC);
				 	  
				 return webservice_details; 
			 }
		     
		     webservice_details.addProperty("PAYLOAD", O_PAYLOAD);
		     
		     String requesturl = webservice_details.get("URI").getAsString(); 
			
	         JsonArray O_Headers = webservice_details.get("Headers").getAsJsonArray();
			  
			 String Headers_str = O_Headers.toString();  
			 
			 Headers_str = Headers_str.replace("~Token~", token);
			 
			 O_Headers = util.StringToJsonArray(Headers_str);
			 
			 webservice_details.addProperty("URI", requesturl);
			 webservice_details.add("Headers", O_Headers);
			
		     String O_ReqRefID  = reqrefid;
		     String O_MSGURL = requesturl; 
		     String O_IP = "";
		     String O_PORT = "";
		     String O_HEAD_MSG = Headers.toString();
		     String O_BODY_MSG = O_PAYLOAD;
		     String O_INITATEDBY = "HPAY";
		     String O_Checksum = "";
		     
		     Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, O_FLOW, O_ReqRefID, O_MSGURL, O_IP, O_PORT, O_HEAD_MSG, O_BODY_MSG, O_INITATEDBY, O_Checksum);
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert outward Request to Request_001 ****/
	
			 JsonObject API_Response = new JsonObject();
		        
	         if(VR.get(0).contains("demo"))
			 {
	        	 API_Response.addProperty("Result", "Success");
		         API_Response.addProperty("Message", "Payment Success");
		         API_Response.addProperty("Response", "Payment Success");
		         API_Response.addProperty("Response_Code", 200);
			 }
	         else
	         {
	        	  API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		/**** Sending API Request ****/
	         }

	         logger.debug("MSC Payment API_Response :::: "+API_Response);
	         
	         Response_001 response = new Response_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, FLOW, reqrefid , requesturl , IP, PORT, HEAD_MSG, API_Response.toString(), paytype, "");
				
		     Res_Modal.Insert_Response_001(response);	 /**** Insert Response to Response_001 ****/	  
		    
		     int Response_Code = API_Response.get("Response_Code").getAsInt();
		     
		     if(Response_Code == 500)
	         {
		    	 STATUS  = "Failed";
		    	 ERRCD   = "HP109";
		    	 ERRDESC = "Biller Gateway Currently Not Available !!";
		    	 
		    	 Status_Update(CHCODE, paytype, trantype, reqrefid, REQSL, STATUS, ERRCD, STATUS);
		    	 
		    	 /*** Initiate Internal Reversal ***/
		    	 
		    	 Job_005 Info3 = new Job_005(SUBORGCODE, SYSCODE, CHCODE, paytype, date, reqrefid, REQSL, "", trantype, "", "", "");  
				 
				 JsonObject Reversal_details = Internal_Reversal_Executer(Info3);
			  
			     logger.debug("<<<<<<< Reversal_details >>>>>> "+Reversal_details);
		    	 
	        	 details.addProperty("result",  "failed"); 
				 details.addProperty("stscode", ERRCD); 
				 details.addProperty("message", ERRDESC); 
	         }
		     else
			 {		
		    	 STATUS  = "Success";
		    	 
		    	 String RESCCD  = "200";
		    	 
	        	 String RESPONSE = API_Response.get("Response").getAsString();
	        	 
	        	 logger.debug("MSC RESPONSE API Response :::: "+RESPONSE); 
	        	
				 Status_Update(CHCODE, paytype, trantype, reqrefid, REQSL, STATUS, RESCCD, STATUS);
	        	 
	        	 details.addProperty("result", "success");
	        	 details.addProperty("stscode", "HP00"); 
			     details.addProperty("message", "Payment processed Successfully !!");
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06"); 
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in MSC Payment_Posting_Request :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Status_Update(String CHCODE, String PAYTYPE, String trantype, String REQREFNO, String REQSL, String STATUS, String ERRCD, String ERRDESC)
	{
		JsonObject details = new JsonObject();
		
		try
		{
			  String sql = "update request002 w set w.STATUS=?, w.RESCODE=?, w.RESPDESC=? where w.CHCODE=? and w.PAYTYPE=? and w.REQREFNO=? and w.REQSL=?";
			   
			  Jdbctemplate.update(sql, new Object[] { STATUS, ERRCD, ERRDESC, CHCODE, PAYTYPE, REQREFNO, REQSL });
				 
			  sql = "update pay001 w set w.STATUS=?, w.RESCODE=? where w.CHCODE=? and w.PAYTYPE=? and w.REQREFNO=? and w.REQSL=?";
			   
			  Jdbctemplate.update(sql, new Object[] { STATUS, ERRCD, CHCODE, PAYTYPE, REQREFNO, REQSL });
			  
			  details.addProperty("result", "success");
	          details.addProperty("stscode", "HP00"); 
	          details.addProperty("message", "Payment Status updated Successfully !!");
		}
		catch(Exception e)
		{
			  details.addProperty("result", "failed");
			  details.addProperty("stscode", "HP06"); 
			  details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in MSC Status_Update :::: "+e.getLocalizedMessage()); 
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
 			logger.debug("REQDATE    :::: "+Job.getREQDATE());
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
						CS.setString(4, Job.getREQDATE());
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
			
 			int err = 0; int crt = 0;
	 		 
			logger.debug("***** Start INTERNAL Reversal CBS Posting ***********");
			
			String sql = "Select * from transactions where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? order by transeq,legsl";
		
			List<Transactions> Transactions_Info = Jdbctemplate.query(sql, new Object[] { Job.getSUBORGCODE(), Job.getCHCODE(), Job.getSYSCODE(), "TRANSFER", Job.getPAYTYPE(), "POSTED", Job.getREFNO(), Job.getREQDATE() }, new Transaction_Mapper());
		
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
				 
				 String BRNCODE = CRBRN + "|" + CRBRN + "|" +DBBRN + "|";
		 
		 		 String errormsg =  base64simulator.formrequestnew(Billamount, Charge, DBCurr, CRAccount, DBAccount, Narration, Reqcode, BRNCODE);
				 
		 		 logger.debug("CBS RESPONSE for Reversal Transeq "+Transeq+" is :::: "+errormsg);  
		 		  		
		 		 if(errormsg.equalsIgnoreCase("S"))
				 {
					  String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? and TRANSEQ=?";
					
					  Jdbctemplate.update(Update_Sql, new Object[] { "REVERSED", Job.getSUBORGCODE(), Job.getCHCODE(), Job.getSYSCODE(), "TRANSFER", Job.getPAYTYPE(), "POSTED", Job.getREFNO(), Job.getREQDATE(), Transeq });
					  
					  crt++;
				 }
		 		 else
		 		 {
		 			  String Update_Sql = "Update transactions SET STATUS=? where SUBORGCODE=? and CHCODE=? and SYSCODE=? and TRANTYPE=? and PAYTYPE=? and STATUS=? and CHREFNO=? and trunc(TRANDATE)=? and TRANSEQ=?";
					
					  Jdbctemplate.update(Update_Sql, new Object[] { "RV_PENDING", Job.getSUBORGCODE(), Job.getCHCODE(), Job.getSYSCODE(), "TRANSFER", Job.getPAYTYPE(), "POSTED", Job.getREFNO(), Job.getREQDATE(), Transeq });
					  
					  err++;
					  
		 			  logger.debug("Error in Reversal CBS POSTING  :::: "+errormsg);
		 		 }
		 		 
		 		 if(err == 0)
		 		 {
		 			  String REV_DATE = util.getCurrentDate("dd-MMM-yyyy");
		        	  
		        	  sql = "update pay002 set ISREVERSED=?, REV_DATE=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQDATE=? and REQSL=?";
					   
		        	  Jdbctemplate.update(sql, new Object[] { "1", REV_DATE, "F", "1", "REVERSED", Job.getCHCODE(), Job.getPAYTYPE(), Job.getREFNO(), Job.getREQDATE(), Job.getREQSL() });
		 		 }
		 		 else if(crt == 0 && err !=0)
		 		 {
		 			 sql = "update pay002 set ISREVERSED=?, REV_TYPE=?, ISFINAL_REV=?, REV_STATUS=? where CHCODE=? and PAYTYPE=? and REQREFNO=? and REQDATE=? and REQSL=?";
					   
		        	 Jdbctemplate.update(sql, new Object[] { "0", "F", "1", "NOT REVERSED", Job.getCHCODE(), Job.getPAYTYPE(), Job.getREFNO(), Job.getREQDATE(), Job.getREQSL() });
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
	
	private JsonObject Generate_Token()
    {
    	JsonObject details = new JsonObject();
    			
    	String SUBORGCODE = "EXIM";
		String CHCODE  = "MSC";  
		String SERVICECD = "999"; 
		String SYSCODE = "HP"; 
		
    	try
    	{
    		String ReqRefID = System.currentTimeMillis()+"";
    		
			Request_001 Inward_Request = new Request_001(SUBORGCODE, SYSCODE, CHCODE, SERVICECD, "I", ReqRefID, "", "", "" , "", "", SYSCODE,  "");
			 
			Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
			 	 
    		JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
    		
    	    if(webservice_details.get("Result").getAsString().equals("Failed"))
			{  
			     logger.debug("MSC Token generation Failed :::: "+webservice_details.toString());
			     
				 return webservice_details; 
			}
    		
    	    String URI = webservice_details.get("URI").getAsString();
    	    
	        JsonArray Headers = webservice_details.get("Headers").getAsJsonArray();
	        
	        JsonObject js = new JsonObject();
	        
	        for(int i=0; i<Headers.size(); i++)
	        {
	        	JsonObject tk = Headers.get(i).getAsJsonObject();
	        	
	        	String Key = tk.get("Key").getAsString();
	        	String Value = tk.get("Value").getAsString();
	        	
	        	js.addProperty(Key, Value);
	        }
	       
    		String authority = URI;
    		String keyPath = js.get("keyPath").getAsString();
    		String certPath = js.get("certPath").getAsString();
    		String clientId = js.get("clientId").getAsString();    		
    		String scope = js.get("scope").getAsString();
    		
    		PrivateKey key = engineGeneratePrivate(keyPath);
            
            InputStream certStream = new ByteArrayInputStream(Files.readAllBytes(Paths.get(certPath)));
            
            X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(certStream);

            ConfidentialClientApplication app = ConfidentialClientApplication.builder(clientId,
                    ClientCredentialFactory.createFromCertificate(key, cert))
                    .authority(authority)
                    .build();
            
            ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(Collections.singleton(scope)).build();

            CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
            
            IAuthenticationResult res = future.get();
            
            String access_token = res.accessToken();
            
            details.addProperty("access_token", access_token);
            
            logger.debug("MSC Token API_Response :::: "+access_token);
            
            Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", Headers.toString(), details.toString(), "HDPAY", "");
			
		    Res_Modal.Insert_Response_001(Response);  /*** Insert outward Response to Response_001 ****/
		       
            details.addProperty("Result", "Succcess");
			details.addProperty("Response_Code", "200");
            details.addProperty("Message", "Token Generated Successfully !!");
    	}
    	catch(Exception e)
        {
    		details.addProperty("Result", "Failed");
    		details.addProperty("Response_Code", "500");
			details.addProperty("Message", e.getLocalizedMessage());  
        }
       
    	return details;
    }
   
    protected static PrivateKey engineGeneratePrivate(String path)
    {
    	PrivateKey key = null;
    	
    	try 
    	{	
    		InputStream inStream = new FileInputStream(path);
        	
        	String body = IOUtils.readInputStreamToString(inStream); 
        	
        	//System.out.println(body);

    		Reader reader = new StringReader(body);
    		    		
    		PemReader pemReader = new PemReader(reader);
    		
    		PemObject pemObject = pemReader.readPemObject();
    		
    		byte[] keyContentAsBytesFromBC = pemObject.getContent();
    		  
    		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyContentAsBytesFromBC);
    		  
    		key = KeyFactory.getInstance("RSA").generatePrivate(spec);
       	} 
    	catch(Exception t) 
    	{
    		  t.printStackTrace();
    	}
    	
    	return key;
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
			 
			 logger.debug("Exception in GEPG Payment_Posting_Validation :::: "+e.getLocalizedMessage());
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
					 
					 String[] trandetails_Members = { "tranrefid" , "tranamt", "trancurr" };
					 String[] sourcedtl_Members = { "s_accoutname" , "s_accoutno", "s_mobile", "s_bracnch" };
					 String[] billpay_Members = { "invoiceno" };
					
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

			 String reqrefid = transfers.get("reqrefid").getAsString();
			 String date = transfers.get("date").getAsString();
			 String paydate = transfers.get("paydate").getAsString();
			 String trantype = transfers.get("trantype").getAsString();
			 String initiatedby = transfers.get("initiatedby").getAsString();
			 
			 String tranrefid = trandetail.get("tranrefid").getAsString();
			 String tranamt = trandetail.get("tranamt").getAsString();
			 String trancurr = trandetail.get("trancurr").getAsString();
			 
			 String s_accoutno = sourcedtl.get("s_accoutno").getAsString();
			 String s_bracnch = sourcedtl.get("s_bracnch").getAsString();
			 String s_mobile = sourcedtl.get("s_mobile").getAsString();
			 
			 String invoiceno = billpay.get("invoiceno").getAsString();
			
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
			 else if(util.isNullOrEmpty(trantype))
			 {
				 Error_Reason = "trantype value should be TRANSFER or CASH";
			 }
			 else if(!trantype.equals("TRANSFER") && !trantype.equals("CASH"))
			 {
				 Error_Reason = "trantype value should be TRANSFER or CASH";
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
			 else if(util.isNullOrEmpty(invoiceno))
			 {
				 Error_Reason = "invoiceno should be not be Empty";
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
			 
			 logger.debug("Exception in MSC Payment_Posting_Validation :::: "+e.getLocalizedMessage());
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
}
