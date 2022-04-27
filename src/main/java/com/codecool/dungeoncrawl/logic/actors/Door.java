package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Actor;

public class Door extends Actor {
    public Door(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "door";
    }
}