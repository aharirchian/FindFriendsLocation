package se.mah.ab8283.p2location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AMIN HARIRCHIAN on 2018/10/22 .
 */

public class FragmentShowMaps extends android.support.v4.app.Fragment {

    Controller controller;
    MainActivity mainActivity;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private MapView theMap;
    String longi = "Can't find";
    String lati = "Can't find";

    private List<Group> content = new ArrayList<Group>();
    private RecyclerView rvExpenses;
    private GroupAdapter groupAdapter;


    public FragmentShowMaps() {
        // Required empty public constructor
    }

    public void setMain(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @SuppressLint("MissingPermission")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_show_maps, container, false);
        initializeComponents(view);
        theMap = view.findViewById(R.id.mapView);
        theMap.onCreate(savedInstanceState);
        theMap.onResume();



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity);


        theMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMyLocationEnabled(true);
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                // LatLng sydney = new LatLng(-34, 151);
                //  googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                //  CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                //  googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return view;
    }


    public String getLocation() {

        FusedLocationProviderClient mFusedLocationProviderClient;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mainActivity);

        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(mainActivity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //  LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    String longi = String.valueOf(location.getLongitude());
                    String lati = String.valueOf(location.getLatitude());

                    System.out.println(" Location:  long " + longi + "    Latitude " + lati);
                    sendLocations(longi, lati);

                } else {
                }
            }
        });
        return longi + "," + lati;
    }

    public void sendLocations(String longi, String lati){
        this.longi = longi;
        this.lati = lati;
    }


    private void initializeComponents(final View view) {


        Button back = view.findViewById(R.id.btnBackFromMaps);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.goToMain();
            }
        });

        rvExpenses = view.findViewById(R.id.rvGroups);
        rvExpenses.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupAdapter = new GroupAdapter(getActivity(), content);
        groupAdapter.setController(controller);
        rvExpenses.setAdapter(groupAdapter);

    }

    public void setContent(ArrayList<String> groups) {
        content.clear();
        System.out.println("showGroups " + groups.size());
        for(int i = 0; i<groups.size(); i++){
            Group group = new Group(groups.get(i), 0);
            if(!(group.getName().equals("groups:["))){
                content.add(group);
                System.out.println("showGroups adding! " +group.getName());
            }

        }
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void showMembersLocation(ArrayList<String> theOnesIneedNow) {
        if(theOnesIneedNow.size() == 0){
        }

        else{
            for(int i = 0; i<theOnesIneedNow.size(); i++){
                String[] test = theOnesIneedNow.get(i).split(",");
                System.out.println(" Marking the member on the map " + test[0] + " " + test[1] + "  " + test[2] + "   " + test[3]);
                LatLng newPos = new LatLng(Double.parseDouble(test[3]), Double.parseDouble(test[2]));
                googleMap.addMarker(new MarkerOptions().position(newPos).title("Marker Title").snippet(test[1]));

            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
                    if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;}
                    getLocation();
                } else {
                    //Permission denied - handle
                }
                break;
        }
    }
}
