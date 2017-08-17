package com.optimustechproject.project2.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.optimustechproject.project2.Activity.AllTrainings;
import com.optimustechproject.project2.Activity.ProfileActivity;
import com.optimustechproject.project2.Activity.TrainingDetails;
import com.optimustechproject.project2.Models.TrainingsPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.DbHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by satyam on 30/7/17.
 */

public class fragment_dashboard  extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    static MapView mMapView;
    static GoogleMap googleMap=null;
    View parentView;
    static Geocoder geocoder;
    TrainingsPOJO data;
    Gson gson=new Gson();
    Button profile,allTrainings;
    List<Marker> markers=new ArrayList<Marker>();
    //ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        profile=(Button)parentView.findViewById(R.id.profile);
        allTrainings=(Button)parentView.findViewById(R.id.allTrainings);

        allTrainings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AllTrainings.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        data= gson.fromJson(DbHandler.getString(getContext(),"training_details","{}"),TrainingsPOJO.class);

        final Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
        //final GPSTracker gpsTracker=new GPSTracker(getContext());

        mMapView = (MapView) parentView.findViewById(R.id.mapView);
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onCreate(savedInstanceState);

        //mMapView.onDestroy();
        // needed to get the map to display immediately

        mMapView.getMapAsync(this);

        return parentView;
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

        googleMap.setOnMarkerClickListener(this);

        for(int i=0;i<data.getVenue().size();i++) {
            try {
                double lati = Double.valueOf(data.getVenueLatitude().get(i));
                double longi = Double.valueOf(data.getVenueLongitude().get(i));

                LatLng loc = new LatLng(lati, longi);

                Marker marker;

                marker=googleMap.addMarker(new MarkerOptions().position(loc).title(data.getTitle().get(i)).snippet(data.getDescription().get(i)));
                //CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(12).build();
                //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                markers.add(marker);

            }
            catch (Exception e){
            }


        }
       // progressDialog.dismiss();


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        final int ind=markers.indexOf(marker);
        //Toast.makeText(getContext(),String.valueOf(ind),Toast.LENGTH_LONG).show();
        new AlertDialog.Builder(getContext()).setTitle(data.getTitle().get(ind))
                .setMessage(data.getCategory().get(ind)+"\n"+data.getPrice().get(ind))
                .setPositiveButton("View more", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent=new Intent(getContext(), TrainingDetails.class);
                        intent.putExtra("index",ind);
                        intent.putExtra("from","dashboard");
                        intent.putExtra("key_learning1",data.getKeyLearning1().get(ind));
                        intent.putExtra("key_learning2",data.getKeyLearning1().get(ind));
                        intent.putExtra("key_learning3",data.getKeyLearning1().get(ind));
                        intent.putExtra("title",data.getTitle().get(ind));
                        intent.putExtra("date",data.getDate().get(ind));
                        intent.putExtra("training_id",data.getId().get(ind));
                        intent.putExtra("desc",data.getDescription().get(ind));
                        intent.putExtra("timings",data.getTimings().get(ind));
                        intent.putExtra("duration",data.getDuration().get(ind));
                        intent.putExtra("category",data.getCategory().get(ind));
                        intent.putExtra("enquiry_status",data.getEnquiryStatus().get(ind));
                        intent.putExtra("venue",data.getVenue().get(ind));
                        intent.putExtra("photo",data.getPhoto().get(ind));
                        intent.putExtra("availability",data.getAvailability().get(ind));
                        startActivity(intent);

                    }
                }).create().show();
        //Toast.makeText(getContext(),String.valueOf(ind),Toast.LENGTH_SHORT).show();
        return false;
    }
}
