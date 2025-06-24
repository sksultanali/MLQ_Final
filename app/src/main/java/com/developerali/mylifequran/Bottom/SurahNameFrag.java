package com.developerali.mylifequran.Bottom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developerali.mylifequran.QuranActivities.OfflineQuranActivity;
import com.developerali.mylifequran.QuranAdapters.SuraNameAdapter;
import com.developerali.mylifequran.QuranModel.JSONResponse;
import com.developerali.mylifequran.QuranModel.SurahNamesModel;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.RetrofitClient;
import com.developerali.mylifequran.databinding.FragmentSurahNameBinding;

import java.util.ArrayList;
import java.util.Arrays;

import me.ibrahimsn.lib.SmoothBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurahNameFrag extends Fragment {

    FragmentSurahNameBinding binding;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSurahNameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SmoothBottomBar bottomBar = getActivity().findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(0);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading data...");
        progressDialog.setCancelable(false);


        if (isConnectedNetwork(getActivity())){
            progressDialog.show();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            binding.suraThisRec.setLayoutManager(linearLayoutManager);

            Call<JSONResponse> call = RetrofitClient.getInstance()
                    .getapi()
                    .getNames();

            call.enqueue(new Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();
                    ArrayList<SurahNamesModel> data = new ArrayList<>(Arrays.asList(jsonResponse.getData()));

                    SuraNameAdapter adapter = new SuraNameAdapter(getActivity(), data);
                    binding.suraThisRec.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 1000);

                    Log.d("MessageName", "code..."+response.code() + " message..." + response.message()+" body..."+response.body());
                }
                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 1000);
                    if (t != null){
                        Toast.makeText(getActivity(), call.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }else {
            binding.readOffline.setVisibility(View.VISIBLE);
            binding.suraThisRec.setVisibility(View.GONE);
        }






    }

    public static boolean isConnectedNetwork (Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}