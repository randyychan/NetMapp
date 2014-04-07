package com.example.searchbar;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParserException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searchbar.buildinginfo.Building3;
import com.example.searchbar.buildinginfo.Building7;
import com.example.searchbar.buildinginfo.MeetingRooms;
import com.google.android.gms.maps.MapFragment;
 
public class MapViewActivity extends Activity implements SearchView.OnQueryTextListener,
SearchView.OnCloseListener {
 
	private View mLoginStatusView;
	private View layoutView;
    private ListView mListView;
    private SearchView searchView;
    private EmployeesDbAdapter mDbHelper;
    private OutdoorMap map;
    public static Button button;
    private int floor_num;
    public static int building_num;
    private String mlast, mfirst, moffice;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView leftDrawerList;
    private ListView rightDrawerList;
    private String[] building_names;
    private String myUsername;
    private CharSequence mTitle;
    public static Fragment fragment = null;
    //Used to indicate whether or not the database has been previously created on our device
    private boolean database_created = false;
    private MenuItem searchItem;
    //Preference file, used to save database_created info
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String DATABASE_CREATED = "dbcreated";
    public static OnNavigationListener mOnNavigationListener  = null;
    public static FragmentManager fragmentManager;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_menu, menu);
		searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		setUpSearchView(searchItem);
		return true;
	}

	/* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpenL = mDrawerLayout.isDrawerOpen(leftDrawerList);
        boolean drawerOpenR = mDrawerLayout.isDrawerOpen(rightDrawerList);
        boolean drawerOpen = drawerOpenL || drawerOpenR;
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        menu.findItem(R.id.right_btn).setVisible(!drawerOpen);
        menu.findItem(R.id.clear_btn).setVisible(!drawerOpen);
        menu.findItem(R.id.help_btn).setVisible(!drawerOpen);
        menu.findItem(R.id.reload_btn).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }
	
	public void setUpSearchView(MenuItem search)
	{
		searchView.setQueryHint("Search Employees");
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setOnCloseListener(this);
		search.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	}
	

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
       
        	case R.id.reload_btn:
        		new LoadXMLTask().execute("");
        		//showProgress(true);
            		System.out.println("strawberries ontap");
            		if (fragment != null) 
        	    	{
        	    		FragmentManager fragmentManager = getFragmentManager();
        	    		fragmentManager.beginTransaction().remove(fragment).commit();
        	    		fragment = null;
        	    	}
        	        //fragment.setArguments(args);
        	        fragment = new PlanetFragment();
        	        FragmentManager fragmentManager = getFragmentManager();
        	        fragmentManager.beginTransaction().add(R.id.content_frame, fragment, "fragment").commit();
        	        

                    searchView.setQuery("",true);
                    searchItem.collapseActionView();
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                    	      Context.INPUT_METHOD_SERVICE);
                    	imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        		
                    	return true;
        	case R.id.clear_btn:
        		map.clearMap();
        		return true;
        	case R.id.help_btn:
        		//display a help fragment!
        		return true;
        	case R.id.right_btn:
        		if (mDrawerLayout.isDrawerOpen(rightDrawerList)) {
        			mDrawerLayout.closeDrawer(rightDrawerList);
        		} else {
        			mDrawerLayout.openDrawer(rightDrawerList);
        		}
        		return true;
        	case R.id.demo_btn:
        		if (map.marker != null) {
    				map.loadIndoorMap(316, 1912);
        		}
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		map.stopUpdates();
		//Update database_created in our preferences file
	      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putBoolean(DATABASE_CREATED, database_created);

	      //Commit the edits!
	      editor.commit();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		map.startUpdate();
	}

	public void refreshRightDrawer(int building, int floor)
	{
		String[] values = null;
		switch (building) {
		case 0:
			values = MeetingRooms.outdoor();
			break;
		case 3:
			if (floor == 0) {
				values = Building3.floor1();
			} else if (floor == 1) {
				values = Building3.floor2();
			} else if (floor == 2) {
				values = Building3.floor3();
			} 
			break;
		case 7:
			if (floor == 0) {
				values = Building7.floor1();
			} else if (floor == 1) {
				values = Building7.floor2();
			} else if (floor == 2) {
				values = Building7.floor3();
			} 
			break;
		default:
			values = MeetingRooms.defaultAnswer();
			break;
		}
		rightDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, values));
	}
	public void setUpDrawer()
	{
		
		building_names = getResources().getStringArray(R.array.buildings);
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        rightDrawerList = (ListView) findViewById(R.id.right_drawer);
        leftDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, building_names));
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        leftDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        this.rightDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				if (building_num == 7 && floor_num != 2) {
					Toast.makeText(getApplicationContext(), "Conference Room not supported", Toast.LENGTH_SHORT).show();
					return;
				}
				
				System.out.println("Right Drawer onclick View: " + view);
				MyTextView tview = (MyTextView)view;
				String roomName = tview.getText().toString();
				roomName = MeetingRooms.getKey(roomName);
				OfficeCoordinates.PxCoord coord = map.cubemap.get(roomName);
				if(coord != null) {
					if (fragment != null) 
			    	{
			    		FragmentManager fragmentManager = getFragmentManager();
			    		fragmentManager.beginTransaction().remove(fragment).commit();
			    		fragment = null;
			    	}
					System.out.println("RANDYYYYY BUILDING: " + building_num);
					
					
					IndoorParameters params = new IndoorParameters(null,building_num, floor_num, -1, -1, coord.x, coord.y, null,getApplicationContext());
					
					try {
						
						new AsyncLoadIndoorMap().execute(params);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        //fragment.initFragment(building_num, floor_num, -1, -1,-1, -1);//, x, y);
			        
				}
				mDrawerLayout.closeDrawer(rightDrawerList);				
			}
		});
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	if (!mDrawerLayout.isDrawerOpen(leftDrawerList) && !mDrawerLayout.isDrawerOpen(rightDrawerList)) {
            		getActionBar().setTitle(mTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu() 		
            	}
            }

            @Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// TODO Auto-generated method stub
				super.onDrawerSlide(drawerView, slideOffset);
			
            }

			public void onDrawerOpened(View drawerView) {
				if (drawerView.getId() == R.id.left_drawer) {
            		//close right drawer
            		mDrawerLayout.closeDrawer(rightDrawerList);
            		invalidateOptionsMenu();
            		
            		getActionBar().setTitle("Select Indoor Map");
            		 // creates call to onPrepareOptionsMenu()
            	} else {
            		mDrawerLayout.closeDrawer(leftDrawerList);
            		invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            	//	getActionBar().setTitle("Conference Rooms");
            	}
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	
    private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
			
			layoutView.setVisibility(View.VISIBLE);
			layoutView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							layoutView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			layoutView.setVisibility(show ? View.GONE : View.VISIBLE);

		}
	}
    
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
		mLoginStatusView = findViewById(R.id.login_status);
		layoutView = findViewById(R.id.layout);
        button = (Button) findViewById(R.id.maps_button);
        fragmentManager = getFragmentManager();
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button.setVisibility(Button.INVISIBLE);
				map.userTouch = false;
				map.fixCamera(null);
			}
		});
        getActionBar().setIcon(getResources().getDrawable(R.drawable.netapp));
        
        map = new OutdoorMap(((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap(), button, getApplicationContext(), this);
        mListView = (ListView) findViewById(R.id.list);
 
        mDbHelper = new EmployeesDbAdapter(this);
        mDbHelper.open();
        
        //Retrieve info about database_created from preference file
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        database_created = settings.getBoolean(DATABASE_CREATED, false);
        
        String[] params = {};
        //If database not created yet on our device we want to create the database and insert all employee contents
        if (!database_created)
        {
        	new LoadXMLTask().execute(params);
        	//showProgress(true);
        	if (fragment != null) 
	    	{
	    		FragmentManager fragmentManager = getFragmentManager();
	    		fragmentManager.beginTransaction().remove(fragment).commit();
	    		fragment = null;
	    	}
	        //fragment.setArguments(args);
	        fragment = new PlanetFragment();
	        FragmentManager fragmentManager = getFragmentManager();
	        fragmentManager.beginTransaction().add(R.id.content_frame, fragment, "fragment").commit();
        	
        }     
        
        setUpDrawer();
        
        if (savedInstanceState == null) {
            selectFloor(0);
            leftDrawerList.setItemChecked(0, true);
            setSpinner(0);
        }
        
    }
    
	public void setSpinner(int building)
	{
		SpinnerAdapter mSpinnerAdapter=null;
		switch(building) {
		case 3:
			mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.building1floors,
			          android.R.layout.simple_spinner_dropdown_item);
			break;
		case 7:
			mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.building7floors,
			          android.R.layout.simple_spinner_dropdown_item);
			break;
		default:
			mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.building1floors,
			          android.R.layout.simple_spinner_dropdown_item);
			break;
		}

		mOnNavigationListener = new OnNavigationListener() {
			  // Get the same strings provided for the drop-down's ArrayAdapter
			  String[] strings = getResources().getStringArray(R.array.building1floors);

			  @Override
			  public boolean onNavigationItemSelected(int position, long itemId) {
			    // Create new fragment from our own Fragment class
				  System.out.println("strawberries");
				  floor_num = position;
				  if(!map.foundPerson)
				  {
					  loadIndoorMap();
				  }
				  map.foundPerson = false;

				  refreshRightDrawer(building_num, floor_num);
			    return true;
			  }
			};
			getActionBar().setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
	}
	
	public void selectFloor(int position) {
        // update the main content by replacing fragments

		
		if (fragment != null) 
    	{
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    		FragmentManager fragmentManager = getFragmentManager();
    		fragmentManager.beginTransaction().remove(fragment).commit();
    		fragment = null;
    	}
		
        if (position == 0)
        {
        	setTitle("NetMapp");
        	map.startUpdate();
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        } else {
        	//TODO
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			setSpinner(position);
			getActionBar().setSelectedNavigationItem(0);
			loadIndoorMap();
          //fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
           setTitle(building_names[position]);
           map.stopUpdates();
        }
        mDrawerLayout.closeDrawer(leftDrawerList);
        
        refreshRightDrawer(building_num, floor_num);
    }
	
	
	public static int[] getCoordinates(String office_num)
	{
		int[] coords = new int[2];
		//TODO: get coords based on params
		
		
		return coords;
	}
	public void loadIndoorMap()
	{
        // update selected item and title, then close the drawer
		if (fragment != null) 
    	{
    		FragmentManager fragmentManager = getFragmentManager();
    		fragmentManager.beginTransaction().remove(fragment).commit();
    		fragment = null;
    	}
		System.out.println("RANDYYYYY BUILDING: " + building_num);
		
		
		IndoorParameters params = new IndoorParameters(null,building_num, floor_num, -1, -1, -1, -1, null,getApplicationContext());
		
		try {
			
			new AsyncLoadIndoorMap().execute(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//new IndoorMapFragment();

        
	}
	
    @Override
    protected void onStop(){
       super.onStop();

      
    }
    
    private class LoadXMLTask extends AsyncTask<String, Void, List<Employee>> {

        @Override
        protected List<Employee> doInBackground(String... urls) {
            try {
            	//Employee XML located in raw resource folder
            	EmployeeXMLParser employeeParser = new EmployeeXMLParser();            
            	InputStream inputStream = getResources().openRawResource(R.raw.employee_directory2);         
            	List<Employee> result = employeeParser.parse(inputStream);
            	mDbHelper.deleteAllEmployees();
            	for (Employee entry : result) {
                	String employee_num = entry.employee_num;
                	String first_name = entry.first_name;
                	String last_name = entry.last_name;
                	String dept_name = entry.dept_name;
                	String office_num = entry.office_num;
                	String email = entry.email;
                	String username = entry.username;
                	String phone_num = entry.mobile_num;
                	String job_title = entry.job_title;
                	mDbHelper.createEmployee(employee_num, first_name, last_name, dept_name, office_num, email, username, job_title, phone_num);
                }
            	database_created = true;
            	return employeeParser.parse(inputStream);
            } catch (IOException e) {
            	return null;
            } catch (XmlPullParserException e) {
            	return null;
            }
        }

        @Override
        protected void onPostExecute(List<Employee> result) {
        	if (fragment != null) 
	    	{
	    		FragmentManager fragmentManager = getFragmentManager();
	    		fragmentManager.beginTransaction().remove(fragment).commit();
	    		fragment = null;
	    	}        	
        	//showProgress(false);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDbHelper  != null) {
            mDbHelper.close();
        }
    }
 
    public boolean onQueryTextChange(String newText) {
    	showResults(newText + "*");
        return false;
    }
 
    public boolean onQueryTextSubmit(String query) {
    	showResults(query + "*");
        return false;
    }
 
    public boolean onClose() {
        showResults("");
        return false;
    }
 
    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	System.out.println("RANDY CLICKED!");
        	building_num = position;
        	if(!map.foundPerson)
			  {
                	selectFloor(position);
			  }
        	
        }
    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    private void showResults(String query) {
 
    	if (query.equals("strawberries ontap*")) {
    		System.out.println("strawberries ontap");
    		if (fragment != null) 
	    	{
	    		FragmentManager fragmentManager = getFragmentManager();
	    		fragmentManager.beginTransaction().remove(fragment).commit();
	    		fragment = null;
	    	}
	        //fragment.setArguments(args);
	        fragment = new PlanetFragment();
	        FragmentManager fragmentManager = getFragmentManager();
	        fragmentManager.beginTransaction().add(R.id.content_frame, fragment, "fragment").commit();
	        

            searchView.setQuery("",true);
            searchItem.collapseActionView();
            InputMethodManager imm = (InputMethodManager)getSystemService(
            	      Context.INPUT_METHOD_SERVICE);
            	imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
	        
	        return;
    	}
    	
    	
        Cursor cursor = mDbHelper.searchCustomer((query != null ? query.toString() : "@@@@"));
        if (cursor == null) {

        } else {
            // Specify the columns we want to display in the result

            String[] from = new String[] {
                    EmployeesDbAdapter.KEY_FIRST_NAME,
                    EmployeesDbAdapter.KEY_LAST_NAME,
                    EmployeesDbAdapter.KEY_DEPT_NAME,
                    EmployeesDbAdapter.KEY_OFFICE_NUM};   
 
            // Specify the Corresponding layout elements where we want the columns to go
            int[] to = new int[] {     
            		R.id.first_name,
                    R.id.last_name,
                    R.id.dept_name,
                    R.id.office_num};
 
            // Create a simple cursor adapter for the definitions and apply them to the ListView
            SimpleCursorAdapter customers = new SimpleCursorAdapter(this,R.layout.customerresult, cursor, from, to);

            mListView.setAdapter(customers);
            
            // Define the on-click listener for the list items
            mListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the cursor, positioned to the corresponding row in the result set
                
                    Cursor cursor = (Cursor) mListView.getItemAtPosition(position);
 
                    // Get the state's capital from this row in the database.
                    String employee_num = cursor.getString(cursor.getColumnIndexOrThrow("employee_num"));
                    String first_name = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                    String last_name = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                    String office_num = cursor.getString(cursor.getColumnIndexOrThrow("office_num"));
                    String dept_name = cursor.getString(cursor.getColumnIndexOrThrow("dept_name"));
                    String job_title = cursor.getString(cursor.getColumnIndexOrThrow("job_title"));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));

                    if (fragment != null) 
                	{
                		FragmentManager fragmentManager = getFragmentManager();
                		fragmentManager.beginTransaction().remove(fragment).commit();
                		fragment = null;
                    	setTitle("NetMapp");
                        leftDrawerList.setItemChecked(0, true);
                        map.startUpdate();
                	}
                    
                    map.setBuildingPin(username, first_name, last_name, office_num,leftDrawerList);
                   
    
                    searchView.setQuery("",true);
                    searchItem.collapseActionView();
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                    	      Context.INPUT_METHOD_SERVICE);
                    	imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
            });
            mListView.setOnItemLongClickListener(new OnItemLongClickListener()  {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
                    Cursor cursor = (Cursor) mListView.getItemAtPosition(position);
                    System.out.println("RANDY LONG CLICK");
                    
                    String employee_num = cursor.getString(cursor.getColumnIndexOrThrow("employee_num"));
                    String first_name = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                    String last_name = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                    String office_num = cursor.getString(cursor.getColumnIndexOrThrow("office_num"));
                    String dept_name = cursor.getString(cursor.getColumnIndexOrThrow("dept_name"));
                    String job_title = cursor.getString(cursor.getColumnIndexOrThrow("job_title"));
                    String email =cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    String phone_num =cursor.getString(cursor.getColumnIndexOrThrow("phone_num"));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                    
            		mfirst = first_name;
            		mlast = last_name;
            		moffice = office_num;

                    System.out.println("RANDYY EMAIL"+ email);
                    System.out.flush();
                    HashMap<String, String> map = new HashMap<String, String>();
    				map.put("FULLNAME", first_name + " " + last_name);
    				map.put("JOBTITLE", job_title);
    				map.put("DEPTNAME", dept_name);
    				map.put("EMAIL", email);
    				map.put("PHONE", phone_num);
    				map.put("USERNAME", username);
    				map.put("CUBELOCATION", office_num);
    				showDialog(map);
                    
                    searchView.setQuery("",true);
                    searchItem.collapseActionView();
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                  	      Context.INPUT_METHOD_SERVICE);
                  	imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
					return false;
				}
			});
        }
    }
    private ImageView profileImage;
    
	public void showDialog(HashMap<String, String> employeeInfo) {

		final Dialog dialog = new Dialog(this); //change to "MapViewActivity.this"
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.profile_dialog);
		
		
		final TextView fullName = (TextView) dialog.findViewById(R.id.full_name);
		final TextView jobTitle = (TextView) dialog.findViewById(R.id.job_title);
		final TextView deptName = (TextView) dialog.findViewById(R.id.dept_name);
		final TextView unixName = (TextView) dialog.findViewById(R.id.unix_name);
		final Button cube_location = (Button) dialog.findViewById(R.id.cube_location_btn);
		final Button email = (Button) dialog.findViewById(R.id.email_btn);
		final Button phone = (Button) dialog.findViewById(R.id.phone_btn);
		 profileImage = (ImageView) dialog.findViewById(R.id.profile_pic);
		//setting employee info
		fullName.setText(employeeInfo.get("FULLNAME"));
		jobTitle.setText(employeeInfo.get("JOBTITLE"));
		deptName.setText(employeeInfo.get("DEPTNAME"));
		unixName.setText(employeeInfo.get("USERNAME"));
		cube_location.setText(employeeInfo.get("CUBELOCATION"));
		email.setText(employeeInfo.get("EMAIL"));
		phone.setText(employeeInfo.get("PHONE"));
		myUsername =employeeInfo.get("USERNAME");
		
		new LoadImage().execute(employeeInfo.get("USERNAME"));
		
		//button listeners
		cube_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// open map fragment 
				// RANDY LOOK HERE
				map.setBuildingPin(myUsername, mfirst, mlast, moffice,leftDrawerList);
				dialog.dismiss();
			}
			
		});
		
		email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// open intent on emailing this guy
				Intent email_intent = new Intent(android.content.Intent.ACTION_SENDTO);
				
				String uriText = "mailto:" + Uri.encode(email.getText().toString());
				Uri uri = Uri.parse(uriText);
				
				email_intent.setData(uri);
				
				try {
					startActivity(email_intent);
				} catch (android.content.ActivityNotFoundException ex){
					Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// open intent on calling this guy
				Intent phone_intent = new Intent(android.content.Intent.ACTION_DIAL);
				
				String uriText = "tel:" + Uri.encode(phone.getText().toString());
				Uri uri = Uri.parse(uriText);
				
				phone_intent.setData(uri);
				startActivity(phone_intent);
			}
		});
		
		dialog.show();

	}
	
	public void getImage(String imageURL, String destFile, String currId) {
		
		imageURL = "http://cedprod.corp.netapp.com:10080/photos/" + currId + ".jpg";
		destFile = currId + ".jpg";

		new ImageFinder().execute(imageURL, destFile);
		
	}

	
	
	public static boolean saveImage(String imageURL, String destFile) {
		boolean success = true;
		
		try {
			URL url = new URL(imageURL);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destFile);
			
			byte[] b = new byte[2048];
			int length;
			
			while((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			
			is.close();
			os.close();
		} catch (Exception e) {
			System.out.println("Error processing image.");
			e.printStackTrace();
			success = false;
		}		

		return success;
	}

	private class ImageFinder extends AsyncTask<String, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(String... arg) {
			String request = arg[0];
			String dest = arg[1];

			boolean success = saveImage(request, dest);
			System.out.println("Success: " + success);
			return success;
		}
		
		protected void onPostExecute(Boolean success)
		{
			
		}
	}
    
	private class LoadImage extends AsyncTask<String, Void, Bitmap > {

        @Override
        protected Bitmap doInBackground(String... urls) {
        	String url = "http://cedprod.corp.netapp.com:10080/photos/" + urls[0] + ".jpg";

        	try {
        		InputStream is = (InputStream) new URL(url).getContent();
        	       // Drawable d = Drawable.createFromStream(is, "src name");
        	    	System.out.println("RANDY loading LOAD");

        	        return BitmapFactory.decodeStream(is);
            } catch (Exception e) {
            	return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
        	if (profileImage != null && result != null)
        		profileImage.setImageBitmap(result);
        	
  
        }
    }
	
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	ImageView image = new ImageView(getActivity().getApplicationContext());
        	image.setImageResource(R.raw.sontap2);
            getActivity().setTitle("Strawberries ONTAP!");

        	/*
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);

            ((ImageView) rootView.findViewById(R.id.image)).setBackgroundResource(R.drawable.sontap);
            getActivity().setTitle("Strawberries ONTAP!");
            */
            return image;
        }
    }
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
    	if (fragment != null) 
    	{
    		FragmentManager fragmentManager = getFragmentManager();
    		fragmentManager.beginTransaction().remove(fragment).commit();
    		fragment = null;
    	}
    	else {
    		finish();
    	}
        return;
    }   

}