<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp"%>

<%@ include file="../../../Headers_&_Footers/FIU/Interpolation/CSS & JS4.JSP"%>

<body>
	<div class="wrapper">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/FIU/common/Side_Bar.jsp" %>   
	
		<div class="main-panel">
			<div class="content">
				<div class="page-inner">

					<%@ include file="../../../Headers_&_Footers/FIU/common/Form_header.jsp" %>   

					<div class="row">

						<div class="col-md-12 mt--1">

							<div id="colour_body" class="card">

								<div id="tab_card" class="card-body">

									<div class="row mt--3">

										<div class="col-md-12">
											<div class="row"></div>
											<br>
											<div class="row">
												<div class="col-md-2"></div>
												<div class="col-md-4">
													<div class="row">
														<div class="col-md-4">
															<div class="form-group">
																<label for="sb">BANK NAME</label>
															</div>
														</div>
														<div class="col-md-8">
															<div class="form-group">
																<input type="text" class="form-control" placeholder="" id="bank">
															</div>
														</div>
													</div>
												</div>
												<div class="col-md-4">
													<div class="row">
														<div class="col-md-3">
															<div class="form-group">
																<label for="sb">Sec ID</label>
															</div>
														</div>
														<div class="col-md-9">
															<div class="form-group">
																<input type="text" class="form-control" placeholder="" id="sec">
															</div>
														</div>
													</div>
												</div>
												<div class="col-md-2">
													
												</div>
												
											</div>
											<hr>	
											<div class="row">
												<div class="col-md-1"></div>
												<div class="col-md-10">
													<div class="table-responsive sm-table">
						              					<table id="example">
												
															<thead>
																<tr role="row" align="center">
																	<th>S.No</th>
																	<th>BANK NAME</th>
																	<th>SEC ID</th>
																	<th>TENOR</th>
																	<th>BID</th>
																	<th>ASK</th>
																	<th>AVG</th>
																</tr>
															</thead>
															
															 <tbody>	
															 	<tr role="row">
															 		<td><input type="text" class="form-control" placeholder="1" disabled ></td>
															 		<td><input type="text" class="form-control bank-name " placeholder="" disabled ></td>
															 		<td><input type="text" class="form-control sec-id " placeholder="" disabled ></td>
															 		<td><input type="text" class="form-control tenor" placeholder=""></td>
															 		<td><input type="number" class="form-control bid"></td>
															 		<td><input type="number" class="form-control ask"></td>
															 		<td><input type="number" class="form-control avg"disabled></td>
																			
															 	</tr>
															 	<tr role="row">
															 		<td><input type="text" class="form-control" placeholder="2" disabled ></td>
															 		<td><input type="text" class="form-control bank-name " placeholder="" disabled ></td>
															 		<td><input type="text" class="form-control sec-id " placeholder=""  disabled></td>
															 		<td><input type="text" class="form-control tenor" placeholder=""></td>
															 		<td><input type="number" class="form-control bid"></td>
															 		<td><input type="number" class="form-control ask"></td>
															 		<td><input type="number" class="form-control avg" disabled></td>
																			
															 	</tr>
															 	<tr role="row">
															 		<td><input type="text" class="form-control" placeholder="3" disabled></td>
															 		<td><input type="text" class="form-control bank-name " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control sec-id " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control tenor" placeholder="" ></td>
															 		<td><input type="number" class="form-control bid"></td>
															 		<td><input type="number" class="form-control ask"></td>
															 		<td><input type="number" class="form-control avg" disabled></td>
																			
															 	</tr>
															 	<tr role="row">
															 		<td><input type="text" class="form-control" placeholder="4" disabled></td>
															 		<td><input type="text" class="form-control bank-name " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control sec-id " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control tenor" placeholder="" ></td>
															 		<td><input type="number" class="form-control bid"></td>
															 		<td><input type="number" class="form-control ask"></td>
															 		<td><input type="number" class="form-control avg" disabled></td>
																			
															 	</tr>
															 	<tr role="row">
															 		<td><input type="text" class="form-control" placeholder="5" disabled></td>
															 		<td><input type="text" class="form-control bank-name " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control sec-id " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control tenor" placeholder="" ></td>
															 		<td><input type="number" class="form-control bid"></td>
															 		<td><input type="number" class="form-control ask"></td>
															 		<td><input type="number" class="form-control avg" disabled></td>
																			
															 	</tr>
															 	<tr role="row">
															 		<td><input type="text" class="form-control" placeholder="6" disabled></td>
															 		<td><input type="text" class="form-control bank-name " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control sec-id " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control tenor" placeholder=""  ></td>
															 		<td><input type="number" class="form-control bid"></td>
															 		<td><input type="number" class="form-control ask"></td>
															 		<td><input type="number" class="form-control avg" disabled></td>
																			
															 	</tr>
															 	<tr role="row">
															 		<td><input type="text" class="form-control" placeholder="7" disabled></td>
															 		<td><input type="text" class="form-control bank-name " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control sec-id " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control tenor" placeholder=""  ></td>
															 		<td><input type="number" class="form-control bid"></td>
															 		<td><input type="number" class="form-control ask"></td>
															 		<td><input type="number" class="form-control avg" disabled></td>
																			
															 	</tr>
															 	<tr role="row">
															 		<td><input type="text" class="form-control" placeholder="8" disabled></td>
															 		<td><input type="text" class="form-control bank-name " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control sec-id " placeholder="" disabled></td>
															 		<td><input type="text" class="form-control tenor" placeholder=""  ></td>
															 		<td><input type="number" class="form-control bid"></td>
															 		<td><input type="number" class="form-control ask"></td>
															 		<td><input type="number" class="form-control avg" disabled></td>
																			
															 	</tr>																																					
															</tbody>
															
														</table> 
												</div>
												<div class="col-md-1"></div>
												</div>
	 										   </div>
										<br>
												<div id="custom_card_action" class="card-action">
													<div class="row">
														<div class="col-md-4"></div>
														<div class="col-md-4">
															<div class="row">
																<div class="col-md-8">
																	<button id="value_add" class="btn btn-secondary">Submit</button>
																</div>
																<div class="col-md-4">
																	<button id="dis" class="btn btn-secondary">Discard</button>
																</div>
															</div>
														</div>
														<div class="col-md-4"></div>
													</div></div>
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
		
		$(".bid").keyup(function() {
			  
			  var index = $( ".bid" ).index(this);
			  
			  var bid =  $(".bid").eq(index).val() * 1;
			  var ask =  $(".ask").eq(index).val() * 1;
			  
 			  bid = bid === '' ? 0 : bid;
			  
			  ask = ask === '' ? 0 : ask;
			  
			  var avg = (bid + ask) /2 ;
			  
			  if(bid != '' && ask != '')
			  {
				  $(".avg").eq(index).val(avg);
			  }  
			});
		
		$(".ask").keyup(function() {
			  
			  var index = $( ".ask" ).index(this);
			  
			  var bid =  $(".bid").eq(index).val() * 1;
			  var ask =  $(".ask").eq(index).val() * 1;
			
			  var avg = (bid + ask) /2 ;
			  
			  if(bid != '' && ask != '')
			  {
				  $(".avg").eq(index).val(avg);
			  }  
			  
			});

		</script>
	
</body>
</html>