package com.audioplay.musica.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.audioplay.musica.R;
import com.audioplay.musica.adapters.MusicAdapter;
import com.audioplay.musica.services.MusicService;
import com.chibde.visualizer.CircleBarVisualizer;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MusicAdapter musicAdapter;
    private CircleBarVisualizer circleBarVisualizer;
    private LinearLayout bottomSheetPeek;
    private MusicService musicService;
    private boolean isBound=false;
    Intent intent;
    SlidingUpPanelLayout slidingUpPanelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //reference to layout view
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        circleBarVisualizer = findViewById(R.id.visualizer);
        bottomSheetPeek = findViewById(R.id.bottom_sheet_peek);
        slidingUpPanelLayout = findViewById(R.id.activity_main);

        circleBarVisualizer.setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright));


        musicAdapter = new MusicAdapter(getSupportFragmentManager());

        viewPager.setAdapter(musicAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) iBinder;
            musicService = musicBinder.getService();
            musicService.setCircleBarVisualizer(circleBarVisualizer);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        if (intent == null) {
            intent = new Intent(this, MusicService.class);
            this.bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);
            this.startService(intent);
        }
    }

    @Override
    public void onDestroy() {
        this.stopService(intent);
        musicService = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
