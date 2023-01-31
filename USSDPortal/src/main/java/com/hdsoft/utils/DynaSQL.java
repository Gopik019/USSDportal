package com.hdsoft.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.hdsoft.common.DTDObject;
import com.hdsoft.common.Transpoter;

public class DynaSQL 
{
	private CallableStatement stmt = null;
	String errMsg;
	private DataSource dataSource = null;
	private Connection connDB = null;
	private HashMap sqlMap = new HashMap();
	PreparedStatement prepStatement = null;
	String varTableName = null;
	String sqlStr = "";
	String valStr = "";
	boolean IsNew = true;
	String loadKeyFlds = "";

	public DynaSQL(String tableName) {
		setTableName(tableName);
	}

	private void clearFlds() {
		sqlMap.clear();
		sqlStr = "";
		valStr = "";
		loadKeyFlds = "";
		IsNew = true;
	}

	public void setTableName(String tableName) {
		clearFlds();
		varTableName = tableName;
	}

	public String getTableName() {
		return varTableName;
	}

	public void setFiled(String fieldName, Object fieldValue) {
		if (fieldValue == null)
			fieldValue = "";
		sqlMap.put(fieldName, fieldValue);
	}

	public void setFiled(String fieldName, int fieldValue) {
		sqlMap.put(fieldName, String.valueOf(fieldValue));
	}

	public void setFiled(String fieldName, long fieldValue) {
		sqlMap.put(fieldName, String.valueOf(fieldValue));
	}

	public void setFiled(String fieldName, double fieldValue) {
		sqlMap.put(fieldName, String.valueOf(fieldValue));
	}

	public void setFiled(String fieldName, float fieldValue) {
		sqlMap.put(fieldName, String.valueOf(fieldValue));
	}

	public void setFiled(String fieldName, boolean fieldValue) {
		sqlMap.put(fieldName, String.valueOf(fieldValue));
	}

	public void setFiled(String fieldName, char fieldValue) {
		sqlMap.put(fieldName, String.valueOf(fieldValue));
	}

	public Object getFiled(String fieldName) {
		return sqlMap.get(fieldName);
	}

	public long getMaxSlByFields(String maxColumn, String maxByFlds) {
		String[] fieldNamesarr = maxByFlds.split(",");
		long maxSl = 0;
		sqlStr = " select nvl(max(" + maxColumn + "),0)+ 1 from "
				+ varTableName + " where ";
		for (int i = 0; i < fieldNamesarr.length; i++) {
			if (i < fieldNamesarr.length - 1) {
				sqlStr = sqlStr + fieldNamesarr[i] + " = '"
						+ sqlMap.get(fieldNamesarr[i]).toString() + "'  and  ";
			} else {
				sqlStr = sqlStr + fieldNamesarr[i] + " = '"
						+ sqlMap.get(fieldNamesarr[i]).toString() + "' ";
			}
		}
		try {
			Statement stmnt = connDB.createStatement();
			ResultSet rs = stmnt.executeQuery(sqlStr);
			if (rs.next()) {
				maxSl = rs.getLong(1);
			} else {
				maxSl = 1;
			}
			rs.close();
			rs = null;
			stmnt.close();
			stmnt = null;
			return maxSl;
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return 0;
		}
	}

	public boolean deleteByFields(String delteByFlds) {
		String[] fieldNamesarr = delteByFlds.split(",");
		sqlStr = " delete from " + varTableName + " where ";
		for (int i = 0; i < fieldNamesarr.length; i++) {
			if (i < fieldNamesarr.length - 1) {
				sqlStr = sqlStr + fieldNamesarr[i] + " = ? and  ";
			} else {
				sqlStr = sqlStr + fieldNamesarr[i] + " = ?";
			}
		}
		int posInt = 0;
		try {
			prepStatement = connDB.prepareStatement(sqlStr);
			for (int i = 0; i < fieldNamesarr.length; i++) {
				prepStatement.setString(++posInt, sqlMap.get(fieldNamesarr[i])
						.toString());
			}
			prepStatement.executeUpdate();
			prepStatement.close();
			prepStatement = null;
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return false;
		}
		return true;
	}

