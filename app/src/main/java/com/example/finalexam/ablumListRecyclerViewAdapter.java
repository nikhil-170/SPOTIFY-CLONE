package com.example.finalexam;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ablumListRecyclerViewAdapter extends RecyclerView.Adapter<ablumListRecyclerViewAdapter.albumViewHolder> {
    Context context;
    ArrayList<Album> albumsList = new ArrayList<>();
    public adapterInterface mListener;
    ArrayList<Album> albumsliked = new ArrayList<>();

    public ablumListRecyclerViewAdapter(ArrayList<Album> albums, ArrayList<Album> albumsliked, Context context, adapterInterface mListener){
        this.mListener = mListener;
        this.context = context;
        this.albumsList = albums;
        this.albumsliked = albumsliked;
        Log.d("demo", "onBindViewHolder: ");
    }

    @NonNull
    @Override
    public albumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_view, parent, false);
        return new albumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull albumViewHolder holder, int position) {
        final Album album = albumsList.get(position);
        holder.albumTitle.setText(album.albumTitles+"");
        holder.noOfTracks.setText(album.nb_tracks+"");
        holder.artistName.setText(album.artisName+"");
        String imageUrl = album.alblumCoverImageMedium;
        Picasso.get().load(imageUrl).into(holder.albumCoverSmall);
        holder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.likesDislikeFunctionality(album);
            }
        });
        Log.d("demo", "onBindViewHolder: "+album.id);
        boolean bln = false;
        for(Album albumTemp: albumsliked){
            if(albumTemp.id == album.id){
                holder.likeImage.setImageResource(R.drawable.like_favorite);
                bln = true;
                break;
            }
        }
        if(!bln) holder.likeImage.setImageResource(R.drawable.like_not_favorite);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.callAlbumActivity(album);

            }
        });

    }

    @Override
    public int getItemCount() {
        return albumsList.size();
    }

    public static class albumViewHolder extends RecyclerView.ViewHolder{


        ImageView albumCoverSmall, likeImage;
        CardView cardView;
        TextView albumTitle, artistName, noOfTracks;
        public albumViewHolder(@NonNull View itemView) {

            super(itemView);

            albumCoverSmall = itemView.findViewById(R.id.albcvrsmHistoryCardViewId);
            likeImage = itemView.findViewById(R.id.likeId);
            albumTitle = itemView.findViewById(R.id.textViewTopId);
            artistName = itemView.findViewById(R.id.textViewMiddle);
            noOfTracks = itemView.findViewById(R.id.textViewBottomLeft);
            cardView = itemView.findViewById(R.id.cardViewID);

        }
    }


    public interface adapterInterface{
        void callAlbumActivity(Album album);
        void likesDislikeFunctionality(Album album);
    }
}
