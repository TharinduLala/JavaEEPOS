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
    public boolean add(Item item) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean remove(String code) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Item item) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ArrayList<Item> getAll() throws SQLException, ClassNotFoundException {
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
    public Item search(String code) throws SQLException, ClassNotFoundException {
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
    public boolean ifItemExist(String code) throws SQLException, ClassNotFoundException {
        Connection connection = ItemServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from Item WHERE itemCode=?");
        statement.setObject(1, code);
        ResultSet resultSet = statement.executeQuery();
        boolean next = resultSet.next();
        connection.close();
        return next;
    }
}
