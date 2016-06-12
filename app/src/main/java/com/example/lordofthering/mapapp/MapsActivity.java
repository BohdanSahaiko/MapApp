package com.example.lordofthering.mapapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar toolbar;
    String name;
    double lat, lng;
    double min = 1000000;
    List<LatLng> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        list = new ArrayList();
    }

    void funk() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {return;}
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        lat = location.getLatitude();
        lng = location.getLongitude();
        LatLng mycoor= new LatLng(lat,lng);
        for(LatLng lng:list){
        double distanceBetween = SphericalUtil.computeDistanceBetween(mycoor,lng);
        if(distanceBetween<min) {
          //  name = nameofrest;
            min = distanceBetween;
        }
        }
}
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_mapss:
                funk();
                Toast.makeText(MapsActivity.this,"Найближчий ресторан:"+ name +" ,на відстані:"+(int) min + " метри", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng raf;
        raf = new LatLng(49.8382395, 24.0307135);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(raf, 13));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        for (String i : Main.getGeomanser()) {
            int r = i.indexOf(",");
            int n = i.indexOf("}");
            int nom = i.indexOf("№");
            double rs = Double.parseDouble(i.substring(4, r));
            double ns = Double.parseDouble(i.substring(r + 1, n));
            String nameofrest = i.substring(n + 1, nom);
            String subtext = i.substring(nom + 1, i.length());
            raf = new LatLng(rs, ns);
            map.addMarker(new MarkerOptions()
                    .title(nameofrest).snippet(subtext)
                    .position(raf));
            list.add(raf);
        }

    }
}
