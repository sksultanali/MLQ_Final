package com.developerali.mylifequran;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developerali.mylifequran.Models.ResultModel;
import com.developerali.mylifequran.databinding.ActivityResultBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    FirebaseFirestore database;
    FirebaseDatabase data;
    FirebaseAuth auth;
    String message;
//    User user;
    MediaPlayer mediaPlayer;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quiz Reward");

        dialog = new ProgressDialog(ResultActivity.this);
        dialog.setMessage("getting ready...");
        dialog.setCancelable(false);

        database = FirebaseFirestore.getInstance();
        data = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        int score = getIntent().getIntExtra("correct", 0);
        int total = getIntent().getIntExtra("total", 0);
        String type = getIntent().getStringExtra("quiz");

        int earnedCoin = score * 2;

        binding.score.setText("গ্রুপে জানানো হবে !");
        //binding.earnedCoins.setText(String.valueOf(earnedCoin));
        String key = data.getReference().push().getKey();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy  |  h:mm a");
        String dateTime = dateFormat.format(calendar.getTimeInMillis());


        if (earnedCoin == 0){
            binding.animationWellDone.setVisibility(View.GONE);
            binding.animationLike.setVisibility(View.VISIBLE);
            binding.textViewWish.setText("Better Luck Next Time");

        }

        if (type.equalsIgnoreCase("quiz")){

            dialog.show();
            uploadOnDatabase(score, earnedCoin, dateTime);

            //Toast.makeText(this, "add korlam na points: "+ earnedCoin, Toast.LENGTH_SHORT).show();
        }else {
            binding.textShow.setVisibility(View.VISIBLE);
        }



        // playing audio and vibration when user se request
        mediaPlayer = MediaPlayer.create(ResultActivity.this, R.raw.finishring);
        mediaPlayer.setLooping(false);
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(ResultActivity.this, R.raw.finishring);
            } mediaPlayer.start();
        } catch(Exception e) { e.printStackTrace(); }

        binding.myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, LeaderBoard.class);
                startActivity(i);
                finish();
            }
        });

        binding.shareMyScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String message = readyText(String.valueOf(earnedCoin));
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                if (sharingIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(sharingIntent,"Quiz Rewards..."));
                }
            }
        });


    }

    private void uploadOnDatabase(int score, int earnedCoin, String dateTime) {
        DatabaseReference dr = data.getReference().child("ramadanWinners")
                .child(auth.getCurrentUser().getUid());
        dialog.show();

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int points = snapshot.child("points").getValue(Integer.class);
                    int answers = snapshot.child("correct").getValue(Integer.class);

                    ResultModel model = new ResultModel(auth.getCurrentUser().getEmail(),
                            answers + score,
                            points + earnedCoin,
                            dateTime);

                    dr.setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            try {
                                addResult(score, earnedCoin, dateTime);
                            }catch (Exception e){
                                dialog.dismiss();
                                Toast.makeText(ResultActivity.this, "POINTS ADDED In LEADERBOARD", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(ResultActivity.this, "POINTS ADDED", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    ResultModel model = new ResultModel(auth.getCurrentUser().getEmail(),
                            score,
                            earnedCoin,
                            dateTime);
                    
                    dr.setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            try {
                                addResult(score, earnedCoin, dateTime);
                            }catch (Exception e){
                                dialog.dismiss();
                                Toast.makeText(ResultActivity.this, "POINTS ADDED In LEADERBOARD", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addResult(int score, int earnedCoin, String dateTime) {
        String url = "https://script.google.com/macros/s/AKfycbwv5zsP2ST497EbkCcD6xN3jOukE05M7WqH6a6rXOEkxCjinx6crEElTwZfMunoPhYM/exec?";
        url = url + "action=create&id="+auth.getCurrentUser().getUid()+"&name="+auth.getCurrentUser().getEmail()
                +"&image="+auth.getCurrentUser().getPhotoUrl()+"&right="+score+"&coins="+earnedCoin
                +"&dateTime="+dateTime;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("created")){
                    database.collection("users")
                            .document(auth.getCurrentUser().getUid())
                            .update("points", FieldValue.increment(earnedCoin))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Snackbar snackbar = Snackbar.make(binding.layout, "POINTS ADDED in PROFILE", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(ResultActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(ResultActivity.this);
        queue.add(stringRequest);
    }


    public String readyText(String Reward){

        message ="আস্সালামুআলাইকুম,\n" + "\n" +
                        "My Life Qur'an পরিচালিত *কুইজ প্রতিযোগিতায়* অংশগ্রহণ করেছিলাম। আলহামদুলিল্লাহ সফল হয়েছি। এই কুইজ থেকে *" + Reward+" Reward* পেয়েছি।\n" +
                        "\n" +
                        "আমার মতো তুমিও *কুইজ খেলে এবং হাদিস কোরান পড়ে* অনেক পুরস্কার ও reward পেতে পারো....\n" +
                        "\n" +
                        "এক্ষুনি My Life Qura'n app ডাউনলোড করো। \n" +

                        "----------------------------\n" + "\n" +
                        "       Presented By\n" +
                        "       *My Life Qur'an*\n" +
                        "\n" +
                        "Download App From PlayStore\n"+
                        "https://play.google.com/store/apps/details?id=com.developerali.mylifequran";
        dialog.dismiss();
        return message;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}