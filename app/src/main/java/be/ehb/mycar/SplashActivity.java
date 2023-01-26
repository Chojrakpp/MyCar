package be.ehb.mycar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {// stop splashactivity dat hij niet runt in background
                setContentView(R.layout.activity_main);
            }
        }, 1000); // Splashscreen timer op 1s

        // om de splashscreen te laten werken, in manifest main met splash veranderen dat splash eerst load
    }
}