package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.io.File;

public class WelcomeActivity extends AppCompatActivity {

    //custom animations for button click because it isn't from MaterialDesign (it's from android.widget.button)
    Animation scale_up_animation,scale_down_animation;
    Button btnSignIn,btnSignUp;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedPreferences = this.getSharedPreferences("login",MODE_PRIVATE);

        //checks if the user is already logged in
        TryAutoLogin();

        //removes the UI actionBar at the top of the screen
        getSupportActionBar().hide();

        //loading animations
        LoadAnimations();
        //loading all activity views
        LoadViews();
        //setting up the touch listeners
        btnSignIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    btnSignIn.startAnimation(scale_up_animation);
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                    btnSignIn.startAnimation(scale_down_animation);


                Intent intent = new Intent(WelcomeActivity.this,SignInActivity.class);
                startActivity(intent);
                return true;
            }
        });

        btnSignUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    btnSignUp.setAnimation(scale_up_animation);
                else if(motionEvent.getAction()== MotionEvent.ACTION_UP)
                    btnSignUp.setAnimation(scale_down_animation);

                Intent intent = new Intent(WelcomeActivity.this,SignUpActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }


    //method that will try to auto login a user if he is already signed in
    private void TryAutoLogin() {
        File f = new File("/data/data/com.mob3000.cinematrum/shared_prefs/login.xml");
        boolean fileExists = f.exists();
        boolean userLoggedIn = GetIsLogged();
        if(fileExists && userLoggedIn)
            GoToMainActivity();
    }

    private boolean GetIsLogged() {
        return sharedPreferences.getBoolean("logged",true);
    }


    //navigates to main activity
    private void GoToMainActivity() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }


    //loading all the views from activity
    private void LoadViews() {
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
    }


    //storing the animations in two Animation variables
    private void LoadAnimations() {
        scale_up_animation = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scale_down_animation = AnimationUtils.loadAnimation(this,R.anim.scale_down);
    }
}