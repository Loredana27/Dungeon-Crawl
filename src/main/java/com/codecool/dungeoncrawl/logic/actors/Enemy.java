package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public abstract class Enemy extends Actor {

    public Enemy(Cell cell) {
        super(cell);
    }

    public abstract void chooseAMove(Player player);

    public abstract void moveToPatrol();

    public abstract void moveToPlayer(Player player);

    public abstract boolean isPlayerInRange(Player player);
}
