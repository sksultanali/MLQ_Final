package com.developerali.mylifequran;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.developerali.mylifequran.databinding.CustomDialogBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper {
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }
    public static String formatDate2(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");
        return dateFormat.format(date);
    }

    public static String subIST(String text){
        return text.substring(0, text.length()-5);
    }


    //24 hrs format to 12 hrs format
    public static String getFormattedTime(String timeString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");

        try {
            Date time = inputFormat.parse(timeString);
            if (time != null) {
                return outputFormat.format(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    //add or less from the time given
    public static String addLessMin(String time, int value){
        SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
        try {
            Date timeDate = inputFormat.parse(time);
            Calendar calendar2 = Calendar.getInstance();

            calendar2.setTime(timeDate);
            calendar2.add(Calendar.MINUTE, value);

            return inputFormat.format(calendar2.getTime());

        } catch (ParseException e) {
            return null;
        }
    }

    public static String FormatIST(String string){
        return Helper.getFormattedTime(Helper.subIST(string));
    }

    public static String exitAmPm(String text){
        return text.substring(0, text.length()-2);
    }


    //calculating two time gaps
    public static long getTimeGap(String time1, String time2) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");

        try {
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);

            // Calculate the time difference in milliseconds
            return Math.abs(date2.getTime() - date1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Return -1 if parsing fails
        }
    }

    private static final Map<String, String> dayMappings = new HashMap<>();
    static {
        dayMappings.put("sunday", "রবিবার");
        dayMappings.put("monday", "সোমবার");
        dayMappings.put("tuesday", "মঙ্গলবার");
        dayMappings.put("wednesday", "বুধবার");
        dayMappings.put("thursday", "বৃহস্পতিবার");
        dayMappings.put("friday", "শুক্রবার");
        dayMappings.put("saturday", "শনিবার");
        dayMappings.put("romzan", "রমজান");
        dayMappings.put("poribar", "পরিবার");
        dayMappings.put("osusthota", "অসুস্থতা");
        dayMappings.put("salat", "সালাত");
        dayMappings.put("khaddo", "খাদ্য");
        dayMappings.put("doinondin", "দৈনন্দিন");
        dayMappings.put("sokalsondha", "সকালসন্ধে");

        dayMappings.put("korantheke", "কোরান থেকে");
        dayMappings.put("bibidh", "বিবিধ");
        dayMappings.put("zikir", "জিকির");
    }

    public static String englishToBengaliDay(String day) {
        return dayMappings.getOrDefault(day.toLowerCase(), "NA");
    }



    public static boolean isLocationEnabled(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGpsEnabled || isNetworkEnabled;
    }

    public static boolean isChromeCustomTabsSupported(@NonNull final Context context) {
        Intent serviceIntent = new Intent("android.support.customtabs.action.CustomTabsService");
        serviceIntent.setPackage("com.android.chrome");
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentServices(serviceIntent, 0);
        return !resolveInfos.isEmpty();
    }

//    public static void openChromeTab(String link, Activity activity) {
//        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.purple_500));
//
//        // Example of adding an action button with a PendingIntent
//        Intent actionIntent = new Intent(Intent.ACTION_SEND);
//        actionIntent.setType("text/plain");
//        actionIntent.putExtra(Intent.EXTRA_TEXT, link);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                activity,
//                0,
//                actionIntent,
//                PendingIntent.FLAG_IMMUTABLE // or PendingIntent.FLAG_MUTABLE if necessary
//        );
//
//        Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.ic_menu_share);
//        builder.setActionButton(icon, "Share", pendingIntent, false);
//
//        CustomTabsIntent customTabsIntent = builder.build();
//        customTabsIntent.launchUrl(activity, Uri.parse(link));
//    }

    public static void openChromeTab(String link, Activity activity){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.purple_500));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(link));
    }


    public static String convertToEndTime(String timeRange) {
        // 04:41 - 05:54 am to 05:54 am
        String[] parts = timeRange.split(" - ");
        String startTime = parts[1];
        return startTime;
    }

    public static String convertToStartTime(String timeRange) {
        // 04:41 - 05:54 am to 05:54 am
        String[] parts = timeRange.split(" - ");
        String startTime = parts[0] + " am";
        return startTime;
    }

    public static boolean isTimeInRange(String timeRange) {
        // Split the time range string into start and end time parts
        String[] parts = timeRange.split(" - ");
        if (parts.length != 1) {

        }

        // Parse start and end times with AM/PM designation
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.parse(parts[0].trim().toUpperCase(), formatter);
        LocalTime endTime = LocalTime.parse(parts[1].trim().toUpperCase(), formatter);

        // Check if the current time is after the start time and before the end time
        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
    }

    public static boolean isTimePassed(String timeString) {
        // Parse the provided time string with AM/PM designation
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime providedTime = LocalTime.parse(timeString.trim().toUpperCase(), formatter);
        LocalTime currentTime = LocalTime.now();

        // Check if the provided time is before the current time
        return providedTime.isBefore(currentTime);
    }

    @SuppressLint("ResourceAsColor")
    public static void showAlertNoAction(Activity activity, String title, String content, String yesText) {
        CustomDialogBinding dialogBinding = CustomDialogBinding.inflate(LayoutInflater.from(activity));
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogBinding.getRoot())
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

        dialogBinding.titleText.setText(title);
        dialogBinding.messageText.setText(Html.fromHtml(content));
        dialogBinding.yesBtnText.setText(yesText);

        dialogBinding.loginBtn.setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.show();
    }

    public static String arbiToBengaliMonth(String Day) {
        if (Day.equalsIgnoreCase("Jumādá al-ūlá")) {
            return "জামাদিউল উলা";
        }else if (Day.equalsIgnoreCase("Jumādá al-ākhirah")) {
            return "জামাদিউস সানি";
        }else if (Day.equalsIgnoreCase("Rabīʿ al-thānī")) {
            return "রবিউস সানী";
        }else if (Day.equalsIgnoreCase("Rabīʿ al-awwal")) {
            return "রবিউল আওয়াল";
        }else if (Day.equalsIgnoreCase("Ṣafar")) {
            return "সফর";
        }else if (Day.equalsIgnoreCase("Muḥarram")) {
            return "মুহররম";
        }else if (Day.equalsIgnoreCase("Dhū al-Ḥijjah")) {
            return "জিলহজ্ব";
        }else if (Day.equalsIgnoreCase("Dhū al-Qaʿdah")) {
            return "জিলকদ";
        }else if (Day.equalsIgnoreCase("Shawwāl")) {
            return "শাওয়াল";
        }else if (Day.equalsIgnoreCase("Ramaḍān")) {
            return "রমজানুল মুবারক";
        }else if (Day.equalsIgnoreCase("Shaʿbān")) {
            return "সাবান";
        }else if (Day.equalsIgnoreCase("Rajab")) {
            return "রজব";
        }






        else if (Day.equalsIgnoreCase("Al Juma'a")) {
            return "ইয়াওমুল জুমা";
        }else if (Day.equalsIgnoreCase("Al Sabt")) {
            return "ইয়াওমুল সাবত";
        }else if (Day.equalsIgnoreCase("Al Ahad")) {
            return "ইয়াওমুল আহাদ";
        }else if (Day.equalsIgnoreCase("Al Athnayn")) {
            return "ইয়াওমুল ইসনাইন";
        }else if (Day.equalsIgnoreCase("Al Thalaata")) {
            return "ইয়াওমূল সালিস";
        }else if (Day.equalsIgnoreCase("Al Arba'a")) {
            return "ইয়াওমুল রবি";
        }else if (Day.equalsIgnoreCase("Al Khamees")) {
            return "ইয়াওমুল খমিস";
        }else{
            return "NA";
        }
    }
}
