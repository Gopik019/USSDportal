package com.hdsoft.hdpay.models;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.zaxxer.hikari.HikariDataSource;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@Component
public class Webservice_call_Modal3 
{
    public JdbcTemplate Jdbctemplate;

    @Autowired
    public void setJdbctemplate(HikariDataSource Datasource) 
    {
        Jdbctemplate = new JdbcTemplate(Datasource);
    }
     
    public JsonObject call_req(String Token)
    {
    	JsonObject details = new JsonObject();
    	
    	try
    	{
    		String URL = "https://sandbox.tips.bot.go.tz:443/tips-account-lookup-service-sandbox/1.2/health";
    		
    		String Method = "GET";
    		
    		OkHttpClient client = new OkHttpClient().newBuilder().build();
    		
    		client = trustAllSslClient(client);
    				
    		Request request = new Request.Builder()
    				  .url(URL)
    				  .method(Method, null)
    				  .addHeader("Authorization", "Bearer "+Token)
    				  .addHeader("accept", "application/json")
    				  .build();
    				
    		Response response = client.newCall(request).execute();
    				
			details.addProperty("Response_Code",	response.code());
			details.addProperty("Response_Body",	response.body().toString());
			details.addProperty("Response_Msg",	response.message());
			details.addProperty("Result",	response.isSuccessful());
    	}
    	catch(Exception e)
    	{
    		details.addProperty("Result", "Failed");
    		details.addProperty("Message", e.getLocalizedMessage());
    	}
    	
    	return details;
    }
    
    public static OkHttpClient trustAllSslClient(OkHttpClient client) 
    {
        Builder builder = client.newBuilder();
        builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager)trustAllCerts[0]);
        builder.hostnameVerifier(new HostnameVerifier() 
        {
        	public boolean verify(String hostname, SSLSession session) {	return true;  }
        });
        
        return builder.build();
    }
    
    private static final TrustManager[] trustAllCerts = new TrustManager[] 
    {
    	    new X509TrustManager() {
    	      
    	        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }
  
    	        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

    	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  return new java.security.cert.X509Certificate[]{}; }
    	    }
    };
    
    private static final SSLContext trustAllSslContext;
    	
    static {
	    try 
	    {
	        trustAllSslContext = SSLContext.getInstance("SSL");
	        trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
	    } 
	    catch (Exception e) 
	    {
	        throw new RuntimeException(e);
	    }
    }
    
    private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();
}