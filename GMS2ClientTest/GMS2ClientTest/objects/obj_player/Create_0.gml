/// @description
myId = -1;
username = "undefined";
hp = -1;
mySpeed = -1;
serverHandler = noone;
ping = 0;
ready = false;

#region MOVEMENT
lastSendDirectionX = 0;
lastSendDirectionY = 0;
vx = 0; // horizontal velocity
vy = 0; // vertical
acc = 0.2; // acceleration
nd = 0.2; // normal deacceleration
sd = 0.3; // skid deacceleration

function setMyDirection(xDir, yDir, posX, posY) {
	x = posX;
	y = posY;
	lastSendDirectionX = xDir;
	lastSendDirectionY = yDir;
}
#endregion