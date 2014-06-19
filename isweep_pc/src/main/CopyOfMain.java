package main;

import java.util.ArrayList;
import java.util.List;

import navigation.PathFinder;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import control.Direction;
import control.Movement;
import boldogrobot.Ball;
import boldogrobot.Placeable;
import boldogrobot.Robot;
import vision.FrontBack;
import vision.RobotFinder;
import vision.WallFinder4;
import vision.CircleFinder;

public class CopyOfMain {
	
	static Converter convert;
	
	static Direction robotBallVector;
	static double robote0;
	static double robote1;
	static double toBalle0;
	static double toBalle1;
	
	static double vinkel;
	static double vinkel_grader;
	
	static double distance_mm;
	static double initial_distance_mm;
	
	static double sumpxpermm;
	static double summmperpx;
	
	static List<Placeable> corners = null;
	static Mat frame;
	
	static double[] pxpermm = new double[4];
	static double[] mmperpx = new double[4];
	
	static double avgPxPerMm;
	static double avgMmPerPx;
	
	static RobotFinder rf;
	static PathFinder pf;
	static WallFinder4 wf;
	static CircleFinder cf;
	
	static List<Placeable> robotpunkter = null;
	static List<Placeable> balls = null;
	
	static VideoCapture vc;
	
	static FrontBack fb = null;
	
	static List<Placeable> robotMiddle = null;
	
	static Robot robot = new Robot();
	static ArrayList<Placeable> objects = null;
	
	static List<Placeable> rute = null;
	
	static Movement m = new Movement();
	static Placeable ball = null;
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		vc =  new VideoCapture(0);
		if(vc.isOpened()){
			System.out.println("Cam opened");
		} else {
			System.out.println("Cam not opened");
		}
//		m.move(2000);
		
//		m.armCollect();
//		m.armHold();
		
		rf = new RobotFinder();
		rf.setVc(vc);
		wf = new WallFinder4();
		wf.setVc(vc);
		cf = new CircleFinder();
		cf.setVc(vc);
		pf = new PathFinder();
		frame = new Mat();

