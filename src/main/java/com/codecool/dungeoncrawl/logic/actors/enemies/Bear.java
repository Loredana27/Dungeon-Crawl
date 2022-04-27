package com.codecool.dungeoncrawl.logic.actors.enemies;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class Bear extends Enemy {
    public Bear(Cell cell) {
        super(cell);
        attack = 5;
        health = 12;
    }

    @Override
    public String getTileName() {
        return "bear";
    }

    @Override
    public void chooseAMove(Player player) {
        if(isPlayerInRange(player)) moveToPlayer(player);
    }

    @Override
    public void moveToPatrol() {
    }

    @Override
    public void moveToPlayer(Player player) {
        int dx,dy;

        dx = Integer.compare(player.getX() - this.getX(), 0);

        dy = Integer.compare(player.getY() - this.getY(), 0);

        move(dx,dy);
    }

    @Override
    public boolean isPlayerInRange(Player player) {
        return (Math.abs(this.getX() - player.getX()) < 3 && Math.abs(this.getY() - player.getY()) < 3);
    }
}
