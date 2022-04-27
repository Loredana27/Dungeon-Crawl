package com.codecool.dungeoncrawl.logic.actors.enemies;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Actor;

public abstract class Enemy extends Actor implements EnemyMovement{

    public Enemy(Cell cell) {
        super(cell);
    }

}
