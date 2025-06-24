package com.developerali.mylifequran.TimingModel;

public class Designation {

    private String abbreviated;
    private String expanded;

    public Designation(String abbreviated, String expanded) {
        this.abbreviated = abbreviated;
        this.expanded = expanded;
    }

    public Designation() {
    }

    public String getAbbreviated() {
        return abbreviated;
    }

    public void setAbbreviated(String abbreviated) {
        this.abbreviated = abbreviated;
    }

    public String getExpanded() {
        return expanded;
    }

    public void setExpanded(String expanded) {
        this.expanded = expanded;
    }
}
