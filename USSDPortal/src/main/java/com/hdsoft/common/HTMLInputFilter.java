package com.hdsoft.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLInputFilter
{	
	
	protected static final boolean ALWAYS_MAKE_TAGS = true;
	
	
	protected static final boolean STRIP_COMMENTS = true;
	
	
	protected static final int REGEX_FLAGS_SI = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;
	
	
	protected Map vAllowed;
	
	
	protected Map vTagCounts;
	
	
	protected String[] vSelfClosingTags;
	
	
	protected String[] vNeedClosingTags;
	
	
	protected String[] vProtocolAtts;
	
	
	protected String[] vAllowedProtocols;
	
	
	protected String[] vRemoveBlanks;
	
	
	protected String[] vAllowedEntities;
	
	protected boolean vDebug;
	
	public HTMLInputFilter()
	{
		this(false);
	}
	
	public HTMLInputFilter( boolean debug )
	{
		vDebug = debug;
		
		vAllowed = new HashMap();
		vTagCounts = new HashMap();
		
		ArrayList a_atts = new ArrayList();
		a_atts.add( "href" );
		a_atts.add( "target" );
		vAllowed.put( "a", a_atts );
		
		ArrayList img_atts = new ArrayList();
		img_atts.add( "src" );
		img_atts.add( "width" );
		img_atts.add( "height" );
		img_atts.add( "alt" );
		vAllowed.put( "img", img_atts );
		
		ArrayList no_atts = new ArrayList();
		vAllowed.put( "b", no_atts );
		vAllowed.put( "strong", no_atts );
		vAllowed.put( "i", no_atts );
		vAllowed.put( "em", no_atts );
		
		vSelfClosingTags = new String[] { "img" };
		vNeedClosingTags = new String[] { "a", "b", "strong", "i", "em" };
		vAllowedProtocols = new String[] { "http", "mailto" }; // no ftp.
		vProtocolAtts = new String[] { "src", "href" };
		vRemoveBlanks = new String[] { "a", "b", "strong", "i", "em" };
		vAllowedEntities = new String[] { "amp", "gt", "lt", "quot" };
	}
	
	protected void reset()
	{
		vTagCounts = new HashMap();
	}
	
	protected void debug( String msg )
	{
		if (vDebug)
			System.out.println( msg );
	}
	
	
	public static String chr( int decimal )
	{
		return String.valueOf( (char) decimal );
	}
	
	public static String htmlSpecialChars( String s )
	{
		s = s.replaceAll( "&", "&amp;" );
		s = s.replaceAll( "\"", "&quot;" );
		s = s.replaceAll( "<", "&lt;" );
		s = s.replaceAll( ">", "&gt;" );
		return s;
	}
	
	
	
	public synchronized String filter( String input )
	{
		reset();
		String s = input;
		if(s == null)
			return null;
		debug( "************************************************" );
		debug( "              INPUT: " + input );
		
		s = escapeComments(s);
		debug( "     escapeComments: " + s );
		
		s = balanceHTML(s);
		debug( "        balanceHTML: " + s );
		
		s = checkTags(s);
		debug( "          checkTags: " + s );
		
		s = processRemoveBlanks(s);
		debug( "processRemoveBlanks: " + s );
		
		s = validateEntities(s);
		debug( "    validateEntites: " + s );
		
		debug( "************************************************\n\n" );
		return s;
	}
	
	protected String escapeComments( String s )
	{
		Pattern p = Pattern.compile( "<!--(.*?)-->", Pattern.DOTALL );
		Matcher m = p.matcher( s );
		StringBuffer buf = new StringBuffer();
		if (m.find()) {
			String match = m.group( 1 ); //(.*?)
			m.appendReplacement( buf, "<!--" + htmlSpecialChars( match ) + "-->" );
		}
		m.appendTail( buf );
		
		return buf.toString();
	}
	
	protected String balanceHTML( String s )
	{
		if (ALWAYS_MAKE_TAGS) 
		{
			s = regexReplace("^>", "", s);
			s = regexReplace("<([^>]*?)(?=<|$)", "<$1>", s);
			s = regexReplace("(^|>)([^<]*?)(?=>)", "$1<$2", s);
			
		} 
		else
		{
			s = regexReplace("<([^>]*?)(?=<|$)", "&lt;$1", s);
			s = regexReplace("(^|>)([^<]*?)(?=>)", "$1$2&gt;<", s);
			
			s = s.replaceAll("<>", "");
		}
		
		return s;
	}
	
	protected String checkTags( String s )
	{		
		Pattern p = Pattern.compile( "<(.*?)>", Pattern.DOTALL );
		Matcher m = p.matcher( s );
		
		StringBuffer buf = new StringBuffer();
		while (m.find()) {
			String replaceStr = m.group( 1 );
			replaceStr = processTag( replaceStr );
			m.appendReplacement(buf, replaceStr);
		}
		m.appendTail(buf);
		
		s = buf.toString();
		
		 for(Iterator iterator = vTagCounts.keySet().iterator(); iterator.hasNext();)
		{
			 String key = (String)iterator.next();
			 for(int ii = 0; ii < ((Integer)vTagCounts.get(key)).intValue(); ii++)
			 {
				s += "</" + key + ">";
			}
		}
		
		return s;
	}
	
	protected String processRemoveBlanks( String s )
	{
		String as[];
        int j = (as = vRemoveBlanks).length;
        for(int i = 0; i < j; i++)
		{
        	String tag = as[i];
        	s = regexReplace( "<" + tag + "(\\s[^>]*)?></" + tag + ">", "", s );
			s = regexReplace( "<" + tag + "(\\s[^>]*)?/>", "", s );
		}
		
		return s;
	}
	
	protected String regexReplace( String regex_pattern, String replacement, String s )
	{
		Pattern p = Pattern.compile( regex_pattern );
		Matcher m = p.matcher( s );
		return m.replaceAll( replacement );
	}
	
	protected String processTag( String s )
	{		
		Pattern p = Pattern.compile( "^/([a-z0-9]+)", REGEX_FLAGS_SI );
		Matcher m = p.matcher( s );
		if (m.find()) {
			String name = m.group(1).toLowerCase();
			if (vAllowed.containsKey( name )) {
				if (!inArray(name, vSelfClosingTags)) {
					if (vTagCounts.containsKey( name )) {
						vTagCounts.put(name, Integer.valueOf("" + (((Integer)vTagCounts.get(name)).intValue() - 1)));
						return "</" + name + ">";
					}
				}
			}
		}
		
		p = Pattern.compile("^([a-z0-9]+)(.*?)(/?)$", REGEX_FLAGS_SI);
		m = p.matcher( s );
		if (m.find()) {
			String name = m.group(1).toLowerCase();
			String body = m.group(2);
			String ending = m.group(3);
			
			if (vAllowed.containsKey( name )) {
				String params = "";
				
				Pattern p2 = Pattern.compile("([a-z0-9]+)=([\"'])(.*?)\\2", REGEX_FLAGS_SI);
				Pattern p3 = Pattern.compile("([a-z0-9]+)(=)([^\"\\s']+)", REGEX_FLAGS_SI);
				Matcher m2 = p2.matcher( body );
				Matcher m3 = p3.matcher( body );
				List paramNames = new ArrayList();
				List paramValues = new ArrayList();
				while (m2.find()) {
					paramNames.add(m2.group(1)); //([a-z0-9]+)
					paramValues.add(m2.group(3)); //(.*?)
				}
				while (m3.find()) {
					paramNames.add(m3.group(1)); //([a-z0-9]+)
					paramValues.add(m3.group(3)); //([^\"\\s']+)
				}
				
				String paramName, paramValue;
				for( int ii=0; ii<paramNames.size(); ii++ ) {
					  paramName = ((String)paramNames.get(ii)).toLowerCase();
	                  paramValue = (String)paramValues.get(ii);
					
					
	                  
	                  if(((List)vAllowed.get(name)).contains(paramName)) {
						if (inArray( paramName, vProtocolAtts )) {
							paramValue = processParamProtocol( paramValue );
						}
						params += " " + paramName + "=\"" + paramValue + "\"";
	                  }
				}
				
				if (inArray( name, vSelfClosingTags )) {
					ending = " /";
				}
				
				if (inArray( name, vNeedClosingTags )) {
					ending = "";
				}
				
				if (ending == null || ending.length() < 1) {
					if (vTagCounts.containsKey( name )) {
						 vTagCounts.put(name, Integer.valueOf("" + ((Integer)vTagCounts.get(name)).intValue() + 1));
					} else {
						 vTagCounts.put(name, Integer.valueOf("1"));
					}
				} else {
					ending = " /";
				}
				return "<" + name + params + ending + ">";
			} else {
				return "";
			}
		}
		
		p = Pattern.compile( "^!--(.*)--$", REGEX_FLAGS_SI );
		m = p.matcher( s );
		if (m.find()) {
			String comment = m.group();
			if (STRIP_COMMENTS) {
				return "";
			} else {
				return "<" + comment + ">"; 
			}
		}
		
		return "";
	}
	
	protected String processParamProtocol( String s )
	{
		s = decodeEntities( s );
		Pattern p = Pattern.compile( "^([^:]+):", REGEX_FLAGS_SI );
		Matcher m = p.matcher( s );
		if (m.find()) {
			String protocol = m.group(1);
			if (!inArray( protocol, vAllowedProtocols )) {
				s = "#" + s.substring( protocol.length()+1, s.length() );
				if (s.startsWith("#//")) s = "#" + s.substring( 3, s.length() );
			}
		}
		
		return s;
	}
	
	protected String decodeEntities( String s )
	{
		StringBuffer buf = new StringBuffer();
		
		Pattern p = Pattern.compile( "&#(\\d+);?" );
		Matcher m = p.matcher( s );
		while (m.find()) {
			String match = m.group( 1 );
			int decimal = Integer.decode( match ).intValue();
			m.appendReplacement( buf, chr( decimal ) );
		}
		m.appendTail( buf );
		s = buf.toString();
		
		buf = new StringBuffer();
		p = Pattern.compile( "&#x([0-9a-f]+);?");
		m = p.matcher( s );
		while (m.find()) {
			String match = m.group( 1 );
			int decimal = Integer.decode( match ).intValue();
			m.appendReplacement( buf, chr( decimal ) );
		}
		m.appendTail( buf );
		s = buf.toString();
		
		buf = new StringBuffer();
		p = Pattern.compile( "%([0-9a-f]{2});?");
		m = p.matcher( s );
		while (m.find()) {
			String match = m.group( 1 );
			int decimal = Integer.decode( match ).intValue();
			m.appendReplacement( buf, chr( decimal ) );
		}
		m.appendTail( buf );
		s = buf.toString();
		
		s = validateEntities( s );
		return s;
	}
	
	protected String validateEntities( String s )
	{
		Pattern p = Pattern.compile( "&([^&;]*)(?=(;|&|$))" );
		Matcher m = p.matcher( s );
		if (m.find()) {
			String one = m.group( 1 ); //([^&;]*) 
			String two = m.group( 2 ); //(?=(;|&|$))
			s = checkEntity( one, two );
		}
		
		p = Pattern.compile( "(>|^)([^<]+?)(<|$)", Pattern.DOTALL );
		m = p.matcher( s );
		StringBuffer buf = new StringBuffer();
		if (m.find()) {
			String one = m.group( 1 ); //(>|^) 
			String two = m.group( 2 ); //([^<]+?) 
			String three = m.group( 3 ); //(<|$) 
			m.appendReplacement( buf, one + two.replaceAll( "\"", "&quot;" ) + three);
		}
		m.appendTail( buf );
		
		return s;
	}
	
	protected String checkEntity( String preamble, String term )
	{
		if (!term.equals(";")) {
			return "&amp;" + preamble;
		}
		
		if ( isValidEntity( preamble ) ) {
			return "&" + preamble;
		}
		
		return "&amp;" + preamble;
	}
	
	protected boolean isValidEntity( String entity )
	{
		return inArray( entity, vAllowedEntities );
	}
	
	private boolean inArray( String s, String[] array )
	{
		String as[];
        int j = (as = array).length;
        for(int i = 0; i < j; i++)
        {
            String item = as[i];
            if(item != null && item.equals(s))
                return true;
        }

        return false;
		
	}
	
}
