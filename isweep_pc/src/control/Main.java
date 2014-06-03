package control;

import boldogrobot.Ball;
import boldogrobot.Placeable;
import boldogrobot.Robot;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		Robot robot = new Robot(1530, 412);
		robot.setBack(new Placeable(1541, 505));
		robot.setDirection(new Direction(robot.getX()-robot.getBack().getX(),robot.getY()-robot.getBack().getY()));
		System.out.println(robot.getDirection());
		
		Ball b1 = new Ball(825,803);
		Ball b1_aligned_arm =  new Ball(b1.getX()+127, b1.getY()+60);
		Direction d = new Direction(b1_aligned_arm.getX() - robot.getX(), b1_aligned_arm.getY() - robot.getY());
		System.out.println(d);
		
		int robote0 = robot.getDirection().getElement(0);
		int robote1 = robot.getDirection().getElement(1);
		int toBalle0 = d.getElement(0);
		int toBalle1 = d.getElement(1);
		
		double vinkel = ( robote0* toBalle0 + robote1 * toBalle1)
				/ ((Math.sqrt(Math.pow(robote0, 2) + (Math.pow(robote1, 2))) * (Math
						.sqrt(Math.pow(toBalle0, 2) + (Math.pow(toBalle1, 2))))));
		
		double vinkel_grader = Math.toDegrees(Math.acos(vinkel));
		
		Movement m = new Movement();
		if(robot.getX()>b1.getX()){
			m.turnLeft((int)vinkel_grader);			
		} else {
			m.turnRight((int)vinkel_grader);
		}
		
		System.out.println(vinkel_grader);

			int L_side_PX=1000, L_side_MM=1200;
			float pixelpermm = (float)L_side_PX/L_side_MM;
			int distance_mm = (int) (robot.getDistance(b1)/pixelpermm);
		
		System.out.println(distance_mm);
		m.move(distance_mm);

		 m.armCollect();
		 m.armHold();
		// m.turnDirection(180);
		// m.move(600);

		// m.armThrow();

		// System.out.println(d);

//		Button.waitForAnyPress(4000);
	}
}
