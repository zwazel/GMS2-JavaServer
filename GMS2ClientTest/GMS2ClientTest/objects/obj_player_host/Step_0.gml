/// @description
if(ready) {
	image_angle = point_direction(x,y,mouse_x,mouse_y);
	
	#region MOVEMENT
	var h move_horizontal;
	var v move_vertical;
	var isSprinting sprint_button;

	lastSendDirectionX = h;	
	lastSendDirectionY = v;
	
	if((h == 0 && v == 0) && (vx == 0 && vy == 0)) {
		mainState = PLAYER_MAIN_STATES.IDLE;
	} else {
		mainState = (isSprinting) ? PLAYER_MAIN_STATES.RUNNING : PLAYER_MAIN_STATES.WALKING;
	}
	
	calculateMovement(h,v);
	#endregion
}