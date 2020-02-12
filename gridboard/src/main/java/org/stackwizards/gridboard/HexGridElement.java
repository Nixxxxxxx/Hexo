package org.stackwizards.gridboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import org.stackwizards.coreengine.GameConstant;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.PaintConstant;

import static java.lang.StrictMath.abs;

public class HexGridElement implements IGameObject {
    protected int priority;
    protected boolean isAlive;
    protected int col, row;
    protected float cX, cY;
    protected int YOffset = 15 ;
    protected int XOffset = 30;
    protected float X, Y;
    protected float sideLength, hexHeight, hexRadius, hexRectangleHeight, hexRectangleWidth, hexagonAngle = (float) 0.523598776; // 30 degrees in radians
    protected boolean isFill;


    private Paint paintCircle;
    protected Paint painBorder = null;
    public int attack;

    public enum type {
        decor,
        panel,
        figure
    }

    public HexGridElement(int col, int row, float hexRadius, int att) {
        this.col = col;
        this.row = row;
        attack = att;
        this.hexRadius = hexRadius;
        this.sideLength = (float) (hexRadius / Math.cos(hexagonAngle));
        this.hexHeight = (float) (Math.sin(hexagonAngle) * sideLength);
        this.hexRectangleHeight = sideLength + 2 * hexHeight;
//        this.YOffset = (int) hexRectangleHeight / 2;
        this.hexRectangleWidth = 2 * hexRadius;
        this.X = (col * hexRectangleWidth + ((row % 2) * hexRadius)) + XOffset;
        this.Y = (row * (sideLength + hexHeight)) + YOffset;
        this.cX = (col * hexRectangleWidth + ((row % 2) * hexRadius) + hexRadius) + XOffset;
        this.cY = (row * (sideLength + hexHeight) + hexRadius + 6) + YOffset;

        priority = 0;
        isAlive = true;

        paintCircle = new Paint();
        paintCircle.setColor(Color.YELLOW);
        paintCircle.setStyle(Paint.Style.FILL);
    }

    public void SetPaintBorder(Paint paint){
        this.painBorder = paint;
    }


    @Override
    public void Update() {
    }

    @Override
    public void Draw(Canvas canvas) {
        Path p = new Path();
        p.moveTo(X + hexRadius, Y);
        p.lineTo(X + hexRectangleWidth, Y + hexHeight);
        p.lineTo(X + hexRectangleWidth, Y + hexHeight + sideLength);
        p.lineTo(X + hexRadius, Y + hexRectangleHeight);
        p.lineTo(X, Y + sideLength + hexHeight);
        p.lineTo(X, Y + hexHeight);
        p.close();
        canvas.drawPath(p, painBorder);

//        if (isFill) {
//            PaintConstant.PaintHexSelected().setStyle(Paint.Style.FILL);
//        } else {
//            PaintConstant.PaintHexSelected().setStyle(Paint.Style.STROKE);
//        }
        canvas.drawCircle(X + hexRadius, Y + hexRadius + 10, hexHeight + 18, paintCircle);
    }

    @Override
    public boolean IsAlive() {
        return isAlive;
    }

    @Override
    public int GetPriority() {
        return priority;
    }

    public boolean getSelectedHexGrid(float x, float y) {
//        Log.i(GameConstant.TAG, "How far touching me at: " +( abs(cX - x) < sideLength / 2) + " " + (abs(cY - y) < sideLength / 2));

        if (abs(cX - x) < sideLength / 2 && abs(cY - y) < sideLength / 2) {
            return true;
        }
        return false;
    }

    public boolean isHexGrid(int row, int col) {
        if (this.row==row && this.col==col) {
            return true;
        }
        return false;
    }

    public void SetSelectedCircle(int color){
        paintCircle.setColor(color);
    }


    public void UnSetSelectedCircle(int color){
        paintCircle.setColor(color);
    }

    public void ToggleFill(){
        if(isFill){
            paintCircle.setColor(Color.RED);
            isFill = !isFill;
        }else {
            paintCircle.setColor(Color.GREEN);
            isFill = !isFill;
        }
//        isFill = status;
    }

    public void SetPaintCircleColor(int color){
        paintCircle.setColor(color);
    }
}
