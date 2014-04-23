package pc;

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

class CircleFinder {
	public List<Ball> run() throws Exception{
		boolean loadImageFromFile = true;
		boolean printCircleCoordinates = true;
		List<Ball> list = new ArrayList<Ball>();
		Mat src_gray = new Mat();
		Mat smooth = new Mat();
		Mat circles = new Mat();
		Mat src = new Mat(); 
		
		if(loadImageFromFile){
			src = Highgui.imread("ball.jpg",1);
		} else {
			// load frame (image) from webcam
			VideoCapture webSource = new VideoCapture(1);
			Thread.sleep(2000);
			webSource.retrieve(src);
		}

		Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(src_gray, smooth, new Size(23,23), 4, 4);
		Imgproc.HoughCircles(smooth, circles, Imgproc.CV_HOUGH_GRADIENT, 2, 90, 40, 40, 30, 60);

		System.out.println("Found "+circles.cols() + " circles.");

		for (int i = 0; i < circles.cols(); i++) {
			double[] circle = circles.get(0,i);
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
		
		Highgui.imwrite("out.jpg", src);
		
		return list;
	}

	public static void main(String[] args) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		try {
			new CircleFinder().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
