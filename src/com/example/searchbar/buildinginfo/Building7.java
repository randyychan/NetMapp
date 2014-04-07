package com.example.searchbar.buildinginfo;

import java.util.ArrayList;

public class Building7 {
	public static int floors = 5;
	
	public static ArrayList<EntrancePoint> getEntrancePoints() {
		ArrayList<EntrancePoint> ret = new ArrayList<EntrancePoint>();
		EntrancePoint new_point = new EntrancePoint(1, 928, 613, 37.413385,-122.010204);
		ret.add(new_point);
 		new_point = new EntrancePoint(2, 287, 60, 37.413593, -122.010617);
 		ret.add(new_point);
 		new_point = new EntrancePoint(3, 44, 654, 37.413338, -122.010933);
 		ret.add(new_point);
		new_point = new EntrancePoint(4, 286, 1353, 37.412963, -122.010987);
 		ret.add(new_point);
 		new_point = new EntrancePoint(5, 637, 1554, 37.412809, -122.010778);
 		ret.add(new_point);
 		
		return ret;
	}
	
	public static String[] floor1() {
		String[] ret = {"Pike Place Market", "Quincy 1", "Quincy 2", "Quincy 3", "South St. Seaport"};
		return ret;
	}
	
	public static String[] floor2() {
		String[] ret = {"Americano", "Arabica","Cafe au Lait", "Cappuccino", "Cortado","Espresso", "Frappucino","Freddo",
				"Kona", "Latte", "Macchiato", "Madras", "Melange", "Mocha","Peaberry", "Robusta", "Sumatra", "Supremo", "Turkish", "Vienna"};
		return ret;
	}
	public static String[] floor3() {
		String[] ret = {"Amedei", "Blanxart", "Cadbury", "Callebaut", "Coppeneur", "Cote D'or", "Domori", "Ghirardelli", "Godiva", "Hersey", "Lindt", "Nestle",
				"Neuchatel", "Perugina", "Richart", "Scharffen Berger", "See's", "Suchard", "Teuscher", "Toblerone", "Valor", "Willy Wonka"};
		return ret;
	}
	
	public static String[] floor4() {
		String[] ret = {"Barbera", "Bordeaux", "Burgundy", "Cabernet", "Chablis", "Champagne", "Chardonnay","Chianti", "Grendache", "Meritage",
				"Merlot", "Muscat", "Pinot Noir", "Riesling", "Sangiovese", "Shiraz", "Syrah", "Zinfandel"};
		return ret;
	}
	
	public static String[] floor5() {
		String[] ret = {"Amstel","Becks", "Budweiser", "Coors", "Corona", "Fat Tire", "Cordon Biersch", "Guinness", "Heineken", "Miller", "Newcastle",
				"O'doul's", "Samuel Adams", "Sapporo", "Sierra Nevada", "Tecate", "Tsingtao"};
		return ret;
	}
}
