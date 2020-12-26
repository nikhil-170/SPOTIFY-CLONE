package com.example.finalexam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class sharedFragmentRecyclerViewAdapter extends RecyclerView.Adapter<sharedFragmentRecyclerViewAdapter.ViewHolder>{


    ArrayList<Album> sharedAlbumsList = new ArrayList<>();
    SharedAdapterInterface mListener;

    public sharedFragmentRecyclerViewAdapter(ArrayList<Album> allAlbumsShared, SharedAdapterInterface mListener){
        this.sharedAlbumsList = allAlbumsShared;
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_view, parent, false);
        return new sharedFragmentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Album album = sharedAlbumsList.get(position);
        holder.albumTitle.setText(album.albumTitles);
        holder.artistName.setText(album.artisName);
        holder.albumNbTracks.setText(album.nb_tracks+"");
        holder.Like.setVisibility(holder.itemView.INVISIBLE);
        holder.sharedUserName.setText(album.sharedUserName);
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm aa");
        String date = sfd.format(new Date(String.valueOf(sharedAlbumsList.get(position).timestamp.toDate())));
        holder.timeStamp.setText(date);
        Picasso.get().load(album.alblumCoverImageMedium).into(holder.albumCoverImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.callAlbumActivity(album);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sharedAlbumsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView albumCoverImage, Like;
        TextView albumTitle, artistName, albumNbTracks, timeStamp, sharedUserName ;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumCoverImage = itemView.findViewById(R.id.albcvrsmHistoryCardViewId);
            Like = itemView.findViewById(R.id.likeId);
            albumTitle = itemView.findViewById(R.id.textViewTopId);
            artistName = itemView.findViewById(R.id.textViewMiddle);
            albumNbTracks = itemView.findViewById(R.id.textViewBottomLeft);
            timeStamp = itemView.findViewById(R.id.textViewRight);
            sharedUserName = itemView.findViewById(R.id.textViewLeftId);
            cardView = itemView.findViewById(R.id.cardViewID);
        }
    }

    public interface SharedAdapterInterface {
        void callAlbumActivity(Album album);
    }
}
