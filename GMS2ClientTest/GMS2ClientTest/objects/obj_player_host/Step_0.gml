/// @description
event_inherited();

if(ready) {
	image_angle = point_direction(x,y,mouse_x,mouse_y);
	
	#region MOVEMENT
	var h move_horizontal;
	var v move_vertical;
	lastSendDirectionX = h;	
	lastSendDirectionY = v;
	calculateMovement(h,v);
	#endregion
}