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
import com.hdsoft.hdpay.models.Report_Tool_Model;
import com.hdsoft.other.Repositories.Report_Params;

@Controller
public class Report_Tool 
{
	@Autowired
	public Report_Tool_Model Report;
	
	@RequestMapping(value = { "/Report" }, method = RequestMethod.GET)
    public ModelAndView Report(Model model , HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     mv.setViewName("Others/Report/Report_Tool");   
	    
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    } 
	
	@RequestMapping(value = { "/Report/Dashboard" }, method = RequestMethod.GET)
    public ModelAndView Report_Dashboard(Model model , HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     mv.setViewName("Others/Dashboard/Main_Dashboard");   
	    
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    } 
	
	@RequestMapping(value = { "/Report/Collection-Dashboard" }, method = RequestMethod.GET)
    public ModelAndView Report_Collection_Dashboard(Model model , HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     mv.setViewName("Others/Dashboard/Collection_Dashboard");    
	    
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
    } 
	
	@RequestMapping(value = {"/Report/Types"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Report_Types(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject();
	     
	     details = Report.Get_Reports_Types() ;
	  
	     return details.toString();
    }
	
	@RequestMapping(value = {"/Report/Inputs"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Report_Inputs(@RequestParam("SUBORGCODE") String SUBORGCODE, @RequestParam("REPORT_CODE") String REPORT_CODE, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject(); 
	     
	     details = Report.Get_Reports_Inputs(SUBORGCODE, REPORT_CODE) ;
	  
	     return details.toString();
    }
	
	@RequestMapping(value = {"/Report/Generate"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Report_Generate(@ModelAttribute Report_Params Info, String REPORT_CODE, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     JsonObject details = new JsonObject(); 
	     
	     details = Report.Generate_Reports(Info) ;
	  
	     return details.toString();
    }
	
	@RequestMapping(value = {"/Report/Field/Suggestions"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Report_Inputs_Suggestions(@RequestParam("REPORTCODE") String REPORTCODE, @RequestParam("FIELDNAME") String FIELDNAME, @RequestParam("term") String FIELDVALUE, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
		 JsonArray details = new JsonArray(); 
	    
	     details = Report.Suggestion_Finder(REPORTCODE, FIELDNAME, FIELDVALUE);
	    
	     return details.toString();
    }
}
