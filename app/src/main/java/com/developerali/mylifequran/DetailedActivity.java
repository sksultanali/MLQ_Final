package com.developerali.mylifequran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.Blogger.BloggerModel;
import com.developerali.mylifequran.Models.UploadModel;
import com.developerali.mylifequran.databinding.ActivityDetailedBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Date;

public class DetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;
    FirebaseFirestore database;
    BloggerModel uploadModel;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(DetailedActivity.this);
        dialog.setMessage("getting ready...");
        dialog.setCancelable(false);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseFirestore.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_500));
        }

        uploadModel = getIntent().getParcelableExtra("models");


        binding.DATitle.setText(uploadModel.getTitle());
        Glide.with(DetailedActivity.this).load(uploadModel.getUrl()).into(binding.DImage);
        if (uploadModel.getUrl() == null){
            Glide.with(DetailedActivity.this).load(getResources().getDrawable(R.drawable.duapng))
                    .into(binding.DImage);
        }

        binding.DDate.setText(uploadModel.getPublished());
//        binding.DLov.setText(String.valueOf(uploadModel.getLove()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.DContent.setText(Html.fromHtml(uploadModel.getContent(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.DContent.setText(Html.fromHtml(uploadModel.getContent()));
        }


        binding.DAuthorName.setText(uploadModel.getAuthorName());


        binding.DShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String post = readyShare(uploadModel);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, post);
                if (sharingIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(sharingIntent,"Share Post"));
                }
            }
        });



    }

    public String readyShare(BloggerModel uploadModel){
        Date date = new Date();
        Document document = Jsoup.parse(uploadModel.getContent());

        String post =
                "       Presented By\n" +
                "       *My Life Qur'an*\n" +
                        "----------------------------\n" +

                        "\n" +
                        "*Download App From PlayStore*" +
                        "https://play.google.com/store/apps/details?id=com.developerali.mylifequran\n"+
                        "Read Complete on App only\n" +
                        "----------------------------\n" +
                        "\n" +

                        "*" +uploadModel.getTitle()+"*" + "\n" + "\n" +
                        document.text() + "\n";

        dialog.dismiss();
        return post;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}