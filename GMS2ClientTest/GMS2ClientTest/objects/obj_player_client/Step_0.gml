/// @description
if(!reachedLastTarget) {
	var dt = delta_time / 1000000;
	var distToTarget = distance_to_point(lastTargetX, lastTargetY);
	var lerpSpeed = (room_speed * dt) + (distToTarget / 100);
	x = lerp(x, lastTargetX, lerpSpeed);
	y = lerp(y, lastTargetY, lerpSpeed);
	
	#region SNAP TO TARGET POS
	if(x >= lastTargetX - lerpSpeedStopOffset && x <= lastTargetX + lerpSpeedStopOffset) {
		x = lastTargetX;
	}
	if(y >= lastTargetY - lerpSpeedStopOffset && y <= lastTargetY + lerpSpeedStopOffset) {
		y = lastTargetY;
	}
	#endregion
	
	if(x == lastTargetX && y == lastTargetY) {
		reachedLastTarget = true;
	}
}
if(reachedLastTarget) {
	calculateMovement(lastSendDirectionX, lastSendDirectionY);
}