package navigation;
import java.awt.List;
import java.util.ArrayList;

import boldogrobot.Placeable;

import navigation.Retangle;
import boldogrobot.Ball;

public class InsideArea {

	public void run(){
		ArrayList<Retangle> Omroder = new ArrayList<Retangle>();

		Omroder.add(new Retangle(new Placeable(100,100),new Placeable(200,100),new Placeable(100,200),new Placeable(200,200)));
		Omroder.add(new Retangle(new Placeable(200,100),new Placeable(200,200),new Placeable(400,100),new Placeable(400,200)));
		Omroder.add(new Retangle(new Placeable(400, 100),new Placeable(500, 100), new Placeable(400, 200), new Placeable(500, 200)));
		Omroder.add(new Retangle(new Placeable(100, 200),new Placeable(200, 200), new Placeable(100, 300), new Placeable(200, 300)));
		Omroder.add(new Retangle(new Placeable(400, 200),new Placeable(500, 200), new Placeable(400, 300), new Placeable(500, 300)));
		Omroder.add(new Retangle(new Placeable(100, 300), new Placeable(200, 300), new Placeable(100, 400), new Placeable(200, 400)));
		Omroder.add(new Retangle(new Placeable(200, 300), new Placeable(400, 300), new Placeable(200, 400), new Placeable(400, 400)));
		Omroder.add(new Retangle(new Placeable(400, 300), new Placeable(500, 300), new Placeable(400, 400), new Placeable(500, 400)));

		ArrayList<Ball> balls = new ArrayList<Ball>();

		balls.add(new Ball(150,50));
		balls.add(new Ball(375,75));
		balls.add(new Ball(200,350));
		
		for (Ball b: balls){
			for(Retangle f: Omroder){
				
				
				
				
				
			}
		}



	}




}
