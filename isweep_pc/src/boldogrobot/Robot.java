package boldogrobot;

import control.Direction;

public class Robot extends Placeable{
	
	Direction direction;
	Placeable back;
	
	public Robot(int x, int y) {
		super(x, y);
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

	public void setBack(Placeable back) {
		this.back = back;
	}
	
	
}