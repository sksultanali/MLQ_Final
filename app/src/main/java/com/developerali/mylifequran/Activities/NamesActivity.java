package com.developerali.mylifequran.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.developerali.mylifequran.Adapters.NamesAdapter;
import com.developerali.mylifequran.LeaderBoard;
import com.developerali.mylifequran.Models.NamesModel;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.databinding.ActivityNamesBinding;

import java.util.ArrayList;

public class NamesActivity extends AppCompatActivity {

    ActivityNamesBinding binding;
    ArrayList<NamesModel> namesList = new ArrayList<>();
    NamesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNamesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("99 Names of Allah");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager lnm = new LinearLayoutManager(NamesActivity.this);
        lnm.setOrientation(RecyclerView.VERTICAL);
        binding.leaderRec.setLayoutManager(lnm);
        binding.leaderRec.showShimmerAdapter();

        //namesList.clear();
        namesList.add(new NamesModel("اَلرَّحْمٰن", "আর রাহমান",  "পরম দয়ালু"));
        namesList.add(new NamesModel("اَلرَّحِيْم", "আর-রাহ়ীম",  "অতিশয়-মেহেরবান"));
        namesList.add(new NamesModel("اَلْمَلِك", "আল-মালিক	", "সর্বকর্তৃত্বময়"));
        namesList.add(new NamesModel("اَلْقُدُّوْس", "আল-কুদ্দুস", "নিষ্কলুষ, অতি পবিত্র"));
        namesList.add(new NamesModel("اَلسَّلاَم", "আস-সালাম", "নিরাপত্তা-দানকারী, শান্তি-দানকারী"));
        namesList.add(new NamesModel("ُاَلْمُؤْمِن", "আল-মু'মিন", "নিরাপত্তা-বিধায়ক"));
        namesList.add(new NamesModel("اَلْمُهَيْمِن", "আল-মুহাইমিন", "পরিপূর্ণ রক্ষণাবেক্ষণকারী"));
        namesList.add(new NamesModel("ُاَلْعَزِيْز", "আল-আ'জীজ", "পরাক্রমশালী, অপরাজেয়"));
        namesList.add(new NamesModel("ُاَلْجَبَّار", "আল-জাব্বার", "মহা প্রতাপশালী"));
        namesList.add(new NamesModel("ُاَلْمُتَكَبِّر", "আল-মুতাকাব্বির", "নিরঙ্কুশ শ্রেষ্ঠত্বের অধিকারী"));
        namesList.add(new NamesModel("ُاَلْخَالِق", "আল-খালিক্ব", "সৃষ্টিকর্তা"));
        namesList.add(new NamesModel("ُاَلْبَارِئ", "আল-বারী", "নমুনাবিহীন সৃষ্টিকারী"));
        namesList.add(new NamesModel("ُاَلْمُصَوِّر", "আল-মুছউইর", "আকৃতি-দানকারী"));
        namesList.add(new NamesModel("ُاَلْغَفَّار", "আল-গফ্ফার", "পরম ক্ষমাশীল"));
        namesList.add(new NamesModel("ُاَلْقَهَّار", "আল-ক্বাহার", "কঠোর"));
        namesList.add(new NamesModel("ُاَلْوَهَّاب", "আল-ওয়াহ্হাব", "সবকিছু দানকারী"));
        namesList.add(new NamesModel("ُاَلْرَّزَّاق", "আর-রজ্জাক্ব", "রিযিকদাতা"));
        namesList.add(new NamesModel("ُاَلْفَتَّاح", "আল ফাত্তাহ", "বিজয়দানকারী"));
        namesList.add(new NamesModel("ُاَلْعَلِيْم", "আল-আ'লীম", "সর্বজ্ঞ"));
        namesList.add(new NamesModel("ُاَلْقَابِض", "আল-ক্ববিদ্ব", "নিয়ন্ত্রণকারী, সরল পথ প্রদর্শনকারী"));
        namesList.add(new NamesModel("ُاَلْبَاسِط", "আল-বাসিত", "প্রশস্তকারী"));
        namesList.add(new NamesModel("ُاَلْخَافِض", "আল-খফিদ্বু", "অবনতকারী"));
        namesList.add(new NamesModel("ُاَلرَّافِع", "আর-রফীই", "উন্নতকারী"));
        namesList.add(new NamesModel("اَلْمُعِزُّ", "আল-মুই'জ্ব", "সম্মান-দানকারী"));
        namesList.add(new NamesModel("اَلْمُذِلُّ", "আল-মুদ্বি'ল্লু", "(অবিশ্বাসীদের) বেইজ্জতকারী"));
        namesList.add(new NamesModel("ُاَلسَّمِيْع", "আস্-সামিউ", "সর্বশ্রোতা"));
        namesList.add(new NamesModel("ُاَلْبَصِيْر", "আল-বাছীর", "সর্ববিষয়-দর্শনকারী"));
        namesList.add(new NamesModel("ُاَلْحَكَم", "আল-হা'কাম", "অটল বিচারক"));
        namesList.add(new NamesModel("ُاَلْعَدْل", "আল-আ'দল", "পরিপূর্ণ-ন্যায়বিচারক"));
        namesList.add(new NamesModel("ُاَللَّطِيْف", "আল-লাতীফ", "সকল-গোপন-বিষয়ে-অবগত"));
        namesList.add(new NamesModel("ُاَلْخَبِيْر", "আল-খ'বীর", "সকল ব্যাপারে জ্ঞাত"));
        namesList.add(new NamesModel("اَلْحَلِيْم", "আল-হা'লীম", "অত্যন্ত ধৈর্যশীল"));
        namesList.add(new NamesModel("ُاَلْعَظِيْم", "আল-আ'জীম", "সর্বোচ্চ-মর্যাদাশীল"));
        namesList.add(new NamesModel("ُاَلْغَفُوْر", "আল-গফুর", "পরম ক্ষমাশীল"));
        namesList.add(new NamesModel("اَلْشَّكُوْر", "আশ্-শাকুর", "গুনগ্রাহী"));
        namesList.add(new NamesModel("اَلْعَلِيُّ", "আল-আ'লিইউ", "উচ্চ-মর্যাদাশীল"));
        namesList.add(new NamesModel("اَلْكَبِيْر", "আল-কাবির", "সুমহান"));
        namesList.add(new NamesModel("ُاَلْحَفِيْظ", "আল-হা'ফীজ	", "সংরক্ষণকারী"));
        namesList.add(new NamesModel("ُاَلْمُقِيْت", "আল-মুক্বীত", "সকলের জীবনোপকরণ-দানকারী"));
        namesList.add(new NamesModel("ُاَلْحَسِيْب", "আল-হাসীব", "হিসাব-গ্রহণকারী"));
        namesList.add(new NamesModel("ُاَلْجَلِيْل", "আল-জালীল", "পরম মর্যাদার অধিকারী"));
        namesList.add(new NamesModel("ُاَلْكَرِيْم", "আল-কারীম", "সুমহান দাতা"));
        namesList.add(new NamesModel("ُاَلْرَّقِيْب", "আর-রক্বীব", "তত্ত্বাবধায়ক"));
        namesList.add(new NamesModel("ُاَلْمُجِيْب", "আল-মুজীব", "জবাব-দানকারী, কবুলকারী"));
        namesList.add(new NamesModel("ُاَلْوَاسِع", "আল-ওয়াসিউ", "সর্ব-ব্যাপী, সর্বত্র-বিরাজমান"));
        namesList.add(new NamesModel("ُاَلْحِكِيْم", "আল-হাকীম", "পরম-প্রজ্ঞাময়"));
        namesList.add(new NamesModel("ُاَلْوَدُوْد", "আল-ওয়াদুদ", "(বান্দাদের প্রতি) সদয়"));
        namesList.add(new NamesModel("ُاَلْمَجِيْد", "আল-মাজীদ", "সকল-মর্যাদার-অধিকারী"));
        namesList.add(new NamesModel("ُاَلْبَاعِث", "আল-বাই'ছ", "পুনুরুজ্জীবিতকারী"));
        namesList.add(new NamesModel("ُاَلشَّهِيْد", "আশ্-শাহীদ", "সর্বজ্ঞ-স্বাক্ষী"));
        namesList.add(new NamesModel("‎‎اَلْحَقُّ", "আল-হা'ক্ব", "পরম সত্য"));
        namesList.add(new NamesModel("ُاَلْوَكِيْل", "আল-ওয়াকিল", "পরম নির্ভরযোগ্য কর্ম-সম্পাদনকারী"));
        namesList.add(new NamesModel("اَلْقَوِيُّ", "আল-ক্বউইউ", "পরম-শক্তির-অধিকারী"));
        namesList.add(new NamesModel("ُاَلْمَتِيْن", "আল-মাতীন", "সুদৃঢ়"));
        namesList.add(new NamesModel("اَلْوَلِيُّ", "আল-ওয়ালিইউ", "অভিভাবক ও সাহায্যকারী"));
        namesList.add(new NamesModel("ُاَلْحَمِيْد", "আল-হা'মীদ", "সকল প্রশংসার অধিকারী"));
        namesList.add(new NamesModel("ْاَلْمُحْصِي", "আল-মুহছী", "গণনা কারী"));
        namesList.add(new NamesModel("ُاَلْمُبْدِئ", "আল-মুব্দিউ", "প্রথমবার-সৃষ্টিকর্তা"));
        namesList.add(new NamesModel("ُاَلْمُعِيْد", "আল-মুঈ'দ", "পুনরায়-সৃষ্টিকর্তা"));
        namesList.add(new NamesModel("ْاَلْمُحْيِي", "আল-মুহ'য়ী", "জীবন-দানকারী"));
        namesList.add(new NamesModel("ُاَلْمُمِيْت", "আল-মুমীত", "মৃত্যু-দানকারী"));
        namesList.add(new NamesModel("اَلْحَيُّ", "আল-হাইয়্যু", "চিরঞ্জীব"));
        namesList.add(new NamesModel("ُاَلْقَيُّوْم", "আল-ক্বাইয়্যুম", "সমস্তকিছুর ধারক ও সংরক্ষণকারী"));
        namesList.add(new NamesModel("ُاَلْوَاجِد", "আল-ওয়াজিদ", "অফুরন্ত ভান্ডারের অধিকারী"));
        namesList.add(new NamesModel("ُاَلْمَاجِد", "আল-মাজিদ", "শ্রেষ্ঠত্বের অধিকারী"));
        namesList.add(new NamesModel("ُاَلْوَاحِد", "আল-ওয়াহি'দ", "এক ও অদ্বিতীয়"));
        namesList.add(new NamesModel("ُاَلْاَحَد", "আল আহাদ", "এক"));
        namesList.add(new NamesModel("ُاَلصَّمَد", "আছ্-ছমাদ", "অমুখাপেক্ষী"));
        namesList.add(new NamesModel("ُاَلْقَادِر", "আল-ক্বদির", "সর্বশক্তিমান"));
        namesList.add(new NamesModel("ُاَلْمُقْتَدِر", "আল-মুক্ব্তাদির", "নিরঙ্কুশ-সিদ্বান্তের-অধিকারী"));
        namesList.add(new NamesModel("ُاَلْمُقَدِّم", "আল-মুক্বদ্দিম", "ত্বরান্বিতকারী"));
        namesList.add(new NamesModel("ُاَلْمُؤَخِّر", "আল-মুয়াখখির", "অবকাশ দানকারী"));
        namesList.add(new NamesModel("ُاَلْأَوَّل", "আল-আউয়াল", "অনাদি"));
        namesList.add(new NamesModel("ُاَلْأٰخِر", "আল-আখির", "অনন্ত, সর্বশেষ"));
        namesList.add(new NamesModel("ُاَلْظَّاهِر", "আজ-জ'হির", "সম্পূর্নরূপে-প্রকাশিত"));
        namesList.add(new NamesModel("ُاَلْبَاطِن", "আল-বাত্বিন", "দৃষ্টি হতে অদৃশ্য"));
        namesList.add(new NamesModel("ْاَلْوَالِي", "আল-ওয়ালি", "সমস্ত-কিছুর-অভিভাবক"));
        namesList.add(new NamesModel("ْاَلْمُتَعَالِي", "আল-মুতাআ'লি", "সৃষ্টির গুনাবলীর উর্দ্ধে"));
        namesList.add(new NamesModel("اَلْبَرُّ", "আল-বার্", "পরম-উপকারী, অণুগ্রহশীল"));
        namesList.add(new NamesModel("ُاَلْتَّوَّاب", "আত্-তাওয়াবু", "তাওফিক দানকারী এবং কবুলকারী	"));
        namesList.add(new NamesModel("ُاَلْمُنْتَقِم", "আল-মুনতাক্বিম", "প্রতিশোধ-গ্রহণকারী"));
        namesList.add(new NamesModel("اَلْعَفُوُّ", "আল-আ'ফঊ", "পরম-উদার"));
        namesList.add(new NamesModel("ُاَلْرَّؤُوْف", "আর-রউফ", "পরম-স্নেহশীল"));
        namesList.add(new NamesModel("ِمَالِكُ الْمُلْك", "মালিকুল-মুলক", "সমগ্র জগতের বাদশাহ্"));
        namesList.add(new NamesModel("ُذُوْ الْجَلَالِ وَالْإِكْرَام", "যুল-জালালি-ওয়াল-ইকরাম", "মহিমান্বিত ও দয়াবান সত্তা"));
        namesList.add(new NamesModel("ُاَلْمُقْسِط", "আল-মুক্ব্সিত", "হকদারের হক-আদায়কারী"));
        namesList.add(new NamesModel("ُاَلْجَامِع", "আল-জামিই", "একত্রকারী, সমবেতকারী"));
        namesList.add(new NamesModel("اَلْغَنِيُّ", "আল-গণি", "মহাধনী"));
        namesList.add(new NamesModel("ْاَلْمُغْنِي", "আল-মুগণিই", "পরম-অভাবমোচনকারী"));
        namesList.add(new NamesModel("ُاَلْمَانِع", "আল-মানিই", "অকল্যাণ প্রতিরোধকারী"));
        namesList.add(new NamesModel("اَلْضَّارُّ", "আয্-যর", "ক্ষতিসাধনকারী"));
        namesList.add(new NamesModel("ُاَلنَّافَع", "আন্-নাফিই", "কল্যাণকারী"));
        namesList.add(new NamesModel("ُاَلنُّوْر", "আন্-নূর", "পরম-আলো"));
        namesList.add(new NamesModel("ْاَلْهَادِي", "আল হাদী", "পথ-প্রদর্শক"));
        namesList.add(new NamesModel("ُاَلْبَدِيْع", "আল-বাদীই", "অতুলনীয়"));
        namesList.add(new NamesModel("ْاَلْبَاقِي", "আল-বাক্বী", "চিরস্থায়ী, অবিনশ্বর"));
        namesList.add(new NamesModel("ُاَلْوَارِث", "আল-ওয়ারিস", "উত্তরাধিকারী"));
        namesList.add(new NamesModel("ُاَلرَّشِيْد", "আর-রাশীদ", "সঠিক পথ-প্রদর্শক"));
        namesList.add(new NamesModel("ُاَلصَّبُوْر", "আস-সবুর", "অত্যধিক ধৈর্যধারণকারী"));

        adapter = new NamesAdapter(NamesActivity.this, namesList);
        binding.leaderRec.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.leaderRec.hideShimmerAdapter();



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}