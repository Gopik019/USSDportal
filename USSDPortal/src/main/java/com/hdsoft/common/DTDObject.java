package com.hdsoft.common;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class DTDObject extends DefaultHandler implements Serializable {
	private ArrayList arrListColNames = new ArrayList();
	private ArrayList arrListRows = new ArrayList();

	private int currColIndex; 							  
	StringBuffer tagValueBuf = new StringBuffer();
	
	public void addColumn(int ColIndex, String ColName){
		arrListColNames.add(ColIndex, ColName);
	} 
	
	public void addColumn(int ColIndex){
		arrListColNames.add(ColIndex, "Cell"+ColIndex);
	} 
	
	
	
	public void addRow() {
		int NoOfCols = arrListColNames.size();
		String [] Row = new String[NoOfCols];
		arrListRows.add(arrListRows.size(),Row);
	} 
	
	
	public void clearRows() {
		for (int i = 0; i < arrListRows.size() ; i++)
			arrListRows.remove(i);
	} 
	
	
	
	public void clear() {
		for (int i = 0; i < arrListColNames.size() ; i++)
			arrListColNames.remove(i);

		for (int i = 0; i < arrListRows.size() ; i++)
			arrListRows.remove(i);

	} 
	
	
	public String getValue(int RowIndex, int ColIndex){
		String [] arrRow = (String []) arrListRows.get(RowIndex);
		if (arrRow[ColIndex] == null)
			return " ";
		else
		   return arrRow[ColIndex];
	} 

	
	public void setValue(int RowIndex, int ColIndex, String Value){
		String [] arrRow = (String []) arrListRows.get(RowIndex);
		arrRow[ColIndex] = Value;
	} 

	
	public String getValue(int RowIndex, String ColName){
		return getValue(RowIndex, arrListColNames.indexOf(ColName));
	} 

	
	public void setValue(int RowIndex, String ColName, String Value){
		setValue(RowIndex,arrListColNames.indexOf(ColName),Value);
	} 

	
	
	public int getColSize(){
		return arrListColNames.size();
	} 
	
	
	public void setColSize(int colSize){
		for (int i=0;i<colSize;i++){
			arrListColNames.add(i, "Cell"+i);
		}
	} 
	
	
	
	public int getRowSize(){
		return arrListRows.size();
	} 
	
	
	public String getXMLFormat(){
		String xml;
		xml = "<rows>";
		
		for(int i=0; i < getRowCount() ; i++ ){
			xml += "<row id='"+ (i+1) + "'><cell>"+(i+1)+"</cell>";
			
			for(int j=0; j < getColCount(); j++){
				xml +=  "<cell>";
				xml +=  getValue(i,j);
				xml +=  "</cell>";
			}		
			xml += "</row>";
		}		
		xml +=  "</rows>";
		xml = xml.replaceAll("&","&amp;") ;
		return xml;
	} 
	
	
	
	
	
	public String getXML(){
		
		StringBuffer buffer = new StringBuffer(getRowSize() * getColSize());
		String xml;
		buffer.append("<rows>");
		
		for(int i=0; i < getRowSize() ; i++ ){
			
			buffer.append("<row id='");
			buffer.append((i+1));
			buffer.append("'><cell>");
			buffer.append((i+1));
			buffer.append("</cell>");
			
			for(int j=0; j < getColSize(); j++){
				buffer.append("<cell>");
				buffer.append(getValue(i,j));
				buffer.append("</cell>");
			}		
			buffer.append("</row>");
		}		
		buffer.append("</rows>");
		xml  = buffer.toString().replaceAll("&", "&amp;");
		
		return xml;
	} 
	
	public String getReportXML(String TXT){
		System.out.println(TXT);
		String sc[] = TXT.split("\\|");
		System.out.println(sc[0]);
		String xml;
		xml = "<rows>";
		
		for(int i=0; i < getRowSize() ; i++ ){
			xml += "<row id='"+ (i+1) + "'>";
			
			for(int j=0; j < getColSize(); j++){
				xml +=  "<"+sc[j]+">";
				xml +=  getValue(i,j);
				xml +=  "</"+sc[j]+">";
			}		
			xml += "</row>";
		}		
		xml +=  "</rows>";
		xml = xml.replaceAll("&","&amp;") ;
		return xml;
	} 
	
	public void setXML(String XmlString) throws Exception	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		StringReader strreader = new StringReader(XmlString);
		InputSource  insource = new InputSource(strreader);
		
    	saxParser.parse( insource, this );	
		
		
	} 

	
	
	
	public void startElement(String namespaceURI, String sName, 
							 String qName, Attributes attrs) throws SAXException	{
		tagValueBuf.setLength(0);
		if(qName.equals("row")) {
		   addRow();
		   currColIndex=-1;
		} 
		
	} 
 
		
	public void characters(char[] ch, int start, int length) throws SAXException {
		String tagValue = new String(ch,start,length);
		tagValueBuf.append(tagValue) ;
	} 

	public void endElement(String namespaceURI, String sName, 
	String qName) throws SAXException {
			if(qName.indexOf("cell") > -1 ){
				   String tagValue = new String();
				   tagValue = tagValueBuf.toString();	
				   currColIndex = currColIndex + 1;
				   setValue(arrListRows.size()-1, currColIndex,tagValue.trim());
			}
			
	} 

	public void warning(SAXParseException e){
		System.out.println("Warning " + e.getLocalizedMessage());
	}
	
	public void error(SAXParseException e){
		System.out.println("Error " + e.getLocalizedMessage());
	}

	
	public String getRowValue(int rowIndex) {
		StringBuffer	strRowValue = null;
		for (int colIndex = 0; colIndex < getColSize(); colIndex++){
			if (colIndex == 0)
				strRowValue =  new StringBuffer(getValue(rowIndex,colIndex));
			else
				strRowValue.append("|");
			    strRowValue.append(getValue(rowIndex, colIndex));		
		}
		return strRowValue.toString();	
	}
	
	public ArrayList getColNames(){
		return arrListColNames;
	}
	public int getColCount(){
		return arrListColNames.size();
	} 

	public int getRowCount(){
		return arrListRows.size();
	} 
} 

