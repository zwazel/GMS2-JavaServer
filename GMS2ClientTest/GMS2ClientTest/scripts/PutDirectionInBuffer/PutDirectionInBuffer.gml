function PutDirectionInBuffer(buffer, xDir, yDir){
	buffer_write(buffer, buffer_s8, xDir);
	buffer_write(buffer, buffer_s8, yDir);
}