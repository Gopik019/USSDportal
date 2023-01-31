<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Whatsapp_Banking/CSS & JS5.jsp" %>	
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
		#hello2{
			 text-align:center;
			 color:black;
			 border: 1px solid #6861ce;
			 border-right: 1px solid;
		}
		table, th,td{
			border: 1px solid #6861ce;
			border-collapse: collapse;
			padding: 10px 15px;
			text-align: center;
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
			<br><br>
			<div class ="row">
					<div class="col-md-2">
						<div class="form-group">	
							<label for="">Template Code</label>
						</div>
					</div>
																
					<div class="col-md-4">		
						<input type="text" class="form-control" id="temp_code" placeholder="">
					</div>
					<!-- <div class="col-md-2">
						<div class="form-group">	
							<label for="">Marketing template</label>
						</div>	
					</div>
					<div class="col-md-1">		
						<input type="radio" class="form-check input">
					</div> -->
					<div class="col-md-2">
					<div class="form-group">	
						<label for="">Group code</label>
					</div>
				</div>
				<div class="col-md-4">		
					<input type="text" class="form-control" id="grp_code" placeholder="">
				</div>
					
				</div>
			<br>
			<div class="row" >
				<div class="col-md-2">
					<div class="form-group">	
						<label for="">Template</label>
					</div>
				</div>
				<div class="col-md-6">		
					<textarea class="form-control" rows="5" id="temp_comment"></textarea>
				</div>
				<div class="col-md-4">		
					<button class="btn btn-secondary" style="width:40%" id="add_temp">Add Details</button>
				</div>
			</div>	<br>
							<div class="row">
								<div class="col-md-1"></div>
								<div class="col-md-10">
									<div class="table-responsive sm-table">
				              
									<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
										<thead>
											<tr>
												<th scope="col">Attribute</th>
												<th scope="col">Name</th>
												<th scope="col">Value Mapping</th>
												<th scope="col">Default Value</th>														
											</tr>
										</thead>
										<tbody>	
											<tr>												
												<td><input type="text" class="form-control attrb" placeholder=""></td>
												<td><input type="text" class="form-control name" placeholder=""></td>
												<td><input type="text" class="form-control val_map" placeholder=""></td>
												<td><input class="form-control Default_val" type="text"></td>
											</tr>
											<!-- <tr>												
												<td><input type="text" class="form-control attrb" placeholder=""></td>
												<td><input type="text" class="form-control name" placeholder=""></td>
												<td><input type="text" class="form-control val_map" placeholder=""></td>
												<td><input class="form-control Default_val" type="text"></td>
											</tr> -->
										</tbody>
									</table>
									</div>
								</div>
								<div class="col-md-1"></div>
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
	 </div></div>
	 <script>
	 
	 $(document).ready(function() { 
			
			//alert("add template");
			
 			 Tempcode_Suggestions();
			 
			 Groupcode_Suggestions();
			
			 $("#add_temp").click(function() { 
					
				 //alert("add template");
				 
				 Add_Template();
			});	
			
		});

		function Add_Template()
		{
			var data = new FormData();

				 	//data.append("SUBORGCODES", "EXIM");
					data.append("TEMPCODE", $("#temp_code").val());
					data.append("GROUPCODE", $("#grp_code").val()); 
					data.append("TEMPLATECONT", $("#temp_comment").val());
					data.append("ATTRIBUTENAME", $(".attrb").val());
					data.append("IDNAME", $(".name").val());
					data.append("VALUEMAP", $(".val_map").val());
					data.append("DEFAULTVALUE", $(".Default_val").val());
			
					//alert("$("#Context_Path2").val()+"/HDPAY/Template");
				$.ajax({		 
				url  :  $("#ContextPath2").val()+"/HDPAY/Template",
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
		
		function Tempcode_Suggestions()
		{
			$("#temp_code").autocomplete({
				source: function(request, response) 
				{
			        $.ajax({
			            url: $("#ContextPath2").val()+"/HDPAY/suggestions/TempCode",
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
			    	
			    	//$("#temp_code").val(ui.item.label);
			    	
			    	var Id = ui.item.id;
	    	    	
			    	//alert(Id);
			    	Retrive_template(Id);
			    }
			 }).autocomplete( "instance" )._renderItem = function(ul,item) 
			  	{
				 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
				}; 	
		}
		
		function Groupcode_Suggestions()
		{
			$("#grp_code").autocomplete({
				source: function(request, response) 
				{
			        $.ajax({
			            url: $("#ContextPath2").val()+"/HDPAY/suggestions/GroupCode",
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
			    	
			    	$("#grp_code").val(ui.item.label);
			    	
			    	//Generate_Report();
			    }
			 }).autocomplete( "instance" )._renderItem = function(ul,item) 
			  	{
				 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
				}; 	
		}
		
		function Retrive_template(Id)
		{		
			//var tempcode = $("#temp_code").val();
			
			var data = new FormData();
			
			data.append("TEMPCODE",Id);

			$.ajax({		 
				url  :  $("#ContextPath2").val()+"/HDPAY/Find/Template",
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
						data = data.Informations;
						
						$("#temp_code").val(data.TEMPCODE);
						$("#grp_code").val(data.GROUPCODE);
						$("#temp_comment").val(data.TEMPLATECONT);
						$(".attrb").val(data.ATTRIBUTENAME);
						$(".name").val(data.IDNAME);
						$(".val_map").val(data.VALUEMAP);
						$(".Default_val").val(data.DEFAULTVALUE);
					}
					else
				    {
						Sweetalert("warning", "", data.Message);
				    }	
				},
				beforeSend: function( xhr )
		 		{
					Reset();
					
		 			Sweetalert("load", "", "Please Wait");
		        },
			    error: function (jqXHR, textStatus, errorThrown) 
				{ 
			    	//Sweetalert("warning", "", "errrrr");  
			    }
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