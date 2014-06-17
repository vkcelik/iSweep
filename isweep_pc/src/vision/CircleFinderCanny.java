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

class CircleFinderCanny {

	Mat src;

	public List<Ball> run() throws Exception{
		boolean loadImageFromFile = false;
		boolean printCircleCoordinates = true;
		List<Ball> list = new ArrayList<Ball>();
		Mat src_gray = new Mat();
		Mat smooth = new Mat();
		Mat circles = new Mat();

		if(loadImageFromFile){
			src = Highgui.imread("555.jpg",1);
			//			src = Highgui.imread("Picture 12.jpg", Imgproc.COLOR_BGR2GRAY);
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
			Highgui.imwrite("original.jpg",src);
		}
		
		Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
		Mat dst = new Mat();
		Imgproc.blur(src_gray, dst, new Size(3, 3));

		Imgproc.Canny(dst, dst, 50, 300, 3,false);
		Highgui.imwrite("canny.jpg",dst);
		
//		Mat gray = new Mat(src.size(), Core.DEPTH_MASK_8U);
//		Mat imageBlurr = new Mat(src.size(), Core.DEPTH_MASK_8U);
//		Mat imageA = new Mat(src.size(), Core.DEPTH_MASK_ALL);
//
//		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
//		Highgui.imwrite("gray.jpg",gray);
//
//		Imgproc.GaussianBlur(gray, imageBlurr, new Size(5,5), 4);
//		//		    Imgproc.adaptiveThreshold(src_gray, bw, 255, 0, 0, 51, -25); 
//		//		    Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
//		Imgproc.adaptiveThreshold(imageBlurr, imageA, 255, 0, 0, 51, -25); 
//
//		Highgui.imwrite("ext.jpg",imageBlurr);
//		Highgui.imwrite("imageA.jpg",imageA);
//
//
//		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(9,9));
//		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(9,9));
//
//		Imgproc.erode(imageA, imageA, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,8)));
//
//		Imgproc.erode(imageA, imageA, erodeElement);
//		Imgproc.dilate(imageA, imageA, dilateElement);
//
//		Imgproc.dilate(imageA, imageA, dilateElement);
//		Imgproc.erode(imageA, imageA, erodeElement);
//
//		Highgui.imwrite("clean.jpg", imageA);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
		Imgproc.findContours(dst, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
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
			if (contourArea > 250 && contourArea < 670){
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
				list.add(new Ball(centerX, centerY));
			}
			System.out.println();
		}


		//		System.out.println("Found "+circles.cols() + " circles.");
		//		for (int i = 0; i < circles.cols(); i++) {
		//			double[] circle = circles.get(0,i);
		////			if (src.get((int)circle[1], (int)circle[0])[2]>140){
		//				list.add(new Ball(circle[0],circle[1]));
		//				Point center = new Point(circle[0], circle[1]);
		//
		//				double radius = circle[2];
		//				// circle center
		//				Core.circle( src, center, 3, new Scalar(0,255,0), -1, 8, 0 );
		//				// circle outline
		//				Core.circle( src, center, (int)radius, new Scalar(0,0,255), 3, 8, 0 );
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

	public CircleFinderCanny(Mat image) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		this.src = image;
	}

	public CircleFinderCanny(){
	}

	public static void main(String[] args) throws Exception {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		new CircleFinderCanny().run();
	}

}
