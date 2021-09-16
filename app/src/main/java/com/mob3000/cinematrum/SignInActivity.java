package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.helpers.Validator;
import com.mob3000.cinematrum.sqlite.DataAcessor;

public class SignInActivity extends AppCompatActivity {

    SharedPreferences sp;

    //initializing warning messages
    private String EMAIL_INPUT_FIELD_MESSAGE = "";
    private String PASSWORD_INPUT_FIELD_MESSAGE = "";
    private String PASSWORD_LENGTH_MESSAGE = "";
    private String VALID_FIELD = "VALID";


    //initializing views inside this activity
    private TextView txtEmailWarningMessage;
    private TextView txtPasswordWarningMessage;
    private EditText etxtEmailSignIn;
    private EditText etxtPasswordSignIn;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sp = getSharedPreferences("login", MODE_PRIVATE);

        //if we are using an Activity that is extending from AppCompatActivity we need to use getSupportActionBar()
        getSupportActionBar().setTitle(R.string.signin_action_title);
        ColorDrawable color = new ColorDrawable(getResources().getColor(R.color.background_theme));
        getSupportActionBar().setBackgroundDrawable(color);


        InitViews();

    }

    //initializing all views from this activity
    private void InitViews() {
        etxtEmailSignIn = findViewById(R.id.etxtEmailSignIn);
        etxtPasswordSignIn = findViewById(R.id.etxtPasswordSignIn);
        txtEmailWarningMessage = findViewById(R.id.txtEmailWarningMessage);
        txtPasswordWarningMessage = findViewById(R.id.txtPasswordWarningMessage);
        btnSignIn = findViewById(R.id.btnSignIn);
    }


    //method for signing in the user with inserted credentials
    public void btnSignInClick(View view) {
        try {

            if (ValidateFields()) {
                ClearWarningLabels();
                //we need salt here in order to hash password and set it as hashPassword so we can check with the one inside database
                User user = new User();
                user.setEmail(etxtEmailSignIn.getText().toString());
                String hashedPassword = Validator.encrypt(etxtPasswordSignIn.getText().toString());
                user.setPasswordHash(hashedPassword);

                if (DataAcessor.checkUserCredentials(this, user)) {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    FillSharedPreferences("logged", true, "email", user.getEmail(), "password", user.getPasswordHash());
                    startActivity(intent);
                    Toast.makeText(SignInActivity.this, "User is logged in -> " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(SignInActivity.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();

            } else
                SetWarningLabels();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    //fills the sharedPreferences with data needed to auto-login
    private void FillSharedPreferences(String logged, boolean loggedValue, String email, String emailValue, String password, String passwordValue) {
        sp.edit().putBoolean(logged, loggedValue).apply();
        sp.edit().putString(email, emailValue).apply();
        sp.edit().putString(password, passwordValue).apply();
    }


    //clears the text of all warningLabels
    private void ClearWarningLabels() {
        txtEmailWarningMessage.setVisibility(View.GONE);
        txtPasswordWarningMessage.setVisibility(View.GONE);
    }

    //sets the text for warningLabels
    private void SetWarningLabels() {
        //if the current warning message is not valid we will show the message only then
        if (txtEmailWarningMessage.getText() != VALID_FIELD)
            txtEmailWarningMessage.setVisibility(View.VISIBLE);
        else
            txtEmailWarningMessage.setVisibility(View.GONE);
        if (txtPasswordWarningMessage.getText() != VALID_FIELD)
            txtPasswordWarningMessage.setVisibility(View.VISIBLE);
        else
            txtPasswordWarningMessage.setVisibility(View.GONE);
    }

    //validating input fields
    private boolean ValidateFields() {
        //if all fields are good the messages value will be VALID and we wont show the warning message
        EMAIL_INPUT_FIELD_MESSAGE = Validator.ValidateInputField(etxtEmailSignIn);
        PASSWORD_INPUT_FIELD_MESSAGE = Validator.ValidatePasswordField(etxtPasswordSignIn);
        PASSWORD_LENGTH_MESSAGE = Validator.ValidatePasswordLength(etxtPasswordSignIn);
        if (EMAIL_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD && PASSWORD_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD && PASSWORD_LENGTH_MESSAGE == Validator.VALID_FIELD)
            return true;


        //if they are not valid we will collect all warning messages inside labels
        txtEmailWarningMessage.setText(EMAIL_INPUT_FIELD_MESSAGE);
        if (Validator.ValidatePasswordLength(findViewById(R.id.etxtPasswordSignIn)) != VALID_FIELD)
            txtPasswordWarningMessage.setText(PASSWORD_LENGTH_MESSAGE);
        else
            txtPasswordWarningMessage.setText(PASSWORD_INPUT_FIELD_MESSAGE);
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignInActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }
}
