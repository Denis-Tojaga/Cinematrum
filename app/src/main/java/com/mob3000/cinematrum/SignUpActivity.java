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

    private String USERNAME_INPUT_FIELD_MESSAGE="";
    private String EMAIL_INPUT_FIELD_MESSAGE="";
    private String PASSWORD_INPUT_FIELD_MESSAGE = "";
    private String PASSWORD_LENGTH_MESSAGE = "";

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
            ClearWarningLabels();
            Toast.makeText(this, "all fields are valid", Toast.LENGTH_SHORT).show();
        }else{
            SetWarningLabels();
        }
    }

    private void ClearWarningLabels() {
        //TODO clear warining labels
    }

    private void SetWarningLabels() {
        //TODO set the logic for these labels
    }



    private boolean ValidateInputFields() {

        //TODO implement the validation warining message
        USERNAME_INPUT_FIELD_MESSAGE = Validator.ValidateInputField(findViewById(R.id.etxtUsername));
        EMAIL_INPUT_FIELD_MESSAGE = Validator.ValidateInputField(findViewById(R.id.etxtEmail));
        PASSWORD_INPUT_FIELD_MESSAGE = Validator.ValidatePasswordField(findViewById(R.id.etxtPassword));
        PASSWORD_LENGTH_MESSAGE = Validator.ValidatePasswordLength(findViewById(R.id.etxtPassword));
        if (USERNAME_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD && EMAIL_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD
                && PASSWORD_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD && PASSWORD_LENGTH_MESSAGE == Validator.VALID_FIELD)
            return true;
        return false;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this,WelcomeActivity.class);
        startActivity(intent);
    }
}