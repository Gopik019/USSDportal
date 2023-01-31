package com.hdsoft.hdpay.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.utils.EncryptDecrypt;
import com.hdsoft.utils.FormatUtils;
import com.hdsoft.utils.PasswordUtils;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Authorize 
{
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private String m_CallStmt = null;
	private String stmt = null;		
	String pgmid;
	String keyval;
	String userid;
	String cbd;
	String Auth_Reject_Flag ;
	String rejcode;
	String rejreason ;
	String calcurrdate;
	String entdby;
	String domainid;
	String authip="";  
	
	@Autowired
	public Adminstration ad;
	
    public JdbcTemplate jdbc;
    
    @Autowired
    public void setJdbcTemplate(HikariDataSource dataSource) {
    	this.jdbc = new JdbcTemplate(dataSource);
    }
	
	public JsonObject updateValues(JsonObject formDTO)
	{
		JsonObject resultDTO = new JsonObject();
	
		resultDTO.addProperty("sucFlg", "0");
		
		String val;	
		
		try
   	 	{
			String SqlArgs = formDTO.get("txtArgs").getAsString();
			
			String[] elements = SqlArgs.split("\\$");
			 
			if(elements.length == 9)
			{
				pgmid = elements[0];
				keyval = elements[1];
				userid = elements[2];
				
				cbd = elements[3];
				
				cbd = FormatUtils.dynaSQLDate(cbd,"DD-MM-YYYY");
				
				Auth_Reject_Flag = elements[4];
				rejcode = elements[5];
				rejreason = elements[6];
				entdby = elements[7];
				domainid = elements[8];
			}
			else
			{
				return resultDTO; 
			}
			JsonObject resultDTO1 = new JsonObject();
			
			logger.debug("PGMID :::::::: "+pgmid);
			
			if(pgmid.equals("unblockuserid"))  //EXIM|KAMAL|1|Update by ADMIN|
			{
				resultDTO1 = update_userid_unblock_temp(keyval);
				
				return resultDTO1; 
			}
			
			resultDTO.add("update", resultDTO1);
			
			
			final String authip = formDTO.get("LOG_USER_IP").getAsString(); 

			logger.debug(">>>>>>>>>>> Calling PROC_DYNAAUTH <<<<<<<<<<<<<<<");
			m_CallStmt = "call PROC_DYNAAUTH(?,?,?,?)";
			Map<String, Object> resultMap = jdbc.call(new CallableStatementCreator() {
				
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(m_CallStmt);
					
					cs.setString(1, pgmid);
					cs.setString(2, keyval);
					cs.registerOutParameter(3, Types.VARCHAR);
					cs.registerOutParameter(4, Types.NUMERIC);
					
					return cs;
				}
			}, PROC_DYNAAUTH_params());
			
			val = new Common_Utils().ReplaceNull(resultMap.get("V_RESULT"));
			
			logger.debug("Result for PROC_DYNAAUTH :::::::: "+val);
			
			if(val.equals("1"))
			{
				try
				{
					JsonObject custdto = new JsonObject();
					JsonObject rejectdto= new JsonObject();
					custdto.addProperty("sucFlg","1");
				
					if(Auth_Reject_Flag.equalsIgnoreCase("Auth"))
					{
						if(pgmid.equalsIgnoreCase("ecustuserreg"))
						{							
							custdto.addProperty("sucFlg","0");
							JsonObject object1 = new JsonObject();
							object1.addProperty("keyval",keyval);							
							custdto = UpdateRequestAlert(object1);									
						}
					}
					
					if(Auth_Reject_Flag.equalsIgnoreCase("Reject"))
					{
						rejectdto.addProperty("sucFlg", "0");
						JsonObject object2 = new JsonObject();
						object2.addProperty("keyval",keyval);
						rejectdto = InsertRejectAlert(object2);
					}
					val = custdto.get("sucFlg").getAsString();
					if(val.equalsIgnoreCase("1"))
					{
						logger.debug(">>>>>>>>>>> Calling SP_UPDATEAUTH <<<<<<<<<<<<<<<");
						
						logger.debug("pgmid ::::::::::: "+pgmid);
						logger.debug("keyval ::::::::::: "+keyval);
						logger.debug("userid ::::::::::: "+userid);
						logger.debug("cbd ::::::::::: "+cbd);
						logger.debug("Auth_Reject_Flag ::::::::::: "+Auth_Reject_Flag);
						logger.debug("rejcode ::::::::::: "+rejcode);
						logger.debug("rejreason ::::::::::: "+rejreason);
						logger.debug("authip ::::::::::: "+authip);
						
						stmt= "call SP_UPDATEAUTH(?,?,?,?,?,?,?,?,?)";
						
						Map<String, Object> resultMap1 = jdbc.call(new CallableStatementCreator() {
							
							public CallableStatement createCallableStatement(Connection con) throws SQLException {
								CallableStatement cs = con.prepareCall(stmt);
								
								cs.setString(1, pgmid);
								cs.setString(2, keyval);
								cs.setString(3, userid);
								cs.setString(4, cbd);
								cs.setString(5, Auth_Reject_Flag);
								cs.setString(6, rejcode);
								cs.setString(7, rejreason);
								cs.setString(8, authip);
								cs.registerOutParameter(9, Types.VARCHAR);
								
								return cs;
							}
						}, SP_UPDATEAUTH_params());
						
						
						logger.debug("Result for SP_UPDATEAUTH :::::::: "+resultMap1.get("P_OUT"));
						
						System.out.println("op="+resultMap1.get("P_OUT").toString());
						
						val = resultMap1.get("P_OUT").toString();
						
						if(val.contains("ORA-"))
						{
							resultDTO.addProperty("result", val);
							
							return resultDTO;
						}
						
						resultDTO.addProperty("programid",pgmid);	
						resultDTO.addProperty("AuthRej",Auth_Reject_Flag);	
						resultDTO.addProperty("result",val);
						
						String parsedArgs1[] = keyval.split("\\|");
						
						if(pgmid.equalsIgnoreCase("udompay") || pgmid.equalsIgnoreCase("sjuit")|| pgmid.equalsIgnoreCase("gepgpay")|| pgmid.equalsIgnoreCase("taxcoll"))
						{
							resultDTO.addProperty("CUSTGRP", parsedArgs1[0]);
							resultDTO.addProperty("INVOICENO",parsedArgs1[1]);
							resultDTO.addProperty("PAYSL",parsedArgs1[2]);
						}
						else 
						{
							resultDTO.addProperty("CUSTGRP", parsedArgs1[0]);
							resultDTO.addProperty("INVOICENO", parsedArgs1[1]);
						}
						
						resultDTO.addProperty("sucFlg", "1");
						
						//######### Addition code for update Password ############
						
						if(Auth_Reject_Flag.equalsIgnoreCase("Auth") && pgmid.equals("userregistration"))
						{
							String[] usr_id_info = keyval.split("\\|");
							
							logger.debug(">>>>>>>>>>> Updating Password <<<<<<<<<<<<<<<");
							
							if(usr_id_info.length == 2) 
							{
								updatepasswordAuth(usr_id_info[1]);
							}
						}						
					}
					else
					{
						System.out.println(resultDTO.get("errMsg"));
						resultDTO.addProperty("result","Error in Interface Process");
					}
				 }
				 catch(Exception e )
				 {
				    System.out.println(e);
				 }				
			 }
			logger.debug("OVER ALL METHODS :::::: "+resultDTO);
		}
	   	catch (Exception e) 
	   	{
	   		 System.out.println("Exception Occured "+e);
		}
		
   	 	  return resultDTO;
	}
	
	public JsonObject updatepasswordAuth(final String userid)
	{
		JsonObject resultDTO = new JsonObject();
	
		resultDTO.addProperty("sucFlag", "0");
		
		//EncryptDecrypt decryption = new EncryptDecrypt();
		
		logger.debug(userid);
		
		try
		{
			//Adminstration ad = new Adminstration();
			
			JsonObject details = ad.Get_users001_Info_by_user_id(userid);
			
			System.out.println("userid :::: "+userid);
			String val = null;
			
			final String hashedpwd = details.get("VERIFY").getAsString();
			
			logger.debug(">>>>>>>>>>> Calling SP_UPDPWD_GEN <<<<<<<<<<<<<<<");
			
			final String CS = "call SP_UPDPWD_GEN(?,?,?,?)";
			Map<String, Object> resultMap = jdbc.call(new CallableStatementCreator() {
				
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(CS);
					
					cs.setString(1, "EXIM");
					cs.setString(2, userid);
					cs.setString(3, hashedpwd);
					cs.registerOutParameter(4, Types.VARCHAR);
					
					return cs;
				}
			}, SP_UPDPWD_GEN_params());
			
			
			System.out.println("CS.getString(4) ::: "+resultMap.get("ERRORMSG"));
			
			logger.debug("Result for SP_UPDPWD_GEN ::::: "+resultMap.get("ERRORMSG"));
			val = resultMap.get("ERRORMSG").toString();
			if(val.equalsIgnoreCase("S"))
			{
				resultDTO.addProperty("sucFlag", "1");
				
				logger.debug(">>>>>>>>>>> Password Updated Succesfully <<<<<<<<<<<<<<<");
			}	
		}
		catch(Exception e) 
	   	{
			logger.debug("Exception Occured while calling SP_UPDPWD_GEN :::: "+e.getLocalizedMessage());
		}
		
		 return resultDTO;
	}
	
	public JsonObject UpdateRequestAlert(JsonObject formDTO)
	{
		JsonObject resultDTO = new JsonObject();
		String SqlArgs = (String) formDTO.get("keyval").getAsString();
		StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
		String domid = parsedArgs.nextToken();
		String useid = parsedArgs.nextToken();
		
		try
		{
			String sqlQuery = "SELECT COUNT(*) RECEXIST from USERS0001 WHERE TRIM(suborgcode)=? AND TRIM(USERSCD)=?";
			String sqlQuery1 = " SELECT PWD_GEN_EMAIL_NUSER FROM SYSCONF005 WHERE PWD_GEN_BY_DOMAIN_ID=? AND PWD_GEN_EFFECTIVE_DATE=(SELECT MAX(PWD_GEN_EFFECTIVE_DATE) FROM SYSCONF005 WHERE PWD_GEN_BY_DOMAIN_ID=? AND PWD_GEN_EFFECTIVE_DATE<=SYSDATE)";
			
			List<String> li = jdbc.queryForList(sqlQuery, new Object[] {domid,useid}, String.class);
			
			
			if(li.size() != 0)
			{
				JsonObject resDTO = new JsonObject();
				String val = resDTO.get("RECEXIST").getAsString();
				if(val.equals("1"))
				{	
					li = jdbc.queryForList(sqlQuery1, new Object[] {domid,domid}, String.class);
					
					
					if(li.size() != 0)
					{
						JsonObject resDTO1 = new JsonObject();
						val = resDTO1.get("PWD_GEN_EMAIL_NUSER").getAsString();
						if(val.equals("1"))
						{
							sqlQuery = "UPDATE  EVENTEXECUTOR SET PORTALLP_DEL = 0 WHERE TRIM(suborgcode) = ? AND TRIM(USER_ID) = ?";		
							li = jdbc.queryForList(sqlQuery, new Object[] {domid,useid}, String.class);
							
							
							if (li.size() > 0)
							{
								resultDTO.addProperty("sucFlg","1");				
							}
						}
						else
						{
							resultDTO.addProperty("sucFlg","0");
						}
					}
					
				}
				else 
				{
					resultDTO.addProperty("sucFlg","0");
				}		
			}
	} 
	catch (Exception e)
	{	
		resultDTO.addProperty("SucFlg","0");
		resultDTO.addProperty("errMsg",e.getLocalizedMessage());
		
		logger.debug("Exception Occured :::: "+e.getLocalizedMessage());
		
	}	
		
		return resultDTO;
	}
	
	public JsonObject InsertRejectAlert(JsonObject formDTO)
	{
		
		JsonObject resultDTO = new JsonObject();	
		int li;
		try
		{
		     String sqlstr = "INSERT INTO EVENTEXECUTOR (suborgcode,USER_ID,ALERT_GEN_DATE,ALERT_FOR_DIREC,FES_REF_NO,CONTENT,PORTALLP_DEL,FWD_ACTION,EVENT_ID,USER_TYPE)VALUES(?,?,?,?,?,?,?,?,?,?)";
			
		     li = jdbc.update(sqlstr,domainid,entdby,new java.sql.Timestamp (System.currentTimeMillis()),"F","111111111111111",rejreason,0,"R","A03","U");
		     
			
			 
			 if (li != 0)
			 { 
					resultDTO.addProperty("sucFlg", "1");
			 }
			 else
			 {
					resultDTO.addProperty("sucFlg", "0");
					resultDTO.addProperty("errMsg", "Nill");			
			 }
			
		}
		catch(Exception e)
		{
			resultDTO.addProperty("sucFlg","0") ;
			resultDTO.addProperty("errMsg",e.getLocalizedMessage()) ;
			
			logger.debug("Exception :::::"+e.getLocalizedMessage());
		}
		
		return resultDTO;		
	}
	
	public static boolean getAuthenticationResult(HttpServletRequest request)
	{
		boolean result = false;
		
		try 
		{
			String serpath = request.getRequestURL().toString();
			
			String pgmId = serpath.substring(serpath.lastIndexOf("/")+1,serpath.lastIndexOf("."));
			
			if(pgmId.endsWith("conf"))
			{
				pgmId=pgmId.replaceAll("conf","");
			}

			result = true;
		} 
		catch (Exception e) 
		{
			result = false;
		}
		
		return result;
	}
	
	public JsonArray User_Id_Unblock_Suggestions(String Search_Word, HttpServletRequest request) 
	{   	
		JsonArray User_Ids = new JsonArray();

		
		try
		{
			String query = "SELECT UPWDINV_USER_ID FROM users012 where UPWDINV_USER_ID LIKE upper(?) or UPWDINV_USER_ID LIKE lower(?)";                                                
			List<String> li = jdbc.queryForList(query,new Object[] {"%"+Search_Word+"%","%"+Search_Word+"%"},String.class);
			
			for(int i = 0 ;i<li.size();i++)
			{
				JsonObject USERSCD = new JsonObject();
				
				USERSCD.addProperty("label", li.get(i));
				
				User_Ids.add(USERSCD); 
			}	
		}
		catch(Exception ex)
		{
			logger.debug("Exception when Loading User_Id ::::: "+ex.getLocalizedMessage());
		}
		
		return User_Ids;
	}
	
	public JsonObject update_userid_unblock_temp(String keyval)
	{
		
		JsonObject resultDTO = new JsonObject();
		
		try
		{
			
			String sql = "Delete from auth001 where AUTHQ_MAIN_PK=?";
			int li = jdbc.update(sql,new Object[] {keyval});
			
			System.out.println(li);
			if(li != 0) {
				sql = "Delete from auth002 where AUTH_MAIN_PK=?";
				li = jdbc.update(sql,new Object[] {keyval});
				
				System.out.println(li);
				if(li != 0) {
					sql = "Delete from auth003 where AUTHDTL_MAIN_PK=?";
					li = jdbc.update(sql,new Object[] {keyval});
					
					System.out.println(li);
					String[] elements = keyval.split("\\|");
					
					if(elements.length == 2)
					 {
						String orgcode = elements[0];
						String user_id = elements[1];
						
						sql = "Delete from USERS012 where UPWDINV_USER_ID=? and SUBORGCODE=?";
						li = jdbc.update(sql,new Object[] {user_id,orgcode});
						System.out.println(li);
					 }
					
				}
			}
			
			
			 
			 resultDTO.addProperty("sucFlg", "1");
			 resultDTO.addProperty("result", "Success");
			 
		}
		catch(Exception e)
		{
			resultDTO.addProperty("sucFlg","0");
			
			resultDTO.addProperty("result",e.getLocalizedMessage()) ;
			
			logger.debug("Exception ::::: "+e.getLocalizedMessage());
			
			return resultDTO;
		}
		return resultDTO;		
	}
	public List<SqlParameter> PROC_DYNAAUTH_params()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("V_PROG_CODE"  		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("V_KEY"   		, Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("V_ERROR_STATUS"       , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("V_RESULT"     , Types.INTEGER));
		
		return inParamMap;
	}
	public List<SqlParameter> SP_UPDATEAUTH_params()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("P_PRGNAME"  		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("P_KEY"   		, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("P_USER"  		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("p_CBD"   		, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("P_AUTH"  		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("P_REJCODE"   		, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("P_REJREASON"  		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("P_AUTHIP"   		, Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("P_OUT"     , Types.INTEGER));
		
		return inParamMap;
	}
	public List<SqlParameter> SP_UPDPWD_GEN_params()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("CUSTID"  		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("USERID"   		, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("HASHED"       , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("ERRORMSG"     , Types.VARCHAR));
		
		return inParamMap;
	}
}
