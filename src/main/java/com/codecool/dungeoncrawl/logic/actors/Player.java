package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.ArrayList;


public class Player extends Actor {

    public Player(Cell cell) {
        super(cell);
        attack = 5;
        setNeighborEnemies();
    }

    public String getTileName() {
        return "player";
    }


}
