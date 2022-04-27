package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.ArrayList;


public class Player extends Actor {

    private String name;

    public Player(Cell cell) {
        super(cell);
        attack = 5;
        setNeighborEnemies();
    }

    public String getTileName() {
        return "player";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
