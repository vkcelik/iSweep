package vision;

import boldogrobot.Placeable;

public class Line {
	
	Placeable endPoint1;
	Placeable endPoint2;
	public Placeable getEndPoint1() {
		return endPoint1;
	}
	public void setEndPoint1(Placeable endPoint1) {
		this.endPoint1 = endPoint1;
	}
	public Placeable getEndPoint2() {
		return endPoint2;
	}
	public void setEndPoint2(Placeable endPoint2) {
		this.endPoint2 = endPoint2;
	}
	public Line(Placeable endPoint1, Placeable endPoint2) {
		this.endPoint1 = endPoint1;
		this.endPoint2 = endPoint2;
	}
	

}
