function GetStringFromBuffer(buffer, stringLength){
	var text = "";
	for(var i = 0; i < stringLength; i++) {
		var charNumber = buffer_read(buffer, buffer_u8);
		var char = chr(charNumber);
		text += char;
	}
	
	return text;
}