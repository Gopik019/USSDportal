package com.hdsoft.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.hdsoft.common.DTDObject;
import com.hdsoft.common.Transpoter;
import com.zaxxer.hikari.HikariDataSource;

public class PreparedQueryManager
{
	private static JdbcTemplate Jdbctemplate;
	private static NamedParameterJdbcTemplate jdbcTemplate;
	
	private static HikariDataSource dataSource;
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultSet = null;
	private String errorMessage;
	private boolean rowPresent = false;

	public boolean openConnection() 
	{
		if (connection != null)
		{
			return true;
		}
		
		try 
		{
			//dataSource = ServiceLocator.getInstance().getDataSource();
			connection = dataSource.getConnection();
		}
		catch (SQLException e) 
		{
			errorMessage = e.getLocalizedMessage();
			return false;
		} 
		catch (Exception e) 
		{
			errorMessage = e.getLocalizedMessage();
			return false;
		}
		
		return true;
	}

	public static JdbcTemplate getJdbctemplate() {
		return Jdbctemplate;
	}

	public static void setJdbctemplate(JdbcTemplate jdbctemplate) {
		Jdbctemplate = jdbctemplate;
	}

	public static NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public static HikariDataSource getDataSource() {
		return dataSource;
	}

	public static void setDataSource(HikariDataSource dataSource) {
		PreparedQueryManager.dataSource = dataSource;
	}

