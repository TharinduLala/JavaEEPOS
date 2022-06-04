package dao.custom.impl;

import controller.ItemServlet;
import dao.custom.ItemDAO;
import entity.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public boolean add(Item item) throws SQLException {
        Connection connection = ItemServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Item VALUES(?,?,?,?)");
        statement.setObject(1, item.getCode());
        statement.setObject(2, item.getDescription());
        statement.setObject(3, item.getQtyOnHand());
        statement.setObject(4, item.getUnitPrice());
        int i = statement.executeUpdate();
        connection.close();
        return i > 0;
    }

    @Override
    public boolean remove(String code) throws SQLException {
        Connection connection = ItemServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM Item WHERE itemCode=?");
        statement.setObject(1, code);
        int i = statement.executeUpdate();
        connection.close();
        return i > 0;
    }

    @Override
    public boolean update(Item item) throws SQLException {
        Connection connection = ItemServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE Item SET description=?,qtyOnHand=?,unitPrice=? WHERE itemCode=?");
        statement.setObject(4, item.getCode());
        statement.setObject(1, item.getDescription());
        statement.setObject(2, item.getQtyOnHand());
        statement.setObject(3, item.getUnitPrice());
        int i = statement.executeUpdate();
        connection.close();
        return i > 0;
    }

    @Override
    public ArrayList<Item> getAll() throws SQLException {
        ArrayList<Item> allItems = new ArrayList<>();
        Connection connection = ItemServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Item");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            allItems.add(new Item(
                    resultSet.getString("itemCode"),
                    resultSet.getString("description"),
                    resultSet.getInt("qtyOnHand"),
                    resultSet.getBigDecimal("unitPrice")
            ));
        }
        connection.close();
        return allItems;
    }

    @Override
    public Item search(String code) throws SQLException {
        Connection connection = ItemServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from Item WHERE itemCode=?");
        statement.setObject(1, code);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        Item item = new Item(
                resultSet.getString("itemCode"),
                resultSet.getString("description"),
                resultSet.getInt("qtyOnHand"),
                resultSet.getBigDecimal("unitPrice")
        );
        connection.close();
        return item;
    }

    @Override
    public boolean ifItemExist(String code) throws SQLException {
        Connection connection = ItemServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from Item WHERE itemCode=?");
        statement.setObject(1, code);
        ResultSet resultSet = statement.executeQuery();
        boolean next = resultSet.next();
        connection.close();
        return next;
    }

    @Override
    public boolean updateQty(String code, int qty) throws SQLException, ClassNotFoundException {
        Connection connection = ItemServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE Item SET qtyOnHand=? WHERE itemCode=?");
        statement.setObject(1, qty);
        statement.setObject(2, code);
        int i = statement.executeUpdate();
        connection.close();
        return i > 0;
    }
}
