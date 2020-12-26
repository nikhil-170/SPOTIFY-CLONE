package com.example.finalexam;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class albumFragment extends Fragment {
    Album album;
    ImageView albumCover, artistImage, share;
    TextView albumtitle, nb_tracks, artistName;
    final String TAG = "demo";
    ArrayList<Track> trackArrayList = new ArrayList<>();
    final okhttp3.OkHttpClient client = new OkHttpClient();
    RecyclerView recyclerView;
    String trackLink;
    LinearLayoutManager layoutManager;
    tracksListRecyclerViewAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public albumFragment(Album album) {
        this.album =album;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        albumCover = view.findViewById(R.id.albumCoverBigId);
        artistImage = view.findViewById(R.id.artistPictureSmallId);
        artistName = view.findViewById(R.id.albumFragmentArtistName);
        share = view.findViewById(R.id.albumFragmentShare);
        nb_tracks = view.findViewById(R.id.album_nb_tracksId);
        albumtitle = view.findViewById(R.id.albumTitleAlbumDetailId);
        recyclerView = view.findViewById(R.id.tracksListRecyclerViewId);
        albumtitle.setText(album.albumTitles+"");
        artistName.setText(album.artisName+"");
        nb_tracks.setText("("+album.nb_tracks+")");
        trackLink = album.trackDetalis;
        getTrackDetails();
        String albumCoverBigUrl = album.alblumCoverImageMedium;
        String artistImageUrl = album.artistCoverImageSmall;
        Picasso.get().load(albumCoverBigUrl).into(albumCover);
        Picasso.get().load(artistImageUrl).into(artistImage);
        layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new tracksListRecyclerViewAdapter(trackArrayList, getContext(), new tracksListRecyclerViewAdapter.trackListAdapterInterface() {
            @Override
            public void trackPlayRecordCall(Track track) {
            writeTrackDetailsHistory(track, album);
            }
        });
        recyclerView.setAdapter(adapter);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call Fragment share screen
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.albumActivityContainerViewId, new sharingFragment(album)).addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public void getTrackDetails(){

        HttpUrl url = HttpUrl.parse("https://api.deezer.com/album").newBuilder()
                        .addEncodedPathSegment(String.valueOf(album.id))
                        .addEncodedPathSegment("tracks")
                        .build();

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try{
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        trackArrayList.clear();
                        for(int i =0 ; i< jsonArray.length(); i++){
                            JSONObject tracksJsonObject = jsonArray.getJSONObject(i);
                            Track track = new Track(tracksJsonObject, album.albumTitles, album.alblumCoverImageMedium);
                            trackArrayList.add(track);
//                            Log.d(TAG, "onResponse: "+ trackArrayList);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            adapter.notifyDataSetChanged();
                            }
                        });
                    }catch (Exception E){
                        E.printStackTrace();
                    }
                }
            }
        });

    }
    public void writeTrackDetailsHistory(Track track, Album album){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String,Object> map = new HashMap<>();
        map.put("trackTitle",track.trackTitle);
        map.put("albumTitle", album.albumTitles);
        map.put("artistName", album.artisName);
        map.put("trackId", track.id);
        map.put("trackPlayedAt", new Timestamp(new Date()));
        map.put("albumCoverMedium", album.alblumCoverImageMedium);
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("history").document(String.valueOf(track.id)).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
}