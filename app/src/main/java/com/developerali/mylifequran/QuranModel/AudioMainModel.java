package com.developerali.mylifequran.QuranModel;

import java.util.List;

public class AudioMainModel {

    String number, name, englishName, englishNameTranslation, revelationType, numberOfAyahs;
    List<Ayah> ayahs;
    Edition edition;

    public AudioMainModel(String number, String name, String englishName, String englishNameTranslation,
                          String revelationType, String numberOfAyahs, List<Ayah> ayahs, Edition edition) {
        this.number = number;
        this.name = name;
        this.englishName = englishName;
        this.englishNameTranslation = englishNameTranslation;
        this.revelationType = revelationType;
        this.numberOfAyahs = numberOfAyahs;
        this.ayahs = ayahs;
        this.edition = edition;
    }

    public AudioMainModel() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getEnglishNameTranslation() {
        return englishNameTranslation;
    }

    public void setEnglishNameTranslation(String englishNameTranslation) {
        this.englishNameTranslation = englishNameTranslation;
    }

    public String getRevelationType() {
        return revelationType;
    }

    public void setRevelationType(String revelationType) {
        this.revelationType = revelationType;
    }

    public String getNumberOfAyahs() {
        return numberOfAyahs;
    }

    public void setNumberOfAyahs(String numberOfAyahs) {
        this.numberOfAyahs = numberOfAyahs;
    }

    public List<Ayah> getAyahs() {
        return ayahs;
    }

    public void setAyahs(List<Ayah> ayahs) {
        this.ayahs = ayahs;
    }

    public Edition getEdition() {
        return edition;
    }

    public void setEdition(Edition edition) {
        this.edition = edition;
    }
}
