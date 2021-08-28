function GetDirectionFromBuffer(buffer){
	var horizontal = buffer_read(buffer,buffer_u8);
	var vertical = buffer_read(buffer,buffer_u8);
	
	return [horizontal, vertical];
}