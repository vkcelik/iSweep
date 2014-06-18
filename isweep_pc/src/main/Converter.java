package main;

public class Converter {
	
	double pixelPerMm;
	double mmPerPixel;
	
//	public double pixelToMm(double pixel, double sideLengthPixel){
//		double L_side_PX=sideLengthPixel, L_side_MM=1200; //1200
//		double mmperpixel = L_side_MM/L_side_PX;
//		return pixel*mmperpixel;
//	}
//	
//	public double mmToPixel(double mm, double sideLengthPixel){
//		double L_side_PX=sideLengthPixel, L_side_MM=1200; //1200
//		double pixelpermm = L_side_PX/L_side_MM;
//		return mm*pixelpermm;
//	}
	
	public Converter(double mmPerPixel, double pixelPerMm) { 
		this.mmPerPixel = mmPerPixel;
		this.pixelPerMm = pixelPerMm;
	}
	
	public double pixelToMm(double pixel){
		return pixel*mmPerPixel;
	}
	
	public double mmToPixel(double mm){
		return mm*pixelPerMm;
	}
	
}
