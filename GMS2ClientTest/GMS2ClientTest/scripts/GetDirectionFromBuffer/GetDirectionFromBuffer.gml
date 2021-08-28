function GetDirectionFromBuffer(buffer){
	var horizontal = buffer_read(buffer,buffer_s8);
	var vertical = buffer_read(buffer,buffer_s8);
	
	return [horizontal, vertical];
}