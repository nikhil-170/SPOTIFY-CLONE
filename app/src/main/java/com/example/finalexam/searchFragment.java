package com.example.finalexam;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class searchFragment extends Fragment {

    Button searchButton;
    final okhttp3.OkHttpClient client = new OkHttpClient();
    ArrayList<Album> albumsList = new ArrayList<>();
    ArrayList<Album> likedAlbumsList = new ArrayList<>();
    static ablumListRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    EditText editText;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final String TAG = "demo";
    public searchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchButton = view.findViewById(R.id.searchButtonId);
        editText = view.findViewById(R.id.editTextTextPersonName);
        recyclerView = view.findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        Log.d(TAG, "brilliant: ");
        adapter = new ablumListRecyclerViewAdapter(albumsList, likedAlbumsList, getContext(), new ablumListRecyclerViewAdapter.adapterInterface() {
            @Override
            public void callAlbumActivity(Album album) {
                mListener.callDetailAlbumFragment(album);
//                Log.d(TAG, "callAlbumActivity: ");
            }

            @Override
            public void likesDislikeFunctionality(Album album) {
                for (Album alb : likedAlbumsList) {
                    Log.d(TAG, "likesDislikeFunctionality: ");
                    if (alb.id == album.id) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Log.d(TAG, "likesDislikeFunctionality: " + mAuth.getCurrentUser().getUid());
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
                        return;
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
                    Log.d(TAG, "likesDislikeFunctionality: "+ mAuth.getCurrentUser().getUid());
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

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = String.valueOf(editText.getText());
                Log.d(TAG, "search button clicked"+searchText);
                if(searchText.length() == 0){
                    Toast.makeText(getContext(), getResources().getString(R.string.emptysearchToast), Toast.LENGTH_SHORT).show();
                }else {
                    getAlbumDetails(searchText);
                }
            }
        });

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
                            Log.d(TAG, "onEvent: ");
                            Album album = new Album();
                            album.id = (long)ds.getData().get("id");
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
    public interface searchfragmentInterface{
        void callDetailAlbumFragment(Album album);
    }

    public searchfragmentInterface mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (searchfragmentInterface) context;
    }

    public void getAlbumDetails(String artistName){

        HttpUrl url = HttpUrl.parse("https://api.deezer.com/search/album?").newBuilder()
                .addQueryParameter("q", artistName)
                .build();


        Request request = new Request.Builder().url(String.valueOf(url)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        albumsList.clear();
                        for(int i = 0; i< jsonArray.length(); i++){
                            JSONObject albumJSONObject = jsonArray.getJSONObject(i);
                            Album album = new Album(albumJSONObject);
                            albumsList.add(album);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}