package com.example.searchbar.buildinginfo;

public class EntrancePoint {
	private int id;
	private int x;
	private int y;
	private double latitude;
	private double longitude;

	
	public EntrancePoint(int id, int x, int y, double latitude, double longitude) {
		this.id	= id;
		this.x = x;
		this.y = y;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}

