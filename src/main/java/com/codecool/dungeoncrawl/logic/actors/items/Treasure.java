package com.codecool.dungeoncrawl.logic.actors.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Treasure extends Item {

    public Treasure(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "treasure";
    }
}
