$(document).ready(function() {
		
		 $("#tuserid").autocomplete({
			    source:  $("#ContextPath").val() + "/Suggestions/User_Id567",			
			    minLength: 1,			
			    change: function(event, ui) 
			    {
			    	// if(ui.item) 
				    // {
				        QUEUECHECKER();
				   //  } 
			    },		   
		  }).autocomplete( "instance" )._renderItem = function(ul,item) 
		  	{
		  		 return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
			};
			
			$("#tbublock").change(function() {
				
				if($("#tbublock").prop('checked') == true)
				{
					$("#tbublock_label").html('Block');
				}
				else
				{
					$("#tbublock_label").html('Unblock');
				}		
			});	

			$("#tuserid").change(function() {
				//QUEUECHECKER();
			});	
			
			$("#proceed").click(function() {
				proceed();
			});	
			$("#reset").click(function() {
				reset();
			});	
	});
		
	function proceed()
	{
		$("#proceed").attr('disabled','disabled');
		
		if($("#tuserid").val() == "")
		{
			$("#tuserid_error").html('Required');
			$("#tuserid_error").show();
			return;
		}
		else
		{
			$("#tuserid_error").hide();
		}	
		
	    var data = new FormData();
			
		data.append("tuserid" , $("#tuserid").val());
		data.append("tbublock" , $("#tbublock").prop('checked') === true ? "1" : "0");
		alert($("#mode").val());
		data.append("mode" , $("#mode").val());
			
	   $.ajax({		 
			url  :  $("#ContextPath").val() + "/Block_Unblock_User",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{
					Sweetalert("success_load_Current", "", data.Message);
				}
				else
				{
					Sweetalert("warning", "", data.Message);
				}
			},
			beforeSend: function( xhr )
	 		{
	 			Sweetalert("load", "", "Please Wait");
	        },
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
   }
	
	function reset()
	{
		//location.reload();
		
		 $("#form").trigger("reset");
	}
	
	function QUEUECHECKER()
	{
		var data = new FormData();
		
		data.append("tuserid" , $("#tuserid").val());
		data.append("torgcd" , "EXIM");
		data.append("pgmID" , "UNBLOCKUSERID");
		
	   $.ajax({		 
			url  :  $("#ContextPath").val() + "/QUEUECHECKER",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{
					if(data.SucFlg == "1" ) 
		            {
						/* if(data.STATUS == "1")
						 {
						 	document.getElementById("tbublock").checked = true;
						 }
						 else
						 {
						 	document.getElementById("tbublock").checked = false;
						 }
						 
						 document.getElementById('mode').value = data.mode;
						 
						 document.getElementById("tbublock").focus(); */
						 
						 if(data.STATUS == "Blocked")
						 {
						 	document.getElementById("tbublock").checked = true;
						 	
						 	$("#tbublock_label").html('Blocked');
						 }
						 else
						 {
						 	document.getElementById("tbublock").checked = false;
						 	
						 	$("#tbublock_label").html('Unblock');
						 }
					 }
		             else
		             {
						document.getElementById("tbublock").checked = false;
						
						 document.getElementById('mode').value =  data.mode;
						 
						document.getElementById("tbublock").focus();
						 
		             	//return true;
		             	
		             } 	
					
					//alert(data.mode);
					//alert(data.STATUS);
				}
			},
			beforeSend: function( xhr )
	 		{
	 			//Sweetalert("load", "", "Please Wait");
	        },
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
	}

	function validateEmail(email)
	{
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    
	    return re.test(String(email).toLowerCase());
	}
	
	function date_format(val)
	{
        var dt = new Date(val); 

        var dd = dt.getDate(); 
        var mm = dt.getMonth() + 1; 
  
        var yyyy = dt.getFullYear(); 
        if (dd < 10) { 
            dd = '0' + dd; 
        } 
        if (mm < 10) { 
            mm = '0' + mm; 
        } 
        
        var condate = dd + '-' + mm + '-' + yyyy; 
        
        return condate;
  
	}
	
	function isvalidPassword(val) 
	{ 
        if (val.match(/[a-z]/g) && val.match(/[A-Z]/g) && val.match(/[0-9]/g) && val.match(/[^a-zA-Z\d]/g)) 
        {
        	return true;
        }   
        else 
        {
        	return false;
        }
    }

	
	function Form_reset(){
		document.getElementById("tuserid").value = "";
		document.getElementById("tbublock").checked = false;
		$("#tbublock_label").html("");
	}

function Sweetalert(Type, Title, Info)
{
	if(Type == "success")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text: Info ,			 
			  timer: 2000
		});
	}
	else if(Type == "success_load_Current")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text:  Info,
			  timer: 2000
		}).then(function (result) {
			  if (true)
			  {							  
				  location.reload();
			  }
		});		
	}
	else if(Type == "error")
	{
		Swal.fire({
			  icon: 'error',
			  title: Title ,
			  text:  Info ,		 
			  timer: 2000
		});
	}
	else if(Type == "warning")
	{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  text:  Info ,		 
			  timer: 2000
		});
	}
	else if(Type == "load")
	{
		Swal.fire({
			  title: Title,
			  html:  Info,
			  timerProgressBar: true,
			  allowOutsideClick: false,
			  onBeforeOpen: () => {
			    Swal.showLoading()
			  },
			  onClose: () => {
			  }
		});	
	}	
}






