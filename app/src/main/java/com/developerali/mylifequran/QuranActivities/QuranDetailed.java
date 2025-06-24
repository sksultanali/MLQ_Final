package com.developerali.mylifequran.QuranActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.developerali.mylifequran.QuranAdapters.SurahDetailedAdapter;
import com.developerali.mylifequran.QuranModel.AudioMainModel;
import com.developerali.mylifequran.QuranModel.Ayah;
import com.developerali.mylifequran.QuranModel.DetailedRespons;
import com.developerali.mylifequran.QuranModel.JSRes;
import com.developerali.mylifequran.QuranModel.detaildAyah;
import com.developerali.mylifequran.QuranModel.detailedData;
import com.developerali.mylifequran.RetrofitClient;
import com.developerali.mylifequran.databinding.ActivityQuranDetailedBinding;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuranDetailed extends AppCompatActivity {

    ActivityQuranDetailedBinding binding;
    String id;
    AudioMainModel audioData;
    detailedData bengaliData;
    ArrayList<Ayah> audioList;
    ArrayList<detaildAyah> bengaliList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuranDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        id = getIntent().getStringExtra("num");




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QuranDetailed.this);
        binding.detailsRec.setLayoutManager(linearLayoutManager);

        Call<JSRes> call = RetrofitClient.getInstance()
                .getapi()
                .getAudios(id);

        call.enqueue(new Callback<JSRes>() {
            @Override
            public void onResponse(Call<JSRes> call, Response<JSRes> response) {
                JSRes jsonResponse = response.body();
                audioData = jsonResponse.getData();

                audioList = new ArrayList<>(audioData.getAyahs().size());
                audioList.clear();
                audioList.addAll(audioData.getAyahs());


                //Toast.makeText(QuranDetailed.this, audioList.get(0).getText(), Toast.LENGTH_SHORT).show();

                binding.surahName.setText(audioData.getEnglishName());
                binding.surahEnglishName.setText(audioData.getEnglishNameTranslation());
                binding.surahDetails.setText(audioData.getNumberOfAyahs() + "Ayahs | " + audioData.getRevelationType());



                Call<DetailedRespons> callMeaning = RetrofitClient.getInstance()
                        .getapi()
                        .getMeaning(id);

                callMeaning.enqueue(new Callback<DetailedRespons>() {
                    @Override
                    public void onResponse(Call<DetailedRespons> call, Response<DetailedRespons> response) {
                        DetailedRespons jsonResponse = response.body();
                        bengaliData = jsonResponse.getData();

                        bengaliList = new ArrayList<>(bengaliData.getAyahs().size());
                        bengaliList.clear();
                        bengaliList.addAll(bengaliData.getAyahs());


                        SurahDetailedAdapter adapter = new SurahDetailedAdapter(QuranDetailed.this, audioList, bengaliList, binding.detailsRec);
                        binding.detailsRec.setAdapter(adapter);
                        binding.detailsRec.hideShimmerAdapter();
                        adapter.notifyDataSetChanged();

                        //Toast.makeText(QuranDetailed.this, bengaliList.get(0).getText(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<DetailedRespons> call, Throwable t) {
                        Toast.makeText(QuranDetailed.this, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });



            }

            @Override
            public void onFailure(Call<JSRes> call, Throwable t) {
                Toast.makeText(QuranDetailed.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}