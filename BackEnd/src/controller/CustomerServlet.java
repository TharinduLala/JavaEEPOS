package controller;

import bo.BOFactory;
import bo.custom.CustomerBO;
import dto.CustomerDTO;

import javax.annotation.Resource;
import javax.json.*;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        String option = req.getParameter("option");
        resp.setContentType("application/json");
        try {
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
                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("status", 400);
                        response.add("message", e.getLocalizedMessage());
                        response.add("data", "Error");
                        writer.print(response.build());
                        resp.setStatus(HttpServletResponse.SC_OK);
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
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        String customerId = req.getParameter("customerId");
        String customerName = req.getParameter("customerName");
        String customerAddress = req.getParameter("customerAddress");
        String customerContactNo = req.getParameter("customerContactNo");
        resp.setContentType("application/json");

        try {
            if (customerBO.ifCustomerExist(customerId)) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "this id already added...");
                response.add("data", "Error");
                writer.print(response.build());
            } else {
                CustomerDTO customerDTO = new CustomerDTO(customerId, customerName, customerAddress, customerContactNo);
                boolean b = customerBO.addNewCustomer(customerDTO);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                if (b) {
                    objectBuilder.add("status", 200);
                    objectBuilder.add("message", "Successfully Added ");
                    objectBuilder.add("data", "Done");
                } else {
                    objectBuilder.add("status", 400);
                    objectBuilder.add("message", "fail to add customer");
                    objectBuilder.add("data", "Error");
                }
                writer.print(objectBuilder.build());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 400);
            objectBuilder.add("message", e.getLocalizedMessage());
            objectBuilder.add("data", "Error");
            writer.print(objectBuilder.build());
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject readObject = reader.readObject();
        String id = readObject.getString("id");
        String name = readObject.getString("name");
        String address = readObject.getString("address");
        String contactNo = readObject.getString("contactNo");

        try {
            if (customerBO.ifCustomerExist(id)) {
                CustomerDTO customerDTO = new CustomerDTO(id, name, address, contactNo);
                boolean b = customerBO.updateCustomer(customerDTO);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                if (b) {
                    objectBuilder.add("status", 200);
                    objectBuilder.add("message", "Successfully Updated ");
                    objectBuilder.add("data", "Done");
                } else {
                    objectBuilder.add("status", 400);
                    objectBuilder.add("message", "fail to update customer");
                    objectBuilder.add("data", "Error");
                }
                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "No customer from this id");
                response.add("data", "Error");
                writer.print(response.build());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 400);
            objectBuilder.add("message", e.getLocalizedMessage());
            objectBuilder.add("data", "Error");
            writer.print(objectBuilder.build());
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        String customerId = req.getParameter("customerId");

        try {
            if (customerBO.ifCustomerExist(customerId)) {
                boolean b = customerBO.deleteCustomer(customerId);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                if (b) {
                    objectBuilder.add("status", 200);
                    objectBuilder.add("message", "Successfully Removed ");
                    objectBuilder.add("data", "Done");
                } else {
                    objectBuilder.add("status", 400);
                    objectBuilder.add("message", "fail to remove customer");
                    objectBuilder.add("data", "Error");
                }
                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "No customer from this id");
                response.add("data", "Error");
                writer.print(response.build());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 400);
            objectBuilder.add("message", e.getLocalizedMessage());
            objectBuilder.add("data", "Error");
            writer.print(objectBuilder.build());
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
