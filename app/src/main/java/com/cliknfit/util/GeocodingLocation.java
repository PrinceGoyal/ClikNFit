package com.cliknfit.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.R.id.list;
import static com.cliknfit.R.id.address;

/**
 * Created by prince on 12/09/17.
 */

public class GeocodingLocation {

    private static final String TAG = "GeocodingLocation";

    public static void getAddressFromLocation(final LatLng location,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String address = null;
                String newaddress= null;
                try {
                    List<Address> addresses = geocoder.getFromLocation(
                            location.latitude, location.longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        /*Address addresses = list.get(0);
                        // sending back first address line and locality
                        result = address.getAddressLine(0) + ", " + address.getLocality();*/

                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        if (address.contains("Unnamed")) {
                            address = addresses.get(0).getAddressLine(0);
                        }
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        if (address == null)
                            newaddress = city + ", " + state;
                      /*  else if (city == null)
                            newaddress = address + ", " + state;
                        else if (state == null)
                            newaddress = address + ", " + city;
                        else if (address == null && state == null)
                            newaddress = city;
                        else if (city == null && state == null)
                            newaddress = address;*/
                        else
                            newaddress = address;

                    }
                } catch (IOException e) {
                    Log.e(TAG, "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (newaddress != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", newaddress);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
