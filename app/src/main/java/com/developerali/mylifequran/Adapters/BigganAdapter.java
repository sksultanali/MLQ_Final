package com.developerali.mylifequran.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class BigganAdapter extends RecyclerView.Adapter<BigganAdapter.Viewholder>{

    Context context;
    ArrayList<BloggerModel> models;
    String where, image, formattedDate;

    public BigganAdapter(Context context, ArrayList<BloggerModel> models, String where) {
        this.context = context;
        this.models = models;
        this.where = where;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_biggan_upload, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
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
        holder.authorName.setText(authorName);

        try {
            Elements elements = document.select("img");
            image = elements.get(0).attr("src");
            Glide.with(context).load(image).into(holder.Image);
        }catch (Exception e){
            Glide.with(context).load("https://digitalfinger.id/wp-content/uploads/2019/12/no-image-available-icon-6.png")
                    .into(holder.Image);
        }

        String gmtDate = published;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy K:mm a");
        formattedDate = "";
        try{
            Date date = dateFormat.parse(gmtDate);
            formattedDate = dateFormat2.format(date);

        }catch(Exception e){
            formattedDate = published;
            e.printStackTrace();
        }

        holder.title.setText(title);
        holder.content.setText(document.text());
        holder.date.setText(formattedDate);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BloggerModel upmodel = new BloggerModel(authorName, document.text(),
                        id, formattedDate, selfLink, title, updated, image);

                Intent i = new Intent(context.getApplicationContext(), DetailedActivity.class);
                i.putExtra("models", upmodel);
                context.startActivity(i);
            }
        });





        if (document.text() != null){
            if (document.text().length() > 131){
                holder.content.setText(document.text().substring(0, 131) + "...Read more");
            }else {
                holder.content.setText(document.text() + "...Read more");
            }
        }

        if (where.equalsIgnoreCase("biggan")){
            holder.title.setTextColor(Color.parseColor("#008305"));
        }

        if (where.equalsIgnoreCase("surah")){
            holder.UView.setVisibility(View.GONE);
            holder.content.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView title, content, date, love, authorName;
        ImageView Image;
        LinearLayout linearLayout;
        View UView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bigganTitle);
            authorName = itemView.findViewById(R.id.B_author_Name);
            content = itemView.findViewById(R.id.biggan_Content);
            date = itemView.findViewById(R.id.biggan_date);
            love = itemView.findViewById(R.id.biggan_love);
            Image = itemView.findViewById(R.id.biggan_image);
            linearLayout = itemView.findViewById(R.id.LinLayFetch);
            UView = itemView.findViewById(R.id.bigganView);

        }
    }
}
