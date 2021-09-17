/// @description
var dt = delta_time / 1000000;
var distToTarget = point_distance(x,y, targetX, targetY);
var lerpSpeed = mySpeed * distToTarget * dt;
show_debug_message("DELTA = " + string(dt) + ",LERP SPEED = " + string(lerpSpeed) + ",dist to target = " + string(distToTarget));
x = lerp(x, targetX, lerpSpeed);
y = lerp(y, targetY, lerpSpeed);