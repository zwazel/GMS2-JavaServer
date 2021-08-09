/// @description
var inputManager = instance_create_layer(0,0,"controls", obj_textInputManager);
ipInput = noone;
portInput = noone;

with(inputManager) {
	ipInput = addTextInputBox(room_width/2, room_height/2-32,128, "IP of Server", "gui", "127.0.0.1");
	portInput = addTextInputBox(room_width/2, room_height/2+32,32, "Port of Server");
}

var button = instance_create_layer(room_width/2, room_height/2-96,"gui", obj_button);
button.owner = id;
with(button) {
	text = "Connect";
	ready = true;
}