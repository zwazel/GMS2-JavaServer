function GetClientFromDsList(list, idToFind){
	var currentClient = noone;
	for(var i = 0; i < ds_list_size(clients); i++) {
		currentClient = ds_list_find_value(clients, i);
		if(currentClient.myId == idToFind) {
			return currentClient;
		}
	}
	return noone;
}