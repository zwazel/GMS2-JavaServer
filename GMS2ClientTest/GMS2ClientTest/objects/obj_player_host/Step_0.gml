/// @description
event_inherited();

if(ready) {
	var moveHorizontal move_horizontal;
	var moveVertical move_vertical;
	
	if(lastSendDirectionX != moveHorizontal) {
		lastSendDirectionX = moveHorizontal;
	}
	
	if(lastSendDirectionY != moveVertical) {
		lastSendDirectionY = moveVertical;
	}
	
	image_angle = point_direction(x,y,mouse_x,mouse_y);
}