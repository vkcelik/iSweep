package control;

import lejos.nxt.Motor;

public class Movement {
	
	double convert = 512.0/180.0;
	double mm_to_wheeldegress_constant = 2.0845396641574985524030110017371;
	double pixelPerMm;
	double mmPerPixel;

	public void move(int millimeter){
		int degressToTurnWheel = (int)(millimeter*mm_to_wheeldegress_constant);
		Motor.A.rotate(degressToTurnWheel,true); 
		Motor.B.rotate(degressToTurnWheel,true);
	}
	
	public void armCollect(){
		Motor.C.setSpeed(200);
		Motor.C.rotateTo(105);
	}
	
	public void armHold(){
		Motor.C.setSpeed(30);
		Motor.C.rotateTo(70);
	}
	
	public void armThrow(){
		Motor.C.setSpeed(100);
		Motor.C.rotateTo(0);
	}	

	public void turnLeft(int angle){
		Motor.A.rotate((int)(-angle*convert),true);
		Motor.B.rotate((int)(angle*convert),false);
	}
	
	public void turnRight(int angle){
		Motor.A.rotate((int)(angle*convert),true);
		Motor.B.rotate((int)(-angle*convert),false);
	}
	
	private int pixelToMm(int pixel, int sideLengthPixel){
		int L_side_PX=sideLengthPixel, L_side_MM=1000; //1200
		float mmperpixel = (float)L_side_MM/L_side_PX;
		return (int) (pixel*mmperpixel);
	}
	
	private int mmToPixel(int mm, int sideLengthPixel){
		int L_side_PX=sideLengthPixel, L_side_MM=1000; //1200
		float pixelpermm = (float)L_side_PX/L_side_MM;
		return (int) (mm*pixelpermm);
	}
	
	private int pixelToMm(int pixel){
		return (int) (pixel*mmPerPixel);
	}
	
	private int mmToPixel(int mm){
		return (int) (mm*pixelPerMm);
	}
	
	public void stop(){
		Motor.A.stop(true);
		Motor.B.stop(false);
	}
	
	public Movement(double mmPerPixel, double pixelPerMm) { 
		this.mmPerPixel = mmPerPixel;
		this.pixelPerMm = pixelPerMm;
	}
	
	public Movement(){
		
	}
}
