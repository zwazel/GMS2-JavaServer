function getIntFromBuffer(buffer) {
	var bytes = readIntAsByteArray(buffer);
	return orderBytesToInt(bytes[0], bytes[1], bytes[2], bytes[3]);
}

function readIntAsByteArray(buffer) {
	var ch1 = buffer_read(buffer, buffer_u8);
	var ch2 = buffer_read(buffer, buffer_u8);
	var ch3 = buffer_read(buffer, buffer_u8);
	var ch4 = buffer_read(buffer, buffer_u8);
	
	return [ch1, ch2, ch3, ch4];
}

function orderBytesToInt(ch1, ch2, ch3, ch4){
	if ((ch1 | ch2 | ch3 | ch4) < 0) {
		return -1;
	}
	
	return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
}