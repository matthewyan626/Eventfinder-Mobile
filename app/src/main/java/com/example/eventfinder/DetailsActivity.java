package com.example.eventfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventfinder.fragments.DetailsFragment;
import com.example.eventfinder.fragments.Objects.Event;
import com.google.android.material.snackbar.Snackbar;

import java.net.URLEncoder;


public class DetailsActivity extends AppCompatActivity {

    public static Event event = new Event();
    private ImageButton backButton;
    private TextView eventTitle;
    private ImageView facebookIcon;
    private ImageView twitterIcon;
    private ImageView favIcon;
    private Boolean isFavorite;
    private String eventName;
    private String eventUrl;
    private String eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        eventName = getIntent().getStringExtra("name");
        eventUrl = getIntent().getStringExtra("url");
        eventId = getIntent().getStringExtra("id");

        Log.d("Details Event Name", eventName);
        Log.d("Details Event Id", eventId);

        eventTitle = findViewById(R.id.eventName);
        backButton = findViewById(R.id.backToSearch);
        facebookIcon = findViewById(R.id.facebookIcon);
        twitterIcon = findViewById(R.id.twitterIcon);
        favIcon = findViewById(R.id.fav);

        eventTitle.setText(eventName);
        eventTitle.setSelected(true);
        if (EventFinderMainActivity.favoriteIds.contains(eventId)){
            favIcon.setImageResource(R.drawable.heart_filled);
        }else{
            favIcon.setImageResource(R.drawable.heart_outline);
        }

        favIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (EventFinderMainActivity.favoriteIds.contains(eventId)){
                    isFavorite = false;
                    EventFinderMainActivity.removeFav(eventId, getApplicationContext());
                    String message = eventName + " removed from favorites";
                    Snackbar.make(eventTitle, message, Snackbar.LENGTH_SHORT).show();
                    favIcon.setImageResource(R.drawable.heart_outline);
                    Log.d("Removing Event From Map", event.getName());
                }else{
                    isFavorite = true;
                    EventFinderMainActivity.addFav(eventId, event, getApplicationContext());
                    String message = eventName + " added to favorites";
                    Snackbar.make(eventTitle, message, Snackbar.LENGTH_SHORT).show();
                    favIcon.setImageResource(R.drawable.heart_filled);
                    Log.d("Adding Event to Map", event.getName());

                }
            }
        });
        facebookIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String shareLink = "https://www.facebook.com/sharer/sharer.php?u=" + eventUrl;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareLink));
                startActivity(intent);
            }
        });
        twitterIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    String tweet = "Check out " + eventName + " on TicketMaster!";
                    String encodedTweet = URLEncoder.encode(tweet, "UTF-8");
                    String encodedUrl = URLEncoder.encode(eventUrl, "UTF-8");
                    String tweetUrl = "https://twitter.com/intent/tweet?text=" + encodedTweet + "&url=" + encodedUrl;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
                    startActivity(intent);
                }catch (Exception e){

                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FrameLayout container = findViewById(R.id.detailsContainer);
        DetailsFragment fragment = new DetailsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(container.getId(), fragment)
                .commit();
    }

    @Override
    public void onBackPressed(){
        Intent res = new Intent();
        res.putExtra("isFav", isFavorite);
        res.putExtra("id", eventId);
        res.putExtra("event", event);
        setResult(Activity.RESULT_OK, res);
        super.onBackPressed();
    }

}