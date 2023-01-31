package com.hdsoft.hdpay.models;

import java.util.HashMap;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.common.Token_System;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Response_001;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class TIPS_Modal_Direct 
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
	
	/*************************************  Direct HDPAY to TIPS API For Postman  *************************************/
	
	public JsonObject Generate_Token(JsonObject Headers, String User_Id, String Password)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "999"; 
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			 
			 if(User_Id == null || Password == null || !User_Id.trim().equals("exim") || !Password.trim().equals("exim@123"))
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Invalid Credentials !!");
				 
				 return details;
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
			 
			 String ChannelID = Headers.has("ChannelID") ? Headers.get("ChannelID").getAsString() : "";
			 String IPAddress = Headers.has("IPAddress") ? Headers.get("IPAddress").getAsString() : "";
			
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, ChannelID, CHCODE, SERVICECD, "I", ReqRefID, "", IPAddress, "" , Headers.toString(), "", "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
			 	 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, ChannelID, CHCODE, SERVICECD, "I", ReqRefID , "", IPAddress, "", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
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
	         
	         logger.debug("Request :::: "+webservice_details);
	         logger.debug("params :::: "+params);
	         
	         String URI = webservice_details.get("URI").getAsString();  
	         String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();	         
		           
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, Params.toString(), "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/

	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("Token API_Response :::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);  /*** Insert outward Response to Response_001 ****/
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, ChannelID, CHCODE, SERVICECD, "I", ReqRefID , "", IPAddress, "", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Generate_Token :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Check_Health(String Type, JsonObject Headers, HttpServletRequest req, String Token)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "900";    
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			
			 if(Type.equals("Transfer")) 
			 {
				 SERVICECD = "800";
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
			
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID, util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), "", "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
			 	 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
			 
			 String Outward_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
			 
			 Outward_Headers = Outward_Headers.replace("~Token~", Token);
		
			 webservice_details.add("Headers", util.StringToJsonArray(Outward_Headers));
			 
			 logger.debug("Request :::: "+webservice_details);
			 
			 String URI = webservice_details.get("URI").getAsString();    
			 String BODYMSG = webservice_details.get("PAYLOAD").getAsString();
			 
		     String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
		              
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, BODYMSG, "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/
			 
	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("API_Response :::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Check_Health :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Register_Identifiers(String Body_MSG, JsonObject Headers, HttpServletRequest req, String Token)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "1000";    
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			
			 JsonObject Request = util.StringToJsonObject(Body_MSG);
			 
			 if(!Request.has("PRIMARY") || !Request.has("SECONDARY") || !Request.has("SYNC"))
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Invalid Request Body"); 
				 
				 return details;
			 }
			 
			 String SYNC = Request.get("SYNC").getAsString();
			 
			 if(SYNC.equals("1")) 
			 {
				 SERVICECD = "2000";
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
			
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID, util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Body_MSG, "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
			 	 
			 JsonObject PRIMARY = Request.get("PRIMARY").getAsJsonObject();
			 JsonObject SECONDARY = Request.get("SECONDARY").getAsJsonObject();
			 			 
			 String identifier = PRIMARY.has("identifier") ? PRIMARY.get("identifier").getAsString() : "";
			 String subIdentifier = PRIMARY.has("subIdentifier") ? PRIMARY.get("subIdentifier").getAsString() : "";
			 String identifierType = PRIMARY.has("identifierType") ? PRIMARY.get("identifierType").getAsString() : "";
			 
			 String requestid = SECONDARY.has("requestid") ? SECONDARY.get("requestid").getAsString() : "";
			 String date = SECONDARY.has("date") ? SECONDARY.get("date").getAsString() : "";
			 String x_forwarded_for = SECONDARY.has("x-forwarded-for") ? SECONDARY.get("x-forwarded-for").getAsString() : "";
			 String fsp_source = SECONDARY.has("fsp-source") ? SECONDARY.get("fsp-source").getAsString() : "";
			 String fsp_destination = SECONDARY.has("fsp-destination") ? SECONDARY.get("fsp-destination").getAsString() : "";
			 String fsp_encryption = SECONDARY.has("fsp-encryption") ? SECONDARY.get("fsp-encryption").getAsString() : "";
			 String fsp_signature = SECONDARY.has("fsp-signature") ? SECONDARY.get("fsp-signature").getAsString() : "";
			 String fsp_uri = SECONDARY.has("fsp-uri") ? SECONDARY.get("fsp-uri").getAsString() : "";
			 String fsp_http_method = SECONDARY.has("fsp-http-method") ? SECONDARY.get("fsp-http-method").getAsString() : "";
			 	 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
			 
			 String URI = webservice_details.get("URI").getAsString();
			 
			 URI = URI.replace("~identifierType~", identifierType);
			 
			 webservice_details.addProperty("URI", URI);
			 
			 String Outward_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
			 
			 Outward_Headers = Outward_Headers.replace("~Token~", Token);
			 Outward_Headers = Outward_Headers.replace("~requestid~", requestid);
			 Outward_Headers = Outward_Headers.replace("~date~", date);
			 Outward_Headers = Outward_Headers.replace("~x-forwarded-for~", x_forwarded_for);
			 Outward_Headers = Outward_Headers.replace("~fsp-source~", fsp_source);
			 Outward_Headers = Outward_Headers.replace("~fsp-destination~", fsp_destination);
			 Outward_Headers = Outward_Headers.replace("~fsp-encryption~", fsp_encryption);
			 Outward_Headers = Outward_Headers.replace("~fsp-signature~", fsp_signature);
			 Outward_Headers = Outward_Headers.replace("~fsp-uri~", fsp_uri);
			 Outward_Headers = Outward_Headers.replace("~fsp-http-method~", fsp_http_method);
			 Outward_Headers = Outward_Headers.replace("~content-length~", Headers.get("Content-Length").getAsString());
			 
			 webservice_details.add("Headers", util.StringToJsonArray(Outward_Headers));
			 
			 String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
			 
			 PAYLOAD = PAYLOAD.replace("~identifier~", identifier);
			 PAYLOAD = PAYLOAD.replace("~subIdentifier~", subIdentifier);
			
			 webservice_details.addProperty("PAYLOAD", PAYLOAD);
			 
			 logger.debug("Request :::: "+webservice_details);
			 
			 URI = webservice_details.get("URI").getAsString();    
			 String BODYMSG = webservice_details.get("PAYLOAD").getAsString();
			 
		     String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
		              
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, BODYMSG, "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/
			 
	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("API_Response :::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Check_Health :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Update_Identifiers(String Body_MSG, JsonObject Headers, HttpServletRequest req, String Token)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "1001";    
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			
			 JsonObject Request = util.StringToJsonObject(Body_MSG);
			 
			 if(!Request.has("PRIMARY") || !Request.has("SECONDARY") || !Request.has("SYNC"))
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Invalid Request Body"); 
				 
				 return details;
			 }
			 
			 String SYNC = Request.get("SYNC").getAsString();
			 
			 if(SYNC.equals("1")) 
			 {
				 SERVICECD = "2001";
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
				
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID, util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Body_MSG, "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/

			 JsonObject PRIMARY = Request.get("PRIMARY").getAsJsonObject();
			 JsonObject SECONDARY = Request.get("SECONDARY").getAsJsonObject();
			 	 
			 String identifier = PRIMARY.has("identifier") ? PRIMARY.get("identifier").getAsString() : "";
			 String identifierType = PRIMARY.has("identifierType") ? PRIMARY.get("identifierType").getAsString() : "";
			 
			 String requestid = SECONDARY.has("requestid") ? SECONDARY.get("requestid").getAsString() : "";
			 String date = SECONDARY.has("date") ? SECONDARY.get("date").getAsString() : "";
			 String x_forwarded_for = SECONDARY.has("x-forwarded-for") ? SECONDARY.get("x-forwarded-for").getAsString() : "";
			 String fsp_source = SECONDARY.has("fsp-source") ? SECONDARY.get("fsp-source").getAsString() : "";
			 String fsp_destination = SECONDARY.has("fsp-destination") ? SECONDARY.get("fsp-destination").getAsString() : "";
			 String fsp_encryption = SECONDARY.has("fsp-encryption") ? SECONDARY.get("fsp-encryption").getAsString() : "";
			 String fsp_signature = SECONDARY.has("fsp-signature") ? SECONDARY.get("fsp-signature").getAsString() : "";
			 String fsp_uri = SECONDARY.has("fsp-uri") ? SECONDARY.get("fsp-uri").getAsString() : "";
			 String fsp_http_method = SECONDARY.has("fsp-http-method") ? SECONDARY.get("fsp-http-method").getAsString() : "";
			  
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
			 
			 String URI = webservice_details.get("URI").getAsString();
			 
			 URI = URI.replace("~identifierType~", identifierType);
			 URI = URI.replace("~identifier~", identifier);
			
			 webservice_details.addProperty("URI", URI);
			 
			 String Outward_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
			 
			 Outward_Headers = Outward_Headers.replace("~Token~", Token);
			 Outward_Headers = Outward_Headers.replace("~requestid~", requestid);
			 Outward_Headers = Outward_Headers.replace("~date~", date);
			 Outward_Headers = Outward_Headers.replace("~x-forwarded-for~", x_forwarded_for);
			 Outward_Headers = Outward_Headers.replace("~fsp-source~", fsp_source);
			 Outward_Headers = Outward_Headers.replace("~fsp-destination~", fsp_destination);
			 Outward_Headers = Outward_Headers.replace("~fsp-encryption~", fsp_encryption);
			 Outward_Headers = Outward_Headers.replace("~fsp-signature~", fsp_signature);
			 Outward_Headers = Outward_Headers.replace("~fsp-uri~", fsp_uri);
			 Outward_Headers = Outward_Headers.replace("~fsp-http-method~", fsp_http_method);
			 Outward_Headers = Outward_Headers.replace("~content-length~", Headers.get("Content-Length").getAsString());
			 
			 webservice_details.add("Headers", util.StringToJsonArray(Outward_Headers));
			 
			 logger.debug("Request :::: "+webservice_details);
			 
			 URI = webservice_details.get("URI").getAsString();    
			 String BODYMSG = webservice_details.get("PAYLOAD").getAsString();
			 
		     String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
		              
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, BODYMSG, "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/
			 
	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("API_Response:::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Update_Identifiers :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Retrieve_Identifiers(String Body_MSG, JsonObject Headers, HttpServletRequest req, String Token)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "1003";    
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			 	
			 JsonObject Request = new Common_Utils().StringToJsonObject(Body_MSG);
			 
			 if(!Request.has("PRIMARY") || !Request.has("SECONDARY") || !Request.has("SYNC"))
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Invalid Request Body"); 
				 
				 return details;
			 }
			 		
			 String SYNC = Request.get("SYNC").getAsString();
			 
			 if(SYNC.equals("1")) 
			 {
				 SERVICECD = "2003";
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
				
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID, util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Body_MSG, "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/

			 JsonObject PRIMARY = Request.get("PRIMARY").getAsJsonObject();
			 JsonObject SECONDARY = Request.get("SECONDARY").getAsJsonObject();

			 String identifier = PRIMARY.has("identifier") ? PRIMARY.get("identifier").getAsString() : "";
			 String subIdentifier = PRIMARY.has("subIdentifier") ? PRIMARY.get("subIdentifier").getAsString() : "";
			 String identifierType = PRIMARY.has("identifierType") ? PRIMARY.get("identifierType").getAsString() : "";
			 String status = PRIMARY.has("status") ? PRIMARY.get("status").getAsString() : "";
			 
			 String LookupId = SECONDARY.has("LookupId") ? SECONDARY.get("LookupId").getAsString() : "";
			 String requestid = SECONDARY.has("requestid") ? SECONDARY.get("requestid").getAsString() : "";
			 String date = SECONDARY.has("date") ? SECONDARY.get("date").getAsString() : "";
			 String x_forwarded_for = SECONDARY.has("x-forwarded-for") ? SECONDARY.get("x-forwarded-for").getAsString() : "";
			 String fsp_source = SECONDARY.has("fsp-source") ? SECONDARY.get("fsp-source").getAsString() : "";
			 String fsp_destination = SECONDARY.has("fsp-destination") ? SECONDARY.get("fsp-destination").getAsString() : "";
			 String fsp_encryption = SECONDARY.has("fsp-encryption") ? SECONDARY.get("fsp-encryption").getAsString() : "";
			 String fsp_signature = SECONDARY.has("fsp-signature") ? SECONDARY.get("fsp-signature").getAsString() : "";
			 String fsp_uri = SECONDARY.has("fsp-uri") ? SECONDARY.get("fsp-uri").getAsString() : "";
			 String fsp_http_method = SECONDARY.has("fsp-http-method") ? SECONDARY.get("fsp-http-method").getAsString() : "";
 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
			 
			 String URI = webservice_details.get("URI").getAsString();
			 
			 URI = URI.replace("~identifierType~", identifierType);
			 URI = URI.replace("~identifier~", identifier);
			
			 webservice_details.addProperty("URI", URI);
			 
			 String Outward_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
			 
			 Outward_Headers = Outward_Headers.replace("~Token~", Token);
			 Outward_Headers = Outward_Headers.replace("~LookupId~", LookupId);
			 Outward_Headers = Outward_Headers.replace("~requestid~", requestid);
			 Outward_Headers = Outward_Headers.replace("~date~", date);
			 Outward_Headers = Outward_Headers.replace("~x-forwarded-for~", x_forwarded_for);
			 Outward_Headers = Outward_Headers.replace("~fsp-source~", fsp_source);
			 Outward_Headers = Outward_Headers.replace("~fsp-destination~", fsp_destination);
			 Outward_Headers = Outward_Headers.replace("~fsp-encryption~", fsp_encryption);
			 Outward_Headers = Outward_Headers.replace("~fsp-signature~", fsp_signature);
			 Outward_Headers = Outward_Headers.replace("~fsp-uri~", fsp_uri);
			 Outward_Headers = Outward_Headers.replace("~fsp-http-method~", fsp_http_method);
			 Outward_Headers = Outward_Headers.replace("~content-length~", Headers.get("Content-Length").getAsString());
			 
			 webservice_details.add("Headers", util.StringToJsonArray(Outward_Headers));
			 
			 String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
			 
			 PAYLOAD = PAYLOAD.replace("~identifier~", identifier);
			 PAYLOAD = PAYLOAD.replace("~subIdentifier~", subIdentifier);
			 PAYLOAD = PAYLOAD.replace("~status~", status);
			 
			 webservice_details.addProperty("PAYLOAD", PAYLOAD);
			 
			 logger.debug("Request :::: "+webservice_details);
			 
			 URI = webservice_details.get("URI").getAsString();    
			 String BODYMSG = webservice_details.get("PAYLOAD").getAsString();
			 
		     String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
		              
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, BODYMSG, "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/
			 
	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("API_Response :::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Retrieve_Identifiers :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Account_Lookup(String Body_MSG, JsonObject Headers, HttpServletRequest req, String Token)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "1004";    
		String SYSCODE = "HP"; 
		
		try
		{		 
			 Common_Utils util = new Common_Utils();
			 	
			 JsonObject Request = util.StringToJsonObject(Body_MSG);
			 
			 if(!Request.has("PRIMARY") || !Request.has("SECONDARY") || !Request.has("SYNC"))
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Invalid Request Body"); 
				 
				 return details;
			 }
			 
			 String SYNC = Request.get("SYNC").getAsString();
			 
			 if(SYNC.equals("1")) 
			 {
				 SERVICECD = "2004";
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
				
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID, util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Body_MSG, "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request); 
			 
			 JsonObject PRIMARY = Request.get("PRIMARY").getAsJsonObject();
			 JsonObject SECONDARY = Request.get("SECONDARY").getAsJsonObject();
			 
			 String identifier = PRIMARY.has("identifier") ? PRIMARY.get("identifier").getAsString() : "";
			 String identifierType = PRIMARY.has("identifierType") ? PRIMARY.get("identifierType").getAsString() : "";
			 
			 String LookupId = SECONDARY.has("LookupId") ? SECONDARY.get("LookupId").getAsString() : "";
			 String requestid = SECONDARY.has("requestid") ? SECONDARY.get("requestid").getAsString() : "";
			 String date = SECONDARY.has("date") ? SECONDARY.get("date").getAsString() : "";
			 String x_forwarded_for = SECONDARY.has("x-forwarded-for") ? SECONDARY.get("x-forwarded-for").getAsString() : "";
			 String fsp_source = SECONDARY.has("fsp-source") ? SECONDARY.get("fsp-source").getAsString() : "";
			 String fsp_destination = SECONDARY.has("fsp-destination") ? SECONDARY.get("fsp-destination").getAsString() : "";
			 String fsp_encryption = SECONDARY.has("fsp-encryption") ? SECONDARY.get("fsp-encryption").getAsString() : "";
			 String fsp_signature = SECONDARY.has("fsp-signature") ? SECONDARY.get("fsp-signature").getAsString() : "";
			 String fsp_uri = SECONDARY.has("fsp-uri") ? SECONDARY.get("fsp-uri").getAsString() : "";
			 String fsp_http_method = SECONDARY.has("fsp-http-method") ? SECONDARY.get("fsp-http-method").getAsString() : "";
				 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
			 
			 String URI = webservice_details.get("URI").getAsString();
			 
			 URI = URI.replace("~identifierType~", identifierType);
			 URI = URI.replace("~identifier~", identifier);
			
			 webservice_details.addProperty("URI", URI);
			 
			 String Outward_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
			 
			 Outward_Headers = Outward_Headers.replace("~Token~", Token);
			 Outward_Headers = Outward_Headers.replace("~LookupId~", LookupId);
			 Outward_Headers = Outward_Headers.replace("~requestid~", requestid);
			 Outward_Headers = Outward_Headers.replace("~date~", date);
			 Outward_Headers = Outward_Headers.replace("~x-forwarded-for~", x_forwarded_for);
			 Outward_Headers = Outward_Headers.replace("~fsp-source~", fsp_source);
			 Outward_Headers = Outward_Headers.replace("~fsp-destination~", fsp_destination);
			 Outward_Headers = Outward_Headers.replace("~fsp-encryption~", fsp_encryption);
			 Outward_Headers = Outward_Headers.replace("~fsp-signature~", fsp_signature);
			 Outward_Headers = Outward_Headers.replace("~fsp-uri~", fsp_uri);
			 Outward_Headers = Outward_Headers.replace("~fsp-http-method~", fsp_http_method);
			 Outward_Headers = Outward_Headers.replace("~content-length~", Headers.get("Content-Length").getAsString());
			 
			 webservice_details.add("Headers", util.StringToJsonArray(Outward_Headers));
			 
			 logger.debug("Request :::: "+webservice_details);
			 
			 URI = webservice_details.get("URI").getAsString();    
			 String BODYMSG = webservice_details.get("PAYLOAD").getAsString();
			 
		     String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
		              
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, BODYMSG, "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/
			 
	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("API_Response :::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Account_Lookup :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Account_Lookup2(String Body_MSG, JsonObject Headers, HttpServletRequest req, String Token)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "1005";    
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			 
			 JsonObject Request = util.StringToJsonObject(Body_MSG);
			 
			 if(!Request.has("PRIMARY") || !Request.has("SECONDARY"))
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Invalid Request Body"); 
				 
				 return details;
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
				
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID, util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Body_MSG, "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request); 
 
			 JsonObject PRIMARY = Request.get("PRIMARY").getAsJsonObject();
			 JsonObject SECONDARY = Request.get("SECONDARY").getAsJsonObject();
			 
			 String identifier = PRIMARY.has("identifier") ? PRIMARY.get("identifier").getAsString() : "";
			 String identifierType = PRIMARY.has("identifierType") ? PRIMARY.get("identifierType").getAsString() : "";
			 String fspId = PRIMARY.has("fspId") ? PRIMARY.get("fspId").getAsString() : "";
			 String fullName = PRIMARY.has("fullName") ? PRIMARY.get("fullName").getAsString() : "";
			 String accountCategory = PRIMARY.has("accountCategory") ? PRIMARY.get("accountCategory").getAsString() : "";
			 String accountType = PRIMARY.has("accountType") ? PRIMARY.get("accountType").getAsString() : "";
			 String type = PRIMARY.has("type") ? PRIMARY.get("type").getAsString() : "";
			 String value = PRIMARY.has("value") ? PRIMARY.get("value").getAsString() : "";
			 
			 String LookupId = SECONDARY.has("LookupId") ? SECONDARY.get("LookupId").getAsString() : "";
			 String requestid = SECONDARY.has("requestid") ? SECONDARY.get("requestid").getAsString() : "";
			 String date = SECONDARY.has("date") ? SECONDARY.get("date").getAsString() : "";
			 String x_forwarded_for = SECONDARY.has("x-forwarded-for") ? SECONDARY.get("x-forwarded-for").getAsString() : "";
			 String fsp_source = SECONDARY.has("fsp-source") ? SECONDARY.get("fsp-source").getAsString() : "";
			 String fsp_destination = SECONDARY.has("fsp-destination") ? SECONDARY.get("fsp-destination").getAsString() : "";
			 String fsp_encryption = SECONDARY.has("fsp-encryption") ? SECONDARY.get("fsp-encryption").getAsString() : "";
			 String fsp_signature = SECONDARY.has("fsp-signature") ? SECONDARY.get("fsp-signature").getAsString() : "";
			 String fsp_uri = SECONDARY.has("fsp-uri") ? SECONDARY.get("fsp-uri").getAsString() : "";
			 String fsp_http_method = SECONDARY.has("fsp-http-method") ? SECONDARY.get("fsp-http-method").getAsString() : "";			 
					 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
			 
			 String URI = webservice_details.get("URI").getAsString();
			 
			 URI = URI.replace("~identifierType~", identifierType);
			 URI = URI.replace("~identifier~", identifier);
			
			 webservice_details.addProperty("URI", URI);
			 
			 String Outward_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
			 
			 Outward_Headers = Outward_Headers.replace("~Token~", Token);
			 Outward_Headers = Outward_Headers.replace("~LookupId~", LookupId);
			 Outward_Headers = Outward_Headers.replace("~requestid~", requestid);
			 Outward_Headers = Outward_Headers.replace("~date~", date);
			 Outward_Headers = Outward_Headers.replace("~x-forwarded-for~", x_forwarded_for);
			 Outward_Headers = Outward_Headers.replace("~fsp-source~", fsp_source);
			 Outward_Headers = Outward_Headers.replace("~fsp-destination~", fsp_destination);
			 Outward_Headers = Outward_Headers.replace("~fsp-encryption~", fsp_encryption);
			 Outward_Headers = Outward_Headers.replace("~fsp-signature~", fsp_signature);
			 Outward_Headers = Outward_Headers.replace("~fsp-uri~", fsp_uri);
			 Outward_Headers = Outward_Headers.replace("~fsp-http-method~", fsp_http_method);
			 Outward_Headers = Outward_Headers.replace("~content-length~", Headers.get("Content-Length").getAsString());
			 
			 webservice_details.add("Headers", util.StringToJsonArray(Outward_Headers));
			 
			 String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
			 
			 PAYLOAD = PAYLOAD.replace("~identifier~", identifier);
			 PAYLOAD = PAYLOAD.replace("~identifierType~", identifierType);
			 PAYLOAD = PAYLOAD.replace("~fspId~", fspId);
			 PAYLOAD = PAYLOAD.replace("~fullName~", fullName);
			 PAYLOAD = PAYLOAD.replace("~accountCategory~", accountCategory);
			 PAYLOAD = PAYLOAD.replace("~accountType~", accountType);
			 PAYLOAD = PAYLOAD.replace("~type~", type);
			 PAYLOAD = PAYLOAD.replace("~value~", value);
			 
			 webservice_details.addProperty("PAYLOAD", PAYLOAD);
 	
			 logger.debug("Request :::: "+webservice_details);
			 
			 URI = webservice_details.get("URI").getAsString();    
			 String BODYMSG = webservice_details.get("PAYLOAD").getAsString();
			 
		     String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
		              
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, BODYMSG, "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/
			 
	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("API_Response :::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Account_Lookup2 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Account_Lookup_Error(String Body_MSG, JsonObject Headers, HttpServletRequest req, String Token)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "1006";    
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
		 	
			 JsonObject Request = util.StringToJsonObject(Body_MSG);
			 
			 if(!Request.has("PRIMARY") || !Request.has("SECONDARY"))
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Invalid Request Body"); 
				 
				 return details;
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
				
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID, util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Body_MSG, "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request);
			 
			 JsonObject PRIMARY = Request.get("PRIMARY").getAsJsonObject();
			 JsonObject SECONDARY = Request.get("SECONDARY").getAsJsonObject();
			 
			 String identifier = PRIMARY.has("identifier") ? PRIMARY.get("identifier").getAsString() : "";
			 String identifierType = PRIMARY.has("identifierType") ? PRIMARY.get("identifierType").getAsString() : "";
			 String errorCode = PRIMARY.has("errorCode") ? PRIMARY.get("errorCode").getAsString() : "";
			 String errorDescription = PRIMARY.has("errorDescription") ? PRIMARY.get("errorDescription").getAsString() : "";
			
			 String LookupId = SECONDARY.has("LookupId") ? SECONDARY.get("LookupId").getAsString() : "";
			 String requestid = SECONDARY.has("requestid") ? SECONDARY.get("requestid").getAsString() : "";
			 String date = SECONDARY.has("date") ? SECONDARY.get("date").getAsString() : "";
			 String x_forwarded_for = SECONDARY.has("x-forwarded-for") ? SECONDARY.get("x-forwarded-for").getAsString() : "";
			 String fsp_source = SECONDARY.has("fsp-source") ? SECONDARY.get("fsp-source").getAsString() : "";
			 String fsp_destination = SECONDARY.has("fsp-destination") ? SECONDARY.get("fsp-destination").getAsString() : "";
			 String fsp_encryption = SECONDARY.has("fsp-encryption") ? SECONDARY.get("fsp-encryption").getAsString() : "";
			 String fsp_signature = SECONDARY.has("fsp-signature") ? SECONDARY.get("fsp-signature").getAsString() : "";
			 String fsp_uri = SECONDARY.has("fsp-uri") ? SECONDARY.get("fsp-uri").getAsString() : "";
			 String fsp_http_method = SECONDARY.has("fsp-http-method") ? SECONDARY.get("fsp-http-method").getAsString() : "";
				 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
			 
			 String URI = webservice_details.get("URI").getAsString();
			 
			 URI = URI.replace("~identifierType~", identifierType);
			 URI = URI.replace("~identifier~", identifier);
			
			 webservice_details.addProperty("URI", URI);
			 
			 String Outward_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
			 
			 Outward_Headers = Outward_Headers.replace("~Token~", Token);
			 Outward_Headers = Outward_Headers.replace("~LookupId~", LookupId);
			 Outward_Headers = Outward_Headers.replace("~requestid~", requestid);
			 Outward_Headers = Outward_Headers.replace("~date~", date);
			 Outward_Headers = Outward_Headers.replace("~x-forwarded-for~", x_forwarded_for);
			 Outward_Headers = Outward_Headers.replace("~fsp-source~", fsp_source);
			 Outward_Headers = Outward_Headers.replace("~fsp-destination~", fsp_destination);
			 Outward_Headers = Outward_Headers.replace("~fsp-encryption~", fsp_encryption);
			 Outward_Headers = Outward_Headers.replace("~fsp-signature~", fsp_signature);
			 Outward_Headers = Outward_Headers.replace("~fsp-uri~", fsp_uri);
			 Outward_Headers = Outward_Headers.replace("~fsp-http-method~", fsp_http_method);
			 Outward_Headers = Outward_Headers.replace("~content-length~", Headers.get("Content-Length").getAsString());
			 
			 webservice_details.add("Headers", util.StringToJsonArray(Outward_Headers));
			 
			 String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
			 
			 PAYLOAD = PAYLOAD.replace("~identifier~", identifier);
			 PAYLOAD = PAYLOAD.replace("~identifierType~", identifierType);
			 PAYLOAD = PAYLOAD.replace("~errorCode~", errorCode);
			 PAYLOAD = PAYLOAD.replace("~errorDescription~", errorDescription);
			 
			 webservice_details.addProperty("PAYLOAD", PAYLOAD);
 	
			 logger.debug("Request :::: "+webservice_details);
			 
			 URI = webservice_details.get("URI").getAsString();    
			 String BODYMSG = webservice_details.get("PAYLOAD").getAsString();
			 
		     String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
		              
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, BODYMSG, "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/
			 
	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("API_Response :::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Account_Lookup_Error :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Send_Transfer(String Body_MSG, JsonObject Headers, HttpServletRequest req, String Token)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "1110";    
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			 
			 JsonObject Request = util.StringToJsonObject(Body_MSG);
			 
			 if(!Request.has("PRIMARY") || !Request.has("SECONDARY"))
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Invalid Request Body"); 
				 
				 return details;
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
				
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID, util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Body_MSG, "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request);

			 JsonObject PRIMARY = Request.get("PRIMARY").getAsJsonObject();
			 JsonObject SECONDARY = Request.get("SECONDARY").getAsJsonObject();

			 String LookupId = SECONDARY.has("LookupId") ? SECONDARY.get("LookupId").getAsString() : "";
			 String requestid = SECONDARY.has("requestid") ? SECONDARY.get("requestid").getAsString() : "";
			 String date = SECONDARY.has("date") ? SECONDARY.get("date").getAsString() : "";
			 String x_forwarded_for = SECONDARY.has("x-forwarded-for") ? SECONDARY.get("x-forwarded-for").getAsString() : "";
			 String fsp_source = SECONDARY.has("fsp-source") ? SECONDARY.get("fsp-source").getAsString() : "";
			 String fsp_destination = SECONDARY.has("fsp-destination") ? SECONDARY.get("fsp-destination").getAsString() : "";
			 String fsp_encryption = SECONDARY.has("fsp-encryption") ? SECONDARY.get("fsp-encryption").getAsString() : "";
			 String fsp_signature = SECONDARY.has("fsp-signature") ? SECONDARY.get("fsp-signature").getAsString() : "";
			 String fsp_uri = SECONDARY.has("fsp-uri") ? SECONDARY.get("fsp-uri").getAsString() : "";
			 String fsp_http_method = SECONDARY.has("fsp-http-method") ? SECONDARY.get("fsp-http-method").getAsString() : "";
					 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
			  
			 String Outward_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
			 
			 Outward_Headers = Outward_Headers.replace("~Token~", Token);
			 Outward_Headers = Outward_Headers.replace("~LookupId~", LookupId);
			 Outward_Headers = Outward_Headers.replace("~requestid~", requestid);
			 Outward_Headers = Outward_Headers.replace("~date~", date);
			 Outward_Headers = Outward_Headers.replace("~x-forwarded-for~", x_forwarded_for);
			 Outward_Headers = Outward_Headers.replace("~fsp-source~", fsp_source);
			 Outward_Headers = Outward_Headers.replace("~fsp-destination~", fsp_destination);
			 Outward_Headers = Outward_Headers.replace("~fsp-encryption~", fsp_encryption);
			 Outward_Headers = Outward_Headers.replace("~fsp-signature~", fsp_signature);
			 Outward_Headers = Outward_Headers.replace("~fsp-uri~", fsp_uri);
			 Outward_Headers = Outward_Headers.replace("~fsp-http-method~", fsp_http_method);
			 Outward_Headers = Outward_Headers.replace("~content-length~", Headers.get("Content-Length").getAsString());
			 
			 webservice_details.add("Headers", util.StringToJsonArray(Outward_Headers));
			 
			 String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
			 
			 PAYLOAD = PRIMARY.toString();
			 
			 webservice_details.addProperty("PAYLOAD", PAYLOAD);
 	
			 logger.debug("Request :::: "+webservice_details);
			 
			 String URI = webservice_details.get("URI").getAsString();    
			 String BODYMSG = webservice_details.get("PAYLOAD").getAsString();
			 
		     String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
		              
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, BODYMSG, "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/
			 
	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("API_Response :::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Send_Transfer :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Transfer_Confirmation(String Body_MSG, JsonObject Headers, HttpServletRequest req, String Token)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "1111";    
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			 
			 JsonObject Request = util.StringToJsonObject(Body_MSG);
			 
			 if(!Request.has("PRIMARY") || !Request.has("SECONDARY"))
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Invalid Request Body"); 
				 
				 return details;
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
				
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID, util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Body_MSG, "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request);

			 JsonObject PRIMARY = Request.get("PRIMARY").getAsJsonObject();
			 JsonObject SECONDARY = Request.get("SECONDARY").getAsJsonObject();
			 
			 String payeeRef = PRIMARY.has("payeeRef") ? PRIMARY.get("payeeRef").getAsString() : "";
			 String payerRef = PRIMARY.has("payerRef") ? PRIMARY.get("payerRef").getAsString() : "";
			 String transferState = PRIMARY.has("transferState") ? PRIMARY.get("transferState").getAsString() : "";
			 String reasonCode = PRIMARY.has("reasonCode") ? PRIMARY.get("reasonCode").getAsString() : "";
			
			 String requestid = SECONDARY.has("requestid") ? SECONDARY.get("requestid").getAsString() : "";
			 String date = SECONDARY.has("date") ? SECONDARY.get("date").getAsString() : "";
			 String x_forwarded_for = SECONDARY.has("x-forwarded-for") ? SECONDARY.get("x-forwarded-for").getAsString() : "";
			 String fsp_source = SECONDARY.has("fsp-source") ? SECONDARY.get("fsp-source").getAsString() : "";
			 String fsp_destination = SECONDARY.has("fsp-destination") ? SECONDARY.get("fsp-destination").getAsString() : "";
			 String fsp_encryption = SECONDARY.has("fsp-encryption") ? SECONDARY.get("fsp-encryption").getAsString() : "";
			 String fsp_signature = SECONDARY.has("fsp-signature") ? SECONDARY.get("fsp-signature").getAsString() : "";
			 String fsp_uri = SECONDARY.has("fsp-uri") ? SECONDARY.get("fsp-uri").getAsString() : "";
			 String fsp_http_method = SECONDARY.has("fsp-http-method") ? SECONDARY.get("fsp-http-method").getAsString() : "";
 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
			 
			 String URI = webservice_details.get("URI").getAsString();
			 
			 URI = URI.replace("~payerRef~", payerRef);
			
			 webservice_details.addProperty("URI", URI);
			 
			 String Outward_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
			 
			 Outward_Headers = Outward_Headers.replace("~Token~", Token);
			 Outward_Headers = Outward_Headers.replace("~requestid~", requestid);
			 Outward_Headers = Outward_Headers.replace("~date~", date);
			 Outward_Headers = Outward_Headers.replace("~x-forwarded-for~", x_forwarded_for);
			 Outward_Headers = Outward_Headers.replace("~fsp-source~", fsp_source);
			 Outward_Headers = Outward_Headers.replace("~fsp-destination~", fsp_destination);
			 Outward_Headers = Outward_Headers.replace("~fsp-encryption~", fsp_encryption);
			 Outward_Headers = Outward_Headers.replace("~fsp-signature~", fsp_signature);
			 Outward_Headers = Outward_Headers.replace("~fsp-uri~", fsp_uri);
			 Outward_Headers = Outward_Headers.replace("~fsp-http-method~", fsp_http_method);
			 Outward_Headers = Outward_Headers.replace("~content-length~", Headers.get("Content-Length").getAsString());
			 
			 webservice_details.add("Headers", util.StringToJsonArray(Outward_Headers));
			 
			 String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
			 
			 PAYLOAD = PAYLOAD.replace("~payeeRef~", payeeRef);
			 PAYLOAD = PAYLOAD.replace("~transferState~", transferState);
			 PAYLOAD = PAYLOAD.replace("~reasonCode~", reasonCode);
			
			 webservice_details.addProperty("PAYLOAD", PAYLOAD);
 	
			 logger.debug("Request :::: "+webservice_details);
			   
			 String BODYMSG = webservice_details.get("PAYLOAD").getAsString();
			 
		     String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
		              
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, BODYMSG, "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/
			 
	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("API_Response :::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Transfer_Confirmation :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Transfer_Enquiry(String Body_MSG, JsonObject Headers, HttpServletRequest req, String Token)
	{
		JsonObject details = new JsonObject();  
		
		String SUBORGCODE = "EXIM";
		String CHCODE  = "TIPS";  
		String SERVICECD = "1114";    
		String SYSCODE = "HP"; 
		
		try
		{
			 Common_Utils util = new Common_Utils();
			 	
			 JsonObject Request = util.StringToJsonObject(Body_MSG);
			 
			 if(!Request.has("PRIMARY") || !Request.has("SECONDARY"))
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Invalid Request Body"); 
				 
				 return details;
			 }
			 
			 String ReqRefID = System.currentTimeMillis()+"";
				
			 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID, util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Body_MSG, "HDPAY",  "");
			 
			 Req_Modal.Insert_Request_001(Inward_Request);
			 
			 JsonObject PRIMARY = Request.get("PRIMARY").getAsJsonObject();
			 JsonObject SECONDARY = Request.get("SECONDARY").getAsJsonObject();
			
			 String payerRef = PRIMARY.has("payerRef") ? PRIMARY.get("payerRef").getAsString() : "";
			 
			 String requestid = SECONDARY.has("requestid") ? SECONDARY.get("requestid").getAsString() : "";
			 String date = SECONDARY.has("date") ? SECONDARY.get("date").getAsString() : "";
			 String x_forwarded_for = SECONDARY.has("x-forwarded-for") ? SECONDARY.get("x-forwarded-for").getAsString() : "";
			 String fsp_source = SECONDARY.has("fsp-source") ? SECONDARY.get("fsp-source").getAsString() : "";
			 String fsp_destination = SECONDARY.has("fsp-destination") ? SECONDARY.get("fsp-destination").getAsString() : "";
			 String fsp_encryption = SECONDARY.has("fsp-encryption") ? SECONDARY.get("fsp-encryption").getAsString() : "";
			 String fsp_signature = SECONDARY.has("fsp-signature") ? SECONDARY.get("fsp-signature").getAsString() : "";
			 String fsp_uri = SECONDARY.has("fsp-uri") ? SECONDARY.get("fsp-uri").getAsString() : "";
			 String fsp_http_method = SECONDARY.has("fsp-http-method") ? SECONDARY.get("fsp-http-method").getAsString() : "";
				 
			 JsonObject webservice_details = Ws.Get_Webserice_Info(SYSCODE, CHCODE, SERVICECD);
			 
			 if(!webservice_details.get("Result").getAsString().equals("Success"))
			 {  
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), webservice_details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			     
				 return webservice_details; 
			 }
			 
			 String URI = webservice_details.get("URI").getAsString();
			 
			 URI = URI.replace("~payerRef~", payerRef);
			
			 webservice_details.addProperty("URI", URI);
			 
			 String Outward_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
			 
			 Outward_Headers = Outward_Headers.replace("~Token~", Token);
			 Outward_Headers = Outward_Headers.replace("~requestid~", requestid);
			 Outward_Headers = Outward_Headers.replace("~date~", date);
			 Outward_Headers = Outward_Headers.replace("~x-forwarded-for~", x_forwarded_for);
			 Outward_Headers = Outward_Headers.replace("~fsp-source~", fsp_source);
			 Outward_Headers = Outward_Headers.replace("~fsp-destination~", fsp_destination);
			 Outward_Headers = Outward_Headers.replace("~fsp-encryption~", fsp_encryption);
			 Outward_Headers = Outward_Headers.replace("~fsp-signature~", fsp_signature);
			 Outward_Headers = Outward_Headers.replace("~fsp-uri~", fsp_uri);
			 Outward_Headers = Outward_Headers.replace("~fsp-http-method~", fsp_http_method);
			 Outward_Headers = Outward_Headers.replace("~content-length~", Headers.get("Content-Length").getAsString());
			 
			 webservice_details.add("Headers", util.StringToJsonArray(Outward_Headers));
			
			 logger.debug("Request :::: "+webservice_details);
			 
			 String BODYMSG = webservice_details.get("PAYLOAD").getAsString();
			 
		     String O_Headers = webservice_details.get("Headers").getAsJsonArray().toString();
		              
	         Request_001 Request_001 = new Request_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID, URI, "", "", O_Headers, BODYMSG, "HDPAY", "");
			 
			 Req_Modal.Insert_Request_001(Request_001);     /*** Insert Inward Request to Request_001 ****/
			 
	         JsonObject API_Response = Wsc.Okhttp_Send_Rest_Request(webservice_details); 		
	         
	         logger.debug("API_Response :::: "+API_Response);
	         
	         details =  API_Response;
	         
	         Response_001 Response = new Response_001(SUBORGCODE, CHCODE, CHCODE, SERVICECD, "O", ReqRefID , URI , "", "", O_Headers, API_Response.toString(), "HDPAY", "");
				
		     Res_Modal.Insert_Response_001(Response);
		     
		     Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), CHCODE, SERVICECD, "I", ReqRefID , util.current_Url(req), Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
				
		     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  

			 logger.debug("Exception in Transfer_Enquiry :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
}
