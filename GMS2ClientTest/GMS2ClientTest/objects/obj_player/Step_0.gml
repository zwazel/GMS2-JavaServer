/// @description
if(canShoot && subState == PLAYER_SUB_STATES.SHOOTING) {
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