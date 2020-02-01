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

    float hexHeight,
            hexRadius,
            hexRectangleHeight,
            hexRectangleWidth,
            hexagonAngle = (float) 0.523598776; // 30 degrees in radians
    int boardCols, boardRows;
    int _Width;
    int _Height;
    int YOffset ;
    int XOffset = 30;
    float sideLength;
    float gX, gY;
    Paint boardPaint;
    Paint paintBlack;
    Paint hexSelected;
    Paint paintClear;

    private HexGrid[] gameHexGrid;

    public HexBoard(Context context) {
        InitScreenSize(context);
//        sideLength = _Width / 22;
//        hexHeight = (float) (Math.sin(hexagonAngle) * sideLength);
//        hexRadius = (float) (Math.cos(hexagonAngle) * sideLength);
//        hexRectangleHeight = sideLength +  hexHeight;
//        hexRectangleWidth = 2 * hexRadius;
//
//        Log.i(GameConstant.TAG, "sidelength:" + sideLength + "  hexheight:" + hexHeight + "  hexRadius:" + hexRadius + " hexRectHeight:" + hexRectangleHeight + " hexRectWidth: " + hexRectangleWidth);
//
//        boardCols = 10;
//        boardRows = 6;
//        gameHexGrid = new HexGrid[boardCols * boardRows];
        InitPaints();
//        initGrid();

    }

    public HexBoard(Context context, int boardCols) {
        this(context);
        this.boardCols = boardCols;
        this.boardRows = boardCols/2;
        hexRadius = _Width/(2*(boardCols+1));
        sideLength = (float) ( hexRadius/ Math.cos(hexagonAngle));
        hexHeight = (float) (Math.sin(hexagonAngle) * sideLength);
        hexRectangleHeight = sideLength + 2 * hexHeight;
        hexRectangleWidth = 2 * hexRadius;

//        while (((boardRows+1)*hexRectangleHeight) < _Height){
//            boardRows++;
//        }

        YOffset =  (int)(_Height - ((boardRows-1)*hexRectangleHeight))/2 ;
        gameHexGrid = new HexGrid[boardCols * boardRows];

        initGrid();

        Log.i(GameConstant.TAG, "col:" + boardCols + "  row:" + boardRows + "  width:" + _Width + " height:" + _Height + " sidelength: " + sideLength + " offset:" + YOffset);
    }

    private void InitPaints(){
        boardPaint = new Paint();
        boardPaint.setColor(Color.WHITE);
        boardPaint.setTextSize(80);
        boardPaint.setStyle(Paint.Style.STROKE);
        boardPaint.setStrokeWidth(1);

        paintBlack = new Paint();
        paintBlack.setColor(Color.BLACK);
        paintBlack.setStyle(Paint.Style.FILL);

        hexSelected = new Paint();
        hexSelected.setColor(Color.RED);
        hexSelected.setStyle(Paint.Style.FILL);


        paintClear = new Paint();
        paintClear.setColor(Color.parseColor("#8833FF33"));
        paintClear.setStyle(Paint.Style.FILL);
    }

    private  void InitScreenSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics =  new DisplayMetrics();
        display.getMetrics(metrics);
        _Width = metrics.widthPixels;
        _Height = metrics.heightPixels;
    }

    public void setFillHexGrid(float x, float y) {
        gX = x;
        gY = y;

        getSelectedHexGridIndex(x, y);
    }

    private boolean getSelectedHexGrid(float x, float y) {
        if (abs(gX - x) < sideLength / 2 && abs(gY - y) < sideLength / 2) {
            return true;
        }
        return false;
    }

    public void clearBoard(Canvas canvas) {
        canvas.drawRect(0, 0, _Width, _Height, paintBlack);
    }

    public void drawBoard(Canvas canvas) {
        int i, j;

        for (i = 0; i < boardCols; ++i) {
            for (j = 0; j < boardRows; ++j) {

                float x = (i * hexRectangleWidth + ((j % 2) * hexRadius)) + XOffset ;
                float y = (j * (sideLength + hexHeight)) + YOffset ;
                drawHexagon(
                        canvas,
                        x,
                        y,
                        getSelectedHexGrid(i * hexRectangleWidth + ((j % 2) * hexRadius) + hexRadius,
                                j * (sideLength + hexHeight) + hexRadius + 6)
                );
            }
        }
    }

    public void drawHexagon(Canvas canvas, float x, float y, boolean fill) {
        Path p = new Path();

//        canvas.drawLine(float startX, float startY, float stopX, float stopY, Paint paint);
        p.moveTo(x + hexRadius, y);
        p.lineTo(x + hexRectangleWidth, y + hexHeight);
        p.lineTo(x + hexRectangleWidth, y + hexHeight + sideLength);
        p.lineTo(x + hexRadius, y + hexRectangleHeight);
        p.lineTo(x, y + sideLength + hexHeight);
        p.lineTo(x, y + hexHeight);
        p.close();
        canvas.drawPath(p, boardPaint);


        if (fill) {
            hexSelected.setStyle(Paint.Style.FILL);
            hexSelected.setColor(Color.YELLOW);
            canvas.drawCircle(x + hexRadius, y + hexRadius + 6, hexHeight, hexSelected);
        } else {


            if (getAccessibleAreas(x + hexRadius, y + hexRadius + 6)) {
                hexSelected.setStyle(Paint.Style.FILL);
                hexSelected.setColor(Color.parseColor("#88FF3333"));
                canvas.drawCircle(x + hexRadius, y + hexRadius + 6, hexHeight, hexSelected);
            }

            //                HexGrid mGrid = getSelectedHexGridIcon(x, y);
//                if(mGrid.row==2 && mGrid.col == 3){
//                    canvas.drawBitmap(testBitmap,x ,y ,null);
//                }


            if (getAreaBeenTo(x + hexRadius, y + hexRadius + 6)) {
                canvas.drawCircle(x + hexRadius, y + hexRadius + 6, hexHeight, paintClear);
            } else {
                hexSelected.setStyle(Paint.Style.STROKE);
                hexSelected.setColor(Color.RED);
                canvas.drawCircle(x + hexRadius, y + hexRadius + 6, hexHeight, hexSelected);
            }


        }


    }

    public void initGrid() {
        int h = 0, i, j;
        for (i = 1; i <= boardCols; ++i) {
            for (j = 1; j <= boardRows; ++j) {
                Log.i(GameConstant.TAG, "I:" + i + "  J:" + j);
                gameHexGrid[h] = new HexGrid();
                gameHexGrid[h].col = i;
                gameHexGrid[h].row = j;
                gameHexGrid[h].cX = i * hexRectangleWidth + ((j % 2) * hexRadius) + hexRadius;
                gameHexGrid[h].cY = j * (sideLength + hexHeight) + hexRadius + 6;
                h++;
            }
        }
    }

    private void getSelectedHexGridIndex(float x, float y) {
        for (HexGrid hx : gameHexGrid) {
            if (abs(hx.cX - x) < sideLength / 2 && abs(hx.cY - y) < sideLength / 2) {
//                Log.i(GameConstants.TAG, "col:" + hx.col + "  row:" + hx.row);

//                if (getAccessibleAreas(x, y) && !getAreaBeenTo(x, y)) {
//                    GameConstants.gameLogic = new StoryEngine(hx.row, hx.col);
//                    GameConstants.board = null;
//                } else if (hx.col == 1 && hx.row == 1) {
//                    GameConstants.gameLogic = new StoryEngine(hx.row, hx.col);
//                }
                break;
            }
        }
    }

    private HexGrid getSelectedHexGridIcon(float x, float y) {
        for (HexGrid hx : gameHexGrid) {
            if (abs(hx.cX - x) < sideLength / 2 && abs(hx.cY - y) < sideLength / 2) {
//                Log.i(GameConstants.TAG, "col:" + hx.col + "  row:" + hx.row);
                return hx;
            }
        }
        return null;
    }

    private boolean getAreaBeenTo(float x, float y) {
        for (HexGrid hx : gameHexGrid) {
            if (abs(hx.cX - x) < sideLength / 2 && abs(hx.cY - y) < sideLength / 2) {
//                Log.i("BADGAME", "col:" + hx.col + "  row:" + hx.row);
//                if (GameConstants.areaBeenTo.contains((hx.row) + "," + (hx.col)))
//                    return true;


            }
        }
        return false;
    }

    private boolean getAccessibleAreas(float x, float y) {
        for (HexGrid hx : gameHexGrid) {
            if (abs(hx.cX - x) < sideLength / 2 && abs(hx.cY - y) < sideLength / 2) {
                if (hx.col == 4 && hx.row == 4)
                    return true;
            }
        }
        return false;
    }

    private class HexGrid {
        int col, row;
        float cX, cY;
    }

}
