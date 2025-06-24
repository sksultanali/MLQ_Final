package com.developerali.mylifequran.QuranActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.developerali.mylifequran.Blogger.BloggerAdapter;
import com.developerali.mylifequran.Blogger.BloggerModel;
import com.developerali.mylifequran.Blogger.Constants;
import com.developerali.mylifequran.ProfileActivity;
import com.developerali.mylifequran.QuranModel.blogsModel;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.apiset;
import com.developerali.mylifequran.databinding.ActivityOfflineQuranBinding;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OfflineQuranActivity extends AppCompatActivity{

    ActivityOfflineQuranBinding binding;
    int no[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    int page[] = {1, 23, 43, 63, 83, 103, 123, 143, 163, 183, 203, 223, 243, 263, 283, 303, 323, 343, 363,
            383, 403, 423, 443, 463, 483, 503, 523, 543, 563, 587};

    private ProgressDialog progressDialog;
    private static final String TAG = "MAIN_TAG";
    ArrayList<String> arrayList;
    int Page;

    String Links[] = {"8042741384193751317", "6940441350510551234", "5627669558656117427", "8089848623283499122",
            "8884179690616281677", "5686261370365604047", "2969216841521608503", "1941095038808052038",
            "277916777044492113", "6231966263841227083", "5448841720411486295", "6234195277978148172",
            "285831137926080248", "", "", "",


    };

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfflineQuranBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gestureDetector = new GestureDetector(this, new MyGestureListener());

        progressDialog = new ProgressDialog(OfflineQuranActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        arrayList = new ArrayList<>();
        SultanAdapter obj = new SultanAdapter();
        binding.listItem.setAdapter(obj);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiset prayerTimesApi = retrofit.create(apiset.class);
        
        binding.listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                progressDialog.show();
                prayerTimesApi.getImages(Constants.Blog_Id+"", "8042741384193751317", Constants.API_Key+"")
                        .enqueue(new Callback<blogsModel>() {
                            @Override
                            public void onResponse(Call<blogsModel> call, retrofit2.Response<blogsModel> response) {
                                blogsModel blogsModel = response.body();
                                arrayList.clear();
                                Document document = Jsoup.parse(blogsModel.content);
                                Elements elements = document.select("img");
                                for (int i = 0; i < elements.size(); i++){
                                    arrayList.add(String.valueOf(elements.get(i).attr("src")));
                                }
                                setNextPhoto();
                                progressDialog.dismiss();
                                binding.pdfContainer.setVisibility(View.VISIBLE);
                                binding.listItem.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<blogsModel> call, Throwable t) {
                                Toast.makeText(OfflineQuranActivity.this, "server down...", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

            }
        });

        binding.prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Page < arrayList.size()){
                    Page = Page + 1;
                    setNextPhoto();
                    binding.pdfContainer.setVisibility(View.VISIBLE);
                    binding.listItem.setVisibility(View.GONE);
                }else {
                    binding.caption.setText("This is ending page");
                    binding.caption.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Page > 0){
                    Page = Page - 1;
                    setNextPhoto();
                    binding.pdfContainer.setVisibility(View.VISIBLE);
                    binding.listItem.setVisibility(View.GONE);
                }else {
                    binding.caption.setText("This is starting page");
                    binding.caption.setTextColor(getResources().getColor(R.color.blue_700));
                }
            }
        });

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pdfContainer.setVisibility(View.GONE);
                binding.listItem.setVisibility(View.VISIBLE);
            }
        });
    }

    void setNextPhoto() {
        if (Page < arrayList.size()){
            Glide.with(OfflineQuranActivity.this)
                    .load(arrayList.get(Page))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(getResources().getDrawable(R.drawable.wait))
                    .into(binding.imageViewer);
            int setPage = Page + 1;
            binding.pageNo.setText(setPage + "/" + arrayList.size());
            binding.caption.setText("");
        }else {
            binding.caption.setText("COMPLETED");
            binding.caption.setTextColor(getResources().getColor(R.color.greenDark));
        }
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
        if (Page < arrayList.size()){
            Page = Page + 1;
            setNextPhoto();
        }else {
            binding.caption.setText("This is end page");
            binding.caption.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void onSwipeLeft() {
        if (Page > 0){
            Page = Page - 1;
            setNextPhoto();
        }else {
            binding.caption.setText("This is starting page");
            binding.caption.setTextColor(getResources().getColor(R.color.blue_700));
        }
    }

    class SultanAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return no.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater obj = getLayoutInflater();
            View row = obj.inflate(R.layout.sample_surah_name, null);
            TextView ParaNo = row.findViewById(R.id.suraNo);
            TextView Title = row.findViewById(R.id.suraName);
            TextView Page = row.findViewById(R.id.suraAat);
            TextView Arbi = row.findViewById(R.id.nameArbi);

            ParaNo.setText(String.valueOf(no[position]));
            Title.setText("Para- " + String.valueOf(no[position]));
            Page.setText("Page Start- " + String.valueOf(page[position]));
            Arbi.setText("Read");
            return row;
        }
    }
}