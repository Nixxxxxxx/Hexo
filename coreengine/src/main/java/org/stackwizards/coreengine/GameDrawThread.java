package org.stackwizards.coreengine;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

import org.stackwizards.coreengine.Interfaces.IGameObject;

import java.util.List;

class GameDrawThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private long startTime, loopTime;
    private long DELAY = GameConstant.UPDATE_DELAY;
    private List<IGameObject> gameObjects;

    public GameDrawThread(SurfaceHolder surfaceHolder, List<IGameObject> gameObjects) {
        this.surfaceHolder = surfaceHolder;
        this.gameObjects = gameObjects;
        isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            startTime = SystemClock.uptimeMillis();
            Canvas canvas = surfaceHolder.lockCanvas(null);
            if (canvas != null) {
                synchronized (surfaceHolder) {
                    try {
                        for (IGameObject gameObject : gameObjects) {
                            gameObject.Draw(canvas);
                        }
                    } catch (Exception e) {

                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
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
