<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Whatsapp_Banking/CSS & JS8.jsp" %>	
	 <style>
	  
         #hello{
			 background-color:#6861ce;
			 text-align:center;
			 color:white;
			 }
         #hello1{
			 background-color:#6861ce;
			 text-align:center;
			 color:white;
			 }
      </style>   
 
<body data-background-color="${Menu.get('body_color').getAsString()}">   
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/HDPAY/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
		<div class="content">
			<div class="page-inner">	
			
		<%@ include file="../../../Headers_&_Footers/HDPAY/common/Form_header.jsp" %>
			
		        <div class="row">
		        
					<div class="col-md-12 mt--1">
		
						<div id="colour_body" class="card">
		
							<div id="tab_card" class="card-body">
								<div class="row mt--3">	
	
				                	<div class="col-md-12">
								
		 <div class="container">
			 <!-- <div style="width:50%">
				<label id="hello" class="form-control">Configuration - service configuration</label>
			 </div> -->
			  <br><br>
			 <div class="row">
					   <div class="col-md-2">
							<div class="form-group">	
								<label for="">Service Code</label>
							</div>
						</div>
						<div class="col-md-4">		
							<input type="text" class="form-control" id="serv_code" placeholder="">
						</div>
						
						<div class="col-md-2">
							<div class="form-group">	
								<label for="">Service Name</label>
							</div>
						</div>
						<div class="col-md-4">		
							<input type="text" class="form-control" id="serv_name" placeholder="">
						</div>
					
			 </div>
			 <div class="row" >
				 <div class="col-md-2">
						<div class="form-group">	
							<label for="">Amount Limit</label>
						</div>
				</div>
																
				<div class="col-md-4">		
					<input type="text" class="form-control" id="amnt" placeholder="">
				</div>
				 <div class="col-md-1">
					<div class="form-group">	
						<label for="">From Time</label>
					</div>
				</div>
				<div class="col-md-2">		
					<input type="time" class="form-control" id="frm_time" placeholder="">
				</div>
				<div class="col-md-1">
					<div class="form-group">	
						<label for="">To Time</label>
					</div>
				</div>
				<div class="col-md-2">		
					<input type="time" class="form-control" id="to_time" placeholder="">
				</div>
				 <!-- <div class="col-md-1">
						<div class="form-group">	
							<label for="">Status</label>
						</div>
				</div>
						<div class="col-md-1">		
							<input type="checkbox" class="form-control" id="ac_no" placeholder="">
						</div>
				<div class="col-md-2">		
					<input type="checkbox" class="form-control" id="ac_no" placeholder="">
				</div>
				<div class="col-md-1">
					<div class="form-group">	
						<label for="">Transaction Limit</label>
					</div>
				</div>
					<div class="col-md-1">		
						<input type="checkbox" class="form-control" id="ac_no" placeholder="">
					</div> -->

																
				<!-- <div class="col-md-2">		
					
				</div> -->
			</div>
			<div class="row">
				 <div class="col-md-2">
					<div class="form-group">	
						<label for="">Template</label>
					</div>
				</div>
				<div class="col-md-7">		
					<textarea class="form-control" id="temp" placeholder=""></textarea>
				</div>
				 <!-- <div class="col-sm-3">
					<button type="button" class="btn btn-secondary" style="width:40%">View</button>
				 </div> -->
			</div><br>	
			<div class="card-action">
				<div class="row">	
					<div class="col-md-6">
						<button class="btn btn-secondary float-right" id="submit" onclick="">Add</button>	
					</div>
					<div class="col-md-6">	
						<button class="btn btn-danger float-left" onclick="">Cancel</button>
					</div>
				</div>
      		</div>
						</div>
					</div>	
				</div>   
			</div>	
      	</div>
	 </div>
	 </div>
	 </div>
	 </div>
	 </div>
	 </div>
	 <script>
	 
	 $(document).ready(function() { 
			
			//alert("add template");
			
 			 Service_Suggestions();
			 
			 //Groupcode_Suggestions();
			
			 $("#submit").click(function() { 
					
				 //alert("add template");
				 
				 Add_Service();
			});	
			
		});

		function Add_Service()
		{
			var data = new FormData();

				 	//data.append("SUBORGCODES", "EXIM");
					data.append("SERVICECODE", $("#serv_code").val());
					data.append("SERVICENAME", $("#serv_name").val()); 
					data.append("AMOUNTLIMIT", $("#amnt").val());
					data.append("FROMTIME", $("#frm_time").val());
					data.append("TOTIME", $("#to_time").val());
					data.append("TEMPLATECONT", $("#temp").val());
					
				$.ajax({		 
				url  :  $("#ContextPath2").val()+"/HDPAY/Service",
				type :  'POST',
				data :  data,
				cache : false,
				contentType : false,
				processData : false,
				success: function (data) 
				{   
					if(data.Result == "Success")
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
			    error: function (jqXHR, textStatus, errorThrown) { 
			    	
			    }
		   });
		}
		
		function Service_Suggestions()
		{
			$("#serv_code").autocomplete({
				source: function(request, response) 
				{
			        $.ajax({
			            url: $("#ContextPath2").val()+"/HDPAY/suggestions/Servicecode",
			            type :  'POST',
			            dataType: "json",
			            data:  
						{    term : request.term	    
						},
			            success: function(data) { response(data); }
			        });
		    	},
			    minLength: 1,
			    select: function(event, ui) 
			    {
			    	//$("#sec").val(ui.item.label);
			    	
			    	$("#serv_code").val(ui.item.label);
			    	
			    	//Generate_Report();
			    }
			 }).autocomplete( "instance" )._renderItem = function(ul,item) 
			  	{
				 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
				}; 	
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
			else if(Type == "validation")
			{
				Swal.fire({
					  icon: 'warning',
					  title: Title ,
					  html: Info
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
	 </script>
	</body>
</html>