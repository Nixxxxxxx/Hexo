package org.stackwizards.gridboard;

import android.graphics.Canvas;
import android.graphics.Path;
import android.util.Log;

import org.stackwizards.GameCore;
import org.stackwizards.coreengine.GameConstant;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.Interfaces.ITouchEventHandler;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

public class HexBoard implements IGameObject, ITouchEventHandler {
    private int priority;
    private boolean isAlive;
    private List<IGameObject> hexs ;

    public HexBoard(int width, int rows) {
        priority = 0;
        isAlive =  true;
        hexs = new ArrayList<>();
        int cols = rows;
        int  hexRadius = width / (2 * (cols + 1));
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                HexGridElement hexGridElement = new HexGridElement(i,j,hexRadius);
                hexs.add(hexGridElement);
            }
        }
    }

    @Override
    public void Update() {
        for(IGameObject hex : hexs){
            hex.Update();
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        for(IGameObject hex : hexs){
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
        Log.i(GameConstant.TAG, "Someone touching me at: " + x + " " + y);
        for(IGameObject hex : hexs){
            if(((HexGridElement)hex).getSelectedHexGrid(x,y)){
                ((HexGridElement)hex).SetFill(true);
                GameCore.soundBank.Play("hit");
            }
        }
    }


}
