package predigsystem.udl.org.predigsystem.JavaClasses;

import java.util.Date;

/**
 * Created by Pau on 30/10/17.
 */

public class BloodPressure {
    private Float systolic;
    private Float diastolic;
    private Date dateTaken;

    public BloodPressure(Float systolic, Float diastolic, Date dateTaken) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.dateTaken = dateTaken;
    }

    public Float getSystolic() {
        return systolic;
    }

    public void setSystolic(Float systolic) {
        this.systolic = systolic;
    }

    public Float getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Float diastolic) {
        this.diastolic = diastolic;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }
}
