package com.hdsoft.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import org.apache.log4j.Logger;

public class base64simulator2 
{
	private final  static Logger logger = Logger.getLogger(base64simulator2.class);
	
	static Socket s=null;
	public static String stan="1000";
	static OutputStream os=null;
	static InputStream is=null;
	static Properties p=new Properties();
	Properties p1=new Properties();
	static String server_ip="";
	static int port=0;
	static String log_path="";
	static String[] outField=new String[128];
	static String[] inField=new String[128];
	static char[] chrOutBITMAP = new char[128];
	static char[] chrInBITMAP = new char[128];
	static boolean secondary_BtMp=false;
	String _curPath="";	
	static byte[] outbinBITMAP1 = new byte[16];
	static byte[] outbinBITMAP2 = new byte[8];
	static byte[] inbinBITMAP = new byte[16];
	static String msgType="";
	static String inMsgType="";
	static byte[] OUT_MSG;
	File f=null;
	FileWriter fw=null;
	BufferedWriter bw=null;
	File f1=null;
	FileWriter fw1=null;
	BufferedWriter bw1=null;
	File f2=null;
	FileReader fr1=null;
	BufferedReader br1=null;

	static int[][] iso_map={
		
		   /* 1       2       3       4       5       6       7       8       9      10                */
            {0,8}, 	{1,2}, 	{0,6}, 	{0,16}, {0,12},	{0,12}, {0,10}, {0,8}, 	{0,8},  {0,8},     /* 10   */
            {0,12}, {0,14}, {0,4}, 	{0,4}, 	{0,6}, 	{0,4}, 	{0,8}, 	{0,4}, 	{0,3},	{0,3},     /* 20   */
            {0,3}, 	{0,12}, {0,3}, 	{0,3}, 	{0,4}, 	{0,4}, 	{0,1}, 	{0,6}, 	{0,3},  {0,24},    /* 30   */
            {1,2}, 	{1,2}, 	{1,2}, 	{1,2}, 	{1,2}, 	{1,3}, 	{0,12}, {0,6}, 	{0,3},  {0,3},     /* 40   */
            {0,16}, {0,15}, {1,2}, 	{1,2}, 	{1,2}, 	{1,3}, 	{1,3}, 	{1,3}, 	{0,3},  {0,3},     /* 50   */
            {0,3}, 	{0,8}, 	{1,2}, 	{1,3}, 	{1,3}, 	{1,2}, 	{0,3}, 	{1,2}, 	{1,3},  {1,3},     /* 60   */
            {1,3}, 	{1,3}, 	{1,3}, 	{0,8}, 	{0,8}, 	{1,3}, 	{0,2}, 	{0,3}, 	{0,3},  {0,3},     /* 70   */
            {0,8}, 	{1,3}, 	{0,6}, 	{0,10}, {0,10}, {0,10}, {0,10}, {0,10}, {0,10}, {0,10},    /* 80   */
            {0,10}, {0,10}, {0,10}, {0,10}, {0,10}, {0,16}, {0,16}, {0,16}, {0,16}, {0,10},    /* 90   */
            {0, 3}, {0, 3}, {1,2}, 	{1,2}, 	{1,2}, 	{1,3}, 	{0,16}, {0,25}, {1,2},  {1,2},     /* 100  */
            {1,2}, 	{1,2}, 	{1,2}, 	{1,3}, 	{0,16}, {0,16}, {0,10}, {0,10}, {1,2},  {1,2},     /* 110  */
            {1,3}, 	{1,3}, 	{1,3}, 	{1,3}, 	{1,3}, 	{1,3}, 	{1,3}, 	{1,3}, 	{1,3},  {1,3},     /* 120  */
            {1,3}, 	{1,3}, 	{1,3}, 	{1,3}, 	{1,3}, 	{1,3}, 	{1,3}, 	{0,8}      				   /* 128  */
            
    };

	public static void main (String args[])
	{
		formrequestnew("100", "0", "834", "5407195239", "5407195239", "narration", "500000", "1"+"|"+"1"+"|"+"1"+"|");
	}
	
