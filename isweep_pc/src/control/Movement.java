package control;

import lejos.nxt.Motor;

public class Movement {
	
	double convert = 512.0/180.0;
	double mm_to_wheeldegress_constant = 2.0845396641574985524030110017371;

	void move(int millimeter){
		int degressToTurnWheel = (int)(millimeter*mm_to_wheeldegress_constant);
		Motor.A.rotate(degressToTurnWheel,true); 
		Motor.B.rotate(degressToTurnWheel,false);
	}
	
	void armCollect(){
		Motor.C.setSpeed(200);
		Motor.C.rotateTo(105);
	}
	
	void armHold(){
		Motor.C.setSpeed(30);
		Motor.C.rotateTo(70);
	}
	
	void armThrow(){
		Motor.C.setSpeed(100);
		Motor.C.rotateTo(0);
	
	}	

	void turnDirection(int angle){
		Motor.A.rotate((int)(angle*convert),true);
		Motor.B.rotate((int)(-angle*convert),false);
	}
}
