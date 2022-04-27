package com.codecool.dungeoncrawl.logic.actors.enemies;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.Random;

public class Soldier extends Enemy {
    private int stepsToMove;
    public Soldier(Cell cell) {
        super(cell);
        attack = 5;
        health = 20;
        stepsToMove = 3;
    }

    @Override
    public String getTileName() {
        return "soldier";
    }

    @Override
    public void chooseAMove(Player player) {
        if(stepsToMove == 0) moveToPatrol();
        else stepsToMove--;
    }

    @Override
    public void moveToPatrol() {
        Random random = new Random();
        int dx = random.nextInt(3) - 1;
        int dy = random.nextInt(3) - 1;
        if(getCell().getNeighbor(dx,dy).getTileName().equalsIgnoreCase("WALL")) moveToPatrol();
        else move(dx,dy);
        stepsToMove = random.nextInt(3) + 2;
    }

    @Override
    public void moveToPlayer(Player player) {
    }

    @Override
    public boolean isPlayerInRange(Player player) {
        return false;
    }
}
