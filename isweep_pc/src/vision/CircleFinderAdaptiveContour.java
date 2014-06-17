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

class CircleFinderAdaptiveContour {

	Mat src;

	public List<Ball> run() throws Exception{
		boolean loadImageFromFile = false;
		boolean printCircleCoordinates = true;
		List<Ball> list = new ArrayList<Ball>();
		Mat src_gray = new Mat();
		Mat smooth = new Mat();
		Mat circles = new Mat();

		if(loadImageFromFile){
			//			src = Highgui.imread("Picture 12.jpg",1);
			src = Highgui.imread("Picture 12.jpg", Imgproc.COLOR_BGR2GRAY);
		} else {
			// load frame (image) from webcam
			VideoCapture webSource = new VideoCapture(0);
			
			src = new Mat();
			webSource.read(src);
			
			System.out.println(webSource.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 1080));
			System.out.println(webSource.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1920));
			Thread.sleep(2000);
			webSource.retrieve(src);
			System.out.println(src.cols());
			System.out.println(src.rows());
		}
		
		Mat imageHSV = new Mat(src.size(), Core.DEPTH_MASK_8U);
		Mat imageBlurr = new Mat(src.size(), Core.DEPTH_MASK_8U);
		Mat imageA = new Mat(src.size(), Core.DEPTH_MASK_ALL);
		Imgproc.cvtColor(src, imageHSV, Imgproc.COLOR_BGR2GRAY);
		
		Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
		//		    Imgproc.adaptiveThreshold(src_gray, bw, 255, 0, 0, 51, -25); 
		//		    Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
		Imgproc.adaptiveThreshold(imageBlurr, imageA, 255, 0, 0, 51, -25); 

		Highgui.imwrite("ext.jpg",imageBlurr);
		Highgui.imwrite("imageA.jpg",imageA);
		
//		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,4));
//		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,4));
//
//		Imgproc.erode(imageA, imageA, erodeElement);
//		Imgproc.dilate(imageA, imageA, dilateElement);
//
//		Imgproc.dilate(imageA, imageA, dilateElement);
//		Imgproc.erode(imageA, imageA, erodeElement);

		Highgui.imwrite("clean.jpg", imageA);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
		Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
		//		    Imgproc.drawContours(imageBlurr, contours, 1, new Scalar(0,0,255));
		//		    Imgproc.drawContours(src, contours, -1, new Scalar(0, 213, 16), 2);
		for(int i=0; i< contours.size();i++){
			System.out.println(Imgproc.contourArea(contours.get(i)));
			if (Imgproc.contourArea(contours.get(i)) > 314 && Imgproc.contourArea(contours.get(i)) < 530){
				Rect rect = Imgproc.boundingRect(contours.get(i));
				System.out.println(rect.x+rect.width/2+", "+rect.y+rect.height/2);
				//		            Rect rect = Imgproc.boundingRect(contours.get(i));
				//		            System.out.println(rect.height);
				//		            if (rect.height > 28){
				//		            //System.out.println(rect.x +","+rect.y+","+rect.height+","+rect.width);
				//		            Core.rectangle(image, new Point(rect.x,rect.height), new Point(rect.y,rect.width),new Scalar(0,0,255));
				//		        	System.out.println("Found 1");
				Core.circle( src, new Point(rect.x+rect.width/2, rect.y+rect.height/2), (int)(Math.sqrt(Math.pow(rect.width/2,2)+Math.pow(rect.height/2, 2))), new Scalar(0,0,255), 3, 8, 0 );
			}
		}

		//		System.out.println("Found "+circles.cols() + " circles.");
		//		for (int i = 0; i < circles.cols(); i++) {
		//			double[] circle = circles.get(0,i);
		////			if (src.get((int)circle[1], (int)circle[0])[2]>140){
		//				list.add(new Ball((int)circle[0],(int)circle[1]));
		//				Point center = new Point((int)circle[0], (int)circle[1]);
		//
		//				int radius =  (int) circle[2];
		//				// circle center
		//				Core.circle( src, center, 3, new Scalar(0,255,0), -1, 8, 0 );
		//				// circle outline
		//				Core.circle( src, center, radius, new Scalar(0,0,255), 3, 8, 0 );
		////			}
		//		}
		//
		//		if(printCircleCoordinates){
		//			for (Ball b: list){
		//				System.out.println(b);
		//			}
		//		}
		//		
		//		System.out.println("Found "+list.size() + " white circles.");


		Highgui.imwrite("out.jpg", src);

		return list;
	}

	public CircleFinderAdaptiveContour(Mat image) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		this.src = image;
	}

	public CircleFinderAdaptiveContour(){
	}

	public static void main(String[] args) throws Exception {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		new CircleFinderAdaptiveContour().run();
	}

}
