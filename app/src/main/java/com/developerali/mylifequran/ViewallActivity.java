package com.developerali.mylifequran;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.developerali.mylifequran.Adapters.BigganAdapter;
import com.developerali.mylifequran.Blogger.BloggerAdapter;
import com.developerali.mylifequran.Blogger.BloggerModel;
import com.developerali.mylifequran.Blogger.Constants;
import com.developerali.mylifequran.Models.SearchModel;
import com.developerali.mylifequran.Models.UploadModel;
import com.developerali.mylifequran.databinding.ActivityViewallBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewallActivity extends AppCompatActivity {

    ActivityViewallBinding binding;
    FirebaseFirestore database;
    String title, collection;
    ProgressDialog dialog;
    FirebaseDatabase data;

    private String url = "";
    private String nextToken = "";

    private ArrayList<BloggerModel> postArrayList;
    private BigganAdapter adapterPost;
    private static final String TAG = "MAIN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewallBinding.inflate(getLayoutInflater());
        database = FirebaseFirestore.getInstance();
        data = FirebaseDatabase.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_500));
        }

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = getIntent().getStringExtra("title");
        collection = getIntent().getStringExtra("collection");

        postArrayList = new ArrayList<>();
        postArrayList.clear();
        loadPost(collection);

        if (title != null){
            getSupportActionBar().setTitle(title);
        }


//        binding.goBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.RecView.setVisibility(View.GONE);
//                binding.SearchRec.setVisibility(View.VISIBLE);
//                binding.searchingLayout.setVisibility(View.GONE);
//                searchPost(binding.searchView.getText().toString());
//
//            }
//        });

        binding.loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPost(collection);
            }
        });

    }

    private void loadPost(String category) {
        //progressDialog.show();

        LinearLayoutManager lnm = new LinearLayoutManager(ViewallActivity.this);
        lnm.setOrientation(RecyclerView.VERTICAL);
        lnm.setReverseLayout(true);
        lnm.setStackFromEnd(true);

        binding.RecView.setLayoutManager(lnm);
        binding.RecView.showShimmerAdapter();


        if (nextToken.equals("")){
            Log.d(TAG, "loadPosts: Next Page token is empty, no more posts");
            url = "https://www.googleapis.com/blogger/v3/blogs/"
                    + Constants.Blog_Id
                    + "/posts?maxResults=" + Constants.MAX_POST_RESULT
                    + "&key=" + Constants.API_Key
                    + "&labels=" + category;
        }else if (nextToken.equals("end")){
            Log.d(TAG, "loadPosts: Next Page token is empty/end, no more posts");
            Toast.makeText(ViewallActivity.this, "No more posts...", Toast.LENGTH_SHORT).show();
            binding.RecView.hideShimmerAdapter();
            return;
        }else {
            Log.d(TAG, "loadPosts: Next token: " + nextToken);
            url = "https://www.googleapis.com/blogger/v3/blogs/"
                    + Constants.Blog_Id
                    + "/posts?maxResults=" + Constants.MAX_POST_RESULT
                    + "&pageToken=" + nextToken
                    + "&key=" + Constants.API_Key
                    + "&labels=" + category;
        }
        Log.d(TAG, "loadPost: URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d(TAG, "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    try {
                        nextToken = jsonObject.getString("nextPageToken");
                        Log.d(TAG, "onResponse: NextPageToken: " + nextToken);

                    }catch (Exception e){
                        Toast.makeText(ViewallActivity.this, "Reached end of page...", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Reached end of page..." + e.getMessage());
                        binding.RecView.hideShimmerAdapter();
                        nextToken = "end";
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++){
                        try {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String id = jsonObject1.getString("id");
                            String title = jsonObject1.getString("title");
                            String content = jsonObject1.getString("content");
                            String published = jsonObject1.getString("published");
                            String updated = jsonObject1.getString("updated");
                            String url = jsonObject1.getString("url");
                            String selfLink = jsonObject1.getString("selfLink");
                            String authorName = jsonObject1.getJSONObject("author").getString("displayName");

                            BloggerModel bloggerModel = new BloggerModel(authorName, content, id, published,
                                    selfLink, title, updated, url);
                            postArrayList.add(bloggerModel);

                        }catch (Exception e){
                            Log.d(TAG, "onResponse: 1: " + e.getMessage());
                            Toast.makeText(ViewallActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.RecView.hideShimmerAdapter();
                        }

                        adapterPost = new BigganAdapter(ViewallActivity.this, postArrayList, "where");
                        binding.RecView.hideShimmerAdapter();
                        adapterPost.notifyDataSetChanged();
                        binding.RecView.setAdapter(adapterPost);

                    }


                }catch (Exception e){
                    Log.d(TAG, "onResponse: 2: " + e.getMessage());
                    Toast.makeText(ViewallActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.RecView.hideShimmerAdapter();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Toast.makeText(ViewallActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                binding.RecView.hideShimmerAdapter();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ViewallActivity.this);
        requestQueue.add(stringRequest);


        binding.goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPost(binding.searchView.getText().toString());
            }
        });


    }


    private void searchPost(String category) {
        //progressDialog.show();
        postArrayList.clear();

        LinearLayoutManager lnm = new LinearLayoutManager(ViewallActivity.this);
        lnm.setOrientation(RecyclerView.VERTICAL);
        lnm.setReverseLayout(true);
        lnm.setStackFromEnd(true);

        binding.RecView.setLayoutManager(lnm);
        binding.RecView.showShimmerAdapter();

        url = "https://www.googleapis.com/blogger/v3/blogs/"
                + Constants.Blog_Id
                +"/posts/search?q=" + category
                + "&key=" + Constants.API_Key;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d(TAG, "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    try {
                        nextToken = jsonObject.getString("nextPageToken");
                        Log.d(TAG, "onResponse: NextPageToken: " + nextToken);

                    }catch (Exception e){
                        Toast.makeText(ViewallActivity.this, "Reached end of page...", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Reached end of page..." + e.getMessage());
                        nextToken = "end";
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++){
                        try {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String id = jsonObject1.getString("id");
                            String title = jsonObject1.getString("title");
                            String content = jsonObject1.getString("content");
                            String published = jsonObject1.getString("published");
                            String updated = jsonObject1.getString("updated");
                            String url = jsonObject1.getString("url");
                            String selfLink = jsonObject1.getString("selfLink");
                            String authorName = jsonObject1.getJSONObject("author").getString("displayName");

                            BloggerModel bloggerModel = new BloggerModel(authorName, content, id, published,
                                    selfLink, title, updated, url);
                            postArrayList.add(bloggerModel);

                        }catch (Exception e){
                            Log.d(TAG, "onResponse: 1: " + e.getMessage());
                            Toast.makeText(ViewallActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        adapterPost = new BigganAdapter(ViewallActivity.this, postArrayList, "where");
                        binding.RecView.hideShimmerAdapter();
                        adapterPost.notifyDataSetChanged();
                        binding.RecView.setAdapter(adapterPost);
                    }


                }catch (Exception e){
                    Log.d(TAG, "onResponse: 2: " + e.getMessage());
                    Toast.makeText(ViewallActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Toast.makeText(ViewallActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ViewallActivity.this);
        requestQueue.add(stringRequest);



    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}