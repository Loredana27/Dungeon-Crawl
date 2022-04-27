package com.codecool.dungeoncrawl.logic.actors.enemies;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class Farmer extends Enemy {
    public Farmer(Cell cell) {
        super(cell);
        attack = 0;
        health = 6;
    }

    @Override
    public String getTileName() {
        return "farmer";
    }

    @Override
    public void chooseAMove(Player player) {

    }

    @Override
    public void moveToPatrol() {

    }

    @Override
    public void moveToPlayer(Player player) {

    }

    @Override
    public boolean isPlayerInRange(Player player) {
        return false;
    }
}
