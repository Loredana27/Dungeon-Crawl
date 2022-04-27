package com.codecool.dungeoncrawl.logic.actors.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Actor;

public class Treasury extends Item {

    public Treasury(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "treasury";
    }
}
