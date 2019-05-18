package com.example.myapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<Park> parks;
    List<Address> address = null;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        bundle = intent.getExtras();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            Users user = bundle.getParcelable("userObject");
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            address = geocoder.getFromLocationName(user.getAddress()+" "+user.getPostcode(), 1);
            String lat = Double.toString(address.get(0).getLatitude());
            String lng = Double.toString(address.get(0).getLongitude());
            new PlacesAPI().execute(lat,lng);
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Some error occurred!",Toast.LENGTH_LONG).show();
        }

    }
    private class PlacesAPI extends AsyncTask<String,Void,String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String[] parameters = new String[]{"location","radius","type"};
            String[] values = {params[0]+","+params[1],"5000","park"};
            return com.example.myapplication.PlacesAPI.callAPI(parameters,values);
        }

        @Override
        protected void onPostExecute(String s) {
            parks = com.example.myapplication.PlacesAPI.getParks(s);
            LatLng location = new LatLng(-34, 151);
            String subLocal ="" ;
            if(address != null)
            {
                for(Address add : address) {
                    location = new LatLng(add.getLatitude(), add.getLongitude());
                    subLocal = add.getThoroughfare();
                }
            }

            // Setting markers of parks
            if(parks != null)
            {
                for(Park park : parks)
                {
                    LatLng loc = new LatLng(park.getLatitude(),park.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(loc).title(park.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }
            }

            mMap.addMarker(new MarkerOptions().position(location).title(subLocal));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
        }
    }
}
