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
	double backToFrontRobot;
	
	public static void main(String[] args) {
		new FrontBack(new Placeable (2753, 837), new Placeable (2829, 1481), 47.5, 10.5, 17.5, 30).run();
	}
	
	public List<Placeable> run(){
		List<Placeable> punkter =  new ArrayList<Placeable>();
		
		//udregninger her
		double percentageA = robotToback/fronttobackdist;
		double percentageB = backToFrontRobot/fronttobackdist;
	

	    double xt= (front.getX()-back.getX())* percentageA; 
	    double yt=(front.getY()-back.getY())*percentageA;
	    double xK = (xt+back.getX());
	    double yK = (yt + back.getY());
	    
	    double xxt= (front.getX()-back.getX())* percentageB; 
	    double yyt=(front.getY()-back.getY())*percentageB;
	    double xxK = (xxt+back.getX());
	    double yyK = (yyt + back.getY());
		
		punkter.add(new Placeable((int)xK,(int)yK));
		punkter.add(new Placeable((int)xxK,(int)yyK));
		System.out.println(punkter);
		return punkter;
	
	}
	
	public FrontBack (Placeable front, Placeable back, double fronttobackdist, double robotToback, double robotToFront, double backToFrontRobot){
		this.front = front;
		this.back = back;
		this.fronttobackdist = fronttobackdist;
		this.robotToback = robotToback;
		this.robotToFront = robotToFront;
		this.backToFrontRobot = backToFrontRobot;
	}

}
