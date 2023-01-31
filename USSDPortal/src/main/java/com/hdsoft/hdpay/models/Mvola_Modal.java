package com.hdsoft.hdpay.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Response_001;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Mvola_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	@Autowired
	public Request_Modal Req_Modal; 
	
	@Autowired
	public Response_Modal Res_Modal;
	
	public JsonObject getAccountBalance(String Body_MSG)
	{
		JsonObject details = new JsonObject(); /*** for output ***/
		
		Common_Utils util = new Common_Utils();
		
		JsonObject Request_body = util.StringToJsonObject(Body_MSG);
		
		// Request_001 Info = new Request_001(SUBORGCODE, CHCODE, paytype, MSGTYPE, FLOW, date, REQTIME, reqrefid, MSGURL, IP, PORT, HEAD_MSG, BODY_MSG, initiatedby, checksum);
		 
		// Req_Modal.Insert_Request_001(Info);  /**** Insert request into request001 ***/
		 	
		String OPERATION_TYPE = Request_body.get("OPERATION_TYPE").getAsString();
		String ACCOUNT_ALIAS = Request_body.get("ACCOUNT_ALIAS").getAsString();
		String OPERATION_DATE = Request_body.get("OPERATION_DATE").getAsString();
		String OPERATION_ID = Request_body.get("OPERATION_ID").getAsString();
		String MSISDN = Request_body.get("MSISDN").getAsString();
		
		String sql = "Select count(*) from CLIENT010 where ID_CODE=? and MOBILE=? and STATUS=?";
		
		int count = Jdbctemplate.queryForObject(sql, new Object[] { MSISDN, MSISDN, "active"}, Integer.class);
		
		if(count == 1)
		{
			//Cbs posting call for balance
			
			//Response_001 Response = new Response_001(Job.getSUBORGCODE(), Job.getCHCODE(), Job.getPAYTYPE(), Job.getSERVCODE(), "O", Job.getREFNO() , Requesturl , "IP", "PORT", Headers.toString(), API_Response.toString(), "TIPS", "");
				
			//Res_Modal.Insert_Response_001(Response);  /**** Insert Response to Response_001 ****/	
						
			details.addProperty("OPERATION_ID", OPERATION_ID);
			details.addProperty("ACCOUNT_ALIAS", ACCOUNT_ALIAS);
			details.addProperty("AVAILABLE_BALANCE", "5000032");
			details.addProperty("MSISDN", MSISDN);
		}
		else
		{
			details.addProperty("errorCategory", "");
			details.addProperty("errorCode", "");
			details.addProperty("errorDescription", "");
			details.addProperty("errorDateTime", "");
		}
		
		return details;
	}
}
