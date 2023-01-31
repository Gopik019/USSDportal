package com.hdsoft.hdpay.Repositories;

public class API_Store 
{
	private String URL_End_Point;
	private String Service_Id;
	private String API_Type;
	private String Method;
	private String Format;
	private String User_Name;
	private String Password;
	private String Headers;
	private String[] Keys;
	private String[] Values;
	private String Payload;
	
	public String getURL_End_Point() {
		return URL_End_Point;
	}
	public void setURL_End_Point(String uRL_End_Point) {
		URL_End_Point = uRL_End_Point;
	}
	public String getService_Id() {
		return Service_Id;
	}
	public void setService_Id(String service_Id) {
		Service_Id = service_Id;
	}
	public String getAPI_Type() {
		return API_Type;
	}
	public void setAPI_Type(String aPI_Type) {
		API_Type = aPI_Type;
	}
	public String getMethod() {
		return Method;
	}
	public void setMethod(String method) {
		Method = method;
	}
	public String getFormat() {
		return Format;
	}
	public void setFormat(String format) {
		Format = format;
	}
	public String getUser_Name() {
		return User_Name;
	}
	public void setUser_Name(String user_Name) {
		User_Name = user_Name;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getHeaders() {
		return Headers;
	}
	public void setHeaders(String headers) {
		Headers = headers;
	}
	public String[] getKeys() {
		return Keys;
	}
	public void setKeys(String[] keys) {
		Keys = keys;
	}
	public String[] getValues() {
		return Values;
	}
	public void setValues(String[] values) {
		Values = values;
	}
	public String getPayload() {
		return Payload;
	}
	public void setPayload(String payload) {
		Payload = payload;
	}
}
