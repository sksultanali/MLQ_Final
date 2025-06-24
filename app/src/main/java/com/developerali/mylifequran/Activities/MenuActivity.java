package com.developerali.mylifequran.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developerali.mylifequran.DonateActivity;
import com.developerali.mylifequran.Helper;
import com.developerali.mylifequran.JoinCommunity;
import com.developerali.mylifequran.LeaderBoard;
import com.developerali.mylifequran.LoginActivity;
import com.developerali.mylifequran.Models.ToolsModel;
import com.developerali.mylifequran.ProfileActivity;
import com.developerali.mylifequran.QuranActivities.OfflineQuranActivity;
import com.developerali.mylifequran.QuranActivities.TasbiActivity;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.WebViewActivity;
import com.developerali.mylifequran.databinding.ActivityMenuBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    ActivityMenuBinding binding;
    ArrayList<ToolsModel> arrayList = new ArrayList<>();
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Menu Options");
        auth = FirebaseAuth.getInstance();

        arrayList.add(new ToolsModel("প্রোফাইল", getDrawable(R.drawable.man)));//0
        arrayList.add(new ToolsModel("কুইজ চ্যাম্পিয়ন", getDrawable(R.drawable.tro)));//1
        arrayList.add(new ToolsModel("আরবি ক্যালেন্ডার", getDrawable(R.drawable.timetable)));//2
        arrayList.add(new ToolsModel("হিফজ কোরআন", getDrawable(R.drawable.quranlogo)));//3
        arrayList.add(new ToolsModel("ফ্রি বই", getDrawable(R.drawable.books)));//4
        arrayList.add(new ToolsModel("তাসবিহি", getDrawable(R.drawable.tasbih)));//5
        arrayList.add(new ToolsModel("আল্লাহর ৯৯টি নাম", getDrawable(R.drawable.allah)));//6
        arrayList.add(new ToolsModel("কিবলা কম্পাস", getDrawable(R.drawable.direction)));//7
        arrayList.add(new ToolsModel("নিজের প্রশ্ন জিজ্ঞেস করুন", getDrawable(R.drawable.ques)));//8

        arrayList.add(new ToolsModel("আর্থিক অনুদান দিন", getDrawable(R.drawable.monecurrency)));//9
        arrayList.add(new ToolsModel("Facebook", getDrawable(R.drawable.fblogo)));//10
        arrayList.add(new ToolsModel("Whatsapp", getDrawable(R.drawable.whatsapp)));//11
        arrayList.add(new ToolsModel("YouTube", getDrawable(R.drawable.youtube)));//12
        arrayList.add(new ToolsModel("Our Website", getDrawable(R.drawable.wesite)));//13
        arrayList.add(new ToolsModel("ডেভেলপারের সাথে কথা বলুন", getDrawable(R.drawable.developer_mode_24)));//14

        myListAdapter adapter = new myListAdapter();
        binding.toolsList.setAdapter(adapter);


        binding.toolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(MenuActivity.this, ProfileActivity.class));
                        break;
                    case 1:
                        if (auth.getCurrentUser() != null) {
                            startActivity(new Intent(MenuActivity.this, LeaderBoard.class));
                        }else {
                            Snackbar snackbar = Snackbar.make(binding.consLayout, "Please Login...", Snackbar.LENGTH_LONG)
                                    .setAction("Login", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                                            startActivity(i);
                                        }
                                    });
                            snackbar.show();
                        }
                        break;
                    case 2:
                        startActivity(new Intent(MenuActivity.this, DetailCalenderActivity.class));
                        break;
                    case 3:
                        //startActivity(new Intent(MenuActivity.this, OfflineQuranActivity.class));
                        Helper.showAlertNoAction(MenuActivity.this, "Work in process",
                                "We are working hard to give you this facility. Please be waited for our next step!",
                                "okay");
                        break;
                    case 4:
                        startActivity(new Intent(MenuActivity.this, BooksActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(MenuActivity.this, TasbiActivity.class));
                        break;
                    case 6:
                        //99 names
                        startActivity(new Intent(MenuActivity.this, NamesActivity.class));
                        break;
                    case 7:
                        //startActivity(new Intent(MenuActivity.this, QiblaCompasActivity.class));
                        Helper.showAlertNoAction(MenuActivity.this, "Work in process",
                                "We are working hard to give you this facility. Please be waited for our next step!",
                                "okay");
                        break;
                    case 8:
                        startActivity(new Intent(MenuActivity.this, JoinCommunity.class));
                        break;
                    case 9:
                        startActivity(new Intent(MenuActivity.this, DonateActivity.class));
                        break;
                    case 10:
                        if (Helper.isChromeCustomTabsSupported(MenuActivity.this)){
                            Helper.openChromeTab("https://www.facebook.com/MyLifeQuran112", MenuActivity.this);
                        }else {
                            String url = "https://www.facebook.com/MyLifeQuran112";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        }
                        break;
                    case 11:
                        String url2 = "https://chat.whatsapp.com/G4L2od0SE75JlnGGJQSzYt";
                        Uri uri = Uri.parse(url2);
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                        intent2.setPackage("com.android.chrome"); // Specify Chrome's package name
                        startActivity(intent2);
                        break;
                    case 12:
                        if (Helper.isChromeCustomTabsSupported(MenuActivity.this)){
                            Helper.openChromeTab("https://www.youtube.com/@MyLifeQuran", MenuActivity.this);
                        }else {
                            String url3 = "https://www.youtube.com/@MyLifeQuran";
                            Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url3));
                            startActivity(intent3);
                        }
                        break;
                    case 13:
                        if (Helper.isChromeCustomTabsSupported(MenuActivity.this)){
                            Helper.openChromeTab("https://mylifequran1.blogspot.com/", MenuActivity.this);
                        }else {
                            String url6 = "https://mylifequran1.blogspot.com/";
                            Intent intent6 = new Intent(Intent.ACTION_VIEW, Uri.parse(url6));
                            startActivity(intent6);
                        }
                        break;
                    case 14:
                        String message = "Assalamu alaikum. I am using My Life Quran App. Should we talk now?";
                        Intent intent4 = new Intent(Intent.ACTION_VIEW);
                        intent4.setData(Uri.parse("http://api.whatsapp.com/send?phone=+918967254087&text=" + message));
                        startActivity(intent4);
                        break;

                }
            }
        });





    }

    public class myListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater obj = getLayoutInflater();
            View view1 = obj.inflate(R.layout.sample_menu, null);
            ImageView imageView = view1.findViewById(R.id.toolImg);
            TextView textView = view1.findViewById(R.id.toolName);

            ToolsModel toolsModel = arrayList.get(i);
            textView.setText(toolsModel.getName());
            imageView.setImageDrawable(toolsModel.getDrawable());

            return view1;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}