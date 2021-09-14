package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.helpers.Validator;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //if we are using an Activity that is extending from AppCompatActivity we need to use getSupportActionBar()
        getSupportActionBar().setTitle(R.string.signup_action_title);

        ColorDrawable color = new ColorDrawable(getResources().getColor(R.color.background_theme));
        getSupportActionBar().setBackgroundDrawable(color);
    }


    //making a new user with credentials and inserting him in the database
    public void btnSignUpClick(View view) {

        if (ValidateInputFields()) {
            //if all the fields are filled then we can create a user and save it
            Toast.makeText(this, "all fields are valid", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean ValidateInputFields() {
        String validUsername = Validator.ValidateInputField(findViewById(R.id.etxtUsername));
        String validEmail = Validator.ValidateInputField(findViewById(R.id.etxtEmail));
        String validPassword = Validator.ValidatePasswordField(findViewById(R.id.etxtPassword));
        if (validUsername == Validator.VALID_FIELD && validEmail == Validator.VALID_FIELD
                && validPassword == Validator.VALID_FIELD)
            return true;
        return false;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this,WelcomeActivity.class);
        startActivity(intent);
    }
}