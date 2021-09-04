/// @description
if(mePlayer != noone) {
	var posX = room_width / 2;
	var posY = 16;
	draw_text(posX, posY, "Ping{" + string(latency)+"},Pos{X=" + string(mePlayer.x) + ",Y=" + string(mePlayer.y) + "},Sent/Received{" + string(sentPackages) + "/" + string(receivedPackages) + "}");
	for(var i = 0; i < ds_list_size(clients); i++) {
		var currentClient = ds_list_find_value(clients, i);
		draw_text(posX,48 + (posY*i), currentClient.username + ":{Ping: " + string(currentClient.ping) + ",Pos:{X=" + string(currentClient.x) + ",Y=" + string(currentClient.y) + "}}"); 	
	}
}