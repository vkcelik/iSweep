package boldogrobot;

public class Placeable {
	int x;
	int y;

	public Placeable(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double getDistance(Placeable c) {
		return Math.sqrt(Math.pow(c.x-x, 2) + Math.pow(c.y-y, 2));
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
