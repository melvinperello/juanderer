package melvinperello.juanderer.featureset.location;

import android.location.Location;
import android.support.annotation.NonNull;

import melvinperello.juanderer.util.TimeTool;

/**
 * This event object will be the only one that will interact with the UI.
 */
public class LocationReceivedEvent {
    private final double longitude;
    private final double latitude;
    private final float speed;
    private final float accuracy;
    private final long time;

    public LocationReceivedEvent(Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.speed = location.getSpeed();
        this.accuracy = location.getAccuracy();
        this.time = TimeTool.getCurrentTimeInMillsFromERN(location.getElapsedRealtimeNanos());
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public float getSpeed() {
        return speed;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public long getTime() {
        return time;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s [%s,%s,%s] @ %s", this.time, this.longitude, this.latitude, this.accuracy, this.speed);
    }
}
