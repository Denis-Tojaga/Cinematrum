package com.mob3000.cinematrum.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {

    private ArrayList<Category> CategoryList;
    private CategoryAdapter.CategoryClickListener listener;

    public CategoryAdapter(ArrayList<Category> CategoryList, Context context, CategoryClickListener listener)
    {
        this.CategoryList = CategoryList;
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //TODO picture property needs to be added to the database
        String picture = CategoryList.get(position).getImageUrl();
        System.out.println(picture);
        String name = CategoryList.get(position).getName();
        holder.name.setText(name);
        Picasso.get().load(picture).placeholder(R.drawable.custom_bacground).into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return CategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private ImageView picture;
        private ConstraintLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.category_name);
            picture = itemView.findViewById(R.id.categoryImage);
            parent = itemView.findViewById(R.id.categoryItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
    public interface CategoryClickListener{
        void onClick(View v, int position);
    }
}
