package predigsystem.udl.org.predigsystem.JavaClasses;

import android.animation.TimeInterpolator;

/**
 * Created by Pau on 1/12/17.
 */

public class FAQ {
    public final String description;
    public final String answer;
    public final int colorId1;
    public final int colorId2;
    public final TimeInterpolator interpolator;

    public FAQ(String description, String answer, int colorId1, int colorId2, TimeInterpolator interpolator) {
        this.description = description;
        this.answer = answer;
        this.colorId1 = colorId1;
        this.colorId2 = colorId2;
        this.interpolator = interpolator;
    }
}
