package melvinperello.juanderer.featureset.location;

import android.location.Location;

public interface LocationPublisher {
    void startPublishing();

    void stopPublishing();

    boolean isPublishing();

    void publish(LocationReceivedEvent locationEvent);
}
