	var submitFlag;
	var message;
	var othervalues
	var w_dummy_amount;
	var w_integer_value;
	var w_decimal_value;
	var version;
	var convSubUnit='1000';
	var convSubUnit=getSubUnits(convSubUnit);	
	var amtFormatType='L';
	 
	
	 	
	
	 
	function disableRightClick(e){
	 
	}
	
	function To_Uppercase(ch,splcharallowed){
			if (!((ch>=KEY_LOWERCASE_A && ch<=KEY_LOWERCASE_Z) || (ch>=KEY_UPPERCASE_A && ch<=KEY_UPPERCASE_Z) 
			   || (ch>=KEY_NUMBER_0 && ch<=KEY_NUMBER_9))){
			    if (arguments.length == 1){
					window.event.keyCode = 0;
					return;
				}
			}
			if (ch>=KEY_LOWERCASE_A && ch<=KEY_LOWERCASE_Z)
				{
				ch  = ch  - 32;
				window.event.keyCode = ch;
				}
	}
		
		
	function To_Lowercase(ch){
			if (ch>=KEY_UPPERCASE_A && ch<=KEY_UPPERCASE_Z)
				{
				ch  = ch  + 32;
				window.event.keyCode = ch;
				}
	}	

	function isInteger(text){
		var i;
	    var flag;
	    var ZeroCnt = 0;
	    if(text.indexOf(".") == "0")
			return false;
	    for (i = 0; i < text.length; i++) {
			var c = text.charAt(i);
			if((c != ".") && ((c < "0") || (c > "9")))
	      		flag = false;
			else 
	      		flag = true;
			if (flag == false) return flag;
			if (c == ".") ZeroCnt = ZeroCnt + 1;
		}
		if(ZeroCnt > 1)
			flag = false;
		else {
			var amt = text.split(".");
			if(amt[1] == "")
				return false;
		}
		return flag;
	}
		
	 
	function isNumeric(keycode,decFlag){
		if(keycode >= KEY_NUMBER_0 && keycode <=KEY_NUMBER_9){
			return true;	
		}else{
			if(decFlag == null){
				if(keycode == KEY_ENTER)
					return true;
				else{
					window.event.keyCode = 0;
					return false;	
				}
			}else{
				if(isDotPressed(keycode,decFlag)){
					return true;
				}else{
					return false;
				} 	
			}		
		}		
	}

	function checkNumeric(text){
		var textLength=text.length;
		for(i=0;i<textLength;i++){
			if(text.charCodeAt(i)<48||text.charCodeAt(i)>57)
				return false;
		}
		return true;	
	}
	
	
	
	
	 
	function isDotPressed(keycode,strVal){
		if(keycode == KEY_DOT){
			if(strVal=='N' ) {
				window.event.keyCode = 0;
				return false;
			}
			return true;		
		}else{
			window.event.keyCode = 0;
			return false;
		}			
	}
	
	
	 
	function isEnterKeyPressed(){
		if (window.event.keyCode ==KEY_ENTER) {
			window.event.returnValue=false;
			return true
		}else
			return false;		
	}
	
	 
	function isAlpha(ch){
		if(ch >= KEY_UPPERCASE_A && ch <=KEY_UPPERCASE_Z || ch>=KEY_LOWERCASE_A && ch<=KEY_LOWERCASE_Z)
			return true;
		else
			return false;
	}
	
	function isAlphabet(text){
		var textLength=text.length;
		for(i=0;i<textLength;i++){
			if(text.charCodeAt(i)<65||text.charCodeAt(i)>122)
				return false;
		}
		return true;	
	}
	
	 
	function isSpace(ch){
		if(ch == KEY_SPACE)
			return true;
		else
			return false;
	}
	
	 
	
	function getDHttpAppletObj(){
		return objApplet;
	}	
	
	 

	function SODDateFormat(sod){
  var mon=sod.substring(3,5);
 
	var mm;
	switch(mon){
    	case "01" :  mm="JAN";
      break;
		case "02" :  mm="FEB";
      break;
		case "03" :  mm="MAR";
      break;
		case "04" :  mm="APR";
      break;
		case "05" :  mm="MAY";
      break;
		case "06" :  mm="JUN";
      break;
		case "07" :  mm="JUL";
      break;
		case "08" :  mm="AUG";
      break;
		case "09" :  mm="SEP";
      break;
		case "10" :  mm="OCT";
      break;
		case "11" :  mm="NOV";
			break;
		case "12" :  mm="DEC";
			break;
	}
  sodformat=sod.substring(0,2)+ "/" +mm+"/"+sod.substring(6,11);
	return sodformat;
}


	
	function formatNumber(ch,numeric,intLen,decLen){
		var number = numeric.value;	
		var dotPos = number.indexOf(".");
		if(dotPos == -1){
			if(number.length >= intLen)
				return false;
			else
				return true;
		}else{
			if(number.lastIndexOf(".") >= intLen){
				if(ch == KEY_DOT){
					window.event.keyCode = 0;
					return false;
				}			
			}
			var intPart = number.substring(0,dotPos);
			var decPart = number.substring(dotPos+1,number.length);
			if(intPart.length > intLen)
				return false;
			if(decPart.length > decLen)
				return false;
		}			
	}
	
	 
	function trim( str ) {
		var resultStr = "";
		resultStr = trimLeft(str);
		resultStr = trimRight(resultStr);
		return resultStr;
	} 


	  
	function trimLeft( str ) {
		var resultStr = "";
		var i = len = 0;
		
		if (str+"" == "undefined" || str == null)	
			return null;
	
		str += "";
		if (str.length == 0) 
			resultStr = "";
		else {	
			len = str.length;
	  		while ((i <= len) && (str.charAt(i) == " "))
				i++;
		
	  		resultStr = str.substring(i, len);
	  	}				
	  	return resultStr;
	}
			
	 
	function trimRight( str ) {
		var resultStr = "";
		var i = 0;
		
		if (str+"" == "undefined" || str == null)	
			return null;
	
		str += "";
			
		if (str.length == 0) 
			resultStr = "";
		else {
	  		i = str.length - 1;
	  		while ((i >= 0) && (str.charAt(i) == " "))
	 			i--;
				 			
		  		resultStr = str.substring(0, i + 1);
		  	}
		  	
		  	return resultStr;  	
	} 

	 
	function setErrMsg(val){
		if (val==""){
			document.all.errmsg.className = "footererrmsg"
		} else {
			document.all.errmsg.className = "footererrmsg1"
		}
		document.all.errmsg.innerHTML = val
	}
	
	 
	function setFldHlpMsg(txt1,txt2,txt3){	
		document.all.fldmsg.innerHTML = trim(txt1);
		document.all.hlpmsg.innerHTML = "";
		if(! (txt2==null) ) document.all.hlpmsg.innerHTML = txt2;
		if(! (txt3==null) )	document.all.hlpmsg.innerHTML = document.all.hlpmsg.innerHTML + " , " +  txt3;
		
	}
	
	 
	function setUserId(userID){
		document.all.usrid.innerHTML = userID;
	}
	
	function setBranchName(strBrnName){
		document.all.BranchName.innerHTML = strBrnName;
	}
	
	function setVersion(STRVersion){
		document.all.Version.innerHTML = STRVersion;
	}

	
	
	 
	function setDate(strdate){
		document.all.Date.innerHTML = strdate;
	}
	
	
	
	
	 
	function generateGridData(gridID,recNum,noOfCols){
		var sendXml;
		rowCount = gridID.documentElement.childNodes.length;
		if(rowCount != 0){
			sendXml = "\n";
			for(var j=0;j<noOfCols;j++){
				sendXml += "<Col" + j + ">";
				sendXml += gridID.documentElement.childNodes(recNum).childNodes(j).text;
				sendXml += "</Col" + j +  ">";
			}
		}else{
			sendXml = "";
		}
		return sendXml;
	}
	
 
	function getDecimal(objVal){
		var dtSym;
		var fldVal;
		var stObjVal;
		var frmVal;
		stObjVal=objVal.toString();
		dtSym=stObjVal.indexOf(".");
		if (dtSym==-1){
			fldVal=stObjVal + ".00";
			return fldVal;
		}else{
			return objVal;
		}
	}
	
 	
	function clock(){
		if (!document.layers && !document.all) return;
		var digital = new Date();
		var hours = digital.getHours();
		var minutes = digital.getMinutes();
		var seconds = digital.getSeconds();
		var amOrPm = "AM";
		if (hours > 11) amOrPm = "PM";
		if (hours > 12) hours = hours - 12;
		if (hours == 0) hours = 12;
		if (minutes <= 9) minutes = "0" + minutes;
		if (seconds <= 9) seconds = "0" + seconds;
		dispTime = hours + ":" + minutes + ":" + seconds + " " + amOrPm;
		if (document.layers) {
			document.layers.Time.document.write(dispTime);
			document.layers.Time.document.close();
		}else
			if (document.all)
				Time.innerHTML = dispTime;
			
		setTimeout("clock()", 1000);
	}
	
	function resizeWindow()
	{
	  window.moveTo(0,0);
	  window.resizeTo(screen.width,screen.height);
	}
	
	
