/// @description

// Inherit the parent event
event_inherited();

if(ready) {
	draw_text(x,y+112, "lastTargetX="+string(lastTargetX) + ", lastTargetY=" + string(lastTargetY));
	draw_text(x,y+128, "targetX="+string(targetX) + ", targetY=" + string(targetY));
}