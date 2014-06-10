package main;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import control.Direction;
import control.Movement;
import boldogrobot.Ball;
import boldogrobot.Placeable;
import boldogrobot.Robot;
import vision.ImageAnalyzer;

public class Main {
	
	static Converter convert;
	static Movement m;
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		VideoCapture vc = new VideoCapture(0);
		if(vc.isOpened()){
			System.out.println("Cam opened");
		} else {
			System.out.println("Cam not opened");
		}

		Mat frame = new Mat();

		vc.read(frame);
		System.out.println(vc.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1920.0));
		System.out.println(vc.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 1080.0));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		while(vc.read(frame)){
//			
//		}
		
		// TAKE PICTURE
		// Find koordinaterne p� hvor v�gene m�des (inderside).
		// Find l�ngderne p� v�gene i pixels 
		// For hver v�g: divider l�ngden i pixels med hvor langt de er i virkeligheden og omvendt
		// Find gennemsnittet af de 2 tals�t
		
		
		Ball ball = new Ball(2100, 100);
		Robot robot = new Robot(100, 100);
		robot.setBack(new Placeable(0, 100));
		robot.updateDirection();
		
		Direction robotBallVector = new Direction(ball.getX() - robot.getX(), ball.getY() - robot.getY());
		
		int robote0 = robot.getDirection().getElement(0);
		int robote1 = robot.getDirection().getElement(1);
		int toBalle0 = robotBallVector.getElement(0);
		int toBalle1 = robotBallVector.getElement(1);
		
		double vinkel = ( robote0* toBalle0 + robote1 * toBalle1)
				/ ((Math.sqrt(Math.pow(robote0, 2) + (Math.pow(robote1, 2))) * (Math
						.sqrt(Math.pow(toBalle0, 2) + (Math.pow(toBalle1, 2))))));

		double vinkel_grader = Math.toDegrees(Math.acos(vinkel));
		
		m = new Movement();
		convert = new Converter(0.4,2.5);

//		if(robot.getX()>ball.getX()){
//			m.turnLeft((int)vinkel_grader);			
//		} else {
//			m.turnRight((int)vinkel_grader);
//		}

		System.out.println(vinkel_grader);

		// TAKE PICTURE
		// Robot front og bagende(m�ske) har nu �ndret position fordi vi har drejet
		
		robot.setX(100);
		robot.setY(100);
		robot.setBack(new Placeable(0, 100));
		robot.updateDirection();
		
		
		int distance_mm = convert.pixelToMm((int) robot.getDistance(ball), 1000);
		System.out.println(distance_mm);
		followLine(distance_mm);
		
		m.armCollect();
		m.armHold();
		
		//		ImageAnalyzer ia = new ImageAnalyzer();
		//		ia.setImage(frame);
		//		ia.getBalls();
	}
	
	
	static void followLine(int distance_mm){
		boolean haveArrived = false;
		while (!haveArrived){
			m.move(distance_mm);
			while (true){
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// TAG ET BILLEDE
				// FIND ROBOTTENS FRONT OG BAGENDE
				// HVIS FRONTEN AFVIGER MERE END 10 MM
					// STOP
					// DREJ ROBOTTEN MOD M�LET
					// MOVE TILBAGEV�RENDE DISTANCE 
				int afvigelse = 0;
				if (afvigelse > 10){
					m.stop();
					
					break;
				} else /*DISTANCE TIL M�L ER MINDRE END 2 MM*/{
					haveArrived = true;
				}
			}
		}		
	}
}
