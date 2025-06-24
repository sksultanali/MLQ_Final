package com.developerali.mylifequran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.developerali.mylifequran.Models.QuestionModel;
import com.developerali.mylifequran.Models.QuestionsResponse;
import com.developerali.mylifequran.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;
    List<QuestionModel> questions;

    int index = 0;
    QuestionModel question;
    CountDownTimer timer, nextTimer;
    FirebaseFirestore database;
    FirebaseAuth auth;
    FirebaseDatabase data;
//    User user;
    String date;

    MediaPlayer worng, right, lock, tiktik, select, whip;
    int correctAnswers = 0;
    ProgressDialog dialog;
    private boolean isAnimationStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(binding.getRoot());

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
        date = simpleDateFormat.format(new Date());

        AlertDialog.Builder obj = new AlertDialog.Builder(QuizActivity.this);
        obj.setTitle("নিয়মাবলী");
        obj.setMessage("১. প্রত্যেক প্রশ্নের জন্য ৩০ সেকেন্ড সময় দেওয়া আছে।" + "\n" +
                "২. উত্তর না দিলে, কোনো নম্বর পাওয়া যাবেনা।" + "\n" +
                "৩. উত্তর সাবমিট হওয়ার আগে পর্যন্ত উত্তর পরিবর্তন করা যাবে।" + "\n" +
                "৪. ৩০ সেকেন্ড সময় পুরন হওয়ার পর উত্তর সাবমিট হবে।" + "\n" +
                "৫. কুইজ চলাকালীন নেট বিচ্ছিন্ন হওয়া, sms বা কল রিসিভ করা যাবেনা।"
        );
        obj.setIcon(R.drawable.error_24);
        obj.setCancelable(false);

        obj.setPositiveButton("বিসমিল্লা", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                obj.setCancelable(true);

                dialog = new ProgressDialog(QuizActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("কুইজ শুরু হচ্ছে...");
                dialog.show();

//                if (auth.getCurrentUser() != null){
//                    user = getData();
//                }

                // playing audio and vibration when user
                worng = MediaPlayer.create(QuizActivity.this, R.raw.wrong);
                worng.setLooping(false);

                right = MediaPlayer.create(QuizActivity.this, R.raw.right);
                right.setLooping(false);

                lock = MediaPlayer.create(QuizActivity.this, R.raw.lock);
                lock.setLooping(false);

                tiktik = MediaPlayer.create(QuizActivity.this, R.raw.tiktik);
                tiktik.setLooping(false);

                whip = MediaPlayer.create(QuizActivity.this, R.raw.whip);
                whip.setLooping(false);

//              Path = getIntent().getStringExtra("path");

                database.collection("quizTime")
                        .document("credentials")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    String extension = documentSnapshot.get("extension", String.class);
                                    String code = documentSnapshot.get("code", String.class);

                                    data.getReference().child("givenAns")
                                                    .child(date).child(auth.getCurrentUser().getUid())
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (!snapshot.exists()){
                                                                readDataFromSheet(extension, code);
                                                            }else {
                                                                Toast.makeText(QuizActivity.this, "Already played !", Toast.LENGTH_LONG).show();
                                                                finish();
                                                                dialog.dismiss();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                }else {
                                    Toast.makeText(QuizActivity.this, "please try again..", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    finish();
                                }
                            }
                        });

