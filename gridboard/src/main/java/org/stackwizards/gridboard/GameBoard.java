package org.stackwizards.gridboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.stackwizards.bitmap.BitmapBank;
import org.stackwizards.coreengine.GameConstant;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.Interfaces.ITouchEventHandler;
import org.stackwizards.coreengine.PaintConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class GameBoard extends View implements IGameObject, ITouchEventHandler, View.OnTouchListener {
    private int priority;
    private boolean isAlive;
    private List<IGameObject> hexs;

    private PanelBoard panelBoard;

    private List<Bitmap> decorations = new ArrayList<>();

    public GameBoard(Context context, int width, int rows, int cols) {
        super(context);
        setOnTouchListener(this);
        InitDecoationBitmap();
        priority = 0;
        isAlive = true;
        int index = 1;
        hexs = new ArrayList<>();
        int hexRadius = width / (2 * (cols + 1));
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                Hexo hexGridElement = new Hexo(i, j, hexRadius, new BitmapBank.MyBitMap("p1", null), 0, HexGridElement.type.decor);
                hexGridElement.SetPaintBorder(PaintConstant.PaintBlack());
                hexs.add(hexGridElement);
                Log.i(GameConstant.TAG, "Created element me at: " + hexGridElement.cX + " " + hexGridElement.cY);

            }
        }


        Random random = new Random();

        int decoartionNum = 16;
        while (decoartionNum > 0) {
            int rrow = random.nextInt(rows);
            int rcol = random.nextInt(cols);
            Hexo hexo = GetHex(rrow, rcol);
            if (!hexo.isSet) {
                hexo.isSet = true;
                decoartionNum--;
                hexo.bitmap = decorations.get(random.nextInt(decorations.size()));
                hexo.SetPaintCircleColor(Color.GRAY);
            }
        }

        setMeasuredDimension(width, width);

    }

    private void InitDecoationBitmap() {
        decorations.add(BitmapBank.GetBitmapFromResourceID(R.drawable.deco1));
        decorations.add(BitmapBank.GetBitmapFromResourceID(R.drawable.rock1));
        decorations.add(BitmapBank.GetBitmapFromResourceID(R.drawable.rock2));
        decorations.add(BitmapBank.GetBitmapFromResourceID(R.drawable.tree2));
        decorations.add(BitmapBank.GetBitmapFromResourceID(R.drawable.tree3));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Compute the height required to render the view
        // Assume Width will always be MATCH_PARENT.
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width - 200; // Since 3000 is bottom of last Rect to be drawn added and 50 for padding.
        setMeasuredDimension(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Draw(canvas);
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
        for (IGameObject hex : hexs) {
            if (((HexGridElement) hex).getSelectedHexGrid(x, y)) {
                ((HexGridElement) hex).ToggleFill();
                if (panelBoard != null && panelBoard.chosen != null && !((Hexo) hex).isSet) {


                    ((Hexo) hex).bitmap = panelBoard.chosen.bitmap;
                    ((Hexo) hex).attack = panelBoard.chosen.attack;
                    panelBoard.DeselectAllPanels(panelBoard.currentPlayer.color);
                    ((HexGridElement) hex).SetSelectedCircle(panelBoard.currentPlayer.color);

                    ((Hexo) hex).isSet = true;
                    ((Hexo) hex).name = panelBoard.chosen.name;
                    ((Hexo) hex).PlayerName = panelBoard.currentPlayer.name;
                    panelBoard.chosen = null;
                    ((Hexo) hex).hexoType = HexGridElement.type.figure;

                }
                Log.i(GameConstant.TAG, "COL ROW touching me at: " + ((HexGridElement) hex).col + " " + ((HexGridElement) hex).row);
                this.invalidate();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TouchPosition((int) event.getX(), (int) event.getY());
        return false;
    }

    public void SetPanel(PanelBoard panelBoard) {
        this.panelBoard = panelBoard;
    }

    private Hexo GetHex(int row, int col) {
        for (IGameObject hex : hexs) {
            if (((HexGridElement) hex).isHexGrid(row, col)) {
//                GameCore.soundBank.Play("hit");
                return ((Hexo) hex);
            }
        }
        return null;
    }

}