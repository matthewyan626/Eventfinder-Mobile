package com.example.eventfinder.fragments.adapters;


import android.content.Context;

import androidx.annotation.NonNull;
import com.example.eventfinder.fragments.SearchFragment;
import com.example.eventfinder.fragments.FavoriteFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private SearchFragment searchFragment;
    private FavoriteFragment favoriteFragment;
    private Context context;


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.context = fragmentActivity.getApplicationContext();
        searchFragment = new SearchFragment();
        favoriteFragment = new FavoriteFragment(context);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                }
                return searchFragment;
            case 1:
                if (favoriteFragment == null) {
                    favoriteFragment = new FavoriteFragment(context);
                }
                return favoriteFragment;
            default:
                return new SearchFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
