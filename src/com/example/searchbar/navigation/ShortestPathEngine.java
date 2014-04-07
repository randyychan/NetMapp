package com.example.searchbar.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;

public class ShortestPathEngine {

	private static ArrayList<Vertex> vertices;
	private static ArrayList<Edge> edges;
	private static Graph graph;
	
	public static float[] createPath(int start_x, int start_y, int end_x, int end_y, Activity activity) {
		float[] results;
		int path_size;
		
		System.out.println("x: " + start_x);
		System.out.println("y: " + start_y);
		System.out.println("ex: " + end_x);
		System.out.println("ey: " + end_y);
		
		int start_id;
		int end_id;
		
		HashMap<Integer, PxCoord> vertices_from_file;
		HashMap<Integer, EdgeCoord> edges_from_file;
		
		vertices_from_file = PathParser.parsePathways(activity);
		edges_from_file = PathParser.parseEdges(vertices_from_file, activity);
        
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		
		for(int i = 0; i < vertices_from_file.size(); i++) {
			PxCoord coord = vertices_from_file.get(i);

			Vertex location = new Vertex(i, coord.x, coord.y);
			vertices.add(location);
		}

		
			
		for(int i = 0; i < edges_from_file.size(); i++) {
			EdgeCoord coord = edges_from_file.get(i);
			
			addEdge(i, Integer.valueOf(coord.startPath), Integer.valueOf(coord.endPath), coord.weight);
		}
	
	    graph = new Graph(edges, vertices);
	    
	    ShortestPath algorithm = new ShortestPath(graph);
	    
	    start_id = getClosestVertex(start_x, start_y);
	    end_id = getClosestVertex(end_x, end_y);
	    
	    System.out.println("RANDY START ID: " + start_id);
	    System.out.println("RANDY END ID: " + end_id);

	    
	    algorithm.useShortestPath(vertices.get(start_id));
	    LinkedList<Vertex> path = algorithm.getShortestPath(vertices.get(end_id));
	
	    if (path == null) {
	    	return null;
	    }
	    path_size = path.size() * 4 - 2;
	    results = new float[path_size];
	    
	    int i = 0;
	    
	    for(Vertex vertex : path) {
	    	
	    	if (i == 0) {
	    		results[i] = Float.valueOf(vertex.getX());
	    		results[i + 1] = Float.valueOf(vertex.getY());
	    		
	    		i += 2;
	    	}
	    	else {
	    		results[i] = Float.valueOf(vertex.getX());
	    		results[i + 1] = Float.valueOf(vertex.getY());
	    		results[i + 2] = Float.valueOf(vertex.getX());
	    		results[i + 3] = Float.valueOf(vertex.getY());
	    		
	    		i += 4;
	    	}
	    	
	    }
//	    
//	    results[path_size - 2] = end_x;
//	    results[path_size - 1] = end_y;
	    
	    return results;
	}
	
	private static int getClosestVertex(int x, int y) {
		int vertex_index = Integer.MAX_VALUE;
		float smallest = Float.MAX_VALUE;
		
		for(int i = 0; i < vertices.size(); i++) {
			int current_x = vertices.get(i).getX();
			int current_y = vertices.get(i).getY();
			
			float distance = getDistance(x, y, current_x, current_y);

			if( smallest > distance) {
				smallest = distance;
				vertex_index = i;
			}
		}
		
		return vertices.get(vertex_index).getId();
	}
	
	private static Float getDistance(int x, int y, int x2, int y2) {
		Double distance = new Double(Math.sqrt((Math.pow(x - x2, 2) + Math.pow(y - y2, 2))));
		return distance.floatValue();
	}
	
	private static void addEdge(int edge_id, int start, int end, double weight) {
		Edge edge = new Edge(edge_id, vertices.get(start), vertices.get(end), weight);
		
		edges.add(edge);
	}
	
}
