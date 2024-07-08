package com.example.eventfinder.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.eventfinder.DetailsActivity;
import com.example.eventfinder.R;
import com.example.eventfinder.fragments.Objects.ArtistDetails;
import com.example.eventfinder.fragments.Objects.Event;
import com.example.eventfinder.fragments.Objects.EventDetails;
import com.example.eventfinder.fragments.Objects.VenueDetails;
import com.example.eventfinder.fragments.adapters.DetailsViewPageAdapter;
import com.example.eventfinder.fragments.api.ApiCall;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class DetailsFragment extends Fragment {
    TabLayout detailsTabLayout;
    ViewPager2 detailsViewPager;
    DetailsViewPageAdapter detailsViewPageAdapter;
    FrameLayout detailsFrame;


    private ProgressBar progressBar;

    private String eventId;
    private EventDetails eventDetails;
    private ArrayList<ArtistDetails> artistDetails = new ArrayList<>();
    private VenueDetails venueDetails;

    private boolean eventCheck;
    private boolean artistCheck;
    private boolean venueCheck;
    private int artistCount;
    private int otherCount;
    private int totalCount;

    private Handler handler = new Handler(Looper.getMainLooper());
    private long debounceTime = 200;
    private Runnable eventRunnable = null;
    private Runnable artistRunnable = null;
    private Runnable venueRunnable = null;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details_loading, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        Bundle extras = getActivity().getIntent().getExtras();
        eventCheck = false;
        artistCheck = false;
        venueCheck = false;

        if (extras != null) {
            eventId = extras.getString("id");
            Log.d("eventid is:", eventId);
            requestEventInfo(eventId);
        }

        return view;
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.detailsTabLayout = tabLayout;
    }



    private void requestEventInfo(String id) {
        if (eventRunnable != null) {
            handler.removeCallbacks(eventRunnable);
        }
        eventRunnable = new Runnable() {
            @Override
            public void run() {
                ApiCall.requestEventDetails(getContext(), id, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);
                            eventDetails = parseEventDetails(obj);
                            eventCheck = true;
                            if (eventCheck && artistCheck && venueCheck) {
                                switchActivity();
                            }
                            if (obj.has("_embedded") && obj.getJSONObject("_embedded").has("attractions")) {
                                JSONArray artistArr = obj.getJSONObject("_embedded").getJSONArray("attractions");
                                requestArtistInfo(artistArr);
                            }
                            if (obj.has("_embedded") && obj.getJSONObject("_embedded").has("venues")){
                                String venueName = obj.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                requestVenueInfo(venueName);
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        };
        handler.postDelayed(eventRunnable, debounceTime);
    }

    private EventDetails parseEventDetails(JSONObject e){
        EventDetails details = new EventDetails();
        try{
            String name = "";
            String artists = "";
            String venue = "";
            String date = "";
            String time = "";
            String genres = "";
            String priceRange = "";
            String status = "";
            String buyLink = "";
            String seatMap = "";

            String artistName = "";
            String eventGenre = "";

            name = e.getString("name");

            if (e.has("_embedded") && e.getJSONObject("_embedded").has("attractions")) {
                JSONArray attractions = e.getJSONObject("_embedded").getJSONArray("attractions");
                Log.d("attractions array", attractions.toString());
                artists = attractions.getJSONObject(0).getString("name");
                artistName = attractions.getJSONObject(0).getString("name");
                for (int i = 1; i < attractions.length(); i++) {
                    String a = attractions.getJSONObject(i).getString("name");
                    artists = artists + " | " + a;
                }
            }
            Log.d("Formatted Artists", artists);


            if (e.has("_embedded") && e.getJSONObject("_embedded").has("venues") && e.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("name")) {
                venue = e.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
            }

            if (e.has("dates") && e.getJSONObject("dates").has("start") && e.getJSONObject("dates").getJSONObject("start").has("localDate")) {
                date = e.getJSONObject("dates").getJSONObject("start").getString("localDate");
                Log.d("local date is:", date);
            }

            if (e.has("dates") && e.getJSONObject("dates").has("start") && e.getJSONObject("dates").getJSONObject("start").has("localTime")) {
                String objTime = e.getJSONObject("dates").getJSONObject("start").getString("localTime");
                LocalTime locTime = LocalTime.parse(objTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
                DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("h:mm a");
                time = locTime.format(outputFormat);
                Log.d("Formatted time", time);
            }

            JSONObject genreObj = e.getJSONArray("classifications").getJSONObject(0);
            genres = genreObj.getJSONObject("segment").getString("name");
            eventGenre = genreObj.getJSONObject("segment").getString("name");
            if (genreObj.has("genre") && !genreObj.getJSONObject("genre").getString("name").equals("Undefined")) {
                genres += " | " + genreObj.getJSONObject("genre").getString("name");
            }
            if (genreObj.has("subGenre") && !genreObj.getJSONObject("subGenre").getString("name").equals("Undefined")) {
                genres += " | " + genreObj.getJSONObject("subGenre").getString("name");
            }
            if (genreObj.has("type") && !genreObj.getJSONObject("type").getString("name").equals("Undefined")) {
                genres += " | " + genreObj.getJSONObject("type").getString("name");
            }
            if (genreObj.has("subType") && !genreObj.getJSONObject("subType").getString("name").equals("Undefined")) {
                genres += " | " + genreObj.getJSONObject("subType").getString("name");
            }
            Log.d("formatted genres", genres);

            if (e.has("priceRanges") && e.getJSONArray("priceRanges").length() > 0) {
                JSONObject priceObj = e.getJSONArray("priceRanges").getJSONObject(0);
                if (priceObj.has("min") && priceObj.has("max")) {
                    Double min = priceObj.getDouble("min");
                    Double max = priceObj.getDouble("max");
                    priceRange = min.toString() + " - " + max.toString() + " (USD)";
                }
            }

            if (e.has("dates") && e.getJSONObject("dates").has("status") && e.getJSONObject("dates").getJSONObject("status").has("code")) {
                status = e.getJSONObject("dates").getJSONObject("status").getString("code");
                Log.d("status is", status);
            }

            if (e.has("url")) {
                buyLink = e.getString("url");
            }

            if (e.has("seatmap")){
                if (e.getJSONObject("seatmap").has("staticUrl")){
                    seatMap = e.getJSONObject("seatmap").getString("staticUrl");
                }
            }
            String artistIcon = e.getJSONArray("images").getJSONObject(0).getString("url");
            Event eventReturn = new Event(eventId, date, time, artistIcon, artistName, eventGenre, venue, buyLink);
            DetailsActivity.event = eventReturn;

            details = new EventDetails(name, artists, venue, date, time, genres, priceRange, status, buyLink, seatMap);
        }catch (Exception err) {
            err.printStackTrace();
        }
        return details;
    }

    private void requestArtistInfo(JSONArray a) {
        if (artistRunnable != null) {
            handler.removeCallbacks(artistRunnable);
        }
        artistRunnable = new Runnable() {
            @Override
            public void run() {
                try{
                    for (int i = 0; i < a.length(); i++) {
                        if (a.getJSONObject(i).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name").equals("Music")) {
                            totalCount++;
                        }
                    }
                    for (int i = 0; i < a.length(); i++) {
                        if (a.getJSONObject(i).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name").equals("Music")){
                            String artistName = a.getJSONObject(i).getString("name");
                            Log.d("Artist name is", artistName);
                            ApiCall.requestArtistDetails(getContext(), artistName, new Response.Listener<String>(){
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        Log.d("Returned Artist Deets", obj.getJSONObject("body").getJSONObject("artists").getJSONArray("items").getJSONObject(0).toString());
                                        requestAlbumCovers(obj.getJSONObject("body").getJSONObject("artists").getJSONArray("items").getJSONObject(0));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                        }else{
                            artistCheck = true;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(artistRunnable, debounceTime);
    }

    private void requestAlbumCovers(JSONObject a){
        try{
            String artistId = a.getString("id");
            ApiCall.requestAlbumCovers(getContext(), artistId, new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject covers = new JSONObject(response);
                        Log.d("covers object", covers.toString());
                        JSONArray coversArr = covers.getJSONObject("body").getJSONArray("items");

                        parseArtistDetails(a, coversArr);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseArtistDetails(JSONObject a, JSONArray coversArr){
        ArtistDetails details = new ArtistDetails();
        try {
            String name = "";
            String image = "";
            int popularity = 0;
            int followers = 0;
            String spotify = "";
            ArrayList<String> albumCovers = new ArrayList<>();

            if (a.has("name")) {
                name = a.getString("name");
            }

            if (a.has("images") && a.getJSONArray("images").length() > 0) {
                image = a.getJSONArray("images").getJSONObject(0).getString("url");
            }

            if (a.has("popularity")) {
                popularity = a.getInt("popularity");
            }

            if (a.has("followers") && a.getJSONObject("followers").has("total")) {
                followers = a.getJSONObject("followers").getInt("total");
            }

            if (a.has("external_urls") && a.getJSONObject("external_urls").has("spotify")) {
                spotify = a.getJSONObject("external_urls").getString("spotify");
            }


            for (int i = 0; i < coversArr.length(); i++){
                String coverUrl = coversArr.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url");
                Log.d("Album Cover URL", coverUrl);
                albumCovers.add(coverUrl);
            }

            details = new ArtistDetails(name, image, popularity, followers, spotify, albumCovers);

            Log.d("Adding to ArtistDetails", details.getName());
            artistCount++;
            Log.d("# artists in Deets", Integer.toString(artistCount));
            artistDetails.add(details);
            if (artistCount + otherCount == totalCount){
                artistCheck = true;
            }
            if (eventCheck && artistCheck && venueCheck) {
                switchActivity();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void requestVenueInfo(String venueName) {
        if (venueRunnable != null) {
            handler.removeCallbacks(venueRunnable);
        }
        venueRunnable = new Runnable() {
            @Override
            public void run() {
                ApiCall.getVenueDetails(getContext(),venueName, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONObject obj = new JSONObject(response);
                            JSONObject venue = obj.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                            venueDetails = parseVenueDetails(venue);
                            venueCheck = true;
                            if (eventCheck && artistCheck && venueCheck) {
                                switchActivity();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        };
        handler.postDelayed(venueRunnable, debounceTime);
    }

    private VenueDetails parseVenueDetails(JSONObject v){
        VenueDetails deets = new VenueDetails();
        try {
            String name = "";
            String address = "";
            String lineTwo = "";
            String contact = "";
            String openHours = "";
            String generalRule = "";
            String childRule = "";
            double location[] = new double[2];

            if (v.has("name")) {
                name = v.getString("name");
            }
            if (v.has("address") && v.getJSONObject("address").has("line1")) {
                address = v.getJSONObject("address").getString("line1");
            }
            if (v.has("city") && v.getJSONObject("city").has("name")) {
                lineTwo += v.getJSONObject("city").getString("name");
            }
            if (v.has("state") && v.getJSONObject("state").has("name")) {
                lineTwo += ", " + v.getJSONObject("state").getString("name");
            }
            if (v.has("boxOfficeInfo") && v.getJSONObject("boxOfficeInfo").has("phoneNumberDetail")) {
                contact = v.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");
            }
            if (v.has("boxOfficeInfo") && v.getJSONObject("boxOfficeInfo").has("openHoursDetail")) {
                openHours = v.getJSONObject("boxOfficeInfo").getString("openHoursDetail");
            }
            if (v.has("generalInfo") && v.getJSONObject("generalInfo").has("generalRule")) {
                generalRule = v.getJSONObject("generalInfo").getString("generalRule");
            }
            if (v.has("generalInfo") && v.getJSONObject("generalInfo").has("childRule")) {
                childRule = v.getJSONObject("generalInfo").getString("childRule");
            }
            if (v.has("location") && v.getJSONObject("location").has("longitude") && v.getJSONObject("location").has("latitude")){
                location[0] = Double.parseDouble(v.getJSONObject("location").getString("longitude"));
                location[1] = Double.parseDouble(v.getJSONObject("location").getString("latitude"));
            }

            Log.d("long to string", Double.toString(location[0]));

            Log.d("Test general rule", generalRule);
            Log.d("test line2", lineTwo);

            deets = new VenueDetails(name, address, lineTwo, contact, openHours, generalRule, childRule, location);
        }catch (Exception e){
            e.printStackTrace();
        }
        return deets;
    }

    private void switchActivity(){
        Log.d("Switching bc all done", "yay");


        detailsTabLayout = view.findViewById(R.id.detailTabs);
        detailsViewPager = view.findViewById(R.id.detailsViewPager);
        Log.d("Sending these event deets", eventDetails.getName());
        Log.d("Sending these venue deets", venueDetails.getName());

        TabLayout.Tab detailsTab = detailsTabLayout.getTabAt(0);
        TabLayout.Tab artistsTab = detailsTabLayout.getTabAt(1);
        TabLayout.Tab venueTab = detailsTabLayout.getTabAt(2);

        detailsTab.setIcon(R.drawable.info_icon);
        artistsTab.setIcon(R.drawable.artist_icon);
        venueTab.setIcon(R.drawable.venue_icon);

        detailsViewPageAdapter = new DetailsViewPageAdapter(this, eventDetails, artistDetails, venueDetails);
        detailsViewPager.setAdapter(detailsViewPageAdapter);

        Bundle bundle = new Bundle();
        bundle.putSerializable("eventDetails", eventDetails);
        EventTabFragment eventTabFragment = detailsViewPageAdapter.getEventTabFragment();
        eventTabFragment.setArguments(bundle);

        detailsFrame = view.findViewById(R.id.detailsFrame);
        detailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                detailsViewPager.setVisibility(View.VISIBLE);
                detailsFrame.setVisibility(View.GONE);
                detailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                detailsViewPager.setVisibility(View.VISIBLE);
                detailsFrame.setVisibility(View.GONE);
            }
        });

        detailsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                    case 1:
                        detailsTabLayout.getTabAt(position).select();
                }
                super.onPageSelected(position);
            }
        });


        progressBar.setVisibility(View.GONE);
        detailsViewPager.setVisibility(View.VISIBLE);
        detailsFrame.setVisibility(View.GONE);
        detailsTabLayout.setVisibility(View.VISIBLE);
    }

}