/// @description
if(ready) {
	draw_self();
	draw_text(x,y-32,username);
	draw_text(x,y+32, "server id="+string(myId) + ", gm id=" + string(id));
	draw_text(x,y+48, "speed="+string(mySpeed));
	draw_text(x,y+64, "health="+string(hp));
	draw_text(x,y+80, "posX="+string(x) + ", posY=" + string(y));
	draw_text(x,y+96, "mainState="+string(mainState) + ", subState=" + string(subState));
}