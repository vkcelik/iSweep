package control;


import lejos.nxt.Button;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Direction d = new Direction(5,6);
		System.out.println(d);
		Movement m = new Movement();



		//		m.move(600);
		//		m.armCollect();
		//		m.armHold();
//		m.turnDirection(180);
		//		m.move(600);

		//		m.armThrow();

//		System.out.println(d);

		Button.waitForAnyPress(4000);
	}
}
