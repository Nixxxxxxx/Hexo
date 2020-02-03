package org.stackwizards.coreengine.Interfaces;

import android.graphics.Canvas;

public interface IGameObject {
    void Update();
    void Draw(Canvas canvas);
    boolean IsAlive();
    int GetPriority();
}
