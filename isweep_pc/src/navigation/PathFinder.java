package navigation;

import java.util.ArrayList;

import boldogrobot.Placeable;
import interfaces.PathFinderIntf;

public class PathFinder implements PathFinderIntf {

	@Override
	public ArrayList<Placeable> getShortestPath(ArrayList<Placeable> objects) {
		
		// KONSTRUER ADJENCY MATRIX

		int[][] graph = new int[objects.size()][objects.size()];
		
		for(int a = 0; a<objects.size();a++){
			for(int b = 0; b<objects.size();b++){
				graph[b][a]=(int)objects.get(b).getDistance(objects.get(a));
			}
		}
		
		System.out.println("Matrix");
		for(int a = 0; a<objects.size();a++){
			for(int b = 0; b<objects.size();b++){
				System.out.print(graph[b][a]+"\t");
			}
			System.out.println();
		}
		
		int[] path = new ShortestHamiltonianPath().getShortestHamiltonianPath(graph);
		ArrayList<Placeable> fixedPath = new ArrayList<Placeable>();
//	    fixedPath.add(0);
	    for (int i = 1; i < path.length; i++) {
	    	fixedPath.add(objects.get(path[i]));
		}
		return fixedPath;
	}

}
