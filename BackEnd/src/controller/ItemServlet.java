package controller;

import bo.BOFactory;
import bo.custom.CustomerBO;
import bo.custom.ItemBO;
import dto.CustomerDTO;
import dto.ItemDTO;

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

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    public static DataSource dataSource;

    private final ItemBO itemBO = (ItemBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.ITEM);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                            objectBuilder.add("description",itemDTO.getItemDescription());
                            objectBuilder.add("qtyOnHand",itemDTO.getItemQtyOnHand());
                            objectBuilder.add("unitPrice",itemDTO.getItemUnitPrice());

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
                        objectBuilder.add("description",itemDTO.getItemDescription());
                        objectBuilder.add("qtyOnHand",itemDTO.getItemQtyOnHand());
                        objectBuilder.add("unitPrice",itemDTO.getItemUnitPrice());
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
