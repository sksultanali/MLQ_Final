package com.developerali.mylifequran.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.Models.ResultModel;
import com.developerali.mylifequran.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.Viewholder>{

    Context context;
    ArrayList<ResultModel> models;
    FirebaseAuth auth;
    FirebaseDatabase database;


    public LeaderAdapter(Context context, ArrayList<ResultModel> models){
        this.context = context;
        this.models = models;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.sample_leader, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ResultModel rm = models.get(position);

        holder.dateTime.setText(rm.getDateTime());
        holder.rank.setText("#" + (position+1));

        holder.points.setText("Points- " + rm.getPoints());
        holder.correct.setText("Answer- " + rm.getCorrect());

        if (rm.getPoints() == 100){
            holder.points.setTextColor(Color.rgb(0,120,0));
        }

        database.getReference().child("players")
                    .child(rm.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                holder.LL_MName.setText(snapshot.getValue(String.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            holder.LL_MName.setVisibility(View.GONE);
                        }
                    });


        if (auth.getCurrentUser().getUid().equalsIgnoreCase(rm.getId())){
            holder.LL_MName.setVisibility(View.VISIBLE);
            holder.name.setText(rm.getEmail());
            holder.LL_MName.setTextColor(context.getColor(R.color.greenDark));
        }else {
            holder.name.setText(rm.getEmail().substring(0,2) + "***@gmail.com");
            holder.LL_MName.setVisibility(View.GONE);
            holder.LL_MName.setTextColor(context.getColor(R.color.blue));
        }


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        TextView name, LL_MName, dateTime, points, correct, rank;
        ImageView imageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            LL_MName = itemView.findViewById(R.id.LL_MName);
            name = itemView.findViewById(R.id.LL_Name);
            dateTime = itemView.findViewById(R.id.LL_Date);
            points = itemView.findViewById(R.id.points);
            correct = itemView.findViewById(R.id.correct);
            rank = itemView.findViewById(R.id.rank);
            imageView = itemView.findViewById(R.id.LLImage);
        }
    }
}
