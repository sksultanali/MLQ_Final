package com.developerali.mylifequran;

import com.developerali.mylifequran.CalenderModels.AdvanceDateResponse;
import com.developerali.mylifequran.CalenderModels.DateAdvanceData;
import com.developerali.mylifequran.CalenderModels.DateResponse;
import com.developerali.mylifequran.CalenderModels.SingleDateResponse;
import com.developerali.mylifequran.Models.QuestionsResponse;
import com.developerali.mylifequran.QuranModel.DetailedRespons;
import com.developerali.mylifequran.QuranModel.JSONResponse;
import com.developerali.mylifequran.QuranModel.JSRes;
import com.developerali.mylifequran.QuranModel.blogsModel;
import com.developerali.mylifequran.TimingModel.PrayerTimes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apiset {

    @GET("/v1/surah")
    Call<JSONResponse> getNames();

    @GET("/v1/surah/{id}/ar.alafasy")
    Call<JSRes> getAudios(@Path("id") String id);

    @GET("/v1/surah/{id}/bn.bengali")
    Call<DetailedRespons> getMeaning(@Path("id") String id);

    @GET("timings/17-07-2007?latitude=51.508515&longitude=-0.1254872&method=2")
    Call<PrayerTimes> getTimings();

    @GET("timings/{date}")
    Call<PrayerTimes> getPrayerTimes(
            @Path("date") String date,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("method") int method,
            @Query("school") int school
    );

    @GET("blogger/v3/blogs/{id}/posts/{blog}")
    Call<blogsModel> getImages(
            @Path("id") String id,
            @Path("blog") String blog,
            @Query("key") String key
    );

    //getting arbi calender through api calling
    @GET("v1/hToGCalendar/{month}/{year}")
    Call<DateResponse> getHijriCalendar(
            @Path("month") int month,
            @Path("year") int year
    );

    @GET("v1/calendar/{year}/{month}")
    Call<AdvanceDateResponse> getAdvanceHijriCalendar(
            @Path("month") int month,
            @Path("year") int year,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("method") int method,
            @Query("school") int school
    );

    @GET("v1/gToHCalendar/{month}/{year}")
    Call<DateResponse> getEngCalendar(
            @Path("month") int month,
            @Path("year") int year
    );

    @GET("v1/gToH/{date}")
    Call<SingleDateResponse> getDate(
            @Path("date") String date
    );

//    @GET("exec?action=read")
//    Call<QuestionsResponse> getQuestions();

    @GET("{extension}/{code}")
    Call<QuestionsResponse> getQuestions(
            @Path("extension") String extension,
            @Path("code") String code
    );
}
