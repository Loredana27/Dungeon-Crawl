package com.codecool.dungeoncrawl.manager.DAOs;

public class PlayerDAO {
    private int id;
    private String name;
    private int posX;
    private int posY;
    private int gameID;

    public PlayerDAO(String name, int posX, int posY, int gameID) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.gameID = gameID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
