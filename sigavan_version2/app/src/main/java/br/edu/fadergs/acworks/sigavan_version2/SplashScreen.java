package br.edu.fadergs.acworks.sigavan_version2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler delayer = new Handler();
        delayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                iniciarapp();
            }
        }, 4000);
    }

    private void iniciarapp() {
        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
