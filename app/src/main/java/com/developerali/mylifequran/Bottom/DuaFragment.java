package com.developerali.mylifequran.Bottom;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developerali.mylifequran.DuaListActivity;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.databinding.FragmentDuaBinding;

import me.ibrahimsn.lib.SmoothBottomBar;


public class DuaFragment extends Fragment {

    FragmentDuaBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDuaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        SmoothBottomBar bottomBar = getActivity().findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(3);

        binding.romzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DuaListActivity.class);
                i.putExtra("collection", "Romzan");
                getActivity().startActivity(i);
            }
        });
        binding.poribar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DuaListActivity.class);
                i.putExtra("collection", "Poribar");
                startActivity(i);
            }
        });
        binding.osusthota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DuaListActivity.class);
                i.putExtra("collection", "osusthota");
                startActivity(i);
            }
        });
        binding.salat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DuaListActivity.class);
                i.putExtra("collection", "salat");
                startActivity(i);
            }
        });
        binding.khaddo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DuaListActivity.class);
                i.putExtra("collection", "khaddo");
                startActivity(i);
            }
        });
        binding.doinondinJibon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DuaListActivity.class);
                i.putExtra("collection", "doinondin");
                startActivity(i);
            }
        });
        binding.sokalSondha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DuaListActivity.class);
                i.putExtra("collection", "sokalsondha");
                startActivity(i);
            }
        });
        binding.koranTheke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DuaListActivity.class);
                i.putExtra("collection", "korantheke");
                startActivity(i);
            }
        });
        binding.bibidh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DuaListActivity.class);
                i.putExtra("collection", "bibidh");
                startActivity(i);
            }
        });
    }
}