function getStringFromBuffer(buffer, stringLength){
	var text = "";
	for(var i = 0; i < stringLength; i++) {
		show_debug_message("i = " + string(i));
		var charNumber = buffer_read(buffer, buffer_u8);
		var char = chr(charNumber);
		show_debug_message("char = " + char);
		text += char;
	}
	
	return text;
}