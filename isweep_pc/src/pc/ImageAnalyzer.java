package pc;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class ImageAnalyzer {
    public static void main(String[] args) {
    	int h_min = 100;
    	int h_max = 200;
    	int s_min = 100;
    	int s_max = 200;
    	int v_min = 100;
    	int v_max = 200;
    	
        System.loadLibrary("opencv_java248"); // loading the dll file from the native library location
        VideoCapture vc = new VideoCapture(0);
        if(!vc.isOpened()){
            System.out.println("Did not connect to camera");
        } else {
			System.out.println("Found webcam: "+vc);
		}
        Mat frame = new Mat();
        vc.retrieve(frame);
      
        Highgui.imwrite("original.jpg", frame);
        
        // HSV colors
        Mat HSV = new Mat();
        Mat filtered = new Mat();
        
        Imgproc.cvtColor(frame, HSV, Imgproc.COLOR_BGR2HSV);
        Highgui.imwrite("hsv.jpg", HSV);       
        if (frame.empty()) System.out.println("empty image");
        Core.inRange(frame, new Scalar(h_min, s_min, v_min), new Scalar(h_max, s_max, v_max), filtered);
        Highgui.imwrite("threshold_applied.jpg", filtered);
        
        morphOps(filtered);
        
        Highgui.imwrite("threshold_applied_noise_removed.jpg", filtered);
        
        showResult(frame);
        showResult(filtered);
        
        vc.release();
    }
    
     static void morphOps(Mat thresh){
    	 Mat erode = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3));
    	 Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(8,8));
    	 
    	 Imgproc.erode(thresh, thresh, erode);
    	 Imgproc.erode(thresh, thresh, erode);
    	 
    	 Imgproc.dilate(thresh, thresh, dilate);
    	 Imgproc.dilate(thresh, thresh, dilate);
    	 
     }
     
     public static void showResult(Mat img) {
 	    Imgproc.resize(img, img, new Size(640, 480));
 	    MatOfByte matOfByte = new MatOfByte();
 	    Highgui.imencode(".jpg", img, matOfByte);
 	    byte[] byteArray = matOfByte.toArray();
 	    BufferedImage bufImage = null;
 	    try {
 	        InputStream in = new ByteArrayInputStream(byteArray);
 	        bufImage = ImageIO.read(in);
 	        JFrame frame = new JFrame();
 	        frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
 	        frame.pack();
 	        frame.setVisible(true);
 	    } catch (Exception e) {
 	        e.printStackTrace();
 	    }
 	}
}