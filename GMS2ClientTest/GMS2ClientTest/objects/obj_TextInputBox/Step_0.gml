/// @description Handle keys presses/input
if(active) {
	if(keyboard_check(vk_anykey) and string_length(text) < max_chars) { // check if any key has been pressed and if our length isnt the max
		text = text+string(keyboard_string);
		keyboard_string = "";
	}

	if(keyboard_check(vk_backspace) and !keyboard_check_pressed(vk_backspace) and delete_timer = delete_timer_max) {
		text = string_delete(text, string_length(text), 1);
		delete_timer = 0;
		keyboard_string = "";
	}

	if(keyboard_check_pressed(vk_backspace)) {
		text = string_delete(text, string_length(text), 1);
		keyboard_string = "";
		delete_timer = -4;
	}

	// handle timer update
	if(delete_timer != delete_timer_max) {
		delete_timer++;
	}
} else {
	keyboard_string = "";
}