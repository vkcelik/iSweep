package vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import boldogrobot.Ball;
import boldogrobot.Robot;

public class RobotFinder {
	
	Mat src;

	public Robot run() throws Exception{
		boolean loadImageFromFile = true;
		boolean printCircleCoordinates = true;
		List<Ball> list = new ArrayList<Ball>();
		Mat src_gray = new Mat();
		Mat smooth = new Mat();
		Mat circles_g = new Mat();
		Mat circles_l = new Mat();
		Mat filteredlilla = new Mat();
		Mat filteredgron= new Mat();
		Mat hsv = new Mat();
		
		if(loadImageFromFile){
			src = Highgui.imread("Picture 12.jpg",1);
		} else {
			// load frame (image) from webcam
			VideoCapture webSource = new VideoCapture(1);
			Thread.sleep(2000);
			webSource.retrieve(src);
		}

		Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);
		Core.inRange(hsv, new Scalar(130, 98, 97), new Scalar(175, 255, 147), filteredlilla);
		Core.inRange(hsv, new Scalar(74, 92, 93), new Scalar(99, 127, 240), filteredgron);
		
		Highgui.imwrite("grøn.jpg", filteredgron);
		Highgui.imwrite("lilla.jpg", filteredlilla);
		
		Imgproc.GaussianBlur(filteredlilla, filteredlilla, new Size(27,27), 4, 4);
		Imgproc.HoughCircles(filteredlilla, circles_l, Imgproc.CV_HOUGH_GRADIENT, 2, 255, 25, 25, 5, 15);

		Imgproc.GaussianBlur(filteredgron, filteredgron, new Size(27,27), 4, 4);
		Highgui.imwrite("fejl.jpg", filteredgron);
		Imgproc.HoughCircles(filteredgron, circles_g, Imgproc.CV_HOUGH_GRADIENT, 2, 255, 25, 25, 5, 15);
		
		System.out.println("Found "+circles_g.cols() + " grøn circles.");
		System.out.println("Found "+circles_l.cols() + " lilla circles.");

		for (int i = 0; i < circles_g.cols(); i++) {
			double[] circle = circles_g.get(0,i);
			list.add(new Ball((int)circle[0],(int)circle[1]));
			Point center = new Point((int)circle[0], (int)circle[1]);
			
			int radius =  (int) circle[2];
			// circle center
			Core.circle( src, center, 3, new Scalar(0,255,0), -1, 8, 0 );
			// circle outline
			Core.circle( src, center, radius, new Scalar(0,0,255), 3, 8, 0 );
		}
		
		for (int i = 0; i < circles_l.cols(); i++) {
			double[] circle = circles_l.get(0,i);
			list.add(new Ball((int)circle[0],(int)circle[1]));
			Point center = new Point((int)circle[0], (int)circle[1]);
			
			int radius =  (int) circle[2];
			// circle center
			Core.circle( src, center, 3, new Scalar(0,255,0), -1, 8, 0 );
			// circle outline
			Core.circle( src, center, radius, new Scalar(0,0,255), 3, 8, 0 );
		}

		if(printCircleCoordinates){
			for (Ball b: list){
				System.out.println(b);
			}
		}
		
		Highgui.imwrite("picture12new.jpg", src);	
		return null;
	}
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		try {
			new RobotFinder().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
