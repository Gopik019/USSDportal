<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Whatsapp_Banking/CSS & JS7.jsp" %>	
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
			 <!-- <div style="width:50%">
				<label id="hello" class="form-control">Configuration -  Marketing campaign</label>
			 </div> -->
			 <br>
			<div class ="row">
					<div class="col-md-2">
						<div class="form-group">	
							<label for="">Marketing template</label>
						</div>
					</div>
																
					<div class="col-md-3">		
						<input type="text" class="form-control" id="ac_no" placeholder="">
					</div>
					<div class="col-md-2">
						<div class="form-group">	
							<label for="">Group Code</label>
						</div>	
					</div>
					<div class="col-md-3">		
						<input type="text" class="form-control" id="ac_no" placeholder="">
					</div>
					<div class="col-md-2">		
						<button type="button" class="btn btn-secondary" style="width:40%">View</button>
					</div>
					
				</div>
				<div class ="row">
					<div class="col-md-2">
						<div class="form-group">	
							<label for=""> Message</label>
						</div>
					</div>
																
					<div class="col-md-8">		
						<textarea class="form-control" rows="5" id="comment"></textarea>
					</div>
				</div>
			<br>
			<div class="card-action">
				<div class="row">	
					<div class="col-md-6">
						<button class="btn btn-secondary float-right" id="submit" onclick="">Send</button>	
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

   </body>
</html>