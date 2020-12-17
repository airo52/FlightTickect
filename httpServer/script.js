$(document).ready(function(){
 $.ajax({
    type:"GET",
    url:'demo',
    async:true,
    success:function(response){
      resp = JSON.parse(response);
      console.log(resp);
      for(var i=0;i<response.length;i++){
     if(resp["id"+i] !== undefined)
      $("#myTable").append('<tr><td>'+resp["Number"+i]+'</td><td>'+resp["Origin"+i]+'</td><td>'+resp["Destination"+i]+'</td><td>'+resp["Time"+i]+'</td><td>'+resp["Depature"+i]+'</td><td><button class="book" onclick="handle(alert("ok"))" id='+resp["id"+i]+'>Book</button></td></tr>')
     // console.log(resp["id"+i]);
      }

    }
 })




})

