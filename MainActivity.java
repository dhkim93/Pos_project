package kdh.com.map;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import net.daum.mf.map.api.MapView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey(getString(R.string.API_KEY));

        RelativeLayout container = (RelativeLayout) findViewById(R.id.mapView);
        container.addView(mapView);
    }


}
