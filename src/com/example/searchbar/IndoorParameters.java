package com.example.searchbar;

import android.content.Context;

public class IndoorParameters
{
	public int building_num, floor_num;
	public int myX, myY, officeX, officeY;
	Context context;
	public float[] path;
	String username;
	public IndoorParameters(String username, int building_num, int floor_num, int myX, int myY, int officeX, int officeY, float[] path, Context context)
	{
		this.building_num = building_num;
		this.floor_num = floor_num;
		this.username = username;
		this.myX = myX;
		this.myY = myY;
		this.path = path;
		this.officeX = officeX;
		this.officeY = officeY;
		this.context = context;
	}
}