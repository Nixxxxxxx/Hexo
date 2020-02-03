package org.stackwizards.coreengine;

import android.graphics.Color;
import android.graphics.Paint;

public class PaintConstant {

    private static Paint paintWhite;
    private static Paint paintHexSelected;

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
            paintHexSelected.setStyle(Paint.Style.STROKE);
        }

        return paintHexSelected;
    }


}
