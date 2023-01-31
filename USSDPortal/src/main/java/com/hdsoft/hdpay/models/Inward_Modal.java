package com.hdsoft.hdpay.models;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.common.Token_System;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Response_001;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Inward_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	@Autowired
	public Reconcilation_Modal Reconcilation;  
	
	@Autowired
	public Request_Modal Req_Modal; 
	
	@Autowired
	public Response_Modal Res_Modal;
	
	@Autowired
	public GEPG_Modal GEPG;
	
	@Autowired
	public MSC_Modal MSC;
	
	@Autowired
	public Airtel_Modal AIRTEL;
	
	@Autowired
	public TIGO_Modal TIGO;
	
	@Autowired
	public BSMART_Modal BSMART;
	
	@Autowired
	public TIPS_Modal TIPS;
	
	@Autowired
	public TRA_Modal TRA;
	
	@Autowired
	public Token_System tk;
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Handle_Payment_Request(String request, JsonObject Headers, HttpServletRequest req)
	{
		 JsonObject details = new JsonObject();
		
		 boolean flag = true;
		 
		 try
		 {
			 Common_Utils util = new Common_Utils();
			 
			 String SUBORGCODE = "EXIM";
			 String CHCODE = Headers.has("ChannelID") ? Headers.get("ChannelID").getAsString() : ""; 
			 String Token = Headers.has("Authorization") ? Headers.get("Authorization").getAsString() : "";

			 int token_validation = tk.ValidateJWTToken(SUBORGCODE, CHCODE, Token);
								
			 if(token_validation != 1)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "400"); 
			     details.addProperty("message", "token validation failed !!");
			    
			     return details;
			 }
			 
			 JsonObject Request = util.StringToJsonObject(request);
			 
			 if(!Request.has("transfers")) 
			 {
				 flag = false;
			 }
			 else
			 {
				 JsonObject transfers = Request.get("transfers").getAsJsonObject();	
				 
				 if(!transfers.has("paytype"))
				 { 
					 flag = false;
				 }
			 }
			 
			 if(!flag)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06"); 
				 details.addProperty("message", "validation failed !!");
				 
				 return details;
			 }
			 
			 JsonObject transfers = Request.get("transfers").getAsJsonObject();	
			 
			 String paytype = transfers.get("paytype").getAsString().toUpperCase(); 
			 
			 if(paytype.equals("GEPG"))
			 {	
				  details = GEPG.Handle_GEPG_Payment_Request(Request, Headers, req);
			 }
			 
			 if(paytype.equals("TRA"))
			 {
				  details = TRA.Handle_TRA_Payment_Request(Request, Headers, req);
			 }
			 
			 if(paytype.equals("MSC"))
			 {
				  details = MSC.Handle_MSC_Payment_Request(Request, Headers, req);
			 }
			 
			 if(paytype.equals("AIRTEL"))
			 {
				  details = AIRTEL.Handle_Airtel_Payment_Request(Request, Headers, req);
			 }
			 
			 if(paytype.equals("TIGO"))
			 {
				  details = TIGO.Handle_TIGO_Payment_Request_From_Channels(Request, Headers, req);
			 }

			 if(paytype.equals("TIPS"))
			 {	
				  details = TIPS.Handle_TIPS_Transfer_Request_From_Channels(Request, Headers, req);
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500"); 
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Payment_Req :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Handle_Payment_Enquiry_Request(String request, JsonObject Headers, HttpServletRequest req)
	{
		 JsonObject details = new JsonObject();
		
		 boolean flag = true;
		 
		 try
		 {
			 Common_Utils util = new Common_Utils();
			 
			 JsonObject Request = util.StringToJsonObject(request);
			 
			 if(!Request.has("transfers"))
			 {
				 flag = false;
			 }
			 else
			 {
				 JsonObject transfers = Request.get("transfers").getAsJsonObject();	
				 
				 if(!transfers.has("paytype") || transfers.get("paytype").getAsString() == null)
				 { 
					 flag = false;
				 }
				 else if(!transfers.has("trandetail"))
				 {
					 flag = false;
				 }
				 else
				 {
					 JsonObject trandetail = transfers.get("trandetail").getAsJsonObject();	
					 
					 if(!trandetail.has("tranrefid"))
					 {
						 flag = false;
					 }
				 }
			 }
			 
			 if(!flag)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06"); 
				 details.addProperty("message", "validation failed !!");
				 
				 return details;
			 }
			 
			 JsonObject transfers = Request.get("transfers").getAsJsonObject();	
			 
			 String PAYTYPE = transfers.get("paytype").getAsString().toUpperCase(); 
			 
			 if(PAYTYPE.equals("GEPG"))
			 {	
				 //details = GEPG.Handle_GEPG_Payment_Request(Request, Headers, req);
			 }
			 
			 if(PAYTYPE.equals("TIPS"))
			 {	
				 String SUBORGCODE = "EXIM";
				 String SYSCODE = "HP";
				 String CHCODE = Headers.get("ChannelID").getAsString(); 
				 String Token = Headers.get("Authorization").getAsString();
				 
				 int token_validation = tk.ValidateJWTToken(SUBORGCODE, CHCODE, Token);
									
				 if(token_validation != 1)
				 {
					 details.addProperty("result", "failed");
					 details.addProperty("stscode", "400"); 
				     details.addProperty("message", "token validation failed !!");
				    
				     return details;
				 }
				
				 details = TIPS.TIPS_Transfer_Enquiry(SUBORGCODE, SYSCODE, CHCODE, Request);
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500"); 
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Payment_Req :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Handle_Bill_Request(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		boolean flag = true;
		
		try
		{
			JsonObject Request = new Common_Utils().StringToJsonObject(request);
			
			String SUBORGCODE = "EXIM";
			String CHCODE = Headers.has("ChannelID") ? Headers.get("ChannelID").getAsString() : ""; 
			String Token = Headers.has("Authorization") ? Headers.get("Authorization").getAsString() : "";

			int token_validation = tk.ValidateJWTToken(SUBORGCODE, CHCODE, Token);
								
			if(token_validation != 1)
			{
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "400"); 
			     details.addProperty("message", "token validation failed !!");
			    
			     return details;
			}
			 
			if(!Request.has("getbill"))
			{
				flag = false;
			}
			else
			{
				JsonObject getbill = Request.get("getbill").getAsJsonObject();	
				
				if(!getbill.has("reqrefid") || !getbill.has("initiatedby") || !getbill.has("paytype"))
				{
					flag = false;
				}
			}
			
			 if(!flag)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06"); 
			     details.addProperty("message", "validation failed !!");
			    
			     return details;
			 }
			
			 JsonObject GETBILL = Request.get("getbill").getAsJsonObject();	
				
			 String ReqRefID = GETBILL.get("reqrefid").getAsString();
			 String initiatedby = GETBILL.get("initiatedby").getAsString();
			 String PAYTYPE = GETBILL.get("paytype").getAsString(); 
			 
			 if(PAYTYPE.equals("GEPG"))
			 {	
				 String SYSCODE = "HP";  String SERVICECD = "001"; 
				 
				 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), PAYTYPE, SERVICECD, "I", ReqRefID, "/HDPAY/Bill-request", Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Request.toString(), initiatedby,  "");
				 
				 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
				 				
				 details =  GEPG.Get_GEPG_Bill_Details(request, Headers, req);	   /*** Calling GEPG BILL Function ****/
				
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), PAYTYPE, SERVICECD, "I", ReqRefID , "/HDPAY/Bill-request" , Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			  }
			 
			 if(PAYTYPE.equals("MSC"))
			 {
				 String SYSCODE = "HP";  String SERVICECD = "001"; 
				 
				 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), PAYTYPE, SERVICECD, "I", ReqRefID, "/HDPAY/Bill-request", Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Request.toString(), initiatedby,  "");
				 
				 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
				 				
				 details =  MSC.Get_MSC_Bill_Details(request, Headers, req);	   /*** Calling GEPG BILL Function ****/
				
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), PAYTYPE, SERVICECD, "I", ReqRefID , "/HDPAY/Bill-request" , Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			 }
			 
			 if(PAYTYPE.equals("BSMART"))
			 {
				 String SYSCODE = "HP";  String SERVICECD = "001"; 
				 
				 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), PAYTYPE, SERVICECD, "I", ReqRefID, "/HDPAY/Bill-request", Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Request.toString(), initiatedby,  "");
				 
				 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
				 				
				 details = BSMART.Get_BSMART_Bill_Details(request, Headers, req);	   /*** Calling GEPG BILL Function ****/
				
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), PAYTYPE, SERVICECD, "I", ReqRefID , "/HDPAY/Bill-request" , Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			 }
			 
			 if(PAYTYPE.equals("TRA"))
			 {
				 details = TRA.Get_TRA_Bill_Details(request, Headers, req);	   /*** Calling GEPG BILL Function ****/
			 }
		}
		catch(Exception e)
		{
			details.addProperty("result", "failed");
			details.addProperty("stscode", "HP06"); 
			details.addProperty("message", e.getLocalizedMessage());  	
			
			logger.debug("Exception in Handle_Bill_Request :::: "+e.getLocalizedMessage()); 
		}
		
		 return details;	
	}
	
	public JsonObject Handle_Account_Lookup_Request(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		boolean flag = true;
		
		try
		{
			 String SUBORGCODE = "EXIM";
			 String CHCODE = Headers.get("ChannelID").getAsString(); 
			 String Token = Headers.get("Authorization").getAsString();

			 int token_validation = tk.ValidateJWTToken(SUBORGCODE, CHCODE, Token);
				
			 if(token_validation != 1)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP107"); 
			     details.addProperty("message", "Token Validation Failed !!");
			    
			     return details;
			 }
			 
			 Common_Utils utils = new Common_Utils();
			 
			 JsonObject Request = utils.StringToJsonObject(request);
			 
			 if(!Request.has("aclookup"))
			 {
				 flag = false;
			 }
			 else
			 {
				 JsonObject aclookup = Request.get("aclookup").getAsJsonObject();	
				
				 String[] Members = new String[] { "primaryarg", "paytype", "date", "reqrefid", "initiatedby" };
				
				 if(!utils.JsonMemberNullChecker(aclookup, Members))
				 {
					flag = false;
				 }
			 }
			
			 if(!flag)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06"); 
			     details.addProperty("message", "validation failed !!");
			    
			     return details;
			 }
			
			 JsonObject aclookup = Request.get("aclookup").getAsJsonObject();	
				
			 String paytype = aclookup.get("paytype").getAsString();
			
			 if(paytype.equals("TIPS"))
			 {						
				  details = TIPS.Get_Account_Lookup_Details(request, Headers, req);	  
			 }
			 
			 if(paytype.equals("AIRTEL"))
			 {						
				  details = AIRTEL.Get_Account_Lookup_Details(request, Headers, req);	  
			 }
			 
			 if(paytype.equals("TIGO"))
			 {						
				  details =  TIGO.Get_Account_Lookup_Details(request, Headers, req);	  
			 }
		}
		catch(Exception e)
		{
			details.addProperty("result", "failed");
			details.addProperty("stscode", "HP06"); 
			details.addProperty("message", e.getLocalizedMessage());  	
			
			logger.debug("Exception in Handle_Account_Lookup_Request :::: "+e.getLocalizedMessage()); 
		}
		
		 return details;	
	}
	
	public JsonObject Handle_All_Account_Lookup_Request(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		boolean flag = true;
		
		try
		{
			 String SUBORGCODE = "EXIM";
			 String CHCODE = Headers.get("ChannelID").getAsString(); 
			 String Token = Headers.get("Authorization").getAsString();

			 int token_validation = tk.ValidateJWTToken(SUBORGCODE, CHCODE, Token);
				
			 if(token_validation != 1)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP107"); 
			     details.addProperty("message", "token validation failed !!");
			    
			     return details;
			 }
			 
			JsonObject Request = new Common_Utils().StringToJsonObject(request);
			 
			if(!Request.has("aclookup"))
			{
				flag = false;
			}
			else
			{
				JsonObject aclookup = Request.get("aclookup").getAsJsonObject();	
				
				if(!aclookup.has("primaryarg") || !aclookup.has("paytype") || !aclookup.has("date") || !aclookup.has("reqrefid") || !aclookup.has("initiatedby"))
				{
					flag = false;
				}
				else
				{
					JsonObject PRIMARYARG = aclookup.get("primaryarg").getAsJsonObject();	
					
					if(!PRIMARYARG.has("bankcode") || !PRIMARYARG.has("identifiertype") || !PRIMARYARG.get("identifiertype").getAsString().equals("ALIAS"))
					{
						flag = false;
					}
				}
			}
			
			 if(!flag)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06"); 
			     details.addProperty("message", "validation failed !!");
			    
			     return details;
			 }
			
			 JsonObject aclookup = Request.get("aclookup").getAsJsonObject();	
				
			 String paytype = aclookup.get("paytype").getAsString();
			 String reqrefid = aclookup.get("reqrefid").getAsString();
			 String initiatedby = aclookup.get("initiatedby").getAsString();
			 
			 if(paytype.equals("TIPS"))
			 {	
				 String SYSCODE = "HP";  String SERVICECD = "2005"; 
				 
				 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), paytype, SERVICECD, "I", reqrefid, "/HDPAY/ACLOOKUP/ALL", Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Request.toString(), initiatedby,  "");
				 
				 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
				 				
				 details =  TIPS.Get_All_Account_Lookup_Details(request, Headers, req);	   /*** Calling GEPG BILL Function ****/
				
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), paytype, SERVICECD, "I", reqrefid , "/HDPAY/ACLOOKUP/ALL" , Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			  }
		}
		catch(Exception e)
		{
			details.addProperty("result", "failed");
			details.addProperty("stscode", "500"); 
			details.addProperty("message", e.getLocalizedMessage());  	
			
			logger.debug("Exception in Handle_Bill_Request :::: "+e.getLocalizedMessage()); 
		}
		
		 return details;	
	}
	
	public JsonObject Handle_Identifer_Registration_Request(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		boolean flag = true;
		
		try
		{
			 String SUBORGCODE = "EXIM";
			 String CHCODE = Headers.get("ChannelID").getAsString(); 
			 String Token = Headers.get("Authorization").getAsString();

			 int token_validation = tk.ValidateJWTToken(SUBORGCODE, CHCODE, Token);
			
			 if(token_validation != 1)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP107"); 
			     details.addProperty("message", "token validation failed !!");
			    
			     return details;
			 }
	    	 
			JsonObject Request = new Common_Utils().StringToJsonObject(request);
			
			if(!Request.has("identifierreg"))
			{
				flag = false; 
			}
			else
			{
				JsonObject IdentifierReg = Request.get("identifierreg").getAsJsonObject();	
				
				if(!IdentifierReg.has("primaryarg") || !IdentifierReg.has("paytype") || !IdentifierReg.has("date") || !IdentifierReg.has("reqrefid") || !IdentifierReg.has("initiatedby"))
				{
					flag = false;  
				}
				else
				{
					JsonObject primaryarg = IdentifierReg.get("primaryarg").getAsJsonObject();
					
					if(!primaryarg.has("acno") || !primaryarg.has("identifier") || !primaryarg.has("identifiertype") || !primaryarg.has("fullname") || !primaryarg.has("bankcode"))
					{
						flag = false;
					}
				}
			}
			
			 if(!flag)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06"); 
			     details.addProperty("message", "Validation Failed !!");
			    
			     return details;
			 } 
			
			 JsonObject Identifierreg = Request.get("identifierreg").getAsJsonObject();	
				
			 String paytype = Identifierreg.get("paytype").getAsString();
			 String reqrefid = Identifierreg.get("reqrefid").getAsString();
			 String initiatedby = Identifierreg.get("initiatedby").getAsString();
			 
			 if(paytype.equals("TIPS"))
			 {	
				 String SYSCODE = "HP";  String SERVICECD = "2000"; 
				 
				 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), paytype, SERVICECD, "I", reqrefid, "/HDPAY/IDENTIFIER/REGISTER", Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Request.toString(), initiatedby,  "");
				 
				 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
				 				
				 details =  TIPS.Register_Identifier_details(request, Headers, req);	   /*** Calling GEPG BILL Function ****/
				
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), paytype, SERVICECD, "I", reqrefid , "/HDPAY/IDENTIFIER/REGISTER" , Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			  }
			  else
			  {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP06"); 
				  details.addProperty("message", "invalid payment type");  
			  }
		}
		catch(Exception e)
		{
			details.addProperty("result", "failed");
			details.addProperty("stscode", "500"); 
			details.addProperty("message", e.getLocalizedMessage());  	
			
			logger.debug("Exception in Handle_Bill_Request :::: "+e.getLocalizedMessage()); 
		}
		
		 return details;	
	}
	
	public JsonObject Handle_Identifer_Update_Request(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		boolean flag = true;
		
		try
		{
			 String SUBORGCODE = "EXIM";
			 String CHCODE = Headers.get("ChannelID").getAsString(); 
			 String Token = Headers.get("Authorization").getAsString();

			 int token_validation = tk.ValidateJWTToken(SUBORGCODE, CHCODE, Token);
							
			 if(token_validation != 1)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP107"); 
			     details.addProperty("message", "token validation failed !!");
			    
			     return details;
			 }
			 
			JsonObject Request = new Common_Utils().StringToJsonObject(request);
			 
			if(!Request.has("identifierupdate"))
			{
				flag = false;
			}
			else
			{
				JsonObject identifierupdate = Request.get("identifierupdate").getAsJsonObject();	
				
				if(!identifierupdate.has("primaryarg") || !identifierupdate.has("paytype") || !identifierupdate.has("date") || !identifierupdate.has("reqrefid") || !identifierupdate.has("initiatedby"))
				{
					flag = false;
				}
				else
				{
					JsonObject primaryarg = identifierupdate.get("primaryarg").getAsJsonObject();
					
					if(!primaryarg.has("acno") || !primaryarg.has("identifier"))
					{
						flag = false;
					}
				}
			}
			
			 if(!flag)
			 {
				 details.addProperty("result", "failed");
				 details.addProperty("stscode", "HP06"); 
			     details.addProperty("message", "validation failed !!");
			    
			     return details;
			 } 
			
			 JsonObject identifierupdate = Request.get("identifierupdate").getAsJsonObject();	
				
			 String paytype = identifierupdate.get("paytype").getAsString();
			 String reqrefid = identifierupdate.get("reqrefid").getAsString();
			 String initiatedby = identifierupdate.get("initiatedby").getAsString();
			 
			 if(paytype.equals("TIPS"))
			 {	
				 String SYSCODE = "HP";  String SERVICECD = "2001"; 
				 
				 Request_001 Inward_Request = new Request_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), paytype, SERVICECD, "I", reqrefid, "/HDPAY/IDENTIFIER/UPDATE", Headers.get("IPAddress").getAsString(), req.getRemotePort()+"" , Headers.toString(), Request.toString(), initiatedby,  "");
				 
				 Req_Modal.Insert_Request_001(Inward_Request);     /*** Insert Inward Request to Request_001 ****/
				 				
				 details =  TIPS.Update_Identifier_details(request, Headers, req);	   /*** Calling GEPG BILL Function ****/
				
				 Response_001 Inward_Response = new Response_001(SUBORGCODE, Headers.get("ChannelID").getAsString(), paytype, SERVICECD, "I", reqrefid , "/HDPAY/IDENTIFIER/UPDATE" , Headers.get("IPAddress").getAsString(), req.getRemotePort()+"", Headers.toString(), details.toString(), SYSCODE, "");
					
			     Res_Modal.Insert_Response_001(Inward_Response);   /*** Insert Inward Response to Response_001 ****/
			  }
			  else
			  {
				  details.addProperty("result", "failed");
				  details.addProperty("stscode", "HP06"); 
				  details.addProperty("message", "invalid payment type");  
			  }
		}
		catch(Exception e)
		{
			details.addProperty("result", "failed");
			details.addProperty("stscode", "500"); 
			details.addProperty("message", e.getLocalizedMessage());  	
			
			logger.debug("Exception in Handle_Bill_Request :::: "+e.getLocalizedMessage()); 
		}
		
		return details;	
	}
	
	
	
	public JsonObject Insert_Recon(String request, JsonObject Headers, HttpServletRequest req)
	{
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 details = Reconcilation.Insert_Recon_Request(request, Headers, req);
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06"); 
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Recon :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
}
