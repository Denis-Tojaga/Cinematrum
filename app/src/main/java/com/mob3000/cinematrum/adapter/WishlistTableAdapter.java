package com.mob3000.cinematrum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.Wishlist;

import java.util.ArrayList;

public class WishlistTableAdapter extends ArrayAdapter<Wishlist> {

    private ArrayList<Wishlist> _wishlist;
    private Context _context;

    public WishlistTableAdapter(@NonNull Context context, ArrayList<Wishlist> wishlist) {
        super(context, 0, wishlist);

        _wishlist = wishlist;
        _context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null)
            listItem = LayoutInflater.from(_context).inflate(R.layout.wishlist_itemview, parent, false);

        TextView nameView = (TextView) listItem.findViewById(R.id.textView_name);
        TextView descriptionView = (TextView) listItem.findViewById(R.id.textView_description);
        ImageView imageView = (ImageView) listItem.findViewById(R.id.imageView);
        TextView categoriesView = (TextView) listItem.findViewById(R.id.textView_categories);

        if (_wishlist != null && _wishlist.size() > 0) {

            Movie currentMovie = _wishlist.get(position).get_movie();
            nameView.setText(currentMovie.getName());
            descriptionView.setText(currentMovie.getDescription());
            categoriesView.setText(currentMovie.getCategoriesNamesConcat());

            Picasso.with(_context).load(currentMovie.getPicture()).into(imageView);
            // TODO: DOWNLOAD IMAGE?

        }

         return listItem;
    }

    public void updateData(ArrayList<Wishlist> wishlist){
        this._wishlist = wishlist;
        this.clear();
        this.addAll(wishlist);
        this.notifyDataSetChanged();
    }
}
