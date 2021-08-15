function handlePacket(pack) {
	//trace(buffer_base64_encode(pack, 0, buffer_get_size(pack)))
	
	var data = snap_from_messagepack(pack)	// Deserialize/unpack msgpack into a struct
	var cmd = string_lower(data.cmd) // you can get rid of this line, 
									 // i just like the commands being lowercase
	
	//trace("Received cmd: %", cmd)
	
	switch(cmd) {
		case "hello":
			trace(data.str)
			break
	}
}