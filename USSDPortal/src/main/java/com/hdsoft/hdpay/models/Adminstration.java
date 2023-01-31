package com.hdsoft.hdpay.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.hdpay.Repositories.Auth001;
import com.hdsoft.hdpay.Repositories.Auth003;
import com.hdsoft.hdpay.Repositories.Menu001;
import com.hdsoft.hdpay.Repositories.Menu_002;
import com.hdsoft.hdpay.Repositories.PgmID;
import com.hdsoft.hdpay.Repositories.Suboffcd;
import com.hdsoft.hdpay.Repositories.Users0001;
import com.hdsoft.hdpay.Repositories.Users014;
import com.hdsoft.utils.EncryptDecrypt;
import com.hdsoft.utils.FormatUtils;
import com.hdsoft.utils.PasswordUtils;
import com.zaxxer.hikari.HikariDataSource;


@Component
public class Adminstration

{
	public JdbcTemplate jdbc;
    
    @Autowired
    public void setJdbcTemplate(HikariDataSource dataSource) {
    	this.jdbc = new JdbcTemplate(dataSource);
    }
    
	 @Autowired
	    PasswordUtils pu;
	 
	//private String errMsg;
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    Timestamp tstamp;
    
    
    public Adminstration()
    {
    	tstamp = new java.sql.Timestamp(new java.util.Date().getTime());
    }
    
    public JsonObject updateValues(JsonObject formDTO)
    {
		String main_pk = null;
		String allvalues = null;
		
		String transeq = transeq();
		
		String mode = formDTO.get("mode").getAsString();
		//String torgcd = formDTO.get("torgcd");
		String tuserid = formDTO.get("tuserid").getAsString();
		String tusernme = formDTO.get("tusernme").getAsString();
		String tbirthdate = formDTO.get("tbirthdate").getAsString();
		String tmobile = formDTO.get("tmobile").getAsString();
		String temail = formDTO.get("temail").getAsString();
		//String tcomaddr = formDTO.get("tcomaddr");
		String tconfirmpwd = formDTO.get("tconfirmpwd").getAsString();
		String trolecd = formDTO.get("trolecd").getAsString();
		String tregdate = formDTO.get("tregdate").getAsString();
		String branchcd = formDTO.get("branchcd").getAsString();
		
		String sesMcontDate = formDTO.get("sesMcontDate").getAsString();
		
		String randomSalt = formDTO.get("randomSalt").getAsString();
		String pwd = formDTO.get("hashedPassword").getAsString();
	
		EncryptDecrypt decryption = new EncryptDecrypt();
		logger.debug("hashedPassword :::: "+pwd);
		logger.debug("salt :::: "+randomSalt);
		
		pwd = decryption.doDecrypt(pwd, randomSalt);
		
		logger.debug("Random salt -------->" + randomSalt);
		
		logger.debug("pwd---------------->" + pwd);
		
		logger.debug("userid---------------->" + tuserid);
		
		String final_salt = PasswordUtils.getSalt(tuserid);
	
		if(final_salt == null) final_salt = "";
		
		final_salt = "";
		
		final String hashedpwd = PasswordUtils.getEncrypted(pwd, tuserid, final_salt);
		
		tbirthdate = FormatUtils.dynaSQLDate(tbirthdate,"DD-MM-YYYY");
		tregdate = FormatUtils.dynaSQLDate(tregdate,"DD-MM-YYYY"); 
		sesMcontDate = FormatUtils.dynaSQLDate(sesMcontDate,"DD-MM-YYYY"); 

		main_pk = formDTO.get("sesDomainID").getAsString()+"|"+formDTO.get("tuserid").getAsString();
		
		logger.debug("primary key"+main_pk);
		
		allvalues = main_pk+"|"+tusernme+"|"+tbirthdate+"|"+tmobile+"|"+temail+"|"+randomSalt+"|"+hashedpwd+"|"+trolecd+"|"+
					tregdate+"|"+formDTO.get("sesUserId").getAsString()+"|"+sesMcontDate+"|"+formDTO.get("sesUserId").getAsString()+"|"+   //A-USER
					sesMcontDate+"|"+formDTO.get("sesUserId").getAsString()+"|"+sesMcontDate+"|"+branchcd+"|";		
		logger.debug(allvalues);
		logger.debug("Data block created");
		
		long Blocksl = 1L;
		
		JsonObject input_values = new JsonObject();
		
		input_values.addProperty("Primarykey", main_pk);
		input_values.addProperty("Domain", formDTO.get("sesDomainID").getAsString());
		input_values.addProperty("EntryMode", mode);
		logger.debug(mode);
		input_values.addProperty("DoneBy", formDTO.get("sesUserId").getAsString());
		
		input_values.addProperty("Table", "USERS0001");
		input_values.addProperty("DataBlock", allvalues);
		input_values.addProperty("Blocksl", String.valueOf(Blocksl));
		input_values.addProperty("PgmId", "userregistration");
		input_values.addProperty("userIp", formDTO.get("sesUserId").getAsString());  
		input_values.addProperty("SEQNO",transeq) ;

		logger.debug("<<<<<<<<<<< Calling AuthEntry >>>>>>>>>>>");
		
		JsonObject resultDTO = authEntry(input_values);
		
		String val = resultDTO.get("sucFlg").getAsString();
		
		if (val.equals("1")) 
		{
			resultDTO.addProperty("Result", "Success");
			resultDTO.addProperty("Message", "Record Updated Successfully");
			
			logger.debug("<<<<<<<<<<< AuthEntry Success >>>>>>>>>>>");
		} 
		else
		{
			resultDTO.addProperty("Result", "Failed");
			resultDTO.addProperty("Message", "Record Not Updated");
			
			logger.debug("<<<<<<<<<<< AuthEntry Failed >>>>>>>>>>>");
		}
		
		return resultDTO;
	}
    
