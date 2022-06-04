package dao.custom;

import dao.CrudDAO;
import entity.Customer;
import entity.Item;

import java.sql.SQLException;

public interface ItemDAO extends CrudDAO<Item,String> {

    boolean ifItemExist(String code) throws SQLException, ClassNotFoundException;
    boolean updateQty (String code,int qty) throws SQLException, ClassNotFoundException;

}
