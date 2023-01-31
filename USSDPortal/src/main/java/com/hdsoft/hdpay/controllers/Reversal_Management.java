package com.hdsoft.hdpay.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.hdsoft.common.Menu_Generation;
import com.hdsoft.hdpay.models.Session_Model;
import com.hdsoft.hdpay.Repositories.Transactions;
import com.hdsoft.hdpay.models.Reversal_Modal;
import com.zaxxer.hikari.HikariDataSource;

@Controller
public class Reversal_Management 
{
	 public JdbcTemplate Jdbctemplate;
	
	 private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	 @Autowired
	 public void setJdbctemplate(HikariDataSource Datasource)
	 {
		Jdbctemplate = new JdbcTemplate(Datasource);
	 }
	 
	 @Autowired
	 public Menu_Generation MG;
	 
	 @Autowired
	 public Reversal_Modal Rev;
	
	 @RequestMapping(value = { "/HDPAY/Transactions/Reversal" }, method = RequestMethod.GET)
	 public ModelAndView Transactions_Reversal(Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
	 {	 
	     ModelAndView mv = new ModelAndView();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Reversal/Reverse_Transaction");  
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "REVERSAL", "TRANREVERSAL"));
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
	 
	 @RequestMapping(value = { "/HDPAY/Reversal/Reports" }, method = RequestMethod.GET)
	 public ModelAndView Reversal_Reports(Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
	 {	 
	     ModelAndView mv = new ModelAndView();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Reversal/Pending_Reversal");   
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "REVERSAL", "REVERSALREP"));
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
	 
	  @RequestMapping(value = { "/HDPAY/Reversal/Tran/Details" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody String Account_Information(@ModelAttribute Transactions Info, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
	  {	 
	  	 JsonObject details = new JsonObject();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 details = Rev.Retrieve_Transaction_Information(Info);
	     }
	     else
    	 {
    		 details.addProperty("result", "failed");
			 details.addProperty("message", "Please re-login for security concerns");
    	 }
	     
	     return details.toString();
	  }
	  
	  /*@RequestMapping(value = { "/HDPAY/Reversal/Trantypes" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody String Reversal_Trantypes(@ModelAttribute Transactions Info, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
	  {	 
	  	 JsonObject details = new JsonObject();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 details = Rev.Retrieve_Reversal_Amount_Types(Info.getPAYTYPE());
	     }
	     else
    	 {
    		 details.addProperty("result", "failed");
			 details.addProperty("message", "Please re-login for security concerns");
    	 }
	     
	     return details.toString();
	  }
	  */
}
