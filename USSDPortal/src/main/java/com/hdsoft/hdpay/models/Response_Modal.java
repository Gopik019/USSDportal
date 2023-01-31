package com.hdsoft.hdpay.models;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.hdsoft.hdpay.Repositories.Response_001;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Response_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Insert_Response_001(Response_001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into response001(SUBORGCODE,CHCODE,PAYTYPE,MSGTYPE,FLOW,RESDATE,RESTIME,UNIREFNO,MSGURL,IP,PORT,HEAD_MSG,BODY_MSG,RESBY,HASHVAL) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getMSGTYPE(), Info.getFLOW(), Info.getRESDATE(), Info.getTimestamp(), 
					 Info.getUNIREFNO(), Info.getMSGURL(), Info.getIP(), Info.getPORT(), Info.getHEAD_MSG(), Info.getBODY_MSG(), Info.getRESBY(), Info.getHASHVAL() });
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Response added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Response_001 :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
}
