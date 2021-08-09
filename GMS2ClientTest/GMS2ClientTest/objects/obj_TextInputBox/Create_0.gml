/// @description init variables
delete_timer_max = 2;
delete_timer = 0;
text = "";
placeholder = "";
max_chars = 64;
active = false;
borderXOffsetInactive = 8;
borderYOffsetInactive = 16;
borderXOffsetActive = 12;
borderYOffsetActive = 20;
ready = false;
textLength = 0;
textInputManager = noone;
canClearString = false;

function getText() {
	show_debug_message("returning text = " + text);
	return text;
}