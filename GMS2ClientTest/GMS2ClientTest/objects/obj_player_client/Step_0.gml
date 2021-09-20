/// @description
if(newCoords) {
	if(x != targetX || y != targetY) {
		var dt = delta_time / 1000000;
		var distToTarget = point_distance(x,y, targetX, targetY);
		var lerpSpeed = mySpeed * (distToTarget / 10) * dt;
		lerpSpeed = clamp(lerpSpeed, 0, 1);
		#region LERP TO TARGET POS
		if(x != targetX) {
			x = lerp(x, targetX, lerpSpeed);
		}
		if(y != targetY) {
			y = lerp(y, targetY, lerpSpeed);
		}
		#endregion
		#region SNAP TO TARGET POS
		if(x >= targetX - lerpSpeedStopOffset && x <= targetX + lerpSpeedStopOffset) {
			x = targetX;
		}
		if(y >= targetY - lerpSpeedStopOffset && y <= targetY + lerpSpeedStopOffset) {
			y = targetY;
		}
		#endregion
	} else {
		newCoords = false;
	}
} else {
	// Keep moving in the direction to "guess" where the player goes to (if for example we lost connection or something)
	x += lastSendDirectionX * mySpeed;
	y += lastSendDirectionY * mySpeed;
}