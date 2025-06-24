package com.developerali.mylifequran.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.Blogger.BloggerModel;
import com.developerali.mylifequran.DetailedActivity;
import com.developerali.mylifequran.Models.UploadModel;
import com.developerali.mylifequran.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class DuaAdapter extends RecyclerView.Adapter<DuaAdapter.Viewholder>{

    Context context;
    ArrayList<BloggerModel> models;
    UploadModel upmodel;
    String where, image, formattedDate;

    public DuaAdapter(Context context, ArrayList<BloggerModel> models) {
        this.context = context;
        this.models = models;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.sample_dua_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DuaAdapter.Viewholder holder, int position) {
        BloggerModel bloggerModel = models.get(position);

        String authorName = bloggerModel.getAuthorName();
        String content = bloggerModel.getContent();
        String id = bloggerModel.getId();
        String title = bloggerModel.getTitle();
        String url = bloggerModel.getUrl();
        String selfLink = bloggerModel.getSelfLink();
        String updated = bloggerModel.getUpdated();
        String published = bloggerModel.getPublished();

        Document document = Jsoup.parse(content);

        try {
            Elements elements = document.select("img");
            image = elements.get(0).attr("src");
            Glide.with(context).load(image).into(holder.image);
        }catch (Exception e){
            Glide.with(context).load(context.getDrawable(R.drawable.duapng))
                    .into(holder.image);
        }

        String gmtDate = published;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("K:mm a  dd/MM/yyyy");
        formattedDate = "";
        try{
            Date date = dateFormat.parse(gmtDate);
            formattedDate = dateFormat2.format(date);

        }catch(Exception e){
            formattedDate = published;
            e.printStackTrace();
        }

        holder.title.setText(title);
        //holder.date.setText(formattedDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Elements elements = document.select("img");
                    image = elements.get(0).attr("src");
                }catch (Exception e){

                }
                BloggerModel upmodel = new BloggerModel(authorName, content,
                        id, formattedDate, selfLink, title, updated, image);

                Intent i = new Intent(context.getApplicationContext(), DetailedActivity.class);
                i.putExtra("models", upmodel);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView title;
        CircleImageView image;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.duaTitle);
            image = itemView.findViewById(R.id.duaImage);

        }
    }

}
