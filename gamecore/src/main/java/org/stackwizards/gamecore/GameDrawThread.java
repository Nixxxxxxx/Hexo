package org.stackwizards.gamecore;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

class GameDrawThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private long startTime, loopTime;
    private long DELAY = GameConstant.UPDATE_DELAY;
    private HexBoard _Hexboard;

    public GameDrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        _Hexboard = new HexBoard(context,9);
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
                          _Hexboard.clearBoard(canvas);
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

    public void GetTouchEvent(int x, int y){
        _Hexboard.setFillHexGrid(x,y);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
