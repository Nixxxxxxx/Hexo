package org.stackwizards.coreengine.Interfaces;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;

import org.stackwizards.coreengine.GameConstant;

public class GameThread extends Thread {
    private boolean isRunning;
    private long startTime, loopTime;
    private long DELAY = GameConstant.UPDATE_DELAY;
    private IGameObject gameObject;

    private Canvas canvas;

    public GameThread(IGameObject gameObject, Canvas canvas) {
        this.gameObject = gameObject;
        this.canvas = canvas;
    }

    @Override
    public void run() {
        while (isRunning) {
            startTime = SystemClock.uptimeMillis();
            gameObject.Draw(canvas);
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