function chkAddInfoFormat(dataFormat, inputStr){
	if (dataFormat.length!=inputStr.length){
		return false ;
	}
	
	var formatCurChar,curChar ;
	for (var i=0;i<inputStr.length ;i++){
	    curChar = inputStr.substring(i,i+1) ;
	    formatCurChar = dataFormat.substring(i,i+1);
	   
	
	   if (formatCurChar == "S" && curChar!=" ") return false;
       if (formatCurChar == "S" && curChar ==" ") continue;
	   if (formatCurChar == "/" && curChar == "/") continue; 
	   if (formatCurChar == "-" && curChar == "-") continue; 
	   if (formatCurChar == ":" && curChar == ":") continue; 
	   if (formatCurChar == "." && curChar == ".") continue; 
	   

	   switch(formatCurChar){
	   		case "X" :if (trim(curChar)=="") return false ;break;
	   		case "N" :if (checkNumeric(curChar)==false) return false ;break;
	   		case "A" :if(chkCharacter(curChar)==false || curChar==curChar.toUpperCase()==false) return false ;break;
	   		case "a" :if(chkCharacter(curChar)==false || curChar==curChar.toLowerCase()==false) return false; break;
	   		default  :return false;
	   }
	}
	return true;
}
	
function chkCharacter(str){
	if(str.length==0){
	    	return false;
	} else{
	   	var m="";
	  	m=str.match(/[^a-zA-Z]/g);
	   	if(m!=null){
	   		return false;
   		}
    	}
	return true;
}

