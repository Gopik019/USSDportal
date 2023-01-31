package com.hdsoft.pulse.listeners;

import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;

public class SessionListner implements HttpSessionListener   
{
	 private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	 private final AtomicInteger counter = new AtomicInteger();
	 
	 public void sessionCreated(HttpSessionEvent se) 
	 {
		 logger.info("New session is created. Adding Session to the counter.");
		 
	     counter.incrementAndGet();  
	        
	     updateSessionCounter(se);
	 }

     public void sessionDestroyed(HttpSessionEvent se) 
     {
    	 logger.info("Session destroyed. Removing the Session from the counter.");
    	 
         counter.decrementAndGet();  
         
         updateSessionCounter(se);
     }
     
     private void updateSessionCounter(HttpSessionEvent httpSessionEvent)
     {
         httpSessionEvent.getSession().getServletContext().setAttribute("activeSession", counter.get());
                 
         logger.info("Total active session are {} "+counter.get());
     }
}