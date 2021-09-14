package com.mob3000.cinematrum.helpers;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

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
}
