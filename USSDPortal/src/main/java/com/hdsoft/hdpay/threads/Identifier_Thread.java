package com.hdsoft.hdpay.threads;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.Repositories.Identifiers001;
import com.hdsoft.hdpay.models.TIPS_Modal;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Identifier_Thread implements Runnable
{
	 public JdbcTemplate Jdbctemplate;

	 @Autowired
	 public void setJdbctemplate(HikariDataSource Datasource) 
	 {
		Jdbctemplate = new JdbcTemplate(Datasource);
	 }
	 
	 @Autowired
	 public TIPS_Modal TIPS;
	 
	 private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
		
	 public void run() 
	 {
		logger.debug(">>>> EXIMPAY IDENTIFIER THREAD HAS BEEN STARTED  <<<<<<");
		
		try 
		{
			while(true)
			{   	
				 List<Identifiers001> All_Identifiers = Get_Threads();
				 
				 for(int i=0; i<All_Identifiers.size(); i++)
			     {
					 JsonObject details = new JsonObject();
					 
					 JsonObject Update_details = Update_Identifier(All_Identifiers.get(i));
					 
					 details.addProperty("Update_identifier001", Update_details.toString());
					  
					 JsonObject Out_details = Method_Finder(All_Identifiers.get(i)); 	 /*** Need to check the PAYTYPE such as GEPG, TRA, DERMOLOG etc. ***/
		             
		             details.addProperty("Method_Finder", Out_details.toString());
		             	 
		             JsonObject Del_details = Delete_Identifier(All_Identifiers.get(i));
		            	 
		             details.addProperty("Delete_Job005", Del_details.toString());   
			      }
				 
	              String FREQINSEC = "1800"; /** 30 Mins ***/
  
	              long sleep_time = 1000L * Long.valueOf(FREQINSEC).longValue();  
	            
	        	  Thread.sleep(sleep_time);
			}
		}
		catch (Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage());
		}
    }
	 
	public JsonObject Method_Finder(Identifiers001 Job) 
	{
		 JsonObject details = new JsonObject();
		 
		 try 
		 {
			 boolean Status = false;
			 
			 String PAYTYPE = Job.getPAYTYPE();
			 
			 if(PAYTYPE.equals("TIPS"))  
	         {
				  details = TIPS.Register_Identifier(Job);

	     	      Status = true;
	     	 }
			
			 details.addProperty("Result", Status ? "Success" : "Failed");
		     details.addProperty("Message", Status ? "Payment Gateway Called Successfuly !!" : "Payment Gateway not Called !!");
		 }
		 catch(Exception e) 
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());
			 
			 logger.debug("Exception :::: "+e.getLocalizedMessage());
		 } 
		 
		 return details;
	}
	
	public JsonObject Update_Identifier(Identifiers001 Job)
	{
		JsonObject details =  new JsonObject();
		
		try 
		{
			 String Sql = "update identifiers001 set STATUS=? where SUBORGCODE=? and SYSCODE=? and PAYTYPE=? and CHCODE=? and REQUESTID=? and REQSL=? and APPROVED=?";
			
			 int status = Jdbctemplate.update(Sql, new Object[] { "U", Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getPAYTYPE(), Job.getCHCODE(), Job.getREQUESTID(), Job.getREQSL(), Job.getAPPROVED() });
			 
			 details.addProperty("Result", status == 1 ? "Success" : "Failed");
		     details.addProperty("Message", status == 1 ? "Record Updated Successfuly !!" : "Record not Updated !!");
		}
		catch(Exception e) 
		{
			details.addProperty("Result", "Failed");
			details.addProperty("Message", e.getLocalizedMessage());
			
			logger.debug("Exception :::: "+e.getLocalizedMessage());
		} 
		
		return details;
	}
	
	public JsonObject Delete_Identifier(Identifiers001 Job)
	{
		JsonObject details =  new JsonObject();
		
		try 
		{
			String Sql =  "Delete from identifiers001 where SUBORGCODE=? and SYSCODE=? and PAYTYPE=? and CHCODE=? and REQUESTID=? and REQSL=? and STATUS=? and APPROVED=?";
			
			int status = Jdbctemplate.update(Sql, new Object[] { Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getPAYTYPE(), Job.getCHCODE(), Job.getREQUESTID(), Job.getREQSL(), "U", Job.getAPPROVED() });	
			
			details.addProperty("Result", status == 1 ? "Success" : "Failed");
			details.addProperty("Message", status == 1 ? "Record Deleted from Job005 Successfuly !!" : "Record Not Deleted !!");
		}
		catch (Exception e) 
		{
			details.addProperty("Result", "Failed");
			details.addProperty("Message", e.getLocalizedMessage());
			
			logger.debug("Exception :::: "+e.getLocalizedMessage());
		} 
		
		return details;
	}
	
	public List<Identifiers001> Get_Threads()
	{
		List<Identifiers001> Threads = new ArrayList<Identifiers001>();

		try 
		{
			 String Sql = "Select * from identifiers001 where SUBORGCODE=? and SYSCODE=? and STATUS=? and APPROVED=? and ROWNUM=?";
			
			 Threads = Jdbctemplate.query(Sql, new Object[] { "EXIM", "HP", "Q", "1", 1 }, new Identifiers001_Mapper());
		}
		catch(Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage());
		} 
		
		return Threads;
	 }

     public class Identifiers001_Mapper implements RowMapper<Identifiers001> 
     {
     	Common_Utils util = new Common_Utils();
     	
 		public Identifiers001 mapRow(ResultSet rs, int rowNum) throws SQLException
 		{
 			Identifiers001 Info = new Identifiers001();  
 			
 			Info.setSERIAL(rowNum+1);
 			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));        
 			Info.setSYSCODE(util.ReplaceNull(rs.getString("SYSCODE")));        
 			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));        
 			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));        
 			Info.setREQUESTID(util.ReplaceNull(rs.getString("REQUESTID")));    
 			Info.setREQSL(util.ReplaceNull(rs.getString("REQSL")));
 			Info.setREQUESTDATE(util.ReplaceNull(rs.getString("REQUESTDATE")));            
 			Info.setFSP_SRCID(util.ReplaceNull(rs.getString("FSP_SRCID")));               
 			Info.setFSP_DESID(util.ReplaceNull(rs.getString("FSP_DESID")));        
 			Info.setIDENTIFIERNO(util.ReplaceNull(rs.getString("IDENTIFIERNO")));         
 			Info.setIDENTIFIERNAME(util.ReplaceNull(rs.getString("IDENTIFIERNAME")));        
 			Info.setSUBIDENT(util.ReplaceNull(rs.getString("SUBIDENT")));       		    
 			Info.setIDENTIFIERTYPE(util.ReplaceNull(rs.getString("IDENTIFIERTYPE"))); 
 			Info.setFSPID(util.ReplaceNull(rs.getString("FSPID")));         
 			Info.setIDENTIFIERNAME(util.ReplaceNull(rs.getString("IDENTIFIERNAME")));         
 			Info.setACCAT(util.ReplaceNull(rs.getString("ACCAT")));       
 			Info.setACTYPE(util.ReplaceNull(rs.getString("ACTYPE")));      
 			Info.setACCOUNTNO(util.ReplaceNull(rs.getString("ACCOUNTNO")));
 			Info.setCURRENCY(util.ReplaceNull(rs.getString("CURRENCY")));
 			Info.setCUSTNO(util.ReplaceNull(rs.getString("CUSTNO")));          
 			Info.setMSISDN(util.ReplaceNull(rs.getString("MSISDN")));           
 			Info.setEMAIL(util.ReplaceNull(rs.getString("EMAIL")));     
 			Info.setIDTYPE1(util.ReplaceNull(rs.getString("IDTYPE1"))); 
 			Info.setIDTYPEVALUE1(util.ReplaceNull(rs.getString("IDTYPEVALUE1"))); 
 			Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));  
 			Info.setREASON(util.ReplaceNull(rs.getString("REASON")));
			Info.setVALID(util.ReplaceNull(rs.getString("VALID")));
 			Info.setAPPROVED(util.ReplaceNull(rs.getString("APPROVED"))); 
 			
 			return Info;
 		}
     }
}
