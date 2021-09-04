/// @description
var buffer = buffer_create(1024, buffer_fixed, 1);
buffer_write(buffer, buffer_s8, networkCommands.send_ping);
var currentTime = current_time;
show_debug_message("current time: " + string(currentTime));
buffer_write(buffer, buffer_s32, currentTime);

network_send_raw(socket, buffer, buffer_tell(buffer));
sentPackages++;
buffer_delete(buffer);
alarm[0] = room_speed;