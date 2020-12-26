package com.example.finalexam;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;

public class historyFragment extends Fragment {
    final String TAG = "demo;";

    RecyclerView historyRecyclerView;
    historyRecyclerViewAdapter adapter;
    ArrayList<Track> trackArrayList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    Button clearHistoryButton;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public historyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        historyRecyclerView = view.findViewById(R.id.recyclerViewHistoryId);
        historyRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        clearHistoryButton = view.findViewById(R.id.clearHistoryButtonId);
        getTrackHistoryDetails();
        historyRecyclerView.setLayoutManager(layoutManager);
        adapter = new historyRecyclerViewAdapter(trackArrayList);
        historyRecyclerView.setAdapter(adapter);
        clearHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                for(Track track:trackArrayList){
                    db.collection("users").document(mAuth.getCurrentUser().getUid())
                            .collection("history").document(String.valueOf(track.id)).delete();
                }
                trackArrayList.clear();
            }
        });
        return view;
    }

    public void getTrackHistoryDetails(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("history").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                trackArrayList.clear();
                for (QueryDocumentSnapshot documentSnapshot: value) {
                    Track track = new Track();
                    track.albumTitle = (String) documentSnapshot.get("albumTitle");
                    track.trackTitle = (String) documentSnapshot.get("trackTitle");
                    track.id = (long) documentSnapshot.get("trackId");
                    track.artistName = (String) documentSnapshot.get("artistName");
                    track.albumCoverImageMedium = (String) documentSnapshot.get("albumCoverMedium");
                    track.timestamp = (com.google.firebase.Timestamp) documentSnapshot.getData().get("trackPlayedAt");
                    trackArrayList.add(track);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}