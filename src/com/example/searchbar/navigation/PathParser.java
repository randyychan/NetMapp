package com.example.searchbar.navigation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

import android.app.Activity;

import com.example.searchbar.R;

public class PathParser {

	//this parses the pathways files. format: id PxX PxY
    //pL - hashmap of pathways coords to be filled
	//files - string array of file names to parse
	public static HashMap<Integer, PxCoord> parsePathways(Activity activity) {
		HashMap<Integer, PxCoord> pL = new HashMap<Integer, PxCoord>();
		
	
			try {
				Scanner in = new Scanner(activity.getResources().openRawResource(R.raw.pathways3p1));

				while (in.hasNextLine()) {
					Scanner inner = new Scanner(in.nextLine());
					int id = Integer.valueOf(inner.next());

					pL.put(id,
							new PxCoord(Integer.parseInt(inner.next()), Integer
									.parseInt(inner.next())));
					inner.close();
				}

				in.close();
			} catch (Exception e) {
				System.out.println("File not found: " + e);
			}
		
		
		return pL;
	}

	//this parses the edges files. format: id idOfPathPxPointStart idOfPathPxPointEnd
	//pL - hashmap of the pathways
	//eL - hashmap of edges to be filled
	//files - string array of file names to parse
	public static HashMap<Integer, EdgeCoord> parseEdges(HashMap<Integer, PxCoord> pL, Activity activity) {
		HashMap<Integer, EdgeCoord> eL = new HashMap<Integer, EdgeCoord>(); 
		
		
			try {
				Scanner in = new Scanner(activity.getResources().openRawResource(R.raw.edges3p1));

				while (in.hasNextLine()) {
					Scanner inner = new Scanner(in.nextLine());
					
					int id = Integer.valueOf(inner.next());
					String startPathId = inner.next();
					String endPathId = inner.next();

					PxCoord start = pL.get(Integer.valueOf(startPathId));
					PxCoord end = pL.get(Integer.valueOf(endPathId));
		
					double weight = Math.sqrt((Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2)));

					eL.put(id, new EdgeCoord(startPathId, endPathId, weight));
					inner.close();

				}

				in.close();
			} catch (Exception e) {
				System.out.println("File not sdfound: " + e);
			}
		
		
		return eL;
	}
	
}