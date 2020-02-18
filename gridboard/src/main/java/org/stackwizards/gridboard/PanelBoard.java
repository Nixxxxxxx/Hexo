package org.stackwizards.gridboard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import org.stackwizards.GameCore;
import org.stackwizards.bitmap.BitmapBank;
import org.stackwizards.coreengine.GameConstant;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.Interfaces.ITouchEventHandler;
import org.stackwizards.coreengine.PaintConstant;
import org.stackwizards.gridboard.minesweeper.MineSweeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.StrictMath.abs;

public class PanelBoard implements IGameObject, ITouchEventHandler {
    private int priority;
    private boolean isAlive;
    private List<IGameObject> hexs;

//    public int player1;

    public Player currentPlayer = null;

    private Player player1 = new Player("Fred", Color.BLUE);
    private Player player2 = new Player("Jimmy", Color.RED);

    public Hexo chosen;

    private boolean turn;

    private GameBoard gameBoard;

    public PanelBoard(int width, int rows, int cols) {
        priority = 0;
//        player1 = Color.BLUE;
        currentPlayer = player1;
        isAlive = true;
        turn = false;
        List<BitmapBank.MyBitMap> bitmaps = BitmapBank.GetLibrary();
        int index = 0;
        hexs = new ArrayList<>();
        int hexRadius = width / (2 * (cols + 1));
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                Hexo hexGridElement = new Hexo(i, j, hexRadius, bitmaps.get(index++), index, HexGridElement.type.panel);
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

    public void SetGameBoard(GameBoard gameBoard){
        this.gameBoard = gameBoard;
    }

    private static <K, V> List<V> createListFromMapEntries(Map<K, V> map) {
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

    public void DeselectAllPanels(int color) {
        for (IGameObject obj : hexs) {
            ((HexGridElement) obj).UnSetSelectedCircle(color);
        }
    }

    @Override
    public void TouchPosition(int x, int y) {
        Log.i(GameConstant.TAG, "Someone touching me at: " + x + " " + y);
        for (IGameObject hex : hexs) {
            if (((HexGridElement) hex).getSelectedHexGrid(x, y)) {
                if (((Hexo) hex).name.equals("next")) {

                   SwitchPlayer();
//                    DeselectAllPanels(player1);
                    DeselectAllPanels(currentPlayer.color);
                } else {
                    DeselectAllPanels(currentPlayer.color);
//                    DeselectAllPanels(player1);
                    ((HexGridElement) hex).SetSelectedCircle(Color.GREEN);
                    chosen = (Hexo) hex;
                }

                if(gameBoard != null){
                    gameBoard.ResetFreePanels();
                    gameBoard.GetFreePlayerPanels();
                    gameBoard.invalidate();
                }
            }
        }
    }

    private void SwitchPlayer() {
        currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
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
