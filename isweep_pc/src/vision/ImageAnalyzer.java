package vision;

import java.util.List;

import org.opencv.core.Mat;

import boldogrobot.Ball;
import interfaces.ImageAnalyzerIntf;

public class ImageAnalyzer implements ImageAnalyzerIntf {

	Mat image;
		
	@Override
	public List<Ball> getBalls() {
		List<Ball> balls = null;
		try {
			balls = new CircleFinder(image).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balls;
	}

	public void setImage(Mat image) {
		this.image = image;
	}

}
