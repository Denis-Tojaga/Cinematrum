package com.mob3000.cinematrum.helpers;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class Validator {
    public static final String INPUT_NOT_VALID_MESSAGE = "This field is required!";
    public static final String PASSWORD_NOT_VALID_MESSAGE = "This field is required!";
    public static final String PASSWORD_LENGTH_NOT_VAlID = "Password must be at least 6 characters!";
    public static final String VALID_FIELD = "Valid";

    private Validator() {
    }

    public static String ValidateInputField(View view) {
        //method for examinating which view is validated and regarding that showing the right message
        if (view instanceof EditText && ((EditText) view).getText() == null)
            return INPUT_NOT_VALID_MESSAGE;
        return VALID_FIELD;
    }


    public static String ValidatePasswordField(View view) {
        if (view instanceof EditText && ((EditText) view).getText() == null)
            return PASSWORD_NOT_VALID_MESSAGE;
        return VALID_FIELD;
    }

    public static String ValidatePasswordLength(View view) {
        if (view instanceof EditText && ((EditText) view).getText().length() < 6)
            return PASSWORD_LENGTH_NOT_VAlID;
        return VALID_FIELD;
    }
}
