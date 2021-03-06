package org.stackwizards.gridboard;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import org.stackwizards.bitmap.BitmapBank;
import org.stackwizards.coreengine.PaintConstant;

public class Hexo extends HexGridElement {

    public Bitmap bitmap;

    public boolean isSet;

    public String name;

    public String PlayerName = "";


    public type hexoType;

    public Hexo(int col, int row, float hexRadius, BitmapBank.MyBitMap bitmap, int att, type t) {
        super(col, row, hexRadius, att);
        this.bitmap = bitmap.bitmap;
        this.name = bitmap.name;
        isSet = false;
        hexoType = t;
    }
    public Hexo(int col, int row, float hexRadius, Bitmap bitmap, int att, type t) {
        super(col, row, hexRadius, att);
        this.bitmap = bitmap;
        this.name = "";
        isSet = false;
        hexoType = t;
    }

    @Override
    public void Draw(Canvas canvas) {
        super.Draw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, cX - (bitmap.getWidth() / 2), cY - (bitmap.getHeight() / 2), painBorder);
            if(name.equals("next")) {
//                canvas.drawText(attack+"", X + (hexRadius / 2), Y + hexRadius + 40, PaintConstant.paintFullWhite());
//                canvas.drawText(name, X + (hexRadius / 3), Y + hexRadius + 70, PaintConstant.paintFullWhite());
            }
            else if(hexoType == type.panel) {
                canvas.drawText(attack+"", X + (hexRadius / 2), Y + hexRadius + 30, PaintConstant.paintFullWhite());
//                canvas.drawText(name, X + (hexRadius / 3), Y + hexRadius + 70, PaintConstant.paintFullWhite());
            }
//            else if(hexoType == type.figure)
//                canvas.drawText( PlayerName, X + (hexRadius / 3), Y + hexRadius + 70, PaintConstant.paintFullWhite());
        }
    }


    @Override
    public void ToggleFill() {
//        super.ToggleFill();
    }
}
