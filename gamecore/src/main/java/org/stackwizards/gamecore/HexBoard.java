package org.stackwizards.gamecore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import static java.lang.StrictMath.abs;

public class HexBoard {

    float hexHeight, hexRadius, hexRectangleHeight, hexRectangleWidth, hexagonAngle = (float) 0.523598776; // 30 degrees in radians
    int boardCols, boardRows;
    int _Width;
    int _Height;
    int YOffset;
    int XOffset = 30;
    float sideLength;
    float gX, gY;
    Paint paintBoard;
    Paint paintBlack;
    Paint paintHexSelected;
    Paint paintClear;

    private HexGridElement[] HexGridElements;

    public HexBoard(Context context, int boardCols) {
        this(context);
        this.boardCols = boardCols;
        this.boardRows = boardCols / 2;
        hexRadius = _Width / (2 * (boardCols + 1));
        sideLength = (float) (hexRadius / Math.cos(hexagonAngle));
        hexHeight = (float) (Math.sin(hexagonAngle) * sideLength);
        hexRectangleHeight = sideLength + 2 * hexHeight;
        hexRectangleWidth = 2 * hexRadius;
        YOffset = (int) (_Height - ((boardRows - 1) * hexRectangleHeight)) / 2;
        HexGridElements = new HexGridElement[boardCols * boardRows];

        initGrid();
        Log.i(GameConstant.TAG, "col:" + boardCols + "  row:" + boardRows + "  width:" + _Width + " height:" + _Height + " sidelength: " + sideLength + " offset:" + YOffset);
    }

    public void clearBoard(Canvas canvas) {
        canvas.drawRect(0, 0, _Width, _Height, paintBlack);
    }

    public void drawBoard(Canvas canvas) {
        int i, j;

        for (i = 0; i < boardCols; ++i) {
            for (j = 0; j < boardRows; ++j) {

                float x = (i * hexRectangleWidth + ((j % 2) * hexRadius)) + XOffset;
                float y = (j * (sideLength + hexHeight)) + YOffset;
                drawHexagon(canvas, x, y,
                        getSelectedHexGrid(i * hexRectangleWidth + ((j % 2) * hexRadius) + hexRadius,
                                j * (sideLength + hexHeight) + hexRadius + 6)
                );
            }
        }
    }

    public void setFillHexGrid(float x, float y) {
        gX = x - XOffset;
        gY = y - YOffset;
    }

    private HexBoard(Context context) {
        InitScreenSize(context);
        InitPaints();
    }

    private void InitPaints() {
        paintBoard = new Paint();
        paintBoard.setColor(Color.WHITE);
        paintBoard.setTextSize(80);
        paintBoard.setStyle(Paint.Style.STROKE);
        paintBoard.setStrokeWidth(1);

        paintBlack = new Paint();
        paintBlack.setColor(Color.BLACK);
        paintBlack.setStyle(Paint.Style.FILL);

        paintHexSelected = new Paint();
        paintHexSelected.setColor(Color.RED);
        paintHexSelected.setStyle(Paint.Style.FILL);


        paintClear = new Paint();
        paintClear.setColor(Color.parseColor("#8833FF33"));
        paintClear.setStyle(Paint.Style.FILL);
    }

    private void InitScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        _Width = metrics.widthPixels;
        _Height = metrics.heightPixels;
    }

    private boolean getSelectedHexGrid(float x, float y) {
        if (abs(gX - x) < sideLength / 2 && abs(gY - y) < sideLength / 2) {
            return true;
        }
        return false;
    }

    private void drawHexagon(Canvas canvas, float x, float y, boolean fill) {
        Path p = new Path();
//        canvas.drawLine(float startX, float startY, float stopX, float stopY, Paint paint);
        p.moveTo(x + hexRadius, y);
        p.lineTo(x + hexRectangleWidth, y + hexHeight);
        p.lineTo(x + hexRectangleWidth, y + hexHeight + sideLength);
        p.lineTo(x + hexRadius, y + hexRectangleHeight);
        p.lineTo(x, y + sideLength + hexHeight);
        p.lineTo(x, y + hexHeight);
        p.close();
        canvas.drawPath(p, paintBoard);

        if (fill) {
            paintHexSelected.setStyle(Paint.Style.FILL);
            paintHexSelected.setColor(Color.YELLOW);
            canvas.drawCircle(x + hexRadius, y + hexRadius + 6, hexHeight, paintHexSelected);
            canvas.drawText("xx", x + hexRadius, y + hexRadius + 6, paintBlack);
        }
    }

    private void initGrid() {
        int h = 0, i, j;
        for (i = 1; i <= boardCols; ++i) {
            for (j = 1; j <= boardRows; ++j) {
                Log.i(GameConstant.TAG, "I:" + i + "  J:" + j);
                HexGridElements[h] = new HexGridElement();
                HexGridElements[h].col = i;
                HexGridElements[h].row = j;
                HexGridElements[h].cX = i * hexRectangleWidth + ((j % 2) * hexRadius) + hexRadius;
                HexGridElements[h].cY = j * (sideLength + hexHeight) + hexRadius + 6;
                h++;
            }
        }
    }
}
