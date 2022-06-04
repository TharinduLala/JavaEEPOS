package bo.custom;

import bo.SuperBO;
import dto.OrderDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderBO extends SuperBO {

    boolean placeOrder(OrderDTO dto) throws SQLException, ClassNotFoundException;
    String generateNewOrderId() throws SQLException, ClassNotFoundException;
    ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException;

}
