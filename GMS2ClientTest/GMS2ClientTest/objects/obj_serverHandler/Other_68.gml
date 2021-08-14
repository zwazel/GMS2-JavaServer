/// @description
var n_id = ds_map_find_value(async_load, "id");
if n_id == socket {
	show_debug_message("we have a new packet from the server!");
	
	var t = ds_map_find_value(async_load, "type");
	show_debug_message("type = " + string(t));
    switch(t) {
        case network_type_data:
			var bufferIn = async_load[? "buffer"];
		    buffer_seek(bufferIn, buffer_seek_start, 0);
		    var messageId = buffer_read(bufferIn, buffer_s32);
		    show_debug_message("message id: "+string(messageId));
  
		    switch (messageId) {
				default:
					break;
			}
    }
}