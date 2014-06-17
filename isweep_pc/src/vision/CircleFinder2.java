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

class CircleFinder2 {
	
	Mat src;
	
	public List<Ball> run() throws Exception{
		boolean loadImageFromFile = true;
		boolean printCircleCoordinates = true;
		List<Ball> list = new ArrayList<Ball>();
		Mat src_gray = new Mat();
		Mat smooth = new Mat();
		Mat circles = new Mat();
		
		if(loadImageFromFile){
			src = Highgui.imread("Picture 10.jpg",1);
		} else {
			// load frame (image) from webcam
			VideoCapture webSource = new VideoCapture(1);
			Thread.sleep(2000);
			webSource.retrieve(src);
		}

		double start=  System.currentTimeMillis();
		Mat srcH = new Mat();
		src.convertTo(srcH, -1, 0.7, 0);
		Highgui.imwrite("contrast.jpg", srcH);
		
		Imgproc.cvtColor(srcH, src_gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(src_gray, src_gray);
		Highgui.imwrite("outgray.jpg", src_gray);
//		Imgproc.GaussianBlur(src_gray, smooth, new Size(11,11),4, 4);
		Core.inRange(src_gray, new Scalar(240), new Scalar(255), smooth);
//		Imgproc.bilateralFilter(src_gray, smooth, 5, 150, 150);
		Highgui.imwrite("blur.jpg", smooth);
		Imgproc.HoughCircles(smooth, circles, Imgproc.CV_HOUGH_GRADIENT, 2, 20, 40, 30, 10, 13);

		System.out.println(System.currentTimeMillis()-start);
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
	
	public CircleFinder2(Mat image) {
		this.src = image;
	}
	
	public CircleFinder2(){
	}
	
	public static void main(String[] args) throws Exception {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		new CircleFinder2().run();
	}

}