	public JsonObject authEntry(JsonObject formDTO) 
	{
		JsonObject output = new JsonObject();
		
		String Primarykey = formDTO.get("Primarykey").getAsString();
		String Domain = formDTO.get("Domain").getAsString();
		String DoneBy =formDTO.get("DoneBy").getAsString();
		
		long Branch = 13;
		
		Branch = branchcd(DoneBy, Domain);
		
		String SEQNO = formDTO.get("SEQNO").getAsString();
		
		long Detailsl = 1;
		
		String TableName =formDTO.get("Table").getAsString();
		String EntryMode = formDTO.get("EntryMode").getAsString();
		String DataBlk =formDTO.get("DataBlock").getAsString();
		String PgmId=formDTO.get("PgmId").getAsString();
		String userIp=formDTO.get("userIp").getAsString();
		
		//String dateStr = FormatUtils.formatToDDMonYear(formDTO.get("sesMcontDate"));
		
		System.out.println(Primarykey+TableName+EntryMode);
		
		if(!EntryMode.equalsIgnoreCase("U") && !EntryMode.equalsIgnoreCase("M"))
		{
			Detailsl = DTSL(Primarykey, TableName, EntryMode);
			System.out.println(Detailsl);
		}
		
		String tableType = "M";
		if(formDTO.has("TableType")) 
		{
			tableType = formDTO.get("TableType").getAsString();
		}

		long Blocksl=1;		
		
		if(formDTO.has("Blocksl"))
		{
			Blocksl = Long.parseLong(formDTO.get("Blocksl").getAsString());
		}

		logger.debug("Authorization queue Updating....");
					 		  
	   if(tableType.equalsIgnoreCase("M"))
	   {
		  try
		  {  
			  if(!EntryMode.equalsIgnoreCase("U") && !EntryMode.equalsIgnoreCase("M"))
			  {  
				String sql = "INSERT INTO auth001 (AUTHQ_PGM_ID,AUTHQ_MAIN_PK,AUTHQ_ENTRY_DATE,AUTHQ_DTL_SL,AUTHQ_DOMAIN,"+
													"AUTHQ_DISPLAY_DTLS,AUTHQ_TABLE_NAME,AUTHQ_OPERN_FLG,AUTHQ_DONE_BY,AUTHQ_DONE_BRN,AUTHQ_OPERN_DATE,"+
													"AUTHQ_FIRST_AUTH_BY,STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				int li = jdbc.update(sql,new Object[] {PgmId,Primarykey,tstamp,Detailsl,Domain,SEQNO,TableName,EntryMode,DoneBy,Branch,new java.sql.Timestamp(new java.util.Date().getTime()),"",""});
				
				if(li != 0) {
					output.addProperty("sucFlg", "1");
					output.addProperty("Result", "Success");
					output.addProperty("Message", "Data Inserted to Auth001");
				}
				
				logger.debug(">>>>>>>>>>> Data Inserted to Auth001 <<<<<<<<<<<<<<<");
			}
			else
			{
					String sql = "UPDATE auth001 SET AUTHQ_OPERN_FLG = ?, AUTHQ_DONE_BY = ?, AUTHQ_OPERN_DATE = ? WHERE" +
							" AUTHQ_PGM_ID = ? AND AUTHQ_MAIN_PK = ?  AND AUTHQ_DOMAIN = ?";
					int li = jdbc.update(sql,EntryMode,DoneBy,new java.sql.Timestamp(new java.util.Date().getTime()),new Object[] {PgmId,Primarykey,Domain});
					
					if(li != 0) {
						output.addProperty("sucFlg", "1");
						output.addProperty("Result", "Success");
						output.addProperty("Message", "Data Updated to Auth001");
					}
					
					logger.debug(">>>>>>>>>>> Data Updated to Auth001 <<<<<<<<<<<<<<<");
				}
			}			
			catch (Exception e)
		    {
				output.addProperty("sucFlg", "0");
				
				logger.debug("Execption in AuthEntry " +e);
			}
		  
			try
			{	
				  if(!EntryMode.equalsIgnoreCase("U") && !EntryMode.equalsIgnoreCase("M"))
				  {
						String sql = "INSERT INTO AUTH002 (AUTH_MAIN_PK,AUTH_ENTRY_DATE,AUTH_DTL_SL,AUTH_DOMAIN,AUTH_OPERN_FLG,AUTH_DONE_BY," +
																"AUTH_DONE_BRN,AUTH_OPERN_DATE,AUTH_STATUS,AUTH_FIRST_AUTH_BY,AUTH_FIRST_AUTH_ON,AUTH_AUTH_BY,AUTH_AUTH_ON,AUTH_REJ_BY,AUTH_REJ_ON,"+
																"AUTH_MAIN_TABLE_NAME) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						int li = jdbc.update(sql,new Object[] {Primarykey,tstamp,Detailsl,Domain,EntryMode,DoneBy,Branch,new java.sql.Timestamp(new java.util.Date().getTime()),"P"," ",null," ",null," ",null,TableName});
						
						if(li != 0) {
							output.addProperty("sucFlg", "1");
							output.addProperty("Result", "Success");
							output.addProperty("Message", "Data Inserted to Auth002");
						}
						output.addProperty("sucFlg", "1");
						
						logger.debug(">>>>>>>>>>> Data Inserted to Auth002 <<<<<<<<<<<<<<<");
				  }
				else
				{
					  String sql = "UPDATE auth002 SET AUTH_OPERN_FLG = ?, AUTH_DONE_BY= ?, AUTH_DONE_BRN= ?, AUTH_OPERN_DATE=? " +
					  		"WHERE AUTH_MAIN_PK = ?  AND AUTH_DOMAIN = ?";
					  int li = jdbc.update(sql,EntryMode,DoneBy,"13",new java.sql.Timestamp(new java.util.Date().getTime()),new Object[] {Primarykey,Domain});
						
						if(li != 0) {
							output.addProperty("sucFlg", "1");
							output.addProperty("Result", "Success");
							output.addProperty("Message", "Data Updated to Auth002");
						}
						
						output.addProperty("sucFlg", "1");
						
						logger.debug(">>>>>>>>>>> Data Updated to Auth002 <<<<<<<<<<<<<<<");
				  }		
			}
			catch (Exception e) 
			{
				output.addProperty("sucFlg", "0");
				
				logger.debug("Exception Occured in AUTH insert "+e);	
			}
			
			try
			{
				String auditsql =  "INSERT INTO LOG001 (suborgcode,USER_ID,LOG_DATETIME,LOG_SL,DTL_SL,FORM_NAME,USER_IP,USER_ACTION,TABLE_NAME,LOG_PK,IMAGE_TYPE,LOG_BRK_SL,LOG_DATA) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				int li = jdbc.update(auditsql,new Object[] {Domain,DoneBy,tstamp,getAuditLogSerial(),"1",PgmId,userIp,EntryMode,TableName,Primarykey,"N","1",DataBlk});
				
				if(li != 0) {
					output.addProperty("sucFlg", "1");
					output.addProperty("Result", "Success");
					output.addProperty("Message", "Data Inserted to Log0001");
				}
				output.addProperty("sucFlg", "1");
				
				logger.debug(">>>>>>>>>>> Data Inserted to Log0001 <<<<<<<<<<<<<<<");
			}
			catch(Exception e)
			{
				output.addProperty("sucFlg", "0");
				
				logger.debug("Exception in Occured in LOG001 insert" +e);	
			}
		 }
	  
		 try
		 {
			 if(!EntryMode.equalsIgnoreCase("U") && !EntryMode.equalsIgnoreCase("M"))
			 {
			 
				String sql2 = "INSERT INTO auth003 (AUTHDTL_MAIN_PK,AUTHDTL_ENTRY_DATE,AUTHDTL_DTL_SL,AUTHDTL_DOMAIN,AUTHDTL_TABLE_NAME,"+
			              "AUTHDTL_BLOCK_SL,AUTHDTL_DATA_BLOCK,AUTHDTL_TABLE_TYPE) VALUES(?,?,?,?,?,?,?,?)";
				int li = jdbc.update(sql2,new Object[] {Primarykey,tstamp,Detailsl,Domain,TableName,Blocksl,DataBlk,tableType});
				
				if(li != 0) {
					output.addProperty("sucFlg", "1");
					output.addProperty("Result", "Success");
					output.addProperty("Message", "Data Inserted to Auth003");
				}
				output.addProperty("sucFlg", "1");
				
				logger.debug(">>>>>>>>>>> Data Inserted to Auth003 <<<<<<<<<<<<<<<");
			  }
			  else
			  {
				  String sql2 = "UPDATE auth003 SET AUTHDTL_DATA_BLOCK = ? WHERE AUTHDTL_MAIN_PK = ? AND AUTHDTL_DOMAIN = ? " +
				  		"AND AUTHDTL_TABLE_NAME = ? AND AUTHDTL_TABLE_TYPE = ?";
				  int li = jdbc.update(sql2,DataBlk,new Object[] {Primarykey,Domain,TableName,tableType});
					
					if(li != 0) {
						output.addProperty("sucFlg", "1");
						output.addProperty("Result", "Success");
						output.addProperty("Message", "Data Updated to Auth003");
					}
					output.addProperty("sucFlg", "1");
					
					logger.debug(">>>>>>>>>>> Data Updated to Auth003 <<<<<<<<<<<<<<<");
			  }		
		 }
		 catch(Exception e)
		 {
			 output.addProperty("sucFlg", "0");
			 
			 logger.debug("Exception in Occured in AUTHDTL insert " +e);		
		 }
		 
		System.out.println("Authorization queue Updated.!");
		
		return output;
	}

