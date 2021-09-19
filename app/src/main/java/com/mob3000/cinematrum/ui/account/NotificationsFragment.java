package com.mob3000.cinematrum.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mob3000.cinematrum.MainActivity;
import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.SignInActivity;
import com.mob3000.cinematrum.WelcomeActivity;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.databinding.FragmentNotificationsBinding;
import com.mob3000.cinematrum.helpers.Validator;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.sqlite.DatabaseHelper;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private SharedPreferences sp;
    private Animation scale_up;
    private Animation scale_down;
    private View root;
    private User loggedUser;

    //initializing Views inside this fragment
    private TextView txtWelcomeAccountLabel;
    private TextView txtSubHeading;
    private EditText etxtAccountUsername;
    private EditText etxtAccountEmail;
    private EditText etxtAccountPassword;
    private ImageView imgEditIcon1;
    private ImageView imgEditIcon2;
    private ImageView imgEditIcon3;
    private Button btnTicketHistory;
    private Button btnLogOut;


    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        root = binding.getRoot();


        InitViews();
        LoadLoggedUserData();
        LoadAnimations();

        //defining a logOut button and setting onTouchListener
        btnLogOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    btnLogOut.startAnimation(scale_up);
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    btnLogOut.startAnimation(scale_down);

                LogOut();
                return true;
            }
        });

        return root;
    }


    //method for loading all data of currently logged user
    private void LoadLoggedUserData() {
        //retrieving the email from sharedPreferences
        String spEmail = sp.getString("email", null);
        //getting the user with that email, when inserting a user it checks if the email is already in use
        //so we will always have user with unique email
        ArrayList<User> users = DataAcessor.getUser(getActivity(), DatabaseHelper.COLUMN_USER_email, spEmail);
        if (users.size() != 0) {
            try {
                loggedUser = users.get(0);
                txtWelcomeAccountLabel.setText("Welcome " + loggedUser.getName());
                etxtAccountUsername.setText(loggedUser.getName());
                etxtAccountEmail.setText(loggedUser.getEmail());
                //TODO implement decryption of a password

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(getActivity(), "Loading logged user went wrong!", Toast.LENGTH_SHORT).show();

    }


    //method for initializing all views
    private void InitViews() {
        txtWelcomeAccountLabel = root.findViewById(R.id.txtWelcomeAccountLabel);
        txtSubHeading = root.findViewById(R.id.txtSubHeading);
        etxtAccountUsername = root.findViewById(R.id.etxtAccountUsername);
        etxtAccountEmail = root.findViewById(R.id.etxtAccountEmail);
        etxtAccountPassword = root.findViewById(R.id.etxtAccountPassword);
        imgEditIcon1 = root.findViewById(R.id.imgEditIcon1);
        imgEditIcon2 = root.findViewById(R.id.imgEditIcon2);
        imgEditIcon3 = root.findViewById(R.id.imgEditIcon3);
        btnTicketHistory = root.findViewById(R.id.btnTicketHistory);
        btnLogOut = root.findViewById(R.id.btnLogOut);
    }


    //loading animation files
    private void LoadAnimations() {
        scale_up = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
        scale_down = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down);
    }


    //clearing the shared preferences and navigating the user back to the welcome screen
    private void LogOut() {
        sp.edit().putBoolean("logged", false).commit();
        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}