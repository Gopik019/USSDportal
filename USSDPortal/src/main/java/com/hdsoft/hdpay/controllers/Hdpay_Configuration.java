package com.hdsoft.hdpay.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.common.Menu_Generation;
import com.hdsoft.common.Token_System;
import com.hdsoft.hdpay.models.Session_Model;
import com.hdsoft.hdpay.Repositories.Channels_Store;
import com.hdsoft.hdpay.Repositories.Payment_Gateway_Store;
import com.hdsoft.hdpay.Repositories.web_service_001;
import com.hdsoft.hdpay.models.Configuration_Modal;
import com.hdsoft.hdpay.models.Pdf_Modal;
import com.hdsoft.hdpay.models.WhatsappSender;

@Controller
public class Hdpay_Configuration
{
	@Autowired
	public Configuration_Modal config;
	
	@Autowired
	public Menu_Generation MG;
	 
	@Autowired
	public Token_System tk;
	
	@Autowired
	public Pdf_Modal PDF;
	 
	@RequestMapping(value = { "/HDPAY/Banking_Channels" }, method = RequestMethod.GET)
    public ModelAndView Configuration_Index(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Configuration/Banking_Channels");
	    	 	
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "CONFIG", "BNKCHN"));
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
	
	@RequestMapping(value = {"/HDPAY/Payment_Gateway"}, method = RequestMethod.GET)
    public ModelAndView Configuration_Payment_Gateway(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Configuration/Payment_Gateway");
		     
		     mv.addObject("Menu", MG.Get_Menus_HTML(session, "CONFIG", "PAYGATE"));
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
	
	@RequestMapping(value = {"/HDPAY/Security_Configuration"}, method = RequestMethod.GET)
    public ModelAndView Configuration_Security_Configuration(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Configuration/#");
		     
		     mv.addObject("Menu", MG.Get_Menus_HTML(session, "CONFIG", "SECONFIG"));
		      
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
	
	@RequestMapping(value = { "/HDPAY/Transactions_Controls" }, method = RequestMethod.GET)
    public ModelAndView Configuration_Transactions_Controls(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Configuration/#");
		     
	    	  mv.addObject("Menu", MG.Get_Menus_HTML(session, "CONFIG", "TRANCTRL"));
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
	
	@RequestMapping(value = {"/HDPAY/API_Configuration"}, method = RequestMethod.GET)
    public ModelAndView Configuration_API_Configurations(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     if(Session_Model.IsSessionValid(session))
	     {
	    	  mv.setViewName("HDPAY/Configuration/API_Configuration");
		     
	    	  mv.addObject("Menu", MG.Get_Menus_HTML(session, "CONFIG", "APICONFIG"));
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
	
	@RequestMapping(value = {"/HDPAY/Sydonia"}, method = RequestMethod.GET)
    public ModelAndView Sydonia(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     if(Session_Model.IsSessionValid(session))
	     {
	    	  mv.setViewName("HDPAY/Configuration/Sydonia_online");
		     
	    	  mv.addObject("Menu", MG.Get_Menus_HTML(session, "CONFIG", "APICONFIG"));
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
	
	@RequestMapping(value = {"/HDPAY/Channel_Configuration"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Channel_Configuration(@ModelAttribute Channels_Store Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject();
	     
	     details = config.Channel_Action(Info) ;
	  
	     return details.toString();
    }
	
	@RequestMapping(value = {"/HDPAY/Payement_Gateway_Configuration"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Payement_Gateway_Configuration(@ModelAttribute Payment_Gateway_Store Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject();
	     
	     details = config.Payment_Gateway_Action(Info) ;
	  
	     return details.toString();
    }
	
	 @RequestMapping(value = {"/HDPAY/suggestions/ChannelCode"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_ChannelCode_Retrieve(@RequestParam("term") String term ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
		 JsonArray details = new JsonArray();
    	   	
    	 details = config.Get_ChannelCodes(term);  
    	
    	 return details.toString();  	
     }
	 
	 @RequestMapping(value = {"/HDPAY/suggestions/Gatewaycode"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_Gatewaycode_Retrieve(@RequestParam("term") String term ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
		 JsonArray details = new JsonArray();
    	   	
    	 details = config.Get_Payment_Gateway_Codes(term);  
    	
    	 return details.toString();  	
     }
	 
	 @RequestMapping(value = {"/HDPAY/suggestions/APIcode"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_APIcode_Retrieve(@RequestParam("term") String term ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
		 JsonArray details = new JsonArray();
    	   	
    	 details = config.Get_API_Codes(term);   
    	
    	 return details.toString();  	
     }
	 
	 @RequestMapping(value = {"/HDPAY/Find/Channel"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Find_ChannelCode_Information(Channels_Store Info ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
		 JsonObject details = new JsonObject();
    	   	
    	 details = config.Get_channel_Info(Info);  
    	
    	 return details.toString();  	
     }
	 
	 @RequestMapping(value = {"/HDPAY/Find/Gateway"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Find_Gateway_Information(Payment_Gateway_Store Info ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
		 JsonObject details = new JsonObject();
    	   	
    	 details = config.Get_Gateway_Info(Info);  
    	
    	 return details.toString();  	
     }
	 
	 @RequestMapping(value = {"/HDPAY/API_Configuration"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Configuration_API_Configurations(@ModelAttribute web_service_001 Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
	     JsonObject details = new JsonObject();
	     
	     details = config.API_Configuration_Action(Info) ;
	  
	     return details.toString();
     }
	 
	 @RequestMapping(value = {"/HDPAY/Find/API_Service"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Find_Api_Information(@ModelAttribute web_service_001 Info ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
		 JsonObject details = new JsonObject();
    	   	
    	 details = config.Find_API_Service(Info.getCHCODE(), Info.getSERVNAME(), Info.getSERVICECD());   
    	
    	 return details.toString();  	
     }
	 
	 @RequestMapping(value = {"/HDPAY/SecretKey/Generation"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Channel_SecretKey(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
	     JsonObject details = new JsonObject();
	     
	     details = config.Channel_Secret() ;
	  
	     return details.toString();
     }
	 
	 @RequestMapping(value = { "/HDPAY/Generate/PDF" }, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody String Generate_PDF(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
	 {	 
	     JsonObject details = new JsonObject();
	     
	     details = PDF.Download_Invoice() ;
	  
	     return details.toString();
	 }
	 

	
	 
	 @RequestMapping(value = { "/HDPAY/Whatsapp-Banking" }, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody String Whatsapp_Banking(@RequestParam("pin") String pin, @RequestParam("mob") String mobile, @RequestParam("msg") String msg, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
	 {	 
	     JsonObject details = new JsonObject();
	     
	     try
		 {
	    	 System.out.println(pin);
	    	 System.out.println(mobile);
	    	 System.out.println(msg);
	    	 
	    	 WhatsappSender.sendMessage(pin+mobile.trim(), msg.trim());
	    	 
	    	 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Message Sent Successfully !!");
		 }
	     catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  
		 }
	    
	     return details.toString();
	 }
}