package com.example.eventfinder.fragments.adapters;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eventfinder.fragments.ArtistTabFragment;
import com.example.eventfinder.fragments.EventTabFragment;
import com.example.eventfinder.fragments.Objects.ArtistDetails;
import com.example.eventfinder.fragments.Objects.EventDetails;
import com.example.eventfinder.fragments.Objects.VenueDetails;
import com.example.eventfinder.fragments.VenueTabFragment;

import java.util.ArrayList;

public class DetailsViewPageAdapter extends FragmentStateAdapter {

    private EventTabFragment eventTabFragment;
    private ArtistTabFragment artistTabFragment;
    private VenueTabFragment venueTabFragment;

    private EventDetails eventDetails;
    private ArrayList<ArtistDetails> artistDetails;
    private VenueDetails venueDetails;


    public DetailsViewPageAdapter(@NonNull Fragment fragment, EventDetails eventDetails, ArrayList<ArtistDetails> artistDetails, VenueDetails venueDetails) {
        super(fragment);
        Log.d("Event Details in ADAPTER", eventDetails.getName());
        Log.d("Venue details in adapter", venueDetails.getName());
        this.eventDetails = eventDetails;
        this.artistDetails = artistDetails;
        this.venueDetails = venueDetails;
        createFragment(0);
        createFragment(1);
        createFragment(2);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                eventTabFragment = new EventTabFragment();
                Bundle args = new Bundle();
                args.putSerializable("details", eventDetails);
                eventTabFragment.setArguments(args);
                return eventTabFragment;
            case 1:
                artistTabFragment = new ArtistTabFragment();
                Bundle artistArgs = new Bundle();
                artistArgs.putSerializable("details", artistDetails);
                artistTabFragment.setArguments(artistArgs);
                return artistTabFragment;
            case 2:
                venueTabFragment = new VenueTabFragment();
                Bundle venueArgs = new Bundle();
                venueArgs.putSerializable("details", venueDetails);
                venueTabFragment.setArguments(venueArgs);
                return venueTabFragment;
            default:
                eventTabFragment = new EventTabFragment();
                return eventTabFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public EventTabFragment getEventTabFragment(){
        return eventTabFragment;
    }

}
