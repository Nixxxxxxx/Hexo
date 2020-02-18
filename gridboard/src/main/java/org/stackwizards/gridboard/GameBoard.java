package org.stackwizards.gridboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.stackwizards.bitmap.BitmapBank;
import org.stackwizards.coreengine.GameConstant;
import org.stackwizards.coreengine.Interfaces.IGameObject;
import org.stackwizards.coreengine.Interfaces.ITouchEventHandler;
import org.stackwizards.coreengine.PaintConstant;
import org.stackwizards.soundbank.SoundBank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class GameBoard extends View implements IGameObject, ITouchEventHandler, View.OnTouchListener {
    private int priority;
    private boolean isAlive;
    private List<IGameObject> hexs;

    private PanelBoard panelBoard;
    private List<Bitmap> decorations = new ArrayList<>();

    private int rows, cols;

    private SoundBank soundBank;

    public GameBoard(Context context, int width, int rows, int cols) {
        super(context);
        setOnTouchListener(this);
        InitDecoationBitmap();
        this.rows = rows;
        this.cols = cols;
        priority = 0;
        isAlive = true;
        int index = 1;
        hexs = new ArrayList<>();
        soundBank = new SoundBank(context);
        int hexRadius = width / (2 * (cols + 1));
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                Hexo hexGridElement = new Hexo(i, j, hexRadius, new BitmapBank.MyBitMap("p1", null), 0, HexGridElement.type.panel);
                hexGridElement.SetPaintBorder(PaintConstant.PaintBlack());
                hexs.add(hexGridElement);
                Log.i(GameConstant.TAG, "Created element me at: " + hexGridElement.cX + " " + hexGridElement.cY);

            }
        }


        Random random = new Random();

        int decoartionNum = 16;
        while (decoartionNum > 0) {
            int rrow = random.nextInt(rows);
            int rcol = random.nextInt(cols);
            Hexo hexo = GetHex(rrow, rcol);
            if (!hexo.isSet) {
                hexo.isSet = true;
                decoartionNum--;
                hexo.bitmap = decorations.get(random.nextInt(decorations.size()));
                hexo.hexoType = HexGridElement.type.decor;
                hexo.SetPaintCircleColor(Color.GRAY);
            }
        }

        setMeasuredDimension(width, width);

    }

    private void InitDecoationBitmap() {
        decorations.add(BitmapBank.GetBitmapFromResourceID(R.drawable.deco1));
        decorations.add(BitmapBank.GetBitmapFromResourceID(R.drawable.rock1));
        decorations.add(BitmapBank.GetBitmapFromResourceID(R.drawable.rock2));
        decorations.add(BitmapBank.GetBitmapFromResourceID(R.drawable.tree2));
        decorations.add(BitmapBank.GetBitmapFromResourceID(R.drawable.tree3));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Compute the height required to render the view
        // Assume Width will always be MATCH_PARENT.
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width - 200; // Since 3000 is bottom of last Rect to be drawn added and 50 for padding.
        setMeasuredDimension(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Draw(canvas);
    }

    @Override
    public void Update() {
        for (IGameObject hex : hexs) {
            hex.Update();
        }
    }

    @Override
    public void Draw(Canvas canvas) {

        for (IGameObject hex : hexs) {
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

    @Override
    public void TouchPosition(int x, int y) {
        for (IGameObject hex : hexs) {
            if (((HexGridElement) hex).getSelectedHexGrid(x, y)) {
                ResetFreePanels();

                ((HexGridElement) hex).ToggleFill();

               List<Hexo> freePanels =  GetFreePlayerPanels();


                if (panelBoard != null && panelBoard.chosen != null && !((Hexo) hex).isSet && ((Hexo) hex).isFree && freePanels.contains((Hexo) hex)) {

                    final Hexo HEXO = (Hexo) hex;

                    ((Hexo) hex).bitmap = panelBoard.chosen.bitmap;
                    ((Hexo) hex).attack = panelBoard.chosen.attack;
                    ((HexGridElement) hex).SetSelectedCircle(panelBoard.currentPlayer.color);
                    ((Hexo) hex).isSet = true;
                    ((Hexo) hex).name = panelBoard.chosen.name;
                    ((Hexo) hex).PlayerName = panelBoard.currentPlayer.name;
                    ((Hexo) hex).hexoType = HexGridElement.type.figure;
                    panelBoard.currentRoundMana -= ((Hexo) hex).attack;
                    panelBoard.SetAvailableUnits();
                    panelBoard.chosen = null;
                    Fight(HEXO);

                }
                Log.i(GameConstant.TAG, "COL ROW touching me at: " + ((HexGridElement) hex).col + " " + ((HexGridElement) hex).row);
            }
        }
        this.invalidate();
    }

    public void ResetFreePanels() {
        for (IGameObject h : hexs) {
            Hexo he = (Hexo) h;
            if (he.hexoType.equals(HexGridElement.type.panel) && !he.isSet) {
                he.SetPaintCircleColor(Color.GRAY);
            }
        }
        this.invalidate();
    }

    public List<Hexo> GetAllFreePanels() {
        List<Hexo> freePanels = new ArrayList<>();
        for (IGameObject h : hexs) {
            Hexo he = (Hexo) h;
            if (!he.isSet && he.hexoType.equals(HexGridElement.type.panel)) {
                he.isFree = true;
                he.SetPaintCircleColor(Color.GREEN);
                freePanels.add(he);
            }
        }
        return freePanels;
    }

    public List<Hexo> GetCurrentPlayerUnits() {
        List<Hexo> units = new ArrayList<>();
        for (IGameObject h : hexs) {
            Hexo he = (Hexo) h;
            if (he.PlayerName.equals(panelBoard.currentPlayer.name) && he.isSet && he.hexoType.equals(HexGridElement.type.figure)) {
                he.isFree = true;
                units.add(he);
            }
        }
        return units;
    }


    public List<Hexo> GetFreePlayerPanels() {
        if (GetCurrentPlayerUnits().size() == 0) {
            return GetAllFreePanels();
        }

        List<Hexo> units = new ArrayList<>();
        for (Hexo h : GetCurrentPlayerUnits()) {
            for (Hexo hi : GetNeighbours(h)) {
                if (!hi.isSet && hi.hexoType.equals(HexGridElement.type.panel) && !units.contains(hi)) {
                    hi.SetPaintCircleColor(Color.GREEN);
                    units.add(hi);
                }
            }
        }
        return units;
    }


    private void Fight(final Hexo hex) {
        ResetFreePanels();
        GetFreePlayerPanels();
        soundBank.Play("appear");
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        List<Hexo> neightbours = GetNeighbours(hex);
                        for (Hexo hexo : neightbours) {
                            if (hexo.hexoType == HexGridElement.type.figure && !hexo.PlayerName.equals(panelBoard.currentPlayer.name)) {
                                if (hexo.attack < ((Hexo) hex).attack) {
                                    hexo.SetPaintCircleColor(panelBoard.currentPlayer.color);
                                    hexo.PlayerName = panelBoard.currentPlayer.name;
                                    final Hexo HexInner = hexo;
                                    invalidMe();
                                    Fight(HexInner);
                                }
                            }
                        }
                    }
                },
                1000
        );
        this.invalidate();
    }

    private void invalidMe() {
        this.invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TouchPosition((int) event.getX(), (int) event.getY());
        return false;
    }

    public void SetPanel(PanelBoard panelBoard) {
        this.panelBoard = panelBoard;
    }

    private List<Hexo> GetNeighbours(Hexo hexo) {
        List<Hexo> neightbours = new ArrayList<>();
        if (hexo.col == 0) {
            neightbours.add(GetHex(hexo.row, hexo.col + 1));
        } else if (hexo.col == cols - 1) {
            neightbours.add(GetHex(hexo.row, hexo.col - 1));
        } else {
            neightbours.add(GetHex(hexo.row, hexo.col - 1));
            neightbours.add(GetHex(hexo.row, hexo.col + 1));
        }

        if (hexo.row % 2 == 0) {
            if (hexo.row == 0) {
                if (hexo.col == 0) {
                    neightbours.add(GetHex(1, 0));
                } else if (hexo.col == cols - 1) {
                    neightbours.add(GetHex(1, hexo.col));
                    if (hexo.col % 2 == 0) {
                        neightbours.add(GetHex(1, hexo.col - 1));
                    }

                } else {
                    neightbours.add(GetHex(1, hexo.col));
                    neightbours.add(GetHex(1, hexo.col - 1));
                }
            } else if (hexo.row == rows - 1) {
                if (hexo.col == 0) {
                    neightbours.add(GetHex(hexo.row - 1, 0));
                } else if (hexo.col == cols - 1) {
                    neightbours.add(GetHex(hexo.row - 1, cols - 1));
                    if (cols % 2 == 1) {
                        neightbours.add(GetHex(hexo.row - 1, cols - 2));
                    }
                } else {
                    neightbours.add(GetHex(hexo.row - 1, hexo.col));
                    neightbours.add(GetHex(hexo.row - 1, hexo.col - 1));
                }
            } else {
                if (hexo.col == 0) {
                    neightbours.add(GetHex(hexo.row - 1, 0));
                    neightbours.add(GetHex(hexo.row - 1, 1));
                } else if (hexo.col == cols - 1) {
                    neightbours.add(GetHex(hexo.row - 1, cols - 1));
                    if (cols % 2 == 1) {
                        neightbours.add(GetHex(hexo.row - 1, cols - 2));
                    }
                } else {
                    neightbours.add(GetHex(hexo.row - 1, hexo.col - 1));
                    neightbours.add(GetHex(hexo.row - 1, hexo.col));
                    neightbours.add(GetHex(hexo.row + 1, hexo.col - 1));
                    neightbours.add(GetHex(hexo.row + 1, hexo.col));
                }
            }
        } else {
            if (hexo.row == rows - 1) {
                if (hexo.col == 0) {
                    neightbours.add(GetHex(hexo.row - 1, 0));
                } else if (hexo.col == cols - 1) {
                    neightbours.add(GetHex(hexo.row - 1, cols - 1));
                    if (cols % 2 == 1) {
                        neightbours.add(GetHex(hexo.row - 1, cols - 2));
                    }
                } else {
                    neightbours.add(GetHex(hexo.row - 1, hexo.col));
                    neightbours.add(GetHex(hexo.row - 1, hexo.col + 1));
                }
            } else {
                if (hexo.col == 0) {
                    neightbours.add(GetHex(hexo.row - 1, 0));
                    neightbours.add(GetHex(hexo.row - 1, 1));
                } else if (hexo.col == cols - 1) {
                    neightbours.add(GetHex(hexo.row - 1, cols - 1));
                    neightbours.add(GetHex(hexo.row - 1, cols - 2));
                } else {
                    neightbours.add(GetHex(hexo.row - 1, hexo.col + 1));
                    neightbours.add(GetHex(hexo.row - 1, hexo.col));
                    neightbours.add(GetHex(hexo.row + 1, hexo.col + 1));
                    neightbours.add(GetHex(hexo.row + 1, hexo.col));
                }
            }
        }

        return neightbours;
    }

    private Hexo GetHex(int row, int col) {
        for (IGameObject hex : hexs) {
            if (((HexGridElement) hex).isHexGrid(row, col)) {
//                GameCore.soundBank.Play("hit");
                return ((Hexo) hex);
            }
        }
        return null;
    }

}