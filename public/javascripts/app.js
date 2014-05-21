$(function(){
  var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
  var socket;
  
  document.getElementById("start").addEventListener("click", function(){
    var id = $("#userId").val();
    console.log("ws://localhost:9000/ws/" + id);
    socket = new WS("ws://localhost:9000/ws/" + id);
    socket.onmessage = function(event) {
      // console.log(event);
      $("#base").append("<p>" + event.data + "</p>");
    };
  }, false);

  document.getElementById("send_0").addEventListener("click", function(){
    var id = $("#userId").val();
    socket.send(id + ":send_0");
  }, false);

  document.getElementById("send_1").addEventListener("click", function(){
    var id = $("#userId").val();
    socket.send(id + "send_1");
  }, false);
  
});