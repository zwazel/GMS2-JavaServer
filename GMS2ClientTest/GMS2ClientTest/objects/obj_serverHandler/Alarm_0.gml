/// @description
buffer_seek(global.buffer, buffer_seek_start, 0);
buffer_write(global.buffer, buffer_s8, 0);
var currentTime = current_time;
show_debug_message("current time: " + string(currentTime));
buffer_write(global.buffer, buffer_s32, currentTime);

buffer_write(global.buffer, buffer_f32, 23.5)

network_send_raw(socket, global.buffer, buffer_tell(global.buffer));
alarm[0] = room_speed;