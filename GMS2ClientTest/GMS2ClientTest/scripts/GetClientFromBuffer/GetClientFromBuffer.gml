function UpdateClientFromBuffer(buffer, server, clients, mePlayer) {
	var clientID = GetIntFromBuffer(buffer);
	var withUsername = buffer_read(buffer, buffer_bool);
	var clientUsername = "undefined";
	show_debug_message("withUsername = " + string(withUsername));
	if(withUsername) {
		var usernameLength = GetIntFromBuffer(buffer);
		show_debug_message("usernameLength = " + string(usernameLength));
		clientUsername = GetStringFromBuffer(buffer, usernameLength);
	}

	var clientHealth = GetIntFromBuffer(buffer);
	var clientPing = GetIntFromBuffer(buffer);
	var positionBefore = GetPositionFromBuffer(buffer);
	var position = GetPositionFromBuffer(buffer);
	var _direction = GetDirectionFromBuffer(buffer);
	var _rotation = GetFloatFromBuffer(buffer);
	var clientToUpdate = noone;
	
	show_debug_message("client id = " + string(clientID));
	show_debug_message("client username = " + string(clientUsername));
	show_debug_message("client health = " + string(clientHealth));
	show_debug_message("client ping = " + string(clientPing));
	show_debug_message("client position = {" + string(position[0]) + "," + string(position[1]) + "}");
	show_debug_message("client direction = {" + string(_direction[0]) + "," + string(_direction[1]) + "}");
	show_debug_message("client rotation = {" + string(_rotation) + "}");
	
	if(clientID == mePlayer.myId) {
		clientToUpdate = mePlayer;
	} else {
		clientToUpdate = GetClientFromDsList(clients, clientID);
	}

	with(clientToUpdate) {
		myId = clientID;
		lastTargetX = positionBefore[0];
		lastTargetY = positionBefore[1];
		reachedLastTarget = false;
		targetX = position[0];
		targetY = position[1];
		lastSendDirectionX = _direction[0];
		lastSendDirectionY = _direction[1];
		hp = clientHealth;
		if(withUsername) {
			username = clientUsername;
		}
		
		serverHandler = server;
		ping = clientPing;
		image_angle = _rotation;
	}
}

function InitClientFromBuffer(buffer, server, setReady = true, playerType = PlayerTypes.enemy, instanceLayer = "instances"){
	var clientID = GetIntFromBuffer(buffer);
	var withUsername = buffer_read(buffer, buffer_bool);
	var clientUsername = "undefined";
	show_debug_message("withUsername = " + string(withUsername));
	if(withUsername) {
		var usernameLength = GetIntFromBuffer(buffer);
		show_debug_message("usernameLength = " + string(usernameLength));
		clientUsername = GetStringFromBuffer(buffer, usernameLength);
	}

	var clientHealth = GetIntFromBuffer(buffer);
	var clientSpeed = GetIntFromBuffer(buffer);
	var acceleration = GetFloatFromBuffer(buffer);
	var normalDeceleration = GetFloatFromBuffer(buffer);
	var skidDeceleration = GetFloatFromBuffer(buffer);
	var position = GetPositionFromBuffer(buffer);
	var _direction = GetDirectionFromBuffer(buffer);
	var _rotation = GetFloatFromBuffer(buffer);
	var newClient = noone;
	
	show_debug_message("client id = " + string(clientID));
	if(withUsername) {
		show_debug_message("client username = " + string(clientUsername));
	}
	show_debug_message("client health = " + string(clientHealth));
	show_debug_message("client speed = " + string(clientSpeed));
	show_debug_message("client position = {" + string(position[0]) + "," + string(position[1]) + "}");
	show_debug_message("client direction = {" + string(_direction[0]) + "," + string(_direction[1]) + "}");
	show_debug_message("client rotation = {" + string(_rotation) + "}");
	
	switch(playerType) {
		case PlayerTypes.host:
			newClient = instance_create_layer(position[0], position[1], instanceLayer, obj_player_host);
		break;
		
		default:
			newClient = instance_create_layer(position[0], position[1], instanceLayer, obj_player_client);
		break;
	}
	
	with(newClient) {
		myId = clientID;
		lastSendDirectionX = _direction[0]
		lastSendDirectionY = _direction[1]
		mySpeed = clientSpeed;
		hp = clientHealth;
		username = clientUsername;
		serverHandler = server;
		image_angle = _rotation;
		acc = acceleration;
		nd = normalDeceleration;
		sd = skidDeceleration;
		
		ready = setReady;
	}
	return newClient;
}