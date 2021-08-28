enum PlayerMoveCommands {
	
}

#macro move_horizontal = ((keyboard_check(ord("D"))) - (keyboard_check(ord("A"))))
#macro move_vertical = ((keyboard_check(ord("S"))) - (keyboard_check(ord("W"))))