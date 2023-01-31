package com.hdsoft.hdpay.models;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.utils.EncryptDecrypt;
import com.hdsoft.utils.FormatUtils;
import com.hdsoft.utils.PasswordUtils;
import com.zaxxer.hikari.HikariDataSource;
import com.hdsoft.common.Accessablity;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.Repositories.Dynamic_Menu;
import com.hdsoft.hdpay.Repositories.MENU_004;
import com.hdsoft.hdpay.Repositories.Menu_Items;
import com.hdsoft.hdpay.Repositories.login_Info;

@Component
public class Login_Model implements Accessablity
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	public static String webServerIntranetIP = "";
	public static String webServerPublicIP = "";
	public static String DomainName = "";
	public static int intraIPCount;
	public static int proxyIPCount;
	public static String[] intraIPArr;
	public static String[] proxyIPArr;
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject updateValues(JsonObject Inputs, String randomSalt) 
	{
		JsonObject details = new JsonObject();
		
		details.addProperty("sucFlg", "0");
		
		try 
		{
			EncryptDecrypt decryption = new EncryptDecrypt();
			 
			final String custid = Inputs.get("txtDomainId").getAsString();
			final String userid = Inputs.get("txtUserId").getAsString();
			String pwd = Inputs.get("hashedPassword").getAsString();
			
			pwd = pwd.trim();
			
			final String INTFLAG = Inputs.get("INTFLAG").getAsString();
	
			//pwd = "cba360c71900b611ef8667f549d2b5344f86db6a91877d81fdba0e5 d221d4edb";
			
			logger.debug("before pwd---------------->" + pwd);
			
			pwd = decryption.doDecrypt(pwd, randomSalt);
			
			logger.debug("Random salt -------->" + randomSalt);
			
			logger.debug("pwd---------------->" + pwd);
			
			String final_salt = PasswordUtils.getSalt(userid);
		
			logger.debug("final_salt---------------->" + final_salt);
			
			if(final_salt == null) final_salt = "";
			
			final_salt = "";
			
			final String hashedpwd = PasswordUtils.getEncrypted(pwd, userid, final_salt);
			
			//final String hashedpwd  = final_salt;
			
			logger.debug("hashedpwd---------------->" + hashedpwd);

			Common_Utils util = new Common_Utils();
			
			if(pwd.equals("") || userid.equals("") || custid.equals("")) 
			{
				details.addProperty("sucFlg", "0");
				
				details.addProperty("errMsg", "Invalid Credentials");
				
				details.addProperty("Result", "Failed");
				details.addProperty("Message", "Invalid Credentials");
				
				return details;
			}
				
			logger.debug(">>>>>>>>>>> Calling PROCD_PWD_LOCKED <<<<<<<<<<<<<<<");
			
			final String procedureCall = "{call PROCD_PWD_LOCKED(?,?,?,?)}";
 			
 			Map<String, Object> resultMap = Jdbctemplate.call(new CallableStatementCreator() {
 	 
					public CallableStatement createCallableStatement(Connection connection) throws SQLException {
 
						CallableStatement CS = connection.prepareCall(procedureCall);
						CS.setString(1, custid);
						CS.setString(2, userid);							
						CS.registerOutParameter(3, Types.VARCHAR);
						CS.registerOutParameter(4, Types.VARCHAR);						
						
						return CS;
					}
 				}, get_PROCD_PWD_LOCKED_Params());

			logger.debug("Result of password lock :::: "+ resultMap.get("p_error") + "------->" + resultMap.get("o_flag"));
			
			if(resultMap.get("p_error") == null && resultMap.get("o_flag") != null && resultMap.get("o_flag").toString().equals("0")) 
			{
				logger.debug(">>>>>>>>>>> Calling PROC_LOGIN_VALID <<<<<<<<<<<<<<<");
				
				final String ProcedureCall = "{call PROC_LOGIN_VALID(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	 			
	 			Map<String, Object> ResultMap = Jdbctemplate.call(new CallableStatementCreator() {
	 	 
						public CallableStatement createCallableStatement(Connection connection) throws SQLException {
	 
							CallableStatement CS = connection.prepareCall(ProcedureCall);
							CS.setString(1, custid);
							CS.setString(2, userid);	
							CS.setString(3, hashedpwd);	
							CS.registerOutParameter(4, Types.VARCHAR);
							CS.registerOutParameter(5, Types.VARCHAR);
							CS.registerOutParameter(6, Types.VARCHAR);
							CS.registerOutParameter(7, Types.VARCHAR);
							CS.registerOutParameter(8, Types.VARCHAR);
							CS.registerOutParameter(9, Types.NUMERIC);
							CS.registerOutParameter(10, Types.NUMERIC);
							CS.registerOutParameter(11, Types.NUMERIC);
							CS.registerOutParameter(12, Types.NUMERIC);
							CS.registerOutParameter(13, Types.NUMERIC);
							CS.registerOutParameter(14, Types.VARCHAR);
							CS.registerOutParameter(15, Types.VARCHAR);
							CS.registerOutParameter(16, Types.NUMERIC);
							CS.registerOutParameter(17, Types.NUMERIC);
							CS.registerOutParameter(18, Types.CHAR);
							CS.registerOutParameter(19, Types.NUMERIC);
							CS.setInt(20, Integer.parseInt(INTFLAG));
							CS.registerOutParameter(21, Types.VARCHAR);
							CS.registerOutParameter(22, Types.VARCHAR);
							CS.registerOutParameter(23, Types.VARCHAR);						
							
							return CS;
						}
	 				}, get_proc_login_valid_Params());
	 			
					if(ResultMap.get("p_error") != null && ResultMap.get("p_error").toString().equals("S")) 
					{
						logger.debug("Login Success for userid:" + userid);
						
						details.addProperty("sucFlg", "0");		
						details.addProperty("sesUserId", userid);
						details.addProperty("sesUserName", util.ReplaceNull(ResultMap.get("p_user_name")));
						details.addProperty("sesMcontDate", util.ReplaceNull(ResultMap.get("p_current_date")));
						details.addProperty("sesDomainID", custid);	
						details.addProperty("sesRole", util.ReplaceNull(ResultMap.get("p_role")));
						details.addProperty("sesEmail", util.ReplaceNull(ResultMap.get("p_email")));
						details.addProperty("sesForcePwd", util.ReplaceNull(ResultMap.get("p_passexpiry")));
						details.addProperty("sesForceUserPwd", util.ReplaceNull(ResultMap.get("p_frs_usr_pwd_chg")));
						
						String pinreset = ResultMap.get("p_pwd_reset") != null ? util.ReplaceNull(ResultMap.get("p_pwd_reset")) : "0";
						
						details.addProperty("sesPinReset", pinreset);

						details.addProperty("suboffcdsess", util.ReplaceNull(ResultMap.get("branchcd")));
						details.addProperty("sesbaseSubUnits", util.ReplaceNull(ResultMap.get("p_curr_code"))); 	
						details.addProperty("sesCustCode", util.ReplaceNull(ResultMap.get("p_cust_code")));
						details.addProperty("sesCustName", util.ReplaceNull(ResultMap.get("p_cust_name")));
						details.addProperty("sucFlg", "1");

						details.addProperty("Result", "Success");
						details.addProperty("Message", "Success");
					} 
					else 
					{
						logger.debug("Login Failure for reason ::::: "+ResultMap.get("p_error"));
						
						details.addProperty("sucFlg", "0");
						details.addProperty("errMsg", util.ReplaceNull(ResultMap.get("p_error")));
						
						details.addProperty("Result", "Failed");
						details.addProperty("Message", util.ReplaceNull(ResultMap.get("p_error")));
					}
				} 
				else if(resultMap.get("p_error") != null && resultMap.get("o_flag").toString().equals("0")) 
				{
					logger.debug("Login Failure for reason :::: "+resultMap.get("p_error"));
					
					details.addProperty("sucFlg", "0");
					details.addProperty("errMsg", util.ReplaceNull(resultMap.get("p_error")));
					
					details.addProperty("Result", "Failed");
					details.addProperty("Message", util.ReplaceNull(resultMap.get("p_error")));
				} 
				else if(resultMap.get("p_error") == null && resultMap.get("o_flag").toString().equals("1")) 
				{
					logger.debug("Login Failure-Userid is Locked");
					
					details.addProperty("sucFlg", "0");
					details.addProperty("errMsg","User ID is Locked Contact System Administrator");
					
					details.addProperty("Result", "Failed");
					details.addProperty("Message", "User ID is Locked Contact System Administrator");
				} 
				else 
				{
					logger.debug("Login Failure-Invalid Password");
					
					details.addProperty("sucFlg", "0");
					details.addProperty("errMsg", "Invalid Password");
					
					details.addProperty("Result", "Failed");
					details.addProperty("Message", "Invalid Password");
				}
		}
		catch(Exception e) 
		{
			details.addProperty("sucFlg", "0");
			details.addProperty("errMsg", e.getLocalizedMessage());
			details.addProperty("Result", "Failed");
			
			logger.debug("Exception occured :::: "+e.getLocalizedMessage());
		} 
		
		return details;
	}
	
	public JsonObject getSessionInfo(String DomainId, String UserId) 
	{
		JsonObject details = new JsonObject();
	
		String ULOGIN_SESSION_ID = ""; String ULOGIN_OUT_DATE = "";
		
		try 
		{
			 String sql = "Select ULOGIN_SESSION_ID,ULOGIN_OUT_DATE FROM USERS008  WHERE suborgcode=? AND ULOGIN_USER_ID=?";
				
			 List<login_Info> obj = Jdbctemplate.query(sql, new Object[] { DomainId, UserId }, new Session_Info_Mapper());
			
			 if(obj.size() != 0)
 			 {
				 ULOGIN_SESSION_ID = obj.get(0).getULOGIN_SESSION_ID();
				 ULOGIN_OUT_DATE = obj.get(0).getULOGIN_OUT_DATE();
 			 }
			 
			 details.addProperty("ULOGIN_SESSION_ID", ULOGIN_SESSION_ID);
			 details.addProperty("ULOGIN_OUT_DATE", ULOGIN_OUT_DATE);
			 
			 details.addProperty("Result", obj.size() != 0 ? "Success" : "Failed");
			 details.addProperty("Message", obj.size() != 0 ? "Session Info Found" : "Session Not Found");
		 }
		 catch(Exception e)
		 {
			 logger.debug("getSessionInfo()::"+e.getLocalizedMessage()); 
			 
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
		 }

		 return details;
	}

	public JsonObject getSessionInterval(String cbd) 
	{
		JsonObject details = new JsonObject();
		
		String SYS_AUTO_LOGOUT = ""; String SYS_MULT_ALLOW = "";
		
		try
		{
			 String sql = "SELECT AUTO_LOGOUT_SECS,MULTIPLE_ALLOWED FROM SYSCONF002 WHERE EFF_DATE = (SELECT MAX(EFF_DATE) FROM SYSCONF002 WHERE EFF_DATE <= TO_DATE(?,'DD-MM-YYYY') AND AUSER IS NOT NULL)";
				
			 List<login_Info> obj = Jdbctemplate.query(sql, new Object[] { FormatUtils.dynaSQLDate(cbd, "DD-MM-YYYY") }, new Session_Interval_Mapper());
			
			 if(obj.size() != 0)
 			 {
				 SYS_AUTO_LOGOUT = obj.get(0).getAUTO_LOGOUT_SECS();
				 SYS_MULT_ALLOW = obj.get(0).getMULTIPLE_ALLOWED();
 			 }
			 
		     details.addProperty("SYS_AUTO_LOGOUT", SYS_AUTO_LOGOUT);
			 details.addProperty("SYS_MULT_ALLOW", SYS_MULT_ALLOW);
 	
			 details.addProperty("Result", obj.size() != 0 ? "Success" : "Failed");
			 details.addProperty("Message", obj.size() != 0 ? "Session Info Found" : "Session Not Found");
		 }
		 catch(Exception e)
		 {
			 logger.debug("getSessionInterval()::"+e.getLocalizedMessage()); 
			 
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
		 }

		 return details;
	}
	
	public JsonObject fetchCurrentTime() throws SQLException
	{
		JsonObject details = new JsonObject();
		 
		try 
		{
			 String sql = "select to_char(sysdate, 'HH24:MI:SS') as CURRENT_TIME from dual";
			 
			 List<String> obj = Jdbctemplate.queryForList(sql, String.class);
				
			 if(obj.size() != 0)
 			 {
				 details.addProperty("currentTime", obj.get(0));
 			 }
			 
			 details.addProperty("Result", obj.size() != 0 ? "Success" : "Failed");
			 details.addProperty("Message", obj.size() != 0 ? "CurrentTime Info Found" : "CurrentTime Info Not Found");
		 }
		 catch(Exception e)
		 {
			 logger.debug("fetchCurrentTime()::"+e.getLocalizedMessage()); 
			 
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
		 }
	
		 return details;
	}
	
	public JsonObject UpdateSignInoutDetails(JsonObject Info) 
	{
		JsonObject details = new JsonObject();
		 
		try 
		{
			boolean flag = false;
			
			if(Info.get("STATUS").getAsString().equals("1"))
			{
				details = Insert_loginout_Details(Info);
				
				flag = true;
			} 
			else if (Info.get("STATUS").getAsString().equals("2"))
			{
				details = updateDetails(Info);
				
				flag = true;
			} 
			else if(Info.get("STATUS").getAsString().equals("3")) 
			{
				details = updatePwdInvalid(Info);
				
				flag = true;
			}
			
			//details.addProperty("USER_STATUS", flag ? details.get("USER_STATUS").getAsString() : "");
			
			details.addProperty("Result", "Success");
			details.addProperty("Message", "USER_STATUS Found");

		}
		catch (Exception e)
		{
			logger.debug("insertSignInDetails()::"+e.getLocalizedMessage());
			
			details.addProperty("Result", "Failed");
			details.addProperty("Message", e.getLocalizedMessage()); 
		} 
	
		return details;
	}
	
	public JsonObject Insert_loginout_Details(JsonObject Info) 
	{
		JsonObject details = new JsonObject();
	
		try
		{
			if(Info.get("SUBORGCODE") != null && Info.get("USER_ID") != null)
			{
				 String SUBORGCODE = Info.get("SUBORGCODE").getAsString();
				 String USER_ID = Info.get("USER_ID").getAsString();
				 String SESSION_ID = Info.get("SESSION_ID").getAsString();
				 String IP = Info.get("IP").getAsString();
				
				 String sql = "SELECT *  FROM USERS008 WHERE suborgcode=? AND ULOGIN_USER_ID=?";
					
				 List<login_Info> obj = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, USER_ID }, new Session_Info_Mapper());
				
				 if(obj.size() != 0)
	 			 {
					 String session_id = obj.get(0).getULOGIN_SESSION_ID();
					 
					 String outdate = obj.get(0).getULOGIN_OUT_DATE();
	
					 if(outdate != null)
					 {
						 sql = "DELETE FROM USERS008  WHERE suborgcode=? AND ULOGIN_USER_ID=?";
						 
						 Jdbctemplate.update(sql, new Object[] { SUBORGCODE, USER_ID } );
						 
						 sql = "INSERT INTO USERS008(suborgcode,ULOGIN_USER_ID,ULOGIN_IN_DATE,ULOGIN_SESSION_ID,ULOGIN_IP) VALUES(?,?,?,?,?)";
					
						 Jdbctemplate.update(sql, new Object[] { SUBORGCODE, USER_ID, new java.sql.Timestamp(new java.util.Date().getTime()),SESSION_ID, IP } );
								
						 details.addProperty("USER_STATUS", "0");
					 }
					 else if(!session_id.equals(SESSION_ID))
					 {
						 sql = "DELETE FROM USERS008  WHERE suborgcode=? AND ULOGIN_USER_ID=?";
						 
						 Jdbctemplate.update(sql, new Object[] { SUBORGCODE, USER_ID } );
						 
						 sql = "INSERT INTO USERS008(suborgcode,ULOGIN_USER_ID,ULOGIN_IN_DATE,ULOGIN_SESSION_ID,ULOGIN_IP) VALUES(?,?,?,?,?)";
					
						 Jdbctemplate.update(sql, new Object[] { SUBORGCODE, USER_ID, new java.sql.Timestamp(new java.util.Date().getTime()),SESSION_ID, IP } );
								
						 details.addProperty("USER_STATUS", "0");
					} 
					else
					{
						details.addProperty("USER_STATUS", "1");
					}
				}
				else
				{
					sql = "INSERT INTO USERS008(suborgcode,ULOGIN_USER_ID,ULOGIN_IN_DATE,ULOGIN_SESSION_ID,ULOGIN_IP) VALUES(?,?,?,?,?)";
					
					Jdbctemplate.update(sql, new Object[] { SUBORGCODE, USER_ID, new java.sql.Timestamp(new java.util.Date().getTime()), SESSION_ID, IP } );
			
					details.addProperty("USER_STATUS", "0");
				}
				
				sql = "INSERT INTO USERS009(suborgcode,ULHST_USER_ID,ULHST_IN_DATE,ULHST_REMARKS,ULHST_SESSION_ID,ULHST_IP) VALUES(?,?,?,?,?,?)";
					
				Jdbctemplate.update(sql, new Object[] { SUBORGCODE, USER_ID, new java.sql.Timestamp(new java.util.Date().getTime()), "SUCCESS", SESSION_ID, IP } );
			
				details.addProperty("INSERT", "Y");
				details.addProperty("sucFlg", "1");
			}
			else
			{
				details.addProperty("sucFlg", "0");
			}
		} 
		catch (Exception e) 
		{
			details.addProperty("INSERT", "F");
			details.addProperty("sucFlg", "0");
			details.addProperty("errmsg", e.getLocalizedMessage());
		} 
		
		return details;
	}

	private JsonObject updateDetails(JsonObject Info) 
	{
		JsonObject details = new JsonObject();
		
		try 
		{
			if(Info.get("SUBORGCODE") != null && Info.get("USER_ID") != null)
			{
				 String SUBORGCODE = Info.get("SUBORGCODE").getAsString();
				 String USER_ID = Info.get("USER_ID").getAsString();
				 String SESSION_ID = Info.get("SESSION_ID").getAsString();
				 
				 String sql = "SELECT * FROM USERS008  WHERE suborgcode=? AND ULOGIN_USER_ID=? AND ULOGIN_SESSION_ID = ?";
					
				 List<login_Info> obj = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, USER_ID, SESSION_ID }, new Session_Mapper());
				
				 if(obj.size() != 0)
	 			 {
					 sql = "UPDATE USERS008 SET ULOGIN_OUT_DATE=? WHERE suborgcode=? AND ULOGIN_USER_ID=?";
					 
					 Jdbctemplate.update(sql, new Object[] { new java.sql.Timestamp(new java.util.Date().getTime()), SUBORGCODE, USER_ID });
					
					 sql = "UPDATE USERS009 SET ULHST_OUT_DATE=? WHERE suborgcode=? AND ULHST_USER_ID=? AND ULHST_IN_DATE=(SELECT MAX(ULHST_IN_DATE) FROM USERS009  WHERE suborgcode=? AND ULHST_USER_ID=? )";
					 
					 Jdbctemplate.update(sql, new Object[] { new java.sql.Timestamp(new java.util.Date().getTime()), SUBORGCODE, USER_ID, SUBORGCODE, USER_ID });
			
					 details.addProperty("UPDATE", "Y");
					 details.addProperty("sucFlg", "1");
				}
				else
				{
					details.addProperty("sucFlg", "0");
				}
			}
			else
			{
				details.addProperty("sucFlg", "0");
			}
		}
		catch(Exception e) 
		{
			details.addProperty("UPDATE", "F");
			details.addProperty("sucFlg", "0");
			details.addProperty("errmsg", e.getLocalizedMessage());
		}
		
		return details;
	}
	
	private JsonObject updatePwdInvalid(JsonObject Info) 
	{
		JsonObject details = new JsonObject();
		
		try 
		{
			if(Info.get("SUBORGCODE") != null && Info.get("USER_ID") != null)
			{
				String SUBORGCODE = Info.get("SUBORGCODE").getAsString();
				
				String USER_ID = Info.get("USER_ID").getAsString();
				
				 String sql = "SELECT * FROM USERS012 WHERE suborgcode=? AND UPWDINV_USER_ID=?";
					
				 List<login_Info> obj = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, USER_ID }, new Userlock_Mapper());
				
				 if(obj.size() != 0)
	 			 {
					 int counter =  Integer.parseInt(obj.get(0).getINVALID_COUNT());
					 
					 sql = "UPDATE USERS012 SET UPWDINV_INVALID_COUNT=? WHERE suborgcode=? AND UPWDINV_USER_ID=?";
					 
					 Jdbctemplate.update(sql, new Object[] { counter + 1, SUBORGCODE, USER_ID });
				 } 
				 else 
				 {
					 sql = "INSERT INTO USERS012 VALUES(?,?,?)";
					 
					 Jdbctemplate.update(sql, new Object[] { USER_ID, SUBORGCODE, 1 });
				}

				 details.addProperty("sucFlg", "1");
			} 
			else 
			{
				details.addProperty("sucFlg", "0");
			}
		} 
		catch(Exception e) 
		{
			details.addProperty("INSERT", "F");
			details.addProperty("sucFlg", "0");
		}
		
		return details;
	}
	
	public JsonObject get_Menu_Detail(String ROLECD, String USERID) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "SELECT * FROM menu005 where ROLECD=? or USERID=? order by MENU_ORDER";
			 
			 List<Dynamic_Menu> obj = Jdbctemplate.query(sql, new Object[] { ROLECD, USERID }, new Menu_Mapper());
				
			 List<Menu_Items> Main_Menu = new ArrayList<Menu_Items>();
			
			 JsonArray Main_Menus = new JsonArray(); 
			 
			 for(int i=0;i<obj.size();i++)
 			 {
				 Menu_Items menu = new Menu_Items();
				 
				 menu.setMENU_HEADER(obj.get(i).getMENU_HEADER());
				 menu.setMENU_DESCN(obj.get(i).getMENU_DESCN());
				 menu.setMENU_ORDER(obj.get(i).getMENU_ORDER());
				
				 Main_Menu.add(menu);
				 
				 JsonObject Main_Menu_details = new JsonObject();
				 
				 Main_Menu_details.addProperty("MENU_HEADER", menu.getMENU_HEADER());
				 Main_Menu_details.addProperty("MENU_DESCN", menu.getMENU_DESCN());
				 Main_Menu_details.addProperty("MENU_ORDER", menu.getMENU_ORDER());
				 
				 Main_Menus.add(Main_Menu_details);
			  }
			 
			  details.add("Main_Menu", Main_Menus);
			
			  sql = "SELECT * FROM menu004 order by PGM_ID, MENUITEM_SL";
				 
			  List<MENU_004> obj2 = Jdbctemplate.query(sql, new MENU_004_Mapper());
					
			  List<Menu_Items> sub_menu = new ArrayList<Menu_Items>();
			 			
			  for(int i=0;i<obj2.size();i++)
	 		  {
				  Menu_Items menu = new Menu_Items();
				 
				  menu.setPGM_ID(obj2.get(i).getPGM_ID());
				  menu.setMENUITEM_SL(obj2.get(i).getMENUITEM_SL());
				  menu.setPGM_NAME(obj2.get(i).getPGM_NAME());
				  menu.setPGM_PATH(obj2.get(i).getPGM_PATH());
				 
				  sub_menu.add(menu);
			   }
			 
			   JsonObject All_Menus = new JsonObject();
			 
			   for(int i=0; i<Main_Menu.size(); i++)
			   {
				  String Menu_header = Main_Menu.get(i).getMENU_HEADER();
				 
				  JsonArray Menus = new JsonArray(); 
				 
				  for(int j=0; j<sub_menu.size(); j++)
				  {
					 String Pgm_Id =  sub_menu.get(j).getPGM_ID();
					 
					 if(Menu_header.equals(Pgm_Id))
					 {
						 JsonObject Menu_details = new JsonObject();
						 
						 Menu_details.addProperty("PGM_NAME", sub_menu.get(j).getPGM_NAME());							 
						 Menu_details.addProperty("PGM_PATH", sub_menu.get(j).getPGM_PATH());
						 Menu_details.addProperty("MENUITEM_SL", sub_menu.get(j).getMENUITEM_SL());			
						 Menu_details.addProperty("PGM_ID", sub_menu.get(j).getPGM_ID());
						 
						 Menus.add(Menu_details);
					 } 
				 }
				 			 
				 All_Menus.add(Menu_header, Menus);
			 }
			
			 details.add("Sub_Menu", All_Menus);
			
			 details.addProperty("Result", All_Menus.size() !=0 ? "Success" : "Failed");	
			 details.addProperty("Message", All_Menus.size() !=0 ? "Menus Found" : "Menus Not Found");		 	 
		}
		catch(Exception e)
		{
			details.addProperty("Result", "Failed");	
			details.addProperty("Message", "Menus Not Found");	
			
			logger.debug("Exception in menu program :::::"+e.getLocalizedMessage());
		}
		
		return details;
	}
	
	public JsonObject loadImage(String SUBORGCODE, String USERID, HttpServletRequest request)
	{
		JsonObject details = new JsonObject();
		
		Blob img ;  
		
		String Photo = request.getContextPath()+"/resources/FIU/img/Default_Profile.jpg";
		
	    byte[] imgData = null ;
	    
		try 
		{
			 String sql = "SELECT PHOTO FROM USERS014 where SUBORGCODE=? and USERID=?";
				
			 List<Blob> obj = Jdbctemplate.queryForList(sql, new Object[] { SUBORGCODE, USERID }, Blob.class);
			
			 if(obj.size() != 0)
 			 {
				  img = obj.get(0);
					
				  imgData = img.getBytes(1,(int)img.length());
					
					if(imgData != null) 
		 			{
		 	 			String encode = Base64.encodeBase64String(imgData);
		 	 			
		 	 			Photo = "data:image/jpeg;base64,"+encode;
		 			}	
 			 }
			 
			 details.addProperty("Photo", Photo);
			 
			 details.addProperty("Result",  "Success");
			 details.addProperty("Message", "Session Info Found");
		 }
		 catch(Exception e)
		 {
			 logger.debug("loadImage::"+e.getLocalizedMessage()); 
			 
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
		 }
		
		return details;
	}
	
	public JsonObject getInternetIntranetDetail(HttpServletRequest requests,HttpServletResponse responses) 
	{
		JsonObject details = new JsonObject();
		
		char intflag = 'F';
		
		try
		{
			String serverName = requests.getServerName();
			serverName = "localhost";
			String remoteAddr = requests.getRemoteAddr();
			String url = new String(requests.getRequestURL()); 
			
			logger.debug("Remote Addr: " + remoteAddr);
			logger.debug("Request URL: " + url);
			logger.debug("ServerIP: " + serverName);
			
			Properties props = new Properties();
			
			props.put("IntranetIP", IntranetIP);
			props.put("PublicIP", PublicIP);
			props.put("IntraIPCount", IntraIPCount);
			props.put("ProxyIPCount", ProxyIPCount);
			props.put("DomainName", Accessablity.DomainName);
			props.put("IntraIP1", IntraIP1);
			props.put("IntraIP2", IntraIP2);
			props.put("IntraIP3", IntraIP3);
			props.put("IntraIP4", IntraIP4);
			props.put("ProxyIP1", ProxyIP1);
			props.put("ProxyIP2", ProxyIP2);
			props.put("ProxyIP3", ProxyIP3);
			props.put("ProxyIP4", ProxyIP4);
			
			if(webServerIntranetIP == null || webServerIntranetIP == "") 
			{
				boolean filecheck = true;
				
				if (props.getProperty("IntranetIP") != null	&& !"".equals(props.getProperty("IntranetIP"))) 
				{
					webServerIntranetIP = props.getProperty("IntranetIP");
				} 
				else 
				{
					filecheck = false; 
				}
					
				if (props.getProperty("PublicIP") != null	&& !"".equals(props.getProperty("PublicIP"))) 
				{
					webServerPublicIP = props.getProperty("PublicIP");
				} 
				else 
				{
					filecheck = false;  
				}
				
				if (props.getProperty("DomainName") != null	&& !"".equals(props.getProperty("DomainName"))) 
				{
					DomainName = props.getProperty("DomainName"); 
				} 
				else 
				{
					filecheck = false; 
				}
				
				if (props.getProperty("IntraIPCount") != null && !"".equals(props.getProperty("IntraIPCount")))
				{
					intraIPCount = Integer.parseInt(props.getProperty("IntraIPCount"));
					logger.debug("IntraIPCount: " + intraIPCount);
				} 
				else 
				{
					filecheck = false; 
				}
				
				if (props.getProperty("ProxyIPCount") != null && !"".equals(props.getProperty("ProxyIPCount")))
				{
					proxyIPCount = Integer.parseInt(props.getProperty("ProxyIPCount"));
					logger.debug("ProxyIPCount: " + proxyIPCount);
				}
				else 
				{
					filecheck = false;   
				}

				if (filecheck) 
				{
					intraIPArr = new String[intraIPCount];
					proxyIPArr = new String[proxyIPCount];

					for (int i = 0; i < intraIPCount; i++) 
					{
						int j = i + 1;
						
						if (props.getProperty("IntraIP" + j) != null && !"".equals(props.getProperty("IntraIP" + j))) 
						{
							intraIPArr[i] = props.getProperty("IntraIP" + j);
						}
					}

					for (int i = 0; i < proxyIPCount; i++) 
					{
						int j = i + 1;
						
						if (props.getProperty("ProxyIP" + j) != null && !"".equals(props.getProperty("ProxyIP" + j))) 
						{
							proxyIPArr[i] = props.getProperty("ProxyIP" + j);
						}
					}
				} 
				else
				{
					intflag = 'F';
					
					details.addProperty("intflag", intflag);
					details.addProperty("Result", "Failed");
					
					logger.debug("Login Failure-Invalid Internet-Intranet Configuration");
					//details.addProperty("Message", e.getLocalizedMessage());
					
					return details;
				}
			}

			if (serverName.equalsIgnoreCase(webServerIntranetIP)) 
			{
				logger.debug("Request is Intranet");
				
				intflag = '0';
			} 
			else
			{

				if (serverName.equalsIgnoreCase(DomainName))
				{
					boolean furtherChk = false; 
					
					if (furtherChk) 
					{
						for (int i = 0; i < proxyIPCount; i++) 
						{
							if (remoteAddr != null && !"".equals(remoteAddr) && remoteAddr.length() > 6) 
							{
								if (remoteAddr.equalsIgnoreCase(proxyIPArr[i]))
								{
									logger.debug("Request is Intranet");
									intflag = '0';
									furtherChk = false;
									break;
								}
							}
						}
					}

					if (furtherChk) 
					{
						logger.debug("Request is Internet");
						intflag = '1';
					}	
				} 
				else if (serverName.equalsIgnoreCase(webServerPublicIP)) 
				{
					boolean furtherChk = true;
					
					if (furtherChk) 
					{
						for (int i = 0; i < proxyIPCount; i++) 
						{
							if (remoteAddr != null && !"".equals(remoteAddr) && remoteAddr.length() > 6) 
							{
								if (remoteAddr.equalsIgnoreCase(proxyIPArr[i])) 
								{
									logger.debug("Request is Intranet");
									intflag = '0';
									furtherChk = false;
									break;
								}
							}
						}
					}

					if (furtherChk) 
					{
						logger.debug("Request is Internet");
						intflag = '1';
					}
				}
			} 
			
			details.addProperty("Result", "Success");
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage());
			intflag = 'F';
			
			details.addProperty("Result", "Failed");
			details.addProperty("Message", e.getLocalizedMessage());
		}
		
		details.addProperty("intflag", intflag);
		
		return details;
	}
	
	public List<SqlParameter> get_PROCD_PWD_LOCKED_Params()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("i_domain_id" , Types.VARCHAR));
		inParamMap.add(new SqlParameter("i_user_id"   , Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("o_flag"   , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("p_error"  , Types.VARCHAR));
		
		return inParamMap;
	}
	
	public List<SqlParameter> get_proc_login_valid_Params()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("p_suborgcode"  , Types.VARCHAR));
		inParamMap.add(new SqlParameter("p_user_id"   	, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("p_password"    , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("p_user_name"  , Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("p_mn_curr_business_date"  , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("p_current_date", Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("p_role"  		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("p_email"   	, Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("p_passexpiry"  , Types.FLOAT));
		inParamMap.add(new SqlOutParameter("p_syscpm_min_uid_length"  , Types.FLOAT));
		inParamMap.add(new SqlOutParameter("p_syscpm_min_pwd_length"  , Types.FLOAT));
		inParamMap.add(new SqlOutParameter("p_syscpm_min_num_pwd"     , Types.FLOAT));
		inParamMap.add(new SqlOutParameter("p_syscpm_min_alpha_pwd"   , Types.FLOAT)); 
		inParamMap.add(new SqlOutParameter("p_error"  		, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("p_last_login_date_time" , Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("p_frs_usr_pwd_chg"  	 , Types.FLOAT));
		inParamMap.add(new SqlOutParameter("p_syscpm_sess_tmout" 	, Types.FLOAT)); 
		inParamMap.add(new SqlOutParameter("p_pwd_reset"   , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("branchcd"   	, Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("p_int_inta"  	, Types.FLOAT));
		inParamMap.add(new SqlOutParameter("p_curr_code"   , Types.VARCHAR)); 
		inParamMap.add(new SqlOutParameter("p_cust_code"  	, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("p_cust_name"   , Types.VARCHAR)); 
		
		return inParamMap;
	}
	
	public class Session_Info_Mapper implements RowMapper<login_Info> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public login_Info mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			login_Info API = new login_Info();  

			API.setULOGIN_SESSION_ID(util.ReplaceNull(rs.getString("ULOGIN_SESSION_ID")));
			API.setULOGIN_OUT_DATE(util.ReplaceNull(rs.getString("ULOGIN_OUT_DATE")));
			
			return API;
		}
     }
    
    public class Session_Interval_Mapper implements RowMapper<login_Info> 
    {
		public login_Info mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			login_Info API = new login_Info();  
 
			API.setAUTO_LOGOUT_SECS(rs.getString("AUTO_LOGOUT_SECS"));   
			API.setMULTIPLE_ALLOWED(rs.getString("MULTIPLE_ALLOWED"));   
			
			return API;
		}
     }
    
    private class Session_Mapper implements RowMapper<login_Info> 
    {
		public login_Info mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			login_Info API = new login_Info();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setUSER_ID(rs.getString("ULOGIN_USER_ID"));
			API.setSESSION_ID(rs.getString("ULOGIN_SESSION_ID"));
			
			return API;
		}
     }
    
    private class Userlock_Mapper implements RowMapper<login_Info> 
    {
		public login_Info mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			login_Info API = new login_Info();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setUSER_ID(rs.getString("UPWDINV_USER_ID"));
			API.setINVALID_COUNT(rs.getString("UPWDINV_INVALID_COUNT"));
			
			return API;
		}
     }
    
    public class Menu_Mapper implements RowMapper<Dynamic_Menu> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Dynamic_Menu mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Dynamic_Menu API = new Dynamic_Menu();  

			API.setMENU_FOR(rs.getString("MENU_FOR"));
			API.setUSERID_ROLE(rs.getString("USERID_ROLE"));
			API.setSYSCODE(rs.getString("SYSCODE"));
			API.setMENU_HEADER(rs.getString("MENU_HEADER"));
			API.setMENU_DESCN(rs.getString("MENU_DESCN"));
			API.setMENU_PARENTMENU(rs.getString("MENU_PARENTMENU"));
			API.setMENU_ORDER(rs.getString("MENU_ORDER"));
			API.setMENU_SUBORDER(rs.getString("MENU_SUBORDER"));
			API.setMENU_PARENT_FORM(rs.getString("MENU_PARENT_FORM"));
			API.setMENU_FORMPATH(rs.getString("MENU_FORMPATH"));  
			API.setMENU_PARENT_HEADER(rs.getString("MENU_PARENT_HEADER"));
			API.setMENU_LOCATION(rs.getString("MENU_LOCATION"));
			API.setMENU_STATUS(rs.getString("MENU_STATUS"));
			API.setMENU_ICON(util.Blob_to_string(rs.getBlob("MENU_LOGO")));
	
			return API;
		}
    }
    
    public class MENU_004_Mapper implements RowMapper<MENU_004> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public MENU_004 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			MENU_004 API = new MENU_004();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setPGM_ID(rs.getString("PGM_ID"));
			API.setMENUITEM_SL(rs.getString("MENUITEM_SL"));
			API.setPGM_NAME(rs.getString("PGM_NAME"));
			API.setPGM_PATH(rs.getString("PGM_PATH"));
			API.setMENULOCATION(rs.getString("MENULOCATION"));
			
			return API;
		}
    }
   
}
