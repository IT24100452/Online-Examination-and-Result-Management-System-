package OE_RM.servlets;

import OE_RM.services.FileHandler;
import OE_RM.models.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/results")
public class ResultServlet extends HttpServlet {
    private FileHandler fileHandler;

    @Override
    public void init() throws ServletException {
        fileHandler = new FileHandler();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = (String) request.getSession().getAttribute("role");
        String userFilePath = getServletContext().getRealPath("/WEB-INF/users.txt");
        String resultsFilePath = getServletContext().getRealPath("/WEB-INF/results.txt");

        List<FileHandler.ResultEntry> results = fileHandler.readResults(resultsFilePath);
        if ("admin".equals(role)) {
            request.setAttribute("results", results);
        } else {
            Student student = (Student) request.getSession().getAttribute("user");
            List<FileHandler.ResultEntry> studentResults = results.stream()
                    .filter(r -> r.getStudentId().equals(student.getStudentId()))
                    .toList();
            request.setAttribute("studentResults", studentResults);
        }
        request.getRequestDispatcher("results.jsp").forward(request, response);
    }
}

