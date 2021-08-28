/// @description
var n_id = ds_map_find_value(async_load, "id");
if n_id == socket {
	show_debug_message("we have a new packet from the server!");
	
	var t = ds_map_find_value(async_load, "type");
    switch(t) {
        case network_type_data:
			show_debug_message(json_encode(async_load));
		
			var bufferIn = ds_map_find_value(async_load, "buffer");
		    buffer_seek(bufferIn, buffer_seek_start, 0);
			
		    var messageId = buffer_read(bufferIn, buffer_u8);
		    show_debug_message("message id: "+string(messageId));
  
		    switch (messageId) {
				case networkCommands.test:
					var long = GetLongFromBuffer(bufferIn);
					show_debug_message(string(long));
				break;
				
				case networkCommands.receive_my_id:
					var clientID = GetIntFromBuffer(bufferIn);
					mePlayer = instance_create_layer(room_width/2, room_height/2,"instances", obj_player_host);
					with(mePlayer) {
						myId = clientID;
					}
				break;
				
				case networkCommands.send_ping:
					var timeFromServer = GetIntFromBuffer(bufferIn);
					show_debug_message("time from server: "+string(timeFromServer));
					latency = current_time - timeFromServer;
					break;
					
				case networkCommands.client_connect:
					var newClient = GetClientFromBuffer(bufferIn);
					ds_list_add(clients, newClient);
				break;
					
				case networkCommands.get_all_clients:
					var amountClients = GetIntFromBuffer(bufferIn);
					for(var i = 0; i < amountClients; i++) {
						var newClient = GetClientFromBuffer(bufferIn);
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
				
				default:
				break;
			}
    }
}