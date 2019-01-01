package melvinperello.juanderer.featureset.location;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Green Robot Event Bus (GREB) Posting implementation.
 */
public class GREBLocationPublisher implements LocationPublisher {

    private boolean publishing = true;

    private final static String TAG = "GREBLocationPublisher";

    @Override
    public void enablePublishing() {
        this.publishing = true;
    }

    @Override
    public void disablePublishing() {
        this.publishing = false;
    }


    @Override
    public boolean isPublishing() {
        return this.publishing;
    }

    @Override
    public void publish(LocationReceivedEvent locationEvent) {
        if (this.publishing) {
            EventBus.getDefault().post(locationEvent);
            Log.i(TAG, "Location Event was published");
        }
    }
}
