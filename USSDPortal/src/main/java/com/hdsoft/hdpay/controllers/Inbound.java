package com.hdsoft.hdpay.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.common.Token_System;
import com.hdsoft.common.base64simulator;
import com.hdsoft.hdpay.models.Configuration_Modal;
import com.hdsoft.hdpay.models.Inward_Modal;
import com.hdsoft.hdpay.models.Outward_Modal;
import com.hdsoft.hdpay.models.TIPS_Modal;

@Controller
public class Inbound
{
	  @Autowired
	  public Outward_Modal Outward;
	  
	  @Autowired
	  public Inward_Modal Inward;
	  
	  @Autowired
	  public TIPS_Modal TIPS;
	  
	  @Autowired
	  public Configuration_Modal config;
		
	  @Autowired
	  public Token_System tk;
	  
	  private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	  
	  @RequestMapping(value = {"/HDPAY/Token"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String Generate_Token(@RequestHeader("Authorization") String Authorization, @RequestParam("USERID") String USERID, @RequestParam("PASSWORD") String PASSWORD, @RequestParam("CHCODE") String CHCODE, HttpServletRequest request,HttpServletResponse response) 
      {	 
    	  JsonObject details = new JsonObject();  
    	 
    	  details = TIPS.Generate_HDPAY_Token(USERID, PASSWORD, "EXIM", CHCODE, Authorization);
    	
	      return details.toString();
      }
	  
	  @RequestMapping(value = {"/HDPAY/Bill-request"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String Bill_request_GEPG(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "Accept", required=true) String Accept, @RequestHeader(value = "Content-Type", required=true) String Content_Type,
      @RequestHeader(value = "ChannelID", required=true) String ChannelID, @RequestHeader(value = "Authorization", required=true) String Authorization, @RequestHeader(value = "User-Agent", required=false) String User_Agent, @RequestHeader(value = "Longitude", required=false) String Longitude, @RequestHeader(value = "Latitude", required=false) String Latitude,
      @RequestHeader(value = "IPAddress", required=false) String IPAddress, @RequestHeader(value = "DEVICEID", required=false) String DEVICEID, @RequestHeader(value = "Locationcode", required=false) String Locationcode, @RequestHeader(value = "Session_ID", required=false) String Session_ID, @RequestHeader(value = "VERSION", required=true) String VERSION) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Common_Utils util = new Common_Utils();
			 
		  Headers.addProperty("Accept", util.ReplaceNull(Accept));
		  Headers.addProperty("Content-Type", util.ReplaceNull(Content_Type));
		  Headers.addProperty("ChannelID", util.ReplaceNull(ChannelID));
		  Headers.addProperty("Authorization", util.ReplaceNull(Authorization));
		  Headers.addProperty("User-Agent", util.ReplaceNull(User_Agent));
		  Headers.addProperty("Longitude", util.ReplaceNull(Longitude));
		  Headers.addProperty("Latitude", util.ReplaceNull(Latitude));
		  Headers.addProperty("IPAddress", util.ReplaceNull(IPAddress));
		  Headers.addProperty("DEVICEID", util.ReplaceNull(DEVICEID));
		  Headers.addProperty("Locationcode", util.ReplaceNull(Locationcode));
		  Headers.addProperty("Session_ID", util.ReplaceNull(Session_ID));
		  Headers.addProperty("VERSION", util.ReplaceNull(VERSION));
		  
		  logger.debug(">>>>>>>> New Bill Request Received from "+ChannelID+" <<<<<<<<");
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());

		  details =  Inward.Handle_Bill_Request(Body_MSG, Headers, request);
		  
		  logger.debug(">>>>>>>> Response <<<<<<<< "+details);
		 
	      return details.toString();
      } 
	
	  @RequestMapping(value = {"/HDPAY/PAYMENT/POST"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String POST_PAYMENT(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "Accept", required=true) String Accept, @RequestHeader(value = "Content-Type", required=true) String Content_Type,
      @RequestHeader(value = "ChannelID", required=true) String ChannelID, @RequestHeader(value = "Authorization", required=true) String Authorization, @RequestHeader(value = "User-Agent", required=false) String User_Agent, @RequestHeader(value = "Longitude", required=false) String Longitude, @RequestHeader(value = "Latitude", required=false) String Latitude,
      @RequestHeader(value = "IPAddress", required=false) String IPAddress, @RequestHeader(value = "DEVICEID", required=false) String DEVICEID, @RequestHeader(value = "Locationcode", required=false) String Locationcode, @RequestHeader(value = "Session_ID", required=false) String Session_ID, @RequestHeader(value = "VERSION", required=true) String VERSION) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	     
		  Common_Utils util = new Common_Utils();
			 
		  Headers.addProperty("Accept", util.ReplaceNull(Accept));
		  Headers.addProperty("Content-Type", util.ReplaceNull(Content_Type));
		  Headers.addProperty("ChannelID", util.ReplaceNull(ChannelID));
		  Headers.addProperty("Authorization", util.ReplaceNull(Authorization));
		  Headers.addProperty("User-Agent", util.ReplaceNull(User_Agent));
		  Headers.addProperty("Longitude", util.ReplaceNull(Longitude));
		  Headers.addProperty("Latitude", util.ReplaceNull(Latitude));
		  Headers.addProperty("IPAddress", util.ReplaceNull(IPAddress));
		  Headers.addProperty("DEVICEID", util.ReplaceNull(DEVICEID));
		  Headers.addProperty("Locationcode", util.ReplaceNull(Locationcode));
		  Headers.addProperty("Session_ID", util.ReplaceNull(Session_ID));
		  Headers.addProperty("VERSION", util.ReplaceNull(VERSION));
		  
		  logger.debug(">>>>>>>> New Payment Request Received from "+ChannelID+" <<<<<<<<");
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details = Inward.Handle_Payment_Request(Body_MSG, Headers, request);
   	    	 
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details);
		  
	      return details.toString();
      } 
	  
