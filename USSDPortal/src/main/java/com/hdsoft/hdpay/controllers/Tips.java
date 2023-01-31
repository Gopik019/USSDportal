package com.hdsoft.hdpay.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.JsonObject;
import com.hdsoft.common.Menu_Generation;
import com.hdsoft.hdpay.models.Session_Model;
import com.hdsoft.hdpay.Repositories.Identifiers001;
import com.hdsoft.hdpay.models.Inward_Modal;
import com.hdsoft.hdpay.models.TIPS_Modal;
import com.hdsoft.hdpay.models.TIPS_Modal_Direct;
import com.zaxxer.hikari.HikariDataSource;

@Controller
public class Tips
{
	 public JdbcTemplate Jdbctemplate;
	
	 @Autowired
	 public void setJdbctemplate(HikariDataSource Datasource) 
	 {
		Jdbctemplate = new JdbcTemplate(Datasource);
	 }
	
	  @Autowired
	  public TIPS_Modal TIPS;
	  
	  @Autowired
	  public TIPS_Modal_Direct TIPS2;
	  
	  @Autowired
	  public Inward_Modal Inward;
	  
	  @Autowired
	  public Menu_Generation MG;
	  
	  private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	  
	  @RequestMapping(value = { "/HDPAY/TIPS/Identifier-Registration" }, method = RequestMethod.GET)
	  public ModelAndView Identifier_Registration(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
	  {	 
		     ModelAndView mv = new ModelAndView();
		     
		     if(Session_Model.IsSessionValid(session))
		     {
		    	 mv.setViewName("HDPAY/TIPS/Identifier_Registration"); 
		    	 
		    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "IDENTIFIER", "IDENTREG"));
		     }
		     else
		     {
		    	 mv.setViewName("redirect:/HDPAY/login");
		     }
		     
		     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		     response.setHeader("Pragma","no-cache");
		     response.setHeader("Expires","0");
	
