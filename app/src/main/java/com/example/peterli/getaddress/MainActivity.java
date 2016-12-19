package com.example.peterli.getaddress;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager = null;
    // For simplicity, use GPS_PROVIDER
    private String provider = LocationManager.GPS_PROVIDER;

    private TextView tvLatLng = null, tvAddr = null;

    private Geocoder geocoder = null;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateUI(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
            updateUI(null);
        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatLng = (TextView)findViewById(R.id.textViewLatLong);
        tvAddr = (TextView)findViewById(R.id.textViewAddr);

        geocoder = new Geocoder(this, Locale.getDefault());

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Location location= locationManager.getLastKnownLocation(provider);
        updateUI(location);
        locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
    }

    private void updateUI(Location loc) {
        StringBuilder sb = new StringBuilder();
        if(loc == null) {
            sb.append("Not found");
            tvLatLng.setText(sb.toString());
            tvAddr.setText(sb.toString());
        } else {
            sb.append("\nLatitude: ");
            sb.append(Double.toString(loc.getLatitude()));
            sb.append("\n\n\n\n\nLongitude: ");
            sb.append(Double.toString(loc.getLongitude()));

            tvLatLng.setText(sb.toString());



            List<Address> results = null;
            try {
                results = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                sb.delete(0, sb.length());

                Address addr = results.get(0);

                if(results.size() > 0) {
                    for(int i = 0; i < addr.getMaxAddressLineIndex(); i++) {
                        sb.append(addr.getAddressLine(i)).append("\n");
                    }
                    sb.append(addr.getCountryName()).append("\n");
                } else {
                    sb.append("Not address");
                }
                tvAddr.setText("Address: \n"+sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
