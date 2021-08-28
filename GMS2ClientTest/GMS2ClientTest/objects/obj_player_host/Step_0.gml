/// @description
event_inherited();

if(ready) {
	var sendDirection = false;
	var moveHorizontal move_horizontal;
	var moveVertical move_vertical;
	
	if(lastSendDirectionX != moveHorizontal) {
		lastSendDirectionX = moveHorizontal;
		
		sendDirection = true;
	}
	
	if(lastSendDirectionY != moveVertical) {
		lastSendDirectionY = moveVertical;
		
		sendDirection = true;
	}
	
	if(sendDirection) {
		serverHandler.sendMoveCommand(moveHorizontal, moveVertical);
	}
}