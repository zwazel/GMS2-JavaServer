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
			
			var reachedEndOfBuffer = false;
			var bufferSize = buffer_get_size(bufferIn);
			while(buffer_tell(bufferIn) < bufferSize || !reachedEndOfBuffer) {
				show_debug_message("buffer tell = " + string(buffer_tell(bufferIn)));
				show_debug_message("buffer size = " + string(bufferSize));
			    var messageId = buffer_read(bufferIn, buffer_u8);
			    show_debug_message("message id: "+string(messageId));
  
			    switch (messageId) {
					case networkCommands.test:
					break;
				
					case networkCommands.receive_my_id:
						show_debug_message("RECEIVE MY ID");
						mePlayer = InitClientFromBuffer(bufferIn, id, false, PlayerTypes.host);
					break;
				
					case networkCommands.send_ping:
						show_debug_message("SEND PING");
						var timeFromServer = GetIntFromBuffer(bufferIn);
						latency = current_time - timeFromServer;
						mePlayer.ping = latency;
						show_debug_message("mePlayerPing = " + string(mePlayer.ping));
						break;
					
					case networkCommands.client_connect:
						show_debug_message("CLIENT CONNECT");
						var amountClients = GetIntFromBuffer(bufferIn);
						show_debug_message("amount: " + string(amountClients));
						for(var i = 0; i < amountClients; i++) {
							var newClient = InitClientFromBuffer(bufferIn, id);
							ds_list_add(clients, newClient);
						}
					break;
					
					case networkCommands.get_all_clients:
						show_debug_message("GETTING ALL CLIENTS");
						var amountClients = GetIntFromBuffer(bufferIn);
						for(var i = 0; i < amountClients; i++) {
							var newClient = InitClientFromBuffer(bufferIn, id);
							ds_list_add(clients, newClient);
						}
					break;
				
					case networkCommands.client_disconnect:
						show_debug_message("CLIENT DISCONNECT");
						var amountClients = GetIntFromBuffer(bufferIn);
						for(var j = 0; j < amountClients; j++) {
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
						}
					break;
				
					case networkCommands.update_clients:
						show_debug_message("UPDATE CLIENTS");
						var amountClients = GetIntFromBuffer(bufferIn);
						for(var i = 0; i < amountClients; i++) {
							UpdateClientFromBuffer(bufferIn, id, clients, mePlayer);
						}
					break;
				
					case networkCommands.receive_ping_other:
						show_debug_message("RECEIVE PING OTHER");
						var clientID = GetIntFromBuffer(bufferIn);
						var ping = GetIntFromBuffer(bufferIn);
						var client = GetClientFromDsList(clients, clientID);
						if(client != noone) {
							client.ping = ping;
						}
					break;
				
					case networkCommands.end_of_packet:
						reachedEndOfBuffer = true;
						show_debug_message("END OF PACKET");
					break;
				
					default:
					break;
				}
			}
		break;
    }
}