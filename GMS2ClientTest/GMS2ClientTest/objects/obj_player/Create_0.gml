/// @description
myId = -1;
username = "undefined";
hp = -1;
mySpeed = -1;
serverHandler = noone;
ping = 0;
ready = false;

lastSendDirectionX = 0;
lastSendDirectionY = 0;

function setMyDirection(xDir, yDir, posX, posY) {
	x = posX;
	y = posY;
	lastSendDirectionX = xDir;
	lastSendDirectionY = yDir;
}