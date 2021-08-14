/// @description
var n_id = ds_map_find_value(async_load, "id");
if n_id == socket {
	show_debug_message("we have a new packet from the server!");
	
	var t = ds_map_find_value(async_load, "type");
	show_debug_message("type = " + string(t));
    switch(t) {
        case network_type_data:
			show_debug_message("DATA!");
            //Data handling here...
			
			var buffer = async_load[? "buffer"];
			buffer_seek(buffer, buffer_seek_start, 0);
			var data = buffer_read(buffer, buffer_u8);
			show_debug_message("data = " + string(data));
			
            break;
    }
}