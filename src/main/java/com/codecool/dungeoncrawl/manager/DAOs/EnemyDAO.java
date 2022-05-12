package com.codecool.dungeoncrawl.manager.DAOs;

public class EnemyDAO {
    private int id;
    private String type;
    private int posX;
    private int posY;
    private int gameID;

    public EnemyDAO(String type, int posX, int posY) {
        this.type = type;
        this.posX = posX;
        this.posY = posY;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
