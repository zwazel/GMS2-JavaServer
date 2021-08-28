/// @description
inputManager = instance_create_layer(0,0,"controls", obj_textInputManager);
ipInput = noone;
portInput = noone;
usernameInput = noone;
latency = 0;
socket = noone;
clients = ds_list_create();
mePlayer = noone;

with(inputManager) {
	other.ipInput = addTextInputBox(room_width/2, room_height/2-32,128, "IP of Server", "gui", "127.0.0.1");
	other.portInput = addTextInputBox(room_width/2, room_height/2+32,32, "Port of Server", "gui", "21337");
}

currentButton = instance_create_layer(room_width/2, room_height/2+96,"gui", obj_button);
with(currentButton) {
	text = "Connect";
	owner = other.id;
	cmd = buttonCommands.connect_to_server;
	ready = true;
}

function sendMoveCommand(dirX, dirY, posX, posY) {
	var buffer = buffer_create(1024, buffer_fixed, 1);
	
	buffer_write(buffer, buffer_s8, networkCommands.send_move_direction);
	PutDirectionInBuffer(buffer, dirX, dirY);
	PutPositionInBuffer(buffer, posX, posY);
	network_send_raw(socket, buffer, buffer_tell(buffer));
	
	buffer_delete(buffer);
}

function buttonPressed(button) {
	switch(button.cmd) {
		case buttonCommands.connect_to_server:
			socket = network_create_socket(network_socket_tcp);
			var ip = ipInput.text;
			var port = real(portInput.text);
			var server = network_connect_raw(socket, ip, port);
			if(server < 0 ) {
				// not connected
				show_debug_message("couldn't connect to the server!");
			} else {
				// connected
				show_debug_message("connected");
				alarm[0] = room_speed;
		
				with(button) {
					instance_destroy();
				}
		
				with(inputManager) {
					destroyAllInputs()
					other.usernameInput = addTextInputBox(room_width/2, room_height/2-32,128, "Username");
				}
				
				currentButton = instance_create_layer(room_width/2, room_height/2+96,"gui", obj_button);
				with(currentButton) {
					text = "Set Username";
					cmd = buttonCommands.set_username;
					owner = other.id;
					ready = true;
				}
				
			}
			break;
		
		case buttonCommands.set_username:
			if(usernameInput.text != "") {
					var buffer = buffer_create(1024, buffer_fixed, 1);
					buffer_seek(buffer, buffer_seek_start, 0);
					buffer_write(buffer, buffer_s8, networkCommands.send_username);
					
					var username = usernameInput.text
					var usernameLength = string_length(username);
					buffer_write(buffer, buffer_s32, usernameLength);
					show_debug_message("username: " + username);
					buffer_write(buffer, buffer_string, username);

					network_send_raw(socket, buffer, buffer_tell(buffer));
					
					mePlayer.username = username;
					mePlayer.ready = true;
					
					instance_destroy(usernameInput);
					instance_destroy(currentButton);
					buffer_delete(buffer);
			}
			break;
	}
}