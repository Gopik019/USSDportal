package com.hdsoft.configuration;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import com.hdsoft.common.Database;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.hdsoft.common", "com.hdsoft.utils",  
"com.hdsoft.fiu.controllers", "com.hdsoft.fiu.models", 
"com.hdsoft.pulse.controllers", "com.hdsoft.pulse.models", 
"com.hdsoft.hdpay.controllers", "com.hdsoft.hdpay.models", "com.hdsoft.hdpay.threads" ,
"com.hdsoft.whatsapp_banking.controllers","com.hdsoft.whatsapp_banking.models",
"com.hdsoft.ussd.controllers","com.hdsoft.ussd.models","com.hdsoft.whatsapp_banking.Repositories","com.hdsoft.ussd.Repositories"
}) 

public class WebConfiguration implements WebMvcConfigurer, Database
{
   @Bean
   public InternalResourceViewResolver viewResolver()	
   { 
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setPrefix("/Views/");
		vr.setSuffix(".jsp");		
		return vr;
   }

   @Bean
   public CommonsMultipartResolver multipartResolver() 
   {
       CommonsMultipartResolver resolver=new CommonsMultipartResolver();
       resolver.setDefaultEncoding("utf-8");
       resolver.setMaxUploadSize(50 * 1024 * 1024);  // 50 MB
       return resolver;
   }
   
   public void addResourceHandlers(ResourceHandlerRegistry registry)
   {
       registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/statics/").setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
      
       registry.addResourceHandler("/File_Uploads/**").addResourceLocations("classpath:/File_Uploads/").setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
   }     
   
   @Bean
   public static HikariDataSource getDataSource()
   {
	   final HikariConfig config = new HikariConfig();
	   final HikariDataSource datasource;

	   config.setDriverClassName(Database_Driver);
	   config.setJdbcUrl(Connection_URL);  
	   config.setUsername(DB_User); 
	   config.setPassword(DB_Pass); 
       config.addDataSourceProperty("cachePrepStmts", "true");
       config.addDataSourceProperty("prepStmtCacheSize", "250");
       config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
       config.setAutoCommit(true);
       
       datasource = new HikariDataSource(config);

	   return datasource;	
   }  
   
   @Bean
   public RestTemplate getRestTemplate() 
   {
      return new RestTemplate();
   }
}

