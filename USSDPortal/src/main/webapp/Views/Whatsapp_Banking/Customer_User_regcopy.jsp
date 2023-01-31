<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Whatsapp_Banking/CSS & JS13.jsp" %>
	
<% 
	String rsalt = PasswordUtils.getSalt();
%>
<style type="text/css">
	.text-danger{
		display:none;
		margin-bottom: 0px !important;
		
	}
	.toggle-password {
    float: right;
    cursor: pointer;
    margin-right: 10px;
    margin-top: -25px;
    }
    
label
{
    width: 100px;
}

.alert
{
    display: none;
}

.requirements
{
    list-style-type: none;
}

.wrong .fa-check
{
    display: none;
}

.good .fa-times
{
    display: none;
}


</style>
<body data-background-color="${Menu.get('body_color').getAsString()}">

	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Navigation_Bar.jsp" %>  
		     <%@ page import="com.hdsoft.utils.PasswordUtils" %>

			<%@ page import="com.hdsoft.utils.WebContext"%>   
		     
		</div>
	
		<%@ include file="../../../Headers_&_Footers/HDPAY/common/Side_Bar.jsp" %>   
		
    <div class="main-panel">
			<div class="content">
				<div class="page-inner">
					
				<%@ include file="../../../Headers_&_Footers/HDPAY/common/Form_header.jsp" %>   
					
					<div class="row">
					
					<div class="col-md-12 mt--1">
							<div id="colour_body" class="card">
						
							<div class="container"> 
							   <div class="card-body">
								<div class="container">
								  <div class="row" style="margin-bottom:30px" >	
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Suborg code</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<!-- <input type="text" class="form-control white" id="torgcd" value="EXIM" placeholder="" maxlength="25" size="20" readonly> -->
												<input type="text" class="form-control white" id="torgcd"  value="EXIM" placeholder="" maxlength="25" size="20" disabled="disabled">  <!-- style="text-align:center;" -->      
												<label id="torgcd_error" class="text-danger"></label>
											</div>
										</div>
									
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Channel code</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<select class="form-control" id="trolecd">
												    <option value="">Select</option>
													<option value="WHTAPP">Whatsapp-Banking</option>
													<option value="USSD">USSD</option>
												</select>
												<label id="trolecd_error" class="text-danger"></label>
											</div>
										</div>
									</div>
								</div>
									
								
								<div class="container">
								<div class= "col-m-12" >
								
								<div class="row" style="margin-bottom:30px" >											
										<div class="col-md-2">
											<div class="form-group">	
												<label for="number">Primary Mobile No</label>
											</div>
										</div>
										<div class="col-md-1">
											<div class="form-group">
											<input type="text" class="form-control"name="phone" id="phone"  placeholder="" maxlength="15" size="20">
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
											<input type="text" class="form-control"name="phone"  placeholder="" maxlength="15" size="20">
											        <!--  <select class="form-control" id="tcntycd">
												    </select> -->  
												    <!-- <option value="">Select</option>  -->
												<label id="tcntycd_error" class="text-danger"></label>
											</div>
										</div>
										
										
										<div class="col-md-2">
											<div class="form-group">	
												<label >Registration Date</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
														<input type="text" class="form-control " id="tregdate"  placeholder="" maxlength="10" size="12"  disabled="disabled">
												<label id="tregdate_error" class="text-danger"></label>
											</div>
										</div>
																												
									</div>
								
								</div>
								
								  
								</div>
								
								<div class="justify-content-center">
								        <div class="row">	
											<div class="col-md-6">
												<div class="form-group" align="center" style="margin-left:290px">	
													<label for="email2">Status</label>
												</div>
											</div>
											<div class="col-md-6">													
												<div class="form-check">
													<label class="switch small">
														<input id="Amount_limit" class="form-check-input" type="checkbox" value="">
														<span class="slider round small"></span>
													</label>
													<label id="Amount_limit_error" class="text-danger"></label>
												</div>
											</div>	
									  </div>
								 </div>
								
								
								
								
								
		
									  
									<div class="card-action">
									  <div class="container"> 
									    <div class="row">	
										
										<div class="col-md-6">
											<button class="btn btn-secondary float-right" id= "submit" onclick="proceed()">Submit</button>	
										</div>
										
										<div class="col-md-6" style="margin">	
											<button class="btn btn-danger float-left" onclick="reset();">Reset</button>
											
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
   
   <!-- ############### Model 1 for Adding Country code ################ -->
      	
      <!-- 	<div class="modal fade bd-example-modal-md" id="modal1" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  			<div class="modal-dialog modal-md">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLongTitle">Mobile Code Form</h5>
		        <button type="button" class="close" data-dismiss="modal" onclick="reset();" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">	        
	             
				   <div id=""  class="row">						                		
						<div class="col-md-4">
							<div class="form-group">	
								<label for="">Country code</label>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="form-group">		
								<input type="text" class="form-control api_key" id="cntycd" placeholder="">
								<label id="cntycd_error" class="text-danger"></label>
							</div>
						</div>	
					</div>
					
				  <div id=""  class="row">	 
				       <div class="col-md-4">
							<div class="form-group">	
								<label for="">Country Name </label>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="form-group">		
								<input type="text" class="form-control api_key" id="cntyname" placeholder="">
								<label id="cntyname_error" class="text-danger"></label>
							</div>
						</div>
				  </div>	
				  
				  
				  <div id=""  class="row">	 
				       <div class="col-md-4">
							<div class="form-group">	
								<label for="">Mobile code</label>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="form-group">		
								<input type="text" class="form-control api_key" id="mbcd" placeholder="">
								<label id="mbcd_error" class="text-danger"></label>
							</div>
						</div>
				  </div>	
				 			  
				  
				  <div id=""  class="row">	 
				        <div class="col-md-7">
							<div class="form-group"> 														
								<button class="btn btn-secondary float-right" id= "submit" onclick="Add_countrycode()">ADD</button>	
							</div>
						</div>	
				  </div>				                		
					
						
							
															
		      </div>
		      
		      <div class="modal-footer">
		        
		      </div>
		    </div>
		  </div>
		</div>
		
    -->

        

        <script>
            var input = document.querySelector("#phone");
            window.intlTelInput(input, {
                separateDialCode: true,
                excludeCountries: [],
                preferredCountries: ["In", "tz", "dj", "km",]
            });
        </script>
	
	</body>

</html>