	public String transeq()
	{
		long auditSl = 0;
		
		try
		{
			String sql = "SELECT SEQ_ATLOGSL.NEXTVAL FROM DUAL";
			auditSl = jdbc.queryForObject(sql, Integer.class); 
			
			if(auditSl != 0)
			{
				System.out.println(auditSl); 
			}
			
		} 
		catch (Exception e)
		{
			logger.debug("Error in Transeq : " + e.getLocalizedMessage());
		}
		
		return String.valueOf(auditSl);
	}
	public JsonObject User_Id_Check(String Search_Word, HttpServletRequest request) 
	{   	
		JsonObject User_Ids = new JsonObject();
		
		try
		{
			String sql = "SELECT COUNT(USERSCD) FROM USERS0001 where USERSCD = ?";                                                
			
			int count = jdbc.queryForObject(sql, new Object[] {Search_Word}, Integer.class);
			
			if(count != 0) {
				
				User_Ids.addProperty("Result", "Success");
				User_Ids.addProperty("Message", "Record  Available In Database");
				
				logger.debug("<<<<<<<<<<< User Id  Available >>>>>>>>>>>");
				
			}else {
				User_Ids.addProperty("Result", "Failed");
				User_Ids.addProperty("Message", "Record Not Available In Database");
				
				logger.debug("<<<<<<<<<<< User Id Not Available  >>>>>>>>>>>");
				
			}
			
			
		}
		catch(Exception ex)
		{
			logger.debug("Exception when Loading User_Id ::::: "+ex.getLocalizedMessage());
			User_Ids.addProperty("Result", "Failed");
			User_Ids.addProperty("Message", ex.getLocalizedMessage());
		}
		
		return User_Ids;
	}
	
	public int branchcd(String Userid, String SUBOFF)
	{
		int  BRANCD = 0 ;
		String auditsl;
		
		try
		{
			auditsl = "SELECT U.BRANCHCD FROM users0001 u WHERE U.SUBORGCODE = ? AND  u.userscd = ?";
			List<Users0001> li = jdbc.query(auditsl, new Object[] {SUBOFF,Userid}, new Users_mapper());
			
			if(li.size() != 0)
			{
				BRANCD = Integer.parseInt(li.get(0).getBRNHCD());
			}
			else
			{
				BRANCD = 0;
			}
			
		} 
		catch (Exception e)
		{
			logger.debug("Error in BRANCD : " + e.getLocalizedMessage());
		}
		
		return BRANCD;
	}
	
	public int DTSL(String Key , String tablename, String EntryMode)
	{
		int auditSl = 1;
		
		String sql;
		 int count = 0;
		try
		{
			if(!EntryMode.equalsIgnoreCase("U") && !EntryMode.equalsIgnoreCase("M"))
			{
				sql = "SELECT NVL((max(aaa.authdtl_dtl_sl) + 1),0) FROM auth003 aaa WHERE aaa.authdtl_main_pk = ? and aaa.AUTHDTL_TABLE_NAME = ?";
				count = jdbc.queryForObject(sql, new Object[] {Key,tablename},Integer.class);
				logger.debug(count);
			}
			else
			{
				sql = "SELECT NVL((max(a.authq_dtl_sl) + 1),0) FROM auth001 a WHERE a.authq_main_pk = ? and a.AUTHQ_TABLE_NAME = ?" ;
				count = jdbc.queryForObject(sql, new Object[] {Key,tablename}, Integer.class);
				logger.debug(count);
			}	
			
			if(count != 0)
			{
				auditSl = count;
				logger.debug(auditSl);
				if(!(auditSl > 0))
				{
					auditSl = 1;
				}
			}
			else
			{
				auditSl = 1;
			}
			
		} 
		catch (Exception e)
		{
			logger.debug("Error in DTSL : " + e.getLocalizedMessage());
		}
		
		return auditSl;
	}
	 
	public String getAuditLogSerial()
	{
		long auditSl = 0;
		
		try 
		{
			String sql = "SELECT SEQ_ATLOGSL.NEXTVAL FROM DUAL";
			auditSl = jdbc.queryForObject(sql, Integer.class); 
			
			if(auditSl != 0)
			{
				System.out.println(auditSl); 
			}
			
		} 
		catch (Exception e)
		{
			logger.debug("Error in getAuditLogSerial : " + e.getLocalizedMessage());
		}
		
		return String.valueOf(auditSl);
	}
	
