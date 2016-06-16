package com.example.lordofthering.mapapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, android.location.LocationListener {
    private Toolbar toolbar;
    String name,addres;
    double min = 100000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MapsActivity.this);
                builderSingle.setTitle("Найближчі ресторани:-");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    MapsActivity.this,
                    android.R.layout.select_dialog_singlechoice);
                for (int i = 0; i < addreses.size(); i++) {
                    arrayAdapter.add(names.get(i) + ", за адресою:"+ addreses.get(i) +" ,на відстані:" + mins.get(i) + " метри");
                }
                builderSingle.setNegativeButton(
                        "Вихід",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                        MapsActivity.this);
                                builderInner.setMessage(strName);
                                builderInner.setTitle("Your Selected Item is");
                                builderInner.setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                builderInner.show();
                            }
                        });
                builderSingle.show();
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
        for (String i : MainActivity.getGeomanser()) {
            LHelper  lHelper = new LHelper(i);
            map.addMarker(new MarkerOptions()
                    .title(lHelper.nameofrest).snippet(lHelper.subtext)
                    .position(lHelper.rest));
        }
    }

    ArrayList<String> addreses;
    public void setAddres(String addres) {
        addreses.add(addres);
    }

    public String getName() {
        return name;
    }

    ArrayList<String> names;
    public void setName(String name) {
        names.add(name);
    }
    ArrayList<Double> mins;
    public void setMin(double min) {
        mins.add(min);
    }

    public void funk() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
    }

    private static class LHelper {
        String nameofrest;
        String subtext;
        LatLng rest;
        public LHelper(String i) {
            int r = i.indexOf(",");
            int n = i.indexOf("}");
            int nom = i.indexOf("№");
            double rs = Double.parseDouble(i.substring(4, r));
            double ns = Double.parseDouble(i.substring(r + 1, n));
            nameofrest = i.substring(n + 1, nom);
            subtext = i.substring(nom + 1, i.length());
            rest = new LatLng(rs, ns);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        names = new ArrayList<String>();
        mins = new ArrayList<Double>();
        addreses = new ArrayList<>();
        double lat = location.getLatitude();
        double lngt = location.getLongitude();
        LatLng mycoor = new LatLng(lat, lngt);
        TreeMap<Double, LHelper> tm = new TreeMap();
        for (String i : MainActivity.getGeomanser()) {
            LHelper lHelper = new LHelper(i);
            double distanceBetween = SphericalUtil.computeDistanceBetween(mycoor, lHelper.rest);
            tm.put(Double.valueOf(Math.round(distanceBetween)), lHelper);
        }
        int k = 0;
        for (Map.Entry<Double, LHelper> entry : tm.entrySet()) {
            if (++k > 5) {
                break;
            }
            LHelper lHelper = entry.getValue();
            setName(lHelper.nameofrest);
            setMin(entry.getKey());
            setAddres(lHelper.subtext);
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
