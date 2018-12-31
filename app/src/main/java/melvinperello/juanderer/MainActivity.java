package melvinperello.juanderer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import melvinperello.juanderer.featureset.location.LocationReceivedEvent;
import melvinperello.juanderer.service.LocationService;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvHello)
    TextView tvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent startLocationServiceIntent = new Intent(this, LocationService.class);
        startService(startLocationServiceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationReceivedEvent(LocationReceivedEvent locationEvent) {
        tvHello.setText(locationEvent.toString());
    }
}
