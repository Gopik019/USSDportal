package com.hdsoft.hdpay.controllers;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.JsonObject;
import com.hdsoft.common.Menu_Generation;
import com.hdsoft.hdpay.models.Session_Model;
import com.hdsoft.hdpay.Repositories.PAY_001;
import com.hdsoft.hdpay.models.Dashboard_Modal;
import com.hdsoft.hdpay.models.TIPS_Modal.Pay001_Mapper;
import com.hdsoft.utils.FormatUtils;
import com.zaxxer.hikari.HikariDataSource;

@Controller
public class Hdpay_Home 
{
	 public JdbcTemplate Jdbctemplate;
	
	 @Autowired
	 public void setJdbctemplate(HikariDataSource Datasource) 
	 {
		Jdbctemplate = new JdbcTemplate(Datasource);
	 }
	 
	 @Autowired
	 public Dashboard_Modal Dashboard;
	 
	 @Autowired
	 public Menu_Generation MG;
	 
	 /*@PostConstruct
	 public void kka()
	 {
		 String sql = "Select PAYDATE from pay001 where REQSL=?";
		 
		 List<String> info = Jdbctemplate.queryForList(sql, new Object[] { 458 }, String.class);
		 
		 System.out.println(info.get(0));
		 
		 System.out.println(FormatUtils.dynaSQLDate(info.get(0),"YYYY-MM-DD"));
		 
	 }
       */
	 
     @RequestMapping(value = {"/HDPAY/Dashboard" , "/HDPAY/Dashboard/Request_Monotoring" }, method = RequestMethod.GET)
     public ModelAndView Request_Channels_Dashboard(Model model , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
	     ModelAndView mv = new ModelAndView();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Dashboard/Request_Monitoring"); 
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "DASHBOARD", "REQMON"));
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
     
     @RequestMapping(value = {"/HDPAY/Dashboard/Channels"}, method = RequestMethod.GET)
     public ModelAndView Channels_Dashboard(Model model , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
	     ModelAndView mv = new ModelAndView();
	   
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Dashboard/Channels_Dashboard"); 
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "DASHBOARD", "CHNMON"));
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
     
     @RequestMapping(value = {"/HDPAY/Dashboard/Payment_Monitoring"}, method = RequestMethod.GET)
     public ModelAndView Outward_Payments_Dashboard(Model model , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
	     ModelAndView mv = new ModelAndView();
	   
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Dashboard/Payment_Dashboard");  
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "DASHBOARD", "PAYMON"));
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
     
     @RequestMapping(value = {"/HDPAY/Dashboard/Request_Monitoring"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Dashboard_Request_Monitoring(HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject details = new JsonObject();
		  
    	 details = Dashboard.Request_Monitoring();  

	     return details.toString();
     }
      
  /*   @RequestMapping(value = {"/HDPAY/MENU/TEST"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Dashboard_Request_Monitoring_(HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject details = new JsonObject();
		  
    	 details = MG.Get_Menus("HP", "ADMIN", request); 
    	 
    	 session.setAttribute("Menu_details", details);
    	 
    	 details = MG.Get_Menus_HTML(session, "DASHBOARD", "CHNMON");  
    	 
	     return details.toString();
     } */
}