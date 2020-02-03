package org.stackwizards.basegame;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import org.stackwizards.coreengine.GameView;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.gridboard.HexBoard;

import java.util.ArrayList;
import java.util.List;

import static org.stackwizards.Utils.Utils.GetScreenWidth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int width = GetScreenWidth(this);

        HexBoard hexBoard = new HexBoard(width,13,13);
        List<IGameObject> gameObjects = new ArrayList<>();
        gameObjects.add(hexBoard);

        LinearLayout layout = findViewById(R.id.surface);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = width;
        layout.setLayoutParams(params);

        GameView gameView = new GameView(this,gameObjects);
        gameView.AddTouchEventListener(hexBoard);
        ((LinearLayout)findViewById(R.id.surface)).addView(gameView);

    }

}
