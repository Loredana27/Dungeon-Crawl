package com.codecool.dungeoncrawl.logic;

public enum CellType {
    SWORD("sword"),
    EMPTY("empty"),
    FLOOR("floor"),
    TREE("tree"),
    HOUSE("house"),
    GRASS("grass"),
    WALL("wall"),
    GRAVE("grave"),
    PORTAL("portal"),
    CUP("cup");


    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
