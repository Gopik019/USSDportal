package com.hdsoft.ussd.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.hdsoft.ussd.Repositories.Bots004;
import com.hdsoft.ussd.Repositories.Bots004dash;
import com.hdsoft.ussd.Repositories.IPS_ACCOUNT_VIEW001;
import com.hdsoft.ussd.Repositories.Menu007;
import com.hdsoft.ussd.Repositories.Menu008;
import com.hdsoft.ussd.Repositories.Menu009;
import com.hdsoft.ussd.Repositories.Menu010;
import com.hdsoft.ussd.Repositories.TRANSACTIONS;
import com.hdsoft.ussd.Repositories.bill_payments001;
import com.hdsoft.ussd.Repositories.botinvoice001;
import com.zaxxer.hikari.HikariDataSource;


@Component
public class USSD_model_ADDINFO04 {
	
	public JdbcTemplate Jdbctemplate;
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	String SUBORGCODE = "EXIM";
	
	
	//***************************** Main Method *********************************//
	
	public JsonObject Find_Menu_details2(Bots001 B1Info, HttpSession session, HttpServletRequest request) {
		
		JsonObject details = new JsonObject();
		
	 try {
		 
		    Common_Utils utils = new Common_Utils();
		    String MOBILENO = B1Info.getMOBILENO();   					 // Mobile No 
		    String CHCODE = B1Info.getCHCODE();      					 // Channel code
			String SERCODE = utils.ReplaceNull(B1Info.getSERCODE()) ;    // Sercode code
			String INPUT = utils.ReplaceNull(B1Info.getINPUT());         // Input 
		    String ADDINFO1 = utils.ReplaceNull(B1Info.getADDINFO1());   // country code
		    String ADDINFO2 = utils.ReplaceNull(B1Info.getADDINFO2());   // Menuservice Des
		    String ADDINFO3 = utils.ReplaceNull(B1Info.getADDINFO3());   // Ussd code (*333#)
		    String Session_Id = session.getId();                         // Session Id
	        String commdate = utils.getCurrentDate("dd-MMM-yyyy");
			Timestamp commtime = utils.get_oracle_Timestamp();
			
			
			System.out.println("Mobile No "+MOBILENO);
			System.out.println("Channel code "+CHCODE);
			System.out.println("Sercode code "+SERCODE);
			System.out.println("Input "+INPUT);
			System.out.println("country code "+ADDINFO1);
			System.out.println("Menuservice Des "+ADDINFO2);
			System.out.println("Ussd code "+ADDINFO3);
			
						
			// 9 parameter default -  (MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id)
		 
			
			String sql = "select count(*) from newbots001 where mobileno= ? and CHCODE= ? and USSDCODE= ? and status = 1";  //Get Mobile no from Bots001(Customer Registration)
			
			int cntofmobileno = Jdbctemplate.queryForObject(sql, new Object[] { MOBILENO, CHCODE ,ADDINFO3 }, Integer.class);
			
			
			
			if(cntofmobileno == 0)               //Non Registered Mobile No in customer user registration
			{
				 if(utils.isNullOrEmpty(SERCODE))
				 {
					  details = check_Reg_NonReg(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
				      return details;
				 }
			 
						         // Validation Of service code and serial no and menu description 
							boolean flag = false; 
					 //Existing Customer//					
							if (SERCODE.equalsIgnoreCase("M013") && INPUT.equalsIgnoreCase("1") && ADDINFO2.equalsIgnoreCase("Existing Customer")) {
								flag = true;
								details = Existing_Customer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (ADDINFO2.equalsIgnoreCase("Enter the 4digit PIN") && SERCODE.equalsIgnoreCase("M00018H")) {
								flag = true;
								details = Existing_Customer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (ADDINFO2.equalsIgnoreCase("Balance Inquiry") && SERCODE.equalsIgnoreCase("M011")) {
								flag = true;
								details = Balance_Inquiry(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}   
							else if (SERCODE.equalsIgnoreCase("M0BE")) {  // for more than one acc no
								flag = true;
								details = Balance_Inquiry(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}	
							 //Recent Transaction//					
							else if (SERCODE.equalsIgnoreCase("M011") && ADDINFO2.equalsIgnoreCase("Recent Transactions") ) {
								flag = true;
								details = Recent_Transactions001(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0RT")) {
								flag = true;
								details = Recent_Transactions001(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							 //Transfer//					
							else if (SERCODE.equalsIgnoreCase("M011") && ADDINFO2.equalsIgnoreCase("Transfer") ) {
								flag = true;
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00014H") && ADDINFO2.equalsIgnoreCase("Enter the Destination Bank Code") ) {
								flag = true;
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00014H") && ADDINFO2.equalsIgnoreCase("Enter the Destination Account Number") ) {
								flag = true;
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0T")) { //for transfer need to select A/c
								flag = true;
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00014H") && ADDINFO2.equalsIgnoreCase("Enter the Transaction Amount") ) {
								flag = true;
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00014H") && ADDINFO2.equalsIgnoreCase("Select the Transaction Currency") ) {
								flag = true;
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00014C") && ADDINFO2.equalsIgnoreCase("TZS") ) {
								flag = true;
									if (SERCODE.equalsIgnoreCase("M00014C") && ( ADDINFO2.equalsIgnoreCase("TZS")|| ADDINFO2.equalsIgnoreCase("USD"))) {
										String sql1 = "select count(*) from bots004 c where c.mobileno = ? and c.menucd in ('M00014H', 'M0T', 'M00014C') and SESSIONID = (select SESSIONID from (select SESSIONID from bots004 x where x.mobileno = ? order by COMMTIME desc) where rownum = 1) order by COMMTIME";
										int count_ofbots004  = Jdbctemplate.queryForObject(sql1, new Object[] {  MOBILENO , MOBILENO  } , Integer.class);
											if (count_ofbots004==4) {
											    Insert_bot004(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,SERCODE,INPUT,ADDINFO2); //Adding Response in bots004 
											}
									}
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00014C") && ADDINFO2.equalsIgnoreCase("USD") ) {
								flag = true;
									if (SERCODE.equalsIgnoreCase("M00014C") && ( ADDINFO2.equalsIgnoreCase("TZS")|| ADDINFO2.equalsIgnoreCase("USD"))) {
										String sql1 = "select count(*) from bots004 c where c.mobileno = ? and c.menucd in ('M00014H', 'M0T', 'M00014C') and SESSIONID = (select SESSIONID from (select SESSIONID from bots004 x where x.mobileno = ? order by COMMTIME desc) where rownum = 1) order by COMMTIME";
										int count_ofbots004  = Jdbctemplate.queryForObject(sql1, new Object[] {  MOBILENO , MOBILENO  } , Integer.class);
											if (count_ofbots004==4) {
											    Insert_bot004(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,SERCODE,INPUT,ADDINFO2); //Adding Response in bots004 
											}
									}
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00018") && ADDINFO2.equalsIgnoreCase("Enter the Input '1' for confirm the Transaction") && INPUT.equalsIgnoreCase("1") ) {
								flag = true;
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}	
							else if (SERCODE.equalsIgnoreCase("M00018") && ADDINFO2.equalsIgnoreCase("Enter the Input '2' for cancel the Transaction") && INPUT.equalsIgnoreCase("2") ) {
								flag = true;
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M000013H") && ADDINFO2.equalsIgnoreCase("Enter the OTP")) {
								flag = true;
								details = Transfer(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}	
							 //Check Book Request//					
							else if (SERCODE.equalsIgnoreCase("M011") && ADDINFO2.equalsIgnoreCase("Cheque Book Request") && INPUT.equalsIgnoreCase("4") ) {
								flag = true;
								details = Cheque_Book_Request(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0CBR")) {
								flag = true;
								details = Cheque_Book_Request(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0013") && ADDINFO2.equalsIgnoreCase("30 Pages Cheque Book") && INPUT.equalsIgnoreCase("1") ) {
								flag = true;
								details = Cheque_Book_Request(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0013") && ADDINFO2.equalsIgnoreCase("60 Pages Cheque Book") && INPUT.equalsIgnoreCase("2") ) {
								flag = true;
								details = Cheque_Book_Request(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							 //Account Statement//					
							else if (SERCODE.equalsIgnoreCase("M011") && ADDINFO2.equalsIgnoreCase("Account Statement") && INPUT.equalsIgnoreCase("5") ) {
								flag = true;
								details = Account_Statement(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0AS")) {
								flag = true;
								details = Account_Statement(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0014") && ADDINFO2.equalsIgnoreCase("Current Month") && INPUT.equalsIgnoreCase("1") ) {
								flag = true;
								details = Account_Statement(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0014") && ADDINFO2.equalsIgnoreCase("Previous Month") && INPUT.equalsIgnoreCase("2") ) {
								flag = true;
								details = Account_Statement(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0014") && ADDINFO2.equalsIgnoreCase("Last three Month") && INPUT.equalsIgnoreCase("3") ) {
								flag = true;
								details = Account_Statement(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0014") && ADDINFO2.equalsIgnoreCase("Last six Month") && INPUT.equalsIgnoreCase("4") ) {
								flag = true;
								details = Account_Statement(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0014") && ADDINFO2.equalsIgnoreCase("Annual Statement") && INPUT.equalsIgnoreCase("5") ) {
								flag = true;
								details = Account_Statement(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							 // Bill Payments//					
							else if (SERCODE.equalsIgnoreCase("M011") && ADDINFO2.equalsIgnoreCase("Bill Payments") && INPUT.equalsIgnoreCase("6") ) {
								flag = true;
								details = Bill_Payments(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0015") && ADDINFO2.equalsIgnoreCase("Electricity Bill") && INPUT.equalsIgnoreCase("1")) {
								flag = true;
								details = Bill_Payments(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0015") && ADDINFO2.equalsIgnoreCase("Income Tax") && INPUT.equalsIgnoreCase("2")) {
								flag = true;
								details = Bill_Payments(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0015") && ADDINFO2.equalsIgnoreCase("Telephone Bill") && INPUT.equalsIgnoreCase("3")) {
								flag = true;
								details = Bill_Payments(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00013H") && ADDINFO2.equalsIgnoreCase("Enter the Invoice or Control Number")) {
								flag = true;
								details = Bill_Payments(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0BP")) {
								flag = true;
								details = Bill_Payments(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00015") && ADDINFO2.equalsIgnoreCase("Enter the Input '1' for confirm the payment") && INPUT.equalsIgnoreCase("1")) {
								flag = true;
								details = Bill_Payments(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00015") && ADDINFO2.equalsIgnoreCase("Enter the Input '2' for cancel the payment") && INPUT.equalsIgnoreCase("2")) {
								flag = true;
								details = Bill_Payments(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0MM") && ADDINFO2.equalsIgnoreCase("Enter the Input '0' for main menu") && INPUT.equalsIgnoreCase("0") ) {
								flag = true;
								details = Main_Menu(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id,  session, request);
							}
							else if (SERCODE.equalsIgnoreCase("M00MM") && ADDINFO2.equalsIgnoreCase("Enter the Input '0' for service menu") && INPUT.equalsIgnoreCase("0") ) {
								flag = true;
								details = service_menu(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id, session, request);
							}
							
			        //New registration//				
							if (SERCODE.equalsIgnoreCase("M013") && INPUT.equalsIgnoreCase("2") && ADDINFO2.equalsIgnoreCase("New Registration")) {
								flag = true;
								details = New_Registration(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M00016") && INPUT.equalsIgnoreCase("1") && ADDINFO2.equalsIgnoreCase("Accept")) {
								flag = true;
								details = New_Registration(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0019H") && ADDINFO2.equalsIgnoreCase("Please enter your full name")) {
								flag = true;
								details = New_Registration(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
							else if (SERCODE.equalsIgnoreCase("M0020H") && ADDINFO2.equalsIgnoreCase("Please enter your Date Of Birth")) {
								flag = true;	
								details = New_Registration(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
							}
//							if (SERCODE.equalsIgnoreCase("M0021") && ADDINFO2.equalsIgnoreCase("Please confirm your details")) {
//								flag = true;
//								details = Self_Registration(MOBILENO,CHCODE,SERCODE,INPUT,ADDINFO1,ADDINFO2,ADDINFO3,commdate,commtime,Session_Id);
//							}
		
						
							if(flag == true) {
										
								if (SERCODE.equalsIgnoreCase("M00014C") && ( ADDINFO2.equalsIgnoreCase("TZS")|| ADDINFO2.equalsIgnoreCase("USD"))) {
									
								}else {
								    Insert_bot004(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,SERCODE,INPUT,ADDINFO2); //Adding Response in bots004 
								}						  
							}
			}
			else {                    // Registered Mobile No in customer user registration
				
				
				
			}			
			
			
			
				
		} catch (Exception e) {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "HP06");
			 details.addProperty("message", e.getLocalizedMessage()); 	
			 
			 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
		}
		
		
		
		
		return details;	
		
	}
	
	
	
	
	
	
	
	
	
	
	    //***************************** check_Reg_NonReg *********************************//

	
	   public JsonObject check_Reg_NonReg(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime ,String Session_Id) 
	  {
		
		JsonObject details = new JsonObject();
		
				String menucode1 = "";  String menucode2 = "";


					if (SERCODE.equalsIgnoreCase("")) {    //sercode = ""Empty code;
						menucode1 = "M01H1";
						menucode2 = "M013";
					} else {                              //sercode = ""Any code;
						menucode1 = SERCODE;
						menucode2 = SERCODE;
				    }
		
		try
		{	
			
				String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ? and status = 1";
			 
				List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, menucode1 } ,new Menu007Mapper ());
				
				JsonArray js = new JsonArray();
				
				   if (Info1.size() != 0) {
					
	                    JsonObject job = new JsonObject();
						
						job.addProperty("Serial",Info1.get(0).getMENUSL());
						job.addProperty("SERCODE",Info1.get(0).getM1CODE());
						job.addProperty("DESCRIPTION",Info1.get(0).getMENUDESC());
				
						js.add(job);
				    }
				
				   
				   String sql2 = "select * from menu007 where CHCODE= ? and M1CODE= ?";
					 
				   List<Menu007>  Info2 = Jdbctemplate.query(sql2, new Object[] { CHCODE, menucode2  } ,new Menu007Mapper ());
					
				   for (int i=0; i < Info2.size(); i++) {
															
	                    JsonObject job = new JsonObject();
						
						job.addProperty("Serial",Info2.get(i).getMENUSL());
						job.addProperty("SERCODE",Info2.get(i).getM1CODE());
						job.addProperty("DESCRIPTION",Info2.get(i).getMENUDESC());
						
					    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info2.get(i).getM1CODE(),Info2.get(i).getMENUSL(),Info2.get(i).getMENUDESC()); //Adding Response in bots004 
				
						js.add(job);
				    }
			
				
				Common_Utils util = new Common_Utils();  
				
				details.addProperty("Paytype", "demo");
				details.addProperty("CHCODE", CHCODE);
				details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
				details.addProperty("TYPE", "REQ");	 //REQ or RES
				
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
	
	
	
	
	    //***************************** Existing Customer *********************************//
	
	
		public JsonObject Existing_Customer(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
		{
			
			JsonObject details = new JsonObject();
			
											
			if (SERCODE.equalsIgnoreCase("M013")) {
				
						try
						{	
								String sql = "select * from menu009 where CHCODE = ? and M3CODE = ?";
							 
								List<Menu009>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, "M00018H" } ,new Menu009Mapper ());
								
								JsonArray js = new JsonArray();
								
										for(int i=0; i<Info.size(); i++)
										{					
											JsonObject job = new JsonObject();
											
											job.addProperty("Serial", Info.get(i).getMENUSL());
											job.addProperty("SERCODE", Info.get(i).getM3CODE());
											job.addProperty("DESCRIPTION", Info.get(i).getMENUDESC());
											
										    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM3CODE(),Info.get(i).getMENUSL(),Info.get(i).getMENUDESC()); //Adding Response in bots004 

											
//								        	Common_Utils util = new Common_Utils();
//									        String commdate1 = util.getCurrentDate("dd-MMM-yyyy");
//											Timestamp commtime1 = util.get_oracle_Timestamp();
											
											
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
								
						}catch(Exception e)
								 {
									 details.addProperty("result", "failed");
									 details.addProperty("stscode", "HP06");
									 details.addProperty("message", e.getLocalizedMessage());   	
									 
									 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
								 }
					}	
						
			
			
			
				if (ADDINFO2.equalsIgnoreCase("Enter the 4digit PIN")) { 
					
					String sqlcount = "select count (*) from ips_account_view001 where MOBILENO = ?";
					 
					int Accno_count  = Jdbctemplate.queryForObject(sqlcount, new Object[] { MOBILENO } , Integer.class);
					
					
					if ((Accno_count != 1) && (Accno_count != 0)) {    // More than one Accno
						
										try
										{		
											String sql = "select * from ips_account_view001 where MOBILENO = ?";
											 
											List<IPS_ACCOUNT_VIEW001>  Info = Jdbctemplate.query(sql, new Object[] { MOBILENO } ,new IPS_ACCOUNT_VIEW001Mapper ());
											
											JsonArray js = new JsonArray();
											
											if (Info.get(0).getPIN().equalsIgnoreCase(INPUT)) { // Correct PIN
																								
												
							                        String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ?  and status = 1";
												 
												    List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M011"  } ,new Menu007Mapper ());
													
													   for (int i=0; i<Info1.size(); i++) {
														   
															JsonObject job = new JsonObject();
																																		
															job.addProperty("Serial",Info1.get(i).getMENUSL());
															job.addProperty("SERCODE",Info1.get(i).getM1CODE());
															job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
															
														    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info1.get(i).getM1CODE(),Info1.get(i).getMENUSL(),Info1.get(i).getMENUDESC()); //Adding Response in bots004 
															
								               			js.add(job);
													   }
												   									
											} else { //Invalid PIN
			
													String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ? ";
													 
													List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M014H" } ,new Menu007Mapper ());
													
														    for (int i=0; i<Info1.size(); i++) {
															
											                    JsonObject job = new JsonObject();
																
																job.addProperty("Serial","1");
																job.addProperty("SERCODE",Info1.get(i).getM1CODE());
																job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
								
																js.add(job);
														   	 }
														
												}
											
											
											String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ?";
											 
											List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M0MM" } ,new Menu007Mapper ());
											
											   for (int i=0; i<Info1.size(); i++) {
												
								                    JsonObject job = new JsonObject();
													
													job.addProperty("Serial","");
													job.addProperty("SERCODE",Info1.get(i).getM1CODE());
													job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
											
													js.add(job);
											    }
											 	
											
											
												Common_Utils util = new Common_Utils();  
												
												details.addProperty("Paytype", "demo");
												details.addProperty("CHCODE", CHCODE);
												details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
												details.addProperty("TYPE", "REQ");	 //REQ or RES
												
												details.add("data", js);
												 			 
												details.addProperty("result", "success");
												details.addProperty("stscode", "HP00");
												details.addProperty("message", "Menus found !!");
									 
																		
									 }catch(Exception e)
									 {
										 details.addProperty("result", "failed");
										 details.addProperty("stscode", "HP06");
										 details.addProperty("message", e.getLocalizedMessage());   	
										 
										 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
									 }
									
						
					} else {   //One Accno only
						
						try
						{		
							String sql = "select * from ips_account_view001 where MOBILENO = ?";
							 
							List<IPS_ACCOUNT_VIEW001>  Info = Jdbctemplate.query(sql, new Object[] { MOBILENO } ,new IPS_ACCOUNT_VIEW001Mapper ());
							
							JsonArray js = new JsonArray();
							
							if (Info.get(0).getPIN().equalsIgnoreCase(INPUT)) { // Correct PIN
																
			                        String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ? and status = 1  ";
								 
								    List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M011"  } ,new Menu007Mapper ());
									
									   for (int i=0; i<Info1.size(); i++) {
										   
						                    JsonObject job = new JsonObject();
																														
											job.addProperty("Serial",Info1.get(i).getMENUSL());
											job.addProperty("SERCODE",Info1.get(i).getM1CODE());
											job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
											
										    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info1.get(i).getM1CODE(),Info1.get(i).getMENUSL(),Info1.get(i).getMENUDESC()); //Adding Response in bots004 
											
				               			js.add(job);
									   }
								   									
							} else { //Invalid PIN

									String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ?";
									 
									List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M014H" } ,new Menu007Mapper ());
									
										    for (int i=0; i<Info1.size(); i++) {
											
							                    JsonObject job = new JsonObject();
												
												job.addProperty("Serial","1");
												job.addProperty("SERCODE",Info1.get(i).getM1CODE());
												job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
				
												js.add(job);
										   	 }
										
								}
							
							 	String sql2 = "select * from menu007 where CHCODE= ? and M1CODE= ? ";
							 
							 	List<Menu007>  Info1 = Jdbctemplate.query(sql2, new Object[] { CHCODE, "M0MM" } ,new Menu007Mapper ());
							
							
									   if (Info1.size() != 0) {
										
						                    JsonObject job = new JsonObject();
											
											job.addProperty("Serial",Info1.get(0).getMENUSL());
											job.addProperty("SERCODE",Info1.get(0).getM1CODE());
											job.addProperty("DESCRIPTION",Info1.get(0).getMENUDESC());
									
											js.add(job);
									    }
							
							
								Common_Utils util = new Common_Utils();  
								
								details.addProperty("Paytype", "demo");
								details.addProperty("CHCODE", CHCODE);
								details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
								details.addProperty("TYPE", "REQ");	 //REQ or RES
								
								details.add("data", js);
								 			 
								details.addProperty("result", "success");
								details.addProperty("stscode", "HP00");
								details.addProperty("message", "Menus found !!");
					 
														
					 }catch(Exception e)
					 {
						 details.addProperty("result", "failed");
						 details.addProperty("stscode", "HP06");
						 details.addProperty("message", e.getLocalizedMessage());   	
						 
						 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
					 }
					

					}
					
					
							
							
				}
					
			 return details;
		}
		
		
		//*****************************  New_Registration *********************************//
		
		
		public JsonObject New_Registration(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
		{
			
			JsonObject details = new JsonObject();
			
				String menucode1 = "";  String menucode2 = "";

					if (SERCODE.equalsIgnoreCase("M013")) {          // Terms and conditions
						menucode1 = "M0001H1";
						menucode2 = "M00016";
					} 
					else if (SERCODE.equalsIgnoreCase("M00016")) {     // Accept or Reject
						menucode1 = "M0019H";           
					}
					else if (SERCODE.equalsIgnoreCase("M0019H")) {     
						menucode1 = "M0020H";           
					}
					else if (SERCODE.equalsIgnoreCase("M0020H")) {     
						menucode1 = "M0022H";           
					}
					
					
		
					if (SERCODE.equalsIgnoreCase("M013")) {
						
						try
						{	
								String sql1 = "select * from menu009 where CHCODE= ? and M3CODE= ?";
							 
								List<Menu009>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, menucode1 } ,new Menu009Mapper ());
								
								JsonArray js = new JsonArray();
								
								   if (Info1.size() != 0) {
									
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial",Info1.get(0).getMENUSL());
										job.addProperty("SERCODE",Info1.get(0).getM3CODE());
										job.addProperty("DESCRIPTION",Info1.get(0).getMENUDESC());
										
									    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info1.get(0).getM3CODE(),Info1.get(0).getMENUSL(),Info1.get(0).getMENUDESC()); //Adding Response in bots004 

								
										js.add(job);
								    }
								
								   
								   String sql2 = "select * from menu009 where CHCODE= ? and M3CODE= ?";
									 
								   List<Menu009>  Info2 = Jdbctemplate.query(sql2, new Object[] { CHCODE, menucode2  } ,new Menu009Mapper ());
									
								   for (int i=0; i < Info2.size(); i++) {
																			
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial",Info2.get(i).getMENUSL());
										job.addProperty("SERCODE",Info2.get(i).getM3CODE());
										job.addProperty("DESCRIPTION",Info2.get(i).getMENUDESC());
													
										js.add(job);
								    }
							
								
								Common_Utils util = new Common_Utils();  
								
								details.addProperty("Paytype", "demo");
								details.addProperty("CHCODE", CHCODE);
								details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
								details.addProperty("TYPE", "REQ");	 //REQ or RES
								
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
					}
					
					
					if (SERCODE.equalsIgnoreCase("M00016")) {
						
						try
						{	
								String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? and status = 1";
							 
								List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode1 } ,new Menu008Mapper ());
								
								JsonArray js = new JsonArray();
								
										for(int i=0; i<Info.size(); i++)
										{					
											JsonObject job = new JsonObject();
											
											job.addProperty("Serial", Info.get(i).getMENUSL());
											job.addProperty("SERCODE", Info.get(i).getM2CODE());
											job.addProperty("DESCRIPTION", Info.get(i).getMENUDESC());
											
										    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM2CODE(),Info.get(i).getMENUSL(), Info.get(i).getMENUDESC()); //Adding Response in bots004 

											
//								        	Common_Utils util = new Common_Utils();
//									        String commdate1 = util.getCurrentDate("dd-MMM-yyyy");
//											Timestamp commtime1 = util.get_oracle_Timestamp();
											
											
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
								
						}catch(Exception e)
								 {
									 details.addProperty("result", "failed");
									 details.addProperty("stscode", "HP06");
									 details.addProperty("message", e.getLocalizedMessage());   	
									 
									 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
								 }
					}
					
					
				 if (SERCODE.equalsIgnoreCase("M0019H")) {
						
						try
						{	
								String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? and status = 1";
							 
								List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode1 } ,new Menu008Mapper ());
								
								JsonArray js = new JsonArray();
								
										for(int i=0; i<Info.size(); i++)
										{					
											JsonObject job = new JsonObject();
											
											job.addProperty("Serial", Info.get(i).getMENUSL());
											job.addProperty("SERCODE", Info.get(i).getM2CODE());
											job.addProperty("DESCRIPTION", Info.get(i).getMENUDESC());
											
										    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM2CODE(),Info.get(i).getMENUSL(), Info.get(i).getMENUDESC()); //Adding Response in bots004 

											
//								        	Common_Utils util = new Common_Utils();
//									        String commdate1 = util.getCurrentDate("dd-MMM-yyyy");
//											Timestamp commtime1 = util.get_oracle_Timestamp();
											
											
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
								
							}catch(Exception e)
								 {
									 details.addProperty("result", "failed");
									 details.addProperty("stscode", "HP06");
									 details.addProperty("message", e.getLocalizedMessage());   	
									 
									 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
								 }
					}
					
					
				 if (SERCODE.equalsIgnoreCase("M0020H")) {
						
						try
						{	
								String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? and status = 1";
							 
								List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode1 } ,new Menu008Mapper ());
								
								JsonArray js = new JsonArray();
								
										for(int i=0; i<Info.size(); i++)
										{					
											JsonObject job = new JsonObject();
											
											job.addProperty("Serial", Info.get(i).getMENUSL());
											job.addProperty("SERCODE", Info.get(i).getM2CODE());
											job.addProperty("DESCRIPTION", Info.get(i).getMENUDESC());
											
										    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM2CODE(),Info.get(i).getMENUSL(), Info.get(i).getMENUDESC()); //Adding Response in bots004 
											
											js.add(job);
										}
										
										String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ?";
										 
										List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M0MM" } ,new Menu007Mapper ());
										
										   for (int i=0; i<Info1.size(); i++) {
											
							                    JsonObject job = new JsonObject();
												
												job.addProperty("Serial","");
												job.addProperty("SERCODE",Info1.get(i).getM1CODE());
												job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
										
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
								
							}catch(Exception e)
								 {
									 details.addProperty("result", "failed");
									 details.addProperty("stscode", "HP06");
									 details.addProperty("message", e.getLocalizedMessage());   	
									 
									 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
								 }
					}
					
										
					
			 return details;
		}
		
	
		
		//*****************************  Balance Enquiry *********************************//

		
		public JsonObject Balance_Inquiry(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
		{
			
			String menucode1 = "";

			String sqlcount = "select count (*) from ips_account_view001 where MOBILENO = ?";
			 
			int Accno_count  = Jdbctemplate.queryForObject(sqlcount, new Object[] { MOBILENO } , Integer.class);
			
			
			if (ADDINFO2.equalsIgnoreCase("Balance Inquiry")) {
				menucode1 = "M0017H";   //30-12-2022 
			}
			if (ADDINFO2.equalsIgnoreCase("Enter the 4digit PIN")) {
				menucode1 = "M011";
			}
			
			
			JsonObject details = new JsonObject();
			
			if ((Accno_count != 1) && (Accno_count != 0)) {         //More than one Acc
				
				if (ADDINFO2.equalsIgnoreCase("Balance Inquiry")) { 
					
					try
					{		
							String sql = "select * from menu008 where CHCODE= ? and M2CODE= ?";  // select the Acc no
							 
							List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode1 } ,new Menu008Mapper ());
							
							JsonArray js = new JsonArray();

							   for (int i=0; i < Info.size(); i++) {
								
				                    JsonObject job = new JsonObject();
									
									job.addProperty("Serial",Info.get(i).getMENUSL());
									job.addProperty("SERCODE",Info.get(i).getM2CODE());
									job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
							
									js.add(job);
							    }
						
																		
							String sql1 = "select * from ips_account_view001 where MOBILENO = ?";  // it shows the Acc no 
							 
							List<IPS_ACCOUNT_VIEW001>  Info1 = Jdbctemplate.query(sql1, new Object[] { MOBILENO } ,new IPS_ACCOUNT_VIEW001Mapper ());
										
							
							for (int j = 0; j < Info1.size(); j++) {
																
			                    JsonObject job = new JsonObject();

			                    job.addProperty("Serial",j+1);
								job.addProperty("SERCODE",Info1.get(j).getMCODE());
								job.addProperty("DESCRIPTION",Info1.get(j).getACCNO());
								
								js.add(job);
								
							}
							
							
							Common_Utils util = new Common_Utils();  
							
							details.addProperty("Paytype", "demo");
							details.addProperty("CHCODE", CHCODE);
							details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
							details.addProperty("TYPE", "REQ");	 //REQ or RES
							
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
				}
				
				if (SERCODE.equalsIgnoreCase("M0BE")) { 
					
					try
					{		
							String sql1 = "select * from ips_account_view001 where MOBILENO = ? and ACCNO = ?";
							 
							List<IPS_ACCOUNT_VIEW001>  Info1 = Jdbctemplate.query(sql1, new Object[] { MOBILENO,ADDINFO2 } ,new IPS_ACCOUNT_VIEW001Mapper ());
								
							JsonArray js = new JsonArray();

							   for (int i=0; i < Info1.size(); i++) {
								
				                    JsonObject job = new JsonObject();
									
				                    String serial_no = "1";
				                    String sercode = "M00BEH"; 
				                    String bal_desc = "Available Balance "+Info1.get(0).getCURRENCY()+"."+Info1.get(0).getBALANCE();
									
									job.addProperty("Serial",serial_no);
									job.addProperty("SERCODE",sercode);
									job.addProperty("DESCRIPTION",bal_desc);
									
								    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,sercode,serial_no,bal_desc); //Adding Response in bots004 


								    js.add(job);
							    }
						
																		
							String sql2 = "select * from menu008 where CHCODE= ? and M2CODE= ?";
							 
							List<Menu008>  Info2 = Jdbctemplate.query(sql2, new Object[] { CHCODE,"M00MM" } ,new Menu008Mapper ());
										
							
							for (int j = 0; j < Info2.size(); j++) {
																
			                    JsonObject job = new JsonObject();

			                    job.addProperty("Serial","");
								job.addProperty("SERCODE",Info2.get(0).getM2CODE());
								job.addProperty("DESCRIPTION",Info2.get(0).getMENUDESC());
								
								js.add(job);
								
							}
							
							
							Common_Utils util = new Common_Utils();  
							
							details.addProperty("Paytype", "demo");
							details.addProperty("CHCODE", CHCODE);
							details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
							details.addProperty("TYPE", "REQ");	 //REQ or RES
							
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
				}
			
			}else {
						try
						{	
							String sql = "select * from ips_account_view001 where MOBILENO = ? ";
							 
							List<IPS_ACCOUNT_VIEW001>  Info = Jdbctemplate.query(sql, new Object[] { MOBILENO } ,new IPS_ACCOUNT_VIEW001Mapper ());
								
								JsonArray js = new JsonArray();
								
								if (Info.size() != 0) {
									
					                    JsonObject job = new JsonObject();
										
						                    String serial_no = "1";
						                    String sercode = "M00BEH";
						                    String bal_desc = "Available Balance "+Info.get(0).getCURRENCY()+"."+Info.get(0).getBALANCE();
											
											job.addProperty("Serial",serial_no);
											job.addProperty("SERCODE",sercode);
											job.addProperty("DESCRIPTION",bal_desc);
											
										    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,sercode,serial_no,bal_desc); //Adding Response in bots004
											
										    js.add(job);
								    }
								
								   
								String sql1 = "select * from menu008 where CHCODE= ? and M2CODE= ?";
								 
								List<Menu008>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M00MM" } ,new Menu008Mapper ());
									
								 for (int i=0; i<Info1.size(); i++) {
										
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial","");
										job.addProperty("SERCODE",Info1.get(i).getM2CODE());
										job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
								
										js.add(job);
								    }
							
								
								Common_Utils util = new Common_Utils();  
								
								details.addProperty("Paytype", "demo");
								details.addProperty("CHCODE", CHCODE);
								details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
								details.addProperty("TYPE", "REQ");	 //REQ or RES
								
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
				
			}
		return details;

	}
		
		
		
		//*****************************  New Recent_Transactions *********************************//

		
		public JsonObject Recent_Transactions001(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
		{
			
			String menucode1 = "";

			String sqlcount = "select count (*) from ips_account_view001 where MOBILENO = ?";
			 
			int Accno_count  = Jdbctemplate.queryForObject(sqlcount, new Object[] { MOBILENO } , Integer.class);
			
			
			if (ADDINFO2.equalsIgnoreCase("Recent Transactions")) {
				menucode1 = "M0017H";
			}
			
			
			JsonObject details = new JsonObject();
			
			if ((Accno_count != 1) && (Accno_count != 0)) {         //More than one Acc
				
				if (ADDINFO2.equalsIgnoreCase("Recent Transactions")) { 
					
					try
					{		
							String sql = "select * from menu008 where CHCODE= ? and M2CODE= ?";
							 
							List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode1 } ,new Menu008Mapper ());
							
							JsonArray js = new JsonArray();

							   for (int i=0; i < Info.size(); i++) {
								
				                    JsonObject job = new JsonObject();
									
									job.addProperty("Serial",Info.get(i).getMENUSL());
									job.addProperty("SERCODE",Info.get(i).getM2CODE());
									job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
							
									js.add(job);
							    }
						
																		
							String sql1 = "select * from ips_account_view001 where MOBILENO = ?";
							 
							List<IPS_ACCOUNT_VIEW001>  Info1 = Jdbctemplate.query(sql1, new Object[] { MOBILENO } ,new IPS_ACCOUNT_VIEW001Mapper ());
										
							
							for (int j = 0; j < Info1.size(); j++) {
																
			                    JsonObject job = new JsonObject();

			                    job.addProperty("Serial",j+1);
								job.addProperty("SERCODE",Info1.get(j).getMRCODE());
								job.addProperty("DESCRIPTION",Info1.get(j).getACCNO());
								
								js.add(job);
								
							}
							
							
							Common_Utils util = new Common_Utils();  
							
							details.addProperty("Paytype", "demo");
							details.addProperty("CHCODE", CHCODE);
							details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
							details.addProperty("TYPE", "REQ");	 //REQ or RES
							
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
							
				}
				
				if (SERCODE.equalsIgnoreCase("M0RT")) { 
					
					try
					{		
						SimpleDateFormat simpledate = new SimpleDateFormat("dd-MMM-yyyy");

						SimpleDateFormat parsedate = new SimpleDateFormat("yyyy-MM-dd");
						
						Common_Utils util = new Common_Utils();  
						//Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,REQMSG); //Adding Request in bots003 
						
							String sql = "select * from Transactions where ACCOUNTNO = ? and rownum <= 5";
							 
							List<TRANSACTIONS>  Info = Jdbctemplate.query(sql, new Object[] {ADDINFO2} ,new TRANSACTIONSMapper());
										
							JsonArray js = new JsonArray();
							
							String tot_reqmsg = "";
							
							System.out.println("Recent Transaction -"+tot_reqmsg);
							
								for(int i=0; i<Info.size(); i++)
								{ 
									String DBCR = "";
									String reqmsg = "";
									int serial = i+1;

									if (Info.get(i).getDBCR().equalsIgnoreCase("D")) {
									    DBCR = "Debited";
									}else {
										DBCR = "Credited";
									}
									JsonObject job = new JsonObject();
															
									
									job.addProperty("Serial",serial);
									job.addProperty("SERCODE",SERCODE);
									
//									  reqmsg = "Acc.No "+Info.get(i).getACCOUNTNO()+
//						                       " Amount: "+Info.get(i).getAMOUNT()+" "+Info.get(i).getTRANCURR()+" "+DBCR+" "+
//						                       "Remarks: "+Info.get(i).getREMARKS()+" "+
//				                                "Date: "+Info.get(i).getTRANDATE();
									  
									reqmsg = /* "Transaction Type "Info.get(i).getTRANTYPE()+" "+*/
											/* "Transaction Date "*/simpledate.format(parsedate.parse(Info.get(i).getTRANDATE()))+" "+
											/* "Remarks: " */Info.get(i).getREMARKS()+" "+
											/* "Transaction Amount "*/Info.get(i).getAMOUNT()+" "+Info.get(i).getTRANCURR()+" "+DBCR+" "+
											/* "Balance " */"balance is "+Info.get(i).getTRANCURR()+"."+Info.get(i).getSYSAMOUNT()
											+ " (Tnx Ref no "+/* "Batch Ref no: " */Info.get(i).getCHREFNO()+")";
											

						                       
									tot_reqmsg += reqmsg;
									
									job.addProperty("DESCRIPTION",reqmsg);
									
						//	sk???	   //// Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,sercode,serial_no,bal_desc); //Adding Response in bots004 


									js.add(job);
									
								}
						
																		
							String sql2 = "select * from menu008 where CHCODE= ? and M2CODE= ?";
							 
							List<Menu008>  Info2 = Jdbctemplate.query(sql2, new Object[] { CHCODE,"M00MM" } ,new Menu008Mapper ());
										
							
							for (int j = 0; j < Info2.size(); j++) {
																
			                    JsonObject job = new JsonObject();

			                    job.addProperty("Serial","");
								job.addProperty("SERCODE",Info2.get(0).getM2CODE());
								job.addProperty("DESCRIPTION",Info2.get(0).getMENUDESC());
								
								js.add(job);
								
							}
							
							
							details.addProperty("Paytype", "demo");
							details.addProperty("CHCODE", CHCODE);
							details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
							details.addProperty("TYPE", "REQ");	 //REQ or RES
							
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
				}
			
			} else if ((Accno_count == 1) && (Accno_count != 0)) { //less than one Acc
		
				String Accno = "" ;
				
				
				if (SERCODE.equalsIgnoreCase("M011")) {
					
					try{
						String Acc_sql = "select * from ips_account_view001 where MOBILENO = ?";
						 
						List<IPS_ACCOUNT_VIEW001>  AccInfo = Jdbctemplate.query(Acc_sql, new Object[] {MOBILENO} ,new IPS_ACCOUNT_VIEW001Mapper());
						Accno = AccInfo.get(0).getACCNO();
					
					SimpleDateFormat simpledate = new SimpleDateFormat("dd-MMM-yyyy");

					SimpleDateFormat parsedate = new SimpleDateFormat("yyyy-MM-dd");
					
					Common_Utils util = new Common_Utils();  
					//Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,REQMSG); //Adding Request in bots003 
					
						String sql = "select * from Transactions where ACCOUNTNO = ? and rownum <= 5";
						 
						List<TRANSACTIONS>  Info = Jdbctemplate.query(sql, new Object[] {Accno} ,new TRANSACTIONSMapper());
									
						JsonArray js = new JsonArray();
						
						String tot_reqmsg = "";
						
						System.out.println("Recent Transaction -"+tot_reqmsg);
						
							for(int i=0; i<Info.size(); i++)
							{ 
								String DBCR = "";
								String reqmsg = "";
								int serial = i+1;

								if (Info.get(i).getDBCR().equalsIgnoreCase("D")) {
								    DBCR = "Debited";
								}else {
									DBCR = "Credited";
								}
								JsonObject job = new JsonObject();
														
								
								job.addProperty("Serial",serial);
								job.addProperty("SERCODE",SERCODE);
								
//								  reqmsg = "Acc.No "+Info.get(i).getACCOUNTNO()+
//					                       " Amount: "+Info.get(i).getAMOUNT()+" "+Info.get(i).getTRANCURR()+" "+DBCR+" "+
//					                       "Remarks: "+Info.get(i).getREMARKS()+" "+
//			                                "Date: "+Info.get(i).getTRANDATE();
								  
								reqmsg = /* "Transaction Type "Info.get(i).getTRANTYPE()+" "+*/
										/* "Transaction Date "*/simpledate.format(parsedate.parse(Info.get(i).getTRANDATE()))+" "+
										/* "Remarks: " */Info.get(i).getREMARKS()+" "+
										/* "Transaction Amount "*/Info.get(i).getAMOUNT()+" "+Info.get(i).getTRANCURR()+" "+DBCR+" "+
										/* "Balance " */"balance is "+Info.get(i).getTRANCURR()+"."+Info.get(i).getSYSAMOUNT()
										+ " (Tnx Ref no "+/* "Batch Ref no: " */Info.get(i).getCHREFNO()+")";
										

					                       
								tot_reqmsg += reqmsg;
								
								job.addProperty("DESCRIPTION",reqmsg);
								
					//	sk???	   //// Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,sercode,serial_no,bal_desc); //Adding Response in bots004 


								js.add(job);
								
							}
							
							
							String sql2 = "select * from menu008 where CHCODE= ? and M2CODE= ?";
							 
							List<Menu008>  Info2 = Jdbctemplate.query(sql2, new Object[] { CHCODE,"M00MM" } ,new Menu008Mapper ());
										
							
							for (int j = 0; j < Info2.size(); j++) {
																
			                    JsonObject job = new JsonObject();

			                    job.addProperty("Serial","");
								job.addProperty("SERCODE",Info2.get(0).getM2CODE());
								job.addProperty("DESCRIPTION",Info2.get(0).getMENUDESC());
								
								js.add(job);
								
							}
							
							details.addProperty("Paytype", "demo");
							details.addProperty("CHCODE", CHCODE);
							details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
							details.addProperty("TYPE", "REQ");	 //REQ or RES
							
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
				}
										
			}
				
		return details;

	}
			
		
		
		//*****************************  Recent_Transactions *********************************//

		
		public JsonObject Recent_Transactions(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
		{
			
			JsonObject details = new JsonObject();
			
			 String ACC_No = "";
			
			SimpleDateFormat simpledate = new SimpleDateFormat("dd-MMM-yyyy");

			SimpleDateFormat parsedate = new SimpleDateFormat("yyyy-MM-dd");
		
					if (SERCODE.equalsIgnoreCase("M011")) {
						
						if (INPUT.equalsIgnoreCase("2")) {
							ACC_No = "39995686984";
						}
						
						try
						{	
							Common_Utils util = new Common_Utils();  
							//Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,REQMSG); //Adding Request in bots003 
							
								String sql = "select * from Transactions where ACCOUNTNO = ? and rownum <= 5";
								 
								List<TRANSACTIONS>  Info = Jdbctemplate.query(sql, new Object[] {ACC_No} ,new TRANSACTIONSMapper());
											
								JsonArray js = new JsonArray();
								
								
								String tot_reqmsg = "";
								
								System.out.println("Recent Transaction -"+tot_reqmsg);
								
									for(int i=0; i<Info.size(); i++)
									{ 
										String DBCR = "";
										String reqmsg = "";
										int serial = i+1;

										if (Info.get(i).getDBCR().equalsIgnoreCase("D")) {
										    DBCR = "Debited";
										}else {
											DBCR = "Credited";
										}
										JsonObject job = new JsonObject();
																
										
										job.addProperty("Serial",serial);
										job.addProperty("SERCODE",SERCODE);
										
//										  reqmsg = "Acc.No "+Info.get(i).getACCOUNTNO()+
//							                       " Amount: "+Info.get(i).getAMOUNT()+" "+Info.get(i).getTRANCURR()+" "+DBCR+" "+
//							                       "Remarks: "+Info.get(i).getREMARKS()+" "+
//					                                "Date: "+Info.get(i).getTRANDATE();
										  
										reqmsg = /* "Transaction Type "Info.get(i).getTRANTYPE()+" "+*/
												/* "Transaction Date "*/simpledate.format(parsedate.parse(Info.get(i).getTRANDATE()))+" "+
												/* "Remarks: " */Info.get(i).getREMARKS()+" "+
												/* "Transaction Amount "*/Info.get(i).getAMOUNT()+" "+Info.get(i).getTRANCURR()+" "+DBCR+" "+
												/* "Balance " */"balance is "+Info.get(i).getTRANCURR()+"."+Info.get(i).getSYSAMOUNT()
												+ " (Tnx Ref no "+/* "Batch Ref no: " */Info.get(i).getCHREFNO()+")";
												

							                       
										tot_reqmsg += reqmsg;
										
										job.addProperty("DESCRIPTION",reqmsg);
										
							//	sk???	   //// Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,sercode,serial_no,bal_desc); //Adding Response in bots004 

																								
										js.add(job);
										
									}


									String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ?";
									 
									List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M00MM" } ,new Menu007Mapper ());
									
									   for (int i=0; i<Info1.size(); i++) {
										
						                    JsonObject job = new JsonObject();
											
											job.addProperty("Serial","");
											job.addProperty("SERCODE",Info1.get(i).getM1CODE());
											job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
									
											js.add(job);
									    }
									
									
									
//									if(!util.isNullOrEmpty(tot_reqmsg))
//									{
//										Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,Session_Id,commtime,SERCODE,"0",tot_reqmsg); //Adding Request in bots003 
//									}

									        
								
								
								details.addProperty("Paytype", "demo");
								details.addProperty("CHCODE", CHCODE);
								details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
								details.addProperty("TYPE", "REQ");	 //REQ or RES
								

								
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
								
					}
					
					
															
					
			 return details;
		}
		
		
		//*****************************  Transfer *********************************//

		
		public JsonObject Transfer(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
		{
			
			JsonObject details = new JsonObject();
			
						String menucode1 = "";  String menucode2 = ""; 	String MENUSL = "";

			
						if (SERCODE.equalsIgnoreCase("M011") && ADDINFO2.equalsIgnoreCase("Transfer")) {                
							menucode1 = "M00014H";							  					 
							MENUSL = "0";
						} 
						else if (SERCODE.equalsIgnoreCase("M00014H") && ADDINFO2.equalsIgnoreCase("Enter the Destination Bank Code") ) {
							menucode1 = "M00014H";							  					 
							MENUSL = "1";
						}
						else if (SERCODE.equalsIgnoreCase("M00014H") && ADDINFO2.equalsIgnoreCase("Enter the Destination Account Number") ) {
							menucode1 = "M00014H";							  					 
							MENUSL = "2";
						}
						else if (SERCODE.equalsIgnoreCase("M00014H") && ADDINFO2.equalsIgnoreCase("Enter the Transaction Amount") ) {
							menucode1 = "M00014H";		
							menucode2 = "M00014C"; 
							MENUSL = "3";
						}
						else if (SERCODE.equalsIgnoreCase("M00014H") && ADDINFO2.equalsIgnoreCase("Select the Transaction Currency") ) {
							menucode1 = "M00014H"; 
							//MENUSL = "4";
						}
						else if (SERCODE.equalsIgnoreCase("M00014C") && ADDINFO2.equalsIgnoreCase("TZS") ) {
							menucode1 = "M000013H";							  					  
						}
						else if (SERCODE.equalsIgnoreCase("M00014C") && ADDINFO2.equalsIgnoreCase("USD") ) {
							menucode1 = "M000013H";							  					  
						}
						else if (SERCODE.equalsIgnoreCase("M000013H") && ADDINFO2.equalsIgnoreCase("Enter the OTP") ) {
							if (INPUT.equalsIgnoreCase("1234")) {
								menucode1 = "M000011H"; 							 					//All services
							} else {
								menucode1 = "M000014H"; 							  					//Invalid PIN
							}
						}
						
						
						
						if (SERCODE.equalsIgnoreCase("M00014H") && ADDINFO2.equalsIgnoreCase("Enter the Transaction Amount") ) {
							
							try
							{	
								//Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,REQMSG); //Adding Request in bots003 

									MENUSL = (Integer.parseInt(MENUSL)+1)+"";
								
									String sql = "select * from menu009 where CHCODE = ? and M3CODE = ? and MENUSL = ?";
									 
									List<Menu009>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode1 , MENUSL } ,new Menu009Mapper ());
												
									JsonArray js = new JsonArray();
									
									if (Info.size() != 0) {
										
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial",Info.get(0).getMENUSL());
										job.addProperty("SERCODE",Info.get(0).getM3CODE());
										job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());		
										
									    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(0).getM3CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding Response in bots004 
								
										js.add(job);
										
									}
									
									
									String sql1 = "select * from menu009 where CHCODE = ? and M3CODE = ?";
									 
									List<Menu009>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, menucode2 } ,new Menu009Mapper ());
												
									for (int i=0; i<Info1.size(); i++) {
										
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial",Info1.get(i).getMENUSL());
										job.addProperty("SERCODE",Info1.get(i).getM3CODE());
										job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
										
									    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info1.get(i).getM3CODE(),Info1.get(i).getMENUSL(),Info1.get(i).getMENUDESC()); //Adding Response in bots004 
								
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
							
					}
						

						else if (SERCODE.equalsIgnoreCase("M00014H") && ADDINFO2.equalsIgnoreCase("Enter the Destination Account Number") ) {
							

							String sqlcount = "select count (*) from ips_account_view001 where MOBILENO = ?";
							 
							int Accno_count  = Jdbctemplate.queryForObject(sqlcount, new Object[] { MOBILENO } , Integer.class);
							
							try
							{	
								if ((Accno_count != 1) && (Accno_count != 0)) {         //More than one Acc
									
									if (ADDINFO2.equalsIgnoreCase("Enter the Destination Account Number")) { 
										
																				
										try
										{		
												String sql = "select * from menu008 where CHCODE= ? and M2CODE= ?";  // select the Acc no
												 
												List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, "M0018H" } ,new Menu008Mapper ());
												
												JsonArray js = new JsonArray();

												   for (int i=0; i < Info.size(); i++) {
													
									                    JsonObject job = new JsonObject();
														
														job.addProperty("Serial",Info.get(i).getMENUSL());
														job.addProperty("SERCODE",Info.get(i).getM2CODE());
														job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
												
														js.add(job);
												    }
											
																							
												String sql1 = "select * from ips_account_view001 where MOBILENO = ?";  // it shows the Acc no 
												 
												List<IPS_ACCOUNT_VIEW001>  Info1 = Jdbctemplate.query(sql1, new Object[] { MOBILENO } ,new IPS_ACCOUNT_VIEW001Mapper ());
															
												
												for (int j = 0; j < Info1.size(); j++) {
																					
								                    JsonObject job = new JsonObject();

								                    job.addProperty("Serial",j+1);
													job.addProperty("SERCODE",Info1.get(j).getMTCODE());
													job.addProperty("DESCRIPTION",Info1.get(j).getACCNO());
													
													js.add(job);
													 
												}
												
												
												Common_Utils util = new Common_Utils();  
												
												details.addProperty("Paytype", "demo");
												details.addProperty("CHCODE", CHCODE);
												details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
												details.addProperty("TYPE", "REQ");	 //REQ or RES
												
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
									}
									
									
									
								
								}else {   // for single Acc no 
											try
											{	
												
												
												String sql = "select * from menu009 where CHCODE = ? and M3CODE = ? and MENUSL = ?";
												 
												List<Menu009>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, "M00014H" , "3" } ,new Menu009Mapper ());
															
												JsonArray js = new JsonArray();
												
												if (Info.size() != 0) {
													
								                    JsonObject job = new JsonObject();
													
													job.addProperty("Serial",Info.get(0).getMENUSL());
													job.addProperty("SERCODE",Info.get(0).getM3CODE());
													job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
													
												    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(0).getM3CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding Response in bots004 
											
													js.add(job);
													
													if (Accno_count==1) {
														 Common_Utils utils = new Common_Utils();															
													    Timestamp newcommtime = utils.get_oracle_Timestamp();
													    
														String sql2 = "select * from ips_account_view001 where MOBILENO = ?";  // it insert the Acc no in bos004
														 
														List<IPS_ACCOUNT_VIEW001>  Info2 = Jdbctemplate.query(sql2, new Object[] { MOBILENO } ,new IPS_ACCOUNT_VIEW001Mapper ());
																														
												   
														Insert_bot004(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,
																newcommtime,SERCODE,"1",Info2.get(0).getACCNO()); //Adding Response in bots004 
														
													}
													
												}
													
													   												
													
													Common_Utils util = new Common_Utils();  
													
													details.addProperty("Paytype", "demo");
													details.addProperty("CHCODE", CHCODE);
													details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
													details.addProperty("TYPE", "REQ");	 //REQ or RES
													
													details.add("data", js);
													 			 
													details.addProperty("result", "success");
													details.addProperty("stscode", "HP00");
													details.addProperty("message", "Menus found !!");
													
													
											 }catch(Exception e) {
												 details.addProperty("result", "failed");
												 details.addProperty("stscode", "HP06");
												 details.addProperty("message", e.getLocalizedMessage());   	
												 
												 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
											 }
								
								}
								
							}catch(Exception e)
							 {
								 details.addProperty("result", "failed");
								 details.addProperty("stscode", "HP06");
								 details.addProperty("message", e.getLocalizedMessage());   	
								 
								 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
							 }
								
								
						}				

							
						else if (SERCODE.equalsIgnoreCase("M0T")) {
							
							try
							{	
								String sql = "select * from menu009 where CHCODE = ? and M3CODE = ? and MENUSL = ?";
								 
								List<Menu009>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, "M00014H" , "3" } ,new Menu009Mapper ());
											
								JsonArray js = new JsonArray();
								
								if (Info.size() != 0) {
									
				                    JsonObject job = new JsonObject();
									
									job.addProperty("Serial",Info.get(0).getMENUSL());
									job.addProperty("SERCODE",Info.get(0).getM3CODE());
									job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
									
								    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(0).getM3CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding Response in bots004 
							
									js.add(job);
									
								}
									
									   												
									
									Common_Utils util = new Common_Utils();  
									
									details.addProperty("Paytype", "demo");
									details.addProperty("CHCODE", CHCODE);
									details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
									details.addProperty("TYPE", "REQ");	 //REQ or RES
									
									details.add("data", js);
									 			 
									details.addProperty("result", "success");
									details.addProperty("stscode", "HP00");
									details.addProperty("message", "Menus found !!");
									
									
							 }catch(Exception e)
							 {
								 details.addProperty("result", "failed");
								 details.addProperty("stscode", "HP06");
								 details.addProperty("message", e.getLocalizedMessage());   	
								 
								 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
							 }
							
					}
						
						
		
						else if (menucode1.equalsIgnoreCase("M00014H")) {
						
						try
						{	
							//Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,COUNTRYCD,sessionid,commtime,SERCODE,SELECTCD,REQMSG); //Adding Request in bots003 

								MENUSL = (Integer.parseInt(MENUSL)+1)+"";
							
								String sql = "select * from menu009 where CHCODE = ? and M3CODE = ? and MENUSL = ?";
								 
								List<Menu009>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode1 , MENUSL } ,new Menu009Mapper ());
											
								JsonArray js = new JsonArray();
								
								if (Info.size() != 0) {
									
				                    JsonObject job = new JsonObject();
									
									job.addProperty("Serial",Info.get(0).getMENUSL());
									job.addProperty("SERCODE",Info.get(0).getM3CODE());
									job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
									
								    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(0).getM3CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding Response in bots004 
							
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
						
				}
				
																												
				else if (SERCODE.equalsIgnoreCase("M00014C") && ( ADDINFO2.equalsIgnoreCase("TZS")|| ADDINFO2.equalsIgnoreCase("USD"))){  
					
				
					try
					{	
							String sql = "select * from bots004 c where c.mobileno = ? and c.menucd in ('M00014H', 'M0T', 'M00014C') and SESSIONID = (select SESSIONID from (select SESSIONID from bots004 x where x.mobileno = ? order by COMMTIME desc) where rownum = 1) order by COMMTIME";
							 
							List<Bots004>  Info = Jdbctemplate.query(sql, new Object[] { MOBILENO , MOBILENO } ,new Bots004Mapper ());
										
							JsonArray js = new JsonArray();
								
//							for (int i = 0; i < Info.size(); i++) {                    /////BANK CODE - EXIM0012022
//								   JsonObject job = new JsonObject();			
//									job.addProperty("Serial",i);
//								    job.addProperty("SERCODE",Info.get(0).getMENUCD());
//									job.addProperty("DESCRIPTION","Destination Acc no - "+Info.get(0).getSELECTCD());
//									js.add(job);
//								
//							}
							
							
			                    JsonObject job = new JsonObject();			/////Destination Acc no - 1234567890
								job.addProperty("Serial","1");
								job.addProperty("SERCODE",Info.get(1).getMENUCD());
								job.addProperty("DESCRIPTION","Destination Acc no - "+Info.get(1).getSELECTCD());
								js.add(job);
								
								
			                    JsonObject job1 = new JsonObject();			 	/////Source Acc no - 1000
								job1.addProperty("Serial","2");
								job1.addProperty("SERCODE",Info.get(2).getMENUCD());
								job1.addProperty("DESCRIPTION","Source Acc no - "+Info.get(2).getRESMSG());
								js.add(job1);
								
								
			                    JsonObject job2 = new JsonObject();				 /////Transaction Amount - 1000
								job2.addProperty("Serial","3");
								job2.addProperty("SERCODE",Info.get(3).getMENUCD());
								job2.addProperty("DESCRIPTION","Transaction Amount - "+Info.get(3).getSELECTCD());
								js.add(job2);
								
								
			                    JsonObject job3 = new JsonObject();		/////Transaction currecncy Type - TZS OR USD
								job3.addProperty("Serial","4");
								job3.addProperty("SERCODE",Info.get(4).getMENUCD());
								job3.addProperty("DESCRIPTION","Transaction currecncy Type - "+Info.get(4).getRESMSG());
								js.add(job3);
								
								
								
								String sql1 = "select * from menu009 where CHCODE = ? and M3CODE = ? ";    
							 
								List<Menu009>  Info2 = Jdbctemplate.query(sql1, new Object[] { CHCODE,"M00018" } ,new Menu009Mapper ());
										
								
								for (int i=0; i < Info2.size(); i++) {
																		
				                    JsonObject job4 = new JsonObject();
									
									job4.addProperty("Serial",Info2.get(i).getMENUSL());
									job4.addProperty("SERCODE",Info2.get(i).getM3CODE());
									job4.addProperty("DESCRIPTION",Info2.get(i).getMENUDESC());
									
								    //Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info2.get(i).getM3CODE(),Info2.get(i).getMENUSL(),Info2.get(i).getMENUDESC()); //Adding request in bots003 

												
									js.add(job4);
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
				}
						
				
					
				else if (SERCODE.equalsIgnoreCase("M00014H")){
					
					try
					{	
							String sql = "select * from menu010 where CHCODE = ? and M4CODE = ? ";
							 
							List<Menu010>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode1  } ,new Menu010Mapper ());
										
							JsonArray js = new JsonArray();
							
							if (Info.size() != 0 ) {
								
			                    JsonObject job = new JsonObject();
								
								job.addProperty("Serial",Info.get(0).getMENUSL());
								job.addProperty("SERCODE",Info.get(0).getM4CODE());
								job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
								
							    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(0).getM4CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding request in bots003 

														
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
				}	
					
				else if (SERCODE.equalsIgnoreCase("M000013H")){ 
						
						try
						{	
								String sql = "select * from menu010 where CHCODE = ? and M4CODE = ? ";
								 
								List<Menu010>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode1 } ,new Menu010Mapper ());
											
								JsonArray js = new JsonArray();
								
								if (Info.size() != 0 ) {
									
				                    JsonObject job = new JsonObject();
				                    
				                    if (Info.get(0).getMENUDESC().equalsIgnoreCase("Transaction has been initiated successfully")) {
				                    	job.addProperty("Serial",Info.get(0).getMENUSL());
										job.addProperty("SERCODE",Info.get(0).getM4CODE());
										job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC()+"(Tnx Ref no 0184165922)");
				                    }else {
				                    	job.addProperty("Serial",Info.get(0).getMENUSL());
										job.addProperty("SERCODE",Info.get(0).getM4CODE());
										job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());				                    	
									}
																		
								    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(0).getM4CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding request in bots003 

															
									js.add(job);
									
								}
								
								String sql1 = "select * from menu008 where CHCODE= ? and M2CODE= ?";
								 
								List<Menu008>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M00MM" } ,new Menu008Mapper ());
								
								   for (int i=0; i<Info1.size(); i++) {
									
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial","");
										job.addProperty("SERCODE",Info1.get(i).getM2CODE());
										job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
								
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
					}	
							
					
				else if (ADDINFO2.equalsIgnoreCase("Enter the Input '1' for confirm the Transaction") && INPUT.equalsIgnoreCase("1")) {
						
					
					try
					{	
						String sql = "select * from menu010 where CHCODE = ? and M4CODE = ? and MENUSL = ?";
						 
						List<Menu010>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, "M000013H" , "1" } ,new Menu010Mapper ());
									
						JsonArray js = new JsonArray();
						
						if (Info.size() != 0) {
							
		                    JsonObject job = new JsonObject();
							
							job.addProperty("Serial",Info.get(0).getMENUSL());
							job.addProperty("SERCODE",Info.get(0).getM4CODE());
							job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
							
						    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(0).getM4CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding Response in bots004 
				
							js.add(job);
						
						}
						
						String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ?";
						 
						List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M0MM" } ,new Menu007Mapper ());
						
						   for (int i=0; i<Info1.size(); i++) {
							
			                    JsonObject job = new JsonObject();
								
								job.addProperty("Serial","");
								job.addProperty("SERCODE",Info1.get(i).getM1CODE());
								job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
						
								js.add(job);
						    }
							
							
							Common_Utils util = new Common_Utils();  
							
							details.addProperty("Paytype", "demo");
							details.addProperty("CHCODE", CHCODE);
							details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
							details.addProperty("TYPE", "REQ");	 //REQ or RES
							
							details.add("data", js);
							 			 
							details.addProperty("result", "success");
							details.addProperty("stscode", "HP00");
							details.addProperty("message", "Menus found !!");
							
							
					 }catch(Exception e)
					 {
						 details.addProperty("result", "failed");
						 details.addProperty("stscode", "HP06");
						 details.addProperty("message", e.getLocalizedMessage());   	
						 
						 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
					 }
					
			}
			
				else if (ADDINFO2.equalsIgnoreCase("Enter the Input '2' for cancel the Transaction") && INPUT.equalsIgnoreCase("2")
						) {
					
					try
					{	
						String sql = "select * from menu010 where CHCODE = ? and M4CODE = ? and MENUSL = ?";
						 
						List<Menu010>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, "M000015H" , "1" } ,new Menu010Mapper ());
									
						JsonArray js = new JsonArray();
						
						if (Info.size() != 0) {
							
		                    JsonObject job = new JsonObject();
							
							job.addProperty("Serial",Info.get(0).getMENUSL());
							job.addProperty("SERCODE",Info.get(0).getM4CODE());
							job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
							
						    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(0).getM4CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding Response in bots004 
				
							js.add(job);
						
						}
						
						String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ?";
						 
						List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M0MM" } ,new Menu007Mapper ());
						
						   for (int i=0; i<Info1.size(); i++) {
							
			                    JsonObject job = new JsonObject();
								
								job.addProperty("Serial","");
								job.addProperty("SERCODE",Info1.get(i).getM1CODE());
								job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
						
								js.add(job);
						    }
							
							
							Common_Utils util = new Common_Utils();  
							
							details.addProperty("Paytype", "demo");
							details.addProperty("CHCODE", CHCODE);
							details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
							details.addProperty("TYPE", "REQ");	 //REQ or RES
							
							details.add("data", js);
							 			 
							details.addProperty("result", "success");
							details.addProperty("stscode", "HP00");
							details.addProperty("message", "Menus found !!");
							
							
					 }catch(Exception e)
					 {
						 details.addProperty("result", "failed");
						 details.addProperty("stscode", "HP06");
						 details.addProperty("message", e.getLocalizedMessage());   	
						 
						 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
					 }
					
			}
						
						
						
						
						
						
										
			 return details;
		}
		
	
		//*****************************  Cheque_Book_Request *********************************//

		
		public JsonObject Cheque_Book_Request(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
		{
			
			JsonObject details = new JsonObject();
			
						String menucode1 = "";  

			
						if (SERCODE.equalsIgnoreCase("M011") && ADDINFO2.equalsIgnoreCase("Cheque Book Request")) {                
							menucode1 = "M0013";							  					  //Existing Customer
						}
						else if (SERCODE.equalsIgnoreCase("M0013")) {                
							menucode1 = "M00011H";							  					  //Existing Customer
						}
						
						
						
						
						if (SERCODE.equalsIgnoreCase("M011")) {
							
							String sqlcount = "select count (*) from ips_account_view001 where MOBILENO = ?";
							 
							int Accno_count  = Jdbctemplate.queryForObject(sqlcount, new Object[] { MOBILENO } , Integer.class);
							
							try
							{	
								if ((Accno_count != 1) && (Accno_count != 0)) {         //More than one Acc
									
									if (ADDINFO2.equalsIgnoreCase("Cheque Book Request")) { 
										
																				
										try
										{		
												String sql = "select * from menu008 where CHCODE= ? and M2CODE= ?";  // select the Acc no
												 
												List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, "M0017H" } ,new Menu008Mapper ());
												
												JsonArray js = new JsonArray();

												   for (int i=0; i < Info.size(); i++) {
													
									                    JsonObject job = new JsonObject();
														
														job.addProperty("Serial",Info.get(i).getMENUSL());
														job.addProperty("SERCODE",Info.get(i).getM2CODE());
														job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
												
														js.add(job);
												    }
											
																							
												String sql1 = "select * from ips_account_view001 where MOBILENO = ?";  // it shows the Acc no 
												 
												List<IPS_ACCOUNT_VIEW001>  Info1 = Jdbctemplate.query(sql1, new Object[] { MOBILENO } ,new IPS_ACCOUNT_VIEW001Mapper ());
															
												
												for (int j = 0; j < Info1.size(); j++) {
																					
								                    JsonObject job = new JsonObject();

								                    job.addProperty("Serial",j+1);
													job.addProperty("SERCODE",Info1.get(j).getMCBCODE());
													job.addProperty("DESCRIPTION",Info1.get(j).getACCNO());
													
													js.add(job);
													 
												}
												
												
												Common_Utils util = new Common_Utils();  
												
												details.addProperty("Paytype", "demo");
												details.addProperty("CHCODE", CHCODE);
												details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
												details.addProperty("TYPE", "REQ");	 //REQ or RES
												
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
									}
									
									
									
								
								}else {   // for single Acc no 
											try
											{	

												String sql1 = "select * from menu008 where CHCODE = ? and M2CODE = ? ";    
												 
												List<Menu008>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE,"M0013H" } ,new Menu008Mapper ());
															
												JsonArray js = new JsonArray();
													
														for(int i=0; i<Info1.size(); i++) {

														
									                    JsonObject job = new JsonObject();
														
														job.addProperty("Serial",Info1.get(i).getMENUSL());
														job.addProperty("SERCODE",Info1.get(i).getM2CODE());
														job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
														
													    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info1.get(i).getM2CODE(),Info1.get(i).getMENUSL(),Info1.get(i).getMENUDESC()); //Adding request in bots003 
												
														js.add(job);
														
													}
												
												String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? ";    
												 
												List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE,"M0013" } ,new Menu008Mapper ());
																												
														for(int i=0; i<Info.size(); i++) {

														
									                    JsonObject job = new JsonObject();
														
														job.addProperty("Serial",Info.get(i).getMENUSL());
														job.addProperty("SERCODE",Info.get(i).getM2CODE());
														job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
														
													    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM2CODE(),Info.get(i).getMENUSL(),Info.get(i).getMENUDESC()); //Adding request in bots003 
												
														js.add(job);
														
													}													   												
													
													Common_Utils util = new Common_Utils();  
													
													details.addProperty("Paytype", "demo");
													details.addProperty("CHCODE", CHCODE);
													details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
													details.addProperty("TYPE", "REQ");	 //REQ or RES
													
													details.add("data", js);
													 			 
													details.addProperty("result", "success");
													details.addProperty("stscode", "HP00");
													details.addProperty("message", "Menus found !!");
													
													
											 }catch(Exception e) {
												 details.addProperty("result", "failed");
												 details.addProperty("stscode", "HP06");
												 details.addProperty("message", e.getLocalizedMessage());   	
												 
												 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
											 }
								
								}
								
							}catch(Exception e)
							 {
								 details.addProperty("result", "failed");
								 details.addProperty("stscode", "HP06");
								 details.addProperty("message", e.getLocalizedMessage());   	
								 
								 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
							 }
							
					}
						
						
						else if (SERCODE.equalsIgnoreCase("M0CBR")) {
							
							try
							{	
								String sql1 = "select * from menu008 where CHCODE = ? and M2CODE = ?";  // it shows the Acc no 
								 
								List<Menu008>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE,"M0013H" } ,new Menu008Mapper ());
											
								JsonArray js = new JsonArray();

								for (int j = 0; j < Info1.size(); j++) {
																	
				                    JsonObject job = new JsonObject();

				                	job.addProperty("Serial",Info1.get(j).getMENUSL());
									job.addProperty("SERCODE",Info1.get(j).getM2CODE());
									job.addProperty("DESCRIPTION",Info1.get(j).getMENUDESC());
									
									js.add(job);
									 
								}
								
								String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? ";    
								 
								List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE,"M0013" } ,new Menu008Mapper ());
											
									
										for(int i=0; i<Info.size(); i++) {
										
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial",Info.get(i).getMENUSL());
										job.addProperty("SERCODE",Info.get(i).getM2CODE());
										job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
										
									    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM2CODE(),Info.get(i).getMENUSL(),Info.get(i).getMENUDESC()); //Adding request in bots003 
								
										js.add(job);
										
									}
									
									   												
									
									Common_Utils util = new Common_Utils();  
									
									details.addProperty("Paytype", "demo");
									details.addProperty("CHCODE", CHCODE);
									details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
									details.addProperty("TYPE", "REQ");	 //REQ or RES
									
									details.add("data", js);
									 			 
									details.addProperty("result", "success");
									details.addProperty("stscode", "HP00");
									details.addProperty("message", "Menus found !!");
									
									
							 }catch(Exception e)
							 {
								 details.addProperty("result", "failed");
								 details.addProperty("stscode", "HP06");
								 details.addProperty("message", e.getLocalizedMessage());   	
								 
								 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
							 }
							
					}	
						
						
														
				if (SERCODE.equalsIgnoreCase("M0013")) {
					
					try
					{	
						String sql = "select * from menu009 where CHCODE = ? and M3CODE = ? ";    
						 
						List<Menu009>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE,menucode1 } ,new Menu009Mapper ());
									
						JsonArray js = new JsonArray();
							
								for(int i=0; i<Info.size(); i++) {

								
			                    JsonObject job = new JsonObject();
								
								job.addProperty("Serial",Info.get(i).getMENUSL());
								job.addProperty("SERCODE",Info.get(i).getM3CODE());
								job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
								
							    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM3CODE(),Info.get(i).getMENUSL(),Info.get(i).getMENUDESC()); //Adding request in bots003 
						
								js.add(job);
								
							}
								
								String sql1 = "select * from menu008 where CHCODE= ? and M2CODE= ?";
								 
								List<Menu008>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M00MM" } ,new Menu008Mapper ());
								
								   for (int i=0; i<Info1.size(); i++) {
									
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial","");
										job.addProperty("SERCODE",Info1.get(i).getM2CODE());
										job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
								
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
					
			}
				
											
										
			 return details;
		}
		
		
		//*****************************  Account Statement *********************************//

		public JsonObject Account_Statement(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
		{
			
			JsonObject details = new JsonObject();
			
						String menucode1 = "";  

			
						if (SERCODE.equalsIgnoreCase("M011") && ADDINFO2.equalsIgnoreCase("Account Statement")) {                
							menucode1 = "M0014";							  					  
						}
						else if (SERCODE.equalsIgnoreCase("M0014")) {                
							menucode1 = "M00012H";							  					 
						}
						
						
						
						if (SERCODE.equalsIgnoreCase("M011")) {
							
							String sqlcount = "select count (*) from ips_account_view001 where MOBILENO = ?";
							 
							int Accno_count  = Jdbctemplate.queryForObject(sqlcount, new Object[] { MOBILENO } , Integer.class);
							
							try
							{	
								if ((Accno_count != 1) && (Accno_count != 0)) {         //More than one Acc
									
									if (ADDINFO2.equalsIgnoreCase("Account Statement")) { 
										
																				
										try
										{		
												String sql = "select * from menu008 where CHCODE= ? and M2CODE= ?";  // select the Acc no
												 
												List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, "M0017H" } ,new Menu008Mapper ());
												
												JsonArray js = new JsonArray();

												   for (int i=0; i < Info.size(); i++) {
													
									                    JsonObject job = new JsonObject();
														
														job.addProperty("Serial",Info.get(i).getMENUSL());
														job.addProperty("SERCODE",Info.get(i).getM2CODE());
														job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
												
														js.add(job);
												    }
											
																							
												String sql1 = "select * from ips_account_view001 where MOBILENO = ?";  // it shows the Acc no 
												 
												List<IPS_ACCOUNT_VIEW001>  Info1 = Jdbctemplate.query(sql1, new Object[] { MOBILENO } ,new IPS_ACCOUNT_VIEW001Mapper ());
															
												
												for (int j = 0; j < Info1.size(); j++) {
																					
								                    JsonObject job = new JsonObject();

								                    job.addProperty("Serial",j+1);
													job.addProperty("SERCODE",Info1.get(j).getMASCODE());
													job.addProperty("DESCRIPTION",Info1.get(j).getACCNO());
													
													js.add(job);
													 
												}
												
												
												Common_Utils util = new Common_Utils();  
												
												details.addProperty("Paytype", "demo");
												details.addProperty("CHCODE", CHCODE);
												details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
												details.addProperty("TYPE", "REQ");	 //REQ or RES
												
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
									}
									
									
									
								
								}else {   // for single Acc no 
											try
											{	
												String sql1 = "select * from menu008 where CHCODE= ? and M2CODE= ?";  // select the Acc no
												 
												List<Menu008>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M0014H" } ,new Menu008Mapper ());
												
												JsonArray js = new JsonArray();

												   for (int i=0; i < Info1.size(); i++) {
													
									                    JsonObject job = new JsonObject();
														
														job.addProperty("Serial",Info1.get(i).getMENUSL());
														job.addProperty("SERCODE",Info1.get(i).getM2CODE());
														job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
												
														js.add(job);
												    }

												String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? ";    
												 
												List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE,"M0014" } ,new Menu008Mapper ());
																												
														for(int i=0; i<Info.size(); i++) {

														
									                    JsonObject job = new JsonObject();
														
														job.addProperty("Serial",Info.get(i).getMENUSL());
														job.addProperty("SERCODE",Info.get(i).getM2CODE());
														job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
														
													    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM2CODE(),Info.get(i).getMENUSL(),Info.get(i).getMENUDESC()); //Adding request in bots003 
												
														js.add(job);
														
													}													   												
													
													Common_Utils util = new Common_Utils();  
													
													details.addProperty("Paytype", "demo");
													details.addProperty("CHCODE", CHCODE);
													details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
													details.addProperty("TYPE", "REQ");	 //REQ or RES
													
													details.add("data", js);
													 			 
													details.addProperty("result", "success");
													details.addProperty("stscode", "HP00");
													details.addProperty("message", "Menus found !!");
													
													
											 }catch(Exception e) {
												 details.addProperty("result", "failed");
												 details.addProperty("stscode", "HP06");
												 details.addProperty("message", e.getLocalizedMessage());   	
												 
												 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
											 }
								
								}
								
							}catch(Exception e)
							 {
								 details.addProperty("result", "failed");
								 details.addProperty("stscode", "HP06");
								 details.addProperty("message", e.getLocalizedMessage());   	
								 
								 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
							 }
							
					}
						
						
						if (SERCODE.equalsIgnoreCase("M0AS")) {
							
							try
							{	
								String sql2 = "select * from menu008 where CHCODE = ? and M2CODE = ? ";   
								 
								List<Menu008>  Info2 = Jdbctemplate.query(sql2, new Object[] { CHCODE,"M0014H" } ,new Menu008Mapper ());
											
								JsonArray js = new JsonArray();
									
										for(int i=0; i<Info2.size(); i++) {
										
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial",Info2.get(i).getMENUSL());
										job.addProperty("SERCODE",Info2.get(i).getM2CODE());
										job.addProperty("DESCRIPTION",Info2.get(i).getMENUDESC());
																		
										js.add(job);
										
									}
										
										String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? ";   
										 
										List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE,"M0014" } ,new Menu008Mapper ());
													
											
												for(int i=0; i<Info.size(); i++) {
												
							                    JsonObject job = new JsonObject();
												
												job.addProperty("Serial",Info.get(i).getMENUSL());
												job.addProperty("SERCODE",Info.get(i).getM2CODE());
												job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
												
											    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM2CODE(),Info.get(i).getMENUSL(),Info.get(i).getMENUDESC()); //Adding request in bots003 
										
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
							
					}
		
				
				if (SERCODE.equalsIgnoreCase("M0014")) {
					
					try
					{	
						String sql = "select * from menu009 where CHCODE = ? and M3CODE = ? ";    
						 
						List<Menu009>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE,menucode1 } ,new Menu009Mapper ());
									
