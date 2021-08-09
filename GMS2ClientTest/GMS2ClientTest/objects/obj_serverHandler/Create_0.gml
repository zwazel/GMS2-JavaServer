/// @description
var ipInput = instance_create_layer(room_width/2, room_height/2-32, "gui",obj_TextInputBox);
with(ipInput) {
	placeholer = "IP of server";
	max_chars = 128;
	ready = true;
}