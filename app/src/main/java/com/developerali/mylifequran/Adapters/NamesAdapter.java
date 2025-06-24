package com.developerali.mylifequran.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.mylifequran.Models.NamesModel;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.databinding.ChildNameListBinding;

import java.util.ArrayList;

public class NamesAdapter extends RecyclerView.Adapter<NamesAdapter.ViewHolder>{

    Activity activity;
    ArrayList<NamesModel> namesModels;

    public NamesAdapter(Activity activity, ArrayList<NamesModel> namesModels) {
        this.activity = activity;
        this.namesModels = namesModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_name_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NamesModel model = namesModels.get(position);

        holder.binding.name.setText(model.getBengaliTranscription());
        holder.binding.nameMeaning.setText(model.getBengaliTranslation());
        holder.binding.nameArbi.setText(model.getArabicName());
        holder.binding.slNo.setText(position + 1 + "");

    }

    @Override
    public int getItemCount() {
        return namesModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ChildNameListBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChildNameListBinding.bind(itemView);
        }
    }
}
