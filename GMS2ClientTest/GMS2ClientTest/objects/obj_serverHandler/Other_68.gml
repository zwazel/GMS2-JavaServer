/// @description


var type = ds_map_read(async_load, "type");

if (type == network_type_data) {
    var buffer = ds_map_find_value(async_load, "buffer");
    var msgId = buffer_read(buffer, buffer_u8);
    switch (msgId) {
        case 1:
            var time = buffer_read(buffer, buffer_u32);
            latency = current_time - time;
        break;
    }
}