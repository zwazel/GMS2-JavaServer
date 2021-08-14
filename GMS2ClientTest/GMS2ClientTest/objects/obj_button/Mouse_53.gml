/// @description
if(ready) {
	var textLength = string_width(text);
	if(point_in_rectangle(mouse_x, mouse_y, x-(textLength/2)-borderXOffsetInactive,y-borderYOffsetInactive,x+(textLength/2)+borderXOffsetInactive, y+borderYOffsetInactive)) {
		if(owner != noone) {
			show_debug_message("PRESSED")
			with(owner) {
				buttonPressed();
			}
		}
	}
}