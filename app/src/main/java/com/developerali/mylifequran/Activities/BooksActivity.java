package com.developerali.mylifequran.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.developerali.mylifequran.Adapters.booksAdapter;
import com.developerali.mylifequran.Models.BooksModel;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.databinding.ActivityBooksBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity {

    ActivityBooksBinding binding;
    ArrayList<BooksModel> arrayList;
    ArrayList<String> categories;
    FirebaseDatabase database;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        arrayList = new ArrayList<>();
        categories = new ArrayList<>();

        database.getReference().child("books")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            categories.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                categories.add(snapshot1.child("add").getValue(String.class));
                            }

                            ArrayAdapter<String> obj2 = new ArrayAdapter<String>(BooksActivity.this, R.layout.layout_spinner_item, categories);
                            binding.spinnerCat.setAdapter(obj2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = categories.get(position);
                getBooksData(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.goBack.setOnClickListener(v->{
            onBackPressed();
        });









    }

    private void getBooksData(String category) {
        binding.progressBar.setVisibility(View.VISIBLE);
        database.getReference().child("books")
                .child(category)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            arrayList.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                if (!snapshot1.getKey().equalsIgnoreCase("add")){
                                    BooksModel booksModel = snapshot1.getValue(BooksModel.class);
                                    booksModel.setImage(snapshot1.child("image").getValue(String.class));
                                    booksModel.setTitle(snapshot1.child("title").getValue(String.class));
                                    booksModel.setLink(snapshot1.child("link").getValue(String.class));
                                    booksModel.setTime(snapshot1.child("time").getValue(Long.class));
                                    arrayList.add(booksModel);
                                }
                            }

                            if (arrayList.isEmpty()){
                                binding.emptyListMessage.setVisibility(View.VISIBLE);
                            }else {
                                binding.emptyListMessage.setVisibility(View.GONE);
                            }

                            binding.progressBar.setVisibility(View.GONE);
                            GridLayoutManager gnm = new GridLayoutManager(BooksActivity.this, 2);
                            binding.booksRec.setLayoutManager(gnm);
                            booksAdapter booksAdapter = new booksAdapter(BooksActivity.this, arrayList);
                            binding.booksRec.setAdapter(booksAdapter);
                        }else {
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BooksActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}