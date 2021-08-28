function GetClientFromBuffer(buffer, server, withUsername = true, setReady = true, playerType = PlayerTypes.enemy, instanceLayer = "instances"){
	var clientID = GetIntFromBuffer(buffer);
	var clientHealth = GetIntFromBuffer(buffer);
	var clientSpeed = GetIntFromBuffer(buffer);
	var position = GetPositionFromBuffer(buffer);
	var clientUsername = "undefined";
	if(withUsername) {
		var usernameLength = GetIntFromBuffer(buffer);
		clientUsername = GetStringFromBuffer(buffer, usernameLength);
	}
	var newClient = noone;
	
	switch(playerType) {
		case PlayerTypes.enemy:
		case PlayerTypes.ally:
			newClient = instance_create_layer(position[0], position[1], instanceLayer, obj_player_client);
		break;
		
		case PlayerTypes.host:
			newClient = instance_create_layer(position[0], position[1], instanceLayer, obj_player_host);
		break;
		
		default:
			return noone;
	}
	
	with(newClient) {
		myId = clientID;
		x = position[0];
		y = position[1];
		mySpeed = clientSpeed;
		hp = clientHealth;
		username = clientUsername;
		serverHandler = server;
		ready = setReady;
	}
	return newClient;
}