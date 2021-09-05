function InitClientFromBuffer(buffer, server, setReady = true, playerType = PlayerTypes.enemy, instanceLayer = "instances"){
	var clientID = GetIntFromBuffer(buffer);
	var withUsername = buffer_read(buffer, buffer_u8);
	var clientUsername = "undefined";
	show_debug_message("withUsername = " + string(withUsername));
	if(withUsername == 1) {
		var usernameLength = GetIntFromBuffer(buffer);
		clientUsername = GetStringFromBuffer(buffer, usernameLength);
	}

	var clientHealth = GetIntFromBuffer(buffer);
	var clientSpeed = GetIntFromBuffer(buffer);
	var position = GetPositionFromBuffer(buffer);
	var _direction = GetDirectionFromBuffer(buffer);
	var newClient = noone;
	
	switch(playerType) {
		case PlayerTypes.enemy:
		case PlayerTypes.ally:
			newClient = instance_create_layer(position[0], position[1], instanceLayer, obj_player_client);
		break;
		
		case PlayerTypes.host:
			newClient = instance_create_layer(position[0], position[1], instanceLayer, obj_player_host);
			server.mePlayer = newClient;
		break;
		
		default:
			return noone;
	}
	
	with(newClient) {
		myId = clientID;
		x = position[0];
		y = position[1];
		lastSendDirectionX = _direction[0]
		lastSendDirectionY = _direction[1]
		mySpeed = clientSpeed;
		hp = clientHealth;
		username = clientUsername;
		serverHandler = server;
		ready = setReady;
	}
	return newClient;
}