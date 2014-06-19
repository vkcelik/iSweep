package navigation;

public class Vector2D {
	
	double x;
	double y;
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
	public Vector2D(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public double getLength(){
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
	}
	
	public String toString(){
		return "["+x+" "+y+"]";
	}
	
	public Vector2D() {
		super();
	}

}
