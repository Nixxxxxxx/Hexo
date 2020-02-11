package org.stackwizards.gridboard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import org.stackwizards.GameCore;
import org.stackwizards.bitmap.BitmapBank;
import org.stackwizards.coreengine.GameConstant;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.Interfaces.ITouchEventHandler;
import org.stackwizards.coreengine.PaintConstant;
import org.stackwizards.gridboard.minesweeper.MineSweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.StrictMath.abs;

public class PanelBoard implements IGameObject, ITouchEventHandler {
    private int priority;
    private boolean isAlive;
    private List<IGameObject> hexs;

    public PanelBoard(int width, int rows, int cols) {
        priority = 0;
        isAlive = true;
        List<Bitmap> bitmaps = createListFromMapEntries(BitmapBank.GetLibrary());
        int index = 0;
        hexs = new ArrayList<>();
        int hexRadius = width / (2 * (cols + 1));
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                Hexo hexGridElement = new Hexo(i, j, hexRadius, bitmaps.get(index++));
                hexGridElement.SetPaintBorder(PaintConstant.PaintWhite());
                hexs.add(hexGridElement);
            }
        }
    }

    public PanelBoard(int width, int rows) {
        priority = 0;
        isAlive = true;
        hexs = new ArrayList<>();
        int cols = rows;
        int hexRadius = width / (2 * (cols + 1));
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                MineSweeper hexGridElement = new MineSweeper(i, j, hexRadius);
                hexs.add(hexGridElement);
            }
        }

        int r = 10;
        Random rand = new Random();
        while (r > 0) {
            int col = rand.nextInt(cols);
            int row = rand.nextInt(rows);
            MineSweeper element = GetHex(row, col);
            if (!element.isDeadly()) {
                element.setDeadly(true);
                r--;
            }
        }

    }





    private static <K, V>  List<V> createListFromMapEntries (Map<K, V> map){
        return (List<V>) map.values().stream().collect(Collectors.toList());
    }

    @Override
    public void Update() {
        for (IGameObject hex : hexs) {
            hex.Update();
        }
    }

    @Override
    public void Draw(Canvas canvas) {

        for (IGameObject hex : hexs) {
            hex.Draw(canvas);
        }
    }

    @Override
    public boolean IsAlive() {
        return isAlive;
    }

    @Override
    public int GetPriority() {
        return priority;
    }


    @Override
    public void TouchPosition(int x, int y) {
        Log.i(GameConstant.TAG, "Someone touching me at: " + x + " " + y);
        for (IGameObject hex : hexs) {
            if (((HexGridElement) hex).getSelectedHexGrid(x, y)) {
                ((HexGridElement) hex).ToggleFill();
            }
        }
    }

    private MineSweeper GetHex(int row, int col) {
        for (IGameObject hex : hexs) {
            if (((HexGridElement) hex).isHexGrid(row, col)) {
                GameCore.soundBank.Play("hit");
                return ((MineSweeper) hex);
            }
        }
        return null;
    }

}
