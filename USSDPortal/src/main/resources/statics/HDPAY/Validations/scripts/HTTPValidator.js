function xmlHTTPValidator(){ 
	var xmlDOM = null; 
	var validatorAction = null; 
	if (document.all) {
		 
		xmlDOM = new ActiveXObject("Microsoft.XMLDOM") ;
		xmlDOM.loadXML("<Data><Record></Record></Data>");
	}else{
		var objDOMParser = new DOMParser();
		xmlDOM = objDOMParser.parseFromString("<Data><Record></Record></Data>", "text/xml");
		xmlDOM.normalize();
	}
	this.XMLDOM = xmlDOM;
	this.getValue = getValueX ; 
	this.setValue = setValueX ; 
	this.containsKey = containsKeyX ; 
	this.containsValue = containsValueX ; 
	this.sendAndReceive = sendAndReceiveX ; 
	this.submitForm = submitFormX ;
	this.clearMap = clearMapX;
	this.getAllXML = getAllXMLX;
	this.callReport = callReportX ;
	this.viewReport = viewReportX ;
	
	function clearMapX(){
		if (document.all) {
			xmlDOM.loadXML("<Data><Record></Record></Data>");
		}else{
			while (xmlDOM.documentElement.hasChildNodes())
  			xmlDOM.documentElement.removeChild(xmlDOM.documentElement.lastChild);
  			xmlDOM.documentElement.appendChild(xmlDOM.createElement("Record")) ;
		}
	}
	function getValueX(KeyName) {
		if (document.all) {
			try{
				return xmlDOM.documentElement.childNodes(0).getElementsByTagName(KeyName)(0).text ;
			}catch(e){
				return "";
			}
		}else{
		try{
			return xmlDOM.documentElement.childNodes[0].getElementsByTagName(KeyName)[0].firstChild.nodeValue;
			}
			catch(e){
				return "";
			}
		}
	}
	function setValueX(KeyName, Value){
		if (document.all) {
			var Element = xmlDOM.createNode("element",KeyName,"");
			xmlDOM.documentElement.childNodes[0].appendChild(Element);
			xmlDOM.documentElement.childNodes[0].getElementsByTagName(KeyName)(0).text = Value ; 
			
		}else{	
			xmlDOM.documentElement.childNodes[0].appendChild(xmlDOM.createElement(KeyName)).appendChild(xmlDOM.createTextNode(Value)) ; 
		}
	}
	function containsKeyX(keyName) {
		if (document.all) {
			nodeListObj = xmlDOM.documentElement.childNodes(0).getElementsByTagName(keyName)(0) ;
		}else{
			nodeListObj = xmlDOM.documentElement.childNodes[0].getElementsByTagName(keyName)[0] ;
		}	
		if (nodeListObj==null) {
			return false ;
		}else{
			return true ;
		}
	}
	function containsValueX(keyName){
		if (containsKeyX(keyName)) {
			if (document.all) {
				textNodeValue = xmlDOM.documentElement.childNodes(0).getElementsByTagName(keyName)(0).text ;
			}else{
				textNodeValue = xmlDOM.documentElement.childNodes[0].getElementsByTagName(keyName)[0].firstChild.nodeValue ;
			}	
			if ((textNodeValue==null)||(textNodeValue=="")) {
				return false ;
			}else{
				return true ;
			}
		}else{
			return false;
		}
	}
	function setAction(actionName){
		validatorAction = actionName ;
	}
	function getAction(){
		return validatorAction
	}
		
	function sendAndReceiveX(){
		var xmlStr ="";
		if (document.all){
			var objXMLHTTP = new ActiveXObject("Microsoft.XMLHTTP"); 
			
		}else{
			var objXMLHTTP = new XMLHttpRequest();  
		}
		var urlstr = "" ;
		var frmlength = xmlDOM.documentElement.childNodes[0].childNodes.length ;
		for (var j=0;j<frmlength;j++){
			if (j==0) {
				itemName = xmlDOM.documentElement.childNodes[0].childNodes[0].tagName  ; 
			}else{
				itemName=  "&" + xmlDOM.documentElement.childNodes[0].childNodes[j].tagName  ; 
			}	
			try{
				 
				if(itemName == "Args" || itemName == "&Args"){
					var userID = document.getElementById('userId').value;
					var csrfValue = document.getElementById('CSRFTOKEN').value;
					var encryptionKey = userID+csrfValue;
					urlstr = urlstr + itemName+"="+ doEncrypt(xmlDOM.documentElement.childNodes[0].childNodes[j].firstChild.nodeValue,encryptionKey);
				}
				else{
					urlstr = urlstr + itemName+"="+ xmlDOM.documentElement.childNodes[0].childNodes[j].firstChild.nodeValue;
				}
				 
			}catch(e){
				urlstr = urlstr + itemName+"=" ;
			}
		}
		urlstr = encodeURI(urlstr);
		objXMLHTTP.open("POST","valAction.do",false);
		objXMLHTTP.setRequestHeader("content-type", "application/x-www-form-urlencoded") ;
		objXMLHTTP.send(urlstr);
		var rtnXML = objXMLHTTP.responseText;		
		if (objXMLHTTP.statusText == "OK" ) {
			clearMapX();
			if (document.all) {
				xmlDOM.loadXML(rtnXML);
			}else{
				var objDOMParser = new DOMParser();
				xmlDOM = objDOMParser.parseFromString(rtnXML, "text/xml");
				xmlDOM.normalize();
			}
			return true;
		}else{
			return false;
		}
	} 
	  
	  
	function submitFormX(){
		var urlstr = "" ;
		var frmlength = document.forms[0].length ;
		for (var j=0;j<frmlength;j++){
			if (j==0) {
				itemName=  document.forms[0].elements[j].name ; 
			}else{
				itemName=  "&" + document.forms[0].elements[j].name ; 
			}	
			urlstr = urlstr + itemName+"="+ document.forms[0].elements[j].value ;		
		}
		domlength = xmlDOM.documentElement.childNodes[0].childNodes.length ;
		for (var j=0;j<domlength;j++){
			if (domlength==0 && j==0) {
				itemName = xmlDOM.documentElement.childNodes[0].childNodes[0].tagName  ; 
			}else{
				itemName=  "&" + xmlDOM.documentElement.childNodes[0].childNodes[j].tagName  ; 
			}	
			try{
				urlstr = urlstr + itemName+"="+ xmlDOM.documentElement.childNodes[0].childNodes[j].firstChild.nodeValue;
			}catch(e){
				urlstr = urlstr + itemName+"=" ;
			}
		}
		if (document.all){
			var objXMLHTTP = new ActiveXObject("Microsoft.XMLHTTP"); 
		}else{
			var objXMLHTTP = new XMLHttpRequest();  
		}
		
		objXMLHTTP.open("POST","ComAction.do" ,false);
		urlstr = encodeURI(urlstr); 
		objXMLHTTP.setRequestHeader("content-type", "application/x-www-form-urlencoded") ;
		objXMLHTTP.send(urlstr);
		var rtnXML = objXMLHTTP.responseText;
		if (objXMLHTTP.statusText == "OK" ) {
			clearMapX();
			if (document.all) {
				xmlDOM.loadXML(rtnXML);
			}else{
				var objDOMParser = new DOMParser();
				xmlDOM = objDOMParser.parseFromString(rtnXML, "text/xml");
			}
			return true;
		}else{
			return false;
		}
		
	}
	
	function callReportX(){  
		var urlstr = "" ;
		var frmlength = document.forms[0].length ;
		var xmlStr='';
		if (document.all) {	
		var xmlStr = xmlDOM.xml ;
		}else{
	    	var objXMLSerializer = new XMLSerializer;
	   		var xmlStr = objXMLSerializer.serializeToString(xmlDOM);	   		
		}				
		xmlStr  = replaceall(xmlStr,"<Data>","");
		xmlStr  = replaceall(xmlStr,"</Data>","");
		if (document.all) {
				xmlDOM.loadXML(xmlStr);
		}else{
				var objDOMParser = new DOMParser();
				xmlDOM = objDOMParser.parseFromString(xmlStr, "text/xml");
		}		
		domlength = xmlDOM.documentElement.childNodes.length ;		
		for (var j=0;j<domlength;j++){
			if (domlength==0 && j==0) {
				itemName = xmlDOM.documentElement.childNodes[0].tagName;				
			}else{
				itemName=  "&" + xmlDOM.documentElement.childNodes[j].tagName;				
			}	
			try{
				urlstr = urlstr + itemName+"="+ xmlDOM.documentElement.childNodes[j].firstChild.nodeValue;
			}catch(e){
				urlstr = urlstr + itemName+"=" ;
			}
		}		
		if (document.all){
			var objXMLHTTP = new ActiveXObject("Microsoft.XMLHTTP"); 
		}else{
			var objXMLHTTP = new XMLHttpRequest();  
		}		
		
		objXMLHTTP.open("POST","valAction.do",false);
		urlstr = encodeURI(urlstr); 
		objXMLHTTP.setRequestHeader("content-type", "application/x-www-form-urlencoded") ;
		objXMLHTTP.send(urlstr);
		var rtnXML = objXMLHTTP.responseText;		
		if (objXMLHTTP.statusText == "OK" ) {
			clearMapX();
			if (document.all) {
				xmlDOM.loadXML(rtnXML);
			}else{
				var objDOMParser = new DOMParser();
				xmlDOM = objDOMParser.parseFromString(rtnXML, "text/xml");
			}
			if(xmlDOM.childNodes[0].childNodes[0].getElementsByTagName("sucFlg").item(0).firstChild.nodeValue=="0"){
				alert(xmlDOM.childNodes[0].childNodes[0].getElementsByTagName("errMsg").item(0).firstChild.nodeValue);
			}else {
				showReportDivX();
			}
		}else{
			alert("Report Could not be Generated");
		}
	}
	
	
	function showReportDivX(){ 
		repFileName = xmlDOM.childNodes[0].childNodes[0].getElementsByTagName("repFileName").item(0).firstChild.nodeValue ;
		var servletname = "/InTouch/ReportServlet?repFileName=" + repFileName ;
		viewReportX(repFileName);
	}
	
	function viewReportX(repFileName){
		repFileName = xmlDOM.childNodes[0].childNodes[0].getElementsByTagName("repFileName").item(0).firstChild.nodeValue ;
		var servletname = "/InTouch/ReportServlet?repFileName=" + repFileName ;
		
		var windowOpenType = '_blank';
		var windowAttributes = 'toolbar=No,width=1024,height=768,left=0,top=0,resizable=yes';
	    	popUp = window.open(servletname, windowOpenType, windowAttributes);
	}
}


function getXML(DOMobj){
		if (document.all) {	
			return DOMobj.xml ;
		}else{
			var xmlSerializer = new XMLSerializer();
			return xmlSerializer.serializeToString(DOMobj);
		}
}

function replaceall(str,findStr,replaceStr){
		while(str.lastIndexOf(findStr) >= 0 ){
			str = str.replace(findStr,replaceStr); 
		}
		return str;
}


function getAllXMLX(){
	if (document.all) {	
		return xmlDOM.xml ;
	}else{
    	var objXMLSerializer = new XMLSerializer;
   		var strXML = objXMLSerializer.serializeToString(xmlDOM);
   		return strXML;
	}
}
