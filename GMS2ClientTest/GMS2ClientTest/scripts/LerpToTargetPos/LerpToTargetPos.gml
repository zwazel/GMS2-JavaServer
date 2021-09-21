function LerpToTargetPos(){
	var dt = delta_time / 1000000;
	var distToTarget = point_distance(x,y, lastTargetX, lastTargetY);
	var lerpSpeed = mySpeed * (distToTarget) * dt;
	lerpSpeed = clamp(lerpSpeed, 0, 1);
	#region LERP TO TARGET POS
	if(x != lastTargetX) {
		x = lerp(x, lastTargetX, lerpSpeed);
	}
	if(y != lastTargetY) {
		y = lerp(y, lastTargetY, lerpSpeed);
	}
	#endregion
	#region SNAP TO TARGET POS
	if(x >= lastTargetX - lerpSpeedStopOffset && x <= lastTargetX + lerpSpeedStopOffset) {
		x = lastTargetX;
	}
	if(y >= lastTargetY - lerpSpeedStopOffset && y <= lastTargetY + lerpSpeedStopOffset) {
		y = lastTargetY;
	}
	#endregion
}