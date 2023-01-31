package com.hdsoft.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class FormatUtils
{
	String errStr;

	public static String dynaSQLDate(String date, String dateFormat) {
		String returnDate = null;
		String month[] = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
				"AUG", "SEP", "OCT", "NOV", "DEC" };
		try {
			if (dateFormat.equalsIgnoreCase("DD-MM-YYYY")) {
				returnDate = date.substring(0, 2) + "-"
						+ month[Integer.parseInt(date.substring(3, 5)) - 1]
						+ "-" + date.substring(6, 10);
			} else if (dateFormat.equalsIgnoreCase("YYYY-MM-DD")) {
				returnDate = date.substring(8, 10) + "-"
						+ month[Integer.parseInt(date.substring(5, 7)) - 1]
						+ "-" + date.substring(0, 4);
			} else if (dateFormat.equalsIgnoreCase("DDMMYYYY")) {
				returnDate = date.substring(0, 2) + "-"
						+ month[Integer.parseInt(date.substring(2, 4)) - 1]
						+ "-" + date.substring(4, 8);
			} else if (dateFormat.equalsIgnoreCase("YYYYMMDD")) {
				returnDate = date.substring(6, 8) + "-"
						+ month[Integer.parseInt(date.substring(4, 6)) - 1]
						+ "-" + date.substring(0, 4);
			}
		} catch (Exception e) {
			return null;
		}
		return returnDate;
	}

	public static String reverseDate(String date, String dateFormat) {
		if ((date == null) || (date.trim().equals("")))
			return null;
		String reversedDate = null;
		try {
			if (dateFormat.equalsIgnoreCase("DD-MM-YYYY")) {
				reversedDate = date.substring(6, 10) + "-"
						+ date.substring(3, 5) + "-" + date.substring(0, 2);
			} else if (dateFormat.equalsIgnoreCase("YYYY-MM-DD")) {
				reversedDate = date.substring(8, 10) + "-"
						+ date.substring(5, 7) + "-" + date.substring(0, 4);
			} else if (dateFormat.equalsIgnoreCase("DDMMYYYY")) {
				reversedDate = date.substring(4, 8) + "-"
						+ date.substring(2, 4) + "-" + date.substring(0, 2);
			}
		} catch (Exception e) {

		}
		return reversedDate;
	}

	public static Date getDate(String strValue) {
		if (strValue == null || strValue.trim().equals(""))
			return null;
		try {
			return Date
					.valueOf(FormatUtils.reverseDate(strValue, "DD-MM-YYYY"));
		} catch (Exception e) {
			return null;
		}
	}

	
	public static String formatToDDMonYear(String DDMMYear) {
		if (DDMMYear == null)
			return null;

		String month[] = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
				"AUG", "SEP", "OCT", "NOV", "DEC" };

		return DDMMYear.substring(0, 2) + "-"
				+ month[Integer.parseInt(DDMMYear.substring(3, 5)) - 1] + "-"
				+ DDMMYear.substring(6, 10);
	} 

	
	public static String formatErrMessage(String strValue) {
		if (strValue == null)
			return strValue;
		strValue = strValue.replaceAll("&", "");
		strValue = strValue.replaceAll("com.", "").replaceAll("hds.", "")
				.replaceAll("cms.", "").replaceAll("exceptions.", "")
				.replaceAll("CMSException", "").replaceAll("master.", "")
				.replaceAll("entries.", "").replaceAll("master.", "");
		return strValue;
	}

	public static long getLongValue(String strValue) {
		if (strValue == null || strValue.trim().equals(""))
			return 0;
		else
			return Long.parseLong(strValue);
	}

	public static int getIntValue(String strValue) {
		if (strValue == null || strValue.trim().equals(""))
			return 0;
		else
			return Integer.parseInt(strValue);
	}

	public static double getDoubleValue(String strValue) {
		if (strValue == null || strValue.trim().equals(""))
			return 0.00;
		else
			return Double.parseDouble(strValue.replaceAll(",", ""));
	}

	public static String reverseDateTime(String date, String dateFormat,
			String time, String timeFormat) {
		String reverseDateTime = null;
		if (dateFormat == null)
			return null;
		String month[] = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
				"AUG", "SEP", "OCT", "NOV", "DEC" };

		if ((dateFormat.equalsIgnoreCase("DD-MM-YYYY"))
				&& (timeFormat.equalsIgnoreCase("HH24:MI:SS"))) {
			reverseDateTime = date.substring(0, 2) + "-"
					+ month[Integer.parseInt(date.substring(3, 5)) - 1] + "-"
					+ date.substring(6, 10) + " " + time;
		}
		return reverseDateTime;
	}

	public static String reverseTime(String time, String timeFormat) {
		String reverseTime = null;
		if (timeFormat == null)
			return null;
		if (timeFormat.equalsIgnoreCase("HH24:MI")) {
			reverseTime = time.substring(0, 2) + time.substring(3, 5);
		}
		return reverseTime;
	}

	public static String unFormat(String num) {
		String newUnFormatStr = "";
		for (int i = 0; i < num.length(); i++) {
			if (num.charAt(i) != ',') {
				newUnFormatStr = newUnFormatStr + num.charAt(i);
			}
		}
		return newUnFormatStr;
	}

	public static String formatUpdateString(String updateString) {
		String[] sqlTokens = null;
		if (updateString.indexOf("UPDATE") >= 0) {
			sqlTokens = updateString.split(" SET ");
			updateString = sqlTokens[0] + " SET ";
			sqlTokens[1] = sqlTokens[1].trim();
			if (sqlTokens[1].trim().indexOf(",") == 0) {
				updateString = updateString
						+ sqlTokens[1].substring(1, sqlTokens[1].length());
			} else {
				updateString = updateString + sqlTokens[1];
			}
		}
		updateString = updateString.replaceAll("'null'", "null");
		return updateString;
	}

	public static String getToDateStr(String strDate) {
		if (strDate == null || strDate.trim().equals(""))
			return null;
		else {
			return "TO_DATE('" + strDate + "','DD-MM-YYYY')";
		}
	}

	public static String nvl(String s1, String s2) {
		if (s1 == null || s1.trim().equals(""))
			return s2;
		else
			return s1;
	}

	public static String rPad(String argument1, int no, String paddingvalue) {
		StringBuffer tempString = new StringBuffer(argument1);
		for (int j = argument1.length() + 1; j <= no; j++) {
			tempString = tempString.append(paddingvalue);
		}
		return tempString.toString();
	}

	public static String lPad(String argument1, int no, String paddingvalue) {
		StringBuffer tempString = new StringBuffer();

		for (int j = 1; j <= no - argument1.length(); j++) {
			tempString = tempString.append(paddingvalue);
		}
		tempString.append(argument1);
		return tempString.toString();
	}

	public static String getString(String argument1) {
		if (argument1 == null)
			return null;
		if (argument1.toLowerCase().equals("null"))
			return null;
		return argument1;
	}

	public static String getString(Object arg1) {

		if (arg1 == null)
			return null;
		if (arg1.toString().toLowerCase().equals("null"))
			return null;
		return (String) arg1;
	}

	
	public static boolean isNumeric(String value) {
		String validValues = "0123456789.";
		int length = value.length();
		for (int i = 0; i < length; ++i) {
			if (validValues.indexOf(value.charAt(i)) == -1) {
				return false;
			}
		}
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException numberFormatException) {
			return false;
		}
	}

	
	public static boolean isZero(double value) {
		if (value == 0) {
			return true;
		}
		return false;
	}

	
	public static boolean isNegativeValue(double value) {
		if (value < 0) {
			return true;
		}
		return false;
	}

	
	public static boolean isPostiveValue(double value) {
		if (value > 0) {
			return true;
		}
		return false;
	}

	
	public static boolean isInteger(double value) {
		try {
			Long.parseLong(value + "");
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	
	public static boolean isInRange(BigDecimal value, BigDecimal minValue,
			BigDecimal includeMinValue, BigDecimal maxValue,
			BigDecimal includeMaxValue) {
		if (includeMinValue.intValue() == 1 && includeMaxValue.intValue() == 1) {
			return (value.doubleValue() >= minValue.doubleValue() && value
					.doubleValue() <= maxValue.doubleValue());
		} else if (includeMinValue.intValue() == 1
				&& includeMaxValue.intValue() == 0) {
			return (value.doubleValue() >= minValue.doubleValue() && value
					.doubleValue() < maxValue.doubleValue());
		} else if (includeMinValue.intValue() == 0
				&& includeMaxValue.intValue() == 1) {
			return (value.doubleValue() > minValue.doubleValue() && value
					.doubleValue() <= maxValue.doubleValue());
		} else {
			return (value.doubleValue() > minValue.doubleValue() && value
					.doubleValue() < maxValue.doubleValue());
		}

	}

	
	public static String unFormat(String value, char removeChar) {
		return value.replaceAll(removeChar + "", "");
	}

	

	public static String unFormat(String value, String removeChar) {
		if (value.indexOf(removeChar) == -1)
			return value;
		else
			return value.substring(0, value.indexOf(removeChar));
	}

	public static String formatAmount(String num1, String decLen) {
		String res = "";
		String decPart = "";
		if (Integer.parseInt(decLen) > 0) {
			for (int i = 1; i <= Integer.parseInt(decLen); i++) {
				decPart = decPart + '0';
			}
			decPart = "." + decPart;
		}
		res = num1.trim().concat(decPart);
		return res;
	}

	
	public static String unFormatAmount(String value) {
		return unFormat(value, ',');
	}

	
	public static boolean containsInvalidChars(String value, String charList) {
		int length = charList.length();
		for (int i = 0; i < length; ++i) {
			if (value.indexOf(charList.charAt(i)) != -1) {
				return true;
			}
		}
		return false;
	}

	
	public static boolean hasMinimumLength(String value, int length) {
		if (value.length() < length) {
			return false;
		}
		return true;
	}

	
	public static boolean hasMaximumLength(String value, int length) {
		if (value.length() > length) {
			return false;
		}
		return true;
	}

	
	public static boolean isBlank(String value) {
		if (value == null || value.trim().equals("")) {
			return true;
		}
		return false;
	}

	
	public static boolean isBlank(String value, boolean trim) {
		if (trim) {
			return isBlank(value.trim());
		} else {
			return isBlank(value);
		}
	}

	
	public static boolean isNull(String value) {
		if (value == null) {
			return true;
		}
		return false;
	}

	
	public static String padWithBlank(String value, int length) {
		StringBuffer str = new StringBuffer(value);
		while (str.length() != length) {
			str.insert(str.length(), ' ');
		}
		return str.toString();
	}

	
	public static String padWithZero(String value, int length) {
		StringBuffer str = new StringBuffer(value);
		while (str.length() != length) {
			str.insert(0, '0');
		}
		return str.toString();
	}

	
	public static boolean matchPattern(String value, String expression) {
		boolean match = false;
		if (validateRequired(expression)) {
			match = Pattern.matches(expression, value);
		}
		return match;
	}

	public static boolean validateRequired(String value) {
		boolean isFieldValid = false;
		if (value != null && value.trim().length() > 0) {
			isFieldValid = true;
		}
		return isFieldValid;
	}

	
	public static boolean isAlphaNumeric(String value) {
		String expression = "^[a-zA-Z0-9]*$";
		boolean match = false;
		if (validateRequired(value)) {
			match = Pattern.matches(expression, value);
		}
		return match;
	}

	
	public static boolean allowAlphaNumericSpaces(String value) {
		String expression = "^[a-zA-Z0-9 ]*$";
		boolean match = false;
		if (validateRequired(value)) {
			match = Pattern.matches(expression, value);
		}
		return match;
	}

	
	public static boolean allowSomeSpecialChars(String value) {
		String expression = "^[a-zA-Z0-9 \\[\\]()#-;,.']*$";
		boolean match = false;
		if (validateRequired(value)) {
			match = Pattern.matches(expression, value);
		}
		return match;
	}

	public static boolean allowFesSpecialChars(String value) {
		String expression = "^[a-zA-Z0-9\\`~@#$%^&*()-_[]{}|;:/?.,\"]*$";
		boolean match = false;
		if (validateRequired(value)) {
			match = Pattern.matches(expression, value);
		}
		return match;
	}


	public static String formatAmountWithEditCode(String amount, int editcode) {
		String amt = "";
		try {
			BigDecimal a = new BigDecimal(amount);
			BigDecimal b = new BigDecimal(Math.pow(10, editcode));
			BigDecimal c = new BigDecimal(0);

			c = a.divide(b, editcode, editcode);

			NumberFormat num = NumberFormat.getInstance();

			num.setMinimumFractionDigits(editcode);
			amt = String.valueOf(num.format(c));
		} catch (ArithmeticException ae) {
			System.out.println(ae.getMessage());
		} catch (NumberFormatException ne) {
			System.out.println(ne.getMessage());
		}
		return amt;
	}


	public static String formatInterestRate(String intRate) {
		String interestRate = "";
		
		interestRate = FormatUtils.formatAmountWithEditCode(intRate, 7);
		return interestRate;
	}

	public static Timestamp DateToYYYYMMDD1(String DateValue) {
		return DateToYYYYMMDD1(DateValue, "DD-MM-YYYY");
	}

	public static Timestamp DateToYYYYMMDD1(String DateValue, String dateFormat) {

		if (DateValue == null)
			return null;

		String OriginalDateValue = "";
		OriginalDateValue = DateValue;

		if (dateFormat.equalsIgnoreCase("DD-MM-YYYY")) {
			DateValue = DateValue.substring(6, 10) + "-"
					+ DateValue.substring(3, 5) + "-"
					+ DateValue.substring(0, 2);
		}

		if (OriginalDateValue.trim().length() < 11) {
			DateValue = DateValue + " " + doGetMethods();
		} else {
			try {
				DateValue = DateValue + " "
						+ OriginalDateValue.substring(11).replaceAll("-", ":");

			} catch (Exception e) {

			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm");
		java.util.Date date = null;

		try {
			date = sdf.parse(DateValue);
		} catch (java.text.ParseException e) {

		}
		java.sql.Timestamp RetValue = new java.sql.Timestamp(date.getTime());
		return RetValue;
	}

	

	public static String getValidDate(String argument1) {
		String b[] = new String[10];
		StringTokenizer s1 = new StringTokenizer(argument1, "/-");

		int i = 0;
		while (s1.hasMoreTokens()) {
			b[i] = s1.nextToken();
			System.out.println("first" + b[i]);
			i++;

		}
		if ((Integer.parseInt(b[0]) > 31) || (Integer.parseInt(b[0]) < 1)) {
			argument1 = "Error";
		} else if ((Integer.parseInt(b[1]) > (12))
				|| (Integer.parseInt(b[1]) < 1)) {
			argument1 = "Error";
		}
		else if ((b[2].length() != 4) || (2000 > (Integer.parseInt(b[2])))
				|| (2999 < (Integer.parseInt(b[2])))) 
		{
			argument1 = "Error";
		}
		return argument1;
	}

	public static String doGetMethods() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":"
				+ c.get(Calendar.SECOND);
	}

	public static Timestamp  getTimeStamp(String date, String format) {
		SimpleDateFormat dateFormat = null;
		Timestamp timestamp = null;
		java.util.Date parsedDate = null;
		try {
			dateFormat = new SimpleDateFormat(format);
			parsedDate = dateFormat.parse(date);
			timestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return timestamp;
	}
}
