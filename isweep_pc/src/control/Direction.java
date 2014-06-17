package control;

import java.util.Vector;

public class Direction {
	Vector<Double> elements=new Vector<Double>(2);
	
	
	
	public Direction(double e1, double e2){
		
		elements=new Vector<Double>(2);
		elements.addElement(e1);
		elements.addElement(e2);

		 
	}
	
	public String toString(){
		return "["+elements.elementAt(0)+","+elements.elementAt(1)+"]";
	}
	
	public double getElement(int numberFromTop){
		return elements.elementAt(numberFromTop);
	}
		 
}