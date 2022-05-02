package manager;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class AvailableItemDAOJdbc {

    private DataSource dataSource;

    public AvailableItemDAOJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertAvailableItem(AvailableItemDAO availableItem){
        try(Connection conn = dataSource.getConnection()){
            String sql = "INSERT INTO available_item (type, posx, posy, game_id) values (?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, availableItem.getType());
            st.setInt(2, availableItem.getPosX());
            st.setInt(3,availableItem.getPosY());
            st.setInt(4,availableItem.getGameID());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            availableItem.setId(rs.getInt(1));
        }catch (SQLException e){ throw new RuntimeException("Error while adding new available Item!!", e);

        }
    }

    public void updateAvailableItem(AvailableItemDAO availableItem){
        try (Connection conn = dataSource.getConnection()){
            String sql = "UPDATE available_item SET type = ?, posx =?, posy = ?, game_id = ? WHERE  available_item_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, availableItem.getType());
            st.setInt(2, availableItem.getPosX());
            st.setInt(3, availableItem.getPosY());
            st.setInt(4, availableItem.getGameID());
            st.setInt(5,availableItem.getId());
            st.executeUpdate();
        }catch (SQLException e){throw new RuntimeException("Error while updating available item!!!", e);}
    }

    public void deleteAvailableItems(int gameID){
        try (Connection conn = dataSource.getConnection()){
            String sql = "DELETE FROM available_item WHERE game_id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,gameID);
            st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error while deleting available item!!!", e);
        }
    }


    public ArrayList<AvailableItemDAO> getAllAvailableItems(int gameID){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT type, posx, posy FROM available_item WHERE game_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, gameID);
            ResultSet rs = st.executeQuery();
            ArrayList<AvailableItemDAO> availableItemDAOs= new ArrayList<>();
            while (rs.next()) {
                AvailableItemDAO availableItem = new AvailableItemDAO(rs.getNString("type"), rs.getInt(2),rs.getInt(3) , gameID);
                availableItem.setId(availableItem.getId());
                availableItemDAOs.add(availableItem);
            }
            return availableItemDAOs;

        } catch (SQLException e) {
            throw new RuntimeException("Error!!!" , e);
        }
    }


}
