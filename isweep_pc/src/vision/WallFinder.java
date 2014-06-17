package vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import boldogrobot.Placeable;

public class WallFinder {
	
	Mat src;

//	Placeable computeIntersect(Line a, Line b){
//		int x1=a.getEndPoint1().getX(), y1=a.getEndPoint2(), x
//	}
	
	public List<Placeable> run() throws Exception{
		boolean loadImageFromFile = true; 
		List<Placeable> list = new ArrayList<Placeable>();
		
		Mat src_gray =  new Mat();
		Mat lines =  new Mat();
		Mat canny_out = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		
		
		if(loadImageFromFile){
//			src = Highgui.imread("ball.jpg",1);
			src = Highgui.imread("picture12new.jpg",1);
		} else {
			// load frame (image) from webcam
			VideoCapture webSource = new VideoCapture(1);
			Thread.sleep(2000);
			webSource.retrieve(src);
		}
		
		Mat srcH = new Mat();
		src.convertTo(srcH, -1, 2.0, 0);
		Highgui.imwrite("contrast.jpg", srcH);
		
		Mat img_hist_equalized = new Mat();
		List<Mat> channels = new ArrayList<Mat>();
		
		Imgproc.cvtColor(srcH, img_hist_equalized, Imgproc.COLOR_BGR2YCrCb);
		Core.split(img_hist_equalized, channels);
		
		Imgproc.equalizeHist(channels.get(0), channels.get(0));
		Core.merge(channels, img_hist_equalized);
		Imgproc.cvtColor(img_hist_equalized, img_hist_equalized, Imgproc.COLOR_YCrCb2BGR);
		
		Highgui.imwrite("equalized.jpg", img_hist_equalized);
		Imgproc.cvtColor(img_hist_equalized, src_gray, Imgproc.COLOR_BGR2GRAY);
		
		Mat hsv = new Mat();
		Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);
		
		Mat filtered = new Mat();
//		Core.inRange(hsv, new Scalar(0, 36, 30), new Scalar(11, 255, 255), filtered);
		Core.inRange(hsv, new Scalar(0, 39, 0), new Scalar(193, 255, 255), filtered);
		Highgui.imwrite("filtered.jpg", filtered);
		
		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12,12));
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12,12));

		Imgproc.erode(filtered, filtered, erodeElement);
		
		Imgproc.dilate(filtered, filtered, dilateElement);
//		Imgproc.erode(filtered, filtered, erodeElement);

		Highgui.imwrite("filtered2.jpg", filtered);
		
		Imgproc.findContours(filtered, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
		Imgproc.drawContours(src, contours, -1, new Scalar(0, 213, 16), 2);
		
//		Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
//		
//		Mat imageA = new Mat();
//		Imgproc.adaptiveThreshold(src_gray, imageA, 255, 0, 0, 51, -25);
//		Highgui.imwrite("a.jpg", imageA);
		
//		Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
//		Imgproc.blur(src_gray, src_gray, new Size(3,3));
//		Imgproc.Canny(src_gray, canny_out, 45, 60);
//		Imgproc.findContours(canny_out, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));
//		
//		System.out.println(contours.size());
//		for (int i = 0; i<contours.size();i++){
//			Imgproc.drawContours(src, contours, i, new Scalar(150, 150, 200),2);
//		}
		
//		Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
//		Imgproc.blur(src_gray, src_gray, new Size(3,3));
//		Imgproc.HoughLinesP(src_gray, lines, 1, Math.PI/180, 70, 30, 10);
//		
//		System.out.println(lines.rows());
//		System.out.println(lines.cols());
		
//		Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
//		Mat bw =  new Mat();
//		Imgproc.Canny(src_gray, bw, 18, 54);
//		Imgproc.findContours(bw.clone(), contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//		
//		MatOfPoint2f approx = new MatOfPoint2f();
//		Mat dst = src.clone();
//		
//		for (int i = 0; i<contours.size(); i++){
//			Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i)), approx, Imgproc.arcLength(new MatOfPoint2f(contours.get(i)), true)*0.02,true);
//			System.out.println(approx.cols());
//		}
		
//		Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
////		Imgproc.blur(src_gray, src_gray, new Size(3,3));
//		Imgproc.Canny(src_gray, canny_out, 18, 54);
//		Highgui.imwrite("canny.jpg", canny_out);
		
		Imgproc.HoughLinesP(src_gray, lines, 1, Math.PI/180, 30, 800, 200);
		
		System.out.println(lines.cols());
		
		 for (int x = 0; x < lines.cols(); x++) 
		    {
		          double[] vec = lines.get(0, x);
		          double x1 = vec[0], 
		                 y1 = vec[1],
		                 x2 = vec[2],
		                 y2 = vec[3];
		          Point start = new Point(x1, y1);
		          Point end = new Point(x2, y2);

		          Core.line(src, start, end, new Scalar(255,0,0), 3);

		    }


		
		Highgui.imwrite("out3.jpg", src);
		
		
		
		
		return list; 
	}
	
	public WallFinder(Mat image) { 
		
	}
	
	public WallFinder(){
		
	}
	
	public static void main(String[] args) throws Exception {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		new WallFinder().run();
	}

	

}
