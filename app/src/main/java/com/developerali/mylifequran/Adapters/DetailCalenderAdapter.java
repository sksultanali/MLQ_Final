package com.developerali.mylifequran.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.mylifequran.CalenderModels.AdvanceDateResponse;
import com.developerali.mylifequran.CalenderModels.DateAdvanceData;
import com.developerali.mylifequran.CalenderModels.DateData;
import com.developerali.mylifequran.CalenderModels.Gregorian;
import com.developerali.mylifequran.CalenderModels.Hijri;
import com.developerali.mylifequran.Helper;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.TimingModel.Timings;
import com.developerali.mylifequran.databinding.ItemCalender2Binding;
import com.google.rpc.Help;

import java.util.Calendar;
import java.util.List;

public class DetailCalenderAdapter extends RecyclerView.Adapter<DetailCalenderAdapter.ViewHolder>{

    Activity activity;
    List<DateAdvanceData> dateModel;

    public DetailCalenderAdapter(Activity activity, List<DateAdvanceData> dateModel) {
        this.activity = activity;
        this.dateModel = dateModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_calender2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hijri arbiDate = dateModel.get(position).getDate().getHijri();
        Gregorian englishDate = dateModel.get(position).getDate().getGregorian();
        Timings timings = dateModel.get(position).getTimings();


        holder.binding.surjodoi.setText(activity.getString(R.string.sunrise_bengali) + "\n" + Helper.FormatIST(timings.getSunrise()));
        holder.binding.surjasto.setText(activity.getString(R.string.sunset_bengali) + "\n" + Helper.FormatIST(timings.getSunset()));

        holder.binding.engDate.setText(englishDate.getDay());
        holder.binding.engMonth.setText(englishDate.getMonth().en +", " +englishDate.getYear());
        holder.binding.engDay.setText(Helper.englishToBengaliDay(englishDate.getWeekday().en));



        if (Integer.parseInt(englishDate.getDay()) == (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) &&
                Integer.parseInt(englishDate.getMonth().number) == (Calendar.getInstance().get(Calendar.MONTH)+1) &&
                Integer.parseInt(englishDate.getYear()) == Calendar.getInstance().get(Calendar.YEAR)) {
            holder.binding.todayIndicator.setVisibility(View.VISIBLE);
            holder.binding.todayIndicator.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.blink));
        }else {
            holder.binding.todayIndicator.setVisibility(View.GONE);
        }

        int arbiDay = Integer.parseInt(arbiDate.getDay());
        if (arbiDay == 1){
            holder.binding.arabDate.setText(arbiDate.getDay());
        }else {
            holder.binding.arabDate.setText((arbiDay - 1) + "," + arbiDate.getDay());
        }
        holder.binding.arabMonth.setText(Helper.arbiToBengaliMonth(arbiDate.getMonth().en) +", " +arbiDate.getYear());
        holder.binding.arabDay.setText(Helper.arbiToBengaliMonth(arbiDate.getWeekday().en));

        holder.binding.fazar.setText(activity.getString(R.string.Fozor) +"\n"
                + Helper.FormatIST(timings.getFajr()) + "\n"
                + Helper.addLessMin(Helper.FormatIST(timings.getSunrise()), -1));

        holder.binding.johor.setText(activity.getString(R.string.Johor) +"\n"
                + Helper.FormatIST(timings.getDhuhr()) + "\n"
                + Helper.addLessMin(Helper.FormatIST(timings.getAsr()), -1));

        holder.binding.asor.setText(activity.getString(R.string.Asor) +"\n"
                + Helper.FormatIST(timings.getAsr()) + "\n"
                + Helper.addLessMin(Helper.FormatIST(timings.getMaghrib()), -1));

        holder.binding.magrib.setText(activity.getString(R.string.Magrib) +"\n"
                + Helper.FormatIST(timings.getMaghrib()) + "\n"
                + Helper.addLessMin(Helper.FormatIST(timings.getIsha()), -1));

        holder.binding.isha.setText(activity.getString(R.string.Esa) +"\n"
                + Helper.FormatIST(timings.getIsha()) + "\n"
                + Helper.addLessMin(Helper.FormatIST(timings.getFajr()), -5));

        holder.binding.seheri.setText(activity.getString(R.string.seheri) +"\n"
                + Helper.addLessMin(Helper.FormatIST(timings.getFajr()), -5));

        holder.binding.iftar.setText(activity.getString(R.string.iftar) +"\n"
                + Helper.FormatIST(timings.getMaghrib()));

        holder.binding.nisidhoSomoi.setText(activity.getString(R.string.Namazer_Nisiddho_Somoi) +"\n"
                + activity.getString(R.string.Vor) + " : " + Helper.exitAmPm(Helper.FormatIST(timings.getSunrise())) + " - " + Helper.addLessMin(Helper.FormatIST(timings.getSunrise()), 15) + "\n"
                + activity.getString(R.string.Noon_Bengali) + " : " + Helper.exitAmPm(Helper.addLessMin(Helper.FormatIST(timings.getDhuhr()), -5)) + " - " + Helper.FormatIST(timings.getDhuhr()) + "\n"
                + activity.getString(R.string.Sondha) + " : " + Helper.exitAmPm(Helper.addLessMin(Helper.FormatIST(timings.getSunset()), -15)) + " - " + Helper.FormatIST(timings.getSunset()) + "\n");


        if (arbiDate.getHolidays().isEmpty()){
            holder.binding.holiday.setVisibility(View.GONE);
        }else {
            holder.binding.holiday.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v->{
            CalenderAdapter.showDialog(activity, arbiDate, englishDate);
        });


    }

    @Override
    public int getItemCount() {
        return dateModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemCalender2Binding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCalender2Binding.bind(itemView);
        }
    }
}
