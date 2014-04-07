package com.example.searchbar;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchWrapper extends FrameLayout {
	private float x;
	private float y;
	
  public TouchWrapper(Context context) {
    super(context);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
    	  x =  event.getX();
    	  y =  event.getY();
            break;
      case MotionEvent.ACTION_UP:
    	  if (Math.sqrt((x - event.getX())*(x-event.getX()) +
    			(y-event.getY())*(y-event.getY())) > 20) {
    		  MapViewActivity.button.setVisibility(VISIBLE);
    		  OutdoorMap.userTouch = true;
    	  }
            break;
    }
    return super.dispatchTouchEvent(event);
  }
}