package predigsystem.udl.org.predigsystem.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import predigsystem.udl.org.predigsystem.R;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final LatLng CENTRECAT = new LatLng(41.837555, 1.537764);
    private static final LatLng LLEIDA = new LatLng(41.607644, 0.622699);



    @Override
    public void onCreate (Bundle savecInstanceBundle) {
        super.onCreate(savecInstanceBundle);
        setContentView(R.layout.fragment_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Lleida and move the camera
        LatLng lleida = LLEIDA;
        mMap.addMarker(new MarkerOptions().position(lleida).title("Marker in Lleida").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LLEIDA, 12f));
    }
}
