package com.example.searchbar.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class ShortestPath {
	private Graph graph;
	
	private Set<Vertex> visited_vertices;
	private Set<Vertex> unvisited_vertices;
	
	private HashMap<Vertex, Vertex> previous_vertices;
	private HashMap<Vertex, Double> distances;
	
	public ShortestPath(Graph graph) {
		this.graph = graph;
	}
	
	private boolean hasVisited(Vertex vertex) {
		return visited_vertices.contains(vertex);
	}
	
	public void useShortestPath(Vertex start) {
		visited_vertices = new HashSet<Vertex>();
		unvisited_vertices = new HashSet<Vertex>();
		previous_vertices = new HashMap<Vertex, Vertex>();
		distances = new HashMap<Vertex, Double>();

		unvisited_vertices.add(start);
		distances.put(start, Double.valueOf(0));
	
		while( !unvisited_vertices.isEmpty() ) {
			Vertex working_vertex = getClosestVertex(unvisited_vertices);
			unvisited_vertices.remove(working_vertex);
			visited_vertices.add(working_vertex);
			findShortestDistances(working_vertex);
		}
	}

	private ArrayList<Vertex> getNeighbours(Vertex vertex) {
		ArrayList<Vertex> neighbours = new ArrayList<Vertex>();
		
		// Finding neighbours based off edges
		for(Edge edge : graph.getEdges()) {
			if( edge.getStart().getId() == vertex.getId() &&
				!hasVisited( edge.getEnd() )) {
				neighbours.add( edge.getEnd() );
			}
		}
		
		return neighbours;
	}
	
	private Vertex getClosestVertex(Set<Vertex> unvisited) {
		Vertex lowest = null;
		
		for(Vertex vertex : unvisited) {
			if(lowest == null) {
				lowest = vertex;
			} else {
				if(getShortestDistance(vertex) < getShortestDistance(lowest)) {
					lowest = vertex;
				}
			}
		}
		
		return lowest;
	}
	
	private void findShortestDistances(Vertex vertex) {
		ArrayList<Vertex> neighbours = getNeighbours(vertex);
		
		for(Vertex working : neighbours) {
			if( getShortestDistance(working) > getShortestDistance(vertex) 
					+ getDistance(vertex, working)) {
				distances.put(working, getShortestDistance(vertex) 
					+ getDistance(vertex, working));
				previous_vertices.put(working, vertex);
				unvisited_vertices.add(working);
			}
		}
	}
	
	private Double getShortestDistance(Vertex end) {
		Double distance = distances.get(end);
		
		if(distance == null) {
			return Double.MAX_VALUE;
		}
		
		return distance;
	}

	private Double getDistance(Vertex start, Vertex end) {
		for(Edge edge : this.graph.getEdges()) {
			if(edge.getStart().equals(start) && edge.getEnd().equals(end)) {
				return edge.getWeight();
			}
		}
		
		return Double.valueOf(0);
	}
	
	public LinkedList<Vertex> getShortestPath(Vertex vertex) {
		LinkedList<Vertex> shortest_path = new LinkedList<Vertex>();
		Vertex current_vertex = vertex;
		
		// Vertex doesn't exist
		if( previous_vertices.get(current_vertex) == null ) {
			return null;
		}
		
		shortest_path.add(current_vertex);
		
		while( previous_vertices.get(current_vertex) != null ) {
			current_vertex = previous_vertices.get(current_vertex);
			shortest_path.add(current_vertex);
		}
		
		Collections.reverse(shortest_path);
		
		return shortest_path;
	}
}
