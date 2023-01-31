package com.hdsoft.hdpay.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.models.BSMART_Modal;

@Controller
public class Bsmart 
{	
	@Autowired
	public BSMART_Modal bsmart;
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	@RequestMapping(value = {"/HDPAY/Generate/Invoice" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Bill_request_GEPG(@RequestBody String Body_MSG, HttpServletRequest request, @RequestHeader(value = "Accept", required=true) String Accept, @RequestHeader(value = "Content-Type", required=true) String Content_Type) 
    {	 
		  JsonObject details = new JsonObject();
		  
		  JsonObject Headers = new JsonObject();
	
		  Common_Utils util = new Common_Utils();
			 
		  Headers.addProperty("Accept", util.ReplaceNull(Accept));
		  Headers.addProperty("Content-Type", util.ReplaceNull(Content_Type));
		
		  logger.debug(">>>>>>>> New Invoice Generation Request Received from BSMART <<<<<<<<");
		  
		  logger.debug(">>>>>>>> Request Body <<<<<<<< "+Body_MSG);
		  
		  logger.debug(">>>>>>>> Request Headers <<<<<<<< "+Headers.toString());
		  
		  details =  bsmart.Handle_Invoice_Info(Body_MSG, Headers, request);

		  logger.debug(">>>>>>>> Response <<<<<<<< "+details);
		  
	      return details.toString();
     } 
}
