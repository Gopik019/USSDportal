var CSRFTOKEN = 'CSRFTOKEN';
var AJAX_TOKEN = 'AJAX_TOKEN';
var COLUMN_ENABLE = '1';
var COLUMN_DISABLE = '0';

var INVALID_CLICK_CHECK = "0"; 
var ctrlkey_cnt=0;

var frmeName=top.frames.name;

window.history.forward(1);
	function noBack()
  { 
  window.history.forward(); 
  }
window.history.forward(1);


function Get_Cookie( check_name ) {


	var a_all_cookies = document.cookie.split( ';' );
	var a_temp_cookie = '';
	var cookie_name = '';
	var cookie_value = '';
	var b_cookie_found = false; 

	for ( i = 0; i < a_all_cookies.length; i++ )
	{

		a_temp_cookie = a_all_cookies[i].split( '=' );



		cookie_name = a_temp_cookie[0].replace(/^\s+|\s+$/g, '');


		if ( cookie_name == check_name )
		{
			b_cookie_found = true;

			if ( a_temp_cookie.length > 1 )
			{
				cookie_value = unescape( a_temp_cookie[1].replace(/^\s+|\s+$/g, '') );
			}

			return cookie_value;
			break;
		}
		a_temp_cookie = null;
		cookie_name = '';
	}
	if ( !b_cookie_found )
	{
		return null;
	}
}


function Delete_Cookie( name, path, domain ) {
if ( Get_Cookie( name ) ) document.cookie = name + "=" +
( ( path ) ? ";path=" + path : "") +
( ( domain ) ? ";domain=" + domain : "" ) + ";expires=Thu, 01-Jan-1970 00:00:01 GMT";
}

var _userAgent = navigator.userAgent.toLowerCase();
var _version = (_userAgent.match( /.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/ ) || [])[1];
var _safari = /webkit/.test( _userAgent );
var _opera = /opera/.test(_userAgent);
var _msie = /msie/.test( _userAgent ) && !/opera/.test( _userAgent );
var _mozilla = /mozilla/.test( _userAgent ) && !/(compatible|webkit)/.test(_userAgent);

function getVersion(){
	return _version;
}

function getUserAgent(){
	return _userAgent;
}
function isSafari(){
	return _safari;
}

function isOpera(){
	return _opera;
}

function isIE()	{
	return _msie;
}

function isMozilla(){
	return _mozilla;
}


var statusMsg="InTouch";

setInterval("updateStatusBar()",3000);

function updateStatusBar()
{
	window.status = statusMsg;
}


var _fnkey = false;
function dostopevent(event) {
	try	{
		event.stopPropagation();
	}catch(e){}
	try	{
		event.preventDefault();
	}catch(e){}
	try {
		window.event.keyCode = 0;
	}catch(e)   {}
	try {
		window.event.returnValue = false;
	}catch(e){}
}
function dokeydown(obj,event){
	_event = (window.event) ? window.event : event;
	var key = isPressKeyCode(_event);
	var pressedKey = String.fromCharCode(event.keyCode).toLowerCase();
	var shift = _event.shiftKey;
	var ctrl = _event.ctrlKey;

	if(obj.id !=='tpwd' && obj.id !=='tconfirmpwd'  && obj.id !=='txtPwd'){
	if(key==59 || key==61 || key==106 || key==107 || key==109|| key==111  || key==186 || key==187 
	 || key == 190 && obj.id !=='temail'
	|| key == 189 && obj.id !=='temail' && obj.type!=="textarea"|| key==191 || key==192 || key >=219 && key<=222 || key == 188 && obj.id !=='txtusrnamedets' ){
		dostopevent(_event);
		}
	
	if(shift){
	if (key == 50 && obj.id=='temail'||key == 173 && obj.id=='temail' ||key == 189 && obj.id=='temail' && obj.type=="textarea") {
       		  var d = event.srcElement || event.target;
              doPrevent = d.readOnly || d.disabled;
           		}
	if(key==45 || key==49 ||key == 50 && obj.id!=='temail' || key >=51 && key <=57 || key==48 || key==173|| key==61 || key==192 || key==106 || key==107 || key==109 || key==111 || 
			 key==188 || key==190 || key==191 || key==59 || key==222
			|| key==219 || key==220 || key==221 ){
		dostopevent(_event);
		}
	}

	}

   var doPrevent = false;
    if (key === 8) {
        var d = event.srcElement || event.target;
        if ((d.tagName.toUpperCase() === 'INPUT' && (d.type.toUpperCase() === 'TEXT' || d.type.toUpperCase() === 'PASSWORD' || d.type.toUpperCase() === 'FILE')) 
             || d.tagName.toUpperCase() === 'TEXTAREA') {
            doPrevent = d.readOnly || d.disabled;
        }
        else {
            doPrevent = true;
        }
    }
    if (doPrevent) {
        dostopevent(_event);
    }

     
	try{
	if (obj.readOnly==true){
		dostopevent(_event);
	}
	}catch(e){}
	if(_event.metaKey)
		_ctrl = _event.metaKey;
	if(ctrl)	{	
		dostopevent(_event);
	}else	{

		if(key == KEY_ENTER){
			
		}

		if(key == KEY_TAB || key == KEY_F5 || key == KEY_F11)    {
			_fnkey = true;
			dostopevent(_event);
			var id = null;
			try{
				id = obj.id;
			}catch(e){
			}
			if(obj != null && id != undefined && id != null && id != "")   {
				if(key == KEY_TAB)	{
					if(shift)	{
						BACKWARD(id);
					}else{
						FORWARD(id);
					}
				}
			}
		}else	{
			_fnkey = false;
		}
		if(!isOpera())  {
			if(_fnkey)    {
				dostopevent(_event);
				return false;
			}
		}
	}
	return true;
}
function dokeydown1(obj,event){
	_event = (window.event) ? window.event : event;
	var key = isPressKeyCode(_event);
	var pressedKey =String.fromCharCode(event.keyCode).toLowerCase();
	var shift = _event.shiftKey;
	var ctrl = _event.ctrlKey;
	if(key==192||key==111||key==106||key==107||key==109||key==61||key==188||key==191||key==59||key==222
	 ||key==219||key==220||key==221||key==186||key==187){dostopevent(_event);}
	if(shift){
	if(key==45||key==49||key >=51&&key<=57||key==48||key==61||key==192||key==106||key==107||key==111||key==188||key==190||
      key==191||key==59||key==222||key==219||key==220||key==221 ){dostopevent(_event);}
	}

	var doPrevent = false;
   if (key === 8) {var d = event.srcElement || event.target;if ((d.tagName.toUpperCase() === 'INPUT' && (d.type.toUpperCase() === 'TEXT' || d.type.toUpperCase() === 'PASSWORD' || d.type.toUpperCase() === 'FILE')) 
   || d.tagName.toUpperCase() === 'TEXTAREA') {doPrevent = d.readOnly || d.disabled;}else {doPrevent = true;}}if (doPrevent) {dostopevent(_event);}
   try{if (obj.readOnly==true){dostopevent(_event);}}catch(e){}
   if(_event.metaKey)_ctrl = _event.metaKey;if(ctrl){dostopevent(_event);}else{if(key == KEY_ENTER){}if(key == KEY_TAB || key == KEY_F5 || key == KEY_F11)    {
   _fnkey = true;dostopevent(_event);var id = null;try{id = obj.id;}catch(e){}if(obj != null && id != undefined && id != null && id != ""){if(key == KEY_TAB)	{
   if(shift){BACKWARD(id);}else{FORWARD(id);}}}}else{_fnkey = false;}if(!isOpera()){if(_fnkey){dostopevent(_event);return false;}}}
   return true;}

