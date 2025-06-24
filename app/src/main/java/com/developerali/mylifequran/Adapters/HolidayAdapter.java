package com.developerali.mylifequran.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.mylifequran.R;
import com.developerali.mylifequran.databinding.ChildHolidayBinding;

import java.util.List;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.ViewHolder>{

    List<String> model;
    Activity activity;

    public HolidayAdapter(List<String> model, Activity activity) {
        this.model = model;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_holiday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.holiday.setText(model.get(position));
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ChildHolidayBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChildHolidayBinding.bind(itemView);
        }
    }
}
