package com.developerali.mylifequran.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developerali.mylifequran.Helper;
import com.developerali.mylifequran.Models.BooksModel;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.WebViewActivity;
import com.developerali.mylifequran.databinding.ChildBooksBinding;
import com.google.rpc.Help;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class booksAdapter extends RecyclerView.Adapter<booksAdapter.ViewHolder>{

    Activity activity;
    ArrayList<BooksModel> models;

    public booksAdapter(Activity activity, ArrayList<BooksModel> models) {
        this.activity = activity;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_books, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BooksModel booksModel = models.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");
        String date = dateFormat.format(booksModel.getTime());
        holder.binding.addedOn.setText("updated on " + date);
        holder.binding.pdfCaption.setText(booksModel.getTitle());
        if (booksModel.getImage() != null && !activity.isDestroyed()){
            Glide.with(activity)
                    .load(booksModel.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(activity.getDrawable(R.drawable.books))
                    .into(holder.binding.bookImage);
        }

        holder.itemView.setOnClickListener(v->{
            if (booksModel.getLink() != null){
                if (Helper.isChromeCustomTabsSupported(activity.getApplicationContext())){
                    Helper.openChromeTab(booksModel.getLink(), activity);
                }else {
                    Intent i = new Intent(activity.getApplicationContext(), WebViewActivity.class);
                    i.putExtra("share", booksModel.getLink());
                    activity.startActivity(i);
                }
            }else {
                Helper.showAlertNoAction(activity, "Broken link",
                        "This book link is not available right now. Please send a feedback from your profile to diagnose the problem.",
                        "Okay");
            }
        });



    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ChildBooksBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChildBooksBinding.bind(itemView);
        }
    }
}
