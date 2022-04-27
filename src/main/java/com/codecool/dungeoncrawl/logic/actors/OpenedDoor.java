package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class OpenedDoor extends Door {

    public OpenedDoor(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "opened-door";
    }
}
