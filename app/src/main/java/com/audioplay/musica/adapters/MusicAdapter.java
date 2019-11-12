package com.audioplay.musica.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.audioplay.musica.fragments.AlbumsFragment;
import com.audioplay.musica.fragments.ArtistsFragment;
import com.audioplay.musica.fragments.SongsFragment;

public class MusicAdapter extends FragmentStatePagerAdapter {

    private String[] tabsTitle = {"Tracks", "Artists", "Albums"};

    public MusicAdapter(@NonNull FragmentManager fm){
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new SongsFragment();
            case 1:
                return new ArtistsFragment();
            case 2:
                return new AlbumsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabsTitle.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitle[position];
    }
}
