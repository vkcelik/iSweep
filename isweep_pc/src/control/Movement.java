package control;

import lejos.nxt.Motor;

public class Movement {
	
	double convert = 512.0/180.0;
	double mm_to_wheeldegress_constant = 2.0845396641574985524030110017371;
	double pixelPerMm;
	double mmPerPixel;

	public void move(double millimeter){
		int degressToTurnWheel = (int)(millimeter*mm_to_wheeldegress_constant);
		Motor.A.rotate(degressToTurnWheel,true); 
		Motor.B.rotate(degressToTurnWheel,true);
	}
	
	public void moveBlocking(double millimeter){
		int degressToTurnWheel = (int)(millimeter*mm_to_wheeldegress_constant);
		Motor.A.rotate(degressToTurnWheel,true); 
		Motor.B.rotate(degressToTurnWheel,false);
	}
	
	public void armCollect(){
		Motor.C.setSpeed(400);
		Motor.C.rotateTo(100, false);
	}
	
	public void armHold(){
		Motor.C.setSpeed(30);
		Motor.C.rotateTo(50);
	}
	
	public void armThrow(){
		Motor.C.setSpeed(100);
		Motor.C.rotateTo(0, false);
	}	

//	public void turnLeft(double angle){
//		Motor.A.rotate((int)(-angle*convert),true);
//		Motor.B.rotate((int)(angle*convert),false);
//	}
	
	public void turn(double angle){
		Motor.A.rotate((int)(angle*convert),true);
		Motor.B.rotate((int)(-angle*convert),false);
	}
	
	private double pixelToMm(double pixel, double sideLengthPixel){
		double L_side_PX=sideLengthPixel, L_side_MM=1200; //1200
		double mmperpixel = L_side_MM/L_side_PX;
		return pixel*mmperpixel;
	}
	
	private double mmToPixel(double mm, double sideLengthPixel){
		double L_side_PX=sideLengthPixel, L_side_MM=1200; //1200
		double pixelpermm = L_side_PX/L_side_MM;
		return mm*pixelpermm;
	}
	
	private double pixelToMm(double pixel){
		return pixel*mmPerPixel;
	}
	
	private double mmToPixel(double mm){
		return mm*pixelPerMm;
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
