package com.hdsoft.utils;

import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;

public class TrippleDes 
{
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;

    public TrippleDes(String keyValue) throws Exception 
    {
    	myEncryptionKey = keyValue;
       // myEncryptionKey = "ThisIsSpartaThisIsSparta";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }

    public String encrypt(String unencryptedString) 
    {
        String encryptedString = null;
        
        try
        {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            encryptedString = "";
        }
        
        return encryptedString;
    }


    public String decrypt(String encryptedString)
    {
        String decryptedText=null;
        
        try 
        {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString.getBytes());
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            encryptedString = "";
        }
        
        return decryptedText;
    }


    public static void main(String args []) throws Exception
    {
        TrippleDes td= new TrippleDes("RETAILBANKRAMANBHAIqqwefg7");
        TrippleDes td1= new TrippleDes("RETAILBANKRAMANBHAIqqwefg7889j");

        String target="sathish";
        String encrypted=td.encrypt(target);
        String decrypted=td1.decrypt(encrypted);

        System.out.println("String To Encrypt: "+ target);
        System.out.println("Encrypted String:" + encrypted);
        System.out.println("Decrypted String:" + decrypted);
    }
}