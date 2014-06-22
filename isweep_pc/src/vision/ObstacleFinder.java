package vision;

import java.util.ArrayList;
import java.util.List;

import navigation.PathFinder;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import boldogrobot.Placeable;

public class ObstacleFinder {


	Mat src = new Mat();
	VideoCapture vc;

	public Placeable run(){

		vc.read(src);
//		src = Highgui.imread("Picture 41.jpg",1);
		
		List<Placeable> list = new ArrayList<Placeable>();

		Mat srcH = new Mat();
		src.convertTo(srcH, -1, 1.5, 0);
		Highgui.imwrite("obstaclec.jpg", srcH);

		Mat hsv = new Mat();
		Imgproc.cvtColor(srcH, hsv, Imgproc.COLOR_BGR2HSV);

		Mat filtered = new Mat();
		Core.inRange(hsv, new Scalar(0, 0, 19), new Scalar(48, 255, 255), filtered);

		Highgui.imwrite("obstacle.jpg", filtered);

		//	Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,4));
		//	Imgproc.erode(filtered, filtered, erodeElement);
		//	Imgproc.dilate(filtered, filtered, erodeElement);

			Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2));
			Imgproc.dilate(filtered, filtered, dilateElement);
			Imgproc.dilate(filtered, filtered, dilateElement);
			Highgui.imwrite("obstaclef.jpg", filtered);
		//	
		//		Highgui.imwrite("bin"+outFileName+"1.jpg", filtered);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(filtered, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

			Imgproc.drawContours(srcH, contours, -1, new Scalar(0, 213, 16), 2);

		double contourArea;
		MatOfPoint contour;
		double h,w,x,y;
		double centerX, centerY;
		double boundingCircleRadius;
		List<MatOfPoint> big = new ArrayList<MatOfPoint>();
		double areaMin = 1920*1080;
		int innerContour = -1;

		MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
		MatOfPoint2f mMOP2f2 = new MatOfPoint2f();
		List<Point> pts = new ArrayList<Point>();

		for(int i=0; i< contours.size();i++){
			contour = contours.get(i);
			contourArea = Imgproc.contourArea(contour);
			System.out.print(contourArea+" ");
			//		if (contourArea > 500000){
			if (contourArea > 5000 && contourArea < 9000){
				System.out.print(" OK");
				innerContour = i;
				
//				Point[] points = new Point[4];
//				rrect.points(points);
//				for (int j = 0; i < 4; i++)
//				    Core.line(srcH, points[j], points[(j+1)%4], new Scalar(0,255,255));
//				Core.circle( srcH, new Point(rrect.center.x, rrect.center.y), 3, new Scalar(0,255,255), -1, 8, 0 );
			}
		}
		contour = contours.get(innerContour);
		contour.convertTo(mMOP2f1, CvType.CV_32FC2);
		RotatedRect rrect = Imgproc.minAreaRect(mMOP2f1);
		Core.circle( srcH, new Point(rrect.center.x, rrect.center.y), 5, new Scalar(0,255,255), -1, 8, 0 );
		Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 9, true);
        // convert back to MatOfPoint and put it back in the list
        mMOP2f2.convertTo(contour, CvType.CV_32S);
        Converters.Mat_to_vector_Point2f(contour, pts);
        Imgproc.drawContours(srcH, contours, innerContour, new Scalar(0, 255, 255), 2);
		
		Highgui.imwrite("obstaclecontours.jpg", srcH);
		src.release();
		srcH.release();
		filtered.release();
		hsv.release();
		return new Placeable(rrect.center.x, rrect.center.y);
	}

	public void setVc(VideoCapture vc){
		this.vc= vc;
	}

	public static void main(String[] args) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		ObstacleFinder of = new ObstacleFinder();

		VideoCapture vc =  new VideoCapture(0);
		if(vc.isOpened()){
			System.out.println("Cam opened");
		} else {
			System.out.println("Cam not opened");
		}

		of.setVc(vc);

//		src = new Mat();
//		vc.read(src);
//		System.out.println("frame: "+src);
//		System.out.println(vc.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1920.0));
//		System.out.println(vc.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 1080.0));
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		vc.read(src);
//		of.run();
	}
}

