package vision;

import java.util.List;

import org.opencv.core.Mat;

import boldogrobot.Ball;
import boldogrobot.Placeable;
import interfaces.ImageAnalyzerIntf;

public class ImageAnalyzer implements ImageAnalyzerIntf {

	Mat image;
		
	@Override
	public List<Placeable> getBalls() {
		List<Placeable> balls = null;
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
