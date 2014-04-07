package com.example.searchbar.buildinginfo;

public class MeetingRooms {

	private String[] outdoor;
	
	public static String[] outdoor() {
		String[] ret = {"Java 3", "Java 7", "Java 11", "Volley ball", "Gym"};
		return ret;
	}
	
	public static String[] building1_1() {
		String[] ret = {"Amsterdam", "Bangalore", "Cranberry Township", "Founders Theater", "Munich",
				"Paris", "Raleigh", "Sao Paulo", "Stockley Park", "Sunnyvale","Sydney","Tokyo","Waitham"};
		return ret;
	}
	public static String[] building2() {
		String[] ret = {"Floor 1", "Floor 2", "Floor 3"};
		return ret;
	}
	
	public static String[] defaultAnswer() {
		String[] ret = {"This", "will","have a", "list", "of", "conference", "rooms"};
		return ret;
	}
	
	public static String getKey(String room) {
		room = room.replaceAll("\\s", "");
		room = room.replaceAll("'","");
		System.out.println("RANDY ROOM: " + room);
		
		return room;
	}
}
