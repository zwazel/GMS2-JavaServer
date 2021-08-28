function PutPositionInBuffer(buffer, _x,_y){
	buffer_write(buffer, buffer_f64, _x);
	buffer_write(buffer, buffer_f64, _y);
}