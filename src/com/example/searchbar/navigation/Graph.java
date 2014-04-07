package com.example.searchbar.navigation;

import java.util.ArrayList;

public class Graph {
	private ArrayList<Edge> edges;
	private ArrayList<Vertex> vertices;
	
	public Graph(ArrayList<Edge> edges, ArrayList<Vertex> vertices) {
		this.edges = edges;
		this.vertices = vertices;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}
}
