package org.stackwizards.gamecore;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

class GameDrawThread extends Thread {

    SurfaceHolder surfaceHolder;
    private boolean isRunning;
    long startTime, loopTime;
    long DELAY = GameConstant.UPDATE_DELAY;

    private HexBoard _Hexboard;

    public GameDrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        _Hexboard = new HexBoard(context,13);
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
//                        AppConstants.getGameEngine().drawAll(canvas);
                          _Hexboard.drawBoard(canvas);
                    }catch (Exception e){

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
