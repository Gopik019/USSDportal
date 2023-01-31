package com.hdsoft.hdpay.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.JsonObject;
import com.hdsoft.hdpay.models.Mvola_Modal;

@Controller
public class MVOLA 
{
	 @Autowired
	 public Mvola_Modal Mv;
	
	 @RequestMapping(value = { "/EXIMPAY/MVOLA/AccountBalance" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody ResponseEntity<String> Account_Balance(@RequestBody String Body_MSG, @RequestHeader(value = "accept", required=false) String Accept,  @RequestHeader(value = "content-type", required=true) String Content_Type, 
	 @RequestHeader(value = "authorization", required=false) String authorization,  HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 	 		
	 {	 
	  	JsonObject details = new JsonObject();
	    
	    details = Mv.getAccountBalance(Body_MSG);
	    
	    if(details.has("errorCategory"))
		{
			  return new ResponseEntity<String>(details.toString(), HttpStatus.BAD_REQUEST);
		}
		else
		{
			  return new ResponseEntity<String>(details.toString(), HttpStatus.OK);
		} 
	  }
}
