package com.hdsoft.hdpay.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.models.Outward_Modal;

@Controller
public class Outbound
{
	 @Autowired
	 public Outward_Modal Modal;
	 
	/* @RequestMapping(value = {"/HDPAY/Test/GEPG"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Test_Inbound(HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
     {	 
   	 	JsonObject details = new JsonObject();
   
   	 	details = GEPG_Executer();
   	    	 
	    return details.toString();
     } */
	
     @RequestMapping(value = {"/HDPAY/Test/Health"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Test_Health(HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject details = new JsonObject();
    
    	 details.addProperty("Messsage", "200");
    	 details.addProperty("Request", "");
    	 details.addProperty("Response", "coming soon");
		 
	     return details.toString();
     } 
     
     @RequestMapping(value = {"/HDPAY/Test/merchant"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Test_Health_merchant(@RequestBody String request) 
     {	 
    	 JsonObject details = new JsonObject();
    
    	 details.addProperty("Messsage", "200");
    	 details.add("Request", new Common_Utils().StringToJsonObject(request));
    	 details.addProperty("Response", "coming soon");
    	    	 
	     return details.toString();
     } 
     
     @RequestMapping(value = {"/HDPAY/Test/tipsidentifier"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Test_tipsidentifiert(@RequestBody String request) 
     {	 
    	 JsonObject details = new JsonObject();
    
    	 details.addProperty("Messsage", "200");
    	 details.add("Request", new Common_Utils().StringToJsonObject(request));
    	 details.addProperty("Response", "coming soon");
    	    	 
	     return details.toString();
     } 
     
     @RequestMapping(value = {"/HDPAY/Test/lookup/{idType}/{idValue}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Test_lookup(@PathVariable(value="idType") String idType, @PathVariable(value="idValue") String idValue) 
     {	 
    	 JsonObject details = new JsonObject();
    
    	 JsonObject Request = new JsonObject();
    	 
    	 Request.addProperty("idType", idType);
    	 Request.addProperty("idValue", idValue);
    	 
    	 details.addProperty("Messsage", "200");
    	 details.add("Request", Request);
    	 details.addProperty("Response", "coming soon");
    	    	 
	     return details.toString();
     } 
     
     @RequestMapping(value = {"/HDPAY/Test/sendp2ptransfer"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Test_sendp2ptransfer(@RequestBody String request) 
     {	 
    	 JsonObject details = new JsonObject();
    
    	 details.addProperty("Messsage", "200");
    	 details.add("Request", new Common_Utils().StringToJsonObject(request));
    	 details.addProperty("Response", "coming soon");
    	    	 
	     return details.toString();
     } 
     
     @RequestMapping(value = {"/HDPAY/Test/sendp2btransfer"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Test_sendp2btransfer(@RequestBody String request) 
     {	 
    	 JsonObject details = new JsonObject();
    
    	 details.addProperty("Messsage", "200");
    	 details.add("Request", new Common_Utils().StringToJsonObject(request));
    	 details.addProperty("Response", "coming soon");
    	    	 
	     return details.toString();
     } 
     
     /*  @RequestMapping(value = {"/HDPAY/Test"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public String SessionManager(@RequestBody HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject details = new JsonObject();
    	 
    	 HttpHeaders headers = new HttpHeaders();
         headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
         HttpEntity <String> entity = new HttpEntity<String>(headers);
    	 
         String out =  restTemplate.exchange("http://localhost:8080/products", HttpMethod.GET, entity, String.class).getBody();
                	 
    	 details.addProperty("Messsage", "hiiii");
    	// details.addProperty("Request", request_str);
    	 details.addProperty("out", out);
    	    	 
	     return details.toString();
     }  */
}