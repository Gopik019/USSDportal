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
import com.hdsoft.hdpay.Repositories.Job_005;
import com.hdsoft.hdpay.models.GEPG_Modal;
import com.hdsoft.hdpay.models.Outward_Modal;
import com.hdsoft.hdpay.models.TIPS_Modal;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Payment_Thread implements Runnable
{
	 public JdbcTemplate Jdbctemplate;

	 @Autowired
	 public void setJdbctemplate(HikariDataSource Datasource) 
	 {
		Jdbctemplate = new JdbcTemplate(Datasource);
	 }
	 
	 @Autowired
	 public Outward_Modal Modal;
	 
	 @Autowired
	 public GEPG_Modal GEPG;
	 
	 @Autowired
	 public TIPS_Modal TIPS;
	 
	 private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
		
	 public void run() 
	 {
		logger.debug(">>>> EXIMPAY PAYMENT THREAD HAS BEEN STARTED  <<<<<<");
		
		try 
		{
			while(true)
			{   	
				 List<Job_005> All_Jobs = Get_Threads();
				 
				 for(int i=0; i<All_Jobs.size(); i++)
			     {
					 JsonObject details = new JsonObject();
					 
					 JsonObject Update_details = Update_Job005(All_Jobs.get(i));
					 
					 details.addProperty("Update_Job005", Update_details.toString());
					  
					 JsonObject Out_details = Method_Finder(All_Jobs.get(i)); 	 /*** Need to check the PAYTYPE such as GEPG, TRA, DERMOLOG etc. ***/
		             
		             details.addProperty("Method_Finder", Out_details.toString());
		             
		             logger.debug("Method_Finder out ::: "+Out_details);
		                         	 
		             if(Out_details.get("Result").getAsString().equals("Success"))
		     	     {
		            	 JsonObject Del_details = Delete_Job005(All_Jobs.get(i));
		            	 
		            	 details.addProperty("Delete_Job005", Del_details.toString());
		            	 
		            	 logger.debug("Delete_Job005 out ::: "+Del_details);       
		     	     }
		             
		             logger.debug("Dedictated out ::: "+details);   
			     }
				 
	            String FREQINSEC = "5";  /** 5 Seconds ***/
  
	            long sleep_time = 1000L * Long.valueOf(FREQINSEC).longValue();  
	            
	        	Thread.sleep(sleep_time);
			}
		}
		catch (Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage());
		}
    }
	 
	public JsonObject Method_Finder(Job_005 Job) 
	{
		 JsonObject details = new JsonObject();
		 
		 try 
		 {
			 boolean Status = false;
			 
			 String PAYTYPE = Job.getPAYTYPE().toUpperCase();
			 
			 if(PAYTYPE.equals("GEPG"))  
	         {
				 logger.debug("EXECMETHOD is GEPG");
	     	  
	     	     details = GEPG.GEPG_Executer(Job);
	     	    
	     	     Status = true;
	     	 }
			 
			 if(PAYTYPE.equals("TIPS"))  
	         {
				 logger.debug("EXECMETHOD is TIPS");
				 
				 String TRANTYPE = Job.getTRANTYPE().toUpperCase();
				 
				 if(TRANTYPE.equals("TRANSFER"))
				 {
					 details = TIPS.TIPS_Payment_Executer(Job);
				 }
				 
				 if(TRANTYPE.equals("REVERSAL"))
				 {
					 details = TIPS.TIPS_Payment_Reversal_Executer(Job);
				 }
	     	  
	     	     Status = true;
	     	 }
			 
			 if(PAYTYPE.equals("TRA"))  
	         {
				 logger.debug("EXECMETHOD is TRA");
	     	    
	     	     Status = true;
	     	 }
			 
			 if(PAYTYPE.equals("MSC"))  
	         {
				 logger.debug("EXECMETHOD is MSC");
	     	    
	     	     Status = true;
	     	 }
			 
			 details.addProperty("Result", Status ? "Success" : "Failed");
		     details.addProperty("Message", Status ? "Payment Gateway Called Successfuly !!" : "Payment Gateway not Called !!");
		 }
		 catch (Exception e) 
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());
			 
			 logger.debug("Exception :::: "+e.getLocalizedMessage());
		 } 
		 
		 return details;
	}
	
	public JsonObject Update_Job005(Job_005 Job)
	{
		JsonObject details =  new JsonObject();
		
		try 
		{
			 String Sql = "update job005 set STATUS=? where REFNO=? and REQSL=?";
			
			 int status = Jdbctemplate.update(Sql, new Object[] { "U", Job.getREFNO(), Job.getREQSL()});
			 
			 details.addProperty("Result", status == 1 ? "Success" : "Failed");
		     details.addProperty("Message", status == 1 ? "Record Updated Successfuly !!" : "Record not Updated !!");
		}
		catch (Exception e) 
		{
			details.addProperty("Result", "Failed");
			details.addProperty("Message", e.getLocalizedMessage());
			
			logger.debug("Exception :::: "+e.getLocalizedMessage());
		} 
		
		return details;
	}
	
	public JsonObject Delete_Job005(Job_005 Job)
	{
		JsonObject details =  new JsonObject();
		
		try 
		{
			String Sql =  "Delete from job005 where PAYTYPE=? and REFNO=? and REQSL=? and STATUS=?";
			
			int status = Jdbctemplate.update(Sql, new Object[] { Job.getPAYTYPE(), Job.getREFNO(), Job.getREQSL(), "U" });	
			
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
	
	public List<Job_005> Get_Threads()
	{
		List<Job_005> Threads = new ArrayList<Job_005>();

		try 
		{
			 String Sql = "Select * from job005 where SUBORGCODE=? and SYSCODE=? and STATUS=? and ROWNUM=?";
			
			 Threads = Jdbctemplate.query(Sql, new Object[] { "EXIM", "HP", "Q", 1 }, new DThread_Mapper());
		}
		catch(Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage());
		} 
		
		return Threads;
	}

	 public class DThread_Mapper implements RowMapper<Job_005> 
     {
		public Job_005 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Job_005 SQL = new Job_005();  
			
			SQL.setSUBORGCODE(rs.getString("SUBORGCODE"));
			SQL.setSYSCODE(rs.getString("SYSCODE"));
			SQL.setCHCODE(rs.getString("CHCODE"));
			SQL.setPAYTYPE(rs.getString("PAYTYPE"));
			SQL.setREQDATE(rs.getString("REQDATE"));
			SQL.setREFNO(rs.getString("REFNO"));
			SQL.setREQSL(rs.getString("REQSL"));
			SQL.setSTATUS(rs.getString("STATUS"));
			SQL.setTRANTYPE(rs.getString("TRANTYPE"));
			SQL.setREASON(rs.getString("REASON"));
			SQL.setREV_REF(rs.getString("REV_REF"));
			SQL.setSERVCODE(rs.getString("SERVCODE"));
			
			return SQL;
		}
     }
}
