package control;


import lejos.nxt.Button;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		

		int  x2=0,x1=0,y2=0, y1=0;

		Direction d = new Direction(x2-x1,y2-y1);
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