var oprlogsl=0;
var objHttpOperVal;

function doload(event,ldpgm){
    operlogProgramEntry(ldpgm); 

	startScrolling(); 	
	LOADER();
	return true;
}


function dounload(event,ldpgm){


	operlogProgramExit(ldpgm);
	
}

function dokeypress(obj,event){
	event = (window.event) ? window.event : event;
	

	var FF = !(window.mozInnerScreenX == null);
            

   if (FF && document.getElementById('chkDissolvedate') ) {
	      if(document.getElementById('chkDissolvedate').checked ){
           dostopevent(event); 
           return true;
		  }
      }
		 

	if(isOpera() || isSafari() || isMozilla()) {
		if(_fnkey)    {
			dostopevent(event);
			_fnkey = false;
			return false;
		}
	}else{
		if(_fnkey)    {
			dostopevent(event);
			_fnkey = false;
			return false;
		}
	}
	return true;
}
function dovalidate(obj,event){
	var shift = _event.shiftKey;
	if(shift =_event.shiftKey )
	{
	
	}else{
		VALIDATE(obj.id);
	}
	document.getElementById(obj.id).style.backgroundColor = "";
	
	
}


function myFocusFunction(id) 
{
	document.getElementById(id.id).style.backgroundColor = "aqua";
	
}
				
				
document.oncontextmenu=new Function("return false");

function docontextmenu(obj,event)	{

	return false;
}

function doclick(obj,event)	{

	event = (window.event) ? window.event : event;

		if (event.button == 2) {

			return true;
		}
		


		if(isIE())
		{
			if (event.button == 4) {
				alert("Middle click is disabled");
				return false;
			}
			if(event.ctrlKey==1){
			    alert("Ctrl key is disabled.!");
			}
			if(event.shiftKey==1){
				alert("Shift key + Click is disabled");
				return false;
			}
		}
		
		
		if(isMozilla())
		{
			if (event.which == 2 || event.ctrlKey==1 || event.shiftKey==1) {      
				if(INVALID_CLICK_CHECK == "0"){
					INVALID_CLICK_CHECK = "1";
					alert("This click is not allowed ! Repeating it will log you out of the system");
					return false;
				}
				else{    
					alert("Please login again");
					window.location.href='../InTouch/unauth.jsp';
				}
			}	
		}


	
	if (document.all) {
		if (event.button == 2) {

			return true;
		}
	}
	if (document.layers) {
		if (e.which == 3) {

			return true;
		}
	}	
}



var statusMsg="InTouch";

var character=1;



function startScrolling() 
{
	window.status=statusMsg.substring(0, character);
	if (character >= statusMsg.length) 
	{
		character=1;
		window.setTimeout("stopScrolling()",300); 
	} 
	else
	{
		character++;
		window.setTimeout("startScrolling()",50); 
   } 
}
function stopScrolling() 
{
	window.status=statusMsg.substring(character, statusMsg.length);
	if (character >= statusMsg.length) 
	{
		character=1;
		window.setTimeout("startScrolling()", 500);
	} 
	else
	{
		character++;
		window.setTimeout("stopScrolling()", 50);   
	}
}


function domouseover(obj,event){
	try{


		window.status = "InTouch";

	}
	catch (e){}
	return true;
}

function domousemove(obj,event){
	try{

		window.status = "InTouch";

	}
	catch (e){}
	return true;

}

function setHelp(id,msg){
	document.getElementById(id).innerHTML = "<span style='color:black'>" + msg + "</span>"; 
}

function setError(id,msg){
	document.getElementById(id).innerHTML = "<span style='color:red'>" + msg + "</span>"; 
}

