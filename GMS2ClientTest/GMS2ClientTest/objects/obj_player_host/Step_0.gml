/// @description
event_inherited();

if(ready) {
	image_angle = point_direction(x,y,mouse_x,mouse_y);
	
	#region MOVEMENT
	var h move_horizontal;
	var v move_vertical;
	lastSendDirectionX = h;	
	lastSendDirectionY = v;
	
	var spd = sqrt(vx * vx + vy * vy);
	if h == 0 && v == 0 {
	    // deaccelerate when not moving
	    if spd <= nd {
	        vx = 0;
	        vy = 0;
	    } else {
	        vx -= vx / spd * nd;
	        vy -= vy / spd * nd;
	    }
	} else {
	    if vx * h + vy * v < 0 {
	        // skid
	        if spd <= sd {
	            vx = 0;
	            vy = 0;
	        } else {
	            vx -= vx / spd * sd;
	            vy -= vy / spd * sd;
	        }
	    } else {
	        // accelerate
	        vx += h * acc;
	        vy += v * acc;
	        spd = sqrt(vx * vx + vy * vy);
	        if spd > mySpeed {
	            vx = vx / spd * mySpeed;
	            vy = vy / spd * mySpeed;
	        }
	    }
	}
	
	x += vx;
	y += vy;
	#endregion
}