package control;

import lejos.nxt.Button;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		int x2 = 4, x1 = -3, y2 = 2, y1 = 2;

		Direction d = new Direction(x2 - x1, y2 - y1);

		double vinkel = (x2 * y2 + x1 * y1)
				/ ((Math.sqrt(Math.pow(x2, 2) + (Math.pow(x1, 2))) * (Math
						.sqrt(Math.pow(y2, 2) + (Math.pow(y1, 2))))));

		System.out.println(Math.toDegrees(Math.acos(vinkel)));

		System.out.println(d);

		Movement m = new Movement();

		// m.move(600);
		// m.armCollect();
		// m.armHold();
		// m.turnDirection(180);
		// m.move(600);

		// m.armThrow();

		// System.out.println(d);

		Button.waitForAnyPress(4000);
	}
}
