$(document).ready(function() {
	
	$("#light_mode_btn").click(function() { 
		
		window.location.href = $("#ContextPath").val() + "/Dashboard_light";	
	});
	
	$("#dark_mode_btn").click(function() { 
		
		window.location.href = $("#ContextPath").val()+ "/Dashboard_Dark";
	});
	
	$("#mode_type, #light_mode").show(); 
	
	$("#dark_mode").hide(); 
	
});