package org.stackwizards.gridboard.minesweeper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import org.stackwizards.GameCore;
import org.stackwizards.coreengine.PaintConstant;
import org.stackwizards.gridboard.HexGridElement;

public class MineSweeper extends HexGridElement {

    private boolean isDeadly;
    private int deadlyNeighbours;

    public MineSweeper(int col, int row, float hexRadius) {
        super(col, row, hexRadius, 1);
    }


    public boolean isDeadly() {
        return isDeadly;
    }

    public void setDeadly(boolean deadly) {
        isDeadly = deadly;
    }

    public int getDeadlyNeighbours() {
        return deadlyNeighbours;
    }

    public void setDeadlyNeighbours(int deadlyNeighbours) {
        this.deadlyNeighbours = deadlyNeighbours;
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
            if(isDeadly){
                PaintConstant.PaintHexSelected().setColor(Color.RED);
//                GameCore.soundBank.Play("hit");
                //create two paints one for deadly and another one for OK
            }else {
                PaintConstant.PaintHexSelected().setColor(Color.GREEN);
            }
        } else {
            PaintConstant.PaintHexSelected().setStyle(Paint.Style.STROKE);
        }
        canvas.drawCircle(X + hexRadius, Y + hexRadius + 6, hexHeight, PaintConstant.PaintHexSelected());
        canvas.drawText("xx", X + (hexRadius/2), Y + hexRadius + 18, PaintConstant.PaintWhite());
    }

}
