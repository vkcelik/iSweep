package vision;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import boldogrobot.Ball;

class WebcamContinous {


	public List<Ball> run() throws Exception{
		boolean loadFromImage = false;
		boolean printCoordinates = false;
		List<Ball> list = new ArrayList<Ball>();

		ImageDrawer imageViewer = new ImageDrawer();
		JFrame window = new JFrame();
		window.add(imageViewer);
		window.setSize(640, 360);
		window.setVisible(true);

		Mat src_gray = new Mat();
		Mat smooth = new Mat();
		Mat circles = new Mat();
		Mat src = new Mat(); 
		VideoCapture webSource = null;
		MatOfByte mem = new MatOfByte();

		if (loadFromImage){
			src = Highgui.imread("ball.jpg",1);
		} else {
			//		load frame (image) from webcam
			webSource = new VideoCapture(0);
			Thread.sleep(2000);

			if (!webSource.isOpened()) {
				System.out.println("Error Initializing camera");
			} else {
				System.out.println("Iniciando camera");
			}
		}

		while(webSource.grab()){
			double[] circle = null;
			try{
				webSource.retrieve(src);

				Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
				Imgproc.GaussianBlur(src_gray, smooth, new Size(23,23), 4, 4);
				Imgproc.HoughCircles(smooth, circles, Imgproc.CV_HOUGH_GRADIENT, 2, 90, 40, 40, 30, 60);

				System.out.println("Found "+circles.cols() + " circles.");

				if (circles.cols()>0){
					for (int i = 0; i < circles.cols(); i++) {
						circle = circles.get(0,i);
						//					list.add(new Ball(circle[0],circle[1]));
						Point center = new Point(circle[0], circle[1]);
						
						double radius =  circle[2];
						// circle center
						Core.circle( src, center, 3, new Scalar(0,255,0), -1, 8, 0 );
						// circle outline
						Core.circle( src, center, (int)radius, new Scalar(0,0,255), 3, 8, 0 );
					}

					if (printCoordinates){
						for (Ball b: list){
							System.out.println(b);
						}
					}
				}


				Highgui.imencode(".png", src, mem);
				Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
				imageViewer.updateImage(im);

				System.out.println("Frame grabbed");
			} catch(Exception ex){
				ex.printStackTrace();
				System.out.println("Error");
				System.out.println(circle);
			}
		}

		return list;
	}

//	public static void main(String[] args) {
//		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
//		try {
//			new CircleFinder().run();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
