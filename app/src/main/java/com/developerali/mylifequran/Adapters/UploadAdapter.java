package com.developerali.mylifequran.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.DetailedActivity;
import com.developerali.mylifequran.Models.UploadModel;
import com.developerali.mylifequran.R;

import java.util.ArrayList;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.Viewholder>{

    Context context;
    ArrayList<UploadModel> models;
    String where;

    public UploadAdapter(Context context, ArrayList<UploadModel> models, String where) {
        this.context = context;
        this.models = models;
        this.where = where;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.sample_upload, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        UploadModel uploadModel = models.get(position);
        holder.title.setText(uploadModel.getTitle());

        if (uploadModel.getDescription() != null){
            if (uploadModel.getDescription().length() > 131){
                holder.content.setText(uploadModel.getDescription().substring(0, 131) + "...Read more");
            }else {
                holder.content.setText(uploadModel.getDescription() + "...Read more");
            }
        }

        if (where.equalsIgnoreCase("biggan")){
            holder.title.setTextColor(Color.parseColor("#008305"));
        }

        //holder.date.setText(uploadModel.getDate());
        holder.love.setText(String.valueOf(uploadModel.getLove()));
        Glide.with(context).load(uploadModel.getImageUrl()).into(holder.Image);
        if (uploadModel.getImageUrl() == null){
            Glide.with(context).load("https://digitalfinger.id/wp-content/uploads/2019/12/no-image-available-icon-6.png")
                    .into(holder.Image);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadModel upmodel = new UploadModel(uploadModel.getTitle(), uploadModel.getDescription(),
                        uploadModel.getImageUrl(), uploadModel.getDate(), uploadModel.getKey(),
                        uploadModel.getCollection(), uploadModel.getLove());

                Intent i = new Intent(context.getApplicationContext(), DetailedActivity.class);
                i.putExtra("models", upmodel);
                context.startActivity(i);
            }
        });

        if (where.equalsIgnoreCase("surah")){
            holder.uView.setVisibility(View.GONE);
            holder.content.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (where.equalsIgnoreCase("home")){
            int limit = 11;
            return Math.min(models.size(), limit);
        }
        return models.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView title, content, date, love;
        ImageView Image;
        ConstraintLayout cardView;
        View uView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.child_title);
            content = itemView.findViewById(R.id.child_content);
            //date = itemView.findViewById(R.id.child_date);
            love = itemView.findViewById(R.id.child_love);
            Image = itemView.findViewById(R.id.child_image);
            cardView = itemView.findViewById(R.id.cardUploads);
            uView = itemView.findViewById(R.id.U_View);


        }
    }
}
