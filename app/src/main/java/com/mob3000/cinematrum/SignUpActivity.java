package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.helpers.Validator;
import com.mob3000.cinematrum.sqlite.DataAcessor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SignUpActivity extends AppCompatActivity {

    private String USERNAME_INPUT_FIELD_MESSAGE = "";
    private String EMAIL_INPUT_FIELD_MESSAGE = "";
    private String PASSWORD_INPUT_FIELD_MESSAGE = "";
    private String PASSWORD_LENGTH_MESSAGE = "";
    private String VALID_FIELD = "VALID";

    //instantiating warning messages
    private TextView txtUsernameWarningMessage;
    private TextView txtEmailWarningMessage;
    private TextView txtPasswordWarningMessage;

    //encrypt algorithm tag
    private final String AES = "AES";

    //instantiating editText views
    private EditText etxtUsername;
    private EditText etxtEmail;
    private EditText etxtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        InitViews();
        InitWarningMessages();

        //if we are using an Activity that is extending from AppCompatActivity we need to use getSupportActionBar()
        getSupportActionBar().setTitle(R.string.signup_action_title);

        ColorDrawable color = new ColorDrawable(getResources().getColor(R.color.background_theme));
        getSupportActionBar().setBackgroundDrawable(color);
    }



    //initializing all views in this activity
    private void InitViews() {
        etxtUsername = findViewById(R.id.etxtUsername);
        etxtEmail = findViewById(R.id.etxtEmail);
        etxtPassword = findViewById(R.id.etxtPassword);
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
                String saltValue = GenerateSalt();
                String encryptPassword = encrypt(etxtPassword.getText().toString());
                String hashedPasswordWithSalt = saltValue + encryptPassword;
                newUser.setSalt(saltValue);
                newUser.setPasswordHash(hashedPasswordWithSalt);


                if (DataAcessor.insertUser(this, newUser)) {
                    Toast.makeText(SignUpActivity.this, "You logged in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.putExtra("username", newUser.getName());
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUpActivity.this, "Sorry something went wrong please try again!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(this, "all fields are valid", Toast.LENGTH_SHORT).show();
        } else
            SetWarningLabels();
    }


    //method for generating salt string of 10 characters
    private String GenerateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[10];
        random.nextBytes(bytes);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    //password encryption
    private String encrypt(String text) throws Exception {
        //we generated this key using the generateKeyMethod
        SecretKeySpec encryptKey = generateKey(text);

        //we create Cipher class instance
        Cipher c = Cipher.getInstance(AES);

        //we encrypt
        c.init(Cipher.ENCRYPT_MODE, encryptKey);


        byte[] encryptedValue = c.doFinal(text.getBytes());

        //then we encode the value to string
        String encValue = Base64.encodeToString(encryptedValue, Base64.DEFAULT);
        return encValue;
    }
    private SecretKeySpec generateKey(String text) throws Exception {
        //we use MessageDigest insance to get SHA-256 algorithm
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = text.getBytes("UTF-8");

        //we update the digest using the specified number of bytes
        digest.update(bytes, 0, bytes.length);

        //setting up the key
        byte[] key = digest.digest();

        //we make a secretKey out of our key and algorithm we want
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
        //now we return this
        return secretKeySpec;
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
        EMAIL_INPUT_FIELD_MESSAGE = Validator.ValidateInputField(findViewById(R.id.etxtEmail));
        PASSWORD_INPUT_FIELD_MESSAGE = Validator.ValidatePasswordField(findViewById(R.id.etxtPassword));
        PASSWORD_LENGTH_MESSAGE = Validator.ValidatePasswordLength(findViewById(R.id.etxtPassword));
        if (USERNAME_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD && EMAIL_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD
                && PASSWORD_INPUT_FIELD_MESSAGE == Validator.VALID_FIELD && PASSWORD_LENGTH_MESSAGE == Validator.VALID_FIELD)
            return true;


        //if they are not valid we will collect all warning messages inside labels
        txtUsernameWarningMessage.setText(USERNAME_INPUT_FIELD_MESSAGE);
        txtEmailWarningMessage.setText(EMAIL_INPUT_FIELD_MESSAGE);
        if (Validator.ValidatePasswordLength(findViewById(R.id.etxtPassword)) != VALID_FIELD)
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