	  @RequestMapping(value = {"/HDPAY/PAYMENT/REVERSAL/POST"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String Payment_Reversal_POST(HttpServletRequest request, @RequestParam(value = "SUBORGCODE") String SUBORGCODE, @RequestParam(value = "CHCODE") String CHCODE, @RequestParam(value = "PAYTYPE") String PAYTYPE, @RequestParam(value = "REQSL") String REQSL, @RequestParam(value = "HOLD_REASON") String HOLD_REASON)  
      {	 
		  JsonObject details = new JsonObject();
		  
		  details = TIPS.Transfer_Reversal_Message_From_Channel(SUBORGCODE, CHCODE, PAYTYPE, REQSL, HOLD_REASON);
   	    	 
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details);
		  
	      return details.toString();
      } 
	  
	  @RequestMapping(value = {"/HDPAY/PAYMENT/ENQUIRY"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String PAYMENT_Enquiry(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "Accept", required=true) String Accept, @RequestHeader(value = "Content-Type", required=true) String Content_Type,
      @RequestHeader(value = "ChannelID", required=true) String ChannelID, @RequestHeader(value = "Authorization", required=true) String Authorization, @RequestHeader(value = "User-Agent", required=false) String User_Agent, @RequestHeader(value = "Longitude", required=false) String Longitude, @RequestHeader(value = "Latitude", required=false) String Latitude,
      @RequestHeader(value = "IPAddress", required=false) String IPAddress, @RequestHeader(value = "DEVICEID", required=false) String DEVICEID, @RequestHeader(value = "Locationcode", required=false) String Locationcode, @RequestHeader(value = "Session_ID", required=false) String Session_ID, @RequestHeader(value = "VERSION", required=true) String VERSION) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	     
		  Common_Utils util = new Common_Utils();
			 
		  Headers.addProperty("Accept", util.ReplaceNull(Accept));
		  Headers.addProperty("Content-Type", util.ReplaceNull(Content_Type));
		  Headers.addProperty("ChannelID", util.ReplaceNull(ChannelID));
		  Headers.addProperty("Authorization", util.ReplaceNull(Authorization));
		  Headers.addProperty("User-Agent", util.ReplaceNull(User_Agent));
		  Headers.addProperty("Longitude", util.ReplaceNull(Longitude));
		  Headers.addProperty("Latitude", util.ReplaceNull(Latitude));
		  Headers.addProperty("IPAddress", util.ReplaceNull(IPAddress));
		  Headers.addProperty("DEVICEID", util.ReplaceNull(DEVICEID));
		  Headers.addProperty("Locationcode", util.ReplaceNull(Locationcode));
		  Headers.addProperty("Session_ID", util.ReplaceNull(Session_ID));
		  Headers.addProperty("VERSION", util.ReplaceNull(VERSION));
		  
		  logger.debug(">>>>>>>> New Payment Enquiry Request Received from "+ChannelID+" <<<<<<<<");
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details = Inward.Handle_Payment_Enquiry_Request(Body_MSG, Headers, request);
   	    	 
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details);
		  
	      return details.toString();
      } 

