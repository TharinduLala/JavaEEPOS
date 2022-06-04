package dao.custom.impl;

import controller.OrderServlet;
import dao.custom.OrderDAO;
import entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean add(Order order) throws SQLException {
        Connection connection = OrderServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO `Order` VALUES(?,?,?,?,?,?)");
        statement.setObject(1, order.getOrderId());
        statement.setObject(2, order.getOrderDate());
        statement.setObject(3, order.getCustomerId());
        statement.setObject(4, order.getTotal());
        statement.setObject(5, order.getDiscount());
        statement.setObject(6, order.getNetTotal());
        int i = statement.executeUpdate();
        connection.close();
        return i > 0;
    }

    @Override
    public boolean remove(String orderId) throws SQLException {
        /*Connection connection = ItemServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM `Order` WHERE orderId=?");
        statement.setObject(1, orderId);
        int i = statement.executeUpdate();
        connection.close();
        return i > 0;*/
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public boolean update(Order order) throws SQLException {
        /*Connection connection = ItemServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE `Order` SET orderDate=?,customerId=?,total=?,`discount(%)`=?,netTotal=? WHERE orderId=?");
        statement.setObject(1, order.getOrderDate());
        statement.setObject(2, order.getCustomerId());
        statement.setObject(3, order.getTotal());
        statement.setObject(4, order.getDiscount());
        statement.setObject(5, order.getNetTotal());
        statement.setObject(6, order.getOrderId());
        int i = statement.executeUpdate();
        connection.close();
        return i > 0;*/
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ArrayList<Order> getAll() throws SQLException {
        ArrayList<Order> allOrders = new ArrayList<>();
        Connection connection = OrderServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Order`");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            allOrders.add(new Order(
                    resultSet.getString("orderId"),
                    LocalDate.parse(resultSet.getString("orderDate")),
                    resultSet.getString("customerId"),
                    resultSet.getBigDecimal("total"),
                    resultSet.getBigDecimal("`discount(%)`"),
                    resultSet.getBigDecimal("netTotal")
            ));
        }
        connection.close();
        return allOrders;
    }

    @Override
    public Order search(String orderId) throws SQLException {
        Connection connection = OrderServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from `Order` WHERE orderId=?");
        statement.setObject(1, orderId);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        Order order = new Order(
                resultSet.getString("orderId"),
                LocalDate.parse(resultSet.getString("orderDate")),
                resultSet.getString("customerId"),
                resultSet.getBigDecimal("total"),
                resultSet.getBigDecimal("`discount(%)`"),
                resultSet.getBigDecimal("netTotal")
        );
        connection.close();
        return order;
    }

    @Override
    public boolean ifOrderExist(String orderId) throws SQLException {
        Connection connection = OrderServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from `Order` WHERE orderId=?");
        statement.setObject(1, orderId);
        ResultSet resultSet = statement.executeQuery();
        boolean next = resultSet.next();
        connection.close();
        return next;
    }

    @Override
    public String generateNewOrderId() throws SQLException {
        Connection connection = OrderServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1;");
        ResultSet resultSet = statement.executeQuery();
        boolean b = resultSet.next();
        String orderId=null;
        if (b){
            orderId = resultSet.getString("orderId");
        }
        connection.close();
        return b? String.format("OD%03d", (Integer.parseInt(orderId.replace("OD", "")) + 1)) : "OD001";

    }
}
