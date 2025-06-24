package com.developerali.mylifequran;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import com.developerali.mylifequran.Adapters.PublicAnswerAdapter;
import com.developerali.mylifequran.Models.PublicPostModel;
import com.developerali.mylifequran.Models.User;
import com.developerali.mylifequran.databinding.ActivityRepliesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Replies extends AppCompatActivity {

    ActivityRepliesBinding binding;
    FirebaseFirestore database;
    ProgressDialog dialog;
    FirebaseDatabase data;
    User user;
    FirebaseAuth auth;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRepliesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Replies...");
        getSupportActionBar().setSubtitle("give answer to get points");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(Replies.this);
        dialog.setMessage("uploading answer...");
        dialog.setCancelable(false);

        database = FirebaseFirestore.getInstance();
        data = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        id = getIntent().getStringExtra("id");
        if (auth.getCurrentUser() != null){
            getData();
        }
        getExistAnswers();
        binding.swipe.setRefreshing(true);

        binding.sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar snackbar = Snackbar.make(binding.sentBtn, "Admin Denied Request...", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                params.gravity = Gravity.CENTER_VERTICAL;
                view.setLayoutParams(params);
                snackbar.show();
            }
        });


        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getExistAnswers();
            }
        });




    }


    void getExistAnswers(){
        ArrayList<PublicPostModel> models = new ArrayList<>();
        PublicAnswerAdapter adapter = new PublicAnswerAdapter(Replies.this, models);

        LinearLayoutManager lnm = new LinearLayoutManager(Replies.this);
        lnm.setOrientation(RecyclerView.VERTICAL);
        lnm.setReverseLayout(true);
        lnm.setStackFromEnd(true);

        binding.answerRec.setLayoutManager(lnm);
        binding.answerRec.setAdapter(adapter);
        binding.answerRec.showShimmerAdapter();



        database.collection("answers")
                .document(id)
                .collection("answers")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.isEmpty()){
                            binding.emptyListMessage.setVisibility(View.VISIBLE);
                            binding.answerRec.hideShimmerAdapter();
                            binding.swipe.setRefreshing(false);
                        }else {
                            models.clear();
                            for (DocumentSnapshot snapshot : value.getDocuments()){
                                PublicPostModel ppb = snapshot.toObject(PublicPostModel.class);
                                ppb.setId(snapshot.getString("id"));
                                ppb.setName(snapshot.getString("name"));
                                ppb.setContent(snapshot.getString("content"));
                                ppb.setImageUrl(snapshot.getString("imageUrl"));
//                            ppb.setDateTime(snapshot.getString("dateTime"));
                                ppb.setReply(snapshot.getLong("reply"));
                                models.add(ppb);
                            }
                            adapter.notifyDataSetChanged();
                            binding.answerRec.hideShimmerAdapter();
                            binding.swipe.setRefreshing(false);
                            binding.emptyListMessage.setVisibility(View.GONE);
                        }
                    }
                });

    }

    void getData(){
        final DocumentReference docref = FirebaseFirestore.getInstance()
                .collection("users")
                .document(auth.getCurrentUser().getUid());
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    user = documentSnapshot.toObject(User.class);
                }
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}