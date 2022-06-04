package bo.custom.impl;

import bo.custom.OrderBO;
import controller.OrderServlet;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import entity.Item;
import entity.Order;
import entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderBOImpl implements OrderBO {
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public boolean placeOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        Connection connection = null;

        connection = OrderServlet.dataSource.getConnection();

        if (orderDAO.ifOrderExist(orderDTO.getOrderId())) {
            return false;
        }

        connection.setAutoCommit(false);
        Order order = new Order(orderDTO.getOrderId(),
                orderDTO.getOrderDate(),
                orderDTO.getCustomerId(),
                orderDTO.getTotal(),
                orderDTO.getDiscount(),
                orderDTO.getNetTotal());

        if (!(orderDAO.add(order))) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        for (OrderDetailDTO detail : orderDTO.getOrderDetails()) {
            OrderDetail orderDetail = new OrderDetail(
                    detail.getOrderId(),
                    detail.getItemCode(),
                    detail.getUnitPrice(),
                    detail.getQty(),
                    detail.getTotal());

            if (!(orderDetailDAO.add(orderDetail))) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            Item temp = itemDAO.search(detail.getItemCode());
            int newQty = temp.getQtyOnHand() - detail.getQty();
            boolean update = itemDAO.updateQty(temp.getCode(), newQty);
            if (!update) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }

        connection.commit();
        connection.setAutoCommit(true);
        return true;
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewOrderId();
    }

    @Override
    public ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        ArrayList<Order> tempAllOrders = orderDAO.getAll();
        ArrayList<OrderDTO> allOrders = new ArrayList<>();
        for (Order order : tempAllOrders) {
            ArrayList<OrderDetail> tempAllDetails = orderDetailDAO.getAllById(order.getOrderId());
            ArrayList<OrderDetailDTO> allDetails = new ArrayList<>();
            for (OrderDetail detail : tempAllDetails) {
                allDetails.add(new OrderDetailDTO(
                        detail.getOrderId(),
                        detail.getItemCode(),
                        detail.getUnitPrice(),
                        detail.getQty(),
                        detail.getTotal()
                ));
            }
            allOrders.add(new OrderDTO(
                    order.getOrderId(),
                    order.getOrderDate(),
                    order.getCustomerId(),
                    order.getTotal(),
                    order.getDiscount(),
                    order.getNetTotal(),
                    allDetails
            ));
        }
        return allOrders;
    }
}
