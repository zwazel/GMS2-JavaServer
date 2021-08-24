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
				case 1:
					var timeFromServer = getIntFromBuffer(bufferIn);
					show_debug_message("time from server: "+string(timeFromServer));
					latency = current_time - timeFromServer;
					break;
				
				case 2:
					show_debug_message("buffer position = " + string(buffer_tell(bufferIn)));
					var stringLength = getIntFromBuffer(bufferIn);
					show_debug_message("buffer position = " + string(buffer_tell(bufferIn)));
					show_debug_message("stringLength = " + string(stringLength));
				
					var textFromServer = getStringFromBuffer(bufferIn, stringLength);
					show_debug_message("text from server = " + textFromServer);
				break;
				
				default:
					break;
			}
    }
}