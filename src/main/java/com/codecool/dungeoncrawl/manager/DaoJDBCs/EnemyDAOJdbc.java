package com.codecool.dungeoncrawl.manager.DaoJDBCs;

import com.codecool.dungeoncrawl.manager.DAOs.EnemyDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class EnemyDAOJdbc {

    private final DataSource dataSource;

    public EnemyDAOJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertEnemy(EnemyDAO enemy){
        try (Connection conn = dataSource.getConnection()){
            String sql = "INSERT INTO enemy (type, posx, posy, game_id) values (?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1,enemy.getType());
            st.setInt(2,enemy.getPosX());
            st.setInt(3, enemy.getPosY());
            st.setInt(4, enemy.getGameID());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            enemy.setId(rs.getInt(1));
        }catch (SQLException e){
            throw new RuntimeException("Error while adding new Enemy!!!", e);
        }
    }

    public void updateEnemy(EnemyDAO enemy){
        try (Connection conn = dataSource.getConnection()){
            String sql = "UPDATE enemy SET type = ?, posx = ?, posy = ?, game_id = ? WHERE enemy_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, enemy.getType());
            st.setInt(2, enemy.getPosX());
            st.setInt(3, enemy.getPosY());
            st.setInt(4,enemy.getGameID());
            st.setInt(5,enemy.getId());
            st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error while updating enemy!!!", e);
        }
    }

    public void deleteEnemies(int gameID){
        try (Connection conn = dataSource.getConnection()){
            String sql = "DELETE FROM enemy WHERE game_id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,gameID);
            st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error while deleting enemy!!!",e);
        }
    }

    public ArrayList<EnemyDAO> getAllEnemy(int gameID){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT type, posx, posy FROM enemy WHERE game_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, gameID);
            ResultSet rs = st.executeQuery();
            ArrayList<EnemyDAO> enemyDAOs= new ArrayList<>();
            while (rs.next()) {
                EnemyDAO enemyDAO = new EnemyDAO(rs.getNString("type"), rs.getInt(2),rs.getInt(3));
                enemyDAO.setId(enemyDAO.getId());
                enemyDAOs.add(enemyDAO);
            }
            return enemyDAOs;
        } catch (SQLException e) {
            throw new RuntimeException("Error!!!" , e);
        }
    }

}
