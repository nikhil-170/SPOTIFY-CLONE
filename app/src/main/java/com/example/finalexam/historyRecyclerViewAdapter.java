package com.example.finalexam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class historyRecyclerViewAdapter extends RecyclerView.Adapter<historyRecyclerViewAdapter.ViewHolder> {

    ArrayList<Track> trackArrayList = new ArrayList<>();


    public historyRecyclerViewAdapter(ArrayList<Track> TracksList) {
        this.trackArrayList = TracksList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Track track = trackArrayList.get(position);
        holder.albumTitle.setText(track.albumTitle+"");
        holder.artistName.setText(track.artistName);
        holder.trackTitle.setText(track.trackTitle);
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm aa");
        String date = sfd.format(new Date(String.valueOf(trackArrayList.get(position).timestamp.toDate())));

        holder.leftTextView.setText(date+"");
        String imageUrl = track.albumCoverImageMedium;
        Picasso.get().load(imageUrl).into(holder.coverImage);
        holder.like.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return trackArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView coverImage, like;
        TextView artistName, albumTitle, trackTitle, leftTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.albcvrsmHistoryCardViewId);
            artistName = itemView.findViewById(R.id.textViewBottomLeft);
            trackTitle = itemView.findViewById(R.id.textViewTopId);
            albumTitle = itemView.findViewById(R.id.textViewMiddle);
            like = itemView.findViewById(R.id.likeId);
            leftTextView = itemView.findViewById(R.id.textViewLeftId);
        }
    }
}
