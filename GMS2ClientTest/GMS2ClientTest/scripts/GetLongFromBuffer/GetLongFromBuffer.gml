function GetLongFromBuffer(buffer) {
	var scratch = buffer_create(8, buffer_fixed, 1);
	buffer_poke(scratch, 7, buffer_u8, buffer_read(buffer, buffer_u8));
	buffer_poke(scratch, 6, buffer_u8, buffer_read(buffer, buffer_u8));
	buffer_poke(scratch, 5, buffer_u8, buffer_read(buffer, buffer_u8));
	buffer_poke(scratch, 4, buffer_u8, buffer_read(buffer, buffer_u8));
	buffer_poke(scratch, 3, buffer_u8, buffer_read(buffer, buffer_u8));
	buffer_poke(scratch, 2, buffer_u8, buffer_read(buffer, buffer_u8));
	buffer_poke(scratch, 1, buffer_u8, buffer_read(buffer, buffer_u8));
	buffer_poke(scratch, 0, buffer_u8, buffer_read(buffer, buffer_u8));
	var low = buffer_read(scratch, buffer_u32);
	var high = buffer_read(scratch, buffer_s32);
	
	buffer_delete(scratch);
	return (high << 32) | low;
}