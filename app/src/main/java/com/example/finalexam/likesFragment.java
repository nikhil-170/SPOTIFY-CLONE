package com.example.finalexam;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class likesFragment extends Fragment {

    FirebaseAuth mAuth;
    ArrayList<Album> likedAlbumsList = new ArrayList<>();
    RecyclerView recyclerView;
    static ablumListRecyclerViewAdapter adapter;
    likesInterface mListener;
    final String TAG = "demo";
    public likesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (likesFragment.likesInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_likes, container, false);
        getlikesDetails();
        recyclerView = view.findViewById(R.id.likesRecyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter = new ablumListRecyclerViewAdapter(likedAlbumsList, likedAlbumsList, getContext(), new ablumListRecyclerViewAdapter.adapterInterface() {
            @Override
            public void callAlbumActivity(Album album) {
                mListener.goBackToAlbumactivity(album);
            }

            @Override
            public void likesDislikeFunctionality(Album album) {
                mAuth = FirebaseAuth.getInstance();
                for(Album album1: likedAlbumsList){
                    if(album1.id == album.id){
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users")
                                .document(mAuth.getCurrentUser().getUid())
                                .collection("liked")
                                .document(String.valueOf(album.id)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                getlikesDetails();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                        return ;
                    }
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                HashMap<String, Object> map = new HashMap<>();
                map.put("id",album.id);
                map.put("title", album.albumTitles);
                map.put("artistName", album.artisName);
                map.put("artistImage",album.artistCoverImageSmall);
                map.put("image",album.alblumCoverImageMedium);
                map.put("nb_tracks",album.nb_tracks);

                db.collection("users")
                        .document(mAuth.getCurrentUser().getUid())
                        .collection("liked").document(String.valueOf(album.id)).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getlikesDetails();
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void getlikesDetails(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("liked")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        likedAlbumsList.clear();
                        for(QueryDocumentSnapshot ds: value){
                            Album album = new Album();
                            album.id = (long) ds.getData().get("id");
                            album.albumTitles = (String) ds.getData().get("title");
                            album.artisName = (String) ds.getData().get("artistName");
                            album.nb_tracks = (long) ds.getData().get("nb_tracks");
                            album.alblumCoverImageMedium = (String) ds.getData().get("image");
                            album.timestamp = (Timestamp) ds.getData().get("createdAt");
                            likedAlbumsList.add(album);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public interface likesInterface{
        void goBackToAlbumactivity(Album album);
    }

}