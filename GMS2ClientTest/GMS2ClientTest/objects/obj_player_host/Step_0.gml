/// @description
if(ready) {
	image_angle = point_direction(x,y,mouse_x,mouse_y);
	
	#region MOVEMENT
	var h move_horizontal;
	var v move_vertical;
	var isSprinting sprint_button;
	var isShooting shoot_button;

	lastSendDirectionX = h;	
	lastSendDirectionY = v;
	
	if((h == 0 && v == 0) && (vx == 0 && vy == 0)) {
		mainState = PLAYER_MAIN_STATES.IDLE;
	} else {
		mainState = (isSprinting) ? PLAYER_MAIN_STATES.RUNNING : PLAYER_MAIN_STATES.WALKING;
	}
	
	calculateMovement(h,v);
	
	if(isShooting) {
		subState = PLAYER_SUB_STATES.SHOOTING;
		
		if(canShoot) {
			var bullet = instance_create_layer(x,y,"instances",obj_bullet);
			ownedBullets++;
			with(bullet) {
				owner = other.id;
				direction = other.image_angle;
				image_angle = other.image_angle;
			}
		
			canShoot = false;
			alarm[0] = room_speed / shootingCooldown;
		}
	} else {
		subState = PLAYER_SUB_STATES.NOTHING;
	}
	#endregion
}