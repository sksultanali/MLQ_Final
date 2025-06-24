package com.developerali.mylifequran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.Models.PublicPostModel;
import com.developerali.mylifequran.Models.User;
import com.developerali.mylifequran.databinding.ActivityPostQuestionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostQuestion extends AppCompatActivity {

    ActivityPostQuestionBinding binding;
    FirebaseFirestore database;
    FirebaseDatabase data;
    ProgressDialog dialog;
    FirebaseAuth auth;
    String Name, Question, ImageUrl, DateTime;
    long reply, share;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(PostQuestion.this);
        dialog.setCancelable(false);
        dialog.setMessage("post updating...");

        getData();

        getSupportActionBar().setTitle("Post Question...");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






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
                    binding.nameMy.setText(user.getName());
                    if (user.getImageUrl().equalsIgnoreCase("null")){
                        binding.myProImage.setImageResource(R.drawable.placeholder);
                    }else {
                        Glide.with(PostQuestion.this).load(user.getImageUrl()).into(binding.myProImage);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.postThis){

            if (binding.useName.isChecked()){
                Name = user.getName();
                ImageUrl = user.getImageUrl();
            }else {
                Name = "Anonymous";
                ImageUrl = "null";
            }

            Question = binding.questionPost.getText().toString();
            if (Question.isEmpty()){
                binding.questionPost.setError("Required");
            }else {
                if (isConnectedNetwork(PostQuestion.this)){
                    PostThis(Name, ImageUrl, Question);
                }else {
                    Snackbar snackbar = Snackbar.make(binding.toolbar, "Internet Not Connected...", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                    params.gravity = Gravity.CENTER_VERTICAL;
                    view.setLayoutParams(params);
                    snackbar.show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean isConnectedNetwork (Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void PostThis(String Name, String ImageUrl, String Question) {

        dialog.show();

        reply = 0;
        share = 0;

        String key = data.getReference().push().getKey();
        PublicPostModel ppb = new PublicPostModel();
        ppb.setId(key);
        ppb.setName(Name);
        ppb.setProfId(auth.getCurrentUser().getUid());
        ppb.setContent(Question);
        ppb.setImageUrl(ImageUrl);
        ppb.setDateTime(new Date().getTime());

//        data.getReference().child("questions")
//                        .push().setValue(ppb)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                dialog.dismiss();
//                                Toast.makeText(PostQuestion.this, "Posted Question", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(PostQuestion.this, JoinCommunity.class);
//                                startActivity(i);
//                                finish();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        dialog.dismiss();
//                        Toast.makeText(PostQuestion.this, e.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });

        database.collection("public")
                .document(key)
                .set(ppb)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(PostQuestion.this, "Posted Question", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PostQuestion.this, JoinCommunity.class);
                        startActivity(i);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(PostQuestion.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}