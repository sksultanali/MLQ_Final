package com.developerali.mylifequran.QuranAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.mylifequran.QuranActivities.QuranDetailed;
import com.developerali.mylifequran.QuranModel.SurahNamesModel;
import com.developerali.mylifequran.R;

import java.util.ArrayList;

public class SuraNameAdapter extends RecyclerView.Adapter<SuraNameAdapter.ViewHolder>{

    Context context;
    String temp_name;
    ArrayList<SurahNamesModel> models;

    public SuraNameAdapter (Context context, ArrayList<SurahNamesModel> models){
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_surah_name, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SurahNamesModel snm = models.get(position);
        holder.Name.setText(snm.getEnglishName());
        holder.No.setText(snm.getNumber());
        holder.ArbiName.setText(snm.getName());
        if (snm.getRevelationType().equalsIgnoreCase("Meccan")){
            temp_name = "মাক্কী";
        }else {
            temp_name = "মাদানী";
        }
        holder.ANumber.setText("আয়াত- " + snm.getNumberOfAyahs() + "টি (" + temp_name + ")");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(), QuranDetailed.class);
                i.putExtra("num", snm.getNumber());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView Name, ANumber, ArbiName, No;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.suraName);
            ANumber = itemView.findViewById(R.id.suraAat);
            ArbiName = itemView.findViewById(R.id.nameArbi);
            No = itemView.findViewById(R.id.suraNo);
        }
    }

}
