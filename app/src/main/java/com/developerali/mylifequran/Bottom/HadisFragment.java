package com.developerali.mylifequran.Bottom;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.developerali.mylifequran.Activities.DetailCalenderActivity;
import com.developerali.mylifequran.Adapters.UploadAdapter;
import com.developerali.mylifequran.Blogger.BloggerAdapter;
import com.developerali.mylifequran.Blogger.BloggerModel;
import com.developerali.mylifequran.Blogger.Constants;
import com.developerali.mylifequran.JoinCommunity;
import com.developerali.mylifequran.Models.ExtraModel;
import com.developerali.mylifequran.Models.UploadModel;
import com.developerali.mylifequran.Models.viewFlipperModel;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.ViewallActivity;
import com.developerali.mylifequran.databinding.FragmentHadisBinding;
import com.google.firebase.auth.FirebaseAuth;
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

import me.ibrahimsn.lib.SmoothBottomBar;

public class HadisFragment extends Fragment {

    FragmentHadisBinding binding;
    FirebaseFirestore database;
    FirebaseDatabase data;
    FirebaseAuth auth;

    private String url = "";
    private String nextToken = "";
    private BloggerAdapter adapterPost;
    private ProgressDialog progressDialog;
    private static final String TAG = "MAIN_TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHadisBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        data = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SmoothBottomBar bottomBar = getActivity().findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(1);

        Animation anim_in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        Animation anim_out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);

        data.getReference().child("viewflipers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewFlipperModel vm = snapshot.getValue(viewFlipperModel.class);
                binding.textA.setText(vm.getHadisText1());
                binding.textB.setText(vm.getHadisText2());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.viewFlipper.setInAnimation(anim_in);
        binding.viewFlipper.setOutAnimation(anim_out);
        binding.viewFlipper.setFlipInterval(5000);
        binding.viewFlipper.startFlipping();


        //ImageSlider
        final ArrayList<SlideModel> slideModels = new ArrayList<>();

        data.getReference().child("hadis")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            slideModels.clear();

                            for (DataSnapshot ds:snapshot.getChildren()){

                                slideModels.add(new SlideModel(ds.child("imageUrl").getValue(String.class), ScaleTypes.CENTER_CROP));
                                binding.imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        if (slideModels.isEmpty() || !isConnectedNetwork(getActivity())){
            slideModels.add(new SlideModel(R.drawable.bannera, ScaleTypes.CENTER_CROP));
            slideModels.add(new SlideModel(R.drawable.bannerb, ScaleTypes.CENTER_CROP));
            binding.imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        }

        binding.joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), JoinCommunity.class);
                startActivity(i);
            }
        });
        binding.goCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DetailCalenderActivity.class);
                startActivity(i);
            }
        });



        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait...");

        ArrayList<BloggerModel> hadisList = new ArrayList<>();
        ArrayList<BloggerModel> bigganList = new ArrayList<>();
        ArrayList<BloggerModel> surahList = new ArrayList<>();
        hadisList.clear();
        bigganList.clear();
        surahList.clear();

        loadPost("Hadis", binding.hadisRec, hadisList);
        loadPost("Surah", binding.suraRec, surahList);
        loadPost("Biggan", binding.bigganRec, bigganList);
        pasingIntent();

    }

    private void loadPost(String category, ShimmerRecyclerView recyclerView,
                          ArrayList<BloggerModel> postArrayList) {
        //progressDialog.show();

        LinearLayoutManager lnm = new LinearLayoutManager(getActivity());
        lnm.setOrientation(RecyclerView.HORIZONTAL);
        lnm.setReverseLayout(true);
        lnm.setStackFromEnd(true);

        recyclerView.setLayoutManager(lnm);
        recyclerView.showShimmerAdapter();


        if (nextToken.equals("")){
            Log.d(TAG, "loadPosts: Next Page token is empty, no more posts");
            url = "https://www.googleapis.com/blogger/v3/blogs/"
                    + Constants.Blog_Id
                    + "/posts?maxResults=" + Constants.MAX_POST_RESULT
                    + "&key=" + Constants.API_Key
                    + "&labels=" + category;
        }else if (nextToken.equals("end")){
            Log.d(TAG, "loadPosts: Next Page token is empty/end, no more posts");
            //Toast.makeText(getActivity(), "No more posts...", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
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
                
                progressDialog.dismiss();
                Log.d(TAG, "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    try {
                        nextToken = jsonObject.getString("nextPageToken");
                        Log.d(TAG, "onResponse: NextPageToken: " + nextToken);

                    }catch (Exception e){
                        //Toast.makeText(getActivity(), "Reached end of page...", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        adapterPost = new BloggerAdapter(getActivity(), postArrayList, category);
                        recyclerView.hideShimmerAdapter();
                        adapterPost.notifyDataSetChanged();
                        recyclerView.setAdapter(adapterPost);
                    }


                }catch (Exception e){
                    Log.d(TAG, "onResponse: 2: " + e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);



    }

    public static boolean isConnectedNetwork (Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    void pasingIntent(){
        binding.hadisViewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), ViewallActivity.class);
                i.putExtra("collection", "Hadis");
                i.putExtra("title", "হাদিস- কোরান");
                startActivity(i);
            }
        });

        binding.bigganViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), ViewallActivity.class);
                i.putExtra("collection", "Biggan");
                i.putExtra("title", "বিজ্ঞানের আলকে ইসলাম");
                startActivity(i);
            }
        });
        binding.surahViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), ViewallActivity.class);
                i.putExtra("collection", "Surah");
                i.putExtra("title", "প্রয়োজনীয় সুরাহসমূহ");
                startActivity(i);
            }
        });
    }


}