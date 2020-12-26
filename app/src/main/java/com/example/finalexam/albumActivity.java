package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class albumActivity extends AppCompatActivity {
    Album album;
    final String TAG = "demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        album = (Album) getIntent().getSerializableExtra("albumdetails");

        getSupportFragmentManager().beginTransaction().replace(R.id.albumActivityContainerViewId, new albumFragment(album)).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}