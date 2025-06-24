package com.developerali.mylifequran;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.Models.User;
import com.developerali.mylifequran.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore database;
    FirebaseDatabase data;
    FirebaseStorage storage;
    ProgressDialog dialog;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        database = FirebaseFirestore.getInstance();
        data = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setMessage("connecting to server..");
        dialog.setCancelable(false);


        if (auth.getCurrentUser() != null && isConnectedNetwork(ProfileActivity.this)){

            binding.shimmerLayout.startShimmerAnimation();

            binding.LogInContainer.setVisibility(View.GONE);
            binding.ProfileContainer.setVisibility(View.VISIBLE);

            final DocumentReference docref = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(auth.getCurrentUser().getUid());
            docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        User user = documentSnapshot.toObject(User.class);

                        binding.PName.setText(user.getName());
                        binding.PEmail.setText(user.getEmail());

                        if (user.getPhone() != null){
                            binding.PPhone.setText(user.getPhone());
                        }else {
                            binding.PPhone.setText("NA");
                        }

                        if (user.getPoints() != null){
                            binding.PWallet.setText(String.valueOf(user.getPoints()));
                        }
                        if (user.getReply() != null){
                            binding.reply.setText(String.valueOf(user.getReply()));
                        }


                        if (user.getImageUrl().equalsIgnoreCase("null")){
                            binding.proImage.setImageResource(R.drawable.placeholder);
                        }else {
                            Glide.with(ProfileActivity.this).load(user.getImageUrl()).into(binding.proImage);
                        }
                        binding.shimmerLayout.stopShimmerAnimation();
                    }
                }
            });
            dialog.dismiss();

        }else {
            binding.LogInContainer.setVisibility(View.VISIBLE);
            binding.ProfileContainer.setVisibility(View.GONE);
        }


        binding.leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, LeaderBoard.class);
                startActivity(i);
            }
        });

        binding.proImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent();
                j.setAction(Intent.ACTION_GET_CONTENT); //sob content samne esbe!
                j.setType("image/*"); //datatype bola holo
                startActivityForResult(j, 108);
            }
        });

        binding.LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        binding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null && isConnectedNetwork(ProfileActivity.this)){
                    Toast.makeText(ProfileActivity.this, "Already Logged In", Toast.LENGTH_SHORT).show();
                }else if (auth.getCurrentUser() != null && isConnectedNetwork(ProfileActivity.this) == false){
                    Toast.makeText(ProfileActivity.this, "Intent Not Connected", Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        binding.userGuideLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.isChromeCustomTabsSupported(ProfileActivity.this)){
                    Helper.openChromeTab("https://mylifequran1.blogspot.com/p/user-guidelines.html", ProfileActivity.this);
                }else {
                    Intent i = new Intent(ProfileActivity.this, WebViewActivity.class);
                    i.putExtra("share", "https://mylifequran1.blogspot.com/p/user-guidelines.html");
                    startActivity(i);
                }
            }
        });

        binding.faQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.isChromeCustomTabsSupported(ProfileActivity.this)){
                    Helper.openChromeTab("https://mylifequran1.blogspot.com/p/faqs.html", ProfileActivity.this);
                }else {
                    Intent i = new Intent(ProfileActivity.this, WebViewActivity.class);
                    i.putExtra("share", "https://mylifequran1.blogspot.com/p/faqs.html");
                    startActivity(i);
                }
            }
        });

        binding.privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.isChromeCustomTabsSupported(ProfileActivity.this)){
                    Helper.openChromeTab("https://mylifequran1.blogspot.com/p/privacy-policy.html", ProfileActivity.this);
                }else {
                    Intent i = new Intent(ProfileActivity.this, WebViewActivity.class);
                    i.putExtra("share", "https://mylifequran1.blogspot.com/p/privacy-policy.html");
                    startActivity(i);
                }
            }
        });

        binding.donateSomething.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.isChromeCustomTabsSupported(ProfileActivity.this)){
                    Helper.openChromeTab("https://mylifequran1.blogspot.com/p/donate-something.html", ProfileActivity.this);
                }else {
                    Intent i = new Intent(ProfileActivity.this, WebViewActivity.class);
                    i.putExtra("share", "https://mylifequran1.blogspot.com/p/donate-something.html");
                    startActivity(i);
                }
            }
        });

        binding.sendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.isChromeCustomTabsSupported(ProfileActivity.this)){
                    Helper.openChromeTab("https://docs.google.com/forms/d/e/1FAIpQLSdWPETem0a9uPv5xxgZot8FT0RYo1KRhj5OVjgdDfyO5lH9sw/viewform?usp=sf_link", ProfileActivity.this);
                }else {
                    Intent i = new Intent(ProfileActivity.this, WebViewActivity.class);
                    i.putExtra("share", "https://docs.google.com/forms/d/e/1FAIpQLSdWPETem0a9uPv5xxgZot8FT0RYo1KRhj5OVjgdDfyO5lH9sw/viewform?usp=sf_link");
                    startActivity(i);
                }
            }
        });
    }

    public static boolean isConnectedNetwork (Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 108){
            if (data.getData() != null){
                selectedImage = data.getData();
                binding.proImage.setImageURI(data.getData());
                dialog.show();
                UploadData();
            }
        }
    }

    private void UploadData() {
        String key = data.getReference().push().getKey();
        StorageReference reference = storage.getReference().child("profiles").child(key);     // storage e folder banalam
        reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            database.collection("users").document(auth.getCurrentUser().getUid())
                                    .update("imageUrl", imageUrl)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            Toast.makeText(ProfileActivity.this, "image updated...", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
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