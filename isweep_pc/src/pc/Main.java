package pc;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class Main {
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

		VideoCapture webSource = new VideoCapture(1);
		Thread.sleep(2000);

		if (!webSource.isOpened()) {
			System.out.println("Error Initializing camera");
		} else {
			System.out.println("Iniciando camera");
		}

		Mat frame = new Mat();
		Mat hsv = new Mat();
		Mat filtered = new Mat();
		MatOfByte mem = new MatOfByte();
		MatOfByte mem2 = new MatOfByte();
		
		while(webSource.grab()){
			try{
				webSource.retrieve(frame);
				Highgui.imencode(".png", frame, mem);
				Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
				imageViewer.updateImage(im);
				
				Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV);
				Core.inRange(hsv, new Scalar(gui.getH_MIN(), gui.getS_MIN(), gui.getV_MIN()), new Scalar(gui.getH_MAX(), gui.getS_MAX(), gui.getV_MAX()), filtered);
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
}
