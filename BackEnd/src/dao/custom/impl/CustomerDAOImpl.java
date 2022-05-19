package dao.custom.impl;

import controller.CustomerServlet;
import dao.custom.CustomerDAO;
import entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean add(Customer customer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean remove(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ArrayList<Customer> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> allCustomers=new ArrayList<>();
        Connection connection = CustomerServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Customer");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            allCustomers.add(new Customer(
                    resultSet.getString("customerId"),
                    resultSet.getString("customerName"),
                    resultSet.getString("customerAddress"),
                    resultSet.getString("contactNo")
            ));
        }
        connection.close();
        return allCustomers;
    }

    @Override
    public Customer search(String id) throws SQLException, ClassNotFoundException {
        Connection connection = CustomerServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from Customer WHERE customerId=?");
        statement.setObject(1, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        Customer customer = new Customer(
                resultSet.getString("customerId"),
                resultSet.getString("customerName"),
                resultSet.getString("customerAddress"),
                resultSet.getString("contactNo")
        );
        connection.close();
        return customer;
    }

    @Override
    public boolean ifCustomerExist(String id) throws SQLException, ClassNotFoundException {
        Connection connection = CustomerServlet.dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from Customer WHERE customerId=?");
        statement.setObject(1, id);
        ResultSet resultSet = statement.executeQuery();
        boolean next = resultSet.next();
        connection.close();
        return next;
    }
}
