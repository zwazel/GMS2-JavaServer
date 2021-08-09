/// @description
var inputManager = instance_create_layer(0,0,"controls", obj_textInputManager);
ipInput = noone;
portInput = noone;
connectToServerButton = noone;
socket = noone;
latency = 0;
global.buffer = buffer_create(1024, buffer_fixed, 1);

with(inputManager) {
	ipInput = addTextInputBox(room_width/2, room_height/2-32,128, "IP of Server","gui", "127.0.0.1");
	portInput = addTextInputBox(room_width/2, room_height/2+32,32, "Port of Server");
}

function buttonPressed() {
	//socket = network_create_socket(network_socket_tcp);
	//var ip = "";
	//show_debug_message("ipInput = " + object_get_name(ipInput))
	//with(ipInput) {
	//	ip = getText();
	//}
	//show_debug_message("IP = " + ip);
	//var port = 0;
	//show_debug_message("portInput = " + object_get_name(portInput))
	//with(portInput) {
	//	port = real(getText());
	//}
	//show_debug_message("PORT = " + string(port))
	//network_connect(socket, ip, port);

	//latency = 0;
	//alarm[0] = 30; 
}

connectToServerButton = instance_create_layer(room_width/2, room_height/2+64,"gui", obj_button);
connectToServerButton.owner = id;
with(connectToServerButton) {
	text = "Connect to server";
	ready = true;
}