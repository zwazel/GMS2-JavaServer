/// @description
textInputs = [];

function addTextInputBox(_posX, _posY, _maxChars = 64, _placeHolder, _layer = "gui", _text = "") {
	var newTextInputInstance = instance_create_layer(_posX, _posY, _layer, obj_TextInputBox);
	newTextInputInstance.textInputManager = id;
	with(newTextInputInstance) {
		max_chars = _maxChars;
		placeholder = _placeHolder;
		text = _text;
		ready = true;
	}
	array_push(textInputs, newTextInputInstance);
	show_debug_message("textInputs = " + string(array_length(textInputs)));
	show_debug_message("new text input instance = " + string(newTextInputInstance))
	return newTextInputInstance.id;
}

function checkIfCanClearString() {
	var myCanClearString = true;
	for(i = 0; i < array_length(textInputs); i++) {
		with(textInputs[i]) {
			if(active) {
				show_debug_message("CAN'T CLEAR STRING SOMETHINGS ACTIVE");
				myCanClearString = false;
				break;
			}
		}
	}
	show_debug_message("clear string = " + string(myCanClearString));
	for(i = 0; i < array_length(textInputs); i++) {
		with(textInputs[i]) {
			canClearString = myCanClearString;
		}
	}
}