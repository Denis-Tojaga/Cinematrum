package com.mob3000.cinematrum.ui.wishlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.adapter.WishlistTableAdapter;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.dataModels.Wishlist;
import com.mob3000.cinematrum.sqlite.DataAcessor;

import java.util.ArrayList;


public class WishlistFragment extends Fragment {

    private static String LOG_TAG = "WISHLISTLOG";

    private ArrayList<Wishlist> _wishlist;
    private User _currentUser;

    private EditText _searchTextInput;
    private ListView _wishlistListView;

    private WishlistTableAdapter _wishlistAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // INIT VIEW
        _searchTextInput = (EditText) view.findViewById(R.id.search_textInput);

        _wishlistListView = view.findViewById(R.id.wishlist_listView);

        _currentUser = DataAcessor.getLoggedInUser(getActivity().getApplicationContext());
        _wishlist = _currentUser.getWishlist();

        if (_wishlist == null)
            _wishlist = new ArrayList<>();

        Log.d(LOG_TAG, "WISHLIST SIZE:");
        Log.d(LOG_TAG, String.valueOf(_wishlist.size()));

        _wishlistAdapter = new WishlistTableAdapter(getActivity().getApplicationContext(), _wishlist);
        _wishlistListView.setAdapter(_wishlistAdapter);

        _searchTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = _searchTextInput.getText().toString();

                if (text != ""){
                    ArrayList<Wishlist> completeList = _wishlist;
                    ArrayList<Wishlist> filteredWishlist = new ArrayList<>();
                    for (int i = 0; i < completeList.size(); i ++){
                        Wishlist item = completeList.get(i);
                        if (item.get_movie().getDescription().contains(text) ||item.get_movie().getName().contains(text))
                            filteredWishlist.add(completeList.get(i));
                    }
                    _wishlistAdapter.updateData(filteredWishlist);
                }
                else {
                    //_wishlist = _currentUser.getWishlist();
                    _wishlistAdapter.updateData(_wishlist);
                }

            }
        });

    }
}