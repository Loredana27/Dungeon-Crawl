package com.codecool.dungeoncrawl.logic.actors.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Actor;

public class TreasuryKey extends Actor {
    public TreasuryKey(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "treasurykey";
    }
}
