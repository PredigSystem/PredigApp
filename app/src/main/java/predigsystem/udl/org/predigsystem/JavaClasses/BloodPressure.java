package predigsystem.udl.org.predigsystem.JavaClasses;

import java.sql.Date;
import java.util.UUID;

public class BloodPressure {
    public String userId;

    public Date date;
    public Double latitude;
    public Double longitude;

    public Double systolic;
    public Double diastolic;
    public Integer pulse;

    public BloodPressure(){

    }

    public BloodPressure(String userId, Date date, Double latitude, Double longitude, Double systolic, Double diastolic, Integer pulse) {
        this.userId = userId;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulse = pulse;
    }

    public String generateId(){
        //random ID Generator
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSystolic() {
        return systolic;
    }

    public void setSystolic(Double systolic) {
        this.systolic = systolic;
    }

    public Double getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Double diastolic) {
        this.diastolic = diastolic;
    }

    public Integer getPulse() {
        return pulse;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    @Override
    public String toString() {
        return "BloodPressure{" +
                "userId='" + userId + '\'' +
                ", date=" + date +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", systolic=" + systolic +
                ", diastolic=" + diastolic +
                ", pulse=" + pulse +
                '}';
    }
}