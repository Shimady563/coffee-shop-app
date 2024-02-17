package project.coffeeshop.menu;

import jakarta.servlet.ServletException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDao {
    private final DataSource dataSource;

    public MenuDao() throws ServletException {
        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/db");
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

    public List<MenuItem> findAll() throws ServletException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeQuery("SELECT * FROM public.menu_item");

            return getMenuItems(statement.getResultSet());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    public List<MenuItem> findByUserId(long userId) throws ServletException {
        String query = "select * from public.menu_item a " +
                "join public.user_menu_item b on a.id = b.menu_item_id " +
                "where b.user_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(query)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.executeQuery();

            return getMenuItems(preparedStatement.getResultSet());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    public void saveToFavorites(long userId, int itemId) throws ServletException {
        try (Connection connection = dataSource.getConnection()) {
            if (!isAlreadyAdded(connection, userId, itemId)) {
                try (PreparedStatement preparedStatement = connection
                        .prepareStatement("insert into public.user_menu_item values (?, ?)")) {

                    preparedStatement.setLong(1, userId);
                    preparedStatement.setInt(2, itemId);

                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    public void deleteUserFavorites(long userId, int itemId) throws ServletException {
        String query = "delete from public.user_menu_item " +
                "where user_id = ? and menu_item_id = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection
                .prepareStatement(query)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, itemId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private boolean isAlreadyAdded(Connection connection, long userId, int itemId) throws ServletException {
        String query = "select count(*) from public.user_menu_item " +
                "where user_id = ? and menu_item_id = ?";
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(query)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, itemId);
            preparedStatement.executeQuery();

            ResultSet resultSet = preparedStatement.getResultSet();
            boolean isEmpty = true;
            while (resultSet.next()) {
                int count = resultSet.getInt(1);
                isEmpty = count >  0;
            }

            return isEmpty;
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private List<MenuItem> getMenuItems(ResultSet resultSet) throws ServletException {
        try {
            List<MenuItem> menuItems = new ArrayList<>();
            while (resultSet.next()) {
                MenuItem menuItem = new MenuItem(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("volume"),
                        resultSet.getString("image")
                );
                menuItems.add(menuItem);
            }

            resultSet.close();
            return menuItems;
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}