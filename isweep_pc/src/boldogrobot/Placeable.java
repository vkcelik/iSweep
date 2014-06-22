package boldogrobot;

public class Placeable {
	double x;
	double y;
	String type;

	public Placeable(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Placeable(double x, double y, String type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public Placeable() {
	}
	
	public double getDistance(Placeable c) {
		return Math.sqrt(Math.pow(c.x-x, 2) + Math.pow(c.y-y, 2));
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