		     return mv;
	  }
	  
	  @RequestMapping(value = { "/HDPAY/TIPS/Bulk/Identifier-Registration" }, method = RequestMethod.GET)
	  public ModelAndView Identifier_Registration_bulk(Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
	  {	 
		     ModelAndView mv = new ModelAndView();
		     
		     if(Session_Model.IsSessionValid(session))
		     {
		    	 mv.setViewName("HDPAY/TIPS/Identifiers_Bulk_Upload");  
		    	 
		    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "IDENTIFIER", "IDENTRBULK"));
		     }
		     else
		     {
		    	 mv.setViewName("redirect:/HDPAY/login");
		     }
		     
		     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		     response.setHeader("Pragma","no-cache");
		     response.setHeader("Expires","0");
	
		     return mv;
	  }
	  
	  @RequestMapping(value = { "/HDPAY/TIPS/Account/Details" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody String Account_Information(@RequestParam("Ac_No") String Ac_No, @RequestParam("Mobile_No") String Mobile_No, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
	  {	 
		  	 JsonObject details = new JsonObject();
		     
		     if(Session_Model.IsSessionValid(session))
		     {
		    	 details = TIPS.Retrieve_Account_Information(Ac_No, Mobile_No);
		     }
		     else
	    	 {
	    		 details.addProperty("result", "failed");
				 details.addProperty("message", "Please re-login for security concerns");
	    	 }
		     
		     return details.toString();
	  }
	  
	  @RequestMapping(value = { "/HDPAY/TIPS/Identifier/Availability" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody String Identifier_Availability(@RequestParam("Ac_No") String Ac_No, @RequestParam("Mobile_No") String Mobile_No, @RequestParam("Identifier") String Identifier, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
	  {	 
		  	 JsonObject details = new JsonObject();
		     
		     if(Session_Model.IsSessionValid(session))
		     {
		    	 details = TIPS.Retrieve_Account_Information(Ac_No, Mobile_No);
		     }
		     else
	    	 {
	    		 details.addProperty("result", "failed");
				 details.addProperty("message", "Please re-login for security concerns");
	    	 }
		     
		     return details.toString();
	  }
	  
	  @RequestMapping(value = { "/HDPAY/TIPS/Identifier/View" }, method = RequestMethod.GET)
	  public ModelAndView Configuration_Index(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
	  {	 
		     ModelAndView mv = new ModelAndView();
		     
		     if(Session_Model.IsSessionValid(session))
		     {
		    	 mv.setViewName("HDPAY/TIPS/View_Identifiers");  
		    	 
		    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "IDENTIFIER", "IDENTRVIEW"));
		     }
		     else
		     {
		    	 mv.setViewName("redirect:/HDPAY/login");
		     }
		     
		     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		     response.setHeader("Pragma","no-cache");
		     response.setHeader("Expires","0");
	
		     return mv;
	  }
	  
	  @RequestMapping(value = { "/HDPAY/TIPS/Identifier/View" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody String Channel_Configuration(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
	  {	 
	      JsonObject details = new JsonObject();
	      
	      if(Session_Model.IsSessionValid(session))
		  {
	    	  details = TIPS.Retrieve_All_TIPS_Identfifer();
	      }
	      else
	      {
	    	  details.addProperty("result", "failed");
			  details.addProperty("stscode", "500");
	      }
	     
	      return details.toString();
	  }
	  
	  @RequestMapping(value = { "/HDPAY/TIPS/Identifier/Creation" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody String Identifier_Creation(@ModelAttribute Identifiers001 Info, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
	  {	 
	      JsonObject details = new JsonObject();
	      
	      if(Session_Model.IsSessionValid(session))
		  {
	    	  details = TIPS.Register_Identifier(Info);
	      }
	      else
	      {
	    	  details.addProperty("result", "failed");
			  details.addProperty("stscode", "500");
	      }
	     
	      return details.toString();
	  }
	  
	  @RequestMapping(value = { "/HDPAY/TIPS/Identifier/Bulk/Excel/Upload" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody String Projects_Add_lease_Excel_upload(@RequestParam("File") CommonsMultipartFile File, @RequestParam("PAYTYPE") String PAYTYPE, HttpServletRequest request, HttpServletResponse response, HttpSession session)
	  {   	
	 	  JsonObject details = new JsonObject();
  		
	 	  if(Session_Model.IsSessionValid(session))
		  {
  			  details = TIPS.Identifier_Excel_Upload(PAYTYPE, File);
		  }
	 	  else
	      {
	    	  details.addProperty("result", "failed");
			  details.addProperty("stscode", "500");
	      }
  		 
	 	  return details.toString();	 
	  }
	  
	  @RequestMapping(value = { "/HDPAY/TIPS/Identifier/Bulk/Excel/data/Upload" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody String Excel_data_upload(@RequestBody String info, HttpServletRequest request, HttpServletResponse response, HttpSession session)
	  {   	
	 	  JsonObject details = new JsonObject();
  		
	 	  if(Session_Model.IsSessionValid(session))
		  {
  			  details = TIPS.Identifier_Excel_Data_Upload(info);
		  }
	 	  else
	      {
	    	  details.addProperty("result", "failed");
			  details.addProperty("stscode", "500");
	      }
  		 
	 	  return details.toString();	 
	  }
	  
	  @RequestMapping(value = { "/HDPAY/TIPS/Identifier/Queue" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody String Identifier_Queue(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	  {   	
	 	  JsonObject details = new JsonObject();
  		
	 	  if(Session_Model.IsSessionValid(session))
		  {
  			  details = TIPS.Queued_Identifiers();
		  }
	 	  else
	      {
	    	  details.addProperty("result", "failed");
			  details.addProperty("stscode", "500");
	      }
  		 
	 	  return details.toString();	 
	  }
			  
	  /************** Consumed by TIPS ***************/
	  
	  @RequestMapping(value = { "/EXIMPAY/TIPS/Inward/parties/{identifierType}/{identifier}/sync" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody ResponseEntity<String> lookup_parties_identifierType(@PathVariable("identifierType") String identifierType, @PathVariable("identifier") String identifier, HttpServletRequest request, @RequestHeader(value = "accept", required=false) String Accept, @RequestHeader(value = "content-type", required=true) String Content_Type, 
      @RequestHeader(value = "date", required=true) String date,  @RequestHeader(value = "fsp-source", required=true) String fsp_source, @RequestHeader(value = "fsp-destination", required=true) String fsp_destination, @RequestHeader(value = "fsp-encryption", required=false) String fsp_encryption, 
      @RequestHeader(value = "fsp-signature", required=false) String fsp_signature, @RequestHeader(value = "fsp-uri", required=false) String fsp_uri, @RequestHeader(value = "fsp-http-method", required=false) String fsp_http_method, @RequestHeader(value = "LookupId", required=false) String LookupId, @RequestHeader(value = "requestid", required=false) String requestid) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Headers.addProperty("accept", Accept); 
		  Headers.addProperty("content-type", Content_Type);  
		  Headers.addProperty("date", date);  
		  Headers.addProperty("fsp-source", fsp_source); 
		  Headers.addProperty("fsp-destination", fsp_destination);
		  Headers.addProperty("fsp-encryption", fsp_encryption);  
		  Headers.addProperty("fsp-signature", fsp_signature);
		  Headers.addProperty("fsp-uri", fsp_uri); 
		  Headers.addProperty("fsp-http-method", fsp_http_method);
		  Headers.addProperty("LookupId", LookupId);  
		  Headers.addProperty("requestid", requestid); 
		  
		  logger.debug(">>>>>>>> New Request Received from TIPS - GET /EXIMPAY/TIPS/parties/"+identifierType+"/"+identifier+"/sync <<<<<<<<");
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  logger.debug(">>>>>>>> identifierType >>>>>>>>> "+identifierType);
		  logger.debug(">>>>>>>> identifier >>>>>>>>> "+identifier);
		  
		  JsonObject Response = new JsonObject();
		  
		  Response = TIPS.Retrieve_Account_Information_for_Tips(identifierType, identifier);
		  
		  if(Response.has("Informations") && !Response.has("errorInformation"))
		  {
			  details = Response.get("Informations").getAsJsonObject();
			  
			  logger.debug(">>>>>>>> Response from HDPAY >>>>>>>>> "+details.toString());
			  
			  return new ResponseEntity<String>(details.toString(), HttpStatus.OK);
		  }
		  else
		  {
			  details = Response.get("errorInformation").getAsJsonObject();
			  
			  logger.debug(">>>>>>>> Response from HDPAY >>>>>>>>> "+details.toString());
			  
			  return new ResponseEntity<String>(details.toString(), HttpStatus.BAD_REQUEST);
		  }
      } 
	  
	  @RequestMapping(value = { "/EXIMPAY/TIPS/Inward/transfers" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody ResponseEntity<String> Inward_transfers(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "accept", required=false) String Accept, @RequestHeader(value = "content-type", required=true) String Content_Type, 
      @RequestHeader(value = "date", required=true) String date,  @RequestHeader(value = "fsp-source", required=true) String fsp_source, @RequestHeader(value = "fsp-destination", required=true) String fsp_destination, @RequestHeader(value = "fsp-encryption", required=false) String fsp_encryption, 
      @RequestHeader(value = "signature", required=true) String signature, @RequestHeader(value = "fsp-uri", required=false) String fsp_uri, @RequestHeader(value = "fsp-http-method", required=false) String fsp_http_method) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Headers.addProperty("accept", Accept); 
		  Headers.addProperty("content-type", Content_Type);  
		  Headers.addProperty("date", date);  
		  Headers.addProperty("fsp-source", fsp_source); 
		  Headers.addProperty("fsp-destination", fsp_destination);
		  Headers.addProperty("fsp-encryption", fsp_encryption);  
		  Headers.addProperty("signature", signature);
		  Headers.addProperty("fsp-uri", fsp_uri); 
		  Headers.addProperty("fsp-http-method", fsp_http_method); 
		  
		  logger.debug(">>>>>>>> New Request Received from TIPS - POST /EXIMPAY/TIPS/Inward/transfers <<<<<<<<");  
		 
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		
		  details = TIPS.Handle_TIPS_Transfer_Request_From_TIPS(Body_MSG, Headers, request);
		  
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details.toString());
		  
		  if(details.has("errorInformation"))
		  {
			  return new ResponseEntity<String>(details.toString(), HttpStatus.BAD_REQUEST);
		  }
		  else
		  {
			  return new ResponseEntity<String>(details.toString(), HttpStatus.ACCEPTED);
		  } 
      } 
	  
	  @RequestMapping(value = { "/EXIMPAY/TIPS/Inward/transfers/{payerRef}" }, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody ResponseEntity<String> Inward_transfers_payerRef(@PathVariable("payerRef") String payerRef, @RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "accept", required=true) String Accept, @RequestHeader(value = "content-type", required=true) String Content_Type, 
      @RequestHeader(value = "date", required=true) String date,  @RequestHeader(value = "fsp-source", required=true) String fsp_source, @RequestHeader(value = "fsp-destination", required=true) String fsp_destination, @RequestHeader(value = "fsp-encryption", required=false) String fsp_encryption, 
      @RequestHeader(value = "fsp-signature", required=false) String fsp_signature, @RequestHeader(value = "fsp-uri", required=false) String fsp_uri, @RequestHeader(value = "fsp-http-method", required=false) String fsp_http_method) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Headers.addProperty("accept", Accept); 
		  Headers.addProperty("content-type", Content_Type);  
		  Headers.addProperty("date", date);  
		  Headers.addProperty("fsp-source", fsp_source); 
		  Headers.addProperty("fsp-destination", fsp_destination);
		  Headers.addProperty("fsp-encryption", fsp_encryption);  
		  Headers.addProperty("fsp-signature", fsp_signature);
		  Headers.addProperty("fsp-uri", fsp_uri); 
		  Headers.addProperty("fsp-http-method", fsp_http_method);
		
		  logger.debug(">>>>>>>> New Request Received from TIPS - PUT /EXIMPAY/TIPS/Inward/transfers/"+payerRef+" <<<<<<<<");  
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details = TIPS.Transfer_Confirmation_Notification(Body_MSG, Headers, request, payerRef);
		  
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details.toString());
	
		  return new ResponseEntity<String>(HttpStatus.OK);
      } 
	  
	  @RequestMapping(value = { "/EXIMPAY/TIPS/Inward/transfers/{payerRef}/error" }, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody ResponseEntity<String> Inward_transfers_payerRef_error(@PathVariable("payerRef") String payerRef, @RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "accept", required=false) String Accept, @RequestHeader(value = "content-type", required=true) String Content_Type, 
      @RequestHeader(value = "date", required=true) String date,  @RequestHeader(value = "fsp-source", required=true) String fsp_source, @RequestHeader(value = "fsp-destination", required=true) String fsp_destination, @RequestHeader(value = "fsp-encryption", required=false) String fsp_encryption, 
      @RequestHeader(value = "fsp-signature", required=false) String fsp_signature, @RequestHeader(value = "fsp-uri", required=false) String fsp_uri, @RequestHeader(value = "fsp-http-method", required=false) String fsp_http_method) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Headers.addProperty("accept", Accept); 
		  Headers.addProperty("content-type", Content_Type);  
		  Headers.addProperty("date", date);  
		  Headers.addProperty("fsp-source", fsp_source); 
		  Headers.addProperty("fsp-destination", fsp_destination);
		  Headers.addProperty("fsp-encryption", fsp_encryption);  
		  Headers.addProperty("fsp-signature", fsp_signature);
		  Headers.addProperty("fsp-uri", fsp_uri); 
		  Headers.addProperty("fsp-http-method", fsp_http_method);
		 
		  logger.debug(">>>>>>>> Request Received from TIPS - PUT /EXIMPAY/TIPS/Inward/transfers/"+payerRef+"/error <<<<<<<<");  
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details = TIPS.Transfer_Failed_Notification(Body_MSG, payerRef, Headers, request);
	
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details.toString());
		  
		  return new ResponseEntity<String>(HttpStatus.OK);
      } 
	  
	  @RequestMapping(value = { "/EXIMPAY/TIPS/Inward/transfers/{payerRef}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody ResponseEntity<String> Retrieve_transfers_payerRef(@PathVariable("payerRef") String payerRef, HttpServletRequest request, @RequestHeader(value = "accept", required=false) String Accept, @RequestHeader(value = "content-type", required=true) String Content_Type, 
      @RequestHeader(value = "date", required=false) String date,  @RequestHeader(value = "fsp-source", required=true) String fsp_source, @RequestHeader(value = "fsp-destination", required=true) String fsp_destination, @RequestHeader(value = "fsp-encryption", required=false) String fsp_encryption, 
      @RequestHeader(value = "fsp-signature", required=false) String fsp_signature, @RequestHeader(value = "fsp-uri", required=false) String fsp_uri, @RequestHeader(value = "fsp-http-method", required=false) String fsp_http_method) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  logger.debug(">>>>>>>> New Enquiry Request Received from TIPS - GET /EXIMPAY/TIPS/Inward/transfers/"+payerRef+" <<<<<<<<"); 
		  
		  JsonObject Headers = new JsonObject();
	
		  Headers.addProperty("accept", Accept); 
		  Headers.addProperty("content-type", Content_Type);  
		  Headers.addProperty("date", date);  
		  Headers.addProperty("fsp-source", fsp_source); 
		  Headers.addProperty("fsp-destination", fsp_destination);
		  Headers.addProperty("fsp-encryption", fsp_encryption);  
		  Headers.addProperty("fsp-signature", fsp_signature);
		  Headers.addProperty("fsp-uri", fsp_uri); 
		  Headers.addProperty("fsp-http-method", fsp_http_method);
		  
		  logger.debug(">>>>>>>> Headers >>>>>>>>> "+Headers.toString());

		  details = TIPS.Retrieve_Transaction_by_PayerRef(payerRef, Headers);
		  
		  logger.debug(">>>>>>>> Response from HDPAY >>>>>>>>> "+details.toString());
		  
		  if(details.has("Information")) 
		  {
			  details = details.get("Information").getAsJsonObject();
			  
			  return new ResponseEntity<String>(details.toString(), HttpStatus.OK);
		  }
		  else
		  {
			  return new ResponseEntity<String>(details.toString(), HttpStatus.BAD_REQUEST);
		  }
      }  
	  
	  @RequestMapping(value = { "/EXIMPAY/TIPS/Inward/messageTransfersReversal" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody ResponseEntity<String> Inward_messageTransfersReversal(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "accept", required=false) String Accept, @RequestHeader(value = "content-type", required=true) String Content_Type, 
      @RequestHeader(value = "date", required=false) String date,  @RequestHeader(value = "fsp-source", required=true) String fsp_source, @RequestHeader(value = "fsp-destination", required=true) String fsp_destination, @RequestHeader(value = "fsp-encryption", required=false) String fsp_encryption, 
      @RequestHeader(value = "fsp-signature", required=false) String fsp_signature, @RequestHeader(value = "fsp-uri", required=false) String fsp_uri, @RequestHeader(value = "fsp-http-method", required=false) String fsp_http_method) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Headers.addProperty("accept", Accept); 
		  Headers.addProperty("content-type", Content_Type);  
		  Headers.addProperty("date", date);  
		  Headers.addProperty("fsp-source", fsp_source); 
		  Headers.addProperty("fsp-destination", fsp_destination);
		  Headers.addProperty("fsp-encryption", fsp_encryption);  
		  Headers.addProperty("fsp-signature", fsp_signature);
		  Headers.addProperty("fsp-uri", fsp_uri); 
		  Headers.addProperty("fsp-http-method", fsp_http_method);
		  
		  logger.debug(">>>>>>>> New Request Received from TIPS - POST /EXIMPAY/TIPS/messageTransfersReversal <<<<<<<<");  
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  JsonObject Response = new JsonObject();
		  
		  Response = TIPS.Transfer_Reversal_Message(Body_MSG, Headers, request);
		  
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details.toString());
		  
		  if(Response.has("errorInformation")) 
		  {
			  return new ResponseEntity<String>(details.toString(), HttpStatus.BAD_REQUEST);
		  }
		  else
		  {
			  return new ResponseEntity<String>(HttpStatus.ACCEPTED);  
		  }
      } 
	  
	  @RequestMapping(value = { "/EXIMPAY/TIPS/Inward/messageTransfersReversal/{payerRef}" }, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody ResponseEntity<String> Inward_messageTransfersReversal_payerRef(@PathVariable("payerRef") String payerRef, @RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "accept", required=false) String Accept, @RequestHeader(value = "content-type", required=true) String Content_Type, 
      @RequestHeader(value = "date", required=false) String date,  @RequestHeader(value = "fsp-source", required=true) String fsp_source, @RequestHeader(value = "fsp-destination", required=true) String fsp_destination, @RequestHeader(value = "fsp-encryption", required=false) String fsp_encryption, 
      @RequestHeader(value = "fsp-signature", required=false) String fsp_signature, @RequestHeader(value = "fsp-uri", required=false) String fsp_uri, @RequestHeader(value = "fsp-http-method", required=false) String fsp_http_method) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Headers.addProperty("accept", Accept); 
		  Headers.addProperty("content-type", Content_Type);  
		  Headers.addProperty("date", date);  
		  Headers.addProperty("fsp-source", fsp_source); 
		  Headers.addProperty("fsp-destination", fsp_destination);
		  Headers.addProperty("fsp-encryption", fsp_encryption);  
		  Headers.addProperty("fsp-signature", fsp_signature);
		  Headers.addProperty("fsp-uri", fsp_uri); 
		  Headers.addProperty("fsp-http-method", fsp_http_method);
		 
		  logger.debug(">>>>>>>> New Request Received from TIPS - PUT /EXIMPAY/TIPS/Inward/messageTransfersReversal/"+payerRef+" <<<<<<<<");  
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details = TIPS.Transfer_Reversal_Message_Notification(Body_MSG, payerRef, Headers, request);
	
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details.toString());
		  
		  return new ResponseEntity<String>(HttpStatus.OK);
      } 
	  
	  @RequestMapping(value = { "/EXIMPAY/TIPS/Inward/transfersReversal/{payerRef}" }, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody ResponseEntity<String> Inward_transfersReversal_payerRef(@PathVariable("payerRef") String payerRef, @RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "accept", required=false) String Accept, @RequestHeader(value = "content-type", required=true) String Content_Type, 
      @RequestHeader(value = "date", required=false) String date,  @RequestHeader(value = "fsp-source", required=true) String fsp_source, @RequestHeader(value = "fsp-destination", required=true) String fsp_destination, @RequestHeader(value = "fsp-encryption", required=false) String fsp_encryption, 
      @RequestHeader(value = "fsp-signature", required=false) String fsp_signature, @RequestHeader(value = "fsp-uri", required=false) String fsp_uri, @RequestHeader(value = "fsp-http-method", required=false) String fsp_http_method) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Headers.addProperty("accept", Accept); 
		  Headers.addProperty("content-type", Content_Type);  
		  Headers.addProperty("date", date);  
		  Headers.addProperty("fsp-source", fsp_source); 
		  Headers.addProperty("fsp-destination", fsp_destination);
		  Headers.addProperty("fsp-encryption", fsp_encryption);  
		  Headers.addProperty("fsp-signature", fsp_signature);
		  Headers.addProperty("fsp-uri", fsp_uri); 
		  Headers.addProperty("fsp-http-method", fsp_http_method);
		 
		  logger.debug(">>>>>>>> Request Received from TIPS - PUT /EXIMPAY/TIPS/Inward/transfersReversal/"+payerRef+" <<<<<<<<");  
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details = TIPS.Transfer_Reversal_Update(Body_MSG, payerRef, Headers, request);
	
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details.toString());
		  
		  return new ResponseEntity<String>(HttpStatus.OK);
      } 
	  
	  @RequestMapping(value = { "/EXIMPAY/TIPS/Inward/settlements" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody ResponseEntity<String> Inward_settlements(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "accept", required=false) String Accept, @RequestHeader(value = "content-type", required=true) String Content_Type, 
      @RequestHeader(value = "date", required=true) String date,  @RequestHeader(value = "fsp-source", required=true) String fsp_source, @RequestHeader(value = "fsp-destination", required=true) String fsp_destination, @RequestHeader(value = "fsp-encryption", required=false) String fsp_encryption, 
      @RequestHeader(value = "fsp-signature", required=false) String fsp_signature, @RequestHeader(value = "fsp-uri", required=false) String fsp_uri, @RequestHeader(value = "fsp-http-method", required=false) String fsp_http_method) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Headers.addProperty("accept", Accept); 
		  Headers.addProperty("content-type", Content_Type);  
		  Headers.addProperty("date", date);  
		  Headers.addProperty("fsp-source", fsp_source); 
		  Headers.addProperty("fsp-destination", fsp_destination);
		  Headers.addProperty("fsp-encryption", fsp_encryption);  
		  Headers.addProperty("fsp-signature", fsp_signature);
		  Headers.addProperty("fsp-uri", fsp_uri); 
		  Headers.addProperty("fsp-http-method", fsp_http_method);
		  
		  logger.debug(">>>>>>>> Request Received from TIPS - POST EXIMPAY/TIPS/settlements <<<<<<<<");  
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  logger.debug(">>>>>>>> Response >>>>>>>>> "+details.toString());
		  
		  return new ResponseEntity<String>(HttpStatus.OK);
      }  
	  
	  /*@RequestMapping(value = { "/EXIMPAY/SIGN/TEST" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody String Inward_settlementss(@RequestBody String Body_MSG, HttpServletRequest request) 
      {	 
		  JsonObject details = new JsonObject();
		  
		  TIPS_Modal mx = new TIPS_Modal();
		 
		  details.addProperty("sign", mx.get_signature(Body_MSG));
		  
		  details.addProperty("sign", mx.get_signature(Body_MSG));
		  
		  return details.toString();
      } */
}