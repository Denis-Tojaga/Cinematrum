package com.mob3000.cinematrum.ui.home;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mob3000.cinematrum.MainActivity;
import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.WebActivity;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.databinding.FragmentHomeBinding;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.ui.ReservationActivity;

import java.util.List;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button btnOpenReservation;
    private Button btnOpenYoutube;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        btnOpenReservation = root.findViewById(R.id.btnOpenReservation);
        btnOpenReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReservationActivity.class);
                intent.putExtra(ReservationActivity.INTENT_CINEMA_ID, 1);
                intent.putExtra(ReservationActivity.INTENT_MOVIE_ID, 1);
                startActivity(intent);
            }
        });



        //opening movie trailer logic
        btnOpenYoutube = root.findViewById(R.id.btnOpenYoutube);
        btnOpenYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webIntent = new Intent(getContext(), WebActivity.class);
                webIntent.putExtra("url", "https://www.youtube.com/watch?v=K_8yRH2KPVo&list=RDMM0sca9FP6zl8&index=6");
                startActivity(webIntent);
            }
        });
        return root;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TEST
        // Login in user
        User u = DataAcessor.getSingleUser(getActivity().getApplicationContext(), 1);
        DataAcessor.logInUser(getActivity().getApplicationContext(), u);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}