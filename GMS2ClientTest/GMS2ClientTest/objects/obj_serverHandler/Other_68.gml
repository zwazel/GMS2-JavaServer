/// @description
var n_id = ds_map_find_value(async_load, "id");
if n_id == socket {
	show_debug_message("we have a new packet from the server!");
	
	var t = ds_map_find_value(async_load, "type");
    switch(t) {
        case network_type_data:
			receivedPackages++;
			show_debug_message(json_encode(async_load));
		
			var bufferIn = ds_map_find_value(async_load, "buffer");
		    buffer_seek(bufferIn, buffer_seek_start, 0);
			
		    var messageId = buffer_read(bufferIn, buffer_u8);
		    show_debug_message("message id: "+string(messageId));
  
		    switch (messageId) {
				case networkCommands.test:
				break;
				
				case networkCommands.receive_my_id:
					mePlayer = GetClientFromBuffer(bufferIn, id, false, false, PlayerTypes.host);
				break;
				
				case networkCommands.send_ping:
					var timeFromServer = GetIntFromBuffer(bufferIn);
					latency = current_time - timeFromServer;
					
					sendFinalPing(latency);
					break;
					
				case networkCommands.client_connect:
				show_debug_message("NEW CLIENT");
					var newClient = GetClientFromBuffer(bufferIn, id);
					ds_list_add(clients, newClient);
				break;
					
				case networkCommands.get_all_clients:
					var amountClients = GetIntFromBuffer(bufferIn);
					for(var i = 0; i < amountClients; i++) {
						var newClient = GetClientFromBuffer(bufferIn, id);
						ds_list_add(clients, newClient);
					}
				break;
				
				case networkCommands.client_disconnect:
					var clientID = GetIntFromBuffer(bufferIn);
					
					for(var i = 0; i < ds_list_size(clients); i++) {
						var currentClient = ds_list_find_value(clients, i);
						if(currentClient.myId == clientID) {
							var dsListIndex = ds_list_find_index(clients, currentClient);
							ds_list_delete(clients, dsListIndex);
							instance_destroy(currentClient);
							break;
						}
					}
				break;
				
				case networkCommands.get_move_direction:
					var clientID = GetIntFromBuffer(bufferIn);
					var directions = GetDirectionFromBuffer(bufferIn);
					var positions = GetPositionFromBuffer(bufferIn);
					
					var client = GetClientFromDsList(clients, clientID);
					if(client!=noone) {
						client.setMyDirection(directions[0], directions[1], positions[0], positions[1]);
					}
				break;
				
				case networkCommands.receive_ping_other:
					var clientID = GetIntFromBuffer(bufferIn);
					var ping = GetIntFromBuffer(bufferIn);
					var client = GetClientFromDsList(clients, clientID);
					if(client != noone) {
						client.ping = ping;
					}
				break;
				
				default:
				break;
			}
    }
}