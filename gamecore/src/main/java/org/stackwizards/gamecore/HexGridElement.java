package org.stackwizards.gamecore;

public class HexGridElement {
    int col, row;
    float cX, cY;

    //    private void getSelectedHexGridIndex(float x, float y) {
//        for (HexGridElement hx : HexGridElements) {
//            if (abs(hx.cX - x) < sideLength / 2 && abs(hx.cY - y) < sideLength / 2) {
////                Log.i(GameConstants.TAG, "col:" + hx.col + "  row:" + hx.row);
//
////                if (getAccessibleAreas(x, y) && !getAreaBeenTo(x, y)) {
////                    GameConstants.gameLogic = new StoryEngine(hx.row, hx.col);
////                    GameConstants.board = null;
////                } else if (hx.col == 1 && hx.row == 1) {
////                    GameConstants.gameLogic = new StoryEngine(hx.row, hx.col);
////                }
//                break;
//            }
//        }
//    }

//    private HexGridElement getSelectedHexGridIcon(float x, float y) {
//        for (HexGridElement hx : HexGridElements) {
//            if (abs(hx.cX - x) < sideLength / 2 && abs(hx.cY - y) < sideLength / 2) {
////                Log.i(GameConstants.TAG, "col:" + hx.col + "  row:" + hx.row);
//                return hx;
//            }
//        }
//        return null;
//    }

//    private boolean getAreaBeenTo(float x, float y) {
//        for (HexGridElement hx : HexGridElements) {
//            if (abs(hx.cX - x) < sideLength / 2 && abs(hx.cY - y) < sideLength / 2) {
////                Log.i("BADGAME", "col:" + hx.col + "  row:" + hx.row);
////                if (GameConstants.areaBeenTo.contains((hx.row) + "," + (hx.col)))
////                    return true;
//
//
//            }
//        }
//        return false;
//    }

//    private boolean getAccessibleAreas(float x, float y) {
//        for (HexGridElement hx : HexGridElements) {
//            if (abs(hx.cX - x) < sideLength / 2 && abs(hx.cY - y) < sideLength / 2) {
//                if (hx.col == 4 && hx.row == 4)
//                    return true;
//            }
//        }
//        return false;
//    }
}
