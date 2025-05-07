package OE_RM.servlets;

import OE_RM.models.Quiz;
import OE_RM.services.FileHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/quizList")
public class QuizListServlet extends HttpServlet {
    private FileHandler fileHandler;

    @Override
    public void init() throws ServletException {
        fileHandler = new FileHandler();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = (String) request.getSession().getAttribute("role");
        if (!"admin".equals(role)) {
            String quizFilePath = getServletContext().getRealPath("/WEB-INF/quizzes.txt");
            List<Quiz> quizzes = fileHandler.readQuizzes(quizFilePath);
            request.setAttribute("quizzes", quizzes);
            request.getRequestDispatcher("quizList.jsp").forward(request, response);
        } else {
            response.sendRedirect("adminDashboard");
        }
    }
}
