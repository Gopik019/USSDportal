
$(document).ready(function() {  
		$("#tuserid").autocomplete({
			source:  $("#ContextPath").val() + "/Suggestions/User_Id567",   
			minLength: 1,		  
			change : function(event, ui)
			{
			        if(ui.item) 
			        {
			        	alert('This User Id is already taken');
						$("#submit").attr('disabled','disabled');
			        } 
			        else
			        {
						$("#submit").attr('disabled',false);
			        }
			}
		 }).autocomplete( "instance" )._renderItem = function(ul,item){
		  		 return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
		 };
			
			
			$("#torgcd").blur(function() {
				validate_org_Id();
			});
			
			$("#tuserid").blur(function() {
				validate_User_Id();
			});
			
			$("#tusernme").blur(function() {
				validate_User_Name();
			});
			
			$("#tbirthdate").blur(function() {
				validate_DOB();
			});
			
			$("#tmobile").keyup(function() {
				if(isNaN($("#tmobile").val()))
					$("#tmobile").val(""); 
			});
			
			$("#tmobile").blur(function() {
				validate_MOBILE();
			});
			
			$("#temail").blur(function() {
				validate_EMAIL();
			});
			
			$("#tpwd, #tconfirmpwd").keyup(function() {
				validate_passwords();
			});
			
			$("#tpwd, #tconfirmpwd").blur(function() {
				validate_passwords();
			});
			
			$("#trolecd").blur(function() {
				validate_Role();
			});
			
			$("#tregdate").blur(function() {
				validate_REG_Date();
			});
			
			$("#branchcd").blur(function() {
				validate_branch_Code();
			});
			
			branch_code_sugg();
});


function eyeiconclick(e,f){
	var eyeicon = document.getElementById(f);
	var passinp = document.getElementById(e);
	if(passinp.type === 'password'){
		passinp.setAttribute('type', 'text');
		eyeicon.classList.remove('fa-eye');
		eyeicon.classList.add('fa-eye-slash');
	}else{
		passinp.setAttribute('type', 'password');
		eyeicon.classList.add('fa-eye');
		eyeicon.classList.remove('fa-eye-slash');
	}
}

var isIE = /*@cc_on!@*/false || !!document.documentMode;
var isEdge = !isIE && !!window.StyleMedia;
var showButton = !(isIE || isEdge)
if (!showButton) {
    document.getElementById("toggle").style.visibility = "hidden";
}

function branch_code_sugg()
{
	//alert($("#ContextPath").val() + "/Suggestions/BRANCHCODE_Value");
	$.ajax({		 
		url  :  $("#ContextPath").val() + "/Suggestions/BRANCHCODE_Value",
		type :  'GET',
		data :  '',
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			if(data.Result == "Success")
			{ 
				for(var i=0;i<data.Branch_Codes.length;i++)
				{
					$("#branchcd").append($('<option></option>').val(data.Branch_Codes[i].label).html(data.Branch_Codes[i].label+" -  "+data.Branch_Codes[i].id)); 
				}
			}	
		},
		beforeSend: function( xhr )
		{
			 $("#branchcd").empty();
			 $("#branchcd").append($('<option></option>').val("").html("Select"));   
        }			   
   })
}

function validate_org_Id()
{
	$("#torgcd").val('EXIM');
	
	return true;
}

function validate_User_Id()
{
	if($("#tuserid").val() == "")
	{
		$("#tuserid_error").html('Required');
		$("#tuserid_error").show();
		return false;
	}
	else
	{
		$("#tuserid_error").hide();
		return true;
	}	
}

function validate_User_Name()
{
	if($("#tusernme").val() == "")
	{
		$("#tusernme_error").html('Required');
		$("#tusernme_error").show();
		return false;
	}
	else
	{
		$("#tusernme_error").hide();
		return true;
	}	
}

function validate_DOB()
{
	if($("#tbirthdate").val() == "")
	{
		$("#tbirthdate_error").html('Required');
		$("#tbirthdate_error").show();
		return false;
	}
	else
	{
		$("#tbirthdate_error").hide();
		return true;
	}	
}

function validate_EMAIL()
{
	if($("#temail").val() == "")
	{
		$("#temail_error").html('Required');
		$("#temail_error").show();
		
		return false;
	}
	
	if(!validateEmail($("#temail").val()))
	{
		$("#temail_error").html('Invalid Email Address');
		$("#temail_error").show();
		
		return false;
	}
	
	$("#temail_error").hide();
	
	return true;
}

