package main;

import java.util.ArrayList;
import java.util.List;

import navigation.PathFinder;
import navigation.Vector2D;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import control.Direction;
import control.Movement;
import boldogrobot.Ball;
import boldogrobot.Placeable;
import boldogrobot.Robot;
import vision.FrontBack;
import vision.ObstacleFinder;
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
	
	static Placeable obstacleCenter = null;
	static Mat frame;
	
	static double[] pxpermm = new double[4];
	static double[] mmperpx = new double[4];
	
	static double avgPxPerMm;
	static double avgMmPerPx;
	
	static boolean goToOtherSide;
	
	static RobotFinder rf;
	static PathFinder pf;
	static WallFinder4 wf;
	static CircleFinder cf;
	static ObstacleFinder of;
	
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
	static Placeable target = null;
	
	public static void main(String[] args) {
		m.armCollect();
		m.armHold();
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		vc =  new VideoCapture(0);
		if(vc.isOpened()){
			System.out.println("Cam opened");
		} else {
			System.out.println("Cam not opened");
		}
		
		rf = new RobotFinder();
		rf.setVc(vc);
		wf = new WallFinder4();
		wf.setVc(vc);
		cf = new CircleFinder();
		cf.setVc(vc);
		of = new ObstacleFinder();
		of.setVc(vc);
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
		
//		avgPxPerMm = 0.8007585714394285;
//		avgMmPerPx =  1.4666499545548353;

		convert = new Converter(avgMmPerPx, avgPxPerMm);
		
		System.out.println(avgMmPerPx);
		System.out.println(avgPxPerMm);
		
		obstacleCenter = of.run();
		
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
//		ball = new Placeable(0, 0);
//		findAndUpdateRobot();
		align();
		if (goToOtherSide){
			Vector2D RT = new Vector2D(target.getX()-robot.getX(), target.getY()-robot.getY());
			double RT_length = RT.getLength();
			Vector2D rt_unit_v = new Vector2D(RT.getX()/RT_length, RT.getY()/RT_length);
			double extend_length =  convert.mmToPixel(20);
			double extend_factor = (RT_length+extend_length)/RT_length;
			Vector2D extendedVector = new Vector2D(rt_unit_v.getX()*extend_factor, rt_unit_v.getY()*extend_factor);
			Placeable otherSide = new Placeable(robot.getX()+extendedVector.getX(), robot.getY()+extendedVector.getY());
			
		}
		gotoXY();
		turn();
//		findAndUpdateRobot();
//		try {Thread.sleep(2000);} catch (InterruptedException e) { e.printStackTrace();}
		m.armCollect();
		m.armHold();
		
		
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
		
		findAndUpdateRobot();
		align();
		gotoXY();
		turn();
		m.armCollect();
		m.armHold();
//		turn();
//		findAndUpdateRobot();
//		drive();
//
//		findAndUpdateRobot();
		
		ball=rute.get(2);
		
		findAndUpdateRobot();
		align();
		gotoXY();
		turn();
		m.armCollect();
		m.armHold();
//		turn();
//		findAndUpdateRobot();
//		drive();
//		
//		
//		findAndUpdateRobot();
		
		ball=rute.get(3);
		
		findAndUpdateRobot();
		align();
		gotoXY();
		turn();
		m.armCollect();
		m.armHold();
		
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
		findAndUpdateRobot();
		robotBallVector = new Direction(target.getX() - robot.getX(), target.getY() - robot.getY());
		
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
		
		if(vinkel_grader > 180){
			vinkel_grader = 360-vinkel_grader;
		} else if (vinkel_grader < -180){
			vinkel_grader = vinkel_grader + 360;
		}
//		try {Thread.sleep(2000);} catch (InterruptedException e) { e.printStackTrace();}
		m.turn(vinkel_grader);

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
//		double initial_distance = robot.getDistance(target);
//		double distance_left = robot.getDistance(target);
		while (true){
			m.stop();
			findAndUpdateRobot();
			align();
			turn();
			findAndUpdateRobot();
			distance_mm = convert.pixelToMm(robot.getDistance(target));
			System.out.println("distance to target: " +distance_mm);
			if (distance_mm <= 80){
				m.moveBlocking(distance_mm);
				break;
			} else {
				m.moveBlocking(80);
//				distance_left = distance_left - distance_mm;
			}
//			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
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
	
	static void align(){
		Vector2D retning = new Vector2D(robot.getDirection().getElement(0),robot.getDirection().getElement(1));
		Vector2D RB= new Vector2D(ball.getX()-robot.getX(), ball.getY()-robot.getY());
		System.out.println(RB);
		Vector2D vinkelretRetning = new Vector2D(retning.getY(), -1*retning.getX());
		System.out.println(vinkelretRetning);
		double laengdeVinkelRetning = vinkelretRetning.getLength();
		System.out.println(laengdeVinkelRetning);
		Vector2D enhedVinkelretRetning = new Vector2D(vinkelretRetning.getX()/laengdeVinkelRetning, vinkelretRetning.getY()/laengdeVinkelRetning);
		System.out.println(enhedVinkelretRetning);
		System.out.println(enhedVinkelretRetning.getLength());
		Vector2D korrektLaengdeVinkelret = new Vector2D(enhedVinkelretRetning.getX()*convert.mmToPixel(48), enhedVinkelretRetning.getY()*convert.mmToPixel(48));
		System.out.println(korrektLaengdeVinkelret);
		System.out.println(korrektLaengdeVinkelret.getLength());
		Placeable ballArm = new Placeable(ball.getX()+korrektLaengdeVinkelret.getX(), ball.getY()+korrektLaengdeVinkelret.getY());
		System.out.println(ballArm);
		Vector2D armRobot = new Vector2D(robot.getX()-ballArm.getX(), robot.getY()-ballArm.getY());
		System.out.println(armRobot);
		double laengdeArmRobot = armRobot.getLength();
		System.out.println(laengdeArmRobot);
		double forhold = convert.mmToPixel(140)/laengdeArmRobot;
		System.out.println(forhold);
		Vector2D modRobot = new Vector2D(armRobot.getX()*forhold, armRobot.getY()*forhold);
		System.out.println(modRobot);
		System.out.println(convert.pixelToMm(modRobot.getLength()));
		Placeable maal = new Placeable(ballArm.getX()+modRobot.getX(), ballArm.getY()+modRobot.getY());
		
		if(convert.pixelToMm(obstacleCenter.getDistance(maal)) < 200){
			goToOtherSide = true;
		}
		
		System.out.println(maal);
		target = maal;
//		target = new Placeable( 740, 548);
		
		
		
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
