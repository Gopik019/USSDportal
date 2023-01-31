package com.hdsoft.hdpay.models;

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
import com.hdsoft.hdpay.Repositories.Payment_Gateway_Store;
import com.hdsoft.hdpay.Repositories.web_service_001;
import com.hdsoft.hdpay.Repositories.web_service_002;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Configuration_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Channel_Action(Channels_Store Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select count(*) from channel001 where CHCODE=? and CHNAME=?";
			 
			 int Count = Jdbctemplate.queryForObject(sql, new Object[] { Info.getCHCODE(), Info.getCHNAME() } , Integer.class);
			 
			 if(Count == 0)
			 {
				  Add_Channel_Configuration(Info);
			 }
			 else
			 {
				  Update_Channel_Configuration(Info);
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
	}
	
	public JsonObject Add_Channel_Configuration(Channels_Store Info) 
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
	
	public JsonObject Update_Channel_Configuration(Channels_Store Info) 
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
	}
	
	public JsonObject Get_channel_Info(Channels_Store Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select * from channel001 where CHCODE=?";
			
			 List<Channels_Store> obj = Jdbctemplate.query(sql, new Object[] { Info.getCHCODE() }, new Channel_Mapper());
			
			 if(obj.size() != 0)
 			 {
				 details.add("Info", new Gson().toJsonTree(obj.get(0)));
				 
				/* details.addProperty("SUBORGCODE", obj.get(0).getSUBORGCODE());
				 details.addProperty("CHCODE", obj.get(0).getCHCODE());
				 details.addProperty("CHNAME", obj.get(0).getCHNAME());
				 details.addProperty("STATUS", obj.get(0).getSTATUS());
				 details.addProperty("BUS_STARTIME", obj.get(0).getBUS_STARTIME());
				 details.addProperty("BUS_ENDTIME", obj.get(0).getBUS_ENDTIME());
				 details.addProperty("AUTO_RECON", obj.get(0).getAUTO_RECON());
				 details.addProperty("AMTLIMITREQ", obj.get(0).getAMTLIMITREQ());
				 details.addProperty("USERID", obj.get(0).getUSERID());
				 details.addProperty("HASHPWD", obj.get(0).getHASHPWD());
				 details.addProperty("OAUTHVALREQ", obj.get(0).getOAUTHVALREQ());
				 details.addProperty("SECRETKEY", obj.get(0).getSECRETKEY()); */
				 
				 sql = "select * from channel002 where CHCODE=?";
				 
				 List<Channels_Store> Channels_obj = Jdbctemplate.query(sql, new Object[] { Info.getCHCODE() }, new Channel_002_Mapper());
				 
				 JsonArray Payment_Service = new JsonArray();
				 
				 for(int i=0;i<Channels_obj.size();i++)
				 {
					 JsonObject channel_Info = new JsonObject();
					 
					 channel_Info.addProperty("SUBORGCODE", Channels_obj.get(i).getSUBORGCODE());
					 channel_Info.addProperty("CHCODE", Channels_obj.get(i).getCHCODE());
					 channel_Info.addProperty("PAYGATECD", Channels_obj.get(i).getPAYGATEWAYCD());
					 channel_Info.addProperty("ALLOWED", Channels_obj.get(i).getPAYGATEWAYALLOWED());	
					 
					 Payment_Service.add(channel_Info);
				 }	
				 
				 details.add("Payment_Service", Payment_Service);
 			 }
			 
			 details.addProperty("Result", obj.size() != 0 ? "Success" : "Failed");
			 details.addProperty("Message", obj.size() != 0 ? "Channel Info Found" : "Channel Info Not Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Get_channel_Info :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonArray Get_ChannelCodes(String term) 
	{
		JsonArray Event_Codes = new JsonArray();
		
		try
		{
			 String sql = "select CHCODE from channel001 where CHCODE LIKE upper(?) or CHCODE LIKE lower(?)";
			
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
			 logger.debug("Exception in Get_ChannelCodes :::: "+e.getLocalizedMessage());
		 }
		
		 return Event_Codes;
	}
	
	public JsonObject Channel_Secret() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 details.addProperty("SecretKey", new Common_Utils().Generate_Random_String(64));
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "SecretKey Generated Successfully");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Channel_Action :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}

	/* ##################### Payment Gateway ##################### */
	
	public JsonObject Payment_Gateway_Action(Payment_Gateway_Store Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select count(*) from paygate001 where PAYGATECD=?";
			 
			 int Count = Jdbctemplate.queryForObject(sql, new Object[] { Info.getPAYGATECD() } , Integer.class);
			 
			 if(Count == 0)
			 {
				 details = Add_Payement_Gateway_Configuration(Info);
			 }
			 else
			 {
				 details = Update_Payement_Gateway_Configuration(Info);
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Payment_Gateway_Action :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Add_Payement_Gateway_Configuration(Payment_Gateway_Store Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into paygate001(SUBORGCODE,PAYGATECD,PAYGATE_NAME,STATUS,PROTOCOL,BUS_STARTIME,BUS_ENDTIME,FORMAT,FEE_REQ,AMTLIMITREQ,AUTO_RECON,REVERSAL,REVERSAL_CHRG) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getPAYGATECD(), Info.getPAYGATE_NAME(), Info.getSTATUS(), Info.getPROTOCOL(), 
					 Info.getBUS_STARTIME(), Info.getBUS_ENDTIME(), Info.getFORMAT(), Info.getFEE_REQ(), Info.getAMTLIMITREQ(), Info.getAUTO_RECON(), Info.getREVERSAL(), Info.getREVERSAL_CHRG() });
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Payement Gateway Added Successfully");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  
			 
			 logger.debug("Exception in Add_Payement_Gateway_Configuration :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Update_Payement_Gateway_Configuration(Payment_Gateway_Store Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Update paygate001 set SUBORGCODE=?,PAYGATE_NAME=?,STATUS=?,PROTOCOL=?,BUS_STARTIME=?,BUS_ENDTIME=?,FORMAT=?,FEE_REQ=?,AMTLIMITREQ=?,AUTO_RECON=?,REVERSAL=?,REVERSAL_CHRG=? where PAYGATECD=?";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getPAYGATE_NAME(), Info.getSTATUS(), Info.getPROTOCOL(), Info.getBUS_STARTIME(), Info.getBUS_ENDTIME(), 
					  Info.getFORMAT(), Info.getFEE_REQ(), Info.getAMTLIMITREQ(), Info.getAUTO_RECON(), Info.getREVERSAL(), Info.getREVERSAL_CHRG(), Info.getPAYGATECD() });
			 
			 details.addProperty("Result",  "Success");
			 details.addProperty("Message", "Payement Gateway Updated Successfully");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Update_Payement_Gateway_Configuration :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Get_Gateway_Info(Payment_Gateway_Store Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 List<Payment_Gateway_Store> obj = new ArrayList<Payment_Gateway_Store>();
			 
			 if(Info.getPAYGATECD().equals("All"))
			 {
				 String sql = "select * from paygate001 where STATUS=?";
					
				 obj = Jdbctemplate.query(sql, new Object[] { 1 }, new Gateway_Mapper());
			 }
			 else
			 {
				 String sql = "select * from paygate001 where PAYGATECD=?";
					
				 obj = Jdbctemplate.query(sql, new Object[] { Info.getPAYGATECD() }, new Gateway_Mapper());
			 }
			 
			 if(obj.size() != 0)
 			 {
				 details.add("Info", new Gson().toJsonTree(obj));
 			 }
			 
			 details.addProperty("Result", obj.size() != 0 ? "Success" : "Failed");
			 details.addProperty("Message", obj.size() != 0 ? "Payment Gateway Info Found" : "Payment Gateway Info Not Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Get_Gateway_Info :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Get_Gateway_Info_by_channel(String CHCODE) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select PAYGATECD from channel002 where SUBORGCODE=? and CHCODE=? and ALLOWED=?";
					
			 List<String> obj = Jdbctemplate.queryForList(sql, new Object[] { "EXIM", CHCODE, 1 }, String.class);
			 
			 if(obj.size() != 0)
 			 {
				 details.add("Payment_Gateways", new Gson().toJsonTree(obj));
 			 }
			 
			 details.addProperty("Result", obj.size() != 0 ? "Success" : "Failed");
			 details.addProperty("Message", obj.size() != 0 ? "Payment Gateway Info Found" : "Payment Gateway Info Not Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Get_Gateway_Info :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}

	public JsonArray Get_Payment_Gateway_Codes(String term) 
	{
		JsonArray Event_Codes = new JsonArray();
		
		try
		{
			 String sql = "select PAYGATECD from paygate001 where PAYGATECD LIKE upper(?) or PAYGATECD LIKE lower(?)";
			
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
			 logger.debug("Exception in Get_Payment_Gateway_Codes :::: "+e.getLocalizedMessage());
		 }
		
		 return Event_Codes;
	}
	
	/* ##################### API Configuration ##################### */
	
	public JsonObject API_Configuration_Action(web_service_001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select count(*) from webservice001 where CHCODE=? and SERVICECD=?";
			 
			 int Count = Jdbctemplate.queryForObject(sql, new Object[] { Info.getCHCODE(), Info.getSERVICECD() } , Integer.class);
			 
			 String PAYLOAD  = Info.getPAYLOAD(); 
			 
			 String SIGNPAYLOAD =  Info.getSIGNPAYLOAD(); 
			
			 PAYLOAD = PAYLOAD.replaceAll("\\n", "");
			 PAYLOAD = PAYLOAD.replaceAll("\\r", "");
			
			 SIGNPAYLOAD = SIGNPAYLOAD.replaceAll("\\n", "");
			 SIGNPAYLOAD = SIGNPAYLOAD.replaceAll("\\r", "");
			
			 //if(Info.getFORMAT().equalsIgnoreCase("JSON"))
			 //{
				 PAYLOAD = PAYLOAD.replaceAll("\\s+", "");
				 SIGNPAYLOAD = SIGNPAYLOAD.replaceAll("\\s+", "");
			//}
			  
			 Info.setPAYLOAD(PAYLOAD);
			 
			 Info.setSIGNPAYLOAD(SIGNPAYLOAD);
			 
			 if(Count == 0)
			 {
				 Add_Api_Configuration(Info);
			 }
			 else
			 {
				 Update_Api_Configuration(Info);
			 }

			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Channel Added Successfully");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed"); 
			 details.addProperty("Message", e.getLocalizedMessage());  	 
			 
			 logger.debug("Exception in API_Configuration_Action :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Add_Api_Configuration(web_service_001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 JsonObject Header_details = new JsonObject();
			
			 Header_details = Generate_Header_Id();
			 
			 if(Header_details.get("Result").getAsString().equals("Success"))
			 {
				 String Header_Id = Header_details.get("Header_Id").getAsString();
				 
				 String sql = "Insert into webservice001(SUBORGCODE,SYSCODE,CHCODE,SERVICECD,SERVNAME,FORMAT,PROTOCOL,METHOD,CHTYPE,URI,PAYLOAD,SIGNPAYLOAD,HEADERID,FLOW,JOBREQ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				   
				 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getSYSCODE(), Info.getCHCODE(), Info.getSERVICECD(), Info.getSERVNAME(), Info.getFORMAT(), 
						  Info.getPROTOCOL(), Info.getMETHOD(), Info.getCHTYPE(),Info.getURI(), Info.getPAYLOAD(), Info.getSIGNPAYLOAD(), Header_Id ,Info.getFLOW(), Info.getJOBREQ() });
						 
				 List<Object[]> batch = new ArrayList<Object[]>();
				 
				 for(int i=0;i<Info.getKeys().length;i++)
				 {
					 Object[] values = new Object[] { Info.getSUBORGCODE(), Info.getSYSCODE(), Info.getCHCODE(), Info.getSERVICECD(), Header_Id, i+1, Info.getKeys()[i], Info.getValues()[i] };
					 
					 batch.add(values);				 
				 }
				 
				 sql = "Insert into webservice002(SUBORGCODE,SYSCODE,CHCODE,SERVICECD,HEADERID,SLNO,HEADKEY,HEADVALUE) values(?,?,?,?,?,?,?,?)"; 
				 
				 Jdbctemplate.batchUpdate(sql, batch);
				 
				 details.addProperty("Result", "Success");
				 details.addProperty("Message", "API Added Successfully");
			 }
			 else
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Issue When Creating Header Id !!!");
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Add_Api_Configuration :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Update_Api_Configuration(web_service_001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select HEADERID from webservice001 where CHCODE=? and SERVICECD=?";
			 
			 List<String> HEADERID = Jdbctemplate.queryForList(sql, new Object[] { Info.getCHCODE(), Info.getSERVICECD() } , String.class);
			 
			 if(HEADERID.size() !=0) 
			 {
				 Info.setHEADERID(HEADERID.get(0));
				 
				 sql = "Update webservice001 set SUBORGCODE=?,SYSCODE=?,SERVICECD=?,SERVNAME=?,FORMAT=?,PROTOCOL=?,METHOD=?,CHTYPE=?,URI=?,PAYLOAD=?,SIGNPAYLOAD=?,HEADERID=?,FLOW=?,JOBREQ=? where CHCODE=? and SERVICECD=?";
				   
				 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getSYSCODE(), Info.getSERVICECD(), Info.getSERVNAME(), Info.getFORMAT(), Info.getPROTOCOL(), Info.getMETHOD(),
						  Info.getCHTYPE(),Info.getURI(), Info.getPAYLOAD(), Info.getSIGNPAYLOAD(), Info.getHEADERID(),Info.getFLOW(), Info.getJOBREQ(), Info.getCHCODE(), Info.getSERVICECD() });
				
				 sql = "Delete from webservice002 where CHCODE=? and SERVICECD=? and HEADERID=?";
				 
				 Jdbctemplate.update(sql, new Object[] { Info.getCHCODE(), Info.getSERVICECD(), Info.getHEADERID() });
				 
				 List<Object[]> batch = new ArrayList<Object[]>();
				 
				 for(int i=0;i<Info.getKeys().length;i++)
				 {
					 Object[] values = new Object[] { Info.getSUBORGCODE(), Info.getSYSCODE(), Info.getCHCODE(), Info.getSERVICECD(), Info.getHEADERID(), i+1, Info.getKeys()[i], Info.getValues()[i] };
					 
					 batch.add(values);				 
				 }
				 
				 sql = "Insert into webservice002(SUBORGCODE,SYSCODE,CHCODE,SERVICECD,HEADERID,SLNO,HEADKEY,HEADVALUE) values(?,?,?,?,?,?,?,?)"; 
				 
				 if(batch.size() > 0 ) Jdbctemplate.batchUpdate(sql, batch);
				 
				 details.addProperty("Result", "Success");
				 details.addProperty("Message", "API Updated Successfully");
			 }
			 else
			 {
				 details.addProperty("Result", "Failed");
				 details.addProperty("Message", "Issue when Finding Header Id !!");
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Update_Api_Configuration :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Find_API_Service(String Channel_Id, String Service_Name, String Service_Id) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select * from webservice001 where CHCODE=? and SERVNAME=? and SERVICECD=?";
			 
			 List<web_service_001> API_Info = Jdbctemplate.query(sql, new Object[] { Channel_Id, Service_Name, Service_Id }, new API_Mapper() );
			 
			 if(API_Info.size()!=0)
			 {
				 //details.add("info", new Gson().toJsonTree(API_Info.get(0)));
				 
				 details.addProperty("SUBORGCODE", API_Info.get(0).getSUBORGCODE());
				 details.addProperty("CHCODE", API_Info.get(0).getCHCODE());
				 details.addProperty("SERVICECD", API_Info.get(0).getSERVICECD());
				 details.addProperty("SERVNAME", API_Info.get(0).getSERVNAME());
				 details.addProperty("FORMAT", API_Info.get(0).getFORMAT());
				 details.addProperty("PROTOCOL", API_Info.get(0).getPROTOCOL());	 
				 details.addProperty("METHOD", API_Info.get(0).getMETHOD());
				 details.addProperty("CHTYPE", API_Info.get(0).getCHTYPE());
				 details.addProperty("URI", API_Info.get(0).getURI());
				 details.addProperty("PAYLOAD", API_Info.get(0).getPAYLOAD());
				 details.addProperty("JOBREQ", API_Info.get(0).getJOBREQ());
				 details.addProperty("SIGNPAYLOAD", API_Info.get(0).getSIGNPAYLOAD());
				 details.addProperty("HEADERID", API_Info.get(0).getHEADERID());
				 details.addProperty("FLOW", API_Info.get(0).getFLOW());
				
				 sql = "Select * from webservice002 where SERVICECD=? and CHCODE=?";
				 
				 List<web_service_002> Header_Info = Jdbctemplate.query(sql, new Object[] { API_Info.get(0).getSERVICECD(), API_Info.get(0).getCHCODE() }, new Header_Mapper() );
				 
				 JsonArray Headers = new JsonArray();
				 
				 for(int i=0;i<Header_Info.size();i++)
				 {
					JsonObject Header_details = new JsonObject();
					 
					Header_details.addProperty("Key", Header_Info.get(i).getHEADKEY());
					Header_details.addProperty("Value", Header_Info.get(i).getHEADVALUE());
					
					Headers.add(Header_details);
				 }
				
				 details.add("Headers", Headers);
			 }
			 
			 details.addProperty("Result", API_Info.size()!=0 ? "Success" : "Failed");
			 details.addProperty("Message", API_Info.size()!=0 ? "API Details Found !!" : "API Details Not Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  
			 
			 logger.debug("Exception in Find_API_Service :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonArray Get_API_Codes(String term) 
	{
		JsonArray Event_Codes = new JsonArray();
		
		try
		{
			 //String sql = "Select * from webservice001 where CHCODE=?";
			 
			// List<web_service_001> API_Info = Jdbctemplate.query(sql, new Object[] { Channel_Id }, new API_Mapper() );
			 
			 
			 String sql = "select * from webservice001 where CHCODE LIKE upper(?) or CHCODE LIKE lower(?)";
			
			 List<web_service_001> API_Info = Jdbctemplate.query(sql, new Object[] { "%"+term+"%", "%"+term+"%" }, new API_Mapper() );
			 
			// List<String> obj = Jdbctemplate.queryForList(sql, new Object[] { "%"+term+"%", "%"+term+"%" }, String.class);
		 
			 for(int i=0; i<API_Info.size();i++)
 			 {
				 String CHCODE = API_Info.get(i).getCHCODE();
				 String SERVNAME = API_Info.get(i).getSERVNAME();
				 String SERVICECD = API_Info.get(i).getSERVICECD();
				 
				 JsonObject Informations = new JsonObject();

				 Informations.addProperty("label", CHCODE + " ("+SERVNAME+ "-"+ SERVICECD+" )");
				 Informations.addProperty("id", CHCODE+"|"+SERVNAME+"|"+ SERVICECD);
				 
				 Event_Codes.add(Informations);
 			 }
			 
			 /*for(int i=0; i<obj.size();i++)
 			 {
				 JsonObject Informations = new JsonObject();

				 Informations.addProperty("label", obj.get(i));
				 
				 Event_Codes.add(Informations);
 			 } */
		 }
		 catch(Exception e)
		 {
			 logger.debug("Exception in Get_API_Codes :::: "+e.getLocalizedMessage());
		 }
		
		 return Event_Codes;
	}
	
	public JsonObject Generate_Header_Id() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 int Count = 0;
			
			 do
			 {
				 String Header_Id = new Common_Utils().Generate_Random_String(25);
				 
				 String sql = "Select count(*) from webservice001 where HEADERID=?";
				 
				 Count = Jdbctemplate.queryForObject(sql, new Object[] { Header_Id } , Integer.class);
				 
				 details.addProperty("Header_Id", Header_Id);
				 
			  }while(Count !=0);

			  details.addProperty("Result", "Success");
			  details.addProperty("Message", "Header_Id Created Successfully");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Generate_Header_Id :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	/*public JsonObject Call_API(String Service_Id)
	{
		 JsonObject details = new JsonObject();
		 
		 Service_Id = "1234";
		
		 try
		 {
			 JsonObject Api_details = Find_API_Service(Service_Id);
			 
			 String API_Type = Api_details.get("PROTOCOL").getAsString();
			 
			 if(API_Type.equals("REST"))
			 {
				 send_Rest_request(Api_details);
			 }

			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "API Added Successfully");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
		 }
		
		 return details;
	} */
	
	public JsonObject send_Rest_request(JsonObject Api_details)
	{
		JsonObject details = new JsonObject();
		
		try
		{
			String Url = Api_details.get("URI").getAsString();
			String Method = Api_details.get("METHOD").getAsString();
			String URLParameters = Api_details.get("PAYLOAD").getAsString();
			
			URL obj = new URL(Url);
			
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod(Method);
			
			JsonArray Headers = Api_details.get("Headers").getAsJsonArray();
			
			for(int i=0;i<Headers.size();i++)
			{
				JsonObject header = Headers.get(i).getAsJsonObject();
				
				String key = header.get("Key").getAsString();
				String value = header.get("Value").getAsString();
				
				con.setRequestProperty(key, value);
			}
	
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(URLParameters);
			wr.flush();
			wr.close();
	
			int responseCode = con.getResponseCode();
			
			details.addProperty("Request URL" , Url);
			details.addProperty("Method" , Method);
			details.addProperty("Parameters"  , URLParameters);
			details.addProperty("Response Code :" , responseCode);
	
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String inputLine;
			
			StringBuffer response = new StringBuffer();
	
			while((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			
			in.close();
	
			details.addProperty("Response" , response.toString());
			
			details.addProperty("Result", "Success");
			details.addProperty("Message", "API Calling Success");
		}
	    catch(Exception e)
	    {
		   details.addProperty("Result", "Failed");
		   details.addProperty("Message", e.getLocalizedMessage()); 
		   
		   logger.debug("Exception in send_Rest_request :::: "+e.getLocalizedMessage());
	    }
		
		return details;
	} 
	 
	private class API_Mapper implements RowMapper<web_service_001> 
    {
		public web_service_001 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			web_service_001 API = new web_service_001();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setCHCODE(rs.getString("CHCODE"));
			API.setSERVICECD(rs.getString("SERVICECD"));
			API.setSERVNAME(rs.getString("SERVNAME"));
			API.setFORMAT(rs.getString("FORMAT"));
			API.setPROTOCOL(rs.getString("PROTOCOL"));
			API.setMETHOD(rs.getString("METHOD"));
			API.setCHTYPE(rs.getString("CHTYPE"));
			API.setURI(rs.getString("URI"));
			API.setPAYLOAD(rs.getString("PAYLOAD"));
			API.setSIGNPAYLOAD(rs.getString("SIGNPAYLOAD"));
			API.setHEADERID(rs.getString("HEADERID"));
			API.setFLOW(rs.getString("FLOW"));
			API.setJOBREQ(rs.getString("JOBREQ"));
			
			return API;
		}
     }
    
    public class Channel_Mapper implements RowMapper<Channels_Store> 
    {
		public Channels_Store mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Channels_Store API = new Channels_Store();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setCHCODE(rs.getString("CHCODE"));
			API.setCHNAME(rs.getString("CHNAME"));
			API.setSTATUS(rs.getString("STATUS"));
			API.setBUS_STARTIME(rs.getString("BUS_STARTIME"));
			API.setBUS_ENDTIME(rs.getString("BUS_ENDTIME"));
			API.setAUTO_RECON(rs.getString("AUTO_RECON"));
			API.setAMTLIMITREQ(rs.getString("AMTLIMITREQ"));
			API.setUSERID(rs.getString("USERID"));
			API.setHASHPWD(rs.getString("HASHPWD"));
			API.setOAUTHVALREQ(rs.getString("OAUTHVALREQ"));
			API.setSECRETKEY(rs.getString("SECRETKEY"));
			
			return API;
		}
    }
    
    private class Channel_002_Mapper implements RowMapper<Channels_Store> 
    {
		public Channels_Store mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Channels_Store API = new Channels_Store();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setCHCODE(rs.getString("CHCODE"));
			API.setPAYGATEWAYCD(rs.getString("PAYGATECD"));
			API.setPAYGATEWAYALLOWED(rs.getString("ALLOWED"));
			
			return API;
		}
     }
    
    private class Gateway_Mapper implements RowMapper<Payment_Gateway_Store> 
    {
		public Payment_Gateway_Store mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Payment_Gateway_Store API = new Payment_Gateway_Store();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setPAYGATECD(rs.getString("PAYGATECD"));
			API.setPAYGATE_NAME(rs.getString("PAYGATE_NAME"));
			API.setSTATUS(rs.getString("STATUS"));
			API.setPROTOCOL(rs.getString("PROTOCOL"));
			API.setFEE_REQ(rs.getString("FEE_REQ"));
			API.setBUS_STARTIME(rs.getString("BUS_STARTIME"));
			API.setBUS_ENDTIME(rs.getString("BUS_ENDTIME"));
			API.setFORMAT(rs.getString("FORMAT"));
			API.setAMTLIMITREQ(rs.getString("AMTLIMITREQ"));
			API.setAUTO_RECON(rs.getString("AUTO_RECON"));
			API.setREVERSAL(rs.getString("REVERSAL"));
			API.setREVERSAL_CHRG(rs.getString("REVERSAL_CHRG"));
			
			return API;
		}
    }
    
    private class Header_Mapper implements RowMapper<web_service_002> 
    {
		public web_service_002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			web_service_002 API = new web_service_002();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setSYSCODE(rs.getString("SYSCODE"));
			API.setCHCODE(rs.getString("CHCODE"));
			API.setSERVICECD(rs.getString("SERVICECD"));
			API.setHEADERID(rs.getString("HEADERID"));
			API.setSLNO(rs.getString("SLNO"));
			API.setHEADKEY(rs.getString("HEADKEY"));
			API.setHEADVALUE(rs.getString("HEADVALUE"));
			
			return API;
		}
     }
}
