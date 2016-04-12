package com.example.ashwaq.tasktwo;

/*created by: ASH @ashsoo
date: April 10, 2016
TASK 2
 */
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;


public class task2 extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener ,GoogleMap.OnMapClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // adding marker
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //show zoom in zoom out buttons
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // check permission to access user's location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission was denied. Display an error message.
            showPermissionError();
        }
        else if (mMap != null) {
            //permission granted - will show user's location layer - user's location button
            mMap.setMyLocationEnabled(true);
            //set user's location  with zoom 13
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null)
            {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 13));
            }

        }


    }

    //-----------------------End Map Ready--------------------------

    //add marker
    @Override
    public void onMapClick(LatLng point) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
    }

    @Override
    public void onMapLongClick(LatLng point) {
        //clear map so no other markers there
        mMap.clear();
        //check the zoom
        float zoom = mMap.getCameraPosition().zoom;
        if (zoom>=15){
            //add marker with  title
            mMap.addMarker(new MarkerOptions().position( point).title(point.toString())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        }
        else if (zoom<15){
            Toast.makeText(this, R.string.map_zoom, Toast.LENGTH_SHORT).show();
        }

    }


    //-------------END for Adding Marker ----------------------------------

    //toolbar buttons to change map type
    //1- add buttons into action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 2- when user clicked on one of the icons to change map type
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.m_satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;

            case R.id.m_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //--------------End change Map Type--------------------------



    private void showPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}