	  @RequestMapping(value = {"/HDPAY/ACLOOKUP"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String ACLOOKUP(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "Accept", required=true) String Accept, @RequestHeader(value = "Content-Type", required=true) String Content_Type,
      @RequestHeader(value = "ChannelID", required=true) String ChannelID, @RequestHeader(value = "Authorization", required=true) String Authorization, @RequestHeader(value = "User-Agent", required=false) String User_Agent, @RequestHeader(value = "Longitude", required=false) String Longitude, @RequestHeader(value = "Latitude", required=false) String Latitude,
      @RequestHeader(value = "IPAddress", required=false) String IPAddress, @RequestHeader(value = "DEVICEID", required=false) String DEVICEID, @RequestHeader(value = "Locationcode", required=false) String Locationcode, @RequestHeader(value = "Session_ID", required=false) String Session_ID, @RequestHeader(value = "VERSION", required=true) String VERSION) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Common_Utils util = new Common_Utils();
			 
		  Headers.addProperty("Accept", util.ReplaceNull(Accept));
		  Headers.addProperty("Content-Type", util.ReplaceNull(Content_Type));
		  Headers.addProperty("ChannelID", util.ReplaceNull(ChannelID));
		  Headers.addProperty("Authorization", util.ReplaceNull(Authorization));
		  Headers.addProperty("User-Agent", util.ReplaceNull(User_Agent));
		  Headers.addProperty("Longitude", util.ReplaceNull(Longitude));
		  Headers.addProperty("Latitude", util.ReplaceNull(Latitude));
		  Headers.addProperty("IPAddress", util.ReplaceNull(IPAddress));
		  Headers.addProperty("DEVICEID", util.ReplaceNull(DEVICEID));
		  Headers.addProperty("Locationcode", util.ReplaceNull(Locationcode));
		  Headers.addProperty("Session_ID", util.ReplaceNull(Session_ID));
		  Headers.addProperty("VERSION", util.ReplaceNull(VERSION));
		  
		  logger.debug(">>>>>>>> New Ac Lookup Request Received from "+ChannelID+" <<<<<<<<");
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details =  Inward.Handle_Account_Lookup_Request(Body_MSG, Headers, request);
		  
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details);
		 
