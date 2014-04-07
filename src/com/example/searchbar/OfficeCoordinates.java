package com.example.searchbar;
import java.util.HashMap;
import java.util.Scanner;

import android.app.Activity;

import com.example.searchbar.R;


public class OfficeCoordinates {
	public static HashMap<String, PxCoord> getHashmap(Activity activity)
	{
		HashMap<String, PxCoord> oL = new HashMap<String, PxCoord>();

		int[] loop = new int[6];
		loop[0] = R.raw.pixel3_1;
		loop[1] = R.raw.pixel3_2;
		loop[2] = R.raw.pixel3_3;
		loop[3] = R.raw.pixel7_2;
		loop[4] = R.raw.conference3;
		loop[5] = R.raw.conference7;
		
		for (int i = 0; i < loop.length; i++) {
			try {
				Scanner in = new Scanner(activity.getResources().openRawResource(loop[i]));

				while (in.hasNextLine()) {
					Scanner inner = new Scanner(in.nextLine());
					String key = inner.next();
					oL.put(key, new PxCoord(Integer.parseInt(inner.next()),
							Integer.parseInt(inner.next())));
					inner.close();
				}

				in.close();
			} catch (Exception e) {
				System.out.println("File not found: " + e);
			}
		}
		
		return oL;
	}
	public static class PxCoord {
		int x;
		int y;

		public PxCoord(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
