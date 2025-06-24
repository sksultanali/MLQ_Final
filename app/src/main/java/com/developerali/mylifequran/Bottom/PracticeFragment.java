package com.developerali.mylifequran.Bottom;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.developerali.mylifequran.Models.CategoryAdapter;
import com.developerali.mylifequran.Models.CategoryModel;
import com.developerali.mylifequran.Models.viewFlipperModel;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.databinding.FragmentPracticeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.ibrahimsn.lib.SmoothBottomBar;

public class PracticeFragment extends Fragment {

    FragmentPracticeBinding binding;
    FirebaseFirestore database;
    FirebaseDatabase data;
    ArrayList<CategoryModel> aList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPracticeBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        data = FirebaseDatabase.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SmoothBottomBar bottomBar = getActivity().findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(4);

        Animation anim_in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        Animation anim_out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);


        data.getReference().child("viewflipers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewFlipperModel vm = snapshot.getValue(viewFlipperModel.class);
                binding.textA.setText(vm.getPracticeText1());
                binding.textB.setText(vm.getPracticeText2());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.viewFlipper.setInAnimation(anim_in);
        binding.viewFlipper.setOutAnimation(anim_out);
        binding.viewFlipper.setFlipInterval(5000);
        binding.viewFlipper.startFlipping();


        //ImageSlider
        final ArrayList<SlideModel> slideModels = new ArrayList<>();

        data.getReference().child("practice")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            slideModels.clear();

                            for (DataSnapshot ds:snapshot.getChildren()){

                                slideModels.add(new SlideModel(ds.child("imageUrl").getValue(String.class), ScaleTypes.CENTER_CROP));
                                binding.imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        if (slideModels.isEmpty() || !isConnectedNetwork(getActivity())){
            slideModels.add(new SlideModel(R.drawable.bannera, ScaleTypes.CENTER_CROP));
            slideModels.add(new SlideModel(R.drawable.bannerb, ScaleTypes.CENTER_CROP));
            binding.imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        }


        binding.categoryRec.showShimmerAdapter();
        database.collection("categories").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            CategoryModel ob = d.toObject(CategoryModel.class);
                            aList.add(ob);
                        }

                        Collections.reverse(aList);
                        CategoryAdapter adapter = new CategoryAdapter(getContext(), aList);
                        GridLayoutManager gnm = new GridLayoutManager(getContext(), 2);

                        binding.categoryRec.setLayoutManager(gnm);
                        binding.categoryRec.setAdapter(adapter);
                        binding.categoryRec.hideShimmerAdapter();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public static boolean isConnectedNetwork (Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}