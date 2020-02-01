package org.stackwizards.gamecore;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    GameDrawThread gameDrawThread;
    GameUpdateThread gameUpdateThread;
    Context _Context;

    public GameView(Context context) {
        super(context);
        _Context = context;
        initView(context);
    }

    private void initView(Context context) {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        gameDrawThread = new GameDrawThread(context,holder);
        gameUpdateThread = new GameUpdateThread();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
//            AppConstants.getGameEngine().getButtonEvent((int)event.getX(), (int)event.getY());
        }
        return true;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!gameDrawThread.isRunning()) {
            gameDrawThread = new GameDrawThread(_Context,holder);
            gameDrawThread.start();
        } else {
            gameDrawThread.start();
        }

        if (!gameUpdateThread.isRunning()) {
            gameUpdateThread = new GameUpdateThread();
            gameUpdateThread.start();
        } else {
            gameUpdateThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (gameDrawThread.isRunning()) {
            gameDrawThread.setRunning(false);
            boolean retry = true;
            while (retry) {
                try {
                    gameDrawThread.join();
                    retry = false;
                } catch (InterruptedException e) {

                }
            }
        }
        if (gameUpdateThread.isRunning()) {
            gameUpdateThread.setRunning(false);
            boolean retryUpdate = true;
            while (retryUpdate) {
                try {
                    gameUpdateThread.join();
                    retryUpdate = false;
                } catch (InterruptedException e) {

                }
            }
        }
    }
}
