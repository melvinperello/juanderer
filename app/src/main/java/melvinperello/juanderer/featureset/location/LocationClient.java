package melvinperello.juanderer.featureset.location;

import android.location.Location;

/**
 * Location Client
 */
public interface LocationClient {

    /**
     * starts the location client from gathering the location data.
     */
    void startLocationClient();

    /**
     * stops the location client from gathering location data.
     */
    void stopLocationClient();

    /**
     * checks whether this location client is gathering location data.
     *
     * @return
     */
    boolean isStarted();

    /**
     * allows to set a new callback without changing the location client.
     *
     * @param callback
     */
    void setLocationCallback(OnLocationReceived callback);

    /**
     * Interval in gathering the location.
     *
     * @param interval
     */
    void setInterval(long interval);

    /**
     * Interval in gathering location from other applications.
     *
     * @param interval
     */
    void setIntervalFromOtherApplications(long interval);

    /**
     * Location received callback. this is called inside the built in callback of the location client.
     * <br>
     * This will allow to change callbacks even the client is already instantiated.
     */
    @FunctionalInterface
    public interface OnLocationReceived {
        void onLocationReceived(Location location);
    }
}

