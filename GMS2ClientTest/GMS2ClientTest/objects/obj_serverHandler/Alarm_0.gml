/// @description
buffer_seek(global.buffer, buffer_seek_start, 0);
buffer_write(global.buffer, buffer_u8, 0);
buffer_write(global.buffer, buffer_u32, current_time);
network_send_raw(socket, global.buffer, buffer_tell(global.buffer));
alarm[0] = 30;