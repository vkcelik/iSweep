package boldogrobot;


public class Bold {
	private static final int BANE_W = 180;
	private static final int BANE_H = 120;

	class Bane {
		private char[][] pixels;

		public Bane(int w, int h) {
			this.pixels = new char[w][h];

			for (int i = 0; i < pixels.length; i++) {
				for (int j = 0; j < pixels[i].length; j++) {
					pixels[i][j] = '-';
				}
			}
		}

		public void tegn(int x, int y, char symbol) {
			pixels[x][y] = symbol;

		}
	}

}
