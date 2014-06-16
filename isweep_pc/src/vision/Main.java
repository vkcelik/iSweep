package vision;
import interfaces.ImageAnalyzerIntf;

import java.awt.EventQueue;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import boldogrobot.Ball;



public class Main implements ImageAnalyzerIntf{
	static int FRAME_HEIGHT = 480;
	static int FRAME_WIDTH = 640;
	int MAX_NUM_OBJECTS=1;
	//minimum and maximum object area
	int MIN_OBJECT_AREA = 20*20;
	int MAX_OBJECT_AREA = (int)(FRAME_HEIGHT*FRAME_WIDTH/1.5);
	
	private static Treshhold gui;
	public static void main(String[] args) throws IOException, InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui = new Treshhold();
					gui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		ImageDrawer imageViewer = new ImageDrawer();
		ImageDrawer imageViewer2 = new ImageDrawer();
		JFrame window = new JFrame();
		JFrame window2 = new JFrame();
		window.add(imageViewer);
		window.setSize(640, 360);
		window.setVisible(true);
		window2.add(imageViewer2);
		window2.setSize(640, 360);
		window2.setVisible(true);

		VideoCapture webSource = new VideoCapture(0);
//		webSource.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT,FRAME_HEIGHT);
//		webSource.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, FRAME_WIDTH);
		Thread.sleep(2000);

		if (!webSource.isOpened()) {
			System.out.println("Error Initializing camera");
		} else {
			System.out.println("Iniciando camera");
		}

//		Mat frame = new Mat();
		Mat frame =  Highgui.imread("picture12new.jpg",1);
		Mat hsv = new Mat();
		Mat filtered = new Mat();
		MatOfByte mem = new MatOfByte();
		MatOfByte mem2 = new MatOfByte();
		
		while(webSource.grab()){
			try{
//				webSource.retrieve(frame);
				Highgui.imencode(".png", frame, mem);
				Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
				imageViewer.updateImage(im);
				
				Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV);
				Core.inRange(hsv, new Scalar(gui.getH_MIN(), gui.getS_MIN(), gui.getV_MIN()), new Scalar(gui.getH_MAX(), gui.getS_MAX(), gui.getV_MAX()), filtered);
//				morphOps(filtered);
				trackFilteredObject(filtered);
				
				Highgui.imencode(".png", filtered, mem2);
				Image im2 = ImageIO.read(new ByteArrayInputStream(mem2.toArray()));
				imageViewer2.updateImage(im2);
				
				System.out.println("Frame grabbed");
			} catch(Exception ex){
				ex.printStackTrace();
				System.out.println("Error");
			}
		}

		System.out.println("Terminated");
		System.exit(0);    
	}
	
	static void morphOps(Mat thresh){
		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,8));
		Imgproc.erode(thresh, thresh, erodeElement);
		Imgproc.erode(thresh, thresh, erodeElement);
		Imgproc.dilate(thresh, thresh, dilateElement);
		Imgproc.dilate(thresh, thresh, dilateElement);
	}
	
	static void trackFilteredObject(Mat threshold){
		Mat temp = new Mat();
		threshold.copyTo(temp);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		double refArea = 0;
		boolean objectFound = false;
		if (hierarchy.size().width > 0) {
			double numObjects = hierarchy.size().width;
			
//		for(int i=0; i< contours.size();i++){
//	        System.out.println(Imgproc.contourArea(contours.get(i)));
//	        if (Imgproc.contourArea(contours.get(i)) > 50 ){
//	            Rect rect = Imgproc.boundingRect(contours.get(i));
//	            System.out.println(rect.x + ", " + rect.y);
//	        }
	    }
	}

	@Override
	public List<Ball> getBalls() {
		List<Ball> balls = new ArrayList<Ball>();
		return balls;
	}
}