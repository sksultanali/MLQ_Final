package com.developerali.mylifequran;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.developerali.mylifequran.Adapters.NoticeAdapter;
import com.developerali.mylifequran.Models.NotificationModel;
import com.developerali.mylifequran.databinding.ActivityNotificationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    ActivityNotificationBinding binding;
    FirebaseFirestore database;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");

        dialog = new ProgressDialog(NotificationActivity.this);
        dialog.setMessage("loading notifications...");
        dialog.setCancelable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_500));
        }

        database = FirebaseFirestore.getInstance();
        getData();



    }

    void getData(){
        dialog.show();
        ArrayList<NotificationModel> models = new ArrayList<>();
        NoticeAdapter adapter = new NoticeAdapter(NotificationActivity.this, models);

        LinearLayoutManager lnm = new LinearLayoutManager(NotificationActivity.this);
        lnm.setOrientation(RecyclerView.VERTICAL);
        lnm.setReverseLayout(true);
        lnm.setStackFromEnd(true);

        binding.noticeRec.setLayoutManager(lnm);
        binding.noticeRec.setAdapter(adapter);

        database.collection("notifications")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        models.clear();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            NotificationModel notificationModel = snapshot.toObject(NotificationModel.class);
                            models.add(notificationModel);
                        }
                        adapter.notifyDataSetChanged();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 500);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NotificationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}