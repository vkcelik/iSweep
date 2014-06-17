package vision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import boldogrobot.Placeable;

public class FrontBack {

	Placeable front;
	Placeable back;
	double fronttobackdist;
	double robotToback;
	double robotToFront;
	
	public static void main(String[] args) {
		new FrontBack(new Placeable (1310, 879), new Placeable (1306, 616), 36.8, 10.5, 17.8).run();
	}
	
	public List<Placeable> run(){
		List<Placeable> punkter =  new ArrayList<Placeable>();
		
		//udregninger her
		double percentageA = robotToFront/fronttobackdist;
		double percentageB = (fronttobackdist-robotToback)/fronttobackdist;
	
	    double scaledXFront= (back.getX()-front.getX())* percentageA;
	    double scaledYFront=(back.getY()-front.getY())*percentageA;
	    double frontXCoord = scaledXFront+front.getX();
	    double frontYCoord = scaledYFront + front.getY();
	    
	    double scaledXBack= (back.getX()-front.getX())* percentageB;
	    double scaledYBack=(back.getY()-front.getY())*percentageB;
	    double backXCoord = scaledXBack+front.getX();
	    double backYCoord = scaledYBack + front.getY();
	    
		punkter.add(new Placeable(frontXCoord,frontYCoord));
		punkter.add(new Placeable(backXCoord,backYCoord));
		System.out.println(punkter);
		return punkter;
	}
	
	public FrontBack (Placeable front, Placeable back, double fronttobackdist, double robotToback, double robotToFront){
		this.front = front;
		this.back = back;
		this.fronttobackdist = fronttobackdist;
		this.robotToback = robotToback;
		this.robotToFront = robotToFront;
	}

}
