package com.hdsoft.hdpay.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.Repositories.web_service_001;
import com.hdsoft.hdpay.Repositories.web_service_002;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Webservice_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Get_Webserice_Info(String SYSCODE,String CHCODE, String SERVICECD) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select * from webservice001 where SYSCODE=? and CHCODE=? and SERVICECD=?";
			 
			 List<web_service_001> API_Info = Jdbctemplate.query(sql, new Object[] { SYSCODE, CHCODE, SERVICECD }, new API_Mapper() );
			 
			 if(API_Info.size()!=0)
			 {
				 String PAYLOAD = API_Info.get(0).getPAYLOAD();
				 
				 PAYLOAD = PAYLOAD.replaceAll("\\n", "");
				 PAYLOAD = PAYLOAD.replaceAll("\\r", "");
				
				 String SIGNPAYLOAD = API_Info.get(0).getSIGNPAYLOAD();
				 
				 SIGNPAYLOAD = SIGNPAYLOAD.replaceAll("\\n", "");
				 SIGNPAYLOAD = SIGNPAYLOAD.replaceAll("\\r", "");
				
				 if(API_Info.get(0).getFORMAT().equalsIgnoreCase("JSON"))
				 {
					 PAYLOAD = PAYLOAD.replaceAll("\\s+", "");
					 SIGNPAYLOAD = SIGNPAYLOAD.replaceAll("\\s+", "");
				 }
				 
				 details.addProperty("SUBORGCODE", API_Info.get(0).getSUBORGCODE());
				 details.addProperty("CHCODE", API_Info.get(0).getCHCODE());
				 details.addProperty("SERVICECD", API_Info.get(0).getSERVICECD());
				 details.addProperty("SERVNAME", API_Info.get(0).getSERVNAME());
				 details.addProperty("FORMAT", API_Info.get(0).getFORMAT());
				 details.addProperty("PROTOCOL", API_Info.get(0).getPROTOCOL());	 
				 details.addProperty("METHOD", API_Info.get(0).getMETHOD());
				 details.addProperty("CHTYPE", API_Info.get(0).getCHTYPE());
				 details.addProperty("URI", API_Info.get(0).getURI());
				 details.addProperty("PAYLOAD", PAYLOAD);
				 details.addProperty("SIGNPAYLOAD", SIGNPAYLOAD);
				 details.addProperty("HEADERID", API_Info.get(0).getHEADERID());
				 details.addProperty("FLOW", API_Info.get(0).getFLOW());
				 
				 sql = "Select * from webservice002 where SERVICECD=? and CHCODE=? and HEADERID=?";
				 
				 List<web_service_002> Header_Info = Jdbctemplate.query(sql, new Object[] { API_Info.get(0).getSERVICECD(), API_Info.get(0).getCHCODE(), API_Info.get(0).getHEADERID() }, new Header_Mapper() );
				 
				 JsonArray Headers = new JsonArray();
				 
				 for(int i=0;i<Header_Info.size();i++)
				 {
					JsonObject Header_details = new JsonObject();
					 
					Header_details.addProperty("Key", Header_Info.get(i).getHEADKEY());
					Header_details.addProperty("Value", Header_Info.get(i).getHEADVALUE());
					
					Headers.add(Header_details);
				 }
				
				 details.add("Headers", Headers);
			 }
			 
			 details.addProperty("Result", API_Info.size()!=0 ? "Success" : "Failed");
			 details.addProperty("Message", API_Info.size()!=0 ? "API Configuration Details Found !!" : "API Configuration Details Not Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Get_Webserice_Info :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Get_Webserice002_Info(String SERVICECD,String CHCODE, String HEADERID) 
	{
		JsonObject details = new JsonObject();
		
		try
		{	
			 String sql = "Select * from webservice002 where SERVICECD=? and CHCODE=? and HEADERID=?";
			 
			 List<web_service_002> Header_Info = Jdbctemplate.query(sql, new Object[] { SERVICECD, CHCODE, HEADERID }, new Header_Mapper() );
			 
			 JsonArray Headers = new JsonArray();
			 
			 for(int i=0;i<Header_Info.size();i++)
			 {
				JsonObject Header_details = new JsonObject();
				 
				Header_details.addProperty("Key", Header_Info.get(i).getHEADKEY());
				Header_details.addProperty("Value", Header_Info.get(i).getHEADVALUE());
				
				Headers.add(Header_details);
			 }
			
			 details.add("Headers", Headers);
			 
			 details.addProperty("Result", Header_Info.size() !=0 ? "Success" : "Failed");
			 details.addProperty("Message", Header_Info.size() !=0 ? "Headers Configuration Found Successfully !!" : "Headers Configuration Not Found !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Get_Webserice002_Info :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Get_CALLBACK_URL(String SUBORGCODE, String SYSCODE, String CHCODE, String SERVICECD, String FLOW) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select URI from webservice001 where SUBORGCODE=? and SYSCODE=? and CHCODE=? and SERVICECD=? and FLOW=?";
			   
			 List<String> CallBackURL = Jdbctemplate.queryForList(sql, new Object[] { SUBORGCODE, SYSCODE, CHCODE, SERVICECD, FLOW }, String.class);
			 
			 if(CallBackURL.size() !=0)
			 {
				 details.addProperty("CallBackURL", CallBackURL.get(0));
			 }
			 
			 details.addProperty("Result", CallBackURL.size() !=0 ? "Success" : "Failed");
			 details.addProperty("Message", CallBackURL.size() !=0 ? "CallBackURL Configuration found !!" : "CallBackURL Configuration not found !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Get_CALLBACK_URL :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public class Header_Mapper implements RowMapper<web_service_002> 
    {
		public web_service_002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			web_service_002 API = new web_service_002();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setSYSCODE(rs.getString("SYSCODE"));
			API.setCHCODE(rs.getString("CHCODE"));
			API.setSERVICECD(rs.getString("SERVICECD"));
			API.setHEADERID(rs.getString("HEADERID"));
			API.setSLNO(rs.getString("SLNO"));
			API.setHEADKEY(rs.getString("HEADKEY"));
			API.setHEADVALUE(rs.getString("HEADVALUE"));
			
			return API;
		}
     }
    
    public class API_Mapper implements RowMapper<web_service_001> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public web_service_001 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			web_service_001 API = new web_service_001();  

			API.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));
			API.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));
			API.setSERVICECD(util.ReplaceNull(rs.getString("SERVICECD")));
			API.setSERVNAME(util.ReplaceNull(rs.getString("SERVNAME")));
			API.setFORMAT(util.ReplaceNull(rs.getString("FORMAT")));
			API.setPROTOCOL(util.ReplaceNull(rs.getString("PROTOCOL")));
			API.setMETHOD(util.ReplaceNull(rs.getString("METHOD")));
			API.setCHTYPE(util.ReplaceNull(rs.getString("CHTYPE")));
			API.setURI(util.ReplaceNull(rs.getString("URI")));
			API.setPAYLOAD(util.ReplaceNull(rs.getString("PAYLOAD")));
			API.setSIGNPAYLOAD(util.ReplaceNull(rs.getString("SIGNPAYLOAD")));
			API.setHEADERID(util.ReplaceNull(rs.getString("HEADERID")));
			API.setFLOW(util.ReplaceNull(rs.getString("FLOW")));
			
			return API;
		}
     }
}
