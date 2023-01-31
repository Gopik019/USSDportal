package com.hdsoft.hdpay.threads;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import com.hdsoft.common.Menu_Generation;
import com.hdsoft.hdpay.Repositories.Thread_Store;
import com.zaxxer.hikari.HikariDataSource;

@Controller
public class Thread_Engine 
{
	 public JdbcTemplate Jdbctemplate;
		
	 @Autowired
	 public void setJdbctemplate(HikariDataSource Datasource) 
	 {
		Jdbctemplate = new JdbcTemplate(Datasource);
	 }
	 
	 @Autowired
	 public Payment_Thread PT;
	 
	 @Autowired
	 public Identifier_Thread IdT;
	 
	 @Autowired
	 public Recon_Thread RT;
	 
	 @Autowired
	 public Callback_Thread CT;
	 
	 @Autowired
	 public Menu_Generation MG;
	 
	 @PostConstruct
	 public void onstartup() throws IOException 
	 {	 
		 /******* Dedicated Thread for Payment *******/
		 
		 Thread t1 = new Thread(PT);
			 
		 //t1.start(); 
		 
		 /******* Dedicated Thread for Identifier Registration *******/
		 
		 Thread t2 = new Thread(IdT);
			 
		 //t2.start(); 
		 
		 /******* Dedicated Thread for Callback Registration *******/
		 
		 Thread t3 = new Thread(CT);
			 
		 //t3.start(); 
		
		 /******* Dedicated Thread for Recon *******/
		
		 List<Thread_Store> All_Threads = RT.Get_Threads();
		
	     Thread[] threads = new Thread [All_Threads.size()];
	     
	     for(int i=0; i<threads.length; i++)
	     {
	         threads[i] = new Thread(new Recon_Thread(All_Threads.get(i)));
	         threads[i].setName(All_Threads.get(i).getThread_Name()); 

	        // threads[i].start();
	     }  
	 }
}
