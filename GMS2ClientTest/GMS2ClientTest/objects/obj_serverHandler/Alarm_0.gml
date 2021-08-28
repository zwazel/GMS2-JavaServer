/// @description
buffer_seek(global.buffer, buffer_seek_start, 0);
buffer_write(global.buffer, buffer_s8, networkCommands.send_ping);
var currentTime = current_time;
show_debug_message("current time: " + string(currentTime));
buffer_write(global.buffer, buffer_s32, currentTime);

network_send_raw(socket, global.buffer, buffer_tell(global.buffer));
alarm[0] = room_speed;