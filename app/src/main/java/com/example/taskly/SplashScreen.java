package com.example.taskly;

import android.animation.ObjectAnimator; // Wichtig: ObjectAnimator importieren
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar; // Wichtig: ProgressBar importieren

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 5000; // 2 Sekunden

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_screen);

        progressBar = findViewById(R.id.progressBar);

        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        progressAnimator.setDuration(SPLASH_SCREEN_TIMEOUT); // Die Animation dauert so lange wie der Splash-Screen
        progressAnimator.start();

        // Handler for next activity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Wechsle zur MyTasksActivity
                //Intent intent = new Intent(SplashScreen.this, MyTasksActivity.class);
                //startActivity(intent);

                // Schließe den SplashScreen
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
