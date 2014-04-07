package com.example.searchbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

public class MyMapFragment extends MapFragment {
  public View mOriginalContentView;
  public TouchWrapper mTouchView;   

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);    
    mTouchView = new TouchWrapper(getActivity());
    mTouchView.addView(mOriginalContentView);
    return mTouchView;
  }

  @Override
  public View getView() {
    return mOriginalContentView;
  }
}