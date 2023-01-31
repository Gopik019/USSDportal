var table;

$(document).ready(function() {
	
	table = $('#example').DataTable();
	
	Generate_Report();
});

function Generate_Report()
{	
	var data = new FormData();

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/TIPS/Identifier/View",       
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
				var Identifiers = data.Informations;

				Identifiers = eval(Identifiers);
				
				table = $('#example').DataTable( {
					  "aaData": Identifiers,
					  "aoColumns": [
							{ "mData": "SERIAL"},			
							{ "mData": "IDENTIFIERNO"},		
							{ "mData": "IDENTIFIERNAME"},  	   
							{ "mData": "ACCOUNTNO"},	        
							{ "mData": "MSISDN"},	
							{ "mData": "CUSTNO"},
							{ "mData": "STATUS"},
							{ "mData": "IDENTIFIERNO"}
					  ],
					  "paging":true,
					  "destroy": true,
					  "deferRender": true,
					  "responsive": true,
					  "bSort": false,
		                "columnDefs" : [
						  {	
					    	  "targets" : 7,
					   		  "render"  : function (data, type, row, meta)
					    	   {
					        		if (type === 'display')
					        		{
					        			if(row.STATUS === 'Active')
					        			{
					        				data = '<button type="button" class="btn btn-success btn-sm2" onclick="Status_details(\''+row.IDENTIFIERNO+'\',\''+row.ACCOUNTNO+'\',\''+row.MSISDN+'\')"><i class="fas fa-power-off"></i></button>' +
					            			   '<button type="button" class="btn btn-secondary btn-sm2 ml-2" onclick="Get_details(\''+row.IDENTIFIERNO+'\',\''+row.ACCOUNTNO+'\',\''+row.MSISDN+'\')"><i class="fas fa-eye"></i></button>';
					        			}
					        			else
					        			{
					        				data = '<button type="button" class="btn btn-danger btn-sm2" onclick="Status_details(\''+row.IDENTIFIERNO+'\',\''+row.ACCOUNTNO+'\',\''+row.MSISDN+'\')"><i class="fas fa-power-off"></i></button>' +
					            			   '<button type="button" class="btn btn-secondary btn-sm2 ml-2" onclick="Get_details(\''+row.IDENTIFIERNO+'\',\''+row.ACCOUNTNO+'\',\''+row.MSISDN+'\')"><i class="fas fa-eye"></i></button>';
					        			}		 			   
					        		}
			
					        		return data;
					    	 	}		
						   }	
					  ],
		          "lengthMenu": [[5, 10, 50, 75, -1], [5, 10, 50, 75, "All"]],
				  "pageLength":10						 
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

function Status_details()
{
	$('#modal2').modal('show');
	
	return;
}

function Get_details(IDENTIFIERNO, ACCOUNTNO, MSISDN)
{
	
	$('#modal1').modal('show');
	
	return;
	
	var data = new FormData();

	data.append("IDENTIFIERNO", IDENTIFIERNO);
	data.append("ACCOUNTNO", ACCOUNTNO);
	data.append("MSISDN", MSISDN);
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Candidates/Virtual_Id",       
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			Swal.close();
			
			if(data.Result == 'Success')
			{
				var Candidate = data.Candidate;
				
				var Office = data.Office;
				
				$('#c_name').html('<b>'+Candidate.Name+'</b>');
				$('#profession').html(Candidate.Role); 
				$('#cand_id').html('<b>'+Candidate.User_Id+'</b>');
				$('#comp_name').html('<b>'+Office.Name+'</b>');
				$('#cand_email').html('<b>Email : </b> <br> '+Candidate.Email_Id);
				$('#cand_mob').html('<b>Mobile No : </b> <br> '+Candidate.Contact_No);
				$('#off_address').html('<b>Office Address : </b> <br> '+Office.Address_1+', <br>'+Office.Area+', <br>'+Office.City+' - '+Office.Pincode);	
				$('#cand_qr').attr('src', Candidate.QR_CODE); 
				$('#profile').attr('src', Candidate.Image); 
				
				$('#modal1').modal('show');
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
			  text:  Info, 	 
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

/*
$(document).ready(function() {	   
	
	var currSeconds = 0;  var max_time = 0;
	
	$(this).mousemove(function (e) {
		currSeconds = max_time;
    });
	
	$(this).keypress(function (e) {
		currSeconds = max_time;
    });

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Session/Interval",       
		type :  'POST',
		data :  '',
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			max_time = Number(data.Interval);
			
			currSeconds = max_time;
		
		    var timer = setInterval(function() {
	    	 
			    currSeconds--;  
			   	   
				if(currSeconds < 1 )
			    {
					clearInterval(timer);
					
					$(this).bind("contextmenu", function(e) {
		                e.preventDefault();
		            });
				  
				    Swal.fire({
						  icon: 'warning',
						  title: '' ,
						  text:  'Session Timeout !',
						  allowOutsideClick: false
					 }).then(function (result) {
						  if (true)
						  {							  
							  window.location.href = $("#ContextPath").val()+ "/logout";	
						  }
					  });		   
			    }
		    
			 }, 1000);
		},
		error: function (jqXHR, textStatus, errorThrown)  {}
	 });
}); */