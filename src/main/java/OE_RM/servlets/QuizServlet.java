package OE_RM.servlets;

import OE_RM.models.Student;
import OE_RM.models.Quiz;
import OE_RM.services.FileHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    private FileHandler fileHandler;

    @Override
    public void init() throws ServletException {
        fileHandler = new FileHandler();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String moduleName = request.getParameter("moduleName");
        String quizFilePath = getServletContext().getRealPath("/WEB-INF/quizzes.txt");
        List<Quiz> quizzes = fileHandler.readQuizzes(quizFilePath);

        if ("attempt".equals(action) && moduleName != null) {
            Quiz quiz = quizzes.stream()
                    .filter(q -> q.getModuleName().equals(moduleName))
                    .findFirst()
                    .orElse(null);
            if (quiz != null) {
                request.setAttribute("quiz", quiz);
                request.getRequestDispatcher("exam.jsp").forward(request, response);
            } else {
                response.sendRedirect("studentDashboard?error=quizNotFound");
            }
        } else if ("edit".equals(action) && moduleName != null) {
            Quiz quiz = quizzes.stream()
                    .filter(q -> q.getModuleName().equals(moduleName))
                    .findFirst()
                    .orElse(null);
            if (quiz != null) {
                request.setAttribute("quiz", quiz);
                request.getRequestDispatcher("exam.jsp").forward(request, response);
            } else {
                response.sendRedirect("adminDashboard?error=quizNotFound");
            }
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String moduleName = request.getParameter("moduleName");
        String role = (String) request.getSession().getAttribute("role");
        String quizFilePath = getServletContext().getRealPath("/WEB-INF/quizzes.txt");
        String resultsFilePath = getServletContext().getRealPath("/WEB-INF/results.txt");
        List<Quiz> quizzes = fileHandler.readQuizzes(quizFilePath);

        if ("submit".equals(action) && moduleName != null && "student".equals(role)) {
            Student student = (Student) request.getSession().getAttribute("user");
            if (student == null) {
                response.sendRedirect("login");
                return;
            }
            Quiz quiz = quizzes.stream()
                    .filter(q -> q.getModuleName().equals(moduleName))
                    .findFirst()
                    .orElse(null);
            if (quiz != null) {
                int correctAnswers = 0;
                for (int i = 0; i < quiz.getTotalQuestions(); i++) {
                    String selected = request.getParameter("answer" + i);
                    if (selected != null) {
                        int answer = Integer.parseInt(selected);
                        if (answer == quiz.getCorrectAnswer(i)) {
                            correctAnswers++;
                        }
                    }
                }
                double percentageScore = (correctAnswers * 100.0) / quiz.getTotalQuestions();
                student.setScore((int) percentageScore);
                fileHandler.updateResult(student, moduleName, null, resultsFilePath);
                request.getSession().setAttribute("submissionMessage", "Quiz submitted successfully");
                response.sendRedirect("studentDashboard");
            } else {
                response.sendRedirect("studentDashboard?error=quizNotFound");
            }
