	 
	var KEY_ENTER = 13;
	var KEY_F2 = 113;
	var KEY_ESC = 27;
	var KEY_TAB = 9;
	var KEY_F5 = 116;
	var KEY_LEFTARROW = 37;
	var KEY_RIGHTARROW = 39;
	var KEY_UPPERCASE_A = 65;
	var KEY_UPPERCASE_Z = 90;
	var KEY_LOWERCASE_A = 65;
	var KEY_LOWERCASE_Z = 90;
	var KEY_HYPHEN = 45;
	var KEY_FWDSLASH = 47;
	var KEY_DOT = 46;

	 
	function dateValidator(objVal,kCode,currDate){
		var returnDate="";
		if(kCode >= KEY_LOWERCASE_A  && kCode <=KEY_LOWERCASE_Z ||
		   kCode >=KEY_UPPERCASE_A   && kCode <=KEY_UPPERCASE_Z){
			window.event.keyCode = 0;
			return "false";
		}
		
		if(objVal.length >2 && objVal.indexOf("-")==0){ 
			return "false";
		}
	
		if(kCode !=KEY_ENTER){
			if(kCode == KEY_FWDSLASH || kCode == KEY_HYPHEN || kCode == KEY_DOT){
				if(objVal.length>0){
					returnDate=objVal.substring(0,objVal.length);
				}
			}else{
				if(objVal.length== 2){
					returnDate=objVal+"-";
				}
				if(objVal.length == 5){
					returnDate=objVal+"-";
				}
		     } 
		}
	
		if(kCode==13){
			objVal=objVal.replace(" ","");
			if(objVal.length >2 && objVal.indexOf("-")==0){
				return "false";
			}
			dfarr= objVal.split("-");
			if(dfarr.length>0){			
				for(var i=0 ;i< dfarr.length;i++){			
					dfarr[i]=dfarr[i].trim();	
				}
				if(dfarr.length==2){
					if (dfarr[0].length==1){
						objVal="0"+dfarr[0]+"-"+dfarr[1]+"-"+dfarr[2];
					}
					if(dfarr[1].length==1){
						objVal=dfarr[0]+"-0"+dfarr[1]+"-"+dfarr[2];
					}
				}
			}
			currarr=(currDate.toString()).split("-");  
			if (objVal.length>=10){
				if(dfarr.length!=3){		
					return "false";
				}
				datestatus=dateValidation(objVal).trim();
				returnDate= datestatus.trim();
				return returnDate;
			}
		 	ls=objVal.length;
			if(ls>5){
				if(dfarr.length!=3){		
						return "false";
				}
			}
			switch(ls) {		
				case 0:	returnDate= currDate;break;
				case 1:
						returnDate="0"+objVal+"-"+currarr[1]+"-"+currarr[2];
						datestatus= dateValidation(returnDate);
		   				if(datestatus==false){
							return "false";
		   				}
		   				break;
				case 2:
						returnDate=objVal+"-"+currarr[1]+"-"+currarr[2];
						datestatus=dateValidation(returnDate);
						if(datestatus==false){
							return "false";
		   				}
		   				break;
				case 3:
						returnDate=objVal+currarr[1]+"-"+currarr[2];
						datestatus= dateValidation(returnDate);
						if(datestatus==false){
							return "false";
		   				}
		   				break;
				case 4:
						returnDate=dfarr[0]+"-0"+dfarr[1]+"-"+currarr[2];
						datestatus= dateValidation(returnDate);
		   				if(datestatus==false){
							return "false";
		   				}
		   				break;
				case 5:
						returnDate=objVal+"-"+currarr[2];
						datestatus= dateValidation(returnDate);
		   				if(datestatus==false){
							return "false";
		   				}
		   				break;
				case 6:
						returnDate=objVal+currarr[2];
						datestatus=dateValidation(returnDate);
		   				if(datestatus==false){
							return "false";
						}
						break;
				case 7: 
						if (isNaN(dfarr[2])==true){
							return "false";
						}	
						if(parseFloat(dfarr[2])< 51)							
							returnDate= dfarr[0]+"-"+dfarr[1]+"-"+currarr[2].substring(0,2)+"0"+dfarr[2];	
						else
							returnDate=dfarr[0]+"-"+dfarr[1]+"-"+((currarr[2].substring(0,2))-1)+"0"+dfarr[2];
						datestatus= dateValidation(returnDate);
							
							if(datestatus==false){
								return "false";
		   					}
		   				break;		
				case 8: 
						if(isNaN(dfarr[2])==true){
								return "false";
						}
						if(parseFloat(dfarr[2])< 51)
							returnDate=dfarr[0]+"-"+dfarr[1]+"-"+currarr[2].substring(0,2)+dfarr[2];		
						else
							returnDate=dfarr[0]+"-"+dfarr[1]+"-"+((currarr[2].substring(0,2))-1)+dfarr[2];
						    datestatus= dateValidation(returnDate);
		   					if(datestatus==false){
								return "false";
		   					}
		   	 			break;
				case 9:
						if(isNaN(dfarr[2])==true){
							return "false";
				  		}	
						if(parseFloat(dfarr[2])< 51)
							returnDate=dfarr[0]+"-"+dfarr[1]+"-"+currarr[2].substring(0,2)+dfarr[2];
						else
							returnDate=dfarr[0]+"-"+dfarr[1]+"-"+((currarr[2].substring(0,2))-1)+dfarr[2];
						datestatus= dateValidation(returnDate);
						if(datestatus==false){
							return "false";
				  		}	
				  		break;
				} 
			}	
			return returnDate;
	}


	 
	function dateValidation(objVal){
		  
		var pattern = /[0-9]{2}-[0-9]{2}-[0-9]{4}/;
		if(!pattern.test(objVal))
			return false;
		 		
		
		dfarr= objVal.split("-");
		dd=dfarr[0];
		mm=dfarr[1];
	 	yyyy=dfarr[2];
	 	days=new Array();
		
		if(parseFloat(dd)<=0 || parseFloat(dd)>31){
	 		return false;
	    }
		
		if(parseFloat(mm)>12 || parseFloat(mm)<=0){
			return false;
		}
	
		if(parseFloat(yyyy)<=0){
			return false;
		}

		
		x= parseFloat(mm)-1;
	    days[0]="31";
	  	if(parseFloat(yyyy)% 4 >0)
	  		days[1]="28";
		else
		    days[1]="29";
			   	
		days[2]="31"; 
		days[3]="30";
		days[4]="31";
		days[5]="30";
		days[6]="31";
		days[7]="31";
		days[8]="30";
		days[9]="31";
		days[10]="30";
		days[11]="31";
		if(parseFloat(dd)>parseFloat(days[x])){
			return false;
	  	}
	
		if((parseFloat(yyyy)< 1900) || (parseFloat(yyyy)> 2079)){
			return false;
	     }   
	  	return objVal;
	}
	
		
		 
	function restrictGreaterDt(dt1,dt2){
		if(getYr(dt1) > getYr(dt2)){
			
			return false;
		}else if(getYr(dt1) < getYr(dt2)){
			return true
		}else{
			if(getMnth(dt1) > getMnth(dt2)){
				
				return false;
			}else if(getMnth(dt1) == getMnth(dt2)){
				if(getDt(dt1) > getDt(dt2)){
					
					return false;
				}else {
					return true;
				}
			}
		}
	}




	 
	function restrictLesserDt(dt1,dt2){
		if(getYr(dt1) < getYr(dt2)){
			
			return false;
		}else if(getYr(dt1) > getYr(dt2)){
			return true;
		}else{
			if(getMnth(dt1)< getMnth(dt2)){
				
				return false;
			}else if(getMnth(dt1) == getMnth(dt2)){
				if(getDt(dt1)< getDt(dt2)){
					
					return false;
				}else {
					return true;
				}
				 
			}
			//}
			return true;
			
		}
	}

	 


	 
	function restrictEqualToDt(dt1,dt2){
		if(getYr(dt1) == getYr(dt2)){
			if(getMnth(dt1)== getMnth(dt2)){
				if(getDt(dt1)== getDt(dt2)){
					return false;
				}else {
					return true;
				}
			}else {
				return true;
			}
		}else {
			return true;
		}			
	}
	
	 
	function restrictLesserAndEqualToDt(dt1,dt2){
		retVal = restrictLesserDt(dt1,dt2);
		
		if(retVal == true){
			retVal = restrictEqualToDt(dt1,dt2);
			if(retVal == true){
				return true;
			}else {
				return false;
			}
		}else if(retVal == false){
			return false;
		}
	}
	
	 
	function restrictGreaterAndEqualToDt(dt1,dt2){
		retVal = restrictGreaterDt(dt1,dt2);
		if(retVal == true){
			retVal = restrictEqualToDt(dt1,dt2);
			if(retVal == true){
				return true;			
			}else {
				return false;
			}
		}else if(retVal == false){
			return false;
		}
	}
	
	 
	function getDt(dt){
		var date1 = dt.substring(0,2);
		return date1;
	}
	
	 
	function getMnth(dt){
		month = dt.substring(3,5);
		return month;
	}
	
	 
	function getYr(dt){
		year = dt.substring(6,10);
		return year;
	}
	function getJulianDate(bdte) {
	greg=1582;
		
		byy = parseInt(getYr(bdte),10);
		bmm = parseInt(getMnth(bdte),10);
		bdd = parseInt(getDt(bdte),10);
		bhh = parseInt("0",10);
	
		if (byy>greg) {
			ly=(!(byy%4)-!(byy%100)+!(byy%400));
		} else {
			ly=!(byy%4);
		}
	
		
		if (bmm<1||bmm>12) {
			alert("Month must be a value between 1 and 12!");
			return 0;
		}
	
		if ((bdd<1) || 
		     (bdd>31) ||
		     ((bmm==4||bmm==6||bmm==9||bmm==11)&&bdd>30) ||
		     (bmm==2&&bdd>(28+ly)) ) {
			alert("Date is not valid for given month!");
			return 0;
		}
	
		if (bhh<0||bhh>23) {
			alert("Hour must be a value between 0 and 23!");
			return 0;
		}
	
		
	
		byys = byy + bmm/100 + bdd/10000;
	
		
		if (		(
				greg==1582 && byys > 1582.1004 && byys < 1582.1015
			) || (
				greg==1752 && byys > 1752.0902 && byys < 1752.0914
			)
		) {
			alert("The given date was skipped during the introduction of the Gregorian calendar!");
			return 0;
		}
	
		
		if (bmm<3) {
			bmm += 12;
			byy -= 1;
		}
	
		
		if (
			(
				greg==1582 && byys >= 1582.1015
			) || (
				greg==1752 && byys >= 1752.0914
			)
		) {
			gc=2-Math.floor(0.01*byy)+Math.floor(0.0025*byy);
		} else {
			gc=0;
		}
	
		jd=Math.floor(365.25*byy)+Math.floor(30.6001*(bmm+1))+bdd+1720994.5+gc+(bhh/24);
	
		wd=Math.floor(jd+0.5-(7*Math.floor((jd+0.5)/7)));
	
		msg = '';
	
		if (wd==0) {msg = 'Monday';}
		if (wd==1) {msg = 'Tuesday';}
		if (wd==2) {msg = 'Wednesday';}
		if (wd==3) {msg = 'Thursday';}
		if (wd==4) {msg = 'Friday';}
		if (wd==5) {msg = 'Saturday';}
		if (wd==6) {msg = 'Sunday';}
	
		
		if (!isNaN(jd)) {
			return Math.floor(jd*100)/100;
		}
	
	
		return 1;
	}
	
	function getDateDiff(bdte,edte)
	{
		var j1=getJulianDate(bdte);
		var j2=getJulianDate(edte);
		return (Math.floor(j2)-Math.floor(j1));
	}
	
	
	function getMonthDiff(bdte,edte){
	
		
	}
	
	 
	function convertToYMD(dt){
		day = dt.substring(0,2);
		month = dt.substring(3,5);
		year = dt.substring(6,10);
		date = year + "-" + month + "-" + day;
		return date;
	}
	
	 
	function convertSysDtToMDY(dt){
		date = dt.getDate();
		month = dt.getMonth()+1;
		yr = dt.getYear();
		sysDt = date + "-" + month + "-" + yr;
		return sysDt;
	}


	
