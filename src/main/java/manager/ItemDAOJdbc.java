package manager;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class ItemDAOJdbc {

    private DataSource dataSource;

    public ItemDAOJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertItem(ItemDAO item){
        try(Connection conn = dataSource.getConnection()){
            String sql = "INSERT INTO item (type, posx, posy, game_id) values (?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, item.getType());
            st.setInt(2, item.getPosX());
            st.setInt(3,item.getPosY());
            st.setInt(3,item.getGameID());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            item.setId(rs.getInt(1));
        }catch (SQLException e){ throw new RuntimeException("Error while adding new Item!!!", e);

        }
    }

    public void updateItem(ItemDAO item){
        try (Connection conn = dataSource.getConnection()){
            String sql = "UPDATE item SET type = ?, posx =?, posy =?, game_id =? WHERE item_id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, item.getType());
            st.setInt(2, item.getPosX());
            st.setInt(3, item.getPosY());
            st.setInt(4,item.getGameID());
            st.setInt(5,item.getId());
            st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error while updating item!!!", e);
        }
    }

    public void deleteItems(int gameID){
        try (Connection conn = dataSource.getConnection()){
            String sql = "DELETE FROM item WHERE game_id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,gameID);
            st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error while deleting item!!!",e);
        }
    }

    public ArrayList<ItemDAO> getAllItems(int gameID){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT type, posx, posy FROM item WHERE game_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, gameID);
            ResultSet rs = st.executeQuery();
            ArrayList<ItemDAO> itemDAOs= new ArrayList<>();
            while (rs.next()) {
                ItemDAO itemDAO = new ItemDAO(rs.getNString("type"), rs.getInt(2),rs.getInt(3) , gameID);
                itemDAO.setId(itemDAO.getId());
                itemDAOs.add(itemDAO);
            }
            return itemDAOs;

        } catch (SQLException e) {
            throw new RuntimeException("Error!!!" , e);
        }
    }
}
