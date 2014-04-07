package com.example.searchbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.searchbar.OfficeCoordinates.PxCoord;
import com.example.searchbar.buildinginfo.Building3;
import com.example.searchbar.buildinginfo.Building7;
import com.example.searchbar.buildinginfo.BuildingLocations;
import com.example.searchbar.buildinginfo.EntrancePoint;
import com.example.searchbar.navigation.ShortestPathEngine;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class OutdoorMap  {
	GoogleMap map;
	Button button;
	LocationManager locationManager;
	Context context;
	Activity activity;
	LocationListener list;
	MarkerOptions markerOption;
	Marker marker;
	String office_string;
	int building_num;
	int floor_num;
	int cube_num;
    public HashMap<String, PxCoord> cubemap;
    private String username=null;
    private ListView mDrawerList;
    public boolean foundPerson = false;
    private String[] building_names;
    public static boolean userTouch = false;
	private static final float ZOOM_LEVEL = 18;
	private boolean first_time = true;
    private static final long SECONDS_PER_HOUR = 60;
    private static final long MILLISECONDS_PER_SECOND = 1000;
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 200;
    private static final long GEOFENCE_EXPIRATION_TIME =
            GEOFENCE_EXPIRATION_IN_HOURS *
            SECONDS_PER_HOUR *
            MILLISECONDS_PER_SECOND;
	

    
	public OutdoorMap(final GoogleMap map, Button button, Context context, Activity activity) {
		building_names = activity.getResources().getStringArray(R.array.buildings);

		this.map = map;
		this.button = button;
		this.context = context;
		this.activity = activity;
        this.cubemap = OfficeCoordinates.getHashmap(activity);

        BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.raw.three);
        LatLng southwest = new LatLng(37.411223,-122.01274);
        LatLng northeast = new LatLng(37.411492,-122.012391);
        LatLngBounds bounds2 = new LatLngBounds(southwest, northeast);
        
        // Adds a ground overlay with 50% transparency.
         map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.one);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_1_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_1_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_1_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_1_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
     // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.two);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_2_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_2_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_2_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_2_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
     // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.four);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_4_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_4_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_4_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_4_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
     // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.seven);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_7_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_7_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_7_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_7_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
     // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.eight);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_8_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_8_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_8_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_8_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
     // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.nine);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_9_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_9_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_9_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_9_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
     // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.ten);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_10_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_10_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_10_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_10_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
     // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.eleven);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_11_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_11_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_11_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_11_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
     // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.twelve);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_12_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_12_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_12_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_12_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
     // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.fourteen);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_14_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_14_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_14_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_14_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
        // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        image = BitmapDescriptorFactory.fromResource(R.raw.fifteen);
        southwest = new LatLng(37.411223-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_15_LAT,
        					  -122.01274-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_15_LONG);
        northeast = new LatLng(37.411492-BuildingLocations.BUILDING_3_LAT + BuildingLocations.BUILDING_15_LAT,
        					 -122.012391-BuildingLocations.BUILDING_3_LONG + BuildingLocations.BUILDING_15_LONG);
        bounds2 = new LatLngBounds(southwest, northeast);
        
        // Adds a ground overlay with 50% transparency.
        map.addGroundOverlay(new GroundOverlayOptions()
            .image(image)
            .positionFromBounds(bounds2)
            .transparency((float)0.5));
        
        
		this.map.getUiSettings().setZoomControlsEnabled(false);
		this.map.getUiSettings().setMyLocationButtonEnabled(false);

		
		
		
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker arg0) {
				// TODO Auto-generated method stub
				loadIndoorMap(-1, -1);

			}
		});
		
		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				userTouch = true;
	    		MapViewActivity.button.setVisibility(View.VISIBLE);

				return false;
			}
		});

		
		
		setLocation();
	}
	
	public void fixCamera(Location location)
	{
		if (location == null)
		{
			location = map.getMyLocation();
		}
		
		if (marker == null) {
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
	    	CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL);
	    	CameraPosition cameraPosition = new CameraPosition.Builder()
	        .target(latLng)      // Sets the center of the map to Mountain View
	        .zoom(ZOOM_LEVEL)                   // Sets the zoom
	        .bearing(0)                // Sets the orientation of the camera to east
	        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
	        .build();                   // Creates a CameraPosition from the builder
	   
	    	
	    	if (!first_time) {
	    		 map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));	    	}
	    	else {
	    		map.moveCamera(cameraUpdate);
	    		first_time = false;
	    	}
		} else {
			fixZoom();
		}
	}
	
	public void setLocation() {
		map.setMyLocationEnabled(true);
		map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			
			@Override
			public void onMyLocationChange(Location location) {
				// TODO Auto-generated method stub
				if (!userTouch) {
					fixCamera(location);
				}
			}
		});

	}

	public void stopUpdates()
	{
		//locationManager.removeUpdates(list);
		
	}
	
	public void startUpdate()
	{
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, list);
	}

	public boolean setBuildingPin(String username, String first_name, String last_name, String office_string, ListView mDrawerList) {
		this.mDrawerList = mDrawerList;
		StringTokenizer strTok = new StringTokenizer(office_string, ".");
		if (marker != null)
		{
			marker.remove();
			marker = null;
		}
		this.username = username;
		String nameTitle = first_name + " " + last_name;
		
		this.office_string = office_string;
		
		try {
			building_num = Integer.parseInt(strTok.nextToken());
			floor_num = Integer.parseInt(strTok.nextToken());
			cube_num = Integer.parseInt(strTok.nextToken());
		} catch (Exception e)
		{
			Toast.makeText(context, "Office number poorly formed. Cannot determine location", Toast.LENGTH_LONG).show();
			return false;
		}
		System.out.println("RANDY office num: " + office_string);
		System.out.println("RANDY: BUILDING NUMBER: " + building_num);
		switch (building_num)
		{
			case 1: 
				markerOption =  new MarkerOptions()
			        .position(new LatLng(BuildingLocations.BUILDING_1_LAT, BuildingLocations.BUILDING_1_LONG))
			        .title(nameTitle).snippet(office_string);
				break;
			case 2: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_2_LAT, BuildingLocations.BUILDING_2_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 3: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_3_LAT, BuildingLocations.BUILDING_3_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 4: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_4_LAT, BuildingLocations.BUILDING_4_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 5: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_5_LAT, BuildingLocations.BUILDING_5_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 6: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_6_LAT, BuildingLocations.BUILDING_6_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 7: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_7_LAT, BuildingLocations.BUILDING_7_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 8: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_8_LAT, BuildingLocations.BUILDING_8_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 9: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_9_LAT, BuildingLocations.BUILDING_9_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 10: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_10_LAT, BuildingLocations.BUILDING_10_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 11: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_11_LAT, BuildingLocations.BUILDING_11_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 12: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_12_LAT, BuildingLocations.BUILDING_12_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 13: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_13_LAT, BuildingLocations.BUILDING_13_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 14: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_14_LAT, BuildingLocations.BUILDING_14_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			case 15: 
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_15_LAT, BuildingLocations.BUILDING_15_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
			default:
				markerOption =  new MarkerOptions()
		        .position(new LatLng(BuildingLocations.BUILDING_15_LAT, BuildingLocations.BUILDING_15_LONG))
		        .title(nameTitle).snippet(office_string);
				break;
		}
		marker = map.addMarker(markerOption);
		marker.showInfoWindow();
		fixZoom();
		
		//check if the building is already too close to the user if they're close return true, else return false;
		return false;
	}
	
	private void fixZoom() {
		
		if (marker == null)
		{
			return;
		}

	    LatLngBounds.Builder bc = new LatLngBounds.Builder();
		Location location = map.getMyLocation();
	    LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
	    bc.include(marker.getPosition());
	    bc.include(myLoc);
	    LatLngBounds bounds = bc.build();
	    View view = (View) activity.getFragmentManager().findFragmentById(R.id.map).getView();
	    //768 and 1038 are numbers for the screen size of nexus 4
	    CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 768, 1038, 150);
	    map.animateCamera(update);
	    System.out.println("RANDY DISTANCE: " + distance(marker.getPosition(), new LatLng(location.getLatitude(), location.getLongitude())));
	    if (distance(marker.getPosition(), new LatLng(location.getLatitude(), location.getLongitude())) < 50)
	    {
	    	int[] loc = findClosestEntrance(location);
	    	loadIndoorMap(loc[0], loc[1]);
	    }
	  
	    
	}
	
	public int[] findClosestEntrance(Location location) {
		ArrayList<EntrancePoint> entrance_points = null;
		int entrance_id = 0;
		int[] myLoc = new int[2];
		switch(building_num) {
			case 3:
				entrance_points = Building3.getEntrancePoints();
				break;
			case 7:
				entrance_points = Building7.getEntrancePoints();
				break;
			default:
				entrance_points = Building7.getEntrancePoints();
				break;
		}
		LatLng current_location = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
		
		double shortest_distance = Double.MAX_VALUE;
		LatLng testpoint = null;
		for( EntrancePoint point: entrance_points ) {
			double current_distance;
			
			current_distance = distance( current_location, new LatLng(point.getLatitude(), point.getLongitude()) );
			
			if(current_distance < shortest_distance) {
				shortest_distance = current_distance;
				entrance_id = point.getId();
				myLoc[0] = point.getX();
				myLoc[1] = point.getY();
			}
		}
		return myLoc;

	}
	
	public void loadIndoorMap(int myX, int myY)
	{
    	FragmentManager fragmentManager = activity.getFragmentManager();
    	if (MapViewActivity.fragment != null)
    	{
    		fragmentManager.beginTransaction().remove(MapViewActivity.fragment).commit();
    		MapViewActivity.fragment=null;
    	}
    	
    	int x, y;
    	OfficeCoordinates.PxCoord coord = cubemap.get(office_string);
    	if (coord != null)
    	{
    		x = coord.x;
    		y = coord.y;
    	} else {
    		x = -1;
    		y = -1;
    	}
    	
    	float[] path = null;
        if (myX != -1 && myY != -1 && x != -1 && x != -1) {
        	path = ShortestPathEngine.createPath(myX, myY, x, y, activity);
        }
        IndoorParameters params;
    	if (path != null)
    		params = new IndoorParameters(username, building_num, floor_num-1, myX, myY, x, y, path, activity);//coords[0], coords[1], activity);
    	else
    		params = new IndoorParameters(username, building_num, floor_num-1, myX, myY, x, y, null, activity);
	IndoorMapFragment fragment=null;
		try {
			
			new AsyncLoadIndoorMap().execute(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//new IndoorMapFragment();

          //fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
          activity.setTitle(building_names[building_num]);
          mDrawerList.setItemChecked(building_num, true);
          MapViewActivity.building_num = building_num;
          //should we remove the marker? dont know but doing it.
          marker.remove();
          marker = null;
          stopUpdates();
          foundPerson = true;
   
  		SpinnerAdapter mSpinnerAdapter=null;
  		switch(building_num) {
  		case 3:
  			mSpinnerAdapter = ArrayAdapter.createFromResource(activity, R.array.building1floors,
  			          android.R.layout.simple_spinner_dropdown_item);
  			break;
  		case 7:
  			mSpinnerAdapter = ArrayAdapter.createFromResource(activity, R.array.building7floors,
  			          android.R.layout.simple_spinner_dropdown_item);
  			break;
  		default:
  			mSpinnerAdapter = ArrayAdapter.createFromResource(activity, R.array.building1floors,
  			          android.R.layout.simple_spinner_dropdown_item);
  			break;
  		}

  		OnNavigationListener mOnNavigationListener = MapViewActivity.mOnNavigationListener;
  			activity.getActionBar().setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
          
          
          activity.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
          activity.getActionBar().setSelectedNavigationItem(floor_num-1);
	}
	
	public void clearMap()
	{
		if (marker != null) {
			marker.remove();
			marker = null;
		}
		
		fixCamera(map.getMyLocation());		
	}
	
	//in meters
	public static double distance(LatLng StartP, LatLng EndP) {
	    double lat1 = StartP.latitude;
	    double lat2 = EndP.latitude;
	    double lon1 = StartP.longitude;
	    double lon2 = EndP.longitude;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLon = Math.toRadians(lon2-lon1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	    Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.asin(Math.sqrt(a));
	    System.out.println("RANDY DISTANCE: " + (6366000 * c));
	    return 6366000 * c;
	}
}
