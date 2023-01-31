package com.hdsoft.hdpay.models;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import com.hdsoft.hdpay.Repositories.Job_005;
import com.hdsoft.hdpay.Repositories.Request_001;
import com.hdsoft.hdpay.Repositories.Request_002;
import com.hdsoft.hdpay.Repositories.Response_001;
import com.hdsoft.hdpay.Repositories.web_service_001;
import com.hdsoft.hdpay.Repositories.web_service_002;
import com.zaxxer.hikari.HikariDataSource;

public class Outward_Modal_old2 
{
	public static JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	public Outward_Modal_old2(JdbcTemplate jdbctemplate) 
	{ 
		Jdbctemplate = jdbctemplate;
	}
	
	public Outward_Modal_old2() { }
	
	public JsonObject Insert_Request_001(Request_001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into request001(SUBORGCODE,CHCODE,PAYTYPE,FLOW,MSGTYPE,FLOW,REQDATE,REQTIME,UNIREFNO,MSGURL,IP,PORT,HEAD_MSG,BODY_MSG,REQBY,HASHVAL) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE(), Info.getPAYTYPE(), Info.getFLOW(), Info.getMSGTYPE(), Info.getFLOW(), Info.getREQDATE(), Info.getREQTIME(), Info.getUNIREFNO(), 
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
			   
			 Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(),Info.getSYSCODE(),Info.getCHCODE(),Info.getPAYTYPE(),Info.getREQDATE(),Info.getREQREFNO(),Info.getREQSL(),
					 Info.getREQTIME(),Info.getPAYDATE(), Info.getFLOW(),Info.getSTATUS(),Info.getTRANAMT(),Info.getTRANCURR(),Info.getREQTYPE(),
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
	
	public JsonObject GEPG_Executer(String SYSCODE, String PAYTYPE, String REFNO, String REQSL) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select * from request002 where PAYTYPE=? and REFNO=? and REQSL=?";   /**** Find GPEG Request from request002  ****/
			   
			 List<Request_002> obj = Jdbctemplate.query(sql, new Object[] { PAYTYPE, REFNO, REQSL }, new GEPG_Mapper());
			 
			 for(int i=0;i<obj.size();i++)
			 {
				 String SUBORGCODE = obj.get(i).getSUBORGCODE();
				 //String SYSCODE = obj.get(i).getSYSCODE();
				 String CHCODE = obj.get(i).getCHCODE();
				 String PSPCODE = obj.get(i).getPSPCODE();
				 String SPCODE = obj.get(i).getSPCODE();
				 String PAYEEREF = obj.get(i).getPAYEEREF();
				 //String REFNO = obj.get(i).getREFNO();
				 String INVOICENO = obj.get(i).getINVOICENO();
				 String CREDITAMT = obj.get(i).getCREDITAMT();
				 String CREDITCURR = obj.get(i).getCREDITCURR();
				 //String D_ACCOUNT = obj.get(i).getD_ACCOUNT();
				 String S_ACCOUNT = obj.get(i).getS_ACCOUNT();
				 String S_MOBILE = obj.get(i).getS_MOBILE();
				 String S_ACNAME = obj.get(i).getS_ACNAME();
				 String S_EMAILID = obj.get(i).getS_EMAILID();
				 String CALLBACKURL = obj.get(i).getCALLBACKURL();
				 //String PAYTYPE = obj.get(i).getPAYTYPE();
				 
				 String Channel_Code = "GEPG";  /** for Outward request ***/
				 String Service_Code = "001";  /** for Outward request ***/
				 
				 JsonObject webservice_details = Get_Webserice_Info(SYSCODE, Channel_Code, Service_Code); 	/**** Getting Details from webservice 001 & webservice002 ****/
				 
				 if(webservice_details.get("Result").getAsString().equals("Success"))
				 {			
					  String PAYLOAD = webservice_details.get("PAYLOAD").getAsString();
					  
					  String Requesturl = webservice_details.get("URI").getAsString();
					 
					  String XML_Tag = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
					  
					  String Signature_Tag = "<gepgSignature>~gepgSignature~</gepgSignature>";
					 
					  PAYLOAD = PAYLOAD.replace(XML_Tag, "");
					  PAYLOAD = PAYLOAD.replace(Signature_Tag, "");
					  PAYLOAD = PAYLOAD.replace("<Gepg>", "");
					  PAYLOAD = PAYLOAD.replace("</Gepg>>", "");
					  
					  Date currentDatetime = new Date(System.currentTimeMillis()); 
						 
					  String datetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(currentDatetime);		  
					  
					  String dgdata = PAYLOAD;
					  
					  dgdata = dgdata.replace("~PspCode~", PSPCODE);		
					  dgdata = dgdata.replace("~ResultUrl~", CALLBACKURL);
					  dgdata = dgdata.replace("~TrxId~", PAYEEREF);			
					  dgdata = dgdata.replace("~spCode~", SPCODE);
					  dgdata = dgdata.replace("~PayRefId~", REFNO);			
					  dgdata = dgdata.replace("~BillCtrNum~", INVOICENO);
					  dgdata = dgdata.replace("~PaidAmt~", CREDITAMT);		
					  dgdata = dgdata.replace("~CCy~", CREDITCURR);
					  dgdata = dgdata.replace("~CtrAccNum~", S_ACCOUNT); 	
					  dgdata = dgdata.replace("~TrxDtTm~", datetime);
					  dgdata = dgdata.replace("~UsdPayChnl~", CHCODE);		
					  dgdata = dgdata.replace("~DptCellNum~", S_MOBILE);
					  dgdata = dgdata.replace("~DptName~", S_ACNAME);			
					  dgdata = dgdata.replace("~DptNameEmailAddr~", S_EMAILID);
					  			
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
					    
					  byte[] data2 = dgdata.getBytes("UTF8");

				      Signature sig2 = Signature.getInstance("SHA1WithRSA");
				      sig2.initSign(privateKey);
				      sig2.update(data2);
				      byte[] signatureBytes2 = sig2.sign();
				        
				      String Signeddata =  Base64.getEncoder().encodeToString(signatureBytes2);
				      		 
			          Signeddata = Signeddata.replaceAll("\n", "");
			          sig2.initVerify(pk);
			          sig2.update(data2);
			           
			          Signature_Tag = Signature_Tag.replace("~gepgSignature~", Signeddata);
			          
					  String urlparam = XML_Tag + "<Gepg>" + dgdata + Signature_Tag + "</Gepg>";
						
					  details.addProperty("urlparam", urlparam);
					  
					  webservice_details.addProperty("PAYLOAD", urlparam);
					  
					  JsonObject API_Response = Send_Rest_request(webservice_details); 		/**** Sending API Request ****/
					 
					  details.add("API_Calling_Response", API_Response);
					  
					  if(API_Response.get("Result").getAsString().equals("Success"))
					  {
						  String response = API_Response.get("Response").getAsString();
						  
						  String Res_Date = new SimpleDateFormat( "dd-MMM-yyyy", Locale.ENGLISH).format(System.currentTimeMillis());
						  
						  Timestamp RESTIME = new java.sql.Timestamp(new java.util.Date().getTime());
						    
						  Response_001 Response = new Response_001(SUBORGCODE, CHCODE, PAYTYPE, "MSGTYPE", "O", Res_Date, RESTIME, REFNO , Requesturl , "IP", "PORT", "HEAD_MSG", response, "RESBY", Signeddata);
							
						  JsonObject Response_001 = Insert_Response_001(Response);	/**** Insert Response to Response_001 ****/	  
						  
						  details.add("Response_001", Response_001);
						  
						  Request_002 info = new Request_002();

						  info.setSTATUS(API_Response.get("Result").getAsString());
						  info.setERRCD("");
						  info.setERRDESC("");
						  info.setRESCODE(API_Response.get("Response_Code").getAsString());
						  info.setRESPDESC(API_Response.get("Message").getAsString());
						  info.setCHCODE(CHCODE);
						  info.setPAYTYPE(PAYTYPE);
						  info.setREQREFNO(REFNO);
						  
						  JsonObject Update_Request_002 = Update_Request_002(info);  /**** Update request 002 ****/
						  
						  details.add("Update_Request_002", Update_Request_002);						 						 
					  }  
					  else
					  {
						  Request_002 info = new Request_002();

						  info.setSTATUS(API_Response.get("Result").getAsString());
						  info.setERRCD(API_Response.get("Code").getAsString());
						  info.setERRDESC(API_Response.get("Message").getAsString());
						  info.setRESCODE("");
						  info.setRESPDESC("");
						  info.setCHCODE(CHCODE);
						  info.setPAYTYPE(PAYTYPE);
						  info.setREQREFNO(REFNO);
						  
						  JsonObject Update_Request_002 = Update_Request_002(info);  /**** Update request 002 ****/
						  
						  details.add("Update_Request_002", Update_Request_002);
					  }
				 }
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
	
	public JsonObject Get_Webserice_Info(String SYSCODE,String CHCODE, String SERVICECD) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Select * from webservice001 where SYSCODE=? and CHCODE=? and SERVICECD=?";
			 
			 List<web_service_001> API_Info = Jdbctemplate.query(sql, new Object[] { SYSCODE, CHCODE, SERVICECD }, new API_Mapper() );
			 
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
	
	public JsonObject Send_Rest_request(JsonObject Api_details)
	{
		JsonObject details = new JsonObject();
		
		try
		{
			String Url = Api_details.get("URI").getAsString();
			String Method = Api_details.get("METHOD").getAsString();
			String URLParameters = Api_details.get("PAYLOAD").getAsString();
			
			URL obj = new URL(Url);
			
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod(Method);
			
			JsonArray Headers = Api_details.get("Headers").getAsJsonArray();
			
			for(int i=0;i<Headers.size();i++)
			{
				JsonObject header = Headers.get(i).getAsJsonObject();
				
				String key = header.get("Key").getAsString();
				String value = header.get("Value").getAsString();
				
				con.setRequestProperty(key, value);
			}
	
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(URLParameters);
			wr.flush();
			wr.close();
	
			int responseCode = con.getResponseCode();
			
			details.addProperty("Request URL" , Url);
			details.addProperty("Method" , Method);
			details.addProperty("Parameters"  , URLParameters);
			details.addProperty("Response_Code" , responseCode);
	
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String inputLine;
			
			StringBuffer response = new StringBuffer();
	
			while((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			
			in.close();
	
			details.addProperty("Response" , response.toString());
			
			details.addProperty("Result", "Success");
			details.addProperty("Code", "200");
			details.addProperty("Message", "API Calling Success");
		}
	    catch(Exception e)
	    {
	       details.addProperty("Result", "Failed");
	       details.addProperty("Code", "500");
		   details.addProperty("Message", e.getLocalizedMessage());  	
	    }
		
		return details;
	} 
	
	public JsonObject Insert_Response_001(Response_001 Info) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "Insert into response001(SUBORGCODE,CHCODE,MSGTYPE,FLOW,RESDATE,RESTIME,UNIREFNO,MSGURL,IP,PORT,HEAD_MSG,BODY_MSG,RESBY,HASHVAL) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			   
			 int status = Jdbctemplate.update(sql, new Object[] { Info.getSUBORGCODE(), Info.getCHCODE(), Info.getMSGTYPE(), Info.getFLOW(), Info.getRESDATE(), Info.getRESTIME(), 
					 Info.getUNIREFNO(), Info.getMSGURL(), Info.getIP(), Info.getPORT(), Info.getHEAD_MSG(), Info.getBODY_MSG(), Info.getRESBY(), Info.getHASHVAL() });
			
			 details.addProperty("Result", status == 1 ? "Success" : "Failed");
			 details.addProperty("Message", status == 1 ? "Response added Successfully !!" : "Response not added!!");
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
			API.setSYSCODE(rs.getString("SYSCODE"));
			API.setCHCODE(rs.getString("CHCODE"));
			API.setPSPCODE(rs.getString("PSPCODE"));
			API.setSPCODE(rs.getString("SPCODE"));
			API.setPAYEEREF(rs.getString("PAYEEREF"));
			API.setREQREFNO(rs.getString("REQREFNO"));
			API.setINVOICENO(rs.getString("INVOICENO"));
			API.setCREDITAMT(rs.getString("CREDITAMT"));
			API.setCREDITCURR(rs.getString("CREDITCURR"));  
			API.setD_ACCOUNT(rs.getString("D_ACCOUNT"));
			API.setS_ACCOUNT(rs.getString("S_ACCOUNT"));
			API.setS_MOBILE(rs.getString("S_MOBILE"));
			API.setS_ACNAME(rs.getString("S_ACNAME"));
			API.setS_EMAILID(rs.getString("S_EMAILID"));
			API.setCALLBACKURL(rs.getString("CALLBACKURL"));
			API.setPAYTYPE(rs.getString("PAYTYPE"));
			
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
