package com.example.eventfinder.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventfinder.R;
import com.example.eventfinder.fragments.Objects.ArtistDetails;
import com.example.eventfinder.fragments.Objects.EventDetails;
import com.example.eventfinder.fragments.adapters.ArtistTabAdapter;

import java.util.ArrayList;


public class ArtistTabFragment extends Fragment {


    ArtistTabAdapter adapter;
    private ArrayList<ArtistDetails> details;
    private View view;
    private RecyclerView recyclerView;
    private ConstraintLayout constraintView;


    public void setArtistDetails(ArrayList<ArtistDetails> details){
        this.details = details;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<ArtistDetails> details = (ArrayList<ArtistDetails>) getArguments().getSerializable("details");

        view = inflater.inflate(R.layout.fragment_artist_tab, container, false);
        recyclerView = view.findViewById(R.id.artistsRecycler);
        constraintView = view.findViewById(R.id.noArtists);

        if (details.size() == 0){
            recyclerView.setVisibility(View.GONE);
            constraintView.setVisibility(View.VISIBLE);
        }else{
            adapter = new ArtistTabAdapter(getActivity(), details);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}