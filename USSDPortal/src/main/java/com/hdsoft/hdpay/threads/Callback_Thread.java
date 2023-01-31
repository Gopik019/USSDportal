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
import com.hdsoft.hdpay.Repositories.Callback001;
import com.hdsoft.hdpay.models.GEPG_Modal;
import com.hdsoft.hdpay.models.Outward_Modal;
import com.hdsoft.hdpay.models.TIPS_Modal;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Callback_Thread implements Runnable
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
		logger.debug(">>>> EXIMPAY Callback THREAD HAS BEEN STARTED  <<<<<<");
		
		try 
		{
			while(true)
			{   	
				 List<Callback001> All_Jobs = Get_Threads();
				 
				 for(int i=0; i<All_Jobs.size(); i++)
			     {
					 JsonObject details = new JsonObject();
					 
					 JsonObject Update_details = Update_Callback001(All_Jobs.get(i));
					 
					 details.addProperty("Update_Callback001", Update_details.toString());
					  
					 JsonObject Out_details = Method_Finder(All_Jobs.get(i)); 	 /*** Need to check the PAYTYPE such as GEPG, TRA, DERMOLOG etc. ***/
		             
		             details.addProperty("Method_Finder", Out_details.toString());
		             
		             logger.debug("Method_Finder out ::: "+Out_details);
		                         	 
		             if(Out_details.get("Result").getAsString().equals("Success"))
		     	     {
		            	 JsonObject Del_details = Delete_Callback001(All_Jobs.get(i));
		            	 
		            	 details.addProperty("Delete_Job005", Del_details.toString());
		            	 
		            	 logger.debug("Delete_Job005 out ::: "+Del_details);       
		     	     }
		             
		             logger.debug("Dedictated out ::: "+details);   
			     }
				 
	            String FREQINSEC = "900"; /** 15 Mins ***/
  
	            long sleep_time = 1000L * Long.valueOf(FREQINSEC).longValue();  
	            
	        	Thread.sleep(sleep_time);
			}
		}
		catch (Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage());
		}
    }
	 
	public JsonObject Method_Finder(Callback001 Job) 
	{
		 JsonObject details = new JsonObject();
		 
		 try 
		 {
			 boolean Status = false;
			 
			 String PAYTYPE = Job.getPAYTYPE();
			 
			 if(PAYTYPE.equals("GEPG"))  
	         {
				 logger.debug("EXECMETHOD is GEPG");
	     	  
	     	     Status = true;
	     	 }
			 
			 if(PAYTYPE.equals("TIPS"))  
	         {
				 logger.debug("EXECMETHOD is TIPS");
				 
				 details = TIPS.TIPS_Callabck_Executer(Job);	
				 
	     	     Status = true;
	     	 }
			 
			 if(PAYTYPE.equals("TRA"))  
	         {
				 logger.debug("EXECMETHOD is TRA");
	     	    
	     	     Status = true;
	     	 }
			 
			 if(PAYTYPE.equals("DERMOLOG"))  
	         {
				 logger.debug("EXECMETHOD is DERMOLOG");
	     	    
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
	
	public JsonObject Update_Callback001(Callback001 Job)
	{
		JsonObject details =  new JsonObject();
		
		try 
		{
			 String Sql = "update callback001 set STATUS=? where SUBORGCODE=? and SYSCODE=? and CHCODE=? and PAYTYPE=? and REQREFNO=? and TRANREFNO=? and REQSL=? and PAYSL=?";
			
			 int status = Jdbctemplate.update(Sql, new Object[] { "U", Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getREQREFNO(), Job.getTRANREFNO(), Job.getREQSL(), Job.getPAYSL() });
			 
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
	
	public JsonObject Delete_Callback001(Callback001 Job)
	{
		JsonObject details =  new JsonObject();
		
		try 
		{
			String Sql =  "Delete from callback001 where SUBORGCODE=? and SYSCODE=? and CHCODE=? and PAYTYPE=? and REQREFNO=? and TRANREFNO=? and REQSL=? and PAYSL=? and STATUS=?";
			
			int status = Jdbctemplate.update(Sql, new Object[] { Job.getSUBORGCODE(), Job.getSYSCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getREQREFNO(), Job.getTRANREFNO(), Job.getREQSL(), Job.getPAYSL(), "U" });	
			
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
	
	public List<Callback001> Get_Threads()
	{
		List<Callback001> Threads = new ArrayList<Callback001>();

		try 
		{
			 String Sql = "Select * from callback001 where SUBORGCODE=? and SYSCODE=? and STATUS=? and ROWNUM=?";
			
			 Threads = Jdbctemplate.query(Sql, new Object[] { "EXIM", "HP", "Q", 1 }, new Callback_Mapper());
		}
		catch(Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage());
		} 
		
		return Threads;
	}

	public class Callback_Mapper implements RowMapper<Callback001> 
    {
		public Callback001 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Callback001 SQL = new Callback001();  
			
			SQL.setSUBORGCODE(rs.getString("SUBORGCODE"));
			SQL.setSYSCODE(rs.getString("SYSCODE"));
			SQL.setCHCODE(rs.getString("CHCODE"));
			SQL.setPAYTYPE(rs.getString("PAYTYPE"));
			SQL.setFLOW(rs.getString("FLOW"));
			SQL.setREQDATE(rs.getString("REQDATE"));
			SQL.setREQREFNO(rs.getString("REQREFNO"));
			SQL.setTRANREFNO(rs.getString("TRANREFNO"));
			SQL.setPAYDATE(rs.getString("PAYDATE"));
			SQL.setTRANTYPE(rs.getString("TRANTYPE"));
			SQL.setREQSL(rs.getString("REQSL"));
			SQL.setPAYSL(rs.getString("PAYSL"));
			SQL.setSTATUS(rs.getString("STATUS"));
			SQL.setREQTIME(rs.getString("REQTIME"));
			
			return SQL;
		}
     }
}
