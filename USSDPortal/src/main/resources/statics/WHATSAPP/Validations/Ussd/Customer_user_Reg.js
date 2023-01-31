

	
		$(document).ready(function(){   
			get_countrycode();
			
			/* Registration Date */
			  var today = new Date();
			  var dd = String(today.getDate()).padStart(2, '0');
			  var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
			  var yyyy = today.getFullYear();
			  today = dd + '-' + mm + '-' + yyyy;
			  $('#tregdate').val(today);
			  
			  
			  $("#trolecd").blur(function() { 
				  validation("trolecd");
			  });
			  $("#tmobile").blur(function() {  
				 MobileNo_validation("tmobile");
			  });
			  $("#tcntycd").blur(function() {  
				  validation("tcntycd");
			  });
			  $("#cntycd").blur(function() {  
				  validation("cntycd");
			  });
			  $("#cntyname").blur(function() {  
				  validation("cntyname");
			  });
			  $("#mbcd").blur(function() {  
				  validation("mbcd");
			  });
			  
			  
		});
	
		function reset()              /* Reset Function */
		{
			location.reload();
			//$('form :input').val('');
		}
		
		function validation(id){
			var input_control = document.getElementById(id);
			
			var Validate = false;
			
			if(input_control.value == "" || input_control.value == null || input_control.value == "undefined" || input_control.value.trim() == ""){
				Validate = false;
				$("#"+id+"_error").html("Required");
				$("#"+id+"_error").show();
			}else{
				Validate = true;
				$("#"+id+"_error").html("");
				$("#"+id+"_error").hide();
				
			}
			return Validate;
		}
		
		
		function MobileNo_validation(id){
			
			var input_control = document.getElementById(id);
			
			var Validate = false;
			
			if(input_control.value == "" || input_control.value == null || input_control.value == "undefined" || input_control.value.trim() == ""){
				Validate = false;
				$("#"+id+"_error").html("Required");
				$("#"+id+"_error").show();
				
			}else{
				if(input_control.value.length > 0 && input_control.value.length < 8){
					Validate = false;
				    $("#"+id+"_error").html("Enter more than 8 characters");
				    $("#"+id+"_error").show();
				    
				}else{
					Validate = true;
					$("#"+id+"_error").html("");
					$("#"+id+"_error").hide();
				}
			}
			return Validate;
		}
		
		
		
		function proceed(){
			var Count = 0;
	       	
			var data = new FormData();
			
		    if(!MobileNo_validation("tmobile"))     { Count++; }
	       	if(!validation("torgcd"))      { Count++; }
	       	if(!validation("trolecd"))     { Count++; }
	    	if(!validation("tcntycd"))     { Count++; }
	    	if(!validation("tregdate"))    { Count++; }

	    	
	    	if (Count == 0){
	    		
			 data.append("SUBORGCODE" , $("#torgcd").val());
			 data.append("CHCODE", $("#trolecd").val());
			 data.append("MOBILENO" , $("#tmobile").val());
			 data.append("COUNTRY_CODE" , $("#tcntycd").val());
			 data.append("REGISTRATION_DATE" , $("#tregdate").val());
			 data.append("STATUS" , $("#Amount_limit").prop('checked') === true ? "1" : "0");
	
			 
	    		
	    		$.ajax({	
	    			url  :  $("#ContextPath").val()+"/Customer_User_Registration",
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
	    		
		}
		
		function Add_countrycode(){
			var Count = 0;
	       	
			var data = new FormData();
			
	       	if(!validation("cntycd"))      { Count++; }
	       	if(!validation("cntyname"))     { Count++; }
	    	if(!validation("mbcd"))     { Count++; }
	    	
	    	if (Count == 0){
	    		
			 data.append("COUNTRYCODE" , $("#cntycd").val());
			 data.append("MOBCODE", $("#mbcd").val());
			 data.append("COUNTRYNAME" , $("#cntyname").val());
	
			 
	    		
	    		$.ajax({	
	    			url  :  $("#ContextPath").val()+"/Customer_User_Registration_Country_code_model",
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
	    		
		}
		
		
		function get_countrycode(){
	       				 
	    		var data = new FormData();
	    		$.ajax({	
	    			url  :  $("#ContextPath").val()+"/Customer_User_Registration_Select_Country_code_model",
					type :  'POST',
					data :  data,
					cache : false,
					contentType : false,
					processData : false,
					success: function (data) 
					{ 
						if(data.Result == 'Success')
						{
							//Sweetalert("success", "", data.Message);
							
							Swal.close();
							
							var rateArr = data.array_values;
							
							for(let i = 0 ; i < rateArr.length ; i++){
								console.log(rateArr[i]);
								$('#tcntycd').append($("<option></option>").text(rateArr[i]).val(rateArr[i]));
							}
						}
						else
						{
							Sweetalert("warning", "", data.Message);
						}
					},
					beforeSend: function( xhr )
			 		{
			 			Sweetalert("load", "", "Please Wait");
			 			 $("#tcntycd").empty();
			 			 $("#tcntycd").append($('<option></option>').val("").html("Select")); 
			        },
				    error: function (jqXHR, textStatus, errorThrown) { }
			   });
	    		
	    	}
	    	
	    	
	    
	
