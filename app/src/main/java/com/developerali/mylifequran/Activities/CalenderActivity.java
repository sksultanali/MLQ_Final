package com.developerali.mylifequran.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.developerali.mylifequran.Adapters.CalenderAdapter;
import com.developerali.mylifequran.CalenderModels.DateResponse;
import com.developerali.mylifequran.CalenderModels.Gregorian;
import com.developerali.mylifequran.CalenderModels.Hijri;
import com.developerali.mylifequran.CalenderModels.SingleDateResponse;
import com.developerali.mylifequran.Helper;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.RetrofitClient;
import com.developerali.mylifequran.apiset;
import com.developerali.mylifequran.databinding.ActivityCalenderBinding;
import com.developerali.mylifequran.databinding.ChildHolidayBinding;
import com.google.rpc.Help;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalenderActivity extends AppCompatActivity {

    ActivityCalenderBinding binding;
    apiset calenderApi;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    Retrofit retrofit;
    int month, year, m, y;
    String type;
    private static final String BASE_URL = "http://api.aladhan.com/";
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalenderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gestureDetector = new GestureDetector(this, new MyGestureListener());

        month = Integer.parseInt(getIntent().getStringExtra("month"));
        year = Integer.parseInt(getIntent().getStringExtra("year"));
        type = getIntent().getStringExtra("type");

        m = month;
        y = year;

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("LLL yyyy");

        binding.todayBtn.setOnClickListener(v->{
            month = m;
            year = y;
            getDates();
            Toast.makeText(this, "Today", Toast.LENGTH_SHORT).show();
        });
        
        binding.nextDateBtn.setOnClickListener(v->{
            if (type.equalsIgnoreCase("eng")){
                calendar.add(Calendar.MONTH, +1);
                getEngDates();
            }else {
                if (month == 12){
                    year = year + 1;
                    month = 1;

                    getDates();
                }else {
                    month = month +1;
                    getDates();
                }
            }
        });
        binding.previousDateBtn.setOnClickListener(v->{
            if (type.equalsIgnoreCase("eng")){
                calendar.add(Calendar.MONTH, -1);
                getEngDates();
            }else {
                if (month == 1){
                    year = year - 1;
                    month = 12;

                    getDates();
                }else {
                    month = month - 1;
                    getDates();
                }
            }
        });

        binding.clickDateDetails.setOnClickListener(v->{
            Toast.makeText(this, "click any date for details", Toast.LENGTH_LONG).show();
        });


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        calenderApi = retrofit.create(apiset.class);
        getDates();



        binding.selectDate.setOnClickListener(v->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String date = dateFormat.format(calendar.getTimeInMillis()); // 10-12-2023 format
                        binding.selectDate.setText(date);

                        getSelectedDate(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });

        binding.convertedDate.setOnClickListener(v->{
            binding.selectDate.performClick();
        });

        binding.goBack.setOnClickListener(v->{
            onBackPressed();
        });


        binding.selectDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String txt = binding.selectDate.getText().toString();
                if (txt.contains("DD")){
                    binding.detailDateBtn.setVisibility(View.GONE);
                }else {
                    binding.detailDateBtn.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void getSelectedDate(String date) {
        calenderApi = retrofit.create(apiset.class);
        Call<SingleDateResponse> call = calenderApi
                .getDate(date);

        call.enqueue(new Callback<SingleDateResponse>() {
            @Override
            public void onResponse(Call<SingleDateResponse> call, Response<SingleDateResponse> response) {
                if (response.isSuccessful()) {
                    SingleDateResponse responseData = response.body();
                    Hijri arbDate = responseData.getData().getHijri();
                    Gregorian engDate = responseData.getData().getGregorian();

                    binding.convertedDate.setText("হিজরি তারিখ: " + arbDate.getDate());
                    binding.convertedDate.startAnimation(AnimationUtils.loadAnimation(CalenderActivity.this, R.anim.blink));
                    binding.convertedDate.setTextColor(getColor(R.color.white));
                    binding.convertedDate.setBackground(getDrawable(R.drawable.bg_blue_purple_corner8));

                    binding.detailDateBtn.setBackground(getDrawable(R.drawable.bg_btn_effect_red_gray));

                    binding.detailDateBtn.setOnClickListener(v->{
                        CalenderAdapter.showDialog(CalenderActivity.this, arbDate, engDate);
                    });


                } else {
                    // Handle error
                    Toast.makeText(CalenderActivity.this, "error: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SingleDateResponse> call, Throwable t) {
                Toast.makeText(CalenderActivity.this, "error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDates(){
        Call<DateResponse> call = calenderApi
                .getHijriCalendar(month, year);

        call.enqueue(new Callback<DateResponse>() {
            @Override
            public void onResponse(Call<DateResponse> call, Response<DateResponse> response) {
                if (response.isSuccessful()) {
                    DateResponse responseData = response.body();


                    String startDay =  responseData.getData().get(0).getGregorian().weekday.en;
                    String hijriYear = responseData.getData().get(0).getHijri().getYear();
                    String hijriMonth = responseData.getData().get(0).getHijri().getMonth().getEn();
                    String engMonth = responseData.getData().get(0).getGregorian().getMonth().getEn();
                    String engYear = responseData.getData().get(0).getGregorian().getYear();

                    binding.monthYearTV.setText(Helper.arbiToBengaliMonth(hijriMonth) + " " +hijriYear);

                    binding.arbiYear.setText("হিজরি ক্যালেন্ডার ");
                    binding.arbiMonth.setText(engMonth.substring(0, 3) + " " + engYear);

                    GridLayoutManager gnm = new GridLayoutManager(CalenderActivity.this, 7);
                    binding.calendarRecyclerView.setLayoutManager(gnm);
                    CalenderAdapter adapter = new CalenderAdapter(responseData.getData(), CalenderActivity.this, startDay);
                    binding.calendarRecyclerView.setAdapter(adapter);

                    //Toast.makeText(CalenderActivity.this, responseData.getData().get(0).getGregorian().getDate() + "", Toast.LENGTH_SHORT).show();
                    // Process the data here
                } else {
                    // Handle error
                    Toast.makeText(CalenderActivity.this, "error: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DateResponse> call, Throwable t) {
                Toast.makeText(CalenderActivity.this, "error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getEngDates(){
        String date = dateFormat.format(calendar.getTime());
        binding.monthYearTV.setText(date);

        Call<DateResponse> call = calenderApi
                .getEngCalendar(calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));

        call.enqueue(new Callback<DateResponse>() {
            @Override
            public void onResponse(Call<DateResponse> call, Response<DateResponse> response) {
                if (response.isSuccessful()) {
                    DateResponse responseData = response.body();

                    String startDay =  responseData.getData().get(0).getGregorian().weekday.en;
                    String hijriYear = responseData.getData().get(0).getHijri().getYear();
                    binding.arbiYear.setText("হিজরি " + hijriYear);
                    binding.arbiMonth.setText(responseData.getData().get(0).getHijri().getMonth().getEn());

                    GridLayoutManager gnm = new GridLayoutManager(CalenderActivity.this, 7);
                    binding.calendarRecyclerView.setLayoutManager(gnm);
                    CalenderAdapter adapter = new CalenderAdapter(responseData.getData(), CalenderActivity.this, startDay);
                    binding.calendarRecyclerView.setAdapter(adapter);

                    //Toast.makeText(CalenderActivity.this, responseData.getData().get(0).getGregorian().getDate() + "", Toast.LENGTH_SHORT).show();
                    // Process the data here
                } else {
                    // Handle error
                    Toast.makeText(CalenderActivity.this, "error: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DateResponse> call, Throwable t) {
                Toast.makeText(CalenderActivity.this, "error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    private void onSwipeRight() {
        binding.nextDateBtn.performClick();
    }

    private void onSwipeLeft() {
        binding.previousDateBtn.performClick();
    }
}