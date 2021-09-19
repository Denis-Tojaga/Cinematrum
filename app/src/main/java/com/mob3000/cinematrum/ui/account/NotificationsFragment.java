package com.mob3000.cinematrum.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mob3000.cinematrum.MainActivity;
import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.WelcomeActivity;
import com.mob3000.cinematrum.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private SharedPreferences sp;
    private Button btnLogOut;

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //defining a logOut button and setting onClickListener
        btnLogOut = root.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

        return root;
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