	public JsonObject validateAdmPgmId(String Args)
	{
		String sqlQuery;
		
		JsonObject resultDTO = new JsonObject();	
		
		resultDTO.addProperty("sucFlg","0") ;
		
		try
		{
				String SqlArgs = Args;
				StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
				String domainid = parsedArgs.nextToken();	
				String userid = parsedArgs.nextToken();	
				
				logger.info(domainid);
				
				sqlQuery = "select CATEGORY FROM SYSCONF004 WHERE suborgcode = ?";
				
				List<Integer> li = jdbc.queryForList(sqlQuery, new Object[] {domainid}, Integer.class);
				
				logger.info("size ::: "+li.size());
				
				if(li.size() != 0)
				{ 
					JsonArray resultDTO2 = new JsonArray();
					
					int category = li.get(0);
					
					logger.info(category);
					if(category != 0) {
						sqlQuery="((SELECT ROLECD from USERS0001 users WHERE TRIM(USERS.suborgcode)  LIKE ? AND TRIM(USERS.USERSCD) LIKE ? ) UNION ( SELECT TRIM(ROLE_ID) FROM USERS005 WHERE TRIM(suborgcode) LIKE ? AND TRIM(USER_ID) LIKE ? AND EFF_DATE = (SELECT MAX(EFF_DATE) FROM USERS005 WHERE TRIM(suborgcode) LIKE ? AND TRIM(USER_ID) LIKE ?)))";
						
						List<String> li1 = jdbc.queryForList(sqlQuery, new Object[] {domainid,userid,domainid,userid,domainid,userid}, String.class);
						
						
						if(li1.size() != 0)
						{
							for(int i = 0;i<li1.size();i++)
							{								
								String squery = "SELECT distinct(p.pgm_id),DESCN FROM MENU003 P, menu001 M  WHERE (( P.ROLE_ID = ? AND P.EFF_DATE = (SELECT MAX(EFF_DATE) FROM MENU003 WHERE ROLE_ID = ? AND P.EFF_DATE <= SYSDATE ) ) OR ( P.ROLE_ID = ? AND P.EFF_DATE = (SELECT MAX(EFF_DATE) FROM MENU003 WHERE ROLE_ID = ? AND P.EFF_DATE <= SYSDATE ) )OR ( P.ROLE_ID = ? AND P.EFF_DATE = (SELECT MAX(EFF_DATE) FROM MENU003 WHERE ROLE_ID = ? AND P.EFF_DATE <= SYSDATE ) )) AND  P.RIGHTS_AUTH = 1 AND M.PGM_ID = P.PGM_ID AND (M.CUST_REQ = '0' or M.CUST_REQ is null) AND M.AUTH_REQUIRED = 1AND M.PGM_WIDGET=1 ORDER BY DESCN ";
								String Role = li1.get(i);
								List<PgmID> li2 = jdbc.query(squery, new Object[] {Role,Role,Role,Role,Role,Role},new PgmID_mapper());
								
								for(int index = 0; index<li2.size();index++)
								{
									JsonObject val = new JsonObject();
									logger.info(li2.get(index));
									
									val.addProperty("PGMID", li2.get(index).getPGM_ID());
									val.addProperty("DESC", li2.get(index).getDESCN());
									
									logger.info(val);
									
									resultDTO2.add(val);
								}
							}
							
							logger.debug("PGMID Array :::::::::: "+resultDTO2);
							
							resultDTO.add("PGMIDs", resultDTO2);
							
							resultDTO.addProperty("CATEGORY",category);
							
							resultDTO.addProperty("sucFlg", "1");
							
							resultDTO.addProperty("Result", "Success");
							resultDTO.addProperty("Message", "Program Id found!.");
							logger.debug("PGMID Array :::::::::: "+resultDTO);
						}
						else
						{							
							resultDTO.addProperty("Result", "Failed");
							resultDTO.addProperty("Message", "Program Id NOt found!.");
						}
					}
				}
				else
				{
					resultDTO.addProperty("Result", "Failed");
					resultDTO.addProperty("Message", "Role Code NOt found!.");
			    }
				resultDTO.addProperty("Result", "Success");
				resultDTO.addProperty("Message", "ProGram  found!.");
				
			}
			catch(Exception e)
			{
				resultDTO.addProperty("sucFlg","0") ;
				resultDTO.addProperty("Message",e.getLocalizedMessage()) ;
				logger.debug("exception occurs when loading program id "+e.getLocalizedMessage());
			}
		return resultDTO;
	}
	
	public JsonObject Get_auth001_Info(String Pgm_Id) throws ParseException 
	{
		JsonObject details = new JsonObject();
		
		try {
			logger.debug("<<<<<<<<<<< Get_auth001_Info >>>>>>>>>>>");
			String SQL = "select * from auth001 where AUTHQ_PGM_ID=?";
			List<Auth001> li = jdbc.query(SQL, new Object[] {Pgm_Id}, new Auth1_mapper());
			
			if(li.size() != 0) {
				JsonArray Infos = new JsonArray();
				for(int i = 0;i<li.size();i++) {
					JsonObject info = new JsonObject();
					logger.debug("<<<<<<<<<<< Get_auth001_Info >>>>>>>>>>>"+li.get(i).getAUTHQ_PGM_ID());
					info.addProperty("S_NO", i);
					info.addProperty("AUTHQ_PGM_ID", li.get(i).getAUTHQ_PGM_ID());
					info.addProperty("AUTHQ_MAIN_PK", li.get(i).getAUTHQ_MAIN_PK());
					info.addProperty("AUTHQ_DTL_SL", li.get(i).getAUTHQ_DTL_SL());
					info.addProperty("AUTHQ_DOMAIN", li.get(i).getAUTHQ_DOMAIN());
					info.addProperty("AUTHQ_DISPLAY_DTLS", li.get(i).getAUTHQ_DISPLAY_DTLS());
					info.addProperty("AUTHQ_TABLE_NAME", li.get(i).getAUTHQ_TABLE_NAME());
					info.addProperty("AUTHQ_OPERN_FLG", li.get(i).getAUTHQ_OPERN_FLG());
					info.addProperty("AUTHQ_DONE_BY", li.get(i).getAUTHQ_DONE_BY());
					info.addProperty("AUTHQ_DONE_BRN", li.get(i).getAUTHQ_DONE_BRN());
					info.addProperty("AUTHQ_OPERN_DATE", li.get(i).getAUTHQ_OPERN_DATE().toString());
					info.addProperty("AUTHQ_FIRST_AUTH_BY",li.get(i).getAUTHQ_FIRST_AUTH_BY());
					info.addProperty("STATUS", li.get(i).getSTATUS());
					String AUTHQ_ENTRY_DATE = li.get(i).getAUTHQ_ENTRY_DATE().toString();
					
					if(AUTHQ_ENTRY_DATE != null && !AUTHQ_ENTRY_DATE.isEmpty())
					{
						String[] parts = AUTHQ_ENTRY_DATE.split(" "); 
						
						AUTHQ_ENTRY_DATE = parts[0];
					}
					
					info.addProperty("AUTHQ_ENTRY_DATE", AUTHQ_ENTRY_DATE);
					info.addProperty("Checkbox", "");
					
					info.addProperty("View", "<a href=\"#\" onclick=\"show_details(\'"+li.get(i).getAUTHQ_MAIN_PK()+"\')\">View</a>");

					Infos.add(info);
				}
				logger.debug("<<<<<<<<<<< Get_auth001_Info >>>>>>>>>>>"+Infos);
				details.add("Info", Infos);
				details.addProperty("Result", Infos.size() != 0 ?"Success":"Failed");
				details.addProperty("Message", Infos.size() != 0 ? "Informations Found !!" : "Informations Not Found !!");
			}else {
				details.addProperty("Result", "Failed");
				details.addProperty("Message", "Database Connection Problem !!");
				
				logger.debug("<<<<<<<<<<< Database Connection Problem !! >>>>>>>>>>>");
			}
			
		}catch(Exception e) {
			details.addProperty("Result", "Failed");
			details.addProperty("Message", e.getLocalizedMessage());
		}
		return details;
	}
	
