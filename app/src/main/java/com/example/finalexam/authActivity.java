package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class authActivity extends AppCompatActivity implements newAccountFragment.newAccountInterface,loginScreenFragment.loginInterface{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().add(R.id.authContainerViewId, new loginScreenFragment()).commit();
    }


    @Override
    public void callMainActivity() {
        Intent intent = new Intent(authActivity.this, MainActivity.class);
        startActivityForResult(intent, 22);
    }

    @Override
    public void callnewAccountFragment() {
                getSupportFragmentManager().beginTransaction().replace(R.id.authContainerViewId, new newAccountFragment()).addToBackStack(null).commit();
    }

    @Override
    public void callMainactivity() {
        Intent intent = new Intent(authActivity.this, MainActivity.class);
        startActivityForResult(intent, 23);
    }
}

