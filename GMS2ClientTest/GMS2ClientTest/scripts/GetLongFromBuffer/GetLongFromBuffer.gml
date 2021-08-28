function GetLongFromBuffer(buffer) {
	var bytes = ReadLongAsByteArray(buffer);
	return OrderBytesToLong(bytes);
}

function ReadLongAsByteArray(buffer) {
	var ch1 = buffer_read(buffer, buffer_u8);
	var ch2 = buffer_read(buffer, buffer_u8);
	var ch3 = buffer_read(buffer, buffer_u8);
	var ch4 = buffer_read(buffer, buffer_u8);
	var ch5 = buffer_read(buffer, buffer_u8);
	var ch6 = buffer_read(buffer, buffer_u8);
	var ch7 = buffer_read(buffer, buffer_u8);
	var ch8 = buffer_read(buffer, buffer_u8);
	
	return [ch1, ch2, ch3, ch4, ch5, ch6, ch7, ch8];
}

function OrderBytesToLong(readBuffer) {
	return ((readBuffer[0] << 56) +
            ((readBuffer[1] & 255) << 48) +
            ((readBuffer[2] & 255) << 40) +
            ((readBuffer[3] & 255) << 32) +
            ((readBuffer[4] & 255) << 24) +
            ((readBuffer[5] & 255) << 16) +
            ((readBuffer[6] & 255) <<  8) +
            ((readBuffer[7] & 255) <<  0));
}