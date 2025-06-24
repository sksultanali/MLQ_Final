package com.developerali.mylifequran.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.developerali.mylifequran.Adapters.CalenderAdapter;
import com.developerali.mylifequran.Adapters.DetailCalenderAdapter;
import com.developerali.mylifequran.CalenderModels.AdvanceDateResponse;
import com.developerali.mylifequran.CalenderModels.DateAdvanceData;
import com.developerali.mylifequran.CalenderModels.DateResponse;
import com.developerali.mylifequran.Helper;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.apiset;
import com.developerali.mylifequran.databinding.ActivityDetailCalenderBinding;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailCalenderActivity extends AppCompatActivity {

    ActivityDetailCalenderBinding binding;
    String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    List<String> yearsList;
    int selectedMonth;
    int selectedYear;
    apiset calenderApi;
    Retrofit retrofit;
    private static final String BASE_URL = "http://api.aladhan.com/";
    SharedPreferences sharedPreferences;
    String lat, lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailCalenderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        sharedPreferences = getSharedPreferences("locations", Context.MODE_MULTI_PROCESS);
        lat = sharedPreferences.getString("latitude", null);
        lon = sharedPreferences.getString("longitude", null);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        selectedYear = currentYear;
        selectedMonth = Calendar.getInstance().get(Calendar.MONTH);

        binding.todayBtn.setOnClickListener(v->{
            if (lat != null && lon != null){
                getAdvanceDates(Calendar.getInstance().get(Calendar.MONTH)+1,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Double.parseDouble(lat), Double.parseDouble(lon));
            }else {
                checkLocationAndUpdateTime();
            }
            binding.spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH));
            binding.spinnerYear.setSelection(100);
            Toast.makeText(this, "Today", Toast.LENGTH_SHORT).show();

        });

        ArrayAdapter<String> obj2 = new ArrayAdapter<String>(DetailCalenderActivity.this, R.layout.layout_spinner_item, months);
        binding.spinnerMonth.setAdapter(obj2);
        binding.spinnerMonth.setSelection(selectedMonth);

        binding.spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position + 1;
                if (lat != null && lon != null){
                    getAdvanceDates(selectedMonth,selectedYear, Double.parseDouble(lat), Double.parseDouble(lon));
                }else {
                    checkLocationAndUpdateTime();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.goBack.setOnClickListener(v->{
            onBackPressed();
        });

        yearsList = new ArrayList<>();
        for (int i = currentYear-100; i <= currentYear + 100; i++) {
            yearsList.add(String.valueOf(i));
        }

        ArrayAdapter<String> obj3 = new ArrayAdapter<String>(DetailCalenderActivity.this, R.layout.layout_spinner_item, yearsList);
        binding.spinnerYear.setAdapter(obj3);
        binding.spinnerYear.setSelection(100);

        binding.spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = Integer.parseInt(yearsList.get(position));
                if (lat != null && lon != null){
                    getAdvanceDates(selectedMonth,selectedYear, Double.parseDouble(lat), Double.parseDouble(lon));
                }else {
                    checkLocationAndUpdateTime();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        calenderApi = retrofit.create(apiset.class);


        if (Helper.isLocationEnabled(DetailCalenderActivity.this)){
            checkLocationAndUpdateTime();
        }else {
            if (lat != null && lon != null){
                getAdvanceDates(selectedMonth,selectedYear, Double.parseDouble(lat), Double.parseDouble(lon));
            }else {
                checkLocationAndUpdateTime();
            }
        }

    }

    private void checkLocationAndUpdateTime() {
        if (Helper.isLocationEnabled(DetailCalenderActivity.this)){
            Dexter.withContext(DetailCalenderActivity.this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                            getLocation();
                        }
                        @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                            // Permission is denied
                        }
                        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            // Show a rationale to the user
                            token.continuePermissionRequest();
                        }
                    }).check();
        }else {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            SettingsClient settingsClient = LocationServices.getSettingsClient(DetailCalenderActivity.this);

            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .build());

            task.addOnSuccessListener(DetailCalenderActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    // Location settings are satisfied
                    // You can proceed with location retrieval
                    getLocation();
                }
            });

            task.addOnFailureListener(DetailCalenderActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        try {
                            // Location settings are not satisfied
                            // Show the dialog to enable location services
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(DetailCalenderActivity.this, 20);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Handle the exception
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 || resultCode == RESULT_OK) {
            getLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(DetailCalenderActivity.this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(DetailCalenderActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Get the latitude and longitude from the location object
                            lat = String.valueOf(location.getLatitude());
                            lon = String.valueOf(location.getLongitude());

                            SharedPreferences.Editor obj = sharedPreferences.edit();
                            obj.putString("latitude", lat);
                            obj.putString("longitude", lon);
                            obj.commit();

                            getAdvanceDates(Calendar.getInstance().get(Calendar.MONTH)+1,
                                    Calendar.getInstance().get(Calendar.YEAR),
                                    Double.parseDouble(lat), Double.parseDouble(lon));
                        }
                    }
                });
    }

    public void getDates(int month, int year){
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

//                    binding.monthYearTV.setText(Helper.arbiToBengaliMonth(hijriMonth) + " " +hijriYear);
//                    binding.arbiYear.setText("হিজরি ক্যালেন্ডার ");
//                    binding.arbiMonth.setText(engMonth.substring(0, 3) + " " + engYear);


                    GridLayoutManager gnm = new GridLayoutManager(DetailCalenderActivity.this, 7);
                    binding.calendarRecyclerView.setLayoutManager(gnm);
                    CalenderAdapter adapter = new CalenderAdapter(responseData.getData(), DetailCalenderActivity.this, startDay);
                    binding.calendarRecyclerView.setAdapter(adapter);

                    //Toast.makeText(CalenderActivity.this, responseData.getData().get(0).getGregorian().getDate() + "", Toast.LENGTH_SHORT).show();
                    // Process the data here
                } else {
                    // Handle error
                    Toast.makeText(DetailCalenderActivity.this, "error: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DateResponse> call, Throwable t) {
                Toast.makeText(DetailCalenderActivity.this, "error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getAdvanceDates(int month, int year, double lat, double lon){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<AdvanceDateResponse> call = calenderApi
                .getAdvanceHijriCalendar(month, year, lat, lon,1,1);

        call.enqueue(new Callback<AdvanceDateResponse>() {
            @Override
            public void onResponse(Call<AdvanceDateResponse> call, Response<AdvanceDateResponse> response) {
                if (response.isSuccessful()) {
                    AdvanceDateResponse responseData = response.body();

                    String startDayEng =  responseData.getData().get(0).getDate().getGregorian().date;
                    String arbiMonth = String.valueOf(responseData.getData().get(0).getDate().getHijri().month.number);
                    String arbiYear = String.valueOf(responseData.getData().get(0).getDate().getHijri().year);
                    String time = responseData.getData().get(0).getTimings().getFajr();

                    List<DateAdvanceData> dateAdvanceData = responseData.getData();

                    binding.dots.setOnClickListener(v->{
                        Intent i = new Intent(DetailCalenderActivity.this, CalenderActivity.class);
                        i.putExtra("month", arbiMonth);
                        i.putExtra("year", arbiYear);
                        i.putExtra("type", "arbi");
                        startActivity(i);
                    });

                    LinearLayoutManager lnm = new LinearLayoutManager(DetailCalenderActivity.this);
                    binding.calendarRecyclerView.setLayoutManager(lnm);
                    DetailCalenderAdapter adapter = new DetailCalenderAdapter(DetailCalenderActivity.this, dateAdvanceData);
                    binding.calendarRecyclerView.setAdapter(adapter);


                    if (month == (Calendar.getInstance().get(Calendar.MONTH)+1) &&
                            year == Calendar.getInstance().get(Calendar.YEAR)) {
                        try {
                            binding.calendarRecyclerView.smoothScrollToPosition(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        }catch (Exception e){

                        }
                    }

                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    // Handle error
                    Toast.makeText(DetailCalenderActivity.this, "error: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Error: " + response.message());
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AdvanceDateResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(DetailCalenderActivity.this, "error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}