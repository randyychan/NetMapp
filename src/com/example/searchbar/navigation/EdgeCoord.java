package com.example.searchbar.navigation;

public class EdgeCoord {
	String startPath;
	String endPath;
	double weight;
	
	public EdgeCoord(String x, String y, Double weight) {
		startPath = new String(x);
		endPath = new String(y);
		this.weight = weight;
	}
}