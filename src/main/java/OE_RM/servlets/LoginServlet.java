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
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userFilePath = getServletContext().getRealPath("/WEB-INF/users.txt");

        try {
            User user = fileHandler.readUser(username, userFilePath);
            if (user != null && user.validatePassword(password)) {
                request.getSession().setAttribute("user", user);
                request.getSession().setAttribute("role", user.getRole());
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect("adminDashboard");
                } else if ("student".equals(user.getRole())) {
                    Student student = (Student) user; // Safe cast after role check
                    request.getSession().setAttribute("user", student);
                    response.sendRedirect("studentDashboard");
                } else {
                    response.sendRedirect("index.jsp?error=invalidRole");
                }
            } else {
                response.sendRedirect("index.jsp?error=invalid");
            }
        } catch (IOException e) {
            request.setAttribute("error", "Login failed: Unable to access user data. Please try again later.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
