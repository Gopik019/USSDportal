package com.hdsoft.utils;

public class EncryptDecrypt {

	private String strText = "";
	private String strpasswd = "";
	private String strSIGNATURE = "";
	private boolean bESCApE = false;
	private int nNumtranspositions = 0;
	private int antranspositions[] = new int[10000];
	
	public static void main(String[] args) throws Exception 
	{
		EncryptDecrypt ne = new EncryptDecrypt();
		
		String encPwd = "";
		String decryptionKey = "";
		
		System.out.println(ne.doDecrypt( encPwd, decryptionKey));
	}

	public String doEncrypt(String password,String encryptionKey) 
	{
		try 
		{
			SecureContext(password, "", true);   //var sc = new SecureContext(encryptText,"", "1");
			SecureContext_secure(encryptionKey);
			int[] ampern = password_getpermutation();
			SecureContext_sign(ampern.length);
			SecureContext_transliterate(true);
			SecureContext_encypher(ampern);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return strText;
	}

	public String doDecrypt(String encPwd,String decryptionKey) 
	{
		try 
		{
			SecureContext(encPwd, "", true);
			SecureContext_secure(decryptionKey);
			int[] ampern = password_getpermutation();
			SecureContext_decypher(ampern);
			SecureContext_transliterate(false);
			SecureContext_unsign(ampern.length);
			strText = strText.trim();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return strText;
	}

	private void SecureContext(String strText1, String strSIGNATURE1,boolean bESCApE1)
	{
		strText = strText1;
		strSIGNATURE = strSIGNATURE1;
		bESCApE = bESCApE1;
	}

	private void Password(String pwd) {
		strpasswd = pwd;
	}

	private void SecureContext_encypher(int[] anperm) {
		String strEncyph = "";

		int nCols = anperm.length;
		int nRows = strText.length() / nCols;
		for (int i = 0; i < nCols; i++) {
			int k = anperm[i];
			for (int j = 0; j < nRows; j++) {
				strEncyph += strText.charAt(k);
				k += nCols;
			}
		}
		strText = strEncyph;
	}

	private void SecureContext_decypher(int[] anperm) {
		int nRows = anperm.length;
		int nCols = strText.length() / nRows;
		int anRowOfs[] = new int[10000];
		for (int i = 0; i < nRows; i++)
			anRowOfs[anperm[i]] = i * nCols;
		String strplain = "";
		for (int i = 0; i < nCols; i++) {
			for (int j = 0; j < nRows; j++) {
				strplain += strText.charAt(anRowOfs[j] + i);
			}
		}
		strText = strplain;
	}

	private int[] password_getpermutation() {
		int nNUMELEMENTS = 13;
		int nCYCLELENGTH = 21;
		Double anCycle1[] = new Double[nCYCLELENGTH];
		int anCycle[] = new int[nCYCLELENGTH];
		permutationGenerator(nNUMELEMENTS);
		Double npred = password_getHashValue();
		for (int i = 0; i < nCYCLELENGTH; i++) {
			npred = 314159269 * npred + 907633409;
			anCycle1[i] = npred % nNumtranspositions;
			anCycle[i] = anCycle1[i].intValue();
		}
		int[] returnvalue = permutationGenerator_fromCycle(anCycle);
		return returnvalue;
	}

	private int[] permutationGenerator_fromCycle(int[] anCycle) {
		int nNUMELEMENTS = 13;
		int nCYCLELENGTH = 21;
		int anpermutation[] = new int[nNUMELEMENTS];
		for (int i = 0; i < nNUMELEMENTS; i++) {
			anpermutation[i] = i;
		}
		for (int i = 0; i < anCycle.length; i++) {
			int nT = antranspositions[anCycle[i]];
			int n1 = nT & 255;
			int n2 = (nT >> 8) & 255;
			nT = anpermutation[n1];
			anpermutation[n1] = anpermutation[n2];
			anpermutation[n2] = nT;
		}
		return anpermutation;
	}

	private void permutationGenerator(int nNUMELEMENTS) {
		int k = 0;
		for (int i = 0; i < nNUMELEMENTS - 1; i++) {
			for (int j = i + 1; j < nNUMELEMENTS; j++) {
				antranspositions[k++] = (i << 8) | j;
			}
			nNumtranspositions = k;
		}
	}

	private Double password_getHashValue() {
		int m = 907633409;
		int a = 65599;
		Double h = 0.0;
		for (int i = 0; i < strpasswd.length(); i++) {
			h = (h % m) * a + (int) strpasswd.charAt(i);
		}
		return h;
	}

	private void SecureContext_secure(String encpwd) {
		Password(encpwd);
	}

	private Boolean SecureContext_unsign(int nCols) {
		if (bESCApE) {
			strText = SecureContext_unescape(strText);
			strSIGNATURE = SecureContext_unescape(strSIGNATURE);
		}
		if ("" == strSIGNATURE)
			return true;
		int nTextLen = strText.lastIndexOf(strSIGNATURE);
		if (-1 == nTextLen)
			return false;
		strText = strText.substring(0, nTextLen);
		return true;
	}

	private String SecureContext_unescape(String strToUnescape) {
		String strUnescaped = "";
		int i = 0;
		while (i < strToUnescape.length()) {
			char chT = strToUnescape.charAt(i++);
			if ('\\' == chT) {
				chT = strToUnescape.charAt(i++);
				switch (chT) {
				case 'r':
					strUnescaped += '\r';
					break;
				case 'n':
					strUnescaped += '\n';
					break;
				case '\\':
					strUnescaped += '\\';
					break;
				default: // not possible
				}
			} else
				strUnescaped += chT;
		}
		return strUnescaped;
	}

	private void SecureContext_transliterate(Boolean btransliterate) {
		String strDest = "";

		int nTextIter = 0;
		int nTexttrail = 0;

		while (nTextIter < strText.length()) {
			String strRun = "";
			int cSkipped = 0;
			while (cSkipped < 7 && nTextIter < strText.length()) {
				char chT = strText.charAt(nTextIter++);
				if (-1 == strRun.indexOf(chT)) {
					strRun += chT;
					cSkipped = 0;
				} else
					cSkipped++;
			}
			while (nTexttrail < nTextIter) {
				int nRunIdx = strRun.indexOf(strText.charAt(nTexttrail++));
				if (btransliterate) {
					nRunIdx++;
					if (nRunIdx == strRun.length())
						nRunIdx = 0;
				} else {
					nRunIdx--;
					if (nRunIdx == -1)
						nRunIdx += strRun.length();
				}
				strDest += strRun.charAt(nRunIdx);
			}
		}
		
		strText = strDest;
	}

	private void SecureContext_sign(int nCols) {
		if (bESCApE) {
			strText = SecureContext_escape(strText);
			strSIGNATURE = SecureContext_escape(strSIGNATURE);
		}
		int nTextLen = strText.length() + strSIGNATURE.length();
		int nMissingCols = nCols - (nTextLen % nCols);
		String strpadding = "";
		if (nMissingCols < nCols)
			for (int i = 0; i < nMissingCols; i++)
				strpadding += ' ';
		int x = strText.length();
		strText += strpadding + strSIGNATURE;
	}

	private String SecureContext_escape(String strToEscape) {
		String strEscaped = "";
		for (int i = 0; i < strToEscape.length(); i++) {
			char chT = strToEscape.charAt(i);
			switch (chT) {
			case '\r':
				strEscaped += 'r';
				break;
			case '\n':
				strEscaped += 'n';
				break;
			case '\\':
				strEscaped += '\\';
				break;
			default:
				strEscaped += chT;
			}
		}
		return strEscaped;
	}

}
