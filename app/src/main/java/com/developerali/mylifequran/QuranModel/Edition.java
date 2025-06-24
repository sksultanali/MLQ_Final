package com.developerali.mylifequran.QuranModel;

public class Edition {

    String identifier, language, name, englishName, format, type, direction;

    public Edition(String identifier, String language, String name, String englishName, String format, String type, String direction) {
        this.identifier = identifier;
        this.language = language;
        this.name = name;
        this.englishName = englishName;
        this.format = format;
        this.type = type;
        this.direction = direction;
    }

    public Edition() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
