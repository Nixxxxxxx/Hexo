package org.stackwizards.coreengine;

import android.graphics.Color;
import android.graphics.Paint;

public class PaintConstant {

    private static Paint paintWhite;
    private static Paint paintHexSelected;
    private static Paint paintCircle;
    private static Paint paintBlack;

    public static Paint PaintBlack() {
        if(paintBlack==null) {
            paintBlack = new Paint();
            paintBlack.setColor(Color.BLACK);
            paintBlack.setTextSize(48);
            paintBlack.setStyle(Paint.Style.STROKE);
        }
        return  paintBlack;

    }

    public static Paint PaintWhite(){
        if(paintWhite == null){
            paintWhite = new Paint();
            paintWhite.setColor(Color.WHITE);
            paintWhite.setTextSize(48);
            paintWhite.setStyle(Paint.Style.STROKE);
        }

        return paintWhite;
    }

    public static Paint PaintHexSelected(){
        if(paintHexSelected == null){
            paintHexSelected = new Paint();
            paintHexSelected.setColor(Color.RED);
            paintHexSelected.setStyle(Paint.Style.FILL);
        }

        return paintHexSelected;
    }
    public static Paint PaintCircle(){
        if(paintCircle == null){
            paintCircle = new Paint();
            paintCircle.setColor(Color.YELLOW);
            paintCircle.setStyle(Paint.Style.FILL);
        }
        return paintCircle;
    }


}
