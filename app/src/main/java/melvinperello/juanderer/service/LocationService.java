package melvinperello.juanderer.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Objects;

import melvinperello.juanderer.featureset.location.GREBLocationPublisher;
import melvinperello.juanderer.featureset.location.GoogleFusedLocationClient;
import melvinperello.juanderer.featureset.location.LocationClient;
import melvinperello.juanderer.featureset.location.LocationPublisher;
import melvinperello.juanderer.featureset.location.LocationReceivedEvent;

public class LocationService extends Service implements LocationClient.OnLocationReceived {

    /**
     * Tag.
     */
    public final static String TAG = "LocationService";

    /**
     * Local Binder.
     */
    public final class LocationServiceBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    /**
     * Binder Instance.
     */
    private final IBinder mLocalBinder = new LocationServiceBinder();

    /**
     * Bind Event.
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.mLocalBinder;
    }

    //----------------------------------------------------------------------------------------------
    public final static String ACTION_STOP_SERVICE = "melvinperello.juanderer.service.LocationService.ACTION_STOP_SERVICE";

    /**
     * Service Handler.
     */
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            int startId = msg.arg1;
            //--------------------------------------------------------------------------------------
            // Start Location Service Client
            mLocationClient = new GoogleFusedLocationClient(LocationService.this);
            mLocationClient.setLocationCallback(LocationService.this);
            mLocationClient.setLooper(mServiceLooper);
            mLocationClient.setInterval(3000);
            mLocationClient.setIntervalFromOtherApplications(5000);
            mLocationClient.startLocationClient();
            //
            // create notification
            Notification notification = new NotificationCompat.Builder(LocationService.this)
                    .setContentTitle("Juanderer Tracker")
                    .setContentText("Gathering Real Time Location")
                    .setShowWhen(false)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();
            // explicit flags add
            notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR
                    | Notification.FLAG_FOREGROUND_SERVICE;
            // show
            NotificationManagerCompat.from(LocationService.this)
                    .notify(1, notification);
            // put service in foreground
            LocationService.this.startForeground(1, notification);
            //--------------------------------------------------------------------------------------
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            // stopSelf(startId);
        }
    }

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    //
    private LocationClient mLocationClient;
    private final LocationPublisher mLocationPublisher = new GREBLocationPublisher();

    /**
     * Location Callback.
     *
     * @param location
     */
    @Override
    public void onLocationReceived(Location location) {
        // publish available location
        mLocationPublisher.publish(new LocationReceivedEvent(location));
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, String.format("id:[%s] / flags:[%s] - CALLED", startId, flags));
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        if (mLocationClient == null) {
            this.startLocationServiceHandler(startId);
        } else {
            if (!mLocationClient.isStarted()) {
                this.startLocationServiceHandler(startId);
            }
        }

        if (intent != null) {
            if (Objects.equals(intent.getAction(), ACTION_STOP_SERVICE)) {
                this.stopSelf(); // stop the entire service.
            }
        }
        return START_STICKY;
    }

    private void startLocationServiceHandler(int startId) {
        // not started
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        // start
        mServiceHandler.sendMessage(msg);
        Log.i(TAG, String.format("id:[%s] -- Location service started", startId));
    }
}
