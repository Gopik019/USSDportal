package com.hdsoft.hdpay.models;

import com.hdsoft.common.Common_Utils;

public class Test2 
{
	 public static void main(String args[]) throws Exception
	 {
		 Common_Utils util = new Common_Utils();
		 
		 String test = "<Gepg><gepgPmtSubReqAck><PayReqId>254385-2835934-85-23458342-5834-5</PayReqId><PayStsCode>Failed Response Code</PayStsCode>"+
						 "<PayStsDesc>Failed Response Description</PayStsDesc></gepgPmtSubReqAck>"+
						 "<gepgSignature>SignatureGoesHere</gepgSignature>"+
						 "</Gepg>";
		 
		 System.out.println(util.XMLToJsonObject(test));
	 }
}
