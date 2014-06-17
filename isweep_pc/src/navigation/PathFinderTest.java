package navigation;

import java.util.ArrayList;

import boldogrobot.Placeable;

public class PathFinderTest {

	public static void main(String[] args) {
		PathFinder pf = new PathFinder();
		ArrayList<Placeable> points = new ArrayList<Placeable>();
		// BRUG KOORDINATER
//		int[][] adjacency = {
//				{0,49,42,29,56},
//				{49,0,91,57,85},
//				{42,91,0,52,59},
//				{29,57,52,0,83},
//				{56,85,59,83,0}};
		System.out.println(pf.getShortestPath(points));
	}

}
