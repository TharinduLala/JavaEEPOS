package dao.custom.impl;

import controller.OrderServlet;
import dao.custom.OrderDetailDAO;
import entity.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Override
    public ArrayList<OrderDetail> getAllById(String orderId) throws SQLException {
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        Connection connection = OrderServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Order Detail` WHERE orderId=?");
        statement.setObject(1, orderId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            orderDetails.add(new OrderDetail(
                    resultSet.getString("orderId"),
                    resultSet.getString("itemCode"),
                    resultSet.getBigDecimal("unitPrice"),
                    resultSet.getInt("qty"),
                    resultSet.getBigDecimal("total")
            ));
        }
        connection.close();
        return orderDetails;
    }

    @Override
    public boolean add(OrderDetail orderDetail) throws SQLException {
        Connection connection = OrderServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO `Order Detail` VALUES(?,?,?,?,?)");
        statement.setObject(1, orderDetail.getOrderId());
        statement.setObject(2, orderDetail.getItemCode());
        statement.setObject(3, orderDetail.getUnitPrice());
        statement.setObject(4, orderDetail.getQty());
        statement.setObject(5, orderDetail.getTotal());
        int i = statement.executeUpdate();
        connection.close();
        return i > 0;
    }

    @Override
    public boolean remove(String s) throws SQLException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public boolean update(OrderDetail orderDetail) throws SQLException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ArrayList<OrderDetail> getAll() throws SQLException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public OrderDetail search(String s) throws SQLException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }
}

