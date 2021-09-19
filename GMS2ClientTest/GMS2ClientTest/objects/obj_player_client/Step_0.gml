/// @description
if(x != targetX || y != targetY) {
	var dt = delta_time / 1000000;
	var distToTarget = point_distance(x,y, targetX, targetY);
	var lerpSpeed = mySpeed * (distToTarget / 10) * dt;
	lerpSpeed = clamp(lerpSpeed, 0, 1);
	show_debug_message("lerpspeed = " + string(lerpSpeed));
	if(x != targetX) {
		x = lerp(x, targetX, lerpSpeed);
	}
	if(y != targetY) {
		y = lerp(y, targetY, lerpSpeed);
	}
}
if(x >= targetX - lerpSpeedStopOffset && x <= targetX + lerpSpeedStopOffset) {
	x = targetX;
}
if(y >= targetY - lerpSpeedStopOffset && y <= targetY + lerpSpeedStopOffset) {
	y = targetY;
}