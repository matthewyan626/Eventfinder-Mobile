package com.example.eventfinder;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableMap;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.eventfinder.fragments.Objects.Event;
import com.example.eventfinder.fragments.adapters.FavoriteListAdapter;
import com.example.eventfinder.fragments.adapters.ViewPagerAdapter;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class EventFinderMainActivity extends AppCompatActivity{

    public static final ObservableArrayList<String> favoriteIds = new ObservableArrayList<>();
    public static final ObservableArrayList<Event> favoriteEvents = new ObservableArrayList<>();

    private static ArrayList<String> savedIds;
    private static ArrayList<Event> savedEvents;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private FrameLayout frameLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Eventfinder);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(this);

        loadIdData();
        loadEventData();

        viewPager.setAdapter(viewPagerAdapter);
        frameLayout = findViewById(R.id.framelayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                    case 1:
                        tabLayout.getTabAt(position).select();
                }
                super.onPageSelected(position);
            }
        });
    }


    public static void addFav(String id, Event e, Context context){
        favoriteIds.add(id);
        favoriteEvents.add(e);
        savedIds.add(id);
        savedEvents.add(e);
        saveIdData(context);
        saveEventData(context);
    }

    public static void removeFav(String id, Context context){
        Log.d("Listened to change", "attempting remove");
        if (favoriteIds.contains(id)){
            int index = favoriteIds.indexOf(id);
            favoriteIds.remove(index);
            favoriteEvents.remove(index);
            savedIds.remove(index);
            savedEvents.remove(index);
            saveIdData(context);
            saveEventData(context);
        }
    }

    private static void startAddFav(String id, Event e){
        favoriteIds.add(id);
        favoriteEvents.add(e);
    }

    private void loadIdData(){
        Log.d("Trying to load", " Ids");
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String idJson = sharedPreferences.getString("ids", null);
        Type ids = new TypeToken<ArrayList<String>>() {}.getType();
        savedIds = gson.fromJson(idJson, ids);
        if (savedIds == null){
            Log.d("Empty saved ids", "yup");
            savedIds = new ArrayList<>();
        }
    }

    private void loadEventData(){
        Log.d("Trying to load", " Events");
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String eventJson = sharedPreferences.getString("events", null);
        Type ids = new TypeToken<ArrayList<Event>>() {}.getType();
        savedEvents = gson.fromJson(eventJson, ids);
        if (savedEvents == null){
            Log.d("Empty saved events", "yup");
            savedEvents = new ArrayList<>();
        }else{
            for (int i = 0; i < savedEvents.size(); i++){
                startAddFav(savedIds.get(i),savedEvents.get(i));
            }
        }
    }

    public static void saveIdData(Context context){
        Log.d("Destroyed", "Saving Ids");
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(savedIds);
        editor.putString("ids", json);
        editor.apply();
    }

    private static void saveEventData(Context context){
        Log.d("Destroyed", "Saving Events");
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(savedEvents);
        editor.putString("events", json);
        editor.apply();
    }

}