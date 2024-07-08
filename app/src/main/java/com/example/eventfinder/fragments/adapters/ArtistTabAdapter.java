package com.example.eventfinder.fragments.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventfinder.R;
import com.example.eventfinder.fragments.Objects.ArtistDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArtistTabAdapter extends RecyclerView.Adapter<ArtistTabAdapter.ViewHolder>{
    private List<ArtistDetails> artists;
    private LayoutInflater mInflater;
    private Context context;

    public ArtistTabAdapter(Context context, ArrayList<ArtistDetails> data){
        this.mInflater = LayoutInflater.from(context);
        this.artists = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtistTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.artist_info_view,parent,false);
        TextView artistName = item.findViewById(R.id.artistName);
        artistName.setSelected(true);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistTabAdapter.ViewHolder holder, int position) {
        final ArtistDetails a = artists.get(artists.size() - 1 - position);

        if (!Objects.equals(a.getImage(), "")) {
            Picasso.get().load(a.getImage()).into(holder.artistImage);
        }
        if (!Objects.equals(a.getAlbumCovers(), "")) {
            Picasso.get().load(a.getAlbumCovers().get(0)).into(holder.album1);
        }
        if (!Objects.equals(a.getAlbumCovers(), "")) {
            Picasso.get().load(a.getAlbumCovers().get(1)).into(holder.album2);
        }
        if (!Objects.equals(a.getAlbumCovers(), "")) {
            Picasso.get().load(a.getAlbumCovers().get(2)).into(holder.album3);
        }

        double d = a.getFollowers();

        String followerFormatted = formatFollowers(d);
        String popularityString = Integer.toString(a.getPopularity());

        holder.artistName.setText(a.getName());
        holder.followers.setText(followerFormatted);
        holder.popularityNumber.setText(popularityString);
        holder.popularityBar.setProgress(a.getPopularity());

        SpannableString spotString = new SpannableString("Check out on Spotify");
        spotString.setSpan(new UnderlineSpan(), 0, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.spotifyLink.setText(spotString);
        holder.spotifyLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(a.getSpotify()));
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView artistImage;
        TextView artistName;
        TextView followers;
        TextView spotifyLink;
        ProgressBar popularityBar;
        TextView popularityNumber;
        ImageView album1;
        ImageView album2;
        ImageView album3;

        ViewHolder(View itemView){
            super(itemView);
            artistImage = itemView.findViewById(R.id.artistImage);
            artistName = itemView.findViewById(R.id.artistName);
            followers = itemView.findViewById(R.id.followers);
            spotifyLink = itemView.findViewById(R.id.spotifyLink);
            popularityBar = itemView.findViewById(R.id.popularityBar);
            popularityNumber = itemView.findViewById(R.id.popularityNumber);
            album1 = itemView.findViewById(R.id.album1);
            album2 = itemView.findViewById(R.id.album2);
            album3 = itemView.findViewById(R.id.album3);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public static String formatFollowers(double number) {
        if (number >= 1_000_000) {
            return String.format("%.1fM Followers", number / 1_000_000);
        } else if (number >= 1_000) {
            return String.format("%.1fK Followers", number / 1_000);
        } else {
            return Double.toString(number);
        }
    }


}
