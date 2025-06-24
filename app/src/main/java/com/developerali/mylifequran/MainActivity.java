package com.developerali.mylifequran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developerali.mylifequran.Activities.MenuActivity;
import com.developerali.mylifequran.Bottom.DuaFragment;
import com.developerali.mylifequran.Bottom.HadisFragment;
import com.developerali.mylifequran.Bottom.HomeFragment;
import com.developerali.mylifequran.Bottom.PracticeFragment;
import com.developerali.mylifequran.Bottom.SurahNameFrag;
import com.developerali.mylifequran.Models.User;
import com.developerali.mylifequran.Models.updateModel;
import com.developerali.mylifequran.databinding.ActivityMainBinding;
import com.developerali.mylifequran.databinding.HomeDialogBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore database;
    FirebaseDatabase data;
    FirebaseAuth auth;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance();

        setSupportActionBar(binding.toolbar);
        //getSupportActionBar().setIcon(R.drawable.action_name);


        binding.bottomBar.setItemActiveIndex(2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_500));
        }


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new HomeFragment());
        transaction.commit();


        FirebaseMessaging.getInstance().subscribeToTopic("/topics/all")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                    }
                });



        data.getReference().child("update")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            updateModel model = snapshot.getValue(updateModel.class);

                            try {
                                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                int versionCode = packageInfo.versionCode;

                                if (model.getVersionCode() > versionCode){
                                    showCustomDialog(model.getTitle(), model.getDescription(), versionCode);
                                }

                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        transaction.replace(R.id.content, new SurahNameFrag()).addToBackStack(null);
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.content, new HadisFragment()).addToBackStack(null);
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.content, new HomeFragment()).addToBackStack(null);
                        transaction.commit();
                        break;
                    case 3:
                        transaction.replace(R.id.content, new DuaFragment()).addToBackStack(null);
                        transaction.commit();
                        break;
                    case 4:
                        transaction.replace(R.id.content, new PracticeFragment()).addToBackStack(null);
                        transaction.commit();
                        break;
                }
                return false;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.profile) {
//            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
//            startActivity(i);
//        }
        if(item.getItemId() == R.id.notifications) {
            Intent i = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(i);
        }
        if(item.getItemId() == R.id.options) {
            Intent i = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(i);
        }
//        if(item.getItemId() == R.id.click) {
//            Intent i = new Intent(MainActivity.this, TasbiActivity.class);
//            startActivity(i);
//        }
//        if(item.getItemId() == R.id.leaderB) {
//            Intent i = new Intent(MainActivity.this, LeaderBoard.class);
//            startActivity(i);
//        }
//        if(item.getItemId() == R.id.shareApp) {
//            Intent intent = new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("market://details?id=" + getPackageName()));
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            }
//        }
        return super.onOptionsItemSelected(item);
    }

    private void showCustomDialog(String title, String description, int versionCode) {

        HomeDialogBinding dialogBinding = HomeDialogBinding.inflate(getLayoutInflater());

        // Create a new dialog and set the custom layout
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);

        dialogBinding.dialogTitle.setText(title);
        dialogBinding.dialogDescription.setText(description);
        dialogBinding.dialogVersion.setText("installed_version- " + versionCode);

        dialogBinding.closeDialog.setOnClickListener(c->{
            dialog.dismiss();
        });

        dialogBinding.btnUpdate.setOnClickListener(c->{
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        // Show the dialog
        dialog.show();
    }

}