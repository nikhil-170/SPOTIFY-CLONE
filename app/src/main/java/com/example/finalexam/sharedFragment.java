package com.example.finalexam;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;


public class sharedFragment extends Fragment {
    RecyclerView sharedFragmentRecyclerView;
    sharedFragmentRecyclerViewAdapter sharedFragmentAdapter;
    ArrayList<Album> allAlbumsShared = new ArrayList<>();
    FirebaseAuth mAuth;
    SharedFragmentInterface mListener;
    final String TAG = "TAG";
    public sharedFragment() {
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
        View view = inflater.inflate(R.layout.fragment_shared, container, false);
        sharedFragmentRecyclerView = view.findViewById(R.id.sharedRecyclerVierId);
        sharedFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        sharedFragmentAdapter = new sharedFragmentRecyclerViewAdapter(allAlbumsShared, new sharedFragmentRecyclerViewAdapter.SharedAdapterInterface() {
            @Override
            public void callAlbumActivity(Album album) {
                mListener.callToAlbumActivity(album);
            }
        });
        sharedFragmentRecyclerView.setAdapter(sharedFragmentAdapter);
        getAllAlbumsShared();

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SharedFragmentInterface) context;
    }

    public interface SharedFragmentInterface{
        void callToAlbumActivity(Album album);
    }

    public void getAllAlbumsShared(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("shared")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        allAlbumsShared.clear();
                        for(QueryDocumentSnapshot ds: value){
                            Album album = new Album();
                            album.id = (long) ds.getData().get("albumID");
                            album.albumTitles = (String) ds.getData().get("albumTitle");
                            album.alblumCoverImageMedium = (String) ds.getData().get("image");
                            album.nb_tracks = (Long) ds.getData().get("nb_tracks");
                            album.artisName = (String) ds.getData().get("artistName");
                            album.artistCoverImageSmall = (String) ds.getData().get("artistImage");
                            album.sharedUserName = (String) ds.getData().get("sharedUsername");
                            album.timestamp = (Timestamp) ds.getData().get("sharedTime");

                            allAlbumsShared.add(album);
                        }
                        sharedFragmentAdapter.notifyDataSetChanged();
                    }
                });
    }
}