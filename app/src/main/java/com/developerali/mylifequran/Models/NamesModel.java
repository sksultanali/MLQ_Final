package com.developerali.mylifequran.Models;

public class NamesModel {

    String arabicName;
    String bengaliTranscription;
    String bengaliTranslation;

    public NamesModel(String arabicName, String bengaliTranscription, String bengaliTranslation) {
        this.arabicName = arabicName;
        this.bengaliTranscription = bengaliTranscription;
        this.bengaliTranslation = bengaliTranslation;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    public String getBengaliTranscription() {
        return bengaliTranscription;
    }

    public void setBengaliTranscription(String bengaliTranscription) {
        this.bengaliTranscription = bengaliTranscription;
    }

    public String getBengaliTranslation() {
        return bengaliTranslation;
    }

    public void setBengaliTranslation(String bengaliTranslation) {
        this.bengaliTranslation = bengaliTranslation;
    }
}
