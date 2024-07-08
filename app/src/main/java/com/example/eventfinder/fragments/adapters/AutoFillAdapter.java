package com.example.eventfinder.fragments.adapters;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AutoFillAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> mlistData;

    public AutoFillAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mlistData = new ArrayList<>();
    }

    public void setData(List<String> list){
        mlistData.clear();
        mlistData.addAll(list);
    }

    @Override
    public int getCount(){
        return mlistData.size();
    }

    @Nullable
    @Override
    public String getItem(int position){
        return mlistData.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter(){
        Filter dataFilter = new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence constraint){
                FilterResults filterResults = new FilterResults();
                if (constraint != null){
                    filterResults.values = mlistData;
                    filterResults.count = mlistData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };
        return dataFilter;
    }

}
