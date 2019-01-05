package us.ait.android.shoppinglist.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import us.ait.android.shoppinglist.MainActivity;
import us.ait.android.shoppinglist.R;

public class Splash extends AppCompatActivity {
    private final int WAIT_TIME = 3000;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(progressBar.VISIBLE);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, WAIT_TIME);
    }
}
