package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.items.Item;

public class Cell implements Drawable {
    private CellType type;
    private Actor actor;

    private Item tempItem;

    public void setTempItem(Item tempItem) {
        this.tempItem = tempItem;
    }

    private final GameMap gameMap;
    private final int x;
    private final int y;

    Cell(GameMap gameMap, int x, int y, CellType type) {
        this.gameMap = gameMap;
        this.x = x;
        this.y = y;
        this.type = type;
        tempItem = null;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public void setActor(Actor actor) {
        if(actor == null )
            this.actor = tempItem;
        else{
            if(this.actor instanceof Item) tempItem = (Item) this.actor;
            this.actor = actor;
        }
    }

    public Actor getActor() {
        return actor;
    }

    public String getTempItem() {
        return tempItem.getTileName();
    }

    public void cleanTempItem(){
        tempItem = null;
    }

    public Cell getNeighbor(int dx, int dy) {
        try{
            return gameMap.getCell(x + dx, y + dy);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    @Override
    public String getTileName() {
        return type.getTileName();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
