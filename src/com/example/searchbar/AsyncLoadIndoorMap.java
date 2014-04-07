package com.example.searchbar;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;




public class AsyncLoadIndoorMap extends AsyncTask<IndoorParameters, Void, IndoorMapFragment> {

	public int building_num, floor_num;
	public int myX, myY, officeX, officeY;
	float[] path;
	Context context;
	Toast toast=null;
	private int SIZE = 25;
	private String username=null;
	
    public InputStream getIndoorMapStream()
    {
    	InputStream res = null;
    	
    	System.out.printf("building: %d, floor: %d", building_num, floor_num);
    	System.out.flush();
    	
    	switch (building_num) {
    	case 3:
    		if (floor_num == 0) {
    	    	res = context.getResources().openRawResource(R.raw.build31);
    		} else if (floor_num == 1) {
    	    	res = context.getResources().openRawResource(R.raw.bld3_2);

    		} else if (floor_num == 2) {
    	    	res = context.getResources().openRawResource(R.raw.bld3_3);
    		}
    		break;
    	case 7:
    		if (floor_num == 0) {
    	    	res = context.getResources().openRawResource(R.raw.bld7_1);
    		} else if (floor_num == 1) {
    			res = context.getResources().openRawResource(R.raw.bld7_2);
    		} else if (floor_num == 2) {
    	    	res = context.getResources().openRawResource(R.raw.bld7_3);
    		} else if (floor_num == 3) {
    	    	res = context.getResources().openRawResource(R.raw.bld7_4);
    		} else if (floor_num == 4) {
    	    	res = context.getResources().openRawResource(R.raw.bld7_5);
    		}
    		break;

    	default:
    		
    		if (floor_num == 0) {
    	    	res = context.getResources().openRawResource(R.raw.bld3_1);
    		} else if (floor_num == 1) {
    	    	res = context.getResources().openRawResource(R.raw.bld3_2);
    		} else if (floor_num == 2) {
    	    	res = context.getResources().openRawResource(R.raw.bld3_3);
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
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawCircle(x, y, SIZE, paint);
    }
    
    public void plotMyLocation(int x, int y, Bitmap copy)
    {
    	System.out.println("DULCY: " + x + " " + y);

    	if (x == -1  && y == -1)
    		return;

    	Canvas canvas = new Canvas(copy);
    	Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        canvas.drawCircle(x, y, SIZE, paint);
        //canvas.drawBitmap(bitmap, left, top, paint)
    }
	
    public static Bitmap LoadImageFromWebOperations(String username) {
    	String url = "http://cedprod.corp.netapp.com:10080/photos/" + username + ".jpg";
    	Bitmap bitmap=null;
        try {

            InputStream is = (InputStream) new URL(url).getContent();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            return null;
        }
        return bitmap;
    }
    
    public void plotMyPicture(int x, int y, Bitmap copy, Bitmap myPic)
    {
    	System.out.println("DULCY: " + x + " " + y);

    	if (x == -1  && y == -1)
    		return;

    	int sizeX = copy.getWidth();
    	int sizeY = copy.getHeight();
    	int lineX, lineY;
    	int diffX, diffY;
    	
    	if(x > sizeX/2)
    	{
    		diffX = -50 - myPic.getWidth();
    		lineX = -50;
    	}
    	else {
    		diffX = 50;
    		lineX = 50;
    	}
    	
    	if(y > sizeY/2)
    	{
    		diffY = -50 - myPic.getHeight();
    		lineY = -50;
    	} else {
    		diffY = 50;
    		lineY = 50;
    	}
    	
    	Canvas canvas = new Canvas(copy);
    	Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        canvas.drawLine(x, y, x+lineX, y+lineY, paint);
        canvas.drawBitmap(myPic, x+diffX, y+diffY, null);
    }
    
    
    public void plotPath(float[] path, Bitmap copy)
    {

    	Canvas canvas = new Canvas(copy);
    	Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.argb(255, 46, 147, 63));
        canvas.drawLines(path, paint);
        canvas.drawLine(myX, myY, path[0], path[1], paint);

        canvas.drawLine(path[path.length-2], path[path.length-1], officeX, officeY, paint);
    }
    
    
    
    @Override
    protected IndoorMapFragment doInBackground(IndoorParameters... urls) {
    	IndoorParameters parameters = urls[0];
    	this.building_num = parameters.building_num;
    	this.context = parameters.context;
    	this.floor_num = parameters.floor_num;
    	this.myX = parameters.myX;
    	this.myY = parameters.myY;
    	this.officeX = parameters.officeX;
    	this.officeY = parameters.officeY;
    	this.username = parameters.username;
    	this.path = parameters.path;
        publishProgress(null);
        
        
        
        TouchImageView view = new TouchImageView(parameters.context);
        InputStream res = getIndoorMapStream();
        Bitmap bitmap = BitmapFactory.decodeStream(res);
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
      
        if (path != null && building_num == 3) {
        	plotPath(path, copy);
        }
        plotDestination(officeX, officeY, copy);
        plotMyLocation(myX, myY, copy);
      
        
        
        Bitmap myPic=null;
//        if (username != null)
//        	myPic = LoadImageFromWebOperations(username);
    	System.out.println("RANDY  THERE");

        if (myPic != null)
        {
        	//double scale = 0.1;
        	double scale = 200/(double)myPic.getWidth();
        	
        	myPic = Bitmap.createScaledBitmap(myPic, (int)(myPic.getWidth()*scale), (int)(myPic.getHeight()*scale), true);
        	System.out.println("RANDY MYPIC THERE");
        	plotMyPicture(officeX, officeY, copy,myPic);
        }
        
        
        view.setImageBitmap(copy);
        IndoorMapFragment frag = new IndoorMapFragment();
        frag.loadView(view);
        
        return frag;
    }

    @Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		if (this.building_num == 3 || this.building_num == 7)
			Toast.makeText(context, "Building " + building_num + ", Floor " + (floor_num+1), Toast.LENGTH_SHORT).show(); 
		else {
			Toast.makeText(context, "Building " + building_num + " not supported. Select building 3 or 7. Showing Building 3 instead.", Toast.LENGTH_SHORT).show(); 
		}
	}

    @Override
    protected void onPostExecute(IndoorMapFragment result) {
    	if (MapViewActivity.fragment != null) 
    	{
    		MapViewActivity.fragmentManager.beginTransaction().remove(MapViewActivity.fragment).commit();
    		MapViewActivity.fragment = null;
    	}
    	
    	
    	Bundle args = new Bundle();
        //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        result.setArguments(args);
        MapViewActivity.fragment = result;
        
        MapViewActivity.fragmentManager.beginTransaction().add(R.id.content_frame, result, "fragment").commit();
        
 
        
        }	
}