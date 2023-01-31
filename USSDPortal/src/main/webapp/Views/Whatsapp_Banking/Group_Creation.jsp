<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Whatsapp_Banking/CSS & JS6.jsp" %>	
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
				    text-align: center;
				    color: black;
				    border: 1px solid #6861ce;
				    border-right: 1px solid;
				}
		
		#Border-color{
						  border-style: solid;
						  border-color: #92a8d1;
					  }	
			 
		table, th, td {
						    border: 1px solid #6861ce;
						    border-collapse: collapse;
						    padding: 15px 15px;
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
			<!-- <div style="width:40%" >
				<label id="hello" class="form-control">Configuration -  Marketing creation</label>
			</div> -->
			<br>
			<div class ="row">
					<div class="col-md-2">
						<div class="form-group">	
							<label for="">Group code</label>
						</div>
					</div>
																
					<div class="col-md-3">		
						<input type="text" class="form-control" id="grp_code" placeholder="">
					</div>
					<div class="col-md-2">
						<div class="form-group">	
							<label for="">Group Type</label>
						</div>	
					</div>
					<div class="col-md-3">		
						<input type="text" class="form-control" id="grp_type" placeholder="">
					</div>
					<div class="col-md-2">		
						<button type="button" class="btn btn-secondary" style="width:40%" id="get_status">Get</button>
					</div>
					
				</div>
				<br>
			<hr>
			<div class="row">
								<div class="col-md-2"></div>
								<div class="col-md-8">
									<div class="table-responsive sm-table">
				              
									<table id="example2" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
										<thead>
											<tr>
												<th scope="col">Customer No</th>
												<th scope="col">Mobile No</th>
												<th scope="col">Status</th>														
											</tr>
										</thead>
										<tbody>	
										</tbody>
									</table>
									</div>
								</div>
								<div class="col-md-2"></div>
							</div>
			<div class="card-action">
				<div class="row">	
					<div class="col-md-6">
						<button class="btn btn-secondary float-right" id="submit" onclick="">Add</button>	
					</div>
					<div class="col-md-6">	
						<button class="btn btn-danger float-left" onclick="">Reset</button>
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
		 
		 	//alert("hi");
		 	//$('#example2').DataTable();
		 	
		 	Groupcode_Suggestions();
		 	
		 	Grouptype_Suggestions();
			
			 $("#get_status").click(function() { 
				 
				 //table = $('#example2').DataTable();
				 
				 Get_Status();
			});
			
		});

		function Get_Status()
		{	
			var group_code = $("#grp_code").val();
			var group_type = $("#grp_type").val();
			
			var data = new FormData();
			
			data.append("GROUPCODE",group_code);
			data.append("GROUPTYPE",group_type);
			
			//alert($("#ContextPath2").val()+"/HDPAY/Group_Creation");

			$.ajax({		 
				url  :  $("#ContextPath2").val()+"/HDPAY/Group_Creation",
				type :  'POST',
				data :  data,
				cache : false,
				contentType : false,
				processData : false,
				success: function (data) 
				{ 
					Swal.close();
					
					if(data.result == 'success')
					{	
						$('.data_report').html('<table id="example2" class="table table-striped table-hover table-bordered dt-responsive nowrap" aria-describedby="example_info"></table>');

						var reports = data.Informations;

						reports = eval(reports);
						
						table = $('#example2').DataTable( {
							  "aaData": reports,
							  "aoColumns": [
									{ "sTitle" : "Customer No", "mData": "CUSTOMERNO"},					
									{ "sTitle" : "Mobile No", "mData": "MOBILENO"},  	   
									{ "sTitle" : "Status", "mData": "STATUS"}	        
							  ],
							  "destroy": true,
							  "deferRender": true,
							  "responsive": true,
							// "dom": "	<'row'<'col-sm-4'l><'col-sm-4 text-center'B><'col-sm-4'f>>"+
								    //  "<'row'<'col-sm-12'tr>>" +
								     // "<'row'<'col-sm-6'i><'col-sm-6'p>>", 	
								     "dom" : "t",
					          /*  "columnDefs": [
					                {
					                    "targets": [ 0 ],
					                    "visible": true,
					                    "searchable": true
					                }			          
					            ],  */
					           "lengthMenu": [[5, 10, 50, 75, -1], [5, 10, 50, 75, "All"]],
							  "pageLength": 10						 
					    }); 	
						
					}
				},
				beforeSend: function( xhr )
				{	
					Sweetalert("load", "", "Please Wait..");
		        },
			    error: function (jqXHR, textStatus, errorThrown) 
			    { 
			    	
			    }
		        
		   });
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
		
		function Grouptype_Suggestions()
		{
			$("#grp_type").autocomplete({
				source: function(request, response) 
				{
			        $.ajax({
			            url: $("#ContextPath2").val()+"/HDPAY/suggestions/GroupType",
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
			    	
			    	$("#grp_type").val(ui.item.label);
			    	
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