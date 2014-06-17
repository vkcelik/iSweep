package vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import boldogrobot.Ball;

class CircleFinderContour {

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

		Core.inRange(src_gray, new Scalar(67, 0, 175), new Scalar(137, 255, 255), imgThresholded);

		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));

		Imgproc.erode(imgThresholded, imgThresholded, erodeElement);
		Imgproc.dilate(imgThresholded, imgThresholded, dilateElement);

		Imgproc.dilate(imgThresholded, imgThresholded, dilateElement);
		Imgproc.erode(imgThresholded, imgThresholded, erodeElement);

		Highgui.imwrite("clean.jpg", imgThresholded);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
//		Imgproc.findContours(imgThresholded, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		Imgproc.findContours(imgThresholded, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//		Imgproc.drawContours(src, contours, -1, new Scalar(0, 213, 16), 2);

		List<Moments> mu = new ArrayList<Moments>(contours.size());
		for (int i = 0; i < contours.size(); i++) {
			double area = Imgproc.contourArea(contours.get(i));
			System.out.println(area);
			if (area>130 && area<532){
				mu.add(i, Imgproc.moments(contours.get(i), false));
				Moments p = mu.get(i);
				int x = (int) (p.get_m10() / p.get_m00());
				int y = (int) (p.get_m01() / p.get_m00());
				Core.circle(src, new Point(x, y), 4, new Scalar(255,49,0,255));
			}
		}

		//		Moments oMoments = Imgproc.moments(imgThresholded);
		//
		//		double dM01 = oMoments.get_m01();
		//		double dM10 = oMoments.get_m10();
		//		double dArea = oMoments.get_m00();
		//
		//		if (dArea > 10000){
		//			//calculate the position of the ball
		//			   int posX = (int) (dM10 / dArea);
		//			   int posY = (int) (dM01 / dArea);        
		//			   System.out.println(posX+","+posY);	        
		//		}

		System.out.println("Found "+circles.cols() + " circles.");
		for (int i = 0; i < circles.cols(); i++) {
			double[] circle = circles.get(0,i);
			if (src.get((int)circle[1], (int)circle[0])[2]>140){
				list.add(new Ball((int)circle[0],(int)circle[1]));
				Point center = new Point((int)circle[0], (int)circle[1]);

				int radius =  (int) circle[2];
				// circle center
				Core.circle( src, center, 3, new Scalar(0,255,0), -1, 8, 0 );
				// circle outline
				Core.circle( src, center, radius, new Scalar(0,0,255), 3, 8, 0 );
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

	public CircleFinderContour(Mat image) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		this.src = image;
	}

	public CircleFinderContour(){
	}

	public static void main(String[] args) throws Exception {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		new CircleFinderContour().run();
	}

}
