package com.hdsoft.hdpay.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;  
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.common.Mail_Service;
import com.hdsoft.hdpay.Repositories.Users0001;
import com.hdsoft.hdpay.Repositories.Users006;
import com.hdsoft.hdpay.Repositories.Users007;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class ForgetPassword {
	private static final Logger logger = LogManager.getLogger(ForgetPassword.class);
	
	public JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(HikariDataSource datasource) {
		this.jdbc = new JdbcTemplate(datasource);
	}
	
	
	public JsonObject getToken(JsonObject val) {
		JsonObject js = new JsonObject();
		
		try {
			final String token = val.get("token").getAsString();
			final String type = val.get("type").getAsString();
			final String username = val.get("username").getAsString();
			final String unique = val.get("unique").getAsString();
			final String sendthrough = val.get("sendthrough").getAsString();
			
			logger.debug(token+type+sendthrough+unique+username);
			
			final String m_CallStmt = "call pack_otp.proc_otpgenerator(?,?,?,?,?,?,?,?,?,?,?)";
			
			
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Calling otp generator>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			
			
			Map<String, Object> resultMap = jdbc.call(new CallableStatementCreator() {
				
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(m_CallStmt);
					
					cs.setString(1, type);
					cs.setString(2, username);
					cs.setString(3, sendthrough);
					cs.setString(4, unique);
					cs.setString(5, token);
					cs.registerOutParameter(6, Types.VARCHAR);
					cs.registerOutParameter(7, Types.VARCHAR);
					cs.registerOutParameter(8, Types.VARCHAR);
					cs.registerOutParameter(9, Types.VARCHAR);
					cs.registerOutParameter(10, Types.VARCHAR);
					cs.registerOutParameter(11, Types.VARCHAR);
					
					return cs;
				}
			}, proc_otpgenerator_params());
			
			
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<otp generator Called>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			
			
			String O_MOBILE = new Common_Utils().ReplaceNull(resultMap.get("O_MOBILE"));
			String O_MESSAGE = new Common_Utils().ReplaceNull(resultMap.get("O_MESSAGE"));
			String O_EMAIL = new Common_Utils().ReplaceNull(resultMap.get("O_EMAIL"));
			String O_SUBJECT = new Common_Utils().ReplaceNull(resultMap.get("O_SUBJECT"));
			String O_BODY = new Common_Utils().ReplaceNull(resultMap.get("O_BODY"));
			
			logger.debug(O_MOBILE+O_MESSAGE+O_EMAIL+
					O_SUBJECT+O_BODY);
			
			
			if(resultMap.get("O_ERROR").equals("S")) {
				Mail_Service ms  = new Mail_Service();
				
				logger.debug(O_EMAIL);
				
				boolean v = ms.SendingMail(O_EMAIL, O_SUBJECT, O_BODY);
				
				logger.debug(v);
				
				if(v == true) {
					js.addProperty("Result", "Success");
					js.addProperty("Message", "Mail Has been sent !!!");
				}else {
					js.addProperty("Result", "Failed");
					js.addProperty("Message", "Mail Has not been sent !!!");
				}
				
			}
		}catch(Exception e) {
			js.addProperty("Result", "Failed");
			js.addProperty("Message", e.getMessage());
		}
		
		return js;
	}
	
	public JsonObject Validate(JsonObject val) {
		JsonObject js = new JsonObject();
		
		
		
		final String token = val.get("token").getAsString();
		final String type = val.get("type").getAsString();
		final String otp = val.get("otp").getAsString();
		final String unique = val.get("unique").getAsString();
		final String sendthrough = val.get("sendthrough").getAsString();
		
		js.addProperty("unique", unique);
		js.addProperty("token", token);
		js.addProperty("type", type);
		js.addProperty("otp", otp);
		js.addProperty("sendthrough", sendthrough);
		js.addProperty("Domainid", "STANBIC");
		
		try {
			
			final String m_CallStmt = "call pack_otp.proc_otpvalidator(?,?,?,?,?,?)";
			
			
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Calling otp generator>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			
			
			Map<String, Object> resultMap = jdbc.call(new CallableStatementCreator() {
				
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(m_CallStmt);
					
					cs.setString(1, type);
					cs.setString(2, sendthrough);
					cs.setString(3, unique);
					cs.setString(4, token);
					cs.setString(5, otp);
					cs.registerOutParameter(6, Types.VARCHAR);
					
					return cs;
				}
			}, proc_otpvalidator_params());
			
			logger.debug(resultMap);
			
			if(resultMap.get("O_RESULT").equals("S")) {
				js.addProperty("Result", "Success");
				js.addProperty("Message", "Valid OTP !!!");
			}else {
				js.addProperty("Result", "Failed");
				js.addProperty("Message", "Invalid OTP !!!");
			}
			
			/*
			 * JsonObject js2 = OTP_Count(js);
			 * 
			 * js.add("DataInsert", js2);
			 */
			
		}catch(Exception e) {
			js.addProperty("Result", "Failed");
			js.addProperty("Message", e.getLocalizedMessage());
			
			logger.debug("Exception occured in Validate >>>>>>>>>>>>>"+e.getLocalizedMessage());
		}
		
		return js;
	}
	
	public JsonObject OTP_Count(JsonObject val) {
		JsonObject js = new JsonObject();
		final DateFormat fd = new SimpleDateFormat("dd-MMM-yyyy");
		final Date cudate = new Date();	
		try {
			
			logger.debug("<<<<<<<<<<<<<getting info from 007 >>>>>>>>>>>>>");
			
			String sql = "select * from users007 where userid = ? and suborgcode = ? ";
			List<Users007> li = jdbc.query(sql, new Object[] {val.get("unique").getAsString(), val.get("Domainid").getAsString()}, new Users007_mapper());
			
			
			if(li.size() != 0) {
				
				
				logger.debug("<<<<<<<<<<<<<Value find from 007 >>>>>>>>>>>>>");
				
				
				int count = li.get(0).getOTPFAILCOUNT();
				
				int count1 = val.get("Result").getAsString().equalsIgnoreCase("Failed") ? count+1 : count;
				
				sql = "select * from users007 w where w.forgotintidate = TO_DATE(sysdate, 'dd-MM-yy') and w.userid = ? and w.suborgcode = ?";
				
				li = jdbc.query(sql, new Object[] {val.get("unique").getAsString(), val.get("Domainid").getAsString()}, new Users007_mapper());
				
				if(li.size() != 0) {
				
					logger.debug("<<<<<<<<<<<<<<<<<<<<<<< Checking Second condition >>>>>>>>>>>>>>>>>>>>>>>");
					
					sql = "update users007 set CHGTIME = ? , OTPFAILCOUNT = ? , VSTATUS = ? where userid = ? and suborgcode = ?";
					int u = jdbc.update(sql,new Object[] {new java.sql.Timestamp(new java.util.Date().getTime()),count1,val.get("Result").getAsString(),val.get("unique").getAsString(), val.get("Domainid").getAsString()});
					
					if(u != 0) {
						logger.debug("<<<<<<<<<<<<<<<<<<<<<<< Checking third condition >>>>>>>>>>>>>>>>>>>>>>>");
						
						js.addProperty("Result", "Success");
						js.addProperty("Message", "Details successfully updated !!!.");
						
						logger.debug("<<<<<<<<<<<<<Value updated to 007 >>>>>>>>>>>>>");
					}else {
						js.addProperty("Result", "Failed");
						js.addProperty("Message", "Details are not updated !!!.");
						
						logger.debug("<<<<<<<<<<<<<Value not updated to 007 >>>>>>>>>>>>>");
					}
				}else {
					logger.debug("<<<<<<<<<<<<<<<<<<<<<<< Checking Second condition Parallel >>>>>>>>>>>>>>>>>>>>>>>");
					
					int failcount = (val.get("Result").getAsString().equalsIgnoreCase("Failed"))? 1 : 0;
					
					sql = "insert into users007 values(?,?,?,?,?,?)";
					int in = jdbc.update(sql , new Object[] {val.get("Domainid").getAsString(),val.get("unique").getAsString(),fd.format(cudate),new java.sql.Timestamp(new java.util.Date().getTime()),failcount,val.get("Result").getAsString()});
					
					if(in != 0) {
						logger.debug("<<<<<<<<<<<<<<<<<<<<<<< Checking secound parallel condition >>>>>>>>>>>>>>>>>>>>>>>");
						
						js.addProperty("Result", "Success");
						js.addProperty("Message", "Details successfully inserted !!!.");
						
						logger.debug("<<<<<<<<<<<<<Value inserted to 007 >>>>>>>>>>>>>");
					}else {
						js.addProperty("Result", "Failed");
						js.addProperty("Message", "Details are not inserted !!!.");
						
						logger.debug("<<<<<<<<<<<<<Value not inserted to 007 >>>>>>>>>>>>>");
					}
				}
			}else {
				
				logger.debug("<<<<<<<<<<<<<<<<<<<<<<< Checking Parallel condition >>>>>>>>>>>>>>>>>>>>>>>");
				
				int failcount = (val.get("Result").getAsString().equalsIgnoreCase("Failed"))? 1 : 0;
				
				sql = "insert into users007(SUBORGCODE,USERPAPH_USER_ID,USERPAPH_CHANGE_DATE,USERPAPH_PREV_PASSWORD,USERPAPH_CHANGED_BY) values(?,?,?,?,?)";
				int in = jdbc.update(sql , new Object[] {val.get("Domainid").getAsString(),val.get("unique").getAsString(),fd.format(cudate),failcount,val.get("Result").getAsString()});
				
				if(in != 0) {
					logger.debug("<<<<<<<<<<<<<<<<<<<<<<< Checking secound parallel condition >>>>>>>>>>>>>>>>>>>>>>>");
					
					js.addProperty("Result", "Success");
					js.addProperty("Message", "Details successfully inserted !!!.");
					
					logger.debug("<<<<<<<<<<<<<Value inserted to 007 >>>>>>>>>>>>>");
				}else {
					js.addProperty("Result", "Failed");
					js.addProperty("Message", "Details are not inserted !!!.");
					
					logger.debug("<<<<<<<<<<<<<Value not inserted to 007 >>>>>>>>>>>>>");
				}
			}
			
		}catch(Exception e) {
			js.addProperty("Result", "Failed");
			js.addProperty("Message", e.getMessage());
			
			logger.debug("Exception occured in users007 >>>>>>>>>>>>>"+e.getLocalizedMessage());
		}
		
		return js;
	}
	
	public JsonObject prepassword(JsonObject js) {
		JsonObject js1 = new JsonObject();
		
		
		
		try {
			String sql = "select * from users0001 w where w.USERSCD = ? and w.SUBORGCODE = ?";
			List<Users0001> li = jdbc.query(sql, new Object[] {js.get("tuserid").getAsString(),js.get("torgcd").getAsString()}, new Users1_mapper());
			
			if(li.size() != 0) {
				js1.addProperty("VERIFY1", li.get(0).getVERIFY1());
				js1.addProperty("VERIFY", li.get(0).getVERIFY());
				
				js1.addProperty("Result", "Success");
				js1.addProperty("Message", "Prepasswords found !!!");
			}else {
				js1.addProperty("Result", "Failed");
				js1.addProperty("Message", "User Not Found!!!");
			}
			
		}catch(Exception e) {
			js1.addProperty("Result", "Failed");
			js1.addProperty("Message", e.getLocalizedMessage());
		}
		
		return js1;
	}
	
	public JsonObject addDetails(JsonObject js1) {
		JsonObject js = new JsonObject();
		
		final DateFormat fd = new SimpleDateFormat("dd-MMM-yyyy");
		final Date cudate = new Date();	
		
		logger.debug(js1);
		
		try {
			
			//logger.debug(js1.get("prepassword.VERIFY1").getAsString());
			//Common_Utils util = new Common_Utils();
			
			logger.debug("Get details from users006");
			
			String sql = "SELECT * FROM USERS006  WHERE SUBORGCODE = ? AND USERPPH_USER_ID = ?";
			List<Users006> li = jdbc.query(sql, new Object[] {js1.get("torgcd").getAsString(),js1.get("tuserid").getAsString()}, new Users006_mapper());
			
			JsonObject prepassword = js1.get("prepassword").getAsJsonObject();
			
			logger.debug(prepassword);
			
			if(li.size() != 0) {
				
				logger.debug("details are found from users006");
				
				sql = "UPDATE USERS006 SET USERPPH_CHANGE_DATE = ?,USERPPH_CHANGED_BY = ? ,USERPPH_PREV_PASSWORD=? WHERE SUBORGCODE = ? AND USERPPH_USER_ID = ?";
				int u = jdbc.update(sql, new Object[] {fd.format(cudate),js1.get("tuserid").getAsString(),prepassword.get("VERIFY1").getAsString(),js1.get("torgcd").getAsString(),js1.get("tuserid").getAsString()});
				
				if(u != 0) {
					logger.debug("details are update to users006");
					
					js.addProperty("Result", "Success");
					js.addProperty("Message", "Details updated !!!");
				}else {
					js.addProperty("Result", "Failed");
					js.addProperty("Message", "Details not updated !!!");
				}
			}else {
				logger.debug("Alternative process for users006");
				
				
				sql = "INSERT INTO USERS006(SUBORGCODE,USERPPH_USER_ID,USERPPH_CHANGE_DATE,USERPPH_PREV_PASSWORD,USERPPH_CHANGED_BY) VALUES(?,?,?,?,?,?)";
				int i = jdbc.update(sql, new Object[] {js1.get("torgcd").getAsString(),js1.get("tuserid").getAsString(),fd.format(cudate),prepassword.get("VERIFY1").getAsString(),js1.get("tuserid").getAsString()});
				
				if(i != 0) {
					logger.debug("details are inserted to users006");
					
					js.addProperty("Result", "Success");
					js.addProperty("Message", "Details added !!!");
				}else {
					js.addProperty("Result", "Failed");
					js.addProperty("Message", "Details not added !!!");
				}
			}
			
		}catch(Exception e) {
			js.addProperty("Result", "Failed");
			js.addProperty("Message", e.getLocalizedMessage());
		}
		
		return js;
	}
	
	public List<SqlParameter> proc_otpgenerator_params()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("I_TRANTYPE"  		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("I_USERID"   		, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("I_TRANPMODE"       , Types.VARCHAR));
		inParamMap.add(new SqlParameter("I_UNIQREF"       , Types.VARCHAR));
		inParamMap.add(new SqlParameter("I_TOKEN"       , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("O_MOBILE"     , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("O_MESSAGE"     , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("O_ERROR"     , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("O_EMAIL"     , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("O_SUBJECT"     , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("O_BODY"     , Types.VARCHAR));
		
		return inParamMap;
	}
	
	public List<SqlParameter> proc_otpvalidator_params()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("I_TRANTYPE"  		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("I_TRANPMODE"   		, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("I_UNIQREF"       , Types.VARCHAR));
		inParamMap.add(new SqlParameter("I_TOKEN"       , Types.VARCHAR));
		inParamMap.add(new SqlParameter("I_OTPVALUE"       , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("O_RESULT"     , Types.VARCHAR));
		
		return inParamMap;
	}
	
	public class Users007_mapper implements RowMapper<Users007>{

		public Users007 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Users007 u07 = new Users007();
			
			u07.setSUBORGCODE(rs.getString("SUBORGCODE"));
			u07.setUSERID(rs.getString("USERID"));
			u07.setFORGOTINTIDATE(rs.getDate("FORGOTINTIDATE"));
			u07.setCHGTIME(rs.getTimestamp("CHGTIME"));
			u07.setOTPFAILCOUNT(rs.getInt("OTPFAILCOUNT"));
			u07.setVSTATUS(rs.getString("VSTATUS"));
			
			return u07;
		}
		
	}
	
	public class Users006_mapper implements RowMapper<Users006>{

		public Users006 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Users006 us6 = new Users006();
			
			us6.setSUBORGCODE(rs.getString("SUBORGCODE"));
			us6.setUSERID(rs.getString("USERPPH_USER_ID"));
			us6.setCHGDATE(rs.getDate("USERPPH_CHANGE_DATE"));
			us6.setCHGTIME(rs.getTimestamp("USERPPH_CHANGE_DATE"));
			us6.setPREPASSWORD(rs.getString("USERPPH_PREV_PASSWORD"));
			us6.setCHANGED_BY(rs.getString("USERPPH_CHANGED_BY"));
			
			return us6;
		}
		
	}
	
	public class Users1_mapper implements RowMapper<Users0001>{

		public Users0001 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Users0001 u1 = new Users0001();
			
			u1.setDOB(rs.getString("DOB"));
			u1.setBRNHCD(rs.getString("BRANCHCD"));
			u1.setEMAILID(rs.getString("EMAILID"));
			u1.setMOBILENO(rs.getString("MOBILENO"));
			u1.setREGDATE(rs.getString("REGDATE"));
			u1.setROLECD(rs.getString("ROLECD"));
			u1.setSUBORGCODE(rs.getString("SUBORGCODE"));
			u1.setUNAME(rs.getString("UNAME"));
			u1.setUSERSCD(rs.getString("USERSCD"));
			u1.setVERIFY(rs.getString("VERIFY"));
			u1.setVERIFY1(rs.getString("VERIFY1"));
			u1.setAUSER(rs.getString("AUSER"));
			u1.setADATE(rs.getDate("ADATE"));
			u1.setCUSER(rs.getString("CUSER"));
			u1.setCDATE(rs.getDate("CDATE"));
			u1.setEUSER(rs.getString("EUSER"));
			u1.setEDATE(rs.getDate("EDATE"));
			
			return u1;
		}
		
	}
	
}
