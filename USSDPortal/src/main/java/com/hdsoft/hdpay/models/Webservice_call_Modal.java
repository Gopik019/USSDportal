package com.hdsoft.hdpay.models;

import java.io.IOException;
import java.security.cert.CertificateException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.common.Common_Utils;
import com.zaxxer.hikari.HikariDataSource;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.OkHttpClient.Builder;

@Component
public class Webservice_call_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	public JsonObject Okhttp_Send_Rest_Request(JsonObject Api_details)
    {
    	JsonObject details = new JsonObject();
    	
    	try
    	{
    		String URL = Api_details.get("URI").getAsString();
			String Method = Api_details.get("METHOD").getAsString();
			String URLParameters = Api_details.get("PAYLOAD").getAsString();

    		JsonArray Headers = Api_details.has("Headers") ? Api_details.get("Headers").getAsJsonArray() : new JsonArray();
    		JsonArray Params = Api_details.has("Form_Data") ? Api_details.get("Form_Data").getAsJsonArray() : new JsonArray();
			
			Headers.Builder header_builder = new Headers.Builder();
			
			for(int i=0;i<Headers.size();i++)
			{
				JsonObject header = Headers.get(i).getAsJsonObject();
				
				String key = header.get("Key").getAsString();
				String value = header.get("Value").getAsString();
				
				header_builder.add(key, value);
			}
			
			Headers Request_Headers = header_builder.build();
			
    		RequestBody Req_Body = RequestBody.create(null, new byte[0]);  
    		
    		if(!URLParameters.equals(""))
    		{
    			Common_Utils util = new Common_Utils();
    			
    			if(Api_details.has("Form_Data"))  /*** For Form Data Request body ***/
    			{
    				if(Params.size() !=0 && URL.contains("tips") && URL.contains("token"))
    				{
    					JsonObject Param = Params.get(0).getAsJsonObject();
    					
    					String key = Param.get("Key").getAsString();
    					String value = Param.get("Value").getAsString();
    					
    					MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded"); 
        				
    					Req_Body = RequestBody.create(mediaType, key+"="+value);
    				}
    			}
    			else if(util.isJSONValid(URLParameters))  /*** For Json Request body ***/
    			{
    				MediaType JSON = MediaType.parse("application/json"); 
    				
    				Req_Body = RequestBody.create(JSON, new Gson().toJson(URLParameters));
    			}
    			else if(util.isXMLValid(URLParameters))  /*** For XML Request body ***/
    			{
    				MediaType XML = MediaType.parse("application/xml"); 
    				
    				Req_Body = RequestBody.create(XML, URLParameters); 
    			}
    		}

    		logger.debug("Request details :::: "+Api_details.toString());
    		
    		if(Method.equals("GET"))
    		{
    			details = doGetRequest(URL, Method, Request_Headers);
    		}
    		else if(Method.equals("POST"))
    		{
    			details = doPostRequest(URL, Method, Request_Headers, Req_Body, URLParameters);
    			
    			//details = Send_Request(URL, Method, Request_Headers, Req_Body, URLParameters, PAYTYPE);
    		}
    		else if(Method.equals("PUT"))
    		{
    			details = doPutRequest(URL, Method, Request_Headers, Req_Body);
    		}
    		else 
    		{
    			details = doDeleteRequest(URL, Method, Request_Headers, Req_Body);
    		}
    	}
    	catch(Exception e)
    	{
    		details.addProperty("Result", "Failed");
    		details.addProperty("Message", e.getLocalizedMessage());
    		
    		logger.debug("Exception in Send_Rest_request :::: "+e.getLocalizedMessage());
    	}
    	
    	return details;
    }
	
	public JsonObject doGetRequest(String URL, String Method, Headers Request_Headers)
	{
		JsonObject details = new JsonObject();
		
		try
    	{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
    		
			if(URL.contains("https"))
			{
				client = trustAllSslClient(client);
			}
    		
    		Request request = new Request.Builder()
  				  .url(URL)
  				  .headers(Request_Headers)
  				  .build();
    		
    		Response response = client.newCall(request).execute();
    		
    		String Response = response.body().string();
    		
    		int Response_Code = response.code();
    		
    		details.addProperty("Request URL", URL);
			details.addProperty("Method", Method);
			details.addProperty("Parameters", "");
			details.addProperty("Response_Code", Response_Code);
			details.addProperty("Response", Response);
			details.addProperty("Result", response.isSuccessful() ? "Success" : "Failed");
			details.addProperty("Message", response.message());
    	}	
		catch(Exception e)
    	{
    		details.addProperty("Result", "Failed");
    		details.addProperty("Response_Code", 500);
    		details.addProperty("Message", e.getLocalizedMessage());
    		
    		logger.debug("Exception in Send_Rest_request :::: "+e.getLocalizedMessage());
    	}
		
		return details;
	}
	
	public JsonObject doPostRequest(String URL, String Method, Headers Request_Headers, RequestBody Req_Body, String URLParameters)
	{
		JsonObject details = new JsonObject();
		
		try
    	{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
    		
			if(Request_Headers.get("content-type").contains("application/json") || Request_Headers.get("Content-Type").contains("application/json"))
			{
				client = new OkHttpClient().newBuilder().addNetworkInterceptor(new FixContentTypeInterceptor()).build();
			}
			
			if(URL.contains("https"))
			{
				client = trustAllSslClient(client);
			}
			
    		Request request = new Request.Builder()
  				  .url(URL)
  				  .post(Req_Body)
  				  .headers(Request_Headers)
  				  .build();
    		
    		Response response = client.newCall(request).execute();
    		
    		String Response = response.body().string();
    		
    		int Response_Code = response.code();
    		
    		details.addProperty("Request URL", URL);
			details.addProperty("Method", Method);
			details.addProperty("Parameters", "");
			details.addProperty("Response_Code", Response_Code);
			details.addProperty("Response", Response);
			details.addProperty("Result", response.isSuccessful() ? "Success" : "Failed");
			details.addProperty("Message", response.message());
    	}	
		catch(Exception e)
    	{
    		details.addProperty("Result", "Failed");
    		details.addProperty("Response_Code", 500);
    		details.addProperty("Message", e.getLocalizedMessage());
    		
    		logger.debug("Exception in Send_Rest_request :::: "+details);
    	}
		
		return details;
	}
	
	public JsonObject doPutRequest(String URL, String Method, Headers Request_Headers, RequestBody Req_Body)
	{
		JsonObject details = new JsonObject();
		
		try
    	{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
    		
			if(Request_Headers.get("content-type").contains("application/json") || Request_Headers.get("Content-Type").contains("application/json"))
			{
				client = new OkHttpClient().newBuilder().addNetworkInterceptor(new FixContentTypeInterceptor()).build();
			}
			
			if(URL.contains("https"))
			{
				client = trustAllSslClient(client);
			}
    		
    		Request request = new Request.Builder()
  				  .url(URL)
  				  .headers(Request_Headers)
  				  .put(Req_Body)
  				  .build();
    		
    		Response response = client.newCall(request).execute();
    		
    		String Response = response.body().string();
    		
    		int Response_Code = response.code();
    			
    		details.addProperty("Request URL", URL);
			details.addProperty("Method", Method);
			details.addProperty("Parameters", "");
			details.addProperty("Response_Code", Response_Code);
			details.addProperty("Response", Response);
			details.addProperty("Result", response.isSuccessful() ? "Success" : "Failed");
			details.addProperty("Message", response.message());
    	}	
		catch(Exception e)
    	{
    		details.addProperty("Result", "Failed");
    		details.addProperty("Response_Code", 500);
    		details.addProperty("Message", e.getLocalizedMessage());
    		
    		logger.debug("Exception in Send_Rest_request :::: "+details);
    	}
		
		return details;
	}
	
	public JsonObject doDeleteRequest(String URL, String Method, Headers Request_Headers, RequestBody Req_Body)
	{
		JsonObject details = new JsonObject();
		
		try
    	{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
    		
			if(Request_Headers.get("content-type").contains("application/json") || Request_Headers.get("Content-Type").contains("application/json"))
			{
				client = new OkHttpClient().newBuilder().addNetworkInterceptor(new FixContentTypeInterceptor()).build();
			}
			
			if(URL.contains("https"))
			{
				client = trustAllSslClient(client);
			}
    		
    		Request request = new Request.Builder()
  				  .url(URL)
  				  .headers(Request_Headers)
  				  .delete(Req_Body)
  				  .build();
    		
    		Response response = client.newCall(request).execute();
    		
    		String Response = response.body().string();
    		
    		int Response_Code = response.code();
    			
    		details.addProperty("Request URL", URL);
			details.addProperty("Method", Method);
			details.addProperty("Parameters", "");
			details.addProperty("Response_Code", Response_Code);
			details.addProperty("Response", Response);
			details.addProperty("Result", response.isSuccessful() ? "Success" : "Failed");
			details.addProperty("Message", response.message());
    	}	
		catch(Exception e)
    	{
    		details.addProperty("Result", "Failed");
    		details.addProperty("Response_Code", 500);
    		details.addProperty("Message", e.getLocalizedMessage());
    		
    		logger.debug("Exception in Send_Rest_request :::: "+details);
    	}
		
		return details;
	}
	
	/*** we will call all the methods *****/
	
	public JsonObject Send_Request(String URL, String Method, Headers Request_Headers, RequestBody Req_Body, String URLParameters, String PAYTYPE)
	{
		JsonObject details = new JsonObject();
		
		try
    	{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
    		
			if(Request_Headers.get("content-type").contains("application/json") || Request_Headers.get("Content-Type").contains("application/json"))
			{
				client = new OkHttpClient().newBuilder().addNetworkInterceptor(new FixContentTypeInterceptor()).build();
			}
			
			if(URL.contains("https"))
			{
				client = trustAllSslClient(client);
			}
			
    		Request request = new Request.Builder()
  				  .url(URL)
  				  .method(Method, Req_Body)
  				  .headers(Request_Headers)
  				  .build();
    	
    		Response response = client.newCall(request).execute();
    		
    		String Response = response.body().string();
    		
    		int Response_Code = response.code();
    		
    		logger.debug("Response :::: "+Response);

    		details.addProperty("Request URL", URL);
			details.addProperty("Method", Method);
			details.addProperty("Parameters", "");
			details.addProperty("Response_Code", Response_Code);
			details.addProperty("Response", Response);
			details.addProperty("Result", response.isSuccessful() ? "Success" : "Failed");
			details.addProperty("Message", response.message());
    	}	
		catch(Exception e)
    	{
    		details.addProperty("Result", "Failed");
    		details.addProperty("Response_Code", 500);
    		details.addProperty("Message", e.getLocalizedMessage());
    		
    		logger.debug("Exception in Send_Rest_request :::: "+e.getLocalizedMessage());
    	}
		
		return details;
	}
	
	public final class FixContentTypeInterceptor implements Interceptor 
	{
	    public Response intercept(Interceptor.Chain chain) throws IOException 
	    {
	        Request originalRequest = chain.request();

	        Request fixedRequest = originalRequest.newBuilder()
	                .header("Content-Type", "application/json")
	                .build();
	        
	        return chain.proceed(fixedRequest);
	    }
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
    	
    static 
    {
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
