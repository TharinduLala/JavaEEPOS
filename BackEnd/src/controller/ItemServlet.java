package controller;

import bo.BOFactory;
import bo.custom.ItemBO;
import dto.ItemDTO;

import javax.annotation.Resource;
import javax.json.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    public static DataSource dataSource;

    private final ItemBO itemBO = (ItemBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.ITEM);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        try {
            String option = req.getParameter("option");
            resp.setContentType("application/json");
            switch (option) {
                case "SEARCH":
                    String itemCode = req.getParameter("itemCode");
                    try {
                        if (itemBO.ifItemExist(itemCode)) {
                            ItemDTO itemDTO = itemBO.searchItem(itemCode);
                            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                            objectBuilder.add("code", itemDTO.getItemCode());
                            objectBuilder.add("description", itemDTO.getItemDescription());
                            objectBuilder.add("qtyOnHand", itemDTO.getItemQtyOnHand());
                            objectBuilder.add("unitPrice", itemDTO.getItemUnitPrice());

                            JsonObjectBuilder response = Json.createObjectBuilder();
                            response.add("status", 201);
                            response.add("message", "Done");
                            response.add("data", objectBuilder.build());
                            writer.print(response.build());
                        } else {
                            JsonObjectBuilder response = Json.createObjectBuilder();
                            response.add("status", 400);
                            response.add("message", "No item from this code");
                            response.add("data", "Error");
                            writer.print(response.build());
                        }
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                case "GET_ALL":
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                    ArrayList<ItemDTO> allItems = itemBO.getAllItems();
                    for (ItemDTO itemDTO : allItems) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("code", itemDTO.getItemCode());
                        objectBuilder.add("description", itemDTO.getItemDescription());
                        objectBuilder.add("qtyOnHand", itemDTO.getItemQtyOnHand());
                        objectBuilder.add("unitPrice", itemDTO.getItemUnitPrice());
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        String code = req.getParameter("code");
        String description = req.getParameter("description");
        String qtyOnHand = req.getParameter("qtyOnHand");
        String unitPrice = req.getParameter("unitPrice");
        resp.setContentType("application/json");

        try {
            if (itemBO.ifItemExist(code)) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "this item already added...");
                response.add("data", "Error");
                writer.print(response.build());
            } else {
                ItemDTO itemDTO = new ItemDTO(code, description, Integer.parseInt(qtyOnHand), new BigDecimal(unitPrice));
                boolean b = itemBO.addNewItem(itemDTO);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                if (b) {
                    objectBuilder.add("status", 200);
                    objectBuilder.add("message", "Successfully Added ");
                    objectBuilder.add("data", "Done");
                } else {
                    objectBuilder.add("status", 400);
                    objectBuilder.add("message", "fail to add item");
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
        String code = readObject.getString("code");
        String description = readObject.getString("description");
        String unitPrice = readObject.getString("unitPrice");
        String qtyOnHand = readObject.getString("qtyOnHand");

        try {
            if (itemBO.ifItemExist(code)) {
                ItemDTO itemDTO = new ItemDTO(code, description, Integer.parseInt(qtyOnHand), new BigDecimal(unitPrice));
                boolean b = itemBO.updateItem(itemDTO);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                if (b) {
                    objectBuilder.add("status", 200);
                    objectBuilder.add("message", "Successfully Updated ");
                    objectBuilder.add("data", "Done");
                } else {
                    objectBuilder.add("status", 400);
                    objectBuilder.add("message", "fail to update item");
                    objectBuilder.add("data", "Error");
                }
                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "No item from this code");
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
        String code = req.getParameter("code");

        try {
            if (itemBO.ifItemExist(code)) {
                boolean b = itemBO.deleteItem(code);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                if (b) {
                    objectBuilder.add("status", 200);
                    objectBuilder.add("message", "Successfully Removed ");
                    objectBuilder.add("data", "Done");
                } else {
                    objectBuilder.add("status", 400);
                    objectBuilder.add("message", "fail to remove item");
                    objectBuilder.add("data", "Error");
                }
                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "No item from this code");
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
