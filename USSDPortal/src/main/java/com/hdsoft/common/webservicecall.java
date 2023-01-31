package com.hdsoft.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import com.google.gson.JsonObject;

public class webservicecall 
{
	private final static String USER_AGENT = "Mozilla/5.0";
	private static String URL;
	private static String URLParameters;
	static String ress = "";

	public static String getHttprequest(String url) throws Exception
	{
		String responsedata = null;
		URL = url;
		sendGet();
		responsedata = ress.toString();
		System.out.println("Response Date : " +  responsedata );
		return responsedata;
	}
	
	
	
	public static String getDERMAHttprequest(String url,String urlparameters) throws Exception
	{
		String responsedata = null;
		URL = url;
		URLParameters = urlparameters;
		senddermaGet();
		responsedata = ress.toString();
		System.out.println("Response Date : " +  responsedata );
		return responsedata;
	}
	
	public static String postGEPGHttprequest(String url, String urlparameters) throws Exception
	{
		String responsedata = null;
		URL = url;
		URLParameters = urlparameters;
		sendgepgPost();
		responsedata = ress.toString();
		
		System.out.println("Response Date : " +  responsedata ); 
		return responsedata;
	}
	
	
	public static String postTIGOHttprequest(String url, String urlparameters) throws Exception
	{
		String responsedata = null;
		URL = url;
		URLParameters = urlparameters;
		sendCheckCustomerNamePost();
		responsedata = ress.toString();
		
		
		System.out.println("Response Date : " +  responsedata ); 
		return responsedata;
	}
	
	
	
	public static String postTIGOPOSTHttprequest(String url, String urlparameters) throws Exception
	{
		String responsedata = null;
		URL = url;
		URLParameters = urlparameters;
		sendCheckCustomerNamePost();
		responsedata = ress.toString();
		
		System.out.println("Response Date : " +  responsedata ); 
		return responsedata;
	}
	
	
	public static String postGEPGTRAHttprequest(String url, String urlparameters) throws Exception
	{
		String responsedata = null;
		URL = url;
		URLParameters = urlparameters;
		sendgepgtraPost();
		responsedata = ress.toString();
		
		System.out.println("Response Date : " +  responsedata ); 
		return responsedata;
	}
	
	public static String postHttprequest(String url, String urlparameters) throws Exception
	{
		String responsedata = null;
		URL = url;
		URLParameters = urlparameters;
		sendPost();
		responsedata = ress.toString();
		
		System.out.println("Response Date : " +  responsedata ); 
		return responsedata;
	}	
	
	public static String postHttpsrequestDer(String url, String urlparameters) throws Exception
	{
		String responsedata = null;
		URL = url;
		System.out.println("request url : " +  URL ); 
		
		URLParameters = urlparameters;
		
		System.out.println("request url Parm : " +  URLParameters ); 
		sendpost2();
		//dermlg();
		responsedata = ress.toString();
		
		
		System.out.println("Response Date : " +  responsedata ); 
		return responsedata;
	}
	
	   private static void TOKENMETHOD (HttpsURLConnection con){
     	  String params1 = null ;
               if(con!=null){
             	  
             	  try{
             		 JsonObject jj = new JsonObject();
       		        jj.addProperty("Username", "exim");
       		        jj.addProperty("Password", "3bmrh41D#!");
       		        params1 =jj.toString();

             	  }catch(Exception e){
             		  e.printStackTrace();
             	  }
             	  
               try {
                  System.out.println("****** Content of the URL ********");
                 con.setRequestMethod("POST");
             	//con.setRequestProperty("User-Agent", USER_AGENT);
         		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
         		con.setRequestProperty("content-type", "application/json");
         	
          		con.setDoOutput(true);
         		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
         		wr.writeBytes(params1);
         		wr.flush();
         		wr.close();

         		int responseCode = con.getResponseCode();
        // 		System.out.println("\nSending 'POST' request to URL : " + URL);
         		System.out.println("Post parameters : " + params1);
         		System.out.println("Response Code : " + responseCode);

         		BufferedReader in = new BufferedReader(
         		        new InputStreamReader(con.getInputStream()));
         		String inputLine;
         		StringBuffer response = new StringBuffer();

         		while ((inputLine = in.readLine()) != null) {
         			response.append(inputLine);
         			System.out.println(response.toString());
         		}
         		in.close();

         		//print result
         		System.out.println(response.toString());
         		 ress = response.toString();

         		
         		/*
 		        
                  BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                  String input;
                  while ((input = br.readLine()) != null){
                     System.out.println(input);
                  }
                  br.close();
					*/
         		
               } catch (IOException e) {
                  e.printStackTrace();
               }
           }
       }

	
	public static String postHttprequest3(String url, String urlparameters) throws Exception
	{
		String responsedata = null;
		URL = url;
		URLParameters = urlparameters;
		sendPost3();
		responsedata = ress.toString();
		
		System.out.println("Response Date : " +  responsedata ); 
		return responsedata;
	}
	
	
	
