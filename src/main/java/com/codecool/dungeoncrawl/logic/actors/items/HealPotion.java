package com.codecool.dungeoncrawl.logic.actors.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Actor;

public class HealPotion extends Actor {
    public HealPotion(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "heal";
    }
}
