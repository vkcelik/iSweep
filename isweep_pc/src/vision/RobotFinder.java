package vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import boldogrobot.Ball;
import boldogrobot.Placeable;
import boldogrobot.Robot;

public class RobotFinder {
	
	Mat src;

	public List<Placeable> run() throws Exception{
		boolean loadImageFromFile = true;
		boolean printCircleCoordinates = true;
		List<Placeable> list = new ArrayList<Placeable>();
		Mat src_gray = new Mat();
		Mat smooth = new Mat();
		Mat circles_g = new Mat();
		Mat circles_l = new Mat();
		Mat filteredlilla = new Mat();
		Mat filteredgron= new Mat();
		Mat hsv = new Mat();
		
//		if(loadImageFromFile){
//			src = Highgui.imread("555.jpg",1);
//		} else {
//			// load frame (image) from webcam
//			
//			VideoCapture vc = new VideoCapture(1);
//			if(vc.isOpened()){
//				System.out.println("Cam opened");
//			} else {
//				System.out.println("Cam not opened");
//			}
//			src = new Mat();
//			vc.read(src);
//
//			System.out.println(vc.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1920.0));
//			System.out.println(vc.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 1080.0));
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			vc.read(src);
//		}
		
		
	
		Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);
//		Core.inRange(hsv, new Scalar(30, 26, 0), new Scalar(90, 212, 255), filteredgron);
		Core.inRange(hsv, new Scalar(66, 15, 96), new Scalar(81, 255, 255), filteredgron);
//		Core.inRange(hsv, new Scalar(101, 103, 165), new Scalar(199, 239, 235), filteredlilla);
//		Core.inRange(hsv, new Scalar(27,134,169), new Scalar(255,255,255), filteredlilla);
		Core.inRange(hsv, new Scalar(162,21,81), new Scalar(251,255,255), filteredlilla);
		
		
//		Imgproc.GaussianBlur(filteredlilla, filteredlilla, new Size(27,27), 4, 4);

//		Imgproc.GaussianBlur(filteredgron, filteredgron, new Size(27,27), 4, 4);
		
		List<MatOfPoint> contoursgron = new ArrayList<MatOfPoint>();
		List<MatOfPoint> contourslilla = new ArrayList<MatOfPoint>();
		
		
		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7,7));
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7,7));

		Imgproc.erode(filteredgron, filteredgron, erodeElement);
		Imgproc.dilate(filteredgron, filteredgron, dilateElement);

		Imgproc.dilate(filteredgron, filteredgron, dilateElement);
		Imgproc.erode(filteredgron, filteredgron, erodeElement);
		
		Highgui.imwrite("gr�n.jpg", filteredgron);
		Highgui.imwrite("lilla.jpg", filteredlilla);
		
		Imgproc.findContours(filteredgron, contoursgron, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		Imgproc.drawContours(src, contoursgron, -1, new Scalar(0,213,16),2);
		for(int i=0; i< contoursgron.size(); i++){
			System.out.println(Imgproc.contourArea(contoursgron.get(i)));
			
			if(Imgproc.contourArea(contoursgron.get(i))>350 && Imgproc.contourArea(contoursgron.get(i))< 550){
				System.out.println("Found 1 green");
				Rect rect = Imgproc.boundingRect(contoursgron.get(i));
				list.add(new Placeable(rect.x+rect.width/2, rect.y+rect.height/2));
//				 Core.circle(src, new Point(rect.x+rect.width/2, rect.y+rect.height/2), (int)(Math.sqrt(Math.pow(rect.width/2,2)+Math.pow(rect.height/2, 2))), new Scalar(0,0,255), 3, 8, 0 );
				
			}
			
		}
		
		Imgproc.erode(filteredlilla, filteredlilla, erodeElement);
		Imgproc.dilate(filteredlilla, filteredlilla, dilateElement);

		Imgproc.dilate(filteredlilla, filteredlilla, dilateElement);
		Imgproc.erode(filteredlilla, filteredlilla, erodeElement);
		
		Imgproc.findContours(filteredlilla, contourslilla, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		Imgproc.drawContours(src, contourslilla, -1, new Scalar(0,213,16),2);
		for(int i=0; i< contourslilla.size(); i++){
			System.out.println(Imgproc.contourArea(contourslilla.get(i)));
			
			if(Imgproc.contourArea(contourslilla.get(i))>350 && Imgproc.contourArea(contourslilla.get(i))< 600){
				System.out.println("Found 1 purple");
				Rect rect = Imgproc.boundingRect(contourslilla.get(i));
				list.add(new Placeable(rect.x+rect.width/2, rect.y+rect.height/2));
//				Core.circle(src, new Point(rect.x+rect.width/2, rect.y+rect.height/2), (int)(Math.sqrt(Math.pow(rect.width/2,2)+Math.pow(rect.height/2, 2))), new Scalar(0,0,255), 3, 8, 0 );
				
			}
			
		}
		
		
		
		
		Highgui.imwrite("robot.jpg", src);	
		return list;
	}
	
	
	
	public RobotFinder(Mat src) {
		super();
		this.src = src;
	}



	public RobotFinder() {
		super();
	}

	public void setImage(Mat frame){
		src= frame;
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