						JsonArray js = new JsonArray();
							
								if(Info.size() !=0) {

								String Ac_desc = Info.get(0).getMENUDESC();
								
			                    JsonObject job = new JsonObject();
								
								job.addProperty("Serial",Info.get(0).getMENUSL());
								job.addProperty("SERCODE",Info.get(0).getM3CODE());
								job.addProperty("DESCRIPTION",Ac_desc);
								
							    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(0).getM3CODE(),Info.get(0).getMENUSL(),Ac_desc); //Adding request in bots003 
						
								js.add(job);
								
							}
							
								String sql1 = "select * from menu008 where CHCODE= ? and M2CODE= ?";
								 
								List<Menu008>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M00MM" } ,new Menu008Mapper ());
								
								   for (int i=0; i<Info1.size(); i++) {
									
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial","");
										job.addProperty("SERCODE",Info1.get(i).getM2CODE());
										job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
								
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
					
			}
				
											
										
			 return details;
		}
		
		
		//*****************************  Bill Payments *********************************//
	
		public JsonObject Bill_Payments(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
		{
			
			JsonObject details = new JsonObject();
			
						String menucode1 = "";  
						String invoice = "";  
						String menusl = "";
			
			
						if (SERCODE.equalsIgnoreCase("M011") && ADDINFO2.equalsIgnoreCase("Bill Payments")) {                
							menucode1 = "M0015";							  					  
						}
						else if (SERCODE.equalsIgnoreCase("M0015")) {                
							invoice = INPUT;	
							menucode1 = "M00013H";
						}
						else if (SERCODE.equalsIgnoreCase("M00013H")) {                
							invoice = INPUT;	
							menucode1 = "M00015";
						}
						else if (SERCODE.equalsIgnoreCase("M00015")) {
							menucode1 ="M000012H";
							if (INPUT.equalsIgnoreCase("1")) {
								menusl = "1";
							}else {
								menusl = "2";
							}
						}
						
				
				
				if (SERCODE.equalsIgnoreCase("M011")) {
					
					try
					{	
						
						String sql1 = "select * from menu008 where CHCODE = ? and M2CODE = ? ";   
						 
						List<Menu008>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE,"M0015H" } ,new Menu008Mapper ());
									
						JsonArray js = new JsonArray();
							
								for(int i=0; i<Info1.size(); i++) {
								
			                    JsonObject job = new JsonObject();
								
								job.addProperty("Serial",Info1.get(i).getMENUSL());
								job.addProperty("SERCODE",Info1.get(i).getM2CODE());
								job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC()); 
								
						
								js.add(job);
								
							}
						
						
						String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? ";   
						 
						List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE,menucode1 } ,new Menu008Mapper ());
																
								for(int i=0; i<Info.size(); i++) {
								
			                    JsonObject job = new JsonObject();
								
								job.addProperty("Serial",Info.get(i).getMENUSL());
								job.addProperty("SERCODE",Info.get(i).getM2CODE());
								job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC()); 
								
							    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM2CODE(),Info.get(i).getMENUSL(),Info.get(i).getMENUDESC()); //Adding request in bots003 
						
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
					
			}
				
				
				if (SERCODE.equalsIgnoreCase("M0015")) {
					
					try
					{	
						String sql = "select * from menu009 where CHCODE = ? and M3CODE = ? ";   
						 
						List<Menu009>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE,menucode1 } ,new Menu009Mapper ());
									
						JsonArray js = new JsonArray();
							
								for(int i=0; i<Info.size(); i++) {
								
			                    JsonObject job = new JsonObject();
								
								job.addProperty("Serial",Info.get(i).getMENUSL());
								job.addProperty("SERCODE",Info.get(i).getM3CODE());
								job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC()); 
								
							    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(i).getM3CODE(),Info.get(i).getMENUSL(),Info.get(i).getMENUDESC()); //Adding request in bots003 
						
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
					
			}
				
				if (SERCODE.equalsIgnoreCase("M00013H") && INPUT.equalsIgnoreCase(invoice)) {
					
					String invsql = "select * from botinvoice001 where BILL_NO = ? ";    
					 
					List<botinvoice001>  Infoinv = Jdbctemplate.query(invsql, new Object[] { invoice } ,new botinvoice001Mapper ());
					
					System.out.println(Infoinv);
					
					String sqlcount = "select count (*) from ips_account_view001 where MOBILENO = ?";
					 
					int Accno_count  = Jdbctemplate.queryForObject(sqlcount, new Object[] { MOBILENO } , Integer.class);
					
					try
					{	
						if ((Accno_count != 1) && (Accno_count != 0)) {         //More than one Acc
							
							if (ADDINFO2.equalsIgnoreCase("Enter the Invoice or Control Number")) { 
								
																		
								try
								{		
										String sql = "select * from menu008 where CHCODE= ? and M2CODE= ?";  // select the Acc no
										 
										List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, "M0017H" } ,new Menu008Mapper ());
										
										JsonArray js = new JsonArray();

										   for (int i=0; i < Info.size(); i++) {
											
							                    JsonObject job = new JsonObject();
												
												job.addProperty("Serial",Info.get(i).getMENUSL());
												job.addProperty("SERCODE",Info.get(i).getM2CODE());
												job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
										
												js.add(job);
										    }
									
																					
										String sql1 = "select * from ips_account_view001 where MOBILENO = ?";  // it shows the Acc no 
										 
										List<IPS_ACCOUNT_VIEW001>  Info1 = Jdbctemplate.query(sql1, new Object[] { MOBILENO } ,new IPS_ACCOUNT_VIEW001Mapper ());
													
										
										for (int j = 0; j < Info1.size(); j++) {
																			
						                    JsonObject job = new JsonObject();

						                    job.addProperty("Serial",j+1);
											job.addProperty("SERCODE",Info1.get(j).getMBPCODE());
											job.addProperty("DESCRIPTION",Info1.get(j).getACCNO());
											
											js.add(job);
											 
										}
										
										
										Common_Utils util = new Common_Utils();  
										
										details.addProperty("Paytype", "demo");
										details.addProperty("CHCODE", CHCODE);
										details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
										details.addProperty("TYPE", "REQ");	 //REQ or RES
										
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
							}
							
							
							
						
						}else {   // for single Acc no 
									try
									{	

										String sql = "select * from botinvoice001 where BILL_NO = ? ";    
										 
										List<botinvoice001>  Info = Jdbctemplate.query(sql, new Object[] { invoice } ,new botinvoice001Mapper ());
										
										JsonArray js = new JsonArray();
											
												if(Info.size() !=0) {

							                    JsonObject job = new JsonObject();
							                    
							                    String serial_no = "1";
							                    String sercode = "M0000H";
							                    String bill_desc = "Invoice No "+Info.get(0).getBILL_NO()+" "+"Bill Amount "+Info.get(0).getBILL_AMOUNT()+" TZS";
												
												job.addProperty("Serial",serial_no);
												job.addProperty("SERCODE",sercode);
												job.addProperty("DESCRIPTION",bill_desc);
												
											    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,SERCODE,serial_no,bill_desc); //Adding request in bots003 
										
												js.add(job);
																												
											}
											
											String sql1 = "select * from menu009 where CHCODE = ? and M3CODE = ? ";    
												 
											List<Menu009>  Info2 = Jdbctemplate.query(sql1, new Object[] { CHCODE,menucode1 } ,new Menu009Mapper ());
														
												
											  for (int i=0; i < Info2.size(); i++) {
																						
								                    JsonObject job = new JsonObject();
													
													job.addProperty("Serial",Info2.get(i).getMENUSL());
													job.addProperty("SERCODE",Info2.get(i).getM3CODE());
													job.addProperty("DESCRIPTION",Info2.get(i).getMENUDESC());
													
												    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info2.get(i).getM3CODE(),Info2.get(i).getMENUSL(),Info2.get(i).getMENUDESC()); //Adding request in bots003 

																
													js.add(job);
											    }												   												
											
											Common_Utils util = new Common_Utils();  
											
											details.addProperty("Paytype", "demo");
											details.addProperty("CHCODE", CHCODE);
											details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
											details.addProperty("TYPE", "REQ");	 //REQ or RES
											
											details.add("data", js);
											 			 
											details.addProperty("result", "success");
											details.addProperty("stscode", "HP00");
											details.addProperty("message", "Menus found !!");
											
											
									 }catch(Exception e) {
										 details.addProperty("result", "failed");
										 details.addProperty("stscode", "HP06");
										 details.addProperty("message", e.getLocalizedMessage());   	
										 
										 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
									 }
						
						}
						
					}catch(Exception e)
					 {
						 details.addProperty("result", "failed");
						 details.addProperty("stscode", "HP06");
						 details.addProperty("message", e.getLocalizedMessage());   	
						 
						 logger.debug("Exception in get_Menudata :::: "+e.getLocalizedMessage());
					 }
						
				}


				
				
				if (SERCODE.equalsIgnoreCase("M0BP") ) {
					
					try
					{	
						String sql = "select * from botinvoice001 where BILL_NO = ? ";    
						 
						List<botinvoice001>  Info = Jdbctemplate.query(sql, new Object[] { "1234567890" } ,new botinvoice001Mapper ());
						
						JsonArray js = new JsonArray();
							
								if(Info.size() !=0) {

			                    JsonObject job = new JsonObject();
			                    
			                    String serial_no = "1";
			                    String sercode = "M0000H";
			                    String bill_desc = "Invoice No "+Info.get(0).getBILL_NO()+" "+"Bill Amount "+Info.get(0).getBILL_AMOUNT()+" TZS";
								
								job.addProperty("Serial",serial_no);
								job.addProperty("SERCODE",sercode);
								job.addProperty("DESCRIPTION",bill_desc);
								
							    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,SERCODE,serial_no,bill_desc); //Adding request in bots003 
						
								js.add(job);
																								
							}
							
							String sql1 = "select * from menu009 where CHCODE = ? and M3CODE = ? ";    
								 
							List<Menu009>  Info2 = Jdbctemplate.query(sql1, new Object[] { CHCODE,"M00015" } ,new Menu009Mapper ());
										
								
							  for (int i=0; i < Info2.size(); i++) {
																		
				                    JsonObject job = new JsonObject();
									
									job.addProperty("Serial",Info2.get(i).getMENUSL());
									job.addProperty("SERCODE",Info2.get(i).getM3CODE());
									job.addProperty("DESCRIPTION",Info2.get(i).getMENUDESC());
									
								    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info2.get(i).getM3CODE(),Info2.get(i).getMENUSL(),Info2.get(i).getMENUDESC()); //Adding request in bots003 

												
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
					
			}
				
				
				
				if (SERCODE.equalsIgnoreCase("M00015")) {
					
					try
					{	
						String sql = "select * from menu010 where CHCODE = ? and M4CODE = ? and MENUSL = ?";    
						 
						List<Menu010>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE,menucode1,menusl } ,new Menu010Mapper ());
									
						JsonArray js = new JsonArray();
							
								if(Info.size() !=0) {
								
			                    JsonObject job = new JsonObject();
								
								job.addProperty("Serial",Info.get(0).getMENUSL());
								job.addProperty("SERCODE",Info.get(0).getM4CODE());
								job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
								
							    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info.get(0).getM4CODE(),Info.get(0).getMENUSL(),Info.get(0).getMENUDESC()); //Adding request in bots003 
						
								js.add(job);
								
							}
								String sql1 = "select * from menu008 where CHCODE= ? and M2CODE= ?";
								 
								List<Menu008>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M00MM" } ,new Menu008Mapper ());
								
								   for (int i=0; i<Info1.size(); i++) {
									
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial","");
										job.addProperty("SERCODE",Info1.get(i).getM2CODE());
										job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
								
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
					
			}
				
											
										
			 return details;
		}
		
		
		//*****************************   Main Menu *********************************//
		
		public JsonObject Main_Menu(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id, HttpSession session, HttpServletRequest request) 
		{
			
			
			
			JsonObject details = new JsonObject();
			

					if (SERCODE.equalsIgnoreCase("M0MM")) {
						
						try
						{								
							if(session.getId().equals(Session_Id))
							{
								session.invalidate();
							}
							
							request.getSession(true);  
								 
								//session.getServletContext().
							
							JsonArray js = new JsonArray();
							
							String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ? and status = 1";
							 
						 	List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M01H1" } ,new Menu007Mapper ());
						
						
								   if (Info1.size() != 0) {
									
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial",Info1.get(0).getMENUSL());
										job.addProperty("SERCODE",Info1.get(0).getM1CODE());
										job.addProperty("DESCRIPTION",Info1.get(0).getMENUDESC());
								
										js.add(job);
								    }
						
						   
						   String sql2 = "select * from menu007 where CHCODE= ? and M1CODE= ?";
							 
						   List<Menu007>  Info2 = Jdbctemplate.query(sql2, new Object[] { CHCODE, "M013"  } ,new Menu007Mapper ());
							
								   for (int i=0; i < Info2.size(); i++) {
																			
					                    JsonObject job = new JsonObject();
										
										job.addProperty("Serial",Info2.get(i).getMENUSL());
										job.addProperty("SERCODE",Info2.get(i).getM1CODE());
										job.addProperty("DESCRIPTION",Info2.get(i).getMENUDESC());
										
									    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info2.get(i).getM1CODE(),Info2.get(i).getMENUSL(),Info2.get(i).getMENUDESC()); //Adding Response in bots004 
								
										js.add(job);
								    }
						

								Common_Utils util = new Common_Utils();  
								
								details.addProperty("Paytype", "demo");
								details.addProperty("CHCODE", CHCODE);
								details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
								details.addProperty("TYPE", "REQ");	 //REQ or RES
								
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
								
					}
					
					
															
					
			 return details;
		}
		
		//*****************************   Main Menu1 *********************************//
		
				public JsonObject service_menu(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id, HttpSession session, HttpServletRequest request) 
				{
					
					
					
					JsonObject details = new JsonObject();
					
					String menucode1 = ""; 
					
					if(session.getId().equals(Session_Id))
					{
						session.invalidate();
					}
					
					request.getSession(true);  
					
					if (SERCODE.equalsIgnoreCase("M00MM")) {
						menucode1 = "M011";
					}

				
							if (SERCODE.equalsIgnoreCase("M00MM")) {
								
								try
								{		
									String sql = "select * from menu007 where CHCODE= ? and M1CODE= ? and status = 1";
								 
									List<Menu007>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, menucode1 } ,new Menu007Mapper ());
									
									JsonArray js = new JsonArray();
									
									   for (int i=0; i<Info.size(); i++) {
										
						                    JsonObject job = new JsonObject();
											
											job.addProperty("Serial",Info.get(i).getMENUSL());
											job.addProperty("SERCODE",Info.get(i).getM1CODE());
											job.addProperty("DESCRIPTION",Info.get(i).getMENUDESC());
									
											js.add(job);
									    }
								
									   String sql1 = "select * from menu007 where CHCODE= ? and M1CODE= ? and status = 1";
										 
										List<Menu007>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, "M0MM" } ,new Menu007Mapper ());
										
										
										   for (int i=0; i<Info1.size(); i++) {
											
							                    JsonObject job = new JsonObject();
												
												job.addProperty("Serial","");
												job.addProperty("SERCODE",Info1.get(i).getM1CODE());
												job.addProperty("DESCRIPTION",Info1.get(i).getMENUDESC());
										
												js.add(job);
										    }

										Common_Utils util = new Common_Utils();  
										
										details.addProperty("Paytype", "demo");
										details.addProperty("CHCODE", CHCODE);
										details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
										details.addProperty("TYPE", "REQ");	 //REQ or RES
										
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
										
							}
							
							
																	
							
					 return details;
				}
		
		
		
		//***************************** Self Registration *********************************//
	
		public JsonObject Self_Registration(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
	{
		
		JsonObject details = new JsonObject();
		
		      String menucode ="";
		
		     if (SERCODE.equalsIgnoreCase("M013")) {
		    	  menucode = "M0019";
				} 
		     else if (SERCODE.equalsIgnoreCase("M0019")) {
			      menucode = "M0020";
				} 
		     else if (SERCODE.equalsIgnoreCase("M0020")) {
			      menucode = "M0021";
				} 
		     else if (SERCODE.equalsIgnoreCase("M0021")) {
			      menucode = "M00016";
				}

				
		try
		{	
			     String RESNAME = "";  String RESDOB = "";
			   
				JsonArray js = new JsonArray();
				
				if (SERCODE.equalsIgnoreCase("M0021")) { 
					
					String sql1 = "select * from menu009 where CHCODE= ? and M3CODE= ?";
					 
					   List<Menu009>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE, menucode } ,new Menu009Mapper ());
					   
					   if ( Info1.size() != 0 ) {
															
		                    JsonObject job = new JsonObject();

								JsonObject dataM0019 = Get_data_bots004(MOBILENO, CHCODE, "M0019", INPUT, ADDINFO1, ADDINFO2, ADDINFO3, commdate, commtime, Session_Id);
								
								JsonArray data1 =  dataM0019.get("data").getAsJsonArray();
								
								for(int k=0; k<data1.size(); k++)
								{
									JsonObject element1 = data1.get(k).getAsJsonObject();
									
									String Name= element1.get("DESCRIPTION1").getAsString();
									System.out.println(Name);
									RESNAME  = Name;
									

								}
								

								JsonObject dataM0020 = Get_data_bots004(MOBILENO, CHCODE, "M0020", INPUT, ADDINFO1, ADDINFO2, ADDINFO3, commdate, commtime, Session_Id);
								
								JsonArray data2 =  dataM0020.get("data").getAsJsonArray();
								
								for(int k=0; k<data2.size(); k++)
								{
									JsonObject element2 = data2.get(k).getAsJsonObject();
									
									String Dob= element2.get("DESCRIPTION2").getAsString();
									System.out.println(Dob);
									RESDOB  = Dob;
									
									String des = "Dear"+RESNAME+" "+Info1.get(0).getMENUDESC();

								
								job.addProperty("Serial",Info1.get(0).getMENUSL());
								job.addProperty("SERCODE",Info1.get(0).getM3CODE());
								job.addProperty("DESCRIPTION",des);
								
							    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info1.get(0).getM3CODE(),Info1.get(0).getMENUSL(),des); //Adding request in bots003 

										
							 
							js.add(job);
					     }
				     }
					
					
				}else {
					
					String sql2 = "select * from menu008 where CHCODE= ? and M2CODE= ?";
					 
					   List<Menu008>  Info2 = Jdbctemplate.query(sql2, new Object[] { CHCODE, menucode } ,new Menu008Mapper ());
						
					   if ( Info2.size() != 0 ) {
																
		                    JsonObject job = new JsonObject();
		                    
		                    String desc = Info2.get(0).getMENUDESC()+"\n"+"Name :"+RESNAME+"\n"+"Name :"+RESDOB;
							
							job.addProperty("Serial",Info2.get(0).getMENUSL());
							job.addProperty("SERCODE",Info2.get(0).getM2CODE());
							if (Info2.get(0).getM2CODE().equalsIgnoreCase("M0021")) {
								job.addProperty("DESCRIPTION",desc);
							    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info2.get(0).getM2CODE(),Info2.get(0).getMENUSL(),desc); //Adding request in bots003 

							} else {
								job.addProperty("DESCRIPTION",Info2.get(0).getMENUDESC());
							    Insert_bot003(SUBORGCODE,CHCODE,commdate,MOBILENO,ADDINFO1,Session_Id,commtime,Info2.get(0).getM2CODE(),Info2.get(0).getMENUSL(),Info2.get(0).getMENUDESC()); //Adding request in bots003 

							}
							

						    
							js.add(job);
					    }
				}

				   
				
				Common_Utils util = new Common_Utils();  
				
				details.addProperty("Paytype", "demo");
				details.addProperty("CHCODE", CHCODE);
				details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
				details.addProperty("TYPE", "REQ");	 //REQ or RES
				
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
	
		
		//***************************** Get DATA (BOTS004) *********************************//

		public JsonObject Get_data_bots004(String MOBILENO,String CHCODE,String SERCODE,String INPUT,String ADDINFO1,String ADDINFO2,String ADDINFO3,String commdate,Timestamp commtime,String Session_Id) 
	{
		
		JsonObject details = new JsonObject();
		    
		try
		{	
			
				JsonArray js = new JsonArray();
				
					   String sql1 = "select * from bots004 where CHCODE= ? and COMMDATE= ? and MOBILENO= ? and SESSIONID= ? and MENUCD= ? ";
					 
					   List<Bots004>  Info1 = Jdbctemplate.query(sql1, new Object[] { CHCODE,commdate, MOBILENO,Session_Id,SERCODE } ,new Bots004Mapper ());
						
					   String NameRES = "";
					   String DobRES = "";

					   
					   for (int i = 0; i < Info1.size(); i++) {
																
		                    JsonObject job = new JsonObject();
		                    
		                    if (Info1.get(i).getMENUCD().equalsIgnoreCase("M0019")) {
		                    	NameRES = Info1.get(i).getRESMSG();
							}else {
								NameRES = SERCODE;
							}
		                    
		                    if (Info1.get(i).getMENUCD().equalsIgnoreCase("M0020")) {
		                    	DobRES = Info1.get(i).getRESMSG();
		                    }else {
		                    	DobRES = SERCODE;
							}
				
							job.addProperty("DESCRIPTION1",NameRES);   //Mukesh
							job.addProperty("DESCRIPTION2",DobRES);    //18-12-2000

									
							js.add(job);
					    }
							
				
				Common_Utils util = new Common_Utils();  
				
				details.addProperty("Paytype", "demo");
				details.addProperty("CHCODE", CHCODE);
				details.addProperty("Date", util.getCurrentDate("dd-MMM-yyyy"));
				details.addProperty("TYPE", "REQ");	 //REQ or RES
				
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
		
	
		//***************************** Interaction Session *********************************//
		
		public JsonObject DAY_Interaction_Session() 
		{
			
			JsonObject details = new JsonObject();
			    
			try
			{	
						Common_Utils utils = new Common_Utils();
						
				        String current_date = utils.getCurrentDate("dd-MMM-yyyy");
				        
						String sql1 = "SELECT COUNT(DISTINCT SESSIONID) FROM bots004 where COMMDATE = ?" ;
						 
						 int Count = Jdbctemplate.queryForObject(sql1,  new Object[] { current_date }, Integer.class);
						 
						 if(Count == 0)
						 {
							  System.out.println("Count is "+ Count);
						 }
						 else
						 {
							  System.out.println("Count is "+ Count);
						 }
										
						 details.addProperty("Interaction_Session", Count);		
						 details.addProperty("result", "Success");
						 details.addProperty("stscode", "HP00");
						 details.addProperty("message", "Interaction data found");
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
		
		
		
		public JsonObject Week_Interaction_Session() 
		{
			
			JsonObject details = new JsonObject();
			    
			try
			{	
				        
						String sql1 = "SELECT COMMDATE,To_char(COMMDATE,'DAY') DAYS, COUNT(DISTINCT SESSIONID)count FROM bots004 where COMMDATE between TRUNC(SYSDATE,'D') and TRUNC(SYSDATE,'D') + 6 group by COMMDATE order by COMMDATE;" ;
						  
						List<Bots004dash>  Info = Jdbctemplate.query(sql1, new Bots004dashmapper());
					
						
						
						List<String> days = new ArrayList<String>();
						
						days.add("SUNDAY");
						days.add("MONDAY");
						days.add("TUESDAY");
						days.add("WEDNESDAY");
						days.add("THURSDAY");
						days.add("FRIDAY");
						days.add("SATURDAY");
						
						List<Bots004dash>  no_exist = new ArrayList<Bots004dash>();
						
						for(int i=0; i<Info.size(); i++)
						{
							String day = Info.get(i).getDAYS();
							
							if(!days.contains(day))
							{
								Bots004dash infor = new Bots004dash();
								
								infor.setCOMMDATE("");
								infor.setDAYS(day);
								infor.setCOUNT("0");
								
								no_exist.add(infor);
							}
						}
						
						Info.addAll(no_exist);
						
						JsonArray Dates = new JsonArray();
						JsonArray Days = new JsonArray();
						JsonArray Counts = new JsonArray();
						
						for(int i=0; i<Info.size(); i++)
						{
							//JsonObject info = new JsonObject();
							
							//info.addProperty("COMMDATE", Info.get(i).getCOMMDATE());
							//info.addProperty("DAYS", Info.get(i).getDAYS());
							//info.addProperty("COUNT", Info.get(i).getCOUNT());
							
							//js.add(info);
							
							Dates.add(Info.get(i).getCOMMDATE());
							Days.add(Info.get(i).getDAYS());
							Counts.add(Info.get(i).getCOUNT());
						}
						
						
						 details.add("COMMDATE", Dates);	
						 details.add("DAYS", Days);	
						 details.add("COUNT", Counts);	
						 
						 details.addProperty("result", "Success");
						 details.addProperty("stscode", "HP00");
						 details.addProperty("message", "Interaction data found");
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//***************************** Insert_bot003 *********************************//
		
		
		// (Request) Menu Description
		public JsonObject Insert_bot003(String SUBORGCODE, String CHCODE,String COMMDATE,String MOBILENO,String COUNTRYCD,String Session_Id,Timestamp COMMTIME,String MENUCD, String selectcd, String reqmsg) 
		{
			
			JsonObject details = new JsonObject();
			
			try
			{	
				 if(reqmsg != null && reqmsg.length() > 2000)
				 {
					 reqmsg = reqmsg.substring(0, 1999);
				 }
				 
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
		
				

		//***************************** Insert_bot004 *********************************//

		
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
		   
		   
		   
		   public class Bots004Mapper implements RowMapper<Bots004> 
		    {
		    	Common_Utils util = new Common_Utils();
		    	
				public Bots004 mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					Bots004 Info = new Bots004();  

					Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));  
					Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));  
					Info.setCOMMDATE(util.ReplaceNull(rs.getString("COMMDATE")));  
					Info.setMOBILENO(util.ReplaceNull(rs.getString("MOBILENO")));  
					Info.setCOUNTRYCD(util.ReplaceNull(rs.getString("COUNTRYCD")));  
					Info.setSESSIONID(util.ReplaceNull(rs.getString("SESSIONID")));  
					Info.setCOMMTIME(util.ReplaceNull(rs.getString("COMMTIME"))); 
					Info.setMENUCD(util.ReplaceNull(rs.getString("MENUCD")));  
					Info.setSELECTCD(util.ReplaceNull(rs.getString("SELECTCD")));  
					Info.setRESMSG(util.ReplaceNull(rs.getString("RESMSG"))); 

			return Info;
				}
		     }
		   
		   public class Bots004dashmapper implements RowMapper<Bots004dash> 
		   {
		    	Common_Utils util = new Common_Utils();
		    	
				public Bots004dash mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					Bots004dash Info = new Bots004dash();  

					Info.setCOMMDATE(util.ReplaceNull(rs.getString("COMMDATE")));  
					Info.setDAYS(util.ReplaceNull(rs.getString("DAYS")));  
					Info.setCOUNT(util.ReplaceNull(rs.getString("COUNT")));  
					
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
		   
		   
		   public class Menu009Mapper implements RowMapper<Menu009> 
		    {
		    	Common_Utils util = new Common_Utils();
		    	
				public Menu009 mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					Menu009 Info = new Menu009();  
					
					try
					{
						Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));  
						Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));  
						Info.setM3CODE(util.ReplaceNull(rs.getString("M3CODE")));  
						Info.setMENUSL(util.ReplaceNull(rs.getString("MENUSL")));  
						Info.setMENUDESC(util.ReplaceNull(rs.getString("MENUDESC")));  
						Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));  
						Info.setEDATE(util.ReplaceNull(rs.getString("EDATE")));      
						Info.setEUSER(util.ReplaceNull(rs.getString("EUSER")));         
						Info.setCDATE(util.ReplaceNull(rs.getString("CDATE")));         
						Info.setCUSER(util.ReplaceNull(rs.getString("CUSER")));          
						Info.setADATE(util.ReplaceNull(rs.getString("ADATE")));
						Info.setAUSER(util.ReplaceNull(rs.getString("AUSER")));        
					}
					catch(Exception ex)
					{
						logger.debug("Exception in Menu009Mapper "+ex.getLocalizedMessage());
					}

					return Info;
				}
		     }
		   
		      
		   public class Menu010Mapper implements RowMapper<Menu010> 
		    {
		    	Common_Utils util = new Common_Utils();
		    	
				public Menu010 mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					Menu010 Info = new Menu010();  
					
					try
					{
						Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));  
						Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));  
						Info.setM4CODE(util.ReplaceNull(rs.getString("M4CODE")));  
						Info.setMENUSL(util.ReplaceNull(rs.getString("MENUSL")));  
						Info.setMENUDESC(util.ReplaceNull(rs.getString("MENUDESC")));  
						Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));  
						Info.setEDATE(util.ReplaceNull(rs.getString("EDATE")));      
						Info.setEUSER(util.ReplaceNull(rs.getString("EUSER")));         
						Info.setCDATE(util.ReplaceNull(rs.getString("CDATE")));         
						Info.setCUSER(util.ReplaceNull(rs.getString("CUSER")));          
						Info.setADATE(util.ReplaceNull(rs.getString("ADATE")));
						Info.setAUSER(util.ReplaceNull(rs.getString("AUSER")));        
					}
					catch(Exception ex)
					{
						logger.debug("Exception in Menu009Mapper "+ex.getLocalizedMessage());
					}

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
					Info.setPIN(util.ReplaceNull(rs.getString("PIN"))); 
					Info.setMCODE(util.ReplaceNull(rs.getString("MCODE")));
					Info.setMRCODE(util.ReplaceNull(rs.getString("MRCODE")));
					Info.setMTCODE(util.ReplaceNull(rs.getString("MTCODE")));
					Info.setMCBCODE(util.ReplaceNull(rs.getString("MCBCODE")));
					Info.setMASCODE(util.ReplaceNull(rs.getString("MASCODE")));
					Info.setMBPCODE(util.ReplaceNull(rs.getString("MBPCODE")));





					


			      
					return Info;
				}
		     }
		   
		    public class TRANSACTIONSMapper implements RowMapper<TRANSACTIONS> 
		    {
		    	Common_Utils util = new Common_Utils();
		    	
				public TRANSACTIONS mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					TRANSACTIONS Info = new TRANSACTIONS();  

					Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));  
					Info.setTRANTYPE(util.ReplaceNull(rs.getString("TRANTYPE")));  
					Info.setTRANCODE(util.ReplaceNull(rs.getString("TRANCODE")));  
					Info.setSYSTEMDATE(util.ReplaceNull(rs.getString("SYSTEMDATE")));  
					Info.setTRANSEQ(util.ReplaceNull(rs.getString("TRANSEQ")));  
					Info.setLEGSL(util.ReplaceNull(rs.getString("LEGSL")));  
					Info.setTRANDATE(util.ReplaceNull(rs.getString("TRANDATE"))); 
					Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));  
					Info.setCHREFNO(util.ReplaceNull(rs.getString("CHREFNO")));  
					Info.setDBCR(util.ReplaceNull(rs.getString("DBCR")));  
					Info.setAMOUNT(util.ReplaceNull(rs.getString("AMOUNT")));  
					Info.setTRANCURR(util.ReplaceNull(rs.getString("TRANCURR")));  
					Info.setSYSAMOUNT(util.ReplaceNull(rs.getString("SYSAMOUNT")));  
					Info.setSYSCURR(util.ReplaceNull(rs.getString("SYSCURR")));  
					Info.setACCOUNTNO(util.ReplaceNull(rs.getString("ACCOUNTNO"))); 
					Info.setIBAN(util.ReplaceNull(rs.getString("IBAN")));  
					Info.setSWIFTCD(util.ReplaceNull(rs.getString("SWIFTCD")));  
					Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));  
					Info.setCHARGES(util.ReplaceNull(rs.getString("CHARGES")));  
					Info.setCHGCODE(util.ReplaceNull(rs.getString("CHGCODE")));  
					Info.setREMARKS(util.ReplaceNull(rs.getString("REMARKS")));  
					Info.setEUSER(util.ReplaceNull(rs.getString("EUSER")));  
					Info.setEDATE(util.ReplaceNull(rs.getString("EDATE"))); 
					Info.setAUSER(util.ReplaceNull(rs.getString("AUSER")));  
					Info.setADATE(util.ReplaceNull(rs.getString("ADATE")));  
					Info.setCUSER(util.ReplaceNull(rs.getString("CUSER")));  
					Info.setCDATE(util.ReplaceNull(rs.getString("CDATE")));  
					Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));  
					Info.setINVOICENO(util.ReplaceNull(rs.getString("INVOICENO")));  
					Info.setSYSCODE(util.ReplaceNull(rs.getString("SYSCODE")));  
					Info.setPAYSL(util.ReplaceNull(rs.getString("PAYSL"))); 
					Info.setADDINFO1(util.ReplaceNull(rs.getString("ADDINFO1")));  
					Info.setADDINFO2(util.ReplaceNull(rs.getString("ADDINFO2")));  
					Info.setADDINFO3(util.ReplaceNull(rs.getString("ADDINFO3")));  
					Info.setADDINFO4(util.ReplaceNull(rs.getString("ADDINFO4")));  
					Info.setADDINFO5(util.ReplaceNull(rs.getString("ADDINFO5")));  

			      
					return Info;
				}
		     }
		    
		    public class bill_payments001Mapper implements RowMapper<bill_payments001> 
		    {
		    	Common_Utils util = new Common_Utils();
		    	
				public bill_payments001 mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					bill_payments001 Info = new bill_payments001();  

					Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));  
					Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));  
					Info.setBILLAMOUNT(util.ReplaceNull(rs.getString("BILLAMOUNT")));  
					Info.setTRANCURR(util.ReplaceNull(rs.getString("TRANCURR")));  
					Info.setINVOICENO(util.ReplaceNull(rs.getString("INVOICENO")));  
					Info.setEDATE(util.ReplaceNull(rs.getString("EDATE")));  
					Info.setEUSER(util.ReplaceNull(rs.getString("EUSER"))); 
					Info.setCDATE(util.ReplaceNull(rs.getString("CDATE")));  
					Info.setCDATE(util.ReplaceNull(rs.getString("CUSER")));  
					Info.setCDATE(util.ReplaceNull(rs.getString("ADATE")));  
					Info.setCDATE(util.ReplaceNull(rs.getString("AUSER")));  
					Info.setCDATE(util.ReplaceNull(rs.getString("MOBILENO")));  


			      
					return Info;
				}
		     }
	
	             
		    public class botinvoice001Mapper implements RowMapper<botinvoice001> 
		    {
		    	Common_Utils util = new Common_Utils();
		    	
				public botinvoice001 mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					botinvoice001 Info = new botinvoice001();  

					Info.setBILL_NO(util.ReplaceNull(rs.getString("BILL_NO")));  
					Info.setBILL_AMOUNT(util.ReplaceNull(rs.getString("BILL_AMOUNT")));  
					 
			      
					return Info;
				}
		     }
	
	
	
	
	
	
	
	
	
	
	

}
