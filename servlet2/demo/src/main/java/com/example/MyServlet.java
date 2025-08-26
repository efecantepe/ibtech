package com.example;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@WebServlet("/myservlet")
public class MyServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Context initContext = new InitialContext();
            // Lookup the JNDI DataSource configured in Tomcat
            dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/PostgresDB");

            // Initialize DB (only first time)
            initializeDatabase();
        } catch (Exception e) {
            throw new ServletException("Failed to lookup DataSource", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("mainFunction".equals(action)) {
            try {
                String result = queryCarsAndBuildHtml();
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println(result);
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().println("Error: " + e.getMessage());
            }
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/query.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void initializeDatabase() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            String createTable = "CREATE TABLE IF NOT EXISTS car (" +
                    "id SERIAL PRIMARY KEY," +
                    "make TEXT NOT NULL," +
                    "model TEXT NOT NULL," +
                    "year INTEGER NOT NULL)";
            stmt.execute(createTable);

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM car");
            rs.next();
            int count = rs.getInt("count");

            if (count == 0) {
                stmt.execute("INSERT INTO car (make, model, year) VALUES ('Toyota', 'Corolla', 2019)");
                stmt.execute("INSERT INTO car (make, model, year) VALUES ('Honda', 'Civic', 2020)");
                stmt.execute("INSERT INTO car (make, model, year) VALUES ('Ford', 'Mustang', 2018)");
            }
        }
    }

    private String queryCarsAndBuildHtml() throws SQLException {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Car List</title></head><body>");
        html.append("<h2>Car Database</h2>");
        html.append("<table border='1' cellpadding='5' cellspacing='0'>");
        html.append("<tr><th>ID</th><th>Make</th><th>Model</th><th>Year</th></tr>");

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM car")) {

            while (rs.next()) {
                html.append("<tr>");
                html.append("<td>").append(rs.getInt("id")).append("</td>");
                html.append("<td>").append(rs.getString("make")).append("</td>");
                html.append("<td>").append(rs.getString("model")).append("</td>");
                html.append("<td>").append(rs.getInt("year")).append("</td>");
                html.append("</tr>");
            }
        }

        html.append("</table></body></html>");
        return html.toString();
    }
}
