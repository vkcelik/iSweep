package boldogrobot;

import control.Direction;

public class Robot extends Placeable{
	
	Direction direction;
	Placeable back;
	
	public Robot(double x, double y) {
		super(x, y);
	}
	
	public Robot() {
		super();
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void updateDirection(){
		direction = new Direction(this.getX()-back.getX(),this.getY()-back.getY());
	}

	public Placeable getBack() {
		return back;
	}
	
	public Placeable getFront() {
		return new Placeable(x, y);
	}

	public void setBack(Placeable back) {
		this.back = back;
	}
	
	public void setFront(Placeable front){
		this.x = front.getX();
		this.y = front.getY();
	}
	
	public Robot(Placeable front){
		super(front.getX(),front.getY());
	}

	
}