package org.stackwizards.gridboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.stackwizards.bitmap.BitmapBank;
import org.stackwizards.coreengine.GameConstant;
import org.stackwizards.coreengine.Interfaces.GameThread;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.Interfaces.ITouchEventHandler;
import org.stackwizards.coreengine.PaintConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameBoard extends View implements IGameObject, ITouchEventHandler, View.OnTouchListener {
    private int priority;
    private boolean isAlive;
    private List<IGameObject> hexs;
    private Canvas canvas;

    public GameBoard(Context context, int width, int rows, int cols) {
        super(context);
        setOnTouchListener(this);
        priority = 0;
        isAlive = true;

        List<Bitmap> bitmaps = createListFromMapEntries(BitmapBank.GetLibrary());
        int index = 1;
        hexs = new ArrayList<>();
        int hexRadius = width / (2 * (cols + 1));
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                Hexo hexGridElement = new Hexo(i, j, hexRadius, bitmaps.get(index));
                hexGridElement.SetPaintBorder(PaintConstant.PaintBlack());
                hexs.add(hexGridElement);
                Log.i(GameConstant.TAG, "Created element me at: " + hexGridElement.cX + " " + hexGridElement.cY);

            }
        }



        setMeasuredDimension(width, width);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Compute the height required to render the view
        // Assume Width will always be MATCH_PARENT.
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width; // Since 3000 is bottom of last Rect to be drawn added and 50 for padding.
        setMeasuredDimension(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {

//        paint.setColor(Color.GREEN);
//        canvas.drawRect(30, 30, 90, 20000, paint);
//        paint.setColor(Color.BLUE);
//
//        canvas.drawLine(600, 20, 100, 1900, paint);
//
//        paint.setColor(Color.GREEN);

        Draw(canvas);
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
        for (IGameObject hex : hexs) {
            if (((HexGridElement) hex).getSelectedHexGrid(x, y)) {
                ((HexGridElement) hex).ToggleFill();
                Log.i(GameConstant.TAG, "COL ROW touching me at: " + ((HexGridElement) hex).col + " " + ((HexGridElement) hex).row);
                this.invalidate();
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TouchPosition((int)event.getX(),(int)event.getY());
        return false;
    }
}