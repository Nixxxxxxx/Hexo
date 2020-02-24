package org.stackwizards.gridboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import org.stackwizards.coreengine.GameConstant;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.Interfaces.ITouchEventHandler;


public class PanelBoard implements IGameObject, ITouchEventHandler {
    private int priority;
    private boolean isAlive;
    private GameBoard gameBoard;
    private Player firstPlayer;
    private Player secondPlayer;

    public Hexo chosen;
    public Player currentPlayer = null;
    public int rounds = 1;
    public int currentRoundMana = 1;
    public boolean enableNext = true;

    public PanelBoard(Player firstPlayer,Player secondPlayer) {
        priority = 0;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        currentPlayer = firstPlayer;
        isAlive = true;
        SetAvailableUnits();
    }

    public void SetGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

//    private static <K, V> List<V> createListFromMapEntries(Map<K, V> map) {
//        return (List<V>) map.values().stream().collect(Collectors.toList());
//    }

    @Override
    public void Update() {
        for (IGameObject hex : currentPlayer.hexs) {
            hex.Update();
        }
    }

    @Override
    public void Draw(Canvas canvas) {

        for (IGameObject hex : currentPlayer.hexs) {
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

    public void DeselectAllPanels(int color) {
        for (IGameObject obj : currentPlayer.hexs) {
            ((HexGridElement) obj).UnSetSelectedCircle(color);
        }
    }

    public void SetAvailableUnits() {
        for (Hexo obj : currentPlayer.hexs) {
            if (obj.attack <= currentRoundMana) {
                obj.SetPaintCircleColor(currentPlayer.color);
            } else {
                obj.SetPaintCircleColor(Color.GRAY);
            }
        }

    }

    @Override
    public void TouchPosition(int x, int y) {
        Log.i(GameConstant.TAG, "Someone touching me at: " + x + " " + y);
        for (Hexo hex : currentPlayer.hexs) {
            if (hex.getSelectedHexGrid(x, y)) {
                if (hex.name.equals("next") && enableNext) {
                    rounds++;
                    currentRoundMana = rounds;
//                    enableNext = false;
                    SwitchPlayer();
                    SetAvailableUnits();
//                    DeselectAllPanels(player1);
                } else {
                    SetAvailableUnits();
                    if (currentRoundMana >= hex.attack) {
//                        DeselectAllPanels(currentPlayer.color);
                        chosen = hex;
                        hex.SetSelectedCircle(Color.GREEN);

                    }
                }

                if (gameBoard != null) {
                    gameBoard.ResetFreePanels();
                    gameBoard.GetFreePlayerPanels();
                    gameBoard.invalidate();
                }
            }
        }
    }

    private void SwitchPlayer() {
        currentPlayer = currentPlayer.equals(firstPlayer) ? secondPlayer : firstPlayer;
    }

}
