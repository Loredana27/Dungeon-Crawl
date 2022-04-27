package com.codecool.dungeoncrawl.logic.actors.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class TreasureKey extends Item {
    public TreasureKey(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "treasure key";
    }
}
