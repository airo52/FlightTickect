setTimeout(()=>{
var buttons = document.querySelectorAll('.book');

 buttons.forEach(element => {
         element.addEventListener('click',function(){
       //  $('.container').load("index.php");
       $('.container').css("display","none");
        $('#booking').css("display","block");
        $('#booking').append('<input hidden value='+element.getAttribute('id')+'>');
sendRequest(element.getAttribute('id'));
          //   window.location="index.php?page=booking&flightId="+element.getAttribute('id');


    });
    })
},1000);

function sendRequest(id){
 $.ajax({
    type:"GET",
    url:'?detail='+id,
    async:true,
    success:function(response){
     // resp = JSON.parse(response);
      console.log(response);
       for(var i=0;i<response.length;i++){
           if(resp["id"+i] !== undefined)
            $('#flight').html(resp["Origin"+i]);
           $('#origin').html(resp["Number"+i]);
            $('#To').html(resp["Destination"+i]);
           $('#depature').html(resp["Depature"+i]);
           $('#Time').html(resp["Time"+i]);
   $("#myTable").append('<tr><td>'+resp["Number"+i]+'</td><td>'+resp["Origin"+i]+'</td><td>'+resp["Destination"+i]+'</td><td>'+resp["Time"+i]+'</td><td>'+resp["Depature"+i]+'</td><td><button class="book" onclick="handle(alert("ok"))" id='+resp["id"+i]+'>Book</button></td></tr>')
           // console.log(resp["id"+i]);
            }


    }
 })
}
$(document).ready(function(){

var btt = document.getElementById("send");
btt.onclick = function(){
Tosh();
}
})



function Tosh(){
var data = "?username="+$("#username").val()+"&lastname="+$("#last").val()+"&flight="+$("#flight").html()+"&Origin="+$('#origin').html()+"&depature="+$('#depature').html()+"&Time="+$('#Time').html()+"&id="+$("#booking").val();
$.ajax({
    type:"GET",
    url:data,
    async:true,
    success:function(response){
     alert(response);
    }})
}


