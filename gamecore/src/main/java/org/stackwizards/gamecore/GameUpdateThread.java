package org.stackwizards.gamecore;

import android.os.SystemClock;
import android.util.Log;

public class GameUpdateThread extends Thread {
    private boolean isRunning;
    long startTime, loopTime;
    long DELAY = GameConstant.UPDATE_DELAY;

    public GameUpdateThread() {
        isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            startTime = SystemClock.uptimeMillis();
//            AppConstants.getGameEngine().updateAll();
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
