function timer(a){a=JSON.parse(a),this.element=$(a.element),this.time=0,this.maxTime=60,this.counter=1e3,this.dialWidth=5,this.size=150,this.fontSize=20,this.fontColor="rgb(135, 206, 235)";var b=0,c=this,d=0,e=this.element.find("div")[0].parentNode.dataset;this.initialise=function(){c.time=parseInt(e.timerStarttime)||parseInt(a.startTime)||0,c.maxTime=parseInt(e.timerMaxtime)||parseInt(a.maxTime)||60,c.counter=parseInt(e.timerCounter)||parseInt(a.counter)||1e3,c.dialWidth=parseInt(e.timerDialwidth)||parseInt(a.dialWidth)||5,c.size=e.timerSize||a.size||"150px",c.fontSize=e.timerFontsize||a.fontSize||"20px",c.fontColor=e.timerFontcolor||a.fontColor||"rgb(135, 206, 235)",c.element.find(".loader-bg").css("border-width",c.dialWidth+"px"),c.element.find(".loader-spinner").css("border-width",c.dialWidth+"px"),c.element.css({width:c.size,height:c.size}),c.element.find(".loader-bg .text").css({"font-size":c.fontSize,color:c.fontColor})},this.initialise(),this.timer=setInterval(function(){c.time<c.maxTime?(c.time+=parseInt(c.counter/1e3),b=100*c.time/c.maxTime,c.renderProgress(b),d=new Date(null),d.setSeconds(c.time),c.element.find(".text").html(d.toISOString().substr(11,8)),c.element.find(".text")[0].dataset.time=c.time):clearInterval(c.timer)},this.counter),this.renderProgress=function(a){a=Math.floor(a);var b=0;a<25?(b=-90+a/100*360,c.element.find(".animate-0-25-b").css("transform","rotate("+b+"deg)")):a>=25&&a<50?(b=-90+(a-25)/100*360,c.element.find(".animate-0-25-b").css("transform","rotate(0deg)"),c.element.find(".animate-25-50-b").css("transform","rotate("+b+"deg)")):a>=50&&a<75?(b=-90+(a-50)/100*360,c.element.find(".animate-25-50-b, .animate-0-25-b").css("transform","rotate(0deg)"),c.element.find(".animate-50-75-b").css("transform","rotate("+b+"deg)")):a>=75&&a<=100&&(b=-90+(a-75)/100*360,c.element.find(".animate-50-75-b, .animate-25-50-b, .animate-0-25-b").css("transform","rotate(0deg)"),c.element.find(".animate-75-100-b").css("transform","rotate("+b+"deg)"))}}$(document).ready(function(){var a='{"element" : ".correctTimer"}';$(".correctTimer").each(function(b,c){new timer(a)})});