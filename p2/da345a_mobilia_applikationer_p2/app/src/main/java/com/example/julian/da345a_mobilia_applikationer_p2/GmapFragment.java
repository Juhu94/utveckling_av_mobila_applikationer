package com.example.julian.da345a_mobilia_applikationer_p2;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Fragment that uses google maps to display a group on the map.
 * Marks every member of the group on the map with google maps marker.
 */
public class GmapFragment extends Fragment {
    private final String TAG = "MAP FRAGMENT";
    private GoogleMap mMap;
    private Button btnBack;

    private Controller controller;
    private ArrayList<Marker> markers = new ArrayList<>();

    private MapView mMapView;

    public GmapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment'
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        btnBack = view.findViewById(R.id.btnBack);

        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                //updateGroupPos();
                // For dropping a marker at a point on the Map
                // LatLng sydney = new LatLng(-34, 151);
                //  googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                //  CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                //  googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.goToMainMenu();
            }
        });

        return view;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    /**
     * Method for updating the markers on the map.
     * @param jsonArray, the json array used to get the member and the position of the member (LAT LONG).
     */
    public void updateGroupsMarkers(final String jsonArray){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(markers.size() > 0){
                        for(int i = 0; i < markers.size(); i++){
                            markers.get(i).remove();
                        }
                        markers.clear();
                    }
                    JSONArray members = new JSONArray(jsonArray);
                    Log.d(TAG, members.getJSONObject(0).get("member").toString());

                    int length = members.length();
                    for(int i = 0; i < length; i++){
                        double latitude = Double.parseDouble(members.getJSONObject(i).get("latitude").toString());
                        double longitude = Double.parseDouble(members.getJSONObject(i).get("longitude").toString());

                        Log.d(TAG, latitude + " " + longitude);
                        LatLng newMarker = new LatLng(latitude,longitude);
                        //LatLng newMarker = new LatLng(Double.parseDouble(members.getJSONObject(i).get("latitude").toString()), Double.parseDouble(members.getJSONObject(i).get("longitude").toString()));
                        Marker mMarker = mMap.addMarker(new MarkerOptions().position(newMarker).title(members.getJSONObject(i).get("member").toString()));
                        markers.add(mMarker);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
