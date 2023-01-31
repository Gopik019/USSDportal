package com.hdsoft.hdpay.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.Repositories.Simulator001;
import com.hdsoft.hdpay.Repositories.Simulator002;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Simulator_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Get_Simulator_Response(String URL, String Method, String SUBORGCODE, String CHCODE, String PAYTYPE, String MSGTYPE, String FLOW)
	{
		JsonObject details = new JsonObject();

		try 
		{
			 String SQL = "Select * from simulator001 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and MSGTYPE=? and FLOW=?";
			
			 List<Simulator001> Info = Jdbctemplate.query(SQL, new Object[] { SUBORGCODE, CHCODE, PAYTYPE, MSGTYPE, FLOW } , new Simulater001_Mapper());
			 
			 if(Info.size() !=0)
			 {
				  String SUCCESS = Info.get(0).getSUCCESS();
				  String FAILURE = Info.get(0).getFAILURE();
				  
				  details.addProperty("Request URL", URL);
				  details.addProperty("Method", Method);
				  details.addProperty("Parameters", "");
					 
				  if(SUCCESS.equals("1") && FAILURE.equals("0"))
				  {
					  String SUCCESS_CODE = Info.get(0).getSUCCESS_CODE();
					  String SUCCESS_RES = Info.get(0).getSUCCESS_RES();
					  
					  details.addProperty("Response_Code", SUCCESS_CODE);
					  details.addProperty("Response", SUCCESS_RES);
					  details.addProperty("Result", "Success");
					  details.addProperty("Message", "Response From Simulator");
				  }
				  else
				  {
					  String FAILURE_CODE = Info.get(0).getFAILURE_CODE();
					  
					  JsonObject err_details = Get_Error_details( SUBORGCODE,  CHCODE,  PAYTYPE,  FLOW,  FAILURE_CODE);
					 
					  details.addProperty("Response_Code", FAILURE_CODE);
					  details.addProperty("Response", err_details.has("RESPONSE_DATA") ? err_details.get("RESPONSE_DATA").getAsString() : "");
					  details.addProperty("Result", "Failed");
					  details.addProperty("Message", "Response From Simulator");
				  }
			 }
			 
			 logger.debug("Response from Simulator :::: "+details.get("Response").getAsString());
    	 }	
		 catch(Exception e)
    	 {
    		 details.addProperty("Result", "Failed");
    		 details.addProperty("Response_Code", "500");
    		 details.addProperty("Message", e.getLocalizedMessage());
    		
    		 logger.debug("Exception in Simulator_Modal :::: "+e.getLocalizedMessage());
    	 }
			
		 return details;
	}
	
	public JsonObject Get_Simulator_Response_with_range(String URL, String Method, String SUBORGCODE, String CHCODE, String PAYTYPE, String MSGTYPE, String FLOW, String Range)
	{
		JsonObject details = new JsonObject();

		try 
		{
			 String SQL = "Select * from simulator002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and FLOW=? and RANGE_FROM<=? and RANGE_TO>=?";
			
			 List<Simulator002> Info = Jdbctemplate.query(SQL, new Object[] { SUBORGCODE, CHCODE, PAYTYPE, FLOW, Range, Range } , new Simulater002_Mapper());
			 
			 if(Info.size() !=0)
			 {
				  details.addProperty("Request URL", URL);
				  details.addProperty("Method", Method);
				  details.addProperty("Parameters", "");
				  		  
				  if(Info.get(0).getRESTYPE().equals("SUCCESS"))
				  {
					  details.addProperty("Response_Code", Info.get(0).getRESCODE());
					  details.addProperty("Response", Info.get(0).getRESPONSE_DATA());
					  details.addProperty("Result", "Success");
					  details.addProperty("Message", "Response From Simulator");
				  }
				  else
				  {
					  details.addProperty("Response_Code", Info.get(0).getRESCODE());
					  details.addProperty("Response", Info.get(0).getRESPONSE_DATA());
					  details.addProperty("Result", "Failed");
					  details.addProperty("Message", "Response From Simulator");
				  }
			 }
			 else
			 {
				  details.addProperty("Response_Code", "500");
				  details.addProperty("Response", "");
				  details.addProperty("Result", "Failed");
				  details.addProperty("Message", "Response From Simulator");
			 }
			 
			 logger.debug("Response from Simulator :::: "+details.get("Response").getAsString());
    	 }	
		 catch(Exception e)
    	 {
    		 details.addProperty("Result", "Failed");
    		 details.addProperty("Response_Code", "500");
    		 details.addProperty("Message", e.getLocalizedMessage());
    		
    		 logger.debug("Exception in Simulator_Modal :::: "+e.getLocalizedMessage());
    	 }
			
		 return details;
	}
	
	public JsonObject Get_Error_details(String SUBORGCODE, String CHCODE, String PAYTYPE, String FLOW, String RESCODE)
	{
		JsonObject details = new JsonObject();

		try 
		{
			 String SQL = "Select * from simulator002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and FLOW=? and RESCODE=?";
			
			 List<Simulator002> Info = Jdbctemplate.query(SQL, new Object[] { SUBORGCODE, CHCODE, PAYTYPE, FLOW, RESCODE } , new Simulater002_Mapper());
			 
			 if(Info.size() !=0)
			 {
				  String ERRCODE = Info.get(0).getRESCODE();
				  String RESDESC = Info.get(0).getRESDESC();
				  String RESPONSE_DATA = Info.get(0).getRESPONSE_DATA();
				  
				  if(PAYTYPE.equals("TIPS"))
				  {
					  RESPONSE_DATA = RESPONSE_DATA.replace("~RESCODE~", ERRCODE);
					  RESPONSE_DATA = RESPONSE_DATA.replace("~RESDESC~", RESDESC);
				  }
				  
				  details.addProperty("RESPONSE_DATA", RESPONSE_DATA);
			 }
			 
			 details.addProperty("Result", Info.size() !=0 ? "Success" : "Failed");
    		 details.addProperty("Message", Info.size() !=0 ? "Info Found !!" : "Info not Found !!");
    	 }	
		 catch(Exception e)
    	 {
    		 details.addProperty("Result", "Failed");
    		 details.addProperty("Message", e.getLocalizedMessage());
    		
    		 logger.debug("Exception in Simulator_Modal :::: "+e.getLocalizedMessage());
    	 }
			
		 return details;
	}
	
	public class Simulater001_Mapper implements RowMapper<Simulator001> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Simulator001 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Simulator001 Info = new Simulator001();  

			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));
			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));
			Info.setMSGTYPE(util.ReplaceNull(rs.getString("MSGTYPE")));
			Info.setFLOW(util.ReplaceNull(rs.getString("FLOW")));
			Info.setFAILURE_CODE(util.ReplaceNull(rs.getString("FAILURE_CODE")));  
			Info.setSUCCESS_CODE(util.ReplaceNull(rs.getString("SUCCESS_CODE")));   
			Info.setSUCCESS(util.ReplaceNull(rs.getString("SUCCESS")));
			Info.setFAILURE(util.ReplaceNull(rs.getString("FAILURE")));
			Info.setSUCCESS_RES(util.ReplaceNull(rs.getString("SUCCESS_RES")));
			
			return Info;
		}
     }
	
	public class Simulater002_Mapper implements RowMapper<Simulator002> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Simulator002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Simulator002 Info = new Simulator002();  

			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));
			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));
			Info.setFLOW(util.ReplaceNull(rs.getString("FLOW")));
			Info.setRESCODE(util.ReplaceNull(rs.getString("RESCODE")));  
			Info.setRESTYPE(util.ReplaceNull(rs.getString("RESTYPE")));
			Info.setRESNAME(util.ReplaceNull(rs.getString("RESNAME")));
			Info.setRESDESC(util.ReplaceNull(rs.getString("RESDESC")));
			Info.setRESPONSE_DATA(util.ReplaceNull(rs.getString("RESPONSE_DATA")));
			
			return Info;
		}
     }
	
}
