package com.hdsoft.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Transpoter implements Serializable {

	public static final String INVALID_AUTH = "Invalid Authentication Code";
	public static final String CHECK_BLANK = "CHECK_BLANK";
	public static final String REQUIRED = "REQUIRED";
	public static final String NOT_REQUIRED = "NOT_REQUIRED";
	public static final String CHECK_NUMERIC = "CHECK_NUMERIC";
	public static final String CHECK_LENGTH = "CHECK_LENGTH";
	public static final String MIN_LENGTH = "MIN_LENGTH";
	public static final String MAX_LENGTH = "MAX_LENGTH";
	public static final String FIELD_NAME = "FIELD_NAME";
	public static final String FIELD_VALUE = "FIELD_VALUE";
	public static final String ERROR_CODE = "ERROR_CODE";
	public static final String FORM_ERROR = "FORM_ERROR";
	public static final String FORM_ERROR_PRESENT = "FORM_ERROR_PRESENT";
	public static final String FORM_ERROR_ABSENT = "FORM_ERROR_ABSENT";
	public static final String ROW_STATUS = "ROW_STATUS";
	public static final String ROW_PRESENT = "ROW_PRESENT";
	public static final String ROW_ABSENT = "ROW_ABSENT";
	public static final String FIELD_BLANK = "errors.required";
	public static final String FIELD_INVALID = "errors.invalid";
	public static final String DATE_LESS_THAN_EQUAL_CBD = "errors.dltecbd";
	public static final String DATE_GREATER_THAN_EQUAL_CBD = "errors.dgtecbd";
	public static final String ZERO_CHECK = "errors.zero";
	public static final String CBD = "sesMcontDate";
	public static final String REQUEST_STATUS_NEW = "N";
	public static final String REQUEST_STATUS_REPAIR = "I";
	public static final String ENTRY_MODE_MANUAL = "M";
	private static final long serialVersionUID = -1317531709445243060L;
	public static final String ERROR_KEY = "ERROR_KEY";
	public static final String ERROR_STATUS = "ERROR";
	public static final String ERROR_PRESENT = "ERROR_PRESENT";
	public static final String ERROR_ABSENT = "ERROR_ABSENT";

	public static final String SUCCESS = "S";
	public static final String FAILURE = "F";
	public static final String UPDATE = "U";
	public static final String PROCESSED = "P";
	public static final String ERROR = "E";

	public static final String REQUEST_STATUS_REPAIRED = "D";

	protected HashMap MapObject = new HashMap();
	
	private String error = "";

	public String getValue(String KeyName) {
		return (String) MapObject.get(KeyName);
	} 

	public void setValue(String KeyName, String Value) {
		MapObject.put(KeyName, Value);
	} 

	public DTDObject getDTDObject(String KeyName) {
		return (DTDObject) MapObject.get(KeyName);
	} 

	public void setDTDObject(String KeyName, DTDObject Value) {
		MapObject.put(KeyName, Value);
	} 

	public String getDTDObject(String KeyName, int RowIndex, int ColIndex) {
		DTDObject objDtlInfo = (DTDObject) MapObject.get(KeyName);
		return objDtlInfo.getValue(RowIndex, ColIndex);
	} 

	public void setDTDObject(String KeyName, int RowIndex, int ColIndex,
			String Value) {
		DTDObject objDtlInfo = (DTDObject) MapObject.get(KeyName);
		if (objDtlInfo == null) {
			objDtlInfo = new DTDObject();
			MapObject.put(KeyName, objDtlInfo);
		}
		objDtlInfo.setValue(RowIndex, ColIndex, Value);
	} 

	public String getDTDObject(String KeyName, int RowIndex, String ColName) {
		DTDObject objDtlInfo = (DTDObject) MapObject.get(KeyName);
		return objDtlInfo.getValue(RowIndex, ColName);
	} 

	public void setDTDObject(String KeyName, int RowIndex, String ColName,
			String Value) {
		DTDObject objDtlInfo = (DTDObject) MapObject.get(KeyName);

		if (objDtlInfo == null) {
			objDtlInfo = new DTDObject();
			MapObject.put(KeyName, objDtlInfo);
		}
		objDtlInfo.setValue(RowIndex, ColName, Value);
	} 

	public String getXMLDTDObject(String KeyName) {
		DTDObject objDtlInfo = (DTDObject) MapObject.get(KeyName);
		return objDtlInfo.getXML();
	} 

	public String getReportXMLDTDObject(String KeyName, String TXT) {
		DTDObject objDtlInfo = (DTDObject) MapObject.get(KeyName);

		return objDtlInfo.getReportXML(TXT);
	}

	public void setXMLDTDObject(String KeyName, String XmlString)
	throws Exception {
		DTDObject objDtlInfo = (DTDObject) MapObject.get(KeyName);

		if (objDtlInfo == null) {
			objDtlInfo = new DTDObject();
			MapObject.put(KeyName, objDtlInfo);
		}

		objDtlInfo.setXML(XmlString);
	} 

	public HashMap getHashMap(String KeyName) {
		return (HashMap) MapObject.get(KeyName);
	} 

	public void setHashMap(String KeyName, HashMap Value) {
		MapObject.put(KeyName, Value);
	} 

	public void addColumn(String KeyName, int ColIndex, String ColName) {
		DTDObject objDtlInfo = (DTDObject) MapObject.get(KeyName);

		if (objDtlInfo == null) {
			objDtlInfo = new DTDObject();
			MapObject.put(KeyName, objDtlInfo);
		}

		objDtlInfo.addColumn(ColIndex, ColName);
	} 

	public void setMap(Map formMap) {
		String strKey;
		String strVal;
		Object[] keys = formMap.keySet().toArray();
		String reqID = System.currentTimeMillis() + "";
		int k = keys.length;
		HTMLInputFilter filter = new HTMLInputFilter();
		for (int i = 0; i < k; i++) {
			strKey = (String) keys[i];
			strVal = ((String[]) formMap.get(keys[i]))[0];
			if (strVal.startsWith("<?xml version=\"1.0\"?>")
					|| (strVal.indexOf("<rows>") != -1)) {
			} else {
				try {
					strVal = filter.filter(strVal);
				} catch (Exception e) {
					System.out.println("EXCEPTION in Transpoter class *&*&");
				}
			}
			MapObject.put(strKey, strVal);

		}
	} 

	public Map getMap() {
		return MapObject;
	} 

	public boolean containsKey(String keyName) {
		return MapObject.containsKey(keyName);
	}

	public boolean containsValue(String keyName) {
		return MapObject.containsValue(keyName);
	}

	public void clearMap() {
		MapObject.clear();
	}

	public void removeKey(String keyName) {
		if (MapObject.containsKey(keyName))
			MapObject.remove(keyName);
	}

	public void setObject(String KeyName, Object Value) {
		MapObject.put(KeyName, Value);
	} 

	public Object getObject(String KeyName) {
		return MapObject.get(KeyName);
	} 

	public Set getKeyNames() {
		return MapObject.keySet();
	}

	public void splitStr(String strColumns) {
		String[] keyNameValue = new String[2];
		if (strColumns.indexOf("|") > 0) {
			StringTokenizer st = new StringTokenizer(strColumns, "|");
			while (st.hasMoreTokens()) {
				keyNameValue = st.nextToken().split(":");
				try {
					this.setValue(keyNameValue[0], keyNameValue[1]);
				} catch (Exception e) {
					this.setValue(keyNameValue[0], null);
				}
			}
		} else {
			keyNameValue = strColumns.split(":");
			this.setValue(keyNameValue[0], keyNameValue[1]);
		}
	}

	public String getClientDTO() {
		String keyName;
		StringBuffer buffer = new StringBuffer();
		buffer.append("<Data>");
		buffer.append("<Record>");
		Set keyNames = this.getKeyNames();
		Iterator keyCount = keyNames.iterator();
		while (keyCount.hasNext()) {
			try {
				keyName = keyCount.next().toString();
				buffer.append("<" + keyName + ">");
				if (this.getValue(keyName) != null) {
					if (this.getValue(keyName).toString().indexOf("<Data>") >= 0
							|| this.getValue(keyName).toString().indexOf(
							"<DATA>") >= 0
							|| this.getValue(keyName).toString().indexOf(
							"<rows>") >= 0
							|| this.getValue(keyName).toString().indexOf(
							"<ROWS>") >= 0) {
						buffer.append(this.getValue(keyName).toString()
								.replaceAll("&", "&amp;").replaceAll("<",
								"&lt;").replaceAll(">", "&gt;"));
					} else {
						buffer.append(this.getValue(keyName).toString()
								.replaceAll("&", "&amp;"));
					}
				} else {
					buffer.append("");
				}
				buffer.append("</" + keyName + ">");
			} catch (Exception e) {
				buffer.setLength(0);
				buffer.append("<Data><Record><Result>"
						+ e.getLocalizedMessage() + "</Result>");
			} finally {
			}
		}
		buffer.append("</Record></Data>");
		return buffer.toString();
	}
	
	public String getError() {
		if (error == null)
			return "";
		else
			return error;
	} 

	public void setError(String error) {
		this.error = error;
	}
}
