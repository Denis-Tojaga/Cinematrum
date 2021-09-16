package com.mob3000.cinematrum.helpers;

import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Validator {
    public static final String INPUT_NOT_VALID_MESSAGE = "This field is required!";
    public static final String PASSWORD_NOT_VALID_MESSAGE = "This field is required!";
    public static final String PASSWORD_LENGTH_NOT_VAlID = "Password must be at least 6 characters!";
    public static final String VALID_FIELD = "VALID";

    private Validator() {
    }


    //validating regular input field
    public static String ValidateInputField(View view) {
        //method for checking which view is validated and regarding that showing the right message
        EditText object = (EditText) view;
        if (view instanceof EditText && TextUtils.isEmpty(object.getText()))
            return INPUT_NOT_VALID_MESSAGE;
        return VALID_FIELD;
    }

    //validating password field
    public static String ValidatePasswordField(View view) {
        EditText object = (EditText) view;
        if (view instanceof EditText && TextUtils.isEmpty(object.getText()))
            return PASSWORD_NOT_VALID_MESSAGE;
        return VALID_FIELD;
    }

    //validating password length only
    public static String ValidatePasswordLength(View view) {
        EditText object = (EditText) view;
        if (view instanceof EditText && object.getText().toString().length() < 6)
            return PASSWORD_LENGTH_NOT_VAlID;
        return VALID_FIELD;
    }













    //method for extracting only the password part from hashedPassword, we will remove salt
    public static String ExtractPasswordPart(String hashedPassword) {
        String newString = new String();
        for (int i = 17; i < hashedPassword.length(); i++)
            newString += hashedPassword.charAt(i);
        return newString;
    }


    //method for generating salt string of 10 characters
    public static String GenerateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[10];
        random.nextBytes(bytes);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    //password encryption
    public static String encrypt(String text) throws Exception {
        //we generated this key using the generateKeyMethod
        SecretKeySpec encryptKey = generateKey(text);

        //we create Cipher class instance
        Cipher c = Cipher.getInstance("AES");

        //we encrypt
        c.init(Cipher.ENCRYPT_MODE, encryptKey);


        byte[] encryptedValue = c.doFinal(text.getBytes());

        //then we encode the value to string
        String encValue = Base64.encodeToString(encryptedValue, Base64.DEFAULT);
        return encValue;
    }


    //method for generating key
    public static SecretKeySpec generateKey(String text) throws Exception {
        //we use MessageDigest insance to get SHA-256 algorithm
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = text.getBytes("UTF-8");

        //we update the digest using the specified number of bytes
        digest.update(bytes, 0, bytes.length);

        //setting up the key
        byte[] key = digest.digest();

        //we make a secretKey out of our key and algorithm we want
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        //now we return this
        return secretKeySpec;
    }
}
