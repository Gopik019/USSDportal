package com.hdsoft.common;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hdsoft.hdpay.models.Token_Info;
import com.zaxxer.hikari.HikariDataSource;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Token_System 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	public static ArrayList<Token_Info> HDPAY_TOKENS = new ArrayList<Token_Info>();
	
	public static String Secret_key = null;
	
	public String getSecret()
	{
		try
		{
			if(Secret_key == null)
			{
				Secret_key = new Common_Utils().Generate_Random_String(64).toUpperCase();
			}
		}
		catch(Exception Ex)
		{
			System.out.println(Ex.getLocalizedMessage());
		}

		return Secret_key;
	}
	
	public String getJWTToken(String SUBORGCODE, String CHCODE) 
	{
		String jwtToken = "";
		
		try
		{
			String secret = getSecret();  
			
			Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
			
			jwtToken = Jwts.builder()
			        .claim("Role", "CHANNEL")
			        .claim("SUBORGCODE", SUBORGCODE)
			        .claim("SYSCODE", "HP")
			        .claim("CHCODE", CHCODE)
			        .setSubject("EXIMPAY")
			        .setId(UUID.randomUUID().toString())
			        .setIssuedAt(new Date(System.currentTimeMillis()))
			        .setExpiration(new Date(System.currentTimeMillis() + (180 * 60 * 1000)))  /*** 180 Minutes ***/
			        .signWith(hmacKey)
			        .compact();
		}
		catch(Exception Ex)
		{
			Ex.getLocalizedMessage();
		}
		
		return jwtToken;
	}

	public int ValidateJWTToken(String SUBORGCODE, String CHCODE, String Token) 
	{
		int Status = 0;
		
		String secret = getSecret();
		
		try 
		{
			Token = Token.replace("Bearer", "");
			 
			Token = Token.trim();
			 
			Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());

			Jws<Claims> jwt = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(Token);  
			
			JsonObject info = new Gson().toJsonTree(jwt.getBody()).getAsJsonObject();
			
			if(info.has("CHCODE") && info.has("SUBORGCODE") &&  info.get("CHCODE").getAsString().equals(CHCODE) &&  info.get("SUBORGCODE").getAsString().equals(SUBORGCODE))
			{
				Status = 1;
			}
			else
			{
				Status = -1;
			}
		}
		catch(ExpiredJwtException expired)
		{
			Status = 0;
		}
		catch(Exception Ex)
		{
			Status = -1;
		}
		
		return Status;
	}
	
	public JsonObject decodeJWT(String Token) 
	{
		JsonObject details = new JsonObject();
		
		String secret = getSecret();
		
		try 
		{
			Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
			
			Jws<Claims> jwt = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(Token); 
			
			details.add("Header", new Gson().toJsonTree(jwt.getHeader()));
			details.add("Body", new Gson().toJsonTree(jwt.getBody()));
			details.add("Signature", new Gson().toJsonTree(jwt.getSignature()));
		}
		catch(Exception Ex)
		{
			System.out.println(Ex.getLocalizedMessage());   
		}
		
		return details;
	}
	
	public JsonObject get_stored_token(String SUBORGCODE, String CHCODE, String MODULEID)
	{
		JsonObject Token_details = new JsonObject();
		
		boolean flag = false;  
		
		for(int i=0 ; i< HDPAY_TOKENS.size(); i++)
		{
			if(HDPAY_TOKENS.get(i).getSUBORGCODE().equals(SUBORGCODE) && HDPAY_TOKENS.get(i).getCHCODE().equals(CHCODE) && HDPAY_TOKENS.get(i).getMODULEID().equals(MODULEID) && !Isexpiredtoken(HDPAY_TOKENS.get(i).getCREATED_AT()))
			{
				Token_details.addProperty("token", HDPAY_TOKENS.get(i).getTOKEN());
				
				flag = true;
				
				break;
			}
		} 
	
		Token_details.addProperty("Result", flag ? "Success" : "Failed");
		
		return Token_details;
	}
	
	public void Check_and_Update_tokens(Token_Info info)
	{
		 String SUBORGCODE = info.getSUBORGCODE();
		 String CHCODE = info.getCHCODE();
		 String MODULEID = info.getMODULEID();
		 String TOKEN = info.getTOKEN();
		 
		 boolean flag = false;
			
		 Common_Utils util = new Common_Utils();
		 
		 for(int i=0 ; i< HDPAY_TOKENS.size(); i++)
		 {
			if(HDPAY_TOKENS.get(i).getSUBORGCODE().equals(SUBORGCODE) && HDPAY_TOKENS.get(i).getCHCODE().equals(CHCODE) && HDPAY_TOKENS.get(i).getMODULEID().equals(MODULEID))
			{
				flag = true;
				
				HDPAY_TOKENS.get(i).setTOKEN(TOKEN);
				
				HDPAY_TOKENS.get(i).setCREATED_AT(util.getCurrentDateTime());
				
				break;
			}
		 } 
		
		if(!flag)
		{
			HDPAY_TOKENS.add(info);
		}
	}
	
	public boolean Isexpiredtoken(String Created_Time)
	{
		boolean expired = true;
		
		try
		{
			Common_Utils util = new Common_Utils();
			
			String new_time = util.getCurrentDateTime();
	        
	        String old_time = Created_Time;
	       
	        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  
	        Date date1 = simpleDateFormat.parse(old_time);
	        Date date2 = simpleDateFormat.parse(new_time);

	        long diff = date2.getTime() - date1.getTime();
	      
	        long differenceInHours = diff / (60 * 60 * 1000);

	        long differenceInMinutes = diff / (60 * 1000) % 60;

	        int diffInDays = (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
	  
	        if(diffInDays < 1 && differenceInHours < 1 && differenceInMinutes < 59) 
	        {
	        	expired = false;
	        } 
		}
		catch(Exception ex)
		{
			expired = true;
		}
		 
		return expired;
	}
	
	public void print_tokens()
	{
		for(int i=0 ; i< HDPAY_TOKENS.size(); i++)
		{
			 System.out.println(HDPAY_TOKENS.get(i).getSUBORGCODE());
			 System.out.println(HDPAY_TOKENS.get(i).getCHCODE());
			 System.out.println(HDPAY_TOKENS.get(i).getMODULEID());
			 System.out.println(HDPAY_TOKENS.get(i).getTOKEN());
			 System.out.println();
		} 
	}
}
