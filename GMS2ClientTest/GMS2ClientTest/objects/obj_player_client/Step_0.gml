/// @description
event_inherited();

if(!reachedLastTarget) {
	if(x==targetX && y==targetY) {
		reachedLastTarget = true;
	} else {
		if(distance_to_point(targetX, targetY) > lerpSpeedStopOffset) {
			x = targetX;
			y = targetY;
		} else {
			x = lerp(x,targetX,0.9);
			y = lerp(y,targetY,0.9);
		}
	}
}else {
	calculateMovement(lastSendDirectionX, lastSendDirectionY);
}