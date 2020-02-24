package org.stackwizards.basegame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.stackwizards.GameCore;
import org.stackwizards.bitmap.BitmapBank;
import org.stackwizards.coreengine.GameConstant;
import org.stackwizards.coreengine.GameView;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.PaintConstant;
import org.stackwizards.gridboard.GameBoard;
import org.stackwizards.gridboard.HexGridElement;
import org.stackwizards.gridboard.Hexo;
import org.stackwizards.gridboard.PanelBoard;
import org.stackwizards.gridboard.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.stackwizards.Utils.Utils.GetScreenWidth;

public class MainActivity extends AppCompatActivity {
    private Player player1 ;
    private Player player2 ;
    private ArrayList<Hexo> fullDeck;
    ArrayList<Minions> minions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GameCore.initialisation(this);
        int width = GetScreenWidth(this);

        InitialBitmapBankData();
        int numCols = BitmapBank.GetLibrary().size();
        LoadBitmapData();


        player1 = new Player("Fred", Color.BLUE, GetHexPieces(width, 1, numCols));
        player2 = new Player("Jimmy", Color.RED, GetHexPieces(width, 1, numCols));

        PanelBoard hexPalette = new PanelBoard(player1,player2);
        List<IGameObject> paletteObjects = new ArrayList<>();
        paletteObjects.add(hexPalette);

        LinearLayout layoutPalette = findViewById(R.id.palette);
        ViewGroup.LayoutParams paramsPalette = layoutPalette.getLayoutParams();
        paramsPalette.height = (width / numCols) + 40;
        layoutPalette.setLayoutParams(paramsPalette);

        GameView gameViewPalette = new GameView(this, paletteObjects);
        gameViewPalette.AddTouchEventListener(hexPalette);
        ((LinearLayout) findViewById(R.id.palette)).addView(gameViewPalette);

        GameBoard gameBoard = new GameBoard(this, width, 8, 8);
        gameBoard.SetPanel(hexPalette);
        ((LinearLayout) findViewById(R.id.surface)).addView(gameBoard);
        hexPalette.SetGameBoard(gameBoard);


    }


    private  void LoadBitmapData(){
        fullDeck =  new ArrayList<>();
        Gson gson = new Gson();
        InputStream is = getResources().openRawResource(R.raw.hexo_data);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
        }catch (Exception e){

        }finally {
        }

        String jsonString = writer.toString();
        Minions[] deck = gson.fromJson(jsonString, Minions[].class);
        minions= new ArrayList<Minions>(Arrays.asList(deck));

        for(Minions m : deck){
            Log.i(GameConstant.TAG, "Loaded Minion: " + m.Name + " : Cost:" + m.Cost);
        }
    }


    private ArrayList<Hexo> GetHexPieces(int width, int rows, int cols) {
        List<BitmapBank.MyBitMap> bitmaps = BitmapBank.GetLibrary();
        int index = 0;
        ArrayList<Hexo> hexs = new ArrayList<>();
        int hexRadius = width / (2 * (cols + 1));
        Random random = new Random();
        Collections.shuffle(minions);
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {

                if(i==cols-1){
                    Bitmap bmp = BitmapBank.GetBitmapFromResourceID(R.drawable.next_arrow);
                    Hexo hexGridElement = new Hexo(i, j, hexRadius, bmp, 0, HexGridElement.type.panel);
                    hexGridElement.SetPaintBorder(PaintConstant.PaintWhite());
                    hexGridElement.name = "next";
                    hexs.add(hexGridElement);
                }else {
                    Minions m = minions.get(i);
                    Bitmap bmp = BitmapBank.Crop(BitmapBank.spriteSheet, m.X*2,m.Y*2,m.Width*2,m.Height*2);
                    bmp = BitmapBank.scaleImage(bmp,120,120);
                    Hexo hexGridElement = new Hexo(i, j, hexRadius, bmp, m.Cost, HexGridElement.type.panel);
                    hexGridElement.SetPaintBorder(PaintConstant.PaintWhite());
                    hexs.add(hexGridElement);
                }
            }
        }
        return hexs;
    }

    private void InitialBitmapBankData() {
        BitmapBank.InitInstance(this.getResources());
        BitmapBank.AddBitmapToBank("next", R.drawable.next_arrow);
        BitmapBank.AddBitmapToBank("knight", R.drawable.knight);
        BitmapBank.AddBitmapToBank("arrow", R.drawable.arrow);
        BitmapBank.AddBitmapToBank("pirate", R.drawable.pirate);
        BitmapBank.AddBitmapToBank("drake", R.drawable.drake);
        BitmapBank.AddBitmapToBank("mage", R.drawable.mage);
        BitmapBank.AddBitmapToBank("ninja", R.drawable.ninja3);
    }

}
