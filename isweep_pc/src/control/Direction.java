package control;

import java.util.Vector;

public class Direction {
	Vector<Integer> elements=new Vector<Integer>(2);
	
	
	
	public Direction(int e1, int e2){
		
		elements=new Vector<Integer>(2);
		elements.addElement(e1);
		elements.addElement(e2);

		 
	}
	
	public String toString(){
		return "["+elements.elementAt(0)+","+elements.elementAt(1)+"]";
	}
	
	public int getElement(int numberFromTop){
		return elements.elementAt(numberFromTop);
	}
		 
}