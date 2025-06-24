package com.developerali.mylifequran;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.developerali.mylifequran.Models.User;
import com.developerali.mylifequran.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore database;
    FirebaseStorage storage;
    ProgressDialog dialog;
    FirebaseDatabase data;
    String uid, name, email, phone, password, key;
    Uri selectedImage;
    Long wallet, reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        }


        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        data = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("creating new account...");


        binding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        //step 3
        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT); //sob content samne esbe!
                i.setType("image/*"); //datatype bola holo
                startActivityForResult(i, 45);

            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = binding.Name.getText().toString();
                email = binding.Email.getText().toString();
                phone = binding.Phone.getText().toString();
                password = binding.Password.getText().toString();
                key = data.getReference().push().getKey();

                if (name.isEmpty()) {
                    binding.Name.setError("Enter Name");
                    return;
                } else if (email.isEmpty()) {
                    binding.Email.setError("Enter Email");
                    return;
                } else if (phone.isEmpty()) {
                    binding.Phone.setError("Enter Phone");
                    return;
                } else if (password.isEmpty()) {
                    binding.Password.setError("Enter Password");
                    return;
                }else {

                    wallet = Long.valueOf(0);
                    reply = Long.valueOf(0);

                    if (selectedImage != null){
                        dialog.show();
                        UploadData();

                    }else {

                        AlertDialog.Builder obj = new AlertDialog.Builder(SignUpActivity.this);
                        obj.setTitle("profile image not found!");
                        obj.setMessage("continue without profile picture?");
                        obj.setCancelable(false);

                        obj.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            uid = task.getResult().getUser().getUid();
                                            String imageUrl = "null";

                                            User user = new User(uid, name, email, phone, password, imageUrl,
                                                    wallet, reply);   //constructor sequence rakhte hobe variable k!

                                            database
                                                    .collection("users")
                                                    .document(uid)
                                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()) {
                                                                dialog.dismiss();
                                                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                                                dialog.dismiss();
                                                                //Notification();
                                                                finish();
                                                            } else {
                                                                dialog.dismiss();
                                                                Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                obj.setCancelable(true);
                            }
                        });
                        obj.setNegativeButton("Upload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent j = new Intent();
                                j.setAction(Intent.ACTION_GET_CONTENT); //sob content samne esbe!
                                j.setType("image/*"); //datatype bola holo
                                startActivityForResult(j, 108);
                                dialogInterface.cancel();

                                obj.setCancelable(true);
                            }
                        });
                        obj.show();

                    }
                }
            }
        });


        binding.Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.passwordCount.setText(s.toString().length() + "/8");

                if (s.toString().length() > 8){
                    binding.passwordCount.setTextColor(Color.parseColor("#F44336"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    // step 4   //user profile
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            if (data.getData() != null){
                selectedImage = data.getData();
                binding.imageProfile.setImageURI(data.getData());
            }
        }

        if (data != null && requestCode == 108){
            if (data.getData() != null){
                selectedImage = data.getData();
                binding.imageProfile.setImageURI(data.getData());
                dialog.show();
                UploadData();
            }
        }

    }

    void UploadData(){

        StorageReference reference = storage.getReference().child("profiles").child(key);     // storage e folder banalam
        reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {    //ekhane uri hochche user er profile link!

                            // ekhane theke sob data load hochche database e -->

                            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {

                                        uid = task.getResult().getUser().getUid();

                                        String imageUrl = uri.toString();   // image direct store hoina tai url banate holo!

                                        User user = new User(uid, name, email, phone, password, imageUrl, wallet, reply);

                                        database
                                                .collection("users")
                                                .document(uid)
                                                .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()) {
                                                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                                            dialog.dismiss();
                                                            finish();
                                                        } else {
                                                            dialog.dismiss();
                                                            Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}