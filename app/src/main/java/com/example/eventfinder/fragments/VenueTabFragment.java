package com.example.eventfinder.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eventfinder.R;
import com.example.eventfinder.fragments.Objects.EventDetails;
import com.example.eventfinder.fragments.Objects.VenueDetails;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.Objects;


public class VenueTabFragment extends Fragment {

    private VenueDetails details;
    private View view;
    MapView map;
    private GoogleMap googleMap;

    TextView nameInfo;
    TextView addressInfo;
    TextView address2Info;
    TextView contactInfo;
    TextView hoursInfo;
    TextView generalInfo;
    TextView childInfo;
    TextView hoursInfoExp;
    TextView generalInfoExp;
    TextView childInfoExp;

    CardView infoCard;

    LinearLayout nameRow;
    LinearLayout addressRow;
    LinearLayout address2Row;
    LinearLayout contactRow;
    LinearLayout hoursRow;
    LinearLayout generalRow;
    LinearLayout childRow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_venue_tab, container, false);

        Bundle venueArgs = getArguments();

        details = (VenueDetails) venueArgs.getSerializable("details");

        nameInfo = view.findViewById(R.id.nameInfo);
        addressInfo = view.findViewById(R.id.addressInfo);
        address2Info = view.findViewById(R.id.address2Info);
        contactInfo = view.findViewById(R.id.contactInfo);
        hoursInfo = view.findViewById(R.id.hoursInfo);
        hoursInfoExp = view.findViewById(R.id.hoursInfoExp);
        generalInfo = view.findViewById(R.id.generalInfo);
        generalInfoExp = view.findViewById(R.id.generalInfoExp);
        childInfo = view.findViewById(R.id.childInfo);
        childInfoExp = view.findViewById(R.id.childInfoExp);
        infoCard = view.findViewById(R.id.infoCard);
        nameRow = view.findViewById(R.id.nameRow);
        addressRow = view.findViewById(R.id.addressRow);
        address2Row = view.findViewById(R.id.address2Row);
        contactRow = view.findViewById(R.id.contactRow);
        hoursRow = view.findViewById(R.id.hoursRow);
        generalRow = view.findViewById(R.id.generalRow);
        childRow = view.findViewById(R.id.childRow);

        fillDetails();
        nameInfo.setSelected(true);
        addressInfo.setSelected(true);
        address2Info.setSelected(true);
        contactInfo.setSelected(true);


        generalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalInfo.setVisibility(View.GONE);
                generalInfoExp.setVisibility(View.VISIBLE);
            }
        });
        generalInfoExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalInfoExp.setVisibility(View.GONE);
                generalInfo.setVisibility(View.VISIBLE);
            }
        });
        hoursInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoursInfo.setVisibility(View.GONE);
                hoursInfoExp.setVisibility(View.VISIBLE);
            }
        });
        hoursInfoExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoursInfoExp.setVisibility(View.GONE);
                hoursInfo.setVisibility(View.VISIBLE);
            }
        });
        childInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childInfo.setVisibility(View.GONE);
                childInfoExp.setVisibility(View.VISIBLE);
            }
        });
        childInfoExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childInfoExp.setVisibility(View.GONE);
                childInfo.setVisibility(View.VISIBLE);
            }
        });


        map = view.findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.onResume();
        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For dropping a marker at a point on the Map
                LatLng location = new LatLng(details.getLocation()[1], details.getLocation()[0]);
                googleMap.addMarker(new MarkerOptions().position(location).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        return view;
    }


    private void fillDetails(){
        if (!Objects.equals(details.getName(), "")){
            nameInfo.setText(details.getName());
        }else{
            nameRow.setVisibility(View.GONE);
        }
        if (!Objects.equals(details.getAddress(), "")){
            addressInfo.setText(details.getAddress());
        }else{
            addressRow.setVisibility(View.GONE);
        }
        if (!Objects.equals(details.getLineTwo(), "")){
            address2Info.setText(details.getLineTwo());
        }else{
            address2Row.setVisibility(View.GONE);
        }
        if (!Objects.equals(details.getContact(), "")){
            contactInfo.setText(details.getContact());
        }else{
            contactRow.setVisibility(View.GONE);
        }
        if (!Objects.equals(details.getOpenHours(), "")){
            hoursInfo.setText(details.getOpenHours());
            hoursInfoExp.setText(details.getOpenHours());
        }else{
            hoursRow.setVisibility(View.GONE);
        }
        if (!Objects.equals(details.getGeneralRule(), "")){
            generalInfo.setText(details.getGeneralRule());
            generalInfoExp.setText(details.getGeneralRule());
        }else{
            generalRow.setVisibility(View.GONE);
        }
        if (!Objects.equals(details.getChildRule(), "")){
            childInfo.setText(details.getChildRule());
            childInfoExp.setText(details.getChildRule());
        }else{
            childRow.setVisibility(View.GONE);
        }
        if (Objects.equals(details.getChildRule(), "") && Objects.equals(details.getGeneralRule(), "") && Objects.equals(details.getOpenHours(), "")){
            infoCard.setVisibility(View.GONE);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }
}