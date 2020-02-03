package org.stackwizards.gridboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.PaintConstant;

import static java.lang.StrictMath.abs;

public class HexGridElement implements IGameObject {
    private int priority;
    private boolean isAlive;
    private int col, row;
    private float cX, cY;
    private int YOffset;
    private int XOffset = 30;
    private float X, Y;
    private float sideLength, hexHeight, hexRadius, hexRectangleHeight, hexRectangleWidth, hexagonAngle = (float) 0.523598776; // 30 degrees in radians
    private boolean isFill;

    public HexGridElement(int col, int row, float hexRadius) {
        this.col = col;
        this.row = row;
        this.hexRadius = hexRadius;
        this.sideLength = (float) (hexRadius / Math.cos(hexagonAngle));
        this.hexHeight = (float) (Math.sin(hexagonAngle) * sideLength);
        this.hexRectangleHeight = sideLength + 2 * hexHeight;
        this.YOffset = (int) hexRectangleHeight / 2;
        this.hexRectangleWidth = 2 * hexRadius;
        this.X = (col * hexRectangleWidth + ((row % 2) * hexRadius)) + XOffset;
        this.Y = (row * (sideLength + hexHeight)) + YOffset;
        this.cX = (col * hexRectangleWidth + ((row % 2) * hexRadius) + hexRadius) + XOffset;
        this.cY = (row * (sideLength + hexHeight) + hexRadius + 6) + YOffset;
    }

    @Override
    public void Update() {
        priority = 0;
        isAlive = true;
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
        canvas.drawPath(p, PaintConstant.PaintWhite());

        if (isFill) {
            PaintConstant.PaintHexSelected().setStyle(Paint.Style.FILL);
        } else {
            PaintConstant.PaintHexSelected().setStyle(Paint.Style.STROKE);
        }
        canvas.drawCircle(X + hexRadius, Y + hexRadius + 6, hexHeight, PaintConstant.PaintHexSelected());
        canvas.drawText("xx", X + (hexRadius/2), Y + hexRadius + 18, PaintConstant.PaintWhite());
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
        if (abs(cX - x) < sideLength / 2 && abs(cY - y) < sideLength / 2) {
            return true;
        }
        return false;
    }

    public void SetFill(boolean status){
        isFill = status;
    }
}
