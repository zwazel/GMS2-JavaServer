function GetClientFromBuffer(buffer){
	var clientID = GetIntFromBuffer(buffer);
	var usernameLength = GetIntFromBuffer(buffer);
	var clientUsername = GetStringFromBuffer(buffer, usernameLength);
	var newClient = instance_create_layer(random_range(10, room_width-10), random_range(10, room_height-10),"instances",obj_player_client);
	with(newClient) {
		myId = clientID;
		username = clientUsername;
		ready = true;
	}
	return newClient;
}