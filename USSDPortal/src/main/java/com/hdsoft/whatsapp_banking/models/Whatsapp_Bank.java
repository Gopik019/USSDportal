package com.hdsoft.whatsapp_banking.models;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.Repositories.Channels_Store;
import com.hdsoft.whatsapp_banking.Repositories.Whatsapp_Info;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Whatsapp_Bank {
	
public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	//##################### Account Configuration ########################
	
	/*public JsonObject Account_Configuration(Whatsapp_Info Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select count(*) from channel001 where CHCODE=? and CHNAME=?";
			 
			 int Count = Jdbctemplate.queryForObject(sql, new Object[] { Info.getCHCODE(), Info.getCHNAME() } , Integer.class);
			 
			 if(Count == 0)
			 {
				  //Add_Channel_Configuration(Info);
			 }
			 else
			 {
				 // Update_Channel_Configuration(Info);
			 }

			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Channel Added Successfully");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Channel_Action :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}*/
	
	public JsonObject Add_Template_Configuration(Channels_Store Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into channel001(SUBORGCODE,CHCODE,CHNAME,STATUS,BUS_STARTIME,BUS_ENDTIME,AUTO_RECON,AMTLIMITREQ,USERID,HASHPWD,OAUTHVALREQ,SECRETKEY) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE(), Info.getCHNAME(), Info.getSTATUS(), Info.getBUS_STARTIME(), 
					 Info.getBUS_ENDTIME(),  Info.getAUTO_RECON(), Info.getAMTLIMITREQ(), Info.getUSERID(), Info.getHASHPWD(),Info.getOAUTHVALREQ(), Info.getSECRETKEY()});
			
			 for(int i=0;i<Info.getPAYGATECD().length;i++)
			 {
				 sql = "Insert into channel002(SUBORGCODE,CHCODE,PAYGATECD,ALLOWED) values(?,?,?,?)"; 
				 
				 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE(), Info.getPAYGATECD()[i], Info.getALLOWED()[i] });
			 }
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Channel Added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Add_Channel_Configuration :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonArray Get_Template_Code(String term) 
	{
		JsonArray Event_Codes = new JsonArray();
		
		try
		{
			 String sql = "select TEMPCODE from WHATSAPP_TEMPLATE where TEMPCODE LIKE upper(?) or TEMPCODE LIKE lower(?)";
			
			 List<String> obj = Jdbctemplate.queryForList(sql, new Object[] { "%"+term+"%", "%"+term+"%"}, String.class);
		 
			 for(int i=0; i<obj.size();i++)
 			 {
				 JsonObject Informations = new JsonObject();

				 Informations.addProperty("id", obj.get(i));
				 Informations.addProperty("label", obj.get(i));
				 
				 Event_Codes.add(Informations);
 			 }
		 }
		 catch(Exception e)
		 {
			 logger.debug("Exception in Get_Template_Code :::: "+e.getLocalizedMessage());
		 }
		
		 return Event_Codes;
	}
	
	/*public JsonObject Update_Template_Configuration(Channels_Store Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Update channel001 set SUBORGCODE=?,CHNAME=?,STATUS=?,BUS_STARTIME=?,BUS_ENDTIME=?,AUTO_RECON=?,AMTLIMITREQ=?,USERID=?,HASHPWD=?,OAUTHVALREQ=? where CHCODE=? and CHNAME=?";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHNAME(), Info.getSTATUS(), Info.getBUS_STARTIME(),  Info.getBUS_ENDTIME(),
					  Info.getAUTO_RECON(), Info.getAMTLIMITREQ(), Info.getUSERID(), Info.getHASHPWD(),Info.getOAUTHVALREQ(), Info.getCHCODE(), Info.getCHNAME() });
			
			 sql = "delete from channel002 where SUBORGCODE=? and CHCODE=?";
			 
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE()  });
			
			 for(int i=0;i<Info.getPAYGATECD().length;i++)
			 {
				 sql = "Insert into channel002(SUBORGCODE,CHCODE,PAYGATECD,ALLOWED) values(?,?,?,?)"; 
				 
				 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE(), Info.getPAYGATECD()[i], Info.getALLOWED()[i] });
			 }
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Channel Updated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());
			 
			 logger.debug("Exception in Update_Channel_Configuration :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}*/
	
	//##################### Customer Configuration #####################
	
	/*public JsonObject Customer_Configuration(Whatsapp_Info Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select count(*) from channel001 where CHCODE=? and CHNAME=?";
			 
			// int Count = Jdbctemplate.queryForObject(sql, new Object[] { Info.getCHCODE(), Info.getCHNAME() } , Integer.class);
			 
			// if(Count == 0)
			 {
				  //Add_Channel_Configuration(Info);
			 }
			 else
			 {
				  //Update_Channel_Configuration(Info);
			 }

			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Channel Added Successfully");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Channel_Action :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}*/
	
	//################# Template Configuration ################
	
	public JsonObject Template_Configuration(Whatsapp_Info Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
				//Common_Utils util = new Common_Utils();	
				
					//String Suborgcode = "EXIM";
					String TEMPCODE = Info.getTEMPCODE();
					String GROUPCODE = Info.getGROUPCODE();
					String TEMPLATECONT = Info.getTEMPLATECONT();
					String ATTRIBUTENAME = Info.getATTRIBUTENAME();
					String IDNAME = Info.getIDNAME();
					String VALUEMAP = Info.getVALUEMAP();
					String DEFAULTVALUE = Info.getDEFAULTVALUE();
					
					String sql = "Insert into WHATSAPP_TEMPLATE(TEMPCODE,GROUPCODE,TEMPLATECONT,ATTRIBUTENAME,IDNAME,VALUEMAP,DEFAULTVALUE) values(?,?,?,?,?,?,?)";
					   
					 Jdbctemplate.update(sql, new Object[] {TEMPCODE, GROUPCODE, TEMPLATECONT, ATTRIBUTENAME, IDNAME, VALUEMAP, DEFAULTVALUE});
					 
					 details.addProperty("Result", "Success");
					 details.addProperty("Message", "Template Configured Successfully");
				}
	
			 catch(Exception e)
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", e.getLocalizedMessage());  
				 
				 logger.debug("Exception in Add Template Value :::: "+e.getLocalizedMessage());
			 }
			
			 return details;
				
		}
	
	public JsonArray Get_Group_Code(String term) 
	{
		JsonArray Event_Codes = new JsonArray();
		
		try
		{
			 String sql = "select GROUPCODE from WHATSAPP_TEMPLATE where GROUPCODE LIKE upper(?) or TEMPCODE LIKE lower(?)";
			
			 List<String> obj = Jdbctemplate.queryForList(sql, new Object[] { "%"+term+"%", "%"+term+"%"}, String.class);
		 
			 for(int i=0; i<obj.size();i++)
 			 {
				 JsonObject Informations = new JsonObject();

				 Informations.addProperty("id", obj.get(i));
				 Informations.addProperty("label", obj.get(i));
				 
				 Event_Codes.add(Informations);
 			 }
		 }
		 catch(Exception e)
		 {
			 logger.debug("Exception in Get_Group_Code :::: "+e.getLocalizedMessage());
		 }
		
		 return Event_Codes;
	}
	
	public JsonArray Get_Group_Type(String term) 
	{
		JsonArray Event_Codes = new JsonArray();
		
		try
		{
			 String sql = "select GROUPTYPE from WHATSAPP_GROUP where GROUPTYPE LIKE upper(?) or TEMPCODE LIKE lower(?)";
			
			 List<String> obj = Jdbctemplate.queryForList(sql, new Object[] { "%"+term+"%", "%"+term+"%"}, String.class);
		 
			 for(int i=0; i<obj.size();i++)
 			 {
				 JsonObject Informations = new JsonObject();

				 Informations.addProperty("label", obj.get(i));
				 
				 Event_Codes.add(Informations);
 			 }
		 }
		 catch(Exception e)
		 {
			 logger.debug("Exception in Get_Group_Code :::: "+e.getLocalizedMessage());
		 }
		
		 return Event_Codes;
	}
	
	public JsonObject Find_Template(Whatsapp_Info Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select * from WHATSAPP_TEMPLATE where TEMPCODE=?";
			 
			 List<Whatsapp_Info> info = Jdbctemplate.query(sql, new Object[] { Info.getTEMPCODE()},  new Whatsapp_Template_Find());
			 
			 if(info.size() !=0)
			 {
				 details.add("Informations", new Gson().toJsonTree(info.get(0)));
			 }
			 
			 System.out.println(details);
			 
			 details.addProperty("Result", info.size()!=0 ? "Success" : "Failed");
			 details.addProperty("Message", info.size()!=0 ? "Template Details Found !!" : "Template Details Not Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  
			 
			 logger.debug("Exception in Find_API_Service :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	//################## Service Code Configuration ########################
	
	public JsonObject Service_Configuration(Whatsapp_Info Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
				//Common_Utils util = new Common_Utils();	
				
					//String Suborgcode = "EXIM";
					String SERVICECODE = Info.getSERVICECODE();
					String SERVICENAME = Info.getSERVICENAME();
					String TEMPLATECONT = Info.getTEMPLATECONT();
					String AMOUNTLIMIT = Info.getAMOUNTLIMIT();
					String FROMTIME = Info.getFROMTIME();
					String TOTIME = Info.getTOTIME();
					//String VALUEMAP = Info.getVALUEMAP();
					//String DEFAULTVALUE = Info.getDEFAULTVALUE();
					
					String sql = "Insert into WHATSAPP_SERVICE(SERVICECODE,SERVICENAME,TEMPLATECONT,AMOUNTLIMIT,FROMTIME,TOTIME) values(?,?,?,?,?,?)";
					   
					 Jdbctemplate.update(sql, new Object[] {SERVICECODE, SERVICENAME, TEMPLATECONT, AMOUNTLIMIT, FROMTIME,TOTIME});
					 
					 details.addProperty("Result", "Success");
					 details.addProperty("Message", "Service Configured Successfully");
				}
	
			 catch(Exception e)
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", e.getLocalizedMessage());  
				 
				 logger.debug("Exception in Add Service Value :::: "+e.getLocalizedMessage());
			 }
			
			 return details;
				
		}
	
	public JsonArray Get_Service_Code(String term) 
	{
		JsonArray Event_Codes = new JsonArray();
		
		try
		{
			 String sql = "select SERVICECODE from WHATSAPP_SERVICE where SERVICECODE LIKE upper(?) or TEMPCODE LIKE lower(?)";
			
			 List<String> obj = Jdbctemplate.queryForList(sql, new Object[] { "%"+term+"%", "%"+term+"%"}, String.class);
		 
			 for(int i=0; i<obj.size();i++)
 			 {
				 JsonObject Informations = new JsonObject();

				 Informations.addProperty("label", obj.get(i));
				 
				 Event_Codes.add(Informations);
 			 }
		 }
		 catch(Exception e)
		 {
			 logger.debug("Exception in Get_Service_Code :::: "+e.getLocalizedMessage());
		 }
		
		 return Event_Codes;
	}
	
	// ################# Group Code Configuration ##############################
	
	public JsonObject Group_Configuration(Whatsapp_Info Info)
	{
		 JsonObject details = new JsonObject();
		
		 try
		 {   
			 
			 String sql = "select CUSTOMERNO,MOBILENO,STATUS from WHATSAPP_GROUP where GROUPCODE=? and GROUPTYPE=?";
			 
			 List<Whatsapp_Info> info = Jdbctemplate.query(sql, new Object[] { Info.getGROUPCODE(), Info.getGROUPTYPE()},  new Whatsapp_Group_Creation());
		
			// if()
			 details.add("Informations", new Gson().toJsonTree(info));
			 
			 details.addProperty("result", info.size() !=0 ? "success" : "failed");
			 details.addProperty("stscode", info.size() !=0 ? "200" : "400");
			 details.addProperty("message", info.size() !=0 ? "Information found" : "Information not found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("result", "failed");
			 details.addProperty("stscode", "500");
			 details.addProperty("message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Whatsapp Banking Group Status :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	// ################# Marketing Compaign Configuration #################
	
	// ############## Marketing Configuration #####################
	
	// ############# Account Suggestions ##################
	
	// #################### Customer No Suggestions #########################
	
	// #################### Service Code Sugesstions #######################
	
	//################## Group Code Suggestions #####################
	
	// ######################## Template Code Suggestions ########################
	
	public class Whatsapp_Group_Creation implements RowMapper<Whatsapp_Info> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Whatsapp_Info mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Whatsapp_Info Info = new Whatsapp_Info();  

			//Info.setGROUPCODE(util.ReplaceNull(rs.getString("GROUPCODE")));
			//Info.setGROUPTYPE(util.ReplaceNull(rs.getString("GROUPTYPE")));
			Info.setCUSTOMERNO(util.ReplaceNull(rs.getString("CUSTOMERNO")));
			Info.setMOBILENO(util.ReplaceNull(rs.getString("MOBILENO")));
			Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));  
			//Info.setGROUPDESC(util.ReplaceNull(rs.getString("GROUPDESC")));
			
			return Info;
		}
		
    }
		public class Whatsapp_Template_Find implements RowMapper<Whatsapp_Info> 
	    {
	    	Common_Utils util = new Common_Utils();
	    	
			public Whatsapp_Info mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Whatsapp_Info Info = new Whatsapp_Info();  

				Info.setTEMPCODE(util.ReplaceNull(rs.getString("TEMPCODE")));
				Info.setGROUPCODE(util.ReplaceNull(rs.getString("GROUPCODE")));
				Info.setTEMPLATECONT(util.ReplaceNull(rs.getString("TEMPLATECONT")));
				Info.setATTRIBUTENAME(util.ReplaceNull(rs.getString("ATTRIBUTENAME")));
				Info.setIDNAME(util.ReplaceNull(rs.getString("IDNAME")));
				Info.setVALUEMAP(util.ReplaceNull(rs.getString("VALUEMAP")));  
				Info.setDEFAULTVALUE(util.ReplaceNull(rs.getString("DEFAULTVALUE")));
				
				return Info;
			}
     }
	    
    }


