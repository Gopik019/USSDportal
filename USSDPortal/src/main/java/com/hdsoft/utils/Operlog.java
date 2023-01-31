package com.hdsoft.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.zaxxer.hikari.HikariDataSource;

public class Operlog 
{
	private ApplicationLogger logger = null;

	public Operlog()
	{
		logger = ApplicationLogger.getInstance(this.getClass().getSimpleName());
		logger.logDebug("Init");
	}
	
	public JsonObject updatePageEntry(HashMap<String, String> input) 
	{
		JsonObject details = new JsonObject();
		 
		logger.logDebug("updatePageEntry");
		PreparedQueryManager manager = new PreparedQueryManager();
		PreparedStatement stmt = null;
		String query = Constants.EMPTY_STRING;

		String domainid = input.get("sesDomainID") ;
		String userid = input.get("sesUserId");
		String ipaddress = input.get("sessionIP");
		String useragent = input.get("sessionUserAgent");
		String roleid = input.get("sesRole");		
		String userLoginTime = input.get("sessionUserLoginTime");		
		String Programid = input.get("PROGRAMID");
	
		long opersl = 0;

		try 
		{
			if (!domainid.equals("") && !userid.equals("")
					&& !ipaddress.equals("") && !roleid.equals("")
					&& !Programid.equals("")) {			
				opersl = getNextOperSL(domainid, userid, Programid, userLoginTime);
				query = "INSERT INTO log002(SUBORGCODE, USER_ID, FORM_NAME, LOGIN_DATETIME, OPR_SL, IN_TIME, OUT_TIME, ROLE_CODE, USER_AGENT, IP_ADDRESS) VALUES(?,?,?,?,?,?,?,?,?,?)";
				manager.openConnection();
				manager.prepareStatement(query);
				stmt = manager.getStatement();
				stmt.setString(1, domainid);
				stmt.setString(2, userid);
				stmt.setString(3, Programid);				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss a");					
				java.util.Date date = sdf.parse(userLoginTime);		
				Timestamp ts = new Timestamp(date.getTime());
				stmt.setTimestamp(4,ts);
				stmt.setLong(5, opersl);
				stmt.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				stmt.setString(7, null);
				stmt.setString(8, roleid);
				stmt.setString(9, useragent);
				stmt.setString(10, ipaddress);
				stmt.execute();
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			}
			
			details.addProperty("Opersl", String.valueOf(opersl));
			details.addProperty("sucFlg", "1");
			
			 details.addProperty("Result",  "Success");
			
		} 
		catch (Exception e)
		{
			logger.logDebug("updatePageEntry::" + e.getLocalizedMessage());
			
			details.addProperty("sucFlg", "0");
			
			 details.addProperty("Result",  "Failed");
		} 
		finally
		{
			if (stmt != null)
			{
				try 
				{
					stmt.close();
				}
				catch (SQLException e)
				{
					logger.logDebug("updatePageEntry::"
							+ e.getLocalizedMessage());
				}
				
				stmt = null;
			}
			manager.closeConnection();
		}
		return details;

	}

	public JsonObject updatePageExit(HashMap<String, String> input) 
	{
		JsonObject details = new JsonObject();
		
		logger.logDebug("updatePageExit");
		PreparedQueryManager manager = new PreparedQueryManager();
		//Transpoter resultDTO = new Transpoter();
		PreparedStatement stmt = null;
		String query = "";
	
		String domainid = input.get("sesDomainID");
		String userid = input.get("sesUserId");		
		String userLoginTime = input.get("sessionUserLoginTime");
		String Programid = input.get("PROGRAMID");
		String operlogsl = input.get("OPERLOGSL");
		
		try {
			if (!domainid.equals("") && !userid.equals("")
					&& !operlogsl.equals("") && !Programid.equals("")) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss a");					
				java.util.Date date = sdf.parse(userLoginTime);		
				Timestamp ts = new Timestamp(date.getTime());
				query = "UPDATE log002 SET  OUT_TIME=? WHERE SUBORGCODE=? AND  USER_ID=? AND  FORM_NAME=? AND  LOGIN_DATETIME=? AND  OPR_SL=? ";
				manager.openConnection();
				manager.prepareStatement(query);
				stmt = manager.getStatement();
				stmt.setTimestamp(1, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				stmt.setString(2, domainid);
				stmt.setString(3, userid);
				stmt.setString(4, Programid);
				stmt.setTimestamp(5, ts);				
				stmt.setString(6, operlogsl);
				stmt.execute();
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			}
			
			details.addProperty("sucFlg", "1");
			details.addProperty("Result",  "Success");
		} 
		catch (Exception e) 
		{
			logger.logDebug("updatePageExit::" + e.getLocalizedMessage());
			details.addProperty("sucFlg", "0");
		
			details.addProperty("Result",  "Failed");
		}
		finally 
		{
			if (stmt != null) 
			{
				try 
				{
					stmt.close();
				} 
				catch (SQLException e) 
				{
					logger.logDebug("updatePageExit::"
							+ e.getLocalizedMessage());
				}
				stmt = null;
			}
			manager.closeConnection();
		}
		return details;
	}

	public static long getNextOperSL(String domainId, String userId,String programid, String logdatetime) throws SQLException 
	{
		long opersl = 0;
		PreparedStatement _pstmt = null;
		ResultSet rs = null;
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			pqm = null;
		}
		try {
			String sqlStr = "SELECT NVL(MAX( OPR_SL),0)+1 FROM log002 WHERE SUBORGCODE=? AND  USER_ID=? AND  FORM_NAME=? AND TO_CHAR( LOGIN_DATETIME,'DD/Mon/YYYY hh:mi:ss AM')=?";
			if (pqm.prepareStatement(sqlStr)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, domainId);
				_pstmt.setString(2, userId);
				_pstmt.setString(3, programid);
				_pstmt.setString(4, logdatetime);
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						opersl = rs.getLong(1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return opersl;
		} finally {
			pqm.closeConnection();
			rs.close();
			rs = null;
			_pstmt.close();
			_pstmt = null;
		}
		return opersl;
	}

}