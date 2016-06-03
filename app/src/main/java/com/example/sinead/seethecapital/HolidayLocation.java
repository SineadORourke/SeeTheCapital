package com.example.sinead.seethecapital;
//Commented by Tommy Coyne

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class HolidayLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String holidayName="";
    private List<Address> addressList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_location);

        Bundle holidayData = getIntent().getExtras();                                                 //receive the extra information passed through intent
        holidayName = holidayData.getString("holidayName");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        EditText destination = (EditText)findViewById(R.id.TFlocale)                                  //set editText box to the location name
        destination.setText(holidayName);                                                             //the user just has to press search to get the location

        LinearLayout linLayout = (LinearLayout)findViewById(R.id.holidaylocationbackground);          //reference the xml file
        Holiday info = new Holiday (this);                                                            //make an instance of the database
        info.open();                                                                                  //open the database
        int theme = info.getThemeID(holidayName);                                                     //get the theme ID for this holiday
        info.close();                                                                                 //close the database
        switch(theme){                                                                                //use switch case to set one of the five backgrounds
            case 0:
                linLayout.setBackgroundResource(R.mipmap.beach_main);
                break;
            case 1:
                linLayout.setBackgroundResource(R.mipmap.snow_main);
                break;
            case 2:
                linLayout.setBackgroundResource(R.mipmap.city_main);
                break;
            case 3:
                linLayout.setBackgroundResource(R.mipmap.country_main);
                break;
            case 4:
                linLayout.setBackgroundResource(R.mipmap.main_bg);
                break;
        }



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


    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.TFlocale);
        String location = location_tf.getText().toString();

        if(location !=null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));                   // Sets the marker option to be viewable when setting target.
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));                              // Animates the camera scroll when choosing new destination.


        }
    }

    public void onZoom(View view)
    {
        if(view.getId() == R.id.Bzoomin)                                                            // Controls the zoom in function when the button is pressed
        {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId() == R.id.Bzoomout)                                                           // Controls the zoom in function when the button is pressed
        {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void changeType(View view)
    {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)                                          // Sets map type to normal view at start of operation.
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);                                          // Changes map type to satellite view when button is pressed.
        }
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);                                             // Changes map type back to normal view when button is pressed for a second time.

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    public void setUpMap(){
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));             // This sets a location marker.
        mMap.setMyLocationEnabled(true);                                                            // Location is visible once the location setting on users mobile device is set to on.
    }

    public void toHolidayProfile(View view){
        Intent intent = new Intent("com.example.sinead.seethecapital.HolidayProfilePage");  //go back to the HolidayProfilePage
        intent.putExtra("holidayName", holidayName);                                        //attach holidayName as an extra on the intent
        startActivity(intent);
        finish();                                                                           //finish HolidayLocation activity
    }
}
