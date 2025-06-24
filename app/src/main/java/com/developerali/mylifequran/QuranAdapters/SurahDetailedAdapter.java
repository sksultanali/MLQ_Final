package com.developerali.mylifequran.QuranAdapters;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.mylifequran.QuranModel.Ayah;
import com.developerali.mylifequran.QuranModel.detaildAyah;
import com.developerali.mylifequran.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SurahDetailedAdapter extends RecyclerView.Adapter<SurahDetailedAdapter.ViewHolder>{

    Context context;
    ArrayList<Ayah> audio;
    ArrayList<detaildAyah> bengali;
    RecyclerView recyclerView;
    int complete;


    public SurahDetailedAdapter(Context context, ArrayList<Ayah> audio, ArrayList<detaildAyah> bengali, RecyclerView recyclerView){
        this.context = context;
        this.audio = audio;
        this.bengali = bengali;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_detailed_surah, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ayah audioModel = audio.get(position);
        detaildAyah bengaliModel = bengali.get(position);

        holder.play.setEnabled(false);
        holder.arbiText.setText(audioModel.getText());
        holder.bangText.setText(bengaliModel.getText());
        holder.verseNum.setText(audioModel.getNumber());

        complete = 1;

        if (audioModel.getAudio() != null){

            Handler myhandler = new Handler();
            MediaPlayer mediaPlayer;
            String audioUrl;

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audioUrl = audioModel.getAudio();

            myhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.startTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) mediaPlayer.getCurrentPosition()),
                            TimeUnit.MILLISECONDS.toSeconds((long) mediaPlayer.getCurrentPosition()) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) mediaPlayer.getCurrentPosition()))));
                    myhandler.postDelayed(this, 100);
                    holder.progressBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }, 100);
            holder.progressBar.setProgress(mediaPlayer.getCurrentPosition()/1000 );


            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mediaPlayer.isPlaying()){
                        holder.play.setBackgroundResource(R.drawable.play_arrow_24);
                        mediaPlayer.pause();
                    }
                    else {
                        holder.play.setBackgroundResource(R.drawable.pause_24);
                        mediaPlayer.start();
                    }

                }
            });


            holder.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "please wait a while...", Toast.LENGTH_SHORT).show();
                    holder.play.setBackgroundResource(R.drawable.pause_24);
                    holder.play.setEnabled(true);
                    mediaPlayer.reset();

                    ViewHolder myholder = holder;

                    try {
                        mediaPlayer.setDataSource(audioUrl);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int finalTime = mediaPlayer.getDuration();
                    int startTime = mediaPlayer.getCurrentPosition();

                    myholder.startTime.setText(Integer.toString(mediaPlayer.getCurrentPosition()/1000));
                    myholder.startTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

                    myholder.endTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
                    myholder.progressBar.setMax(finalTime);

                    myhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myholder.startTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) mediaPlayer.getCurrentPosition()),
                                    TimeUnit.MILLISECONDS.toSeconds((long) mediaPlayer.getCurrentPosition()) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) mediaPlayer.getCurrentPosition()))));
                            myhandler.postDelayed(this, 100);
                            myholder.progressBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    }, 100);
                    myholder.progressBar.setProgress(mediaPlayer.getCurrentPosition()/1000 );

                    mediaPlayer.start();



                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            holder.play.setBackgroundResource(R.drawable.play_arrow_24);
                        }
                    });





//                    startMedia(mediaPlayer, audioUrl, holder, myhandler);
//                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            int currentPosition = holder.getAdapterPosition();
//                            currentPosition++;
//
//                            if (currentPosition < getItemCount()) {
//                                // If there is a next Ayah, update the ViewHolder and start playback
//                                ViewHolder nextViewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(currentPosition);
//
//                                if (nextViewHolder != null) {
//
//                                    nextViewHolder.play.setBackgroundResource(R.drawable.play_arrow_24);
//                                    nextViewHolder.play.setEnabled(true);
//
//                                    // Reset the MediaPlayer and start playback for the next Ayah
//                                    mediaPlayer.reset();
//                                    try {
//                                        startMedia(mediaPlayer, audio.get(complete).getAudio(), nextViewHolder, myhandler);
//                                        complete++;
//                                    }catch (Exception e){
//
//                                    }
//                                }
//                            }
//                        }
//                    });

                }
            });


            holder.loop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isLooping()){
                        mediaPlayer.setLooping(false);
                        holder.loop.setBackgroundResource(R.drawable.loop_24_start);
                        Toast.makeText(context, "looping is off", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mediaPlayer.setLooping(true);
                        holder.loop.setBackgroundResource(R.drawable.setup_24);
                        Toast.makeText(context, "looping is on", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }




    }

    private void startMedia(MediaPlayer mediaPlayer, String audioUrl, ViewHolder holder, Handler myhandler) {
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                // Media playback has completed, you can handle this event here
//
//                if (!mediaPlayer.isLooping()){
//
//                    int currentPosition = holder.getAdapterPosition();
//                    currentPosition++;
//
//                    if (currentPosition < getItemCount()) {
//                        // If there is a next Ayah, update the ViewHolder and start playback
//                        ViewHolder nextViewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(currentPosition);
//
//                        if (nextViewHolder != null) {
//                            Ayah nextAudioModel = audio.get(currentPosition);
//                            String nextAudioUrl = nextAudioModel.getAudio();
//
//                            nextViewHolder.play.setBackgroundResource(R.drawable.play_arrow_24);
//                            nextViewHolder.play.setEnabled(true);
//
//                            // Reset the MediaPlayer and start playback for the next Ayah
//                            mediaPlayer.reset();
//                            try {
//                                startMedia(mediaPlayer, audio.get(complete).getAudio(), holder, myhandler);
//                                complete++;
//                            }catch (Exception e){
//
//                            }
//                        }
//                    }
//                }
//            }
//        });


        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int finalTime = mediaPlayer.getDuration();
        int startTime = mediaPlayer.getCurrentPosition();

        holder.startTime.setText(Integer.toString(mediaPlayer.getCurrentPosition()/1000));
        holder.startTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

        holder.endTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
        holder.progressBar.setMax(finalTime);

        myhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.startTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) mediaPlayer.getCurrentPosition()),
                        TimeUnit.MILLISECONDS.toSeconds((long) mediaPlayer.getCurrentPosition()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) mediaPlayer.getCurrentPosition()))));
                myhandler.postDelayed(this, 100);
                holder.progressBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 100);
        holder.progressBar.setProgress(mediaPlayer.getCurrentPosition()/1000 );

        mediaPlayer.start();
    }

    @Override
    public int getItemCount() {
        return audio.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView play, pause, loop;
        TextView startTime, endTime;
        ProgressBar progressBar;
        TextView verseNum, arbiText, bangText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            play = itemView.findViewById(R.id.play);
            pause = itemView.findViewById(R.id.pause);
            loop = itemView.findViewById(R.id.loop);
            startTime = itemView.findViewById(R.id.currenttime);
            endTime = itemView.findViewById(R.id.totaltime);
            progressBar = itemView.findViewById(R.id.progress_bar);

            verseNum = itemView.findViewById(R.id.numberVerse);
            arbiText = itemView.findViewById(R.id.arbiText);
            bangText = itemView.findViewById(R.id.bangText);
        }
    }




}
