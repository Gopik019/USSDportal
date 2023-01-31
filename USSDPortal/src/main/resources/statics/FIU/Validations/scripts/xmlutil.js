
var xmlDoc;
function loadXML(txt) {

try 
  {
  xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
  xmlDoc.async="false";
  xmlDoc.loadXML(txt);
  
  }
catch(e)
  {
  parser=new DOMParser();
  xmlDoc=parser.parseFromString(txt,"text/xml");
  
  }

  return this;
  
}

function getRowCount() {

	return xmlDoc.getElementsByTagName("row").length;

}

function getColumnCount() {

	return xmlDoc.getElementsByTagName("row")[0].childNodes.length;

}

function getValue(x,y) {

	var t = xmlDoc.getElementsByTagName("row")[x];	
	var u = t.childNodes[y];	
	var v = u.childNodes[0];
	if (v==null)
		return "";
	else
		return v.nodeValue;


}



