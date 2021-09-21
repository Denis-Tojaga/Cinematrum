package com.mob3000.cinematrum.ui.wishlist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.adapter.WishlistTableAdapter;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.dataModels.Wishlist;
import com.mob3000.cinematrum.sqlite.DataAcessor;

import java.util.ArrayList;


public class WishlistFragment extends Fragment {

    private ArrayList<Wishlist> _wishlist; //
    private ArrayList<Wishlist> _fullWishlist; // All wishlist items copied once cause _wishlist gets passed to adapter and possibly cleared.
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
        _searchTextInput = view.findViewById(R.id.search_textInput);

        _wishlistListView = view.findViewById(R.id.wishlist_listView);

        _currentUser = DataAcessor.getLoggedInUser(getActivity().getApplicationContext());
        _wishlist = _currentUser.getWishlist();

        if (_wishlist == null)
            _wishlist = new ArrayList<>();

        _fullWishlist = new ArrayList<>(_wishlist);

        _wishlistAdapter = new WishlistTableAdapter(getActivity().getApplicationContext(), _wishlist);
        _wishlistListView.setAdapter(_wishlistAdapter);

        _wishlistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Wishlist currentEntry = _wishlist.get(i);
                Toast.makeText(getActivity().getApplicationContext(), "Item Clicked: " + currentEntry.get_movie().getName(), Toast.LENGTH_LONG).show();
                // TODO: Navigate to Movie Detail Screen

            }
        });


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

                if (!TextUtils.isEmpty(text)) {
                    ArrayList<Wishlist> filteredWishlist = new ArrayList<>();
                    for (int i = 0; i < _fullWishlist.size(); i++) {
                        Wishlist item = _fullWishlist.get(i);
                        if (item.get_movie().getDescription().contains(text) || item.get_movie().getName().contains(text))
                            filteredWishlist.add(_fullWishlist.get(i));
                    }
                    _wishlist = filteredWishlist;
                    _wishlistAdapter.updateData(filteredWishlist);
                } else {
                    _wishlist = _fullWishlist;
                    _wishlistAdapter.updateData(_fullWishlist);
                }
            }
        });
    }
}