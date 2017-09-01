package com.optimustechproject.project2.Fragments;

import android.app.Dialog;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.optimustechproject.project2.Models.LoginDataumPOJO;
import com.optimustechproject.project2.Models.TrainingsPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.DbHandler;

import java.util.Locale;

/**
 * Created by satyam on 31/7/17.
 */

public class dialog_users_location extends DialogFragment implements OnMapReadyCallback {

    View view;
    static MapView mMapView;
    static GoogleMap googleMap=null;
    LoginDataumPOJO data;
    Gson gson=new Gson();

    //////////// DISPLAY USER'S LOCATION ON GOOGLE MAPS IN PROFILE TAB ON LOCATION CLICK ////////

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_users_location, null);
        builder.setView(view);

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        data= gson.fromJson(DbHandler.getString(getContext(),"login_data","{}"),LoginDataumPOJO.class);

        final Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
        //final GPSTracker gpsTracker=new GPSTracker(getContext());

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onCreate(savedInstanceState);

        //mMapView.onDestroy();
        // needed to get the map to display immediately

        mMapView.getMapAsync(this);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap mMap) {

        mMapView.setVisibility(View.VISIBLE);
        final Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());

        googleMap = mMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);

            try {
                double lati = Double.valueOf(data.getLatitude());
                double longi = Double.valueOf(data.getLongitude());

                LatLng loc = new LatLng(lati, longi);

                googleMap.addMarker(new MarkerOptions().position(loc).title(data.getLocation()).snippet(data.getLatitude()+" , "+data.getLongitude()));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
            catch (Exception e){
            }


        }
        // progressDialog.dismiss();

}