//                Random random = new Random();
//                final int rand = random.nextInt(12);
//                database.collection("categories")
//                        .document(Path)
//                        .collection("questions")
//                        .whereGreaterThanOrEqualTo("index", rand)
//                        .orderBy("index")
//                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                                    QuestionModel question = snapshot.toObject(QuestionModel.class);
//                                    questions.add(question);
//                                }
//
//                                setNextQuestion();
//                                dialog.dismiss();
//                            }
//                        });
//                resetTimer();
            }
        });
        obj.show();


        Animation anim_in = AnimationUtils.loadAnimation(QuizActivity.this, android.R.anim.slide_out_right);
        Animation anim_out = AnimationUtils.loadAnimation(QuizActivity.this, android.R.anim.slide_in_left);

        binding.viewFlipper.setInAnimation(anim_in);
        binding.viewFlipper.setOutAnimation(anim_out);
        binding.viewFlipper.setFlipInterval(10000);
        binding.viewFlipper.startFlipping();

        binding.textA.setText(" উত্তর না দিলে, কোনো নম্বর পাওয়া যাবেনা।");
        binding.textC.setText(" ৬০ সেকেন্ড সময় পুরন হওয়ার পর উত্তর সাবমিট হবে।");






    }

    private void readDataFromSheet(String extension, String code) {

//        String url = "https://script.google.com/macros/s/AKfycbySVRJL3TWbrM3K4_De0GrZo-QB5OfeGcx9d68FSUWk76jSG4ZjteuwcuQSI44X7N5T/exec?"+
//                "action=read";
//        String url = "https://jsonkeeper.com/b/0490";
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("items");
//                    for (int i = 0; i<jsonArray.length(); i++){
//                        JSONObject object = jsonArray.getJSONObject(i);
//
//                        String question = object.getString("question");
//                        String optionA = object.getString("optionA");
//                        String optionB = object.getString("optionB");
//                        String optionC = object.getString("optionC");
//                        String optionD = object.getString("optionD");
//                        String answer = object.getString("answer");
//
//                        questions.add(new QuestionModel(question, optionA, optionB, optionC, optionD, answer));
//                    }
//
//
//                    resetTimer();
//                    setNextQuestion();
//                    dialog.dismiss();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(QuizActivity.this, e.getMessage() + " error 001", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(QuizActivity.this, error.getLocalizedMessage() + " error  002", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        RequestQueue queue = Volley.newRequestQueue(QuizActivity.this);
//        queue.add(jsonObjectRequest);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.jsonkeeper.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the ApiService interface
        apiset apiService = retrofit.create(apiset.class);

        // Make the API call
        Call<QuestionsResponse> call = apiService.getQuestions(extension,code);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                if (response.isSuccessful()) {
                    QuestionsResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        questions = apiResponse.getItems();

                        resetTimer();
                        setNextQuestion();
                        dialog.dismiss();

//                        data.getReference().child("alreadyPlayed")
//                                .child(date)
//                                .child(auth.getCurrentUser().getUid())
//                                .setValue(true);
                    }
                } else {
                    Log.e("API Error", "Failed to get questions");
                    Toast.makeText(QuizActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<QuestionsResponse> call, Throwable t) {
                Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });

    }

