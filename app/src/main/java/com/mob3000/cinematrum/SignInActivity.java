package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //if we are using an Activity that is extending from AppCompatActivity we need to use getSupportActionBar()
        getSupportActionBar().setTitle(R.string.signin_action_title);
        ColorDrawable color = new ColorDrawable(getResources().getColor(R.color.background_theme));
        getSupportActionBar().setBackgroundDrawable(color);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignInActivity.this, com.mob3000.cinematrum.HomeFragment.class);
        startActivity(intent);
    }
}