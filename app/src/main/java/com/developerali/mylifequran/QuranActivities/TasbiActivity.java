package com.developerali.mylifequran.QuranActivities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.developerali.mylifequran.Adapters.DuaAdapter;
import com.developerali.mylifequran.Blogger.BloggerModel;
import com.developerali.mylifequran.Blogger.Constants;
import com.developerali.mylifequran.DuaListActivity;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.databinding.ActivityTasbiBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TasbiActivity extends AppCompatActivity {

    ActivityTasbiBinding binding;
    int i = 0;
    int newCount = 0;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<BloggerModel> postArrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private static final String TAG = "MAIN_TAG";
    String url = "";
    String nextToken = "";
    int mainIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTasbiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textCount.setText(String.valueOf(i));
        mainIndex = 0;
        postArrayList.clear();


        binding.clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                binding.textCount.setText(String.valueOf(i));
                update();
            }
        });

        binding.resetBtn.setOnClickListener(v->{
            i = 0;
            newCount = 0;
            list.clear();
            adapter.notifyDataSetChanged();
            binding.textCount.setText(String.valueOf(i));
            update();
        });

        adapter = new ArrayAdapter<>(TasbiActivity.this, R.layout.child_list_item, list);
        binding.listView.setAdapter(adapter);


        binding.addBtn.setOnClickListener(v->{
            SimpleDateFormat dateFormat = new SimpleDateFormat("K:mma");
            String date = dateFormat.format(new Date().getTime());

            int counted = Integer.parseInt(binding.textCount.getText().toString());
            int now =  i - newCount;
            newCount = counted;

            list.add(now + " count  |  total- " + counted + "  |  time- " + date);
            adapter.notifyDataSetChanged();
            binding.listView.smoothScrollToPosition(list.size() - 1);
        });

        binding.goBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.list.setOnClickListener(v->{
            Intent i = new Intent(TasbiActivity.this, DuaListActivity.class);
            i.putExtra("collection", "Zikir");
            startActivity(i);
        });

        loadPost("Zikir");
    }

    void update(){
        if (i>0){
            binding.resetBtn.setVisibility(View.VISIBLE);
            binding.addBtn.setVisibility(View.VISIBLE);
        }else {
            binding.resetBtn.setVisibility(View.INVISIBLE);
            binding.addBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void loadPost(String category) {
//        if (nextToken.equals("")){
//            Log.d(TAG, "loadPosts: Next Page token is empty, no more posts");
//
//        }else if (nextToken.equals("end")){
//            Log.d(TAG, "loadPosts: Next Page token is empty/end, no more posts");
//            Toast.makeText(TasbiActivity.this, "No more posts...", Toast.LENGTH_SHORT).show();
//            return;
//        }else {
//            Log.d(TAG, "loadPosts: Next token: " + nextToken);
//            url = "https://www.googleapis.com/blogger/v3/blogs/"
//                    + Constants.Blog_Id
//                    + "/posts?maxResults=" + Constants.MAX_POST_RESULT
//                    + "&pageToken=" + nextToken
//                    + "&key=" + Constants.API_Key
//                    + "&labels=" + category;
//        }

        url = "https://www.googleapis.com/blogger/v3/blogs/"
                + Constants.Blog_Id
                + "/posts?"
                + "key=" + Constants.API_Key
                + "&labels=" + category;

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
                        postArrayList.clear();

                    }catch (Exception e){
                        Toast.makeText(TasbiActivity.this, "Reached end of page...", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(TasbiActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        //here i am getting results
                        setNext(0);

                        binding.nextTextBtn.setOnClickListener(v->{
                            mainIndex++;
                            if (postArrayList.size() > mainIndex){
                                setNext(mainIndex);
                            }else {
                                mainIndex = 0;
                                setNext(mainIndex);
                            }
                        });

                        binding.previousTextBtn.setOnClickListener(v->{
                            if (mainIndex == 0){
                                mainIndex = postArrayList.size() - 1;
                                setNext(mainIndex);
                            }else {
                                mainIndex = mainIndex - 1;
                                setNext(mainIndex);
                            }
                        });
                    }


                }catch (Exception e){
                    Log.d(TAG, "onResponse: 2: " + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(TasbiActivity.this);
        requestQueue.add(stringRequest);
    }

    void setNext(int index){
        try {
            binding.count.setText("#"+ (index + 1));
            binding.arbiText.setText(Html.fromHtml(postArrayList.get(index).getContent(), Html.FROM_HTML_MODE_COMPACT));
            binding.banglaText.setText(postArrayList.get(index).getTitle());
        }catch (Exception e){

        }
    }
}