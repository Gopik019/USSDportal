$(document).ready(function() {
	 	
	/*var table = $('#example').DataTable();
	
	table = $('#example').DataTable( {
		  "paging":true,
		  "destroy": true,
		  "deferRender": true,
		  "responsive": true,
		  "searching": false,
  		  "info": true,   
		  "pageLength":5,
		  "lengthChange": false,
		  "fnRowCallback": function (nRow, aData, iDisplayIndex) { var oSettings = this.fnSettings (); $("td:eq(1)", nRow).html(oSettings._iDisplayStart+iDisplayIndex +1);  return nRow; } 						 
	}); 
	
	$('#page_len').on('change', function () {
	    table.page.len( this.value ).draw();
	}); */
		
	/*add_dropdown();	
	
	$(document).on('change','#media', function() {
		
		table.columns().search('').draw();
		 
		var Media = $("#media").val();
		
		if(Media == "Favourite")
		{
			table.column(7).search('true').draw();
			
			table.column(8).order( 'asc' ).draw();
		}
		else
		{
			 table.column(3).search(Media).draw();   
			 
			 table.column(9).order( 'asc' ).draw();	   
		}
	});  
	
	$(document).on('keyup', "input[type='search']", function() {
		
	    table.search(this.value).draw();
   
	}); */
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

function Sweetalert(Type, Title, Info)
{
	if(Type == "success")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text:  Info,		 		  
			  timer: 2000,
			  showConfirmButton: false 
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
	else if(Type == "confirm")
	{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  text:  Info ,		 
			  showCancelButton: true,
			  confirmButtonColor: '#3085d6',
			  cancelButtonColor: '#d33',
			  confirmButtonText: 'Yes',
			  closeOnConfirm: true
			}).then((res) => {
                if(res.value){ 
                //kannan();
                   return true;
                }else if(res.dismiss == 'cancel'){ 
                    return false;
                }
                else if(res.dismiss == 'esc'){ 
                	 return false;
                }
            });
	}	
}