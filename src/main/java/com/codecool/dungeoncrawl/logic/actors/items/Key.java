package com.codecool.dungeoncrawl.logic.actors.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Actor;

public class Key extends Actor {

    public Key(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "key";
    }
}
