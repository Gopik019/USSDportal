package com.hdsoft.hdpay.controllers;

import java.io.IOException;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.common.Menu_Generation;
import com.hdsoft.hdpay.models.Session_Model;
import com.hdsoft.hdpay.models.Adminstration;
import com.hdsoft.hdpay.models.Authorize;
import com.zaxxer.hikari.HikariDataSource;

@Controller
public class Hdpay_Admin {
	public JdbcTemplate Jdbctemplate;
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource){
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	@Autowired
	public Adminstration ad;
	
	@Autowired
	public Authorize ud;
	 
	@Autowired
	public Menu_Generation MG;
	 
	@RequestMapping(value = {"/HDPAY/Admin/User_register"}, method = RequestMethod.GET)
    public ModelAndView User_Register(Model model , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
	     ModelAndView mv = new ModelAndView();
	   
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Admin/User_reg");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "ADMINISTRATOR", "USERREG"));
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
	
	@RequestMapping(value = {"/HDPAY/User_Registration"}, method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String User_Registration_(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
    {	 
		JsonObject details = new JsonObject();
   	 
   	 	if(Session_Model.IsSessionValid(session)){
   	 		JsonObject InaddPropertys = new JsonObject();
	   
			InaddPropertys.addProperty("torgcd", request.getParameter("torgcd") != null ? request.getParameter("torgcd") : "");System.out.println(request.getParameter("torgcd"));
			InaddPropertys.addProperty("tuserid", request.getParameter("tuserid") != null ? request.getParameter("tuserid") : "");System.out.println(request.getParameter("tuserid"));
			InaddPropertys.addProperty("tusernme", request.getParameter("tusernme") != null ? request.getParameter("tusernme") : "");
			InaddPropertys.addProperty("tbirthdate", request.getParameter("tbirthdate") != null ? request.getParameter("tbirthdate") : "");
			InaddPropertys.addProperty("tmobile", request.getParameter("tmobile") != null ? request.getParameter("tmobile") : "");
			InaddPropertys.addProperty("temail", request.getParameter("temail") != null ? request.getParameter("temail") : "");
			InaddPropertys.addProperty("tcomaddr", request.getParameter("tcomaddr") != null ? request.getParameter("tcomaddr") : "");
			InaddPropertys.addProperty("tconfirmpwd", request.getParameter("tconfirmpwd") != null ? request.getParameter("tconfirmpwd") : "");
			InaddPropertys.addProperty("trolecd", request.getParameter("trolecd") != null ? request.getParameter("trolecd") : "");
			InaddPropertys.addProperty("tregdate", request.getParameter("tregdate") != null ? request.getParameter("tregdate") : "");
			InaddPropertys.addProperty("branchcd", request.getParameter("branchcd") != null ? request.getParameter("branchcd") : "");
			 
			InaddPropertys.addProperty("hashedPassword", request.getParameter("hashedPassword") != null ? request.getParameter("hashedPassword") : "");
			InaddPropertys.addProperty("randomSalt", request.getParameter("randomSalt") != null ? request.getParameter("randomSalt") : "");
			
			String suborgcode = "EXIM";
			
			InaddPropertys.addProperty("mode", "I");
			InaddPropertys.addProperty("suborgcode", suborgcode);
			
			if(session.getAttribute("sesUserId").toString().trim() != null || session.getAttribute("sesUserId").toString().trim() != "" )
			{ 
				InaddPropertys.addProperty("sesUserId", session.getAttribute("sesUserId").toString().trim() ) ;
				InaddPropertys.addProperty("sesMcontDate", session.getAttribute("sesMcontDate").toString().trim() ) ;
				InaddPropertys.addProperty("sesDomainID", session.getAttribute("sesDomainID").toString().trim() ) ;
			}
			 
			InaddPropertys.addProperty("LOG_DOMAIN_ID", session.getAttribute("sesDomainID").toString());
			InaddPropertys.addProperty("LOG_USER_ID", session.getAttribute("sesUserId").toString());
			InaddPropertys.addProperty("LOG_USER_IP", request.getRemoteAddr());
			InaddPropertys.addProperty("programid", "userregistration");
			
			logger.debug("<<<<<<<<<<<<<<<<<<< Entering Into User Registeration >>>>>>>>>>>>>>>>>>>");
			
			JsonObject outaddPropertys = ad.updateValues(InaddPropertys);
			String val = outaddPropertys.get("Result").getAsString();
			if(val.equals("Success")) {
				details.addProperty("Result", "Success");
				details.addProperty("Message", "Record Inserted Successfully !!");
			}else{
				details.addProperty("Result", "Failed");
				details.addProperty("Message", "Something Went Wrong !!");
			}
			 
			System.out.println("<<<<<<<<<<<<<<<<< End Of Main Step >>>>>>>>>>>>>>>");

   	 	}
   	 else
   	 {
   		 details.addProperty("Result", "Failed");
			 details.addProperty("Message", "Please re-login for security concerns");
   	 }
   	 
	     return details.toString();
    }
	
	@RequestMapping(value = {"/HDPAY/Password_Reset"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String Password_Reset_(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {	 
   	 JsonObject details = new JsonObject();
   	 
   	JsonObject InaddPropertys = new JsonObject();

   	 if(Session_Model.IsSessionValid(session))
	     {
	 		 try
	 		 {
	 			InaddPropertys.addProperty("torgcd", request.getParameter("torgcd"));
	 			InaddPropertys.addProperty("tuserid", request.getParameter("tuserid"));
	 			InaddPropertys.addProperty("hashedPassword", request.getParameter("hashedPassword"));
	 			InaddPropertys.addProperty("randomSalt", request.getParameter("randomSalt"));
	 			
	 			JsonObject resultDTO = ad.updateValues_Password(InaddPropertys, session);
	 			
	 			String val = resultDTO.get("sucFlg").getAsString();
	 			
	 			if(val.equals("1"))
				{
					details.addProperty("Result", "Success");
					details.addProperty("Message", "Succesfully Updated !!");
				}
				else
				{
					details.addProperty("Result", "Failed");
					details.addProperty("Message", "Something went Wrong !!");
				}	
			}
			catch (Exception e)
			{
				details.addProperty("Result", "Failed");
				details.addProperty("Message", e.getLocalizedMessage()); e.printStackTrace();
			}
	     }
   	 else
   	 {
   		 details.addProperty("Result", "Failed");
			 details.addProperty("Message", "Please re-login for security concerns");
   	 }
   	 
	     return details.toString();
    } 
	 @RequestMapping(value = {"/HDPAY/Info/Get_auth001_Info"}, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Get_auth001_Info(Model model , HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException, ParseException 
     {	 
    	 JsonObject details = new JsonObject();
    	 
    	 if(Session_Model.IsSessionValid(session))
 	     {
    		 String pgmid = request.getParameter("pgmid");
        	 
        	 details = ad.Get_auth001_Info(pgmid);
 	     }
    	 else
    	 {
    		 details.addProperty("Result", "Failed");
			 details.addProperty("Message", "Please re-login for security concerns");
    	 }

	     return details.toString();
     }
     
     @RequestMapping(value = {"/HDPAY/Info/pk/Get_auth003_Info"}, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Get_auth003_Info_by_pk(Model model , HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException, ParseException 
     {	 
    	 JsonObject details = new JsonObject();
    	 
    	 if(Session_Model.IsSessionValid(session))
 	     {
    		 String pk = request.getParameter("pk");
    		 
    		 logger.debug("PRIMARY KEY ::::::::::::::::"+pk);
        	 
        	 details = ad.Get_auth003_Info_by_pk(pk);	
 	     }
    	 else
    	 {
    		 details.addProperty("Result", "Failed");
			 details.addProperty("Message", "Please re-login for security concerns");
    	 }
    	
	     return details.toString();
     }
     
     @RequestMapping(value = {"/HDPAY/Suggestions/User_Id_Check"}, method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_User_Id(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	JsonArray User_Ids = new JsonArray();
    	if(Session_Model.IsSessionValid(session))
	    {
    		String Search_Word = request.getParameter("term");
     		System.out.println(Search_Word);
     		User_Ids =  ad.User_Id_Suggestions(Search_Word, request); 
	    }
		return User_Ids.toString();
     } 
     
     @RequestMapping(value = {"/HDPAY/Suggestions/User_Id"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_UserId(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject User_Ids = new JsonObject();
    	
    	if(Session_Model.IsSessionValid(session))
	    {
    		String Search_Word = request.getParameter("user");
    		
    		System.out.println(Search_Word);

    		User_Ids =  ad.User_Id_Check(Search_Word, request); 
	    }else {
	    	User_Ids.addProperty("Result", "Failed");
			User_Ids.addProperty("Message", "Please relogin for security concern");
	    }
    	
		return User_Ids.toString();
     } 
     @RequestMapping(value = {"/HDPAY/Suggestions/User_Id/Test"}, method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_User_Id_Test(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	JsonArray User_Ids = new JsonArray();
    	
    	System.out.println("Calling API.....");
    	
    	//if(Session_Model.IsSessionValid(session))
	    //{
    		String Search_Word = request.getParameter("term");
     		
     		User_Ids =  ad.User_Id_Suggestions(Search_Word, request); 
	   // }
     		System.out.println("User_Ids ::....."+User_Ids);
 		
		return User_Ids.toString();
     } 
     
     @RequestMapping(value = {"/HDPAY/Suggestions/ORGCODE"}, method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_ORGCODE(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	JsonArray Org_Codes = new JsonArray();
    	
    	if(Session_Model.IsSessionValid(session))
	    {
    		String Search_Word = request.getParameter("term");
     		Org_Codes =  ad.Org_Code_Suggestions(Search_Word, request); 
	    }
    	
		return Org_Codes.toString();
     } 
     
     @RequestMapping(value = {"/HDPAY/Suggestions/BRANCHCODE_Value"}, method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_BRANCHCODE(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject Branch_Codes = new JsonObject();
    	
    	if(Session_Model.IsSessionValid(session))
	    {
    		
    		Branch_Codes = ad.All_Branch_Code();
    		
    		logger.debug(Branch_Codes);
    		
	    }
    	
		return Branch_Codes.toString();
     } 
     
     @RequestMapping(value = {"/HDPAY/Suggestions/ROLECODE"}, method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_ROLECODE(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	JsonArray Branch_Codes = new JsonArray();
    	
    	if(Session_Model.IsSessionValid(session))
	    {
    		String Search_Word = request.getParameter("term");
     		
     		Branch_Codes =  ad.Role_Code(Search_Word, request); 
	    }
 		
		return Branch_Codes.toString();
     } 
     
     @RequestMapping(value = {"/HDPAY/QUEUECHECKER"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String QUEUECHECKER(HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject details = new JsonObject();
 
    	 if(Session_Model.IsSessionValid(session))
	     {
	    	 JsonObject formDTO = new JsonObject();
	    	 
	    	 JsonObject resultDTO  = new JsonObject();
					 
			 try
			 {
				 formDTO.addProperty("tuserid", request.getParameter("tuserid"));
				 formDTO.addProperty("torgcd", request.getParameter("torgcd"));
				 formDTO.addProperty("pgmID", request.getParameter("pgmID"));
				
				 resultDTO = ad.QUEUECHECKER(formDTO);
				 
				 logger.debug(resultDTO);
				 details.addProperty("mode", resultDTO.get("mode").getAsString());
				 resultDTO = ad.QUEUECHECKER2(formDTO);
				
				 logger.debug(resultDTO);
				 
									
				details.addProperty("STATUS", resultDTO.get("STATUS").getAsString());
				details.addProperty("SucFlg", resultDTO.get("SucFlg").getAsString());
				
				details.addProperty("Result", "Success");
				details.addProperty("Message", "Success");					
			}
			catch(Exception e)
			{
				details.addProperty("Result", "Failed");
				details.addProperty("Message", e.getLocalizedMessage());
				
				logger.debug("Exception when Queue checker ::::: "+e.getLocalizedMessage());
			}
	     }
    	 else
    	 {
    		 details.addProperty("Result", "Failed");
			 details.addProperty("Message", "Please re-login for security concerns");
    	 }
    	 
	    return details.toString();
     }
     
     @RequestMapping(value = {"/HDPAY/Block_Unblock_User"}, method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Block_Unblock_User(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject details = new JsonObject();
    	 
    	 if(Session_Model.IsSessionValid(session))
	     {
	    	 JsonObject InaddPropertys = new JsonObject();
	   
	    	 InaddPropertys.addProperty("tuserid", request.getParameter("tuserid"));
	    	 InaddPropertys.addProperty("tbublock", request.getParameter("tbublock"));
	    	 InaddPropertys.addProperty("mode", request.getParameter("mode"));
	    	   	
	    	 String suborgcode = "EXIM";
	    	 
	    	 //InaddPropertys.addProperty("mode", "I");
	    	 InaddPropertys.addProperty("suborgcode", suborgcode);
	    	 
	    	 if(session.getAttribute("sesUserId").toString().trim() != null || session.getAttribute("sesUserId").toString().trim() != "" )
	    	 { 
	    		 InaddPropertys.addProperty("sesUserId", session.getAttribute("sesUserId").toString().trim() ) ;
	    		 InaddPropertys.addProperty("sesMcontDate", session.getAttribute("sesMcontDate").toString().trim() ) ;
	    		 InaddPropertys.addProperty("sesDomainID", session.getAttribute("sesDomainID").toString().trim() ) ;
			 }
	    	 
	    	 InaddPropertys.addProperty("userIp", session.getAttribute("sesUserId").toString());
	    	 InaddPropertys.addProperty("programid", "unblockuserid");
	
	    	 JsonObject outaddPropertys = ad.updateValues_for_UnblockUser(InaddPropertys);
	    	 String val = outaddPropertys.get("Result").getAsString();
	    	 if(val.equals("Success")) 
	    	 {
	    		 details.addProperty("Result", "Success");
				 details.addProperty("Message", "Record updated Successfully !!");
	    	 }
	    	 else
	    	 {
	    		 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Something Went Wrong !!");
	    	 }
	    	 
	    	 System.out.println("<<<<<<<<<<<<<<<<< End Of Main Step >>>>>>>>>>>>>>>");
	     }
    	 else
    	 {
    		 details.addProperty("Result", "Failed");
			 details.addProperty("Message", "Please re-login for security concerns");
    	 }
    	 
	     return details.toString();
     }
     
     @RequestMapping(value = {"/HDPAY/Admin/Authorize"}, method = RequestMethod.GET)
     public ModelAndView Setting_Approval(Model model , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
	     ModelAndView mv = new ModelAndView();///Portal/src/main/webapp/Views/HDPAY/Admin/Admin_Approval.jsp
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Admin/Admin_Approval"); 
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "AUTHORIZATION", "ADMINAPR"));
	     }
	     else
	     {
	    	 mv.setViewName("redirect:/login");
	     }
	   
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
     }
     @RequestMapping(value = {"/HDPAY/Admin_Approval"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Admin_Approval_(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject details = new JsonObject();
 
    	 if(Session_Model.IsSessionValid(session))
	     {
	    	 JsonObject formDTO = new JsonObject();
	    	 
	    	 JsonObject resultDTO  = new JsonObject();
					 
			 try
			 {
				formDTO.addProperty("txtArgs", request.getParameter("txtArgs"));
				formDTO.addProperty("LOG_USER_IP", request.getRemoteAddr());
				
				resultDTO = ud.updateValues(formDTO);
				logger.debug(resultDTO);
				String val = resultDTO.get("sucFlg").getAsString();
				String val1 = resultDTO.get("result").getAsString();
				
				logger.debug(val+"************"+val1);
				
				logger.debug(resultDTO);
				
				if(val.equals("1") && !val1.equals("ERROR"))
				{
					logger.debug(resultDTO);
					details.addProperty("programid", resultDTO.get("programid").getAsString());
					String pgmid = resultDTO.get("programid").getAsString();
					logger.debug(resultDTO.get("programid").getAsString());
					details.addProperty("AuthRej", resultDTO.get("AuthRej").getAsString());
					logger.debug(resultDTO.get("AuthRej").getAsString());
					details.addProperty("result", resultDTO.get("result").getAsString());
					logger.debug(resultDTO.get("result").getAsString());
					details.addProperty("sucFlg", resultDTO.get("sucFlg").getAsString());
					logger.debug(resultDTO.get("sucFlg").getAsString());
					details.addProperty("CUSTGRP", resultDTO.get("CUSTGRP").getAsString());
					logger.debug(resultDTO.get("CUSTGRP").getAsString());
					details.addProperty("INVOICENO", resultDTO.get("INVOICENO").getAsString());
					logger.debug(resultDTO.get("INVOICENO").getAsString());
					if(pgmid.equalsIgnoreCase("udompay") || pgmid.equalsIgnoreCase("sjuit")|| pgmid.equalsIgnoreCase("gepgpay")|| pgmid.equalsIgnoreCase("taxcoll")) {
						details.addProperty("PAYSL", resultDTO.get("PAYSL").getAsString());
					}
					
					logger.debug(resultDTO);
					
					logger.debug(details);
					
					details.addProperty("Result", "Success");
					details.addProperty("Message", "Succesfully Updated !!");
				}
				else
				{
					details.addProperty("Result", "Failed");
					details.addProperty("Message", resultDTO.get("result").getAsString()); 
				}
			}
			catch(Exception e)
			{
				details.addProperty("sucFlag", "0");
				
				details.addProperty("Result", "Failed");
				details.addProperty("Message", e.getLocalizedMessage());
				
				logger.debug("Exception when Admin Approval ::::: "+e.getLocalizedMessage());
			}
	     }
    	 else
    	 {
    		 details.addProperty("Result", "Failed");
			 details.addProperty("Message", "Please re-login for security concerns");
    	 }
    	 
	    return details.toString();
     }
     
     @RequestMapping(value = {"/HDPAY/validateAdmPgmId"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String validateAdmPgmId(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject details = new JsonObject();
    	 
    	
    	 if(Session_Model.IsSessionValid(session))
	     {
	    	 String auguments = request.getParameter("auguments");
	 		
	    	 logger.debug(auguments);
	    	 JsonObject res = ad.validateAdmPgmId(auguments);
	    	 
	    	 logger.debug("PGMUD :::::::::::: "+res.get("PGMIDs"));
	    	 
	    	 String val = res.get("sucFlg").getAsString();
	    	 logger.debug(val);
	    	 
	    	 if(val.equals("1"))
	    	 {
	    		 details.addProperty("sucFlg", "1");
	    		 
	    		 details.add("PGMID1", res.get("PGMIDs"));
	    		 logger.debug("PGMUD :::::::::::: "+details);
	    		 details.addProperty("CATEGORY", res.get("CATEGORY").getAsString());
	    		 
	    		 details.addProperty("Result", "Success");
				 details.addProperty("Message", "Record updated Successfully!");
	    	 }
	    	 else
	    	 {
	    		 details.addProperty("Result", "Failed");
			     details.addProperty("Message", "SOMEthing went Wrong !!!");
	    	 }
	     }
    	 else
    	 {
    		 details.addProperty("Result", "Failed");
			 details.addProperty("Message", "Please re-login for security concerns");
    	 } 
    	 
	     return details.toString();
     }
     
     @RequestMapping(value = {"/HDPAY/Suggestions/Unblock/User_Id"}, method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Suggestions_User_Id1(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	JsonArray User_Ids = new JsonArray();
    	
    	if(Session_Model.IsSessionValid(session))
	    {
    		String Search_Word = request.getParameter("term");
     		
     		User_Ids =  ud.User_Id_Unblock_Suggestions(Search_Word, request); 
	    }
    	
 		
		return User_Ids.toString();
     } 
     
		/*
		 * @RequestMapping(value = {"/HDPAY/ADMIN/Block_unblock"}, method =
		 * RequestMethod.GET) public ModelAndView Setting_Block_user(Model model ,
		 * HttpServletRequest request, HttpServletResponse response, HttpSession
		 * session) throws IOException { ModelAndView mv = new ModelAndView();
		 * 
		 * if(Session_Model.IsSessionValid(session)) {
		 * mv.setViewName("HDPAY/Admin/Block_Unblock_User");
		 * 
		 * mv.addObject("Menu", MG.Get_Menus_HTML(session, "AUTHORIZATION",
		 * "BLKUNBLK")); } else { mv.setViewName("redirect:/login"); }
		 * 
		 * response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		 * response.setHeader("Pragma","no-cache"); response.setHeader("Expires","0");
		 * 
		 * return mv; }
		 */
     
     @RequestMapping(value = {"/HDPAY/ADMIN/ResetPass"}, method = RequestMethod.GET)
     public ModelAndView Profile_Reset(Model model , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
	     ModelAndView mv = new ModelAndView();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Admin/resetpassword");
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "ADMINISTRATOR", "PASSRESET"));
	     }
	     else
	     {
	    	 mv.setViewName("redirect:/login");
	     }
	   
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
     }
     
	 @RequestMapping(value = {"/HDPAY/ADMIN/ProfUpload"}, method = RequestMethod.GET)
     public ModelAndView Profile_Image(Model model , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
	     ModelAndView mv = new ModelAndView();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Admin/Profile_Image_Upload"); 
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "ADMINISTRATOR", "PROIMG"));
	     }
	     else
	     {
	    	 mv.setViewName("redirect:/login");
	     }
	   
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
     }
	 
	 @RequestMapping(value = {"/HDPAY/Profile_Image_Upload"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody String Profile_Image_Upload(@RequestParam("Image") CommonsMultipartFile Image, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
    	 JsonObject details = new JsonObject();
    	 
    	 JsonObject InaddPropertys = new JsonObject();

    	 if(Session_Model.IsSessionValid(session))
	     {
	 		 try
	 		 {
	 			InaddPropertys.addProperty("hiddomainId", session.getAttribute("sesDomainID").toString());
	 			InaddPropertys.addProperty("hiduserId", session.getAttribute("sesUserId").toString());
	 			InaddPropertys.addProperty("LOG_DOMAIN_ID", session.getAttribute("sesDomainID").toString());
	 			InaddPropertys.addProperty("LOG_USER_ID", session.getAttribute("sesUserId").toString());
	 			
	 			logger.debug(InaddPropertys);
	 				
	 			JsonObject resultDTO = ad.updateValues(Image,  InaddPropertys);
	 			
	 			logger.debug(resultDTO);
	 			
	 			//String val = resultDTO.get("sucFlag").getAsString();
	 			
	 			if(resultDTO.get("sucFlag").getAsString().equals("1"))
				{
					
					String Photo = ad.loadImage(session.getAttribute("sesDomainID").toString(), session.getAttribute("sesUserId").toString(), request);
					 
					session.setAttribute("sess_user_photo", Photo);
					
					details.addProperty("Result", "Success");
					details.addProperty("Message", "Succesfully Updated !!");
				}
				else
				{
					details.addProperty("Result", "Failed");
					details.addProperty("Message", "Something went Wrong !!");
				}	
			}
			catch (Exception e)
			{
				details.addProperty("Result", "Failed");
				details.addProperty("Message", e.getLocalizedMessage()); e.printStackTrace();
			}
	     }
    	 else
    	 {
    		 details.addProperty("Result", "Failed");
			 details.addProperty("Message", "Please re-login for security concerns");
    	 }
    	 
	     return details.toString();
     }
	 @RequestMapping(value = {"/HDPAY/Admin/User_Password_Reset"}, method = RequestMethod.GET)
     public ModelAndView Setting_Uswer_Passrst(Model model , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
     {	 
	     ModelAndView mv = new ModelAndView();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	    	 mv.setViewName("HDPAY/Admin/Password_Reset"); 
	    	 
	    	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "ADMINISTRATOR", "PASSRESET"));
	     }
	     else
	     {
	    	 mv.setViewName("redirect:/login");
	     }
	   
	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
     }


@RequestMapping(value = {"/HDPAY/ADMIN/Block_unblock"}, method = RequestMethod.GET)
public ModelAndView Block_unblock_User(Model model ,HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException 
{	 
	     ModelAndView mv = new ModelAndView();
	     
	     if(Session_Model.IsSessionValid(session))
	     {
	 	 mv.addObject("Title", "Block / Unblock User");
	     
	 	 mv.addObject("Menu", MG.Get_Menus_HTML(session, "AUTHORIZATION", "BLKUNBLK"));
	     	 
	     	 mv.setViewName("HDPAY/Admin/Block_Unblock_User"); 
	     }
	     else
	     {
	    	 mv.setViewName("redirect:/login");
	     }

	     response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	     response.setHeader("Pragma","no-cache");
	     response.setHeader("Expires","0");

	     return mv;
}

}
	 
	 

