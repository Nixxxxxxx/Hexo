package org.stackwizards.gridboard;

import org.stackwizards.coreengine.Interfaces.IGameObject;

import java.util.List;

public class Player {
  public  String name;
  public int color;
  public List<Hexo> hexs;

    public Player(String name, int color, List<Hexo> hexs) {
        this.name = name;
        this.color = color;
        this.hexs = hexs;
    }
}
