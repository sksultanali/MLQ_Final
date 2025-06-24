package com.developerali.mylifequran;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.Adapters.PublicAdapter;
import com.developerali.mylifequran.Models.PublicPostModel;
import com.developerali.mylifequran.Models.User;
import com.developerali.mylifequran.databinding.ActivityJoinCommunityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class JoinCommunity extends AppCompatActivity {

    ActivityJoinCommunityBinding binding;
    FirebaseFirestore database;
    FirebaseDatabase data;
    FirebaseAuth auth;
    PublicAdapter adapter;

    private DocumentSnapshot lastVisibleDocument;
    private boolean isLoading = false;
    Query collectionReference;
    DatabaseReference reference;
    ArrayList<PublicPostModel> models;
    private boolean isLastPageReached = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance();

        if (auth.getCurrentUser() != null){
            getPersonalData();
        }

        binding.writeSomething.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null){
                    Intent i = new Intent(JoinCommunity.this, PostQuestion.class);
                    startActivity(i);
                }else {
                    Snackbar snackbar = Snackbar.make(binding.nestedScrollView2, "logIn to add question...", Snackbar.LENGTH_LONG)
                            .setAction("LogIn", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(JoinCommunity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            });
                    snackbar.show();
                }
            }
        });

        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (auth.getCurrentUser() != null){
                    InitialPage();
                }
                binding.swipe.setRefreshing(false);
            }
        });

        collectionReference = database.collection("public");
        reference = data.getReference().child("questions");

        models = new ArrayList<>();
        adapter = new PublicAdapter(JoinCommunity.this, models);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.questionRec.setLayoutManager(layoutManager);
        binding.questionRec.setAdapter(adapter);
        binding.questionRec.setHasFixedSize(true);


        binding.myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser() != null){
                    collectionReference = database.collection("public")
                            .whereEqualTo("profId", auth.getCurrentUser().getUid());
                    binding.myPosts.setBackground(getDrawable(R.drawable.text_background_active));
                    binding.savedQuestions.setBackground(getDrawable(R.drawable.text_background));
                    binding.myFeed.setBackground(getDrawable(R.drawable.text_background));
                    InitialPage();
                }else {
                    Snackbar.make(view, "Login Required...", Snackbar.LENGTH_LONG)
                            .setAction("Login", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(JoinCommunity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            })
                            .show();
                }
            }
        });


        binding.savedQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser() != null){
                    binding.swipe.setRefreshing(true);
                    collectionReference = database.collection("save")
                            .document(auth.getCurrentUser().getUid())
                            .collection("mySaves");
                    binding.savedQuestions.setBackground(getDrawable(R.drawable.text_background_active));
                    binding.myFeed.setBackground(getDrawable(R.drawable.text_background));
                    binding.myPosts.setBackground(getDrawable(R.drawable.text_background));
                    InitialPage();
                }else {
                    Snackbar.make(view, "Login Required...", Snackbar.LENGTH_LONG)
                            .setAction("Login", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(JoinCommunity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            })
                            .show();
                }

            }
        });

        binding.myFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                models.clear();
                binding.swipe.setRefreshing(true);
                collectionReference = database.collection("public");
                binding.myFeed.setBackground(getDrawable(R.drawable.text_background_active));
                binding.savedQuestions.setBackground(getDrawable(R.drawable.text_background));
                binding.myPosts.setBackground(getDrawable(R.drawable.text_background));
                InitialPage();
            }
        });


        binding.swipe.setRefreshing(true);
        InitialPage();

        binding.loadMore.setOnClickListener(c->{
            binding.loadMore.setVisibility(View.GONE);
            loadNextPage();
        });




        //getData();



    }

    private void getData() {

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){

                        String format = "dd-MM-yyyy HH:mm";

                        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                        try {
                            Date date = sdf.parse(snapshot.getString("dateTime"));
                            long timestamp = date.getTime();
                            Task<Void> docRefnew = FirebaseFirestore.getInstance()
                                    .collection("public")
                                    .document(snapshot.getString("id"))
                                    .update("dateTime", timestamp);


                        } catch (ParseException e) {
                            e.printStackTrace();
                            // Handle the parse exception
                        }

                    }
                }
            }
        });


    }


    private void loadNextPage() {
        binding.nestedScrollView2.smoothScrollTo(0, 0);
        binding.progressBar2.setVisibility(View.VISIBLE);
        binding.loadMore.setVisibility(View.GONE);
        collectionReference // Order by a suitable field
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .startAfter(lastVisibleDocument)
                .limit(20)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        models.clear();

                        lastVisibleDocument = queryDocumentSnapshots.getDocuments()
                                .get(queryDocumentSnapshots.size() - 1);
                        // Process the data from the next page
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                            PublicPostModel ppb = snapshot.toObject(PublicPostModel.class);
                            ppb.setId(snapshot.getString("id"));
                            ppb.setName(snapshot.getString("name"));
                            ppb.setContent(snapshot.getString("content"));
                            ppb.setImageUrl(snapshot.getString("imageUrl"));
                            ppb.setDateTime(snapshot.getLong("dateTime"));
                            ppb.setReply(snapshot.getLong("reply"));
                            models.add(ppb);
                        }
                        adapter.notifyDataSetChanged();
                        binding.swipe.setRefreshing(false);
                        binding.loadMore.setVisibility(View.VISIBLE);
                        binding.emptyListMessage.setVisibility(View.GONE);
                        binding.progressBar2.setVisibility(View.GONE);
                    }else {
                        binding.swipe.setRefreshing(false);
                        binding.progressBar2.setVisibility(View.GONE);
                        binding.emptyListMessage.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any potential errors
                    binding.progressBar2.setVisibility(View.GONE);
                    binding.swipe.setRefreshing(false);
                    binding.loadMore.setVisibility(View.GONE);
                });
    }

    private void InitialPage() {
        binding.progressBar2.setVisibility(View.VISIBLE);
        binding.loadMore.setVisibility(View.GONE);
        models.clear();
        adapter.notifyDataSetChanged();
        collectionReference.orderBy("dateTime", Query.Direction.DESCENDING)
                .limit(20).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            lastVisibleDocument = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);
                            // Process the data from the initial page
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                PublicPostModel ppb = snapshot.toObject(PublicPostModel.class);
                                ppb.setId(snapshot.getString("id"));
                                ppb.setName(snapshot.getString("name"));
                                ppb.setContent(snapshot.getString("content"));
                                ppb.setImageUrl(snapshot.getString("imageUrl"));
                                ppb.setDateTime(snapshot.getLong("dateTime"));
                                ppb.setReply(snapshot.getLong("reply"));
                                models.add(ppb);
                            }
                            adapter.notifyDataSetChanged();
                            binding.loadMore.setVisibility(View.VISIBLE);
                            binding.progressBar2.setVisibility(View.GONE);
                            binding.emptyListMessage.setVisibility(View.GONE);
                            binding.swipe.setRefreshing(false);
                        }else {
                            binding.progressBar2.setVisibility(View.GONE);
                            binding.loadMore.setVisibility(View.GONE);
                            binding.emptyListMessage.setVisibility(View.VISIBLE);
                            binding.swipe.setRefreshing(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar2.setVisibility(View.GONE);
                        binding.loadMore.setVisibility(View.GONE);
                        binding.emptyListMessage.setVisibility(View.VISIBLE);
                        Toast.makeText(JoinCommunity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        binding.swipe.setRefreshing(false);
                    }
                });
    }

    void getPersonalData(){
        final DocumentReference docref = FirebaseFirestore.getInstance()
                .collection("users")
                .document(auth.getCurrentUser().getUid());
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    User user = documentSnapshot.toObject(User.class);
                    if (user.getImageUrl().equalsIgnoreCase("null")){
                        binding.myProImage.setImageResource(R.drawable.placeholder);
                    }else {
                        Glide.with(JoinCommunity.this).load(user.getImageUrl()).into(binding.myProImage);
                    }
                }
                binding.swipe.setRefreshing(false);
            }
        });
    }
}