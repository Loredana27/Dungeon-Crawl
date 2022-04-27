package com.codecool.dungeoncrawl.logic.actors.enemies;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class Soldier extends Enemy {
    public Soldier(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "soldier";
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
