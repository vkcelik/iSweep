package main;

public class Converter {
	
	double pixelPerMm;
	double mmPerPixel;
	
	public int pixelToMm(int pixel, int sideLengthPixel){
		int L_side_PX=sideLengthPixel, L_side_MM=1000; //1200
		float mmperpixel = (float)L_side_MM/L_side_PX;
		return (int) (pixel*mmperpixel);
	}
	
	public int mmToPixel(int mm, int sideLengthPixel){
		int L_side_PX=sideLengthPixel, L_side_MM=1000; //1200
		float pixelpermm = (float)L_side_PX/L_side_MM;
		return (int) (mm*pixelpermm);
	}
	
	public Converter(double mmPerPixel, double pixelPerMm) { 
		this.mmPerPixel = mmPerPixel;
		this.pixelPerMm = pixelPerMm;
	}
	
	public int pixelToMm(int pixel){
		return (int) (pixel*mmPerPixel);
	}
	
	public int mmToPixel(int mm){
		return (int) (mm*pixelPerMm);
	}
	
}
