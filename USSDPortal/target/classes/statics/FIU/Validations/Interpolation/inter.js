$(document).ready(function() {  

	Bank_Suggestions();
	
	SecID_Suggestions();
	 
	$(document).on('keyup', '#bank', function() {
			
		  var bank = $("#bank").val();
			
		  $(".bank-name").val(bank);
		  
		  $(".bid, .ask, .avg").val('');
     });
	 
	 $(document).on('change', '#sec', function() {
		
		 var sec = $("#sec").val();
			
		  $(".sec-id").val(sec);
		  
		  $(".bid, .ask, .avg").val('');
     });
	  
	 $("#value_add").click(function() { 
			
			Value_Creation();
		});
	 
	 $("#get_value").click(function() { 
			
		 //table = $('#example').DataTable();
			
		 Generate_Report();
		 
		});
	 
	 $("#excel").click(function() {                      //excel function
			
		 fnExcelReport();
		 
		});

	 $("#pdf").click(function() {   					//pdf function
			
		 alert("PDF");
		 
		 ExportPdf();
		 
		});
});

function SecID_Suggestions()
{
	$("#sec").autocomplete({
		source: function(request, response) 
		{
	        $.ajax({
	            url: $("#ContextPath2").val()+"/Datavision/suggestions/SecID",
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
	    	$("#sec").val(ui.item.label);
	    	
	    	//Generate_Report();
	    }
	 }).autocomplete( "instance" )._renderItem = function(ul,item) 
	  	{
		 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
		}; 	
}

function Bank_Suggestions()
{
	//alert($("#ContextPath2").val()+"/Datavision/suggestions/SecID");
	
	$("#bank").autocomplete({
		source: function(request, response) 
		{
	        $.ajax({
	            url: $("#ContextPath2").val()+"/Datavision/suggestions/Bank",
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
	    	$("#bank").val(ui.item.label);
	    	
	    	//Generate_Report();
	    }
	 }).autocomplete( "instance" )._renderItem = function(ul,item) 
	  	{
		 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
		}; 	
}
	
//########## EXCEL #########################

function fnExcelReport() {
	  var table = document.getElementById('example'); // id of table
	  var tableHTML = table.outerHTML;
	  var fileName = 'Table.xls';

	  var msie = window.navigator.userAgent.indexOf("MSIE ");

	  // If Internet Explorer
	  if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
	    dummyFrame.document.open('txt/html', 'replace');
	    dummyFrame.document.write(tableHTML);
	    dummyFrame.document.close();
	    dummyFrame.focus();
	    return dummyFrame.document.execCommand('SaveAs', true, fileName);
	  }
	  //other browsers
	  else {
	    var a = document.createElement('a');
	    tableHTML = tableHTML.replace(/  /g, '').replace(/ /g, '%20'); // replaces spaces
	    a.href = 'data:application/vnd.ms-excel,' + tableHTML;
	    a.setAttribute('download', fileName);
	    document.body.appendChild(a);
	    a.click();
	    document.body.removeChild(a);
	  }
	}

//##################  PDF     ############################

function ExportPdf() {
    $(function () {
        var doc = new jsPDF('p','pt','a4','pdf');
        doc.autoTable("#table");
            doc.save('Table.pdf'); 
    }); 
}



function Value_Creation()
{
	/*var err = 0;
	
	if(!validation("Gateway_Id", "txt"))          { err++; }
	if(!validation("Gateway_Name", "txt"))          { err++; }
	if(!validation("From_Time", "txt"))     { err++; }
	if(!validation("To_Time", "txt"))     { err++; }
	if(!validation("Protocol", "dd"))    { err++; }
	if(!validation("format", "dd"))       { err++; }
	
	if(err !=0 )
	{
		return;
	}*/

	var data = new FormData();
	
		
	for(var i=0; i<$(".bid").length; i++)
		 {

		 	data.append("SUBORGCODES", "EXIM");
			data.append("BANKS", $(".bank-name").eq(i).val());
			data.append("SECIDS", $(".sec-id").eq(i).val()); 
			data.append("TENORS", $(".tenor").eq(i).val());
			data.append("BIDS", $(".bid").eq(i).val());
			data.append("ASKS", $(".ask").eq(i).val());
			data.append("AVGVAS", $(".avg").eq(i).val());
		 }
	
	$.ajax({		 
		url  :  $("#ContextPath2").val()+"/Datavision/Inter",
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
function Add_Value()
{
	var value = [];
	
	for(var i=0; i<$(".bid").length; i++)
    {
		var bid = $(".bid").eq(i).val();
		var ask = $(".ask").eq(i).val();
		var avg = $(".avg").eq(i).val();
		
		var content = '{'+bid+'-'+ask+'-'+avg+'}';
		
		value.push(content);
		
    }
	
	var bank = $("#bank").val();
	
	var serial = $(".serial").length + 1;
	
	var Id = randomString(6);
	
	var row = '<tr id="'+Id+'"><td class="serial">'+serial+'</td><td>'+bank+'</td><td>'+value+'</td>'+
				'<td><button type="button" class="btn btn-secondary btn-sm" onclick="Remove(\''+Id+'\')"><i class="fas fa-trash"></i></button></td>'+
			  '</tr>';
	
	//$('#example > tbody:last').append(row);	
	
	//$('#my-task-modal').modal('hide');
}

function Remove(Id)
{
	$('#'+Id).remove();
}

function randomString(len) 
{
    var charSet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var randomString = '';
    for (var i = 0; i < len; i++) {
        var randomPoz = Math.floor(Math.random() * charSet.length);
        randomString += charSet.substring(randomPoz,randomPoz+1);
    }
    return randomString;
}


							//###############   Report Generate    ######################
function Generate_Report()
{	
	var sec = $("#sec").val();
	//var date = $("#date").val();
	
	var data = new FormData();
	
	data.append("SECIDS",sec);
	//data.append("EDATE",date);
	
	alert($("#ContextPath").val()+"/Datavision/Interpolation");
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Datavision/Interpolation",
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
				
				var reports = data.Informations;

				reports = eval(reports);
				
				table = $('#example').DataTable( {
					  "aaData": reports,
					  "aoColumns": [
							{ "mData": "BANKS"},			
							{ "mData": "SECIDS"},		
							{ "mData": "TENORS"},  	   
							{ "mData": "BIDS"},	        
							{ "mData": "ASKS"},	
							{ "mData": "AVGVAS"}
					  ],
					  "paging":true,
					  "destroy": true,
					  "deferRender": true,
					  "responsive": true,
					  "bSort": false,
		                "columnDefs" : [
						  {	
					    	  "targets" : 6,
					   		  "render"  : function (data, type, row, meta)
					    	   {
			
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
			logger.debug("Exception in Retrieve_TIPS_Identfifer :::: "+e.getLocalizedMessage());
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