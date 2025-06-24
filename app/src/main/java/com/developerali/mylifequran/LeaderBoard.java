package com.developerali.mylifequran;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.developerali.mylifequran.Adapters.LeaderAdapter;
import com.developerali.mylifequran.Models.QuestionModel;
import com.developerali.mylifequran.Models.ResultModel;
import com.developerali.mylifequran.databinding.ActivityLeaderBoardBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class LeaderBoard extends AppCompatActivity {

    ActivityLeaderBoardBinding binding;
    FirebaseFirestore database;
    FirebaseDatabase data;
    ArrayList<ResultModel> models;
    LeaderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quiz Results");

        database = FirebaseFirestore.getInstance();
        data = FirebaseDatabase.getInstance();
        models = new ArrayList<>();
        adapter = new LeaderAdapter(LeaderBoard.this, models);

        //getData();
        //readDataFromSheet();

        LinearLayoutManager lnm = new LinearLayoutManager(LeaderBoard.this);
        lnm.setOrientation(RecyclerView.VERTICAL);
//        lnm.setReverseLayout(true);
//        lnm.setStackFromEnd(true);

        binding.leaderRec.setLayoutManager(lnm);
        binding.leaderRec.setAdapter(adapter);
        binding.leaderRec.showShimmerAdapter();


        data.getReference().child("ramadanWinners")
                .orderByChild("points")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            models.clear();
                            //int rank = 1;
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                ResultModel rm = new ResultModel();
                                rm.setId(snapshot1.getKey());
                                rm.setEmail(snapshot1.child("email").getValue(String.class));
                                rm.setCorrect(snapshot1.child("correct").getValue(Integer.class));
                                rm.setPoints(snapshot1.child("points").getValue(Integer.class));
                                rm.setDateTime(snapshot1.child("dateTime").getValue(String.class));
                                //rm.setRank(rank);

                                models.add(rm);
                                //rank++;
                            }
                            Collections.reverse(models);
                            adapter.notifyDataSetChanged();
                            binding.leaderRec.hideShimmerAdapter();
                        }else {

                            binding.leaderRec.hideShimmerAdapter();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    void readDataFromSheet(){


        String url = "https://script.google.com/macros/s/AKfycbwv5zsP2ST497EbkCcD6xN3jOukE05M7WqH6a6rXOEkxCjinx6crEElTwZfMunoPhYM/exec?";
        url = url + "action=read";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        String id = object.getString("id");
                        String name = object.getString("name");
                        String img = object.getString("image");
                        int right = object.getInt("right");
                        int coin = object.getInt("coins");
                        String dateTime = object.getString("dateTime");

                        models.add(new ResultModel(name, right, coin, dateTime));
                    }

                    adapter.notifyDataSetChanged();
                    binding.leaderRec.hideShimmerAdapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LeaderBoard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LeaderBoard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue queue = Volley.newRequestQueue(LeaderBoard.this);
        queue.add(jsonObjectRequest);
    }
    void getData(){
        ArrayList<ResultModel> models = new ArrayList<>();
        LeaderAdapter adapter = new LeaderAdapter(LeaderBoard.this, models);

        LinearLayoutManager lnm = new LinearLayoutManager(LeaderBoard.this);
        lnm.setOrientation(RecyclerView.VERTICAL);
        lnm.setReverseLayout(true);
        lnm.setStackFromEnd(true);

        binding.leaderRec.setLayoutManager(lnm);
        binding.leaderRec.setAdapter(adapter);
        binding.leaderRec.showShimmerAdapter();

        Query query = database.collection("results")
                .orderBy("date", Query.Direction.ASCENDING)
                .orderBy("points", Query.Direction.ASCENDING);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                models.clear();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                    ResultModel model = snapshot.toObject(ResultModel.class);
//                    model.setUserId(snapshot.getString("userId"));
//                    model.setName(snapshot.getString("name"));
//                    model.setImageUrl(snapshot.getString("imageUrl"));
//                    model.setDateTime(snapshot.getTimestamp("dateTime"));
//                    model.setCorrect(snapshot.getLong("correct"));
//                    model.setPoints(snapshot.getLong("points"));
//                    models.add(model);
                }
                adapter.notifyDataSetChanged();
                binding.leaderRec.hideShimmerAdapter();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LeaderBoard.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                binding.leaderRec.hideShimmerAdapter();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}