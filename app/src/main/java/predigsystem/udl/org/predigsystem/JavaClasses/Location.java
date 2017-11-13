package predigsystem.udl.org.predigsystem.JavaClasses;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Didac on 09/11/2017.
 */

public class Location {
    private Float latitud;
    private Float longitud;

    public Location (Float latitud, Float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Float getLatitud () {
        return this.latitud;
    }

    public void setLatitud (Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud () {
        return this.longitud;
    }

    public void setLongitud (Float longitud) {
        this.longitud = longitud;
    }

    public LatLng getLocation () {
        return new LatLng(this.latitud, this.longitud);
    }

}