function checkNumeric(str){
	var m="";
	m=str.match(/[^0-9]/g);
	if(m!=null){
		return false;	
   	}
   	return true;
}
	
	function URLEncode(urlstr ){
		var SAFECHARS = "0123456789" +	"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +	"abcdefghijklmnopqrstuvwxyz" + "-_.!~*'()=?&";
		var HEX = "0123456789ABCDEF";
		var plaintext = urlstr;
		var encoded = "";
		for (var i = 0; i < plaintext.length; i++ ) {
			var ch = plaintext.charAt(i);
		    if (ch == " "){
			    encoded += "+";				
			}else if (SAFECHARS.indexOf(ch) != -1) {
			    encoded += ch;
			}else {
			    var charCode = ch.charCodeAt(0);
				if (charCode > 255) {
				    alert( "Unicode Character '" + ch + "' cannot be encoded using standard URL encoding.\n" +
					        "(URL encoding only supports 8-bit characters.)\n" +
							"A space (+) will be substituted." );
					encoded += "+";
				}else {
					encoded += "%";
					encoded += HEX.charAt((charCode >> 4) & 0xF);
					encoded += HEX.charAt(charCode & 0xF);
				}
			}
		} 
		return encoded;
	}
	
	function recreateSession(){
		var urlstr = "";
		var itemName = "";
		var frmlength = document.forms[2].length ;
		for (var j=0;j<frmlength;j++){
			if (j==0) {
				itemName=  escape(document.forms[2].item(j).name) ; 
			}else{
				itemName=  "&" + escape(document.forms[2].item(j).name) ; 
			}
			urlstr = urlstr + itemName+"="+ escape(document.forms[2].item(j).value) ;		
		}
		if (document.all) {
			var objXMLHTTP = new ActiveXObject("Microsoft.XMLHTTP"); 
		}else {
			var objXMLHTTP = new XMLHttpRequest();  
		}
		objXMLHTTP.open("POST",document.forms[2].action,false);
		var urlstring = urlstr; 
		objXMLHTTP.setRequestHeader("content-type", "application/x-www-form-urlencoded") ;
		objXMLHTTP.send(urlstring);
		returnvalue = trim(objXMLHTTP.responseText);
		returnvalue = replaceall(returnvalue, "Error 500:", "");
		returnvalue = replaceall(returnvalue, "com.hds.cms.exceptions.CMSException:", "");
		returnvalue = returnvalue.split("~");
		viewResult(returnvalue[0], returnvalue[1]);
	}
	
	function submitForm(){
		var urlstr = "" ;
		var frmlength = document.forms[0].length ;
		for (var j=0;j<frmlength;j++){
			if (j==0) {
				itemName=  escape(document.forms[0].item(j).name) ; 
			}else{
				itemName=  "&" + escape(document.forms[0].item(j).name) ; 
			}	
			urlstr = urlstr + itemName+"="+ escape(document.forms[0].item(j).value) ;		
		}

		if (document.all){
			var objXMLHTTP = new ActiveXObject("Microsoft.XMLHTTP"); 
		}else{
			var objXMLHTTP = new XMLHttpRequest();  
		}

		objXMLHTTP.open("POST",document.forms[0].action ,false);
		urlstr = urlstr; 
		objXMLHTTP.setRequestHeader("content-type", "application/x-www-form-urlencoded") ;
		objXMLHTTP.send(urlstr);
		returnvalue = trim(objXMLHTTP.responseText);
		othervalues=null;
	
		var returnarray = returnvalue.split("|");
		try{
			submitFlag	= returnarray[0];
			errorType	= returnarray[1];
			message     = returnarray[2];
		
			message		= replaceall(message,"com.hds.cms.exceptions.CMSException:","");
			grdRowNum	= 0;
			if(! (errorType=="BO") ) {
				revalidationFlag=false;
				
				try{
					if( trim(errorType) == "AC" ){

					}else if( errorType.indexOf("#") > 0 ){
						var grdColumn=errorType.split("#");
						grdRowNum = parseInt(grdColumn[0]);
						errorType = trim(grdColumn[1]);
					}else{
						var colName = trim(errorType) + "Label";
						setErrMsg(parent.document.getElementById(trim(errorType) + "Label").innerHTML + " " +message);
					} 
				}catch(ee){} 
				return false; 
			}else{

			} 
			othervalues = returnarray[3];
		}catch(e){}
		if (objXMLHTTP.statusText != "OK" ) {
			return false ; 
		}else{
			return true ; 
		} 
	}
 
	function showHttpReport(actionname){
  	    var servletname= "ReportServlet";
		var urlstr = "" ;
		var frmlength = document.forms[0].length ;
			
		for (var j=0;j<frmlength;j++){
			if (j==0) {
				itemName=  escape(document.forms[0].item(j).name) ; 
			}else{
				itemName=  "&" + escape(document.forms[0].item(j).name) ; 
			} 	
			urlstr = urlstr + itemName+"="+ escape(document.forms[0].item(j).value) ;		
		}
		
		urlstr = urlstr + "&Bean"+"="+ escape(actionname);
		servletname = servletname + "?" + urlstr;		

		reportExportReq="Y";
		
		if(reportExportReq="Y"){
			if (document.all){
				var objXMLHTTP = new ActiveXObject("Microsoft.XMLHTTP"); 
			}else{
				var objXMLHTTP = new XMLHttpRequest(); 
			}
			objXMLHTTP.open("POST",servletname,false);
			urlstr = encodeURI(""); 
			objXMLHTTP.setRequestHeader("content-type", "application/x-www-form-urlencoded") ;
			objXMLHTTP.send("");
			returnvalue = objXMLHTTP.responseText;
			if (objXMLHTTP.statusText !== "OK" ) {
				alert("ok");
				return false ; 
			}else{
				var filename = returnvalue.substring(returnvalue.lastIndexOf("//")+2,returnvalue.length);
				if(trim(filename) == REPORT_FAILURE) {
					alert(filename);
					ReportSpan.style.visibility = "hidden";
					return false;
				}
				else {
					ReportSpan.style.visibility="visible";
					document.all.ReportHref.target="_blank";
					document.all.ReportHref.href = "ReportDownLoader?repFileName=" + filename ;
					if(trim(filename) == EMAIL_REPORT_SUCCESS) {
						alert(EMAIL_REPORT_SUCCESS);
						ReportSpan.style.visibility = "hidden";
					}else{
						alert(REPORT_SUCCESS);				
					}
					document.all.ReportHref.msg =  trim(filename) ;
					return true ; 
				}
				}
		}else{
			if (document.all){
		    	window.open(servletname,'_blank','toolbar=No,width=600,height=600,left=0,top=0,resizable=yes');		
	    	}else{
		    	window.open(servletname,'_blank','toolbar=No,height=600,width=600,left=0,top=0,resizable=yes');		
	    	} 
		} 
    	
	}

	function chkFocus(){
		this.focus();
	}
	
	function showerror(rtn) {
		var aa = window.open("" , "laser" , "left =10,width = 700 , height = 500 , resizable = no" ) ;
		aa.document.write("<html><title>CMS:Error Status</title><body>"+rtn+"</body></html><br><br>") ;
		aa.focus() ;
	}
	
	function showAuthError(rtn) {
		var aa = window.open("" , "laser" , "left =10,width = 700 , height = 500 , resizable = no" ) ;
		aa.document.write("<html><title>CMS:Authorization Error Status</title><body><body><div id='ERRORREC'  style='border:2px;'></div></body></html><br><br>");
		aa.ERRORREC.innerHTML = rtn;
		aa.focus() ;
	}
	

	function resetSessions(sessionVar){
		if (document.all){
			var objXMLHTTP = new ActiveXObject("Microsoft.XMLHTTP"); 
		}else{
			var objXMLHTTP = new XMLHttpRequest();  
		}
		objXMLHTTP.open("POST","ResetLogin.jsp?"+sessionVar,false);
		urlstr = encodeURI(sessionVar); 
		objXMLHTTP.setRequestHeader("content-type", "application/x-www-form-urlencoded") ;
		objXMLHTTP.send(urlstr);
		returnvalue = objXMLHTTP.responseText;
		if (objXMLHTTP.statusText !== "OK" ) {
			return false ; 
		}else{
			return true ; 
		}
	}
		
	function indcur(tot) {
		var negflag ; 
		var wholeno ; 
		var amt ; 
		var indcur;
		var negflag;
		tot = trim(tot);
		if (isNaN(tot)==false){
			negflag=false ;
			if (parseFloat(tot) < 0){
				tot=Math.abs(parseFloat(tot));
				negflag=true;
			}else{
				tot =  parseFloat(tot);
			} 
	
			 var a1 = parseFloat(tot);
			 
			 if(String(a1).indexOf(".") < 0 ){
				 wholeno =  String(a1);
				 decpart =  "00";
			 }else{
			 	wholeno =  String(a1).substring(0,String(a1).indexOf("."));
				decpart =  String(a1).substring(String(a1).indexOf(".") + 1 );
			 } 		 		 
			 amt ="";
			 var a3=  wholeno.length ; 
			 if (a3 <=3 ){
			    amt=wholeno + "." + decpart;
	         }else if(( a3 > 3 ) && ( a3 <= 5 ) ){
				amt= wholeno.substring(0,(a3-3)) +  "," +  wholeno.substring(a3-3) + "." + decpart;
			 }else if ( (a3>5) && (a3 <=7) ){
				amt= wholeno.substring(0,a3-5) + "," +  wholeno.substring( a3-5 , a3-3 ) + "," + wholeno.substring (a3-3) + "."+ decpart;
			 }else if(a3>7){
				amt = wholeno.substring(0,a3-7) + "," +  wholeno.substring( a3-7 , a3-5 ) + "," ;
				amt+= wholeno.substring (a3-5,a3-3)+"," + wholeno.substring (a3-3) + "."+ decpart;
			 } 
	
			 if (negflag==true){
				indcur= "-" + amt ;
			 }else{
				indcur=amt ;
			 } 
		}else{
			indcur = "--" ;
		} 	
		return(indcur);
	}  
	

	function replaceall(str,findStr,replaceStr){
		var returnString =""; 
		var strArray     =str.split(findStr);
		for (var arrIndex=0 ; arrIndex < strArray.length -1 ; arrIndex++ ){
			returnString = returnString+strArray[arrIndex]+ replaceStr;
		}
		returnString = returnString + strArray[strArray.length -1];
		return returnString;
	}
	
	function replaceChatAt(str,position,replaceStr){
		if (str.length==0) return '';
		if (str.length < position) return str;
		return str.substring(0,position-1) +  replaceStr + str.substring(position,str.length);
	}
	
	
	function roundTwoDec(sum){
		sum = sum+"";
		var decimalValue = sum .split(".") ;
		if(decimalValue[1] == null ){
			sum = sum + ".00" ;
		}else{
		   switch(decimalValue[1].length){
			case 1  : sum = sum + "0" ;break;
			case 2  : sum = sum;break;
			default : sum = Math.round(sum*100) / 100 ;break;
		   }
		} 
		return sum;
	}


	function addXY(x,y){
		
		return  roundTwoDec(
							Math.round((Math.round( replaceall(x,",","") * 100) / 100 +  
							Math.round( replaceall(y,",","") * 100)/100) * 100) / 100
							);
	}
	

	function subXY(x,y){
		return  roundTwoDec(
							Math.round((Math.round( replaceall(x,",","") * 100) / 100 - 
							Math.round( replaceall(y,",","") * 100)/100) * 100) / 100
							);
	}
	
	
	
 	
	
	
