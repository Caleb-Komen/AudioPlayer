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

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MusicAdapter musicAdapter;
    private RelativeLayout layoutBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private CircleBarVisualizer circleBarVisualizer;
    private LinearLayout bottomSheetPeek;
    private AppBarLayout appBarLayout;
    private MusicService musicService;
    private boolean isBound=false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //reference to layout view
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        circleBarVisualizer = findViewById(R.id.visualizer);
        bottomSheetPeek = findViewById(R.id.bottom_sheet_peek);
        appBarLayout = findViewById(R.id.app_bar);

        circleBarVisualizer.setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright));
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

                switch (i){

                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetPeek.setVisibility(View.INVISIBLE);
                        appBarLayout.setVisibility(View.INVISIBLE);
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottomSheetPeek.setVisibility(View.VISIBLE);
                        appBarLayout.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });

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
}
