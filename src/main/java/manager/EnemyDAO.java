package manager;

public class EnemyDAO {
    private int id;
    private String type;
    private int posX;
    private int posY;
    private int gameID;

    public EnemyDAO(String type, int posX, int posY, int gameID) {
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
