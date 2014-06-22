package navigation;
import java.awt.List;
import java.util.ArrayList;

import boldogrobot.Placeable;

import navigation.Retangle;
import boldogrobot.Ball;

public class InsideArea {

	
	public int run(ArrayList<Retangle> omroder, Placeable ball){
		int ret = -1;
		for(int i=0; i<omroder.size();i++){
			Retangle f = omroder.get(i);
			Placeable P = f.topLeft;
			Placeable Q = f.topRight;
			Placeable R = f.bottomLeft;
			Placeable A = new Placeable(ball.getX(), ball.getY());

			Vector2D PQ = new Vector2D(Q.getX()-P.getX(), Q.getY()-P.getY());
			Vector2D PR = new Vector2D(R.getX()-P.getX(), R.getY()-P.getY());
			Vector2D PA = new Vector2D(A.getX()-P.getX(), A.getY()-P.getY());

			double d = det(PQ, PR);

			double n = -det(PA, PQ)/d;
			double m = det(PA, PR)/d;


			if ((0 <= n && n <= 1) && (0 <= m && m<=1)){

				ret = i;
			}
			else {

				ret = -1;

			}


		}
		return ret;
	}

	
	public void run(ArrayList<Retangle> omroder, ArrayList<Placeable> balls){
		for (Placeable b: balls){
			for(Retangle f: omroder){

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
		//new InsideArea().run();
	}

}
