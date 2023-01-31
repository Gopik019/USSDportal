package com.hdsoft.hdpay.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Outward_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	// private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
		
		
	/*public JsonObject Get_GEPG_Bill_Details2(String request, JsonObject Headers, HttpServletRequest req)
	{
		JsonObject details = new JsonObject();  
		
		JsonObject Internal_Response = new JsonObject();  
		
		JsonObject BILLINFO = new JsonObject();
		
		try
		{
			 JsonObject Request = new Common_Utils().StringToJsonObject(request);
			 
			 JsonObject GETBILL = Request.get("GETBILL").getAsJsonObject();			
			 
			 JsonObject PRIMARYARG = GETBILL.get("PRIMARYARG").getAsJsonObject();	
			 
			 String PAYTYPE = GETBILL.get("PAYTYPE").getAsString();   
			 String DATE = GETBILL.get("DATE").getAsString();
			 String ReqRefID = GETBILL.get("ReqRefID").getAsString();
			 String Checksum = GETBILL.get("Checksum").getAsString();
			 String INITATEDBY = GETBILL.get("INITATEDBY").getAsString();
			 String BILLNO = PRIMARYARG.get("BILLNO").getAsString();
					 
			 String SUBORGCODE  = "EXIM";
			 //String SYSCODE     = "HP";
			 String CHCODE 		=  Headers.get("ChannelID").getAsString();  					
			 String MSGTYPE 	=  "003";  									
			 String FLOW    	=  "I"; 									
			 String MSGURL 	 	=  "";   									
			 String IP      	=  Headers.get("IPAddress").getAsString(); 
			 String PORT    	=  "";  								
			 String HEAD_MSG    =  Headers.toString();  				
			 String BODY_MSG    =  request;   
			
			 Timestamp REQTIME = new java.sql.Timestamp(new java.util.Date().getTime());
			 
			 JsonObject Res1 = Insert_Request_001(SUBORGCODE, CHCODE, MSGTYPE, FLOW, DATE, REQTIME, ReqRefID, MSGURL, IP, PORT, HEAD_MSG, BODY_MSG, INITATEDBY,  Checksum); 		
			 
			 Internal_Response.add("Request_001", Res1);
	
			 if(PAYTYPE.equals("GEPG"))
			 {			
				 String password = "eximbank123";
				 String alias = "eximbank";
					
				 char[] passord = null;
				 PrivateKey privateKey = null;
				
			     File file = new File("C:\\gepg\\gepg.p12");
			     InputStream stream = new FileInputStream(file);
			     java.security.KeyStore keyStore = KeyStore.getInstance("PKCS12");
			     keyStore.load(stream, passord);
			     privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
				      
			     FileInputStream fin = new FileInputStream("C:\\gepg\\gepgclient.cer");
			     CertificateFactory f = CertificateFactory.getInstance("X.509");
			     X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
			     PublicKey pk = certificate.getPublicKey();
			    
			     String dgdata = "<gepgBillQryReq>"+
					     		   	"<BillReqId>"+ReqRefID+"</BillReqId>"+
					     		   	"<BillCtrNum>"+BILLNO+"</BillCtrNum>\r\n"+
					     		 "</gepgBillQryReq>";	
	
			     byte[] data2 = dgdata.getBytes("UTF8");
			     
		         Signature sig2 = Signature.getInstance("SHA1WithRSA");
		         sig2.initSign(privateKey);
		         sig2.update(data2);
		         byte[] signatureBytes2 = sig2.sign();
		        
		         String Signeddata =  Base64.getEncoder().encodeToString(signatureBytes2);
		        
		         Signeddata = Signeddata.replaceAll("\n", "");
		         sig2.initVerify(pk);
		         sig2.update(data2);
		            
		         //System.out.println(sig2.verify(signatureBytes2));
		        
		         String requesturl = "http://154.118.230.18:80/api/bill/transfer-query";
				
				 String xmlParam="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" ;
				 String urlparam = xmlParam + "<Gepg>"+dgdata+"<gepgSignature>"+Signeddata+"</gepgSignature></Gepg>";
				
				 //details.addProperty("urlparam", urlparam);
				
				 String response = webservicecall.postGEPGHttprequest(requesturl, urlparam);
				 
				 JsonObject XML_Response =  Parse_XML_Bill_Response(response);
				 
				 BILLINFO.addProperty("PAYTYPE", PAYTYPE);
				 BILLINFO.addProperty("DATE", DATE);
				 BILLINFO.addProperty("ReqRefID", ReqRefID);
				 BILLINFO.addProperty("Checksum", Checksum);
				 BILLINFO.addProperty("INITATEDBY", INITATEDBY);
				 
				 JsonObject BILLDETAILS = new JsonObject();
				 
				 JsonObject PRIELEMENTS = XML_Response.get("PRIELEMENTS").getAsJsonObject();
				 
				 JsonObject SECELEMENTS = XML_Response.get("SECELEMENTS").getAsJsonObject();
				 
				 BILLDETAILS.addProperty("BILLNO", BILLNO);
				 
				 BILLDETAILS.add("PRIELEMENTS", PRIELEMENTS);
				 BILLDETAILS.add("SECELEMENTS", SECELEMENTS);
				 
				 BILLINFO.add("BILLDETAILS", BILLDETAILS);

			     String Res_Date = new SimpleDateFormat( "dd-MMM-yyyy", Locale.ENGLISH).format(System.currentTimeMillis());
			  
			     Timestamp RESTIME = new java.sql.Timestamp(new java.util.Date().getTime());
			     
			     Response_001 Response = new Response_001(SUBORGCODE, CHCODE, MSGTYPE, "O", Res_Date, RESTIME, ReqRefID , requesturl , IP, PORT, HEAD_MSG, response, "RESBY", Signeddata);
				
			     JsonObject Response_001 = Insert_Response_001(Response);		  
			  
			     Internal_Response.add("Response_001", Response_001);		
			     
			     details.add("BILLINFO", BILLINFO);
			 }

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
	*/
	
}
