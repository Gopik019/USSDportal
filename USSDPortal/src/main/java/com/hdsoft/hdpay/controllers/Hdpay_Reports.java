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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.JsonObject;
import com.hdsoft.common.Menu_Generation;
import com.hdsoft.hdpay.models.Session_Model;
import com.hdsoft.hdpay.Repositories.PAY_001;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.models.Report_Modal;

@Controller
public class Hdpay_Reports
{
	@Autowired
	public Report_Modal report;
	
	@Autowired
	public Menu_Generation MG;
	 
	@RequestMapping(value = { "/HDPAY/Transaction_Reports" }, method = RequestMethod.GET)
    public ModelAndView Configuration_Index(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Reports/Transaction_Reports");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "REPORTS", "TRANREPORT"));
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
	
	@RequestMapping(value = {"/HDPAY/Bill_Request_Reports"}, method = RequestMethod.GET)
    public ModelAndView Configuration_Payment_Gateway(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Reports/Bill_Request_Reports");
		     
		     mv.addObject("Menu", MG.Get_Menus_HTML(session, "REPORTS", "BILLREQREPORT"));
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
	
	@RequestMapping(value = {"/HDPAY/Reconcilation_Reports"}, method = RequestMethod.GET)
    public ModelAndView Configuration_Security_Configuration(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Reports/Reconcilation_Reports");
		     
		     mv.addObject("Menu", MG.Get_Menus_HTML(session, "REPORTS", "RECONREPORT"));
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
	
	@RequestMapping(value = {"/HDPAY/Transaction_Reports"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Channel_Configuration(@ModelAttribute PAY_001 Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	      JsonObject details = new JsonObject();
	     
	      if(Session_Model.IsSessionValid(session))
		  {
	    	  details = report.Get_Transaction_Reports(Info);
	      }
	      else
	      {
	    	  details.addProperty("Result", "Failed");
			  details.addProperty("Message", "Session Expired");
	      }
	     
	      return details.toString();
    }
	
	@RequestMapping(value = {"/HDPAY/Bill_Request_Reports"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Payement_Gateway_Configuration(@ModelAttribute Request_001 Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject();
	     
	     if(Session_Model.IsSessionValid(session))
		 {
	    	 details = report.Get_Bill_Request_Reports(Info) ;
		 }
	     else
	     {
	    	  details.addProperty("Result", "Failed");
			  details.addProperty("Message", "Session Expired");
	     }
	     
	     return details.toString();
    }
	
	 @RequestMapping(value = {"/HDPAY/Reconcilation_Reports"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_ChannelCode_Retrieve(@ModelAttribute Request_001 Info, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
		 JsonObject details = new JsonObject();
	     	
		 if(Session_Model.IsSessionValid(session))
		 {
			 details = report.Get_Recon_Request_Reports(Info);
		 }
	     else
	     {
	    	  details.addProperty("Result", "Failed");
			  details.addProperty("Message", "Session Expired");
	     }
		 
    	 return details.toString();  	
     }
}