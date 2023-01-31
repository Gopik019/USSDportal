package com.hdsoft.hdpay.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Random;

public class Test 
{
	public static String get_signature(String PAYLOAD)
	{
		String Signeddata = "";
		
		String password = "P@SSphrase";
		String alias = "013";

		try
		{
			char[] passord = null;
			
			PrivateKey privateKey = null;
			PublicKey publicKey = null;
				
		    File file = new File("C:\\HDPAY\\TIPS\\EXIMTZT0Keystore.jks");
			
		    InputStream stream = new FileInputStream(file);
		    java.security.KeyStore keyStore1 = KeyStore.getInstance("JKS");  
		    keyStore1.load(stream, passord);

		    privateKey = (PrivateKey) keyStore1.getKey(alias, password.toCharArray());
		 
		    //System.out.println("Private Key " + privateKey); 
		    
		    File file2 = new File("C:\\HDPAY\\TIPS\\EXIMTZT0Keystore.jks");
			
		    InputStream stream2= new FileInputStream(file2);
		    
		    KeyStore ks = KeyStore.getInstance("JKS");
		    ks.load(stream2, password.toCharArray());
		    
		    Key key = ks.getKey(alias, password.toCharArray());
		    
		    if(key instanceof PrivateKey) 
		    {
		        Certificate cert = (Certificate) ks.getCertificate(alias);

		        publicKey = cert.getPublicKey();

		        new KeyPair(publicKey, (PrivateKey) key);
		    }
		  
		    //System.out.println("Public Key" + publicKey);
			    
		    String dgdata = PAYLOAD;
		    
	        byte[] data = dgdata.getBytes("UTF8");
	
	        Signature sig = Signature.getInstance("SHA256WithRSA");
	        
	        sig.initSign(privateKey);
	        sig.update(data);
	        
	        byte[] signatureBytes = sig.sign();
	        
	        Signeddata = Base64.getEncoder().encodeToString(signatureBytes);
	        
	        Signeddata = Signeddata.replaceAll("\n", "");
	        Signeddata = Signeddata.replaceAll("\r", "");
	        
	        sig.initVerify(publicKey);
	        sig.update(data);     	        
	        
	        System.out.println("signature Verification ::: "+sig.verify(signatureBytes));  
		}
		catch(Exception e)
		{
			System.out.println("Exception in signature Verification ::: "+e.getLocalizedMessage());
		}
		
		System.out.println("Encrypted data :::: "+Signeddata);
		
		return null;
	}
	
	 public static void main(String args[]) throws Exception
	 {
		 get_signature("hai");
	 }
}
