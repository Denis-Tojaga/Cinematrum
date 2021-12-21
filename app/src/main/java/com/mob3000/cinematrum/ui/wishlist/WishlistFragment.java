package com.mob3000.cinematrum.ui.wishlist;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mob3000.cinematrum.MainActivity;
import com.mob3000.cinematrum.MovieDetailsActivity;
import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.helpers.WishlistTableAdapter;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.dataModels.Wishlist;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.sqlite.DatabaseHelper;

import java.util.ArrayList;


public class WishlistFragment extends Fragment {

    private ArrayList<Wishlist> _wishlist; //
    private ArrayList<Wishlist> _fullWishlist; // All wishlist items copied once cause _wishlist gets passed to adapter and possibly cleared.
    private User _currentUser;
    private int distance;
    private MainActivity mainActivity;
    private SearchView _searchTextInput;
    private ListView _wishlistListView;

    private WishlistTableAdapter _wishlistAdapter;
    private SharedPreferences sp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sp = getActivity().getSharedPreferences("login", MODE_PRIVATE);

        mainActivity =(MainActivity) getActivity();
        distance = mainActivity.distance;

        // INIT VIEW
        _searchTextInput = view.findViewById(R.id.search_textInput);
        _wishlistListView = view.findViewById(R.id.wishlist_listView);

        _wishlistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Wishlist currentEntry = _wishlist.get(i);
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra("movieID", currentEntry.getMovie_id());
                intent.putExtra("AddOrRemove", 0);
                intent.putExtra("distance", distance);
                intent.putExtra("location", mainActivity._location);
                intent.putExtra("usingLocationService", mainActivity.usingLocationService);
                startActivity(intent);

            }
        });

        _searchTextInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                String text = _searchTextInput.getQuery().toString();

                if (!TextUtils.isEmpty(text)) {
                    ArrayList<Wishlist> filteredWishlist = new ArrayList<>();
                    for (int i = 0; i < _fullWishlist.size(); i++) {
                        Wishlist item = _fullWishlist.get(i);
                        if (item.get_movie().getDescription().toLowerCase().contains(text.toLowerCase()) || item.get_movie().getName().toLowerCase().contains(text.toLowerCase()) || item.get_movie().getCategoriesNamesConcat().toLowerCase().contains(text.toLowerCase()))
                            filteredWishlist.add(_fullWishlist.get(i));
                    }
                    _wishlist = filteredWishlist;
                    if(_wishlistAdapter != null)
                        _wishlistAdapter.updateData(filteredWishlist);
                } else {
                    _wishlist = _fullWishlist;
                    if (_wishlistAdapter != null)
                        _wishlistAdapter.updateData(_fullWishlist);
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWishlistData();
    }

    private void loadWishlistData(){
        String userMail = sp.getString("email", "default");
        if (userMail != "default") {
            ArrayList<User> users = DataAcessor.getUser(getActivity(), DatabaseHelper.COLUMN_USER_email, userMail);
            if (users.size() == 1)
                _currentUser = users.get(0);
        }


        //_currentUser = DataAcessor.getLoggedInUser(getActivity().getApplicationContext());
        _wishlist = _currentUser.getWishlist();

        if (_wishlist == null)
            _wishlist = new ArrayList<>();

        _fullWishlist = new ArrayList<>(_wishlist);

        _wishlistAdapter = new WishlistTableAdapter(getActivity().getApplicationContext(), _wishlist);
        _wishlistListView.setAdapter(_wishlistAdapter);
    }
}