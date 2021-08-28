/// @description
myId = -1;
username = "undefined";
hp = -1;
mySpeed = -1;
serverHandler = noone;
ready = false;

lastSendDirectionX = 0;
lastSendDirectionY = 0;

function setMyDirection(xDir, yDir) {
	lastSendDirectionX = xDir;
	lastSendDirectionY = yDir;
}