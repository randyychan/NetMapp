package com.example.searchbar.navigation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class ParseLocation {

	//this parses the pathways files. format: id PxX PxY
    //pL - hashmap of pathways coords to be filled
	//files - string array of file names to parse
	public static HashMap<String, PxCoord> parsePathways(String[] files) {
		HashMap<String, PxCoord> pL = new HashMap<String, PxCoord>();
		
		for (int i = 0; i < files.length; i++) {
			try {
				Scanner in = new Scanner(new BufferedReader(new FileReader(
						"C:/Users/arudy/Documents/Android Development/Workspace/ColorFinder/"
								+ files[i] + ".txt")));

				while (in.hasNextLine()) {
					Scanner inner = new Scanner(in.nextLine());
					pL.put(inner.next(),
							new PxCoord(Integer.parseInt(inner.next()), Integer
									.parseInt(inner.next())));
					inner.close();
				}

				in.close();
			} catch (Exception e) {
				System.out.println("File not found: " + e);
			}
		}
		
		return pL;
	}

	//this parses the edges files. format: id idOfPathPxPointStart idOfPathPxPointEnd
	//pL - hashmap of the pathways
	//eL - hashmap of edges to be filled
	//files - string array of file names to parse
	public static HashMap<String, EdgeCoord> parseEdges(HashMap<String, PxCoord> pL, String[] files) {
		HashMap<String, EdgeCoord> eL = new HashMap<String, EdgeCoord>(); 
		
		for (int i = 0; i < files.length; i++) {
			try {
				Scanner in = new Scanner(new BufferedReader(new FileReader(
						"C:/Users/arudy/Documents/Android Development/Workspace/ColorFinder/"
								+ files[i] + ".txt")));

				while (in.hasNextLine()) {
					Scanner inner = new Scanner(in.nextLine());
					
					String id = inner.next();
					String startPathId = inner.next();
					String endPathId = inner.next();
					
					PxCoord start = pL.get(startPathId);
					PxCoord end = pL.get(endPathId);
					
					double weight = Math.sqrt((Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2)));
							
					eL.put(id, new EdgeCoord(startPathId, endPathId, weight));
					inner.close();
				}

				in.close();
			} catch (Exception e) {
				System.out.println("File not found: " + e);
			}
		}
		
		return eL;
	}
	
	public static class PxCoord {
		int x;
		int y;

		public PxCoord(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public static class EdgeCoord {
		String startPath;
		String endPath;
		double weight;
		
		public EdgeCoord(String x, String y, Double weight) {
			startPath = new String(x);
			endPath = new String(y);
			this.weight = weight;
		}
	}
}