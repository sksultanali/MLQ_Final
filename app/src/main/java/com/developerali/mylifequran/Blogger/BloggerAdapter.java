package com.developerali.mylifequran.Blogger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.DetailedActivity;
import com.developerali.mylifequran.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BloggerAdapter extends RecyclerView.Adapter<BloggerAdapter.Viewholder>{

    Context context;
    ArrayList<BloggerModel> models;
    String where, image, formattedDate;

    public BloggerAdapter(Context context, ArrayList<BloggerModel> models, String where) {
        this.context = context;
        this.models = models;
        this.where = where;
    }

    @NonNull
    @Override
    public BloggerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.sample_upload, parent, false));
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
            Glide.with(context).load(context.getDrawable(R.drawable.duapng))
                    .into(holder.Image);
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
        //holder.content.setText(document.text());
        //holder.date.setText(formattedDate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.content.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.content.setText(Html.fromHtml(content));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Elements elements = document.select("img");
                    image = elements.get(0).attr("src");
                }catch (Exception e){

                }
                BloggerModel upmodel = new BloggerModel(authorName, bloggerModel.getContent(),
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
            holder.uView.setVisibility(View.GONE);
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
        ConstraintLayout cardView;
        View uView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.child_title);
            content = itemView.findViewById(R.id.child_content);
            authorName = itemView.findViewById(R.id.c_author_Name);
            //date = itemView.findViewById(R.id.child_date);
            love = itemView.findViewById(R.id.child_love);
            Image = itemView.findViewById(R.id.child_image);
            cardView = itemView.findViewById(R.id.cardUploads);
            uView = itemView.findViewById(R.id.U_View);


        }
    }

}
