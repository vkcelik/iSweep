package navigation;

import java.util.ArrayList;

import interfaces.PathFinderIntf;

public class PathFinder implements PathFinderIntf {

	@Override
	public ArrayList<Integer> getShortestPath(int[][] graph) {
		
		int[] path = new ShortestHamiltonianPath().getShortestHamiltonianPath(graph);
		ArrayList<Integer> fixedPath = new ArrayList<Integer>();
	    fixedPath.add(0);
	    for (int i = 1; i < path.length; i++) {
	    	fixedPath.add(path[i]);
		}
		return fixedPath;
	}

}
