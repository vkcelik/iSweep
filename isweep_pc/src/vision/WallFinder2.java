package vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import boldogrobot.Ball;
import boldogrobot.Placeable;

public class WallFinder2 {
	
	Mat src;
	
	public List<Placeable> run() throws Exception{
		boolean loadImageFromFile = true; 
		List<Placeable> list = new ArrayList<Placeable>();
		
		if(loadImageFromFile){
			src = Highgui.imread("picture12new.jpg",1);
		} else {
			// load frame (image) from webcam
			VideoCapture webSource = new VideoCapture(1);
			Thread.sleep(2000);
			webSource.retrieve(src);
		}
		
		Mat hsv = new Mat();
		Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);
		
		Mat filtered = new Mat();
		Core.inRange(hsv, new Scalar(0, 73, 0), new Scalar(178, 255, 255), filtered);
		Highgui.imwrite("bin.jpg", filtered);

		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2));
		
		Imgproc.dilate(filtered, filtered, dilateElement);
		Imgproc.dilate(filtered, filtered, dilateElement);
		
		Highgui.imwrite("bin0.jpg", filtered);
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(filtered, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		
		Imgproc.drawContours(src, contours, -1, new Scalar(0, 213, 16), 2);
		

		double contourArea;
		MatOfPoint contour;
		int h,w,x,y;
		int centerX, centerY;
		int boundingCircleRadius;
		
		List<MatOfPoint> big = new ArrayList<MatOfPoint>();
		
		for(int i=0; i< contours.size();i++){
			contour = contours.get(i);
			contourArea = Imgproc.contourArea(contour);
			System.out.print(contourArea);
			if (contourArea > 500000){
				System.out.print(" OK");
				Rect rect = Imgproc.boundingRect(contour);
				//				System.out.println(rect.x+rect.width/2+", "+rect.y+rect.height/2);
				h = rect.height;
				w = rect.width;
				x = rect.x;
				y = rect.y;
				centerX = x+w/2;
				centerY = y+h/2;
				boundingCircleRadius = (int)(Math.sqrt(Math.pow(w/2,2)+Math.pow(h/2, 2)));
//				Core.circle( src, new Point(centerX, centerY), 3, new Scalar(0,255,0), -1, 8, 0 );
//				Core.circle( src, new Point(centerX, centerY), boundingCircleRadius, new Scalar(0,0,255), 3, 8, 0 );
				list.add(new Ball(centerX, centerY));
				big.add(contour);
			}
			System.out.println();
		}
		
//		Imgproc.drawContours(src, big, -1, new Scalar(0, 213, 16), 2);
		
		Highgui.imwrite("out4.jpg", src);
		return list; 
	}
	
	public WallFinder2(Mat image) { 
	}
	
	public WallFinder2(){
	}
	
	public static void main(String[] args) throws Exception {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		new WallFinder2().run();
	}
}
