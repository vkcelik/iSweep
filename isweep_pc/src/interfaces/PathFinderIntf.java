package interfaces;

import java.util.ArrayList;

import boldogrobot.Placeable;

public interface PathFinderIntf {

	public ArrayList<Placeable> getShortestPath(ArrayList<Placeable> objects);
	
}

