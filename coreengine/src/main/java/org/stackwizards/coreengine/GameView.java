package org.stackwizards.coreengine;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.Interfaces.ITouchEventHandler;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameDrawThread gameDrawThread;
    private GameUpdateThread gameUpdateThread;
    private Context context;
    private List<IGameObject> gameObjects;
    private List<ITouchEventHandler> eventHandlers;

    public GameView(Context context, List<IGameObject> gameObjects) {
        super(context);
        this.context = context;
        this.gameObjects = gameObjects;
        this.eventHandlers = new ArrayList<>();
        initView();
    }

    public void AddTouchEventListener(ITouchEventHandler eventHandler) {
        this.eventHandlers.add(eventHandler);
    }

    private void initView() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        gameDrawThread = new GameDrawThread(holder, gameObjects);
        gameUpdateThread = new GameUpdateThread(gameObjects);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
//            gameDrawThread.GetTouchEvent((int) event.getX(), (int) event.getY());
            for (ITouchEventHandler eventHandler : eventHandlers) {
                eventHandler.TouchPosition((int) event.getX(), (int) event.getY());
            }
        }
        return true;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!gameDrawThread.isRunning()) {
            gameDrawThread = new GameDrawThread(holder, gameObjects);
            gameDrawThread.start();
        } else {
            gameDrawThread.start();
        }

        if (!gameUpdateThread.isRunning()) {
            gameUpdateThread = new GameUpdateThread(gameObjects);
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
