package com.developerali.mylifequran.Models;

import static android.content.Context.MODE_MULTI_PROCESS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.LoginActivity;
import com.developerali.mylifequran.PracticeQuiz.Practice_Quiz;
import com.developerali.mylifequran.QuizActivity;
import com.developerali.mylifequran.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<CategoryModel> categoryModels;
    FirebaseAuth auth;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_practice_set,null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final CategoryModel model = categoryModels.get(position);
        auth = FirebaseAuth.getInstance();

        holder.textView.setText(model.getCategoryName());
        //holder.questionSize.setText(String.valueOf(categoryModels.size()) + " Questions");
        Glide.with(context)
                .load(model.getCategoryImage())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null){
//                    SharedPreferences pref = context.getSharedPreferences("path", MODE_MULTI_PROCESS);
//                    SharedPreferences.Editor obj = pref.edit();
//                    obj.putString("path", String.valueOf(model.getCategoryId()));
                    Intent intent = new Intent(context.getApplicationContext(), Practice_Quiz.class);
//                    obj.commit();
                    intent.putExtra("path", String.valueOf(model.getCategoryId()));
                    context.startActivity(intent);
                }else {
                    Snackbar snackbar = Snackbar.make(holder.itemView, "LogIn to play quiz...", Snackbar.LENGTH_LONG)
                            .setAction("LogIn", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(context.getApplicationContext(), LoginActivity.class);
                                    context.startActivity(i);
                                }
                            });
                    snackbar.show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, questionSize;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.category);
            questionSize = itemView.findViewById(R.id.questionSize);
        }
    }
}