	public JsonObject Get_auth003_Info_by_pk(String pk) throws ParseException 
	{
		JsonObject details = new JsonObject();
		
		try {
			logger.debug("<<<<<<<<<<< Get_auth003_Info >>>>>>>>>>>");
			
			String SQL = "select * from auth003 where AUTHDTL_MAIN_PK = ?";
			
			List<Auth003> li = jdbc.query(SQL, new Object[] {pk}, new Auth3_mapper());
			
			logger.debug("<<<<<<<<<<< Get_auth003_Info >>>>>>>>>>> ");
			
			if(li.size() != 0) {
				boolean flag = false;
				JsonArray Infos = new JsonArray();
				for(int i = 0;i<li.size();i++) {
					
					
					JsonObject info = new JsonObject();
					
					String AUTHDTL_DATA_BLOCK = li.get(i).getAUTHDTL_DATA_BLOCK();
					if(AUTHDTL_DATA_BLOCK !=null)
					{
						String[] values = AUTHDTL_DATA_BLOCK.split("\\|");

						if(values.length >=15)
						{
							info.addProperty("element_1", values[0]);
							info.addProperty("element_2", values[1]);
							info.addProperty("element_3", values[2]);
							info.addProperty("element_4", values[3]);
							info.addProperty("element_5", values[4]);
							info.addProperty("element_6", values[5]);
							info.addProperty("element_7", values[6]);
							info.addProperty("element_8", values[7]);
							info.addProperty("element_9", values[8]);
							info.addProperty("element_10", values[9]);
							info.addProperty("element_11", values[10]);
							info.addProperty("element_12", values[11]);
							info.addProperty("element_13", values[12]);
							info.addProperty("element_14", values[13]);
							info.addProperty("element_15", values[14]);
							info.addProperty("element_16", values[15]);
							info.addProperty("element_17", values[16]);
							 
							 flag = true;
							 logger.debug("<<<<<<<<<<< Get_auth003_Info >>>>>>>>>>> ");
						}
						if(values.length <=15)
						{
							info.addProperty("element_1", values[0]);
							info.addProperty("element_2", values[1]);
							info.addProperty("element_3", values[2]);
							info.addProperty("element_4", values[3]);
							 
							 flag = true;
							 logger.debug("<<<<<<<<<<< Get_auth003_Info >>>>>>>>>>> ");
						}
					}
					logger.debug("<<<<<<<<<<< Get_auth003_Info >>>>>>>>>>> ");
					Infos.add(info);
				}
				logger.debug("<<<<<<<<<<< Get_auth003_Info >>>>>>>>>>> ");
				details.add("Info", Infos);
				details.addProperty("Result", li.size() != 0 && flag ?"Success":"Failed");
				details.addProperty("Message", li.size() != 0 && flag ? "Informations Found !!" : "Informations Not Found !!");
			}else {
				details.addProperty("Result", "Failed");
				details.addProperty("Message", "Database Connection Problem !!");
				
				logger.debug("<<<<<<<<<<< Get_auth003_Info >>>>>>>>>>>");
			}
			
		}catch(Exception e) {
			details.addProperty("Result", "Failed");
			details.addProperty("Message", e.getLocalizedMessage());
			logger.debug("Exception Occured ::::::: "+e.getLocalizedMessage());
		}
		
		return details;
	}
	
	public JsonObject Get_users001_Info_by_user_id(String user_id)
	{
		JsonObject details = new JsonObject();
		
		try 
		{
			logger.debug(">>>>>>>>> Get_users001_Info_by_user_id <<<<<<<<<<<<<");
			
			logger.debug(user_id);
			
			String SQL = "select * from USERS0001 u where u.USERSCD = ?";
			
			List<Users0001> li = jdbc.query(SQL, new Object[] {user_id}, new Users0001_mapper());
				
			logger.info(">>>>>>>>> Get_users001_Info_by_user_id <<<<<<<<<<<<<  "+user_id);
			
			if(li.size() != 0) {
				
				details.addProperty("VERIFY1", li.get(0).getVERIFY1());
				details.addProperty("VERIFY", li.get(0).getVERIFY());	
				
				logger.info("VERIFY1 :::: " +li.get(0).getVERIFY1());
				logger.info("VERIFY :::: " +li.get(0).getVERIFY());
				
				details.addProperty("Result", li.size() != 0  ? "Success" : "Failed");
				details.addProperty("Message", li.size() != 0 ? "Information Found !!" : "Information Not Found !!");
			}else
			{
				details.addProperty("Result", "Failed");
				details.addProperty("Message", "Database Connection Problem !!");
				
				logger.debug(">>>>>>>>> Database Connection Problem !! <<<<<<<<<<<<<");
			}
			
		}
		catch (Exception e) 
		{
			details.addProperty("Result", "Failed");
			details.addProperty("Message", e.getLocalizedMessage());
			
			logger.debug("Exception Occured ::::: "+e.getLocalizedMessage());
		}
		
		return details;
	}
	
	
	public JsonObject updateValues(MultipartFile File, JsonObject Inputs) throws SQLException, IOException 
	{
		JsonObject resultDTO = new JsonObject();
		
		
		final DateFormat formatDate = new SimpleDateFormat("dd-MMM-yyyy");
		
		final Date cudate = new Date();	
		
		File convFile = ConvertMultipartFileToFile(File);
		
		
		
		final String userId = Inputs.get("hiduserId").getAsString();
		
		final String hiddomainId = Inputs.get("hiddomainId").getAsString();
		
		try
		{
			final FileInputStream ins = new FileInputStream(convFile);
			
			String sql ="select * from users014 where SUBORGCODE = ? and USERID = ?";
			List<Users014> li = jdbc.query(sql, new Object[] {hiddomainId,userId}, new Users14_mapper());
			
			String mode = "A";
			
			if(li.size() != 0){
				mode = "U"; 
						
			}else{
				mode = "A";
			}
			
			logger.debug(mode+userId+hiddomainId+ins+formatDate.format(cudate));
			
			if(mode.equalsIgnoreCase("A"))
			{
			String strquery = "insert into users014(SUBORGCODE,USERID,PHOTO,UPLDTIME) values(?,?,?,?)";
				int li1 = jdbc.execute(strquery, new PreparedStatementCallback<Integer>() {

					public Integer doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ps.setString(1, hiddomainId);
						ps.setString(2, userId);
						try {
							ps.setBinaryStream(3, ins, ins.available());
						} catch (Exception e) {
							logger.debug(e.getLocalizedMessage());
						}
						ps.setString(4, formatDate.format(cudate));
						return ps.executeUpdate();
					}
				});
				
				if(li1 != 0) {
					resultDTO.addProperty("sucFlag","1") ;
					resultDTO.addProperty("result","Image Uploaded Successfully") ;
					logger.debug(">>>>>>>>>>> Image Inserted Successfully <<<<<<<<<<<<<<<");
				}
				
			}
			else
			{
				String strquery = "update users014 set PHOTO =?, UPLDTIME=? where SUBORGCODE=? and USERID=?";
				int li1 = jdbc.execute(strquery, new PreparedStatementCallback<Integer>() {

					public Integer doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						try {
							ps.setBinaryStream(1, ins, ins.available());
						} catch (Exception e) {
							logger.debug(e.getLocalizedMessage());
						}
						ps.setString(2, formatDate.format(cudate));
						ps.setString(3, hiddomainId);
						ps.setString(4, userId);
						
						return ps.executeUpdate();
					}
				});
				
				if(li1 != 0) {
					resultDTO.addProperty("sucFlag","1") ;
					resultDTO.addProperty("result","Image Uploaded Successfully") ;
					logger.debug(">>>>>>>>>>> Image Uploaded Successfully <<<<<<<<<<<<<<<");
				}
				
			}
			
