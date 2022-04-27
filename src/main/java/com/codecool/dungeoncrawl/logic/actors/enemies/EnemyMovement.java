package com.codecool.dungeoncrawl.logic.actors.enemies;

import com.codecool.dungeoncrawl.logic.actors.Player;

public interface EnemyMovement {
    void chooseAMove(Player player);

    void moveToPatrol();

    void moveToPlayer(Player player);

    boolean isPlayerInRange(Player player);
}
