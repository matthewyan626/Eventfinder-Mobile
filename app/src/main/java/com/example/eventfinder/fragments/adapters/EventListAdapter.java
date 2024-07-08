package com.example.eventfinder.fragments.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventfinder.EventFinderMainActivity;
import com.example.eventfinder.R;
import com.example.eventfinder.fragments.Objects.Event;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>{
    private List<Event> events;
    private ItemClickListener mClickListener;
    private Context context;

    public EventListAdapter(Context context, ArrayList<Event> data){
        this.events = data;
        this.context = context;
    }

    @NonNull
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.event_list_item_view,parent,false);
        TextView eventName = item.findViewById(R.id.name);
        eventName.setSelected(true);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ViewHolder holder, int position) {
        final Event e = events.get(position);
        Picasso.get().load(e.getIcon()).into(holder.iconView);
        holder.nameView.setText(e.getName());
        holder.venueView.setText(e.getVenue());
        holder.genreView.setText(e.getGenre());
        holder.dateView.setText(e.getDate());
        holder.timeView.setText(e.getTime());
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
                    EventFinderMainActivity.removeFav(e.getId(), context);
                    holder.favView.setImageResource(R.drawable.heart_outline);
                    String message = e.getName() + " removed from favorites";
                    Snackbar.make(holder.itemView, message, Snackbar.LENGTH_SHORT).show();
                }else{
                    EventFinderMainActivity.addFav(e.getId(), e, context);
                    holder.favView.setImageResource(R.drawable.heart_filled);
                    String message = e.getName() + " added to favorites";
                    Snackbar.make(holder.itemView, message, Snackbar.LENGTH_SHORT).show();
                    Log.d("CHANGING FAV TO", "YES FAV");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconView;
        TextView nameView;
        TextView venueView;
        TextView genreView;
        TextView dateView;
        TextView timeView;
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
            nameView.setSelected(true);
            id = "";
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getAdapterPosition(), id, event);
            }
        }
    }


    public void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, String id, Event event);
    }

}
