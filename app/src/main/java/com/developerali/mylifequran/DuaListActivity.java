package com.developerali.mylifequran;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developerali.mylifequran.Adapters.DuaAdapter;
import com.developerali.mylifequran.Blogger.BloggerModel;
import com.developerali.mylifequran.Blogger.Constants;
import com.developerali.mylifequran.databinding.ActivityDuaListBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DuaListActivity extends AppCompatActivity {

    ActivityDuaListBinding binding;
    FirebaseFirestore database;
    String collection;
    ProgressDialog dialog;

    String url = "";
    String nextToken = "";

    ArrayList<BloggerModel> postArrayList;
    DuaAdapter adapterPost;
    private static final String TAG = "MAIN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDuaListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dialog = new ProgressDialog(DuaListActivity.this);
        dialog.setMessage("loading data...");
        dialog.setCancelable(false);

        database = FirebaseFirestore.getInstance();
        collection = getIntent().getStringExtra("collection");

        if (Helper.englishToBengaliDay(collection).equalsIgnoreCase("NA")){
            getSupportActionBar().setTitle(collection);
        }else {
            getSupportActionBar().setTitle(Helper.englishToBengaliDay(collection));
        }

        ArrayList<BloggerModel> List = new ArrayList<>();
        List.clear();
        loadPost(collection, List);

        binding.loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPost(collection, List);
            }
        });



    }


    private void loadPost(String category, ArrayList<BloggerModel> postArrayList) {
        //progressDialog.show();
        binding.progressBar.setVisibility(View.VISIBLE);

        LinearLayoutManager lnm = new LinearLayoutManager(getApplication());
        lnm.setReverseLayout(true);
        lnm.setStackFromEnd(true);
        //dialog.show();
        binding.duaRec.setLayoutManager(lnm);


        if (nextToken.equals("")){
            Log.d(TAG, "loadPosts: Next Page token is empty, no more posts");
            url = "https://www.googleapis.com/blogger/v3/blogs/"
                    + Constants.Blog_Id
                    + "/posts?maxResults=" + Constants.MAX_POST_RESULT
                    + "&key=" + Constants.API_Key
                    + "&labels=" + category;
        }else if (nextToken.equals("end")){
            Log.d(TAG, "loadPosts: Next Page token is empty/end, no more posts");
            Toast.makeText(DuaListActivity.this, "No more posts...", Toast.LENGTH_SHORT).show();
            binding.loadMoreBtn.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            //progressDialog.dismiss();
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

                //progressDialog.dismiss();
                Log.d(TAG, "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    try {
                        nextToken = jsonObject.getString("nextPageToken");
                        Log.d(TAG, "onResponse: NextPageToken: " + nextToken);

                    }catch (Exception e){
                        //Toast.makeText(getActivity(), "Reached end of page...", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Reached end of page..." + e.getMessage());
                        binding.loadMoreBtn.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
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
                            Toast.makeText(DuaListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.emptyListMessage.setVisibility(View.VISIBLE);
                        }

                        adapterPost = new DuaAdapter(DuaListActivity.this, postArrayList);
                        //recyclerView.hideShimmerAdapter();
                        adapterPost.notifyDataSetChanged();
                        binding.duaRec.setAdapter(adapterPost);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.emptyListMessage.setVisibility(View.GONE);
                        if (postArrayList.size() > 10){
                            binding.loadMoreBtn.setVisibility(View.VISIBLE);
                        }
                    }


                }catch (Exception e){
                    Log.d(TAG, "onResponse: 2: " + e.getMessage());
                    binding.emptyListMessage.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(DuaListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                binding.progressBar.setVisibility(View.GONE);
                binding.emptyListMessage.setVisibility(View.VISIBLE);
                Toast.makeText(DuaListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(DuaListActivity.this);
        requestQueue.add(stringRequest);



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}