package controller;

import bo.BOFactory;
import bo.custom.CustomerBO;
import dto.CustomerDTO;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    public static DataSource dataSource;
    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.CUSTOMER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            String option = req.getParameter("option");
            resp.setContentType("application/json");
            switch (option) {
                case "SEARCH":
                    String customerId = req.getParameter("customerId");
                    try {
                        if (customerBO.ifCustomerExist(customerId)) {
                            CustomerDTO customerDTO = customerBO.searchCustomer(customerId);
                            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                            objectBuilder.add("id", customerDTO.getCustomerId());
                            objectBuilder.add("name", customerDTO.getCustomerName());
                            objectBuilder.add("address", customerDTO.getCustomerAddress());
                            objectBuilder.add("contact", customerDTO.getCustomerContactNo());

                            JsonObjectBuilder response = Json.createObjectBuilder();
                            response.add("status", 201);
                            response.add("message", "Done");
                            response.add("data", objectBuilder.build());
                            writer.print(response.build());
                        } else {
                            JsonObjectBuilder response = Json.createObjectBuilder();
                            response.add("status", 400);
                            response.add("message", "No customer from this id");
                            response.add("data", "Error");
                            writer.print(response.build());
                        }
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                case "GET_ALL":
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                    ArrayList<CustomerDTO> allCustomers = customerBO.getAllCustomers();
                    for (CustomerDTO customerDTO : allCustomers) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id", customerDTO.getCustomerId());
                        objectBuilder.add("name", customerDTO.getCustomerName());
                        objectBuilder.add("address", customerDTO.getCustomerAddress());
                        objectBuilder.add("contact", customerDTO.getCustomerContactNo());
                        arrayBuilder.add(objectBuilder.build());
                    }

                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", 201);
                    response.add("message", "Done");
                    response.add("data", arrayBuilder.build());
                    writer.print(response.build());
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
