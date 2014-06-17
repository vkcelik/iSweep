package test;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

import boldogrobot.Placeable;
import navigation.CalculateRegions;
import navigation.Retangle;

public class RegionsTest {

	public static void main(String[] args) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		Placeable topLeft= new Placeable(325, 95);
		Placeable topRight = new Placeable(1658, 86);
		Placeable bottomLeft = new Placeable(321, 976);
		Placeable bottomRight = new Placeable(1660, 990);

		CalculateRegions reg =  new CalculateRegions(topLeft, topRight, bottomLeft, bottomRight);
		ArrayList<Retangle> omroder =reg.run();
		
		Mat src = Highgui.imread("Picture 23.jpg",1);
		
		for (Retangle r : omroder){
			System.out.println("Drawing from "+r.getTopLeft() + " to "+r.getTopRight());
			Core.line(src, new Point(r.getTopLeft().getX(), r.getTopLeft().getY()), new Point(r.getTopRight().getX(), r.getTopRight().getY()), new Scalar(0,0,255), 2);
			System.out.println("Drawing from "+r.getTopRight() + " to "+r.getBottomRight());
			Core.line(src, new Point(r.getTopRight().getX(), r.getTopRight().getY()), new Point(r.getBottomRight().getX(), r.getBottomRight().getY()), new Scalar(0,0,255), 2);
			System.out.println("Drawing from "+r.getBottomRight() + " to "+r.getBottomLeft());
			Core.line(src, new Point(r.getBottomRight().getX(), r.getBottomRight().getY()), new Point(r.getBottomLeft().getX(), r.getBottomLeft().getY()), new Scalar(0,0,255), 2);
			System.out.println("Drawing from "+r.getBottomLeft() + " to "+r.getTopLeft());
			Core.line(src, new Point(r.getBottomLeft().getX(), r.getBottomLeft().getY()), new Point(r.getTopLeft().getX(), r.getTopLeft().getY()), new Scalar(0,0,255), 2);
		}
		
		Highgui.imwrite("test.jpg", src);
		
		System.out.println(omroder.get(6).getBottomRight());
		System.out.println(omroder.get(7).getBottomLeft());
		System.out.println(omroder.get(5).getBottomRight());
	}

}