function validate_MOBILE()
{
	if($("#tmobile").val() == "")
	{
		$("#tmobile_error").html('Required');
		$("#tmobile_error").show();
		return false;
	}
	else
	{
		$("#tmobile_error").hide();
		return true;
	}	
}

function validate_passwords()
{
	if($("#tpwd").val() == "" || $("#tconfirmpwd").val() == "")
	{
		if($("#tpwd").val() == "")
		{
			$("#tpwd_error").html('Required');
			$("#tpwd_error").show();
		}
		
		if($("#tconfirmpwd").val() == "")
		{
			$("#tconfirmpwd_error").html('Required');
			$("#tconfirmpwd_error").show();
		}
		
		return false;
	}

/*	if(isvalidPassword($("#tpwd").val()) != true)
	{
		$("#tpwd_error").html('Passwords must contains Upper case, lower case and alphanumberic characters');
		
		return false;
	}
	
	if(isvalidPassword($("#tconfirmpwd").val()) != true)
	{
		$("#tconfirmpwd_error").html('Passwords must contains Upper case, lower case and alphanumberic characters');
		
		return false;
	} */
	
	if($("#tpwd").val() != $("#tconfirmpwd").val())
	{
		$("#tconfirmpwd_error, #tpwd_error").html('Passwords does not match');
		$("#tpwd_error, #tconfirmpwd_error").show();
		return false;
	}
	
	$("#tpwd_error, #tconfirmpwd_error").hide();
	
	return true;
}

function validate_Role()
{
	if($("#trolecd").val() == "")
	{
		$("#trolecd_error").html('Required');
		$("#trolecd_error").show();
		return false;
	}
	else
	{
		$("#trolecd_error").hide();
		return true;
	}	
}

function validate_REG_Date()
{
	if($("#tregdate").val() == "")
	{
		$("#tregdate_error").html('Required');
		$("#tregdate_error").show();
		return false;
	}
	else
	{
		$("#tregdate_error").hide();
		return true;
	}	
}

function validate_branch_Code()
{
	if($("#branchcd").val() == "")
	{
		$("#branchcd_error").html('Required');
		$("#branchcd_error").show();
		return false;
	}
	else
	{
		$("#branchcd_error").hide();
		return true;
	}	
}

function revalidate()
{	     
   	var errorCount = 0;
   	
   	if(!validate_org_Id())      { errorCount++; }
	if(!validate_User_Id())     { errorCount++; }
	if(!validate_User_Name())    { errorCount++; }
	if(!validate_DOB())  { errorCount++; }
	if(!validate_MOBILE())     { errorCount++; }
	if(!validate_EMAIL())      { errorCount++; }  	
	if(!validate_passwords())        { errorCount++; }
	if(!validate_Role()) { errorCount++; }
	if(!validate_REG_Date())     { errorCount++; }
	if(!validate_branch_Code())    { errorCount++; }

		if(errorCount == 0)
		{	
			get_hashpassword();
		}
		else
		{
   		return false;
		}
}

function isvalid(Id)
{
	if(isBlank(document.getElementById(Id).value))
	{
		return false;
	}
	else
	{
		return true;
	}
 }

function isBlank(val)	
{
	if(val == undefined || val == null || val.trim() == "")
	{ 
		return true;
	}
	
	return false;
}

function proceed()
{
    var data = new FormData();
    $("#submit").attr('disabled','disabled');
	
	data.append('torgcd' , $("#torgcd").val());
	data.append('tuserid' , $("#tuserid").val());
	data.append('tusernme' , $("#tusernme").val());
	data.append('tbirthdate' , date_format($("#tbirthdate").val()));
	data.append('tmobile' , $("#tmobile").val());
	data.append('temail' , $("#temail").val());
	data.append('tpwd' , $("#tpwd").val());
	data.append('tconfirmpwd' , $("#tconfirmpwd").val());
	data.append('trolecd' , $("#trolecd").val());
	data.append('tregdate' , date_format($("#tregdate").val()));
	data.append('branchcd' , $("#branchcd").val());		
	data.append('hashedPassword', $("#hashedPassword").val());
	data.append('randomSalt', $("#randomSalt").val());
	
   $.ajax({		 
		url  :  $("#ContextPath").val() + "/User_Registration",
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

function get_hashpassword()
{	
	var ciphertext = des(document.getElementById("tuserid").value+document.getElementById("tpwd").value, 1, 0); 
	
	var EncStr = hex_sha256(ciphertext);
	
 	var finalHash = doEncrypt(EncStr,document.getElementById('randomSalt').value);
 	
	document.getElementById("hashedPassword").value = finalHash;

	document.getElementById("tpwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
	document.getElementById("tconfirmpwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
	
	proceed();
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
	location.reload();
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