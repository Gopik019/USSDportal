package com.hdsoft.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import com.hdsoft.pulse.Repositories.SMS_Store;
import com.hdsoft.utils.PreparedQueryManager;

public class SMS_Service
{
	String apikey         = "/sqeHX7ht/0-O67HykvGhr5GQcCZNquCY39o3s9XTA";
	
	String Sender_Id      = "TXTLCL";
	
	public boolean Send_SMS(String Content, String PhoneNumber) 
	{	
		boolean Status =  true;
		
		try
        {
        	String apiKey = "apikey=" + apikey;
			String message = "&message=" + Content;
			String sender = "&sender=" + Sender_Id;
			String numbers = "&numbers=" + PhoneNumber;
			
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			final StringBuffer stringBuffer = new StringBuffer();
			
			String line;
			
			while ((line = rd.readLine()) != null)
			{
				stringBuffer.append(line);
			}
			
			rd.close();
        }
        catch (Exception e)
        {
        	Status = false;
        }
		
		return Status;
	}  
}	