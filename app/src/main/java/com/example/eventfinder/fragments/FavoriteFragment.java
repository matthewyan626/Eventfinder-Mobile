package com.example.eventfinder.fragments;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ObservableMap;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventfinder.DetailsActivity;
import com.example.eventfinder.EventFinderMainActivity;
import com.example.eventfinder.R;
import com.example.eventfinder.fragments.Objects.Event;
import com.example.eventfinder.fragments.adapters.FavoriteListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FavoriteFragment extends Fragment implements FavoriteListAdapter.FavoriteClickListener{

    private ActivityResultLauncher<Intent> favoriteResult;
    private View view;
    FavoriteListAdapter adapter;
    private ConstraintLayout noFav;
    private RecyclerView favRecycler;

    private ArrayList<Event> sortedFav = new ArrayList<>();
    private String eventId;
    private String eventName;
    private String eventUrl;
    private boolean isFavorite;
    private Context context;



    public FavoriteFragment(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorite_page, container, false);

        noFav = view.findViewById(R.id.noFavorites);
        favRecycler = view.findViewById(R.id.favRecycler);
        favRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FavoriteListAdapter(getActivity(), sortedFav);
        adapter.setClickListener(this);
        favRecycler.setAdapter(adapter);


        ItemTouchHelper.SimpleCallback swipe = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                FavoriteListAdapter.ViewHolder favoriteViewHolder = (FavoriteListAdapter.ViewHolder) viewHolder;
                String id = favoriteViewHolder.idView.getText().toString();
                String eventName = favoriteViewHolder.nameView.getText().toString();
                String message = eventName + " removed from favorites";
                Snackbar.make(favRecycler, message, Snackbar.LENGTH_SHORT).show();
                EventFinderMainActivity.removeFav(id, context);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe);
        itemTouchHelper.attachToRecyclerView(favRecycler);

        if (EventFinderMainActivity.favoriteEvents.isEmpty()){
            Log.d("Favorites now empty", "switching visibilities");
            noFav.setVisibility(View.VISIBLE);
            favRecycler.setVisibility(View.GONE);
        }else{
            noFav.setVisibility(View.GONE);
            favRecycler.setVisibility(View.VISIBLE);
            ArrayList<Event> fav = new ArrayList<>();
            for (int i = 0; i < EventFinderMainActivity.favoriteEvents.size(); i++){
                fav.add(EventFinderMainActivity.favoriteEvents.get(i));
            }
            adapter.updateFavorites(fav);
        }

        EventFinderMainActivity.favoriteEvents.addOnListChangedCallback(new ObservableList.OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                if (EventFinderMainActivity.favoriteEvents.isEmpty()){
                    Log.d("Favorites now empty", "switching visibilities");
                    noFav.setVisibility(View.VISIBLE);
                    favRecycler.setVisibility(View.GONE);
                }else{
                    Log.d("Favorites not empty", "switching visibilities");
                    noFav.setVisibility(View.GONE);
                    favRecycler.setVisibility(View.VISIBLE);
                    ArrayList<Event> fav = new ArrayList<>();
                    for (int i = 0; i < EventFinderMainActivity.favoriteEvents.size(); i++){
                        fav.add(EventFinderMainActivity.favoriteEvents.get(i));
                    }
                    adapter.updateFavorites(fav);
                }
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                if (EventFinderMainActivity.favoriteEvents.isEmpty()){
                    Log.d("Favorites now empty", "switching visibilities");
                    noFav.setVisibility(View.VISIBLE);
                    favRecycler.setVisibility(View.GONE);
                }else{
                    Log.d("Favorites not empty", "switching visibilities");
                    noFav.setVisibility(View.GONE);
                    favRecycler.setVisibility(View.VISIBLE);
                    ArrayList<Event> fav = new ArrayList<>();
                    for (int i = 0; i < EventFinderMainActivity.favoriteEvents.size(); i++){
                        fav.add(EventFinderMainActivity.favoriteEvents.get(i));
                    }
                    adapter.updateFavorites(fav);
                }
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                if (EventFinderMainActivity.favoriteEvents.isEmpty()){
                    Log.d("Favorites now empty", "switching visibilities");
                    noFav.setVisibility(View.VISIBLE);
                    favRecycler.setVisibility(View.GONE);
                }else{
                    Log.d("Favorites not empty", "switching visibilities");
                    noFav.setVisibility(View.GONE);
                    favRecycler.setVisibility(View.VISIBLE);
                    ArrayList<Event> fav = new ArrayList<>();
                    for (int i = 0; i < EventFinderMainActivity.favoriteEvents.size(); i++){
                        fav.add(EventFinderMainActivity.favoriteEvents.get(i));
                    }
                    adapter.updateFavorites(fav);
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                if (EventFinderMainActivity.favoriteEvents.isEmpty()){
                    Log.d("Favorites now empty", "switching visibilities");
                    noFav.setVisibility(View.VISIBLE);
                    favRecycler.setVisibility(View.GONE);
                }else{
                    Log.d("Favorites not empty", "switching visibilities");
                    noFav.setVisibility(View.GONE);
                    favRecycler.setVisibility(View.VISIBLE);
                    ArrayList<Event> fav = new ArrayList<>();
                    for (int i = 0; i < EventFinderMainActivity.favoriteEvents.size(); i++){
                        fav.add(EventFinderMainActivity.favoriteEvents.get(i));
                    }
                    adapter.updateFavorites(fav);
                }
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                if (EventFinderMainActivity.favoriteEvents.isEmpty()){
                    Log.d("Favorites now empty", "switching visibilities");
                    noFav.setVisibility(View.VISIBLE);
                    favRecycler.setVisibility(View.GONE);
                }else{
                    Log.d("Favorites not empty", "switching visibilities");
                    noFav.setVisibility(View.GONE);
                    favRecycler.setVisibility(View.VISIBLE);
                    ArrayList<Event> fav = new ArrayList<>();
                    for (int i = 0; i < EventFinderMainActivity.favoriteEvents.size(); i++){
                        fav.add(EventFinderMainActivity.favoriteEvents.get(i));
                    }
                    adapter.updateFavorites(fav);
                }
            }
        });
        return view;
    }


    public void onFavoriteClick(View view, int position, String id, Event event) {
        Log.d("clicked item with id:", id);

        eventId = id;
        eventName = event.getName();
        eventUrl = event.getUrl();

        switchActivity();
    }

    public void emptyCheck(int count){
        if (count == 0){
            noFav.setVisibility(View.VISIBLE);
            favRecycler.setVisibility(View.GONE);
        }
    }

    private void switchActivity(){
        Log.d("Switching bc all done", "yay");
        Intent myIntent = new Intent(getActivity(), DetailsActivity.class);
        myIntent.putExtra("id", eventId);
        myIntent.putExtra("name", eventName);
        myIntent.putExtra("url", eventUrl);
        myIntent.putExtra("isFavorite", isFavorite);
        Log.d("eventName is:", eventName);
        Log.d("event is favorite?", Boolean.toString(isFavorite));
        favoriteResult.launch(myIntent);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        favoriteResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent res = result.getData();
                        isFavorite = res.getExtras().getBoolean("isFav");
                        Log.d("Is returned favorite?", Boolean.toString(isFavorite));
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}