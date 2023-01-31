$(document).ready(function() {

    startTimer();
});

//startTimer();

var minute_calc = 15;
var second_calc = 00;

var timer_calc = minute_calc + ":" + second_calc;

function startTimer() {

    var presentTime = timer_calc;
    var timeArray = presentTime.split(/[:]+/);
    var min = timeArray[0];
    var secd = checkSecond((timeArray[1] - 1));
    if (secd == 59) {
        min = min - 1
    }

    if (min < 0) {
        alert("Your current Session is over due to inactivity.");

        window.location = $("#ContextPath").val() + "/logout";
    }

    timer_calc = min + ":" + secd;
    //document.getElementById('timer').innerHTML =
    //m + ":" + s;
    //console.log(m)
    setTimeout(startTimer, 1000);

}


function checkSecond(sec) {
    if (sec < 10 && sec >= 0) {
        sec = "0" + sec
    }; // add zero in front of numbers < 10
    if (sec < 0) {
        sec = "59"
    };
    return sec;
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
			  timer: 2000,
			  showConfirmButton: false 
		});
	}
	else if(Type == "warning")
	{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  text:  Info ,		 
			  timer: 2000,
			  showConfirmButton: false 
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
	/*
	else if(Type == "Timeout")
		{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  text:  Info ,		 
			  showCancelButton: false,
			 // confirmButtonColor: '#3085d6',
			 // cancelButtonColor: '#d33',
			 // confirmButtonText: 'Yes',	
			  closeOnConfirm: true
			}).then(function (res) {
	        if(res.value)
	        { 
	        	window.location = $("#ContextPath").val() + "/logout";		
	        }
	        else if(res.dismiss == 'cancel')
	        { 
	            return false;
	        }
	        else if(res.dismiss == 'esc')
	        { 
	        	 return false;
	        }
	    });
		}
		
	*/
}