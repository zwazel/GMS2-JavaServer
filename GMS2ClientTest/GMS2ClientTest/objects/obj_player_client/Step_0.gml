/// @description
var dt = delta_time / 1000000;
var distToTarget = point_distance(x,y, targetX, targetY);
var lerpSpeed = mySpeed * dt * distToTarget;
lerpSpeed = clamp(lerpSpeed, 0, 1);
show_debug_message("lerpspeed = " + string(lerpSpeed));
x = lerp(x, targetX, lerpSpeed);
y = lerp(y, targetY, lerpSpeed);