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

public class CircleFinder {

	Mat src = new Mat();
	VideoCapture vc;

	public List<Placeable> run() throws Exception{
		boolean loadImageFromFile = true;
		boolean printCircleCoordinates = true;
		List<Placeable> list = new ArrayList<Placeable>();
		Mat src_gray = new Mat();
		Mat smooth = new Mat();
		Mat circles = new Mat();

//		if(loadImageFromFile){
//			src = Highgui.imread("555.jpg",1);
//			//			src = Highgui.imread("Picture 12.jpg", Imgproc.COLOR_BGR2GRAY);
//		} else {
//			// load frame (image) from webcam
//			VideoCapture webSource = new VideoCapture(0);
//
//			src = new Mat();
//			webSource.read(src);
//
//			System.out.println(webSource.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 1080));
//			System.out.println(webSource.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1920));
//			Thread.sleep(2000);
//			webSource.retrieve(src);
//			System.out.println(src.cols());
//			System.out.println(src.rows());
//			Highgui.imwrite("original.jpg",src);
//		}

		vc.read(src);
		Mat hsv = new Mat();

		Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);
		Mat filtered = new Mat();
//		Core.inRange(hsv, new Scalar(0, 0, 182), new Scalar(255,108,255), filtered);

		Core.inRange(hsv, new Scalar(0, 0, 171), new Scalar(255,58,255), filtered);
		
		Highgui.imwrite("filter.jpg",filtered);


		Mat gray = new Mat(src.size(), Core.DEPTH_MASK_8U);
		Mat imageBlurr = new Mat(src.size(), Core.DEPTH_MASK_8U);
		Mat imageA = new Mat(src.size(), Core.DEPTH_MASK_ALL);

		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		Highgui.imwrite("gray.jpg",gray);

		Imgproc.GaussianBlur(gray, imageBlurr, new Size(5,5), 4);
		Highgui.imwrite("ext.jpg",imageBlurr);
		//		    Imgproc.adaptiveThreshold(src_gray, bw, 255, 0, 0, 51, -25); 
		//		    Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
		Imgproc.adaptiveThreshold(imageBlurr, imageA, 255, 0, 0, 51, -25);
		Highgui.imwrite("imageA.jpg",imageA);
		
//		Imgproc.dilate(imageA, imageA, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(8,8)));
//		Highgui.imwrite("expand.jpg", imageA);
		
		Imgproc.dilate(imageA, imageA, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(13,13)));
		
		Imgproc.erode(imageA, imageA, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(13,13)));
		Imgproc.erode(imageA, imageA, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(8,8)));
//		Imgproc.dilate(imageA, imageA, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9,9)));
//
//		Imgproc.dilate(imageA, imageA, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5)));
//		Highgui.imwrite("dilate.jpg", imageA);
//		
//		Imgproc.erode(imageA, imageA, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15,15)));
		Highgui.imwrite("erode.jpg", imageA);
		

//		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9,9));
//		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9,9));
//
//		Imgproc.erode(imageA, imageA, erodeElement);
//		Imgproc.dilate(imageA, imageA, dilateElement);
//
//		Imgproc.dilate(imageA, imageA, dilateElement);
//		Imgproc.erode(imageA, imageA, erodeElement);
//		Highgui.imwrite("clean.jpg", imageA);

		
		


		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
		Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
		//		    Imgproc.drawContours(imageBlurr, contours, 1, new Scalar(0,0,255));
//		Imgproc.drawContours(src, contours, -1, new Scalar(0, 213, 16), 2);

		double contourArea;
		MatOfPoint contour;
		double h,w,x,y;
		double centerX, centerY;
		double boundingCircleRadius;
		
		for(int i=0; i< contours.size();i++){
			contour = contours.get(i);
			contourArea = Imgproc.contourArea(contour);
			System.out.print(contourArea);
//			if (contourArea > 600 && contourArea < 720){
			if (contourArea > 150 && contourArea < 300){
				System.out.print(" OK");
				Rect rect = Imgproc.boundingRect(contour);
				//				System.out.println(rect.x+rect.width/2+", "+rect.y+rect.height/2);
				h = rect.height;
				w = rect.width;
				x = rect.x;
				y = rect.y;
				centerX = x+w/2;
				centerY = y+h/2;
				boundingCircleRadius = Math.sqrt(Math.pow(w/2,2)+Math.pow(h/2, 2));
				Core.circle( src, new Point(centerX, centerY), 3, new Scalar(0,255,0), -1, 8, 0 );
				Core.circle( src, new Point(centerX, centerY), (int)boundingCircleRadius, new Scalar(0,0,255), 3, 8, 0 );
				list.add(new Placeable(centerX, centerY));
			}
			System.out.println();
		}

		Highgui.imwrite("out.jpg", src);

		return list;
	}

	public CircleFinder(Mat image) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		this.src = image;
	}

	public CircleFinder(){
	}

	public static void main(String[] args) throws Exception {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		new CircleFinder().run();
	}

	public void setVc(VideoCapture vc) {
		this.vc = vc;
	}

}