			convFile.delete();
		}
		catch(Exception e)
		{
			resultDTO.addProperty("sucFlag","0") ;
			resultDTO.addProperty("result",e.getLocalizedMessage()) ;
			
			logger.debug("Exception in Image Upload:::::::: "+e.getLocalizedMessage());
		}
	
		return resultDTO;
	}
	
	public JsonObject updateValues_Password(JsonObject formDTO, HttpSession session) throws SQLException, IOException
	{
		JsonObject resultDTO = new JsonObject();
	
		final String custid = formDTO.get("torgcd").getAsString();
		final String userid = formDTO.get("tuserid").getAsString();
		String pwd = formDTO.get("hashedPassword").getAsString().trim();
		
		try
		{
			
			String randomSalt = formDTO.get("randomSalt").getAsString();
			
			System.out.println(randomSalt);
			
			EncryptDecrypt decryption = new EncryptDecrypt();
			
			pwd = decryption.doDecrypt(pwd, randomSalt);
			
			System.out.println(pwd);
			
			logger.debug("Random salt -------->" + randomSalt);
			
			logger.debug("pwd---------------->" + pwd);
			
			logger.debug("userid---------------->" + userid);
			
			String final_salt = pu.getSalt(userid);
		

			logger.debug("final_salt---------------->" + final_salt);
			
			if(final_salt == null) final_salt = "";
			
			final_salt = "";
			
			final String hashedpwd = PasswordUtils.getEncrypted(pwd, userid, final_salt);
			
			System.out.println(hashedpwd);
			
			logger.debug("Hashed Password ---------->"+hashedpwd);
			
			final String m_CallStmt = "call SP_UPDPWD_GEN(?,?,?,?)";
			
			Map<String, Object> resultMap = jdbc.call(new CallableStatementCreator() {
				
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(m_CallStmt);
					
					cs.setString(1, custid);
					cs.setString(2, userid);
					cs.setString(3, hashedpwd);
					cs.registerOutParameter(4, Types.VARCHAR);
					
					return cs;
				}
			}, SP_UPDPWD_GEN_params());
			
			
			if(resultMap.get("ERRORMSG").equals("S"))
			{
				resultDTO.addProperty("sucFlg","1") ;
				
				logger.debug(">>>>>>>>>> Password Updated Successfully >>>>>>>>>>>>>>");
			}
		} 
		catch (Exception e) 
		{
			resultDTO.addProperty("sucFlg","0") ;
			
			logger.debug("Exception in Password Updation ::::::: "+e.getLocalizedMessage());
		}
		
		return resultDTO;		
	}
	
	public String loadImage(String SUBORGCODE, String USERID, HttpServletRequest request)
	{
		
		String Photo = request.getContextPath()+"/resources/H2H/img/Default_Profile.jpg";
	    
		try
		{
			String query = "SELECT PHOTO FROM USERS014 where SUBORGCODE=? and USERID=?";
			List<byte[]> li = jdbc.queryForList(query, new Object[] {SUBORGCODE,USERID}, byte[].class);
			
			if(li.size() != 0)
			{
		 	 	String encode = Base64.encodeBase64String(li.get(0));
		 	 			
		 	 	Photo = "data:image/jpeg;base64,"+encode;
		 	 	logger.info(Photo);
			} 
		}
		catch(Exception ex)
		{
			logger.debug("Exception when Loading Profile Image :::::"+ex.getLocalizedMessage());
		}
		return Photo;
	}
	
	public JsonArray User_Id_Suggestions(String Search_Word, HttpServletRequest request) 
	{   	
		JsonArray User_Ids = new JsonArray();
		
		try
		{
			String sql = "SELECT USERSCD FROM USERS0001 where USERSCD LIKE upper(?) or USERSCD LIKE lower(?)";                                                
			
			List<String> li = jdbc.queryForList(sql,new Object[] {"%"+Search_Word+"%","%"+Search_Word+"%"},String.class);
			
			for(int i=0;i<li.size();i++)
			{
				JsonObject Branch_Code = new JsonObject();
				
				Branch_Code.addProperty("label", li.get(i));
				User_Ids.add(Branch_Code); 	
				
			}
		}
		catch(Exception ex)
		{
			logger.debug("Exception when Loading User_Id ::::: "+ex.getLocalizedMessage());
		}
		
		return User_Ids;
	}
	
	public JsonArray Org_Code_Suggestions(String Search_Word, HttpServletRequest request) 
	{   	
		JsonArray Org_Codes = new JsonArray();
		
		Search_Word = Search_Word.trim();
		
		try
		{
			String query = "SELECT SUBORGCODE FROM SYSCONF004 where SUBORGCODE LIKE upper(?) or SUBORGCODE LIKE lower(?)";                                                
			
			List<String> li = jdbc.queryForList(query, new Object[] {"%"+Search_Word+"%"}, String.class);
			
			for(int i=0; i<li.size();i++) {
				JsonObject Org_Code = new JsonObject();
				
				Org_Code.addProperty("label", li.get(i));
				
				Org_Codes.add(Org_Code);
			}
		}
		catch(Exception ex)
		{
			logger.debug("Exception when Loading ORG_Code ::::: "+ex.getLocalizedMessage());
		}
		
		 return Org_Codes;	 
	}
	
	public JsonArray Branch_Code_suggestion(String Search_Word) 
	{   	
		JsonArray Branch_Codes = new JsonArray();
		
		try
		{
			String query = "SELECT NAME,SUBOFFCD FROM SUBOFF001 where SUBOFFCD LIKE upper(?) or SUBOFFCD LIKE lower(?) or SUBOFFCD LIKE ? or NAME LIKE upper(?) or NAME LIKE lower(?) or  NAME LIKE ?";                                                
			
			List<Suboffcd> li = jdbc.query(query, new Object[] {"%"+Search_Word+"%" ,"%"+Search_Word+"%","%"+Search_Word+"%","%"+Search_Word+"%","%"+Search_Word+"%","%"+Search_Word+"%"}, new Suboffcd_mapper());
			
			for(int i = 0 ; i<li.size();i++) {
				JsonObject Branch_Code = new JsonObject();
				
				Branch_Code.addProperty("id", li.get(i).getNAME());
				Branch_Code.addProperty("label", li.get(i).getSUBOFFCD());
				
				Branch_Codes.add(Branch_Code); 	
			}
		}
		catch(Exception ex)
		{
			logger.debug("Exception when Loading Branch_Code :::::"+ex.getLocalizedMessage());
		}
		
		 return Branch_Codes;	 
	}
	
	public JsonObject All_Branch_Code() 
	{   	
		JsonObject details = new JsonObject();
		try{
			
			String sql = "SELECT NAME,SUBOFFCD FROM SUBOFF001 order by SUBOFFCD+0 asc";
			
			List<Suboffcd> li = jdbc.query(sql, new Suboffcd_mapper());
			
			JsonArray Branch_Codes = new JsonArray();
			
			
			
			for(int i = 0;i<li.size();i++) 
			{
				JsonObject Branch_Code2 = new JsonObject();
				
				Branch_Code2.addProperty("id", li.get(i).getNAME());
				Branch_Code2.addProperty("label", li.get(i).getSUBOFFCD());
				
				Branch_Codes.add(Branch_Code2);
				
			}
			
			logger.debug(Branch_Codes);
			details.add("Branch_Codes", Branch_Codes);
			
			details.addProperty("Result", li.size() != 0 ? "Success" : "Failed");
			details.addProperty("Message", li.size() != 0 ? "Branch_Codes found" : "Branch not found");
			
		}catch(Exception ex){
			
			logger.debug("Exception when Loading Branch_Code :::::"+ex.getLocalizedMessage());
			
			details.addProperty("Result", "Failed");
			details.addProperty("Message", ex.getLocalizedMessage());
		}
		
		 return details;	 
	}
	
	public JsonArray Role_Code(String Search_Word, HttpServletRequest request) 
	{   	
		JsonArray Role_Codes = new JsonArray();
		
		try
		{
			String query = "SELECT ROLE_ID,REMARKS FROM MENU002 where ROLE_ID LIKE upper(?) or ROLE_ID LIKE lower(?)";                                                
			
			List<Menu_002> li = jdbc.query(query, new Object[] {"%"+Search_Word+"%"}, new Menu002_1_mapper());
			
			for(int i = 0;i<li.size();i++) {
				JsonObject Role_Code = new JsonObject();
				
				Role_Code.addProperty("id", li.get(i).getROLE_ID());
				Role_Code.addProperty("label", li.get(i).getREMARKS());
				
				Role_Codes.add(Role_Code); 
			}
		}
		catch(Exception ex)
		{
			logger.debug("Exception when Loading Role_Code :::::"+ex.getLocalizedMessage());
		}
		 return Role_Codes;	 
	}
	
	 public JsonObject updateValues_for_UnblockUser(JsonObject formDTO)
	 {
			String main_pk = null;
			String allvalues = null;
			
			String transeq = transeq();
			
			String mode = formDTO.get("mode").getAsString();
			
			//String suborgcode = formDTO.get("suborgcode");
			
			String sesDomainID = formDTO.get("sesDomainID").getAsString();
			
			String tuserid = formDTO.get("tuserid").getAsString();
			
			String block = formDTO.get("tbublock").getAsString();
		
			String sesMcontDate = formDTO.get("sesMcontDate").getAsString();
		
			main_pk = sesDomainID +"|"+tuserid;
		
			sesMcontDate = FormatUtils.dynaSQLDate(sesMcontDate,"DD-MM-YYYY"); 
			
			logger.debug("sesMcontDate ::::: "+sesMcontDate);
	
			allvalues = main_pk+"|"+block+"|"+"Update by " +
		        		formDTO.get("sesUserId").getAsString()+"|";
			 		
			long Blocksl = 1L;
			
			JsonObject input_values = new JsonObject();
			
			input_values.addProperty("Primarykey", main_pk);
			input_values.addProperty("Domain", formDTO.get("sesDomainID").getAsString());
			input_values.addProperty("EntryMode", mode);
			input_values.addProperty("DoneBy", formDTO.get("sesUserId").getAsString());
			
			input_values.addProperty("Table", "USERS002");
			input_values.addProperty("DataBlock", allvalues);
			input_values.addProperty("Blocksl", String.valueOf(Blocksl));
			input_values.addProperty("PgmId", "unblockuserid");
			input_values.addProperty("userIp", formDTO.get("sesUserId").getAsString());  
			input_values.addProperty("SEQNO", transeq);

			logger.debug("<<<<<<<<<<< Calling AuthEntry >>>>>>>>>>>");
			
			JsonObject resultDTO = authEntry(input_values);
			String val = resultDTO.get("sucFlg").getAsString(); 
			if(val.equals("1")) 
			{
				resultDTO.addProperty("Transaction", transeq);
				
				resultDTO.addProperty("Result", "Success");
				resultDTO.addProperty("Message", "Record Updated Successfully");
				
				logger.debug("<<<<<<<<<<< AuthEntry for unblock user Id Success >>>>>>>>>>>");
			} 
			else
			{
				resultDTO.addProperty("Result", "Failed");
				resultDTO.addProperty("Message", "Record Not Updated");
				
				logger.debug("<<<<<<<<<<< AuthEntry for unblock user Id Failed >>>>>>>>>>>");
			}
			
			return resultDTO;
		}
	 
	 
	 public JsonObject QUEUECHECKER(JsonObject formDTO) 
	 {
		 JsonObject resultDTO = new JsonObject();
			
			int queuepresent = 0;
			
			int maintable = 0;
			
			
			try 
			{
				String sqlQuery = "SELECT aa.authdtl_data_block FROM auth003 aa , AUTH001 A  " +
								  "WHERE UPPER(A.AUTHQ_PGM_ID) = ? AND A.AUTHQ_MAIN_PK = AA.AUTHDTL_MAIN_PK  AND A.AUTHQ_ENTRY_DATE = AA.AUTHDTL_ENTRY_DATE" +
								  " AND aa.authdtl_main_pk = ? AND aa.authdtl_table_name = 'USERS002'";
				
				List<String> li = jdbc.queryForList(sqlQuery, new Object[] {formDTO.get("pgmID").getAsString(),formDTO.get("torgcd").getAsString()+"|"+formDTO.get("tuserid").getAsString()}, String.class);
				
				
				if(li.size() != 0)
				{
				    queuepresent = 1; 
					String data = li.get(0);
					String resvalue[] = new String[30];
					String[] tokens1 = data.split("\\|");
					
					int y = 0;
					
				    for(String token: tokens1)
				    {
				    	String[] tk = token.split("\\|") ;
				    	
				        for(String splidata: tk)
				        {
				        	resvalue[y] = splidata;
				        	y++;
				        }	
				    }
				   
					    resultDTO.addProperty("mode", "U");
						resultDTO.addProperty("STATUS", resvalue[2]);
						resultDTO.addProperty("SucFlg","1");
						
						logger.debug(resultDTO);
	       			}
	       	
					sqlQuery = "SELECT U.STATUS_FLAG FROM USERS002 U  WHERE U.SUBORGCODE = ? AND U.USER_ID = ?";
					li = jdbc.queryForList(sqlQuery, new Object[] {formDTO.get("torgcd").getAsString(),formDTO.get("tuserid").getAsString()}, String.class);
					
					
					if (li.size() != 0)
					{						
						maintable = 1;
						
						resultDTO.addProperty("mode", "M");
						
						if( queuepresent == 0) 
						{
							resultDTO.addProperty("STATUS", li.get(0));
						}
						
						resultDTO.addProperty("SucFlg","1");							
					} 
					else 
					{
						if(queuepresent == 1)
						{
							resultDTO.addProperty("SucFlg", "1");
						}
						else
						{
							resultDTO.addProperty("SucFlg", "0");
       					} 					
       				}
					
				
				if(maintable == 0 &&  queuepresent == 0)
				{
					resultDTO.addProperty("mode", "A");
				}
				else if(maintable == 0 &&  queuepresent == 1)
				{	
					resultDTO.addProperty("mode", "U");
				}
				else if(maintable == 1 &&  queuepresent == 0)
				{	
					resultDTO.addProperty("mode", "I");
				}
				else if(maintable == 1 &&  queuepresent == 1)
				{	
					resultDTO.addProperty("mode", "M");
				}
	       			
			} 
			catch (Exception e) 
			{
				logger.debug("Users Status Queue: " + e.getLocalizedMessage());
				resultDTO.addProperty("SucFlg", "0");
			}
			return resultDTO;
		}	
	 
	 public JsonObject QUEUECHECKER2(JsonObject formDTO) 
	 {
		 JsonObject resultDTO = new JsonObject();
			
			
			
			try 
			{
				String sqlQuery = "SELECT UPWDINV_USER_ID from USERS012 where UPWDINV_USER_ID = ?";
				
				List<String> li = jdbc.queryForList(sqlQuery, new Object[] {formDTO.get("tuserid").getAsString()}, String.class);
				
				
				if(li.size() != 0)
				{				    				   
				    resultDTO.addProperty("STATUS", "Blocked");
					resultDTO.addProperty("SucFlg","1");
	       		}
				
				else
				{
					resultDTO.addProperty("STATUS", "UnBlocked");
					resultDTO.addProperty("SucFlg", "1");
				}			
			} 
			catch (Exception e) 
			{
				logger.debug("Users Status Queue:" + e.getLocalizedMessage());
				resultDTO.addProperty("SucFlg", "0");
				return resultDTO;
			}
			return resultDTO;
		}
	
	public File ConvertMultipartFileToFile(MultipartFile file) throws IllegalStateException, IOException
	{
		String File_Name = file.getOriginalFilename();
	 
	 	//File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());
	 
	 	File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+File_Name);
	  
	 	file.transferTo(convFile);
	  
	 	return convFile;
	}
	
	public class Suboffcd_mapper implements RowMapper<Suboffcd>{

		public Suboffcd mapRow(ResultSet rs, int rowNum) throws SQLException {
			Suboffcd sb = new Suboffcd();
			
			/*sb.setADATE(rs.getDate("ADATE"));
			sb.setADDRESS(rs.getString("ADDRESS"));
			sb.setAUSER(rs.getString("AUSER"));
			sb.setCDATE(rs.getDate("CDATE"));
			sb.setCUSER(rs.getString("CUSER"));
			sb.setEDATE(rs.getDate("EDATE"));
			sb.setEMAIL(rs.getString("EMAIL"));
			sb.setEUSER(rs.getString("EUSER"));
			sb.setFAX(rs.getString("FAX"));*/
			sb.setNAME(rs.getString("NAME"));
			//sb.setOPENDATE(rs.getDate("OPENDATE"));
			//sb.setSTATUS(rs.getInt("STATUS"));
			sb.setSUBOFFCD(rs.getString("SUBOFFCD"));
			//sb.setSUBORGCODE(rs.getString("SUBORGCODE"));
			//sb.setTELEPHONE(rs.getString("TELEPHONE"));
			
			return sb;
		}
		
	}
	public class Users_mapper implements RowMapper<Users0001>{

		public Users0001 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Users0001 u1 = new Users0001();
			
			u1.setBRNHCD(rs.getString("BRANCHCD"));
			
			return u1;
		}
		
	}
	
	public class Auth1_mapper implements RowMapper<Auth001>{

		public Auth001 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Auth001 a1 = new Auth001();
			
			/**
			 * AUTHQ_PGM_ID
AUTHQ_MAIN_PK
AUTHQ_ENTRY_DATE
AUTHQ_DTL_SL
AUTHQ_DOMAIN
AUTHQ_DISPLAY_DTLS
AUTHQ_TABLE_NAME
AUTHQ_OPERN_FLG
AUTHQ_DONE_BY
AUTHQ_DONE_BRN
AUTHQ_OPERN_DATE
AUTHQ_FIRST_AUTH_BY
STATUS

			 * 
			 * 
			 */
			
			a1.setAUTHQ_DISPLAY_DTLS(rs.getString("AUTHQ_DISPLAY_DTLS"));
			a1.setAUTHQ_DOMAIN(rs.getString("AUTHQ_DOMAIN"));
			a1.setAUTHQ_DONE_BRN(rs.getInt("AUTHQ_DONE_BRN"));
			a1.setAUTHQ_DONE_BY(rs.getString("AUTHQ_DONE_BY"));
			a1.setAUTHQ_DTL_SL(rs.getInt("AUTHQ_DTL_SL"));
			a1.setAUTHQ_ENTRY_DATE(rs.getDate("AUTHQ_ENTRY_DATE"));
			a1.setAUTHQ_FIRST_AUTH_BY(rs.getString("AUTHQ_FIRST_AUTH_BY"));
			a1.setAUTHQ_MAIN_PK(rs.getString("AUTHQ_MAIN_PK"));
			a1.setAUTHQ_OPERN_DATE(rs.getDate("AUTHQ_OPERN_DATE"));
			a1.setAUTHQ_OPERN_FLG(rs.getString("AUTHQ_OPERN_FLG"));
			a1.setAUTHQ_PGM_ID(rs.getString("AUTHQ_PGM_ID"));
			a1.setAUTHQ_TABLE_NAME(rs.getString("AUTHQ_TABLE_NAME"));
			a1.setSTATUS(rs.getString("STATUS"));
			
			return a1;
		}
		
	}
	
	public class Auth3_mapper implements RowMapper<Auth003>{

		public Auth003 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Auth003 a3 = new Auth003();
			
			a3.setAUTHDTL_BLOCK_SL(rs.getInt("AUTHDTL_BLOCK_SL"));
			a3.setAUTHDTL_DATA_BLOCK(rs.getString("AUTHDTL_DATA_BLOCK"));
			a3.setAUTHDTL_DOMAIN(rs.getString("AUTHDTL_DOMAIN"));
			a3.setAUTHDTL_DTL_SL(rs.getInt("AUTHDTL_DTL_SL"));
			a3.setAUTHDTL_ENTRY_DATE(rs.getDate("AUTHDTL_ENTRY_DATE"));
			a3.setAUTHDTL_MAIN_PK(rs.getString("AUTHDTL_MAIN_PK"));
			a3.setAUTHDTL_TABLE_NAME(rs.getString("AUTHDTL_TABLE_NAME"));
			a3.setAUTHDTL_TABLE_TYPE(rs.getString("AUTHDTL_TABLE_TYPE"));
			
			return a3;
		}
		
	}
	
	public class Users0001_mapper implements RowMapper<Users0001>{

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
	
	public class Users14_mapper implements RowMapper<Users014>{

		public Users014 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Users014 u14 = new Users014();
			
			u14.setSUBORGCODE(rs.getString("SUBORGCODE"));
			u14.setUSERID(rs.getString("USERID"));
			u14.setPHOTO(rs.getBytes("PHOTO"));
			u14.setUPLDTIME(rs.getDate("UPLDTIME"));
			
			return u14;
		}
		
	}
	public class Users14_1_mapper implements RowMapper<Users014>{

		public Users014 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Users014 u14 = new Users014();
			
			u14.setPHOTO(rs.getBytes("PHOTO"));
			
			return u14;
		}
		
	}
	public class Menu002_1_mapper implements RowMapper<Menu_002>{

		public Menu_002 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Menu_002 m2 = new Menu_002();
			
			m2.setROLE_ID(rs.getString("ROLE_ID"));
			m2.setREMARKS(rs.getString("REMARKS"));
			
			return m2;
		}
		
	}
	
	public class PgmID_mapper implements RowMapper<PgmID>{

		public PgmID mapRow(ResultSet rs, int rowNum) throws SQLException {
			PgmID pid = new PgmID();
			
			pid.setPGM_ID(rs.getString("PGM_ID"));
			pid.setDESCN(rs.getString("DESCN"));
			
			return pid;
		}
		
	}
	
	public class Menu001_mapper implements RowMapper<Menu001>{

		public Menu001 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Menu001 m1 = new Menu001();
			
			m1.setPGM_ID(rs.getString("PGM_ID"));
			m1.setDESCN(rs.getString("DESCN"));
			
			return m1;
		}
		
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
	private static String File_to_Base64(File file) throws IOException 
	{
	    String encodedBase64 = "";
	  
	    try 
	    {
	    	
	    	 byte[] fileContent = FileUtils.readFileToByteArray(file);

	         encodedBase64 = java.util.Base64.getEncoder().encodeToString(fileContent);
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    return encodedBase64;
	}
}
