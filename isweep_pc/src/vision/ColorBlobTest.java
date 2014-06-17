package vision;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

public class ColorBlobTest {

	public static void main(String[] args) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		
		Mat src = Highgui.imread("Picture 12.jpg",1);
		ColorBlobDetector cbd = new ColorBlobDetector();
		
		cbd.setHsvColor(new Scalar(104, 39, 204));
		

	}

}