function clearError(id)	{
	document.getElementById(id).innerHTML = "";	
}
	
			
				function _grid_addFunction(fref)	{
				fref();
				}
			function _grid_modifyFunction(fref,gridvar)	{

				var ros = "";
				if(gridvar == undefined || gridvar == null || !gridvar){
					ros = mygrid.getCheckedRows(0);
				}else{
					ros = gridvar.getCheckedRows(0);
				}
				var rosaerr = ros.split(",");
				if(ros == "")	{
					alert("Select a row to modify");
				}else{
					if(rosaerr.length > 1)	{
						alert("Select only one row");
					}else	{
						fref(rosaerr[0]);
					}
				}				
			}
			function _grid_clearFunction(gridvar,grididbackup)	{
			 if(confirm("Do you want to clear the grid?"))	{
					gridvar.clearAll();
				}
				document.getElementById(grididbackup).value = "";
			}
			function _grid_deleteFunction(gridvar,grididbackup)	{
			 	var ros = gridvar.getCheckedRows(0);
				var rosaerr = ros.split(",");
				if(ros == "")	{
					alert("Select a row to delete");
				}else{
					if(confirm("Do you want to delete the selected rows?"))	{
						for(i=0;i<rosaerr.length;i++){
							gridvar.deleteRow(rosaerr[i]);
						}
					}
				}
				document.getElementById(grididbackup).value = "";
			}
		
				

		var FORM_DIV = document.createElement('DIV');
		FORM_DIV.id = 'mdl_form_cover';
		FORM_DIV.innerHTML = "<table style='position:absolute;top:40%;left:45%;height:5%;font-size:small;font-family:tahoma;font-weight:bold;color:white'><tr><td><img src='images/progress.gif'></img>&nbsp;&nbsp;<span id='msg' style='position:relative;top:-12px;'>Processing Request...</span></td><tr></table>";
		var FORM_IFRAME = document.createElement('IFRAME');
		FORM_IFRAME.src = "formblank.html";
		FORM_IFRAME.className = "iframe-hidden";
		var CURRENT_TIMEOUT_PERIOD = 5000 ;
		
		function SFM()  {
		    FORM_DIV.visibility = "visible";
		    FORM_DIV.className = "form-modal-visible";
		    document.body.appendChild(FORM_IFRAME);
		    document.body.appendChild(FORM_DIV);
		    disableSelection(document.getElementById('mdl_form_cover'));
		}
		
		
		function HFM()  {
		    FORM_DIV.className = "form-modal-hidden";
		    document.body.removeChild(FORM_IFRAME);
		    document.body.removeChild(FORM_DIV);
		}
		
		
		function disableSelection(target){
		    if (typeof target.onselectstart!="undefined")
		        target.onselectstart=function(){return false};
		    else if (typeof target.style.MozUserSelect!="undefined")
		        target.style.MozUserSelect="none";
		    else
		        target.onmousedown=function(){return false};
		    target.style.cursor = "default";
		}
		
	var KEY_ENTER = 13;
	var KEY_F2 = 113;
	var KEY_ESC = 27;
	var KEY_TAB = 9;
	var KEY_F5 = 116;
	var KEY_F1 = 112;
	var KEY_F3 = 114;
	var KEY_F12=123;
	var KEY_F11 = 122;
	var KEY_F10 = 121;
	var KEY_LEFTARROW = 37;
	var KEY_RIGHTARROW = 39;
	var KEY_UPPERCASE_A = 65;
	var KEY_UPPERCASE_Z = 90;
	var KEY_LOWERCASE_A = 97;
	var KEY_LOWERCASE_Z = 122;
	var KEY_SPACE = 32;
	var KEY_NUMBER_0 = 48;
	var KEY_NUMBER_9 = 57;
	var KEY_COMMA = 188;
	var KEY_F9	= 9;
	var KEY_HYPHEN = 45;
	var KEY_FWDSLASH = 47;
	var KEY_DOT = 46;
	var KEY_BACKSPACE = 8;
	var KEY_DELETE = 46; 	
		

	var calendar;     
	var valueObject;  
	var divObject;    
	
	
	var convSubUnit='1000';
	var convSubUnit=getSubUnits(convSubUnit);	
	var amtFormatType='L';


	function calendarObject(divID,sesMcontDate){
		divObject = document.getElementById(divID);
		calendar = new dhtmlxCalendarObject(divID,true,{isYearEditable: true,isMonthEditable:true});
		calendar.setOnClickHandler(selectDate);
		calendar.setDateFormat("%d-%m-%Y");
		calendar.setDate(sesMcontDate);
	}

	function selectDate(date) {
			
		valueObject.value = calendar.getFormatedDate(null,date);
		divObject.style.display = 'none';
		if(!valueObject.readOnly) valueObject.focus();
	}
	
	function showCalendar(thisObj) {
		if( valueObject != null && valueObject  == thisObj && divObject.style.display == 'block') calendar.hide();
		else{
			divObject.style.left = thisObj.offsetWidth + getAbsoluteLeft(thisObj)+(parseInt(25))+"px";
			divObject.style.top = getAbsoluteTop(thisObj)+"px";
			divObject.style.position = "absolute";
			divObject.style.display = 'block';
			valueObject = thisObj;
		}
	}
	

	function hideCalendar(){
		calendar.hide();
	}


	function isPressKeyCode(evt){
		evt = (evt) ? evt : (window.event) ? event : null;
		  if (evt){
			    var charCode = (evt.charCode) ? evt.charCode :((evt.keyCode) ? evt.keyCode :((evt.which) ? evt.which : 0));
			    return charCode;
  		  }
  		  return 0;
	}
	

	function getSubUnits(SubUnitsAmount){
		SubUnitsAmount=parseInt(""+SubUnitsAmount.length)-1;
		return SubUnitsAmount;
	}
	

	function imageHelp(thisObj,evt){   
		evt = (evt)? evt :(window.event) ? event : null;
		evt.returnValue=false;
		callHelp(thisObj);
		
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
			decpart =  String(a1).substring(String(a1).indexOf(".")+1);
			decpart = decpart.substring(0,2);
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

		 

		 return(signflag+""+amt); 
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
	
	
	function unFormat(num){
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
	
	/*
	function trim(str) {
		var resultStr = "";
		resultStr = trimLeft(str);
		resultStr = trimRight(resultStr);
		return resultStr;
	} 
	
	
	
	Object.prototype.trim =  function() {
		var str = this;
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

	
	
	Object.prototype.isNumeric =  function() { 
	 var sText = this;
	 if (isNaN(sText)==false){
	 	return true;
	 }else{
	 	return false;
	 }	
    }
*/
	
	function replaceall(str,findStr,replaceStr){
		var returnString =""; 
		var strArray     =str.split(findStr);
		for (var arrIndex=0 ; arrIndex < strArray.length -1 ; arrIndex++ ){
			returnString = returnString+strArray[arrIndex]+ replaceStr;
		}
		returnString = returnString + strArray[strArray.length -1];
		return returnString;
	}
	
	
	function focusTextArea(fldName)
	{
	   document.getElementById(fldName).focus();
	   var range = document.getElementById(fldName).createTextRange();
	   range.move("textedit");
	   range.select();
	}

	function validateTextArea(fldName, rows, cols, functionpointer,keyCode)
	{
		var textarray = document.getElementById(fldName).value.split("\n");
		if(keyCode == KEY_TAB || keyCode == 13)
		{
			if(textarray[textarray.length-1] != "" && textarray.length < rows)
			{
				return;
			}	
			else
			{
				event.returnValue = false;
				keyCode=0;
				eval(functionpointer);
				return;
			}
		}
		
		currrow = getCursorPos(fldName) - 1;
		if(textarray[currrow].length >= cols && window.event.keyCode != 13)
		{
			alert("only " + cols + " characters allowed in a line, use return key to go to next line");
			event.returnValue = false;
			event.keyCode=0;
			return;
		}
	}
	
	
	function getCursorPos(textElement) 
	{
	  	var sOldText = document.getElementById(textElement).value;
	    var sOldArr = document.getElementById(textElement).value.split("\n");
	    var linepos =0;
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
	
	
	
	function isNumeric(keycode,decFlag){ 
		if((keycode >= KEY_NUMBER_0 && keycode <=KEY_NUMBER_9) || (keycode==KEY_BACKSPACE)|| (keycode==KEY_TAB))
			return true;	
		else{
			if(decFlag == "Y"){
				if ((keycode>=KEY_NUMBER_0 && keycode<=KEY_NUMBER_9) || (keycode == KEY_DOT) || (keycode==KEY_BACKSPACE)|| (keycode==KEY_TAB)){
					if(isDotPressed(keycode))
						return true;
					else
						return false;
				}
			}else{
				if(keycode == KEY_TAB)
					return true;
				else{
					window.event.keyCode = 0;
					return false;	
				}
			}		
		}		
	}
	
	
	function isDotPressed(keycode,strVal){
		if(keycode == KEY_DOT){
			return true;		
		}else{
			window.event.keyCode = 0;
			return false;
		}			
	}
	
	
	function isAlpha(ch){
	    if(ch >= KEY_UPPERCASE_A && ch <=KEY_UPPERCASE_Z || ch>=KEY_LOWERCASE_A && ch<=KEY_LOWERCASE_Z)
	        return true;
	    else
	        return false;
	}
	
	
	
	


	function isSpace(ch){
	    if(ch == KEY_SPACE)
	        return true;
	    else
	        return false;
	}
	
	

function spacecheck(value) {  
  	str=new String(value);
  	var m="";
  	m=str.match(/[^0-9a-zA-Z\s]/g);
  	return m;
}	



function isAlphaNumeric(value) {  
  	var flag = false;
	str=new String(value);
  	var k="";
  	k=str.match(/[^0-9a-zA-Z]/g);
  	if(k!=null){
  		flag = false;
	}else{	
		flag = true;
	}
	return flag;
}


function allowSomeSpecialChars(value) {  
  	var flag = false;
	str=new String(value);
  	var k="";
  	k=str.match(/[^0-9a-zA-Z\s\[\]\(\)\#\-\;\,\.\']/g);
  	if(k!=null){
  		flag = false;
	}else{	
		flag = true;
	}
	return flag;
}



function specialcharcheck(elem)
 {  
  str=new String(elem);
  var m="";
  m=str.match(/[^0-9a-zA-Z\,\:\;\/\.\s]/g);  
  if(m!=null)
   {
	 
	   return false;
	 }
  return true;	 
 }


	var eventObj;
	var w_gridobj;
	
     function hideHelp() {
		document.getElementById("helpframe").style.visibility="hidden";
	}
	

	function showHelp(helpToken,SearchTextObj,strArgs,ReturnCol,allbrnreq) {	
	  if (arguments.length < 5) 
			allbrnreq = 0;
	  else
		    allbrnreq =allbrnreq;
		if (arguments.length < 4) 
			ReturnCol = 0;
		else
		{
			if(ReturnCol=="")
				ReturnCol = 0;
			else
				ReturnCol = ReturnCol -1;
		}	
			
		if (strArgs == null) {
			strArgs = "" ; 
		}
		w_gridobj = false;
		
		try{
			if 	(SearchTextObj.name == "GRID")
			{
			w_gridobj = true;
			}
	
		}catch(e){}
		
		
		eventObj = SearchTextObj ;
		document.getElementById("helpframe").style.visibility="visible" ;		
		if (w_gridobj == false)
		{
			helpframe.help(helpToken,SearchTextObj.value,strArgs,ReturnCol,allbrnreq) ;
		}else 
		{
			helpframe.help(helpToken,eventObj.cells(eventObj.currrow,parent.eventObj.currcol).getValue(),strArgs,ReturnCol,allbrnreq) ;
		}	
	}
	
	function registerAddInfo(strReturnFormat){
		helpframe.registerAddInfo(strReturnFormat);
	}
	
	function setSearchType(serchtype) {
		helpframe.setSearchType(serchtype);
	}
	function unregisterAddInfo(){
	   helpframe.unregisterAddInfo();
	}
	function eventobjectfocus() {
		eventObj.focus();
	}
	
				document.onhelp= function(){
				callHelp(window.event.srcElement);
				return false;
			}
			
function isSecondOpenParen(textValue){
    var j=0;
    for (i = 0;  i < textValue.length;  i++){
		ch = textValue.charAt(i);
		if(ch=="(")
			j=j+1;
		if(j > 1)
		   break;
		   
    }
    if(j>1)
      	return true;
    else  
    	return false;
}
function isSecondCloseParen(textValue){
    var j=0;
    for (i = 0;  i < textValue.length;  i++){
		ch = textValue.charAt(i);
		if(ch==")")
			j=j+1;
		if(j > 1)
		   break;
		   
    }
    if(j>1)
      	return true;
    else  
    	return false;
}
function isSecondHypen(textValue){
	var j=0;
    for (i = 0;  i < textValue.length;  i++){
		ch = textValue.charAt(i);
		if(ch=="-")
			j=j+1;
		if(j > 1)
		   break;
		   
    }
    if(j>1)
      	return true;
    else  
    	return false;
}
function isSecondPlus(textValue){
    var j=0;
    for (i = 0;  i < textValue.length;  i++){
		ch = textValue.charAt(i);
		if(ch=="+")
			j=j+1;
		if(j > 1)
		   break;
		   
    }
    if(j>1)
      	return true;
    else  
    	return false;
}
function checkEmail(w_ObjVal)
{
	if(w_ObjVal.search(/^[a-z0-9]+((\.|!|_|\+|\-)[a-z0-9]+)*@([a-z0-9]+(\.|\-))+[a-z0-9]{2,}$/i)!= -1)
   	{
		return true;
   	}else{
		return false;
	}	
}



	function isLower(text){
	    var textLength=text.length;
		for(i=0;i<textLength;i++){
			if(text.charCodeAt(i)<97||text.charCodeAt(i)>122)
				return false;
		}
		return true;
	}



function des (message, encrypt, mode, iv, padding) {
	key="this is a 24 byte key !!";
  var spfunction1 = new Array (0x1010400,0,0x10000,0x1010404,0x1010004,0x10404,0x4,0x10000,0x400,0x1010400,0x1010404,0x400,0x1000404,0x1010004,0x1000000,0x4,0x404,0x1000400,0x1000400,0x10400,0x10400,0x1010000,0x1010000,0x1000404,0x10004,0x1000004,0x1000004,0x10004,0,0x404,0x10404,0x1000000,0x10000,0x1010404,0x4,0x1010000,0x1010400,0x1000000,0x1000000,0x400,0x1010004,0x10000,0x10400,0x1000004,0x400,0x4,0x1000404,0x10404,0x1010404,0x10004,0x1010000,0x1000404,0x1000004,0x404,0x10404,0x1010400,0x404,0x1000400,0x1000400,0,0x10004,0x10400,0,0x1010004);
  var spfunction2 = new Array (-0x7fef7fe0,-0x7fff8000,0x8000,0x108020,0x100000,0x20,-0x7fefffe0,-0x7fff7fe0,-0x7fffffe0,-0x7fef7fe0,-0x7fef8000,-0x80000000,-0x7fff8000,0x100000,0x20,-0x7fefffe0,0x108000,0x100020,-0x7fff7fe0,0,-0x80000000,0x8000,0x108020,-0x7ff00000,0x100020,-0x7fffffe0,0,0x108000,0x8020,-0x7fef8000,-0x7ff00000,0x8020,0,0x108020,-0x7fefffe0,0x100000,-0x7fff7fe0,-0x7ff00000,-0x7fef8000,0x8000,-0x7ff00000,-0x7fff8000,0x20,-0x7fef7fe0,0x108020,0x20,0x8000,-0x80000000,0x8020,-0x7fef8000,0x100000,-0x7fffffe0,0x100020,-0x7fff7fe0,-0x7fffffe0,0x100020,0x108000,0,-0x7fff8000,0x8020,-0x80000000,-0x7fefffe0,-0x7fef7fe0,0x108000);
  var spfunction3 = new Array (0x208,0x8020200,0,0x8020008,0x8000200,0,0x20208,0x8000200,0x20008,0x8000008,0x8000008,0x20000,0x8020208,0x20008,0x8020000,0x208,0x8000000,0x8,0x8020200,0x200,0x20200,0x8020000,0x8020008,0x20208,0x8000208,0x20200,0x20000,0x8000208,0x8,0x8020208,0x200,0x8000000,0x8020200,0x8000000,0x20008,0x208,0x20000,0x8020200,0x8000200,0,0x200,0x20008,0x8020208,0x8000200,0x8000008,0x200,0,0x8020008,0x8000208,0x20000,0x8000000,0x8020208,0x8,0x20208,0x20200,0x8000008,0x8020000,0x8000208,0x208,0x8020000,0x20208,0x8,0x8020008,0x20200);
  var spfunction4 = new Array (0x802001,0x2081,0x2081,0x80,0x802080,0x800081,0x800001,0x2001,0,0x802000,0x802000,0x802081,0x81,0,0x800080,0x800001,0x1,0x2000,0x800000,0x802001,0x80,0x800000,0x2001,0x2080,0x800081,0x1,0x2080,0x800080,0x2000,0x802080,0x802081,0x81,0x800080,0x800001,0x802000,0x802081,0x81,0,0,0x802000,0x2080,0x800080,0x800081,0x1,0x802001,0x2081,0x2081,0x80,0x802081,0x81,0x1,0x2000,0x800001,0x2001,0x802080,0x800081,0x2001,0x2080,0x800000,0x802001,0x80,0x800000,0x2000,0x802080);
  var spfunction5 = new Array (0x100,0x2080100,0x2080000,0x42000100,0x80000,0x100,0x40000000,0x2080000,0x40080100,0x80000,0x2000100,0x40080100,0x42000100,0x42080000,0x80100,0x40000000,0x2000000,0x40080000,0x40080000,0,0x40000100,0x42080100,0x42080100,0x2000100,0x42080000,0x40000100,0,0x42000000,0x2080100,0x2000000,0x42000000,0x80100,0x80000,0x42000100,0x100,0x2000000,0x40000000,0x2080000,0x42000100,0x40080100,0x2000100,0x40000000,0x42080000,0x2080100,0x40080100,0x100,0x2000000,0x42080000,0x42080100,0x80100,0x42000000,0x42080100,0x2080000,0,0x40080000,0x42000000,0x80100,0x2000100,0x40000100,0x80000,0,0x40080000,0x2080100,0x40000100);
  var spfunction6 = new Array (0x20000010,0x20400000,0x4000,0x20404010,0x20400000,0x10,0x20404010,0x400000,0x20004000,0x404010,0x400000,0x20000010,0x400010,0x20004000,0x20000000,0x4010,0,0x400010,0x20004010,0x4000,0x404000,0x20004010,0x10,0x20400010,0x20400010,0,0x404010,0x20404000,0x4010,0x404000,0x20404000,0x20000000,0x20004000,0x10,0x20400010,0x404000,0x20404010,0x400000,0x4010,0x20000010,0x400000,0x20004000,0x20000000,0x4010,0x20000010,0x20404010,0x404000,0x20400000,0x404010,0x20404000,0,0x20400010,0x10,0x4000,0x20400000,0x404010,0x4000,0x400010,0x20004010,0,0x20404000,0x20000000,0x400010,0x20004010);
  var spfunction7 = new Array (0x200000,0x4200002,0x4000802,0,0x800,0x4000802,0x200802,0x4200800,0x4200802,0x200000,0,0x4000002,0x2,0x4000000,0x4200002,0x802,0x4000800,0x200802,0x200002,0x4000800,0x4000002,0x4200000,0x4200800,0x200002,0x4200000,0x800,0x802,0x4200802,0x200800,0x2,0x4000000,0x200800,0x4000000,0x200800,0x200000,0x4000802,0x4000802,0x4200002,0x4200002,0x2,0x200002,0x4000000,0x4000800,0x200000,0x4200800,0x802,0x200802,0x4200800,0x802,0x4000002,0x4200802,0x4200000,0x200800,0,0x2,0x4200802,0,0x200802,0x4200000,0x800,0x4000002,0x4000800,0x800,0x200002);
  var spfunction8 = new Array (0x10001040,0x1000,0x40000,0x10041040,0x10000000,0x10001040,0x40,0x10000000,0x40040,0x10040000,0x10041040,0x41000,0x10041000,0x41040,0x1000,0x40,0x10040000,0x10000040,0x10001000,0x1040,0x41000,0x40040,0x10040040,0x10041000,0x1040,0,0,0x10040040,0x10000040,0x10001000,0x41040,0x40000,0x41040,0x40000,0x10041000,0x1000,0x40,0x10040040,0x1000,0x41040,0x10001000,0x40,0x10000040,0x10040000,0x10040040,0x10000000,0x40000,0x10001040,0,0x10041040,0x40040,0x10000040,0x10040000,0x10001000,0x10001040,0,0x10041040,0x41000,0x41000,0x1040,0x1040,0x40040,0x10000000,0x10041000);
  var keys = des_createKeys (key);
  var m=0, i, j, temp, temp2, right1, right2, left, right, looping;
  var cbcleft, cbcleft2, cbcright, cbcright2
  var endloop, loopinc;
  var len = message.length;
  var chunk = 0;
  var iterations = keys.length == 32 ? 3 : 9; 
  if (iterations == 3) {looping = encrypt ? new Array (0, 32, 2) : new Array (30, -2, -2);}
  else {looping = encrypt ? new Array (0, 32, 2, 62, 30, -2, 64, 96, 2) : new Array (94, 62, -2, 32, 64, 2, 30, -2, -2);}

  if (padding == 2) message += "        "; 
  else if (padding == 1) {temp = 8-(len%8); message += String.fromCharCode (temp,temp,temp,temp,temp,temp,temp,temp); if (temp==8) len+=8;} 
  else if (!padding) message += "\0\0\0\0\0\0\0\0"; 

  result = "";
  tempresult = "";

  if (mode == 1) { 
    cbcleft = (iv.charCodeAt(m++) << 24) | (iv.charCodeAt(m++) << 16) | (iv.charCodeAt(m++) << 8) | iv.charCodeAt(m++);
    cbcright = (iv.charCodeAt(m++) << 24) | (iv.charCodeAt(m++) << 16) | (iv.charCodeAt(m++) << 8) | iv.charCodeAt(m++);
    m=0;
  }

  while (m < len) {
    left = (message.charCodeAt(m++) << 24) | (message.charCodeAt(m++) << 16) | (message.charCodeAt(m++) << 8) | message.charCodeAt(m++);
    right = (message.charCodeAt(m++) << 24) | (message.charCodeAt(m++) << 16) | (message.charCodeAt(m++) << 8) | message.charCodeAt(m++);

    if (mode == 1) {if (encrypt) {left ^= cbcleft; right ^= cbcright;} else {cbcleft2 = cbcleft; cbcright2 = cbcright; cbcleft = left; cbcright = right;}}

    temp = ((left >>> 4) ^ right) & 0x0f0f0f0f; right ^= temp; left ^= (temp << 4);
    temp = ((left >>> 16) ^ right) & 0x0000ffff; right ^= temp; left ^= (temp << 16);
    temp = ((right >>> 2) ^ left) & 0x33333333; left ^= temp; right ^= (temp << 2);
    temp = ((right >>> 8) ^ left) & 0x00ff00ff; left ^= temp; right ^= (temp << 8);
    temp = ((left >>> 1) ^ right) & 0x55555555; right ^= temp; left ^= (temp << 1);

    left = ((left << 1) | (left >>> 31)); 
    right = ((right << 1) | (right >>> 31)); 

    for (j=0; j<iterations; j+=3) {
      endloop = looping[j+1];
      loopinc = looping[j+2];
      for (i=looping[j]; i!=endloop; i+=loopinc) { 
        right1 = right ^ keys[i]; 
        right2 = ((right >>> 4) | (right << 28)) ^ keys[i+1];
        temp = left;
        left = right;
        right = temp ^ (spfunction2[(right1 >>> 24) & 0x3f] | spfunction4[(right1 >>> 16) & 0x3f]
              | spfunction6[(right1 >>>  8) & 0x3f] | spfunction8[right1 & 0x3f]
              | spfunction1[(right2 >>> 24) & 0x3f] | spfunction3[(right2 >>> 16) & 0x3f]
              | spfunction5[(right2 >>>  8) & 0x3f] | spfunction7[right2 & 0x3f]);
      }
      temp = left; left = right; right = temp; 
    } 

    left = ((left >>> 1) | (left << 31)); 
    right = ((right >>> 1) | (right << 31)); 

    temp = ((left >>> 1) ^ right) & 0x55555555; right ^= temp; left ^= (temp << 1);
    temp = ((right >>> 8) ^ left) & 0x00ff00ff; left ^= temp; right ^= (temp << 8);
    temp = ((right >>> 2) ^ left) & 0x33333333; left ^= temp; right ^= (temp << 2);
    temp = ((left >>> 16) ^ right) & 0x0000ffff; right ^= temp; left ^= (temp << 16);
    temp = ((left >>> 4) ^ right) & 0x0f0f0f0f; right ^= temp; left ^= (temp << 4);

    if (mode == 1) {if (encrypt) {cbcleft = left; cbcright = right;} else {left ^= cbcleft2; right ^= cbcright2;}}
    tempresult += String.fromCharCode ((left>>>24), ((left>>>16) & 0xff), ((left>>>8) & 0xff), (left & 0xff), (right>>>24), ((right>>>16) & 0xff), ((right>>>8) & 0xff), (right & 0xff));

    chunk += 8;
    if (chunk == 512) {result += tempresult; tempresult = ""; chunk = 0;}
  } 

  return result + tempresult;
}


function des_createKeys (key) {
  pc2bytes0  = new Array (0,0x4,0x20000000,0x20000004,0x10000,0x10004,0x20010000,0x20010004,0x200,0x204,0x20000200,0x20000204,0x10200,0x10204,0x20010200,0x20010204);
  pc2bytes1  = new Array (0,0x1,0x100000,0x100001,0x4000000,0x4000001,0x4100000,0x4100001,0x100,0x101,0x100100,0x100101,0x4000100,0x4000101,0x4100100,0x4100101);
  pc2bytes2  = new Array (0,0x8,0x800,0x808,0x1000000,0x1000008,0x1000800,0x1000808,0,0x8,0x800,0x808,0x1000000,0x1000008,0x1000800,0x1000808);
  pc2bytes3  = new Array (0,0x200000,0x8000000,0x8200000,0x2000,0x202000,0x8002000,0x8202000,0x20000,0x220000,0x8020000,0x8220000,0x22000,0x222000,0x8022000,0x8222000);
  pc2bytes4  = new Array (0,0x40000,0x10,0x40010,0,0x40000,0x10,0x40010,0x1000,0x41000,0x1010,0x41010,0x1000,0x41000,0x1010,0x41010);
  pc2bytes5  = new Array (0,0x400,0x20,0x420,0,0x400,0x20,0x420,0x2000000,0x2000400,0x2000020,0x2000420,0x2000000,0x2000400,0x2000020,0x2000420);
  pc2bytes6  = new Array (0,0x10000000,0x80000,0x10080000,0x2,0x10000002,0x80002,0x10080002,0,0x10000000,0x80000,0x10080000,0x2,0x10000002,0x80002,0x10080002);
  pc2bytes7  = new Array (0,0x10000,0x800,0x10800,0x20000000,0x20010000,0x20000800,0x20010800,0x20000,0x30000,0x20800,0x30800,0x20020000,0x20030000,0x20020800,0x20030800);
  pc2bytes8  = new Array (0,0x40000,0,0x40000,0x2,0x40002,0x2,0x40002,0x2000000,0x2040000,0x2000000,0x2040000,0x2000002,0x2040002,0x2000002,0x2040002);
  pc2bytes9  = new Array (0,0x10000000,0x8,0x10000008,0,0x10000000,0x8,0x10000008,0x400,0x10000400,0x408,0x10000408,0x400,0x10000400,0x408,0x10000408);
  pc2bytes10 = new Array (0,0x20,0,0x20,0x100000,0x100020,0x100000,0x100020,0x2000,0x2020,0x2000,0x2020,0x102000,0x102020,0x102000,0x102020);
  pc2bytes11 = new Array (0,0x1000000,0x200,0x1000200,0x200000,0x1200000,0x200200,0x1200200,0x4000000,0x5000000,0x4000200,0x5000200,0x4200000,0x5200000,0x4200200,0x5200200);
  pc2bytes12 = new Array (0,0x1000,0x8000000,0x8001000,0x80000,0x81000,0x8080000,0x8081000,0x10,0x1010,0x8000010,0x8001010,0x80010,0x81010,0x8080010,0x8081010);
  pc2bytes13 = new Array (0,0x4,0x100,0x104,0,0x4,0x100,0x104,0x1,0x5,0x101,0x105,0x1,0x5,0x101,0x105);

  var iterations = key.length > 8 ? 3 : 1; 
  var keys = new Array (32 * iterations);
  var shifts = new Array (0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0);
  var lefttemp, righttemp, m=0, n=0, temp;

  for (var j=0; j<iterations; j++) { 
    left = (key.charCodeAt(m++) << 24) | (key.charCodeAt(m++) << 16) | (key.charCodeAt(m++) << 8) | key.charCodeAt(m++);
    right = (key.charCodeAt(m++) << 24) | (key.charCodeAt(m++) << 16) | (key.charCodeAt(m++) << 8) | key.charCodeAt(m++);

    temp = ((left >>> 4) ^ right) & 0x0f0f0f0f; right ^= temp; left ^= (temp << 4);
    temp = ((right >>> -16) ^ left) & 0x0000ffff; left ^= temp; right ^= (temp << -16);
    temp = ((left >>> 2) ^ right) & 0x33333333; right ^= temp; left ^= (temp << 2);
    temp = ((right >>> -16) ^ left) & 0x0000ffff; left ^= temp; right ^= (temp << -16);
    temp = ((left >>> 1) ^ right) & 0x55555555; right ^= temp; left ^= (temp << 1);
    temp = ((right >>> 8) ^ left) & 0x00ff00ff; left ^= temp; right ^= (temp << 8);
    temp = ((left >>> 1) ^ right) & 0x55555555; right ^= temp; left ^= (temp << 1);

    temp = (left << 8) | ((right >>> 20) & 0x000000f0);
    left = (right << 24) | ((right << 8) & 0xff0000) | ((right >>> 8) & 0xff00) | ((right >>> 24) & 0xf0);
    right = temp;

    for (i=0; i < shifts.length; i++) {
      if (shifts[i]) {left = (left << 2) | (left >>> 26); right = (right << 2) | (right >>> 26);}
      else {left = (left << 1) | (left >>> 27); right = (right << 1) | (right >>> 27);}
      left &= -0xf; right &= -0xf;

      lefttemp = pc2bytes0[left >>> 28] | pc2bytes1[(left >>> 24) & 0xf]
              | pc2bytes2[(left >>> 20) & 0xf] | pc2bytes3[(left >>> 16) & 0xf]
              | pc2bytes4[(left >>> 12) & 0xf] | pc2bytes5[(left >>> 8) & 0xf]
              | pc2bytes6[(left >>> 4) & 0xf];
      righttemp = pc2bytes7[right >>> 28] | pc2bytes8[(right >>> 24) & 0xf]
                | pc2bytes9[(right >>> 20) & 0xf] | pc2bytes10[(right >>> 16) & 0xf]
                | pc2bytes11[(right >>> 12) & 0xf] | pc2bytes12[(right >>> 8) & 0xf]
                | pc2bytes13[(right >>> 4) & 0xf];
      temp = ((righttemp >>> 16) ^ lefttemp) & 0x0000ffff; 
      keys[n++] = lefttemp ^ temp; keys[n++] = righttemp ^ (temp << 16);
    }
  } 
  return keys;
} 




function stringToHex (s) {
  var r = "0x";
  var hexes = new Array ("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f");
  for (var i=0; i<s.length; i++) {r += hexes [s.charCodeAt(i) >> 4] + hexes [s.charCodeAt(i) & 0xf];}
  return r;
}	

function isZero(val)	{
	if(val == undefined || val == 0)  {
		return true;
	}
	return false;
}		
function checkDate(text){
		var textLength=text.length;
		for(i=0;i<textLength;i++){
			if((text.charCodeAt(i)<48||text.charCodeAt(i)>57)){
              if(text.charCodeAt(i)!=45){
               return false;
              } 
            }   
		}
		return true;	
	}
	function randomString() {
		var chars = "ABCDEFGHIJKLMNOPQRSTUVWXTZ";
		var string_length = 4;
		var randomstring = '';
		for (var i=0; i<string_length; i++) {
			var rnum = Math.floor(Math.random() * chars.length);
			randomstring += chars.substring(rnum,rnum+1);
		}
		return randomstring;
	 }
	 function randomNumbers() {
		var chars = "0123456789";
		var string_length = 5;
		var randomnumber = '';
		for (var i=0; i<string_length; i++) {
			var rnum = Math.floor(Math.random() * chars.length);
			randomnumber += chars.substring(rnum,rnum+1);
		}
		return randomnumber;
	 }	
	
	function pinGeneration(w_fldObj)
	 {
	
	   var uname,pin,randAlpha,randNum,len,username="";
	   uname=w_fldObj;
	   len=6;
	   randAlpha=randomString();
	   randNum=randomNumbers();
	   for(i=0;i<uname.length;i++)
	   {
	    if(!uname.substring(i,i+1).match(/[^0-9a-zA-Z]/g))
	    {
	   		 
	   		 username=username+uname.substring(i,i+1);
	    }
	   }
	  
	   pin=username.substring(0,6)+randAlpha+randNum;
	   return pin;
	 }
	
	function checkAmt(inputAmt){
		var flag = false;
		str=new String(inputAmt);
	  	var k="";
	  	k=str.match(/[^0-9\,\.]/g);
	  	if(k!=null){
	  		flag = false;
		}else{	
			flag = true;
		}
		return flag;
	}	
	
	
	
	var calendar1;      
	var valueObject1;  
	var divObject1;     
	


function calendarObject1(divID,sesMcontDate){
		divObject1 = document.getElementById(divID);
		calendar1 = new dhtmlxCalendarObject(divID,true,{isYearEditable: true,isMonthEditable:true});
		calendar1.setOnClickHandler(selectDate1);
		calendar1.setDateFormat("%d-%m-%Y");
		calendar1.setDate(sesMcontDate);
	}



function selectDate1(date) {
			
		valueObject1.value = calendar1.getFormatedDate(null,date);
		divObject1.style.display = 'none';
		if(!valueObject1.readOnly) valueObject1.focus();
	}

function showCalendar1(thisObj) {
		if( valueObject1 != null && valueObject1  == thisObj && divObject1.style.display == 'block') calendar1.hide();
		else{
			divObject1.style.left = thisObj.offsetWidth + getAbsoluteLeft(thisObj)+(parseInt(25))+"px";
			divObject1.style.top = getAbsoluteTop(thisObj)+"px";
			divObject1.style.position = "absolute";
			divObject1.style.display = 'block';
			valueObject1 = thisObj;
		}
	}
	
	
	
	var calendar2;      
	var valueObject2;  
	var divObject2;     
	


function calendarObject2(divID,sesMcontDate){
		divObject2 = document.getElementById(divID);
		calendar2 = new dhtmlxCalendarObject(divID,true,{isYearEditable: true,isMonthEditable:true});
		calendar2.setOnClickHandler(selectDate2);
		calendar2.setDateFormat("%d-%m-%Y");
		calendar2.setDate(sesMcontDate);
	}



function selectDate2(date) {
			
		valueObject2.value = calendar2.getFormatedDate(null,date);
		divObject2.style.display = 'none';
		if(!valueObject2.readOnly) valueObject2.focus();
	}

function showCalendar2(thisObj) {
		if( valueObject2 != null && valueObject2  == thisObj && divObject2.style.display == 'block') calendar2.hide();
		else{
			divObject2.style.left = thisObj.offsetWidth + getAbsoluteLeft(thisObj)+(parseInt(25))+"px";
			divObject2.style.top = getAbsoluteTop(thisObj)+"px";
			divObject2.style.position = "absolute";
			divObject2.style.display = 'block';
			valueObject2 = thisObj;
		}
	}
	
	
	var calendar3;      
	var valueObject3;  
	var divObject3;     
	


function calendarObject3(divID,sesMcontDate){
		divObject3 = document.getElementById(divID);
		calendar3 = new dhtmlxCalendarObject(divID,true,{isYearEditable: true,isMonthEditable:true});
		calendar3.setOnClickHandler(selectDate3);
		calendar3.setDateFormat("%d-%m-%Y");
		calendar3.setDate(sesMcontDate);
	}



function selectDate3(date) {
			
		valueObject3.value = calendar3.getFormatedDate(null,date);
		divObject3.style.display = 'none';
		if(!valueObject3.readOnly) valueObject3.focus();
	}

function showCalendar3(thisObj) {
		if( valueObject3 != null && valueObject3  == thisObj && divObject3.style.display == 'block') calendar3.hide();
		else{
			divObject3.style.left = thisObj.offsetWidth + getAbsoluteLeft(thisObj)+(parseInt(25))+"px";
			divObject3.style.top = getAbsoluteTop(thisObj)+"px";
			divObject3.style.position = "absolute";
			divObject3.style.display = 'block';
			valueObject3 = thisObj;
		}
	}
	
function spacecheck1(value) {  
  	str=new String(value);
  	var m="";
  	m=str.match(/[^0-9a-zA-Z]/g);
  	return m;
}	
function isBlank1(val)	{
	if(val == undefined || val == null || val == "")	{
		return true;
	}
	return false;
}

function isValidAccountNumber(value) {  
  	var flag = false;
	str=new String(value);
  	var k="";
  	k=str.match(/[^0-9a-zA-Z\-\/]/g);
  	if(k!=null){
  		flag = false;
	}else{	
		flag = true;
	}
	return flag;
}





function permutationGenerator(nNumElements) {
this.nNumElements     = nNumElements;
this.antranspositions = new Array;
var k = 0;
for (i = 0; i < nNumElements - 1; i++)
for (j = i + 1; j < nNumElements; j++)
this.antranspositions[ k++ ] = ( i << 8 ) | j;
this.nNumtranspositions = k;
this.fromCycle = permutationGenerator_fromCycle;
}
function permutationGenerator_fromCycle(anCycle) {
var anpermutation = new Array(this.nNumElements);
for (var i = 0; i < this.nNumElements; i++) anpermutation[i] = i;
for (var i = 0; i < anCycle.length; i++) {
var nT = this.antranspositions[anCycle[i]];
var n1 = nT & 255;
var n2 = (nT >> 8) & 255;
nT = anpermutation[n1];
anpermutation[n1] = anpermutation[n2];
anpermutation[n2] = nT;
}
return anpermutation;
}
function password(strpasswd) {
this.strpasswd = strpasswd;
this.getHashValue   = password_getHashValue;
this.getpermutation = password_getpermutation;
}
function password_getHashValue() {
var m = 907633409;
var a = 65599;
var h = 0;
for (var i = 0; i < this.strpasswd.length; i++) 
h = (h % m) * a + this.strpasswd.charCodeAt(i);
return h;
}
function password_getpermutation() {
var nNUMELEMENTS = 13;
var nCYCLELENGTH = 21;
pg = new permutationGenerator(nNUMELEMENTS);
var anCycle = new Array(nCYCLELENGTH);
var npred   = this.getHashValue();
for (var i = 0; i < nCYCLELENGTH; i++) {
npred = 314159269 * npred + 907633409;
anCycle[i] = npred % pg.nNumtranspositions;
}
return pg.fromCycle(anCycle);
}
function SecureContext(strText, strSignature, bEscape) {
this.strSIGNATURE = strSignature || '';
this.bESCApE      = bEscape || false;
this.strText = strText;
this.escape        = SecureContext_escape;
this.unescape      = SecureContext_unescape;
this.transliterate = SecureContext_transliterate;
this.encypher      = SecureContext_encypher;
this.decypher      = SecureContext_decypher;
this.sign          = SecureContext_sign;
this.unsign        = SecureContext_unsign;
this.secure   = SecureContext_secure;
this.unsecure = SecureContext_unsecure;
}
function SecureContext_escape(strToEscape) {
var strEscaped = '';
for (var i = 0; i < strToEscape.length; i++) {
var chT = strToEscape.charAt( i );
switch(chT) {
case '\r': strEscaped += '\\r'; break;
case '\n': strEscaped += '\\n'; break;
case '\\': strEscaped += '\\\\'; break;
default: strEscaped += chT;
   }
}
return strEscaped;
}
function SecureContext_unescape(strToUnescape) {
var strUnescaped = '';
var i = 0;
while (i < strToUnescape.length) {
var chT = strToUnescape.charAt(i++);
if ('\\' == chT) {
chT = strToUnescape.charAt( i++ );
switch( chT ) {
case 'r': strUnescaped += '\r'; break;
case 'n': strUnescaped += '\n'; break;
case '\\': strUnescaped += '\\'; break;
default: 
   }
}
else strUnescaped += chT;
}
return strUnescaped;
}
function SecureContext_transliterate(btransliterate) {
var strDest = '';

var nTextIter  = 0;
var nTexttrail = 0;

while (nTextIter < this.strText.length) {
var strRun = '';
var cSkipped   = 0;
while (cSkipped < 7 && nTextIter < this.strText.length) {
var chT = this.strText.charAt(nTextIter++);
if (-1 == strRun.indexOf(chT)) {
strRun += chT;
cSkipped = 0;
}
else cSkipped++;
}
while (nTexttrail < nTextIter) {
var nRunIdx = strRun.indexOf(this.strText.charAt(nTexttrail++));
if (btransliterate) {
nRunIdx++
if (nRunIdx == strRun.length) nRunIdx = 0;
}
else {
nRunIdx--;
if (nRunIdx == -1) nRunIdx += strRun.length;
}
strDest += strRun.charAt(nRunIdx);
   }
}
this.strText = strDest;
}
function SecureContext_encypher(anperm) {
var strEncyph = '';

var nCols     = anperm.length;
var nRows     = this.strText.length / nCols;
for (var i = 0; i < nCols; i++) {
var k = anperm[ i ];
for (var j = 0; j < nRows; j++) {
strEncyph += this.strText.charAt(k);
k         += nCols;
   }
}
this.strText = strEncyph;
}
function SecureContext_decypher(anperm) {
var nRows    = anperm.length;
var nCols    = this.strText.length / nRows;
var anRowOfs = new Array;
for (var i = 0 ; i < nRows; i++) anRowOfs[ anperm[ i ] ] = i * nCols;
var strplain = '';
for (var i = 0; i < nCols; i++) {
for (var j = 0; j < nRows; j++)
strplain += this.strText.charAt(anRowOfs[ j ] + i);
}
this.strText = strplain;
}
function SecureContext_sign(nCols) {
if (this.bESCApE) {
this.strText      = this.escape(this.strText);
this.strSIGNATURE = this.escape(this.strSIGNATURE);
}
var nTextLen     = this.strText.length + this.strSIGNATURE.length;
var nMissingCols = nCols - (nTextLen % nCols);
var strpadding   = '';  
if (nMissingCols < nCols)
for (var i = 0; i < nMissingCols; i++) strpadding += ' ';
var x = this.strText.length;
this.strText +=  strpadding + this.strSIGNATURE;
}
function SecureContext_unsign(nCols) {
if (this.bESCApE) {
this.strText      = this.unescape(this.strText);
this.strSIGNATURE = this.unescape(this.strSIGNATURE);
}
if ('' == this.strSIGNATURE) return true;
var nTextLen = this.strText.lastIndexOf(this.strSIGNATURE);
if (-1 == nTextLen) return false;
this.strText = this.strText.substr(0, nTextLen);
return true;
}
function SecureContext_secure(strpasswd) {
var passwd = new password(strpasswd);
var anperm   = passwd.getpermutation()
this.sign(anperm.length);
this.transliterate(true);
this.encypher(anperm);
}
function SecureContext_unsecure(strpasswd) {
var passwd = new password(strpasswd);
var anperm = passwd.getpermutation()
this.decypher(anperm);
this.transliterate(false);
return this.unsign(anperm.length);
}

function doEncrypt(encryptText,encryptionKey) {
	var sc = new SecureContext(encryptText,"", "1");
	sc.secure(encryptionKey);
	return sc.strText;
}

function doDecrypt(decryptText,decryptionKey) {
	var sc = new SecureContext(decryptText,"", "1");
	if (!sc.unsecure(decryptionKey)) 
		alert('Invalid password used.');
	return sc.strText;
}