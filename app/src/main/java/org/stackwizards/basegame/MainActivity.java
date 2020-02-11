package org.stackwizards.basegame;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.stackwizards.GameCore;
import org.stackwizards.bitmap.BitmapBank;
import org.stackwizards.coreengine.GameView;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.gridboard.GameBoard;
import org.stackwizards.gridboard.PanelBoard;

import java.util.ArrayList;
import java.util.List;

import static org.stackwizards.Utils.Utils.GetScreenWidth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GameCore.initialisation(this);
        int width = GetScreenWidth(this);

        InitialBitmapBankData();
        int numCols = BitmapBank.GetLibrary().size();

        PanelBoard hexPalette = new PanelBoard(width,1,numCols);
        List<IGameObject> paletteObjects = new ArrayList<>();
        paletteObjects.add(hexPalette);

        LinearLayout layoutPalette = findViewById(R.id.palette);
        ViewGroup.LayoutParams paramsPalette = layoutPalette.getLayoutParams();
        paramsPalette.height = (width/numCols) + 40;
        layoutPalette.setLayoutParams(paramsPalette);

        GameView gameViewPalette = new GameView(this,paletteObjects);
        gameViewPalette.AddTouchEventListener(hexPalette);
        ((LinearLayout)findViewById(R.id.palette)).addView(gameViewPalette);




//        PanelBoard panelBoard = new PanelBoard(width,13);
//        List<IGameObject> gameObjects = new ArrayList<>();
//        gameObjects.add(panelBoard);
//
//        LinearLayout layout = findViewById(R.id.surface);
//        ViewGroup.LayoutParams params = layout.getLayoutParams();
//        params.height = width*2;
//        layout.setLayoutParams(params);
//
//        GameView gameView = new GameView(this,gameObjects);
//        gameView.AddTouchEventListener(panelBoard);

        GameBoard gameBoard = new GameBoard(this,width,7,7);
        ((LinearLayout)findViewById(R.id.surface)).addView(gameBoard);





    }


    private void InitialBitmapBankData(){
        BitmapBank.InitInstance(this.getResources());
        BitmapBank.AddBitmapToBank("next",R.drawable.next_arrow);
        BitmapBank.AddBitmapToBank("knight",R.drawable.knight);
        BitmapBank.AddBitmapToBank("arrow",R.drawable.arrow);
        BitmapBank.AddBitmapToBank("pirate",R.drawable.pirate);
        BitmapBank.AddBitmapToBank("drake",R.drawable.drake);
        BitmapBank.AddBitmapToBank("mage",R.drawable.mage);
        BitmapBank.AddBitmapToBank("ninja",R.drawable.ninja3);
    }

}
