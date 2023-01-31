package com.hdsoft.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hdsoft.hdpay.Repositories.Dynamic_Menu;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Menu_Generation 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	public JsonObject Get_Menus(String SYSCODE, String USER_ROLE, HttpServletRequest request) 
	{
		JsonObject details = new JsonObject();
		
		try
		{
			 String sql = "select * from Menu005 w where w.USERID_ROLE=? and w.MENU_STATUS=? and w.SYSCODE=? order by w.MENU_PARENTMENU desc,w.MENU_ORDER asc,w.MENU_SUBORDER asc";
			
			 List<Dynamic_Menu> obj = Jdbctemplate.query(sql, new Object[] { USER_ROLE, "1", SYSCODE }, new Menu_Mapper());
			
			 JsonArray Parent_Menus = new JsonArray();
			 
			 for(int i=0;i<obj.size();i++)
 			 {
				 String PARENTMENU = obj.get(i).getMENU_PARENTMENU();
				
				 if(PARENTMENU != null && PARENTMENU.equals("1"))
				 {
					 JsonObject Parent = new JsonObject();
					 
					 Parent.addProperty("MENU_HEADER", obj.get(i).getMENU_HEADER());
					 Parent.addProperty("MENU_DESCN", obj.get(i).getMENU_DESCN());
					 Parent.addProperty("NAVIGATION_ID", obj.get(i).getMENU_PARENT_FORM());
					 Parent.addProperty("MENU_LOGO", obj.get(i).getMENU_ICON());
					 
					 Parent_Menus.add(Parent);
				 }
 			 }
			
			 String Context_path = request.getContextPath();
				
			 for(int k=0;k<Parent_Menus.size();k++)
 			 {
				 JsonObject Parent = Parent_Menus.get(k).getAsJsonObject();
		
				 String Parent_Header = Parent.get("MENU_HEADER").getAsString();
				
				 JsonArray Child_Menus = new JsonArray();

				 for(int i=0;i<obj.size();i++)
	 			 {
					 String MENU_PARENT_HEADER = obj.get(i).getMENU_PARENT_HEADER();
					 
					 if(Parent_Header.equals(MENU_PARENT_HEADER))
					 {
						 String NAVIGATION_LINK = obj.get(i).getMENU_FORMPATH().equals("#") ? obj.get(i).getMENU_FORMPATH() : Context_path + obj.get(i).getMENU_FORMPATH();
						 
						 JsonObject Child = new JsonObject();
						 
						 Child.addProperty("MENU_HEADER", obj.get(i).getMENU_HEADER());
						 Child.addProperty("MENU_DESCN", obj.get(i).getMENU_DESCN());
						 Child.addProperty("NAVIGATION_LINK", NAVIGATION_LINK);
						 Child.addProperty("MENU_LOGO", obj.get(i).getMENU_ICON());
						 
						 Child_Menus.add(Child);
					 } 
	 			 }
				
				 details.add(Parent_Header, Child_Menus);
 			 }
			 			
			 details.add("Parent_Menus", Parent_Menus);

			 details.addProperty("Result", obj.size() != 0 ? "Success" : "Failed");
			 details.addProperty("Message", obj.size() != 0 ? "Menu Configuration Found" : "Menu Configuration Not Found");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
		 }
		
		 return details;
	}
	
	public JsonObject Get_Menus_HTML(HttpSession session,String PARENT_ID, String CHILD_ID) 
	{
		JsonObject details = new JsonObject();
			
		try
		{
			String Menu_info = (String)session.getAttribute("Menu_details");
			
			JsonObject Menu_details = new Common_Utils().StringToJsonObject(Menu_info);
			
			JsonArray Parent_Menus = Menu_details.get("Parent_Menus").getAsJsonArray();
			
			String Final_Menu = "<ul class=\"nav nav-primary\">";  String Title = "";
			
			for(int k=0;k<Parent_Menus.size();k++)
 			{
				 JsonObject Parent = Parent_Menus.get(k).getAsJsonObject();
				 
				 String Parent_Header_Id = Parent.get("MENU_HEADER").getAsString();
				 String Parent_Desc = Parent.get("MENU_DESCN").getAsString();
				 String Navigation_Id = Parent.get("NAVIGATION_ID").getAsString();
				 String Menu_icon = Parent.get("MENU_LOGO").getAsString();
				 
				 String Content = Main_Menu_Content(Parent_Header_Id, Parent_Desc, Navigation_Id, Menu_icon, PARENT_ID);
				 
				 JsonArray Child_Menus = Menu_details.get(Parent_Header_Id).getAsJsonArray();
				 
				 String Child_Content = ""; 
				  
				 for(int i=0;i<Child_Menus.size();i++)
		 		 {
					 JsonObject child = Child_Menus.get(i).getAsJsonObject();
					 
					 String Child_Header_Id = child.get("MENU_HEADER").getAsString();
					 String Child_Desc = child.get("MENU_DESCN").getAsString();
					 String Nav_Link = child.get("NAVIGATION_LINK").getAsString();
					 String Child_Icon = child.get("MENU_LOGO").getAsString();
					
					 Child_Content += Child_Menu_Content(Child_Header_Id, Child_Desc, Nav_Link, Child_Icon, CHILD_ID);
					 
					 if(Child_Header_Id.equals(CHILD_ID))  Title = Child_Desc;
		 		 }
				 
				 Child_Content += "</ul></div></li>"; 
				 
				 Final_Menu = Final_Menu + Content + Child_Content;				 
 			}
			
			Final_Menu += "</ul>";
			
			details.addProperty("Menu_Content", Final_Menu);
			
			details.addProperty("Title", Title.toUpperCase());
			
			details.addProperty("logo_header_color", "dark2"); /***** dark2, purple2 , green2, orange, white***/
			details.addProperty("body_color", "bg3");
			details.addProperty("nav_color", "white");
			details.addProperty("sidebar_color", "dark2");		/***** dark2, purple2, green2, orange, white ***/

			details.addProperty("Result", Parent_Menus.size() !=0  ? "Success" : "Failed");
			details.addProperty("Message", Parent_Menus.size() !=0  ? "Menu Created Successfully" : "Menu not Created !!");
		}
		catch(Exception e)
		{
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getLocalizedMessage()); 
		}
		
		return details;
	} 
	
	public String Main_Menu_Content(String Header_Id, String Name, String Nav_Id, String Icon, String ACTIVE_PARENT)
	{
		String Active_Class = ACTIVE_PARENT.equals(Header_Id) ? "nav-item active submenu" : "nav-item";
		
		String Active_sub_Class = ACTIVE_PARENT.equals(Header_Id) ? "collapse show" : "collapse";
		
		return (
					"<li class=\""+Active_Class+"\">"+
						"<a data-toggle=\"collapse\" href=\"#"+Nav_Id+"\" class=\"collapsed\" aria-expanded=\"false\">"+
							"<i class=\""+Icon+"\"></i>"+
								"<p>"+Name+"</p>"+
								"<span class=\"caret\"></span>"+
							"</a>" +
						"<div class=\""+Active_sub_Class+"\" id=\""+Nav_Id+"\">"+
							"<ul class=\"nav nav-collapse\">"				
			   );
	}
	
	public String Child_Menu_Content(String Header_Id, String Name, String Nav_link, String Icon, String ACTIVE_CHILD)
	{
		String Active_Class = ACTIVE_CHILD.equals(Header_Id) ? "active" : "";
		
		return (
				 "<li class=\""+Active_Class+"\">"+ 
					 "<a href=\""+Nav_link+"\">"+
					   "<span class=\"sub-item\">"+Name+"</span>"+
				     "</a>"+
				  "</li>"				
			   );
	}
	
	public class Menu_Mapper implements RowMapper<Dynamic_Menu> 
    {
    	Common_Utils util = new Common_Utils();
    	
		public Dynamic_Menu mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Dynamic_Menu API = new Dynamic_Menu();  

			API.setMENU_FOR(rs.getString("MENU_FOR"));
			API.setUSERID_ROLE(rs.getString("USERID_ROLE"));
			API.setSYSCODE(rs.getString("SYSCODE"));
			API.setMENU_HEADER(rs.getString("MENU_HEADER"));
			API.setMENU_DESCN(rs.getString("MENU_DESCN"));
			API.setMENU_PARENTMENU(rs.getString("MENU_PARENTMENU"));
			API.setMENU_ORDER(rs.getString("MENU_ORDER"));
			API.setMENU_SUBORDER(rs.getString("MENU_SUBORDER"));
			API.setMENU_PARENT_FORM(rs.getString("MENU_PARENT_FORM"));
			API.setMENU_FORMPATH(rs.getString("MENU_FORMPATH"));  
			API.setMENU_PARENT_HEADER(rs.getString("MENU_PARENT_HEADER"));
			API.setMENU_LOCATION(rs.getString("MENU_LOCATION"));
			API.setMENU_STATUS(rs.getString("MENU_STATUS"));
			API.setMENU_ICON(util.Blob_to_string(rs.getBlob("MENU_LOGO")));
	
			return API;
		}
    }
}
