/// @description Draw the text
draw_set_color(c_white);
if(active) {
	draw_rectangle(x-(string_width(text)/2)-borderXOffsetActive,y-borderYOffsetActive,x+(string_width(text)/2)+borderXOffsetActive, y+borderYOffsetActive, true);
}
draw_rectangle(x-(string_width(text)/2)-borderXOffsetInactive,y-borderYOffsetInactive,x+(string_width(text)/2)+borderXOffsetInactive, y+borderYOffsetInactive, true);
draw_set_halign(fa_center);
draw_set_valign(fa_middle);
draw_text(x,y,text);