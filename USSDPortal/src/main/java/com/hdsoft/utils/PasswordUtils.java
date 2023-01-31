package com.hdsoft.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class PasswordUtils 
{
	public static JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	public static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static final String NUMERIC = "0123456789";
	public static final String SPECIAL_CHARACTERS = "<=:`[?;,\\^_}@>.)\"('$#*%~-/&!]{|+";
	public static final String GENERATION_SPECIAL_CHARACTERS = "@#$%^&*()+!-=_";
	public static final String GENERATION_COMBINATION = ALPHA + NUMERIC + GENERATION_SPECIAL_CHARACTERS;
	private static final int MIN_PASSWORD_LENGTH = 8;
	private static final int MIN_ALPHA_LENGTH = 4;
	private static final int MIN_NUMERIC_LENGTH = 2;
	private static final int MIN_SPECIAL_LENGTH = 2;
	private final static Logger logger = Logger.getLogger(PasswordUtils.class);
	
	private static byte[] doSHA256(byte[] input) 
	{
		byte[] output = null;
		
		try
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			output = digest.digest(input);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			output = input;
		}
		
		return output;
	}

	private static String toHexString(byte[] data) 
	{
		StringBuffer buf = new StringBuffer();
		
		for (int i = 0; i < data.length; ++i) 
		{
			String s = Integer.toHexString(data[i] & 0XFF);
			buf.append((s.length() == 1) ? ("0" + s) : s);
		}
		
		return buf.toString();
	}

	public static final String encryptData(String key)
	{
		return key;
	}

	public static final String getEncrypted(String password, String userID, String salt) 
	{
		String shaValue1 = getEncryptedUserIDPassword(password, userID);
		String shaValue2 = getEncryptedHashSalt(shaValue1, salt);
		
		return shaValue2;
	}

	public static final String getEncryptedUserIDPassword(String password, String userID)
	{
		String shaValue1 = toHexString(doSHA256(password.getBytes()));
		String shaValue2 = toHexString(doSHA256((shaValue1 + userID).getBytes()));
		
		return shaValue2;
	}

	public static final String getEncryptedHashSalt(String hash, String salt) 
	{
		String shaFinal = toHexString(doSHA256((hash + salt).getBytes()));
		
		return shaFinal;
	}

	public static final String getSalt() 
	{
		SecureRandom random = new SecureRandom((System.currentTimeMillis() + "" + System.nanoTime()).getBytes());
		
		int index = 0;
		
		StringBuffer buffer = new StringBuffer(128);
		
		for (int i = 0; i < 128; ++i) 
		{
			index = random.nextInt(GENERATION_COMBINATION.length());
			buffer.append(GENERATION_COMBINATION.charAt(index));
		}
		
		String shaFinal = toHexString(doSHA256(buffer.toString().getBytes()));
		
		return shaFinal;
	}

	public static void main(String[] args) throws Exception 
	{
		/*String salt = getSalt();
		System.out.println(getEncrypted("laser@12345", "ADMINMAKER", salt));	
		System.out.println(salt);
		
		salt = getSalt();		
		System.out.println(getEncrypted("laser@12345", "ADMINCHECKER", salt));
		System.out.println(salt);
		
		salt = getSalt();
		System.out.println(getEncrypted("laser@12345", "OPERMAKER", salt));
		System.out.println(salt);
		salt = getSalt();
		System.out.println(getEncrypted("laser@12345", "OPERCHECKER", salt));
		System.out.println(salt);

		salt = getSalt();
		System.out.println(getEncrypted("laser@12345", "OLTASMAKER", salt));
		System.out.println(salt);
		
		salt = getSalt();
		System.out.println(getEncrypted("laser@12345", "OLTASCHECKER", salt));
		System.out.println(salt);
		
		salt = getSalt();
		System.out.println(getEncrypted("laser@12345", "CUSTMAKER", salt));
		System.out.println(salt);
		
		salt = getSalt();
		System.out.println(getEncrypted("laser@12345", "CUSTCHECKER", salt));
		System.out.println(salt);
		*/
		
		//String salt = getSalt();		
		//System.out.println(getEncrypted("e661e48b65fd264a096dd07522f6f18c680d5efabd986295349a2150a90ceef7", "saro", salt));
		//System.out.println(salt);
		
		EncryptDecrypt decryption = new EncryptDecrypt();
	
		String pwd = "0bdb68f5c3c43640e77ec299ec93e9259a72382 2a802576f7017c b10b6b82f8";
		
		String randomSalt = "dce286b1852dae360f8aa9e8f42145c2c6616696ba6b88af3fbfb53888575d80";
		
		pwd = decryption.doDecrypt(pwd, randomSalt);
		
		System.out.println("Result ::::: "+getEncrypted(pwd, "gopi", ""));
		System.out.println("Expect ::::: b0846190686459b447424083729cb26a3ac47cc2b1fb817becdfe2752d934da6");
	}

	public static final String generatePassword(String userID, int minLength, int minAlpha, int minNumeric, int minSpecial) 
	{
		byte[] seedData = (userID + System.currentTimeMillis() + "" + System.nanoTime()).getBytes();
		SecureRandom random = new SecureRandom(seedData);
		String password = null;
		StringBuffer buffer = new StringBuffer();
		int lengthCounter = 0;
		int partCounter = 0;
		int index = 0;
		if (minLength == 0) minLength = MIN_PASSWORD_LENGTH;
			
		if (minAlpha == 0) minAlpha = MIN_ALPHA_LENGTH;
			
		if (minNumeric == 0) minNumeric = MIN_NUMERIC_LENGTH;
			 
		if (minSpecial == 0) minSpecial = MIN_SPECIAL_LENGTH;
			
		while (partCounter < minAlpha)
		{
			index = random.nextInt(ALPHA.length());
			buffer.append(ALPHA.charAt(index));
			partCounter++;
			lengthCounter++;
		}
		
		partCounter = 0;
		
		while (partCounter < minNumeric) 
		{
			index = random.nextInt(NUMERIC.length());
			buffer.append(NUMERIC.charAt(index));
			partCounter++;
			lengthCounter++;
		}
		
		partCounter = 0;
		
		while (partCounter < minSpecial) 
		{
			index = random.nextInt(GENERATION_SPECIAL_CHARACTERS.length());
			buffer.append(GENERATION_SPECIAL_CHARACTERS.charAt(index));
			partCounter++;
			lengthCounter++;
		}
		while (lengthCounter < minLength) 
		{
			index = random.nextInt(ALPHA.length());
			buffer.append(ALPHA.charAt(index));
			lengthCounter++;
		}
		
		buffer.trimToSize();
		password = buffer.toString();
		return password;
	}
	
	public static String getSalt(String userId)
	{
		String finalSalt = "";
		
		try 
		{
			 String sql = "SELECT VERIFY1 from USERS0001 WHERE USERSCD = ?";
			
			 List<String> result = Jdbctemplate.queryForList(sql, new Object[] {userId}, String.class);

			 if (result.size() != 0) 
			 {
				  finalSalt = result.get(0);
			 } 
			 
			 //logger.debug("finalSalt ::: "+finalSalt);	
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
		return finalSalt;
	}
}
