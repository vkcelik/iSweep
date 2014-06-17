package navigation;

import java.util.ArrayList;

import boldogrobot.Placeable;

public class CalculateRegions {

	Placeable P;
	Placeable Q;
	Placeable Z;
	Placeable R;


	public CalculateRegions(Placeable topLeft, Placeable topRight,
			Placeable bottomLeft, Placeable bottomRight) {
		this.P = topLeft;
		this.Q = topRight;
		this.Z = bottomLeft;
		this.R = bottomRight;
	}
	public static void main(String[] args){
		new CalculateRegions(new Placeable(100, 200), new Placeable(1200, 1300), 
				new Placeable(100,1700), new Placeable(1200,1700)).run();
	}

	public ArrayList<Retangle> run(){

		ArrayList<Retangle> omroder = new ArrayList<Retangle>();
		double Bredde = 1800.0;
		double laengde = 1200.0;

		double PA = 200.0;
		double PD = Bredde-PA;

		double a = PA/Bredde;
		double b = (Bredde-PA)/Bredde;
		double c = PA/laengde;
		double d = (laengde-PA)/laengde;
		double e = a;
		double f = b;
		double g = c;
		double h = d;
		double i = a;
		double j = b;
		double k = e;
		double l = b;

		double scaledPQX= (Q.getX()-P.getX())* a;
		double scaledPQY=(Q.getY()-P.getY())*a;
		double AXCORDINAT = scaledPQX + P.getX();
		double AYCORDINAT = scaledPQY + P.getY();


		double scaledPQ1X =  (Q.getX()-P.getX())* b;
		double scaledPQ1Y=(Q.getY()-P.getY())*b;
		double DXCORDINAT = scaledPQ1X + P.getX();
		double DYCORDINAT = scaledPQ1Y + P.getY();

		double scaledPZX= (Z.getX()-P.getX())*c;
		double scaledPZY=(Z.getY()-P.getY())*c;
		double CXCORDINAT = scaledPZX + P.getX();
		double CYCORDINAT = scaledPZY + P.getY();
		Placeable C = new  Placeable(CXCORDINAT, CYCORDINAT);

		double scaledPZ1X= (Z.getX()-P.getX())*d;
		double scaledPZ1Y=(Z.getY()-P.getY())*d;
		double KXCORDINAT = scaledPZ1X + P.getX();
		double KYCORDINAT = scaledPZ1Y + P.getY();

		Placeable K = new  Placeable(KXCORDINAT, KYCORDINAT);

		double scaledZRX= (R.getX()-Z.getX())*e;
		double scaledZRY=(R.getY()-Z.getY())*e;
		double LXCORDINAT = scaledZRX + Z.getX();
		double LYCORDINAT = scaledZRY + Z.getY();

		double scaledZR1X= (R.getX()-Z.getX())*f;
		double scaledZR1Y=(R.getY()-Z.getY())*f;
		double IXCORDINAT = scaledZR1X + Z.getX();
		double IYCORDINAT = scaledZR1Y + Z.getY();

		double scaledRQX = (Q.getX()-R.getX())*g;
		double scaledRQY=(Q.getY()-R.getY())*g;
		double GXCORDINAT = scaledRQX + R.getX();
		double GYCORDINAT = scaledRQY + R.getY();

		Placeable G= new  Placeable(GXCORDINAT, GYCORDINAT);

		double scaledRQ1X = (Q.getX()-R.getX())*h;
		double scaledRQ1Y=(Q.getY()-R.getY())*h;
		double FXCORDINAT = scaledRQ1X + R.getX();
		double FYCORDINAT = scaledRQ1Y + R.getY();
		Placeable F= new  Placeable(FXCORDINAT, FYCORDINAT);

		double scaledCFX = (F.getX()-C.getX())*i;
		double scaledCFY=(F.getY()-C.getY())*i;
		double BXCORDINAT = scaledCFX + C.getX();
		double BYCORDINAT = scaledCFY + C.getY();

		double scaledCF1X = (F.getX()-C.getX())*j;
		double scaledCF1Y=(F.getY()-C.getY())*j;
		double EXCORDINAT = scaledCF1X + C.getX();
		double EYCORDINAT = scaledCF1Y + C.getY();

		double scaledKGX = (G.getX()-K.getX())*k;
		double scaledKGY=(G.getY()-K.getY())*k;
		double JXCORDINAT = scaledKGX + K.getX();
		double JYCORDINAT = scaledKGY + K.getY();

		double scaledKG1X = (G.getX()-K.getX())*l;
		double scaledKG1Y=(G.getY()-K.getY())*l;
		double HXCORDINAT = scaledKG1X + K.getX();
		double HYCORDINAT = scaledKG1Y + K.getY();


		omroder.add(new Retangle(new Placeable(P.getX(), P.getY()), new Placeable(AXCORDINAT,AYCORDINAT)
		,new Placeable(CXCORDINAT,CYCORDINAT), new Placeable(BXCORDINAT,BYCORDINAT)));

		omroder.add(new Retangle(new Placeable(AXCORDINAT,AYCORDINAT),new Placeable(DXCORDINAT,DYCORDINAT)
		,new Placeable(BXCORDINAT,BYCORDINAT),new Placeable(EXCORDINAT,EYCORDINAT)));

		omroder.add(new Retangle(new Placeable(DXCORDINAT,DYCORDINAT),new Placeable(Q.getX(),Q.getY())
		,new Placeable(EXCORDINAT,EYCORDINAT),new Placeable(FXCORDINAT,FYCORDINAT)));

		omroder.add(new Retangle(new Placeable(CXCORDINAT,CYCORDINAT),new Placeable(BXCORDINAT,BYCORDINAT)
		,new Placeable(KXCORDINAT,KYCORDINAT),new Placeable(JXCORDINAT,JYCORDINAT)));

		omroder.add(new Retangle(new Placeable(EXCORDINAT,EYCORDINAT),new Placeable(FXCORDINAT,FYCORDINAT)
		,new Placeable(HXCORDINAT,HYCORDINAT),new Placeable(GXCORDINAT,GYCORDINAT)));

		omroder.add(new Retangle(new Placeable(KXCORDINAT,KYCORDINAT),new Placeable(JXCORDINAT,JYCORDINAT)
		,new Placeable(Z.getX(),Z.getY()),new Placeable(LXCORDINAT,LYCORDINAT)));
		
		omroder.add(new Retangle(new Placeable(JXCORDINAT,JYCORDINAT),new Placeable(HXCORDINAT,HYCORDINAT)
		,new Placeable(LXCORDINAT,LYCORDINAT),new Placeable(IXCORDINAT,IYCORDINAT)));
		
		omroder.add(new Retangle(new Placeable(HXCORDINAT,HYCORDINAT),new Placeable(GXCORDINAT,GYCORDINAT)
		,new Placeable(IXCORDINAT,IYCORDINAT),new Placeable(R.getX(),R.getY())));
		
		System.out.println(omroder);
		return omroder;
	}
}
