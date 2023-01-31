package com.hdsoft.hdpay.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.hdsoft.hdpay.Repositories.PAY_002;
import com.hdsoft.hdpay.Repositories.Request_002;
import com.hdsoft.hdpay.Repositories.Transactions;
import com.hdsoft.utils.FormatUtils;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Reversal_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public JsonObject Retrieve_Transaction_Information(Transactions Info) 
	{
		JsonObject details = new JsonObject();
		
		JsonObject Additional = new JsonObject();
		
		try
		{
			 Common_Utils util = new Common_Utils();
			
			 String sql = "select * from request002 w where w.SUBORGCODE=? and w.SYSCODE = ? and w.CHCODE=? and w.paytype = ? and w.reqdate = ? and (w.REQREFNO = ? or w.TRANREFNO= ? or w.payerid = ? and w.payeeref = ?)"; 
					
			 List<Request_002> INFO = Jdbctemplate.query(sql, new Object[] { "EXIM", "HP", Info.getCHCODE(), Info.getPAYTYPE(), FormatUtils.dynaSQLDate(Info.getTRANDATE(),"YYYY-MM-DD"), Info.getCHREFNO(), Info.getCHREFNO(), Info.getCHREFNO(), Info.getCHREFNO() }, new Request_002_Mapper());
			
			 if(INFO.size() !=0)
			 {
				 sql = "Select * from PAY002 where SUBORGCODE=? and CHCODE=? and PAYTYPE=? and TRANREFNO=? and REQSL=? and PAYDATE=?";
				  
				 List<PAY_002> Rev_Info = Jdbctemplate.query(sql, new Object[] {"EXIM", Info.getCHCODE(), Info.getPAYTYPE(), INFO.get(0).getTRANREFNO(), INFO.get(0).getREQSL(), FormatUtils.dynaSQLDate(INFO.get(0).getPAYDATE(),"YYYY-MM-DD") }, new Pay002_Mapper());
		    	
				 sql = "Select w.*, to_char(W.TRANDATE,'DD-MM-YYYY') TRANDATE2 from transactions w where w.SUBORGCODE=? and w.CHCODE=? and w.SYSCODE=? and w.TRANTYPE=? and w.PAYTYPE=? and w.CHREFNO=? and trunc(w.TRANDATE)=? order by transeq,legsl";
				 	
		 		 List<Transactions> Transactions_Info = Jdbctemplate.query(sql, new Object[] { "EXIM", Info.getCHCODE(), "HP", "TRANSFER", Info.getPAYTYPE(), Info.getCHREFNO(), FormatUtils.dynaSQLDate(Info.getTRANDATE(),"YYYY-MM-DD") }, new Transaction_Mapper());
		 		
		 		 List<String> Actual_PayTypes = new ArrayList<String>(); 
		 		 
		 		 List<Transactions> Credit_Info = new ArrayList<Transactions>();
				 
	 			 List<Transactions> Debit_Info = new ArrayList<Transactions>();
			    
	 			 for(int x=0;x<Transactions_Info.size();x++)
		 	     {
			    	 String DBCR = Transactions_Info.get(x).getDBCR();

			    	 if(DBCR.equals("D")) {  Debit_Info.add(Transactions_Info.get(x));  }
			 
			    	 if(DBCR.equals("C")) {  Credit_Info.add(Transactions_Info.get(x)); }
		 	     }
	 			 
				 String Hold_Approval = ""; String Rev_Approval = ""; boolean Rev_Flag = false;
				 
	 			 List<Transactions> Trans = new ArrayList<Transactions>();
	 			
	 			 for(int y=0;y<Debit_Info.size();y++)  
				 {
	 				 String TranRef = INFO.get(0).getTRANREFNO();
	 				 
	 				 String DBAccount = Debit_Info.get(y).getLEDGERNO();
					 String CRAccount = Credit_Info.get(y).getLEDGERNO();
					 
					 if(util.isNullOrEmpty(DBAccount))
					 {
						  DBAccount = Debit_Info.get(y).getACCOUNTNO();
					 }

					 if(util.isNullOrEmpty(CRAccount))
					 {
						  CRAccount = Credit_Info.get(y).getACCOUNTNO();
					 }
					 
					 Debit_Info.get(y).setDEBITAC(DBAccount);
					 Debit_Info.get(y).setCREDITAC(CRAccount);
					 Debit_Info.get(y).setTRANREFNO(TranRef);
					 Debit_Info.get(y).setSERIAL(y+1);
					 Debit_Info.get(y).setFLOW(INFO.get(0).getFLOW());
					 Debit_Info.get(y).setREQREFNO(INFO.get(0).getREQREFNO());
					 Debit_Info.get(y).setPAYERID(INFO.get(0).getPAYERID());
					 Debit_Info.get(y).setPAYEEREF(INFO.get(0).getPAYEEREF());
					 
					 String Amount_Type = Find_Amount_Types(Debit_Info.get(y), INFO.get(0)).get("Amount_Type").getAsString();
 
					 Debit_Info.get(y).setAMOUNTTYPE(Amount_Type);
					 
					 Actual_PayTypes.add(Amount_Type);
					 
					 String STATUS = INFO.get(0).getSTATUS();
					 String RESCODE = INFO.get(0).getRESCODE();
					 //String RESPDESC = INFO.get(0).getRESPDESC();
					  
					 String HOLDSTATUS = "";  String REVSTATUS = "";  String REV_REASON = ""; String HOLDREJREASON = ""; String REVREJREASON = "";					
					 
					 if(STATUS.equalsIgnoreCase("SUCCESS") && RESCODE.equalsIgnoreCase("200"))
					 {
						 Debit_Info.get(y).setPAYSTATUS(STATUS);
					 }
					 else if(STATUS.equalsIgnoreCase("SUCCESS") && RESCODE.equalsIgnoreCase("202"))
					 {
						 Debit_Info.get(y).setPAYSTATUS("PENDING");
					 }
					 else
					 {
						 Debit_Info.get(y).setPAYSTATUS("FAILED");
					 }
					
					 if(Rev_Info.size() !=0)
					 {
						 HOLDSTATUS = Rev_Info.get(0).getHOLD_STATE();  
						 
						 Rev_Flag = true;
						 
						 if(HOLDSTATUS.equalsIgnoreCase("REQUESTED"))
						 {
							 Debit_Info.get(y).setHOLDSTATUS("INITIATED");
						 }
						 else if(HOLDSTATUS.equalsIgnoreCase("CONFIRMED_HOLD"))
						 {
							 Debit_Info.get(y).setHOLDSTATUS("SUCCESS");
						 }
						 else if(HOLDSTATUS.equalsIgnoreCase("REJECTED"))
						 {
							 Debit_Info.get(y).setHOLDSTATUS("REJECTED");
						 }
						 else
						 {
							 Debit_Info.get(y).setHOLDSTATUS(HOLDSTATUS);
						 }
						 
						 Hold_Approval = Rev_Info.get(0).getISHOLDAPPROVED();
						 
						 if(Hold_Approval.equals("0"))
						 {
							 Hold_Approval = "Pending";
						 }
						 else if(Hold_Approval.equals("1"))
						 {
							 Hold_Approval = "Approved";
						 }
						 else
						 {
							 Hold_Approval = "Rejected";
						 }
						 
						 REVSTATUS = Debit_Info.get(y).getSTATUS();
						 
						 if(REVSTATUS.equalsIgnoreCase("REVERSED"))
						 {
							 Debit_Info.get(y).setREVSTATUS("SUCCESS");
						 }
						 else if(REVSTATUS.equalsIgnoreCase("RV_PENDING"))
						 {
							 Debit_Info.get(y).setREVSTATUS("PENDING");
						 }
						 else
						 {
							 Debit_Info.get(y).setREVSTATUS("PENDING");
						 }
						 
						 Rev_Approval = Rev_Info.get(0).getISREVAPPROVED();
						 
						 if(Rev_Approval.equals("0"))
						 {
							 Rev_Approval = "Pending";
						 }
						 else if(Rev_Approval.equals("1"))
						 {
							 Rev_Approval = "Approved";
						 }
						 else
						 {
							 Rev_Approval = "Rejected";
						 }
						 
						 REV_REASON = Rev_Info.get(0).getHOLD_REASON();		 
						 HOLDREJREASON = Rev_Info.get(0).getHOLDREJREASON();
						 REVREJREASON = Rev_Info.get(0).getREVREJREASON();
					 }
					 else
					 {
						 Debit_Info.get(y).setHOLDSTATUS(HOLDSTATUS);
						 Debit_Info.get(y).setREVSTATUS(REVSTATUS);
					 }
					 
					 Trans.add(Debit_Info.get(y));
					 
					 Additional.addProperty("Rev_Flag", Rev_Flag);
					 Additional.addProperty("Hold_Status", Hold_Approval);
					 Additional.addProperty("Rev_Status", Rev_Approval);
					 Additional.addProperty("REQSL", INFO.get(0).getREQSL());
					 Additional.addProperty("FLOW", INFO.get(0).getFLOW());
					 Additional.addProperty("REV_REASON", REV_REASON);
					 Additional.addProperty("HOLDREJREASON", HOLDREJREASON);
					 Additional.addProperty("REVREJREASON", REVREJREASON);
				 }
	 			 
	 			 details.add("Informations", new Gson().toJsonTree(Trans));
	 			 
	 			 details.add("Additional", Additional);
	 			
	 			 JsonArray Possible_PayTypes = Possible_Reversal_Amount_Types(Info.getPAYTYPE(), Actual_PayTypes).get("Amount_Types").getAsJsonArray(); 
				
				 details.add("Possible_PayTypes", Possible_PayTypes);
			 }
			
			 details.addProperty("Result", INFO.size() !=0 ? "Success" : "Failed");
			 details.addProperty("Message", INFO.size() !=0 ? "Transaction details found !!" : "Transaction details not found !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Retrieve_Transaction_Information :::: "+e.getLocalizedMessage());
		 }
		
		 return details;
	}
	
	public JsonObject Find_Amount_Types(Transactions info, Request_002 tran) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String Amount_Type = "";
			 
			 if(tran.getTRANAMT().equals(info.getSYSAMOUNT()))
			 {
				 int cnt = 0;
				 
				 if(info.getFLOW().equalsIgnoreCase("O"))
				 {
					 String sql = "select count(*) from ledgermap w, paygate001 v where w.program = v.paygatecd and w.program = ? and (w.craccount = ? or w.crgl = ?) and w.DCFLG=? and v.reversal = ?"; 
					 		 
					 cnt = Jdbctemplate.queryForObject(sql, new Object[] { tran.getPAYTYPE(), info.getCREDITAC(), info.getCREDITAC(), "C", "1" }, Integer.class);
				 }
				 else
				 {
					 String sql = "select count(*) from ledgermap w, paygate001 v where w.program = v.paygatecd and w.program = ? and (w.dbaccount = ? or w.dbgl = ?) and DCFLG=? and v.reversal = ?"; 
			 		 
					 cnt = Jdbctemplate.queryForObject(sql, new Object[] { tran.getPAYTYPE(), info.getCREDITAC(), info.getDEBITAC(), "D", "1" }, Integer.class);
				 }
				 
				 if(cnt !=0)
				 {
					 Amount_Type = "PRINCIPLE";
				 }
			 }
			 else
			 {
				 List<String> Amnt_type = new ArrayList<String>();
				 
				 if(info.getFLOW().equalsIgnoreCase("O"))
				 {
					 String sql = "select ACNAME from charge002 w, paygate001 v where w.hosttype = v.paygatecd and w.hosttype = ? and (w.craccount = ? or w.crgl = ?) and v.REVERSAL_CHRG = ?"; 
					 		 
					 Amnt_type = Jdbctemplate.queryForList(sql, new Object[] { tran.getPAYTYPE(), info.getCREDITAC(), info.getCREDITAC(), "1" }, String.class);
				 }
				 else
				 {
					 String sql = "select ACNAME from charge002 w, paygate001 v where w.hosttype = v.paygatecd and w.hosttype = ? and (w.dbaccount = ? or w.dbgl = ?) and v.REVERSAL_CHRG = ?"; 
			 		 
					 Amnt_type = Jdbctemplate.queryForList(sql, new Object[] { tran.getPAYTYPE(), info.getCREDITAC(), info.getDEBITAC(), "1" }, String.class);
				 }
				 
				 if(Amnt_type.size() !=0)
				 {
					 Amount_Type = Amnt_type.get(0);
				 }
			 }
			 
			 details.addProperty("Amount_Type", Amount_Type);
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "details found !!");
		}
		catch(Exception e)
		{
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Find_Amount_Types :::: "+e.getLocalizedMessage());
		}
		
		return details;	
	}
	
	public JsonObject Possible_Reversal_Amount_Types(String PAYTYPE, List<String> Actual_PayTypes) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 List<String> final_pay =  new ArrayList<String>(); List<String> all =  new ArrayList<String>();
			
			 String sql = "select count(*) from paygate001 v where v.paygatecd = ? and v.reversal = ?"; 
			 		 
			 int cnt = Jdbctemplate.queryForObject(sql, new Object[] { PAYTYPE, "1" }, Integer.class);
		
			 if(cnt !=0)
			 {
				 all.add("PRINCIPLE");
			 }
			 
			 sql = "select ACNAME from charge002 w, paygate001 v where w.hosttype = v.paygatecd and w.hosttype = ? and v.REVERSAL_CHRG = ?"; 
				
			 List<String> Info = Jdbctemplate.queryForList(sql, new Object[] { PAYTYPE, "1" }, String.class);
			 
			 for(String Pay : Info)
			 {
				 all.add(Pay);
			 }
			 
			 for(String Type : all)
			 {
				 if(Actual_PayTypes.contains(Type))
				 {
					 final_pay.add(Type);
				 }
			 }
			 
			 details.add("Amount_Types", new Gson().toJsonTree(final_pay));
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "details found !!");
		}
		catch(Exception e)
		{
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
			 
			 logger.debug("Exception in Retrieve_Transaction_Information :::: "+e.getLocalizedMessage());
		}
		
		return details;
	
	}
	
	public class Transaction_Mapper implements RowMapper<Transactions> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Transactions mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Transactions Info = new Transactions();  
			
			Info.setSERIAL(rowNum+1);
			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));
			Info.setTRANTYPE(util.ReplaceNull(rs.getString("TRANTYPE")));
			Info.setTRANCODE(util.ReplaceNull(rs.getString("TRANCODE")));
			Info.setSYSTEMDATE(util.ReplaceNull(rs.getString("SYSTEMDATE")));
			Info.setTRANSEQ(util.ReplaceNull(rs.getString("TRANSEQ")));
			Info.setLEGSL(util.ReplaceNull(rs.getString("LEGSL")));
			Info.setBRANCHCD(util.ReplaceNull(rs.getString("BRANCHCD")));
			Info.setTRANDATE(util.ReplaceNull(rs.getString("TRANDATE2")));
			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));
			Info.setCHREFNO(util.ReplaceNull(rs.getString("CHREFNO")));
			Info.setDBCR(util.ReplaceNull(rs.getString("DBCR")));
			Info.setAMOUNT(util.ReplaceNull(rs.getString("AMOUNT")));
			Info.setTRANCURR(util.ReplaceNull(rs.getString("TRANCURR")));
			Info.setSYSAMOUNT(util.ReplaceNull(rs.getString("SYSAMOUNT")));
			Info.setSYSCURR(util.ReplaceNull(rs.getString("SYSCURR")));
			Info.setACCOUNTNO(util.ReplaceNull(rs.getString("ACCOUNTNO")));
			Info.setIBAN(util.ReplaceNull(rs.getString("IBAN")));
			Info.setSWIFTCD(util.ReplaceNull(rs.getString("SWIFTCD")));
			Info.setLEDGERNO(util.ReplaceNull(rs.getString("LEDGERNO")));
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));
			Info.setCHARGES(util.ReplaceNull(rs.getString("CHARGES")));
			Info.setCHGCODE(util.ReplaceNull(rs.getString("CHGCODE")));
			Info.setREMARKS(util.ReplaceNull(rs.getString("REMARKS")));
			Info.setEUSER(util.ReplaceNull(rs.getString("EUSER")));
			Info.setEDATE(util.ReplaceNull(rs.getString("EDATE")));
			Info.setAUSER(util.ReplaceNull(rs.getString("AUSER")));
			Info.setADATE(util.ReplaceNull(rs.getString("ADATE")));
			Info.setCUSER(util.ReplaceNull(rs.getString("CUSER")));
			Info.setCDATE(util.ReplaceNull(rs.getString("CDATE")));
			Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS")));
			Info.setINVOICENO(util.ReplaceNull(rs.getString("INVOICENO")));
			Info.setSYSCODE(util.ReplaceNull(rs.getString("SYSCODE")));
			
			return Info;
		}
     }
	
	public class Request_002_Mapper implements RowMapper<Request_002> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Request_002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Request_002 Info = new Request_002(); 
			
			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));         
			Info.setSYSCODE(util.ReplaceNull(rs.getString("SYSCODE")));         
			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));             
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));            
			Info.setFLOW(util.ReplaceNull(rs.getString("FLOW")));       
			Info.setREQDATE(util.ReplaceNull(rs.getString("REQDATE")));  	
			Info.setREQREFNO(util.ReplaceNull(rs.getString("REQREFNO"))); 
			Info.setTRANREFNO(util.ReplaceNull(rs.getString("TRANREFNO"))); 
			Info.setREQSL(util.ReplaceNull(rs.getString("REQSL"))); 
			Info.setREQTIME2(util.ReplaceNull(rs.getString("REQTIME")));         
			Info.setPAYDATE(util.ReplaceNull(rs.getString("PAYDATE")));  
			Info.setSTATUS(util.ReplaceNull(rs.getString("STATUS"))); 
			Info.setERRCD(util.ReplaceNull(rs.getString("ERRCD")));   
			Info.setERRDESC(util.ReplaceNull(rs.getString("ERRDESC")));   
			Info.setRESCODE(util.ReplaceNull(rs.getString("RESCODE"))); 
			Info.setRESPDESC(util.ReplaceNull(rs.getString("RESPDESC")));
			Info.setTRANAMT(util.ReplaceNull(rs.getString("TRANAMT"))); 
			Info.setTRANCURR(util.ReplaceNull(rs.getString("TRANCURR"))); 	
			Info.setREQTYPE(util.ReplaceNull(rs.getString("REQTYPE")));
			Info.setSERPORCD(util.ReplaceNull(rs.getString("SERPORCD"))); 	  
			Info.setDEBITAMT(util.ReplaceNull(rs.getString("DEBITAMT")));        
			Info.setDEBITCURR(util.ReplaceNull(rs.getString("DEBITCURR"))); 
			Info.setCREDITAMT(util.ReplaceNull(rs.getString("CREDITAMT")));       
			Info.setCREDITCURR(util.ReplaceNull(rs.getString("CREDITCURR")));     
			Info.setS_ACCOUNT(util.ReplaceNull(rs.getString("S_ACCOUNT")));       
			Info.setD_ACCOUNT(util.ReplaceNull(rs.getString("D_ACCOUNT")));       
			Info.setS_BANKBIC(util.ReplaceNull(rs.getString("S_BANKBIC")));       
			Info.setD_BANKBIC(util.ReplaceNull(rs.getString("D_BANKBIC")));           
			Info.setS_ACNAME(util.ReplaceNull(rs.getString("S_ACNAME")));        
			Info.setD_ACNAME(util.ReplaceNull(rs.getString("D_ACNAME")));
			Info.setINITIATOR_CIF(util.ReplaceNull(rs.getString("INITIATOR_CIF"))); 
			Info.setS_CLIENTNO(util.ReplaceNull(rs.getString("S_CLIENTNO")));      
			Info.setD_CLIENTNO(util.ReplaceNull(rs.getString("D_CLIENTNO"))); 	
			Info.setFEEAMT1(util.ReplaceNull(rs.getString("FEEAMT1")));         
			Info.setFEECURR1(util.ReplaceNull(rs.getString("FEECURR1")));        
			Info.setFEEAMT2(util.ReplaceNull(rs.getString("FEEAMT2")));         
			Info.setFEECURR2(util.ReplaceNull(rs.getString("FEECURR2")));        
			Info.setFEEAMT3(util.ReplaceNull(rs.getString("FEEAMT3")));         
			Info.setFEECURR3(util.ReplaceNull(rs.getString("FEECURR3")));        
			Info.setFEEAMT4(util.ReplaceNull(rs.getString("FEEAMT4")));         
			Info.setFEECURR4(util.ReplaceNull(rs.getString("FEECURR4"))); 
			Info.setINVOICENO(util.ReplaceNull(rs.getString("INVOICENO")));       
			Info.setS_IBAN(util.ReplaceNull(rs.getString("S_IBAN")));          
			Info.setD_IBAN(util.ReplaceNull(rs.getString("D_IBAN")));  
			Info.setS_BRNCODE(util.ReplaceNull(rs.getString("S_BRNCODE")));       
			Info.setD_BRNCODE(util.ReplaceNull(rs.getString("D_BRNCODE")));       
			Info.setS_ACTYPE(util.ReplaceNull(rs.getString("S_ACTYPE")));        
			Info.setD_ACTYPE(util.ReplaceNull(rs.getString("D_ACTYPE")));        
			Info.setS_ADDRESS(util.ReplaceNull(rs.getString("S_ADDRESS")));       
			Info.setD_ADDRESS(util.ReplaceNull(rs.getString("D_ADDRESS")));       
			Info.setS_EMAILID(util.ReplaceNull(rs.getString("S_EMAILID")));       
			Info.setD_EMAILID(util.ReplaceNull(rs.getString("D_EMAILID")));       
			Info.setS_MOBILE(util.ReplaceNull(rs.getString("S_MOBILE")));        
			Info.setD_MOBILE(util.ReplaceNull(rs.getString("D_MOBILE")));        
			Info.setS_TELEPHONE(util.ReplaceNull(rs.getString("S_TELEPHONE")));     
			Info.setD_TELEPHONE(util.ReplaceNull(rs.getString("D_TELEPHONE")));    
			Info.setS_IDENTIFIERTYPE(util.ReplaceNull(rs.getString("S_IDENTIFIERTYPE")));
			Info.setD_IDENTIFIERTYPE(util.ReplaceNull(rs.getString("D_IDENTIFIERTYPE")));
			Info.setS_ACCATEGORY(util.ReplaceNull(rs.getString("S_ACCATEGORY")));    
			Info.setD_ACCATEGORY(util.ReplaceNull(rs.getString("D_ACCATEGORY"))); 
			Info.setS_FSPID(util.ReplaceNull(rs.getString("S_FSPID")));         
			Info.setD_FSPID(util.ReplaceNull(rs.getString("D_FSPID")));   
			Info.setS_IDENTIFYTYPE(util.ReplaceNull(rs.getString("S_IDENTIFYTYPE")));  
			Info.setD_IDENTIFYTYPE(util.ReplaceNull(rs.getString("D_IDENTIFYTYPE")));  
			Info.setS_IDENTIFYVALUE(util.ReplaceNull(rs.getString("S_IDENTIFYVALUE"))); 
			Info.setD_IDENTIFYVALUE(util.ReplaceNull(rs.getString("D_IDENTIFYVALUE"))); 
			Info.setD_RECEIVERID(util.ReplaceNull(rs.getString("D_RECEIVERID")));  
			Info.setSENDER_INFO(util.ReplaceNull(rs.getString("SENDER_INFO")));     
			Info.setRECEIVER_INFO(util.ReplaceNull(rs.getString("RECEIVER_INFO"))); 
			Info.setPAYEEREF(util.ReplaceNull(rs.getString("PAYEEREF")));
			Info.setLONGITUDE(util.ReplaceNull(rs.getString("LONGITUDE")));
			Info.setLATITUDE(util.ReplaceNull(rs.getString("LATITUDE")));
			Info.setIPADDRESS(util.ReplaceNull(rs.getString("IPADDRESS")));
			Info.setDEVICEID(util.ReplaceNull(rs.getString("DEVICEID")));
			Info.setLOCATION(util.ReplaceNull(rs.getString("LOCATION")));
			Info.setPURPOSECD(util.ReplaceNull(rs.getString("PURPOSECD")));
			Info.setPURSPOSEDESC(util.ReplaceNull(rs.getString("PURSPOSEDESC")));
			Info.setOTPPASSED(util.ReplaceNull(rs.getString("OTPPASSED")));
			Info.setUSERAGENT(util.ReplaceNull(rs.getString("USERAGENT")));
			Info.setSESSIONID(util.ReplaceNull(rs.getString("SESSIONID")));
			Info.setPROCTIME(util.ReplaceNull(rs.getString("PROCTIME")));
			Info.setINITATEDBY(util.ReplaceNull(rs.getString("INITATEDBY")));
			Info.setFRAUDCHK(util.ReplaceNull(rs.getString("FRAUDCHK")));
			Info.setPSPCODE(util.ReplaceNull(rs.getString("PSPCODE")));
			Info.setSPCODE(util.ReplaceNull(rs.getString("SPCODE")));
			Info.setSUBSPCODE(util.ReplaceNull(rs.getString("SUBSPCODE")));
			Info.setRECEIPTNO(util.ReplaceNull(rs.getString("RECEIPTNO")));
			Info.setAMOUNTTYPE(util.ReplaceNull(rs.getString("AMOUNTTYPE")));
			Info.setCALLBACKURL(util.ReplaceNull(rs.getString("CALLBACKURL")));
			Info.setPAYERID(util.ReplaceNull(rs.getString("PAYERID")));
			Info.setTRANTYPE(util.ReplaceNull(rs.getString("TRANTYPE")));
			Info.setCUSTMSISDN(util.ReplaceNull(rs.getString("CUSTMSISDN")));
			Info.setVERSION(util.ReplaceNull(rs.getString("VERSION")));
			Info.setREMARKS(util.ReplaceNull(rs.getString("REMARKS")));
			Info.setSWITCHREF(util.ReplaceNull(rs.getString("SWITCHREF")));
			Info.setS_SENDERID(util.ReplaceNull(rs.getString("S_SENDERID")));
					
			return Info;
		}
    }
    
    public class Pay002_Mapper implements RowMapper<PAY_002> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public PAY_002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			PAY_002 Info = new PAY_002();  

			Info.setSUBORGCODE(util.ReplaceNull(rs.getString("SUBORGCODE")));         
			Info.setCHCODE(util.ReplaceNull(rs.getString("CHCODE")));             
			Info.setPAYTYPE(util.ReplaceNull(rs.getString("PAYTYPE")));  
			Info.setREQDATE(util.ReplaceNull(rs.getString("REQDATE")));   
			Info.setREQREFNO(util.ReplaceNull(rs.getString("REQREFNO")));  
			Info.setTRANREFNO(util.ReplaceNull(rs.getString("TRANREFNO")));    
			Info.setREQSL(util.ReplaceNull(rs.getString("REQSL")));   
			Info.setREQTIME(util.ReplaceNull(rs.getString("REQTIME")));         
			Info.setPAYDATE(util.ReplaceNull(rs.getString("PAYDATE")));         
			Info.setTRANTYPE(util.ReplaceNull(rs.getString("TRANTYPE")));      			
			Info.setINITIATEBY(util.ReplaceNull(rs.getString("INITIATEBY")));               
			Info.setISREVERSED(util.ReplaceNull(rs.getString("ISREVERSED")));        
			Info.setREV_DATE(util.ReplaceNull(rs.getString("REV_DATE")));      
			Info.setORG_AMNT(util.ReplaceNull(rs.getString("ORG_AMNT")));         
			Info.setORG_CURR(util.ReplaceNull(rs.getString("ORG_CURR")));          
			Info.setORG_CHRGAMNT(util.ReplaceNull(rs.getString("ORG_CHRGAMNT")));           
			Info.setORG_CHRGCURR(util.ReplaceNull(rs.getString("ORG_CHRGCURR")));         
			Info.setORG_REFNO(util.ReplaceNull(rs.getString("ORG_REFNO")));         
			Info.setREV_AMOUNT(util.ReplaceNull(rs.getString("REV_AMOUNT")));        
			Info.setREV_CURR(util.ReplaceNull(rs.getString("REV_CURR")));        
			Info.setREV_CHRGAMOUNT(util.ReplaceNull(rs.getString("REV_CHRGAMOUNT")));         
			Info.setREV_CHRGCURR(util.ReplaceNull(rs.getString("REV_CHRGCURR")));        
			Info.setREV_REFNO(util.ReplaceNull(rs.getString("REV_REFNO")));       
			Info.setREV_TYPE(util.ReplaceNull(rs.getString("REV_TYPE")));       
			Info.setISFINAL_REV(util.ReplaceNull(rs.getString("ISFINAL_REV")));      
			Info.setREV_CODE(util.ReplaceNull(rs.getString("REV_CODE")));       
			Info.setREV_REASON(util.ReplaceNull(rs.getString("REV_REASON")));       
			Info.setISHOLD_MARKED(util.ReplaceNull(rs.getString("ISHOLD_MARKED")));          
			Info.setHOLD_AMOUNT(util.ReplaceNull(rs.getString("HOLD_AMOUNT")));          
			Info.setHOLD_CURR(util.ReplaceNull(rs.getString("HOLD_CURR")));        
			Info.setHOLD_REFNO(util.ReplaceNull(rs.getString("HOLD_REFNO")));       
			Info.setHOLD_DATE(util.ReplaceNull(rs.getString("HOLD_DATE")));       
			Info.setHOLD_REASON(util.ReplaceNull(rs.getString("HOLD_REASON")));        
			Info.setREV_STATE(util.ReplaceNull(rs.getString("REV_STATE")));        
			Info.setHOLD_STATE(util.ReplaceNull(rs.getString("HOLD_STATE")));   
			Info.setPAYERID(util.ReplaceNull(rs.getString("PAYERID")));      
			Info.setPAYEEID(util.ReplaceNull(rs.getString("PAYEEID")));      
			Info.setTRAN_SWITCHREF(util.ReplaceNull(rs.getString("TRAN_SWITCHREF")));         
			Info.setREV_SWITCHREF(util.ReplaceNull(rs.getString("REV_SWITCHREF")));        
			Info.setREV_STATUS(util.ReplaceNull(rs.getString("REV_STATUS")));         
			Info.setPAYER_REVREF(util.ReplaceNull(rs.getString("PAYER_REVREF")));        
			Info.setPAYEE_REVREF(util.ReplaceNull(rs.getString("PAYEE_REVREF")));         
			Info.setERRCD(util.ReplaceNull(rs.getString("ERRCD")));        
			Info.setERRDES(util.ReplaceNull(rs.getString("ERRDES")));  
			Info.setISHOLDAPPROVED(util.ReplaceNull(rs.getString("ISHOLDAPPROVED")));  
			Info.setISREVAPPROVED(util.ReplaceNull(rs.getString("ISREVAPPROVED")));  
			Info.setHOLDREJREASON(util.ReplaceNull(rs.getString("HOLDREJREASON")));  
			Info.setREVREJREASON(util.ReplaceNull(rs.getString("REVREJREASON")));  

			return Info;
		}
    }
}
