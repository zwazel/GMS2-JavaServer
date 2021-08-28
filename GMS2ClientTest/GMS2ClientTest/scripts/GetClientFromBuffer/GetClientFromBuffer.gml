function GetClientFromBuffer(buffer, withUsername = true, setReady = true, playerType = PlayerTypes.enemy, instanceLayer = "instances"){
	var clientID = GetIntFromBuffer(buffer);
	var clientX = GetDoubleFromBuffer(buffer);
	var clientY = GetDoubleFromBuffer(buffer);
	var clientUsername = "undefined";
	if(withUsername) {
		var usernameLength = GetIntFromBuffer(buffer);
		clientUsername = GetStringFromBuffer(buffer, usernameLength);
	}
	var newClient = noone;
	
	switch(playerType) {
		case PlayerTypes.enemy:
		case PlayerTypes.ally:
			newClient = instance_create_layer(clientX, clientY,instanceLayer,obj_player_client);
		break;
		
		case PlayerTypes.host:
			newClient = instance_create_layer(clientX, clientY,instanceLayer,obj_player_host);
		break;
		
		default:
			return noone;
	}
	
	with(newClient) {
		myId = clientID;
		x = clientX;
		y = clientY;
		username = clientUsername;
		ready = setReady;
	}
	return newClient;
}