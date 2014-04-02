package boldogrobot;

public class Bane {
	private static final int BANE_W = 180;
	private static final int BANE_H = 120;

	private char[][] pixels;

	public Bane(int w, int h) {
		this.pixels = new char[w][h];

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				pixels[i][j] = ' ';
			}
		}
	}

	public void draw(int x, int y, char symbol) {
		pixels[y][x] = symbol;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				result += "" + pixels[i][j];
			}
			result += "\n";
		}
		return result;
	}

	private Bane bane = new Bane(BANE_W, BANE_H);

	public static class Dijkstra {
		// Dijkstra's algorithm to find shortest path from s to all other nodes
		
		public static int[] dijkstra(WeightedGraph G, int s) {
			final int[] dist = new int[G.size()]; // shortest known distance from "s"
			
			final int[] pred = new int[G.size()]; // preceeding node in path
			
			final boolean[] visited = new boolean[G.size()]; // all false initially

			for (int i = 0; i < dist.length; i++) {
				dist[i] = Integer.MAX_VALUE;
			}
			dist[s] = 0;

			for (int i = 0; i < dist.length; i++) {
				final int next = minVertex(dist, visited);
				visited[next] = true;

				// The shortest path to next is dist[next] and via pred[next].

				final int[] n = G.neighbors(next);
				for (int j = 0; j < n.length; j++) {
					final int v = n[j];
					final int d = dist[next] + G.getWeight(next, v);
					if (dist[v] > d) {
						dist[v] = d;
						pred[v] = next;
					}
				}
			}
			return pred; // (ignore pred[s]==0!)
		}

		private static int minVertex(int[] dist, boolean[] v) {
			int x = Integer.MAX_VALUE;
			
			int y = -1; // graph not connected, or no unvisited vertices
			for (int i = 0; i < dist.length; i++) {
				if (!v[i] && dist[i] < x) {
					y = i;
					x = dist[i];
				}
			}
			return y;
		}

		public static void printPath(WeightedGraph G, int[] pred, int s, int e) {
			final java.util.ArrayList<Object> path = new java.util.ArrayList<Object>();
			int x = e;
			while (x != s) {
				path.add(0, G.getLabel(x));
				x = pred[x];
			}
			path.add(0, G.getLabel(s));
			System.out.println(path);
		}
	}
}