	public static void lengthsetter()
	{
		String[] niv = new String[] { "0|8","1|2","0|6","0|16","0|6","0|16","0|10","0|12","0|10","0|8","0|12","0|14","0|12","0|14","0|4","0|4","0|8","0|4","0|8","0|4","0|3","0|3","0|3","0|3","0|3","0|3","0|4","0|12","0|1","0|6","0|3",
				"1|2","1|2","1|2","1|2","1|2","0|12","0|12","0|4","0|6","0|16","1|2","1|4","0|15","1|2","1|2","1|2","1|3","0|3","0|3","0|3","0|3","0|3","0|8","1|2","1|3","1|3","1|2","0|3","1|2","1|3","1|3","1|3","1|3","1|3","0|8","0|8","1|3","0|2","0|3","0|3","0|3","0|8","1|3","0|6",
				"0|10","0|10","0|10","0|10","0|10","0|10","0|10","0|10","0|10","0|10","0|10","0|10","0|16","0|16","0|16","0|16","0|10","0|3","0|3","1|2","1|2","0|42","1|3","0|16","0|25","1|2","1|2","1|2","1|2","1|2","1|3","0|16","0|16","0|10","0|10","1|2","1|2","1|3","1|3","1|3","1|3","1|3","1|3","1|3","1|3","1|3","1|3","1|3","1|3","1|3","1|3","1|3","1|3" };
		
		int j=0;  
		
		for (int i=0; i<=127; i++)
		{
			j=i+1;
			
			String DE = niv[i];
			
			logger.debug(j + " j - i " +i + " data " + DE);
			
			iso_map [i] [0] =  Integer.parseInt(DE.trim().substring(0,DE.trim().indexOf("|")));	
			iso_map [i] [1] =  Integer.parseInt(DE.trim().substring(DE.trim().indexOf("|")+1));
			
			j=0;
		}
	}

	public static String formrequestnew(String amount,String chargeamt, String cur,String souraccount, String Destinationno, String NARR, String REQCODE, String brncode)
	{
		logger.debug("Amount stage 2 " + amount );
		
		String error = "";
		
		try
		{	
			for(int i=0;i<128;i++)
			{
				outField[i]="";
				inField[i]="";
			}
			server_ip="192.168.3.33";
			port=7777;
			
			
			logger.debug("Server IP" + server_ip);
			logger.debug("Server PORT" + port);
			
			msgType="2200";
			
			lengthsetter();
			
			for(int i=0;i<128;i++)
			{
				try
				{
					chrOutBITMAP[i] = '0';
				}
				catch(Exception e)
				{
					logger.debug("Exception in Bit Map setting");
				}
			}
			
			Date currentDatetime = new Date(System.currentTimeMillis()); 
			String datetime = new SimpleDateFormat("MMddHHmmss").format(currentDatetime);
			String yeardatetime  = new SimpleDateFormat("yyyyMMddHHmmss").format(currentDatetime);
			String stanno  = new SimpleDateFormat("yyMMddHHmmss").format(currentDatetime);
			String branchcode = brncode;
			String date = new SimpleDateFormat("yyyyMMdd").format(currentDatetime);
			String SALTCHARS = "1234567890";
	        StringBuilder salt = new StringBuilder();
	        Random rnd = new Random();
	        
	        while (salt.length() < 12) 
	        { 
	            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	            salt.append(SALTCHARS.charAt(index));
	        }
	        
	        String stanno1 =  salt.toString();
	        
	        logger.debug("Random No " + stanno1);
	        
			int len = 0; 
			
			logger.debug("Amount stage 3 " + amount );
			
			len = amount.length();
			for (int e=len; e<=15; e++)
			{
				amount = "0" + amount;
			}
			
			len = 0;
			len = chargeamt.length();
			for (int e=len; e<=11; e++)
			{
				chargeamt = "0" + chargeamt;
			}
			
			len = 0;
			len = branchcode.length();
			for (int e=len; e<=15; e++)
			{
				branchcode = "0" + branchcode;
			}
			
			chrOutBITMAP[0] = '1';
		
			chrOutBITMAP[1] = '1';
			outField[1] = "1234567890";
			chrOutBITMAP[2] = '1';
			outField[2] = REQCODE;
			chrOutBITMAP[3] = '1';
			logger.debug("Amount stage 4 " + amount );
			outField[3] = amount;
			chrOutBITMAP[6] = '1';
			outField[6] = datetime;
			chrOutBITMAP[10] = '1';
			outField[10] = stanno1;
			chrOutBITMAP[11] = '1';
			outField[11] = yeardatetime;
			chrOutBITMAP[16] = '1';
			outField[16] = date;
			chrOutBITMAP[23] = '1';
			outField[23] = "200";
			chrOutBITMAP[27] = '1';
			outField[27] = chargeamt;
			chrOutBITMAP[31] = '1';
			outField[31] = "12354587865";
			
			chrOutBITMAP[36] = '1';
			outField[36] = stanno;
			chrOutBITMAP[40] = '1';
			outField[40] = branchcode;
			chrOutBITMAP[41] = '1';
			outField[41] = "12";
			chrOutBITMAP[42] = '1';
			outField[42] = NARR; 
			
			chrOutBITMAP[48] = '1';
			outField[48] =  cur;
			chrOutBITMAP[101] = '1';
			outField[101] = souraccount ;
			chrOutBITMAP[102] = '1';
			outField[102] = Destinationno;
			chrOutBITMAP[119] = '1';
			outField[119] = brncode;
			chrOutBITMAP[120] = '1';
			outField[120] = "TD|TC";
			chrOutBITMAP[122] = '1';
			outField[122] = "INT";
			chrOutBITMAP[123] = '1';
			outField[123] = "001";
			
			
			sendRequest();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			error = "Connection Time out";
			logger.debug("Error Simulator:"+e.getMessage());
			error = "R";	
		}
		
		return error;
	}

