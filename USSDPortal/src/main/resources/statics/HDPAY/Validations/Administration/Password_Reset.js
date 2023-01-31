$(document).ready(function(){
	


	$("#tpwd,#topwd").keyup(function(){
		validate_passwords();
	});
	
	$("#tpwd,#topwd").blur(function(){
		validate_passwords();
	});
	
	$("#reset").click(function(){
		reset_form();
	});
	
	$(function () {
    var $password = $(".form-control[type='password']");
    var $passwordAlert = $(".password-alert");
    var $requirements = $(".requirements");
    var leng, bigLetter, smallLetter, num, specialChar;
    var $leng = $(".leng");
    var $bigLetter = $(".big-letter");
    var $smallLetter = $(".small-letter");
    var $num = $(".num");
    var $specialChar = $(".special-char");
    var specialChars = "!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?`~";
    var numbers = "0123456789";

    $requirements.addClass("wrong");
    $password.on("focus", function(){$passwordAlert.show();});

    $password.on("input blur", function (e) {
        var el = $(this);
        var val = el.val();
        $passwordAlert.show();

        if (val.length < 8) {
            leng = false;
        }
        else if (val.length > 7) {
            leng=true;
        }
        
        if(val.toUpperCase()==val)
         {
            smallLetter = false;
        }
        else
       {
 	       smallLetter=true;
	      }

        if(val.toLowerCase()==val)
         {
            bigLetter = false;
        }
        else
       {
 	       bigLetter=true;
	      }
        
        num = false;
        for(var i=0; i<val.length;i++){
            for(var j=0; j<numbers.length; j++){
                if(val[i]==numbers[j]){
                    num = true;
                }
            }
        }
        
        specialChar=false;
        for(var i=0; i<val.length;i++){
            for(var j=0; j<specialChars.length; j++){
                if(val[i]==specialChars.charAt(j)){
                    specialChar = true;
                }
            }
        }

        console.log(leng, bigLetter, num, specialChar);
        
        if(leng==true&&bigLetter==true&&num==true&&specialChar==true&&smallLetter==true){
            $(this).addClass("valid").removeClass("invalid");
            $requirements.removeClass("wrong").addClass("good");
            $passwordAlert.removeClass("alert-warning").addClass("alert-success");
			$("#submit").attr("disabled",false);
        }
        else
        {
            $(this).addClass("invalid").removeClass("valid");
            $passwordAlert.removeClass("alert-success").addClass("alert-warning");

            if(leng==false){$leng.addClass("wrong").removeClass("good");}
            else{$leng.addClass("good").removeClass("wrong");}

            if(bigLetter==false){$bigLetter.addClass("wrong").removeClass("good");}
            else{$bigLetter.addClass("good").removeClass("wrong");}

            if(num==false){$num.addClass("wrong").removeClass("good");}
            else{$num.addClass("good").removeClass("wrong");}

            if(smallLetter==false){$smallLetter.addClass("wrong").removeClass("good");}
            else{$smallLetter.addClass("good").removeClass("wrong");}

            if(specialChar==false){$specialChar.addClass("wrong").removeClass("good");}
            else{$specialChar.addClass("good").removeClass("wrong");}
			$("#submit").attr("disabled","disabled");
        }
        
        
        if(e.type == "blur"){
                $passwordAlert.hide();
            }
    });
});
	
                  	var eyeicon = document.getElementById("toggle");
					var passinp = document.getElementById("topwd");
					
					eyeicon.addEventListener("click",function(){
							if(passinp.type === 'password'){
							passinp.setAttribute('type', 'text');
							eyeicon.classList.remove('fa-eye-slash');
							eyeicon.classList.add('fa-eye');
						}
						else{
							passinp.setAttribute('type', 'password');
							eyeicon.classList.add('fa-eye-slash');
							eyeicon.classList.remove('fa-eye');
						}
					});
					
					
					
					
					var eyeicon1 = document.getElementById("toggle_eye");
					var passinp1 = document.getElementById("tpwd");
					
					eyeicon1.addEventListener("click",function(){
						if(passinp1.type === 'password'){
							passinp1.setAttribute('type', 'text');
							eyeicon1.classList.remove('fa-eye-slash');
							eyeicon1.classList.add('fa-eye');
						}
						else{
							passinp1.setAttribute('type', 'password');
							eyeicon1.classList.add('fa-eye-slash');
							eyeicon1.classList.remove('fa-eye');
						}
					});
});


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

function reset_form(){
	location.reload();
}

function Validate()
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
	
	var ciphertext = des(document.getElementById('tuserid').value+document.getElementById("tpwd").value, 1, 0);    

	var EncStr=hex_sha256(ciphertext);

	var finalHash = doEncrypt(EncStr,document.getElementById('randomSalt').value);
	
	document.getElementById("hashedPassword").value = finalHash;
	
	document.getElementById("tpwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
	document.getElementById("topwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
	
	Reset_Password();
}

function Reset_Password()
{
	var data = new FormData();
	
   	data.append('torgcd' , document.getElementById('torgcd').value);
 	data.append('tuserid' , document.getElementById('tuserid').value);
 	data.append('hashedPassword' , document.getElementById('hashedPassword').value);
	data.append('randomSalt' , document.getElementById('randomSalt').value);
	
	document.getElementById("tpwd").setAttribute('type', 'password');
	document.getElementById("topwd").setAttribute('type', 'password');
	
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