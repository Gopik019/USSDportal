$(document).ready(function() {
	
	$("#Module_Id").change(function() { 
		
		 $("#nonstop_events, #fixed_events").html('');
		
		 Find_Events();
	});
	
	$("#Add_event_Monitoring").click(function() { 
		
		window.location.href = $("#ContextPath").val()+"/Event_Creation";
	});
	
	$("#nonstop_events_card, #fixed_events_card").hide();
	
	Find_Events();
});

function Find_Events()
{
	var Module_Id = $("#Module_Id").val(); 
	
	if(Module_Id == 'Select')
	{
		$("#nonstop_events_card, #fixed_events_card").hide();
		
		$("#nonstop_events, #fixed_events").html('');
		
		return;
	}
	
	var data = new FormData();
	
	data.append("Module_Id" , Module_Id);

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Events/Find/Fixed_and_Nonstop",
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
				if(data.Nonstop_Events.length !=0)
				{
					$("#nonstop_events_card").show();
				}
				else
				{
					$("#nonstop_events_card").hide();
				}
				
				if(data.Fixed_Events.length !=0)
				{
					$("#fixed_events_card").show();
				}
				else
				{
					$("#fixed_events_card").hide();
				}
				
				Add_Row_values(data.Nonstop_Events, 'nonstop_events');
				
				Add_Row_values(data.Fixed_Events, 'fixed_events');
			}
			else
		    {
				$("#nonstop_events_card, #fixed_events_card").hide();
				
				$("#nonstop_events, #fixed_events").html('');
		    }	
		},
		beforeSend: function( xhr )
 		{
 			Sweetalert("load", "", "Please Wait");
        },
	    error: function (jqXHR, textStatus, errorThrown) { }
   });
}

function Add_Row_values(data, div_id)
{
	var content = '';
	
	for(var i=0;i<data.length;i++)
	{
		var Module = data[i].Module;
		var Event_Code = data[i].Event_Code;
		var Event_Name = data[i].Event_Name;
		var Event_Type = data[i].Event_Type;
		var Event_Sub_Type = data[i].Event_Sub_Type;
		var Start_Time = data[i].Start_Time;
		var End_Time = data[i].End_Time;
		
		content+= '<div class="col-md-3">'+
						'<div class="events_monitor">'+
							'<div class="fluid_row">'+
								'<div class="col-5 event_header">'+
									'<h2>Mail Alert </h2>'+
									'<a href="#" style="margin-top: 13px;">View Details</a>'+
								'</div>'+
								'<div class="col-7 event_content">'+
									'<h6 style="opacity: 1.6">CODE - <span class="evt_code">'+Event_Code+'</span></h6>'+
									'<br><br>'+
									'<span class="border-bottom border-dark"></span>'+
									'<h6 style="opacity: 1.6">TIME - '+Start_Time+ ' to ' +End_Time+'</h6>'+
								'</div>'+																			
							'</div>'+
						'</div>'+
					'</div>';
	}
	
	$("#"+div_id).html(content);
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