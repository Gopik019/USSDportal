package com.hdsoft.common;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class File_handling 
{	 
	  public String getDynamicName(MultipartFile file)
      {
    	  long Time_Milli = new Date().getTime();
    	  
   		  //String filename =  Time_Milli + "." + file.getOriginalFilename().split("\\.")[1];
    	  
    	  String filename =  Time_Milli + "." + FilenameUtils.getExtension(file.getOriginalFilename());
   		  
    	  return filename;
      }
	  
	  public File ConvertMultipartFileToFile_with_Dynamic_Name(MultipartFile file) throws IllegalStateException, IOException
      {
		 String File_Name = getDynamicName(file);
		 
		 //File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());
		 
		 File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+File_Name);
		  
		 file.transferTo(convFile);
		  
		 return convFile;
      }
	  
	  public File ConvertMultipartFileToFile(MultipartFile file) throws IllegalStateException, IOException
      {
		 File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());
		  
		 file.transferTo(convFile);
		  
		 return convFile;
      }
	  
	  public String Get_temp_path() throws IllegalStateException, IOException
      {
		 return System.getProperty("java.io.tmpdir") + "/";
      }
}
