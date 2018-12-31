package melvinperello.juanderer.featureset.location;

import android.location.Location;

public interface LocationPublisher {
    void enablePublishing();

    void disablePublishing();

    boolean isPublishing();

    void publish(LocationReceivedEvent locationEvent);
}
