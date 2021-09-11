package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        //removes the UI actionBar at the top of the screen
        getSupportActionBar().hide();
    }




    //navigating to singInActivity
    public void btnSignInClick(View view) {
        Intent intent = new Intent(WelcomeActivity.this,SignInActivity.class);
        startActivity(intent);
    }


    //navigating to singUpActivity
    public void btnSignUpClick(View view) {
        Intent intent = new Intent(WelcomeActivity.this,SignUpActivity.class);
        startActivity(intent);
    }
}