function ReadIntFromServer(ch1, ch2, ch3, ch4){
	if ((ch1 | ch2 | ch3 | ch4) < 0) {
		return -1;
	}
	
	return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
}