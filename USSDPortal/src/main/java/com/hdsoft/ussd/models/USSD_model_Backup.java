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
import com.hdsoft.ussd.Repositories.Bots002;
import com.hdsoft.ussd.Repositories.Menu007;
import com.hdsoft.ussd.Repositories.Menu008;
import com.hdsoft.ussd.Repositories.USSD_Rep;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class USSD_model_Backup 
{
	public JdbcTemplate Jdbctemplate;
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	String SUBORGCODE = "EXIM";
	
	
	public JsonObject Add_Customer(USSD_Rep Info, HttpSession session) 
	{
		JsonObject details = new JsonObject();
		
		Date c_date = new Date();
		
		SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");
		
		try
		{ 
//			String sql = "";
//			if (Info.getSTATUS().equals("1")) {
//				
//				 sql = "Insert into bots001(SUBORGCODE,CHCODE,MOBILENO,COUNTRYCD,REGDATE,STATUS,EUSER,EDATE) values(?,?,?,?,?,?,?,?)";
//			} else {
//				 sql = "Insert into bots001(SUBORGCODE,CHCODE,MOBILENO,COUNTRYCD,DEREGDATE,STATUS,EUSER,EDATE) values(?,?,?,?,?,?,?,?)";
//			}
//			 

			String sql = "Select count(*) from bots001 where CHCODE = ? and mobileno = ? and countrycd = ? ";
			
			int countOfMObcode = Jdbctemplate.queryForObject(sql, new Object[] {Info.getCHCODE(),Info.getMOBILENO(),Info.getCOUNTRY_CODE()}, Integer.class);

			if (countOfMObcode == 0) {
				
				sql = "Insert into bots001(SUBORGCODE,CHCODE,MOBILENO,COUNTRYCD,REGDATE,STATUS,EUSER,EDATE) values(?,?,?,?,?,?,?,?)";

				
				 System.out.println(Info.getSUBORGCODE()+ Info.getCHCODE()+ Info.getMOBILENO()+ Info.getCOUNTRY_CODE()+ Info.getREGISTRATION_DATE()+ Info.getSTATUS());
				   
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


	public JsonObject Find_Menu_details(Bots002 Info) 
	{
		JsonObject details = new JsonObject();	
		
		
		try
		{	
			
			String sql = "select count(*) from bots001 where mobileno = ? and CHCODE= ?";         //Get Mobile no from Bots001(Customer Registration)
			
			int cntofmobileno = Jdbctemplate.queryForObject(sql, new Object[] { Info.getMOBILENO(), Info.getCHCODE()}, Integer.class);
			
			if(cntofmobileno == 0)                            //Non Registered Mobile No in customer user registration
			{
				 String CHCODE = Info.getCHCODE();
				 String M1CODE = "M002";
				 String NonRegstatic_MENUSL = "2";
				 String MB_MENUSL = "2";        //Ussd Mobile app data
				 Common_Utils util = new Common_Utils();
			     String commdate = util.getCurrentDate("dd-MMM-yyyy");
				 String mobileno = "9087369066";
				 String countrycd = "91";
				 String sessionid = "dbhb";
				 Timestamp commtime = util.get_oracle_Timestamp();
				 
				 details = get_Jsondata(CHCODE, M1CODE);
				 
				 if (MB_MENUSL.equalsIgnoreCase(NonRegstatic_MENUSL) ) {
					 
					    String sql1 = "select * from menu007 where CHCODE = ? and M1CODE= ?";
					    
						List<Menu007> info1 = Jdbctemplate.query(sql1, new Object[] {CHCODE, M1CODE,},new Menu007Mapper());
						
								if (info1.size() != 0) {
										
										String channelcd = info1.get(0).getCHCODE();
										String currentmenu = info1.get(0).getM1CODE();
										
								        Insert_Bot002(SUBORGCODE,channelcd,commdate,mobileno,countrycd,sessionid,commtime,currentmenu);  //store current menu in boot002
										
								        if(currentmenu.equals("M002") && Info.getINPUT().equals("2"))
								        {
								        	 details = OnetoOne_Jsondata(channelcd, currentmenu, "0");
								        }
								        
								      
								        
								        return details;
						        
								}
					 
				 } 
				 else {
			
				  }
				 
				 return details;

			} 
			
			
			String sql1 = "select * from bots002 where mobileno = ? and commdate = ? order by commtime desc";     
		 
			List<Bots002> info = Jdbctemplate.query(sql1, new Object[] { Info.getMOBILENO(),Info.getCOMMDATE() },new Bots002Mapper());
			
			if(info.size() == 0)                                //Registration Mobile No
			{				
				 Common_Utils util = new Common_Utils();
				 
				
				 String CHCODE = Info.getCHCODE();
				 String commdate = util.getCurrentDate("dd-MMM-yyyy");
				 String mobileno = "9087369066";
				 String countrycd = "91";
				 String sessionid = "dbhb";
				 Timestamp commtime = util.get_oracle_Timestamp();
				 String M1CODE = "M001";
			
				 details = get_Jsondata(CHCODE, M1CODE);	
				 
				 System.out.println(util.get_oracle_Timestamp());
				 
				 Insert_Bot002(SUBORGCODE,CHCODE,commdate,mobileno,countrycd,sessionid,commtime,M1CODE);
				 
				

			} 
			else    
			{
				
				details = Get_current_menu(Info);                                  
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

	
	
	
	public JsonObject Get_current_menu(Bots002 Info) 
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
	
	
	public JsonObject Insert_Bot002(String SUBORGCODE, String CHCODE,String COMMDATE,String MOBILENO,String COUNTRYCD,String SESSIONID,Timestamp COMMTIME,String CURRMENU) 
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
	
	
	public JsonObject Insert_Bot003(String SUBORGCODE, String CHCODE,String COMMDATE,String MOBILENO,String COUNTRYCD,String SESSIONID,Timestamp COMMTIME,String MENUCD, String selectcd, String reqmsg) 
	{
		
		JsonObject details = new JsonObject();
		
		try
		{	
			
		     String sql = "Insert into bots003(SUBORGCODE,CHCODE,COMMDATE,MOBILENO,COUNTRYCD,SESSIONID,COMMTIME,MENUCD,SELECTCD,REQMSG) values(?,?,?,?,?,?,?,?,?,?)";
			 
			 Jdbctemplate.update(sql, new Object[] {SUBORGCODE,CHCODE,COMMDATE,MOBILENO,COUNTRYCD,SESSIONID,COMMTIME,MENUCD,selectcd,reqmsg});
			
			 
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
	
	
	
	
	
	public JsonObject get_Jsondata(String CHCODE, String M1CODE) 
	{
		JsonObject details = new JsonObject();
		
		
		try
		{	
			
				String sql = "select * from menu007 where CHCODE = ? and M1CODE = ?";
			 
				List<Menu007>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, M1CODE } ,new Menu007Mapper ());
				
				JsonArray js = new JsonArray();
				
				for(int i=0; i<Info.size(); i++)
				{
					
					
					JsonObject job = new JsonObject();
					
					job.addProperty("Serial", Info.get(i).getMENUSL());
					job.addProperty("SERCODE", Info.get(i).getM1CODE());
					job.addProperty("DESCRIPTION", Info.get(i).getMENUDESC());

					
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

	public JsonObject OnetoOne_Jsondata(String CHCODE, String M2CODE ,String MENUSL) 
	{
		JsonObject details = new JsonObject();
		
		
		try
		{	
				//List<Menu008>  Info = new ArrayList<Menu008>();
			
				MENUSL = (Integer.parseInt(MENUSL)+1)+"";
			
				String sql = "select * from menu008 where CHCODE = ? and M2CODE = ? and MENUSL = ?";
				 
				List<Menu008>  Info = Jdbctemplate.query(sql, new Object[] { CHCODE, M2CODE , MENUSL } ,new Menu008Mapper ());
							
				JsonArray js = new JsonArray();
				
				//if (MENUSL.equalsIgnoreCase("1")|| MENUSL.equalsIgnoreCase("4")) {
				
				if (Info.size() != 0) {
					
                    JsonObject job = new JsonObject();
					
					job.addProperty("Serial",Info.get(0).getCHCODE());
					job.addProperty("SERCODE",Info.get(0).getM2CODE());
					job.addProperty("DESCRIPTION",Info.get(0).getMENUDESC());
					
					js.add(job);
					
				}
					
					
				//} else {
//
			//	}
				
				
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
	
	
	
	
	
	                   /*----------------------------Row Mapper----------------------------------------------*/
	
		   
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
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	 
	
}