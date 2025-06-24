package com.developerali.mylifequran.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developerali.mylifequran.Models.PublicPostModel;
import com.developerali.mylifequran.ProfileActivity;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.Replies;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PublicAdapter extends RecyclerView.Adapter<PublicAdapter.ViewHolder>{

    Context context;
    ArrayList<PublicPostModel> models;
    FirebaseFirestore database;
    FirebaseDatabase data;
    FirebaseAuth auth;

    public PublicAdapter(Context context, ArrayList<PublicPostModel> models){
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_question, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PublicPostModel ppm = models.get(position);
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance();

        if (ppm.getImageUrl().equalsIgnoreCase("null")){
            holder.userImage.setImageResource(R.drawable.placeholder);
        }else {
            Glide.with(context).load(ppm.getImageUrl()).into(holder.userImage);
        }
        holder.Name.setText(ppm.getName());


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        Date date = new Date(ppm.getDateTime());
        holder.DateTime.setText(dateFormat.format(date));


        if (ppm.getContent().length() > 131){
            holder.Question.setText(ppm.getContent().substring(0, 131) + "...Read more");
        }else {
            holder.Question.setText(ppm.getContent());
        }
        holder.Reply.setText(String.valueOf(ppm.getReply()));
        holder.Share.setText(String.valueOf(ppm.getShare()));

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.collection("public").document(ppm.getId()).update("share", FieldValue.increment(1));
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, ppm.getContent() +
                        "\n\nFrom \n*My Life Qura'n* \n\n"+

                        "Download App From PlayStore\n" +
                                "https://play.google.com/store/apps/details?id=com.developerali.mylifequran\n\n" );

                if (sharingIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(sharingIntent,"Share Post"));
                }
            }
        });

        holder.Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.Question.setText(ppm.getContent());
            }
        });

        holder.replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(), Replies.class);
                i.putExtra("id", ppm.getId());
                context.startActivity(i);
            }
        });

        if (auth.getCurrentUser() != null){
            if (ppm.getProfId() != null){
                if (ppm.getProfId().equalsIgnoreCase(auth.getCurrentUser().getUid())){
                    holder.remove.setVisibility(View.VISIBLE);
                }
            }
            database.collection("save")
                    .document(auth.getCurrentUser().getUid())
                    .collection("mySaves")
                    .document(ppm.getId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                holder.remove.setVisibility(View.VISIBLE);
                                holder.imgSave.setImageResource(R.drawable.download_done_24);
                                holder.saveText.setText("Saved");
                            }
                        }
                    });
        }

        holder.remove.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete this question");
            builder.setMessage("Are you sure wants to delete this question?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    holder.imgSave.setImageResource(R.drawable.download);
                    holder.saveText.setText("Save");

                    if (ppm.getProfId().equalsIgnoreCase(auth.getCurrentUser().getUid())){
                        database.collection("public")
                                .document(ppm.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        models.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, models.size());
                                        notifyDataSetChanged();

                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                        database.collection("save")
                                .document(auth.getCurrentUser().getUid())
                                .collection("mySaves")
                                .document(ppm.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        models.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, models.size());
                                        notifyDataSetChanged();

                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }else {
                        database.collection("save")
                                .document(auth.getCurrentUser().getUid())
                                .collection("mySaves")
                                .document(ppm.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        models.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, models.size());
                                        notifyDataSetChanged();

                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });


        holder.Question.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", ppm.getContent());
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
                return true;
            }
        });

        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (auth.getCurrentUser() == null){
                    Toast.makeText(context, "Login Required! ", Toast.LENGTH_SHORT).show();
                }else {
                    holder.imgSave.setImageResource(R.drawable.download_done_24);
                    holder.saveText.setText("Saved");

                    database.collection("save")
                            .document(auth.getCurrentUser().getUid())
                            .collection("mySaves")
                            .document(ppm.getId())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        Toast.makeText(context, "Question Already Saved!", Toast.LENGTH_LONG).show();
                                    }else {
                                        database.collection("save")
                                                .document(auth.getCurrentUser().getUid())
                                                .collection("mySaves")
                                                .document(ppm.getId()).set(ppm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(context, "Question Saved", Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView Name, DateTime, Question, Reply, Share, saveText;
        ImageView userImage, remove, imgSave;
        LinearLayout replyBtn, shareBtn, saveBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.PP_Name);
            DateTime = itemView.findViewById(R.id.PP_Date);
            Question = itemView.findViewById(R.id.PP_Post);
            Reply = itemView.findViewById(R.id.replyText);
            Share = itemView.findViewById(R.id.shareText);
            userImage = itemView.findViewById(R.id.userImage);
            replyBtn = itemView.findViewById(R.id.replyBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            saveBtn = itemView.findViewById(R.id.saveBtn);
            remove = itemView.findViewById(R.id.remove);
            imgSave = itemView.findViewById(R.id.imgSave);
            saveText = itemView.findViewById(R.id.saveText);
        }
    }

}
