/// @description
var n_id = ds_map_find_value(async_load, "id");
if n_id == socket {
	show_debug_message("we have a new packet from the server!");
	
	var t = ds_map_find_value(async_load, "type");
    switch(t) {
        case network_type_data:
			var bufferIn = ds_map_find_value(async_load, "buffer");
			show_debug_message("buffer size = " + string(ds_map_find_value(async_load, "size")));
		    buffer_seek(bufferIn, buffer_seek_start, 0);
			
		    var messageId = buffer_read(bufferIn, buffer_u8);
		    show_debug_message("message id: "+string(messageId));
			
			show_debug_message("buffer tell 1 = " + string(buffer_tell(bufferIn)));
			
			var ch1 = buffer_read(bufferIn, buffer_u8);
			var ch2 = buffer_read(bufferIn, buffer_u8);
			var ch3 = buffer_read(bufferIn, buffer_u8);
			var ch4 = buffer_read(bufferIn, buffer_u8);
			
			var timeFromServer = ReadIntFromServer(ch1, ch2, ch3, ch4);
			
			show_debug_message("time from server: "+string(timeFromServer));
			
			show_debug_message("buffer tell 2 = " + string(buffer_tell(bufferIn)));
  
		    switch (messageId) {
				default:
					break;
			}
    }
}