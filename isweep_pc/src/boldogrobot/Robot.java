package boldogrobot;

import control.Direction;

public class Robot extends Placeable{
	
	Direction direction;
	
	public Robot(int x, int y) {
		super(x, y);
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	
}