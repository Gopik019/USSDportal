package com.hdsoft.hdpay.threads;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.hdsoft.hdpay.Repositories.Thread_Store;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Recon_Thread implements Runnable
{
	 public JdbcTemplate Jdbctemplate;
	 
	 @Autowired
	 public void setJdbctemplate(HikariDataSource Datasource) 
	 {
		Jdbctemplate = new JdbcTemplate(Datasource);
	 }
	 
	 private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
		
	 public String Thread_Name;
	 public String SYSCODE; 
	 public String JOBCODE;
	 public String FREQINSEC;
	 public String EXECMETHOD;
	 public String METHODNAME;
	 public String INPUTPARAM;
	
	 public Recon_Thread()  { }
	
	 public Recon_Thread(Thread_Store Thread) 
	 {
		 SYSCODE     = Thread.getSYSCODE(); 
		 JOBCODE     = Thread.getJOBCODE();
    	 Thread_Name = Thread.getThread_Name();
    	 FREQINSEC   = Thread.getFREQINSEC();
    	 EXECMETHOD  = Thread.getEXECMETHOD();
    	 METHODNAME  = Thread.getMETHODNAME();
    	 INPUTPARAM  = Thread.getINPUTPARAM();
    	 Jdbctemplate = Thread.getJdbctemplate();
	 }
	 
	 public void run() 
	 {
		try 
		{   	
			 while(true)
			 {
	             Job_Finder(Thread_Name, SYSCODE);
	            
	             long sleep_time = 1000L * Long.valueOf(FREQINSEC).longValue();  
	            
	        	 Thread.sleep(sleep_time);  
			 }
		}
		catch (Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage()); 
		}
    }
	
	public void Job_Finder(String Thread_Name, String SYSCODE)
	{
		List<Thread_Store> Jobs = new ArrayList<Thread_Store>();

		try 
		{
			String Sql = "select t1.jobcode, t1.execmethod, t1.refno, t1.methodname, t2.INPUTPARAM, t2.OUTPUTPARAM, t2.PARAMETER1, t2.PARAMETER2 "+
						 "from job003 t1, job002 t2 where t1.JOBCODE = t2.JOBCODE and t1.METHODNAME = t2.METHODNAME and t1.SYSCODE = t2.SYSCODE and "+
						 "t1.suborgcode=? and t1.Syscode=? and t1.execode=? and ROWNUM=?";
			
			Jobs = Jdbctemplate.query(Sql, new Object[] { "EXIM", SYSCODE, Thread_Name, "1" }, new Thread_Info_Mapper());
			
			for(int i=0;i<Jobs.size();i++)
			{
				String JOBCODE = Jobs.get(i).getJOBCODE();
				String EXECMETHOD = Jobs.get(i).getEXECMETHOD();
				String METHODNAME = Jobs.get(i).getMETHODNAME();
				String INPUTPARAM = Jobs.get(i).getPARAMETER1();
				String OUTPUTPARAM = Jobs.get(i).getPARAMETER2();
				
				Method_Finder(EXECMETHOD, METHODNAME,  INPUTPARAM, OUTPUTPARAM, SYSCODE, JOBCODE, Thread_Name);
			}
		}
		catch (Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage()); 
		} 
	}
	
	public void Method_Finder(String EXECMETHOD, String METHODNAME, String INPUTPARAM, String OUTPUTPARAM, String SYSCODE, String JOBCODE, String EXECODE) 
	{
		 boolean flag = false;
		 
		 if(EXECMETHOD.equals("JAVA"))  
         {
     	    System.out.println("EXECMETHOD is Java");
     	  
     	    String output = Execute_Java_Method(METHODNAME, INPUTPARAM, OUTPUTPARAM);
     	    
     	    if(output !=null ) flag = true;
     	}
     	else
     	{
     		System.out.println("EXECMETHOD is ORACLE");
     		
	    	String output = Execute_Oracle_Method(METHODNAME, INPUTPARAM, OUTPUTPARAM);
	    	
	    	if(output !=null ) flag = true;
	    	
	    	if(flag)
	     	{
	     		Update_Executoor002(SYSCODE, Thread_Name, FREQINSEC, "GOOD");
	     		
	     		Delete_Threads_from_Job003(SYSCODE, JOBCODE, EXECODE);
	     	}
     	}  
	}
	
	public String Execute_Java_Method(String METHODNAME, String INPUTPARAM, String OUTPUTPARAM) 
	{
		try 
		{
			Recon_Thread obj = new Recon_Thread();
			
			Method method = obj.getClass().getMethod(METHODNAME, String.class);
			
			OUTPUTPARAM = (String) method.invoke(obj,INPUTPARAM);	
		}
		catch (Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage()); 
		}
		
		return OUTPUTPARAM;
	}
	
	public String Execute_Oracle_Method(String Proc_Name, final String INPUTPARAM, String OUTPUTPARAM)
	{
 		try
 		{
 			if(Proc_Name.toLowerCase().contains("recon"))
 			{
 	 			List<SqlParameter> inParamMap = new ArrayList<SqlParameter>();
 	 			
 	 			inParamMap.add(new SqlParameter("i_recondate"  , Types.VARCHAR));
 	 			inParamMap.add(new SqlOutParameter("o_result"  , Types.VARCHAR));	
 	 	 
 	 			final String procedureCall = "{CALL "+Proc_Name+"(?,?)}";
 	 			
 	 			Map<String, Object> resultMap = Jdbctemplate.call(new CallableStatementCreator() {
 	 	 
 						public CallableStatement createCallableStatement(Connection connection) throws SQLException {
 	 
 							CallableStatement CS = connection.prepareCall(procedureCall);
 							CS.setString(1, INPUTPARAM);
 							CS.registerOutParameter(2, Types.VARCHAR);
 							return CS;
 						}
 	 				}, inParamMap);
 	 				
 		        OUTPUTPARAM = resultMap.get("o_result") != null ? resultMap.get("o_result").toString() : ""; 
 			}
 		 }
 		 catch(Exception e)
 		 {
 			logger.debug("Exception :::: "+e.getLocalizedMessage()); 
 		 }
 		
		 return OUTPUTPARAM;
	}
	
	public void Update_Executoor002(String SYSCODE, String EXECODE, String FREQINSEC, String Health)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss.SSS aa");  
		
		Calendar now = Calendar.getInstance();
		
		try 
		{
			String Start_Time = df.format(now.getTime()); //System.out.println("1# Last Run  At ::::: " + Start_Time);
			
			now.add(Calendar.SECOND, Integer.parseInt(FREQINSEC));
			
			String End_Time = df.format(now.getTime()); //System.out.println("1# Next Run At ::::: " + End_Time);
			
			String Sql =  "update executor002 set LASTRUN=?, NEXTRUN=?, HEALTH=? where SYSCODE=? and EXECODE=?";
			
			Jdbctemplate.update(Sql, new Object[] { Start_Time, End_Time, Health , SYSCODE, EXECODE});
		}
		catch (Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage()); 
		} 
	}
	
	public List<Thread_Store> Get_Threads()
	{
		List<Thread_Store> Threads = new ArrayList<Thread_Store>();

		try 
		{
			String Sql = "Select * from executor001 where SYSCODE=? and EXETYPE=? and Disabled=?";
			
			Threads = Jdbctemplate.query(Sql, new Object[] { "HP", "Event", "0" }, new Thread_Mapper());
		}
		catch(Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage()); 
		} 
		
		return Threads;
	}
		
	public void Delete_Threads_from_Job003(String SYSCODE, String JOBCODE, String EXECODE)
	{
		try 
		{
			String Sql = "Delete FROM JOB003 where SYSCODE=? and JOBCODE=? and EXECODE=?";
			
			Jdbctemplate.update(Sql, new Object[] { SYSCODE, JOBCODE, EXECODE });
		}
		catch (Exception e) 
		{
			logger.debug("Exception :::: "+e.getLocalizedMessage()); 
		} 
	}
	
	private class Thread_Mapper implements RowMapper<Thread_Store> 
    {
		public Thread_Store mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Thread_Store SQL = new Thread_Store();
			
			SQL.setSYSCODE(rs.getString("SYSCODE"));
			SQL.setThread_Name(rs.getString("EXECODE"));
			SQL.setJOBCODE(rs.getString("JOBCODE"));
			SQL.setFREQINSEC(rs.getString("FREQINSEC"));
			SQL.setJdbctemplate(Jdbctemplate);
			
			return SQL;
		}
    }
    
    private class Thread_Info_Mapper implements RowMapper<Thread_Store> 
    {
		public Thread_Store mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Thread_Store SQL = new Thread_Store();
			
			SQL.setJOBCODE(rs.getString("JOBCODE"));
			SQL.setEXECMETHOD(rs.getString("EXECMETHOD"));
			SQL.setMETHODNAME(rs.getString("METHODNAME"));
			SQL.setINPUTPARAM(rs.getString("INPUTPARAM"));
			SQL.setOUTPUTPARAM(rs.getString("OUTPUTPARAM"));
			SQL.setPARAMETER1(rs.getString("PARAMETER1"));
			SQL.setPARAMETER2(rs.getString("PARAMETER2"));
			
			return SQL;
		}
    }
}
