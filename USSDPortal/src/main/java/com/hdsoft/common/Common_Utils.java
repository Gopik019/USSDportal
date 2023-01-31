package com.hdsoft.common;

import java.io.StringReader;
import java.sql.Blob;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.InputSource;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Common_Utils 
{
	// ########################## Random String & OTP  utils ########################## */
	
	public String Generate_OTP(int size)
	{
		String numbers = "0123456789"; 
		
        char[] otp = new char[size]; 
  
        for (int i = 0; i < size; i++)  otp[i] = numbers.charAt(new Random().nextInt(numbers.length())); 
             
        return String.valueOf(otp); 
	}
	
	public String Generate_Random_String(int size)
	{
		String numbers = "abcdefghijklmnopqrstuvwxyz0123456789"; 
		  
        char[] user_id = new char[size]; 
  
        for (int i = 0; i < size; i++)  user_id[i] = numbers.charAt(new Random().nextInt(numbers.length())); 
        
        return String.valueOf(user_id);
	}
	
	public String Generate_Random_Amount()
	{
		double min = 10.00;
		
		double max = 10000.00;
		
		double diff = max - min;
		
		DecimalFormat formatter = new DecimalFormat("#0.00"); 
		
		double randomValue = min + Math.random( ) * diff;
		
		double tempRes = Math.floor(randomValue * 10);
		
		double finalRes = tempRes/10;
		
        return formatter.format(finalRes);
	}
	
/*	public String generateNewToken() 
	{
		SecureRandom secureRandom = new SecureRandom();
		
		Base64.Encoder base64Encoder = Base64.getUrlEncoder();
		
	    byte[] randomBytes = new byte[24];
	    secureRandom.nextBytes(randomBytes);
	    return base64Encoder.encodeToString(randomBytes);
	} */
	
	// ########################## String Replace, Nullability utils ########################## */
	
	public String ReplaceNull(String Value)
	{
		return Value == null || Value.isEmpty() ? "" : Value.trim();
	}
	
	public String ReplaceNull(Object Value)
	{
		return Value == null ? "" : Value.toString();
	}
	
	public String Replacewhiespaces(String Value)
	{
		Value = Value.replaceAll("\n", "");
		Value = Value.replaceAll("\r", "");
		    
		return Value;
	}
	
	public String Replace_words_from_String_(String Source, String Old_String, String Replace_String)
	{
		if(Source == null || Source.isEmpty())  
		{
			return "";
		}
		else
		{
			Source = Source.replaceAll(Old_String, Replace_String);
			
			return Source;
		}
	}
	
	public String Replace_Special_Characters(String Source)
	{
		Source = Source.replaceAll("[^a-zA-Z0-9]", "_");
		
		Source = Source.replaceAll("\\s+", "_");
	
		return Source;
	}
	
	public boolean isNullOrEmpty(String str)
	{
	     if(str != null && !str.isEmpty())  return false;
	           
	     return true;
	}
	
	// ########################## Arraylist utils ########################## */
	
	public ArrayList<String> Remove_Dupicates(ArrayList<String> al)
	{
		LinkedHashSet<String> lhs = new LinkedHashSet<String>();
		
		lhs.addAll(al);
		
		al.clear();
		
		al.addAll(lhs);
		
		return al;
	}
	
	// ########################## Date utils ########################## */
	
	public String getCurrentDate() throws ParseException   /* Default my sql date format :::: yyyy-MM-dd */
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dateWithoutTime = sdf.parse(sdf.format(new Date()));
		
		return sdf.format(dateWithoutTime).toString();
	}
	
	public String getCurrentYear() throws ParseException   /* Default my sql date format :::: yyyy-MM-dd */
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		
		Date dateWithoutTime = sdf.parse(sdf.format(new Date()));
		
		return sdf.format(dateWithoutTime).toString();
	}
	
	public String getCurrentDate(String Format) throws ParseException /* Available Formats :::: dd-MMM-yyyy , yyyy-MM-dd, MMM dd, yyyy */
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Format);
		
		Date dateWithoutTime = sdf.parse(sdf.format(new Date()));
		
		return sdf.format(dateWithoutTime).toString();
	}
	
	public String Convert_Date_Format(String Date_value, String From_Format, String To_Format) throws ParseException
	{
		if(Date_value != null && !Date_value.isEmpty())  
		{
			SimpleDateFormat format1 = new SimpleDateFormat(From_Format);
		 
		    SimpleDateFormat format2 = new SimpleDateFormat(To_Format);
		    
		    Date date = format1.parse(Date_value);
	    
		    return format2.format(date);
		}
		else
		{
			return "";
		}
	}
	
	public String getCurrentDateTime_add(int addMinuteTime)
	{
		String res = "";
		
		try 
		{
			 Date targetTime = Calendar.getInstance().getTime();
			
			 targetTime = DateUtils.addMinutes(targetTime, addMinuteTime); 
			 
			 SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			 
			 res = format1.format(targetTime); 
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		       
		 return res;	 
	}
	
	public String getCurrentDateTime()
	{
		String res = "";
		
		try 
		{
			 Date targetTime = Calendar.getInstance().getTime();
			
			 SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			 
			 res = format1.format(targetTime); 
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		       
		 return res;	 
	} 
	
	public boolean isvalidDate(String value, String DATE_FORMAT)
	{
		try 
		{
			 DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			 
			 df.setLenient(false);
	         df.parse(value);
	         
	         return true;
		} 
		catch(Exception ex) 
		{
			return false;
		}
	}
	
	public Timestamp String_To_Timestamp(String dateString)
	{
		Timestamp timestamp = null;
		
		try 
		{
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			Date date = formatter.parse(dateString);

			timestamp = new Timestamp(date.getTime());
			
			return timestamp; 
		} 
		catch(Exception ex) 
		{
			return null;
		}
	}
	
	// ########################## Time utils ########################## */
	
	public Timestamp get_oracle_Timestamp()
	{
		return new java.sql.Timestamp(new java.util.Date().getTime());
	}
	
	public String getCurrentTime_with_seconds()
	{
		SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm:ss a");
		
		return formatDate.format(new Date()).toString(); 
	}
	
	public String getCurrentTime_12_hr()
	{
		SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");
		
		return formatDate.format(new Date()).toString(); 
	}
	
	public String Convert_24_Hr_to_12_hr_Time(String _24HourTime)
	{
		try 
		{       
           SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
           SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
           Date _24HourDt = _24HourSDF.parse(_24HourTime);
           
           return _12HourSDF.format(_24HourDt);
	    } 
		catch (Exception e) 
		{
			return "";
	    }
	}
	
	public String Convert_12_Hr_to_24_hr_Time(String _12HourTime)
	{
		try 
		{       
		   SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
           SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
           
           Date _12HourDt = _12HourSDF.parse(_12HourTime);
           
           return _24HourSDF.format(_12HourDt);
	    } 
		catch (Exception e) 
		{
			return "";
	    }
	}
	
	public String Convert_12_hr_and_Concat(String From_Time, String To_Time)
	{
		if(From_Time != null && !From_Time.equals(""))
		{
			if(To_Time != null && !To_Time.equals(""))
			{
				return Convert_24_Hr_to_12_hr_Time(From_Time) + " to "+Convert_24_Hr_to_12_hr_Time(To_Time);
			}
			else
			{
				return Convert_24_Hr_to_12_hr_Time(From_Time);
			}
		}
		else
		{
			return "";
		}
	}

	// ########################## Size conversion utils ########################## */
	
	public String bytesIntoHumanReadable(long bytes) 
	{
	    long kilobyte = 1024;
	    long megabyte = kilobyte * 1024;
	    long gigabyte = megabyte * 1024;
	    long terabyte = gigabyte * 1024;

	    if ((bytes >= 0) && (bytes < kilobyte)) 
	    {
	        return bytes + " B";
	    }
	    else if ((bytes >= kilobyte) && (bytes < megabyte)) 
	    {
	        return (bytes / kilobyte) + " KB";
	    }
	    else if ((bytes >= megabyte) && (bytes < gigabyte)) 
	    {
	        return (bytes / megabyte) + " MB";
	    } 
	    else if ((bytes >= gigabyte) && (bytes < terabyte))
	    {
	        return (bytes / gigabyte) + " GB";
	    }
	    else if (bytes >= terabyte) 
	    {
	        return (bytes / terabyte) + " TB";
	    } 
	    else 
	    {
	        return bytes + " Bytes";
	    }
	}
	
	// ########################## Json utils ########################## */
	
	public JsonObject StringToJsonObject(String NormalString)
	{		
		JsonObject obj = new JsonObject();
		
		try
		{
			if(NormalString != null && !NormalString.isEmpty())  
			{
				JsonParser parser = new JsonParser();
				 
				obj = parser.parse(NormalString).getAsJsonObject();
			}
		}
		catch(Exception e)
		{
			obj = new JsonObject();
		}
				
		return obj;	
	}
	
	public JsonArray StringToJsonArray(String Json_String)
	{		
		JsonArray arr = new JsonArray();
		
		try
		{
			if(Json_String != null && !Json_String.isEmpty())  
			{
				JsonParser parser = new JsonParser();
				 
				JsonElement Element = parser.parse(Json_String);
				 
				arr = Element.getAsJsonArray();
			}
		}
		catch(Exception e)
		{
			arr = new JsonArray();
		}

		return arr;	
	}

	public JsonObject XMLToJsonObject(String XMLString)
	{		
		JsonObject obj = new JsonObject();
		
		try
		{
			JSONObject json = XML.toJSONObject(XMLString);  
			
			String jsonString = json.toString();  
			
			obj = StringToJsonObject(jsonString);
		}
		catch(Exception e)
		{
			obj = new JsonObject();
		}
	
		return obj;	
	}
	
	public String JsonToXML(String Json_String)
	{		
		String xml = "";
		
		try
		{
			JSONObject json = new JSONObject(Json_String);
			
			xml = XML.toString(json);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
		return xml;	
	}
	
	public JsonArray SplitStringToJsonArray(String Json_String, String Split_char)
	{		
		JsonArray arr = new JsonArray();
		
		try
		{
			if(Json_String != null && !Json_String.isEmpty())  
			{
				String[] Json_Strings = Json_String.split(Split_char);
				
				for(String Str : Json_Strings)
				{
					arr.add(Str);
				}
			}
		}
		catch(Exception e)
		{
			arr = new JsonArray();
		}

		return arr;	
	}
	
	public String JsonNullRemover(JsonObject details, String Member)
	{		
		String Result = "";
		
		try
		{
			if(!details.get(Member).isJsonNull())  
			{
				Result = details.get(Member).getAsString();
			}
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
				
		return Result;	
	}
	
	public boolean JsonMemberNullChecker(JsonObject details, String[] arr)
	{		
		boolean Result = false;
		
		try
		{
			int Valid = 0; int Not_Valid = 0;
			
			for(String Member : arr)
			{
				if(details.has(Member) && !details.get(Member).isJsonNull()) // &&  !details.get(Member).getAsString().equals(""))  
				{
					Valid++;
				}
				else
				{
					Not_Valid++;
				}
			}
			
			if(Valid !=0 && Not_Valid == 0)
			{
				Result = true;
			}
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
				
		return Result;	
	}
	
	public JsonArray get_key_vals(JsonObject object)
	{
		JsonArray Pairs = new JsonArray();
		
		String str = object.toString(); 

        JsonParser parser = new JsonParser();
        
        JsonObject jObj = (JsonObject)parser.parse(str);

        List<String> keys = new ArrayList<String>();
        
        for (Entry<String, JsonElement> e : jObj.entrySet()) 
        {
            keys.add(e.getKey());
        }
        
        for(Entry<String, JsonElement> entry: jObj.entrySet()) 
		{
       	 	JsonObject form_datas = new JsonObject();
       	 
       	 	form_datas.addProperty("Key", entry.getKey());  
       	 	form_datas.addProperty("Value", entry.getValue().getAsString());  
       	 
       	 	Pairs.add(form_datas);
		 }
        
        return Pairs;
	}
	
	public boolean isJSONValid(String test) 
	{
	    try 
	    {
	        new JSONObject(test);
	    } 
	    catch (JSONException ex) 
	    {
	        try 
	        {
	            new JSONArray(test);
	        } 
	        catch (JSONException ex1) 
	        {
	            return false;
	        }
	    }
	    
	    return true;
	}
	
	// ########################## XML utils ########################## */
	
	public boolean isXMLValid(String test) 
	{
	    try 
	    {
	        SAXParserFactory.newInstance().newSAXParser().getXMLReader().parse(new InputSource(new StringReader(test)));
	        
	        return true;
	    } 
	    catch(Exception ex) 
	    {
	    	return false;
	    }
	}
	
	// ###################### Pagination utils ######################## //
	
	public String get_pagination_query(int page, int limit)
	{
		limit = limit < 1 ? 1 : limit;
		
		page = page <= 1 ? 0 : (page-1) * limit;
		
		return " limit "+page+" , "+limit;
	}
	
	// ###################### string builder utils ######################## //
	
	public String Multiparame_sql_statement(String Keys[])
	{
		String out = "";
		 
		for(int i=0;i<Keys.length;i++)
		{
			 out = out+ " "+ Keys[i] + " = ? and "; 
		}
		
		out = out + ",";
		
		out = out.replaceAll("and ," , "");

		return out;
	}
	
	public Object[] Convert_to_object_arrayt(String Values[])
	{
		Object[] obj = new Object[Values.length];
	
		for(int i=0;i<Values.length;i++)
		{
			obj[i] =  Values[i]; 
		}
		
		return obj;
	}
	
	public String Blob_to_string(Blob file)
	{	
		String Res = "";
		
		try 
		{
			 if(file == null) 
			 {
			      return Res;
			 }
			 
			 byte[] bdata = file.getBytes(1, (int) file.length());
			
			 if(bdata == null || bdata.length == 0) 
			 {
			      return Res;
			 }
			
			Res = new String(bdata);
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
 			
 		return Res;	
	}
	
	public String current_Url(HttpServletRequest request)
	{
	    return request.getRequestURL().toString() + request.getQueryString() == null ? "" :  "?" + request.getQueryString() ;
	}
	
/*	public String getJWTToken(String Username, String Role) 
	{
		String secretKey = "mySecretKey";
	
		String token = "";
		
		try 
		{
			String id = Generate_Random_String(8);
			
			List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(Role);
					
			token = Jwts.builder()
					.setId(id)
					.setSubject(Username)
					.setIssuer("EXIM")
					.claim("authorities",grantedAuthorities.stream()			
					.collect(Collectors.toList()))
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + (1 * 60 * 1000))) 
					.signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
					.compact(); 			
		} 
		catch(Exception e) 
		{  
		    e.printStackTrace();
		}	
		
		return token;
	}
	
	public boolean VerifyJWTToken(String token) 
	{
		 String secretKey = "mySecretKey"; 
		
		 try 
		 {  
			//Jws<Claims> jwtClaims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);  
		
			//String subject = jwtClaims.getBody().getSubject();   
				
			 Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
			
			 return true;
		 } 
		 catch(Exception e) 
		 {  
		     return false; 
		 }
	}
	
	public JsonObject printStructure(String token) 
	{
		JsonObject obj = new JsonObject();
		
		String secretKey = "mySecretKey";
		
		Jws<?> parseClaimsJws = Jwts.parser().setSigningKey(secretKey.getBytes(Charset.forName("UTF-8"))).parseClaimsJws(token);
		
		obj.addProperty("Header", parseClaimsJws.getHeader().toString());
		obj.addProperty("Body", parseClaimsJws.getBody().toString());
		obj.addProperty("Signature", parseClaimsJws.getSignature().toString());
		
		return obj;
	}
	
	public JsonObject printBody(String token) 
	{
		JsonObject obj = new JsonObject();
		
		String secretKey = "mySecretKey";
		
		Claims body = Jwts.parser().setSigningKey(secretKey.getBytes(Charset.forName("UTF-8"))).parseClaimsJws(token).getBody();

		obj.addProperty("Issuer", body.getIssuer());
		obj.addProperty("Subject", body.getSubject());
		obj.addProperty("Expiration", body.getExpiration().toString());
		
		return obj;
	} */
	

}
