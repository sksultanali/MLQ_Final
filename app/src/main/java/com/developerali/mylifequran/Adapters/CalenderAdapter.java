package com.developerali.mylifequran.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.mylifequran.CalenderModels.DateData;
import com.developerali.mylifequran.CalenderModels.Gregorian;
import com.developerali.mylifequran.CalenderModels.Hijri;
import com.developerali.mylifequran.Helper;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.databinding.ChildCalenderCellBinding;
import com.developerali.mylifequran.databinding.ChildHolidayBinding;
import com.developerali.mylifequran.databinding.DialogBottomCalenderBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.ViewHolder>{

    List<DateData> dateModel;
    Activity activity;
    String startDay;

    public CalenderAdapter(List<DateData> dateModel, Activity activity, String startDay) {
        this.dateModel = dateModel;
        this.activity = activity;
        this.startDay = startDay;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_calender_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (startDay.equalsIgnoreCase("Saturday")){
            if (position > 5){
                Hijri arbiDate = dateModel.get(position - 6).getHijri();
                Gregorian englishDate = dateModel.get(position - 6).getGregorian();
                setTxt(arbiDate, englishDate, holder);
            }
        } else if (startDay.equalsIgnoreCase("Friday")) {
            if (position > 4){
                Hijri arbiDate = dateModel.get(position - 5).getHijri();
                Gregorian englishDate = dateModel.get(position - 5).getGregorian();
                setTxt(arbiDate, englishDate, holder);
            }
        }else if (startDay.equalsIgnoreCase("Thursday")) {
            if (position > 3){
                Hijri arbiDate = dateModel.get(position - 4).getHijri();
                Gregorian englishDate = dateModel.get(position - 4).getGregorian();
                setTxt(arbiDate, englishDate, holder);
            }
        }else if (startDay.equalsIgnoreCase("Wednesday")) {
            if (position > 2){
                Hijri arbiDate = dateModel.get(position - 3).getHijri();
                Gregorian englishDate = dateModel.get(position - 3).getGregorian();
                setTxt(arbiDate, englishDate, holder);
            }
        }else if (startDay.equalsIgnoreCase("Tuesday")) {
            if (position > 1){
                Hijri arbiDate = dateModel.get(position - 2).getHijri();
                Gregorian englishDate = dateModel.get(position - 2).getGregorian();
                setTxt(arbiDate, englishDate, holder);
            }
        }else if (startDay.equalsIgnoreCase("Monday")) {
            if (position > 0){
                Hijri arbiDate = dateModel.get(position - 1).getHijri();
                Gregorian englishDate = dateModel.get(position - 1).getGregorian();
                setTxt(arbiDate, englishDate, holder);
            }
        }else{
            Hijri arbiDate = dateModel.get(position).getHijri();
            Gregorian englishDate = dateModel.get(position).getGregorian();
            setTxt(arbiDate, englishDate, holder);
        }












    }

    private void setTxt(Hijri arbiDate, Gregorian englishDate, ViewHolder holder) {

        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(englishDate.getDay())  &&
                calendar.get(Calendar.MONTH)+1 == Integer.parseInt(englishDate.getMonth().getNumber()) ){
            holder.binding.parentView.setBackgroundColor(Color.LTGRAY);
        }

        holder.binding.arbiDate.setText(arbiDate.getDay());
        holder.binding.englishDate.setText(englishDate.getDay());

        holder.binding.parentView.setOnClickListener(v->{
            showDialog(activity, arbiDate, englishDate);
        });


        if (englishDate.day.equalsIgnoreCase("01") || arbiDate.day.equalsIgnoreCase("01")){
            holder.binding.engM.setText(englishDate.getMonth().getEn());
        }

        if (arbiDate.getHolidays().size() > 0){
            holder.binding.holiday.setVisibility(View.VISIBLE);
        }else {
            holder.binding.holiday.setVisibility(View.INVISIBLE);
        }

    }

    public static void showDialog(Activity activity, Hijri arbiDate, Gregorian englishDate){
        DialogBottomCalenderBinding dialogBinding = DialogBottomCalenderBinding
                .inflate(activity.getLayoutInflater());

        // Create a new dialog and set the custom layout
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialogBinding.abDate2.setText(arbiDate.getDate());
        dialogBinding.abDay.setText(arbiDate.getDay());
        dialogBinding.abMonthNo.setText(arbiDate.getMonth().getNumber());
        dialogBinding.abMonth.setText(Helper.arbiToBengaliMonth(arbiDate.getMonth().getEn())
                + " (" + arbiDate.getMonth().getAr() + ")");
        dialogBinding.abYear.setText(arbiDate.getYear());
        dialogBinding.abWeekDay.setText(Helper.arbiToBengaliMonth(arbiDate.getWeekday().getEn())
                + " (" + arbiDate.getWeekday().getAr() + ")");

        dialogBinding.egDate2.setText(englishDate.getDate());
        dialogBinding.egDay.setText(englishDate.getDay());
        dialogBinding.egMonthNo.setText(englishDate.getMonth().getNumber());
        dialogBinding.egMonth.setText(englishDate.getMonth().getEn());
        dialogBinding.egYear.setText(englishDate.getYear());
        dialogBinding.egWeekDay.setText(englishDate.getWeekday().getEn() + " (" +
                Helper.englishToBengaliDay(englishDate.getWeekday().getEn()) + ")");


        if (arbiDate.getHolidays().isEmpty()){
            arbiDate.getHolidays().add("No List Found");
        }

        LinearLayoutManager lnm = new LinearLayoutManager(activity);
        dialogBinding.rec.setLayoutManager(lnm);
        HolidayAdapter adapter = new HolidayAdapter(arbiDate.getHolidays(), activity);
        dialogBinding.rec.setAdapter(adapter);

        dialog.show();
    }


    @Override
    public int getItemCount() {
        if (startDay.equalsIgnoreCase("Saturday")){
            return dateModel.size() + 6;
        } else if (startDay.equalsIgnoreCase("Friday")) {
            return dateModel.size() + 5;
        }else if (startDay.equalsIgnoreCase("Thursday")) {
            return dateModel.size() + 4;
        }else if (startDay.equalsIgnoreCase("Wednesday")) {
            return dateModel.size() + 3;
        }else if (startDay.equalsIgnoreCase("Tuesday")) {
            return dateModel.size() + 2;
        }else if (startDay.equalsIgnoreCase("Monday")) {
            return dateModel.size() + 1;
        }else{
            return dateModel.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ChildCalenderCellBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChildCalenderCellBinding.bind(itemView);
        }
    }
}