/*$(document).ready(function() {
		
		 $("#tuserid").autocomplete({
			    source:  $("#ContextPath").val() + "/Suggestions/User_Id567",			
			    minLength: 1,			
			    change: function(event, ui) 
			    {
			    	// if(ui.item) 
				    // {
				        QUEUECHECKER();
				   //  } 
			    },		   
		  }).autocomplete( "instance" )._renderItem = function(ul,item) 
		  	{
		  		 return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
			};
			
			$("#tbublock").change(function() {
				
				if($("#tbublock").prop('checked') == true)
				{
					$("#tbublock_label").html('Block');
				}
				else
				{
					$("#tbublock_label").html('Unblock');
				}		
			});	

			$("#tuserid").change(function() {
				//QUEUECHECKER();
			});	
			
			$("#proceed").click(function() {
				User_test();
				//proceed();
				
			});	
			
			$("#reset").click(function() {
				reset();
				
			});		
	});
		
	function proceed()
	{
		$("#proceed").attr('disabled','disabled');
		
		if($("#tuserid").val() == "")
		{
			$("#tuserid_error").html('Required');
			$("#tuserid_error").show();
			return;
		}
		else
		{
			$("#tuserid_error").hide();
		}	
		
	    var data = new FormData();
			
		data.append("tuserid" , $("#tuserid").val());
		data.append("tbublock" , $("#tbublock").prop('checked') === true ? "1" : "0");
		alert($("#mode").val());
		data.append("mode" , $("#mode").val());
			
	   $.ajax({		 
			url  :  $("#ContextPath").val() + "/Block_Unblock_User",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{
					Sweetalert("success_load_Current", "", data.Message);
				}
				else
				{
					Sweetalert("warning", "", data.Message);
				}
			},
			beforeSend: function( xhr )
	 		{
	 			Sweetalert("load", "", "Please Wait");
	        },
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
   }

function reset()
			{
				
				$('#tuserid').val("");
				$('#tbublock').val("");
				
			}
	
	function QUEUECHECKER()
	{
		var data = new FormData();
		
		data.append("tuserid" , $("#tuserid").val());
		data.append("torgcd" , "EXIM");
		data.append("pgmID" , "UNBLOCKUSERID");
		
	 /*  $.ajax({		 
			url  :  $("#ContextPath").val() + "/QUEUECHECKER",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{
					if(data.SucFlg == "1" ) 
		            {
						/* if(data.STATUS == "1")
						 {
						 	document.getElementById("tbublock").checked = true;
						 }
						 else
						 {
						 	document.getElementById("tbublock").checked = false;
						 }
						 
						 document.getElementById('mode').value = data.mode;
						 
						 document.getElementById("tbublock").focus(); 
						 
						 if(data.STATUS == "Blocked")
						 {
						 	document.getElementById("tbublock").checked = true;
						 	
						 	$("#tbublock_label").html('Blocked');
						 }
						 else
						 {
						 	document.getElementById("tbublock").checked = false;
						 	
						 	$("#tbublock_label").html('Unblock');
						 }
					 }
		             else
		             {
						document.getElementById("tbublock").checked = false;
						
						 document.getElementById('mode').value =  data.mode;
						 
						document.getElementById("tbublock").focus();
						 
		             	//return true;
		             	
		             } 	
					
					//alert(data.mode);
					//alert(data.STATUS);
				}
			},
			beforeSend: function( xhr )
	 		{
	 			//Sweetalert("load", "", "Please Wait");
	        },
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
	}

	function validateEmail(email)
	{
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    
	    return re.test(String(email).toLowerCase());
	}
	
	function date_format(val)
	{
        var dt = new Date(val); 

        var dd = dt.getDate(); 
        var mm = dt.getMonth() + 1; 
  
        var yyyy = dt.getFullYear(); 
        if (dd < 10) { 
            dd = '0' + dd; 
        } 
        if (mm < 10) { 
            mm = '0' + mm; 
        } 
        
        var condate = dd + '-' + mm + '-' + yyyy; 
        
        return condate;
  
	}
	function User_test()
	{

	    var data = new FormData();
	    
	    var user  = $("#tuserid").val();
	    
	    data.append("user",user);

	   $.ajax({	

			url  :  serbasePath + "/HDPAY/Suggestions/User_Id",  
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{

					/* Sweetalert("success","","This User Id is valid"); 
					proceed();
					
				}
				else
				{
					Sweetalert("warning","","This User Id is  Not Valid");
					
				}
			},
			beforeSend: function( xhr )
	 		{
	 			//Sweetalert("load", "", "Please Wait");
	        },
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
	}
	
	function isvalidPassword(val) 
	{ 
        if (val.match(/[a-z]/g) && val.match(/[A-Z]/g) && val.match(/[0-9]/g) && val.match(/[^a-zA-Z\d]/g)) 
        {
        	return true;
        }   
        else 
        {
        	return false;
        }
    }

	
	function Form_reset(){
		document.getElementById("tuserid").value = "";
		document.getElementById("tbublock").checked = false;
		$("#tbublock_label").html("");
	}

function Sweetalert(Type, Title, Info)
{
	if(Type == "success")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text: Info ,			 
			  timer: 2000
		});
	}
	else if(Type == "success_load_Current")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text:  Info,
			  timer: 2000
		}).then(function (result) {
			  if (true)
			  {							  
				  location.reload();
			  }
		});		
	}
	else if(Type == "error")
	{
		Swal.fire({
			  icon: 'error',
			  title: Title ,
			  text:  Info ,		 
			  timer: 2000
		});
	}
	else if(Type == "warning")
	{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  text:  Info ,		 
			  timer: 2000
		});
	}
	else if(Type == "load")
	{
		Swal.fire({
			  title: Title,
			  html:  Info,
			  timerProgressBar: true,
			  allowOutsideClick: false,
			  onBeforeOpen: () => {
			    Swal.showLoading()
			  },
			  onClose: () => {
			  }
		});	
	}	
}*/