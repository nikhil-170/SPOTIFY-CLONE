package com.example.finalexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
/*
    NIKHIL SURYA PETETI
        801166677
        */

public class MainActivity extends AppCompatActivity implements searchFragment.searchfragmentInterface, likesFragment.likesInterface, sharedFragment.SharedFragmentInterface {
    public FirebaseAuth mAuth;
    public static int REQUEST_CODE = 100;
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    ViewPageAdapter viewPageAdapter;
    Album album;
    final String TAG = "demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){

            Intent intent = new Intent(MainActivity.this, authActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }else {
//            getSupportFragmentManager().beginTransaction().add(R.id.mainContainerViewId, new )
        }


        viewPager2 = findViewById(R.id.viewPagerId);
        tabLayout = findViewById(R.id.tabLayoutId);

        viewPageAdapter = new ViewPageAdapter(this);
        viewPager2.setAdapter(viewPageAdapter);


        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText(getResources().getString(R.string.search));
                        break;
                    case 1:
                        tab.setText(getResources().getString(R.string.likes));
                        break;
                    case 2:
                        tab.setText(getResources().getString(R.string.history));
                        break;
                    case 3:
                        tab.setText(getResources().getString(R.string.shared));
                        break;
                }
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logoutid){
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, authActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void callDetailAlbumFragment(Album album) {
        Log.d(TAG, "callDetailAlbumFragment: ");
    Intent intent = new Intent(MainActivity.this, albumActivity.class);
    intent.putExtra("albumdetails",album);
    startActivity(intent);
    }

    @Override
    public void goBackToAlbumactivity(Album album) {

    }

    @Override
    public void callToAlbumActivity(Album album) {
        Intent intent = new Intent(MainActivity.this, albumActivity.class);
        Album album1 = new Album(album.id,album.nb_tracks, album.albumTitles, album.artisName, album.alblumCoverImageMedium, album.artistCoverImageSmall);
        Log.d(TAG, "callToAlbumActivity: "+ album1.alblumCoverImageMedium);
        intent.putExtra("albumdetails",album1);
        startActivity(intent);
    }


    public class ViewPageAdapter extends FragmentStateAdapter{

        public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position){
                case 0:
                    return new searchFragment();
                case 1:
                    return new likesFragment();

                case 2:
                    return new historyFragment();

                case 3:
                    return new sharedFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }


}