var modelobj;
function processStart(msg){
	modelobj = window.showModelessDialog("modelbox.htm",msg,"dialogWidth=300px;dialogHeight=60px;scroll=no;status=no;resizable=no;help=no;edge=raised;center=yes;dialogHide=no" );
	modelobj.focus() ;
}

function processEnd(){
		try{
			modelobj.closeit() ;
		}catch(e){}
}


 



function setProgramInformation(userAccessRights,userID){

	var newOpt;
	resizeWindow();
	var userAccessArray   = userAccessRights.split("|");
	
	 


	userRightsAdd    = userAccessArray[0];
	userRightsMod    = userAccessArray[1];
	userRightsDelete = userAccessArray[2];
	userRightsView   = userAccessArray[3];
	userRightsPrint  = userAccessArray[4];
	programID        = userAccessArray[5];
	programDescn     = userAccessArray[6];
	var brnname      = userAccessArray[7];
	var version      = userAccessArray[8];
	var authReq		 = userAccessArray[9];
	ProgramName.innerText = programDescn;					                                   
    document.title = ProgramName.innerText;  
    if(userRightsAdd != null && document.all.cboUserOption != null ) {        	
		document.all.cboUserOption.innerHTML = "";
		if( userRightsAdd == 1 ){
			newOpt = document.createElement("OPTION");
			newOpt.text = "Add";
			newOpt.value = "A";
			document.all.cboUserOption.add(newOpt);
		}
		if( userRightsMod == 1 ){
			newOpt = document.createElement("OPTION");
			newOpt.text = "Modify";
			newOpt.value = "M";
			document.all.cboUserOption.add(newOpt);
		}
		if( userRightsDelete == 1 ){
			newOpt = document.createElement("OPTION");
			newOpt.text = "Delete";
			newOpt.value = "D";
			document.all.cboUserOption.add(newOpt);
		}if( userRightsView == 1 ){
			newOpt = document.createElement("OPTION");
			newOpt.text = "View";
			newOpt.value = "V";
			document.all.cboUserOption.add(newOpt);
		}if( userRightsPrint == 1 ){
		}
	}
	setUserId(userID);
	setBranchName(brnname);
	setVersion(version);
	setDate(document.getElementById("hubCBD").value);
	clock();   
	return true;	

}



