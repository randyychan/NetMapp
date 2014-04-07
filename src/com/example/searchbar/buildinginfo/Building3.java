package com.example.searchbar.buildinginfo;

import java.util.ArrayList;

public class Building3 {
	public static int floors = 3;
	
	public static ArrayList<EntrancePoint> getEntrancePoints() {
		ArrayList<EntrancePoint> ret = new ArrayList<EntrancePoint>();
		EntrancePoint new_point = new EntrancePoint(1, 287, 1831, 37.411038,-122.01204);
		ret.add(new_point);
 		new_point = new EntrancePoint(2, 1444, 1730, 37.411249,-122.012026);
 		ret.add(new_point);
 		new_point = new EntrancePoint(3, 1025, 1678, 37.411385,-122.011981);
 		ret.add(new_point);
 		new_point = new EntrancePoint(4, 997, 51, 37.411539,-122.012949);
 		ret.add(new_point);
 		new_point = new EntrancePoint(5, 103, 154, 37.411179,-122.013003);
 		ret.add(new_point);
 		new_point = new EntrancePoint(6, 102, 1679, 37.410938,-122.012332);
 		ret.add(new_point);
		return ret;
	}
	
	public static String[] floor1() {
		String[] ret = {"Daytona", "Girvan", "Hana", "Ipanema", "Manhattan", "Nairn", "Santa Cruz", "Sunset", "Waikiki"};
		return ret;
	}
	
	public static String[] floor2() {
		String[] ret = {"Avon", "Brahmaputra", "Congo", "Danube", "Ganges", "Jordon", "Napa", "Nile", "Oder","Rhine","Rho Grande","Thames", "Tigris",
				"Yangtze","Yarrow"};
		return ret;
	}
	public static String[] floor3() {
		String[] ret = {"Aruba", "Bali","Bermuda","Caymans","Crete", "Easter", "Falklands", "Guernsey", "Isle of Man", "Madagascar", "Orkneys", "Shetland", "Tahiti"};
		return ret;
	}
}
