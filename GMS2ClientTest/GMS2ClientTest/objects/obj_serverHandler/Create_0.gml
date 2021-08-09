/// @description
var inputManager = instance_create_layer(0,0,"controls", obj_textInputManager);
ipInput = noone;
portInput = noone;

with(inputManager) {
	ipInput = addTextInputBox(room_width/2, room_height/2-32,128, "IP of Server");
	portInput = addTextInputBox(room_width/2, room_height/2+128,32, "Port of Server");
}