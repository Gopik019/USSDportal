package com.hdsoft.hdpay.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

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
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.Repositories.Recon_004;
import com.hdsoft.utils.FormatUtils;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Reconcilation_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Insert_Recon_Request(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();
		
		try
		{   
			 JsonObject Request = new Common_Utils().StringToJsonObject(request);
			 
			 JsonObject Recon = Request.get("Recon").getAsJsonObject();		
			 
			 final List<Recon_004> Recon_Infos = Construct_Recon_Object(Recon , Headers);
			 
			 details = Insert_Recon_004(Recon_Infos);
			 
			 if(Recon_Infos.size() !=0)
			 {
				 String sql = "Insert into JOB003(SUBORGCODE,SYSCODE,JOBCODE,EXECMETHOD,REFNO,METHODNAME,EXECODE) values(?,?,?,?,?,?,?)";
				 
				 Jdbctemplate.update(sql, new Object[] { "EXIM", "HP", "5", "ORACLE", Recon_Infos.get(0).getREFID(), "Reconcilation.PROC_Update_Reconcilation", "RECON" } );
			 }
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  
			 
			 logger.debug("Exception in Insert_Recon_Request :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Insert_Recon_004(List<Recon_004> Infos) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 List<Object[]> batch = new ArrayList<Object[]>();
		      
			 for(int i=0;i<Infos.size();i++)
			 {
				 Recon_004 Info = Infos.get(i);
				
				 Object[] values = new Object[] { Info.getSUBORGCODE(), Info.getSRCSYSTEMCD(), Info.getDESSYSTEMCD(), Info.getPAYTYPE(), Info.getRECONDATE(), Info.getREQUESTSL(), 
						 Info.getRECORDSL(), Info.getREFID(), Info.getTRANDATE(), Info.getTRANTYPE(), Info.getS_ACCOUNTNO(), Info.getD_ACCOUNTNO(), Info.getTRANAMT(), Info.getTRANCURR(), Info.getS_GLNO(),
						 Info.getD_GLNO(), Info.getBILLPAY(), Info.getINVOICENUM(), Info.getBILLAMT(), Info.getSTATUS(), Info.getRECEIPTNO(), Info.getRECSTATUS() };
		            
		         batch.add(values);
			 }
			 
			 String sql = "Insert into RECON004(SUBORGCODE,SRCSYSTEMCD,DESSYSTEMCD,PAYTYPE,RECONDATE,REQUESTSL,RECORDSL,REFID,TRANDATE,TRANTYPE,S_ACCOUNTNO,D_ACCOUNTNO,TRANAMT,TRANCURR,S_GLNO,D_GLNO,BILLPAY,INVOICENUM,BILLAMT,STATUS,RECEIPTNO,RECSTATUS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.batchUpdate(sql, batch);
			 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Recons added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Insert_Recon_004 :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Insert_Recon_004(Recon_004 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into RECON004(SUBORGCODE,SRCSYSTEMCD,DESSYSTEMCD,PAYTYPE,RECONDATE,REQUESTSL,RECORDSL,REFID,TRANDATE,TRANTYPE,S_ACCOUNTNO,D_ACCOUNTNO,TRANAMT,TRANCURR,S_GLNO,D_GLNO,BILLPAY,INVOICENUM,BILLAMT,STATUS,RECEIPTNO,RECSTATUS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getSRCSYSTEMCD(), Info.getPAYTYPE(), Info.getRECONDATE(), Info.getREQUESTSL(), Info.getRECORDSL(), Info.getREFID(), Info.getTRANDATE(), Info.getTRANTYPE(), Info.getD_ACCOUNTNO(),
					 Info.getTRANAMT(), Info.getS_GLNO(), Info.getD_GLNO(), Info.getBILLPAY(), Info.getINVOICENUM(), Info.getBILLAMT(), Info.getSTATUS(), Info.getRECEIPTNO(), Info.getRECSTATUS()});
					 
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Recons added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Insert_Recon_004 :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public List<Recon_004> Construct_Recon_Object(JsonObject Recon, JsonObject Headers)
	{
		List<Recon_004> Recon_Objects = new ArrayList<Recon_004>();
		
		try
		{
			 String PAYTYPE = Recon.get("PAYTYPE").getAsString();			 
			 String DATE = Recon.get("DATE").getAsString();			 
			 String ReqRefID = Recon.get("ReqRefID").getAsString();
			 String RECONDATE = Recon.get("RECONDATE").getAsString();
			 String Checksum = Recon.get("Checksum").getAsString();
			 String INITATEDBY = Recon.get("INITATEDBY").getAsString();
			 
			 JsonObject RECONDETAILS = Recon.get("RECONDETAILS").getAsJsonObject();
			 
			 JsonArray RECORDS = RECONDETAILS.get("RECORDS").getAsJsonArray();  
			 
			 for(int i=0;i<RECORDS.size();i++)
			 {
				 JsonObject Record_Info = RECORDS.get(i).getAsJsonObject();
				 
				 String REFID = Record_Info.get("REFID").getAsString();
				 String TRANDATE = Record_Info.get("TRANDATE").getAsString();
				 String TRANAMT = Record_Info.get("TRANAMT").getAsString();
				 String TRANCURR = Record_Info.get("TRANCURR").getAsString();
				 String S_ACCOUTNO = Record_Info.get("S_ACCOUTNO").getAsString();
				 String S_GLNO = Record_Info.get("S_GLNO").getAsString();
				 String D_ACCOUTNO = Record_Info.get("D_ACCOUTNO").getAsString();
				 String D_GLNO = Record_Info.get("D_GLNO").getAsString();				 
				 String TRANTYPE = Record_Info.get("TRANTYPE").getAsString();
				 String BILLPAY = Record_Info.get("BILLPAY").getAsString();				 
				 String INVOICENO = Record_Info.get("INVOICENO").getAsString();
				 String BILLAMT = Record_Info.get("BILLAMT").getAsString();
				 String Status = Record_Info.get("Status").getAsString();
			 
				 String SUBORGCODE  = "EXIM";
				 String SYSCODE     = "HP";		
				 String REQSL		=  Generate_Serial().get("Serial").getAsString();		
				 String RECORDSL    = (i+1)+"";
		
				 Recon_004 Rec = new Recon_004();
				 
				 Rec.setSUBORGCODE(SUBORGCODE);
				 Rec.setSRCSYSTEMCD(Headers.get("ChannelID").getAsString());
				 Rec.setDESSYSTEMCD(SYSCODE);
				 Rec.setPAYTYPE(PAYTYPE);
				 Rec.setRECONDATE(RECONDATE);
				 Rec.setREQUESTSL(REQSL);
				 Rec.setRECORDSL(RECORDSL);
				 Rec.setTRANDATE(TRANDATE);
				 Rec.setREFID(REFID);
				 Rec.setTRANTYPE(TRANTYPE);
				 Rec.setS_ACCOUNTNO(S_ACCOUTNO);
				 Rec.setD_ACCOUNTNO(D_ACCOUTNO);
				 Rec.setTRANAMT(TRANAMT);
				 Rec.setTRANCURR(TRANCURR);
				 Rec.setS_GLNO(S_GLNO);
				 Rec.setD_GLNO(D_GLNO);
				 Rec.setBILLPAY(BILLPAY);
				 Rec.setINVOICENUM(INVOICENO);
				 Rec.setBILLAMT(BILLAMT);
				 Rec.setSTATUS(Status);
				 Rec.setRECEIPTNO("");
				 Rec.setRECSTATUS("PENDING");
				 Rec.setFLAG(true);
				 
				 Recon_Objects.add(Rec);
			 }
		}
		catch(Exception e)
		{
			logger.debug("Exception in Construct_Recon_Object :::: "+e.getLocalizedMessage()); 
		}
		
		return Recon_Objects;
	}
	
	public JsonObject Recon_Procedure(final String SUBORGCODE, final String SRCSYSTEMCD, final String DESSYSTEMCD, final String PAYTYPE, final String RECONDATE, final String REQUESTSL, final String RECORDSL) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 final String procedureCall = "{CALL Reconcilation.PROC_Update_Reconcilation(?,?,?,?,?,?,?,?)}";
 			
 			 Map<String, Object> resultMap = Jdbctemplate.call(new CallableStatementCreator() {
 	 
					public CallableStatement createCallableStatement(Connection connection) throws SQLException {
 
						CallableStatement CS = connection.prepareCall(procedureCall);
						CS.setString(1, SUBORGCODE);
						CS.setString(2, SRCSYSTEMCD);
						CS.setString(3, DESSYSTEMCD);
						CS.setString(4, PAYTYPE);
						CS.setString(5, FormatUtils.dynaSQLDate(RECONDATE ,"YYYY-MM-DD"));
						CS.setString(6, REQUESTSL);
						CS.setString(7, RECORDSL);						
						CS.registerOutParameter(8, Types.VARCHAR);

						return CS;
					}
 				}, get_ProcedureParams());
 			
 			 String O_RESULT = resultMap.get("O_RESULT").toString();
 			
 			 details.addProperty("O_RESULT", O_RESULT);
 			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Request added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Recon_Procedure :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Generate_Serial() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select RECON.nextval from dual";
			   
			 int SL = Jdbctemplate.queryForObject(sql, Integer.class);
			 
			 details.addProperty("Serial", SL);
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Serial generated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Generate_Serial from RECON :::: "+e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public List<SqlParameter> get_ProcedureParams()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	
		inParamMap.add(new SqlParameter("SUBORGCODE"  	, Types.VARCHAR));
		inParamMap.add(new SqlParameter("SRCSYSTEMCD"   , Types.VARCHAR)); 
		inParamMap.add(new SqlParameter("DESSYSTEMCD" 	, Types.VARCHAR));
		inParamMap.add(new SqlParameter("PAYTYPE" 		, Types.VARCHAR));
		inParamMap.add(new SqlParameter("RECONDATE"   	, Types.VARCHAR));
		inParamMap.add(new SqlParameter("REQUESTSL"   	, Types.VARCHAR));
		inParamMap.add(new SqlParameter("RECORDSL"   	, Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("O_RESULT"   , Types.VARCHAR));
		
		return inParamMap;
	}
	
	public class Recon_004_Mapper implements RowMapper<Recon_004> 
    {
		public Recon_004 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Recon_004 API = new Recon_004();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setSRCSYSTEMCD(rs.getString("SRCSYSTEMCD"));
			API.setDESSYSTEMCD(rs.getString("DESSYSTEMCD"));
			API.setPAYTYPE(rs.getString("PAYTYPE"));
			API.setRECONDATE(rs.getString("RECONDATE"));
			API.setREQUESTSL(rs.getString("REQUESTSL"));
			API.setRECORDSL(rs.getString("RECORDSL"));
			API.setREFID(rs.getString("REFID"));
			API.setTRANTYPE(rs.getString("TRANTYPE"));
			API.setS_ACCOUNTNO(rs.getString("S_ACCOUNTNO"));
			API.setD_ACCOUNTNO(rs.getString("D_ACCOUNTNO"));
			API.setTRANAMT(rs.getString("TRANAMT"));
			API.setTRANCURR(rs.getString("TRANCURR"));
			API.setS_GLNO(rs.getString("S_GLNO"));
			API.setD_GLNO(rs.getString("D_GLNO"));
			API.setBILLPAY(rs.getString("BILLPAY"));
			API.setINVOICENUM(rs.getString("INVOICENUM"));
			API.setBILLAMT(rs.getString("BILLAMT"));
			API.setSTATUS(rs.getString("STATUS"));
			API.setRECEIPTNO(rs.getString("RECEIPTNO"));
			API.setRECSTATUS(rs.getString("RECSTATUS"));
		
			return API;
		}
     }
}
