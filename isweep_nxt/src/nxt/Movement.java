package nxt;


import lejos.nxt.Motor;

public class Movement {
	
	
	double convert = 512.0/180.0;

	void move(int degress){
		Motor.A.rotate(600,true); 
		Motor.B.rotate(600,false);
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
	



	
	

