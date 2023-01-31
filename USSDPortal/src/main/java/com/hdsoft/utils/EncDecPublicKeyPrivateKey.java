package com.hdsoft.utils;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncDecPublicKeyPrivateKey 
{
	protected static String DEFAULT_ENCRYPTION_ALGORITHM = "RSA";
	protected static int DEFAULT_ENCRYPTION_KEY_LENGTH = 1024;
	protected static String DEFAULT_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	protected String mEncryptionAlgorithm, mTransformation;
	protected int mEncryptionKeyLength;
	protected PublicKey mPublicKey;
	protected PrivateKey mPrivateKey;
	
	EncDecPublicKeyPrivateKey()
	{
		mEncryptionAlgorithm = EncDecPublicKeyPrivateKey.DEFAULT_ENCRYPTION_ALGORITHM;
		mEncryptionKeyLength = EncDecPublicKeyPrivateKey.DEFAULT_ENCRYPTION_KEY_LENGTH;
		mTransformation = EncDecPublicKeyPrivateKey.DEFAULT_TRANSFORMATION;
		mPublicKey = null;
		mPrivateKey = null;
	}
	
	public static BigInteger keyToNumber(byte[] byteArray)
	{
		return new BigInteger(1, byteArray);
	}
	
	public String getEncryptionAlgorithm()
	{
		return mEncryptionAlgorithm;
	}
	
	public int getEncryptionKeyLength()
	{
		return mEncryptionKeyLength;
	}
	
	public String getTransformation()
	{
		return mTransformation;
	}
	
	public PublicKey getPublicKey()
	{
		return mPublicKey;
	}
	
	public byte[] getPublicKeyAsByteArray()
	{
		return mPublicKey.getEncoded();
	}
	
	public String getEncodedPublicKey()
	{
		String encodedKey = Base64.getEncoder().encodeToString(mPublicKey.getEncoded());
		
		return encodedKey;
	}
	
	public PrivateKey getPrivateKey()
	{
		return mPrivateKey;
	}
	
	public byte[] getPrivateKeyAsByteArray()
	{
		return mPrivateKey.getEncoded();
	}
	
	public String getEncodedPrivateKey()
	{
		String encodedKey = Base64.getEncoder().encodeToString(mPrivateKey.getEncoded());
		
		return encodedKey;
	}
	
	public byte[] encryptText(String text) // Encrypt using Public Key
	{
		byte[] encryptedText = null;
		
		try 
		{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(mEncryptionAlgorithm);
			kpg.initialize(mEncryptionKeyLength);
			KeyPair keyPair = kpg.generateKeyPair();
			mPublicKey = keyPair.getPublic();
			mPrivateKey = keyPair.getPrivate();
			Cipher cipher = Cipher.getInstance(mTransformation);
			cipher.init(Cipher.PUBLIC_KEY, mPublicKey);
			encryptedText = cipher.doFinal(text.getBytes());
		} 
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} 
		catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		}
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalBlockSizeException e) 
		{
			e.printStackTrace();
		} 
		catch (BadPaddingException e)
		{
			e.printStackTrace();
		}
		
		return encryptedText;
	}
	public byte[] decryptText() // Decrypt using Public Key
	{
		String str = "9a90e64095a842c2e3727e11de4bb3135e4f1acd4b417270e4b299016601f2ce";
		
		byte[] encryptedText = str.getBytes();
		  
		byte[] decryptedText = null;
		
		try 
		{
			Cipher cipher = Cipher.getInstance(mTransformation);
			cipher.init(Cipher.PRIVATE_KEY, mPrivateKey);
			decryptedText = cipher.doFinal(encryptedText);
			
			System.out.println(decryptedText);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		} 
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalBlockSizeException e) 
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			e.printStackTrace();
		}
		
		return decryptedText;
	}
	

	
	public static void main(String args[])
	{
		EncDecPublicKeyPrivateKey t = new EncDecPublicKeyPrivateKey();
		
		t.decryptText();
	}
}
