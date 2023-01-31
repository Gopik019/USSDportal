package com.hdsoft.hdpay.models;

import javax.servlet.http.HttpSession;

public class Session_Model
{
	public static boolean IsSessionValid(HttpSession session) 
    {	 
   	 	 if(session.getAttribute("sesSessionID") !=null && session.getAttribute("sesUserId") !=null && session.getAttribute("sesDomainID") !=null) 
	     {
   	 		 return true;
	     }
	     else
	     {
	    	 return false;
	     }
    }
}
