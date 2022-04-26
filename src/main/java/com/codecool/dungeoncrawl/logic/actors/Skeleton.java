package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.Random;

public class Skeleton extends Enemy{
    public Skeleton(Cell cell) {
        super(cell);
        attack = 2;
        health = 12;
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }


    @Override
    public void chooseAMove(Player player) {
        if(isPlayerInRange(player)) moveToPlayer(player);
        else moveToPatrol();
    }

    @Override
    public void moveToPatrol() {
        Random random = new Random();
        int dx = random.nextInt(3) - 1;
        int dy = random.nextInt(3) - 1;
        if(getCell().getNeighbor(dx,dy).getTileName().equalsIgnoreCase("WALL")) moveToPatrol();
        else move(dx,dy);
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
