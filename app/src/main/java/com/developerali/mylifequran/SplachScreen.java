package com.developerali.mylifequran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import com.developerali.mylifequran.databinding.ActivitySplachScreenBinding;

public class SplachScreen extends AppCompatActivity {

    ActivitySplachScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplachScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_500));
        }

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread td = new Thread(){

            public void run(){
                try {
                    sleep(4000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    Intent i = new Intent(SplachScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };td.start();


        // playing audio and vibration when user se reques
        MediaPlayer mp = MediaPlayer.create(SplachScreen.this, R.raw.greetings);
        mp.setLooping(false);
        mp.start();

    }
}