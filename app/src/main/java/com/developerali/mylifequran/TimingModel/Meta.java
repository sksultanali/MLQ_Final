package com.developerali.mylifequran.TimingModel;

public class Meta {

    private double latitude;
    private double longitude;
    private String timezone;
    private CalculationMethod method;
    private String latitudeAdjustmentMethod;
    private String midnightMode;
    private String school;
    private Offset offset;

    public Meta(double latitude, double longitude, String timezone,
                CalculationMethod method, String latitudeAdjustmentMethod, String midnightMode, String school, Offset offset) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.method = method;
        this.latitudeAdjustmentMethod = latitudeAdjustmentMethod;
        this.midnightMode = midnightMode;
        this.school = school;
        this.offset = offset;
    }

    public Meta() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public CalculationMethod getMethod() {
        return method;
    }

    public void setMethod(CalculationMethod method) {
        this.method = method;
    }

    public String getLatitudeAdjustmentMethod() {
        return latitudeAdjustmentMethod;
    }

    public void setLatitudeAdjustmentMethod(String latitudeAdjustmentMethod) {
        this.latitudeAdjustmentMethod = latitudeAdjustmentMethod;
    }

    public String getMidnightMode() {
        return midnightMode;
    }

    public void setMidnightMode(String midnightMode) {
        this.midnightMode = midnightMode;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Offset getOffset() {
        return offset;
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }
}
