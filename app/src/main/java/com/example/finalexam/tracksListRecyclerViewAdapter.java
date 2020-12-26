package com.example.finalexam;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class tracksListRecyclerViewAdapter extends RecyclerView.Adapter<tracksListRecyclerViewAdapter.tracksViewHolder> {


    ArrayList<Track> trackArrayList;
    Context context;
    Boolean aBoolean = true;
    trackListAdapterInterface mListener;

    public tracksListRecyclerViewAdapter(ArrayList<Track> trackArrayList, Context context, trackListAdapterInterface mListener) {
        this.mListener = mListener;
        this.trackArrayList = trackArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public tracksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracks_list_card_view, parent, false);

            return new tracksListRecyclerViewAdapter.tracksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final tracksViewHolder holder, int position) {
        final Track track = trackArrayList.get(position);
        holder.trackDuration.setText(track.trackDuration+"");
        holder.trackTitle.setText(track.trackTitle);
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        final String mp3Url = track.mp3Link;
        try {
            mediaPlayer.setDataSource(mp3Url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    mListener.trackPlayRecordCall(track);
                    holder.playPauseButton.setImageResource(R.drawable.pause_button);

                }else{
                    mediaPlayer.stop();
                    holder.playPauseButton.setImageResource(R.drawable.play_button);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return trackArrayList.size();
    }

    public static class tracksViewHolder extends RecyclerView.ViewHolder{
        TextView trackTitle, trackDuration;
        ImageView playPauseButton;
        public tracksViewHolder(@NonNull View itemView) {
            super(itemView);
            trackTitle = itemView.findViewById(R.id.trackTitleHistoryCardViewId);
            trackDuration = itemView.findViewById(R.id.trackDurationId);
            playPauseButton = itemView.findViewById(R.id.playPauseImageId);
        }
    }
    public interface trackListAdapterInterface{
        void trackPlayRecordCall(Track track);
    }
}
//
//SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
//    String date = sfd.format(new Date(String.valueOf(allAlbumsShared.get(position).createdAt.toDate())));
