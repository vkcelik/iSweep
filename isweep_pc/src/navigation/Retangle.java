package navigation;

import boldogrobot.Placeable;

public class Retangle {
	
	Placeable topLeft;
	Placeable topRight;
	Placeable bottomLeft;
	Placeable bottomRight;
	public Placeable getTopLeft() {
		return topLeft;
	}
	public void setTopLeft(Placeable topLeft) {
		this.topLeft = topLeft;
	}
	public Placeable getTopRight() {
		return topRight;
	}
	public void setTopRight(Placeable topRight) {
		this.topRight = topRight;
	}
	public Placeable getBottomLeft() {
		return bottomLeft;
	}
	public void setBottomLeft(Placeable bottomLeft) {
		this.bottomLeft = bottomLeft;
	}
	public Placeable getBottomRight() {
		return bottomRight;
	}
	public void setBottomRight(Placeable bottomRight) {
		this.bottomRight = bottomRight;
	}
	public Retangle(Placeable topLeft, Placeable topRight,
			Placeable bottomLeft, Placeable bottomRight) {
		super();
		this.topLeft = topLeft;
		this.topRight = topRight;
		this.bottomLeft = bottomLeft;
		this.bottomRight = bottomRight;
	}
	public Retangle() {
		super();
	}
	
	public String toString(){
		return topLeft.toString()+ " ," + topRight.toString()+", " + bottomLeft.toString() +", " + bottomRight.toString() +", ";
	}

}
