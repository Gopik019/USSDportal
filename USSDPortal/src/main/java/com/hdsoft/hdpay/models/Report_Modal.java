package com.hdsoft.hdpay.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.Repositories.PAY_001;
import com.hdsoft.hdpay.Repositories.Recon_004;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Request_002;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Report_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Get_Transaction_Reports(PAY_001 info)
	{
		JsonObject details = new JsonObject();

		try 
		{
			 String SQL = "Select * from request002 order by REQSL desc";
				
			 List<Request_002> Info = Jdbctemplate.query(SQL, new TIPS_Modal().new Request_002_Mapper());
				
			 details.add("Transaction_Reports",  new Gson().toJsonTree(Info));
			 
			 details.addProperty("Result", Info.size() !=0 ? "Success" : "Failed");
 			 details.addProperty("Message", Info.size() !=0 ? "Transaction Reports found !!" : "Transaction Reports Not Found !!");
 		 }
 		 catch(Exception e)
 		 {
 			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Get_Transaction_Reports :::: "+e.getLocalizedMessage()); 
 		 }
		
		 return details;
	}
	
	public JsonObject Get_Bill_Request_Reports(Request_001 info)
	{
		JsonObject details = new JsonObject();

		try 
		{
			 String SQL = "Select CHCODE,PAYTYPE,MSGTYPE,FLOW, to_char(REQDATE,'DD/MM/YYYY') REQDATE,UNIREFNO from request001";
			
			 List<Request_001> Info = Jdbctemplate.query(SQL, new Bill_Request_Mapper());
			
			 details.add("Bill_Request_Reports",  new Gson().toJsonTree(Info));
			 
			 details.addProperty("Result", Info.size() !=0 ? "Success" : "Failed");
 			 details.addProperty("Message", Info.size() !=0 ? "Bill Request Reports found !!" : "Bill Request Reports Not Found !!");
 		 }
 		 catch(Exception e)
 		 {
 			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Get_Bill_Request_Reports :::: "+e.getLocalizedMessage()); 
 		 }
		
		 return details;
	}
	
	public JsonObject Get_Recon_Request_Reports(Request_001 info)
	{
		JsonObject details = new JsonObject();

		try 
		{
			 String SQL = "Select SRCSYSTEMCD,PAYTYPE,TRANTYPE,to_char(RECONDATE,'DD/MM/YYYY') RECONDATE,to_char(TRANDATE,'DD/MM/YYYY') TRANDATE,STATUS from recon004";
			
			 List<Recon_004> Info = Jdbctemplate.query(SQL, new Recon_Request_Mapper());
			
			 details.add("Recon_Request_Reports",  new Gson().toJsonTree(Info));
			 
			 details.addProperty("Result", Info.size() !=0 ? "Success" : "Failed");
 			 details.addProperty("Message", Info.size() !=0 ? "Recon Request Reports found !!" : "Recon Request Reports Not Found !!");
 		 }
 		 catch(Exception e)
 		 {
 			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Get_Recon_Request_Reports :::: "+e.getLocalizedMessage()); 
 		 }
		
		 return details;
	}
	
	public class Transaction_Report_Mapper implements RowMapper<PAY_001> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public PAY_001 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			PAY_001 Info = new PAY_001();  

			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));
			Info.setPAYDATE(util.ReplaceNull(rs.getString("PAYDATE")));
			Info.setPAYEEREF(util.ReplaceNull(rs.getString("PAYEEREF")));
			Info.setS_ACCOUNT(util.ReplaceNull(rs.getString("S_ACCOUNT")));
			Info.setD_ACCOUNT(util.ReplaceNull(rs.getString("D_ACCOUNT")));
			Info.setDEBIT_CREDIT(util.ReplaceNull(rs.getString("FLOW")).equals("I") ? "C" : "D");  		
			Info.setTRANAMT(util.ReplaceNull(rs.getString("TRANAMT")));
			Info.setTRANCURR(util.ReplaceNull(rs.getString("TRANCURR")));
			Info.setINVOICENO(util.ReplaceNull(rs.getString("INVOICENO")));
			Info.setFLOW(util.ReplaceNull(rs.getString("FLOW")));
			Info.setREQSL(util.ReplaceNull(rs.getString("REQSL")));
			Info.setHOLD_STATUS(util.ReplaceNull(rs.getString("HOLD_STATUS")));
			Info.setREVERSAL_STATUS(util.ReplaceNull(rs.getString("REVERSAL_STATUS")));
			Info.setROWNUM(rowNum+1);
			
			return Info;
		}
     }
    
	public class Bill_Request_Mapper implements RowMapper<Request_001> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Request_001 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Request_001 Info = new Request_001();  

			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));
			Info.setMSGTYPE(util.ReplaceNull(rs.getString("MSGTYPE")));
			Info.setREQDATE(util.ReplaceNull(rs.getString("REQDATE")));
			Info.setFLOW(util.ReplaceNull(rs.getString("FLOW")));  
			Info.setUNIREFNO(util.ReplaceNull(rs.getString("UNIREFNO")));
			Info.setROWNUM(rowNum+1);
			
			return Info;
		}
     }
    
    public class Recon_Request_Mapper implements RowMapper<Recon_004> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Recon_004 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Recon_004 Info = new Recon_004();  

			Info.setSRCSYSTEMCD(util.ReplaceNull(rs.getString("SRCSYSTEMCD"))); 
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));
			Info.setTRANTYPE(util.ReplaceNull(rs.getString("TRANTYPE")));
			Info.setRECONDATE(util.ReplaceNull(rs.getString("RECONDATE")));
			Info.setTRANDATE(util.ReplaceNull(rs.getString("TRANDATE")));
			Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));
			Info.setROWNUM(rowNum+1);
			
			return Info;
		}
     }
}
