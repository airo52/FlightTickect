

$("#Login").click(function(){
var username = $('#username').val();
var password = $("#password").val();
$.ajax({
type:"GET",
url:"?user="+username+"&password="+password,
success:function(response){
  if(response === "success"){
    $("#pages").css("display","block");
    $("#Log").css("display","none");
  }else{
  alert(response)
  }

}
})
})


$(document).ready(function(){
var elements = ["AddFlights","customers","Flights","customer","add","Flight","submit"];

elements.forEach(elements =>{
window[elements] = document.getElementById(elements);
})

AddFlights.addEventListener('click',function(){
add.style.display="block";
customer.style.display="none";
Flight.style.display="none";
})

customers.addEventListener('click',function(){
add.style.display="none";
customer.style.display="block";
Flight.style.display="none";
getCustomers();
})


Flights.addEventListener('click',function(){
add.style.display="none";
customer.style.display="none";
Flight.style.display="block";
listFlights();
})

})

submit.addEventListener("click",function(){
  var data = "?FlightNumber="+$("#FlightNumber").val()+"&Origin="+$("#Origin").val()+"&Destination="+$("#Destination").val()+"&Depature="+$("#Depature").val()+"&Time="+$("#Time").val();
  addFlight(data);
})

function listFlights(){
$('#myTable tr').remove();
$.ajax({
    type:"GET",
    url:'demo',
    async:true,
    success:function(response){
      resp = JSON.parse(response);
      console.log(resp);
      for(var i=0;i<response.length;i++){
     if(resp["id"+i] !== undefined)
      $("#myTable").append('<tr><td>'+resp["Number"+i]+'</td><td>'+resp["Origin"+i]+'</td><td>'+resp["Destination"+i]+'</td><td>'+resp["Time"+i]+'</td><td>'+resp["Depature"+i]+'</td><td><button class="book"  id='+resp["id"+i]+'>Remove</button></td></tr>')
     // console.log(resp["id"+i]);
      }

    }



 })

  setTimeout(()=>{

 var buttons = document.querySelectorAll('.book');

  buttons.forEach(element => {

          element.addEventListener('click',function(){
        //  alert(element.getAttribute("id"));
         RemoveFlight(element.getAttribute('id'));
         })
  })
  },1000);
}




function addFlight(data){
$.ajax({
    type:"GET",
    url:data,
    async:true,
    success:function(response){

alert(response);
    }
 })
}

function RemoveCustomer(id){
$.ajax({
    type:"GET",
    url:"?RemoveCustomer="+id,
    async:true,
    success:function(response){
if(response === "removed")
 getCustomers();
alert(response);
    }
 })
}
function RemoveFlight(id){
$.ajax({
    type:"GET",
    url:"?RemoveFlight="+id,
    async:true,
    success:function(response){
if(response === "removed")
 getCustomers();
alert(response);
    }
 })
}




function getCustomers(){
$('#myTabCustomer tr').remove();
$.ajax({
    type:"GET",
    url:'customers',
    async:true,
    success:function(response){
      resp = JSON.parse(response);
          console.log(resp);
          for(var i=0;i<response.length;i++){
         if(resp["id"+i] !== undefined)
          $("#myTabCustomer").append('<tr><td>'+resp["username"+i]+'</td><td>'+resp["Origin"+i]+'</td><td>'+resp["flight"+i]+'</td><td>'+resp["Depature"+i]+'</td><td><button class="RemoveCustomer"  id='+resp["id"+i]+'>Remove</button></td></tr>')
         // console.log(resp["id"+i]);
          }

    }
 })
 setTimeout(()=>{

var buttons = document.querySelectorAll('.RemoveCustomer');

 buttons.forEach(element => {

         element.addEventListener('click',function(){
        RemoveCustomer(element.getAttribute('id'));
        })
 })
 },1000);

}
