package com.hdsoft.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Date;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;

public class GenerateQRCode 
{
	public File generateQRCodeImage(String text, int width, int height, String fileType) throws WriterException, IOException 
	{
		File qrFile = Create_Temp_File(fileType);
		
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
 
        Path path = FileSystems.getDefault().getPath(qrFile.getPath());
        
        MatrixToImageWriter.writeToPath(bitMatrix, fileType, path);
        
        return qrFile;
    }
	
	public File generateBarCodeImage(String text, int width, int height, String fileType) throws WriterException, IOException 
	{
		File qrFile = Create_Temp_File(fileType);
	
        BitMatrix bitMatrix = new Code128Writer().encode(text, BarcodeFormat.CODE_128, width, height);
        
        Path path = FileSystems.getDefault().getPath(qrFile.getPath());
        
        MatrixToImageWriter.writeToPath(bitMatrix, fileType, path);
        
        return qrFile;
    }
	
	public File Create_Temp_File(String fileType) throws IllegalStateException, IOException
    {
		 String File_Name = getDynamicName(fileType);
		 
		 File Temp_File = new File(System.getProperty("java.io.tmpdir")+"/"+File_Name);
		  
		 return Temp_File;
    }
	
	public String getDynamicName(String fileType)
    {
		 long Time_Milli = new Date().getTime();
   	  
		 String filename =  Time_Milli +"."+ fileType;
  		  
   	  	 return filename;
    }
}
