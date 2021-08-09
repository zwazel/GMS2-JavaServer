/// @description
if(ready) {
	var textLength = string_width(text);
	draw_set_color(c_white);
	draw_rectangle(x-(textLength/2)-borderXOffsetInactive,y-borderYOffsetInactive,x+(textLength/2)+borderXOffsetInactive, y+borderYOffsetInactive, true);
	draw_set_halign(fa_center);
	draw_set_valign(fa_middle);
	draw_text(x,y,text);
}