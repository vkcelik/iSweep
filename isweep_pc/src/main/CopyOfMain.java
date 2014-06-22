package main;

import java.util.ArrayList;
import java.util.List;

import navigation.CalculateRegions;
import navigation.InsideArea;
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

	static List<Placeable> punkter = null;

	static Movement m = new Movement();
	static Placeable ball = null;
	static Placeable target = null;

	static Placeable lookAt = null;

	static boolean emptied = false;

	static boolean avoiding = false;

	public static void main(String[] args) {
//		m.armCollect();
//		m.armHold();
//		m.turn(360);
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

		int number_of_balls = balls.size();
		int balls_left = number_of_balls;
		System.out.println(balls.size());

		objects = new ArrayList<Placeable>();
		objects.addAll(balls);
		objects.add(0, robot.getFront());

		System.out.println("objects size: " +objects.size());
		System.out.println("balls size: " +balls.size());


		// KØR HAMILTON PATH PROGRAM (PATHFINDER)

		rute = pf.getShortestPath(objects);

		//findGoalTarget();

		punkter = new ArrayList<Placeable>();


		System.out.println(findGoalTarget());

		m.armCollect();
		m.armHold();


		do {
			Placeable ball = rute.remove(0);
			System.out.println("GOING FOR: " + ball);
			gotoXY(ball);
			turnAvoid(align(ball));
			m.armCollect();
			m.armHold();			
		} while (!rute.isEmpty());

		gotoGoal(findGoalTarget());
		turnGoal(lookAt);
		m.armThrow();
		try {Thread.sleep(2000);} catch (InterruptedException e) { e.printStackTrace();}


		//		m.armCollect();
		//		m.armHold();
		//
		//		for(int i=0;i<rute.size();i++){
		//			ball = rute.get(i);
		//			//		findAndUpdateRobot();
		//			//align();
		//			System.out.println(goToOtherSide);
		//			if (goToOtherSide){
		//				Vector2D RT = new Vector2D(target.getX()-robot.getX(), target.getY()-robot.getY());
		//				double RT_length = RT.getLength();
		//				Vector2D rt_unit_v = new Vector2D(RT.getX()/RT_length, RT.getY()/RT_length);
		//				double extend_length =  convert.mmToPixel(20);
		//				double extend_factor = (RT_length+extend_length)/RT_length;
		//				Vector2D extendedVector = new Vector2D(rt_unit_v.getX()*extend_factor, rt_unit_v.getY()*extend_factor);
		//				Placeable otherSide = new Placeable(robot.getX()+extendedVector.getX(), robot.getY()+extendedVector.getY());
		//				gotoXY();
		//			} else {
		//				gotoXY();
		//			}
		//			turn();
		//			//		findAndUpdateRobot();
		//			//		try {Thread.sleep(2000);} catch (InterruptedException e) { e.printStackTrace();}
		//			m.armCollect();
		//			m.armHold();
		//			try {
		//				balls = cf.run();
		//			} catch (Exception e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//			balls_left = balls.size();
		//			if(balls_left == 0){
		//				gotoXY(findGoalTarget());
		//				turnAvoid(lookAt);
		//				try {Thread.sleep(1000);} catch (InterruptedException e) { e.printStackTrace();}
		//				m.turn(10);
		//				try {Thread.sleep(1000);} catch (InterruptedException e) { e.printStackTrace();}
		//				m.armThrow();
		//				try {Thread.sleep(3000);} catch (InterruptedException e) { e.printStackTrace();}
		//				m.armHold();
		//			} else if(number_of_balls - balls_left % 2 == 0 && emptied == false){
		//				gotoXY(findGoalTarget());
		//				turnAvoid(lookAt);
		//				m.turn(10);
		//				try {Thread.sleep(1000);} catch (InterruptedException e) { e.printStackTrace();}
		//				m.armThrow();
		//				try {Thread.sleep(3000);} catch (InterruptedException e) { e.printStackTrace();}
		//				m.armHold();
		//				emptied = true;
		//			} else {
		//				emptied = false;
		//			}
		//		}

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

		//		
		//		ball = rute.get(1);
		////		ball = new Placeable(0, 0);
		////		findAndUpdateRobot();
		//		align();
		//		System.out.println(goToOtherSide);
		//		if (goToOtherSide){
		//			Vector2D RT = new Vector2D(target.getX()-robot.getX(), target.getY()-robot.getY());
		//			double RT_length = RT.getLength();
		//			Vector2D rt_unit_v = new Vector2D(RT.getX()/RT_length, RT.getY()/RT_length);
		//			double extend_length =  convert.mmToPixel(20);
		//			double extend_factor = (RT_length+extend_length)/RT_length;
		//			Vector2D extendedVector = new Vector2D(rt_unit_v.getX()*extend_factor, rt_unit_v.getY()*extend_factor);
		//			Placeable otherSide = new Placeable(robot.getX()+extendedVector.getX(), robot.getY()+extendedVector.getY());
		//			gotoXY();
		//		} else {
		//			gotoXY();
		//		}
		//		turn();
		////		findAndUpdateRobot();
		////		try {Thread.sleep(2000);} catch (InterruptedException e) { e.printStackTrace();}
		//		m.armCollect();
		//		m.armHold();
		//		
		//		
		//		ball=rute.get(1);
		//		findAndUpdateRobot();
		//		align();
		//		gotoXY();
		//		turn();
		//		m.armCollect();
		//		m.armHold();
		//
		//		
		//		
		//		//		turn();
		////		findAndUpdateRobot();
		////		drive();
		////
		////		findAndUpdateRobot();
		//		
		//		ball=rute.get(2);
		//		
		//		findAndUpdateRobot();
		//		align();
		//		gotoXY();
		//		turn();
		//		m.armCollect();
		//		m.armHold();
		////		turn();
		////		findAndUpdateRobot();
		////		drive();
		////		
		////		
		////		findAndUpdateRobot();
		//		
		//		ball=rute.get(3);
		//		
		//		findAndUpdateRobot();
		//		align();
		//		gotoXY();
		//		turn();
		//		m.armCollect();
		//		m.armHold();


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

	static void turnAvoid(Placeable target){
		//findAndUpdateRobot();
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

	static void turnGoal(Placeable target){
		//findAndUpdateRobot();
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

		//		if(vinkel_grader > 180){
		//			vinkel_grader = 360-vinkel_grader;
		//		} else if (vinkel_grader < -180){
		//			vinkel_grader = vinkel_grader + 360;
		//		}
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
		while (true){
			//			m.stop();
			findAndUpdateRobot();
			System.out.println("inside gotoXY");
			// Check om forhindring ligger på ruten
			Vector2D RT = new Vector2D(target.getX()-robot.getX(), target.getY()-robot.getY());
			System.out.println("RT: "+RT);
			// Find afstand fra vektor til forhindring
			double dy = target.getY()-robot.getY();
			System.out.println("dy: "+dy);
			double dx = target.getX()-robot.getX();
			System.out.println("dx: "+dx);
			double d = Math.abs(dy*obstacleCenter.getX()-dx*obstacleCenter.getY()-robot.getX()*target.getY()+target.getX()*robot.getY())/Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
			System.out.println("d: "+d);
			if (d < convert.mmToPixel(200)){
				Vector2D vinkelvenstre = new Vector2D(RT.getY(), -1*RT.getX());
				Vector2D vinkelhoejre = new Vector2D(-1*RT.getY(), RT.getX());
				double laengde = vinkelvenstre.getLength();
				Vector2D venstreUnit = new Vector2D(vinkelvenstre.getX()/laengde, vinkelvenstre.getY()/laengde);
				Vector2D hoejreUnit = new Vector2D(vinkelhoejre.getX()/laengde, vinkelhoejre.getY()/laengde);
				double rPlusA_pixel =  convert.mmToPixel(300);
				Vector2D hoejreKorrekt = new Vector2D(hoejreUnit.getX()*rPlusA_pixel, hoejreUnit.getY()*rPlusA_pixel);
				Vector2D venstreKorrekt = new Vector2D(venstreUnit.getX()*rPlusA_pixel, venstreUnit.getY()*rPlusA_pixel);
				Placeable venstreOm = new Placeable(obstacleCenter.getX()+venstreKorrekt.getX(), obstacleCenter.getY()+venstreKorrekt.getY());
				Placeable hoejreOm = new Placeable(obstacleCenter.getX()+hoejreKorrekt.getX(), obstacleCenter.getY()+hoejreKorrekt.getY());
				if (robot.getDistance(venstreOm) > robot.getDistance(venstreOm)){
					System.out.println("GOING TO HOEJRE: " + hoejreOm);
					gotoXY(hoejreOm);
				} else {
					System.out.println("GOING TO VENSTRE: " + venstreOm);
					gotoXY(venstreOm);
				}
			}
			//	align();
			turn();
			findAndUpdateRobot();


			distance_mm = convert.pixelToMm(robot.getDistance(target));
			System.out.println("distance to target: " +distance_mm);
			if (distance_mm <= 80){
				m.moveBlocking(distance_mm);
				break;
			} else {
				m.moveBlocking(80);
			}
		}
	}		


	static void gotoXY(Placeable target){
		punkter = new ArrayList<Placeable>();
		while (true){
			//			m.stop();
			findAndUpdateRobot();
			Placeable wall = approachWall(target);
			if (wall!=null && !avoiding){
				int wallIndex = -1;
				for (int i = 0; i<punkter.size();i++){
					if (punkter.get(i).getType().equals("Wall")){
						wallIndex = i;
					}
				}
				if (wallIndex != -1){
					punkter.remove(wallIndex);
				}
				punkter.add(wall);
			} else {
			Placeable samlePos = align(target);
			if (!avoiding){
				int samleIndex = -1;
				for (int i = 0; i<punkter.size();i++){
					if (punkter.get(i).getType().equals("Samle")){
						samleIndex = i;
					}
				}
				if (samleIndex != -1){
					punkter.remove(samleIndex);
				}
				punkter.add(samlePos);
			}
			}
			Placeable avoid = avoidCollision(punkter.get(0));
			if (avoid != null) {
				punkter.remove(0);
				punkter.add(0, avoid);
				avoiding = true;
			}
			Placeable next = punkter.get(0);

			turnAvoid(next);
			findAndUpdateRobot();
			distance_mm = convert.pixelToMm(robot.getDistance(next));
			System.out.println("distance to target: " +distance_mm);
			if (distance_mm <= 80){
				m.moveBlocking(distance_mm-2);
				if(avoiding){
					avoiding = false;
				}
				System.out.println("PUNKTER: "+punkter.size());
				if(punkter.size() != 0){
					System.out.println("PUNKT TYPE: "+punkter.get(0).getType());
				}
				if (punkter.size() == 1 && punkter.get(0).getType().equals("Samle")){
					//					punkter.remove(0);
					break;
				}
				
				if (punkter.size() == 1 && punkter.get(0).getType().equals("Wall")){
					
					punkter.add(align(target));
				}
				punkter.remove(0);
			} else {
				m.moveBlocking(80);
			}
			//			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		}

	}


	static void gotoGoal(Placeable target){
		List<Placeable> way = new ArrayList<Placeable>();
		way.add(target);
		do {
			findAndUpdateRobot();
			Placeable avoid = avoidCollision(way.get(0));
			if (avoid != null) {
				way.add(0, avoid);
			}
			Placeable next = way.get(0);

			turnAvoid(next);
			findAndUpdateRobot();
			distance_mm = convert.pixelToMm(robot.getDistance(next));
			System.out.println("distance to target: " +distance_mm);
			if (distance_mm <= 80){
				m.moveBlocking(distance_mm+2);
				way.remove(0);
			} else {
				m.moveBlocking(80);
			}
		} while (!way.isEmpty());
	}

	static Placeable findGoalTarget(){
		Vector2D side = new Vector2D(corners.get(1).getX()-corners.get(3).getX(), corners.get(1).getY()-corners.get(3).getY());
		double sideLength = side.getLength();
		System.out.println(convert.pixelToMm(sideLength));
		System.out.println(sideLength);
		double goalpost = convert.mmToPixel(560);
		double scale = goalpost/sideLength;
		Vector2D goalVector =  new Vector2D(side.getX()*scale, side.getY()*scale);
		Placeable post = new Placeable(corners.get(3).getX()+goalVector.getX(), corners.get(3).getY()+goalVector.getY());
		System.out.println(post);
		Vector2D vinkelret  = new Vector2D(side.getY(), side.getX()*-1);
		double scale2 = convert.mmToPixel(285)/sideLength;
		System.out.println(scale2);
		Vector2D towardCenter =  new Vector2D(vinkelret.getX()*scale2, vinkelret.getY()*scale2);
		System.out.println(towardCenter);
		Placeable dropPoint = new Placeable(post.getX()+towardCenter.getX(), post.getY()+towardCenter.getY());
		System.out.println(dropPoint);
		lookAt =  new Placeable(post.getX()+towardCenter.getX()*2, post.getY()+towardCenter.getY()*2);
		System.out.println(lookAt);
		return dropPoint;
	}

	static Placeable align(Placeable ball){
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
		Placeable maal = new Placeable(ballArm.getX()+modRobot.getX(), ballArm.getY()+modRobot.getY(),"Samle");
		System.out.println(maal);

		System.out.println("target distance from obstacle center: "+convert.pixelToMm(obstacleCenter.getDistance(maal)));
		//		if(convert.pixelToMm(obstacleCenter.getDistance(maal)) < 200){
		//			System.out.println("target inside danger");
		//			goToOtherSide = true;
		//		}



		return maal;
	}

	static Placeable approachWall(Placeable ball){
		double wall_dist = convert.mmToPixel(300);
		Placeable ret = null;
		CalculateRegions cr = new CalculateRegions(corners.get(0), corners.get(1), corners.get(2), corners.get(3));
		InsideArea ia = new InsideArea();
		int area = ia.run(cr.run(), ball);
		if(area != -1){
			switch (area) {
			case 0:

				break;
			case 1:
				ret = new Placeable(ball.getX(), ball.getY()+wall_dist,"Wall");
				break;
			case 2:
				
				break;
			case 3:
				ret = new Placeable(ball.getX() + wall_dist, ball.getY(),"Wall");
				break;
			case 4:
				ret = new Placeable(ball.getX() - wall_dist, ball.getY(),"Wall");
				break;
			case 5:

				break;
			case 6:
				ret = new Placeable(ball.getX(), ball.getY()+ wall_dist,"Wall");
				break;
			case 7:

				break;
			default:
				break;
			}
		}
		return ret;
	}


	static Placeable avoidCollision(Placeable go){
		Placeable ret = null;
		// Check om forhindring ligger på ruten
		double a = go.getX() - robot.getX();
		System.out.println("a: "+a);
		double b = go.getY() - robot.getY();
		System.out.println("b: "+b);
		double c = obstacleCenter.getX() - robot.getX();
		System.out.println("c: "+c);
		double d =  obstacleCenter.getY() - robot.getY();
		System.out.println("d: "+d);
		double r = convert.mmToPixel(200);
		System.out.println("r: "+r);
		if ((d*a - c*b)*(d*a - c*b) <= r*r*(a*a + b*b)) { 
			System.out.println("Collision");
			// collision
			Vector2D RT = new Vector2D(go.getX()-robot.getX(), go.getY()-robot.getY());
			Vector2D vinkelvenstre = new Vector2D(RT.getY(), -1*RT.getX());
			Vector2D vinkelhoejre = new Vector2D(-1*RT.getY(), RT.getX());
			double laengde = vinkelvenstre.getLength();
			Vector2D venstreUnit = new Vector2D(vinkelvenstre.getX()/laengde, vinkelvenstre.getY()/laengde);
			Vector2D hoejreUnit = new Vector2D(vinkelhoejre.getX()/laengde, vinkelhoejre.getY()/laengde);
			double rPlusA_pixel =  convert.mmToPixel(300);
			Vector2D hoejreKorrekt = new Vector2D(hoejreUnit.getX()*rPlusA_pixel, hoejreUnit.getY()*rPlusA_pixel);
			Vector2D venstreKorrekt = new Vector2D(venstreUnit.getX()*rPlusA_pixel, venstreUnit.getY()*rPlusA_pixel);
			Placeable venstreOm = new Placeable(obstacleCenter.getX()+venstreKorrekt.getX(), obstacleCenter.getY()+venstreKorrekt.getY(), "Avoid");
			Placeable hoejreOm = new Placeable(obstacleCenter.getX()+hoejreKorrekt.getX(), obstacleCenter.getY()+hoejreKorrekt.getY(), "Avoid");
			//if (robot.getDistance(venstreOm) > robot.getDistance(venstreOm)){
			if (true){
				System.out.println("GOING TO HOEJRE: " + hoejreOm);
				ret = hoejreOm;
			} /* else {
				System.out.println("GOING TO VENSTRE: " + venstreOm);
				gotoXY(venstreOm);
			} */
		}
		return ret;
	}

}
