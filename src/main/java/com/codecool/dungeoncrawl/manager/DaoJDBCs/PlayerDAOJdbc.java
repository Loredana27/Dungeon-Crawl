package com.codecool.dungeoncrawl.manager.DaoJDBCs;

import com.codecool.dungeoncrawl.manager.DAOs.PlayerDAO;

import javax.sql.DataSource;
import java.sql.*;

public class PlayerDAOJdbc {

    private final DataSource dataSource;

    public PlayerDAOJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertPlayer(PlayerDAO player){
        try (Connection conn = dataSource.getConnection()){
           String sql = "INSERT INTO player (name, posx, posy, game_id) values (?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, player.getName());
            st.setInt(2,player.getPosX());
            st.setInt(3, player.getPosY());
            st.setInt(4, player.getGameID());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            player.setId(rs.getInt(1));
        }catch (SQLException e){throw new RuntimeException("Error while adding new Player!!!", e);}
    }

    public void updatePlayer(PlayerDAO player){
        try (Connection conn = dataSource.getConnection()){
            String sql = "UPDATE player SET name = ?, posx = ?, posy = ?, game_id = ? WHERE player_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, player.getName());
            st.setInt(2, player.getPosX());
            st.setInt(3, player.getPosY());
            st.setInt(4, player.getGameID());
            st.setInt(5, player.getId());
            st.executeUpdate();
        }catch (SQLException e){throw new RuntimeException("Error while updating Player!!",e);}
    }

    public void deletePlayer(int gameID){
        try (Connection conn = dataSource.getConnection()){
            String sql = "DELETE FROM player WHERE game_id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,gameID);
            st.executeUpdate();
        }catch (SQLException e){ throw new RuntimeException("Error while deleting player!!!", e);

        }
    }


    public PlayerDAO getPlayer(int gameID){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, posx, posy FROM player WHERE game_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, gameID);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            PlayerDAO player = new PlayerDAO(rs.getString(1), rs.getInt(2),rs.getInt(3));
            player.setId(player.getId());
            return player;

        } catch (SQLException e) {
            throw new RuntimeException("Error!!!" , e);
        }
    }


}
