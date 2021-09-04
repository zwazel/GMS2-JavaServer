/// @description
buffer_write(myBuffer, buffer_s8, networkCommands.send_ping);
var currentTime = current_time;
show_debug_message("current time: " + string(currentTime));
buffer_write(myBuffer, buffer_s32, currentTime);

sendMoveCommand(mePlayer.lastSendDirectionX, mePlayer.lastSendDirectionY, mePlayer.x, mePlayer.y);

buffer_write(myBuffer, buffer_s8, networkCommands.end_of_packet);

var bufferWithLengthInformation = buffer_create(64, buffer_fixed, 1);
buffer_write(bufferWithLengthInformation, buffer_s32, buffer_get_size(myBuffer));

show_debug_message(string(buffer_get_size(myBuffer)));

network_send_raw(socket, bufferWithLengthInformation, buffer_tell(bufferWithLengthInformation));
buffer_delete(bufferWithLengthInformation);
network_send_raw(socket, myBuffer, buffer_tell(myBuffer));

buffer_seek(myBuffer,buffer_seek_start,0);

sentPackages+=2;

alarm[0] = room_speed;