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
		balls.add(new Ball(150,350));

		for (Ball b: balls){
			for(Retangle f: Omroder){

				Placeable P = f.topLeft;
				Placeable Q = f.topRight;
				Placeable R = f.bottomLeft;
				Placeable A = new Placeable(b.getX(), b.getY());

				Vector2D PQ = new Vector2D(Q.getX()-P.getX(), Q.getY()-P.getY());
				Vector2D PR = new Vector2D(R.getX()-P.getX(), R.getY()-P.getY());
				Vector2D PA = new Vector2D(A.getX()-P.getX(), A.getY()-P.getY());

				double d = det(PQ, PR);
				
				double n = -det(PA, PQ)/d;
				double m = det(PA, PR)/d;

				
				if ((0 <= n && n <= 1) && (0 <= m && m<=1)){
					
					System.out.println("Ball inside");
				}
				else {
					
					System.out.println("Ball outside");
					
				}
				


			}
		}
	}


	private double det(Vector2D a, Vector2D b){
		return (a.x * b.y)-(b.x*a.y); 
	}
	
	public static void main(String[] args) {
		new InsideArea().run();
	}

}
