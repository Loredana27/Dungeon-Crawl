package com.codecool.dungeoncrawl.manager.DaoJDBCs;

import com.codecool.dungeoncrawl.manager.DAOs.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class GameDAOJdbc {
    private final DataSource dataSource;

    private final EnemyDAOJdbc enemyDAOJdbc;

    private final PlayerDAOJdbc playerDAOJdbc;

    private final ItemDAOJdbc itemDAOJdbc;

    private final AvailableItemDAOJdbc availableItemDAOJdbc;

    public GameDAOJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
        this.availableItemDAOJdbc = new AvailableItemDAOJdbc(dataSource);
        this.playerDAOJdbc = new PlayerDAOJdbc(dataSource);
        this.itemDAOJdbc = new ItemDAOJdbc(dataSource);
        this.enemyDAOJdbc = new EnemyDAOJdbc(dataSource);
    }

    public void insertGame(GameDAO game) {
        try(Connection conn = dataSource.getConnection()){
            String sql = "INSERT INTO game (name, actualMap, saveDate) values (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, game.getName());
            st.setInt(2,game.getActualMap());
            st.setDate(3, new Date(System.currentTimeMillis()));
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            game.setId(rs.getInt(1));
            insertGameDetails(game);
        }catch (SQLException e){throw new RuntimeException("Error while adding new game!!!", e);}}

    public void updateGame(GameDAO game){
        try (Connection conn = dataSource.getConnection()){
            String sql = "UPDATE game SET name = ?, actualmap = ?, savedate = ? WHERE id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, game.getName());
            st.setInt(2,game.getActualMap());
            st.setDate(3,new Date(System.currentTimeMillis()));
            st.setInt(4,game.getId());

            enemyDAOJdbc.deleteEnemies(game.getId());
            playerDAOJdbc.deletePlayer(game.getId());
            availableItemDAOJdbc.deleteAvailableItems(game.getId());
            itemDAOJdbc.deleteItems(game.getId());
            st.executeUpdate();


            insertGameDetails(game);
        }catch (SQLException e){throw new RuntimeException("Error while updating game!!! ", e);}
    }

    private void insertGameDetails(GameDAO game) {
        game.getEnemies().forEach(e -> {
            e.setGameID(game.getId());
            enemyDAOJdbc.insertEnemy(e);
        });
        game.getAvailableItems().forEach(e -> {
            e.setGameID(game.getId());
            availableItemDAOJdbc.insertAvailableItem(e);
        });
        game.getItems().forEach(e-> {
            e.setGameID(game.getId());
            itemDAOJdbc.insertItem(e);
        });
        PlayerDAO player = game.getPlayer();
        player.setGameID(game.getId());
        playerDAOJdbc.insertPlayer(player);
    }

    public void deleteGame(int id){
        try (Connection conn = dataSource.getConnection()){
            String sql = "DELETE FROM game WHERE id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,id);

            playerDAOJdbc.deletePlayer(id);
            itemDAOJdbc.deleteItems(id);
            availableItemDAOJdbc.deleteAvailableItems(id);
            enemyDAOJdbc.deleteEnemies(id);

            st.executeUpdate();


        }catch (SQLException e){
            throw new RuntimeException("Error while deleting game!!!",e);
        }
    }

    public GameDAO getGame(int id){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, actualmap, savedate FROM game WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            GameDAO game = new GameDAO(
                    rs.getString(1),
                    rs.getInt(2),
                    playerDAOJdbc.getPlayer(id),
                    enemyDAOJdbc.getAllEnemy(id),
                    itemDAOJdbc.getAllItems(id),
                    availableItemDAOJdbc.getAllAvailableItems(id)
            );
            game.setId(id);
            game.setSaveDate(rs.getDate(3));
            return game;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error!!!" , e);
        }
    }

    public ArrayList<GameDAO> getAllGame(){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, name, actualmap, savedate FROM game";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            ArrayList<GameDAO> gameDAOs= new ArrayList<>();
            while (rs.next()) {
                GameDAO gameDAO = new GameDAO(
                        rs.getString(2),
                        rs.getInt(3),
                        playerDAOJdbc.getPlayer(rs.getInt(1)),
                        enemyDAOJdbc.getAllEnemy(rs.getInt(1)),
                        itemDAOJdbc.getAllItems(rs.getInt(1)),
                        availableItemDAOJdbc.getAllAvailableItems(rs.getInt(1)));
                gameDAO.setId(rs.getInt(1));
                gameDAO.setSaveDate(rs.getDate(4));
                gameDAOs.add(gameDAO);
            }
            return gameDAOs;

        } catch (SQLException e) {
            throw new RuntimeException("Error!!!" , e);
        }
    }


}