function validateAmountOnKeyPress(fldAmt, negativeAllowed, decimalLength)
{
	var keyCode = window.event.keyCode;
	if((keyCode>=KEY_NUMBER_0 && keyCode<=KEY_NUMBER_9) || keyCode==KEY_DOT || keyCode==KEY_MINUS)
	{
		if(keyCode==KEY_DOT)
		{
			if(fldAmt.value.indexOf(".") != -1  || fldAmt.value == "" || decimalLength==null ||decimalLength==0)
			{
				window.event.returnValue = false;
				window.event.keyCode=0;
				return;
			}
			
		}
		if(keyCode==KEY_MINUS)
		{
			if(!negativeAllowed)
			{
				window.event.returnValue = false;
				window.event.keyCode=0;
				return;					
			}
			if(fldAmt.value.indexOf("-") != -1)
			{
				window.event.returnValue = false;
				window.event.keyCode=0;
				return;
			}

			
		}
		
	}
	else
	{

	if ((keyCode == KEY_ENTER) )
		{
		return ;
		}
		window.event.returnValue = false;
		window.event.keyCode=0;
		return ;
	}		
}

function validateAmountOnKeyUp(fldAmt, decimalLength, integerLenth, negativeAllowed)
{
	if(isValidNegative(fldAmt.value,negativeAllowed))
	{
		if(isValidDot(fldAmt.value,decimalLength))
		{
			if(isAmtNumeric(fldAmt.value,"Y",integerLenth,decimalLength, negativeAllowed))
			{
				tempAmt = fldAmt.value;
			}
			else
			{
				fldAmt.value = tempAmt;
			}
		}
		else
		{
			fldAmt.value = tempAmt;
		}
	}
	else
	{
		fldAmt.value = tempAmt;
	}
}