	public boolean loadByKeyFlds(String loadByKeyFlds) {
		boolean isRecExt = false;
		loadKeyFlds = loadByKeyFlds;
		String[] fieldNamesarr = loadByKeyFlds.split(",");
		sqlStr = " select * from " + varTableName + " where ";
		for (int i = 0; i < fieldNamesarr.length; i++) {
			if (i < fieldNamesarr.length - 1) {
				sqlStr = sqlStr + fieldNamesarr[i] + " = ?  and  ";
			} else {
				sqlStr = sqlStr + fieldNamesarr[i] + " = ? ";
			}
		}
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		try {
			prepStatement = connDB.prepareStatement(sqlStr);

			int posInt = 0;

			for (int i = 0; i < fieldNamesarr.length; i++) {
				prepStatement.setString(++posInt, sqlMap.get(fieldNamesarr[i])
						.toString());
			}
			rs = prepStatement.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			if (rs.next()) {
				for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
					if (rs.getMetaData().getColumnTypeName(i).equals("DATE")) {
						if (rs.getDate(i) != null) {
							setFiled(rs.getMetaData().getColumnName(i), sdf
									.format(rs.getDate(i)));
						} else {
							setFiled(rs.getMetaData().getColumnName(i), null);
						}
					} else {

						setFiled(rs.getMetaData().getColumnName(i), rs
								.getString(i));
					}
				}
				isRecExt = true;
			}
			prepStatement.close();
			prepStatement = null;
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return false;
		}
		return isRecExt;
	}

	public void New(boolean isNew) {
		IsNew = isNew;
	}

	public boolean isNew() {
		return IsNew;
	}

	public Timestamp getCurrentTimestamp(String date) {
		if (date.equals("") || date == null || date.equals("null")) {
			return null;
		} else {
			return (new java.sql.Timestamp(new java.util.Date().getTime()));
		}
	}

	public boolean save() {
		if (IsNew) {
			sqlStr = "INSERT INTO " + varTableName + "(";
			valStr = " VALUES (";
			for (Iterator i = sqlMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Map.Entry) i.next();
				sqlStr = sqlStr + e.getKey() + ",";
				valStr = valStr + "?" + ",";
			}
			sqlStr = sqlStr.substring(0, sqlStr.length() - 1) + ")";
			valStr = valStr.substring(0, valStr.length() - 1) + ")";
			sqlStr = sqlStr + valStr;
			try {
				System.out.println(sqlStr);
				prepStatement = connDB.prepareStatement(sqlStr);

				int posInt = 0;
				for (Iterator i = sqlMap.entrySet().iterator(); i.hasNext();) {
					Map.Entry e = (Map.Entry) i.next();
					String key = e.getKey().toString().toUpperCase();
					if (key.equals("ADATE") || key.equals("MOD_ON")
							|| key.equals("EDATE")) {
						prepStatement.setTimestamp(++posInt,
								getCurrentTimestamp((String) e.getValue()));
					} else {
						prepStatement.setString(++posInt, e.getValue()
								.toString());
					}
				}
				prepStatement.executeUpdate();
				prepStatement.close();
				prepStatement = null;
			} catch (SQLException e) {
				prepStatement = null;
				errMsg = e.getLocalizedMessage();
				return false;
			}
		} else {
			sqlStr = "UPDATE " + varTableName + " SET ";
			for (Iterator i = sqlMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Map.Entry) i.next();
				sqlStr = sqlStr + e.getKey() + "= ? ,";
			}
			sqlStr = sqlStr.substring(0, sqlStr.length() - 1);
			sqlStr = sqlStr + " Where ";
			String[] fieldNamesarr = loadKeyFlds.split(",");
			for (int i = 0; i < fieldNamesarr.length; i++) {
				if (i < fieldNamesarr.length - 1) {
					sqlStr = sqlStr + fieldNamesarr[i] + " = ? and  ";
				} else {
					sqlStr = sqlStr + fieldNamesarr[i] + " = ?";
				}
			}
			PreparedStatement prepStatement = null;
			try {
				prepStatement = connDB.prepareStatement(sqlStr);

				int posInt = 0;
				for (Iterator i = sqlMap.entrySet().iterator(); i.hasNext();) {
					Map.Entry e = (Map.Entry) i.next();
					String key = e.getKey().toString().toUpperCase();
					if (key.equals("ADATE") || key.equals("MOD_ON")
							|| key.equals("EDATE")) {
						prepStatement.setTimestamp(++posInt,
								getCurrentTimestamp((String) e.getValue()));
					} else {
						prepStatement.setString(++posInt, e.getValue()
								.toString());
					}
				}
				for (int i = 0; i < fieldNamesarr.length; i++) {
					prepStatement.setString(++posInt, sqlMap.get(
							fieldNamesarr[i]).toString());
				}
				prepStatement.executeUpdate();
				prepStatement.close();
				prepStatement = null;
			} catch (SQLException e) {
				prepStatement = null;
				errMsg = e.getLocalizedMessage();
				return false;
			}
		}
		return true;
	}

	public boolean save(String keyFields) {
		sqlStr = "UPDATE " + varTableName + " SET ";
		for (Iterator i = sqlMap.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			sqlStr = sqlStr + e.getKey() + "= ? ,";
		}
		sqlStr = sqlStr.substring(0, sqlStr.length() - 1);
		sqlStr = sqlStr + " Where ";
		String[] fieldNamesarr = keyFields.split(",");
		for (int i = 0; i < fieldNamesarr.length; i++) {
			if (i < fieldNamesarr.length - 1) {
				sqlStr = sqlStr + fieldNamesarr[i] + " = ? and  ";
			} else {
				sqlStr = sqlStr + fieldNamesarr[i] + " = ?";
			}
		}
		PreparedStatement prepStatement = null;
		try {
			prepStatement = connDB.prepareStatement(sqlStr);

			int posInt = 0;
			for (Iterator i = sqlMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Map.Entry) i.next();
				String key = e.getKey().toString().toUpperCase();
				if (key.equals("ADATE") || key.equals("MOD_ON")
						|| key.equals("EDATE") || key.equals("LAST_UPD_ON")
						|| key.equals("CDATE") || key.equals("REVIEW_ON")
						|| key.equals("RELEASED_ON")
						|| key.equals("REQ_HOLD_ON")
						|| key.equals("REJECTED_ON")
						|| key.equals("REPAIR_MARK_ON")
						|| key.equals("REPAIRED_ON")
						|| key.equals("ACNTS_IF_LAST_UPDATED_ON")
						|| key.equals("ACBAL_IF_LAST_UPDATED_ON")
						|| key.equals("DEAL_IF_LAST_UPDATED_ON")) {
					prepStatement.setTimestamp(++posInt,
							getCurrentTimestamp((String) e.getValue()));
				} else {
					prepStatement.setString(++posInt, e.getValue().toString());
				}
			}
			for (int i = 0; i < fieldNamesarr.length; i++) {
				prepStatement.setString(++posInt, sqlMap.get(fieldNamesarr[i])
						.toString());
			}
			prepStatement.executeUpdate();
			prepStatement.close();
			prepStatement = null;
		} catch (SQLException e) {
			prepStatement = null;
			errMsg = e.getLocalizedMessage();
			return false;
		}

		return true;
	}

	public DynaSQL[] loadByFlds(String loadByFlds) {
		String[] fieldNamesarr = loadByFlds.split(",");
		sqlStr = " select max(nvl(+maxColumn +),0) + 1 from " + varTableName
				+ " where ";
		for (int i = 0; i < fieldNamesarr.length; i++) {
			if (i < fieldNamesarr.length - 1) {
				sqlStr = sqlStr + fieldNamesarr[i] + " = '"
						+ sqlMap.get(fieldNamesarr[i]).toString() + "'  and  ";
			} else {
				sqlStr = sqlStr + fieldNamesarr[i] + " = '"
						+ sqlMap.get(fieldNamesarr[i]).toString() + "' ";
			}
		}
		Statement stmnt = null;
		ResultSet rs = null;
		try {
			stmnt = connDB.createStatement();
			rs = stmnt.executeQuery(sqlStr);
			java.util.Vector v = new java.util.Vector();
			if (rs.next()) {
				v.addElement(decodeRow(rs));
			}
			rs.close();
			rs = null;
			stmnt.close();
			stmnt = null;
			DynaSQL dynaSQL[] = new DynaSQL[v.size()];
			v.copyInto(dynaSQL);
			return dynaSQL;

		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			rs = null;
			stmnt = null;
			return null;
		}

	}

	private DynaSQL decodeRow(ResultSet rs) throws SQLException {
		DynaSQL obj = new DynaSQL(varTableName);
		for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
			obj.setFiled(rs.getMetaData().getColumnName(i), rs.getString(i));
		}
		return obj;
	}

	public boolean openConnection() 
	{
		try 
		{
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
		} 
		catch (SQLException e)
		{
			errMsg = e.getLocalizedMessage();
			return false;
		} 
		
		return true;
	}

	public boolean closeConnection() {
		try {

			connDB.close();
			connDB = null;
			dataSource = null;

		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return false;
		}
		return true;
	}

	public void setConnection(Connection con) {
		connDB = con;
	}

	public Connection getConnection() {
		return connDB;
	}

	public String getError() {
		return errMsg;
	}

	public String SlNo(String Year) {
		try {
			stmt = connDB.prepareCall("call sp_serial_no(?,?)");
			stmt.setString(1, Year);
			stmt.registerOutParameter(2, Types.VARCHAR);
			stmt.execute();
			return stmt.getString(2);
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public String SlNo1(String Year) {
		try {
			stmt = connDB.prepareCall("call sp_serial_no(?,?)");
			stmt.setString(1, Year);
			stmt.registerOutParameter(2, Types.VARCHAR);
			stmt.execute();
			return stmt.getString(2);
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public String getRequestType(String pgmId) {
		try {
			stmt = connDB.prepareCall("call GET_REQ_TYPE(?,?)");
			stmt.setString(1, pgmId);
			stmt.registerOutParameter(2, Types.VARCHAR);
			stmt.execute();
			return stmt.getString(2);
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public String BTR1(String Domid, String usrid, String reqst) {
		try {
			stmt = connDB.prepareCall("call SP_BTR_STOP_REQ(?,?,?,?,?)");
			stmt.setString(1, Domid);
			stmt.setString(2, usrid);
			stmt.setString(3, reqst);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.registerOutParameter(5, Types.VARCHAR);
			stmt.execute();
			return stmt.getString(4) + "|" + stmt.getString(5);
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public String BTR2(String Domid, String usrid) {
		try {
			stmt = connDB.prepareCall("call SP_BTR_ENQ(?,?,?,?)");
			stmt.setString(1, Domid);
			stmt.setString(2, usrid);
			stmt.registerOutParameter(3, Types.VARCHAR);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.execute();
			if (stmt.getString(4).equals("S")) {
				return stmt.getString(3);
			} else {
				stmt = null;
				return null;
			}
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public String BTR3(String Domid, String usrid) {
		try {
			stmt = connDB.prepareCall("call SP_BTR_ACDTLS(?,?,?,?,?)");
			stmt.setString(1, Domid);
			stmt.setString(2, usrid);
			stmt.registerOutParameter(3, Types.VARCHAR);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.registerOutParameter(5, Types.VARCHAR);
			stmt.execute();
			if (stmt.getString(5).equals("S")) {
				return stmt.getString(3) + "|" + stmt.getString(4) + "|"
						+ stmt.getString(5);

			} else {
				stmt = null;
				return null;
			}
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public String ADMIN(String Domid, String usrid, String reqst) {
		try {
			stmt = connDB.prepareCall("call SP_ADMIN(?,?,?,?,?)");
			stmt.setString(1, Domid);
			stmt.setString(2, usrid);
			stmt.setString(3, reqst);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.registerOutParameter(5, Types.VARCHAR);
			stmt.execute();
			return stmt.getString(4) + "|" + stmt.getString(5);
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public String SCF(String Domid, String usrid, String reqst) {
		try {
			stmt = connDB.prepareCall("call SP_SCF(?,?,?,?,?)");
			stmt.setString(1, Domid);
			stmt.setString(2, usrid);
			stmt.setString(3, reqst);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.registerOutParameter(5, Types.VARCHAR);
			stmt.execute();
			return stmt.getString(4) + "|" + stmt.getString(5);
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public String SINGLE(String Domid, String usrid, String reqst) {
		try {
			stmt = connDB.prepareCall("call SP_SINGLE(?,?,?,?,?)");
			stmt.setString(1, Domid);
			stmt.setString(2, usrid);
			stmt.setString(3, reqst);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.registerOutParameter(5, Types.VARCHAR);
			stmt.execute();
			return stmt.getString(4) + "|" + stmt.getString(5);
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public boolean saveData(String Query) {

		PreparedStatement prepStatement = null;
		try {
			prepStatement = connDB.prepareStatement(Query);

			int posInt = 0;

			prepStatement.executeUpdate();
			prepStatement.close();
			prepStatement = null;
		} catch (SQLException e) {
			prepStatement = null;
			errMsg = e.getLocalizedMessage();
			return false;
		}

		return true;
	}

	public String BulkSlNo(String Year) {
		try {
			stmt = connDB.prepareCall("call sp_uploadtmp_no(?,?)");
			stmt.setString(1, Year);
			stmt.registerOutParameter(2, Types.VARCHAR);
			stmt.execute();
			return stmt.getString(2);
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public Transpoter getSubClient(String protype, String dealerno) 
	{
		Transpoter resultDTO1 = new Transpoter();
		QueryManager QM = new QueryManager();
		System.out.println("in getsubclient protype" + protype);
		System.out.println("in getsubclient dealer" + dealerno);
		String Sql = "SELECT SCFSUB_SUB_CLIENT FROM SCFSUBPRODTYPE WHERE "
				+ " SCFSUB_PROD_TYPE='" + protype.trim()
				+ "' AND SCFSUB_SUB_PNEUMONIC='" + dealerno.trim() + "'  ";

		if (!QM.openConnection(connDB)) {
			return null;
		}
		if (QM.executeQuery(Sql)) {
			resultDTO1 = QM.getTranspoter();
		}
		return resultDTO1;
	}

	public String updateRequestMain(String fesnum, String reqType,
			String domainId, String cbd, String userId) throws SQLException {
		String result = null;
		try {
			stmt = connDB.prepareCall("call SP_FES_REQUESTHIST(?,?,?,?,?,?,?)");
			stmt.setString(1, fesnum);
			stmt.setString(2, reqType);
			stmt.setString(3, domainId);
			stmt.setString(4, cbd);
			stmt.setString(5, userId);
			stmt.setString(6, cbd);
			stmt.registerOutParameter(7, Types.VARCHAR);
			stmt.execute();
			result = stmt.getString(7);
			System.out.println("updateRequestMain Result: " + result);
			return result;
		} catch (SQLException e) {
			result = e.getLocalizedMessage();
			stmt = null;
			return result;
		}
	}

	public String getSqlStringForTransAuthorization(String domainId,
			String userId) throws SQLException, NamingException {
		String sqlString = "";
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB.prepareCall("call SP_FES_PORT_PEND_PYMTSQL(?,?,?)");
			stmt.setString(1, domainId);
			stmt.setString(2, userId);
			stmt.registerOutParameter(3, Types.VARCHAR);
			stmt.execute();
			System.out.println("in getSqlStringForTransAuthorization" + "\n"
					+ "SQL is :" + stmt.getString(3));
			sqlString = stmt.getString(3);
		} 
		catch (SQLException e) 
		{

		}
		finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
		return sqlString;
	}

	public String displayService(String pgmId) {
		String resulStr = " " + "|" + " ";
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			pqm = null;
			resulStr = " " + "|" + " ";
			return resulStr;
		}
		try {
			String sqlQuery = "SELECT REQTYPE,DESCN FROM REQUEST WHERE PGM_ID = ?";
			PreparedStatement _pstmt = null;
			if (pqm.prepareStatement(sqlQuery)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, pgmId);
				if (pqm.executeQuery()) {
					ResultSet rs = pqm.getResultSet();
					if (rs.next()) {
						resulStr = rs.getString("REQTYPE") + "|"
								+ rs.getString("DESCN");
					} else {
						resulStr = " " + "|" + " ";
					}
				} else {
					resulStr = " " + "|" + " ";
				}
			} else {
				resulStr = " " + "|" + " ";
			}
		} catch (Exception e) {

			resulStr = " " + "|" + " ";
		} finally {
			pqm.closeConnection();
			pqm = null;
		}
		return resulStr;
	}
	
	public String displayServices(String pgmId) {
		String resulStr = " " + "|" + " ";
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			pqm = null;
			resulStr = " " + "|" + " ";
			return resulStr;
		}
		try {
			String sqlQuery = "SELECT REQTYPE,DESCN,REQ_CHRGS_APPL FROM REQUEST WHERE PGM_ID = ?";
			PreparedStatement _pstmt = null;
			if (pqm.prepareStatement(sqlQuery)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, pgmId);
				if (pqm.executeQuery()) {
					ResultSet rs = pqm.getResultSet();
					if (rs.next()) {
						resulStr = rs.getString("REQTYPE") + "|" + rs.getString("DESCN")+ "|" + rs.getString("REQ_CHRGS_APPL");
					} else {
						resulStr = " " + "|" + " "+ "|" + " ";
					}
				} else {
					resulStr = " " + "|" + " "+ "|" + " ";
				}
			} else {
				resulStr = " " + "|" + " "+ "|" + " ";
			}
		} catch (Exception e) {

			resulStr = " " + "|" + " "+ "|" + " ";
		} finally {
			pqm.closeConnection();
			pqm = null;
		}
		return resulStr;
	}

	public String getScheduleDate(String scheduleDate, String reqType)
			throws SQLException {
		String result = null;
		try {
			stmt = connDB.prepareCall("call SP_FES_PYMNT_GETSCH_DATE(?,?,?,?)");
			stmt.setString(1, reqType);
			stmt.setString(2, FormatUtils.dynaSQLDate(scheduleDate,
					"DD-MM-YYYY"));
			stmt.registerOutParameter(3, Types.DATE);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.execute();
			result = stmt.getDate(3).toString() + "|" + stmt.getString(4);
		} catch (SQLException e) {
			result = e.getLocalizedMessage();
			stmt = null;
		}
		return result;
	}

	
	public String checkHolidayTimings(String currBusDate, String reqType)
			throws SQLException, NamingException {
		String result = null;
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB.prepareCall("call SP_FES_HOLIDAY_WINTIME_VAL(?,?,?,?)");
			stmt.setString(1, reqType);
			stmt.setString(2, currBusDate);
			stmt.registerOutParameter(3, Types.VARCHAR);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.execute();
			result = stmt.getString(3).toString() + "|" + stmt.getString(4);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
		return result;
	}
	
	public String checkHoliday(String reqType, String currBusDate)
			throws SQLException, NamingException {
		String result = null;
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB.prepareCall("call SP_FES_HOLIDAYCHECK(?,?,?,?)");
			stmt.setString(1, reqType);
			stmt.setString(2, currBusDate);
			stmt.registerOutParameter(3, Types.VARCHAR);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.execute();
			result = stmt.getString(4);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
		return result;
	}
	
	public String checkReqRestrictions(String domainId, String userId,
			String requestType) throws SQLException, NamingException {
		String errorStr = "";
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB
					.prepareCall("call SP_FES_CHKREQ_RESTRCITIONS(?,?,?,?)");
			stmt.setString(1, domainId);
			stmt.setString(2, userId);
			stmt.setString(3, requestType);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.execute();
			System.out.println("in checkReqRestrictions " + stmt.getString(4));
			errorStr = stmt.getString(4);
		} catch (SQLException e) {
			errorStr = e.getMessage();
		} finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
		return errorStr;
	}

	public String updateAuthQueue(String fesNumber, String domainId)
			throws SQLException, NamingException {
		String errorStr = "";
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB.prepareCall("call SP_FESVALIDATION1(?,?,?)");
			stmt.setString(1, fesNumber);
			stmt.setString(2, domainId);
			stmt.registerOutParameter(3, Types.VARCHAR);
			stmt.execute();
			errorStr = stmt.getString(3);
			System.out.println("in SP_FESVALIDATION1 " + errorStr);
		} catch (SQLException e) {
			System.out.println("Error in SP_FESVALIDATION1 "
					+ e.getLocalizedMessage());
			errorStr = e.getMessage();
		}finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
		return errorStr;
	}

	public String checkAmountLimitationforRequestType(String domainId,
			String userId, String requestType, double amount,
			String accountNumber) throws SQLException, NamingException {
		String errorStr = "";
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB.prepareCall("call SP_FES_CHKREQ_AMTLMT(?,?,?,?,?,?)");
			stmt.setString(1, domainId);
			stmt.setString(2, userId);
			stmt.setString(3, requestType);
			stmt.setDouble(4, amount);
			stmt.setString(5, accountNumber);
			stmt.registerOutParameter(6, Types.VARCHAR);
			stmt.execute();
			errorStr = stmt.getString(6);
			
		} catch (SQLException e) {

			errorStr = e.getLocalizedMessage();
		} finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
		return errorStr;
	}

	public static int readRequestprocqTable(String fesnum) throws SQLException {
		int recordCount = -1;
		ResultSet rs = null;
		PreparedStatement _pstmt = null;
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			pqm = null;
			return recordCount;
		}
		try {
			String sqlStr = "select count(*) COUNT from REQUESTPROCQ where FES_REF_NO=?";
			pqm.getConnection();
			if (pqm.prepareStatement(sqlStr)) {
				_pstmt = pqm.getStatement();
				_pstmt.setDouble(1, Double.parseDouble(fesnum));
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						recordCount = (int) rs.getDouble("COUNT");
					}
				}
			}
		} catch (SQLException e) {

		} finally {
			rs.close();
			rs = null;
			_pstmt.close();
			_pstmt = null;
			pqm.closeConnection();
		}
		return recordCount;
	}

	public static String getStatus(String fesnum) throws SQLException {
		String status = "";
		ResultSet rs = null;
		PreparedStatement _pstmt = null;
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			pqm = null;
			return status;
		}
		try {
			String sqlStr = "select REQUEST_STATUS from REQUESTMAIN where FES_REF_NO=?";
			if (pqm.prepareStatement(sqlStr)) {
				_pstmt = pqm.getStatement();
				_pstmt.setDouble(1, Double.parseDouble(fesnum));
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						status = rs.getString("REQUEST_STATUS");
					}
				}
			}
		} catch (SQLException e) {

		} finally {
			rs.close();
			rs = null;
			_pstmt.close();
			_pstmt = null;
			pqm.closeConnection();
		}
		return status;
	}

	public boolean headerupd(DTDObject data, Map insertObj, int pkcount) 
	{
		Transpoter result = new Transpoter();
		int slno = 1;
		sqlStr = "INSERT INTO " + varTableName + "(";
		valStr = " VALUES (";
		try 
		{
			for (Iterator i = insertObj.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Map.Entry) i.next();
				sqlStr = sqlStr + e.getKey() + ",";
				valStr = valStr + "?" + ",";
			}

			sqlStr = sqlStr.substring(0, sqlStr.length() - 1) + ")";
			valStr = valStr.substring(0, valStr.length() - 1) + ")";
			System.out.println("Query" + sqlStr + valStr);
			sqlStr = sqlStr + valStr;
			PreparedStatement stmt = connDB.prepareStatement(sqlStr);
			for (int i = 0; i < data.getRowCount(); ++i) {
				int posInt = 0;

				Iterator k = insertObj.keySet().iterator();

				int pkCurrent = 0;
				while (k.hasNext()) {
					String key = (String) k.next();
					String value = (String) insertObj.get(key);
					stmt.setString(++posInt, value);
					System.out.println("Col ::: " + value);
					pkCurrent++;
					if (pkCurrent == pkcount)
						break;
				}
				stmt.setInt(++posInt, slno);
				for (int j = 0; j < data.getColCount(); j++) {

					stmt.setString(++posInt, data.getValue(i, j).toString());
					System.out.println(posInt + ":np:"
							+ data.getValue(i, j).toString());

				}

				slno++;
				int a = stmt.executeUpdate();
				System.out.println("Inserted values" + a);
			}
			stmt.close();
			stmt = null;
		} catch (Exception e) {

			return false;
		}

		String refno = (String) insertObj.get("FES_REF_NO");
		return true;
	}

	public int insertAudit_Log(AuditLog input, int _audit_dtl_sl)
			throws Exception {
		PreparedQueryManager pqm = new PreparedQueryManager();
		PreparedStatement _pstmt = null;
		_audit_dtl_sl++;
		if (pqm.openConnection()) {
			try {

				String sqlQuery = "INSERT INTO LOG001 VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				_pstmt = null;

				if (pqm.prepareStatement(sqlQuery)) {
					_pstmt = pqm.getStatement();
					_pstmt.setString(1, input.getDomainId());
					_pstmt.setString(2, input.getUserId());
					_pstmt.setTimestamp(3, input.getDateTime());
					_pstmt.setLong(4, input.getLogSl());
					_pstmt.setInt(5, _audit_dtl_sl);
					_pstmt.setString(6, input.getFormName());
					_pstmt.setString(7, input.getUserIp());
					_pstmt.setString(8, input.getUserAction());
					_pstmt.setString(9, input.getTableName());
					_pstmt.setString(10, input.getTablePK());
					_pstmt.setString(11, input.getImageType());
					_pstmt.setInt(12, input.getBreakSl());
					_pstmt.setString(13, input.getTableData());
					_pstmt.executeUpdate();
				}
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			} finally {
				pqm.closeConnection();
			}

		}
		return _audit_dtl_sl;
	}

	public long get_AuditLogSl() {
		PreparedQueryManager pqm = new PreparedQueryManager();
		Connection conn = null;
		long atlog_sl = 0;
		if (!pqm.openConnection()) {
			pqm = null;

		}
		try {
			String sqlQuery = "SELECT SEQ_ATLOGSL.NEXTVAL FROM DUAL";
			Statement _pstmt = null;
			conn = pqm.getConnection();
			_pstmt = conn.createStatement();
			ResultSet set = _pstmt.executeQuery(sqlQuery);
			if (set.next()) {
				atlog_sl = set.getLong(1);
			}

		} catch (Exception e) {

			System.out.println(e.getLocalizedMessage());
		} finally {
			pqm.closeConnection();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {

				}
			}
		}

		return atlog_sl;

	}

	public Map getDataByKeyFlds(String loadByKeyFlds) {
		PreparedQueryManager pqm = new PreparedQueryManager();
		Map resultMap = new HashMap();
		loadKeyFlds = loadByKeyFlds;
		String[] fieldNamesarr = loadByKeyFlds.split(",");
		sqlStr = " select * from " + varTableName + " where ";
		for (int i = 0; i < fieldNamesarr.length; i++) {
			if (i < fieldNamesarr.length - 1) {
				sqlStr = sqlStr + fieldNamesarr[i] + " = ?  and  ";
			} else {
				sqlStr = sqlStr + fieldNamesarr[i] + " = ? ";
			}
		}
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		if (pqm.openConnection()) {
			if (pqm.prepareStatement(sqlStr)) {
				try {
					int posInt = 0;
					prepStatement = pqm.getStatement();
					for (int i = 0; i < fieldNamesarr.length; i++) {
						prepStatement.setString(++posInt, sqlMap.get(
								fieldNamesarr[i]).toString());
					}
					rs = prepStatement.executeQuery();
					if (rs.next()) {
						for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
							if (rs.getMetaData().getColumnTypeName(i).equals(
									"DATE")) {
								if (rs.getTimestamp(i) != null) {
									resultMap.put(rs.getMetaData()
											.getColumnName(i), rs.getTimestamp(
											i).toString());
								} else {
									resultMap.put(rs.getMetaData()
											.getColumnName(i), "");
								}
							} else {
								resultMap.put(
										rs.getMetaData().getColumnName(i), rs
												.getString(i) == null ? "" : rs
												.getString(i));
							}
						}
					}
					prepStatement.close();
					prepStatement = null;

				} catch (SQLException e) {
					errMsg = e.getLocalizedMessage();
					return null;
				} catch (Exception e) {

				} finally {

					pqm.closeConnection();
				}
			}
		}
		return resultMap;
	}

	public String getGridDataByKeyFlds(String tableName, String loadByKeyFlds) {
		PreparedQueryManager pqm = new PreparedQueryManager();
		StringBuffer resultMap = new StringBuffer();
		loadKeyFlds = loadByKeyFlds;
		String[] fieldNamesarr = loadByKeyFlds.split(",");
		sqlStr = " select * from " + tableName + " where ";
		for (int i = 0; i < fieldNamesarr.length; i++) {
			if (i < fieldNamesarr.length - 1) {
				sqlStr = sqlStr + fieldNamesarr[i] + " = ?  and  ";
			} else {
				sqlStr = sqlStr + fieldNamesarr[i] + " = ? ";
			}
		}
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		if (pqm.openConnection()) {
			if (pqm.prepareStatement(sqlStr)) {
				try {
					int posInt = 0;
					prepStatement = pqm.getStatement();
					for (int i = 0; i < fieldNamesarr.length; i++) {
						prepStatement.setString(++posInt, sqlMap.get(
								fieldNamesarr[i]).toString());
					}
					rs = prepStatement.executeQuery();
					resultMap.append("<![CDATA[<properties>");
					while (rs.next()) {
						resultMap.append("<record>");
						for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
							if (rs.getMetaData().getColumnTypeName(i).equals(
									"DATE")) {
								if (rs.getTimestamp(i) != null) {
									resultMap.append("<entry key='"
											+ rs.getMetaData().getColumnName(i)
											+ "'>"
											+ rs.getTimestamp(i).toString()
											+ "</entry>");
								} else {
									resultMap.append("<entry key='"
											+ rs.getMetaData().getColumnName(i)
											+ "'>" + "" + "</entry>");
								}
							} else {
								String value = rs.getString(i) == null ? ""
										: rs.getString(i);
								resultMap.append("<entry key='"
										+ rs.getMetaData().getColumnName(i)
										+ "'>" + value + "</entry>");

							}
						}
						resultMap.append("</record>");
					}
					resultMap.append("</properties>]]>");
					System.out.println("pre dta image in dyna sql:"
							+ resultMap.toString());
					prepStatement.close();
					prepStatement = null;

				} catch (SQLException e) {
					errMsg = e.getLocalizedMessage();
					return null;
				} catch (Exception e) {

				} finally {

					pqm.closeConnection();
				}
			}
		}
		return resultMap.toString();
	}


	public int updateAudit_Log(AuditLog input, int _audit_dtl_sl)
			throws SQLException {
		try {
			String action = (String) input.getUserAction();
			if (action.equalsIgnoreCase("A")) {
				Map map = input.getDataMap();
				Set set = map.entrySet();
				StringBuffer dataMap = new StringBuffer();
				dataMap.append("<![CDATA[<properties>");
				for (Iterator i = set.iterator(); i.hasNext();) {
					Map.Entry entry = (Map.Entry) i.next();
					dataMap.append("<entry key='" + "entry.getKey()" + "'>"
							+ (String) entry.getValue() == null ? ""
							: (String) entry.getValue() + "</entry>");
				}
				dataMap.append("</properties>]]>");

				String data = new String(dataMap.toString());
				int _Q = data.length() / 4000;
				int _R = data.length() % 4000;
				int loopcount = _Q;
				if (_R != 0) {
					loopcount++;
				}
				int startIndex = 0;
				for (int i = 0; i < loopcount; ++i) {
					input.setBreakSl(i + 1);
					if (i == (loopcount - 1))
						input.setTableData(data.substring(startIndex,
								startIndex + _R));
					else
						input.setTableData(data.substring(startIndex,
								startIndex + 4000));
					input.setImageType("N");
					_audit_dtl_sl = insertAudit_Log(input, _audit_dtl_sl);
					startIndex += 4000;
				}
			} else {
				Map prop = input.getDataMaponModify();
				Set set = prop.entrySet();
				StringBuffer dataMap = new StringBuffer();
				dataMap.append("<![CDATA[<properties>");
				for (Iterator i = set.iterator(); i.hasNext();) {
					Map.Entry entry = (Map.Entry) i.next();
					dataMap.append("<entry key='" + "entry.getKey()" + "'>"
							+ entry.getValue() == null ? "" : entry.getValue()
							+ "</entry>");
				}
				dataMap.append("</properties>]]>");
				String data = new String(dataMap.toString());

				int _Q = data.length() / 4000;
				int _R = data.length() % 4000;
				int loopcount = _Q;
				if (_R != 0) {
					loopcount++;
				}
				int startIndex = 0;
				for (int i = 0; i < loopcount; ++i) {
					input.setBreakSl(i + 1);
					if (i == (loopcount - 1))
						input.setTableData(data.substring(startIndex,
								startIndex + _R));
					else
						input.setTableData(data.substring(startIndex,
								startIndex + 4000));
					input.setImageType("O");
					_audit_dtl_sl = insertAudit_Log(input, _audit_dtl_sl);
					startIndex += 4000;
				}
				Map map = input.getDataMap();
				set = map.entrySet();
				dataMap = new StringBuffer();
				dataMap.append("<![CDATA[<properties>");
				for (Iterator i = set.iterator(); i.hasNext();) {
					Map.Entry entry = (Map.Entry) i.next();
					dataMap.append("<entry key='" + "entry.getKey()" + "'>"
							+ entry.getValue() == null ? "" : entry.getValue()
							+ "</entry>");
				}
				dataMap.append("</properties>]]>");

				data = new String(dataMap.toString());
				_Q = data.length() / 4000;
				_R = data.length() % 4000;
				loopcount = _Q;
				if (_R != 0) {
					loopcount++;
				}
				startIndex = 0;
				for (int i = 0; i < loopcount; ++i) {
					input.setBreakSl(i + 1);
					if (i == (loopcount - 1))
						input.setTableData(data.substring(startIndex,
								startIndex + _R));
					else
						input.setTableData(data.substring(startIndex,
								startIndex + 4000));
					input.setImageType("N");
					_audit_dtl_sl = insertAudit_Log(input, _audit_dtl_sl);
					startIndex += 4000;
				}
			}
		} catch (Exception e) {

			throw new SQLException(e.getMessage());
		}
		return _audit_dtl_sl;

	}

	public int updateGridDataAudit_Log(AuditLog input, int _audit_dtl_sl)
			throws SQLException {
		try {
			String action = (String) input.getUserAction();
			if (action.equalsIgnoreCase("A")) {
				String data = input.getGirdData();
				int _Q = data.length() / 4000;
				int _R = data.length() % 4000;
				int loopcount = _Q;
				if (_R != 0) {
					loopcount++;
				}
				int startIndex = 0;
				for (int i = 0; i < loopcount; ++i) {
					input.setBreakSl(i + 1);
					if (i == (loopcount - 1))
						input.setTableData(data.substring(startIndex,
								startIndex + _R));
					else
						input.setTableData(data.substring(startIndex,
								startIndex + 4000));
					input.setImageType("N");
					_audit_dtl_sl = insertAudit_Log(input, _audit_dtl_sl);
					startIndex += 4000;
				}
			} else {
				String data = input.getGridDataonModify();
				int _Q = data.length() / 4000;
				int _R = data.length() % 4000;
				int loopcount = _Q;
				if (_R != 0) {
					loopcount++;
				}
				int startIndex = 0;
				for (int i = 0; i < loopcount; ++i) {
					input.setBreakSl(i + 1);
					if (i == (loopcount - 1)) {
						input.setTableData(data.substring(startIndex,
								startIndex + _R));
					} else {
						input.setTableData(data.substring(startIndex,
								startIndex + 4000));
					}
					input.setImageType("O");
					_audit_dtl_sl = insertAudit_Log(input, _audit_dtl_sl);
					startIndex += 4000;
				}
				data = input.getGirdData();

				_Q = data.length() / 4000;
				_R = data.length() % 4000;
				loopcount = _Q;
				if (_R != 0) {
					loopcount++;
				}
				startIndex = 0;
				for (int i = 0; i < loopcount; ++i) {
					input.setBreakSl(i + 1);
					if (i == (loopcount - 1)) {
						input.setTableData(data.substring(startIndex,
								startIndex + _R));
					} else {
						input.setTableData(data.substring(startIndex,
								startIndex + 4000));
					}
					input.setImageType("N");
					_audit_dtl_sl = insertAudit_Log(input, _audit_dtl_sl);
					startIndex += 4000;
				}
			}
		} catch (Exception e) {

			throw new SQLException(e.getMessage());
		}
		return _audit_dtl_sl;

	}

	public String copyVal(String Domid, String srcaccno, String desaccno,
			String userId) {
		try {
			stmt = connDB.prepareCall("call SP_FES_COPY_TRANCTL(?,?,?,?,?)");
			stmt.setString(1, Domid);
			stmt.setString(2, srcaccno);
			stmt.setString(3, desaccno);
			stmt.setString(4, userId);
			stmt.registerOutParameter(5, Types.VARCHAR);
			stmt.execute();
			return stmt.getString(5);
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;

		}
	}

	

	

	public String getSqlStringForTradeAuthorization(String domainId,
			String userId, String service) throws SQLException, NamingException {
		String sqlString = "";
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB
					.prepareCall("call SP_FES_PORT_PEND_TRADESQL(?,?,?,?)");
			stmt.setString(1, domainId);
			stmt.setString(2, userId);
			stmt.setString(3, service);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.execute();
			System.out.println("in getSqlStringForTradeAuthorization" + "\n"
					+ "SQL is :" + stmt.getString(4));
			sqlString = stmt.getString(4);
		} catch (SQLException e) {

		} finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
		return sqlString;
	}

	
	public String getSqlStringForAdminAuthorization(String pgmid,
			String extdomid, String entdby, String sesdomid, String sesusrid, String searchval)
			throws SQLException, NamingException {
		String sqlString = "";
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB.prepareCall("call PROC_AUTHORIZER (?,?,?,?,?,?,?)");
			stmt.setString(1, pgmid);
			stmt.setString(2, extdomid);
			stmt.setString(3, entdby);
			stmt.setString(4, sesdomid);
			stmt.setString(5, sesusrid);
			stmt.setString(6, searchval);
			stmt.registerOutParameter(7, Types.VARCHAR);
			stmt.execute();
			sqlString = stmt.getString(7);
		} catch (SQLException e) {

		} finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
		return sqlString;
	}

	

	public String getRequestStatus(String ref) throws Exception {
		PreparedQueryManager pqm = new PreparedQueryManager();
		Connection conn = null;
		String req_status = ref;
		if (!pqm.openConnection()) {
			pqm = null;
		}
		try {
			String sqlQuery = "SELECT REQUEST_STATUS FROM REQUESTMAIN WHERE FES_REF_NO="
					+ ref;
			Statement _pstmt = null;
			conn = pqm.getConnection();
			_pstmt = conn.createStatement();
			ResultSet set = _pstmt.executeQuery(sqlQuery);
			if (set.next()) {
				req_status = set.getString(1);
			}

		} catch (Exception e) {

			System.out.println(e.getLocalizedMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e1) {

				}
			}
		}

		return req_status;
	}


	public String TempRefNo(String Year) {
		try {
			stmt = connDB.prepareCall("call sp_uploadtmp_no(?,?)");
			stmt.setString(1, Year);
			stmt.registerOutParameter(2, Types.VARCHAR);
			stmt.execute();
			return stmt.getString(2);
		} catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			stmt = null;
			return null;
		}
	}

	public String checkReqRestrictions_scf(String domainId, String userId,
			String requestType) throws SQLException, NamingException {
		String errorStr = "";
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB
					.prepareCall("call SP_FES_CHKREQ_RESTRCITIONS_SCF(?,?,?,?)");
			stmt.setString(1, domainId);
			stmt.setString(2, userId);
			stmt.setString(3, requestType);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.execute();
			System.out.println("in checkReqRestrictions " + stmt.getString(4));
			errorStr = stmt.getString(4);
		} catch (SQLException e) {
			errorStr = e.getMessage();
		}finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
		return errorStr;
	}

	public String getScheduleDate_SCF(String scheduleDate, String reqType)
			throws SQLException, NamingException {
		String result = null;
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB.prepareCall("call SP_FES_PYMNT_GETSCH_DATE(?,?,?,?)");
			stmt.setString(1, reqType);
			stmt.setString(2, FormatUtils.dynaSQLDate(scheduleDate,
					"DD-MM-YYYY"));
			stmt.registerOutParameter(3, Types.DATE);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.execute();
			result = stmt.getDate(3).toString() + "|" + stmt.getString(4);
			return result;
		} catch (SQLException e) {
			result = e.getLocalizedMessage();
			stmt = null;
			return result;
		} finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
	}

	public String updateAuthQueue_SCF(String fesNumber, String domainId)
			throws SQLException, NamingException {
		String errorStr = "";
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB.prepareCall("call SP_FESVALIDATION1_SCF(?,?,?)");
			stmt.setString(1, fesNumber);
			stmt.setString(2, domainId);
			stmt.registerOutParameter(3, Types.VARCHAR);
			stmt.execute();
			errorStr = stmt.getString(3);
			System.out.println("in SP_FESVALIDATION1_SCF " + errorStr);
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
			return errorStr;
		} catch (SQLException e) {
			System.out.println("Error in SP_FESVALIDATION1_SCF "
					+ e.getLocalizedMessage());
			errorStr = e.getLocalizedMessage();
			dataSource = null;
			connDB = null;
			stmt = null;
			return errorStr;
		} 
	}

	public boolean Blk_save(PreparedQueryManager pqm, PreparedStatement prepStat) {
		sqlStr = "INSERT INTO " + varTableName + "(";
		valStr = " VALUES (";
		for (Iterator i = sqlMap.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			sqlStr = sqlStr + e.getKey() + ",";
			valStr = valStr + "?" + ",";
		}
		sqlStr = sqlStr.substring(0, sqlStr.length() - 1) + ")";
		valStr = valStr.substring(0, valStr.length() - 1) + ")";
		sqlStr = sqlStr + valStr;
		try {
			pqm.prepareStatement(sqlStr);
			prepStat = pqm.getStatement();
			int posInt = 0;
			for (Iterator i = sqlMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Map.Entry) i.next();
				String key = e.getKey().toString().toUpperCase();
				if (key.equals("ADATE") || key.equals("MOD_ON")
						|| key.equals("EDATE")) {
					prepStat.setTimestamp(++posInt,
							getCurrentTimestamp((String) e.getValue()));
				} else {
					prepStat.setString(++posInt, e.getValue().toString());
				}
			}
			prepStat.executeUpdate();
			prepStat.close();
			prepStat = null;
		} catch (SQLException e) {
			prepStatement = null;
			errMsg = e.getLocalizedMessage();
			return false;
		}

		return true;
	}

	public String checkReqRestrictions_trade(String domainId, String userId,
			String requestType) throws SQLException, NamingException {
		String errorStr = "";
		try {
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
			stmt = connDB
					.prepareCall("call SP_FES_CHKREQ_RESTRCITIONS_TRD(?,?,?,?)");
			stmt.setString(1, domainId);
			stmt.setString(2, userId);
			stmt.setString(3, requestType);
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.execute();
			System.out.println("in checkReqRestrictions " + stmt.getString(4));
			errorStr = stmt.getString(4);
		} catch (SQLException e) {
			errorStr = e.getMessage();
		}  finally {
			dataSource = null;
			connDB.close();
			connDB = null;
			stmt = null;
		}
		return errorStr;
	}


	public String encodeXML(String data) {
		if (data == null)
			return (data);
		data = data.replaceAll("&", "&amp;");
		data = data.replaceAll("<", "& lt;");
		data = data.replaceAll(">", "& gt;");
		data = data.replaceAll("\\(", "& #40;");
		data = data.replaceAll("\\)", "& #41;");
		data = data.replaceAll("\"", "&quot;");
		data = data.replaceAll("\'", "&apos;");
		return data;
	}

	public String decodeXML(String data) {
		if (data == null)
			return (data);
		data = data.replaceAll("&amp;", "&");
		data = data.replaceAll("& lt;", "<");
		data = data.replaceAll("& gt;", ">");
		data = data.replaceAll("& #40;", "\\(");
		data = data.replaceAll("& #41;", "\\)");
		data = data.replaceAll("&quot;", "\"");
		data = data.replaceAll("&apos;", "\'");
		return data;
	}

}