	      return details.toString();
      } 
	  
	  @RequestMapping(value = {"/HDPAY/ACLOOKUP/ALL"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String ACLOOKUP_ALL(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "Accept", required=true) String Accept, @RequestHeader(value = "Content-Type", required=true) String Content_Type,
      @RequestHeader(value = "ChannelID", required=true) String ChannelID, @RequestHeader(value = "Authorization", required=true) String Authorization, @RequestHeader(value = "User-Agent", required=false) String User_Agent, @RequestHeader(value = "Longitude", required=false) String Longitude, @RequestHeader(value = "Latitude", required=false) String Latitude,
      @RequestHeader(value = "IPAddress", required=false) String IPAddress, @RequestHeader(value = "DEVICEID", required=false) String DEVICEID, @RequestHeader(value = "Locationcode", required=false) String Locationcode, @RequestHeader(value = "Session_ID", required=false) String Session_ID, @RequestHeader(value = "VERSION", required=true) String VERSION) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Common_Utils util = new Common_Utils();
			 
		  Headers.addProperty("Accept", util.ReplaceNull(Accept));
		  Headers.addProperty("Content-Type", util.ReplaceNull(Content_Type));
		  Headers.addProperty("ChannelID", util.ReplaceNull(ChannelID));
		  Headers.addProperty("Authorization", util.ReplaceNull(Authorization));
		  Headers.addProperty("User-Agent", util.ReplaceNull(User_Agent));
		  Headers.addProperty("Longitude", util.ReplaceNull(Longitude));
		  Headers.addProperty("Latitude", util.ReplaceNull(Latitude));
		  Headers.addProperty("IPAddress", util.ReplaceNull(IPAddress));
		  Headers.addProperty("DEVICEID", util.ReplaceNull(DEVICEID));
		  Headers.addProperty("Locationcode", util.ReplaceNull(Locationcode));
		  Headers.addProperty("Session_ID", util.ReplaceNull(Session_ID));
		  Headers.addProperty("VERSION", util.ReplaceNull(VERSION));
		  
		  logger.debug(">>>>>>>> New Ac Lookup Request Received from "+ChannelID+" <<<<<<<<");
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details =  Inward.Handle_All_Account_Lookup_Request(Body_MSG, Headers, request);
		  
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details);
		 
	      return details.toString();
      } 
	  
	  @RequestMapping(value = {"/HDPAY/IDENTIFIER/REGISTER"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String IDENTIFIER_REGISTER(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "Accept", required=true) String Accept, @RequestHeader(value = "Content-Type", required=true) String Content_Type,
      @RequestHeader(value = "ChannelID", required=true) String ChannelID, @RequestHeader(value = "Authorization", required=true) String Authorization, @RequestHeader(value = "User-Agent", required=false) String User_Agent, @RequestHeader(value = "Longitude", required=false) String Longitude, @RequestHeader(value = "Latitude", required=false) String Latitude,
      @RequestHeader(value = "IPAddress", required=false) String IPAddress, @RequestHeader(value = "DEVICEID", required=false) String DEVICEID, @RequestHeader(value = "Locationcode", required=false) String Locationcode, @RequestHeader(value = "Session_ID", required=false) String Session_ID, @RequestHeader(value = "VERSION", required=true) String VERSION) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Common_Utils util = new Common_Utils();
			 
		  Headers.addProperty("Accept", util.ReplaceNull(Accept));
		  Headers.addProperty("Content-Type", util.ReplaceNull(Content_Type));
		  Headers.addProperty("ChannelID", util.ReplaceNull(ChannelID));
		  Headers.addProperty("Authorization", util.ReplaceNull(Authorization));
		  Headers.addProperty("User-Agent", util.ReplaceNull(User_Agent));
		  Headers.addProperty("Longitude", util.ReplaceNull(Longitude));
		  Headers.addProperty("Latitude", util.ReplaceNull(Latitude));
		  Headers.addProperty("IPAddress", util.ReplaceNull(IPAddress));
		  Headers.addProperty("DEVICEID", util.ReplaceNull(DEVICEID));
		  Headers.addProperty("Locationcode", util.ReplaceNull(Locationcode));
		  Headers.addProperty("Session_ID", util.ReplaceNull(Session_ID));
		  Headers.addProperty("VERSION", util.ReplaceNull(VERSION));
		  
		  logger.debug(">>>>>>>> New Identifier Registration Request Received from "+ChannelID+" <<<<<<<<");
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details = Inward.Handle_Identifer_Registration_Request(Body_MSG, Headers, request);
		  
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details);
		 
	      return details.toString();
      } 
	  
	  @RequestMapping(value = {"/HDPAY/IDENTIFIER/UPDATE"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String IDENTIFIER_UPDATE(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "Accept", required=true) String Accept, @RequestHeader(value = "Content-Type", required=true) String Content_Type,
      @RequestHeader(value = "ChannelID", required=true) String ChannelID, @RequestHeader(value = "Authorization", required=true) String Authorization, @RequestHeader(value = "User-Agent", required=false) String User_Agent, @RequestHeader(value = "Longitude", required=false) String Longitude, @RequestHeader(value = "Latitude", required=false) String Latitude,
      @RequestHeader(value = "IPAddress", required=false) String IPAddress, @RequestHeader(value = "DEVICEID", required=false) String DEVICEID, @RequestHeader(value = "Locationcode", required=false) String Locationcode, @RequestHeader(value = "Session_ID", required=false) String Session_ID, @RequestHeader(value = "VERSION", required=true) String VERSION) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Common_Utils util = new Common_Utils();
			 
		  Headers.addProperty("Accept", util.ReplaceNull(Accept));
		  Headers.addProperty("Content-Type", util.ReplaceNull(Content_Type));
		  Headers.addProperty("ChannelID", util.ReplaceNull(ChannelID));
		  Headers.addProperty("Authorization", util.ReplaceNull(Authorization));
		  Headers.addProperty("User-Agent", util.ReplaceNull(User_Agent));
		  Headers.addProperty("Longitude", util.ReplaceNull(Longitude));
		  Headers.addProperty("Latitude", util.ReplaceNull(Latitude));
		  Headers.addProperty("IPAddress", util.ReplaceNull(IPAddress));
		  Headers.addProperty("DEVICEID", util.ReplaceNull(DEVICEID));
		  Headers.addProperty("Locationcode", util.ReplaceNull(Locationcode));
		  Headers.addProperty("Session_ID", util.ReplaceNull(Session_ID));
		  Headers.addProperty("VERSION", util.ReplaceNull(VERSION));
		  
		  logger.debug(">>>>>>>> New Identifier update Request Received from "+ChannelID+" <<<<<<<<");
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details = Inward.Handle_Identifer_Update_Request(Body_MSG, Headers, request);
		  
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details);
		 
	      return details.toString();
      } 
	  
		/*
		 * @RequestMapping(value = {"/HDPAY/CALLBACK/{PAYTYPE}"}, method =
		 * RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces =
		 * MediaType.APPLICATION_XML_VALUE) public @ResponseBody String
		 * GEPG_CALLBACK(@RequestBody String Body_MSG, @PathVariable("PAYTYPE") String
		 * PAYTYPE, HttpServletRequest request) { JsonObject details = new JsonObject();
		 * 
		 * details = Inward.Handle_Callback_Request(PAYTYPE, Body_MSG, request);
		 * 
		 * String XML = new Common_Utils().JsonToXML(details.toString());
		 * 
		 * return XML; }
		 */
	  
	  @RequestMapping(value = {"/HDPAY/Recon"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String Reconsilation_request(@RequestBody String Body_MSG, HttpServletRequest request,@RequestHeader(value = "Accept", required=true) String Accept, @RequestHeader(value = "Content-Type", required=true) String Content_Type,
      @RequestHeader(value = "ChannelID", required=true) String ChannelID, @RequestHeader(value = "Authorization", required=true) String Authorization, @RequestHeader(value = "User-Agent", required=false) String User_Agent, @RequestHeader(value = "Longitude", required=false) String Longitude, @RequestHeader(value = "Latitude", required=false) String Latitude,
      @RequestHeader(value = "IPAddress", required=false) String IPAddress, @RequestHeader(value = "DEVICEID", required=false) String DEVICEID, @RequestHeader(value = "Locationcode", required=false) String Locationcode, @RequestHeader(value = "Session_ID", required=false) String Session_ID, @RequestHeader(value = "VERSION", required=true) String VERSION) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Common_Utils util = new Common_Utils();
			 
		  Headers.addProperty("Accept", util.ReplaceNull(Accept));
		  Headers.addProperty("Content-Type", util.ReplaceNull(Content_Type));
		  Headers.addProperty("ChannelID", util.ReplaceNull(ChannelID));
		  Headers.addProperty("Authorization", util.ReplaceNull(Authorization));
		  Headers.addProperty("User-Agent", util.ReplaceNull(User_Agent));
		  Headers.addProperty("Longitude", util.ReplaceNull(Longitude));
		  Headers.addProperty("Latitude", util.ReplaceNull(Latitude));
		  Headers.addProperty("IPAddress", util.ReplaceNull(IPAddress));
		  Headers.addProperty("DEVICEID", util.ReplaceNull(DEVICEID));
		  Headers.addProperty("Locationcode", util.ReplaceNull(Locationcode));
		  Headers.addProperty("Session_ID", util.ReplaceNull(Session_ID));
		  Headers.addProperty("VERSION", util.ReplaceNull(VERSION));
		  
		  logger.debug(">>>>>>>> New Recon Request Received from "+ChannelID+" <<<<<<<<");
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details =  Inward.Insert_Recon(Body_MSG, Headers, request);
		  
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details);

	      return details.toString();
     }   
	  
	 @RequestMapping(value = {"/HDPAY/CHANNEL/PAYGATEWAYS"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Find_Gateway_Information(@RequestHeader("Authorization") String Authorization, @RequestParam("CHCODE") String CHCODE, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
		 JsonObject details = new JsonObject();
    	   	
		 int flag = tk.ValidateJWTToken("EXIM", CHCODE, Authorization);
		 
		 if(flag == 1)
    	 {
			 details = config.Get_Gateway_Info_by_channel(CHCODE);  
			 
			 details.addProperty("result", details.get("Result").getAsString().equals("Success") ? "success" : "failed");
			 details.addProperty("stscode", details.get("Result").getAsString().equals("Success") ? "HP00" : "HP06"); 
			 details.addProperty("message", details.get("Message").getAsString()); 
    	 }
		 else if(flag == 0)
    	 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06"); 
			 details.addProperty("message", "token expired !!"); 
    	 }
    	 else
    	 {
    		 details.addProperty("result", "failed");
    		 details.addProperty("stscode", "HP107");
    		 details.addProperty("message", "invalid token !!");
    	 }
		 
    	 return details.toString();  	
     }
	  
     @RequestMapping(value = {"/HDPAY/Token/Decode"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Token_validate(@RequestParam("Token") String Token, HttpServletRequest request,HttpServletResponse response) 
     {	 
    	 JsonObject details = new JsonObject();  
    	 
    	 details = tk.decodeJWT(Token); 
		
	     return details.toString();
     }
     
     @RequestMapping(value = {"/HDPAY/Token/Validate"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Token_validate(@RequestHeader("Authorization") String Authorization, @RequestParam("CHCODE") String CHCODE, HttpServletRequest request,HttpServletResponse response) 
     {	 
    	 JsonObject details = new JsonObject();  
    	 
    	 int flag = tk.ValidateJWTToken("EXIM", CHCODE, Authorization);
    	 
    	 if(flag == 1)
    	 {
    		 details.addProperty("Result", "Success");
    		 details.addProperty("Message", "Token Validation Success !!");
    	 }
    	 else if(flag == 0)
    	 {
    		 details.addProperty("Result", "Failed");
    		 details.addProperty("Message", "Token Expired !!");
    	 }
    	 else
    	 {
    		 details.addProperty("Result", "Failed");
    		 details.addProperty("Message", "Invalid Token !!");
    	 }
    	 
	     return details.toString();
     }
     
     @RequestMapping(value = {"/HDPAY/Manual/Reversal"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Token_validate(@RequestHeader("USER_ID") String USER_ID, @RequestHeader("PASSWORD") String PASSWORD, @RequestParam("Billamount") String Billamount, @RequestParam("Charge") String Charge, @RequestParam("DBCurr") String DBCurr, @RequestParam("DBAccount") String DBAccount, @RequestParam("CRAccount") String CRAccount, @RequestParam("Narration") String Narration, @RequestParam("Reqcode") String Reqcode, @RequestParam("Branch") String BRNCODE, HttpServletRequest request,HttpServletResponse response) 
     {	 
    	 JsonObject details = new JsonObject();  
    	 
    	 if(USER_ID.equals("kamal") && PASSWORD.equals("HD005"))
    	 {
    		 String errormsg =  base64simulator.formrequestnew(Billamount, Charge, DBCurr, DBAccount, CRAccount, Narration, Reqcode, BRNCODE);
 	 		
        	 if(errormsg.equalsIgnoreCase("S"))
    		 {
        		 details.addProperty("Result", "Success");
        		 details.addProperty("Stscode", errormsg);
        		 details.addProperty("Message", "CBS POSTING SUCCESS");
    		 }
        	 else
        	 {
        		  details.addProperty("Result", "Failed");
        		  details.addProperty("Stscode", errormsg);	
        		 
        		  if(errormsg.equalsIgnoreCase("0014"))  /** Invalid Account **/
    			  {
        			 details.addProperty("Message", "Invalid Account");	  
    			  }
    			  else if(errormsg.equalsIgnoreCase("0096")) /** Transaction Declined **/
    			  {
    				  details.addProperty("Message", "Transaction Declined");	 
    			  }
    			  else if(errormsg.equalsIgnoreCase("0051")) /** Insufficient Fund **/
    			  {
    				  details.addProperty("Message", "Insufficient Fund");
    			  }
    			  else
    			  {
    				  details.addProperty("Message", "Issue In CBS");
    			  }
        	 }
    	 }
    	 else
    	 {
    		 details.addProperty("Result", "Failed");
    		 details.addProperty("Stscode", "400");	
    		 details.addProperty("Message", "Authentication failed");
    	 }
    	 
	     return details.toString();
     }
}