//    public User getData(){
//        final DocumentReference docref = FirebaseFirestore.getInstance()
//                .collection("users")
//                .document(auth.getCurrentUser().getUid());
//        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()){
//                    user = documentSnapshot.toObject(User.class);
//                }
//            }
//        });
//
//        return user;
//    }

    void resetTimer() {

        lock = MediaPlayer.create(QuizActivity.this, R.raw.lock);

        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
                binding.textB.setText(" উত্তর সাবমিট হতে " + millisUntilFinished/1000 + " সেকেন্ড বাকি আছে" );
                if (millisUntilFinished/1000 == 10){
                    tiktik = MediaPlayer.create(QuizActivity.this, R.raw.tiktik);
                    tiktik.start();
                    isAnimationStarted = true;
                }

                if (isAnimationStarted){
                    binding.timer.setTextColor(getColor(R.color.red));
                    binding.timer.setAnimation(AnimationUtils.loadAnimation(QuizActivity.this, R.anim.blink));
                    binding.timerImg.setAnimation(AnimationUtils.loadAnimation(QuizActivity.this, R.anim.blink));
                }
            }

            @Override
            public void onFinish() {
                binding.option1.setEnabled(false);
                binding.option2.setEnabled(false);
                binding.option3.setEnabled(false);
                binding.option4.setEnabled(false);

                binding.timer.setTextColor(getColor(R.color.white));
                binding.timer.setAnimation(null);
                binding.timerImg.setAnimation(null);
                isAnimationStarted = false;

                binding.option1.setBackground(getResources().getDrawable(R.drawable.option_missed));
                binding.option2.setBackground(getResources().getDrawable(R.drawable.option_missed));
                binding.option3.setBackground(getResources().getDrawable(R.drawable.option_missed));
                binding.option4.setBackground(getResources().getDrawable(R.drawable.option_missed));

                lock.start();
                checkAnswer();
            }
        };
    }

    void nextTimer() {
        
        nextTimer = new CountDownTimer(5000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {

                binding.nextTimeText.setVisibility(View.VISIBLE);
                binding.nextTimeText.setText(getString(R.string.submitting_answer_please_wait) + String.valueOf(millisUntilFinished/1000));
                binding.progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFinish() {
                binding.nextTimeText.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.nextBtn.performClick();
            }
        };
    }

    void showAnswer() {
        if(question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
        try {
            worng = MediaPlayer.create(QuizActivity.this, R.raw.wrong);
            worng.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    void setNextQuestion() {
        if(timer != null)
            timer.cancel();

        reset();

        if(index < questions.size()) {
            whip = MediaPlayer.create(QuizActivity.this, R.raw.whip);
            whip.start();


            binding.questionCounter.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText("Q. " +question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());

            timer.start();
        }

    }

    void checkAnswer() {
        int radioButtonID = binding.radioGroup.getCheckedRadioButtonId();
        if(radioButtonID == -1) {
            Toast.makeText(this, "কোনো উত্তর পাওয়া যায়নি!", Toast.LENGTH_SHORT).show();
        }else {
            RadioButton selectedRadioButton = findViewById(radioButtonID);
            String selectedRbText = selectedRadioButton.getText().toString();
            int mark;

            if(selectedRbText.equals(question.getAnswer())) {
                correctAnswers++;
                //Toast.makeText(this, "correct..", Toast.LENGTH_SHORT).show();
                mark = 2;
            }else {
                mark = 0;
            }

            data.getReference().child("givenAns")
                    .child(date)
                    .child(auth.getCurrentUser().getUid())
                    .child(String.valueOf(index))
                    .setValue(selectedRbText +  " \n Point- " + mark);
        }

        nextTimer();
        if(nextTimer != null)
            nextTimer.cancel();
        nextTimer.start();

        if (tiktik.isPlaying()){
            tiktik.stop();
        }
    }

    void reset() {

        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));

        binding.option1.setEnabled(true);
        binding.option2.setEnabled(true);
        binding.option3.setEnabled(true);
        binding.option4.setEnabled(true);

        binding.radioGroup.clearCheck();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.option_1 || id == R.id.option_2 || id == R.id.option_3 || id == R.id.option_4) {
//            if (timer != null)
//                timer.cancel();
            select = MediaPlayer.create(QuizActivity.this, R.raw.select);

            if (binding.option1.isChecked()) {
                //String SelectedAns = binding.option1.getText().toString();
                //checkAnswer(SelectedAns, binding.option1);
                select.start();
                binding.option1.setBackground(getResources().getDrawable(R.drawable.option_selected));
                binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
                binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
                binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));

            } else if (binding.option2.isChecked()) {
                //String SelectedAns = binding.option2.getText().toString();
                //checkAnswer(SelectedAns, binding.option2);
                select.start();
                binding.option2.setBackground(getResources().getDrawable(R.drawable.option_selected));
                binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
                binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
                binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));

            } else if (binding.option3.isChecked()) {
                //String SelectedAns = binding.option3.getText().toString();
                //checkAnswer(SelectedAns, binding.option3);
                select.start();
                binding.option3.setBackground(getResources().getDrawable(R.drawable.option_selected));
                binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
                binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
                binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
            } else if (binding.option4.isChecked()) {
                //String SelectedAns = binding.option4.getText().toString();
                //checkAnswer(SelectedAns, binding.option4);
                select.start();
                binding.option4.setBackground(getResources().getDrawable(R.drawable.option_selected));
                binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
                binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
                binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
            } else {
                Toast.makeText(this, "Timed Out", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nextBtn) {

            index++;

            if (index < questions.size()) {
                reset();
                setNextQuestion();

            } else {
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("correct", correctAnswers);
                intent.putExtra("total", questions.size());
                intent.putExtra("quiz", "quiz");
                startActivity(intent);

                finish();
            }
        }else if (id == R.id.nextQuestion) {

            if(timer != null)
                timer.cancel();

            binding.option1.setEnabled(false);
            binding.option2.setEnabled(false);
            binding.option3.setEnabled(false);
            binding.option4.setEnabled(false);

            binding.timer.setTextColor(getColor(R.color.white));
            binding.timer.setAnimation(null);
            binding.timerImg.setAnimation(null);
            isAnimationStarted = false;

            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_missed));
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_missed));
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_missed));
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_missed));

            lock.start();
            checkAnswer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (nextTimer != null) {
            nextTimer.cancel(); // Cancel the timer
        }
        if (timer != null) {
            timer.cancel(); // Cancel the timer
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}