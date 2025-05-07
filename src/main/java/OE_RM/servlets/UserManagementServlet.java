package OE_RM.servlets;

import OE_RM.models.User;
import OE_RM.models.Student;
import OE_RM.services.FileHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/userManagement")
public class UserManagementServlet extends HttpServlet {
    private FileHandler fileHandler;

    @Override
    public void init() throws ServletException {
        fileHandler = new FileHandler();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = (String) request.getSession().getAttribute("role");
        if (!"admin".equals(role)) {
            response.sendRedirect("login");
            return;
        }

        String userFilePath = getServletContext().getRealPath("/WEB-INF/users.txt");
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    User user = parts[2].equals("student") && parts.length >= 4
                            ? new Student(parts[0], parts[1], parts[3])
                            : new User(parts[0], parts[1], parts[2]);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            request.setAttribute("error", "Error reading users: " + e.getMessage());
        }
        request.setAttribute("users", users);
        request.getRequestDispatcher("userManagement.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = (String) request.getSession().getAttribute("role");
        if (!"admin".equals(role)) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String userFilePath = getServletContext().getRealPath("/WEB-INF/users.txt");

        try {
            if ("add".equals(action)) { //  "addUser"
                String password = request.getParameter("password");
                String selectedRole = request.getParameter("role");
                String name = request.getParameter("name");

                if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty() || selectedRole == null) {
                    request.setAttribute("error", "Username, password, and role are required to add a user.");
                    doGet(request, response);
                    return;
                }

                