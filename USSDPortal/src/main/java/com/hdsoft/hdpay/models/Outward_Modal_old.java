package com.hdsoft.hdpay.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.common.webservicecall;
import com.hdsoft.hdpay.Repositories.Job_005;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Request_002;
import com.hdsoft.hdpay.Repositories.Response_001;
import com.hdsoft.hdpay.Repositories.web_service_001;
import com.hdsoft.hdpay.Repositories.web_service_002;
import com.zaxxer.hikari.HikariDataSource;

public class Outward_Modal_old 
{
	public static JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	public Outward_Modal_old(JdbcTemplate jdbctemplate) 
	{ 
		Jdbctemplate = jdbctemplate;
	}
	
	public Outward_Modal_old() { }
	
	public JsonObject Insert_Request_001(Request_001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into request001(SUBORGCODE,CHCODE,PAYTYPE,MSGTYPE,FLOW,REQDATE,REQTIME,UNIREFNO,MSGURL,IP,PORT,HEAD_MSG,BODY_MSG,REQBY,HASHVAL) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getMSGTYPE(), Info.getFLOW(), Info.getREQDATE(), Info.getREQTIME(), Info.getUNIREFNO(), 
					 Info.getMSGURL(),  Info.getIP(), Info.getPORT(), Info.getHEAD_MSG(), Info.getBODY_MSG(), Info.getREQBY(), Info.getHASHVAL()});
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Request added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
		 }
		
		 return details;
	}
	
	public JsonObject Insert_Job_005(Job_005 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into job005(SUBORGCODE,SYSCODE,CHCODE,PAYTYPE,REQDATE,REFNO,REQSL,STATUS) values(?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getSYSCODE(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getREQDATE(), Info.getREFNO(), 
					 Info.getREQSL(),  Info.getSTATUS() });
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Job added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
		 }
		
		 return details;
	}
	
	public JsonObject Insert_Request_002(Request_002 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into request002(SUBORGCODE,SYSCODE,CHCODE,PAYTYPE,FLOW,REQDATE,REQREFNO,REQSL,REQTIME,PAYDATE,STATUS,TRANAMT,"+
				 	"TRANCURR,REQTYPE,SERPORCD,DEBITAMT,DEBITCURR,CREDITAMT,CREDITCURR,S_ACCOUNT,D_ACCOUNT,S_BANKBIC,D_BANKBIC,S_ACNAME,D_ACNAME,INITIATOR_CIF,S_CLIENTNO,"+
					"D_CLIENTNO,FEEAMT1,FEECURR1,FEEAMT2,FEECURR2,FEEAMT3,FEECURR3,FEEAMT4,FEECURR4,INVOICENO,S_IBAN,D_IBAN,S_BRNCODE,D_BRNCODE,S_ACTYPE,D_ACTYPE,S_ADDRESS,"+
				 	"D_ADDRESS,S_EMAILID,D_EMAILID,S_MOBILE,D_MOBILE,S_TELEPHONE,D_TELEPHONE,SENDER_INFO,RECEIVER_INFO,PAYEEREF,LONGITUDE,LATITUDE,IPADDRESS,DEVICEID,"+
					"LOCATION,PURPOSECD,PURSPOSEDESC,OTPPASSED,USERAGENT,SESSIONID,PROCTIME,INITATEDBY,FRAUDCHK) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+
				 	"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(),Info.getSYSCODE(),Info.getCHCODE(),Info.getPAYTYPE(),Info.getFLOW(),Info.getREQDATE(),Info.getREQREFNO(),Info.getREQSL(),
					 Info.getREQTIME(),Info.getPAYDATE(),Info.getSTATUS(),Info.getTRANAMT(),Info.getTRANCURR(),Info.getREQTYPE(),
					 Info.getSERPORCD(),Info.getDEBITAMT(),Info.getDEBITCURR(),Info.getCREDITAMT(),Info.getCREDITCURR(),Info.getS_ACCOUNT(),Info.getD_ACCOUNT(),Info.getS_BANKBIC(),
					 Info.getD_BANKBIC(),Info.getS_ACNAME(),Info.getD_ACNAME(),Info.getINITIATOR_CIF(),Info.getS_CLIENTNO(),Info.getD_CLIENTNO(),Info.getFEECURR1(),Info.getFEEAMT2(),
					 Info.getFEEAMT3(),Info.getFEECURR3(),Info.getFEEAMT4(),Info.getFEECURR4(),Info.getINVOICENO(),Info.getS_IBAN(),Info.getD_IBAN(),Info.getS_BRNCODE(),
					 Info.getD_BRNCODE(),Info.getS_ACTYPE(),Info.getD_ACTYPE(),Info.getS_ADDRESS(),Info.getD_ADDRESS(),Info.getD_EMAILID(),Info.getD_MOBILE(),Info.getS_TELEPHONE(),
					 Info.getD_TELEPHONE(),Info.getSENDER_INFO(),Info.getRECEIVER_INFO(),Info.getPAYEEREF(),Info.getLONGITUDE(),Info.getIPADDRESS(),Info.getLOCATION(),Info.getPURPOSECD(),
					 Info.getPURSPOSEDESC(),Info.getOTPPASSED(),Info.getUSERAGENT(),Info.getSESSIONID(),Info.getPROCTIME(),Info.getINITATEDBY(),Info.getFRAUDCHK() } );
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Request added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
		 }
		
		 return details;
	}
	
	public JsonObject Update_Request_002(Request_002 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "update request002 set ERRCD=?,ERRDESC=?,RESCODE=?,RESPDESC=? where CHCODE=? and PAYTYPE=? and REQREFNO=?";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getERRCD(), Info.getERRDESC(), Info.getRESCODE(), Info.getRESPDESC(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getREQREFNO() } );
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Request updated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
		 }
		
		 return details;
	}
	
	public JsonObject Find_GEPG_Request() 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select * from request002 where PAYTYPE=?";
			   
			 List<Request_002> obj = Jdbctemplate.query(sql, new Object[] { "GEPG" }, new GEPG_Mapper());
			 
			 for(int i=0;i<obj.size();i++)
			 {
				 String SUBORGCODE = obj.get(i).getSUBORGCODE();
				 String CHCODE = obj.get(i).getCHCODE();
				 String PSPCODE = obj.get(i).getPSPCODE();
				 String SPCODE = obj.get(i).getSPCODE();
				 String PAYEEREF = obj.get(i).getPAYEEREF();
				 String REQREFNO = obj.get(i).getREQREFNO();
				 String INVOICENO = obj.get(i).getINVOICENO();
				 String CREDITAMT = obj.get(i).getCREDITAMT();
				 String CREDITCURR = obj.get(i).getCREDITCURR();
				 String D_ACCOUNT = obj.get(i).getD_ACCOUNT();
				 String S_MOBILE = obj.get(i).getS_MOBILE();
				 String S_ACNAME = obj.get(i).getS_ACNAME();
				 String S_EMAILID = obj.get(i).getS_EMAILID();
				 
				 Date currentDatetime = new Date(System.currentTimeMillis()); 
				 
				 String datetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(currentDatetime);
				  
				 String password = "passpass";
				 String alias = "gepgclient";
					
				 char[] passord = null;
				 PrivateKey privateKey = null;
					
			     File file = new File("C:\\gepg\\gepgclientprivatekey.pfx");
			     InputStream stream = new FileInputStream(file);
			     java.security.KeyStore keyStore = KeyStore.getInstance("PKCS12");
			     keyStore.load(stream, passord);
			     privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
				      
			     FileInputStream fin = new FileInputStream("C:\\gepg\\gepgclient.cer");
			     CertificateFactory f = CertificateFactory.getInstance("X.509");
			     X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
			     PublicKey pk = certificate.getPublicKey();
			    
			     String dgdata = "<gepgPmtPstReq>"+
							    	"<PspDtl>"+
							    		"<PspCode>"+PSPCODE+"</PspCode>"+
							    		"<ResultUrl>"+"http://192.168.3.84:18080/ITCallback/rest/call/send"+"</ResultUrl>"+
							    	 "</PspDtl>"+
							         "<PymtTrxInf>"+
										    "<TrxId>"+PAYEEREF+"</TrxId >"+
										    "<spCode>"+SPCODE+"</spCode>"+
										    "<PayRefId>"+REQREFNO+"</PayRefId>"+
										    "<BillCtrNum>"+INVOICENO+"</BillCtrNum>"+
										    "<PaidAmt>"+CREDITAMT+"</PaidAmt >"+
										    "<CCy>"+CREDITCURR+"</CCy>"+
										    "<CtrAccNum>"+D_ACCOUNT+"</CtrAccNum >"+
										    "<TrxDtTm>"+datetime+"</TrxDtTm>"+
										    "<UsdPayChnl>"+CHCODE+"</UsdPayChnl>"+
										    "<DptCellNum>"+S_MOBILE+"</DptCellNum >"+
										    "<DptName>"+S_ACNAME+"</DptName >"+
										    "<DptNameEmailAddr>"+S_EMAILID+"</DptNameEmailAddr>"+
									 "</PymtTrxInf>"+
							      "</gepgPmtPstReq>";
			    
			    byte[] data2 = dgdata.getBytes("UTF8");

		        Signature sig2 = Signature.getInstance("SHA1WithRSA");
		        sig2.initSign(privateKey);
		        sig2.update(data2);
		        byte[] signatureBytes2 = sig2.sign();
		        
		        String Signeddata =  Base64.getEncoder().encodeToString(signatureBytes2);
		        
		        Signeddata = Signeddata.replaceAll("\n", "");
		        sig2.initVerify(pk);
		        sig2.update(data2);
		        System.out.println(sig2.verify(signatureBytes2));
		        
		        String requesturl = "http://154.118.230.18:80/api/payment/sigqrequest";
				
				String xmlParam="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" ;
				String urlparam = xmlParam + "<Gepg>"+dgdata+"<gepgSignature>"+Signeddata+"</gepgSignature></Gepg>";
				
				details.addProperty("urlparam", urlparam);
				
				String res = webservicecall.postGEPGHttprequest(requesturl, urlparam);
				
				details.addProperty("Response", res);
		        
		       // String Res_Date = FormatUtils.dynaSQLDate("5-FEB-2021" ,"DD-MM-YYYY");
				
				//datetime2 = new java.sql.Timestamp(new java.util.Date().getTime());
				
				String Res_Date = new SimpleDateFormat( "dd-MMM-yyyy", Locale.ENGLISH).format(System.currentTimeMillis());
		      
				Timestamp RESTIME = new java.sql.Timestamp(new java.util.Date().getTime());
				   
				details.addProperty("Res_Date", Res_Date); 
				//details.addProperty("Res_Time", RESTIME);
				
				Response_001 Response = new Response_001(SUBORGCODE, CHCODE, "GEPG", "MSGTYPE", "O", Res_Date, RESTIME, REQREFNO , requesturl , "IP", "PORT", "HEAD_MSG", res, "RESBY", Signeddata);
				
				Insert_Response_001(Response);
			 }
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Request updated Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
		 }
		
		 return details;
	}
	
	public JsonObject Get_Webserice_Info(String Channel_Id, String SYSCODE) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select * from webservice001 where CHCODE=? and SYSCODE=?";
			 
			 List<web_service_001> API_Info = Jdbctemplate.query(sql, new Object[] { Channel_Id, SYSCODE }, new API_Mapper() );
			 
			 if(API_Info.size()!=0)
			 {
				 details.addProperty("SUBORGCODE", API_Info.get(0).getSUBORGCODE());
				 details.addProperty("CHCODE", API_Info.get(0).getCHCODE());
				 details.addProperty("SERVICECD", API_Info.get(0).getSERVICECD());
				 details.addProperty("SERVNAME", API_Info.get(0).getSERVNAME());
				 details.addProperty("FORMAT", API_Info.get(0).getFORMAT());
				 details.addProperty("PROTOCOL", API_Info.get(0).getPROTOCOL());	 
				 details.addProperty("METHOD", API_Info.get(0).getMETHOD());
				 details.addProperty("CHTYPE", API_Info.get(0).getCHTYPE());
				 details.addProperty("URI", API_Info.get(0).getURI());
				 details.addProperty("PAYLOAD", API_Info.get(0).getPAYLOAD());
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
			 details.addProperty("Message", API_Info.size()!=0 ? "API Details Found !!" : "API Details Not Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
		 }
		
		 return details;
	}
	
	public JsonObject Insert_Response_001(Response_001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into response001(SUBORGCODE,CHCODE,PAYTYPE,MSGTYPE,FLOW,RESDATE,RESTIME,UNIREFNO,MSGURL,IP,PORT,HEAD_MSG,BODY_MSG,RESBY,HASHVAL) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getMSGTYPE(), Info.getFLOW(), Info.getRESDATE(), Info.getRESTIME(), 
					 Info.getUNIREFNO(), Info.getMSGURL(), Info.getIP(), Info.getPORT(), Info.getHEAD_MSG(), Info.getBODY_MSG(), Info.getRESBY(), Info.getHASHVAL() });
			
			 details.addProperty("Result", "Success");
			 details.addProperty("Message", "Response added Successfully !!");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage());  	
		 }
		
		 return details;
	}
	
	private class GEPG_Mapper implements RowMapper<Request_002> 
    {
		public Request_002 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Request_002 API = new Request_002();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setCHCODE(rs.getString("CHCODE"));
			API.setPSPCODE(rs.getString("PSPCODE"));
			API.setSPCODE(rs.getString("SPCODE"));
			API.setPAYEEREF(rs.getString("PAYEEREF"));
			API.setREQREFNO(rs.getString("REQREFNO"));
			API.setINVOICENO(rs.getString("INVOICENO"));
			API.setCREDITAMT(rs.getString("CREDITAMT"));
			API.setCREDITCURR(rs.getString("CREDITCURR"));  
			API.setD_ACCOUNT(rs.getString("D_ACCOUNT"));
			API.setS_MOBILE(rs.getString("S_MOBILE"));
			API.setS_ACNAME(rs.getString("S_ACNAME"));
			API.setS_EMAILID(rs.getString("S_EMAILID"));
			
			return API;
		}
     }
    private class API_Mapper implements RowMapper<web_service_001> 
    {
		public web_service_001 mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			web_service_001 API = new web_service_001();  

			API.setSUBORGCODE(rs.getString("SUBORGCODE"));
			API.setCHCODE(rs.getString("CHCODE"));
			API.setSERVICECD(rs.getString("SERVICECD"));
			API.setSERVNAME(rs.getString("SERVNAME"));
			API.setFORMAT(rs.getString("FORMAT"));
			API.setPROTOCOL(rs.getString("PROTOCOL"));
			API.setMETHOD(rs.getString("METHOD"));
			API.setCHTYPE(rs.getString("CHTYPE"));
			API.setURI(rs.getString("URI"));
			API.setPAYLOAD(rs.getString("PAYLOAD"));
			API.setHEADERID(rs.getString("HEADERID"));
			API.setFLOW(rs.getString("FLOW"));
			
			return API;
		}
     }
    
    private class Header_Mapper implements RowMapper<web_service_002> 
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
}
