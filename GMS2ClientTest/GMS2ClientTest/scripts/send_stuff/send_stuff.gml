// All send* functions go here
function sendHello(socket) {
	send({cmd: "hello", myMessage: "Hello World!"}, socket)
}