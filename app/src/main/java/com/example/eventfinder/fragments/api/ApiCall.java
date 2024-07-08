package com.example.eventfinder.fragments.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;



public class ApiCall {

    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;


    public ApiCall(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();
    }


    public static synchronized ApiCall getInstance(Context context){
        if (mInstance == null){
            mInstance = new ApiCall(context);
        }
        return mInstance;
    }


    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }


    public <T> void addRequest(Request req){
        getRequestQueue().add(req);
    }


    public static void getAutodetect(Context context, Response.Listener<String> listener,Response.ErrorListener err){
        String url = "https://ipinfo.io/?token=c1b4830390e0b4";
        Log.d("AutoDetect API CALL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("AutoDetect API CALL", response);
                listener.onResponse(response);
            }
        }, err);
        ApiCall.getInstance(context).addRequest(stringRequest);
    }

    public static void getAutofill(Context context,String userInput, Response.Listener<String> listener, Response.ErrorListener err){
        String url = "https://mobilebackendeventfinder.wl.r.appspot.com/api/search/autofill/?userInput=" + userInput;
        Log.d("url is",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("ApiCall", response);
                listener.onResponse(response);
            }
        }, err);
        ApiCall.getInstance(context).addRequest(stringRequest);
    }


    public static void getGeolocate(Context context, String location, Response.Listener<String> listener, Response.ErrorListener err){
        String formattedLoc = location.replaceAll("\\s+", "+");
        String url = "https://mobilebackendeventfinder.wl.r.appspot.com/api/search/geolocate/?location=" + formattedLoc;
        Log.d("Geolocate Url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("Geolocate Api call", response);
                listener.onResponse(response);
            }
        }, err);
        ApiCall.getInstance(context).addRequest(stringRequest);
    }


    public static void requestSearch(Context context, JSONObject params, Response.Listener<String> listener, Response.ErrorListener err){
        String url = "https://mobilebackendeventfinder.wl.r.appspot.com/api/search?";
        String query = String.format("keyword=%s&distance=%s&category=%s&latitude=%s&longitude=%s",
                params.optString("keyword", ""),
                params.optString("distance", ""),
                params.optString("category", ""),
                params.optString("latitude", ""),
                params.optString("longitude", ""));
        String requestUrl = url + query;
        Log.d("FIXED URL PLEASE", requestUrl);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                response -> {
                    Log.d("Search Results", response.toString());
                    listener.onResponse(response.toString());
                },
                error -> {
                    Log.e("Search Results", error.toString());
                    err.onErrorResponse(error);
                });
        ApiCall.getInstance(context).addRequest(req);
    }


    public static void requestEventDetails(Context context, String params, Response.Listener<String> listener, Response.ErrorListener err){
        String url = "https://mobilebackendeventfinder.wl.r.appspot.com/api/search/getEventDetails?id=";
        String formattedParams = params.replaceAll("\\s+", "+");
        String requestUrl = url + formattedParams;
        Log.d("FIXED URL PLEASE", requestUrl);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                response -> {
                    Log.d("Search Results", response.toString());
                    listener.onResponse(response.toString());
                },
                error -> {
                    Log.e("Search Results", error.toString());
                    err.onErrorResponse(error);
                });
        ApiCall.getInstance(context).addRequest(req);
    }


    public static void requestArtistDetails(Context context, String params, Response.Listener<String> listener, Response.ErrorListener err){
        String url = "https://mobilebackendeventfinder.wl.r.appspot.com/api/search/getArtistDetails?artistName=";
        String formattedParams = params.replaceAll("\\s+", "+");
        String requestUrl = url + formattedParams;
        Log.d("FIXED URL PLEASE", requestUrl);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                response -> {
                    Log.d("Artist Details", response.toString());
                    listener.onResponse(response.toString());
                },
                error -> {
                    Log.e("Search Results", error.toString());
                    err.onErrorResponse(error);
                });
        ApiCall.getInstance(context).addRequest(req);
    }

    public static void requestAlbumCovers(Context context, String params, Response.Listener<String> listener, Response.ErrorListener err){
        String url = "https://mobilebackendeventfinder.wl.r.appspot.com/api/search/getAlbumCovers?id=";
        String formattedParams = params.replaceAll("\\s+", "+");
        String requestUrl = url + formattedParams;
        Log.d("FIXED URL PLEASE", requestUrl);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                response -> {
                    Log.d("Artist Details", response.toString());
                    listener.onResponse(response.toString());
                },
                error -> {
                    Log.e("Search Results", error.toString());
                    err.onErrorResponse(error);
                });
        ApiCall.getInstance(context).addRequest(req);
    }


    public static void getVenueDetails(Context context, String params, Response.Listener<String> listener, Response.ErrorListener err){
        String url = "https://mobilebackendeventfinder.wl.r.appspot.com/api/search/getVenueDetails?venueName=";
        String formattedParams = params.replaceAll("\\s+", "+");
        String requestUrl = url + formattedParams;
        Log.d("FIXED URL PLEASE", requestUrl);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                response -> {
                    Log.d("Venue Details", response.toString());
                    listener.onResponse(response.toString());
                },
                error -> {
                    Log.e("Search Results", error.toString());
                    err.onErrorResponse(error);
                });
        ApiCall.getInstance(context).addRequest(req);
    }
}
