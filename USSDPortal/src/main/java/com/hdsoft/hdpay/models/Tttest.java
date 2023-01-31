package com.hdsoft.hdpay.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import com.hdsoft.common.Common_Utils;

public class Tttest 
{
	public static void main(String args[]) throws ParseException, IOException
	{
		Common_Utils util = new Common_Utils();
		
		String datet ="{1:F01EXTNTZTZAXXX0000000000}{2:I103TARATZTZXXXXN}{3:{103:TIS}}{4:\r\n" + 
				":20:0088TrReport_Serial" + 
				":23B:CRED\r\n" + 
				":32A:dmyear+DBCurrs+TISSAMT" + 
				":50K:Receipt_No"+
				"\r\n@payerNameo@\r\n" +
				"TANZANIA|ibranchnoRefid"  +
				":56A:TANZTZTXXXX\r\n" + 
				":57A:TARATZTZXXX\r\n" + 
				":59:/CRAccountsw_cracna@\r\n" + "TANZANIA"+"\r\n"+
				":70:/ROC/billno\r\n" + 
				":71A:SHA\r\n" + 
				":72:/TRA/GePG PAYMENT\r\n" + 
				"-}\r\n" + 
				"";
		
		String datetime1 = util.getCurrentDate("yyyy-MM-dd'T'HHmmss");   	////9927700042692021-11-02T133615.txt
	
		String Tisspath = "C://EXIMPAY-GEPG//TISS//";  
		
		Files.createDirectories(Paths.get(Tisspath));
	
		String filename = "992770004269" + "-"+ datetime1 + ".txt";  //9927700042692021-11-02T133615.txt
		
		FileWriter writer = new FileWriter(Tisspath + filename , false);
		
		writer.write(datet);
		
		writer.close();
		
	}
}
