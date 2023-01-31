package com.hdsoft.utils;

import java.sql.Timestamp;
import java.util.Map;

public class AuditLog
{
	private String domainId;
	private String userId;
	private Timestamp dateTime;
	private long logSl;
	private String formName;
	private String userIp;
	private String userAction;
	private String tableName;
	private String tablePK;
	private String imageType;
	private Map dataMap;
	private int breakSl;
	private String tableData;
	private Map dataMaponModify;
	private String girdData;
	private String gridDataonModify;

	public String getGirdData() {
		return girdData;
	}

	public void setGirdData(String girdData) {
		this.girdData = girdData;
	}

	
	public String getGridDataonModify() {
		return gridDataonModify;
	}

	
	public void setGridDataonModify(String gridDataonModify) {
		this.gridDataonModify = gridDataonModify;
	}

	
	public String getTableData() {
		return tableData;
	}

	
	public void setTableData(String tableData) {
		this.tableData = tableData;
	}

	
	public int getBreakSl() {
		return breakSl;
	}

	
	public void setBreakSl(int breakSl) {
		this.breakSl = breakSl;
	}

	
	public String getDomainId() {
		return domainId;
	}

	
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	
	public String getUserId() {
		return userId;
	}

	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public Timestamp getDateTime() {
		return dateTime;
	}

	
	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	
	public long getLogSl() {
		return logSl;
	}

	
	public void setLogSl(long logSl) {
		this.logSl = logSl;
	}

	
	public String getFormName() {
		return formName;
	}

	
	public void setFormName(String formName) {
		this.formName = formName;
	}

	
	public String getUserIp() {
		return userIp;
	}

	
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	
	public String getUserAction() {
		return userAction;
	}

	
	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}

	
	public String getTableName() {
		return tableName;
	}

	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	
	public String getTablePK() {
		return tablePK;
	}

	
	public void setTablePK(String tablePK) {
		this.tablePK = tablePK;
	}

	
	public String getImageType() {
		return imageType;
	}

	
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	
	public Map getDataMap() {
		return dataMap;
	}

	
	public void setDataMap(Map dataMap) {
		this.dataMap = dataMap;
	}
	
	public Map getDataMaponModify() {
		return dataMaponModify;
	}
	
	public void setDataMaponModify(Map dataMaponModify) {
		this.dataMaponModify = dataMaponModify;
	}
}
