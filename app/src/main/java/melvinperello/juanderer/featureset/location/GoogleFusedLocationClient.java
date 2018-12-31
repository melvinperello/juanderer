package melvinperello.juanderer.featureset.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class GoogleFusedLocationClient implements LocationClient {

    private final static String TAG = "GFLocationClient";

    /**
     * Google Callback Wrapper.
     */
    private class GoogleLocationCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            // check for null.
            if (mUserDefinedCallback != null) {
                // loop all available location.
                for (Location location : locationResult.getLocations()) {
                    // call user-defined callback that a location is available.
                    mUserDefinedCallback.onLocationReceived(location);
                }
            }
        }
    }

    // dependencies
    private final Context mContext;
    // members
    private boolean mlocationClientRunning = false;
    private OnLocationReceived mUserDefinedCallback;
    private long mInterval = 5000;
    private long mIntervalFromOtherApplication = 3000;
    private FusedLocationProviderClient mGoogleFusedLocationClient;
    private final GoogleLocationCallback mGoogleCallback = new GoogleLocationCallback();
    private Looper mLooper;

    /**
     * Creates a Google Fused Location client.
     *
     * @param context
     */
    public GoogleFusedLocationClient(Context context) {
        this.mContext = context;
    }

    @Override
    public void startLocationClient() {
        this.mGoogleFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.mContext);

        if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //
            //
            //
            Log.e(TAG, "Operation was aborted missing runtime permissions.");
            return;
        }
        this.mGoogleFusedLocationClient.requestLocationUpdates(
                LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(this.mInterval)
                        .setFastestInterval(this.mIntervalFromOtherApplication)
                , mGoogleCallback, this.mLooper
        );
        //
        this.mlocationClientRunning = true;
    }

    @Override
    public void stopLocationClient() {
        // check if the client is not null
        if (this.mGoogleFusedLocationClient != null) {
            // remove google call back
            this.mGoogleFusedLocationClient.removeLocationUpdates(mGoogleCallback);
        }
        //
        this.mlocationClientRunning = false;
    }

    @Override
    public boolean isStarted() {
        return this.mlocationClientRunning;
    }

    @Override
    public void setLocationCallback(OnLocationReceived callback) {
        this.mUserDefinedCallback = callback;
    }

    @Override
    public void setInterval(long interval) {
        this.mInterval = interval;
    }

    @Override
    public void setIntervalFromOtherApplications(long interval) {
        this.mIntervalFromOtherApplication = interval;
    }

    @Override
    public void setLooper(Looper looper) {
        this.mLooper = looper;
    }
}
