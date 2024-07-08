package com.example.eventfinder.fragments.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventfinder.EventFinderMainActivity;
import com.example.eventfinder.R;
import com.example.eventfinder.fragments.EventListFragment;
import com.example.eventfinder.fragments.FavoriteFragment;
import com.example.eventfinder.fragments.Objects.Event;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>{

    private ArrayList<Event> favorites;
    private FavoriteListAdapter.FavoriteClickListener mClickListener;

    private Context context;

    public FavoriteListAdapter(Context context, ArrayList<Event> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    public void updateFavorites(ArrayList<Event> favorites){
        this.favorites = favorites;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.event_list_item_view,parent,false);
        TextView eventName = item.findViewById(R.id.name);
        eventName.setSelected(true);
        return new FavoriteListAdapter.ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteListAdapter.ViewHolder holder, int position) {
        final Event e = favorites.get(position);
        Picasso.get().load(e.getIcon()).into(holder.iconView);
        holder.nameView.setText(e.getName());
        holder.venueView.setText(e.getVenue());
        holder.genreView.setText(e.getGenre());
        holder.dateView.setText(e.getDate());
        holder.timeView.setText(e.getTime());
        holder.idView.setText(e.getId());
        holder.id = e.getId();
        holder.event= e;
        if (EventFinderMainActivity.favoriteIds.contains(e.getId())) {
            holder.favView.setImageResource(R.drawable.heart_filled);
            Log.d("Details Event Fav?", "Yes, contains KEY");
        }else{
            holder.favView.setImageResource(R.drawable.heart_outline);
            Log.d("Details Event Fav?", "No, NOKEY");
        }

        holder.favView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (EventFinderMainActivity.favoriteIds.contains(e.getId())){
                    String message = e.getName() + " removed from favorites";
                    Snackbar.make(holder.itemView, message, Snackbar.LENGTH_SHORT).show();
                    EventFinderMainActivity.removeFav(e.getId(), context);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconView;
        public TextView nameView;
        TextView venueView;
        TextView genreView;
        TextView dateView;
        TextView timeView;
        public TextView idView;
        ImageView favView;
        String id;
        Event event;
        ViewHolder(View itemView){
            super(itemView);
            iconView = itemView.findViewById(R.id.icon);
            nameView = itemView.findViewById(R.id.name);
            venueView = itemView.findViewById(R.id.venue);
            genreView = itemView.findViewById(R.id.genre);
            dateView = itemView.findViewById(R.id.date);
            timeView = itemView.findViewById(R.id.time);
            favView = itemView.findViewById(R.id.favorite);
            idView = itemView.findViewById(R.id.id);
            nameView.setSelected(true);
            id = "";
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onFavoriteClick(v, getAdapterPosition(), id, event);
            }
        }
    }


    public void setClickListener(FavoriteListAdapter.FavoriteClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public interface FavoriteClickListener {
        void onFavoriteClick(View view, int position, String id, Event event);
    }

}
