package OE_RM.servlets;

import OE_RM.models.User;
import OE_RM.models.Student;
import OE_RM.services.FileHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private FileHandler fileHandler;

    @Override
    public void init() throws ServletException {
        fileHandler = new FileHandler();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
