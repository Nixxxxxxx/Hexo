package org.stackwizards.basegame;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.navigation.NavigationView;

import org.stackwizards.gamecore.GameView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        hideSystemUI();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        NavigationView navigationView = findViewById(R.id.nav_view);

        GameView gameView = new GameView(this);
        ((LinearLayout)findViewById(R.id.surface)).addView(gameView);
    }

}
