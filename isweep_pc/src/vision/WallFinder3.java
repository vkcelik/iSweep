package vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import boldogrobot.Ball;
import boldogrobot.Placeable;

public class WallFinder3 {
	
	Mat src;
	
	public List<Placeable> run(String outFileName) throws Exception{
		boolean loadImageFromFile = true; 
		List<Placeable> list = new ArrayList<Placeable>();
		
		if(loadImageFromFile){
			if(src.empty()) {src = Highgui.imread("Picture 20.jpg",1);}
		} else {
			// load frame (image) from webcam
			VideoCapture webSource = new VideoCapture(1);
			Thread.sleep(2000);
			webSource.retrieve(src);
		}
		
		Mat hsv = new Mat();
		Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);
		
		Mat filtered = new Mat();
//		Core.inRange(hsv, new Scalar(0, 73, 0), new Scalar(178, 255, 255), filtered);
//		Core.inRange(hsv, new Scalar(0, 40, 0), new Scalar(10, 255, 255), filtered);
//		Core.inRange(hsv, new Scalar(0, 132, 62), new Scalar(255, 255, 173), filtered);
//		Core.inRange(hsv, new Scalar(0, 100, 50), new Scalar(255, 255, 255), filtered);
//		Core.inRange(hsv, new Scalar(0, 91, 51), new Scalar(255, 255, 255), filtered);
//		Core.inRange(hsv, new Scalar(0, 83, 93), new Scalar(255, 255, 255), filtered);
//		Core.inRange(hsv, new Scalar(0, 83, 0), new Scalar(255, 255, 255), filtered);
//		Core.inRange(hsv, new Scalar(0, 110, 0), new Scalar(255, 255, 255), filtered);
		Core.inRange(hsv, new Scalar(0, 116, 0), new Scalar(255, 255, 255), filtered);
		Highgui.imwrite("bin"+outFileName+".jpg", filtered);

		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2));
//		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,4));
//		Imgproc.erode(filtered, filtered, erodeElement);
//		Imgproc.dilate(filtered, filtered, erodeElement);
		
		Imgproc.dilate(filtered, filtered, dilateElement);
		Imgproc.dilate(filtered, filtered, dilateElement);
//		
		Highgui.imwrite("bin"+outFileName+"1.jpg", filtered);
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(filtered, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		
//		Imgproc.drawContours(src, contours, -1, new Scalar(0, 213, 16), 2);
		

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
//			if (contourArea > 500000){
			if (contourArea > 95000){
				if (contourArea < areaMin){
					areaMin = contourArea;
					innerContour = i;
				}
				
//				contour.convertTo(mMOP2f1, CvType.CV_32FC2);
//	            Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 25, true);
//	            // convert back to MatOfPoint and put it back in the list
//	            mMOP2f2.convertTo(contour, CvType.CV_32S);
//	            System.out.println("rows: "+contours.get(i).rows());
//	            Converters.Mat_to_vector_Point2f(contour, pts);
//	            Imgproc.drawContours(src, contours, i, new Scalar(0, 213, 16), 2);
//	            Core.line(src, pts.get(0), pts.get(2), new Scalar(0, 213, 16), 2);
//	            Core.line(src, pts.get(1), pts.get(3), new Scalar(0, 213, 16), 2);
	            
				big.add(contour);
			}
//			System.out.println();
		}
		System.out.println(innerContour);
		System.out.println(Imgproc.contourArea(contours.get(innerContour)));
		
		contour = contours.get(innerContour);
		
		contour.convertTo(mMOP2f1, CvType.CV_32FC2);
//		Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 25, true);
//        Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 35, true);
		Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 600, true);
        // convert back to MatOfPoint and put it back in the list
        mMOP2f2.convertTo(contour, CvType.CV_32S);
        System.out.println("rows: "+contours.get(innerContour).rows());
        Converters.Mat_to_vector_Point2f(contour, pts);
        Imgproc.drawContours(src, contours, innerContour, new Scalar(0, 213, 16), 2);
        Core.line(src, pts.get(0), pts.get(1), new Scalar(0, 213, 16), 2);
        Core.line(src, pts.get(1), pts.get(2), new Scalar(0, 213, 150), 2);
        Core.line(src, pts.get(2), pts.get(3), new Scalar(0, 213, 255), 2);
        Core.line(src, pts.get(3), pts.get(0), new Scalar(150, 150, 16), 2);
        
//        Point topLeft = pts.get(3);
//        Point topRight = pts.get(0);
//        Point bottomLeft = pts.get(2);
//        Point bottomRight = pts.get(1);
//        list.add(new Placeable(topLeft.x, topLeft.y));
//        Core.putText(src, "TL", new Point(topLeft.x, topLeft.y), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 213, 16), 2);
//        list.add(new Placeable(topRight.x, topRight.y)); 
//        Core.putText(src, "TR", new Point(topRight.x, topRight.y), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 213, 16), 2);
//        list.add(new Placeable(bottomLeft.x, bottomLeft.y));
//        Core.putText(src, "BL", new Point(bottomLeft.x, bottomLeft.y), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 213, 16), 2);
//        list.add(new Placeable(bottomRight.x, bottomRight.y));
//        Core.putText(src, "BR", new Point(bottomRight.x, bottomRight.y), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 213, 16), 2);

        
        
//		contour = contours.get(innerContour);
//		System.out.println(contour.cols());
//		System.out.println(contour.rows());
//		MatOfPoint2f cnt = new MatOfPoint2f(contour.toArray());
//		MatOfPoint2f poly = new MatOfPoint2f();
//		Imgproc.approxPolyDP(cnt, poly ,0.6*Imgproc.arcLength(cnt, true), true);
//		List<MatOfPoint> idk = new ArrayList<MatOfPoint>();
//		idk.add(contour);
//		Imgproc.drawContours(src, idk, -1, new Scalar(150, 213, 16), 2);
//		
//		Placeable topLeft;
//		Placeable topRight;
//		Placeable bottomLeft;
//		Placeable bottomRight;
//		
//		for (int i = 0; i<1;i++){
//			Mat points = contour.col(i);
//			System.out.println(points.rows());
//			System.out.println(points.cols());
//			double[] point = points.get(row, col);
			
//		}
		
//		Imgproc.drawContours(src, big, -1, new Scalar(0, 213, 16), 2);
		
		Highgui.imwrite(outFileName+".jpg", src);
		return list; 
	}
	
	public WallFinder3(Mat image) { 
		src = image;
	}
	
	public WallFinder3(){
		
	}
	
	public static void main(String[] args) throws Exception {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		
		List<Mat> images = new ArrayList<Mat>();
		
		for(int i=13;i<21;i++){
			images.add(new Mat());
			String name = "Picture " +i+".jpg";
			String out = "" +i+"_";
			images.set(i-13, Highgui.imread(name,1));
			new WallFinder3(images.get(i-13)).run(out);
		}
		
		
	}
}
