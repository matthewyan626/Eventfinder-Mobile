package com.example.eventfinder.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eventfinder.R;
import com.example.eventfinder.fragments.Objects.EventDetails;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class EventTabFragment extends Fragment {

    private EventDetails details;
    private View view;

    private TextView artistInfo;
    private TextView venueInfo;
    private TextView dateInfo;
    private TextView timeInfo;
    private TextView genreInfo;
    private TextView rangeInfo;
    private TextView statusInfo;
    private TextView buyInfo;
    private ImageView seatMap;

    private LinearLayout artistRow;
    private LinearLayout venueRow;
    private LinearLayout dateRow;
    private LinearLayout timeRow;
    private LinearLayout genreRow;
    private LinearLayout rangeRow;
    private LinearLayout statusRow;
    private LinearLayout buyRow;
    private LinearLayout seatMapRow;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_event_tab, container, false);

        Bundle args = getArguments();
        details = (EventDetails) args.getSerializable("details");

        Log.d("EventTab Get Name", details.getName());

        artistInfo = view.findViewById(R.id.artists);
        venueInfo = view.findViewById(R.id.venue);
        dateInfo = view.findViewById(R.id.date);
        timeInfo = view.findViewById(R.id.time);
        genreInfo = view.findViewById(R.id.genres);
        rangeInfo = view.findViewById(R.id.priceRange);
        statusInfo = view.findViewById(R.id.status);
        buyInfo = view.findViewById(R.id.buyAt);
        seatMap = view.findViewById(R.id.seatMap);


        artistRow = view.findViewById(R.id.artistsRow);
        venueRow = view.findViewById(R.id.venueRow);
        dateRow = view.findViewById(R.id.dateRow);
        timeRow = view.findViewById(R.id.timeRow);
        genreRow = view.findViewById(R.id.genreRow);
        rangeRow = view.findViewById(R.id.priceRangeRow);
        statusRow = view.findViewById(R.id.statusRow);
        buyRow = view.findViewById(R.id.buyRow);
        seatMapRow = view.findViewById(R.id.seatMapRow);

        artistInfo.setSelected(true);
        genreInfo.setSelected(true);
        buyInfo.setSelected(true);

        fillDetails();

        return view;
    }

    private void fillDetails(){
        Log.d("artist details are", details.getArtists());
        Log.d("TextView text is", artistInfo.getText().toString());
        if (!Objects.equals(details.getArtists(), "")){
            artistInfo.setText(details.getArtists());
        }else{
            artistRow.setVisibility(View.GONE);
        }

        if (!Objects.equals(details.getVenue(), "")){
            venueInfo.setText(details.getVenue());
        }else{
            venueRow.setVisibility(View.GONE);
        }

        if (!Objects.equals(details.getDate(), "")){
            dateInfo.setText(details.getDate());
        }else{
            dateRow.setVisibility(View.GONE);
        }

        if (!Objects.equals(details.getTime(), "")){
            timeInfo.setText(details.getTime());
        }else{
            timeRow.setVisibility(View.GONE);
        }

        if (!Objects.equals(details.getGenres(), "")){
            genreInfo.setText(details.getGenres());
        }else{
            genreRow.setVisibility(View.GONE);
        }

        if (!Objects.equals(details.getPriceRange(), "")){
            rangeInfo.setText(details.getPriceRange());
        }else{
            rangeRow.setVisibility(View.GONE);
        }

        if (!Objects.equals(details.getStatus(), "")){
            String status = details.getStatus();
            if (status.equals("onsale")){
                statusInfo.setText("On Sale");
                statusInfo.setBackgroundResource(R.drawable.onsale);
            }else if (status.equals("offsale")){
                statusInfo.setText("Off Sale");
                statusInfo.setBackgroundResource(R.drawable.canceled);
            }else if (status.equals("cancelled")) {
                statusInfo.setText("Cancelled");
                statusInfo.setBackgroundResource(R.drawable.canceled);
            }else if (status.equals("postponed")) {
                statusInfo.setText("Postponed");
                statusInfo.setBackgroundResource(R.drawable.rescheduled);
            }else if (status.equals("rescheduled")) {
                statusInfo.setText("Rescheduled");
                statusInfo.setBackgroundResource(R.drawable.rescheduled);
            }
        }else{
            statusRow.setVisibility(View.GONE);
        }

        if (!Objects.equals(details.getBuyLink(), "")){
            SpannableString buySpan = new SpannableString(details.getBuyLink());
            buySpan.setSpan(new UnderlineSpan(), 0, details.getBuyLink().length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            buyInfo.setText(buySpan);
            buyInfo.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(details.getBuyLink()));
                    startActivity(intent);
                }
            });
        }else{
            buyInfo.setVisibility(View.GONE);
        }

        if (!Objects.equals(details.getSeatMap(), "")){
            Picasso.get().load(details.getSeatMap()).into(seatMap);
        }else{
            seatMapRow.setVisibility(View.GONE);
        }
    }
}