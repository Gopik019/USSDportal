package com.hdsoft.utils;

import javax.sql.DataSource;

import com.hdsoft.common.DTDObject;
import com.hdsoft.common.Transpoter;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.naming.NamingException;

public class QueryManager 
{
	public QueryManager() 
	{
		super();
	}

	private DataSource dataSource = null;
	private Connection connDB = null;
	private Statement stmt = null;
	private ResultSet rsGeneric = null;
	private String errMsg;
	private boolean isRowPresent = false;

	public String getError()
	{
		return errMsg;
	}

	public boolean openConnection() 
	{
		try
		{
			//dataSource = ServiceLocator.getInstance().getDataSource();
			
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB.createStatement();
		} 
		catch (SQLException e)
		{
			errMsg = e.getLocalizedMessage();
			return false;
		}
		
		return true;
	}

	public boolean openConnection(Connection Con) {
		if (Con == null) {
			openConnection();
		} else {
			connDB = Con;
			try {
				stmt = connDB.createStatement();
			} catch (SQLException e) {
				
				errMsg = e.getLocalizedMessage();
				return false;
			}
		}
		return true;
	}

	
	public boolean closeConnection() {
		try {
			if (rsGeneric != null) {
				rsGeneric.close();
				rsGeneric = null;
			}

			stmt.close();
			stmt = null;
			connDB.close();
			connDB = null;
			dataSource = null;
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return true;
		} catch (Exception e) {
			errMsg = e.getLocalizedMessage();
			return true;
		}
		return true;
	}

	public boolean closeConnection(Connection Con) {
		try {
			if (rsGeneric != null) {
				rsGeneric.close();
				rsGeneric = null;
			}

			stmt.close();
			stmt = null;
			if (Con == null) {
				connDB.close();
				connDB = null;
				dataSource = null;
			}

		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return true;
		} catch (Exception e) {
			errMsg = e.getLocalizedMessage();
			return true;
		}
		return true;
	}

	

	public Connection getConnection() {
		return connDB;
	}

