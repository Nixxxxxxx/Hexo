package org.stackwizards.coreengine;

import android.os.SystemClock;
import android.util.Log;

import org.stackwizards.coreengine.Interfaces.IGameObject;

import java.util.List;

public class GameUpdateThread extends Thread {
    private boolean isRunning;
    private long startTime, loopTime;
    private long DELAY = GameConstant.UPDATE_DELAY;
    private List<IGameObject> gameObjects;


    public GameUpdateThread(  List<IGameObject> gameObjects) {
        this.gameObjects = gameObjects;
        isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            startTime = SystemClock.uptimeMillis();
                for(IGameObject gameObject : gameObjects){
                    gameObject.Update();
                }
            loopTime = SystemClock.uptimeMillis() - startTime;
            if (loopTime < DELAY) {
                try {
                    Thread.sleep(DELAY - loopTime);
                } catch (InterruptedException e) {
                    Log.i(GameConstant.TAG, " Interrupted while sleeping");
                }
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
