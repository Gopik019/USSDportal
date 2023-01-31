package com.hdsoft.hdpay.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.Repositories.Account_Information;
import com.hdsoft.hdpay.Repositories.Dashboard;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Dashboard_Modal 
{

	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Request_Monitoring() 
	{
		JsonObject details = new JsonObject();
		
		try
		{	 
			 JsonObject Bill =  Bill_Request_Monitoring();
			
			 JsonObject Payment =  Payment_Request_Monitoring();
			
			 JsonObject Account =  Account_Lookup_Request_Monitoring();
			
			 JsonObject Recon_Request_Monitoring =  Recon_Request_Monitoring();
			 
			 JsonObject Recon_Request_Monitoring_week = Reconcilation_Monitoring_Week();
			 
			 JsonObject Recon_Channel_Monitoring =  Reconcilation_Monitoring();
			 
			 details.add("Bill", Bill);
			 details.add("PAYMENT", Payment);
			 details.add("ACCOUNT", Account);
			 details.add("RECON", Recon_Request_Monitoring);
			 details.add("RECON_WEEK", Recon_Request_Monitoring_week);
			 details.add("RECON_CHANNEL", Recon_Channel_Monitoring);
			 
			 //System.out.println(details);
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Dashboard Retrieved Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Dashboard Request_Monitoring:::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Bill_Request_Monitoring() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select count(*) from request001 where SUBORGCODE=? and MSGTYPE=?";
				   
			 int Total_bill = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "001" }, Integer.class);
			 
			 sql = "Select count(*) from request001 where SUBORGCODE=? and MSGTYPE=? and PAYTYPE=?";
			   
			 int GEPG = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "001", "GEPG" }, Integer.class);
			 
			 sql = "Select count(*) from request001 where SUBORGCODE=? and MSGTYPE=? and PAYTYPE=?";
			   
			 int MSC = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "001", "MSC" }, Integer.class);
			 
			 sql = "Select count(*) from request001 where SUBORGCODE=? and MSGTYPE=? and PAYTYPE=?";
			   
			 int TIGO = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "001", "TIGO" }, Integer.class);
			 
			 details.addProperty("TOTAL", Total_bill);
			 details.addProperty("GEPG", GEPG);
			 details.addProperty("MSC", MSC);
			 details.addProperty("TIGO", TIGO);
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Dashboard Retrieved Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Dashboard Bill_Request_Monitoring:::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Payment_Request_Monitoring() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select count(*) from request002 where SUBORGCODE=?";
			   
			 int Total_Payments = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM" }, Integer.class);
			 
			 sql = "Select count(*) from request002 where SUBORGCODE=? and PAYTYPE=? and FLOW=?";
				   
			 int Outward_GEPG = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "GEPG", "O" }, Integer.class);
			 
			 sql = "Select count(*) from request002 where SUBORGCODE=? and PAYTYPE=? and FLOW=?";
			   
			 int Outward_MSC = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "MSC", "O" }, Integer.class);
			 
			 sql = "Select count(*) from request002 where SUBORGCODE=? and PAYTYPE=? and FLOW=?";
			
			 int Inward_TIPS = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "TIPS", "I" }, Integer.class);
			 
			 sql = "Select count(*) from request002 where SUBORGCODE=? and PAYTYPE=? and FLOW=?";
				
			 int Outward_TIPS = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "TIPS", "O" }, Integer.class);
		
			 details.addProperty("TOTAL", Total_Payments);
			 details.addProperty("Outward_GEPG", Outward_GEPG);
			 details.addProperty("Outward_MSC", Outward_MSC);
			 details.addProperty("Inward_TIPS", Inward_TIPS);
			 details.addProperty("Outward_TIPS", Outward_TIPS);
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Dashboard Retrieved Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Dashboard Payment_Request_Monitoring:::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Account_Lookup_Request_Monitoring() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 //String sql = "Select count(*) from request001 where SUBORGCODE=? and MSGTYPE=?";
			   
			// int Total_AC = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "2004" }, Integer.class);
			 
			 String sql = "Select count(*) from request001 where SUBORGCODE=? and MSGTYPE=? and PAYTYPE=?";
			   
			 int GEPG = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "2004", "GEPG" }, Integer.class);
			 
			 sql = "Select count(*) from request001 where SUBORGCODE=? and MSGTYPE=? and PAYTYPE=?";
			   
			 int MSC = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "2004", "MSC" }, Integer.class);
			 
			 sql = "Select count(*) from request001 where SUBORGCODE=? and PAYTYPE=? and MSGTYPE in (?,?,?)";
			   
			 int TIPS = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "TIPS", 2004, 2005, 2003 }, Integer.class);
			 
			 details.addProperty("TOTAL", GEPG+MSC+TIPS);
			 details.addProperty("GEPG", GEPG);
			 details.addProperty("MSC", MSC);
			 details.addProperty("TIPS", TIPS);		
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Dashboard Retrieved Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Dashboard Account_Lookup_Request_Monitoring:::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Recon_Request_Monitoring() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select count(*) from recon004 where SUBORGCODE=? and DESSYSTEMCD=?";
			   
			 int Total_Recon = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "HP" }, Integer.class);			 
			 
			 sql = "Select count(*) from recon004 where SUBORGCODE=? and DESSYSTEMCD=? and PAYTYPE=?";
			   
			 int GEPG = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "HP", "GEPG" }, Integer.class);
			 
			 sql = "Select count(*) from recon004 where SUBORGCODE=? and DESSYSTEMCD=? and PAYTYPE=?";
			   
			 int TIPS = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "HP", "TIPS" }, Integer.class);
			 
			 sql = "Select count(*) from recon004 where SUBORGCODE=? and DESSYSTEMCD=? and PAYTYPE=?";
			   
			 int MSC = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "HP", "MSC" }, Integer.class);

			 details.addProperty("TOTAL", Total_Recon);
			 details.addProperty("GEPG", GEPG);
			 details.addProperty("MSC", MSC);
			 details.addProperty("TIPS", TIPS);
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Dashboard Retrieved Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Dashboard Recon_Request_Monitoring:::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Reconcilation_Monitoring() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select count(*) from recon004 where SUBORGCODE=? and SRCSYSTEMCD=? and DESSYSTEMCD=? and RECSTATUS=?";
			   
			 int IB_PENDING = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "IB" , "HP", "PENDING" }, Integer.class);
			 
			 sql = "Select count(*) from recon004 where SUBORGCODE=? and SRCSYSTEMCD=? and DESSYSTEMCD=? and RECSTATUS=?";
			   
			 int IB_MATCHED = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "IB" , "HP", "MATCHED" }, Integer.class);
			 
			 sql = "Select count(*) from recon004 where SUBORGCODE=? and SRCSYSTEMCD=? and DESSYSTEMCD=? and RECSTATUS=?";
			   
			 int IB_NOT_MATCHED = Jdbctemplate.queryForObject(sql, new Object[] { "EXIM", "IB" , "HP", "NOT MATCHED" }, Integer.class);
			 
			 details.addProperty("IB_PENDING", IB_PENDING);
			 details.addProperty("IB_MATCHED", IB_MATCHED);
			 details.addProperty("IB_NOT_MATCHED", IB_NOT_MATCHED);
		
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Reconcilation_IB Dashboard Retrieved Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Dashboard Reconcilation_Monitoring:::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Reconcilation_Monitoring_Week() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			JsonObject Days = new JsonObject();
			
			Days.addProperty("MONDAY", "0");
			Days.addProperty("TUESDAY", "0");
			Days.addProperty("WEDNESDAY", "0");
			Days.addProperty("THURSSDAY", "0");
			Days.addProperty("FRIDAY", "0");
			Days.addProperty("SATURDAY", "0");
			
			JsonObject Pending = new JsonObject();
			JsonObject Matched = new JsonObject();
			JsonObject Not_Matched = new JsonObject();
			
			Pending = Days; Matched = Days; Not_Matched = Days;
			
			String sql = "Select RECONDATE, trim(TO_CHAR(RECONDATE, 'DAY')) as DAY, count(*) as Count from recon004 where SUBORGCODE=? and SRCSYSTEMCD=? and DESSYSTEMCD=? and RECSTATUS=? and \r\n" + 
			 		"RECONDATE between (select TRUNC(sysdate, 'iw') from dual) and (select TRUNC(sysdate, 'iw') + 6 - 1/86400 from dual) group by RECONDATE order by RECONDATE";
			   
			List<Dashboard> Pending_Info = Jdbctemplate.query(sql, new Object[] { "EXIM", "H2H" , "HP", "PENDING" }, new Dashboard_Mapper());
			 	
			for(int i=0;i<Pending_Info.size();i++)
			{
				Pending.addProperty(Pending_Info.get(i).getDAY(), Pending_Info.get(i).getCOUNT());
			}

			sql = "Select RECONDATE, trim(TO_CHAR(RECONDATE, 'DAY')) as DAY, count(*) as Count from recon004 where SUBORGCODE=? and SRCSYSTEMCD=? and DESSYSTEMCD=? and RECSTATUS=? and \r\n" + 
			 		"RECONDATE between (select TRUNC(sysdate, 'iw') from dual) and (select TRUNC(sysdate, 'iw') + 6 - 1/86400 from dual) group by RECONDATE order by RECONDATE";
			   
			List<Dashboard> Matched_Info = Jdbctemplate.query(sql, new Object[] { "EXIM", "H2H" , "HP", "MATCHED" }, new Dashboard_Mapper());

			for(int i=0;i<Matched_Info.size();i++)
			{
				Matched.addProperty(Matched_Info.get(i).getDAY(), Matched_Info.get(i).getCOUNT());
			}

			sql = "Select RECONDATE, trim(TO_CHAR(RECONDATE, 'DAY')) as DAY, count(*) as Count from recon004 where SUBORGCODE=? and SRCSYSTEMCD=? and DESSYSTEMCD=? and RECSTATUS=? and \r\n" + 
			 		"RECONDATE between (select TRUNC(sysdate, 'iw') from dual) and (select TRUNC(sysdate, 'iw') + 6 - 1/86400 from dual) group by RECONDATE order by RECONDATE";
			   
			List<Dashboard> Not_Matched_Info = Jdbctemplate.query(sql, new Object[] { "EXIM", "H2H" , "HP", "NOT MATCHED" }, new Dashboard_Mapper());

			for(int i=0;i<Not_Matched_Info.size();i++)
			{
				Not_Matched.addProperty(Not_Matched_Info.get(i).getDAY(), Not_Matched_Info.get(i).getCOUNT());
			}
			
			Common_Utils util = new Common_Utils();
			
			details.add("Total_Days", util.get_key_vals(Days)); 
			details.add("Pending", util.get_key_vals(Pending));  
			details.add("Matched", util.get_key_vals(Matched));
			details.add("Not_Matched", util.get_key_vals(Not_Matched));
			
			details.addProperty("Result", "Success");
			details.addProperty("Message", "Reconcilation_IB Dashboard Retrieved Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Dashboard Reconcilation_Monitoring:::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public class Dashboard_Mapper implements RowMapper<Dashboard> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Dashboard mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Dashboard Info = new Dashboard();   
			
			Info.setRECONDATE(util.ReplaceNull(rs.getString("RECONDATE")));
			Info.setDAY(util.ReplaceNull(rs.getString("DAY")));
			Info.setCOUNT(rs.getInt("COUNT"));
			
			return Info;
		}
    }
}
