package melvinperello.juanderer.featureset.location;

import org.greenrobot.eventbus.EventBus;

/**
 * Green Robot Event Bus (GREB) Posting implementation.
 */
public class GREBLocationPublisher implements LocationPublisher {

    private boolean publishing = false;

    @Override
    public void startPublishing() {
        this.publishing = true;
    }

    @Override
    public void stopPublishing() {
        this.publishing = false;
    }

    @Override
    public boolean isPublishing() {
        return this.publishing;
    }

    @Override
    public void publish(LocationReceivedEvent locationEvent) {
        if (this.isPublishing()) {
            EventBus.getDefault().post(locationEvent);
        }
    }
}
