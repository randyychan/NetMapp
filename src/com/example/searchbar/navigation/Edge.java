package com.example.searchbar.navigation;

public class Edge {
	private int id;
	private Vertex start;
	private Vertex end;
	private double weight;
	
	public Edge(int id, Vertex start, Vertex end, double weight) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public Vertex getStart() {
		return start;
	}

	public void setStart(Vertex start) {
		this.start = start;
	}

	public Vertex getEnd() {
		return end;
	}

	public void setEnd(Vertex end) {
		this.end = end;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}
