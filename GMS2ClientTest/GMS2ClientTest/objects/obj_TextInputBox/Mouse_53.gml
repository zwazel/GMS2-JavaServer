/// @description
if(ready) {
	active = point_in_rectangle(mouse_x, mouse_y, x-(textLength/2)-borderXOffsetActive,y-borderYOffsetActive,x+(textLength/2)+borderXOffsetActive, y+borderYOffsetActive);
	with(textInputManager) {
		checkIfCanClearString();
	}
}