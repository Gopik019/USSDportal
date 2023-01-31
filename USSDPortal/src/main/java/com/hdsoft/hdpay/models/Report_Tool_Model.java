package com.hdsoft.hdpay.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.other.Repositories.Report_003;
import com.hdsoft.other.Repositories.Report_004;
import com.hdsoft.other.Repositories.Report_Details;
import com.hdsoft.other.Repositories.Report_Params;
import com.hdsoft.utils.FormatUtils;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Report_Tool_Model 
{
	private JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	private ArrayList<String> Columns = new ArrayList<String>();
	
	public JsonObject Get_Reports_Types() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select * from REPORT003 order by REPORT_CODE";
			
			 List<Report_003> obj = Jdbctemplate.query(sql, new Report_003_Mapper());
			
			 details.add("details", new Gson().toJsonTree(obj));
			 
			 details.addProperty("Result", obj.size() != 0 ? "Success" : "Failed");
			 details.addProperty("Message", obj.size() != 0 ? "Reports types Found" : "Reports types Not Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Get_Reports_Types :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Get_Reports_Inputs(String SUBORGCODE, String REPORT_CODE) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select * from REPORT004 where SUBORGCODE=? and REPORT_CODE=? order by REPROT_SL";
			
			 List<Report_004> obj = Jdbctemplate.query(sql, new Object[] { SUBORGCODE, REPORT_CODE},  new Report_004_Mapper());
			
			 details.add("details", new Gson().toJsonTree(obj));
			 
			 details.addProperty("Result", obj.size() != 0 ? "Success" : "Failed");
			 details.addProperty("Message", obj.size() != 0 ? "Reports Fields Found" : "Reports Fields Not Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Get_Reports_Inputs :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Generate_Reports(final Report_Params Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			final String procedureCall = get_ProcedureName(Info);
			
 			Map<String, Object> resultMap = Jdbctemplate.call(new CallableStatementCreator() {
 				
					public CallableStatement createCallableStatement(Connection connection) throws SQLException {
 
						CallableStatement CS = connection.prepareCall(procedureCall);
						
						int i = 0;
						
						for(i=0; i<Info.getInputs_labels().length; i++)
						{
							if(Info.getInputs_types()[i].equals("D"))
							{
								CS.setString(i+1, FormatUtils.dynaSQLDate(Info.getInputs_values()[i] ,"YYYY-MM-DD"));
							}
							else
							{
								CS.setString(i+1, Info.getInputs_values()[i]);
							}
						}
						
						CS.registerOutParameter(i+1, Types.INTEGER);
						CS.registerOutParameter(i+2, Types.VARCHAR);
						
						return CS;
				}
 			 }, get_ProcedureParams(Info));
 			
 			 Common_Utils util = new Common_Utils();
			 
 			 String REPORTSL = util.ReplaceNull(resultMap.get("REPORTSL"));
 			 String MESSAGE = util.ReplaceNull(resultMap.get("MESSAGE"));
 			 
 			 Columns = new ArrayList<String>();
 			 
 			 int Total_Columns = Integer.parseInt(Info.getTotal_Columns());
 			 
 			 for(int i=1; i<=Total_Columns; i++)
 			 {
 				  Columns.add("COLUMN"+i);
 			 }

 			 logger.debug("REPORTSL :::: "+REPORTSL);
 			 logger.debug("MESSAGE :::: "+MESSAGE);
 			 
 			 REPORTSL = "114321";
 			 
 			 String sql = "select * from REPORT002 where SERIAL = ?";
 			
			 List<Report_Details> Reports = Jdbctemplate.query(sql, new Object[] { REPORTSL }, new Report_Mapper());
			 
			 List<Report_Details> Data_Reports = new ArrayList<Report_Details>();
			 
			 int Count = 0;
					
			 JsonArray Table_Columns = new JsonArray(); 
			
			 for(int i=0; i<Reports.size();i++)
			 {
				 String COLUMN1 = Reports.get(i).getCOLUMN1();
				 
				 if(COLUMN1.equals("C"))
				 {
					 JsonElement jsonElement = new Gson().toJsonTree(Reports.get(i));
					 
					 JsonObject Columns_details = jsonElement.getAsJsonObject();
					 
					 for(int j=0; j<Columns.size(); j++)
		 			 {
						 if(Columns_details.has(Columns.get(j)))
						 {
							 JsonObject Column_Info = new JsonObject();
							 
							 Column_Info.addProperty("sTitle", Columns_details.get(Columns.get(j)).getAsString());
							 Column_Info.addProperty("mData", util.Replace_Special_Characters(Columns_details.get(Columns.get(j)).getAsString()));
							 
							 Table_Columns.add(Column_Info);
						 }
		 			 }
					 
					 details.add("Columns_details", Table_Columns);    
				 }
				 else if(COLUMN1.equals("H"))
				 {
					 details.add("Heading_details", new Gson().toJsonTree(Reports.get(i))); 
				 }
				 else 
				 {
					 Count++;
					 
					 Reports.get(i).setCOLUMN2(Count);
					 
					 Data_Reports.add(Reports.get(i));
				 }
			 }
			 
			 JsonElement jsonElement = new Gson().toJsonTree(Data_Reports);
			 
			 JsonArray Report_details = jsonElement.getAsJsonArray();
			 
			 String Report_details_= Report_details.toString();
			 
			 for(int i=Table_Columns.size()-1; i>=0; i--)
			 {
				  JsonObject Column_Info = Table_Columns.get(i).getAsJsonObject();
				  
				  String Replace_from = Columns.get(i);   
				  
				  String Replace_by = Column_Info.get("mData").getAsString();  
				  
				  Report_details_ = Report_details_.replaceAll(Replace_from, Replace_by);
			 }
			 
			 Report_details = util.StringToJsonArray(Report_details_); 
			 
			 details.add("Report_details", Report_details);   
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Reports Fields Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
			 
			 logger.debug("Exception in Generate_Reports :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	private String get_ProcedureName(Report_Params Info)
	{
		String Procedure_Name = "{CALL "+Info.getProcedure_Name()+"(,";
		
		for(int j=0; j<Info.getInputs_labels().length+2; j++)
		{
			Procedure_Name += ",?";
		}
		
		Procedure_Name += ")}";
		
		Procedure_Name = Procedure_Name.replace(",,", "");
		
		return Procedure_Name;
	}
	
	public JsonArray Suggestion_Finder(final String REPORTCODE, final String FIELDNAME, final String FIELDVALUE) 
	{
		JsonArray Values = new JsonArray();
		
		try
		{
			 final String procedureCall = "{CALL COMMON_PACK."+FIELDNAME+"(?,?,?,?)}";
			
 			 Map<String, Object> resultMap = Jdbctemplate.call(new CallableStatementCreator() {
 	 
					public CallableStatement createCallableStatement(Connection connection) throws SQLException {
 
						CallableStatement CS = connection.prepareCall(procedureCall);
						
						CS.setString(1, REPORTCODE);	
						CS.setString(2, FIELDNAME);	
						CS.setString(3, FIELDVALUE);	
						CS.registerOutParameter(4, Types.VARCHAR);
						
						return CS;
				}
 			 }, get_Suggestion_ProcedureParams());
 			
 			 Common_Utils util = new Common_Utils();
			 
 			 String MESSAGE = util.ReplaceNull(resultMap.get("RESULT"));
 			 
 			 logger.debug("MESSAGE :::: "+MESSAGE);
 			 
 			 if(!MESSAGE.contains("Invalid") && MESSAGE.contains("0|"))
 			 {
 				 String[] arr = MESSAGE.split("\\|"); 
 				 
 				 for(int i=1; i<arr.length; i++)
 				 {
 					 JsonObject Informations = new JsonObject();

 					 Informations.addProperty("label", arr[i]);
 					 Informations.addProperty("value", arr[i]);
 					 
 					Values.add(Informations);
 				 }			
 			 }
 			 
			 return Values;
		 }
		 catch(Exception e)
		 {
			 logger.debug("Exception in Suggestion_Finder :::: "+e.getLocalizedMessage());
		 }
		
		 return Values;
	}
	
	private List<SqlParameter> get_ProcedureParams(Report_Params Info)
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	    
		for(int i=0; i<Info.getInputs_labels().length; i++)
		{
			inParamMap.add(new SqlParameter(Info.getInputs_labels()[i], Types.VARCHAR));
		}
		
		inParamMap.add(new SqlOutParameter("REPORTSL" , Types.INTEGER));
		inParamMap.add(new SqlOutParameter("MESSAGE"  , Types.VARCHAR));
	
		return inParamMap;
	}
	
	private List<SqlParameter> get_Suggestion_ProcedureParams()
	{
		List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
	    
		inParamMap.add(new SqlParameter("REPORTCODE" , Types.VARCHAR));
		inParamMap.add(new SqlParameter("FIELDNAME"  , Types.VARCHAR));
		inParamMap.add(new SqlParameter("FIELDVALUE" , Types.VARCHAR));
		inParamMap.add(new SqlOutParameter("RESULT"  , Types.VARCHAR));
	
		return inParamMap;
	}
	
	private class Report_003_Mapper implements RowMapper<Report_003> 
    {
		public Report_003 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Report_003 obj = new Report_003();  

			obj.setSUBORGCODE(rs.getString("SUBORGCODE"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setREPORT_SRC(rs.getString("REPORT_SRC"));
			obj.setREPORT_SRC_PARAM_CNT(rs.getString("REPORT_SRC_PARAM_CNT"));
			obj.setREPORT_OP_COL_CNT(rs.getString("REPORT_OP_COL_CNT"));
			obj.setSTATUS(rs.getString("STATUS"));
			
			return obj;
		}
     }
    
    private class Report_004_Mapper implements RowMapper<Report_004> 
    {
		public Report_004 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Report_004 obj = new Report_004();  

			obj.setSUBORGCODE(rs.getString("SUBORGCODE"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPROT_SL(rs.getString("REPROT_SL"));
			obj.setREPORT_FIELD(rs.getString("REPORT_FIELD"));
			obj.setREPORT_FIELD_DESC(rs.getString("REPORT_FIELD_DESC"));
			obj.setREPROT_FIELD_TYPE(rs.getString("REPROT_FIELD_TYPE"));
			
			return obj;
		}
     }
    
    private class Report_Mapper implements RowMapper<Report_Details> 
    {
    	Common_Utils util = new Common_Utils(); 
    	
		public Report_Details mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Report_Details obj = new Report_Details();  

			obj.setCOLUMN1(Columns.contains("COLUMN1") ? util.ReplaceNull(rs.getString("COLUMN1")) : null);
			obj.setCOLUMN2(Columns.contains("COLUMN2") ? util.ReplaceNull(rs.getString("COLUMN2")) : null);
			obj.setCOLUMN3(Columns.contains("COLUMN3") ? util.ReplaceNull(rs.getString("COLUMN3")) : null);
			obj.setCOLUMN4(Columns.contains("COLUMN4") ? util.ReplaceNull(rs.getString("COLUMN4")) : null);
			obj.setCOLUMN5(Columns.contains("COLUMN5") ? util.ReplaceNull(rs.getString("COLUMN5")) : null);
			obj.setCOLUMN6(Columns.contains("COLUMN6") ? util.ReplaceNull(rs.getString("COLUMN6")) : null);
			obj.setCOLUMN7(Columns.contains("COLUMN7") ? util.ReplaceNull(rs.getString("COLUMN7")) : null);
			obj.setCOLUMN8(Columns.contains("COLUMN8") ? util.ReplaceNull(rs.getString("COLUMN8")) : null);
			obj.setCOLUMN9(Columns.contains("COLUMN9") ? util.ReplaceNull(rs.getString("COLUMN9")) : null);
			obj.setCOLUMN10(Columns.contains("COLUMN10") ? util.ReplaceNull(rs.getString("COLUMN10")) : null);
			obj.setCOLUMN11(Columns.contains("COLUMN11") ? util.ReplaceNull(rs.getString("COLUMN11")) : null);
			obj.setCOLUMN12(Columns.contains("COLUMN12") ? util.ReplaceNull(rs.getString("COLUMN12")) : null);
			obj.setCOLUMN13(Columns.contains("COLUMN13") ? util.ReplaceNull(rs.getString("COLUMN13")) : null);
			obj.setCOLUMN14(Columns.contains("COLUMN14") ? util.ReplaceNull(rs.getString("COLUMN14")) : null);
			obj.setCOLUMN15(Columns.contains("COLUMN15") ? util.ReplaceNull(rs.getString("COLUMN15")) : null);
			obj.setCOLUMN16(Columns.contains("COLUMN16") ? util.ReplaceNull(rs.getString("COLUMN16")) : null);
			obj.setCOLUMN17(Columns.contains("COLUMN17") ? util.ReplaceNull(rs.getString("COLUMN17")) : null);
			obj.setCOLUMN18(Columns.contains("COLUMN18") ? util.ReplaceNull(rs.getString("COLUMN18")) : null);
			obj.setCOLUMN19(Columns.contains("COLUMN19") ? util.ReplaceNull(rs.getString("COLUMN19")) : null);
			obj.setCOLUMN20(Columns.contains("COLUMN20") ? util.ReplaceNull(rs.getString("COLUMN20")) : null);
			obj.setCOLUMN21(Columns.contains("COLUMN21") ? util.ReplaceNull(rs.getString("COLUMN21")) : null);
			obj.setCOLUMN22(Columns.contains("COLUMN22") ? util.ReplaceNull(rs.getString("COLUMN22")) : null);
			obj.setCOLUMN23(Columns.contains("COLUMN23") ? util.ReplaceNull(rs.getString("COLUMN23")) : null);
			obj.setCOLUMN24(Columns.contains("COLUMN24") ? util.ReplaceNull(rs.getString("COLUMN24")) : null);
			obj.setCOLUMN25(Columns.contains("COLUMN25") ? util.ReplaceNull(rs.getString("COLUMN25")) : null);
			obj.setCOLUMN26(Columns.contains("COLUMN26") ? util.ReplaceNull(rs.getString("COLUMN26")) : null);
			obj.setCOLUMN27(Columns.contains("COLUMN27") ? util.ReplaceNull(rs.getString("COLUMN27")) : null);
			obj.setCOLUMN28(Columns.contains("COLUMN28") ? util.ReplaceNull(rs.getString("COLUMN28")) : null);
			obj.setCOLUMN29(Columns.contains("COLUMN29") ? util.ReplaceNull(rs.getString("COLUMN29")) : null);
			obj.setCOLUMN30(Columns.contains("COLUMN30") ? util.ReplaceNull(rs.getString("COLUMN30")) : null);
			obj.setCOLUMN31(Columns.contains("COLUMN31") ? util.ReplaceNull(rs.getString("COLUMN31")) : null);
			obj.setCOLUMN32(Columns.contains("COLUMN32") ? util.ReplaceNull(rs.getString("COLUMN32")) : null);
			obj.setCOLUMN33(Columns.contains("COLUMN33") ? util.ReplaceNull(rs.getString("COLUMN33")) : null);
			obj.setCOLUMN34(Columns.contains("COLUMN34") ? util.ReplaceNull(rs.getString("COLUMN34")) : null);
			obj.setCOLUMN35(Columns.contains("COLUMN35") ? util.ReplaceNull(rs.getString("COLUMN35")) : null);
			obj.setCOLUMN36(Columns.contains("COLUMN36") ? util.ReplaceNull(rs.getString("COLUMN36")) : null);
			obj.setCOLUMN37(Columns.contains("COLUMN37") ? util.ReplaceNull(rs.getString("COLUMN37")) : null);
			obj.setCOLUMN38(Columns.contains("COLUMN38") ? util.ReplaceNull(rs.getString("COLUMN38")) : null);
			obj.setCOLUMN39(Columns.contains("COLUMN39") ? util.ReplaceNull(rs.getString("COLUMN39")) : null);
			obj.setCOLUMN40(Columns.contains("COLUMN40") ? util.ReplaceNull(rs.getString("COLUMN40")) : null);
			
			return obj;
		}
     }
}
