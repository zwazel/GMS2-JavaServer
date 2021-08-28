function GetFloatFromBuffer(buffer){
	var scratch = buffer_create(8, buffer_fixed, 1);
	buffer_poke(scratch, 3, buffer_u8, buffer_read(buffer, buffer_u8));
	buffer_poke(scratch, 2, buffer_u8, buffer_read(buffer, buffer_u8));
	buffer_poke(scratch, 1, buffer_u8, buffer_read(buffer, buffer_u8));
	buffer_poke(scratch, 0, buffer_u8, buffer_read(buffer, buffer_u8));
	var value = buffer_read(scratch, buffer_f32);
	buffer_delete(scratch);
	return value;
}