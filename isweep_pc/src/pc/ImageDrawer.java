package pc;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImageDrawer extends JPanel{
	private static final long serialVersionUID = -4935629972860953326L;
	BufferedImage buff;

	@Override
	public void paint(Graphics g) {
		if(buff!=null)
			g.drawImage(buff, 0, 0, getWidth(), getHeight(), 0, 0, buff.getWidth(), buff.getHeight(), null);
	}

	public void updateImage(Image im) {
		buff = (BufferedImage) im;
		repaint();
		invalidate();
	}
}
