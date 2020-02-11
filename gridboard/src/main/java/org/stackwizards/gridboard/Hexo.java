package org.stackwizards.gridboard;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import org.stackwizards.bitmap.BitmapBank;

public class Hexo extends HexGridElement {

    private Bitmap bitmap;


    public Hexo(int col, int row, float hexRadius, Bitmap bitmap) {
        super(col, row, hexRadius);
        this.bitmap = bitmap;
    }

    @Override
    public void Draw(Canvas canvas) {
        super.Draw(canvas);
        canvas.drawBitmap(bitmap,cX-(bitmap.getWidth()/2),cY-(bitmap.getHeight()/2), painBorder);
    }


}
