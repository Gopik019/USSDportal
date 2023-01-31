$(document).ready(function(){
	$("#uid").autocomplete({
		source:  $("#ContextPath").val() + "/Suggestions/User_Id567",   
		minLength: 1,			  
		select: function(event, ui){}
	}).autocomplete( "instance" )._renderItem = function(ul,item){
		return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
	};
	
	$("#tpwd,#topwd").keyup(function(){
		validate_passwords();
	});
	
	$("#tpwd,#topwd").blur(function(){
		validate_passwords();
	});
})

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
	

	var isIE = /*@cc_on!@*/false || !!document.documentMode;
	var isEdge = !isIE && !!window.StyleMedia;
	var showButton = !(isIE || isEdge)
	if (!showButton) {
	    document.getElementById(f).style.visibility = "hidden";
	}
}

function reset_form(){
	location.reload();
}

function validate_passwords()
{
	if($("#tpwd").val() == "" || $("#topwd").val() == "")
	{
		if($("#tpwd").val() == "")
		{
			$("#tpwd_error").html('Required');
			$("#tpwd_error").show();
		}
		
		if($("#topwd").val() == "")
		{
			$("#topwd_error").html('Required');
			$("#topwd_error").show();
		}
		
		return false;
	}
	
	if($("#tpwd").val() != $("#topwd").val())
	{
		$("#topwd_error, #tpwd_error").html('Passwords does not match');
		$("#tpwd_error, #topwd_error").show();
		return false;
	}
	
	$("#tpwd_error, #topwd_error").hide();
	
	return true;
}

function Validate(salt)
{
	$("#submit").attr("disabled","disabled");
	var tpwd = document.getElementById("tpwd").value;
	var topwd = document.getElementById("topwd").value;
	
	if(document.getElementById("tpwd").value == "" || document.getElementById("topwd").value == "")
	{
		Sweetalert("warning", "", "Please fill all the Required Fields");
		
		return;
	}
	
	if(document.getElementById('tpwd').value != document.getElementById('topwd').value)
	{
		Sweetalert("warning", "", "Passwords Mismatch"); 
		
		return;
	}
	
	var ciphertext = des(document.getElementById('uid').value+document.getElementById("tpwd").value, 1, 0);    

	var EncStr=hex_sha256(ciphertext);
	
	var finalHash = doEncrypt(EncStr,document.getElementById('randomSalt').value);

	document.getElementById("hashedPassword").value = finalHash;
	
	document.getElementById("tpwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
	document.getElementById("topwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
	
	Reset_Password(salt);
}

function Reset_Password(a)
{
	var data = new FormData();
	
   	data.append('torgcd' , document.getElementById('torgcd').value);
 	data.append('tuserid' , document.getElementById('uid').value);
 	data.append('hashedPassword' , document.getElementById('hashedPassword').value);
	data.append("randomSalt", a);
	
   	$.ajax({		 
		url  :  $("#ContextPath").val() + "/Password_Reset",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			Swal.close();
			
			if(data.Result == "Success")
			{ 
				Sweetalert("success_load_Current", "", "Password Reset Succcesfully !!");
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
	    error: function (jqXHR, textStatus, errorThrown) { return false; }
   });
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