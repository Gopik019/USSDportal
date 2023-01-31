package com.hdsoft.utils;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class WebContext implements Serializable {

	private static final long serialVersionUID = -2828521186784559092L;

	public abstract HttpServletRequest getRequest();

	public abstract HttpServletResponse getResponse();

	public abstract HttpSession getSession();
    
	public abstract HttpSession getSession(boolean create);
	
	public abstract boolean isFormPosted();
	
	
	private static final ThreadLocal<WebContext> context = new ThreadLocal<WebContext>();

	public static WebContext getInstance() {
		WebContext c = context.get();
		return c;
	}

	protected static void setInstance(WebContext c) {
		context.set(c);
	}
}
