/// @description
myId = -1;
username = "undefined";
hp = -1;
serverHandler = noone;
ping = 0;
ready = false;
mainState = PLAYER_MAIN_STATES.IDLE;
subState = PLAYER_SUB_STATES.NOTHING;

#region MOVEMENT
lastSendDirectionX = 0;
lastSendDirectionY = 0;
mySpeed = -1;
mySprintSpeed = -1;
vx = 0; // horizontal velocity
vy = 0; // vertical
acc = 0; // acceleration
nd = 0; // normal deceleration
sd = 0; // skid deceleration

function setMyDirection(xDir, yDir, posX, posY) {
	x = posX;
	y = posY;
	lastSendDirectionX = xDir;
	lastSendDirectionY = yDir;
}

function calculateMovement(h,v) {
	var isSprinting = (mainState == PLAYER_MAIN_STATES.RUNNING);
	calculateMovementWithDefiningSprinting(h,v,isSprinting);
}

function calculateMovementWithDefiningSprinting(h,v,isSprinting) {
	var maxSpeed = (isSprinting) ? mySprintSpeed : mySpeed;
	var spd = sqrt(vx * vx + vy * vy);
	if (h == 0 && v == 0) {
		// deaccelerate when not moving
		if spd <= nd {
			vx = 0;
			vy = 0;
		} else {
			vx -= vx / spd * nd;
			vy -= vy / spd * nd;
		}
	} else {
		if (vx * h + vy * v < 0) {
			// skid
			if spd <= sd {
			    vx = 0;
			    vy = 0;
			} else {
			    vx -= vx / spd * sd;
			    vy -= vy / spd * sd;
			}
		} else {
			// accelerate
			vx += h * acc;
			vy += v * acc;
			spd = sqrt(vx * vx + vy * vy);
			if (spd > maxSpeed) {
			    vx = vx / spd * maxSpeed;
			    vy = vy / spd * maxSpeed;
			}
		}
	}
	
	x += vx;
	y += vy;
}
#endregion