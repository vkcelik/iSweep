package main;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import vision.ImageAnalyzer;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location

		ImageAnalyzer ia = new ImageAnalyzer();

		Mat src = new Mat();

		// load frame (image) from webcam
		VideoCapture webSource = new VideoCapture(0);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		webSource.retrieve(src);

		ia.setImage(src);
		ia.getBalls();

	}

}
