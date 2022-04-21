package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Actor {
    public Skeleton(Cell cell) {
        super(cell);
        attack = 2;
        health = 12;
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
