package com.example.eventfinder.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import com.example.eventfinder.DetailsActivity;
import com.example.eventfinder.EventFinderMainActivity;
import com.example.eventfinder.R;
import com.example.eventfinder.fragments.Objects.Event;

import com.example.eventfinder.fragments.adapters.EventListAdapter;


import java.util.ArrayList;

public class EventListFragment extends Fragment implements EventListAdapter.ItemClickListener {


    private ActivityResultLauncher<Intent> favoriteResult;
    public EventListAdapter eventListAdapter;
    private RecyclerView recyclerView;
    private ConstraintLayout noEvents;

    private String eventId;
    private String eventName;
    private String eventUrl;
    private boolean isFavorite;




    public EventListFragment() {
        // empty public constructor
    }


    public void removedFav(){
        if (eventListAdapter != null) {
            eventListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getParentFragmentManager().popBackStack();
            }
        });

        ArrayList<Event> eventList = (ArrayList<Event>) getArguments().getSerializable("eventList");
        recyclerView = view.findViewById(R.id.recycler);

        if (eventList.size() == 0) {
            noEvents = view.findViewById(R.id.noEvents);
            noEvents.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            eventListAdapter = new EventListAdapter(getActivity(), eventList);
            eventListAdapter.setClickListener(this);
            recyclerView.setAdapter(eventListAdapter);
        }

        EventFinderMainActivity.favoriteEvents.addOnListChangedCallback(new ObservableList.OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                removedFav();
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                removedFav();
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                removedFav();
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                removedFav();
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                removedFav();
            }
        });

        return view;
    }



    @Override
    public void onItemClick(View view, int position, String id, Event event) {
        Log.d("clicked item with id:", id);

        eventId = id;
        eventName = event.getName();
        eventUrl = event.getUrl();

        switchActivity();
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
                        eventListAdapter.notifyDataSetChanged();
                    }
                });
    }
}