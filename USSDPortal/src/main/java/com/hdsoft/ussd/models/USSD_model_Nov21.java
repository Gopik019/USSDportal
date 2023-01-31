package com.hdsoft.ussd.models;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.ussd.Repositories.Bots001;
import com.hdsoft.ussd.Repositories.Bots002;
import com.hdsoft.ussd.Repositories.IPS_ACCOUNT_VIEW001;
import com.hdsoft.ussd.Repositories.Menu007;
import com.hdsoft.ussd.Repositories.Menu008;
import com.hdsoft.ussd.Repositories.USSD_Rep;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class USSD_model_Nov21 
{
	public JdbcTemplate Jdbctemplate;
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	String SUBORGCODE = "EXIM";
	

	
	public JsonObject Find_Menu_details(Bots001 B1Info, HttpSession session) 
	{
		JsonObject details = new JsonObject();	
				
		try
		{	
			Common_Utils utils = new Common_Utils();
			String Session_Id = session.getId();      // Second Session Id
		    String CHCODE = B1Info.getCHCODE();       //Ussd Mobile app data
		    String MOBILENO = B1Info.getMOBILENO();
		    //String MB_MENUSL = B1Info.getINPUT();  
			String SERCODE = utils.ReplaceNull(B1Info.getSERCODE()) ;
			String SELECTCD = utils.ReplaceNull(B1Info.getINPUT());
		    String COUNTRYCD = utils.ReplaceNull(B1Info.getADDINFO1());
        	String RESMSG = utils.ReplaceNull(B1Info.getADDINFO2());
        	String sessionid = session.getId();
        	Common_Utils util = new Common_Utils();
	        String commdate = util.getCurrentDate("dd-MMM-yyyy");
			Timestamp commtime = util.get_oracle_Timestamp();
			String Initial_Regcode = "M001";
			String Initial_NonRegcode = "M00EN";
			
			
			String sql = "select count(*) from bots001 where mobileno = ? and CHCODE= ?";         //Get Mobile no from Bots001(Customer Registration)
			
			int cntofmobileno = Jdbctemplate.queryForObject(sql, new Object[] { MOBILENO, CHCODE  }, Integer.class);
			
			if(cntofmobileno == 0)//---------Non Registered Mobile No in customer user registration
			{
				 if(utils.isNullOrEmpty(SERCODE))
				 {
					 details = check_Reg_NonReg(CHCODE,Initial_NonRegcode,MOBILENO,COUNTRYCD,Session_Id);
			      	 Insert_bot004(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,RESMSG); //Adding Response in bots004 
					 Insert_bot002(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,Session_Id,commtime,Initial_NonRegcode);  //Adding Current menu in bots002(Request API)
			      	 //System.out.println(SUBORGCODE +CHCODE+commdate+MOBILENO+COUNTRYCD+sessionid+commtime+SERCODE+SELECTCD+RESMSG);
					 return details;
				 }
				 
				
				boolean flag = false; 
				 
				if (B1Info.getADDINFO2().equalsIgnoreCase("Create a new account?") && B1Info.getSERCODE().equalsIgnoreCase("M00EN")) {
					 flag=true;
		        	 SERCODE = "M002";
		        	 details = REQ_Jsondata(CHCODE,SERCODE, "0",commdate,MOBILENO,COUNTRYCD,Session_Id,commtime);
				} 
		        if (B1Info.getADDINFO2().equalsIgnoreCase("Please Enter your Full Name") && B1Info.getSERCODE().equalsIgnoreCase("M002")) {
		        	flag=true;
		        	details = REQ_Jsondata(CHCODE, SERCODE, "1",commdate,MOBILENO,COUNTRYCD,Session_Id,commtime);
				}
		        if (B1Info.getADDINFO2().equalsIgnoreCase("Please enter your contact number") && B1Info.getSERCODE().equalsIgnoreCase("M002")) {
		        	flag=true;
		        	details = REQ_Jsondata(CHCODE, SERCODE, "2",commdate,MOBILENO,COUNTRYCD,Session_Id,commtime);
				}
		        if (B1Info.getADDINFO2().equalsIgnoreCase("Please enter your Date Of Birth") && B1Info.getSERCODE().equalsIgnoreCase("M002"))  {
		        	flag=true;
		        	details = RES_Jsondata(CHCODE, SERCODE, "3",commdate,MOBILENO,COUNTRYCD,Session_Id,commtime);
		        	Insert_bot001(B1Info);   //Adding Mobileno in bots001
				}
		        if (B1Info.getADDINFO2().equalsIgnoreCase("One of our agent will be contact you shortly") && B1Info.getSERCODE().equalsIgnoreCase("M002"))  {
		        	details = RES_Jsondata(CHCODE, SERCODE, "99");
		        }		        
		        
		        if(flag == true) {
		         
				          if (B1Info.getADDINFO2().equalsIgnoreCase("Create a new account?")) {
				        	  SERCODE="M00EN";
							  Insert_bot002(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,Session_Id,commtime,SERCODE);  //Adding Current menu in bots002(Request API)
					          // Insert_bot002(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE); //Adding Current menu in bots002(Response API)   !!If we need to store the Response data current menu uncomment this line
					      	  Insert_bot004(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,RESMSG); //Adding Response in bots004 
						  } 
				          else {
							  Insert_bot002(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,Session_Id,commtime,SERCODE);  //Adding Current menu in bots002(Request API)
					          //Insert_bot002(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE); //Adding Current menu in bots002(Response API)   !!If we need to store the Response data current menu uncomment this line
					      	  Insert_bot004(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,RESMSG); //Adding Response in bots004 
						  }

		        }

		        return details;
				 
				 
			} 
			else {//---------Registration Mobile No
				
				String sql1 = "select * from bots002 where mobileno = ? and commdate = ? order by commtime desc";   //Check whether conversation is crossed 15min or not
				 
				List<Bots002> info = Jdbctemplate.query(sql1, new Object[] { B1Info.getMOBILENO(),B1Info.getCOMMDATE() },new Bots002Mapper());
				
				if(info.size() == 0)                                
				{				
					if(utils.isNullOrEmpty(SERCODE))
					 {
						 details = check_Reg_NonReg(CHCODE,Initial_Regcode,MOBILENO,COUNTRYCD,Session_Id);	
				      	 Insert_bot004(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,RESMSG); //Adding Response in bots004 
						 Insert_bot002(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,Session_Id,commtime,Initial_Regcode);  //Adding Current menu in bots002(Request API)
				      	 //System.out.println(SUBORGCODE +CHCODE+commdate+MOBILENO+COUNTRYCD+sessionid+commtime+SERCODE+SELECTCD+RESMSG);
						 return details;
					 }
					 			
					if (B1Info.getADDINFO2().equalsIgnoreCase("Balance Inquiry") && B1Info.getSERCODE().equalsIgnoreCase("M001")) 
					{    SERCODE = "M0021";
			        	 details = ACC_VIEW(CHCODE,MOBILENO,COUNTRYCD,SERCODE,"0",commdate,Session_Id,commtime,B1Info.getINPUT());
					}  
					if (B1Info.getADDINFO2().equalsIgnoreCase("Enter the passcode") && B1Info.getSERCODE().equalsIgnoreCase("M0021")) 
					{    
				         details = ACC_VIEW(CHCODE,MOBILENO,COUNTRYCD,SERCODE,"0",commdate,Session_Id,commtime,B1Info.getINPUT());
					}
				} 
				else    
				{
					
					//details = Get_current_menu(B1Info);                                  
				}
				
			}
			
			
			  
					 
			//details.addProperty("Result", "Success");
			//details.addProperty("Message", "Country code Added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage()); 	
			 
			 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
	}
	
		
	
	public JsonObject check_Reg_NonReg(String CHCODE,String menucode,String MOBILENO,String COUNTRYCD,String Session_Id) 
	{
		
		JsonObject details = new JsonObject();
		
		
		try
		{	
			
				String sql = "select * from menu007 where CHCODE = ? and M1CODE = ?";
			 
				List<Menu007>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode } ,new Menu007Mapper ());
				
				JsonArray js = new JsonArray();
				
				for(int i=0; i<Info.size(); i++)
				{					
					JsonObject job = new JsonObject();
					
					job.addProperty("Serial", Info.get(i).getMENUSL());
					job.addProperty("SERCODE", Info.get(i).getM1CODE());
					job.addProperty("DESCRIPTION", Info.get(i).getMENUDESC());
					
		        	Common_Utils util = new Common_Utils();
			        String commdate1 = util.getCurrentDate("dd-MMM-yyyy");
					Timestamp commtime1 = util.get_oracle_Timestamp();
					
			    	if (menucode.equalsIgnoreCase("M00EN") || menucode.equalsIgnoreCase("M001")) {
						Insert_bot003(SUBORGCODE,CHCODE,commdate1,MOBILENO,COUNTRYCD,Session_Id,commtime1,Info.get(i).getM1CODE(),Info.get(i).getMENUSL(),Info.get(i).getMENUDESC()); //Adding Request in bots003 
					}

					
					js.add(job);
				}
				
				Common_Utils util = new Common_Utils();  
				
				details.addProperty("Paytype", "demo");
				details.addProperty("CHCODE", CHCODE);
				details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
				details.addProperty("TYPE", "REQ");	
				
				details.add("data", js);
				 			 
				details.addProperty("result", "success");
				details.addProperty("stscode", "HP00");
				details.addProperty("message", "Menus found !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
	}

	public JsonObject REQ_Jsondata(String CHCODE,String Menucode,String MENUSL,String commdate,String MOBILENO,String COUNTRYCD,String Session_Id,Timestamp commtime) 
	{
		JsonObject details = new JsonObject();
		
		try
		{	
			//Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,REQMSG); //Adding Request in bots003 

				MENUSL = (Integer.parseInt(MENUSL)+1)+"";
			
				String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? and MENUSL = ?";
				 
				List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, Menucode , MENUSL } ,new Menu008Mapper ());
							
				JsonArray js = new JsonArray();
				
				
				if (Info.size() != 0) {
					
                    JsonObject job = new JsonObject();
					
					job.addProperty("Serial",Info.get(0).getMENUSL());
					job.addProperty("SERCODE",Info.get(0).getM2CODE());
					job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
					
					Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,Session_Id,commtime,Info.get(0).getM2CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding Request in bots003 
			
					js.add(job);
					
				}

				Common_Utils util = new Common_Utils();  
				
				details.addProperty("Paytype", "demo");
				details.addProperty("CHCODE", CHCODE);
				details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
				details.addProperty("TYPE", "REQ");	
				
				details.add("data", js);
				 			 
				details.addProperty("result", "success");
				details.addProperty("stscode", "HP00");
				details.addProperty("message", "Menus found !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
	}
		
	
	public JsonObject RES_Jsondata(String CHCODE, String Menucode ,String MENUSL,String commdate,String MOBILENO,String COUNTRYCD,String Session_Id,Timestamp commtime) 
	{
		JsonObject details = new JsonObject();
		
		try
		{	
			
				MENUSL = (Integer.parseInt(MENUSL)+1)+"";
			
				String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? and MENUSL = ?";
				 
				List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, Menucode , MENUSL } ,new Menu008Mapper ());
							
				JsonArray js = new JsonArray();
				
				
				if (Info.size() != 0) {
					
                    JsonObject job = new JsonObject();
					
					job.addProperty("Serial",Info.get(0).getMENUSL());
					job.addProperty("SERCODE",Info.get(0).getM2CODE());
					job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
					
					Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,Session_Id,commtime,Info.get(0).getM2CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding Request in bots003 
					
					js.add(job);
					
				}

				Common_Utils util = new Common_Utils();  
				
				details.addProperty("Paytype", "demo");
				details.addProperty("CHCODE", CHCODE);
				details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
				details.addProperty("TYPE", "RES");	
				
				details.add("data", js);
				 			 
				details.addProperty("result", "success");
				details.addProperty("stscode", "HP00");
				details.addProperty("message", "Menus found !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
	}
	
	
	public JsonObject ACC_VIEW(String CHCODE,String MOBILENO,String COUNTRYCD,String SERCODE,String MENUSL,String commdate,String Session_Id,Timestamp commtime,String INPUT) 
	{ 
		JsonObject details = new JsonObject();
		
				try
				{ 
					String sql = "select * from ips_account_view001 where MOBILENO = ? and COUNTRYCD = ?";
					
					 List<IPS_ACCOUNT_VIEW001>  Info = Jdbctemplate.query(sql, new Object[] { MOBILENO,COUNTRYCD } ,new IPS_ACCOUNT_VIEW001Mapper ());
					
					 String CBS_Mbno = Info.get(0).getMOBILENO();
					    
					    			
							   if (MOBILENO.equalsIgnoreCase(CBS_Mbno)) {  //Mobileno is Bots001 mobile no || CBS_Mobileno is IPS_ACCOUNT_VIEW001 mobile no
										
								   
									 if(INPUT.equalsIgnoreCase("1")) {
								  		details = REQ_Jsondata(CHCODE,SERCODE,MENUSL,commdate,MOBILENO,COUNTRYCD,Session_Id,commtime);	 
									 }
									 
									 if (INPUT.equalsIgnoreCase("1234")) {
										 details =  Balance_view(CHCODE,SERCODE,MENUSL,commdate,MOBILENO,COUNTRYCD,Session_Id,commtime);	 
									 }
								}
			    }
				catch(Exception e)
				 {
					 details.addProperty("Result", "Failed");
					 details.addProperty("Message", e.getLocalizedMessage());   	
					 
					 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
				 }
		
		
		 return details;
	}	
	
	
	
	
	public JsonObject Balance_view(String CHCODE,String SERCODE,String MENUSL,String commdate,String MOBILENO,String COUNTRYCD,String Session_Id,Timestamp commtime) 
	{
		JsonObject details = new JsonObject();
		
		try
		{	
			//Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,REQMSG); //Adding Request in bots003 
			
				String sql = "select * from ips_account_view001 where MOBILENO = ? and COUNTRYCD = ?";
				 
				List<IPS_ACCOUNT_VIEW001>  Info = Jdbctemplate.query(sql, new Object[] { MOBILENO, COUNTRYCD } ,new IPS_ACCOUNT_VIEW001Mapper ());
							
				JsonArray js = new JsonArray();
				
				
				if (Info.size() != 0) {
					
                    JsonObject job = new JsonObject();
					
					job.addProperty("AccountNo",Info.get(0).getACCNO());
					job.addProperty("MobileNo",Info.get(0).getMOBILENO());
					job.addProperty("DESCRIPTION","Available Balance "+Info.get(0).getCURRENCY()+"."+Info.get(0).getBALANCE());
					
					Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,Session_Id,commtime,SERCODE,"0",Info.get(0).getBALANCE()); //Adding Request in bots003 
			
					js.add(job);
					
				}

				Common_Utils util = new Common_Utils();  
				
				details.addProperty("Paytype", "demo");
				details.addProperty("CHCODE", CHCODE);
				details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
				details.addProperty("TYPE", "REQ");	
				
				details.add("data", js);
				 			 
				details.addProperty("result", "success");
				details.addProperty("stscode", "HP00");
				details.addProperty("message", "Menus found !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
	}
	
	
	
	
	
	
	
	//below function is for the 3 paramater in REQ_Jsondata
	public JsonObject RES_Jsondata(String CHCODE, String Menucode ,String MENUSL) 
	{
		JsonObject details = new JsonObject();
		
		try
		{	
			
				MENUSL = (Integer.parseInt(MENUSL)+1)+"";
			
				String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? and MENUSL = ?";
				 
				List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, Menucode , MENUSL } ,new Menu008Mapper ());
							
				JsonArray js = new JsonArray();
				
				
				if (Info.size() != 0) {
					
                    JsonObject job = new JsonObject();
					
					job.addProperty("Serial",Info.get(0).getMENUSL());
					job.addProperty("SERCODE",Info.get(0).getM2CODE());
					job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
					
					js.add(job);
					
				}

				Common_Utils util = new Common_Utils();  
				
				details.addProperty("Paytype", "demo");
				details.addProperty("CHCODE", CHCODE);
				details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
				details.addProperty("TYPE", "RES");	
				
				details.add("data", js);
				 			 
				details.addProperty("result", "success");
				details.addProperty("stscode", "HP00");
				details.addProperty("message", "Menus found !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
	}
	
	
	
	
	
			
	
	public JsonObject Get_current_menu(Bots001 Info) 
	{
		
		JsonObject details = new JsonObject();
				
		try
		{	     
			
			
		     String sql = "Insert into bots002(SUBORGCODE,CHCODE,COMMDATE,MOBILENO,COUNTRYCD,SESSIONID,COMMTIME,CURRMENU) values(?,?,?,?,?,?,?,?)";
			 
			 Jdbctemplate.update(sql, new Object[] {"EXIM",Info.getCHCODE(),Info.getCOMMDATE(),Info.getMOBILENO(),Info.getCOUNTRYCD(),Info.getSESSIONID(),Info.getCOMMTIME(),Info.getCURRMENU()});
			   
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Coutry code Added Successfully !!");
				

			
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
				
	}
	
	
            /*----------------------------  Insert function ----------------------------------------------*/

	public JsonObject Insert_bot001(Bots001 Info) 
	{
		
		JsonObject details = new JsonObject();
		
        Date c_date = new Date();
		
		SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");
		
		try
		{				
			
			
	       String sql = "Select count(*) from bots001 where CHCODE = ? and mobileno = ? and countrycd = ? ";
			
			int countOfMObcode = Jdbctemplate.queryForObject(sql, new Object[] {Info.getCHCODE(),Info.getMOBILENO(),Info.getCOUNTRYCD()}, Integer.class);

			if (countOfMObcode == 0) {
				
				 String Insertsql = "Insert into bots001(SUBORGCODE,CHCODE,MOBILENO,COUNTRYCD,STATUS,REGDATE,DEREGDATE,EDATE,EUSER,CDATE,CUSER,ADATE,AUSER) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				 
				 Jdbctemplate.update(Insertsql, new Object[] {SUBORGCODE,Info.getCHCODE(),Info.getMOBILENO(),Info.getADDINFO1(),"1",/*Regdate*/date_format.format(c_date),"",/*Edate*/date_format.format(c_date),"ADMIN",/*Cdate*/date_format.format(c_date),"ADMIN",/*Adate*/date_format.format(c_date),"ADMIN"});

				 details.addProperty("Result", "Success");
				 details.addProperty("Message", "User Added Successfully !!");
				 
			}
						
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Coutry code Added Successfully !!");
				
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
				
	}
	
	
	
	public JsonObject Insert_bot002(String SUBORGCODE, String CHCODE,String COMMDATE,String MOBILENO,String COUNTRYCD,String SESSIONID,Timestamp COMMTIME,String CURRMENU) 
	{
		
		JsonObject details = new JsonObject();
						
		try
		{	
			
		     String sql = "Insert into bots002(SUBORGCODE,CHCODE,COMMDATE,MOBILENO,COUNTRYCD,SESSIONID,COMMTIME,CURRMENU) values(?,?,?,?,?,?,?,?)";
			 
			 Jdbctemplate.update(sql, new Object[] {SUBORGCODE,CHCODE,COMMDATE,MOBILENO,COUNTRYCD,SESSIONID,COMMTIME,CURRMENU});
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Coutry code Added Successfully !!");
				
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
				
	}
	
	// (Request) Menu Description
	public JsonObject Insert_bot003(String SUBORGCODE, String CHCODE,String COMMDATE,String MOBILENO,String COUNTRYCD,String Session_Id,Timestamp COMMTIME,String MENUCD, String selectcd, String reqmsg) 
	{
		
		JsonObject details = new JsonObject();
		
		try
		{	
			
		     String sql = "Insert into bots003(SUBORGCODE,CHCODE,COMMDATE,MOBILENO,COUNTRYCD,SESSIONID,COMMTIME,MENUCD,SELECTCD,REQMSG) values(?,?,?,?,?,?,?,?,?,?)";
			 
			 Jdbctemplate.update(sql, new Object[] {SUBORGCODE,CHCODE,COMMDATE,MOBILENO,COUNTRYCD,Session_Id,COMMTIME,MENUCD,selectcd,reqmsg});
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Coutry code Added Successfully !!");
				
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
				
	}
	
	//(Response) Menu Description
	public JsonObject Insert_bot004(String SUBORGCODE, String CHCODE,String COMMDATE,String MOBILENO,String COUNTRYCD,String SESSIONID,Timestamp COMMTIME,String MENUCD, String selectcd, String resmsg) 
	{
		
		JsonObject details = new JsonObject();
		
		try
		{	
			
		     String sql = "Insert into bots004(SUBORGCODE,CHCODE,COMMDATE,MOBILENO,COUNTRYCD,SESSIONID,COMMTIME,MENUCD,SELECTCD,RESMSG) values(?,?,?,?,?,?,?,?,?,?)";
			 
			 Jdbctemplate.update(sql, new Object[] {SUBORGCODE,CHCODE,COMMDATE,MOBILENO,COUNTRYCD,SESSIONID,COMMTIME,MENUCD,selectcd,resmsg});
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Coutry code Added Successfully !!");
				
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
				
	}
	
	
    public JsonObject Add_Customer(USSD_Rep Info, HttpSession session) 
	{
		JsonObject details = new JsonObject();
		
		Date c_date = new Date();
		
		SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");
		
		try
		{ 
			String sql = "Select count(*) from bots001 where CHCODE = ? and mobileno = ? and countrycd = ? ";
			
			int countOfMObcode = Jdbctemplate.queryForObject(sql, new Object[] {Info.getCHCODE(),Info.getMOBILENO(),Info.getCOUNTRY_CODE()}, Integer.class);

			if (countOfMObcode == 0) {
				
				sql = "Insert into bots001(SUBORGCODE,CHCODE,MOBILENO,COUNTRYCD,REGDATE,STATUS,EUSER,EDATE) values(?,?,?,?,?,?,?,?)";

				
				   
				 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE(), Info.getMOBILENO(), Info.getCOUNTRY_CODE(), Info.getREGISTRATION_DATE(), Info.getSTATUS(),session.getAttribute("sesUserId").toString(),date_format.format(c_date)});
				 
				 details.addProperty("Result", "Success");
				 details.addProperty("Message", "User Added Successfully !!");
				 
			} else {

				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Mobile No "+Info.getMOBILENO()+" is already Added !!");
			}
				
			

		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
	}


   public JsonObject Add_Countrycode(USSD_Rep Info) 
   {
	JsonObject details = new JsonObject();
	
	
	try
	{	
		String sql = "Select count(*) from Contrycode001 where mobcode = ?";
		
		int countOfMObcode = Jdbctemplate.queryForObject(sql, new Object[] {Info.getMOBCODE()}, Integer.class);
		
		if (countOfMObcode == 0) {
			
			 sql = "Insert into Contrycode001(COUNTRYCODE,MOBCODE,COUNTRYNAME,STATUS) values(?,?,?,?)";
			 
			 //System.out.println(Info.getCOUNTRYCODE()+ Info.getMOBCODE()+ Info.getCOUNTRYNAME()+ Info.getSTATUS());
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getCOUNTRYCODE(), Info.getMOBCODE(), Info.getCOUNTRYNAME(),"1"});
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Coutry code Added Successfully !!");
			
		} else {

			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", "Mobile code "+Info.getMOBCODE()+" is already Exists !!");
		}
			
		
	 }
	 catch(Exception e)
	 {
		 details.addProperty("Result", "Failed");
		 details.addProperty("Message", e.getLocalizedMessage());   	
		 
		 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
	 }
			
	 return details;
}



    public JsonObject select_Countrycode() 
	{
		JsonObject details = new JsonObject();
		
		
		try
		{	
			 JsonArray js = new JsonArray();
			
				String sql = "Select MOBCODE from Contrycode001 Where STATUS=?";
			 
				 List<String> obj = Jdbctemplate.queryForList(sql, new Object[] {"1"}, String.class);
	
					
					  for(int i=0; i<obj.size(); i++) 
					  { 
						  js.add(obj.get(i)); 
						  
						  }
					  
					  details.add("array_values", js);
					 
				 System.out.println(obj);
				 
			 details.addProperty("Result", "Success");

			 details.addProperty("Message", "Coutry code Added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());   	
			 
			 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
		 }
				
		 return details;
	}
	
	
	
	                   /*----------------------------Row Mapper----------------------------------------------*/
    
    
    public class Bots001Mapper implements RowMapper<Bots001> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Bots001 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Bots001 Info = new Bots001();  

			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));  
			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));  
			Info.setCOMMDATE(util.ReplaceNull(rs.getString("COMMDATE")));  
			Info.setMOBILENO(util.ReplaceNull(rs.getString("MOBILENO")));  
			Info.setCOUNTRYCD(util.ReplaceNull(rs.getString("COUNTRYCD")));  
			Info.setSESSIONID(util.ReplaceNull(rs.getString("SESSIONID")));  
			Info.setCOMMTIME(util.ReplaceNull(rs.getString("COMMTIME"))); 
			Info.setCURRMENU(util.ReplaceNull(rs.getString("CURRMENU")));  

		      
			return Info;
		}
     }
    
    		   
	   public class Bots002Mapper implements RowMapper<Bots002> 
	    {
	    	Common_Utils util = new Common_Utils();
	    	
			public Bots002 mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Bots002 Info = new Bots002();  

				Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));  
				Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));  
				Info.setCOMMDATE(util.ReplaceNull(rs.getString("COMMDATE")));  
				Info.setMOBILENO(util.ReplaceNull(rs.getString("MOBILENO")));  
				Info.setCOUNTRYCD(util.ReplaceNull(rs.getString("COUNTRYCD")));  
				Info.setSESSIONID(util.ReplaceNull(rs.getString("SESSIONID")));  
				Info.setCOMMTIME(util.ReplaceNull(rs.getString("COMMTIME"))); 
				Info.setCURRMENU(util.ReplaceNull(rs.getString("CURRMENU")));  
			       
		return Info;
			}
	     }

	   public class Menu007Mapper implements RowMapper<Menu007> 
	    {
	    	Common_Utils util = new Common_Utils();
	    	
			public Menu007 mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Menu007 Info = new Menu007();  

				Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));  
				Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));  
				Info.setM1CODE(util.ReplaceNull(rs.getString("M1CODE")));  
				Info.setMENUTYPE(util.ReplaceNull(rs.getString("MENUTYPE")));  
				Info.setMENUSL(util.ReplaceNull(rs.getString("MENUSL")));  
				Info.setMENUDESC(util.ReplaceNull(rs.getString("MENUDESC")));  
				Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));  
				Info.setEDATE(util.ReplaceNull(rs.getString("EDATE")));      
				Info.setEUSER(util.ReplaceNull(rs.getString("EUSER")));         
				Info.setCDATE(util.ReplaceNull(rs.getString("CDATE")));         
				Info.setCUSER(util.ReplaceNull(rs.getString("CUSER")));          
				Info.setADATE(util.ReplaceNull(rs.getString("ADATE")));
				Info.setAUSER(util.ReplaceNull(rs.getString("AUSER")));         

				return Info;
			}
	     }
	   
	   
	   public class Menu008Mapper implements RowMapper<Menu008> 
	    {
	    	Common_Utils util = new Common_Utils();
	    	
			public Menu008 mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Menu008 Info = new Menu008();  

				Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));  
				Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));  
				Info.setM2CODE(util.ReplaceNull(rs.getString("M2CODE")));  
				Info.setMENUSL(util.ReplaceNull(rs.getString("MENUSL")));  
				Info.setMENUDESC(util.ReplaceNull(rs.getString("MENUDESC")));  
				Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));  
				Info.setEDATE(util.ReplaceNull(rs.getString("EDATE")));      
				Info.setEUSER(util.ReplaceNull(rs.getString("EUSER")));         
				Info.setCDATE(util.ReplaceNull(rs.getString("CDATE")));         
				Info.setCUSER(util.ReplaceNull(rs.getString("CUSER")));          
				Info.setADATE(util.ReplaceNull(rs.getString("ADATE")));
				Info.setAUSER(util.ReplaceNull(rs.getString("AUSER")));         
				Info.setSERVCD(util.ReplaceNull(rs.getString("SERVCD")));         


				return Info;
			}
	     }
	   
	   	   
	   public class IPS_ACCOUNT_VIEW001Mapper implements RowMapper<IPS_ACCOUNT_VIEW001> 
	    {
	    	Common_Utils util = new Common_Utils();
	    	
			public IPS_ACCOUNT_VIEW001 mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				IPS_ACCOUNT_VIEW001 Info = new IPS_ACCOUNT_VIEW001();  

				Info.setBRANCH(util.ReplaceNull(rs.getString("BRANCH")));  
				Info.setACCNO(util.ReplaceNull(rs.getString("ACCNO")));  
				Info.setCUSTOMERNAME(util.ReplaceNull(rs.getString("CUSTOMERNAME")));  
				Info.setCURRENCY(util.ReplaceNull(rs.getString("CURRENCY")));  
				Info.setBALANCE(util.ReplaceNull(rs.getString("BALANCE")));  
				Info.setCUSTOMERNO(util.ReplaceNull(rs.getString("CUSTOMERNO")));  
				Info.setMOBILENO(util.ReplaceNull(rs.getString("MOBILENO"))); 
				Info.setCOUNTRYCD(util.ReplaceNull(rs.getString("COUNTRYCD")));  
		      
				return Info;
			}
	     }
	   
	   
	   
	   
	   
	   
	   
	   
	   
	 
	
}