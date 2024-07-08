package com.example.eventfinder.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.eventfinder.R;
import com.example.eventfinder.fragments.Objects.Event;
import com.example.eventfinder.fragments.adapters.AutoFillAdapter;
import com.example.eventfinder.fragments.api.ApiCall;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {


    private View view;
    private CardView searchForm;
    private AutoCompleteTextView keyword;
    private TextView keywordValidation;
    private EditText distance;
    private Spinner category;
    private Switch autodetect;
    private EditText location;
    private Button searchButton;
    private Button clearButton;
    private JSONObject useLoc;
    private JSONObject loc;
    private JSONObject autoLoc;
    private ProgressBar progressbar;
    private ProgressBar autoCompletePB;

    private ArrayList<Event> eventList;

    private AutoFillAdapter autofillAdapter;
    private static final int TRIGGER_AUTOFILL = 100;
    private static final long AUTOFILL_DELAY = 200;
    private Handler handler;


    private ArrayAdapter<CharSequence> spinAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search_page, container, false);

        getAutodetect();

        searchForm = view.findViewById(R.id.searchCard);
        keyword = view.findViewById(R.id.keyword);
        distance = view.findViewById(R.id.distance);
        category = view.findViewById(R.id.category);
        autodetect = view.findViewById(R.id.autodetect);
        location = view.findViewById(R.id.location);
        searchButton = view.findViewById(R.id.search);
        clearButton = view.findViewById(R.id.clear);
        autoCompletePB = view.findViewById(R.id.autoCompletePB);

        progressbar = view.findViewById(R.id.progressBar);

        setupAutofill(view);
        //SPINNER
        spinAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.categoryArray, R.layout.spinner_item);
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        category.setAdapter(spinAdapter);


        autodetect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    location.setVisibility(View.GONE);
                }else{
                    location.setVisibility(View.VISIBLE);
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                keyword.setText("");
                distance.setText("10");
                category.setSelection(0);
                location.setText("");
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (keyword.getText().toString().equals("")){
                    String message = "Please provide a keyword";
                    Snackbar.make(searchForm, message, Snackbar.LENGTH_SHORT).show();
                }else if (distance.getText().toString().equals("")){
                    String message = "Please provide a distance";
                    Snackbar.make(searchForm, message, Snackbar.LENGTH_SHORT).show();
                }else if (location.getText().toString().equals("") && !autodetect.isChecked()){
                    String message = "Please provide a location";
                    Snackbar.make(searchForm, message, Snackbar.LENGTH_SHORT).show();
                }else{
                    boolean validSearch = true;
                    Log.d("search clicked", "yellow");
                    if (!autodetect.isChecked()) {
                        progressbar.setVisibility(View.VISIBLE);
                        searchForm.setVisibility(View.GONE);
                        String inputloc = location.getText().toString();
                        getGeolocateData(inputloc, new GeolocateCallback() {
                            @Override
                            public void onSuccess(JSONObject loc) {
                                Log.d("returned coords; requesting search", loc.toString());
                                SearchFragment.this.loc = loc;
                                requestSearch();
                            }
                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    } else {
                        requestSearch();
                    }
                }
            }
        });
        return view;
    }

    private void requestSearch(){
        try {
            progressbar.setVisibility(View.VISIBLE);
            searchForm.setVisibility(View.GONE);

            Log.d("this is keyword", keyword.getText().toString());
            Log.d("this is distance", distance.getText().toString());
            Log.d("this is category", category.getSelectedItem().toString());

            double lat;
            double lng;

            if (autodetect.isChecked()){
                lat = autoLoc.getDouble("lat");
                lng = autoLoc.getDouble("lng");
                Log.d("using auto detect loc", "AUTO");
            }else{
                lat = loc.getDouble("lat");
                lng = loc.getDouble("lng");
                Log.d("using input detect loc", "INPUT");
            }

            Log.d("lat", String.valueOf(lat));
            String selectedCat = category.getSelectedItem().toString();
            String reqCat = "";
            if (selectedCat.equals("Music")){
                reqCat += "KZFzniwnSyZfZ7v7nJ";
            }else if (selectedCat.equals("Sports")){
                reqCat += "KZFzniwnSyZfZ7v7nE";
            }else if (selectedCat.equals("Arts & Theatre")){
                reqCat += "KZFzniwnSyZfZ7v7na";
            }else if (selectedCat.equals("Film")){
                reqCat += "KZFzniwnSyZfZ7v7nn";
            }else if (selectedCat.equals("Miscellaneous")){
                reqCat += "KZFzniwnSyZfZ7v7n1";
            }
            JSONObject reqObject = new JSONObject();
            String formattedKeyword = keyword.getText().toString().replaceAll("\\s+", "+");
            reqObject.put("keyword", formattedKeyword);
            reqObject.put("distance", distance.getText().toString());
            reqObject.put("category", reqCat);
            reqObject.put("latitude", String.valueOf(lat));
            reqObject.put("longitude", String.valueOf(lng));
            Log.d("this is request object", reqObject.toString());

            ApiCall.requestSearch(getContext(), reqObject, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        Log.d("search response", response.toString());
                        JSONObject obj = new JSONObject(response);

                        if (!obj.has("_embedded")){
                            eventList = new ArrayList<>();
                            EventListFragment eventListFragment = new EventListFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("eventList", eventList);
                            eventListFragment.setArguments(bundle);
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, eventListFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }else {
                            JSONObject emb = obj.getJSONObject("_embedded");
                            Log.d("embedded response from search", emb.toString());
                            JSONArray events = emb.getJSONArray("events");
                            Log.d("events", events.toString());
                            eventList = parseEvents(events);

                            EventListFragment eventListFragment = new EventListFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("eventList", eventList);
                            eventListFragment.setArguments(bundle);

                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, eventListFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                        progressbar.setVisibility(View.GONE);
                        searchForm.setVisibility(View.VISIBLE);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ArrayList parseEvents(JSONArray eventArr){
        Log.d("event array in parseEvents", eventArr.toString());
        ArrayList<Event> results = new ArrayList<>();
        for (int i = 0; i < eventArr.length(); i++){
            try {
                JSONObject j = eventArr.getJSONObject(i);
                String id = j.getString("id");


                String date = "";
                if (j.has("dates") && j.getJSONObject("dates").has("start") && j.getJSONObject("dates").getJSONObject("start").has("localDate")) {
                    date = j.getJSONObject("dates").getJSONObject("start").getString("localDate");
                    Log.d("local date is:", date);
                }

                String time = "";
                if (j.has("dates") && j.getJSONObject("dates").has("start") && j.getJSONObject("dates").getJSONObject("start").has("localTime")) {
                    String objTime = j.getJSONObject("dates").getJSONObject("start").getString("localTime");
                    LocalTime locTime = LocalTime.parse(objTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("h:mm a");
                    time = locTime.format(outputFormat);
                    Log.d("Formatted time", time);
                }

                String icon = "";
                if (j.has("images") && j.getJSONArray("images").getJSONObject(0).has("url")) {
                    icon = j.getJSONArray("images").getJSONObject(0).getString("url");
                }

                String name = "";
                if (j.has("name")) {
                    name = j.getString("name");
                }

                String genre = "";
                if (j.has("classifications") && j.getJSONArray("classifications").getJSONObject(0).has("segment") && j.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").has("name")) {
                    genre = j.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                }

                String venue = "";
                if (j.has("_embedded") && j.getJSONObject("_embedded").has("venues") && j.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("name")) {
                    venue = j.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                }

                String url = "";
                if (j.has("url")) {
                    url = j.getString("url");
                    Log.d("event url is:", url);
                }

                Event event = new Event(id, date, time, icon, name, genre, venue, url);
                results.add(event);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        String ex = results.get(0).getName();
        Log.d("example event", ex);
        return results;
    }


    private void setupAutofill(View view){
        final AppCompatAutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.keyword);
        autofillAdapter = new AutoFillAdapter(getContext(), R.layout.autocomplete_list_item);
        autofillAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autofillAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        autoCompletePB.setVisibility(View.GONE);
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    handler.removeMessages(TRIGGER_AUTOFILL);
                    handler.sendEmptyMessageDelayed(TRIGGER_AUTOFILL, AUTOFILL_DELAY);
                    autoCompletePB.setVisibility(View.VISIBLE);
                } else {
                    autoCompletePB.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(Looper.getMainLooper(), new Handler.Callback(){

            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == TRIGGER_AUTOFILL){
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())){
                        getAutofillData(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });
    }


    private void getAutofillData(String userInput){
        ApiCall.getAutofill(getContext(), userInput, new Response.Listener<String>() {
            @Override
            public void onResponse(String res){
                List<String> autofillList = new ArrayList<>();
                try{
                    JSONObject responseObject = new JSONObject(res);
                    JSONObject embedded = responseObject.getJSONObject("_embedded");
                    JSONArray arr = embedded.getJSONArray("attractions");
                    for (int i = 0; i < arr.length(); i++){
                        JSONObject row = arr.getJSONObject(i);
                        autofillList.add(row.getString("name"));
                        Log.d("Adding to AutofillList, entry: " + i, row.getString("name"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                autofillAdapter.setData(autofillList);
                autofillAdapter.notifyDataSetChanged();
                autoCompletePB.setVisibility(View.GONE);
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


    private void getAutodetect() {
        Log.d("starting auto detect yay", "blegh");
        ApiCall.getAutodetect(getContext(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("AutoDetect API CALL", response);
                    JSONObject r = new JSONObject(response);
                    String autoString = r.getString("loc");
                    Log.d("auto string is", autoString);
                    String split[] = autoString.split(",");
                    Double lat = Double.parseDouble(split[0]);
                    Double lng = Double.parseDouble(split[1]);
                    JSONObject autoL = new JSONObject();
                    autoL.put("lat", lat);
                    autoL.put("lng", lng);
                    autoLoc = autoL;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


    private JSONObject getGeolocateData(String inputLocation, GeolocateCallback callback){

        final JSONObject[] loc = new JSONObject[1];
        ApiCall.getGeolocate(getContext(), inputLocation, new Response.Listener<String>() {

            @Override
            public void onResponse(String res) {
                try{
                    JSONObject response = new JSONObject(res);
                    Log.d("Postcall response", response.toString());
                    JSONArray results = response.getJSONArray("results");
                    Log.d("makes results", Integer.toString(results.length()));
                    JSONObject loc = new JSONObject();
                    if (results.length() > 0) {
                        JSONObject firstRes = results.getJSONObject(0);
                        JSONObject geometry = firstRes.optJSONObject("geometry");
                        if (geometry != null) {
                            loc = geometry.optJSONObject("location");
                        }
                    }else{
                        Log.d("zero location results why aint it working","34");
                        double lat = 0;
                        double lng = 0;
                        loc.put("lat", lat);
                        loc.put("lng", lng);
                    }
                    Log.d("location lat value", Double.toString(loc.getDouble("lat")));
                    callback.onSuccess(loc);
                }catch (Exception e){
                    callback.onError(e);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        return loc[0];
    }



    private interface GeolocateCallback{
        void onSuccess(JSONObject loc);
        void onError(Exception e);
    }

}