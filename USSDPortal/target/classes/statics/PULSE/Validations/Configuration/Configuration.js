$(document).ready(function() {
	
	$("#t1_add_header").click(function() { 
		
		Add_Header('#t1_headers');
	});
	
	$("#t1_add_input").click(function() { 
		
		Add_Header('#t1_inputs');
	});
	
	$("#t2_add_header").click(function() { 
		
		Add_Header('#t2_headers');
	});
	
	$("#t2_add_input").click(function() { 
		
		Add_Header('#t2_inputs');
	});
	
	$("#t3_add_header").click(function() { 
		
		Add_Header('#t3_headers');
	});
	
	$("#t3_add_input").click(function() { 
		
		Add_Header('#t3_inputs');
	});
	
	$("#t4_add_header").click(function() { 
		
		Add_Header('#t4_headers');
	});
	
	$("#t4_add_input").click(function() { 
		
		Add_Header('#t4_inputs');
	});
});

function Demo()
{
	var data = new FormData();
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			
		},
		beforeSend: function( xhr )
		{
			
        },
	    error: function (jqXHR, textStatus, errorThrown) 
	    { 
	    	
	    }
   });
}

function Add_Header(Id)
{
	var rnd = randomString(6);
		
	$(Id).append(
		
		'<div id="'+rnd+'" class="row">'+
    		'<div class="col-md-1"></div>'+
			'<div class="col-md-2"></div>'+
				
			'<div class="col-md-3">'+
				'<div class="form-group">'+
					'<input type="text" class="form-control" id="" placeholder="Key">'+
				'</div>'+
			'</div>'+
			
			'<div class="col-md-3">'+
				'<div class="form-group">'+
					'<input type="text" class="form-control" id="" placeholder="Value">'+
				'</div>'+
			'</div>'+
			
			'<div class="col-md-2">'+
				'<div class="form-group">'+
					'<button class="btn btn-danger btn-round" onclick="Remove_header(\''+rnd+'\')">Remove</button>'+
				'</div>'+
			'</div>'+							
		'</div>'	
	); 
}

function Remove_header(Id)
{
	$("#"+Id).remove();
}

function randomString(length) 
{
	var chars =  '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
	
    var result = '';

    for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];

    return result;
}

function change_active(id)
{
	if(id == 'pills-initiate')
	{
		$('#pills-initiate_main_tab').addClass('main_tab_round mtr_active');
		$('#pills-initiate_main_tab_line').addClass('main_tab_line_round mtr_active');
			
		if($('#pills-request_main_tab').hasClass('main_tab_round mtr_active'))
		{
    		$('#pills-request_main_tab, #pills-request_main_tab_line').removeClass('mtr_active');
		}
		
		if($('#pills-Statement_main_tab').hasClass('main_tab_round mtr_active'))
		{
    		$('#pills-Statement_main_tab, #pills-Statement_main_tab_line').removeClass('mtr_active');
		}
		
		if($('#pills-Destination_main_tab').hasClass('main_tab_round mtr_active'))
		{
    		$('#pills-Destination_main_tab, #pills-Destination_main_tab_line').removeClass('mtr_active');
		}
	}
	
	if(id == 'pills-request')
	{
		$('#pills-request_main_tab').addClass('main_tab_round mtr_active');
		$('#pills-request_main_tab_line').addClass('main_tab_line mtr_active');
			
		if($('#pills-Statement_main_tab').hasClass('main_tab_round mtr_active'))
		{
    		$('#pills-Statement_main_tab, #pills-Statement_main_tab_line').removeClass('mtr_active');
		}
		
		if($('#pills-Destination_main_tab').hasClass('main_tab_round mtr_active'))
		{
    		$('#pills-Destination_main_tab, #pills-Destination_main_tab_line').removeClass('mtr_active');
		}
	}
	
	if(id == 'pills-Statement')
	{
		$('#pills-Statement_main_tab').addClass('main_tab_round mtr_active');
		$('#pills-Statement_main_tab_line').addClass('main_tab_round_tab_line mtr_active');
			
		if($('#pills-Destination_main_tab').hasClass('main_tab_round mtr_active'))
		{
    		$('#pills-Destination_main_tab, #pills-Destination_main_tab_line').removeClass('mtr_active');
		}
	}
	
	if(id == 'pills-Destination')
	{
		$('#pills-Destination_main_tab').addClass('main_tab_round mtr_active');
		$('#pills-Destination_main_tab_line').addClass('main_tab_round_tab_line mtr_active');
	}
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