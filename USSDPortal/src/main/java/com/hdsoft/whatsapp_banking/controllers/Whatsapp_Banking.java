package com.hdsoft.whatsapp_banking.controllers;

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
import com.hdsoft.whatsapp_banking.Repositories.Whatsapp_Info;
import com.hdsoft.whatsapp_banking.models.Whatsapp_Bank;


@Controller
public class Whatsapp_Banking
{
	@Autowired
	public Menu_Generation MG;
	
	@Autowired
	public Whatsapp_Bank Wtsapp;
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Day_Notification" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Day_Notification(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Day_Notification");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "DYNT"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/USSD_Day_Notification" }, method = RequestMethod.GET)
    public ModelAndView USSD_Day_Notification(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("USSD_Banking/Day_Notification");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "USSDBK", "DYNT1"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Week_Notification" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Week_Notification(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Week_Notification");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "WKNT"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	
	@RequestMapping(value = { "/HDPAY/USSD_Week_Notification" }, method = RequestMethod.GET)
    public ModelAndView USSD_Week_Notification(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("USSD_Banking/Week_Notification");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "USSDBK", "WKNT1"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Month_Notification" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Month_Notification(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Month_Notification");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "MTHNT"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/USSD_Month_Notification" }, method = RequestMethod.GET)
    public ModelAndView USSD_Month_Notification(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("USSD_Banking/Month_Notification");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "USSDBK", "MTHNT1"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Template_Configuration" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Template_Configuration(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Template_Configuration");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "TEMP"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Group_Creation" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Group_Creation(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Group_Creation");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "MARKT"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;  
    }
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Marketing_Compaing_Configuration" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Marketing_Compaing_Configuration(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Marketing_Compaign_Configuration");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "MRKCON"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Service_Configuration" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Service_Configuration(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Service_Configuration");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "SERVCON"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Marketing_Service_Mapping" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Marketing_Service_Mapping(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Marketing_Service_Mapping"); 
	    	 
	    	 ///Whatsapp_Marketing_Service_Mapping
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "MARKTCOM"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Customer" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Customer(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Customer");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "WHCUS"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Account" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Account(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Account");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "WHACC"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Notification" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Notification(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Notification");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "NOTIF"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;  
    }
	
	
	@RequestMapping(value = { "/HDPAY/Whatsapp_Marketing_Configuration" }, method = RequestMethod.GET)
    public ModelAndView Whatsapp_Marketing_Configuration(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Marketing_Configuration");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "MARKT"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/Demo_Notification" }, method = RequestMethod.GET)
    public ModelAndView Demo_Notification(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Demo_Notification");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "MSGTEST"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	@RequestMapping(value = { "/HDPAY/Customer_User_Registration" }, method = RequestMethod.GET)
    public ModelAndView Customer_User_Registration(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Customer_User_reg");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "CSTURREG"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	
	@RequestMapping(value = { "/HDPAY/Customer_User_regcopy" }, method = RequestMethod.GET)
    public ModelAndView Customer_User_Registration2(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	    
	    	 mv.setViewName("Whatsapp_Banking/Customer_User_regcopy");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "WHBK", "CSTURREG2"));
	     
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    }
	
	/* @RequestMapping(value = { "/HDPAY/Demo_Notification" }, method = RequestMethod.GET)
	    public ModelAndView Demo_Notification1(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
	    {	 
		     ModelAndView mv = new ModelAndView();
		   
		     if(Session_Model.IsSessionValid(session))
		     {
		    	 mv.setViewName("HDPAY/Whatsapp_Banking/Demo_Notification"); 
			     
		    	  mv.addObject("Menu", MG.Get_Menus_HTML(session, "MSGTEST", "WHBK"));
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
	
	
	
	
	
	@RequestMapping(value = {"/HDPAY/Account"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Account_Configuration(@ModelAttribute Whatsapp_Info Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject();
	     
	     //details = Wtsapp.Account_Configuration(Info) ;
	  
	     return details.toString();
    }
	
	@RequestMapping(value = {"/HDPAY/Customer"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Customer_Configuration(@ModelAttribute Whatsapp_Info Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject();
	     
	     //details = Wtsapp.Customer_Configuration(Info) ;
	  
	     return details.toString();
    }*/
	
	@RequestMapping(value = {"/HDPAY/Template"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Template_Configuration(@ModelAttribute Whatsapp_Info Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject();
	     
	     details = Wtsapp.Template_Configuration(Info) ;
	  
	     return details.toString();
    }
	
	@RequestMapping(value = {"/HDPAY/Service"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Service_Configuration(@ModelAttribute Whatsapp_Info Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject();
	     
	     details = Wtsapp.Service_Configuration(Info) ;
	  
	     return details.toString();
    }
	
	@RequestMapping(value = {"/HDPAY/Group_Creation"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Group_Creation(@ModelAttribute Whatsapp_Info Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject();
	     
	     details = Wtsapp.Group_Configuration(Info) ;
	     
	     System.out.println(details);
	  
	     return details.toString();
    }
	
	@RequestMapping(value = {"/HDPAY/suggestions/TempCode"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Suggestions_TempCode_Retrieve(@RequestParam("term") String term ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
		 JsonArray details = new JsonArray();
   	   	
   	 details = Wtsapp.Get_Template_Code(term);  
   	
   	 return details.toString();  	
    }
	
	@RequestMapping(value = {"/HDPAY/suggestions/GroupCode"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Suggestions_GroupCode(@RequestParam("term") String term ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
		 JsonArray details = new JsonArray();
   	   	
   	 details = Wtsapp.Get_Group_Code(term);  
   	
   	 return details.toString();  	
    }
	
	@RequestMapping(value = {"/HDPAY/suggestions/GroupType"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Suggestions_GroupType(@RequestParam("term") String term ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
		 JsonArray details = new JsonArray();
   	   	
   	 details = Wtsapp.Get_Group_Type(term);  
   	
   	 return details.toString();  	
    }
	
	@RequestMapping(value = {"/HDPAY/suggestions/Servicecode"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Suggestions_ServiceCode_Retrieve(@RequestParam("term") String term ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
		 JsonArray details = new JsonArray();
   	   	
   	 details = Wtsapp.Get_Service_Code(term);  
   	
   	 return details.toString();  	
    }
	
	@RequestMapping(value = {"/HDPAY/Find/Template"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Find_Api_Information(@ModelAttribute Whatsapp_Info Info ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
		 JsonObject details = new JsonObject();
   	   	
   	     details = Wtsapp.Find_Template(Info);   
   	
   	     return details.toString();  	
    }
}
