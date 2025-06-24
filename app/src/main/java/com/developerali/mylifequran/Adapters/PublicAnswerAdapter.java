package com.developerali.mylifequran.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.Models.PublicPostModel;
import com.developerali.mylifequran.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PublicAnswerAdapter extends RecyclerView.Adapter<PublicAnswerAdapter.ViewHolder>{

    Context context;
    ArrayList<PublicPostModel> models;

    public PublicAnswerAdapter(Context context, ArrayList<PublicPostModel> models){
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PublicPostModel ppm = models.get(position);
        if (ppm.getImageUrl().equalsIgnoreCase("null")){
            holder.userImage.setImageResource(R.drawable.placeholder);
        }else {
            Glide.with(context).load(ppm.getImageUrl()).into(holder.userImage);
        }
        holder.Name.setText(ppm.getName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        Date date = new Date(ppm.getDateTime());
        holder.DateTime.setText(dateFormat.format(date));

        if (ppm.getContent().length() > 131){
            holder.Answer.setText(ppm.getContent().substring(0, 131) + "...Read more");
        }else {
            holder.Answer.setText(ppm.getContent());
        }

        holder.Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ppm.getContent().length() > 131){
                    holder.Answer.setText(ppm.getContent());
                }
            }
        });

        holder.Answer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", ppm.getContent());
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView Name, DateTime, Answer;
        ImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.nameUser);
            DateTime = itemView.findViewById(R.id.dateUsers);
            Answer = itemView.findViewById(R.id.nameAnswer);
            userImage = itemView.findViewById(R.id.writerImage);

        }
    }

}