function isAmtNumeric(num,decFlag,intLen,decLen, negativeAllowed){
	if(num != "-" && num != ".")
	{
		if(isNaN(num))
		{
			return false;
		}
	}
	if(num.charAt(0) == "-")
	{
		intLen++;
	}
	StringLen = num.length;
	if(decFlag == "Y"){
		if(num.indexOf(".")== -1){
			if(StringLen <= intLen){
				return true;
			}else{
				return false;
			}
		}else {
			localIntLen = num.indexOf(".");
			localDecLen = (num.substring(num.indexOf(".")+1,num.length)).length;
			if ((localIntLen <= intLen) && (localDecLen <= decLen)){
				return true;			
			}else if (localIntLen > intLen) {
				return false;
			}else if (localDecLen > decLen) {
				return false;				
			}
		}
	}else {
		if(num.indexOf(".")== -1)
		{
			if(StringLen <= intLen){
				return true;
			}else{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}

function isValidDot(textValue, decimalLength){
	if(decimalLength == 0 && textValue.indexOf(".")>= 0)
	{
		return false;
	}
	if(textValue.indexOf(".") ==  textValue.lastIndexOf(".") )
	{
		return true;
	}
	else
	{
		return false;
	}
}

function isValidNegative(textValue, negativeAllowed){
	var negativeIndex = textValue.lastIndexOf("-");
	if(negativeAllowed)
	{
		if(negativeIndex <= 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		if(negativeIndex == -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}

function unFormatAmount(fldAmt)
{
	fldAmt.value = unFormat(fldAmt.value);    
}

function getAmountByField(fldAmt)
{
	return unFormat(fldAmt.value);    
}

function formatAmount(fldAmt, decimalLength)
{
	if(fldAmt.value != "")
	{
		fldAmt.value = formatNumbercurr(fldAmt.value, decimalLength);
	}	
}
function formatNumbercurr(num1,decLen) {
	var x;
	var k;
	var indexCounter = 0;
	j=0;
	num1 = unFormat(num1);
	if(amtFormatType.toUpperCase()=="L"){
		return lakhFormat(num1);
	}else{
		newnumString = "";
		dotPos = num1.indexOf(".");
		
		if(dotPos >=0) {
			intPart = num1.substring(0,dotPos);
			decPart = num1.substring(dotPos+1,num1.length);
			
			k=eval(decLen-decPart.length);
			
			if(k<decLen || decPart==""){
				for(i=1;i<=k;i++){
						decPart=decPart+"0";
					}
					decPart = "."+decPart;
				} else {
				decPart = "."+decPart;
			}
			StringLen = intPart.length;
			
		} else {
			intPart = num1.substring(0,num1.length);
			decPart="";
			if(decLen>0)
			{
				for(i=1;i<=decLen;i++){
				decPart = decPart+"0";
				}
				decPart="."+decPart;
			}
			StringLen = intPart.length;
		}
		
		for(x=StringLen-1; x>=0; x--) {
			indexCounter++;
			if(indexCounter == 4) {
				if(num1.charAt(x) != "-")
				{
					numreplace=num1.charAt(x)+",";
					newnumString = numreplace+newnumString;
					indexCounter = 1;
				}
				else
				{
					newnumString = num1.charAt(x)+newnumString;
				}
			} else {
				newnumString = num1.charAt(x)+newnumString;
			}
		}
		if(dotPos==0)
		{
			newnumString = "0" + newnumString;
		}
		if(newnumString.charAt(0)=="") {
		}
		if(newnumString.charAt(0)=="," || (newnumString.charAt(0)=="0" && dotPos >1) ) {
			newnumString=newnumString.substring(1,newnumString.length)+decPart;
			num1 = newnumString;
			return trimNumber(newnumString,decLen);
		} else {
			newnumString=newnumString+decPart;
			num1 = newnumString;
			return trimNumber(newnumString,decLen);
		}	
	}	  
}

function unFormat(num)
{
	newUnFormatStr="";
	for(i=0;i<num.length;i++) {
		if(num.charAt(i)!= ",") {
			newUnFormatStr=newUnFormatStr+num.charAt(i);
		}
		
	}
	var numLen;
	num = newUnFormatStr;
	numLen = num.length;	
	for(i=0;i<numLen;i++) {
		if(num.charAt(0)== "0" && num.indexOf(".") >1) {
			num=num.substring(1,num.length);
		}
	}	
	return num;	
}

function trimNumber(inputNum,decLen) {
	var totLen;
	decLen=parseInt(decLen);
	dotPos = inputNum.indexOf(".");
	totLen = eval(dotPos+decLen+1);
	if(inputNum.indexOf(".") >=0) {
		intPart = inputNum.substring(0,dotPos);
		decPart = inputNum.substring(dotPos+1,inputNum.length);
		decPart = "."+decPart;
		StringLen = intPart.length;
	} else {
		intPart = inputNum.substring(0,inputNum.length);
		decPart = "";
		StringLen = intPart.length;
	}
	if(decPart.length > decLen) {
		decPart = inputNum.substring(dotPos+1,totLen);
		decPart = "."+decPart;
	}
	inputNum = intPart+decPart;
	return inputNum;
}
	function SETBGCOLOR(OBJ,newval,oldval)
	{
		if(trim(newval)!=trim(oldval))
		{
			OBJ.style.backgroundColor = NEWCOLOR;
		}
	
	}

function setAmount(fldAmtName, amount){
	fldAmt = document.getElementById(fldAmtName)
	fldAmt.value = unFormat(amount);
	fldAmt.onblur();
}


function nvl(str1,str2){
	if(str1==null) 	   return str2; 
	if(trim(str1)=='') return str2; 
	return str1;
}

function setEnteredDetails(modBy,modOn,entBy,entOn,authBy,authOn){
	document.all.EntedByuserid.innerHTML 	= entBy;
	document.all.EntedOnDate.innerHTML 		= entOn;
	document.all.AuthByuserid.innerHTML 	= authBy;
	document.all.AuthOnDate.innerHTML 		= authOn;
	document.all.ModByuserid.innerHTML 		= modBy;
	document.all.ModOnDate.innerHTML 		= modOn;
	
}


function email(y){ 
        var setat = 0;
        var setdot = 0; 
        var str1;
        var str2;
        str1=y;
        str = y; 
        str2=y;
        if(str != ""){ 
           if (!((str != "") && (str.indexOf(".") != -1) && (str.indexOf("@") != -1))){  
               return false; 
            }
        }
        for(var i=0;i<str.length;i++) { 
                if(str.charAt(i) == '@'){    
 					str1=str.substring(i,str.length);
                    setat = setat + 1;
                 }       
                      
         }            
        for(var j=0;j<str1.length;j++)
         {
                if(str1.charAt(j)=='.')
                 {
                     setdot = setdot+1; 
                  }
         }   
               
         str2=str.charAt(str.length-1); 
             
        if(setat>1 || setdot>2 ||str2=='.') 
         { 
                return false; 
          }      
       
} 


function getCharAtValue(gridObj,row,col,pos){
	var flagValue=gridObj.cells(row,col).getValue();
	return flagValue.substring(parseInt(pos)-1,parseInt(pos));
}


function formatNumbercurrunit(num1,sUnits) {
	var chkFlg = 0;
	var x;
	var k;
	j=0;
	
	if (sUnits >= 10000) {
		decLen = 4;
	}else if ((sUnits >= 1000) && (sUnits < 10000)) {
		decLen = 3;
	}else if ((sUnits != 0) && (sUnits < 1000)){
		decLen = 2;
	}else if (sUnits == 0) {
		decLen = 0;
	}
	if (num1.substring(0,1) == "-") {
		chkFlg = 1;
		num1 = num1.substring(1,num1.length);
	}
	
	refString = "000,000,000,000,000";
	refStrIndex = refString.length-1;
	newRefIndex = refString.length-1;
	newnumString = "";
	dotPos = num1.indexOf(".");
	if(num1.indexOf(".") >=0) {
		intPart = num1.substring(0,dotPos);
		decPart = num1.substring(dotPos+1,num1.length);
		k=eval(decLen-decPart.length);
		if(k <= 0) k=decLen;
		if(k<decLen || decPart==""){
			for(i=1;i<=k;i++){
				decPart=decPart+"0";
			}
			decPart = "."+decPart;
		}else{
			decPart="." + decPart.substring(0,decLen);
		}
		StringLen = intPart.length;
	} else {
		intPart = num1.substring(0,num1.length);
		decPart="";
		
		for(i=1;i<=decLen;i++){
		decPart = decPart+"0";
		}
		decPart="."+decPart;
		StringLen = intPart.length;
	}

	for(x=StringLen-1; x>=0; x--) {
		refStrIndex = eval(newRefIndex - j);
		if(refString.charAt(refStrIndex) == ",") {
			numreplace=num1.charAt(x)+",";
			newnumString = numreplace+newnumString;
			newRefIndex=newRefIndex-1;
		} else {
			newnumString = num1.charAt(x)+newnumString;
		}
		j=j+1;
	}

	if(newnumString.charAt(0)=="") {
	}
	if(newnumString.charAt(0)=="," || newnumString.charAt(0)=="0") {
		newnumString=newnumString.substring(1,newnumString.length)+decPart;
	} else {
		newnumString=newnumString+decPart;
	}
	 
	num1 = newnumString;
	if (chkFlg == 1) {
		num1 = "-" + num1;
	}
	if (num1 == 0) {
		num1 = "0.00";
	}
	
	return trimNumber(num1);		
}



function formatAmount(fldAmt, decimalLength)
{
	if(fldAmt.value != "")
	{
		fldAmt.value = formatNumbercurr(fldAmt.value, decimalLength);
	}	
	return fldAmt.value;
}

function getSubUnits(SubUnitsAmount){
	SubUnitsAmount=parseInt(""+SubUnitsAmount.length)-1;
	return SubUnitsAmount;
}

function focusTextArea(fldName){
   document.getElementById(fldName).focus();
  
	var range = document.getElementById(fldName).createTextRange();
	range.move("textedit");
	range.select();
}

function validateTextArea(fldName, rows, cols, functionpointer){
	var textarray = document.getElementById(fldName).value.split("\n");
	if(window.event.keyCode == KEY_TAB || window.event.keyCode == 13)
	{
	
		if(textarray[textarray.length-1] != "" && textarray.length < rows)
		{
			setErrMsg("");
			return;
		}	
		else
		{
			setErrMsg("");
			
			window.event.returnValue = false;
			window.event.keyCode=0;
			eval(functionpointer);
			return;
		}
	}
	currrow = getCursorPos(fldName) - 1;
	if(textarray[currrow].length >= cols && window.event.keyCode != 13)
	{
		setErrMsg("only " + cols + " characters allowed in a line, use return key to go to next line");
		window.event.returnValue = false;
		window.event.keyCode=0;
		return;
	}
}

function getCursorPos(textElement) 
	{
  	var sOldText = document.getElementById(textElement).value;
    var  sOldArr = document.getElementById(textElement).value.split("\n");
    var  linepos =0;
	  var objRange = document.selection.createRange();
	  var sOldRange = objRange.text;
	  
	
	
	  var sWeirdString = '#%~';
	
	  objRange.text = sOldRange + sWeirdString; 
	  objRange.moveStart('character', (0 - sOldRange.length - sWeirdString.length));
	
	  var sNewText = document.getElementById(textElement).value;
	
	  objRange.text = sOldRange;
	
	  for (i=0; i <= sNewText.length; i++) {
	    var sTemp = sNewText.substring(i, i + sWeirdString.length);
	    if (sTemp == sWeirdString) {
	      var cursorPos = (i - sOldRange.length);
	      
          
	     for (j=0; j <= (sOldArr.length)-1 ; j++)
	      {
	         	      
	      	linepos = linepos + sOldArr[j].length ; 
	      	if (cursorPos <= linepos)
	      	{
	      	   
	      	 	return j+1 ;
	      	} 
	      } 
	         return j;
	    }
	  }
}

function getCursorPos(textElement) 
	{
  	var sOldText = document.getElementById(textElement).value;
    var  sOldArr = document.getElementById(textElement).value.split("\n");
    var  linepos =0;
	  var objRange = document.selection.createRange();
	  var sOldRange = objRange.text;
	  
	
	
	  var sWeirdString = '#%~';
	
	  objRange.text = sOldRange + sWeirdString; 
	  objRange.moveStart('character', (0 - sOldRange.length - sWeirdString.length));
	
	  var sNewText = document.getElementById(textElement).value;
	
	  objRange.text = sOldRange;
	
	  for (i=0; i <= sNewText.length; i++) {
	    var sTemp = sNewText.substring(i, i + sWeirdString.length);
	    if (sTemp == sWeirdString) {
	      var cursorPos = (i - sOldRange.length);
	      
          
	     for (j=0; j <= (sOldArr.length)-1 ; j++)
	      {
	         	      
	      	linepos = linepos + sOldArr[j].length ; 
	      	if (cursorPos <= linepos)
	      	{
	      	   
	      	 	return j+1 ;
	      	} 
	      } 
	         return j;
	    }
	  }
}

	function exitPara(frmName) {
		frmName = document.forms[0].name.toLowerCase();
		var serverConObj = new fieldValidator("Log");
		var arguments = frmName;
		serverConObj.clearMap();
		serverConObj.setValue("Method","exitOprLog");
		serverConObj.setValue("Args",arguments);
		serverConObj.sendAndReceive();
	}


	function showErrorInWindow(othervaluesparam,rowdelimit,coldelimit,caption) {
	
		var splitarray = othervaluesparam.split(rowdelimit);
		var rowcount = splitarray.length;
		var tablerowcol = "";
		tablerowcol = tablerowcol + "<table>";
		for(var rownum=0;rownum<rowcount ;rownum++) {
			tablerowcol = tablerowcol +"<tr>";
			splitarray1 = splitarray[rownum].split(coldelimit)			
			for(var rownum1=0;rownum1<splitarray1.length ;rownum1++) {
				tablerowcol = tablerowcol + "<td>";
				tablerowcol = tablerowcol + trim(splitarray1[rownum1]);
				tablerowcol = tablerowcol + "</td>";
			} 
			tablerowcol = tablerowcol +"</tr>";
		} 
		tablerowcol = tablerowcol +"</table>"


	
		var aa = window.open("" , "laser" , "left =10,width = 700 , height = 500 , resizable = no" ) ;
		aa.document.write("<html><title>"+caption+"</title><body><body><div id='ERRORREC'  style='border:2px;'></div></body></html><br><br>");
		aa.ERRORREC.innerHTML = tablerowcol;
		aa.focus() ;
	}
	
	function addDays(days,date,dateseperator){
		var w_Date;
		var w_Day;
		var w_Mon;
		var w_Year;
		var w_NewDay;
		var w_NewMon;
		var w_NewDate;
		var w_LeapYr;
		var w_AddVal;
		w_Date = date;
		w_AddVal = days;
		sptArr = w_Date.split(dateseperator);
		w_Day = sptArr[0];
		w_Mon = sptArr[1];
		w_Year = sptArr[2];
		w_NewDay = eval(w_Day) + eval(w_AddVal);
		if ((w_Mon == "01") || (w_Mon == "03") || (w_Mon == "05") || (w_Mon == "07") || (w_Mon == "08") || (w_Mon == "10") || (w_Mon == "12")) {
			while (w_NewDay > 31 ) {
				w_NewDay = w_NewDay - getDays(w_Mon , w_Year);
				w_Mon++;
				if (w_Mon > 12 ) {
					w_Mon = w_Mon - 12;
					w_Year++;
				}
			}
		}else if ((w_Mon == "04") || (w_Mon == "06") || (w_Mon == "09") || (w_Mon == "11")) {
			while (w_NewDay > 30 ) {
				w_NewDay = w_NewDay - getDays(w_Mon , w_Year);
				w_Mon++;
				if (w_Mon > 12 ) {
					w_Mon = w_Mon - 12;
					w_Year++;
				}
			}
		
		}else if (w_Mon == "02") {
			w_LeapYr = chkLeapYr(w_Year);
			while (w_NewDay > w_LeapYr ) {
				w_NewDay = w_NewDay - getDays(w_Mon , w_Year);
				w_Mon++;
				if (w_Mon > 12 ) {
					w_Mon = w_Mon - 12;
					w_Year++;
				}
			}
		
		}
		
		w_NewDay = checkLen(w_NewDay);
		w_NewMon = checkLen(w_Mon);
		w_NewDate = w_NewDay + dateseperator + w_NewMon + dateseperator + w_Year;
		return w_NewDate;
	}
	function checkLen(newVal) {
		var w_Len;
		var w_NewVal;
		w_Len = newVal.toString().length;
		w_NewVal =newVal.toString(); 
		
		if (w_Len == "1") {
			w_NewVal = "0" + w_NewVal; 	
			return (w_NewVal);
		}else {
			return (newVal.toString());
		}
	}
	
	function chkLeapYr(year){
		if (year % 4 == 0 ) {
			return "29";
		} else {
			return "28";
		}
	}
	
	function getDays(Mon,Year){
		var w_LeapYear;
		if ((Mon == "01") || (Mon == "03") || (Mon == "05") || (Mon == "07") || (Mon == "08") || (Mon == "10") || (Mon == "12")) {
			return "31";
		}else if ((Mon == "04") || (Mon == "06") || (Mon == "09") || (Mon == "11")) {
			return "30";
		}else if (Mon == "02") {
			w_LeapYear = chkLeapYr(Year);
			return w_LeapYear;
		}
	}


	
	function setAmountType(amountType){
		amtFormatType=amountType;
	}
	
	function lakhFormat(value){
		var signflag=""; 
		var len;
		var currvalue;
		var currlen;
		var amt;
		var decpart ; 
		var wholeno ;
		var nowlen;
		var nowvalue;
		value = trim(value);
		value=replaceall(value,",","");
		if(value.substring(0,1)=='-'){
			signflag = value.substring(0,1);
			value = value.substring(1,value.length);
		}
		var a1=value;
		if(String(a1).indexOf(".") < 0 ){
		    wholeno =  String(a1);
			decpart =  "00";
		 }else{
		 	wholeno =  String(a1).substring(0,String(a1).indexOf("."));
			decpart =  String(a1).substring(String(a1).indexOf(".") + 1 );
		 } 	
		 len=wholeno.length;	
		 
		 if (len <=3 ){
			    amt=wholeno + "." + decpart;
	      }else{
			 amt=','+wholeno.substring(len-3);
			 currvalue=wholeno.substring(0,len-3);
			 currlen=len-3;
			 nowlen=currlen;
			 for(var i=currlen;i>3;i=i-2){
			 	amt=','+currvalue.substring(i-2,i)+amt;
			 	nowlen=i-2;
			 }
			 if(nowlen==3){
			 	nowvalue=currvalue.substring(0,3);
			 	amt= nowvalue.substring(0,1) +  "," + nowvalue.substring(1,3) + amt;
			 }else{
			    nowvalue=currvalue.substring(0,2);
			 	amt= nowvalue.substring(0,2) + amt;
			 } 
			 amt=amt+ "."+ decpart;
		 } 
		 
		 
		 if(signflag!=""){
		 	return (amt);
		 }else{	
		 	return(signflag+""+amt); 
		 }	

	}

   	var PER_CHECK="Should Not Be Greater Than 100";
      	function isAlphaNumeric(text){
		var textLength=text.length;
		for(i=0;i<textLength;i++){
			if((text.charCodeAt(i)>=65 && text.charCodeAt(i)<=90)|| (text.charCodeAt(i)>=97 && text.charCodeAt(i)<=122)|| (text.charCodeAt(i)>=48 && text.charCodeAt(i)<=57)){
			
			}else{
				return false;
				}
		}
		return true;	
	}