	public static String postHttprequest4(String url, String urlparameters) throws Exception
	{
		String responsedata = null;
		URL = url;
		URLParameters = urlparameters;
		sendPost4();
		responsedata = ress.toString();
		
		System.out.println("Response Date : " +  responsedata ); 
		return responsedata;
	}
	
	
	// HTTP DERMALOG GET request

	
	private static void senddermaGet() throws Exception {


		URL obj = new URL(URL);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		

		

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Authorization", URLParameters);
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");

		

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + URL);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer(); 

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		ress = response.toString();

	}
	
	
	
	
	
	
	// HTTP GET request
	private static void sendGet() throws Exception {

		String url = "http://41.221.32.202/bank_verify.php?id=17642";

		URL obj = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer(); 

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		ress = response.toString();

	}
	
	
	private static void sendpost2() throws Exception {

		URL obj = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("content-type", "application/json");
		con.setRequestProperty("Authorization", "A3DE3640BAD50815E0530100007F6153");
		

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(URLParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + URL);
		System.out.println("Post parameters : " + URLParameters);
		System.out.println("Response Code : " + responseCode);

	

		//print result
		System.out.println(responseCode);
		ress = String.valueOf(responseCode);
		
		
	
	}
	private static void sendPost3() throws Exception {

		URL obj = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(URLParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + URL);
		System.out.println("Post parameters : " + URLParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		ress = response.toString();

	}
	
	
	private static void sendPost4() throws Exception {

		URL obj = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(URLParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + URL);
		System.out.println("Post parameters : " + URLParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		ress = response.toString();

	}
	
	
	// HTTP POST request
	private static void sendPost() throws Exception {
			

		
		
		String url = "http://172.17.31.2/simbanet_bankpay.ews";
		URL obj = new URL(URL);
		//HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		//con.setRequestProperty("content-type", "application/xml");

		//String urlParameters = "login_user=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		String urlParameters = "login_user=eximbank&login_password=!ex1m.b&CustomerID=12345&InvoiceID=12345&ExternalRef=12345&Amount=2000";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(URLParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + URL);
		System.out.println("Post parameters : " + URLParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		ress = response.toString();
		
		
	}	
	
	
	
private static void sendCheckCustomerNamePost() throws Exception {
			

	
	
	
	
	/*		String auth = "user" + ":" + "pass";
		    byte[] encodedAuth = Base64.encodeBase64( 
		    auth.getBytes(Charset.forName("US-ASCII")) );
		    String authHeader = "Basic " + new String( encodedAuth );		*/
		
		URL obj = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		
        
        
		
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("content-type", "application/XML");
		con.setRequestProperty("Authorization","Basic dHRhLmV4aW06RnNkUVh4Ynh0VA==");
		//For Live
		
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(URLParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + URL);
		System.out.println("Post parameters : " + URLParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		ress = response.toString();
	}
	
	private static void sendgepgPost() throws Exception 
	{
		URL obj = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("content-type", "application/XML");
		
		//con.setRequestProperty("Gepg-Com", "exim.psp.in");
		
		//For UAT
	//	con.setRequestProperty("GePG-Code", "PSP022");
		//con.setRequestProperty("Psp-Code", "PSP022");
	//	con.setRequestProperty("Gepg-Com","tib.psp.in");
		
		//For Live
		con.setRequestProperty("GePG-Code", "PSP023");
		con.setRequestProperty("Psp-Code", "PSP023");
		con.setRequestProperty("Gepg-Com","exim.psp.in");
		
		//String urlParameters = "login_user=C02G8416DRJM&cn=&locale=&caller=&num= ";
		//String urlParameters = "login_user=eximbank&login_password=!ex1m.b&CustomerID=12345&InvoiceID=12345&ExternalRef=12345&Amount=2000";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(URLParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + URL);
		System.out.println("Post parameters : " + URLParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) 
		{
			response.append(inputLine);
		}
		
		in.close();

		System.out.println(response.toString());
		ress = response.toString();	
	}
	
	private static void sendgepgtraPost() throws Exception 
	{	
		URL obj = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("content-type", "application/XML");
		
		//String urlParameters = "login_user=C02G8416DRJM&cn=&locale=&caller=&num= ";
		String urlParameters = "login_user=eximbank&login_password=!ex1m.b&CustomerID=12345&InvoiceID=12345&ExternalRef=12345&Amount=2000";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(URLParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + URL);
		System.out.println("Post parameters : " + URLParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		ress = response.toString();
	}
}
