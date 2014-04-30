package pc;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import boldogrobot.Robot;

public class RobotFinder {

	public Robot run() throws Exception{
		boolean loadImageFromFile = true;
		Mat src_gray = new Mat();
		Mat smooth = new Mat();
		Mat circles = new Mat();
		Mat src = new Mat(); 
		
		if(loadImageFromFile){
			src = Highgui.imread("robot.jpg",1);
		} else {
			// load frame (image) from webcam
			VideoCapture webSource = new VideoCapture(1);
			Thread.sleep(2000);
			webSource.retrieve(src);
		}
		
		Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(src_gray, smooth, new Size(23,23), 4, 4);
		Imgproc.HoughCircles(smooth, circles, Imgproc.CV_HOUGH_GRADIENT, 2, 90, 40, 40, 30, 60);
		
		
		
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
