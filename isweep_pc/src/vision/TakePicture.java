package vision;

/*
640x480
1920x1080
2304x1296
3280x1845
5168x2907

2304x1536
 */

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class TakePicture {

	public static void main(String[] args) {
		System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
		VideoCapture vc = new VideoCapture(0);
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		if(vc.isOpened()){
			System.out.println("Cam opened");
		} else {
			System.out.println("Cam not opened");
		}

		Mat frame = new Mat();
		vc.read(frame);

		System.out.println(vc.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 2304.0));
		System.out.println(vc.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 1536.0));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vc.read(frame);
		System.out.println(frame.cols());
		System.out.println(frame.rows());

		//		for (double width=2304.0;width<=5168;width++){
		//			for (double height = 1296.0;height <= 2907;height++){
		//				vc.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, width);
		//				vc.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, height);
		//				vc.read(frame);
		//				if (frame.cols() == width && frame.rows() == height){
		//					System.out.println(width+"x"+height);
		//				}
		//			}
		//		}
		//		vc.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1920.0);
		//		vc.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 1080.0);
		//		
		//		vc.read(frame);
		//
		Highgui.imwrite("pic2.jpg", frame);
		System.out.println("Done");
	}

}
