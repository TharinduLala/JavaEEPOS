package controller;

import bo.BOFactory;
import bo.custom.OrderBO;
import dto.OrderDTO;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/order")
public class OrderServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    public static DataSource dataSource;
    private final OrderBO orderBO = (OrderBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.ORDER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        try {
            String option = req.getParameter("option");
            resp.setContentType("application/json");
            switch (option) {
                case "SEARCH":
                    System.out.println("SEaRCh");
                    break;

                case "GET_ALL":
                    System.out.println("Get all from orders");
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                    ArrayList<OrderDTO> allOrders = orderBO.getAllOrders();
                    for (OrderDTO orderDTO : allOrders) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("orderId", orderDTO.getOrderId());
                        objectBuilder.add("orderDate", orderDTO.getOrderDate().toString());
                        objectBuilder.add("customerId", orderDTO.getCustomerId());
                        objectBuilder.add("total", orderDTO.getTotal());
                        objectBuilder.add("discount", orderDTO.getDiscount());
                        objectBuilder.add("netTotal", orderDTO.getNetTotal());
                        arrayBuilder.add(objectBuilder.build());
                    }
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", 201);
                    response.add("message", "Done");
                    response.add("data", arrayBuilder.build());
                    writer.print(response.build());
                    break;

                case "GETOID":
                    System.out.println("get Id");
                    String newOrderId = orderBO.generateNewOrderId();
                    JsonObjectBuilder response1 = Json.createObjectBuilder();
                    response1.add("status", 201);
                    response1.add("message", "Done");
                    response1.add("data", newOrderId);
                    writer.print(response1.build());
                    break;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 400);
            response.add("message", e.getLocalizedMessage());
            response.add("data", "Error");
            writer.print(response.build());
        }
    }

}
