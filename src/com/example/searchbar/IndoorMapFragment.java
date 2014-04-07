package com.example.searchbar;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public  class IndoorMapFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";
    private Employee employee;
    private String directory;
    private int officeX, officeY, myX, myY;
    private int building_num;
    private int floor_num;
    private View view;
    public IndoorMapFragment() {
        // Empty constructor required for fragment subclasses
    }
    
    public void initFragment(int building_num, int floor_num, int myX, int myY, int officeX, int officeY)
    {
    	this.officeX = officeX;
    	this.officeY = officeY;
    	this.myX = myX;
    	this.myY = myY;
    	this.building_num = building_num;
    	this.floor_num = floor_num;
    	System.out.printf("building: %d, floor: %d", building_num, floor_num);

    }
    
    public void loadView(View view)
    {
    	this.view = view;
    }
    
    public InputStream getIndoorMapStream()
    {
    	InputStream res = null;
    	
    	System.out.flush();
    	
    	switch (building_num) {
    	case 3:
    		if (floor_num == 0) {
    	    	res = this.getResources().openRawResource(R.raw.bld3_1);
    		} else if (floor_num == 1) {
    	    	res = this.getResources().openRawResource(R.raw.bld3_2);

    		} else if (floor_num == 2) {
    	    	res =this.getResources().openRawResource(R.raw.bld3_3);
    		}
    		break;
    	case 7:
    		if (floor_num == 0) {
    	    	res = this.getResources().openRawResource(R.raw.bld7_1);
    		} else if (floor_num == 1) {
    			res = this.getResources().openRawResource(R.raw.bld7_2);
    		} else if (floor_num == 2) {
    	    	res =this.getResources().openRawResource(R.raw.bld7_3);
    		}
    		break;

    	default:
    		Toast.makeText(getActivity().getApplicationContext(),
    				"Building " + building_num + " not supported. Select building 3 or 7. Showing Building 3 instead.", Toast.LENGTH_SHORT).show();
    		if (floor_num == 0) {
    	    	res = this.getResources().openRawResource(R.raw.bld3_1);
    		} else if (floor_num == 1) {
    	    	res = this.getResources().openRawResource(R.raw.bld3_2);
    		} else if (floor_num == 2) {
    	    	res =this.getResources().openRawResource(R.raw.bld3_3);
    		}
    		break;
    	}
 	
    	return res;
    }
    
    public void plotDestination(int x, int y, Bitmap copy)
    {
    	if (x == -1 || y == -1)
    		return;

    	Canvas canvas = new Canvas(copy);
    	Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawRect(1164, 869, 1184, 889, paint);
        
    }
    
    public void plotMyLocation(int x, int y, Bitmap copy)
    {
    	System.out.println("DULCY: " + x + " " + y);

    	if (x == -1  && y == -1)
    		return;

    	Canvas canvas = new Canvas(copy);
    	Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawRect(x-100, y-200, x+200, y+200, paint);    
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
//        TouchImageView image = new TouchImageView(getActivity().getApplicationContext());
//        InputStream res = getIndoorMapStream();
//        Bitmap bitmap = BitmapFactory.decodeStream(res);
//        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//       
//        
//        plotDestination(officeX, officeY, copy);
//        plotMyLocation(myX, myY, copy);
//        
//        image.setImageBitmap(copy);
        
        return this.view;
    }
}