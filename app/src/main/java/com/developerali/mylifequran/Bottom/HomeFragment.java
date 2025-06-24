package com.developerali.mylifequran.Bottom;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.CountDownTimer;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.developerali.mylifequran.Activities.DetailCalenderActivity;
import com.developerali.mylifequran.Helper;
import com.developerali.mylifequran.LoginActivity;
import com.developerali.mylifequran.Models.viewFlipperModel;
import com.developerali.mylifequran.QuizActivity;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.TimingModel.DateInfo;
import com.developerali.mylifequran.TimingModel.PrayerTimes;
import com.developerali.mylifequran.TimingModel.Timings;
import com.developerali.mylifequran.apiset;
import com.developerali.mylifequran.databinding.FragmentHomeBinding;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import me.ibrahimsn.lib.SmoothBottomBar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseFirestore database;
    FirebaseDatabase data;
    FirebaseAuth auth;
    String message, mStart;
    ProgressDialog dialog;
    boolean order;
    ArrayList<String> userPlayers;
    SharedPreferences sharedPreferences;
    private static final String BASE_URL = "http://api.aladhan.com/";
    apiset calenderApi;
    private CountDownTimer countDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        data = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("connecting server");
        dialog.setCancelable(false);

        SmoothBottomBar bottomBar = getActivity().findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(2);

        userPlayers = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("locations", Context.MODE_MULTI_PROCESS);

        String lat = sharedPreferences.getString("latitude", null);
        String lon = sharedPreferences.getString("longitude", null);

        if (lat != null && lon != null){
            getTimes(Double.parseDouble(lat), Double.parseDouble(lon));
        }else {
            checkLocationAndUpdateTime();
        }

        Calendar calendar = Calendar.getInstance();
        String date2 = Helper.formatDate2(calendar.getTime()); // 10 Dec 2023 format
        binding.dateEnglish.setText(date2);

        //showCase();
        if (isConnectedNetwork(getActivity())) {
            if (Helper.isLocationEnabled(getActivity())){
                binding.swipeRefresh.setRefreshing(true);
                refresh();
                checkLocationAndUpdateTime();
            }else {
                setOldTimes();
            }
        } else {
            setOldTimes();
            binding.errorLayout.setVisibility(View.VISIBLE);
            binding.errorText.setText("আপনার ইন্টারনেট চলছে না! সঠিক নামাজের সময় পাওয়ার জন্য ইন্টারনেট চালু করে রিফ্রেশ করুন।");
        }


        Animation anim_in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        Animation anim_out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);

        binding.viewFlipper.setInAnimation(anim_in);
        binding.viewFlipper.setOutAnimation(anim_out);
        binding.viewFlipper.setFlipInterval(5000);
        binding.viewFlipper.startFlipping();


        binding.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getActivity())
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                // Permission is granted
                                // Get the user's current location
                                binding.locationContainer.setVisibility(View.GONE);
                                getLocation();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // Permission is denied
                                binding.locationContainer.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                // Show a rationale to the user
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        binding.cardPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnectedNetwork(getActivity())) {
                    Snackbar snackbar = Snackbar.make(binding.cardPlay, "Live Quiz Need Internet...", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.CENTER_VERTICAL;
                    view.setLayoutParams(params);
                    snackbar.show();
                } else if (auth.getCurrentUser() == null) {
                    Snackbar snackbar = Snackbar.make(binding.nestedScrollView2, "LogIn To Play Quiz...", Snackbar.LENGTH_LONG)
                            .setAction("LogIn", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                }
                            });
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.CENTER_VERTICAL;
                    view.setLayoutParams(params);
                    snackbar.show();
                } else if (order == false){
                    Snackbar snackbar = Snackbar.make(binding.cardPlay, "This Is Not Quiz Time", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.CENTER_VERTICAL;
                    view.setLayoutParams(params);
                    snackbar.show();
                } else {

                    //startActivity(new Intent(getActivity().getApplicationContext(), QuizActivity.class));

                    data.getReference().child("players")
                            .child(auth.getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        startActivity(new Intent(getActivity().getApplicationContext(), QuizActivity.class));
                                    }else {
                                        Toast.makeText(getActivity(), "Not Registered !", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        binding.shareTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedNetwork(getActivity())) {
                    refresh();
                    dialog.show();
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/html");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                    if (sharingIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(Intent.createChooser(sharingIntent, "Namazer Somoi Talika"));
                    }
                    dialog.dismiss();
                } else {
                    Snackbar snackbar = Snackbar.make(binding.shareTimes, "Internet Not Connected", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.CENTER_VERTICAL;
                    view.setLayoutParams(params);
                    snackbar.show();
                }

            }
        });

        binding.swipeRefresh.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                checkLocationAndUpdateTime();
                if (!isConnectedNetwork(getActivity())){
                    binding.errorLayout.setVisibility(View.VISIBLE);
                    binding.errorText.setText("আপনার ইন্টারনেট চলছে না! সঠিক নামাজের সময় পাওয়ার জন্য ইন্টারনেট চালু করে রিফ্রেশ করুন।");
                }else {
                    binding.errorLayout.setVisibility(View.GONE);
                }
            }
        });
        
        binding.shareTimes.setOnClickListener(v->{
            shareTextMaking();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(Intent.createChooser(shareIntent, "Share text via"));
            } else {
                Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });

        binding.ibutton.setOnClickListener(v->{
            if (Helper.isChromeCustomTabsSupported(getActivity())){
                Helper.openChromeTab("https://mylifequran1.blogspot.com/2024/03/blog-post.html", getActivity());
            }else {
                String url = "https://mylifequran1.blogspot.com/2024/03/blog-post.html";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        binding.calender.setOnClickListener(v->{
            startActivity(new Intent(getActivity().getApplicationContext(), DetailCalenderActivity.class));
        });



    }

    private void setOldTimes() {
        String sunrise = sharedPreferences.getString("sunrise", "00:00");
        String sunset = sharedPreferences.getString("sunset", "00:00");
        binding.sunriseText.setText(sunrise);
        binding.sunsetText.setText(sunset);

        String fazar = sharedPreferences.getString("fazar", "00:00");
        String johor = sharedPreferences.getString("johor", "00:00");
        mStart = sharedPreferences.getString("johorStart", "00:00 am");
        String asor = sharedPreferences.getString("asor", "00:00");
        String magrib = sharedPreferences.getString("magrib", "00:00");
        String isha = sharedPreferences.getString("isha", "00:00");
        binding.forozText.setText(fazar);
        binding.johorText.setText(johor);
        binding.asorText.setText(asor);
        binding.magribText.setText(magrib);
        binding.esaText.setText(isha);

        String nisidho1 = sharedPreferences.getString("nisidho1", "00:00");
        String nisidho2 = sharedPreferences.getString("nisidho2", "00:00");
        String nisidho3 = sharedPreferences.getString("nisidho3", "00:00");
        binding.nisidho1.setText(nisidho1);
        binding.nisidho2.setText(nisidho2);
        binding.nisidho3.setText(nisidho3);

        String seheri = sharedPreferences.getString("seheri", "00:00");
        String iftar = sharedPreferences.getString("iftar", "00:00");
        binding.seheriText.setText(seheri);
        binding.iftarText.setText(iftar);

        String ishraq = sharedPreferences.getString("ishraq", "00:00");
        String auabin = sharedPreferences.getString("auabin", "00:00");
        String tahajjud = sharedPreferences.getString("tahajjud", "00:00");
        binding.ishraq.setText(ishraq);
        binding.auabin.setText(auabin);
        binding.tahajjud.setText(tahajjud);

//        if (fazar.equalsIgnoreCase("00:00") || johor.equalsIgnoreCase("00:00") ||
//        asor.equalsIgnoreCase("00:00") || magrib.equalsIgnoreCase("00:00") ||
//        isha.equalsIgnoreCase("00:00")){
//
//        }else {
//
//        }
        try {
            checkForTimer();
        }catch (Exception e){

        }

    }

    void checkForTimer(){
        String[] onlyStartTime = {
                Helper.convertToEndTime(binding.forozText.getText().toString()),
                mStart,
                Helper.convertToEndTime(binding.johorText.getText().toString()),
                Helper.convertToEndTime(binding.asorText.getText().toString()),
                Helper.convertToEndTime(binding.magribText.getText().toString()),
                Helper.convertToEndTime(binding.esaText.getText().toString())
        };
        try {
            long gapInMillis = getNextTimeGapInMillis(onlyStartTime);
            startCountdownTimer(gapInMillis);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "facing problems for counter time", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLocationAndUpdateTime() {
        if (Helper.isLocationEnabled(getActivity())){
            Dexter.withContext(getActivity())
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                            binding.errorLayout.setVisibility(View.GONE);
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

            SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());

            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .build());

            task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    // Location settings are satisfied
                    // You can proceed with location retrieval
                    getLocation();
                }
            });

            task.addOnFailureListener(requireActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        try {
                            // Location settings are not satisfied
                            // Show the dialog to enable location services
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(requireActivity(), 20);
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
            Toast.makeText(getActivity(), "enabled", Toast.LENGTH_SHORT).show();
            refresh();
            getLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Get the latitude and longitude from the location object
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            try {
                                getTimes(latitude, longitude);
                            }catch (Exception e){

                            }

                            SharedPreferences.Editor obj = sharedPreferences.edit();
                            obj.putString("latitude", String.valueOf(latitude));
                            obj.putString("longitude", String.valueOf(longitude));
                            obj.commit();
                        }
                    }
                });
    }

    private void getTimes(double latitude, double longitude) {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        //Toast.makeText(getActivity(), "updating time...", Toast.LENGTH_LONG).show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.aladhan.com/v1/")
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiset prayerTimesApi = retrofit.create(apiset.class);

        prayerTimesApi.getPrayerTimes(
                date,
                latitude,
                longitude,
                1,
                1
        ).enqueue(new Callback<PrayerTimes>() {
            @Override
            public void onResponse(Call<PrayerTimes> call, Response<PrayerTimes> response) {
                if (response.isSuccessful()) {

                    Timings timings = response.body().getData().getTimings();
                    DateInfo date = response.body().getData().getDate();

                    String Day = Helper.englishToBengaliDay(date.getGregorian().getWeekday().getEn());
                    binding.majHab.setText(Day);

                    binding.forozText.setText(Helper.exitAmPm(Helper.getFormattedTime(timings.getFajr())) + " - "
                            + Helper.addLessMin(Helper.getFormattedTime(timings.getSunrise()), -1));

                    binding.johorText.setText(Helper.exitAmPm(Helper.getFormattedTime(timings.getDhuhr())) + " - "
                            + Helper.addLessMin(Helper.getFormattedTime(timings.getAsr()), -1));

                    binding.asorText.setText(Helper.exitAmPm(Helper.getFormattedTime(timings.getAsr())) + " - "
                            + Helper.addLessMin(Helper.getFormattedTime(timings.getMaghrib()), -1));

                    binding.magribText.setText(Helper.exitAmPm(Helper.getFormattedTime(timings.getMaghrib())) + " - "
                            + Helper.addLessMin(Helper.getFormattedTime(timings.getIsha()), -1));

                    binding.esaText.setText(Helper.exitAmPm(Helper.getFormattedTime(timings.getIsha()))+ " - "
                            + Helper.addLessMin(Helper.getFormattedTime(timings.getFajr()), -5));

                    int arbiDay = Integer.parseInt(date.getHijri().getDay());
                    binding.arbiYear.setText(date.getHijri().getYear());
                    if (arbiDay == 1){
                        binding.dateArbi.setText(arbiDay +" " + Helper.arbiToBengaliMonth(date.getHijri().getMonth().getEn()));
                    }else {
                        String text = (arbiDay -1 )
                                + "," +
                                arbiDay
                                + " " + Helper.arbiToBengaliMonth(date.getHijri().getMonth().getEn());

                        SpannableString spannableString = new SpannableString(text);

                        if (Helper.isTimePassed(timings.getMaghrib())){
                            spannableString.setSpan(new StrikethroughSpan(), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.dateArbi.setText(spannableString);
                        }else {
                            spannableString.setSpan(new StrikethroughSpan(), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.dateArbi.setText(spannableString);
                        }
                    }

                    String sunriseText = getString(R.string.sunrise_bengali) + " - " + "<b>" + Helper.getFormattedTime(timings.getSunrise()) + "</b>";
                    String sunsetText = getString(R.string.sunset_bengali) + " - " + "<b>" + Helper.getFormattedTime(timings.getSunset()) + "</b>";

                    // Set the formatted sunrise text to the TextView
                    binding.sunriseText.setText(Html.fromHtml(sunriseText));
                    binding.sunsetText.setText(Html.fromHtml(sunsetText));


                    binding.seheriText.setText(Helper.addLessMin(Helper.getFormattedTime(timings.getFajr()), -5));
                    binding.iftarText.setText(Helper.getFormattedTime(timings.getMaghrib()));

                    binding.nisidho1.setText(Helper.exitAmPm(Helper.getFormattedTime(timings.getSunrise())) + " - " + Helper.addLessMin(Helper.getFormattedTime(timings.getSunrise()), 15));
                    binding.nisidho2.setText(Helper.exitAmPm(Helper.addLessMin(Helper.getFormattedTime(timings.getDhuhr()), -5)) + " - " + Helper.getFormattedTime(timings.getDhuhr()));
                    binding.nisidho3.setText(Helper.exitAmPm(Helper.addLessMin(Helper.getFormattedTime(timings.getSunset()), -15)) + " - " + Helper.getFormattedTime(timings.getSunset()));

                    binding.ishraq.setText(Helper.exitAmPm(Helper.addLessMin(Helper.getFormattedTime(timings.getSunrise()), 15)) +" - "+ Helper.addLessMin(Helper.getFormattedTime(timings.getDhuhr()), -6));
                    binding.auabin.setText(Helper.exitAmPm(Helper.addLessMin(Helper.getFormattedTime(timings.getMaghrib()), 10)) +" - "+ Helper.addLessMin(Helper.getFormattedTime(timings.getIsha()), -1));
                    binding.tahajjud.setText(Helper.addLessMin(Helper.getFormattedTime(timings.getFajr()), -5));


                    SharedPreferences.Editor obj = sharedPreferences.edit();
                    obj.putString("sunrise", binding.sunriseText.getText().toString());
                    obj.putString("sunset", binding.sunsetText.getText().toString());

                    obj.putString("fazar", binding.forozText.getText().toString());
                    obj.putString("johor", binding.johorText.getText().toString());
                    obj.putString("johorStart", Helper.getFormattedTime(timings.getDhuhr()));
                    obj.putString("asor", binding.asorText.getText().toString());
                    obj.putString("magrib", binding.magribText.getText().toString());
                    obj.putString("isha", binding.esaText.getText().toString());

                    obj.putString("nisidho1", binding.nisidho1.getText().toString());
                    obj.putString("nisidho2", binding.nisidho2.getText().toString());
                    obj.putString("nisidho3", binding.nisidho3.getText().toString());

                    obj.putString("seheri", binding.seheriText.getText().toString());
                    obj.putString("iftar", binding.iftarText.getText().toString());

                    obj.putString("ishraq", binding.ishraq.getText().toString());
                    obj.putString("auabin", binding.auabin.getText().toString());
                    obj.putString("tahajjud", binding.tahajjud.getText().toString());
                    obj.commit();

                    try {
                        mStart = Helper.getFormattedTime(timings.getDhuhr());
                        checkForTimer();
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onFailure(Call<PrayerTimes> call, Throwable t) {
                if (t != null){
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public long getNextTimeGapInMillis(String[] onlyStartTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
        Calendar now = Calendar.getInstance();

        long smallestGap = Long.MAX_VALUE;
        boolean found = false;

        for (int i = 0; i < onlyStartTime.length; i++) {
            Date timeDate = sdf.parse(onlyStartTime[i]);
            Calendar timeCal = Calendar.getInstance();
            timeCal.setTime(timeDate);
            // Adjust the date part of the time to today
            timeCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
            timeCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
            timeCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

            // Check if the time has already passed for today, if so, check for next day
            if (timeCal.before(now)) {
                timeCal.add(Calendar.DATE, 1); // Move to the next day
            }

            long gap = timeCal.getTimeInMillis() - now.getTimeInMillis();
            if (gap < smallestGap) {
                smallestGap = gap;
                found = true;
                setNextPrayer(i);
            }
        }

        if (found) {
            return smallestGap;
        } else {
            return 0;
        }
    }

    public void setNextPrayer(int i) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.blink);
        switch (i){
            case 0:
                binding.nextPrayer.setText("ফজর ওয়াক্ত আছে");
                break;
            case 1:
                binding.nextPrayer.setText("ফজর ওয়াক্ত আছে");
                break;
            case 2:
                binding.nextPrayer.setText("জোহর ওয়াক্ত আছে");
                break;
            case 3:
                binding.nextPrayer.setText("আসর ওয়াক্ত আছে");
                break;
            case 4:
                binding.nextPrayer.setText("মাগরিব ওয়াক্ত আছে");
                break;
            case 5:
                binding.nextPrayer.setText("এশা ওয়াক্ত আছে");
                break;
        }
    }

    public static boolean isConnectedNetwork (Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    void refresh(){
        data.getReference().child("viewflipers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    viewFlipperModel vm = snapshot.getValue(viewFlipperModel.class);
                    binding.textA.setText(vm.getHomeText1());
                    binding.textB.setText(vm.getHomeText2());
                    binding.swipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.swipeRefresh.setRefreshing(false);
            }
        });

        database.collection("quizOrder")
                .document("start")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            order = documentSnapshot.getBoolean("order");
                            binding.swipeRefresh.setRefreshing(false);
                        }
                    }
                });

    }

//    private void getAllPlayer() {
//        String url = "https://script.google.com/macros/s/AKfycbwv5zsP2ST497EbkCcD6xN3jOukE05M7WqH6a6rXOEkxCjinx6crEElTwZfMunoPhYM/exec?";
//        url = url + "action=read";
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("items");
//                    for (int i = 0; i<jsonArray.length(); i++){
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        String id = object.getString("id");
//                        userPlayers.add(id);
//                    }
//
//                } catch (JSONException e) {
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//
//
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        queue.add(jsonObjectRequest);
//    }

    private void startCountdownTimer(long totalTimeInMillis) {
        // Create a countdown timer
        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Calculate hours, minutes, and seconds from milliseconds
                long hours = millisUntilFinished / (1000 * 60 * 60);
                long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
                long seconds = (millisUntilFinished % (1000 * 60)) / 1000;

                // Format the time string
                String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                binding.timmerText.setText(timeLeftFormatted);
                int progress = (int) ((millisUntilFinished * 100) / totalTimeInMillis);
                //binding.ringprogressBar2.setProgress(progress);
            }

            @Override
            public void onFinish() {
                binding.timmerText.setText("00:00:00");
            }
        };

        // Start the countdown timer
        countDownTimer.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cancel the countdown timer to prevent memory leaks
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    void shareTextMaking(){
        message =
                "Arbi Date: " + binding.dateArbi.getText().toString() + "\n"+
                        "Date: " + binding.dateEnglish.getText().toString() + "\n" +
                        "নিশ্চয় সালাত মুমিনদের উপর নির্দিষ্ট সময়ে ফরয। সুরা নিসা ১০৩ আল কুরআন\n" +
                        "----------------------------\n" +

                        binding.sunriseText.getText().toString() + "\n"+
                        binding.sunsetText.getText().toString() + "\n\n"+

                        "*নামাজের সময়*\n" +
                        "ফজর: " + binding.forozText.getText().toString() + "\n"+
                        "যোহর: " + binding.johorText.getText().toString() + "\n"+
                        "আসর: " + binding.asorText.getText().toString() + "\n"+
                        "মাগরিব: " + binding.magribText.getText().toString() + "\n"+
                        "ঈশা: " + binding.esaText.getText().toString() + "\n\n"+

                        "সেহেরী শেষ: " + binding.seheriText.getText().toString() + "\n"+
                        "ইফতার শুরু: "+ binding.iftarText.getText().toString() + "\n\n"+

                        "*নামাজের নিষিদ্ধ সময়*\n" +
                        binding.nisidho1.getText().toString() + "\n"+
                        binding.nisidho2.getText().toString() + "\n"+
                        binding.nisidho3.getText().toString() + "\n\n"+

                        "*নফল নামাজের সময়*\n" +
                        "ইশ্রাক: " + binding.ishraq.getText().toString() + "\n"+
                        "আওয়াবিন: " + binding.auabin.getText().toString() + "\n"+
                        "তাহাজ্জুদ শেষ: " + binding.tahajjud.getText().toString() + "\n"+

                        "----------------------------\n" +
                        "বিঃ দ্রঃ- ফজরের সময় শুরু হবার পাঁচ মিঃ পর আজান দেওয়া উত্তম।" + "\n" +
                        "----------------------------\n" + "\n" +
                        "       Presented By\n" +
                        "       *My Life Qur'an*\n" +
                        "\n" +

                        "Download App From PlayStore\n" +
                        "https://play.google.com/store/apps/details?id=com.developerali.mylifequran\n" +
                        "\uD83D\uDC49 Facebook Page\n" +
                        "https://www.facebook.com/MyLifeQuran112?mibextid=ZbWKwL\n" +
                        "\uD83D\uDC49 YouTube \n" +
                        "https://youtube.com/@MyLifeQuran?si=GHklqpqwcKYsnRUf\n" +
                        "\uD83D\uDC49 WhatsApp Channel \n" +
                        "https://whatsapp.com/channel/0029Va4JhR6GZNCxqC2OWr0r\n" +
                        "\uD83D\uDC49 Telegram\n" +
                        "https://t.me/mylifequra\n" +
                        "\uD83D\uDC49 WhatsApp Group\n" +
                        "https://chat.whatsapp.com/HNRbRvaZ77sIoHOTxj2KJM";

    }

}