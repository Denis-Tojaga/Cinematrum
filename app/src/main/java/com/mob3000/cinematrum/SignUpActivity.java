package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.helpers.Validator;
import com.mob3000.cinematrum.sqlite.DataAcessor;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SignUpActivity extends AppCompatActivity {

    private SharedPreferences sp;

    private String USERNAME_INPUT_FIELD_MESSAGE = "";
    private String EMAIL_INPUT_FIELD_MESSAGE = "";
    private String PASSWORD_INPUT_FIELD_MESSAGE = "";
    private String PASSWORD_LENGTH_MESSAGE = "";
    private String VALID_FIELD = "VALID";

    //instantiating warning messages
    private TextView txtUsernameWarningMessage;
    private TextView txtEmailWarningMessage;
    private TextView txtPasswordWarningMessage;


    //instantiating editText views
    private EditText etxtUsername;
    private EditText etxtEmail;
    private EditText etxtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sp = getSharedPreferences("login",MODE_PRIVATE);

        InitViews();
        InitWarningMessages();

        //if we are using an Activity that is extending from AppCompatActivity we need to use getSupportActionBar()
        getSupportActionBar().setTitle(R.string.signup_action_title);

        ColorDrawable color = new ColorDrawable(getResources().getColor(R.color.background_theme));
        getSupportActionBar().setBackgroundDrawable(color);
    }





    //registration of a new user
    public void btnSignUpClick(View view) {

        if (ValidateInputFields()) {
            ClearWarningLabels();
            try {
                User newUser = new User();
                newUser.setName(etxtUsername.getText().toString());
                newUser.setEmail(etxtEmail.getText().toString());
                //encrypting password
                String saltValue = Validator.GenerateSalt();
                String encryptPassword = Validator.encrypt(etxtPassword.getText().toString());
                String hashedPasswordWithSalt = saltValue + encryptPassword;
                newUser.setSalt(saltValue);
                newUser.setPasswordHash(hashedPasswordWithSalt);


                if (DataAcessor.insertUser(this, newUser)) {
                    Toast.makeText(SignUpActivity.this, "You logged in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    FillSharedPreferences("logged",true,"email",newUser.getEmail(),"password",newUser.getPasswordHash());
                    startActivity(intent);
                } else
                    Toast.makeText(SignUpActivity.this, "Sorry something went wrong please try again!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            SetWarningLabels();
    }


    //fills shared preferences with three params
    //logged = true
    //email = user's email
    //password = user's hashedPassword(without salt)
    private void FillSharedPreferences(String logged,boolean loggedValue,String email,String emailValue,String password,String passwordValue) {
        sp.edit().putBoolean(logged,loggedValue).commit();
        sp.edit().putString(email,emailValue).commit();
        sp.edit().putString(password,passwordValue).commit();
    }


    //initializing all views in this activity
    private void InitViews() {
        etxtUsername = findViewById(R.id.etxtUsername);
        etxtEmail = findViewById(R.id.etxtEmailSignIn);
        etxtPassword = findViewById(R.id.etxtPasswordSignIn);
    }

    //initializing warning TextView messages
    private void InitWarningMessages() {
        txtUsernameWarningMessage = findViewById(R.id.txtUsernameWarning);
        txtEmailWarningMessage = findViewById(R.id.txtEmailWarning);
        txtPasswordWarningMessage = findViewById(R.id.txtPasswordWarning);
        ClearWarningLabels();
    }

    //clearing all warning messages
    private void ClearWarningLabels() {
        txtUsernameWarningMessage.setVisibility(View.GONE);
        txtEmailWarningMessage.setVisibility(View.GONE);
        txtPasswordWarningMessage.setVisibility(View.GONE);
    }

    //setting up all warning messages
    private void SetWarningLabels() {

        //if the current warning message is not valid we will show the message only then
        if (txtUsernameWarningMessage.getText() != VALID_FIELD)
            txtUsernameWarningMessage.setVisibility(View.VISIBLE);
        else
            txtUsernameWarningMessage.setVisibility(View.GONE);
        if (txtEmailWarningMessage.getText() != VALID_FIELD)
            txtEmailWarningMessage.setVisibility(View.VISIBLE);
        else
            txtEmailWarningMessage.setVisibility(View.GONE);
        if (txtPasswordWarningMessage.getText() != VALID_FIELD)
            txtPasswordWarningMessage.setVisibility(View.VISIBLE);
        else
            txtPasswordWarningMessage.setVisibility(View.GONE);
    }

    //validation of all input fields
    private boolean ValidateInputFields() {

        //if all fields are good the messages value will be VALID and we wont show the warning message
        USERNAME_INPUT_FIELD_MESSAGE = Validator.ValidateInputField(findViewById(R.id.etxtUsername));
        EMAIL_INPUT_FIELD_MESSAGE = Validator.ValidateInputField(findViewById(R.id.etxtEmailSignIn));
        PASSWORD_INPUT_FIELD_MESSAGE = Validator.ValidatePasswordField(findViewById(R.id.etxtPasswordSignIn));
        PASSWORD_LENGTH_MESSAGE = Validator.ValidatePasswordLength(findViewById(R.id.etxtPasswordSignIn));
        if (USERNAME_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD && EMAIL_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD
                && PASSWORD_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD && PASSWORD_LENGTH_MESSAGE == Validator.VALID_FIELD)
            return true;


        //if they are not valid we will collect all warning messages inside labels
        txtUsernameWarningMessage.setText(USERNAME_INPUT_FIELD_MESSAGE);
        txtEmailWarningMessage.setText(EMAIL_INPUT_FIELD_MESSAGE);
        if (Validator.ValidatePasswordLength(findViewById(R.id.etxtPasswordSignIn)) != VALID_FIELD)
            txtPasswordWarningMessage.setText(PASSWORD_LENGTH_MESSAGE);
        else
            txtPasswordWarningMessage.setText(PASSWORD_INPUT_FIELD_MESSAGE);
        return false;
    }


    //going back to welcome_activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

}