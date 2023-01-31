package com.hdsoft.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;
import javax.sql.DataSource;

import com.google.gson.JsonObject;
import com.hdsoft.common.DTDObject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom; 
import com.hdsoft.common.Transpoter;

public class Comvalidator 
{
	private ApplicationLogger logger = null;

	public Comvalidator() 
	{
		logger = ApplicationLogger.getInstance(this.getClass().getSimpleName());
		logger.logDebug("Init");
	}

	Transpoter resultDTO = new Transpoter();

	Connection Con = null;

	public void setConnection(Connection Con) {
		this.Con = Con;
	}



	public Transpoter varProdCode(Transpoter formDTO) {
		logger.logDebug("varProdCode");
		resultDTO.clearMap();
		String varProdCode = formDTO.getValue("varProdCode");
		PreparedQueryManager QM = new PreparedQueryManager();

		if (!QM.openConnection()) {
			resultDTO.setValue("result", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String strSQL;
			strSQL = "Select CUSPRD_PROD_CODE,MPROD_PROD_DESC from cusprd,mprod where cusprd_prod_code=MPROD_PROD_CODE and CUSPRD_PROD_CODE=?";
			QM.openConnection();
			QM.prepareStatement(strSQL.toString());
			QM.getStatement().setString(1, varProdCode);

			if (QM.executeQuery()) {
				resultDTO = QM.getTranspoter();
				if (QM.isRowPresent()) {
					resultDTO.setValue("sucFlg", "1");
				} else {
					resultDTO.setValue("errMsg", "Row Not Present");
					resultDTO.setValue("sucFlg", "0");
				}

			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");

			}
			QM.closeConnection();
			QM = null;

		} catch (Exception e) {
			logger.logDebug("varProdCode::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
			QM.closeConnection();
			QM = null;
			return resultDTO;
		}
		return resultDTO;
	}

	

	public Transpoter validateCurrencyCode(Transpoter data) {
		logger.logDebug("validateCurrencyCode");
		Transpoter result = new Transpoter();
		result.setValue(Transpoter.ERROR_KEY, Transpoter.ERROR_ABSENT);
		result.setValue(Transpoter.FORM_ERROR, Transpoter.FORM_ERROR_ABSENT);
		String value = data.getValue("CURR_CODE");
		if (data.containsKey(Transpoter.CHECK_BLANK)
				&& data.getValue(Transpoter.CHECK_BLANK)
						.equals(Transpoter.REQUIRED)) {
			if (FormatUtils.isBlank(value)) {
				result.setValue(Transpoter.ERROR_STATUS, Transpoter.ERROR_PRESENT);
				result.setValue(Transpoter.ERROR_CODE, Transpoter.FIELD_BLANK);
				return result;
			}
		}
		if (data.containsKey(Transpoter.CHECK_LENGTH)
				&& data.getValue(Transpoter.CHECK_LENGTH).equals(
						Transpoter.REQUIRED)) {
			int min_length = Integer.parseInt(data
					.getValue(Transpoter.MIN_LENGTH));
			int max_length = Integer.parseInt(data
					.getValue(Transpoter.MAX_LENGTH));
			if (!(FormatUtils.hasMinimumLength(value, min_length) && FormatUtils
					.hasMaximumLength(value, max_length))) {
				result.setValue(Transpoter.ERROR_STATUS, Transpoter.ERROR_PRESENT);
				result.setValue(Transpoter.ERROR_CODE, Transpoter.FIELD_INVALID);
				return result;
			}
		}
		PreparedQueryManager _QM = new PreparedQueryManager();
		if (!_QM.openConnection()) {
			result.setValue(Transpoter.ERROR_STATUS, Transpoter.ERROR_PRESENT);
			result.setValue(Transpoter.FORM_ERROR, Transpoter.FORM_ERROR_PRESENT);
			return result;
		}
		try {
			String sqlQuery = "Select CURR_CODE,CURR_NAME from CURRENCY where CURR_CODE = ?";
			if (!_QM.prepareStatement(sqlQuery)) {
				result.setValue(Transpoter.ERROR_STATUS, Transpoter.ERROR_PRESENT);
				result.setValue(Transpoter.FORM_ERROR,
						Transpoter.FORM_ERROR_PRESENT);
				return result;
			}
			PreparedStatement statement = _QM.getStatement();
			statement.setString(1, value);
			if (!_QM.executeQuery()) {
				result.setValue(Transpoter.ERROR_STATUS, Transpoter.ERROR_PRESENT);
				result.setValue(Transpoter.FORM_ERROR,
						Transpoter.FORM_ERROR_PRESENT);
				return result;
			}
			ResultSet rs = _QM.getResultSet();
			if (rs.next()) {
				result.setValue(Transpoter.ROW_STATUS, Transpoter.ROW_PRESENT);
				result.setValue("CURR_NAME", rs.getString("CURR_NAME"));
			} else {
				result.setValue(Transpoter.ROW_STATUS, Transpoter.ROW_ABSENT);
			}
			_QM.closeConnection();
		} catch (SQLException e) {
			logger.logDebug("validateCurrencyCode::" + e.getLocalizedMessage());
			result.setValue(Transpoter.ERROR_STATUS, Transpoter.ERROR_PRESENT);
			result.setValue(Transpoter.FORM_ERROR, Transpoter.FORM_ERROR_PRESENT);
			return result;
		} catch (Exception e) {
			logger.logDebug("validateCurrencyCode::" + e.getLocalizedMessage());
			result.setValue(Transpoter.ERROR_STATUS, Transpoter.ERROR_PRESENT);
			result.setValue(Transpoter.FORM_ERROR, Transpoter.FORM_ERROR_PRESENT);
			return result;
		}
		return result;
	}

	public Transpoter valCurrCode(Transpoter formDTO) {
		logger.logDebug("valCurrCode");
		resultDTO.clearMap();
		String varCurrCode = formDTO.getValue("varCurrCode");
		PreparedQueryManager QM = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		try {
			if (!QM.openConnection()) {
				resultDTO.setValue("result", QM.getErrorMessage());
				QM = null;
				return resultDTO;
			}
			String strSQL;
			strSQL = "Select CURR_CODE,CURR_NAME from CURRENCY where CURR_CODE=?";
			logger.logDebug("Query:" + strSQL);
			if (QM.prepareStatement(strSQL)) {
				pstmt = QM.getStatement();
				pstmt.setString(1, varCurrCode);
				if (QM.executeQuery()) {
					resultDTO = QM.getTranspoter();
					if (QM.isRowPresent()) {
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("errMsg", "Row Not Present");
						resultDTO.setValue("sucFlg", "0");
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			logger.logDebug("valCurrCode::" + e.getLocalizedMessage());
			e.printStackTrace();

		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				logger.logDebug("valCurrCode::" + e.getLocalizedMessage());
				e.printStackTrace();
			}
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	
	
	public Transpoter validateProgramId(Transpoter formDTO) {
		logger.logDebug("validateProgramId");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager _QM = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		StringBuffer sqlstr = new StringBuffer();
		if (!_QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", _QM.getErrorMessage());
			_QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			sqlstr = new StringBuffer(
					"SELECT PGM_ID,DESCN FROM MENU001 WHERE PGM_ID= ? ");
			if (!_QM.prepareStatement(sqlstr.toString())) {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", _QM.getErrorMessage());
				_QM = null;
				return resultDTO;
			}
			pstmt = _QM.getStatement();
			pstmt.setString(1, SqlArgs);
			if (_QM.executeQuery()) {
				resultDTO = _QM.getTranspoter();
				resultDTO.setValue("sucFlg", "1");
			} else {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", "Row Not Present");
			}

		} catch (Exception e) {
			logger.logDebug("validateProgramId::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger
						.logDebug("validateProgramId::"
								+ e.getLocalizedMessage());

			}
			_QM.closeConnection();
			_QM = null;
		}
		return resultDTO;
	}

	
	public Transpoter validateUser(Transpoter formDTO) {
		logger.logDebug("validateUser");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager _QM = new PreparedQueryManager();
		if (!_QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", _QM.getErrorMessage());
			_QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String userid = parsedArgs.nextToken();
			String sqlQuery = "SELECT USERSCD,UNAME from USERS0001 where USER_ID= ? ";
			if (!_QM.prepareStatement(sqlQuery)) {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("result", _QM.getErrorMessage());
				_QM.closeConnection();
				_QM = null;
				return resultDTO;
			}
			PreparedStatement stmt = _QM.getStatement();
			stmt.setString(1, userid);
			if (_QM.executeAndCheckForRowPresent()) {
				resultDTO = _QM.getTranspoter();
				resultDTO.setValue("sucFlg", "1");
			} else {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", "Row Not Present");
			}
		} catch (Exception e) {
			logger.logDebug("validateUser::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			_QM.closeConnection();
			_QM = null;
		}
		return resultDTO;
	}

	public Transpoter validateUserId(Transpoter formDTO) {
		logger.logDebug("validateUserId");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM = new PreparedQueryManager();
		StringBuffer sqlstr = new StringBuffer();
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainid = parsedArgs.nextToken();
			String userid = parsedArgs.nextToken();

			sqlstr = new StringBuffer(
					"SELECT suborgcode, USER_ID,USER_DOB,USER_NAME,ROLE_ID,ADDRESS,TEL_NO,GSM_NO,EMAIL_ID," +
					"REMARKS,EDATE,EUSER,CDATE,CUSER, ADATE,AUSER,CATEGORY,MODULE," +
					"ADMIN_HIER,REGIS_DATE from USERS0001 WHERE TRIM(suborgcode)=? AND TRIM(USER_ID)=?");

			QM.openConnection();
			QM.prepareStatement(sqlstr.toString());
			QM.getStatement().setString(1, domainid);
			QM.getStatement().setString(2, userid);
			logger.logDebug("Query" + sqlstr);
			if (QM.executeQuery()) {
				resultDTO = QM.getTranspoter();
				if (QM.isRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
				} else {
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", "Row Not Present");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateUserId::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}


	public static String valMpgmCode(String ProgramId) {

		String descn = "";
		PreparedQueryManager _QM = new PreparedQueryManager();
		if (!_QM.openConnection()) {
			return ProgramId;
		}
		try {
			String sqlQuery = "SELECT PGM_ID,DESCN FROM MENU001 WHERE PGM_ID = ? ";
			if (!_QM.prepareStatement(sqlQuery)) {
				return ProgramId;
			}
			PreparedStatement statement = _QM.getStatement();
			statement.setString(1, ProgramId);
			if (!_QM.executeQuery()) {
				return ProgramId;
			}
			ResultSet rs = _QM.getResultSet();
			if (rs.next()) {
				descn = rs.getString("DESCN");
			} else {
				descn = ProgramId;
			}
		} catch (SQLException e) {

			return ProgramId;
		} catch (Exception e) {

			return ProgramId;
		} finally {
			_QM.closeConnection();
		}
		return descn;
	}

	

	public Transpoter validateCustType(Transpoter formDTO) {
		logger.logDebug("validateCustType");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM = new PreparedQueryManager();
		StringBuffer sqlstr = new StringBuffer();
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String CUSTTYPE = parsedArgs.nextToken();
			sqlstr = new StringBuffer(
					"SELECT DESCN FROM CUSTTYPE WHERE CUST_TYPE=? AND ENABLED='1'");
			QM.prepareStatement(sqlstr.toString());
			QM.getStatement().setString(1, CUSTTYPE);
			if (QM.executeQuery()) {
				resultDTO = QM.getTranspoter();
				if (QM.isRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
				} else {
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", "Row Not Present");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateCustType::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	public Transpoter validateAccType(Transpoter formDTO) {
		logger.logDebug("validateAccType");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		StringBuffer sqlstr = new StringBuffer();
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String ACCTYPE = parsedArgs.nextToken();
			sqlstr = new StringBuffer(
					"SELECT DESCN FROM ACCTYPE WHERE ACC_TYPE=? AND ENABLED='1'");
			logger.logDebug("Query:" + sqlstr);
			if (QM.prepareStatement(sqlstr.toString())) {
				pstmt = QM.getStatement();
				pstmt.setString(1, ACCTYPE);
				if (QM.executeQuery()) {
					resultDTO = QM.getTranspoter();
					if (QM.isRowPresent() == true) {
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errMsg", "Row Not Present");
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateAccType::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.logDebug("validateAccType::" + e.getLocalizedMessage());
			}
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	public Transpoter validateSLAType(Transpoter formDTO) {
		logger.logDebug("validateSLAType");
		Transpoter resultDTO = new Transpoter();
		QueryManager QM = new QueryManager();
		StringBuffer sqlstr = new StringBuffer();
		if (!QM.openConnection(Con)) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM.getError());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String SLATYPE = parsedArgs.nextToken();
			sqlstr = new StringBuffer(
					"SELECT DESCN FROM SLA_TYPE WHERE SLA_TYPE='" + SLATYPE
							+ "' AND ENABLED='1'");
			logger.logDebug("Query:" + sqlstr);
			if (QM.executeQuery(sqlstr.toString())) {
				resultDTO = QM.getTranspoter();
				if (QM.isRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
				} else {
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", "Row Not Present");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getError());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateSLAType::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	public Transpoter validateAccNo(Transpoter formDTO) {
		logger.logDebug("validateAccNo");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		StringBuffer sqlstr = new StringBuffer();
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String ACCNO = parsedArgs.nextToken();
			sqlstr = new StringBuffer(
					"SELECT ACNTS_NAME,ACNTS_AC_TYPE FROM ACNTS WHERE ACNTS_NO=?");
			logger.logDebug("Query:" + sqlstr);
			if (QM.prepareStatement(sqlstr.toString())) {
				pstmt = QM.getStatement();
				pstmt.setString(1, ACCNO);
				if (QM.executeQuery()) {
					resultDTO = QM.getTranspoter();
					if (QM.isRowPresent() == true) {
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errMsg", "Row Not Present");
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateAccNo::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.logDebug("validateAccNo::" + e.getLocalizedMessage());
			}
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	public Transpoter validateCboReqType(Transpoter formDTO) {
		logger.logDebug("validateCboReqType");
		String sqlQuery;
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			sqlQuery = "SELECT REQTYPE,DESCN FROM REQUEST WHERE REQTYPE=?";
			if (QM.prepareStatement(sqlQuery)) {
				pstmt = QM.getStatement();
				pstmt.setString(1, SqlArgs);
				if (QM.executeQuery()) {
					resultDTO = QM.getTranspoter();
					if (QM.isRowPresent() == true) {
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errMsg", "Row Not Present");
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateCboReqType::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.logDebug("validateCboReqType::"
						+ e.getLocalizedMessage());
			}
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;

	}


	public static Timestamp CheckOutQforReSend(Transpoter formDTO) {
		String sqlQuery;
		String sqlQuery1;
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Timestamp ts = null;

		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM.getErrorMessage());
			QM = null;

		}
		try {

			String FesRef_no = (String) formDTO.getValue("FesRefNo");
			String Req_type = (String) formDTO.getValue("requestType");
			String Message_type = null;

			if (Req_type.equals("STOPCHQ")) {
				Message_type = "350000";
			} else if (Req_type.equals("FUNDTRF")) {
				Message_type = "400000";
			} else if (Req_type.equals("RTGS")) {
				Message_type = "410000";
			} else if (Req_type.equals("NEFT")) {
				Message_type = "420000";
			} else if (Req_type.equals("CUSTREG")) {
				Message_type = "300000";
			} else {
				Message_type = "";
			}

			sqlQuery = "SELECT O.FESOQ_ACK_RECV_DT, O.FESOQ_IQ_MSG_SL FROM FES_OUTQ O WHERE O.FESOQ_FES_REFNO=? AND O.FESOQ_MSG_TYPE=?";
			if (QM.prepareStatement(sqlQuery)) {
				pstmt = QM.getStatement();
				pstmt.setString(1, FesRef_no);
				pstmt.setString(2, Message_type);

				if (QM.executeQuery()) {
					resultDTO = QM.getTranspoter();
					if (QM.isRowPresent() == true) {
						sqlQuery1 = "SELECT R.ORG_TIMESTAMP FROM REQUESTMAIN R WHERE R.FES_REF_NO=?";
						if (QM.prepareStatement(sqlQuery1)) {
							pstmt = QM.getStatement();
							pstmt.setString(1, FesRef_no);
							if (QM.executeQuery()) {
								rs = QM.getResultSet();
								if (rs.next()) {
									rs = QM.getResultSet();
									formDTO.setValue("BooleanFlg", "1");
									resultDTO.setValue("timestamp", resultDTO
											.getValue("ORG_TIMESTAMP"));
									ts = rs.getTimestamp("ORG_TIMESTAMP");
									return ts;

								} else {
									resultDTO.setValue("BooleanFlg", "1");
									ts = new Timestamp(new Date().getTime());
									return ts;
								}
							}
						} else {
							resultDTO.setValue("errMsg", QM.getErrorMessage());
							resultDTO.setValue("sucFlg", "0");
						}
					}

					else {
						ts = new Timestamp(new Date().getTime());
						String query2 = "UPDATE REQUESTMAIN SET ORG_TIMESTAMP=? WHERE FES_REF_NO=?";
						QM.prepareStatement(query2);
						pstmt = QM.getStatement();
						pstmt.setTimestamp(1, ts);
						pstmt.setString(2, FesRef_no);
						pstmt.executeUpdate();
						formDTO.setValue("BooleanFlg", "0");
						resultDTO.setValue("timestamp", ts.toString());
						return ts;
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			QM.closeConnection();
			QM = null;
		}
		return ts;

	}


	public Transpoter valBenfificiaryID(Transpoter formDTO) {
		logger.logDebug("valBenfificiaryID");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainId = parsedArgs.nextToken();
			String benfId = parsedArgs.nextToken();
			String usrId = parsedArgs.nextToken();
			String strSQL;
			String beneftype = parsedArgs.nextToken();

			
			strSQL = " select suborgcode, USER_ID,BENEF_ID,BRN_CODE,REGIS_DATE,COUNTRY,STATE,DISTRICT,CITY_LOCN,ZIP_PIN,BENEF_NAME,ADDRESS1,ADDRESS2,ADDRESS3,ADDRESS4,ADDRESS5,TEL_NO,GSM_NO,EMAIL_ID,ACNT_NO,IFSC_CODE,DEACTIVATED_ON,REASON_FOR_DEACTIVATION,BANK_NAME,EMAIL_ID,ACNT_NO,BENEF_CURR_CODE,BENEF_TYPE,DEACTIVATED "
					+ " from BENEF_REG where suborgcode=? and BENEF_ID=?  and USER_ID=?  ";
			
			if (beneftype.equals("IFTO") )
			strSQL = strSQL + "and  BENEF_TYPE='IFTO'";
			else if (beneftype.equals("RTGSNEFT"))
			strSQL = strSQL + "and  BENEF_TYPE ='RTGSNEFT'"; 
			else
				strSQL = strSQL + "and  BENEF_TYPE IN ('EFT','SWIFT')"; 
			logger.logDebug("Query:" + strSQL);
			if (QM.prepareStatement(strSQL)) {
				pstmt = QM.getStatement();
				pstmt.setString(1, domainId);
				pstmt.setString(2, benfId);
				pstmt.setString(3, usrId);
				if (QM.executeQuery()) {
					resultDTO = QM.getTranspoter();
					if (QM.isRowPresent() == true) {
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errMsg", "Row Not Present");
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("valBenfificiaryID::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger
						.logDebug("valBenfificiaryID::"
								+ e.getLocalizedMessage());
			}
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;

	}


	public Transpoter validateRequestId(Transpoter formDTO) {
		logger.logDebug("validateRequestId");
		String reqtypelifecycle = "";
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager pqm = new PreparedQueryManager();
		PreparedStatement _pstmt = null;
		ResultSet rs = null;
		StringBuffer sqlQuery = new StringBuffer();
		if (!pqm.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", pqm.getErrorMessage());
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");

			sqlQuery = new StringBuffer(
					"SELECT REQTYPE,REQ_LIFE_CYCLE_DEF,DESCN, ADATE,CLASS FROM REQUEST WHERE REQTYPE=?");

			logger.logDebug("Query:" + sqlQuery.toString());
			if (pqm.prepareStatement(sqlQuery.toString())) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, SqlArgs);
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						resultDTO.setValue("REQTYPE", rs.getString("REQTYPE"));
						resultDTO.setValue("REQ_LIFE_CYCLE_DEF", rs
								.getString("REQ_LIFE_CYCLE_DEF"));
						resultDTO.setValue("DESCN", rs.getString("DESCN"));
						resultDTO.setValue("ADATE", rs.getString("ADATE"));
						resultDTO.setValue("CLASS", rs.getString("CLASS"));
						reqtypelifecycle = resultDTO
								.getValue("REQ_LIFE_CYCLE_DEF");
						if (reqtypelifecycle == null) {
							reqtypelifecycle = "0";
						}
						if (!(reqtypelifecycle.equalsIgnoreCase("1"))) {
							resultDTO.setValue("sucFlg", "2");
						}
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("errMsg", "Row NotPresent");
						resultDTO.setValue("sucFlg", "0");
					}
				} else {
					resultDTO.setValue("errMsg", pqm.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", pqm.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateRequestId::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				rs.close();
				_pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger
						.logDebug("validateRequestId::"
								+ e.getLocalizedMessage());
			}
			pqm.closeConnection();
			pqm = null;
		}
		return resultDTO;
	}

	
	public Transpoter valRemitterID(Transpoter formDTO) {
		logger.logDebug("validateRequestId");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainId = parsedArgs.nextToken();
			String benfId = parsedArgs.nextToken();
			String strSQL;

			strSQL = " select suborgcode, REMITTER_ID,REMITTER_DATE,REMITTER_NAME,ADDRESS1,ADDRESS2,ADDRESS3,ADDRESS4,TEL_NO,GSM_NO,EMAIL_ID,ACNT_NO,COUNTRY,STATE,DISTRICT,CITY_LOCN,ZIP_PIN,DEACTIVATED_ON,REASON_FOR_DEACTIVATION,DEALER_CODE "
					+ " from REMITTER_REG where suborgcode=? and REMITTER_ID=? ";

			logger.logDebug("Query:" + strSQL);
			if (QM.prepareStatement(strSQL)) {
				pstmt = QM.getStatement();
				pstmt.setString(1, domainId);
				pstmt.setString(2, benfId);
				if (QM.executeQuery()) {
					resultDTO = QM.getTranspoter();
					if (QM.isRowPresent() == true) {
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errMsg", "Row Not Present");
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateRequestId::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger
						.logDebug("validateRequestId::"
								+ e.getLocalizedMessage());
			}
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;

	}

	

	public Transpoter validatecboSecQuestion(Transpoter formDTO) {
		logger.logDebug("validatecboSecQuestion");
		Transpoter resultDTO = new Transpoter();
		StringBuffer sqlstr = new StringBuffer();
		PreparedQueryManager QM = new PreparedQueryManager();
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			sqlstr = new StringBuffer(
					"select SECURITY_CODE,SECURITY_QUESTION from USER_SECURITY_QUS");
			QM.prepareStatement(sqlstr.toString());
			if (QM.executeAndCheckForRowPresent() == true) {
				resultDTO.setValue("sucFlg", "1");
				resultDTO.setValue("USERSECURITYQUS", QM.getXML());

				String sqlstr1 = "SELECT NO_OF_PROMPTS_CRE_USERID FROM SYSCONF002 WHERE EFF_DATE=(SELECT MAX(EFF_DATE) FROM SYSCONF002 WHERE EFF_DATE <SYSDATE AND ADATE IS NOT NULL)";
				QM.prepareStatement(sqlstr1);
				if (QM.executeQuery()) {
					ResultSet rset = QM.getResultSet();
					if (rset.next()) {
						resultDTO.setValue("sucFlg", "1");
						resultDTO.setValue("NO_OF_SEC_QUESTN", rset
								.getString("NO_OF_PROMPTS_CRE_USERID"));
					} else {
						resultDTO.setValue("NO_OF_SEC_QUESTN", "");
						resultDTO.setValue("sucFlg", "0");
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else if (QM.executeAndCheckForRowPresent() == false) {
				resultDTO.setValue("USERSECURITYQUS", "<rows></rows>");
				resultDTO.setValue("sucFlg", "0");
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("USERSECURITYQUS", "<rows></rows>");
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validatecboSecQuestion::"
					+ e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("USERSECURITYQUS", "<rows></rows>");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	public Transpoter validatecboSecAns(Transpoter formDTO) {
		logger.logDebug("validatecboSecAns");
		Transpoter resultDTO = new Transpoter();
		StringBuffer sqlstr = null;
		PreparedQueryManager QM = new PreparedQueryManager();
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			if (parsedArgs.hasMoreTokens()) {
				String domainid = parsedArgs.nextToken();
				String userid = parsedArgs.nextToken();
				sqlstr = new StringBuffer(
						"SELECT SECURITY_CODE,SECURITY_QUESTION FROM USER_SECURITY_QUS a where a.security_code IN (SELECT SECURITY_CODE FROM USER_SECURITY_ANS WHERE suborgcode=? AND USER_ID=?) order by dbms_random.value");
				QM.prepareStatement(sqlstr.toString());
				PreparedStatement stmt = QM.getStatement();
				stmt.setString(1, domainid);
				stmt.setString(2, userid);
				if (QM.executeAndCheckForRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
					resultDTO.setValue("USERSECURITYQUS", QM.getXML());

					String sqlstr1 = "SELECT NO_OF_PROMPTS_FORGET_PIN FROM SYSCONF002 WHERE EFF_DATE=(SELECT MAX(EFF_DATE) FROM SYSCONF002 WHERE EFF_DATE <SYSDATE AND ADATE IS NOT NULL)";

					QM.prepareStatement(sqlstr1);
					if (QM.executeQuery()) {
						ResultSet rset = QM.getResultSet();
						if (rset.next()) {
							resultDTO.setValue("sucFlg", "1");
							resultDTO.setValue("NO_OF_SEC_ANS", rset
									.getString("NO_OF_PROMPTS_FORGET_PIN"));

						} else {
							resultDTO.setValue("NO_OF_SEC_ANS", "");
							resultDTO.setValue("sucFlg", "0");
						}
					} else {
						resultDTO.setValue("errMsg", QM.getErrorMessage());
						resultDTO.setValue("sucFlg", "0");
					}
				} else if (QM.executeAndCheckForRowPresent() == false) {
					resultDTO.setValue("USERSECURITYQUS", "<rows></rows>");
					resultDTO.setValue("sucFlg", "0");
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("USERSECURITYQUS", "<rows></rows>");
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", "Empty token");
				resultDTO.setValue("USERSECURITYQUS", "<rows></rows>");
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validatecboSecAns::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
			resultDTO.setValue("USERSECURITYQUS", "<rows></rows>");
		} finally {
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	public Transpoter fetchRoleId(Transpoter formDTO) {
		logger.logDebug("fetchRoleId");
		Transpoter resultDTO = new Transpoter();
		Transpoter tempReturnData = new Transpoter();
		StringBuffer sqlstr = null;
		PreparedQueryManager QM = new PreparedQueryManager();
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainid = parsedArgs.nextToken();
			String userid = parsedArgs.nextToken();
			sqlstr = new StringBuffer(
					"SELECT U.ROLE_TYPE,R.USER_NAME FROM USERS003 U,USERS R WHERE R.suborgcode=? AND r.user_id=? AND  U.ROLE_ID =(SELECT S.ROLE_ID from USERS0001 S WHERE S.suborgcode=? AND S.USER_ID=?)");
			QM.prepareStatement(sqlstr.toString());
			QM.getStatement().setString(1, domainid);
			QM.getStatement().setString(2, userid);
			QM.getStatement().setString(3, domainid);
			QM.getStatement().setString(4, userid);

			if (QM.executeQuery()) {
				tempReturnData = QM.getTranspoter();
				if (QM.isRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
					resultDTO.setValue("ROLE_TYPE", tempReturnData
							.getValue("ROLE_TYPE"));
					resultDTO.setValue("USER_NAME", tempReturnData
							.getValue("USER_NAME"));
				} else if (QM.isRowPresent() == false) {
					resultDTO.setValue("USERSECURITYQUS", "<rows></rows>");
					resultDTO.setValue("sucFlg", "0");
				} else {
					resultDTO.setValue("errMsg", tempReturnData
							.getValue("Result"));
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("fetchRoleId::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	private DataSource dataSource = null;
	private Connection connDB = null;
	private CallableStatement stmt = null;
	private ResultSet rsGeneric = null;
	private String errMsg;

	public boolean openConnection()
	{
		try 
		{
			dataSource = PreparedQueryManager.getDataSource();
			connDB = dataSource.getConnection();
		}
		catch (SQLException e) {
			errMsg = e.getLocalizedMessage();
			return false;
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

	public Transpoter FetchSyscpm(Transpoter formDTO) {
		logger.logDebug("FetchSyscpm");
		Transpoter resultDTO = new Transpoter();
		Transpoter tempReturnData = new Transpoter();
		StringBuffer sqlstr = new StringBuffer();
		PreparedQueryManager QM = new PreparedQueryManager();
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {

			sqlstr = new StringBuffer(
					"SELECT MINUSERLENGTH,MINPASSLENGTH,MIN_NUM_PWD,MINALPHACHAR,PREVENT_PREV_PWD FROM SYSCONF002 where eff_date=(select max(eff_date) FROM SYSCONF002 where eff_date <sysdate AND AUSER IS NOT NULL)");

			QM.prepareStatement(sqlstr.toString());
			if (QM.executeQuery()) {
				tempReturnData = QM.getTranspoter();
				if (QM.isRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
					resultDTO.setValue("MinUserId", tempReturnData
							.getValue("MINUSERLENGTH"));
					resultDTO.setValue("MinPwd", tempReturnData
							.getValue("MINPASSLENGTH"));
					resultDTO.setValue("MinNumPwd", tempReturnData
							.getValue("MIN_NUM_PWD"));
					resultDTO.setValue("MinAlphaPwd", tempReturnData
							.getValue("MINALPHACHAR"));
					resultDTO.setValue("MinPrevPwd", tempReturnData
							.getValue("PREVENT_PREV_PWD"));
				} else if (QM.isRowPresent() == false) {
					resultDTO.setValue("sucFlg", "0");
				} else {
					resultDTO.setValue("errMsg", tempReturnData
							.getValue("Result"));
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("FetchSyscpm::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	public Transpoter updateLogout(Transpoter formDTO) {
		logger.logDebug("updateLogout");
		Transpoter resultDTO = new Transpoter();
		QueryManager QM = new QueryManager();
		StringBuffer sqlstr = new StringBuffer();
		if (!QM.openConnection(Con)) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", QM.getError());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainid = parsedArgs.nextToken();
			String userid = parsedArgs.nextToken();
			String cbd = parsedArgs.nextToken();
			java.sql.Timestamp ts = new java.sql.Timestamp(System
					.currentTimeMillis());
			String current_time = "";
			String dateString = "";
			dateString = ts.toString();
			current_time = dateString.substring(11, dateString.indexOf("."));
			logger.logDebug("Current Time:" + current_time);
			String time = cbd + current_time;
			logger.logDebug("Time:" + time);
			sqlstr = new StringBuffer(
					"INSERT INTO USERS009(suborgcode,ULHST_USER_ID,ULHST_IN_DATE) VALUES('"
							+ domainid + "','" + userid + "',TO_TIMESTAMP('"
							+ time + "','DD-MM-RR HH24:MI:SS'))");
			logger.logDebug("Query" + sqlstr);
			if (QM.executeQuery(sqlstr.toString())) {
				resultDTO = QM.getTranspoter();
				if (QM.isRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
				} else {
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", "Row Not Insert");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getError());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("updateLogout::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	public Transpoter fetchTabData(Transpoter formDTO) {
		logger.logDebug("fetchTabData");
		String path = "/INTOUCHDATA//";
		Transpoter resultDTO = new Transpoter();
		int recCount = 0;
		BufferedReader bufferedReader = null;
		String record = "";
		String note = "";
		String subtitle = "";
		String Abspath = "";
		String pth = "";

		try {
			int SqlArgs;
			SqlArgs = Integer.parseInt(formDTO.getValue("Args"));
			if (SqlArgs == 1) {
				pth = path + "intouchonline.txt";
				subtitle = " intouch Online";
			} else if (SqlArgs == 2) {
				pth = path + "cashmgt.txt";
				subtitle = " Cash Management";
			} else if (SqlArgs == 6) {
				pth = path + "commodities.txt";
				subtitle = " Commodities";
			} else if (SqlArgs == 5) {
				pth = path + "globalremit.txt";
				subtitle = " Global Remittance";
			} else if (SqlArgs == 4) {
				pth = path + "scm.txt";

				subtitle = " Supply Chain Finance";
			} else if (SqlArgs == 3) {
				pth = path + "trade.txt";
				subtitle = " Trade";
			} else if (SqlArgs == 7) {
				pth = path + "capitalmkt.txt";
				subtitle = " Capital Market";
			}
			else if (SqlArgs == 98) {
				pth = path + "assistance.txt";
			} else if (SqlArgs == 99) {
				pth = path + "news.txt";
			}
			File myFile = new File(pth);
			Abspath = myFile.getAbsolutePath();

			FileInputStream fileInputStream = new FileInputStream(myFile);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					fileInputStream);
			bufferedReader = new BufferedReader(new InputStreamReader(
					bufferedInputStream));

			logger.logDebug("File read from:" + Abspath);
			while ((record = bufferedReader.readLine()) != null) {

				note = note + "\n" + record;
			}
			note = "<![CDATA[" + note + "]]>";
			resultDTO.setValue("note", note);
			resultDTO.setValue("subtitle", subtitle);

		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("fetchTabData::" + e.getLocalizedMessage());

		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
			} catch (Exception e) {
				e.printStackTrace();
				logger.logDebug("fetchTabData::" + e.getLocalizedMessage());
			}
		}
		return resultDTO;
	}

	public Transpoter validateBenfID(Transpoter formDTO) {
		logger.logDebug("validateBenfID");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager pqm = new PreparedQueryManager();
		PreparedStatement _pstmt = null;
		ResultSet rs = null;
		if (!pqm.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", pqm.getErrorMessage());
		}
		try {
			String SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainId = parsedArgs.nextToken();
			String benfId = parsedArgs.nextToken();
			String strSQL = " SELECT  B.suborgcode,B.BENEF_ID,B.REGIS_DATE,B.COUNTRY,B.STATE,B.DISTRICT,B.CITY_LOCN,B.ZIP_PIN,B.BENEF_NAME,B.ADDRESS1,B.ADDRESS2,B.ADDRESS3,B.ADDRESS4,B.TEL_NO,B.GSM_NO,B.EMAIL_ID,B.ACNT_NO,B.IFSC_CODE,B.DEACTIVATED_ON,B.REASON_FOR_DEACTIVATION,B.BANK_NAME, I.BRANCH_NAME, I.BRANCH_ADDRESS "
					+ " FROM BENEF_REG B,IFSCODES I WHERE B.suborgcode=? AND B.BENEF_ID=? AND B.IFSC_CODE = I.IFSC_CODE";
			if (pqm.prepareStatement(strSQL)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, domainId);
				_pstmt.setString(2, benfId);
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						resultDTO.setValue("SUBORGCODE", rs
								.getString("SUBORGCODE"));
						resultDTO
								.setValue("BENEF_ID", rs.getString("BENEF_ID"));
						resultDTO.setValue("REGIS_DATE", rs
								.getString("REGIS_DATE"));
						resultDTO.setValue("COUNTRY", rs.getString("COUNTRY"));
						resultDTO.setValue("STATE", rs.getString("STATE"));
						resultDTO
								.setValue("DISTRICT", rs.getString("DISTRICT"));
						resultDTO.setValue("CITY_LOCN", rs
								.getString("CITY_LOCN"));
						resultDTO.setValue("ZIP_PIN", rs.getString("ZIP_PIN"));
						resultDTO.setValue("BENEF_NAME", rs
								.getString("BENEF_NAME"));
						resultDTO
								.setValue("ADDRESS1", rs.getString("ADDRESS1"));
						resultDTO
								.setValue("ADDRESS2", rs.getString("ADDRESS2"));
						resultDTO
								.setValue("ADDRESS3", rs.getString("ADDRESS3"));
						resultDTO
								.setValue("ADDRESS4", rs.getString("ADDRESS4"));
						resultDTO.setValue("TEL_NO", rs.getString("TEL_NO"));
						resultDTO.setValue("GSM_NO", rs.getString("GSM_NO"));
						resultDTO
								.setValue("EMAIL_ID", rs.getString("EMAIL_ID"));
						resultDTO.setValue("ACNT_NO", rs.getString("ACNT_NO"));
						resultDTO.setValue("IFSC_CODE", rs
								.getString("IFSC_CODE"));
						resultDTO.setValue("DEACTIVATED_ON", rs
								.getString("DEACTIVATED_ON"));
						resultDTO.setValue("REASON_FOR_DEACTIVATION", rs
								.getString("REASON_FOR_DEACTIVATION"));
						resultDTO.setValue("BANK_NAME", rs
								.getString("BANK_NAME"));
						resultDTO.setValue("BRANCH_NAME", rs
								.getString("BRANCH_NAME"));
						resultDTO.setValue("BRANCH_ADDRESS", rs
								.getString("BRANCH_ADDRESS"));
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("errMsg", "Row NotPresent");
						resultDTO.setValue("sucFlg", "0");
					}
				} else {
					resultDTO.setValue("errMsg", pqm.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", pqm.getErrorMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateBenfID::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				rs.close();
				_pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.logDebug("validateBenfID::" + e.getLocalizedMessage());
			}
			pqm.closeConnection();
			pqm = null;
		}
		return resultDTO;
	}

	public Transpoter displayNotes(Transpoter formDTO) {
		logger.logDebug("displayNotes");
		Transpoter resultDTO = new Transpoter();
		String fesnum = (String) formDTO.getValue("Args");
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", pqm.getErrorMessage());
		}
		try {
			String strSQL;
			strSQL = "select NOTES_MARK_REPAIR from REQUESTMAIN where FES_REF_NO=? and REPAIR_MARK_ON IS NOT NULL";
			PreparedStatement _pstmt = null;
			if (pqm.prepareStatement(strSQL)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, fesnum);
				if (pqm.executeQuery()) {
					ResultSet rs = pqm.getResultSet();
					if (rs.next()) {
						resultDTO.setValue("NOTES_MARK_REPAIR", rs
								.getString("NOTES_MARK_REPAIR"));
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("errMsg", "Row NotPresent");
						resultDTO.setValue("sucFlg", "0");
					}
				}
			} else {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", pqm.getErrorMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("displayNotes::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			pqm.closeConnection();
			pqm = null;
		}
		return resultDTO;
	}


	public Transpoter validateProgramIdmodule(Transpoter formDTO) {
		logger.logDebug("validateProgramIdmodule");
		Transpoter resultDTO = new Transpoter();
		QueryManager QM = new QueryManager();
		StringBuffer sqlstr = new StringBuffer();
		if (!QM.openConnection(Con)) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", QM.getError());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");

			sqlstr = new StringBuffer(
					"select PGM_ID,DESCN,PGM_CLASS  FROM MENU001 where PGM_ID='"
							+ SqlArgs
							+ "'and AUSER IS NOT NULL and ADATE IS NOT NULL"); 

			logger.logDebug("Query:" + sqlstr);
			if (QM.executeQuery(sqlstr.toString())) {
				resultDTO = QM.getTranspoter();
				if (QM.isRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
					QM.closeConnection();
					QM = null;
					return resultDTO;
				} else {
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", "Row Not Present");
					QM.closeConnection();
					QM = null;
					return resultDTO;
				}
			} else {
				resultDTO.setValue("errMsg", QM.getError());
				resultDTO.setValue("sucFlg", "0");
			}
			QM.closeConnection(Con);
			QM = null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateProgramIdmodule::"
					+ e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
			QM.closeConnection();
			QM = null;
			return resultDTO;
		}
		return resultDTO;
	}

	
	public Transpoter validatecboservicepayment(Transpoter formDTO) {
		logger.logDebug("validatecboservicepayment");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String module = parsedArgs.nextToken();
			String dominid = parsedArgs.nextToken();
			String userid = parsedArgs.nextToken();	
			String sqlstr = " SELECT R.PGM_ID,R.DESCN,(SELECT L.description FROM cm_lovrec L WHERE L.lov_key1=? AND l.lov_key2=? AND L.lov_value=?) "
					+ " FROM REQUEST R WHERE R.PGM_ID IN (SELECT MP.PGM_ID FROM MENU001 MP WHERE MP.MODULE=? AND MP.SUB_MODULE=? AND MP.PGM_WIDGET=1 AND MP.PGM_ID IN ( "
					+ " SELECT PD.PGM_ID FROM MENU003 PD WHERE PD.ENABLED=? AND PD.ROLE_ID IN (SELECT AD.ROLE_ID FROM USERS005 AD WHERE AD.suborgcode=? AND AD.USER_ID=? AND "
					+ " AD.EFF_DATE=(SELECT MAX(A.EFF_DATE) FROM USERS004 A WHERE A.suborgcode=AD.suborgcode AND A.USER_ID=AD.USER_ID AND A.ADATE IS NOT NULL AND A.AUSER "
					+ " IS NOT NULL AND A.EFF_DATE<=SYSDATE) UNION SELECT U.ROLE_ID from USERS0001 U WHERE U.suborgcode=? AND U.USER_ID=?) AND PD.EFF_DATE=( "
					+ " SELECT MAX(P.EFF_DATE) FROM MENU002 P WHERE P.ROLE_ID=PD.ROLE_ID AND P.ADATE IS NOT NULL AND P.AUSER IS NOT NULL AND P.EFF_DATE<=SYSDATE)))";
			if (QM.prepareStatement(sqlstr)) {
				pstmt = QM.getStatement();
				pstmt.setString(1, "PGM");
				pstmt.setString(2, "MODULE");
				pstmt.setString(3, module);
				pstmt.setString(4, module);
				pstmt.setString(5, "1");
				pstmt.setString(6, "1");
				pstmt.setString(7, dominid);
				pstmt.setString(8, userid);
				pstmt.setString(9, dominid);
				pstmt.setString(10, userid);
				if (QM.executeQuery()) {
					DTDObject dtdObj = new DTDObject();
					dtdObj = QM.getDTDObject();
					if (QM.isRowPresent() == true) {
						resultDTO.setValue("sucFlg", "1");
						resultDTO.setValue("CMLOV", dtdObj.getXML());
					} else if (QM.isRowPresent() == false) {
						resultDTO.setValue("CMLOV", "<rows></rows>");
						resultDTO.setValue("sucFlg", "0");
					} else {
						resultDTO.setValue("errMsg", QM.getErrorMessage());
						resultDTO.setValue("CMLOV", "<rows></rows>");
						resultDTO.setValue("sucFlg", "0");
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("CMLOV", "<rows></rows>");
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("CMLOV", "<rows></rows>");
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validatecboservicepayment::"
					+ e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("CMLOV", "<rows></rows>");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;
	}

	public Transpoter validateRoleId(Transpoter formDTO) {
		logger.logDebug("validateRoleId");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager manager = new PreparedQueryManager();
		StringBuffer sqlstr = null;
		if (!manager.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", manager.getErrorMessage());
			manager = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");

			sqlstr = new StringBuffer(
					"SELECT ROLE_ID,DESCN FROM USERS003 WHERE ROLE_ID=?");
			manager.prepareStatement(sqlstr.toString());
			manager.getStatement().setString(1, SqlArgs);
			logger.logDebug("Query:" + sqlstr);
			if (manager.executeQuery()) {
				resultDTO = manager.getTranspoter();
				if (manager.isRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
				} else {
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", "Row Not Present");
				}
			} else {
				resultDTO.setValue("errMsg", manager.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateRoleId::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			manager.closeConnection();
			manager = null;
		}
		return resultDTO;
	}

	
	public Transpoter validatecboAdmin(Transpoter formDTO) {
		logger.logDebug("validatecboAdmin");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager manager = new PreparedQueryManager();
		StringBuffer sqlstr = new StringBuffer();
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String Value = parsedArgs.nextToken();
			String dominid = parsedArgs.nextToken();
			String userid = parsedArgs.nextToken();
			String DATE1 = parsedArgs.nextToken();
			String st;

			st = "SELECT d.menu_header, f.menu_descn, d.pgm_id, m.descn,(SELECT L.description FROM cm_lovrec L WHERE L.lov_key1='PGM' AND l.lov_key2='MODULE'AND L.lov_value=M.module)"
					+ "FROM MENU006 d, MENU005 f,mpgm m WHERE f.menu_header =? and  d.MENU_HEADER=? and d.menu_header= f.menu_header AND M.PGM_WIDGET=1 "
					+ " and m.pgm_id= d.pgm_id AND d.pgm_id in (select pd.pgm_id from MENU003 PD where "
					+ " (PD.ROLE_ID    IN (SELECT UR.ROLE_ID from USERS0001 UR WHERE UR.suborgcode=? AND UR.USER_ID    =?)"
					+ " OR PD.ROLE_ID IN "
					+ " (SELECT ROLE_ID FROM USERS005 WHERE suborgcode=? AND USER_ID=? AND EFF_DATE=(SELECT MAX(EFF_DATE) FROM "
					+ " USERACTROLES WHERE suborgcode=? AND USER_ID=? AND ADATE IS NOT NULL AND AUSER IS NOT NULL AND EFF_DATE <=TO_DATE(?, 'DD-MM-YYYY')) AND ENABLED='1')) "
					+

					"AND PD.ENABLED = '1' AND PD.RIGHTS_AUTH = '0' AND PD.EFF_DATE    = (SELECT MAX(PE.EFF_DATE) FROM MENU003 PE WHERE "
					+ " (PE.ROLE_ID    IN (SELECT UR.ROLE_ID from USERS0001 UR WHERE UR.suborgcode=? AND UR.USER_ID    =?)"
					+ " OR PE.ROLE_ID IN "
					+ " (SELECT ROLE_ID FROM USERS005 WHERE suborgcode=? AND USER_ID=? AND EFF_DATE=(SELECT MAX(EFF_DATE) FROM "
					+ " USERACTROLES WHERE suborgcode=? AND USER_ID=? AND ADATE IS NOT NULL AND AUSER IS NOT NULL AND EFF_DATE<=TO_DATE(?, 'DD-MM-YYYY')) AND ENABLED='1')) "
					+ " AND PE.PGM_ID     =Pd.pgm_id"
					+ " AND PE.ENABLED = '1' "
					+ " AND PE.EFF_DATE=(SELECT MAX(X.EFF_DATE) FROM MENU002 X  WHERE X.ROLE_ID=PE.ROLE_ID AND X.AUSER IS NOT NULL AND X.ADATE IS NOT NULL AND EFF_DATE <= TO_DATE(?, 'DD-MM-YYYY')"
					+ "))) ORDER BY DESCN";

			logger.logDebug("Query:" + st);
			sqlstr = new StringBuffer(st);
			Transpoter Transpoter = new Transpoter();
			Transpoter.setValue("strSQL", sqlstr.toString());
			manager.openConnection();
			manager.prepareStatement(sqlstr.toString());
			manager.getStatement().setString(1, Value);
			manager.getStatement().setString(2, Value);
			manager.getStatement().setString(3, dominid);
			manager.getStatement().setString(4, userid);
			manager.getStatement().setString(5, dominid);
			manager.getStatement().setString(6, userid);
			manager.getStatement().setString(7, dominid);
			manager.getStatement().setString(8, userid);
			manager.getStatement().setString(9, DATE1);
			manager.getStatement().setString(10, dominid);
			manager.getStatement().setString(11, userid);
			manager.getStatement().setString(12, dominid);
			manager.getStatement().setString(13, userid);
			manager.getStatement().setString(14, dominid);
			manager.getStatement().setString(15, userid);
			manager.getStatement().setString(16, DATE1);
			manager.getStatement().setString(17, DATE1);

			if (manager.executeAndCheckForRowPresent()) {
				if (manager.isRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
					resultDTO.setValue("CMLOV", manager.getXML());
				} else if (manager.isRowPresent() == false) {
					resultDTO.setValue("CMLOV", "<rows></rows>");
					resultDTO.setValue("sucFlg", "0");
				} else {
					resultDTO.setValue("errMsg", "Error");
					resultDTO.setValue("CMLOV", "<rows></rows>");
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", manager.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("CMLOV", "<rows></rows>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validatecboAdmin::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("CMLOV", "<rows></rows>");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			manager.closeConnection();
			manager = null;
		}
		return resultDTO;
	}

	public Transpoter validateDomainId2(Transpoter formDTO) {
		logger.logDebug("validateDomainId2");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager manager = new PreparedQueryManager();
		StringBuffer sqlstr = new StringBuffer();
		if (!manager.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", manager.getErrorMessage());
			manager = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			sqlstr = new StringBuffer(
					"SELECT DATE_OF_REG,DOMAIN_NAME,ADDRESS,CONTACT_PERSON,TELEPHONE,MOBILE,EMAILID,EDATE,EUSER,CDATE,CUSER, ADATE,AUSER FROM SYSCONF004 WHERE suborgcode=?");
			logger.logDebug("Query" + sqlstr);
			manager.prepareStatement(sqlstr.toString());
			manager.getStatement().setString(1, SqlArgs);
			if (manager.executeQuery()) {
				resultDTO = manager.getTranspoter();
				if (manager.isRowPresent()) {
					resultDTO.setValue("sucFlg", "1");
				} else {
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", "Row Not Present");
				}
			} else {
				resultDTO.setValue("errMsg", manager.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateDomainId2::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			manager.closeConnection();
			manager = null;
		}
		return resultDTO;
	}

	public Transpoter validateSecAnswer(Transpoter formDTO) {
		logger.logDebug("validateSecAnswer");
		Transpoter resultDTO = new Transpoter();
		Transpoter outputDTO = new Transpoter();
		QueryManager QM = new QueryManager();
		String W_COLSEP = "Æ";
		String W_ROWSEP = "û";
		String grid = null;
		if (!QM.openConnection(Con)) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", QM.getError());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainid = parsedArgs.nextToken();
			String userid = parsedArgs.nextToken();
			String cboSecQustn1 = parsedArgs.nextToken();
			String cboSecQustn2 = parsedArgs.nextToken();
			String cboSecQustn3 = parsedArgs.nextToken();
			String cboSecQustn4 = parsedArgs.nextToken();
			String cboSecQustn5 = parsedArgs.nextToken();
			String hidPwd = parsedArgs.nextToken();
			String hidPassword = parsedArgs.nextToken();
			String hidPwd3 = parsedArgs.nextToken();
			String hidPwd4 = parsedArgs.nextToken();
			String hidPwd5 = parsedArgs.nextToken();
			if (!cboSecQustn1.equals("0")) {
				if (grid == null)
					grid = cboSecQustn1 + W_COLSEP + hidPwd + W_ROWSEP;
				else
					grid = grid + cboSecQustn1 + W_COLSEP + hidPwd + W_ROWSEP;
			}
			if (!cboSecQustn2.equals("0")) {
				if (grid == null)
					grid = cboSecQustn2 + W_COLSEP + hidPassword + W_ROWSEP;
				else
					grid = grid + cboSecQustn2 + W_COLSEP + hidPassword
							+ W_ROWSEP;
			}
			if (!cboSecQustn3.equals("0")) {
				if (grid == null)
					grid = cboSecQustn3 + W_COLSEP + hidPwd3 + W_ROWSEP;
				else
					grid = grid + cboSecQustn3 + W_COLSEP + hidPwd3 + W_ROWSEP;
			}

			if (!cboSecQustn4.equals("0")) {
				if (grid == null)
					grid = cboSecQustn4 + W_COLSEP + hidPwd4 + W_ROWSEP;
				else
					grid = grid + cboSecQustn4 + W_COLSEP + hidPwd4 + W_ROWSEP;
			}

			if (!cboSecQustn5.equals("0")) {
				if (grid == null)
					grid = cboSecQustn5 + W_COLSEP + hidPwd5 + W_ROWSEP;
				else
					grid = grid + cboSecQustn5 + W_COLSEP + hidPwd5 + W_ROWSEP;
			}
			logger.logDebug("Grid:" + grid);
			if (openConnection()) {
				rsGeneric = null;
				if (connDB == null) {
					openConnection();
				}
				stmt = connDB
						.prepareCall("call SP_FORGOT_PASSWORD(?,?,?,?,?,?,?)");
				stmt.setString(1, domainid);
				stmt.setString(2, userid);
				stmt.setString(3, grid);
				stmt.registerOutParameter(4, Types.VARCHAR);
				stmt.registerOutParameter(5, Types.NUMERIC);
				stmt.registerOutParameter(6, Types.NUMERIC);
				stmt.registerOutParameter(7, Types.VARCHAR);
				stmt.execute();

				if (stmt.getString(4).equals("S")
						&& stmt.getInt(5) == stmt.getInt(6)) {
					formDTO.setValue("Args", domainid.trim() + "|"
							+ userid.trim());
					logger.logDebug("Args:" + formDTO.getValue("Args"));
					outputDTO = fetchRoleId(formDTO);
					logger.logDebug("Roletype:"
							+ outputDTO.getValue("ROLE_TYPE"));

					resultDTO.setValue("cbd", stmt.getString(7));
					logger
							.logDebug("CURRENT BUSINESS DATE"
									+ stmt.getString(7));
					resultDTO.setValue("userName", outputDTO
							.getValue("USER_NAME"));
					resultDTO.setValue("sucFlg", "2");
					resultDTO
							.setValue("result",
									" Pin was generated and successfully sent to your mail id");

				} else if (stmt.getString(4).equals("S")) {
					resultDTO.setValue("sucFlg", "1");
				} else {
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", "Invalid Security Code");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateSecAnswer::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			QM.closeConnection();
			QM = null;
			closeConnection();
		}
		return resultDTO;
	}

	public Transpoter insertAlert(Transpoter formDTO) {
		logger.logDebug("insertAlert");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager _QM = new PreparedQueryManager();
		StringBuffer sqlstr = new StringBuffer();
		if (!_QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", _QM.getErrorMessage());
			_QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			int pwd_flg = 0;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainid = parsedArgs.nextToken();
			String userid = parsedArgs.nextToken();
			String pwd = parsedArgs.nextToken();
			String cbd = parsedArgs.nextToken();
			String programid = parsedArgs.nextToken();
			String sessdomainid = parsedArgs.nextToken();
			System.out.println(pwd);
			String sqlQuery = "SELECT AUTH_REQUIRED FROM MENU001 WHERE PGM_ID= ? ";
			if (!_QM.prepareStatement(sqlQuery)) {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", _QM.getErrorMessage());
				_QM.closeConnection();
				return resultDTO;
			}
			PreparedStatement pstmt = _QM.getStatement();
			pstmt.setString(1, programid);
			if (_QM.executeQuery()) {
				resultDTO = _QM.getTranspoter();
			}
			String auth = resultDTO.getValue("AUTH_REQUIRED");

			String sqlQuery1 = " SELECT PWD_GEN_BY_EMAIL,PWD_GEN_BY_MAILER FROM SYSCONF005 WHERE PWD_GEN_BY_DOMAIN_ID=? AND PWD_GEN_EFFECTIVE_DATE=(SELECT MAX(PWD_GEN_EFFECTIVE_DATE) FROM SYSCONF005 WHERE PWD_GEN_BY_DOMAIN_ID=? AND PWD_GEN_EFFECTIVE_DATE<=SYSDATE)";

			if (!_QM.prepareStatement(sqlQuery1)) {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", _QM.getErrorMessage());
				_QM.closeConnection();
				return resultDTO;
			}
			PreparedStatement pstmt1 = _QM.getStatement();
			pstmt1.setString(1, domainid);

			pstmt1.setString(2, domainid);
			if (_QM.executeQuery()) {
				resultDTO = _QM.getTranspoter();
			}

			if (resultDTO.getValue("PWD_GEN_BY_EMAIL").equals("1")
					&& resultDTO.getValue("PWD_GEN_BY_MAILER").equals("1")) {
				pwd_flg = 3;
				resultDTO.setValue("sucFlg", "1");
			} else if (resultDTO.getValue("PWD_GEN_BY_MAILER").equals("1")) {
				pwd_flg = 2;
				resultDTO.setValue("sucFlg", "1");
			} else if (resultDTO.getValue("PWD_GEN_BY_EMAIL").equals("1")) {
				pwd_flg = 1;
				resultDTO.setValue("sucFlg", "1");
			} else {
				pwd_flg = 0;
				resultDTO.setValue("sucFlg", "0");
			}
			if (pwd_flg == 1 || pwd_flg == 3 || pwd_flg == 2) {

				sqlstr = new StringBuffer(
						"INSERT INTO EVENTEXECUTOR (suborgcode,USER_ID,ALERT_GEN_DATE,ALERT_FOR_DIREC,FES_REF_NO,CONTENT,PORTALLP_DEL,FWD_ACTION,BWD_INFO,EVENT_ID,USER_TYPE,PASSWORD_GEN_MTH)VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
				if (!_QM.prepareStatement(sqlstr.toString())) {
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", _QM.getErrorMessage());
					_QM.closeConnection();
					return resultDTO;
				}
				PreparedStatement stmt = _QM.getStatement();
				stmt.setString(1, domainid);
				stmt.setString(2, userid);
				java.sql.Timestamp ts = new java.sql.Timestamp(System
						.currentTimeMillis());
				stmt.setTimestamp(3, ts);
				stmt.setString(4, "F");
				stmt.setString(5, "999999999999999");
				stmt.setString(6, pwd);
				if (auth.equalsIgnoreCase("1")) {
					stmt.setInt(7, 1);
				} else {
					stmt.setInt(7, 0);
				}
				stmt.setString(8, "");
				stmt.setString(9, "");
				stmt.setString(10, "A01");
				stmt.setString(11, "U");
				stmt.setInt(12, pwd_flg);
				if (stmt.executeUpdate() == 1) {
					resultDTO.setValue("sucFlg", "1");
				} else {
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", _QM.getErrorMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("insertAlert::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			_QM.closeConnection();
			_QM = null;
		}
		return resultDTO;
	}

	public Transpoter checkAccountAllowed(Transpoter formDTO) {
		logger.logDebug("checkAccountAllowed");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", pqm.getErrorMessage());
		}
		try {
			String accNum = (String) formDTO.getValue("accNum");
			String reqType = (String) formDTO.getValue("reqType"); 
			String sqlQuery = "SELECT ENABLED FROM ACCTYPEREQ WHERE ACC_TYPE IN (SELECT ACNTS_AC_TYPE FROM ACNTS WHERE ACNTS_NO=?) AND REQ_TYPE=?";
			PreparedStatement _pstmt = null;
			if (pqm.prepareStatement(sqlQuery)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, accNum);
				_pstmt.setString(2, reqType);
				if (pqm.executeQuery()) {
					ResultSet rs = pqm.getResultSet();
					if (rs.next()) {
						System.out
								.println("in checkAccountAllowed ENABLED is : "
										+ rs.getString("ENABLED"));
						resultDTO.setValue("ENABLED", rs.getString("ENABLED"));
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("errMsg", "Row Not Present");
						resultDTO.setValue("sucFlg", "0");
					}
				} else {
					resultDTO.setValue("errMsg", pqm.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", pqm.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("checkAccountAllowed::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getMessage());
		} finally {
			pqm.closeConnection();
			pqm = null;
		}
		return resultDTO;
	}

	public Transpoter fetchRequestTypePassingPgmId(Transpoter formDTO) {
		logger.logDebug("fetchRequestTypePassingPgmId");
		Transpoter resultDTO = new Transpoter();
		resultDTO.clearMap();
		DynaSQL dynaSQL = new DynaSQL("");
		try {
			String pgmId = (String) formDTO.getValue("pgmId");
			if (!dynaSQL.openConnection()) {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("result", " Updation Failed");
				resultDTO.setValue("errMsg", dynaSQL.getError());
				dynaSQL = null;
				return resultDTO;
			}
			String reqType = dynaSQL.getRequestType(pgmId);
			resultDTO.setValue("REQTYPE", reqType);
			resultDTO.setValue("sucFlg", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("fetchRequestTypePassingPgmId::"
					+ e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			dynaSQL.closeConnection();
			dynaSQL = null;
		}
		return resultDTO;
	}

	public Transpoter validate_userpph(Transpoter formDTO) {
		logger.logDebug("validate_userpph");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager manager = new PreparedQueryManager();
		StringBuffer sqlstr = new StringBuffer();
		try {
			String SqlArgs = (String) formDTO.getValue("Args").trim();
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainid = parsedArgs.nextToken();
			String userid = parsedArgs.nextToken();

			if (!SqlArgs.equals(" ")) {
				resultDTO.setValue("sucFlg", "1");
				sqlstr = new StringBuffer(
						"SELECT * FROM USERS006 U WHERE U.suborgcode=? AND U.userpph_user_id=?");
				manager.openConnection();
				manager.prepareStatement(sqlstr.toString());
				manager.getStatement().setString(1, domainid);
				manager.getStatement().setString(2, userid);
				if (manager.executeQuery()) {
					resultDTO = manager.getTranspoter();
					if (manager.isRowPresent()) {
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errMsg", manager.getErrorMessage());
					}
				} else {
					resultDTO.setValue("errMsg", manager.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validate_userpph::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			manager.closeConnection();
		}
		return resultDTO;
	}

	public static Transpoter validateAccountNumber(Transpoter formDTO) {

		Transpoter resultDTO = new Transpoter();
		resultDTO.clearMap();
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errFlg", "1");
			resultDTO.setValue("errMsg", pqm.getErrorMessage());
		}
		try {

			String domainId = formDTO.getValue("sesDomainID");
			String userId = formDTO.getValue("sesUserId");
			String accountNumber = (String) formDTO.getValue("accountNumber");
			String requestType = (String) formDTO.getValue("requestType");

			String sqlQuery = " SELECT ROWNUM, Q.ENABLED REQ_ALLOWED,DECODE(B.AC_STATUS,' ',1,0) REG_AC, NVL(D.ACNT_LINK_ENABLED,0) ENABLED, DECODE(D.ACNT_LINK_NATURE,2,1,0) NATURE,A.ACNTS_NAME,A.ACNTS_AC_CURRENCY,A.ACNTS_CUST_NO "
					+ " FROM DOMAINACREGDTL D, ACNTS A, ACBAL B, ACCTYPEREQ Q,ACCTYPE Z WHERE D.suborgcode=? AND D.USER_ID=? "
					+ " AND D.ACNT_NO=? AND D.CLIENT_NO IN (SELECT R.CLIENT_NO FROM DOMAINACREG R WHERE R.suborgcode=D.suborgcode AND R.USER_ID=D.USER_ID AND R.LINK_ENABLED='1' "
					+ " AND R.ADATE IS NOT NULL AND R.AUSER IS NOT NULL) AND A.ACNTS_NO=D.ACNT_NO AND A.ACNTS_NO=B.ACNT_NO "
					+ " AND Q.ACC_TYPE=A.ACNTS_AC_TYPE AND Q.REQ_TYPE=? AND A.ACNTS_AC_TYPE=Z.ACC_TYPE AND Z.ENABLED='1'";
			PreparedStatement _pstmt = null;
			if (pqm.prepareStatement(sqlQuery)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, domainId);
				_pstmt.setString(2, userId);
				_pstmt.setString(3, accountNumber);
				_pstmt.setString(4, requestType);
				if (pqm.executeQuery()) {
					ResultSet rs = pqm.getResultSet();
					if (rs.next()) {
						if (rs.getString("REQ_ALLOWED").equals("1")) {
							if (rs.getString("REG_AC").equals("1")) {
								if (rs.getString("ENABLED").equals("1")) {
									if (rs.getString("NATURE").equals("1")) {
										resultDTO.setValue("sucFlg", "1");
										resultDTO.setValue("ACNTS_NAME", rs
												.getString("ACNTS_NAME"));
										resultDTO
												.setValue(
														"ACNTS_AC_CURRENCY",
														rs
																.getString("ACNTS_AC_CURRENCY"));
										resultDTO.setValue("ACNTS_CUST_NO", rs
												.getString("ACNTS_CUST_NO"));
									} else {
										resultDTO.setValue("sucFlg", "0");
										resultDTO.setValue("errFlg", "6");
									}
								} else {
									resultDTO.setValue("sucFlg", "0");
									resultDTO.setValue("errFlg", "5");
								}

							} else {
								resultDTO.setValue("sucFlg", "0");
								resultDTO.setValue("errFlg", "3");
							}
						} else {
							resultDTO.setValue("sucFlg", "0");
							resultDTO.setValue("errFlg", "2");
						}
					} else {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errFlg", "1");
					}
				} else {
					System.out.println("Error in validateAccountNumber :"
							+ pqm.getErrorMessage());
					resultDTO.setValue("errMsg", pqm.getErrorMessage());
					resultDTO.setValue("errFlg", "1");
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				System.out.println("Error in validateAccountNumber :"
						+ pqm.getErrorMessage());
				resultDTO.setValue("errMsg", pqm.getErrorMessage());
				resultDTO.setValue("errFlg", "1");
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errFlg", "1");
			resultDTO.setValue("errMsg", e.getMessage());
			System.out.println("Exception in validateAccountNumber :"
					+ e.getMessage());
		} finally {
			pqm.closeConnection();
			pqm = null;
		}
		return resultDTO;
	}

	public static Transpoter validateAccountNumberWithoutRequestType(
			Transpoter formDTO) throws SQLException {
		Transpoter resultDTO = new Transpoter();
		resultDTO.clearMap();
		PreparedQueryManager pqm = new PreparedQueryManager();
		PreparedStatement _pstmt = null;
		ResultSet rs = null;
		if (!pqm.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errFlg", "1");
			resultDTO.setValue("errMsg", pqm.getErrorMessage());
		}
		try {
			String domainId = (String) formDTO.getValue("domainId");
			String userId = (String) formDTO.getValue("userId");
			String accountNumber = (String) formDTO.getValue("accountNumber");

			String sqlQuery = " SELECT ROWNUM, DECODE(A.ACNTS_AC_CURRENCY,'INR',1,1) CURR_ALLOWED, NVL(D.ACNT_LINK_ENABLED,0) ENABLED, NVL(D.ACNT_LINK_NATURE,0) NATURE "
					+ " FROM DOMAINACREGDTL D, ACNTS A, ACBAL B,ACCTYPE C WHERE D.suborgcode=? AND D.USER_ID=? "
					+ " AND D.ACNT_NO=? AND D.CLIENT_NO IN (SELECT R.CLIENT_NO FROM DOMAINACREG R WHERE R.suborgcode=D.suborgcode AND R.USER_ID=D.USER_ID AND R.LINK_ENABLED='1' "
					+ "	AND R.ADATE IS NOT NULL AND R.AUSER IS NOT NULL) AND A.ACNTS_NO=D.ACNT_NO AND A.ACNTS_NO=B.ACNT_NO  AND A.ACNTS_AC_TYPE=C.ACC_TYPE AND C.ENABLED='1'";

			if (pqm.prepareStatement(sqlQuery)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, domainId);
				_pstmt.setString(2, userId);
				_pstmt.setString(3, accountNumber);
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						if (rs.getString("ENABLED").equals("1")) {
							System.out
									.println("Valid Account Number without Request Type");
							resultDTO.setValue("sucFlg", "1");
						} else {
							System.out
									.println("InValid Account Number without Request Type");
							resultDTO.setValue("sucFlg", "0");
						}
					} else {
						resultDTO.setValue("sucFlg", "0");
					}
				} else {
					System.out
							.println("Error in validateAccountNumberWithoutRequestType :"
									+ pqm.getErrorMessage());
					resultDTO.setValue("errMsg", pqm.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				System.out
						.println("Error in validateAccountNumberWithoutRequestType :"
								+ pqm.getErrorMessage());
				resultDTO.setValue("errMsg", pqm.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {

			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getMessage());
			System.out
					.println("Exception in validateAccountNumberWithoutRequestType :"
							+ e.getMessage());
		} finally {
			_pstmt.close();
			rs.close();
			pqm.closeConnection();
			pqm = null;
		}
		return resultDTO;
	}

	public static Transpoter validateFesReferenceNumber(Transpoter formDTO)
			throws SQLException {
		Transpoter resultDTO = new Transpoter();
		resultDTO.clearMap();
		PreparedQueryManager pqm = new PreparedQueryManager();
		PreparedStatement _pstmt = null;
		ResultSet rs = null;
		if (!pqm.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errFlg", "1");
			resultDTO.setValue("errMsg", pqm.getErrorMessage());
		}
		try {
			String domainId = (String) formDTO.getValue("domainId");
			String userId = (String) formDTO.getValue("userId");
			String fesRefNumber = (String) formDTO.getValue("fesRefNumber");
			String sqlQuery = " SELECT * FROM REQUESTMAIN R WHERE R.suborgcode=? AND R.ENTRY_MODE=? AND R.EUSER=? AND R.REQUEST_STATUS IN (?,?,?) AND R.FES_REF_NO=?";
			if (pqm.prepareStatement(sqlQuery)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, domainId);
				_pstmt.setString(2, Transpoter.ENTRY_MODE_MANUAL);
				_pstmt.setString(3, userId);
				_pstmt.setString(4, Transpoter.REQUEST_STATUS_REPAIR);
				_pstmt.setString(5, Transpoter.REQUEST_STATUS_NEW);
				_pstmt.setString(6, Transpoter.REQUEST_STATUS_REPAIRED);
				_pstmt.setString(7, fesRefNumber);
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						System.out.println("Fes Reference Number Exists");
						resultDTO.setValue("sucFlg", "1");
					} else {
						System.out.println("Invalid Fes Reference Number");
						resultDTO.setValue("sucFlg", "0");
					}
				} else {
					System.out.println("Error in validateFesReferenceNumber :"
							+ pqm.getErrorMessage());
					resultDTO.setValue("errMsg", pqm.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				System.out.println("Error in validateFesReferenceNumber :"
						+ pqm.getErrorMessage());
				resultDTO.setValue("errMsg", pqm.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {

			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getMessage());
			System.out.println("Exception in validateFesReferenceNumber :"
					+ e.getMessage());
		} finally {
			_pstmt.close();
			rs.close();
			pqm.closeConnection();
			pqm = null;
		}
		return resultDTO;
	}

	public Transpoter update_RequestMain(String fesnum, String respcode,
			String statusFlag, String equationRefNumber) {
		logger.logDebug("update_RequestMain");
		DynaSQL requestMain = new DynaSQL("REQUESTMAIN");
		try {
			if (!requestMain.openConnection()) {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", requestMain.getError());
				requestMain = null;
			}
			requestMain
					.setFiled("FES_REF_NO", FormatUtils.getLongValue(fesnum));
			requestMain.New(false);
			requestMain.setFiled("OTH_SYS_STATUS", respcode);
			requestMain.setFiled("OTH_SYS_RESPONSE_NO", equationRefNumber);
			requestMain.setFiled("REQUEST_STATUS", statusFlag);
			String keyfields = "FES_REF_NO";
			if (requestMain.save(keyfields) == false) {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("result", " Updation Failed");
				resultDTO.setValue("errMsg", requestMain.getError());
			} else {
				logger
						.logDebug(" Response Code and equationRefNumber updated into REQUESTMAIN");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("update_RequestMain::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", " Updation Failed");
		} finally {
			requestMain.closeConnection();
			requestMain = null;
		}
		return resultDTO;
	}

	public void callSP_FES_UPD_REQSTAT(PreparedQueryManager pqm, String fesnum,
			String statusFlag, String oth_sys_status, String oth_sys_response)
			throws SQLException {
		logger.logDebug("callSP_FES_UPD_REQSTAT");
		try {
			connDB = pqm.getConnection();
			stmt = connDB.prepareCall("call SP_FES_UPD_REQSTAT(?,?,?,?)");
			stmt.setString(1, fesnum);
			stmt.setString(2, statusFlag);
			stmt.setString(3, oth_sys_status);
			stmt.setString(4, oth_sys_response);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.logDebug("callSP_FES_UPD_REQSTAT::"
					+ e.getLocalizedMessage());
		} finally {
			stmt.close();
			stmt = null;
		}
	}

	public static String fetchUserNameFromUsers(String userId, String domainId)
			throws SQLException {

		String userName = "";
		PreparedStatement _pstmt = null;
		ResultSet rs = null;
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			pqm = null;
			userName = userId;
		}
		try {
			String sqlStr = "select USER_NAME from USERS0001 where USER_ID=? and suborgcode=?";
			if (pqm.prepareStatement(sqlStr)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, userId);
				_pstmt.setString(2, domainId);
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						userName = rs.getString("USER_NAME");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			userName = userId;
		} finally {
			pqm.closeConnection();
			rs.close();
			rs = null;
			_pstmt.close();
			_pstmt = null;
		}
		return userName;
	}

	public Transpoter valBenfificiaryID1(Transpoter formDTO) {
		logger.logDebug("valBenfificiaryID1");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM1 = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		if (!QM1.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM1.getErrorMessage());
			QM1 = null;
			return resultDTO;
		}
		try {
			String SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainId = parsedArgs.nextToken();
			logger.logDebug("Domainid:" + domainId);
			String benfId = parsedArgs.nextToken();
			logger.logDebug("Beneficiaryid" + benfId);
			String strSQL;
			strSQL = " SELECT '',R.DESCN,B.ENABLED,'','',B.BANK_NAME,B.IFSC_CODE,B.ACNT_NO  FROM BENEF_REG_REQ B,REQUEST R WHERE B.suborgcode=? AND B.BENEF_ID=? AND R.REQTYPE=B.REQTYPE";
			logger.logDebug("Query:" + strSQL);
			if (QM1.prepareStatement(strSQL)) {
				pstmt = QM1.getStatement();
				pstmt.setString(1, domainId);
				pstmt.setString(2, benfId);
				if (QM1.executeAndCheckForRowPresent() == true) {
					resultDTO.setValue("sucFlg", "1");
					resultDTO.setValue("GRIDVAL", QM1.getXML());
				} else if (QM1.isRowPresent() == false) {
					resultDTO.setValue("GRIDVAL", "<rows></rows>");
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", "Row Not Present");
				} else {
					resultDTO.setValue("errMsg", "Error");
					resultDTO.setValue("GRIDVAL", "<rows></rows>");
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM1.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("valBenfificiaryID1::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.logDebug("valBenfificiaryID1::"
						+ e.getLocalizedMessage());
			}
			QM1.closeConnection();
			QM1 = null;
		}
		return resultDTO;
	}

	public Transpoter validate_countrycode(Transpoter formDTO) {
		logger.logDebug("validate_countrycode");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager pqm = new PreparedQueryManager();
		StringBuffer sqlstr = new StringBuffer();
		if (!pqm.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("result", pqm.getErrorMessage());
		}
		try {
			sqlstr = new StringBuffer("SELECT TRIM(callingcode) FROM sysconf001"); 
			PreparedStatement _pstmt = null;
			if (pqm.prepareStatement(sqlstr.toString())) {
				_pstmt = pqm.getStatement();
				if (pqm.executeQuery()) {
					ResultSet rs = pqm.getResultSet();
					if (rs.next()) {
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errMsg", "Does not exists");
					}
				} else {
					resultDTO.setValue("errMsg", pqm.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", pqm.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validate_countrycode::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			pqm.closeConnection();
			pqm = null;
		}
		return resultDTO;
	}

	public Transpoter validateDomainId(Transpoter formDTO) {
		logger.logDebug("validateDomainId");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager manager = new PreparedQueryManager();
		String authdate = null;
		String authdiss = null;
		StringBuffer sqlstr = null;
		if (!manager.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", manager.getErrorMessage());
			manager = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			sqlstr = new StringBuffer(
					"SELECT DATE_OF_REG,DOMAIN_NAME,ADDRESS,CONTACT_PERSON,TELEPHONE,MOBILE,EMAILID,DISSOLVE_DATE,CATEGORY,EDATE,EUSER,CDATE,CUSER, ADATE,AUSER FROM SYSCONF004 WHERE suborgcode= ? ");
			manager.openConnection();
			manager.prepareStatement(sqlstr.toString());
			manager.getStatement().setString(1, SqlArgs);
			logger.logDebug("Query" + sqlstr);
			if (manager.executeQuery()) {
				resultDTO = manager.getTranspoter();
				if (manager.isRowPresent() == true) {
					authdate = resultDTO.getValue("ADATE");
					authdiss = resultDTO.getValue("DISSOLVE_DATE");
					if (authdiss != null) {
						resultDTO.setValue("sucFlg", "2");
					} else if (authdate == null) {
						resultDTO.setValue("sucFlg", "3");
					} else {
						resultDTO.setValue("sucFlg", "1");
					}
				} else {
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", "Row Not Present");
				}
			} else {
				resultDTO.setValue("errMsg", manager.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("validateDomainId::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			manager.closeConnection();
			manager = null;
		}
		return resultDTO;
	}

	public Transpoter valAuthStatus(String programid) {
		logger.logDebug("valAuthStatus");
		Transpoter resultDTO = new Transpoter();
		resultDTO.setValue("AUTH_REQUIRED", "0");
		PreparedQueryManager _QM = new PreparedQueryManager();
		try {
			if (!_QM.openConnection()) {
				logger.logDebug(_QM.getErrorMessage());
				_QM = null;
				return resultDTO;
			}
			String sqlQuery = "SELECT AUTH_REQUIRED FROM MENU001 WHERE PGM_ID=?";
			_QM.prepareStatement(sqlQuery);
			PreparedStatement stmt = _QM.getStatement();
			stmt.setString(1, programid);
			if (_QM.executeQuery()) {
				ResultSet rs = _QM.getResultSet();
				if (rs.next()) {
					resultDTO.setValue("AUTH_REQUIRED", rs
							.getString("AUTH_REQUIRED"));
				}
			} else {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", _QM.getErrorMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("valAuthStatus::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			_QM.closeConnection();
			_QM = null;
		}
		return resultDTO;
	}

	public Transpoter FetchBeneDet(Transpoter formDTO) {
		logger.logDebug("FetchBeneDet");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager manager = new PreparedQueryManager();
		resultDTO.setValue("sucFlg", "0");
		StringBuffer sqlstr = null;
		if (!manager.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", manager.getErrorMessage());
			manager = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String fesrefno = parsedArgs.nextToken();
			String domainId = parsedArgs.nextToken();
			sqlstr = new StringBuffer(
					"SELECT BENEF_ID,BENEF_NAME,REGIS_DATE FROM BENEF_REG WHERE UPLOAD_FES_REF_NO= ? AND suborgcode = ? ORDER BY BENEF_ID");
			manager.openConnection();
			manager.prepareStatement(sqlstr.toString());
			manager.getStatement().setString(1, fesrefno);
			manager.getStatement().setString(2, domainId);
			if (manager.executeQuery()) {
				resultDTO.setValue("GridData", manager.getXML());
				resultDTO.setValue("sucFlg", "1");
			} else {
				resultDTO.setValue("errMsg", manager.getErrorMessage());
				resultDTO.setValue("GridData", "<rows></rows>");
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("FetchBeneDet::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			manager.closeConnection();
			manager = null;
		}
		return resultDTO;
	}

	public Transpoter FetchRemitDet(Transpoter formDTO) {
		logger.logDebug("FetchRemitDet");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager manager = new PreparedQueryManager();
		resultDTO.setValue("sucFlg", "0");
		StringBuffer sqlstr = null;
		if (!manager.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", manager.getErrorMessage());
			manager = null;
			return resultDTO;
		}
		try {
			String SqlArgs;
			SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String fesrefno = parsedArgs.nextToken();
			String domainId = parsedArgs.nextToken();
			sqlstr = new StringBuffer(
					"SELECT REMITTER_ID,REMITTER_NAME,REMITTER_DATE FROM REMITTER_REG WHERE UPLOAD_FES_REF_NO= ? AND suborgcode = ? ORDER BY REMITTER_ID");
			manager.openConnection();
			manager.prepareStatement(sqlstr.toString());
			manager.getStatement().setString(1, fesrefno);
			manager.getStatement().setString(2, domainId);
			if (manager.executeQuery()) {
				resultDTO.setValue("GridData", manager.getXML());
				resultDTO.setValue("sucFlg", "1");
			} else {
				resultDTO.setValue("errMsg", manager.getErrorMessage());
				resultDTO.setValue("GridData", "<rows></rows>");
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("FetchRemitDet::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			manager.closeConnection();
			manager = null;
		}
		return resultDTO;
	}

	public static Date fetchUserLoginTime(String userId, String domainId)
			throws SQLException {
		java.util.Date userlogintime = null;
		PreparedStatement _pstmt = null;
		ResultSet rs = null;
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			pqm = null;
			userlogintime = null;
		}
		try {
			String sqlStr = "SELECT ULOGIN_IN_DATE FROM USERS008  WHERE ULOGIN_USER_ID = ? AND suborgcode =?";
			if (pqm.prepareStatement(sqlStr)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, userId);
				_pstmt.setString(2, domainId);
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						userlogintime = rs.getTimestamp("ULOGIN_IN_DATE");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			userlogintime = null;
		} finally {
			pqm.closeConnection();
			rs.close();
			rs = null;
			_pstmt.close();
			_pstmt = null;
		}
		return userlogintime;
	}

	public static void validateAccNoToUserId(Transpoter formDTO)
			throws SQLException {
		PreparedStatement _pstmt = null;
		ResultSet rs = null;
		Transpoter resultDTO = new Transpoter();
		resultDTO.setValue("sucFlg", "0");
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			pqm = null;
		} else {
			try {
				String sqlStr = "SELECT '1' FROM DOMAINACREGDTL DA, ACNTS AC WHERE DA.ACNT_NO = AC.ACNTS_NO AND DA.CLIENT_NO = AC.ACNTS_CUST_NO "
						+ "AND DA.suborgcode = ? AND DA.USER_ID = ? AND DA.ACNT_NO = ? AND DA.CLIENT_NO = ?";
				if (pqm.prepareStatement(sqlStr)) {
					_pstmt = pqm.getStatement();
					_pstmt.setString(1, formDTO.getValue(Constants.SUBORGCODE));
					_pstmt.setString(2, formDTO.getValue(Constants.USER_ID));
					_pstmt
							.setString(3, formDTO
									.getValue(Constants.ACNT_NUMBER));
					_pstmt.setString(4, formDTO.getValue(Constants.CUST_NO));
					if (pqm.executeQuery()) {
						rs = pqm.getResultSet();
						if (rs.next()) {
							resultDTO.setValue("sucFlg", "1");
						} else {
							resultDTO.setValue("sucFlg", "0");
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				resultDTO.setValue("errMsg", pqm.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			} finally {
				_pstmt.close();
				_pstmt = null;
				pqm.closeConnection();
				pqm = null;
			}
		}
	}
	
	public Transpoter getbushrs(Transpoter formDTO) {
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager pqm = new PreparedQueryManager();
		if(!pqm.openConnection()){
			resultDTO.setValue("sucFlg","0") ;
			resultDTO.setValue("result",pqm.getErrorMessage()) ;
			pqm = null;
			return resultDTO;
		}
		try{
			try {
				String reqtype = formDTO.getValue("reqType");
				String cbd = (String) formDTO.getValue("cbd");
				connDB = pqm.getConnection();
				stmt= connDB.prepareCall("call SP_FES_HOLIDAY_WINTIME_VAL(?,?,?,?)");
				stmt.setString(1,reqtype);
				stmt.setString(2,cbd); 
				stmt.registerOutParameter(3, Types.VARCHAR);
				stmt.registerOutParameter(4, Types.VARCHAR);
				stmt.execute();
				if(!stmt.getString(3).equals("S")){	
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", stmt.getString(4).toString());
				}
				else{
					resultDTO.setValue("sucFlg", "1");
				}
			} catch (SQLException e) {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", e.getLocalizedMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		}finally{
			pqm.closeConnection(connDB);
			pqm = null;
		}
		return resultDTO;
	}
	
	public Transpoter getMobdigit(Transpoter formDTO) {
		logger.logDebug("getMobdigit");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager manager = new PreparedQueryManager();
		StringBuffer sqlstr = new StringBuffer();
		try {
			String SqlArgs = (String) formDTO.getValue("Args");
			if (!SqlArgs.equals(" ")) {
				sqlstr = new StringBuffer("SELECT COUNTRYCC_NOD FROM COUNTRYCC WHERE COUNTRYCC_NCODE=?");
				manager.openConnection();
				manager.prepareStatement(sqlstr.toString());
				manager.getStatement().setString(1, SqlArgs);
				if (manager.executeQuery()) {
					ResultSet rs = manager.getResultSet();
					if (rs.next()) {
						resultDTO.setValue("MOBDIGIT", rs.getString("COUNTRYCC_NOD"));
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errMsg", "2");
					}
				} else {
					resultDTO.setValue("errMsg", manager.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			logger.logDebug("getMobdigit::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			manager.closeConnection();
			manager = null;
		}

		return resultDTO;

	}
	public Transpoter valBenfificiaryACC(Transpoter formDTO) {
		logger.logDebug("valBenfificiaryACC");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager QM = new PreparedQueryManager();
		PreparedStatement pstmt = null;
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", QM.getErrorMessage());
			QM = null;
			return resultDTO;
		}
		try {
			String SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainId = parsedArgs.nextToken();
			String usrId = parsedArgs.nextToken();
			String accno = parsedArgs.nextToken();
			String ifsc = parsedArgs.nextToken().toUpperCase();
			String strSQL;
			String beneftype = parsedArgs.nextToken();

			
			strSQL = " SELECT ACNT_NO FROM BENEF_REG B WHERE B.suborgcode =? AND B.USER_ID =? AND B.ACNT_NO=? ";
			
			if (beneftype.equals("IFTO") )
			strSQL = strSQL + "and  BENEF_TYPE='IFTO'";
		
			else
				strSQL = strSQL + "AND B.IFSC_CODE= ? and  BENEF_TYPE='RTGSNEFT'";
			logger.logDebug("Query:" + strSQL);
			if (QM.prepareStatement(strSQL)) {
				pstmt = QM.getStatement();
				pstmt.setString(1, domainId);
				pstmt.setString(2, usrId);
				pstmt.setString(3, accno);
				if (!beneftype.equals("IFTO") )
				{
					pstmt.setString(4, ifsc);
				}
				if (QM.executeQuery()) {
					resultDTO = QM.getTranspoter();
					if (QM.isRowPresent() == true) {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errMsg", "Already Exist");
					} else {
						resultDTO.setValue("sucFlg", "1");
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", QM.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.logDebug("valBenfificiaryACC::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger
						.logDebug("valBenfificiaryACC::"
								+ e.getLocalizedMessage());
			}
			QM.closeConnection();
			QM = null;
		}
		return resultDTO;

	}
		
	public  Transpoter fetchCurrentTime(Transpoter formDTO)
	throws SQLException {
		Transpoter resultDTO = new Transpoter();
		String currentTime = null;
		PreparedStatement _pstmt = null;
		ResultSet rs = null;
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			pqm = null;
			currentTime = null;
		}
		try {
			String sqlStr = "select to_char(sysdate, 'HH24:MI:SS') as CURRENT_TIME from dual";
			if (pqm.prepareStatement(sqlStr)) {
				_pstmt = pqm.getStatement();
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						currentTime = rs.getString("CURRENT_TIME");
					}
				}
			}
			resultDTO.setValue("currentTime", currentTime);
		} catch (SQLException e) {
			e.printStackTrace();
			currentTime = null;
		} finally {
			pqm.closeConnection();
			rs.close();
			rs = null;
			_pstmt.close();
			_pstmt = null;
		}
		return resultDTO;
		}
	public static Transpoter validateAccountWithBenefacc(Transpoter formDTO) {
			Transpoter resultDTO = new Transpoter();
			resultDTO.clearMap();
			PreparedQueryManager QM = new PreparedQueryManager();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			if (!QM.openConnection()) {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errFlg", "1");
				resultDTO.setValue("errMsg", QM.getErrorMessage());
			}
			try {
				String domainId = (String) formDTO.getValue("domainId");
				String userId = (String) formDTO.getValue("userId");
				String DebacntNumber = (String) formDTO.getValue("DebacntNumber");
				String BenfAccNum = (String) formDTO.getValue("BenfAccNum");
				String ReqType = (String) formDTO.getValue("ReqType");
				if(ReqType.equals("NEFT") || ReqType.equals("RTGS")){
					ReqType="RTGSNEFT";
				}

				String sqlQuery ="SELECT * FROM( "
								+" SELECT DD.ACNT_NO AS DebitAcc,DD.ACNT_LINK_NATURE, BR.ACNT_NO as BenefAcc,BR.BENEF_TYPE FROM DOMAINACREGDTL DD,BENEF_REG BR WHERE "
								+" DD.suborgcode=BR.suborgcode AND DD.USER_ID=BR.USER_ID AND DD.ACNT_LINK_ENABLED=1 AND DD.ACNT_LINK_NATURE=2 "
								+" AND BR.suborgcode=? AND BR.USER_ID=? AND DD.ACNT_NO=? AND BR.ACNT_NO=? "
								+" UNION SELECT D.ACNT_NO as DebitAcc,D.ACNT_LINK_NATURE,?  as BenefAcc,'IFTO' AS BENEF_TYPE FROM DOMAINACREGDTL D WHERE D.suborgcode=? AND D.USER_ID=? "
								+" AND D.ACNT_LINK_ENABLED=1 AND D.ACNT_NO=? )A WHERE A.BENEF_TYPE=? ";
		if (QM.prepareStatement(sqlQuery)) {
					pstmt = QM.getStatement();
					pstmt.setString(1, domainId);
					pstmt.setString(2, userId);
					pstmt.setString(3, DebacntNumber);
					pstmt.setString(4, BenfAccNum);
					pstmt.setString(5, BenfAccNum);
					pstmt.setString(6, domainId);
					pstmt.setString(7, userId);
					pstmt.setString(8, BenfAccNum);
					pstmt.setString(9, ReqType);
					if (QM.executeQuery()) {
						resultDTO = QM.getTranspoter();
						if (QM.isRowPresent()){
							resultDTO.setValue("sucFlg", "1");
						}else {
						resultDTO.setValue("errMsg", QM.getErrorMessage());
						resultDTO.setValue("sucFlg", "0");
					}
				} else {
					resultDTO.setValue("errMsg", QM.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
						}
				}
					}catch (Exception e) {
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", e.getMessage());
				System.out.println("Exception in validateAccountWithBenefacc :"	+ e.getMessage());
			} finally {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				QM.closeConnection();
				QM = null;
			}
			return resultDTO;
	}
	
			
 public JsonObject getCurrency() throws SQLException
 {
	 JsonObject details = new JsonObject();
	 
 // Transpoter resultDTO = new Transpoter();
  String baseCurrency = null;
  PreparedStatement _pstmt = null;
  ResultSet rs = null;
  PreparedQueryManager pqm = new PreparedQueryManager();
  
  if (!pqm.openConnection())
  {
   pqm = null;
   baseCurrency = null;
  }
  
  try 
  {
   String sqlStr = "SELECT TRIM(currencycd) FROM sysconf001";
   
   if (pqm.prepareStatement(sqlStr))
   {
    _pstmt = pqm.getStatement();
    if (pqm.executeQuery())
    {
     rs = pqm.getResultSet();
     if (rs.next()) 
     {
      baseCurrency = rs.getString("currencycd");
     }
    }
   }
   
  details.addProperty("BASE_CURR_CODE", baseCurrency);
  details.addProperty("Result",  "Success");
  
  } 
  catch (SQLException e) 
  {
   e.printStackTrace();
   baseCurrency = null;
   
   details.addProperty("BASE_CURR_CODE", "null");
   details.addProperty("Result",  "Success");
  } 
  finally {
   pqm.closeConnection();
   rs.close();
   rs = null;
   _pstmt.close();
   _pstmt = null;
  }
  return details;
 }
	public Transpoter PendingRecordChk(Transpoter formDTO){
	logger.logDebug("PendingRecordChk");
	Transpoter resultDTO = new Transpoter();
	resultDTO.clearMap();
	PreparedQueryManager pqm = new PreparedQueryManager();
	if(!pqm.openConnection()){
		resultDTO.setValue("sucFlg","0") ;
		resultDTO.setValue("result",pqm.getErrorMessage()) ;
	}
	try{
		String main_pk = (String) formDTO.getValue("Args");
		String pgmid= (String) formDTO.getValue("pgmID");
		String sqlQuery = "SELECT * FROM AUTH001 	 WHERE AUTHQ_PGM_ID=? AND AUTHQ_MAIN_PK=?";
		PreparedStatement _pstmt = null;
		if (pqm.prepareStatement(sqlQuery)) {
			_pstmt = pqm.getStatement();
			_pstmt.setString(1, pgmid);
			_pstmt.setString(2,main_pk);
			if (pqm.executeQuery()) {
				resultDTO = pqm.getTranspoter();
					if (pqm.isRowPresent()){
						resultDTO.setValue("sucFlg", "1");
					}else {
					resultDTO.setValue("errMsg", pqm.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("errMsg", pqm.getErrorMessage());
				resultDTO.setValue("sucFlg", "0");
					}
			}
	} catch (Exception e) {
		logger.logDebug("PendingRecordChk::"+e.getLocalizedMessage());
		resultDTO.setValue("sucFlg", "0");
		resultDTO.setValue("errMsg", e.getLocalizedMessage());
	}finally{
		pqm.closeConnection();
		pqm = null;
	}
	return resultDTO;
	}
		public Transpoter maskContactDetails(Transpoter formDTO) {
		logger.logDebug("maskContactDetails");
		Transpoter resultDTO = new Transpoter();
        CallableStatement cstmt = null;
        PreparedQueryManager QM = new PreparedQueryManager();
        PreparedStatement ps = null;
        ResultSet rs = null;
		String MobNum="";
		String emailId="";
		if (!QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errFlg", "1");
			resultDTO.setValue("errMsg", QM.getErrorMessage());
		}
		try {
			String SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainid = parsedArgs.nextToken();
			String userid = parsedArgs.nextToken();
			if (!SqlArgs.equals(" ")) {
				String m_Query = "SELECT MOBNUM ('"+domainid+"','"+userid+"') FROM DUAL";
				String m_Query1 = "SELECT emailid ('"+domainid+"','"+userid+"') FROM DUAL";
				logger.logDebug("Query:" + m_Query);
				if (QM.prepareStatement(m_Query)) {
					ps = QM.getStatement();
					if (QM.executeQuery()) {
						rs = QM.getResultSet();
						if (rs.next()) {
							 MobNum = rs.getString(1);
							 resultDTO.setValue("MobNum", MobNum);
							 resultDTO.setValue("sucFlg", "1");
						}
					}
				} else {
				resultDTO.setValue("sucFlg", "0");
			}
				if (QM.prepareStatement(m_Query1)) {
					ps = QM.getStatement();
					if (QM.executeQuery()) {
						rs = QM.getResultSet();
						if (rs.next()) {
							emailId = rs.getString(1);
							 resultDTO.setValue("emailId", emailId);
							 resultDTO.setValue("sucFlg", "1");
						}
					}
				} else {
				resultDTO.setValue("sucFlg", "0");
			}
			} else {
				resultDTO.setValue("sucFlg", "0");
			}
			
		} catch (Exception e) {
			logger.logDebug("maskContactDetails::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			cstmt = null;
		}

		return resultDTO;
	  	}
		
	public Transpoter getAccountCurr(Transpoter formDTO) throws SQLException{
		Transpoter resultDTO = new Transpoter();
		String accCurrency = null;
		PreparedStatement _pstmt = null;
		ResultSet rs = null;
		PreparedQueryManager pqm = new PreparedQueryManager();
		if (!pqm.openConnection()) {
			pqm = null;
			accCurrency = null;
		}
		try {
			String accNo= (String) formDTO.getValue("accNo");
			String sqlStr = "SELECT ACNTS_AC_CURRENCY FROM ACNTS WHERE ACNTS_NO = ?";
			if (pqm.prepareStatement(sqlStr)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, accNo);
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						accCurrency = rs.getString("ACNTS_AC_CURRENCY");
					}
				}
			}
			resultDTO.setValue("ACNTS_AC_CURRENCY", accCurrency);
		} catch (SQLException e) {
			e.printStackTrace();
			accCurrency = null;
		} finally {
			pqm.closeConnection();
			rs.close();
			rs = null;
			_pstmt.close();
			_pstmt = null;
		}
		return resultDTO;
	}
	public Transpoter validateUserStatus(Transpoter formDTO) {
		logger.logDebug("validateUserStatus");
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager manager = new PreparedQueryManager();
		StringBuffer sqlstr = new StringBuffer();
		try {
			String SqlArgs = (String) formDTO.getValue("Args");
			StringTokenizer parsedArgs = new StringTokenizer(SqlArgs, "|");
			String domainid = parsedArgs.nextToken();
			String userid = parsedArgs.nextToken();
			if (!SqlArgs.equals(" ")) {
				sqlstr = new StringBuffer("SELECT DISTINCT(STATUS_FLAG) FROM USERS002 WHERE suborgcode=? AND USER_ID=? AND "
                 + "status_date =( SELECT MAX(status_date) FROM USERS002 WHERE suborgcode=? AND USER_ID=? AND status_date <= sysdate )");
				manager.openConnection();
				manager.prepareStatement(sqlstr.toString());
				manager.getStatement().setString(1, domainid);
				manager.getStatement().setString(2, userid);
				manager.getStatement().setString(3, domainid);
				manager.getStatement().setString(4, userid);
				if (manager.executeQuery()) {
					ResultSet rs = manager.getResultSet();
					if (rs.next()) {
						resultDTO.setValue("STATUS_FLAG", rs.getString("STATUS_FLAG"));
						resultDTO.setValue("sucFlg", "1");
					} else {
						resultDTO.setValue("sucFlg", "0");
						resultDTO.setValue("errMsg", "2");
					}
				} else {
					resultDTO.setValue("errMsg", manager.getErrorMessage());
					resultDTO.setValue("sucFlg", "0");
				}
			} else {
				resultDTO.setValue("sucFlg", "0");
			}
		} catch (Exception e) {
			logger.logDebug("validateUserStatus::" + e.getLocalizedMessage());
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
		} finally {
			manager.closeConnection();
			manager = null;
		}

		return resultDTO;
	}
	
	
	public Transpoter checkOTPRequired(Transpoter formDTO){
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager pqm = new PreparedQueryManager();
		resultDTO.setValue("sucFlg","0");
		try
		{
			pqm.openConnection();
			connDB = pqm.getConnection();
			stmt= connDB.prepareCall("call SP_FES_CHKOTPREQ(?,?,?,?,?)");
			stmt.setString(1,formDTO.getValue("domainid"));
			stmt.setString(2,formDTO.getValue("userid"));
			stmt.setString(3,formDTO.getValue("request"));			
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.registerOutParameter(5, Types.VARCHAR);
			stmt.execute();
			if(stmt.getString(4).equals("S")){	
				resultDTO.setValue("sucFlg", "1");
				String retres = stmt.getString(5).toString(); 
				StringTokenizer parsedArgs = new StringTokenizer(retres, "|");
				resultDTO.setValue("REQ_AUTHPWD_ENABLE", parsedArgs.nextToken());
				resultDTO.setValue("REQ_OTP_ENABLE", parsedArgs.nextToken());
				resultDTO.setValue("REQ_OTP_SERIAL", parsedArgs.nextToken());			
			}
		}
		catch(Exception ex)
		{
			logger.logDebug("CheckOTPRequired::"+ex.getLocalizedMessage());
			resultDTO.setValue("errMsg","No Request Type");
		}
		finally
		{
			if(pqm!=null)
			{
				pqm.closeConnection();
				pqm=null;
				connDB=null;
			}
		}
		return resultDTO;
	}

	 
	
	 
	private void  GenerateSecureRandomNumber(int digits) {
	 
	 
	    try {
		SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
		byte[] randomBytes = new byte[128];
		secureRandomGenerator.nextBytes(randomBytes);
		int seedByteCount = 5;
		byte[] seed = secureRandomGenerator.generateSeed(seedByteCount);
		SecureRandom secureRandom1 = SecureRandom.getInstance("SHA1PRNG");
		secureRandom1.setSeed(seed);
		SecureRandom secureRandom2 = SecureRandom.getInstance("SHA1PRNG");
		secureRandom2.setSeed(seed);
	    } catch (NoSuchAlgorithmException e) {
	    }
	 
	  }
	
	
	
	private int generatePin(int digits){
		logger.logDebug("generatePin()::");
		Random generator = new Random();
		int num = 0;
		String value = "";
		String lessdigitvalue = "";
		String mindigitvalue = "1";
		for (int i = 0 ;i < digits ; i++ ){
			value = value + generator.nextInt(9);
		}
		for (int i = 0 ;i < digits-1 ; i++ ){
			lessdigitvalue = lessdigitvalue + "9";
			mindigitvalue = mindigitvalue + "0";
		}
		num = Integer.parseInt(value);
		if (num < Integer.parseInt(mindigitvalue)){
			num = num + Integer.parseInt(lessdigitvalue);
		}
		return num;
	}
	
	private boolean chkOTPGenCount(Transpoter formDTO)
	{
		boolean OTPRegen = false;
		PreparedQueryManager pqm = new PreparedQueryManager();
		resultDTO.setValue("sucFlg","0");
		try
		{
			pqm.openConnection();
			connDB = pqm.getConnection();
			stmt= connDB.prepareCall("call SP_FES_CHKOTPGENCOUNT(?,?,?,?,?)");
			stmt.setString(1,formDTO.getValue("domainId"));
			stmt.setString(2,formDTO.getValue("userId"));
			stmt.setString(3,formDTO.getValue("request"));
			stmt.setString(4,formDTO.getValue("reqserial"));
			stmt.registerOutParameter(5, Types.VARCHAR);
			stmt.execute();
			if(stmt.getString(5).equals("S")){	
				OTPRegen = true;			
			}
		}
		catch(Exception ex)
		{
			logger.logDebug("chkOTPGenCount::"+ex.getLocalizedMessage());
			resultDTO.setValue("errMsg","");
		}
		finally
		{
			if(pqm!=null)
			{
				pqm.closeConnection();
				pqm=null;
				connDB=null;
			}
		}
		return OTPRegen;
	}
	
	public Transpoter createOtp(Transpoter formDTO){
		Transpoter resultDTO = new Transpoter();
		String _3dgtPin = null;
		String _6dgtPin = null;
		PreparedQueryManager manager = new PreparedQueryManager();
		try {
			logger.logDebug("createOtp()");
			if(chkOTPGenCount(formDTO))
			{
				String salt = "12345678";
				String encKey = formDTO.getValue("domainId")+ formDTO.getValue("userId")+salt;
				for(int i = encKey.length();i < 32;i++){
					encKey = encKey + "0";
				}
				TrippleDes encryption = new TrippleDes(encKey);
				_6dgtPin = String.valueOf(generatePin(6));
				String encText = encryption.encrypt(_6dgtPin);
				_3dgtPin = String.valueOf(generatePin(3));
				logger.logDebug("_6dgtPin"+_6dgtPin);
				logger.logDebug("_3dgtPin"+_3dgtPin);
				String updateQuery = "UPDATE OTP SET OTP_STATUS = 0 WHERE OTP_STATUS IS NULL AND suborgcode = ? AND USER_ID = ? ";			
				manager.openConnection();
				manager.prepareStatement(updateQuery);
				PreparedStatement _stmt = manager.getStatement();
				_stmt.setString(1, formDTO.getValue("domainId"));
				_stmt.setString(2, formDTO.getValue("userId"));
				_stmt.executeQuery();
				_stmt.close();
				String query = "INSERT INTO OTP(suborgcode,USER_ID,REF_NO,OTP_PASSWORD,OTP_GEN_TIME) VALUES (?,?,?,?,?)";
				manager.prepareStatement(query);
				PreparedStatement _stmt1 = manager.getStatement();
				_stmt1.setString(1, formDTO.getValue("domainId"));
				_stmt1.setString(2, formDTO.getValue("userId"));
				_stmt1.setString(3, _3dgtPin);
				_stmt1.setString(4, encText);
				Date today = new java.util.Date();
				_stmt1.setTimestamp(5,new java.sql.Timestamp(today.getTime()));
				_stmt1.executeQuery();
				_stmt.close();
				formDTO.setValue("refno", _3dgtPin);
				formDTO.setValue("otp", _6dgtPin);
				resultDTO.setValue("otpRefNo", _3dgtPin);
				InsertotpAlert(formDTO);
				resultDTO.setValue("sucFlg", "1");
				resultDTO.setValue("errMsg", "");
			}
			else
			{
				resultDTO.setValue("sucFlg", "0");
				resultDTO.setValue("errMsg", "OTP Generate Limit Exceeded");
			}
		} catch (Exception e) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			manager.closeConnection();
		}
		return resultDTO;
	}
	
	public Transpoter InsertotpAlert(Transpoter formDTO)
	{
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager _QM = new PreparedQueryManager();
		StringBuffer sqlstr=new  StringBuffer();
		if (!_QM.openConnection()) {
			resultDTO.setValue("sucFlg", "0");
			resultDTO.setValue("errMsg", _QM.getErrorMessage());
			_QM=null;
			return resultDTO;
		}			
		
		try{
		     sqlstr = new StringBuffer("INSERT INTO EVENTEXECUTOR (suborgcode,USER_ID,ALERT_GEN_DATE,ALERT_FOR_DIREC,FES_REF_NO,CONTENT,PORTALLP_DEL,FWD_ACTION,EVENT_ID,USER_TYPE)VALUES(?,?,?,?,?,?,?,?,?,?)");
			 if (!_QM.prepareStatement(sqlstr.toString())) {					
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", _QM.getErrorMessage());
					_QM.closeConnection();
					return resultDTO;
			  }
			 String content = formDTO.getValue("otp")+"|"+ formDTO.getValue("refno"); 
			 String salt = "12345678";
			 String encKey = formDTO.getValue("domainId")+ formDTO.getValue("userId")+salt;
			 for(int i = encKey.length();i < 32;i++){
					encKey = encKey + "0";
				}
			 TrippleDes encryption = new TrippleDes(encKey);
			 String encContent = encryption.encrypt(content);
			 
			 PreparedStatement stmt = _QM.getStatement();	 
			 stmt.setString(1,formDTO.getValue("domainId"));
			 stmt.setString(2,formDTO.getValue("userId"));
			 java.sql.Timestamp ts = new java.sql.Timestamp (System.currentTimeMillis());
			 stmt.setTimestamp(3,ts);
			 stmt.setString(4,"F");
			 stmt.setString(5,"999999999999999");
			 stmt.setString(6,encContent); 
			 stmt.setInt(7,0);					
			 stmt.setString(8,"R");
			 stmt.setString(9,"A08");
			 stmt.setString(10,"U");
			 if (stmt.executeUpdate() == 1){ 
					resultDTO.setValue("sucFlg", "1");
				}else{
					resultDTO.setValue("sucFlg", "0");
					resultDTO.setValue("errMsg", _QM.getErrorMessage());			
				}
		    
			_QM.closeConnection();
			_QM=null;
			
		
		}catch(Exception e){
			resultDTO.setValue("sucFlg","0") ;
			resultDTO.setValue("errMsg",e.getLocalizedMessage()) ;
			_QM.closeConnection();
			_QM = null;
			return resultDTO;
		}
		return resultDTO;		
	}



public Transpoter getRoleId(Transpoter formDTO)
	{
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager pqm = new PreparedQueryManager();
		resultDTO.setValue("sucFlg","0");
		try
		{
			String query = "SELECT ROLE_ID from USERS0001 WHERE USER_ID=? AND suborgcode=?";
			pqm.openConnection();
			pqm.prepareStatement(query);
			pqm.getStatement().setString(1, formDTO.getValue("user_id"));
			pqm.getStatement().setString(2, formDTO.getValue("SUBORGCODE"));
			
			if(pqm.executeQuery())
			{
				ResultSet rset = pqm.getResultSet();
				if(rset.next())
				{
					resultDTO.setValue("USER_ROLE_ID", rset.getString("ROLE_ID"));
				}
				rset.close();
			}
			resultDTO.setValue("sucFlg","1");
		}
		catch(Exception ex)
		{
			logger.logDebug("getRoleId::"+ex.getLocalizedMessage());
			resultDTO.setValue("errMsg","No Role Id");
		}
		finally
		{
			if(pqm!=null)
			{
				pqm.closeConnection();
				pqm=null;
			}
		}
		return resultDTO;
	}
	
	
	public long calcTimeDifference(String startTime, String endTime, String dateFormat, String calcMode){
		try{
           SimpleDateFormat format = new SimpleDateFormat(dateFormat);  
            Date startDate = null;
            Date endDate = null;
            startDate = format.parse(startTime);
            endDate = format.parse(endTime);
            long difference = endDate.getTime() - startDate.getTime();
            if(calcMode.equals("1")){
            	return (difference / 1000);
            }else if(calcMode.equals("2")){
            	return (difference / (60 * 1000));
            }else{
            	return (difference / (60 * 60 * 1000)); 
            	
            } 
    	}catch(Exception ex){
    		ex.printStackTrace();
    		return -1;
    	}
	}
public Transpoter loadImage(Transpoter formDTO){
		
		Transpoter resultDTO = new Transpoter();
		PreparedQueryManager pqm = new PreparedQueryManager();
		resultDTO.setValue("sucFlg","0");
		Blob img ;
	    byte[] imgData = null ;
		try
		{
			String query = "SELECT PHOTO FROM CONTACTS where rownum=1";
			pqm.openConnection();
			pqm.prepareStatement(query);
			
			if(pqm.executeQuery())
			{
				ResultSet rset = pqm.getResultSet();
				if(rset.next())
				{
					img =rset.getBlob(1);
					imgData=img.getBytes(1,(int)img.length());
					
				}
				rset.close();
			}
			resultDTO.setValue("sucFlg","1");
		}
		catch(Exception ex)
		{
			resultDTO.setValue("errMsg","No Role Id");
		}
		finally
		{
			if(pqm!=null)
			{
				pqm.closeConnection();
				pqm=null;
			}
		}
		return resultDTO;
}

public Transpoter fetchMenuInfo(Transpoter formDTO) throws SQLException{
	logger.logDebug("fetchMenuInfo()");
	String _itemNames="";
	String _itemPaths="";
	Transpoter resultDTO = new Transpoter();
	PreparedQueryManager pqm = new PreparedQueryManager();
	PreparedStatement _pstmt = null;
	ResultSet rs = null;
	if(!pqm.openConnection()){
		resultDTO.setValue("sucFlg","0") ;
		resultDTO.setValue("result",pqm.getErrorMessage()) ;
		pqm = null;
		return resultDTO;
	}
	try{
		String currPageName = (String)formDTO.getValue("pageName");
		String sqlQuery = "SELECT MENUITEM_SL,PGM_NAME,PGM_PATH FROM MENU004 WHERE PGM_ID = ? ORDER BY MENUITEM_SL";
		if (pqm.prepareStatement(sqlQuery)) {
			_pstmt = pqm.getStatement();
			_pstmt.setString(1, currPageName.trim());
			if (pqm.executeQuery()) {
				rs = pqm.getResultSet();
				int count = 0;
				while (rs.next()) {
					count++;
	            	if(_itemNames.equalsIgnoreCase(""))
	            		_itemNames=""+rs.getString("PGM_NAME");
	            	else
	            		_itemNames=_itemNames+"|"+rs.getString("PGM_NAME");
	            	
	            	if(_itemPaths.equalsIgnoreCase(""))
	            		_itemPaths=""+rs.getString("PGM_PATH");
	            	else
	            		_itemPaths=_itemPaths+"|"+rs.getString("PGM_PATH");
				}
            	resultDTO.setValue("NO_OF_MENUITEMS",Integer.toString(count));
            	resultDTO.setValue("ITEM_NAME",_itemNames);
            	resultDTO.setValue("PAGE_PATH",_itemPaths);
				resultDTO.setValue("sucFlg", "1");
			}
		}
	} catch (SQLException e) {
		e.printStackTrace();
		resultDTO.setValue("sucFlg", "0");
		resultDTO.setValue("errMsg", e.getLocalizedMessage());
	} finally {
		pqm.closeConnection();
		rs.close();
		rs = null;
		_pstmt.close();
		_pstmt = null;
	}
	
	return resultDTO;
}


	public static Date fetchUserLastLoginTime(String userId, String domainId)
										throws SQLException {
			Timestamp userlastlogintime = null;
			PreparedStatement _pstmt = null;
			ResultSet rs = null;
			PreparedQueryManager pqm = new PreparedQueryManager();
			if (!pqm.openConnection()) {
			pqm = null;
			userlastlogintime = null;
			}
			try {
			String sqlStr = "SELECT MAX(ULHST_IN_DATE) MAXDATE FROM USERS009 WHERE ULHST_USER_ID=? AND suborgcode=? AND ULHST_OUT_DATE IS NOT NULL";
			if (pqm.prepareStatement(sqlStr)) {
				_pstmt = pqm.getStatement();
				_pstmt.setString(1, userId);
				_pstmt.setString(2, domainId);
				if (pqm.executeQuery()) {
					rs = pqm.getResultSet();
					if (rs.next()) {
						userlastlogintime = rs.getTimestamp("MAXDATE");
						System.out.println("last login "+userlastlogintime);
					}
				}
			}
			
			if( userlastlogintime == null){
				String sqlStr2 = "SELECT MAX(ULHST_IN_DATE) MAXDATE FROM USERS009 WHERE ULHST_USER_ID=? AND suborgcode=? ";
				if (pqm.prepareStatement(sqlStr2)) {
					_pstmt = pqm.getStatement();
					_pstmt.setString(1, userId);
					_pstmt.setString(2, domainId);
					if (pqm.executeQuery()) {
						rs = pqm.getResultSet();
						if (rs.next()) {
							userlastlogintime = rs.getTimestamp("MAXDATE");
							System.out.println("last login "+userlastlogintime);
						}
					}
				}
			}
			} catch (SQLException e) {
			e.printStackTrace();
			userlastlogintime = null;
			} finally {
			pqm.closeConnection();
			rs.close();
			rs = null;
			_pstmt.close();
			_pstmt = null;
			}
			return userlastlogintime;
		}
			
}

