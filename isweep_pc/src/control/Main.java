package control;

import boldogrobot.Ball;
import boldogrobot.Robot;
import lejos.nxt.Button;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		Robot robot = new Robot(10, 10);
		robot.setDirection(new Direction(0, 1));
		Ball b1 = new Ball(20, 20);

		int x2 = robot.getX();
		int y2 = robot.getY(); 
		int x1 = b1.getX();
		int y1 = b1.getY();
		
		Direction d = new Direction(x2 - x1, y2 - y1);

		double vinkel = (x2 * y2 + x1 * y1)
				/ ((Math.sqrt(Math.pow(x2, 2) + (Math.pow(x1, 2))) * (Math
						.sqrt(Math.pow(y2, 2) + (Math.pow(y1, 2))))));
		
		System.out.println(Math.toDegrees(Math.acos(vinkel)));

		System.out.println(d);

//		Movement m = new Movement();

		// m.move(600);
		// m.armCollect();
		// m.armHold();
		// m.turnDirection(180);
		// m.move(600);

		// m.armThrow();

		// System.out.println(d);

//		Button.waitForAnyPress(4000);
	}
}
