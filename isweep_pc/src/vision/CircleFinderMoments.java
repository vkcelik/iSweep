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
import org.opencv.imgproc.Moments;

import boldogrobot.Ball;

class CircleFinderMoments {

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

		Mat imgHSV = new Mat();

		Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2HSV);

		Mat imgThresholded = new Mat();

		Core.inRange(src_gray, new Scalar(0, 0, 178), new Scalar(0, 0, 255), imgThresholded);

		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));

		Imgproc.erode(imgThresholded, imgThresholded, erodeElement);
		Imgproc.dilate(imgThresholded, imgThresholded, dilateElement);

		Imgproc.dilate(imgThresholded, imgThresholded, dilateElement);
		Imgproc.erode(imgThresholded, imgThresholded, erodeElement);

		Moments oMoments = Imgproc.moments(imgThresholded);

		double dM01 = oMoments.get_m01();
		double dM10 = oMoments.get_m10();
		double dArea = oMoments.get_m00();

		if (dArea > 10000){
			//calculate the position of the ball
			double posX = dM10 / dArea;
			double posY = dM01 / dArea;        
		System.out.println(posX+","+posY);	        
		}

		Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(src_gray, src_gray);
		Highgui.imwrite("outgray.jpg", src_gray);
		Imgproc.GaussianBlur(src_gray, smooth, new Size(11,11),4, 4);
		Highgui.imwrite("blur.jpg", smooth);
		Imgproc.HoughCircles(smooth, circles, Imgproc.CV_HOUGH_GRADIENT, 2, 20, 81, 29, 10, 13);

		System.out.println("Found "+circles.cols() + " circles.");
		for (int i = 0; i < circles.cols(); i++) {
			double[] circle = circles.get(0,i);
			if (src.get((int)circle[1], (int)circle[0])[2]>140){
				list.add(new Ball(circle[0],circle[1]));
				Point center = new Point(circle[0], circle[1]);

				double radius =  circle[2];
				// circle center
				Core.circle( src, center, 3, new Scalar(0,255,0), -1, 8, 0 );
				// circle outline
				Core.circle( src, center, (int)radius, new Scalar(0,0,255), 3, 8, 0 );
			}
		}

		if(printCircleCoordinates){
			for (Ball b: list){
				System.out.println(b);
			}
		}

		System.out.println("Found "+list.size() + " white circles.");


		Highgui.imwrite("out.jpg", src);

		return list;
	}

	public CircleFinderMoments(Mat image) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		this.src = image;
	}

	public CircleFinderMoments(){
	}

	public static void main(String[] args) throws Exception {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		new CircleFinderMoments().run();
	}

}
