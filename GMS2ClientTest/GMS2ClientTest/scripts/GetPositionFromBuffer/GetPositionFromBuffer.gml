function GetPositionFromBuffer(buffer){
	var clientX = GetDoubleFromBuffer(buffer);
	var clientY = GetDoubleFromBuffer(buffer);
	
	return [clientX, clientY];
}