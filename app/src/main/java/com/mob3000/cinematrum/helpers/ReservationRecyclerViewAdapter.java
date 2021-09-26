package com.mob3000.cinematrum.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.MoviesCinemas;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReservationRecyclerViewAdapter extends RecyclerView.Adapter<ReservationRecyclerViewAdapter.ViewHolder> {

    private ArrayList<MoviesCinemas> moviesCinemas;
    private Context ctx;
    private OnItemClickListener listener;

    private int selectedPosition = -1;

    public ReservationRecyclerViewAdapter(Context ctx, ArrayList<MoviesCinemas> moviesCinemas, OnItemClickListener listener) {
        this.ctx = ctx;
        this.moviesCinemas = moviesCinemas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MoviesCinemas currentMoviesCinemas = moviesCinemas.get(position);

        holder.txtPrice.setText(currentMoviesCinemas.getPrice() + "€");
        holder.txtSeatsAvailable.setText(currentMoviesCinemas.getSeatsAvailable() + "");
        DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        holder.txtDate.setText(dateFormat.format(currentMoviesCinemas.getDate()));

        DateFormat timeFormat = new SimpleDateFormat("hh:mm");
        holder.txtTime.setText(timeFormat.format(currentMoviesCinemas.getDate()));

        if (this.selectedPosition == position){
            holder.itemView.setBackgroundColor(ctx.getResources().getColor(R.color.white));
            holder.txtPrice.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.txtTime.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.txtSeatsAvailable.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.txtDate.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.txtSeatsAvailableText.setTextColor(ctx.getResources().getColor(R.color.black));
        }
        else {
            holder.itemView.setBackgroundColor(ctx.getResources().getColor(R.color.background_theme));
            holder.txtPrice.setTextColor(ctx.getResources().getColor(R.color.white));
            holder.txtTime.setTextColor(ctx.getResources().getColor(R.color.white));
            holder.txtSeatsAvailable.setTextColor(ctx.getResources().getColor(R.color.white));
            holder.txtDate.setTextColor(ctx.getResources().getColor(R.color.white));
            holder.txtSeatsAvailableText.setTextColor(ctx.getResources().getColor(R.color.white));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return this.moviesCinemas.size();
    }

    public int getSelectedPosition(){return this.selectedPosition;}
    public void setSelectedPosition(int newPosition) {this.selectedPosition = newPosition;}

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPrice;
        TextView txtTime;
        TextView txtSeatsAvailable;
        TextView txtDate;
        TextView txtSeatsAvailableText;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtSeatsAvailable = itemView.findViewById(R.id.txtSeatsAvailable);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtSeatsAvailableText = itemView.findViewById(R.id.txtSeatsAvailableText);
            view = itemView;
        }
    }
}
