package com.hdsoft.utils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Operlog2 
{
	public JdbcTemplate Jdbctemplate;

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}

	public JsonObject updatePageEntry(JsonObject Info) 
	{
		JsonObject details = new JsonObject();
		 
		String domainid = Info.get("sesDomainID").getAsString() ;
		String userid = Info.get("sesUserId").getAsString();
		String ipaddress = Info.get("sessionIP").getAsString();
		String useragent = Info.get("sessionUserAgent").getAsString();
		String roleid = Info.get("sesRole").getAsString();		
		String userLoginTime = Info.get("sessionUserLoginTime").getAsString();		
		String Programid = Info.get("PROGRAMID").getAsString();
	
		long opersl = 0;

		try 
		{
			if(!domainid.equals("") && !userid.equals("") && !ipaddress.equals("") && !roleid.equals("") && !Programid.equals("")) 
			{			
				opersl = getNextOperSL(domainid, userid, Programid, userLoginTime);
					
				Timestamp ts = getTimestamp(userLoginTime);
				
				String sql = "INSERT INTO log002(SUBORGCODE, USER_ID, FORM_NAME, LOGIN_DATETIME, OPR_SL, IN_TIME, OUT_TIME, ROLE_CODE, USER_AGENT, IP_ADDRESS) VALUES(?,?,?,?,?,?,?,?,?,?)";
				
				Jdbctemplate.update(sql, new Object[] { domainid, userid , Programid, ts, opersl, new java.sql.Timestamp(new java.util.Date().getTime()), null, roleid, useragent, ipaddress});				
			}
			
			details.addProperty("Opersl", String.valueOf(opersl));
			details.addProperty("sucFlg", "1");
			
			details.addProperty("Result",  "Success");
			details.addProperty("Message", "Updated Successfully !!"); 
		} 
		catch (Exception e)
		{
			 details.addProperty("sucFlg", "0");
			
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("updatePageEntry::" + e.getLocalizedMessage());
		} 
		
		return details;
	}

	public JsonObject updatePageExit(JsonObject input) 
	{
		JsonObject details = new JsonObject();
		
		String domainid = input.get("sesDomainID").getAsString();
		String userid = input.get("sesUserId").getAsString();		
		String userLoginTime = input.get("sessionUserLoginTime").getAsString();
		String Programid = input.get("PROGRAMID").getAsString();
		String operlogsl = input.get("OPERLOGSL").getAsString();
		
		try 
		{
			if(!domainid.equals("") && !userid.equals("") && !operlogsl.equals("") && !Programid.equals("")) 
			{	
				Timestamp ts = getTimestamp(userLoginTime);
				
				String sql = "UPDATE log002 SET  OUT_TIME=? WHERE SUBORGCODE=? AND  USER_ID=? AND  FORM_NAME=? AND  LOGIN_DATETIME=? AND  OPR_SL=? ";
				
				Jdbctemplate.update(sql, new Object[] { new java.sql.Timestamp(new java.util.Date().getTime()), domainid, userid, Programid, ts, operlogsl } );   
			}
			
			details.addProperty("sucFlg", "1");
		    details.addProperty("Result",  "Success");
		    details.addProperty("Message", "Updated Successfully !!"); 
		} 
		catch (Exception e) 
		{	
			details.addProperty("sucFlg", "0");
			details.addProperty("Result",  "Failed");
			details.addProperty("Message", e.getLocalizedMessage()); 
			
			logger.debug("updatePageExit::" + e.getLocalizedMessage());
		}
		
		return details;
	}

	public long getNextOperSL(String domainId, String userId,String programid, String logdatetime) throws SQLException 
	{
		long opersl = 0;
		
		try 
		{
			String Sql = "SELECT NVL(MAX( OPR_SL),0)+1 FROM log002 WHERE SUBORGCODE=? AND  USER_ID=? AND  FORM_NAME=? AND TO_CHAR( LOGIN_DATETIME,'DD/Mon/YYYY hh:mi:ss AM')=?";
			
			opersl = Jdbctemplate.queryForObject(Sql, new Object[] { domainId, userId, programid, logdatetime } , long.class);		
		} 
		catch(Exception e) 
		{
			logger.debug("getSessionInfo()::"+e.getLocalizedMessage()); 
		} 
		
		return opersl;
	}
	
	public Timestamp getTimestamp(String userLoginTime) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss a");		
		
		java.util.Date date = sdf.parse(userLoginTime);	
		
		Timestamp ts = new Timestamp(date.getTime());
		
		return ts;
	}

}