	static void sendRequest() 
	{
		int i,llvar=0;
        String len,lenstr=null;
        String request= "";
         
		try
		{
			String outbytes="";
			
			for(i=0;i<128;i++)
			{
				outbytes=outbytes+chrOutBITMAP[i]+",";
			}
			
			for(i=0;i<128;i++) 
			{
				
				logger.debug(request);
				
				if (i==41)
				{
					logger.debug(i);
				} 
				
				if(chrOutBITMAP[i]=='1')
				{
					if(iso_map[i][0]==1)
					{
						llvar= iso_map[i][1];
						
						if(llvar==2)
						{ 
							lenstr="00";
						}
						else if(llvar==3)
						{
							lenstr="000";
						}
						
						try
						{
	            			if(outField[i] != null && ! "".equals(outField[i]))
	            			{
	            				len = lenstr + outField[i].length();
	            				len = len.substring(len.length()-llvar);
	            				request = request + len + outField[i];	            				
	            			}
						}
						catch(Exception e)
						{
							logger.debug("Error in Simulator:"+e.getMessage());
						}
					}
					else
					{
						try
						{
							if(outField[i] != null && ! "".equals(outField[i]))
							{																
								request = request + outField[i];
							}
						}
						catch(Exception e)
						{
							logger.debug("Error:"+e.getMessage());
						}
					}								
				}
			}
			
			String chrBITMAP= new String(chrOutBITMAP);
            int bitmaplen=8;
            int msglen=0;
			int n=0;
            int m=8;
            int val=0;
            int[] Bitpwr = {128,64,32,16,8,4,2,1};
            
            if(chrOutBITMAP[0]=='1')
            {
            	bitmaplen=16;
            	
            	for(int k=0;k<16;k++)
            	{
            		String TkStr=chrBITMAP.substring(n,m);
            		n+=8;
            		m+=8;
            		val=0;
            		
            		for(int j=7;j>=0;--j)
            		{   
            			int ind=0;
            			if(TkStr.charAt(j)=='1') 
            				ind=1;
            			val = val + (ind * Bitpwr[j]);
            		}
            		
            		outbinBITMAP1[k]= (byte) val;
            	}
            }
            else 
            {
            	for(int k=0;k<8;k++)
            	{
            		String TkStr=chrBITMAP.substring(n,m);
            		
                	n+=8;
                	m+=8;
                	val=0;	
                	
                	for(int j=7;j>=0;--j)
                	{
                		int ind=0;
                		
                		if(TkStr.charAt(j)=='1') {
                			ind=1;
                		}
                		
                		val = val + (ind * Bitpwr[i]);
                	}
                	
                	outbinBITMAP2[k]= (byte) val;
            	}	
            }
            
            byte [] resBYTE= request.getBytes();
            byte [] mtiBYTE = msgType.getBytes();
            OUT_MSG = new byte[request.length()+ bitmaplen + 4 + 4];
            
            for(i=4; i<8; i++)
            {
            	OUT_MSG[i]= mtiBYTE[i-4];
            }                
               
            if(bitmaplen==8)
            {
         	   for(i=0;i<8;i++)
         	   {
         		   OUT_MSG[i+8]=outbinBITMAP2[i];   
         	   }
            }
            else 
            {
            	for(i=0;i<16;i++) 
            	{
            		OUT_MSG[i+8]=outbinBITMAP1[i];
            	}
            }
            
            for(i=0;i<request.length();i++)
            {
            	OUT_MSG[bitmaplen+8+i]=resBYTE[i];
            }
            
            msglen=request.length()+ bitmaplen + 4;
            String msgLenStr = String.valueOf(msglen);
            
    	    if(msgLenStr.length()!=4)
    	    {
    	    	for(int p=msgLenStr.length(); p < 4 ; p++) 
    	    	{
    	    		msgLenStr = "0" + msgLenStr;
    	    	}
    	    		
    	    }

    	    byte[] msgLenBYTE = msgLenStr.getBytes();
    	    
            for(i=0; i<msgLenStr.length() ; i++)
            {
           		OUT_MSG[i]=msgLenBYTE[i];
            }
                    
            logger.debug("Simulator: Request Sent to Server Successfully");
            
            String msgbytes="";
            
            for(i=0;i<OUT_MSG.length;i++)
            {
            	msgbytes=msgbytes+OUT_MSG[i]+",";
            }
            
            logger.debug("Request Message Bytes:"+msgbytes);
          
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.debug("Error in Simulator:"+e.getMessage());
		}     	
	}
}