		vc.read(frame);
		System.out.println("frame: "+frame);
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
		
//		vc.read(frame);
//		System.out.println("frame: "+frame);
//		Highgui.imwrite("555.jpg", frame);
//		System.out.println(frame.rows());
//		System.out.println(frame.cols());
		

		
		try {
			corners = wf.run("55");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (corners == null) {
			System.out.println("null");
		}
		System.out.println(corners.size());
		for (Placeable p: corners){
			System.out.println(p);
		}
		
		// TOP
		pxpermm[0]=corners.get(0).getDistance(corners.get(1))/1800;
		mmperpx[0]=1800/corners.get(0).getDistance(corners.get(1));
		// LEFT
		pxpermm[1]=corners.get(0).getDistance(corners.get(2))/1200;
		mmperpx[1]=1200/corners.get(0).getDistance(corners.get(2));
		// RIGHT
		pxpermm[2]=corners.get(1).getDistance(corners.get(3))/1200;
		mmperpx[2]=1200/corners.get(1).getDistance(corners.get(3));
		// BOTTOM
		pxpermm[3]=corners.get(2).getDistance(corners.get(3))/1800;
		mmperpx[3]=1800/corners.get(2).getDistance(corners.get(3));
			
		sumpxpermm=0;
		summmperpx=0;
		for (int i = 0; i<4;i++){
			summmperpx+=mmperpx[i];
			sumpxpermm+=pxpermm[i];
		}
		
		avgPxPerMm = sumpxpermm/4;
		avgMmPerPx = summmperpx/4;
		
//		avgPxPerMm = 0.83745;
//		avgMmPerPx =  1.40295;
//		avgMmPerPx =  1.293;
		
		convert = new Converter(avgMmPerPx, avgPxPerMm);
		
		System.out.println(avgMmPerPx);
		System.out.println(avgPxPerMm);
		
		findAndUpdateRobot();
		
		try {
			balls = cf.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(balls.size());
		
		objects = new ArrayList<Placeable>();
		objects.addAll(balls);
		objects.add(0, robot.getFront());
		
		System.out.println("objects size: " +objects.size());
		System.out.println("balls size: " +balls.size());
		
		// Ikke længere nødvendigt her. Pathfinder konstruerer matrixen
//		double[][] adjacency = new double[objects.size()][objects.size()];
		
//		System.out.println(objects.get(0).getDistance(objects.get(1)));
		
//		for(int a = 0; a<objects.size();a++){
//			for(int b = 0; b<objects.size();b++){
//				adjacency[b][a]=objects.get(b).getDistance(objects.get(a));
//			}
//		}
		
		
//		System.out.println();
//		for(int a = 0; a<objects.size();a++){
//			for(int b = 0; b<objects.size();b++){
//				System.out.print(adjacency[b][a]+"\t");
//			}
//			System.out.println();
//		}
		
		// KØR HAMILTON PATH PROGRAM (PATHFINDER)
		
		rute = pf.getShortestPath(objects);

		ball = rute.get(0);
		
//		ball = new Placeable(1419, 768);
		gotoXY();
		try {Thread.sleep(12000);} catch (InterruptedException e) { e.printStackTrace();}
		
//		turn();
//		findAndUpdateRobot();
//		
//		drive();
//		try {Thread.sleep(15000);} catch (InterruptedException e) { e.printStackTrace();}
		
//		frame = new Mat();
//		vc.read(frame);
//		System.out.println("frame: "+frame);
//		System.out.println(frame.rows());
//		System.out.println(frame.cols());
		
//		findAndUpdateRobot();
		
		ball=rute.get(1);
		
		gotoXY();
//		turn();
//		findAndUpdateRobot();
//		drive();
		try {Thread.sleep(12000);} catch (InterruptedException e) { e.printStackTrace();}
//
//		findAndUpdateRobot();
		
		ball=rute.get(2);
		
		gotoXY();
//		turn();
//		findAndUpdateRobot();
//		drive();
		try {Thread.sleep(12000);} catch (InterruptedException e) { e.printStackTrace();}
//		
//		
//		findAndUpdateRobot();
		
		ball=rute.get(3);
		
		gotoXY();
		try {Thread.sleep(12000);} catch (InterruptedException e) { e.printStackTrace();}
		
		// TAKE PICTURE
		// Find koordinaterne pï¿½ hvor vï¿½gene mï¿½des (inderside).
		// Find lï¿½ngderne pï¿½ vï¿½gene i pixels 
		// For hver vï¿½g: divider lï¿½ngden i pixels med hvor langt de er i virkeligheden og omvendt
		// Find gennemsnittet af de 2 talsï¿½t
		
//		
//		Ball ball = new Ball(2100, 100);
//		Robot robot = new Robot(100, 100);
//		robot.setBack(new Placeable(0, 100));
//		robot.updateDirection();
//		
//		Direction robotBallVector = new Direction(ball.getX() - robot.getX(), ball.getY() - robot.getY());
//		
//		double robote0 = robot.getDirection().getElement(0);
//		double robote1 = robot.getDirection().getElement(1);
//		double toBalle0 = robotBallVector.getElement(0);
//		double toBalle1 = robotBallVector.getElement(1);
//		
//		double vinkel = ( robote0* toBalle0 + robote1 * toBalle1)
//				/ ((Math.sqrt(Math.pow(robote0, 2) + (Math.pow(robote1, 2))) * (Math
//						.sqrt(Math.pow(toBalle0, 2) + (Math.pow(toBalle1, 2))))));
//
//		double vinkel_grader = Math.toDegrees(Math.acos(vinkel));
//		
//		m = new Movement();
//		convert = new Converter(0.4,2.5);

//		if(robot.getX()>ball.getX()){
//			m.turnLeft((vinkel_grader);			
//		} else {
//			m.turnRight(vinkel_grader);j
//		}

//		System.out.println(vinkel_grader);

		// TAKE PICTURE
		// Robot front og bagende(mï¿½ske) har nu ï¿½ndret position fordi vi har drejet
		
//		robot.setX(100);
//		robot.setY(100);
//		robot.setBack(new Placeable(0, 100));
//		robot.updateDirection();
//		
//		
//		double distance_mm = convert.pixelToMm((robot.getDistance(ball), 1000);
//		System.out.println(distance_mm);
//		followLine(distance_mm);
//		
//		m.armCollect();
//		m.armHold();
		
		//		ImageAnalyzer ia = new ImageAnalyzer();
		//		ia.setImage(frame);
		//		ia.getBalls();
	}
	
	static void turn(){
		robotBallVector = new Direction(ball.getX() - robot.getX(), ball.getY() - robot.getY());
		
		robote0 = robot.getDirection().getElement(0);
		System.out.println("robote0: " + robote0);
		robote1 = robot.getDirection().getElement(1);
		System.out.println("robote1: " + robote1);
		toBalle0 = robotBallVector.getElement(0);
		System.out.println("toBalle0: " + toBalle0);
		toBalle1 = robotBallVector.getElement(1);
		System.out.println("toBalle1: " + toBalle1);
		
//		vinkel = ( robote0* toBalle0 + robote1 * toBalle1)
//				/ ((Math.sqrt(Math.pow(robote0, 2) + (Math.pow(robote1, 2))) * (Math
//						.sqrt(Math.pow(toBalle0, 2) + (Math.pow(toBalle1, 2))))));
//
//		vinkel_grader = Math.toDegrees(Math.acos(vinkel));
		
//		Udregning af vinkel
		double signed_angle = Math.atan2(robote0,robote1) - Math.atan2(toBalle0,toBalle1);
//		laver vinkel fra radianer til grader
		double vinkel_grader = Math.toDegrees(signed_angle);
//		vinkel_grader = vinkel_grader * 0.95;
		
		m.turn(vinkel_grader);
//		if(vinkel_grader > 180){
//			vinkel_grader = vinkel_grader - 360;
//		}

		System.out.println(vinkel_grader+" grader");
	}
	
	static void findAndUpdateRobot(){
		robotpunkter = null;
		try {
			robotpunkter = rf.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(robotpunkter.size());
		
		fb = new FrontBack(robotpunkter.get(0), robotpunkter.get(1), 36.8, 10.5, 17.8);
		robotMiddle =  fb.run();
		
		System.out.println("robotMiddle points: "+robotMiddle.size());
		
		robot.setFront(robotMiddle.get(0));
		robot.setBack(robotMiddle.get(1));
		robot.updateDirection();
	}
	
	static void drive(){
		System.out.println("Distance from robot to ball in pixels: " +robot.getDistance(ball) );
		distance_mm = convert.pixelToMm(robot.getDistance(ball));
		System.out.println("Distance from robot to ball in mm: " +distance_mm);
		m.move(distance_mm);
	}
	
	
	static void gotoXY(){
		while (true){
			
			m.stop();
			findAndUpdateRobot();
			turn();
			findAndUpdateRobot();
			distance_mm = convert.pixelToMm(robot.getDistance(ball));
			System.out.println("distance to target: " +distance_mm);
			if (distance_mm <= 100){
				m.move(distance_mm);
				break;
			}
			m.move(distance_mm);
			try {Thread.sleep(700);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		
		
//		try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
//		m.stop();
//		findAndUpdateRobot();
//		turn();
//		findAndUpdateRobot();
//		distance_mm = convert.pixelToMm(robot.getDistance(ball));
//		m.move(distance_mm);
//		
//		try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
//		m.stop();
//		findAndUpdateRobot();
//		turn();
//		findAndUpdateRobot();
//		distance_mm = convert.pixelToMm(robot.getDistance(ball));
//		m.move(distance_mm);
//		
//		
//		while (!haveArrived){
//			distance_mm = convert.pixelToMm(robot.getDistance(ball));
//			
//			m.stop();
//			findAndUpdateRobot();
//			turn();
//			findAndUpdateRobot();
//			distance_mm = convert.pixelToMm(robot.getDistance(ball));
//			m.move(distance_mm);
//			if (distance_mm <= 2){
//				m.stop();
//				haveArrived = true;
//			}
//		}
	}		
	
	static void followLine(double distance_mm){
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
					// DREJ ROBOTTEN MOD Mï¿½LET
					// MOVE TILBAGEVï¿½RENDE DISTANCE 
				double afvigelse = 0;
				if (afvigelse > 10){
					m.stop();
					
					break;
				} else /*DISTANCE TIL Mï¿½L ER MINDRE END 2 MM*/{
					haveArrived = true;
				}
			}
		}		
	}
}
