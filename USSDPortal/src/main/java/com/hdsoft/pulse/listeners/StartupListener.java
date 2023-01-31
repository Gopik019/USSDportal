package com.hdsoft.pulse.listeners;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> 
{
	 protected JdbcTemplate Jdbctemplate;
	
	 @Autowired	
	 public void setJdbctemplate(HikariDataSource Datasource) 
	 {
		 Jdbctemplate = new JdbcTemplate(Datasource);
	 }
	 
	 private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	 public void onApplicationEvent(final ContextRefreshedEvent event) 
	 {
	    // Dedicated_Thread DT = new Dedicated_Thread();
	     
	    // DT.Start();
	     
	   //  Event_Based_Thread ET = new Event_Based_Thread();
	     
	   //  ET.Start();
	     
	     logger.debug(">>>>>>>>>>>>>>>  Dedicated and Event Thread Started Successfully <<<<<<<<<<<<<<<<<<<");
	 }
}