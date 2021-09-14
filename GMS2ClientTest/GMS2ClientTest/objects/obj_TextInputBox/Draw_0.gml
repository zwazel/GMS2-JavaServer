/// @description Draw the text
if(ready) {
	draw_set_color(c_white);
	if(text == "") {
		textLength = string_width(placeholder);
	} else if(string_width(text) >= textLength) {
		textLength = string_width(text);	
	}
	if(active) {
		draw_rectangle(x-(textLength/2)-borderXOffsetActive,y-borderYOffsetActive,x+(textLength/2)+borderXOffsetActive, y+borderYOffsetActive, true);
	}
	draw_rectangle(x-(textLength/2)-borderXOffsetInactive,y-borderYOffsetInactive,x+(textLength/2)+borderXOffsetInactive, y+borderYOffsetInactive, true);
	draw_set_halign(fa_center);
	draw_set_valign(fa_middle);
	
	if(text == "") {
		draw_set_alpha(0.5);
		draw_text(x,y,placeholder);
	}
	draw_set_alpha(1);
	draw_text(x,y,text + cursor);
}