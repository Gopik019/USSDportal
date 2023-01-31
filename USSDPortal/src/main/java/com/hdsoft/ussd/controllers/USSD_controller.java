package com.hdsoft.ussd.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.hdsoft.ussd.Repositories.Bots001;
import com.hdsoft.ussd.Repositories.USSD_Rep;
import com.hdsoft.ussd.models.USSD_model;
import com.hdsoft.ussd.models.USSD_model1;
import com.hdsoft.ussd.models.USSD_model2;

@Controller
public class USSD_controller {
	
	@Autowired
	public USSD_model CUSRREG;

	 @RequestMapping(value = {"/HDPAY/Customer_User_Registration"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
	    public @ResponseBody String Insert_Customer_User_Registration(@ModelAttribute USSD_Rep Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
	    {	 
		     JsonObject details = new JsonObject();
		     
		     details = CUSRREG.Add_Customer(Info,session);
		  
		     return details.toString();
	    }
	 
	 @Autowired
		public USSD_model countrycode;

		 @RequestMapping(value = {"/HDPAY/Customer_User_Registration_Country_code_model"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
		    public @ResponseBody String Insert_Customer_User_Registration_Country_code_model (@ModelAttribute USSD_Rep Info , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
		    {	 
			     JsonObject details = new JsonObject();
			     
			     details = countrycode.Add_Countrycode(Info);
			  
			     return details.toString();
		    }
	 
		 @Autowired
			public USSD_model Get_countrycode;

			 @RequestMapping(value = {"/HDPAY/Customer_User_Registration_Select_Country_code_model"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
			    public @ResponseBody String Select_Customer_User_Registration_Country_code_model (HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
			    {	 
				     JsonObject details = new JsonObject();
				     
				     details = Get_countrycode.select_Countrycode();
				  
				     System.out.println("Add country code"+details);
				     
				     return details.toString();
			    }
	 
			 @Autowired
				public USSD_model Req_Mainmenu ;

				 @RequestMapping(value = {"/USSD/callback"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
				    public @ResponseBody String Request_formain_menu (@ModelAttribute Bots001 B1Info, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
				    {	 
					     JsonObject details = new JsonObject();
					     
					     details = Req_Mainmenu.Find_Menu_details(B1Info, session) ;  // before feedback
					  
					     return details.toString();
				    }	 
			 
			 @Autowired
				public USSD_model1 Req_Mainmenu1 ;

				 @RequestMapping(value = {"/USSD/callback_1"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
				    public @ResponseBody String Request_formain_menu1 (@ModelAttribute Bots001 B1Info, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
				    {	 
					     JsonObject details = new JsonObject();
					     
					     details = Req_Mainmenu1.Find_Menu_details1(B1Info, session) ; //first feedback
					  
					     return details.toString();
				    }
				 
					 @Autowired
						public USSD_model2 Req_Mainmenu2 ;

						 @RequestMapping(value = {"/USSD/callback_2"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
						    public @ResponseBody String Request_formain_menu2 (@ModelAttribute Bots001 B1Info, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
						    {	 
							     JsonObject details = new JsonObject();
							     
							     details = Req_Mainmenu2.Find_Menu_details2(B1Info, session, request) ; //second feedback
							     
							     return details.toString();
						    }
				
			 @Autowired	
				public USSD_model2 Day_Interaction ;

				 @RequestMapping(value = {"/HDPAY/USSD_Day_Notification"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
				    public @ResponseBody String DAY_Interaction_Session (@ModelAttribute Bots001 B1Info, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
				    {	 
					     JsonObject details = new JsonObject();
					     
					     details = Day_Interaction.DAY_Interaction_Session() ;
					  					     
					     return details.toString();
				    }
	 
				 @Autowired	
					public USSD_model2 Week_Interaction;

					 @RequestMapping(value = {"/HDPAY/USSD_Week_Notification"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
					    public @ResponseBody String Week_Interaction_Session (@ModelAttribute Bots001 B1Info, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
					    {	 
						     JsonObject details = new JsonObject();
						     
						     details = Week_Interaction.Week_Interaction_Session();
						  					     
						     return details.toString();
					    } 
				 
					 @Autowired	
						public USSD_model2 Month_Interaction;

						 @RequestMapping(value = {"/HDPAY/USSD_Month_Notification"}, method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
						    public @ResponseBody String Month_Interaction_Session (@ModelAttribute Bots001 B1Info, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
						    {	 
							     JsonObject details = new JsonObject();
							     
							     details = Month_Interaction.Month_Interaction_Session();
							  					     
							     return details.toString();
						    } 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
}
