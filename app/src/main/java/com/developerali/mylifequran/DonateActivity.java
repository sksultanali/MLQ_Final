package com.developerali.mylifequran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.developerali.mylifequran.databinding.ActivityDonateBinding;

public class DonateActivity extends AppCompatActivity {

    ActivityDonateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("upi://pay?pa=toha.abu@ybl"));
                Intent chooser = Intent.createChooser(intent, "Pay with...");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
                startActivity(chooser);
            }
        });




    }
}