	public static void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		PreparedQueryManager.jdbcTemplate = jdbcTemplate;
	}

	public boolean closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		}
		return true;
	}

	public boolean closeConnection(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		}
		return true;
	}

	public boolean prepareStatement(String sqlQuery) {
		try {
			statement = connection.prepareStatement(sqlQuery);
		} catch (SQLException e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		}
		return true;
	}

	public boolean closeStatement() {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		}
		return true;
	}

	
	public PreparedStatement getStatement() {
		return statement;
	}

	
	public String getErrorMessage() {
		return errorMessage;
	}

	
	public Connection getConnection() {
		return connection;
	}

	
	public boolean executeQuery() {
		rowPresent = false;
		resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		}
		return true;
	}

	public boolean executeAndCheckForRowPresent() {
		rowPresent = false;
		resultSet = null;
		try {
			resultSet = statement.executeQuery();
			if(resultSet.next()){
				rowPresent = true;
				return true;
			}else{
				rowPresent = false;
				return false;
			}
		} catch (SQLException e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
			return false;
		}
	}
	
	
	public ResultSet getResultSet() {
		return resultSet;
	}

	
	public boolean isRowPresent() {
		return rowPresent;
	}

	
	public String getHelpXML() {
		StringBuffer buffer = new StringBuffer(200);
		int colCount;
		rowPresent = false;
		try {
			colCount = resultSet.getMetaData().getColumnCount();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			buffer.append("<Data>");
			if (resultSet.next()) {
				rowPresent = true;
				do {
					buffer.append("<Record>");
					for (int i = 2; i < colCount + 1; i++) {
						if (resultSet.getMetaData().getColumnTypeName(i)
								.equals("DATE")) {
							if (resultSet.getDate(i) == null) {
								buffer.append("<Col" + (i - 2) + ">"
										+ resultSet.getString(i) + "</Col"
										+ (i - 2) + ">");
							} else {
								buffer.append("<Col" + (i - 2) + ">"
										+ sdf.format(resultSet.getDate(i))
										+ "</Col" + (i - 2) + ">");
							}
						} else {
							buffer.append("<Col" + (i - 2) + ">"
									+ resultSet.getString(i) + "</Col"
									+ (i - 2) + ">");
						}
					}
					buffer.append("</Record>");
				} while (resultSet.next());
			}

		} catch (SQLException e) {
			errorMessage = e.getLocalizedMessage();
			return "";
		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
			return "";
		}
		buffer.append("</Data>");
		return buffer.toString().replaceAll("&", "&amp;");
	}

	
	public String getXML() throws SQLException {
		StringBuffer buffer = new StringBuffer(200);
		int colCount;
		colCount = resultSet.getMetaData().getColumnCount();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		buffer.append("<rows>");
		int k = 1;
		if(rowPresent)	{
			do {
				buffer.append("<row id='" + k + "'><cell>" + k + "</cell>");
				k++;
				for (int i = 1; i < colCount + 1; i++) {
					if (resultSet.getMetaData().getColumnTypeName(i).equals(
							"DATE")) {
						if (resultSet.getDate(i) == null) {
							buffer.append("<cell>" + resultSet.getString(i)
									+ "</cell>");
						} else {
							buffer.append("<cell>"
									+ sdf.format(resultSet.getDate(i))
									+ "</cell>");
						}
					} else {
						if (resultSet.getString(i) == null) {
							buffer.append("<cell></cell>");
						} else {
							buffer.append("<cell>" + resultSet.getString(i)
									+ "</cell>");
						}
					}
				}
				buffer.append("</row>");
			} while (resultSet.next());
		}else	{
			if (resultSet.next()) {
				rowPresent = true;
				do {
					buffer.append("<row id='" + k + "'><cell>" + k + "</cell>");
					k++;
					for (int i = 1; i < colCount + 1; i++) {
						if (resultSet.getMetaData().getColumnTypeName(i).equals(
								"DATE")) {
							if (resultSet.getDate(i) == null) {
								buffer.append("<cell>" + resultSet.getString(i)
										+ "</cell>");
							} else {
								buffer.append("<cell>"
										+ sdf.format(resultSet.getDate(i))
										+ "</cell>");
							}
						} else {
							if (resultSet.getString(i) == null) {
								buffer.append("<cell></cell>");
							} else {
								buffer.append("<cell>" + resultSet.getString(i)
										+ "</cell>");
							}
						}
					}
					buffer.append("</row>");
				} while (resultSet.next());
			}	
		}
		
		buffer.append("</rows>");
		return buffer.toString().replaceAll("&", "&amp;");
	}
	
	public String getJson() throws SQLException
	{
		StringBuffer buffer = new StringBuffer(200);
		int colCount;
		colCount = resultSet.getMetaData().getColumnCount();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		buffer.append("<rows>");
		
		int k = 1;
		
		if(rowPresent)	
		{
			do 
			{
				buffer.append("<row id='" + k + "'><cell>" + k + "</cell>");
				k++;
				
				for (int i = 1; i < colCount + 1; i++) 
				{
					if (resultSet.getMetaData().getColumnTypeName(i).equals("DATE"))
					{
						if (resultSet.getDate(i) == null) 
						{
							buffer.append("<cell>" + resultSet.getString(i)
									+ "</cell>");
						}
						else 
						{
							buffer.append("<cell>"
									+ sdf.format(resultSet.getDate(i))
									+ "</cell>");
						}
					}
					else 
					{
						if (resultSet.getString(i) == null)
						{
							buffer.append("<cell></cell>");
						} 
						else 
						{
							buffer.append("<cell>" + resultSet.getString(i)
									+ "</cell>");
						}
					}
				}
				
				buffer.append("</row>");
				
			} while (resultSet.next());
		}
		else	
		{
			if (resultSet.next()) 
			{
				rowPresent = true;
				
				do
				{
					buffer.append("<row id='" + k + "'><cell>" + k + "</cell>");
					k++;
					for (int i = 1; i < colCount + 1; i++)
					{
						if (resultSet.getMetaData().getColumnTypeName(i).equals("DATE")) 
						{
							if (resultSet.getDate(i) == null) 
							{
								buffer.append("<cell>" + resultSet.getString(i)
										+ "</cell>");
							} 
							else 
							{
								buffer.append("<cell>"
										+ sdf.format(resultSet.getDate(i))
										+ "</cell>");
							}
						}
						else
						{
							if (resultSet.getString(i) == null) 
							{
								buffer.append("<cell></cell>");
							} 
							else 
							{
								buffer.append("<cell>" + resultSet.getString(i)
										+ "</cell>");
							}
						}
					}
					
					buffer.append("</row>");
					
				} while (resultSet.next());
			}	
		}
		
		buffer.append("</rows>");
		
		return buffer.toString().replaceAll("&", "&amp;");
	}


	
	public Transpoter getTranspoter() 
	{
		rowPresent = false;
		int colCount;
		Transpoter returnDTO = new Transpoter();
		
		try 
		{
			colCount = resultSet.getMetaData().getColumnCount();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			
			if (resultSet.next()) 
			{
				rowPresent = true;
				
				for (int i = 1; i < colCount + 1; i++)
				{
					if (resultSet.getMetaData().getColumnTypeName(i).equals("DATE"))
					{
						if (resultSet.getDate(i) == null)
						{
							returnDTO.setValue(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
						} 
						else 
						{
							returnDTO.setValue(resultSet.getMetaData().getColumnName(i), sdf.format(resultSet.getDate(i)));
						}
					} 
					else 
					{
						returnDTO.setValue(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
					}
				}
			}
			
			resultSet.close();
		}
		catch (SQLException e) 
		{
			errorMessage = e.getLocalizedMessage();
			return null;
		} 
		catch (Exception e)
		{
			errorMessage = e.getLocalizedMessage();
			return null;
		}
		
		return returnDTO;
	}

	
	public DTDObject getDTDObject() {
		rowPresent = false;
		int colCount;
		DTDObject returnDTDO = new DTDObject();
		try {
			colCount = resultSet.getMetaData().getColumnCount();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			int rowNo = 0;
			if (resultSet.next()) {
				rowPresent = true;
				do {
					if (rowNo == 0) {
						for (int i = 0; i < colCount; i++) {
							returnDTDO.addColumn(i, resultSet.getMetaData().getColumnName(i + 1));
						}
					}
					returnDTDO.addRow();
					for (int i = 0; i < colCount; i++) {
						if (resultSet.getMetaData().getColumnTypeName(i + 1).equals("DATE")) {
							if (resultSet.getDate(i + 1) == null) {
								returnDTDO.setValue(rowNo, i, resultSet.getString(i + 1));
							} else {
								returnDTDO.setValue(rowNo, i, sdf.format(resultSet.getDate(i + 1)));
							}
						} else {
							returnDTDO.setValue(rowNo, i, resultSet.getString(i + 1));
						}
					}
					rowNo = rowNo + 1;
				} while (resultSet.next());
			}
			resultSet.close();
		} catch (SQLException e) {
			errorMessage = e.getLocalizedMessage();
			return null;
		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
			return null;
		}
		return returnDTDO;
	}
	
	
	
	public String getXMLReport() throws SQLException {		
		StringBuffer buffer = new StringBuffer(200);
		int colCount;
		colCount = resultSet.getMetaData().getColumnCount();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		buffer.append("<rows>");
		int k = 1;
		if(rowPresent)	{
			do {
				buffer.append("<row id='" + k + "'>");
				k++;
				for (int i = 1; i < colCount + 1; i++) {
					if (resultSet.getMetaData().getColumnTypeName(i).equals("DATE")) {						
						if (resultSet.getDate(i) == null) {
							buffer.append("<"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">" + resultSet.getString(i)
									+ "</"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">");
						} else {
							buffer.append("<"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">"
									+ sdf.format(resultSet.getDate(i))
									+ "</"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">");
						}
					} else {
						if (resultSet.getString(i) == null) {
							buffer.append("<"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+"></"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">");
						} else {
							buffer.append("<"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">" + resultSet.getString(i).trim()
									+ "</"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">");
						}
					}
				}
				buffer.append("</row>");
			} while (resultSet.next());
		}else	{
			if (resultSet.next()) {
				rowPresent = true;
				do {
					buffer.append("<row id='" + k + "'><cell>" + k + "</cell>");
					k++;
					for (int i = 1; i < colCount + 1; i++) {
						if (resultSet.getMetaData().getColumnTypeName(i).equals("DATE")) {
							if (resultSet.getDate(i) == null) {
								buffer.append("<"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">" + resultSet.getString(i)
										+ "</"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">");
							} else {
								buffer.append("<"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">"
										+ sdf.format(resultSet.getDate(i))
										+ "</"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">");
							}
						} else {
							if (resultSet.getString(i) == null) {
								buffer.append("<"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+"></"+resultSet.getMetaData().getColumnLabel(i)+">");
							} else {
								buffer.append("<"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">" + resultSet.getString(i)
										+ "</"+resultSet.getMetaData().getColumnLabel(i).toLowerCase()+">");
							}
						}
					}
					buffer.append("</row>");
				} while (resultSet.next());
			}	
		}
		
		buffer.append("</rows>");
		return buffer.toString().replaceAll("&", "&amp;");
	}	
	

	
}