	public boolean executeQuery(String strSQL) {
		rsGeneric = null;
		isRowPresent = false;
		if (connDB == null) {
			openConnection();
		}
		boolean isSelect = strSQL.trim().substring(0, 6).equalsIgnoreCase(
				"SELECT");
		try {
			if (isSelect) {
				rsGeneric = stmt.executeQuery(strSQL);
			} else {
				stmt.executeQuery(strSQL);
			}
		} catch (SQLException e) {
			stmt = null;
			rsGeneric = null;
			errMsg = e.getLocalizedMessage();
			return false;
		}
		return true;
	}

	
	public Transpoter executeQuery(Transpoter inDTO) {
		rsGeneric = null;
		isRowPresent = false;
		Transpoter outDTO = new Transpoter();
		DTDObject outDTDO = null;
		if (connDB == null) {
			openConnection();
		}

		String strSQL = null;
		strSQL = inDTO.getValue("strSQL");
		boolean isSelect = strSQL.trim().substring(0, 6).equalsIgnoreCase(
				"SELECT");
		try {
			if (isSelect) {
				rsGeneric = stmt.executeQuery(strSQL);
				outDTDO = getDTDObject();
				outDTO.setDTDObject("DTDO", outDTDO);
				stmt = null;
				rsGeneric = null;
			} else {
				stmt.executeQuery(strSQL);
			}
			outDTO.setValue("sucFlg", "1");
			connDB.close();
			connDB = null;
			return outDTO;
		} catch (SQLException e) {
			stmt = null;
			rsGeneric = null;
			outDTO.setValue("errMsg", e.getLocalizedMessage());
			outDTO.setValue("sucFlg", "0");
			try {
				if (connDB.isClosed() == false)
					connDB.close();
			} catch (SQLException e1) {
				outDTO.setValue("errMsg", e1.getLocalizedMessage());
			}
			connDB = null;
			return outDTO;
		}
	}

	
	public boolean isRowPresent() {
		return isRowPresent;
	}

	
	public ResultSet getResultSet() {
		return rsGeneric;
	}

	
	public String getHelpXML() {
		int colCount;
		isRowPresent = false;
		String xmlStr = "";
		try {
			colCount = rsGeneric.getMetaData().getColumnCount();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			xmlStr = "<Data>";
			while (rsGeneric.next()) {
				isRowPresent = true;
				xmlStr = xmlStr + "<Record>";
				for (int i = 2; i < colCount + 1; i++) {
					if (rsGeneric.getMetaData().getColumnTypeName(i).equals(
							"DATE")) {
						if (rsGeneric.getDate(i) == null) {
							xmlStr = xmlStr + "<Col" + (i - 2) + ">"
									+ rsGeneric.getString(i) + "</Col"
									+ (i - 2) + ">";
						} else {
							xmlStr = xmlStr + "<Col" + (i - 2) + ">"
									+ sdf.format(rsGeneric.getDate(i))
									+ "</Col" + (i - 2) + ">";
						}
					} else {
						xmlStr = xmlStr + "<Col" + (i - 2) + ">"
								+ rsGeneric.getString(i) + "</Col" + (i - 2)
								+ ">";
					}
				} 
				xmlStr = xmlStr + "</Record>";
			}

		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return "";
		}
		xmlStr = xmlStr + "</Data>";
		xmlStr = xmlStr.replaceAll("&", "&amp;");
		return xmlStr;
	}

	
	public String getXML() {
		int colCount;
		isRowPresent = false;
		String xmlStr = "";
		try {
			colCount = rsGeneric.getMetaData().getColumnCount();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			xmlStr = "<rows>";
			int k = 1;
			while (rsGeneric.next()) {
				isRowPresent = true;

				xmlStr = xmlStr + "<row id='" + k + "'><cell>" + k + "</cell>";
				k++;
				for (int i = 1; i < colCount + 1; i++) {
					if (rsGeneric.getMetaData().getColumnTypeName(i).equals(
							"DATE")) {
						if (rsGeneric.getDate(i) == null) {
							xmlStr = xmlStr + "<cell>" + rsGeneric.getString(i)
									+ "</cell>";
						} else {
							xmlStr = xmlStr + "<cell>"
									+ sdf.format(rsGeneric.getDate(i))
									+ "</cell>";
						}
					} else {
						if (rsGeneric.getString(i) == null) {
							xmlStr = xmlStr + "<cell></cell>";
						} else {
							xmlStr = xmlStr + "<cell>" + rsGeneric.getString(i)
									+ "</cell>";
						}
					}
				} 
				xmlStr = xmlStr + "</row>";
			}

		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return "";
		}
		xmlStr = xmlStr + "</rows>";
		xmlStr = xmlStr.replaceAll("&", "&amp;");
		return xmlStr;
	}

	
	public Transpoter getTranspoter() {
		isRowPresent = false;
		int colCount;
		Transpoter returnDTO = new Transpoter();
		try {
			colCount = rsGeneric.getMetaData().getColumnCount();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			if (rsGeneric.next()) {
				isRowPresent = true;
				for (int i = 1; i < colCount + 1; i++) {
					if (rsGeneric.getMetaData().getColumnTypeName(i).equals(
							"DATE")) {
						if (rsGeneric.getDate(i) == null) {
							returnDTO.setValue(rsGeneric.getMetaData()
									.getColumnName(i), rsGeneric.getString(i));
						} else {
							returnDTO.setValue(rsGeneric.getMetaData()
									.getColumnName(i), sdf.format(rsGeneric
									.getDate(i)));
						}
					} else {
						returnDTO.setValue(rsGeneric.getMetaData()
								.getColumnName(i), rsGeneric.getString(i));
					}
				}
			}
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return null;
		}
		return returnDTO;
	}

	
	public DTDObject getDTDObject() {
		isRowPresent = false;
		int colCount;
		DTDObject returnDTDO = new DTDObject();
		try {
			colCount = rsGeneric.getMetaData().getColumnCount();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			int rowNo = 0;
			while (rsGeneric.next()) {
				isRowPresent = true;

				if (rowNo == 0) {
					for (int i = 0; i < colCount; i++) {
						returnDTDO.addColumn(i, rsGeneric.getMetaData()
								.getColumnName(i + 1));
					}
				}
				returnDTDO.addRow();
				for (int i = 0; i < colCount; i++) {
					if (rsGeneric.getMetaData().getColumnTypeName(i + 1)
							.equals("DATE")) {
						if (rsGeneric.getDate(i + 1) == null) {

							returnDTDO.setValue(rowNo, i, rsGeneric
									.getString(i + 1));
						} else {
							returnDTDO.setValue(rowNo, i, sdf.format(rsGeneric
									.getDate(i + 1)));
						}
					} else {
						returnDTDO.setValue(rowNo, i, rsGeneric
								.getString(i + 1));
					}
				}
				rowNo = rowNo + 1;
			}
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return null;
		}
		return returnDTDO;
	}

}
