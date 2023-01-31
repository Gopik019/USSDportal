package com.hdsoft.hdpay.Repositories;

import java.sql.Blob;

public class Dynamic_Menu 
{
	private String MENU_FOR;
	private String USERID_ROLE;
	private String SYSCODE;
	private String MENU_HEADER;
	private String MENU_DESCN;
	private String MENU_PARENTMENU;
	private String MENU_ORDER;
	private String MENU_SUBORDER;
	private String MENU_PARENT_FORM;
	private String MENU_FORMPATH;
	private Blob MENU_LOGO;
	private String MENU_ICON;
	private String MENU_PARENT_HEADER;
	private String MENU_LOCATION;
	private String MENU_STATUS;
	
	public String getMENU_FOR() {
		return MENU_FOR;
	}
	public void setMENU_FOR(String mENU_FOR) {
		MENU_FOR = mENU_FOR;
	}
	public String getUSERID_ROLE() {
		return USERID_ROLE;
	}
	public String getMENU_ICON() {
		return MENU_ICON;
	}
	public void setMENU_ICON(String mENU_ICON) {
		MENU_ICON = mENU_ICON;
	}
	public void setUSERID_ROLE(String uSERID_ROLE) {
		USERID_ROLE = uSERID_ROLE;
	}
	public String getSYSCODE() {
		return SYSCODE;
	}
	public void setSYSCODE(String sYSCODE) {
		SYSCODE = sYSCODE;
	}
	public String getMENU_HEADER() {
		return MENU_HEADER;
	}
	public void setMENU_HEADER(String mENU_HEADER) {
		MENU_HEADER = mENU_HEADER;
	}
	public String getMENU_DESCN() {
		return MENU_DESCN;
	}
	public void setMENU_DESCN(String mENU_DESCN) {
		MENU_DESCN = mENU_DESCN;
	}
	public String getMENU_PARENTMENU() {
		return MENU_PARENTMENU;
	}
	public void setMENU_PARENTMENU(String mENU_PARENTMENU) {
		MENU_PARENTMENU = mENU_PARENTMENU;
	}
	public String getMENU_ORDER() {
		return MENU_ORDER;
	}
	public void setMENU_ORDER(String mENU_ORDER) {
		MENU_ORDER = mENU_ORDER;
	}
	public String getMENU_SUBORDER() {
		return MENU_SUBORDER;
	}
	public void setMENU_SUBORDER(String mENU_SUBORDER) {
		MENU_SUBORDER = mENU_SUBORDER;
	}
	public String getMENU_PARENT_FORM() {
		return MENU_PARENT_FORM;
	}
	public void setMENU_PARENT_FORM(String mENU_PARENT_FORM) {
		MENU_PARENT_FORM = mENU_PARENT_FORM;
	}
	public String getMENU_FORMPATH() {
		return MENU_FORMPATH;
	}
	public void setMENU_FORMPATH(String mENU_FORMPATH) {
		MENU_FORMPATH = mENU_FORMPATH;
	}
	public Blob getMENU_LOGO() {
		return MENU_LOGO;
	}
	public void setMENU_LOGO(Blob mENU_LOGO) {
		MENU_LOGO = mENU_LOGO;
	}
	public String getMENU_PARENT_HEADER() {
		return MENU_PARENT_HEADER;
	}
	public void setMENU_PARENT_HEADER(String mENU_PARENT_HEADER) {
		MENU_PARENT_HEADER = mENU_PARENT_HEADER;
	}
	public String getMENU_LOCATION() {
		return MENU_LOCATION;
	}
	public void setMENU_LOCATION(String mENU_LOCATION) {
		MENU_LOCATION = mENU_LOCATION;
	}
	public String getMENU_STATUS() {
		return MENU_STATUS;
	}
	public void setMENU_STATUS(String mENU_STATUS) {
		MENU_STATUS = mENU_STATUS;
	}
}
