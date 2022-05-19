package controller;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Request from Customer");
        try {
            String option = req.getParameter("option");
            String customerId = req.getParameter("customerId");
            resp.setContentType("application/json");
            Connection connection = dataSource.getConnection();
            PrintWriter writer = resp.getWriter();
            ResultSet resultSet = null;
            switch (option) {
                case "SEARCH":
                    PreparedStatement statement = connection.prepareStatement("select * from Customer WHERE customerId=?");
                    statement.setObject(1,customerId);
                    resultSet = statement.executeQuery();
                   /* JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();

                    while (resultSet.next()) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id", resultSet.getString(1));
                        objectBuilder.add("name", resultSet.getString(2));
                        objectBuilder.add("address", resultSet.getString(3));
                        objectBuilder.add("contact", resultSet.getString(4));
                        arrayBuilder1.add(objectBuilder.build());
                    }


                    JsonObjectBuilder response1 = Json.createObjectBuilder();
                    response1.add("status", 201);
                    response1.add("message", "Done");
                    response1.add("data", arrayBuilder1.build());

                    writer.print(response1.build());*/
                    break;
                case "GET_ALL":
                    resultSet= connection.prepareStatement("select * from Customer").executeQuery();
                    break;
            }
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            JsonObjectBuilder response = Json.createObjectBuilder();
            if (resultSet != null) {
                while (resultSet.next()) {
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("id", resultSet.getString(1));
                    objectBuilder.add("name", resultSet.getString(2));
                    objectBuilder.add("address", resultSet.getString(3));
                    objectBuilder.add("contact", resultSet.getString(4));
                    arrayBuilder.add(objectBuilder.build());
                }
                response.add("status", 201);
                response.add("message", "Done");
                response.add("data", arrayBuilder.build());
            }else {
                response.add("status", 400);
                response.add("message", "Error");
                response.add("data", "No result found");
            }

            writer.print(response.build());
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
